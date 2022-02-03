package fr.uha.jacquey.hospitalbed.ui;

import android.app.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class FragmentHelper {

    static public void changeTitle(Fragment fragment, String title) {
        AppCompatActivity compatActivity = (AppCompatActivity) fragment.getActivity();
        if (compatActivity == null) return;
        ActionBar actionBar = compatActivity.getSupportActionBar();
        if (actionBar == null) return;
        actionBar.setTitle(title);
    }

    static public void invalidateOptionsMenu(Fragment fragment) {
        Activity activity = fragment.getActivity();
        if (activity == null) return;
        activity.invalidateOptionsMenu();
    }

}
