package com.live.cunix;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.view.MenuItem;

import com.live.cunix.common.ActivityBase;
import com.live.cunix.dialogs.CommentActionDialog;
import com.live.cunix.dialogs.CommentDeleteDialog;
import com.live.cunix.dialogs.MixedCommentActionDialog;
import com.live.cunix.dialogs.MyCommentActionDialog;
import com.live.cunix.dialogs.MyPhotoActionDialog;
import com.live.cunix.dialogs.PhotoActionDialog;
import com.live.cunix.dialogs.PhotoDeleteDialog;
import com.live.cunix.dialogs.PhotoReportDialog;


public class ViewImageActivity extends ActivityBase implements CommentDeleteDialog.AlertPositiveListener, CommentActionDialog.AlertPositiveListener, MyCommentActionDialog.AlertPositiveListener, PhotoDeleteDialog.AlertPositiveListener, PhotoReportDialog.AlertPositiveListener, MyPhotoActionDialog.AlertPositiveListener, PhotoActionDialog.AlertPositiveListener, MixedCommentActionDialog.AlertPositiveListener {

    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_image);

        if (savedInstanceState != null) {

            fragment = getSupportFragmentManager().getFragment(savedInstanceState, "currentFragment");

        } else {

            fragment = new ViewImageFragment();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container_body, fragment).commit();
    }

    @Override
    protected void onSaveInstanceState (Bundle outState) {

        super.onSaveInstanceState(outState);

        getSupportFragmentManager().putFragment(outState, "currentFragment", fragment);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        fragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {

        ViewImageFragment p = (ViewImageFragment) fragment;
        p.hideEmojiKeyboard();

        super.onPause();
    }

    @Override
    public void onPhotoDelete(int position) {

        ViewImageFragment p = (ViewImageFragment) fragment;
        p.onPhotoDelete(position);
    }

    @Override
    public void onPhotoReport(int position, int reasonId) {

        ViewImageFragment p = (ViewImageFragment) fragment;
        p.onPhotoReport(position, reasonId);
    }

    @Override
    public void onPhotoRemoveDialog(int position) {

        ViewImageFragment p = (ViewImageFragment) fragment;
        p.remove(position);
    }

    @Override
    public void onPhotoReportDialog(int position) {

        ViewImageFragment p = (ViewImageFragment) fragment;
        p.report(position);
    }

    @Override
    public void onCommentRemove(int position) {

        ViewImageFragment p = (ViewImageFragment) fragment;
        p.onCommentRemove(position);
    }

    @Override
    public void onCommentDelete(int position) {

        ViewImageFragment p = (ViewImageFragment) fragment;
        p.onCommentDelete(position);
    }

    @Override
    public void onCommentReply(int position) {

        ViewImageFragment p = (ViewImageFragment) fragment;
        p.onCommentReply(position);
    }

    @Override
    public void onBackPressed(){

        finish();
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
}
