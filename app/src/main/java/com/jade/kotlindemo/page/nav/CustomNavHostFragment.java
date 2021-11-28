package com.jade.kotlindemo.page.nav;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

public class CustomNavHostFragment extends NavHostFragment {

    @Override
    protected void onCreateNavController(@NonNull NavController navController) {
        super.onCreateNavController(navController);
        navController.getNavigatorProvider().addNavigator(new KeepStateNavigator(getContext(), getChildFragmentManager(), getId()));
    }
}
