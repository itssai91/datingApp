package com.live.cunix;


import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.live.cunix.common.ActivityBase;
import com.live.cunix.dialogs.MyPhotoActionDialog;
import com.live.cunix.dialogs.PhotoActionDialog;
import com.live.cunix.dialogs.PhotoDeleteDialog;
import com.live.cunix.dialogs.PhotoReportDialog;

public class GalleryActivity extends ActivityBase implements PhotoDeleteDialog.AlertPositiveListener, PhotoReportDialog.AlertPositiveListener, MyPhotoActionDialog.AlertPositiveListener, PhotoActionDialog.AlertPositiveListener {

    Toolbar mToolbar;

    FloatingActionButton mFabButton;

    Fragment fragment;
    Boolean restore = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_gallery);

        mToolbar = findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // Floating action button

        mFabButton = findViewById(R.id.fabButton);
        mFabButton.hide();

        if (savedInstanceState != null) {

            fragment = getSupportFragmentManager().getFragment(savedInstanceState, "currentFragment");

            restore = savedInstanceState.getBoolean("restore");

        } else {

            fragment = new GalleryFragment();

            restore = false;
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container_body, fragment)
                .commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

        outState.putBoolean("restore", true);
        getSupportFragmentManager().putFragment(outState, "currentFragment", fragment);
    }

    @Override
    public void onPhotoDelete(int position) {

        GalleryFragment p = (GalleryFragment) fragment;
        p.onPhotoDelete(position);
    }

    @Override
    public void onPhotoReport(int position, int reasonId) {

        GalleryFragment p = (GalleryFragment) fragment;
        p.onPhotoReport(position, reasonId);
    }

    @Override
    public void onPhotoRemoveDialog(int position) {

        GalleryFragment p = (GalleryFragment) fragment;
        p.onPhotoRemove(position);
    }

    @Override
    public void onPhotoReportDialog(int position) {

        GalleryFragment p = (GalleryFragment) fragment;
        p.report(position);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {

            case android.R.id.home: {

                finish();

                return true;
            }

            default: {

                return super.onOptionsItemSelected(item);
            }
        }
    }

    @Override
    public void onBackPressed() {
        // your code.

        finish();
    }
}