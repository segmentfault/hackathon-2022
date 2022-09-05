package com.example.notepad.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.util.Log;

import com.example.notepad.R;
import com.example.notepad.databinding.ActivityHomeBinding;
import com.example.notepad.viewModel.NoteViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    private NavController mNavController;
    private ActivityHomeBinding mActivityHomeBinding;
    private NoteViewModel mNoteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityHomeBinding = DataBindingUtil.setContentView(this,R.layout.activity_home);
        mNoteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);
    }

    @Override
    public void onBackPressed() {
        if (mNoteViewModel.getCheckBoxAllAppear().getValue()){
            mNoteViewModel.getCheckBoxAllAppear().setValue(false);
        }else {
            super.onBackPressed();
        }
        Log.d(TAG, "onBackPressed....");
        Log.d(TAG, "Boolean值为->"+mNoteViewModel.getCheckBoxAllAppear().getValue());
    }
}