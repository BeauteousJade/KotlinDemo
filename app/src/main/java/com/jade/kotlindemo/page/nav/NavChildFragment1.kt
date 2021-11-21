package com.jade.kotlindemo.page.nav

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.jade.kotlindemo.R
import kotlinx.android.synthetic.main.fragment_nav_child1.*

class NavChildFragment1 : NavBaseFragment() {

    private lateinit var mViewGroup: ViewGroup

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewGroup = view.findViewById(R.id.viewGroup)
        addViewWithClickListener("跳到Fragment2") {
            val action =
                NavChildFragment1Directions.actionNavChildFragment1ToNavChildFragment2()
            findNavController().navigate(action)
        }
        addViewWithClickListener("跳到Activity2") {
            val action = NavChildFragment1Directions.actionNavSecondActivity()
            findNavController().navigate(action)
        }
        addViewWithClickListener("跳到Activity3") {
            val action = NavChildFragment1Directions.actionNavThirdActivity()
            findNavController().navigate(action.actionId, bundleOf("userId" to "pby123"))
        }
        addViewWithClickListener("跳到CommonFragment（嵌套视图）") {
            val action = NavChildFragment1Directions.actionNavChildFragment1ToNavigation();
            findNavController().navigate(action.actionId)
        }
        addViewWithClickListener("使用全局action跳到CommonFragment（嵌套视图）") {
            findNavController().navigate(R.id.global_action_navChildFragment1_to_navigation)
        }
        addViewWithClickListener(
            "使用自动创建的方式，跳转到CommonFragment",
            Navigation.createNavigateOnClickListener(R.id.common_nav_graph)
        )
        addViewWithClickListener("跳到循环跳转页面") {
            findNavController().navigate(R.id.global_action_navChildFragment1_to_loop_graph)
        }
    }

    private fun addViewWithClickListener(text: String, onClickListener: View.OnClickListener) {
        val button = Button(context).apply {
            this.text = text
            setOnClickListener(onClickListener)
        }
        mViewGroup.addView(
            button,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_nav_child1
    }
}