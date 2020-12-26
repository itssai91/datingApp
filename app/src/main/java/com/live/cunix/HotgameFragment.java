package com.live.cunix;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.live.cunix.app.App;
import com.live.cunix.constants.Constants;
import com.live.cunix.model.Profile;
import com.live.cunix.util.CustomRequest;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class HotgameFragment extends Fragment implements Constants {

    private static final String STATE_LIST = "State Adapter Data";

    private FusedLocationProviderClient mFusedLocationClient;
    private Location mLastLocation;

    Menu MainMenu;

    TextView mMessage, mDetails;
    ImageView mSplash;

    TextView mHotgameUsername, mHotgameStatus;

    LinearLayout mSpotLight, mPermissionSpotlight;
    RelativeLayout mHotgameLayout;

    Button mGrantPermission;

    private ArrayList<Profile> itemsList;

    public ImageView mHotgamePhoto, mHotgameStamp;
    public FloatingActionButton mHotgameLike, mHotgameDislike;
    public ProgressBar mHotgameProgressBar;

    private int gender = 3, sex_orientation = 0, liked = 1, matches = 1, distance = 500;

    private int itemId = 0;
    private int arrayLength = 0;
    private Boolean loading = false;
    private Boolean viewMore = false;
    private Boolean restore = false;
    private Boolean spotlight = true;

    private int position = -1;

    int pastVisiblesItems = 0, visibleItemCount = 0, totalItemCount = 0;

    public HotgameFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

//        setRetainInstance(true);

        setHasOptionsMenu(true);

        if (savedInstanceState != null) {

            itemsList = savedInstanceState.getParcelableArrayList(STATE_LIST);

            viewMore = savedInstanceState.getBoolean("viewMore");
            restore = savedInstanceState.getBoolean("restore");
            loading = savedInstanceState.getBoolean("loading");
            spotlight = savedInstanceState.getBoolean("spotlight");
            itemId = savedInstanceState.getInt("itemId");
            position = savedInstanceState.getInt("position");
            distance = savedInstanceState.getInt("distance");

            gender = savedInstanceState.getInt("gender");
            liked = savedInstanceState.getInt("liked");
            matches = savedInstanceState.getInt("matches");
            sex_orientation = savedInstanceState.getInt("sex_orientation");

        } else {

            itemsList = new ArrayList<Profile>();

            restore = false;
            loading = false;
            spotlight = true;
            itemId = 0;
            position = -1;
            sex_orientation = 0;
            distance = 100;

            gender = 3;

            liked = 1;
            matches = 1;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_hotgame, container, false);

        mHotgamePhoto = rootView.findViewById(R.id.hotgamePhoto);
        mHotgameStamp = rootView.findViewById(R.id.hotgameStamp);
        mHotgameLike = rootView.findViewById(R.id.fabLike);
        mHotgameDislike = rootView.findViewById(R.id.fabDislike);

        mHotgameProgressBar = rootView.findViewById(R.id.hotgameProgressBar);

        mHotgameLayout = rootView.findViewById(R.id.hotgameLayout);

        mHotgameUsername = rootView.findViewById(R.id.hotgameUsername);
        mHotgameStatus = rootView.findViewById(R.id.hotgameStatus);

        mMessage = rootView.findViewById(R.id.message);
        mSplash = rootView.findViewById(R.id.splash);

        mSpotLight = rootView.findViewById(R.id.spotlight);
        mDetails = rootView.findViewById(R.id.openLocationSettings);

        mPermissionSpotlight = rootView.findViewById(R.id.permission_spotlight);
        mGrantPermission = rootView.findViewById(R.id.grantPermissionBtn);

        mHotgamePhoto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Profile u = itemsList.get(position);

                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                intent.putExtra("profileId", u.getId());
                startActivity(intent);
            }
        });

        mHotgameLike.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Profile u = itemsList.get(position);

                if (u.isMatch() || u.isMyLike()) {

                    position++;

                } else {

                    mHotgameDislike.hide();
                    mHotgameLike.hide();

                    like(u.getId());
                }

                if ((itemsList.size() - 1) < position && itemId > 1) {

                    showMessage(getText(R.string.msg_loading_2).toString());

                    getItems();

                } else if ((itemsList.size() - 1) < position) {

                    mHotgameLayout.setVisibility(View.GONE);

                    showMessage(getText(R.string.label_empty_list).toString());
                }

                updateHotgameContainer();
            }
        });

        mHotgameDislike.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                position++;

                if ((itemsList.size() - 1) < position && itemId > 1) {

                    showMessage(getText(R.string.msg_loading_2).toString());

                    getItems();

                } else if ((itemsList.size() - 1) < position) {

                    mHotgameLayout.setVisibility(View.GONE);

                    showMessage(getText(R.string.label_empty_list).toString());
                }

                updateHotgameContainer();
            }
        });

        if (itemsList.size() == 0) {

            showMessage(getText(R.string.label_empty_list).toString());

        } else {

            hideMessage();
        }

        mDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), LocationActivity.class);
                startActivityForResult(i, 101);
            }
        });


        mGrantPermission.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)){

                        ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_LOCATION);

                    } else {

                        ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_LOCATION);
                    }
                }
            }
        });

        updateSpotLight();

        if (!restore && App.getInstance().getLat() != 0.000000 && App.getInstance().getLng() != 0.000000) {

            if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                updateSpotLight();

            } else {

                showMessage(getText(R.string.msg_loading_2).toString());

                getItems();
            }
        }


        // Inflate the layout for this fragment
        return rootView;
    }

    public void updateSpotLight() {

        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)){

                showPermissionSpotlight();
                hideNoLocationSpotlight();
                hideHotgameContainer();
                hideMessage();

            } else {

                showPermissionSpotlight();
                hideNoLocationSpotlight();
                hideHotgameContainer();
                hideMessage();
            }

        } else {

            hidePermissionSpotlight();

            if (App.getInstance().getLat() != 0.000000 && App.getInstance().getLng() != 0.000000) {

                hidePermissionSpotlight();
                hideNoLocationSpotlight();
                showHotgameContainer();

            } else {

                showNoLocationSpotlight();
                hideHotgameContainer();
                hideMessage();
            }
        }

        getActivity().invalidateOptionsMenu();
    }

    public void updateHotgameContainer() {

        if (itemsList.size() > 0 && position != -1 && (itemsList.size() - 1) >= position) {

            Profile u = itemsList.get(position);

            mHotgameUsername.setText(u.getFullname() + ", " + u.getAge());

            if (!u.isVerify()) {

                mHotgameUsername.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

            } else {

                mHotgameUsername.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.profile_verify_icon, 0);
            }

            if (u.getLocation().length() >  0) {

                mHotgameStatus.setVisibility(View.VISIBLE);
                mHotgameStatus.setText(u.getLocation());

            } else {

                mHotgameStatus.setVisibility(View.GONE);
            }

            if (!u.isMatch() && !u.isMyLike()) {

                mHotgameStamp.setVisibility(View.GONE);

            } else if (u.isMatch()) {

                mHotgameStamp.setVisibility(View.VISIBLE);
                mHotgameStamp.setImageResource(R.drawable.ic_hotgame_match);

            } else if (u.isMyLike() && !u.isMatch()) {

                mHotgameStamp.setVisibility(View.VISIBLE);
                mHotgameStamp.setImageResource(R.drawable.ic_hotgame_liked);
            }

            mHotgameLayout.setVisibility(View.GONE);
            mHotgameProgressBar.setVisibility(View.VISIBLE);

            if (u.getNormalPhotoUrl() != null && u.getNormalPhotoUrl().length() > 0) {

                final ImageView img = mHotgamePhoto;
                final ProgressBar progressView = mHotgameProgressBar;
                final RelativeLayout layout = mHotgameLayout;

                Picasso.with(getActivity())
                        .load(u.getNormalPhotoUrl())
                        .into(mHotgamePhoto, new Callback() {

                            @Override
                            public void onSuccess() {

                                progressView.setVisibility(View.GONE);
                                img.setVisibility(View.VISIBLE);
                                layout.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onError() {

                                progressView.setVisibility(View.GONE);
                                img.setVisibility(View.VISIBLE);
                                img.setImageResource(R.drawable.profile_default_photo);
                                layout.setVisibility(View.VISIBLE);
                            }
                        });

            }

            if (!loading) {

                if (u.isMatch() || u.isMyLike()) {

                    mHotgameLike.setImageResource(R.drawable.hotgame_action_next);
                }

                if (!u.isMatch() && !u.isMyLike()) {

                    mHotgameLike.setImageResource(R.drawable.hotgame_action_like);
                }

                mHotgameLike.show();
                mHotgameLike.hide();
                mHotgameLike.show();
                mHotgameDislike.show();

            } else {

                mHotgameLike.hide();
                mHotgameDislike.hide();
            }
        }
    }

    public void showHotgameContainer() {

        if (itemsList.size() - 1 < position) {

            mHotgameLayout.setVisibility(View.GONE);

            showMessage(getText(R.string.label_empty_list).toString());

        } else {

            mHotgamePhoto.setVisibility(View.VISIBLE);
            mHotgameLike.show();
            mHotgameDislike.show();

            mHotgameProgressBar.setVisibility(View.GONE);
        }

        updateHotgameContainer();
    }

    public void hideHotgameContainer() {

        mHotgameLayout.setVisibility(View.GONE);

        mHotgamePhoto.setVisibility(View.GONE);
        mHotgameLike.hide();
        mHotgameDislike.hide();

        mHotgameProgressBar.setVisibility(View.GONE);
    }

    public void showPermissionSpotlight() {

        mPermissionSpotlight.setVisibility(View.VISIBLE);
    }

    public void showNoLocationSpotlight() {

        mSpotLight.setVisibility(View.VISIBLE);
    }

    public void hidePermissionSpotlight() {

        mPermissionSpotlight.setVisibility(View.GONE);
    }

    public void hideNoLocationSpotlight() {

        mSpotLight.setVisibility(View.GONE);
    }

    public void updateItems() {

        if (App.getInstance().getLat() != 0.000000 && App.getInstance().getLng() != 0.000000) {

            showMessage(getText(R.string.msg_loading_2).toString());

            itemId = 0;

            getItems();
        }
    }

    @Override
    public void onStart() {

        super.onStart();

        updateSpotLight();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101 && resultCode == getActivity().RESULT_OK) {

            updateSpotLight();

            updateItems();

        } else if (requestCode == 10001 && resultCode == getActivity().RESULT_OK) {

            updateSpotLight();

            updateItems();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {

            case MY_PERMISSIONS_REQUEST_ACCESS_LOCATION: {

                // If request is cancelled, the result arrays are empty.

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    LocationManager lm = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);

                    if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER) && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

                        mFusedLocationClient.getLastLocation().addOnCompleteListener(getActivity(), new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {

                                if (task.isSuccessful() && task.getResult() != null) {

                                    mLastLocation = task.getResult();

                                    Log.d("GPS", "PeopleNearby onComplete" + mLastLocation.getLatitude());
                                    Log.d("GPS", "PeopleNearby onComplete" + mLastLocation.getLongitude());

                                    App.getInstance().setLat(mLastLocation.getLatitude());
                                    App.getInstance().setLng(mLastLocation.getLongitude());

                                } else {

                                    Log.d("GPS", "getLastLocation:exception", task.getException());
                                }

                                updateSpotLight();

                                updateItems();
                            }
                        });
                    }

                    updateSpotLight();

                } else if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {

                    if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) || !ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                        showNoLocationPermissionSnackbar();
                    }
                }

                return;
            }
        }
    }

    public void showNoLocationPermissionSnackbar() {

        Snackbar.make(getView(), getString(R.string.label_no_location_permission) , Snackbar.LENGTH_LONG).setAction(getString(R.string.action_settings), new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                openApplicationSettings();

                Toast.makeText(getActivity(), getString(R.string.label_grant_location_permission), Toast.LENGTH_SHORT).show();

            }

        }).show();
    }

    public void openApplicationSettings() {

        Intent appSettingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getActivity().getPackageName()));
        startActivityForResult(appSettingsIntent, 10001);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

        outState.putBoolean("viewMore", viewMore);
        outState.putBoolean("restore", true);
        outState.putBoolean("loading", loading);
        outState.putBoolean("spotlight", spotlight);
        outState.putInt("itemId", itemId);
        outState.putInt("position", position);
        outState.putInt("gender", gender);
        outState.putInt("matches", matches);
        outState.putInt("liked", liked);
        outState.putInt("sex_orientation", sex_orientation);
        outState.putInt("distance", distance);
        outState.putParcelableArrayList(STATE_LIST, itemsList);
    }

    public void getItems() {

        if (App.getInstance().getLat() != 0.000000 && App.getInstance().getLng() != 0.000000) {

            CustomRequest jsonReq = new CustomRequest(Request.Method.POST, METHOD_HOTGAME_GET, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            if (!isAdded() || getActivity() == null) {

                                Log.e("ERROR", "HotgameFragment Not Added to Activity");

                                return;
                            }

                            try {

                                arrayLength = 0;

                                if (!response.getBoolean("error")) {

                                    itemId = response.getInt("itemId");

                                    if (response.has("items")) {

                                        JSONArray usersArray = response.getJSONArray("items");

                                        arrayLength = usersArray.length();

                                        if (arrayLength > 0) {

                                            for (int i = 0; i < usersArray.length(); i++) {

                                                JSONObject userObj = (JSONObject) usersArray.get(i);

                                                Profile profile = new Profile(userObj);

                                                itemsList.add(profile);
                                            }
                                        }
                                    }

                                }

                            } catch (JSONException e) {

                                e.printStackTrace();

                            } finally {

                                loadingComplete();

                                Log.d("Success", response.toString());
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    if (!isAdded() || getActivity() == null) {

                        Log.e("ERROR", "HotgameFragment Not Added to Activity");

                        return;
                    }

                    loadingComplete();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("accountId", Long.toString(App.getInstance().getId()));
                    params.put("accessToken", App.getInstance().getAccessToken());
                    params.put("distance", Integer.toString(distance));
                    params.put("lat", Double.toString(App.getInstance().getLat()));
                    params.put("lng", Double.toString(App.getInstance().getLng()));
                    params.put("itemId", Long.toString(itemId));
                    params.put("sex", Integer.toString(gender));
                    params.put("sex_orientation", Integer.toString(sex_orientation));
                    params.put("liked", Integer.toString(liked));
                    params.put("matches", Integer.toString(matches));

                    return params;
                }
            };

            RetryPolicy policy = new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(VOLLEY_REQUEST_SECONDS), DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

            jsonReq.setRetryPolicy(policy);

            App.getInstance().addToRequestQueue(jsonReq);

        }
    }

    public void loadingComplete() {

        viewMore = arrayLength == LIST_ITEMS;

        if (itemsList.size() == 0 || (itemsList.size() - 1)  < position) {

            showMessage(getText(R.string.label_empty_list).toString());

        } else {

            hideMessage();

            if (position == -1) position = 0;

            updateHotgameContainer();
            showHotgameContainer();
        }

        loading = false;
    }

    public void showMessage(String message) {

        mMessage.setText(message);
        mMessage.setVisibility(View.VISIBLE);

        mSplash.setVisibility(View.VISIBLE);

        hideHotgameContainer();
    }

    public void hideMessage() {

        mMessage.setVisibility(View.GONE);

        mSplash.setVisibility(View.GONE);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);

        menu.clear();

        inflater.inflate(R.menu.menu_hotgame, menu);

        MainMenu = menu;
    }

    public void getHotgameSettings() {

        AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
        b.setTitle(getText(R.string.label_hotgame_dialog_title));

        LinearLayout view = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.dialog_hotgame_settings, null);

        b.setView(view);

        final CheckBox mLikedCheckBox = view.findViewById(R.id.likedCheckBox);
        final CheckBox mMatchesCheckBox = view.findViewById(R.id.matchesCheckBox);

        final RadioButton mAnyGenderRadio = view.findViewById(R.id.radio_gender_any);
        final RadioButton mMaleGenderRadio = view.findViewById(R.id.radio_gender_male);
        final RadioButton mFemaleGenderRadio = view.findViewById(R.id.radio_gender_female);
        final RadioButton mSecretGenderRadio = view.findViewById(R.id.radio_gender_secret);

        final RadioButton mAnySexOrientationRadio = view.findViewById(R.id.radio_sex_orientation_any);
        final RadioButton mHeterosexualSexOrientationRadio = view.findViewById(R.id.radio_sex_orientation_heterosexual);
        final RadioButton mGaySexOrientationRadio = view.findViewById(R.id.radio_sex_orientation_gay);
        final RadioButton mLesbianSexOrientationRadio = view.findViewById(R.id.radio_sex_orientation_lesbian);
        final RadioButton mBisexualSexOrientationRadio = view.findViewById(R.id.radio_sex_orientation_bisexual);

        final TextView mDistanceLabel = view.findViewById(R.id.distance_label);

        final AppCompatSeekBar mDistanceSeekBar = view.findViewById(R.id.choice_distance);

        switch (gender) {

            case 0: {

                mMaleGenderRadio.setChecked(true);

                break;
            }

            case 1: {

                mFemaleGenderRadio.setChecked(true);

                break;
            }

            case 2: {

                mSecretGenderRadio.setChecked(true);

                break;
            }

            default: {

                mAnyGenderRadio.setChecked(true);

                break;
            }
        }

        switch (sex_orientation) {

            case 0: {

                mAnySexOrientationRadio.setChecked(true);

                break;
            }

            case 1: {

                mHeterosexualSexOrientationRadio.setChecked(true);

                break;
            }

            case 2: {

                mGaySexOrientationRadio.setChecked(true);

                break;
            }

            case 3: {

                mLesbianSexOrientationRadio.setChecked(true);

                break;
            }

            default: {

                mBisexualSexOrientationRadio.setChecked(true);

                break;
            }
        }

        mDistanceSeekBar.setProgress(distance);
        mDistanceLabel.setText(String.format(Locale.getDefault(), getString(R.string.label_distance), distance + 30));

        mDistanceSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                mDistanceLabel.setText(String.format(Locale.getDefault(), getString(R.string.label_distance), progress + 30));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        if (liked == 1) {

            mLikedCheckBox.setChecked(true);

        } else {

            mLikedCheckBox.setChecked(false);
        }

        if (matches == 1) {

            mMatchesCheckBox.setChecked(true);

        } else {

            mMatchesCheckBox.setChecked(false);
        }

        b.setPositiveButton(getText(R.string.action_ok), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                // get distance

                distance = mDistanceSeekBar.getProgress();

                // Gender

                if (mAnyGenderRadio.isChecked()) {

                    gender = 3;
                }

                if (mMaleGenderRadio.isChecked()) {

                    gender = 0;
                }

                if (mFemaleGenderRadio.isChecked()) {

                    gender = 1;
                }

                if (mSecretGenderRadio.isChecked()) {

                    gender = 2;
                }

                // Sex orientation

                if (mAnySexOrientationRadio.isChecked()) {

                    sex_orientation = 0;
                }

                if (mHeterosexualSexOrientationRadio.isChecked()) {

                    sex_orientation = 1;
                }

                if (mGaySexOrientationRadio.isChecked()) {

                    sex_orientation = 2;
                }

                if (mLesbianSexOrientationRadio.isChecked()) {

                    sex_orientation = 3;
                }

                if (mBisexualSexOrientationRadio.isChecked()) {

                    sex_orientation = 4;
                }

                if (mLikedCheckBox.isChecked()) {

                    liked = 1;

                } else {

                    liked = 0;
                }

                if (mMatchesCheckBox.isChecked()) {

                    matches = 1;

                } else {

                    matches = 0;
                }

                itemsList.clear();

                itemId = 0;

                position = -1;

                showMessage(getText(R.string.msg_loading_2).toString());

                getItems();
            }
        });

        b.setNegativeButton(getText(R.string.action_cancel), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });

        AlertDialog d = b.create();

        d.setCanceledOnTouchOutside(false);
        d.setCancelable(false);
        d.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

            case R.id.action_hotgame_settings: {

                getHotgameSettings();

                return true;
            }

            default: {

                return super.onOptionsItemSelected(item);
            }
        }
    }

    public void like(final long profileId) {

        loading = true;

        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, METHOD_PROFILE_LIKE, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (!isAdded() || getActivity() == null) {

                            Log.e("ERROR", "HotgameFragment Not Added to Activity");

                            return;
                        }

                        try {

                            if (!response.getBoolean("error")) {

                                if (response.has("myLike")) {

                                    itemsList.get(position).setMyLike(response.getBoolean("myLike"));
                                }

                                if (response.has("match")) {

                                    itemsList.get(position).setMatch(response.getBoolean("match"));
                                }
                            }

                        } catch (JSONException e) {

                            e.printStackTrace();

                        } finally {

                            loading = false;

                            updateHotgameContainer();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (!isAdded() || getActivity() == null) {

                    Log.e("ERROR", "HotgameFragment Not Added to Activity");

                    return;
                }

                loading = false;

                updateHotgameContainer();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("accountId", Long.toString(App.getInstance().getId()));
                params.put("accessToken", App.getInstance().getAccessToken());
                params.put("profileId", Long.toString(profileId));

                return params;
            }
        };

        App.getInstance().addToRequestQueue(jsonReq);
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