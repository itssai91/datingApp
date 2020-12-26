package com.live.cunix;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.volley.toolbox.ImageLoader;
import com.balysv.materialripple.MaterialRippleLayout;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.live.cunix.app.App;
import com.live.cunix.constants.Constants;


public class MenuFragment extends Fragment implements Constants {

    ImageLoader imageLoader;

    CircularImageView mProfilePhoto, mProfileIcon;
    ImageView mProfileCover, mProfileOnlineIcon, mFriendsIcon, mMatchesIcon, mGuestsIcon;
    private TextView mProfileFullname, mNavProfileSubhead;

    private MaterialRippleLayout mNavProfile, mNavGallery, mNavFriends, mNavMatches, mNavGuests, mNavLikes, mNavLiked, mNavUpgrades, mNavSettings;

    public MenuFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        imageLoader = App.getInstance().getImageLoader();

        setHasOptionsMenu(true);

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_menu, container, false);

        getActivity().setTitle(R.string.nav_menu);

        mNavProfile = rootView.findViewById(R.id.nav_profile);
        mNavProfileSubhead = rootView.findViewById(R.id.nav_profile_subhead);

        mNavGallery = rootView.findViewById(R.id.nav_gallery);

        mNavFriends = rootView.findViewById(R.id.nav_friends);
        mNavMatches = rootView.findViewById(R.id.nav_matches);
        mNavGuests = rootView.findViewById(R.id.nav_guests);
        mNavLikes = rootView.findViewById(R.id.nav_likes);
        mNavLiked = rootView.findViewById(R.id.nav_liked);
        mNavUpgrades = rootView.findViewById(R.id.nav_upgrades);
        mNavSettings = rootView.findViewById(R.id.nav_settings);

        // Counters

        mFriendsIcon = rootView.findViewById(R.id.friendsIcon);
        mMatchesIcon = rootView.findViewById(R.id.matchesIcon);
        mGuestsIcon = rootView.findViewById(R.id.guestsIcon);

        //

        mProfilePhoto = rootView.findViewById(R.id.profilePhoto);
        mProfileIcon = rootView.findViewById(R.id.profileIcon);

        mProfileFullname = rootView.findViewById(R.id.profileFullname);

        mProfileOnlineIcon = rootView.findViewById(R.id.profileOnlineIcon);
        mProfileCover = rootView.findViewById(R.id.profileCover);

        mNavProfile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent i = new Intent(getActivity(), ProfileActivity.class);
                i.putExtra("profileId", App.getInstance().getId());
                getActivity().startActivity(i);
            }
        });

        mNavGallery.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent i = new Intent(getActivity(), GalleryActivity.class);
                i.putExtra("profileId", App.getInstance().getId());
                getActivity().startActivity(i);
            }
        });

        mNavFriends.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent i = new Intent(getActivity(), FriendsActivity.class);
                i.putExtra("profileId", App.getInstance().getId());
                startActivity(i);
            }
        });

        mNavMatches.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent i = new Intent(getActivity(), MatchesActivity.class);
                i.putExtra("profileId", App.getInstance().getId());
                startActivity(i);
            }
        });

        mNavGuests.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent i = new Intent(getActivity(), GuestsActivity.class);
                startActivity(i);
            }
        });

        mNavLikes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent i = new Intent(getActivity(), LikesActivity.class);
                i.putExtra("profileId", App.getInstance().getId());
                startActivity(i);
            }
        });

        mNavLiked.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent i = new Intent(getActivity(), LikedActivity.class);
                i.putExtra("itemId", App.getInstance().getId());
                startActivity(i);
            }
        });

        mNavUpgrades.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent i = new Intent(getActivity(), UpgradesActivity.class);
                startActivity(i);
            }
        });

        mNavSettings.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent i = new Intent(getActivity(), SettingsActivity.class);
                startActivity(i);
            }
        });

        updateView();

        // Inflate the layout for this fragment
        return rootView;
    }

    public void updateView() {

        // Counters

        mFriendsIcon.setVisibility(View.GONE);
        mMatchesIcon.setVisibility(View.GONE);
        mGuestsIcon.setVisibility(View.GONE);

        if (App.getInstance().getNewFriendsCount() != 0) {

            mFriendsIcon.setVisibility(View.VISIBLE);
        }

        if (App.getInstance().getNewMatchesCount() != 0) {

            mMatchesIcon.setVisibility(View.VISIBLE);
        }

        if (App.getInstance().getGuestsCount() != 0) {

            mGuestsIcon.setVisibility(View.VISIBLE);
        }

        // Cover

        if (App.getInstance().getCoverUrl() != null && App.getInstance().getCoverUrl().length() > 0) {

            imageLoader.get(App.getInstance().getCoverUrl(), ImageLoader.getImageListener(mProfileCover, R.drawable.profile_default_cover, R.drawable.profile_default_cover));

        } else {

            mProfileCover.setImageResource(R.drawable.profile_default_cover);
        }

        // Photo

        if (App.getInstance().getPhotoUrl() != null && App.getInstance().getPhotoUrl().length() > 0) {

            imageLoader.get(App.getInstance().getPhotoUrl(), ImageLoader.getImageListener(mProfilePhoto, R.drawable.profile_default_photo, R.drawable.profile_default_photo));

        } else {

            mProfilePhoto.setImageResource(R.drawable.profile_default_photo);
        }

        // Verified

        if (App.getInstance().getVerify() == 1) {

            mProfileIcon.setVisibility(View.VISIBLE);

        } else {

            mProfileIcon.setVisibility(View.GONE);
        }

        // Fullname

        mProfileFullname.setText(App.getInstance().getFullname());
        mNavProfileSubhead.setText(App.getInstance().getFullname());

        // Online icon

        mProfileOnlineIcon.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {

        super.onResume();

        updateView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}