package com.live.cunix.util;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.live.cunix.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helper extends Application {

    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();

    private AppCompatActivity activity;
    private Context context;

    public Helper(Context current){

        this.context = current;
    }

    public Helper(AppCompatActivity activity) {

        this.activity = activity;
    }

    public Helper(FragmentActivity activity) {

    }

    public static String getGenderTitle(Context ctx, int gender) {

        switch (gender) {

            case 0: {

                return ctx.getString(R.string.label_male);
            }

            case 1: {

                return ctx.getString(R.string.label_female);
            }

            case 2: {

                return ctx.getString(R.string.label_secret);
            }

            default: {

                return ctx.getString(R.string.label_select_gender);
            }
        }
    }

    public static String getSexOrientationTitle(Context ctx, int sex_orientation) {

        switch (sex_orientation) {

            case 1: {

                return ctx.getString(R.string.sex_orientation_1);
            }

            case 2: {

                return ctx.getString(R.string.sex_orientation_2);

            }

            case 3: {

                return ctx.getString(R.string.sex_orientation_3);
            }

            case 4: {

                return ctx.getString(R.string.sex_orientation_4);
            }

            default: {

                return ctx.getString(R.string.label_select_sex_orientation);
            }
        }
    }

    public static int getGalleryGridCount(FragmentActivity activity) {

        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);

        float screenWidth  = displayMetrics.widthPixels;
        float cellWidth = activity.getResources().getDimension(R.dimen.gallery_item_size);

        return Math.round(screenWidth / cellWidth);
    }

    public static int dpToPx(Context c, int dp) {

        Resources r = c.getResources();

        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public static int getGridSpanCount(FragmentActivity activity) {

        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        float screenWidth  = displayMetrics.widthPixels;
        float cellWidth = activity.getResources().getDimension(R.dimen.item_size);
        return Math.round(screenWidth / cellWidth);
    }

    public static int getStickersGridSpanCount(FragmentActivity activity) {

        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        float screenWidth  = displayMetrics.widthPixels;
        float cellWidth = activity.getResources().getDimension(R.dimen.sticker_item_size);
        return Math.round(screenWidth / cellWidth);
    }

    public static String randomString(int len) {

        StringBuilder sb = new StringBuilder(len);

        for (int i = 0; i < len; i++)

            sb.append(AB.charAt(rnd.nextInt(AB.length())));

        return sb.toString();
    }

    public boolean isValidEmail(String email) {

    	if (TextUtils.isEmpty(email)) {

    		return false;

    	} else {

    		return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    	}
    }
    
    public boolean isValidLogin(String login) {

        String regExpn = "^([a-zA-Z]{4,24})?([a-zA-Z][a-zA-Z0-9_]{4,24})$";
        CharSequence inputStr = login;
        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        return matcher.matches();
    }

    public boolean isValidSearchQuery(String query) {

        String regExpn = "^([a-zA-Z]{1,24})?([a-zA-Z][a-zA-Z0-9_]{1,24})$";
        CharSequence inputStr = query;
        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        return matcher.matches();
    }
    
    public boolean isValidPassword(String password) {

        String regExpn = "^[a-z0-9_]{6,24}$";
        CharSequence inputStr = password;
        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        return matcher.matches();
    }

    public static String md5(final String s) {

        final String MD5 = "MD5";

        try {

            // Create MD5 Hash

            MessageDigest digest = java.security.MessageDigest.getInstance(MD5);
            digest.update(s.getBytes());

            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();

            for (byte aMessageDigest : messageDigest) {

                String h = Integer.toHexString(0xFF & aMessageDigest);

                while (h.length() < 2) h = "0" + h;

                hexString.append(h);
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {

            e.printStackTrace();
        }

        return "";
    }

    public String getRelationshipStatus(int mRelationship) {

        switch (mRelationship) {

            case 0: {

                return "-";
            }

            case 1: {

                return context.getResources().getString(R.string.relationship_status_1);
            }

            case 2: {

                return context.getResources().getString(R.string.relationship_status_2);
            }

            case 3: {

                return context.getResources().getString(R.string.relationship_status_3);
            }

            case 4: {

                return context.getResources().getString(R.string.relationship_status_4);
            }

            case 5: {

                return context.getResources().getString(R.string.relationship_status_5);
            }

            case 6: {

                return context.getResources().getString(R.string.relationship_status_6);
            }

            case 7: {

                return context.getResources().getString(R.string.relationship_status_7);
            }

            default: {

                break;
            }
        }

        return "-";
    }

    public String getPoliticalViews(int mPolitical) {

        switch (mPolitical) {

            case 0: {

                return "-";
            }

            case 1: {

                return context.getResources().getString(R.string.political_views_1);
            }

            case 2: {

                return context.getResources().getString(R.string.political_views_2);
            }

            case 3: {

                return context.getResources().getString(R.string.political_views_3);
            }

            case 4: {

                return context.getResources().getString(R.string.political_views_4);
            }

            case 5: {

                return context.getResources().getString(R.string.political_views_5);
            }

            case 6: {

                return context.getResources().getString(R.string.political_views_6);
            }

            case 7: {

                return context.getResources().getString(R.string.political_views_7);
            }

            case 8: {

                return context.getResources().getString(R.string.political_views_8);
            }

            case 9: {

                return context.getResources().getString(R.string.political_views_9);
            }

            default: {

                break;
            }
        }

        return "-";
    }

    public String getWorldView(int mWorld) {

        switch (mWorld) {

            case 0: {

                return "-";
            }

            case 1: {

                return context.getResources().getString(R.string.world_view_1);
            }

            case 2: {

                return context.getResources().getString(R.string.world_view_2);
            }

            case 3: {

                return context.getResources().getString(R.string.world_view_3);
            }

            case 4: {

                return context.getResources().getString(R.string.world_view_4);
            }

            case 5: {

                return context.getResources().getString(R.string.world_view_5);
            }

            case 6: {

                return context.getString(R.string.world_view_6);
            }

            case 7: {

                return context.getString(R.string.world_view_7);
            }

            case 8: {

                return context.getString(R.string.world_view_8);
            }

            case 9: {

                return context.getString(R.string.world_view_9);
            }

            default: {

                break;
            }
        }

        return "-";
    }

    public String getPersonalPriority(int mPriority) {

        switch (mPriority) {

            case 0: {

                return "-";
            }

            case 1: {

                return context.getString(R.string.personal_priority_1);
            }

            case 2: {

                return context.getString(R.string.personal_priority_2);
            }

            case 3: {

                return context.getString(R.string.personal_priority_3);
            }

            case 4: {

                return context.getString(R.string.personal_priority_4);
            }

            case 5: {

                return context.getString(R.string.personal_priority_5);
            }

            case 6: {

                return context.getString(R.string.personal_priority_6);
            }

            case 7: {

                return context.getString(R.string.personal_priority_7);
            }

            case 8: {

                return context.getString(R.string.personal_priority_8);
            }

            default: {

                break;
            }
        }

        return "-";
    }

    public String getImportantInOthers(int mImportant) {

        switch (mImportant) {

            case 0: {

                return "-";
            }

            case 1: {

                return context.getString(R.string.important_in_others_1);
            }

            case 2: {

                return context.getString(R.string.important_in_others_2);
            }

            case 3: {

                return context.getString(R.string.important_in_others_3);
            }

            case 4: {

                return context.getString(R.string.important_in_others_4);
            }

            case 5: {

                return context.getString(R.string.important_in_others_5);
            }

            case 6: {

                return context.getString(R.string.important_in_others_6);
            }

            default: {

                break;
            }
        }

        return "-";
    }

    public String getSmokingViews(int mSmoking) {

        switch (mSmoking) {

            case 0: {

                return "-";
            }

            case 1: {

                return context.getString(R.string.smoking_views_1);
            }

            case 2: {

                return context.getString(R.string.smoking_views_2);
            }

            case 3: {

                return context.getString(R.string.smoking_views_3);
            }

            case 4: {

                return context.getString(R.string.smoking_views_4);
            }

            case 5: {

                return context.getString(R.string.smoking_views_5);
            }

            default: {

                break;
            }
        }

        return "-";
    }

    public String getAlcoholViews(int mAlcohol) {

        switch (mAlcohol) {

            case 0: {

                return "-";
            }

            case 1: {

                return context.getString(R.string.alcohol_views_1);
            }

            case 2: {

                return context.getString(R.string.alcohol_views_2);
            }

            case 3: {

                return context.getString(R.string.alcohol_views_3);
            }

            case 4: {

                return context.getString(R.string.alcohol_views_4);
            }

            case 5: {

                return context.getString(R.string.alcohol_views_5);
            }

            default: {

                break;
            }
        }

        return "-";
    }

    public String getLooking(int mLooking) {

        switch (mLooking) {

            case 0: {

                return "-";
            }

            case 1: {

                return context.getString(R.string.you_looking_1);
            }

            case 2: {

                return context.getString(R.string.you_looking_2);
            }

            case 3: {

                return context.getString(R.string.you_looking_3);
            }

            default: {

                break;
            }
        }

        return "-";
    }

    public String getGenderLike(int mLike) {

        switch (mLike) {

            case 0: {

                return "-";
            }

            case 1: {

                return context.getString(R.string.profile_like_1);
            }

            case 2: {

                return context.getString(R.string.profile_like_2);
            }

            default: {

                break;
            }
        }

        return "-";
    }
}
