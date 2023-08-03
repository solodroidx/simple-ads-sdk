package com.solodroid.ads.sdk.format;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.solodroid.ads.sdk.util.OnShowAdCompleteListener;

import java.util.Date;

public class AppOpenAdAppLovin {

    private static final String LOG_TAG = "AppOpenAd";
    private boolean isLoadingAd = false;
    public boolean isShowingAd = false;
    private long loadTime = 0;

    public AppOpenAdAppLovin() {
    }

    public void loadAd(Context context, String maxAppOpenAdUnitId) {
        if (isLoadingAd || isAdAvailable()) {
            return;
        }
        isLoadingAd = true;
    }

    public boolean wasLoadTimeLessThanNHoursAgo(long numHours) {
        long dateDifference = (new Date()).getTime() - loadTime;
        long numMilliSecondsPerHour = 3600000;
        return (dateDifference < (numMilliSecondsPerHour * numHours));
    }

    public boolean isAdAvailable() {
        return false;
    }

    public void showAdIfAvailable(@NonNull final Activity activity, String appOpenAdUnitId) {
        showAdIfAvailable(activity, appOpenAdUnitId, () -> {
        });
    }

    public void showAdIfAvailable(@NonNull final Activity activity, String appOpenAdUnitId, @NonNull OnShowAdCompleteListener onShowAdCompleteListener) {
        if (isShowingAd) {
            Log.d(LOG_TAG, "The app open ad is already showing.");
            return;
        }

        if (!isAdAvailable()) {
            Log.d(LOG_TAG, "The app open ad is not ready yet.");
            onShowAdCompleteListener.onShowAdComplete();
            loadAd(activity, appOpenAdUnitId);
            return;
        }

        Log.d(LOG_TAG, "Will show ad.");

        isShowingAd = true;
    }

}