package com.solodroid.ads.sdk.format;

import static com.solodroid.ads.sdk.util.Constant.ADMOB;
import static com.solodroid.ads.sdk.util.Constant.AD_STATUS_ON;
import static com.solodroid.ads.sdk.util.Constant.FAN_BIDDING_ADMOB;
import static com.solodroid.ads.sdk.util.Constant.FAN_BIDDING_AD_MANAGER;
import static com.solodroid.ads.sdk.util.Constant.GOOGLE_AD_MANAGER;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.solodroid.ads.sdk.util.OnShowAdCompleteListener;

@SuppressLint("StaticFieldLeak")
public class AppOpenAd {
    public static com.google.android.gms.ads.appopen.AppOpenAd appOpenAd = null;
    public static boolean isAppOpenAdLoaded = false;

    public static class Builder {

        private static final String TAG = "AppOpenAd";
        private final Activity activity;
        private String adStatus = "";
        private String adNetwork = "";
        private String backupAdNetwork = "";
        private String adMobAppOpenId = "";
        private String adManagerAppOpenId = "";
        private String applovinAppOpenId = "";
        private String wortiseAppOpenId = "";

        public Builder(Activity activity) {
            this.activity = activity;
        }

        public Builder build() {
            loadAppOpenAd();
            return this;
        }

        public Builder build(OnShowAdCompleteListener onShowAdCompleteListener) {
            loadAppOpenAd(onShowAdCompleteListener);
            return this;
        }

        public Builder show() {
            showAppOpenAd();
            return this;
        }

        public Builder show(OnShowAdCompleteListener onShowAdCompleteListener) {
            showAppOpenAd(onShowAdCompleteListener);
            return this;
        }

        public Builder setAdStatus(String adStatus) {
            this.adStatus = adStatus;
            return this;
        }

        public Builder setAdNetwork(String adNetwork) {
            this.adNetwork = adNetwork;
            return this;
        }

        public Builder setBackupAdNetwork(String backupAdNetwork) {
            this.backupAdNetwork = backupAdNetwork;
            return this;
        }

        public Builder setAdMobAppOpenId(String adMobAppOpenId) {
            this.adMobAppOpenId = adMobAppOpenId;
            return this;
        }

        public Builder setAdManagerAppOpenId(String adManagerAppOpenId) {
            this.adManagerAppOpenId = adManagerAppOpenId;
            return this;
        }

        public Builder setApplovinAppOpenId(String applovinAppOpenId) {
            this.applovinAppOpenId = applovinAppOpenId;
            return this;
        }

        public Builder setWortiseAppOpenId(String wortiseAppOpenId) {
            this.wortiseAppOpenId = wortiseAppOpenId;
            return this;
        }

        public void destroyOpenAd() {
            AppOpenAd.isAppOpenAdLoaded = false;
            if (adStatus.equals(AD_STATUS_ON)) {
                switch (adNetwork) {
                    case ADMOB:
                    case FAN_BIDDING_ADMOB:
                    case GOOGLE_AD_MANAGER:
                    case FAN_BIDDING_AD_MANAGER:
                        if (appOpenAd != null) {
                            appOpenAd = null;
                        }
                        break;

                    default:
                        break;
                }
            }
        }

        //main ads
        public void loadAppOpenAd(OnShowAdCompleteListener onShowAdCompleteListener) {
            if (adStatus.equals(AD_STATUS_ON)) {
                switch (adNetwork) {
                    case ADMOB:
                    case FAN_BIDDING_ADMOB:
                        AdRequest adRequest = new AdRequest.Builder().build();
                        com.google.android.gms.ads.appopen.AppOpenAd.load(activity, adMobAppOpenId, adRequest, new com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback() {
                            @Override
                            public void onAdLoaded(@NonNull com.google.android.gms.ads.appopen.AppOpenAd ad) {
                                appOpenAd = ad;
                                showAppOpenAd(onShowAdCompleteListener);
                                Log.d(TAG, "[" + adNetwork + "] " + "[on start] app open ad loaded");
                            }

                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                appOpenAd = null;
                                loadBackupAppOpenAd(onShowAdCompleteListener);
                                Log.d(TAG, "[" + adNetwork + "] " + "[on start] failed to load app open ad: " + loadAdError.getMessage());
                            }
                        });
                        break;

                    case GOOGLE_AD_MANAGER:
                    case FAN_BIDDING_AD_MANAGER:
                        @SuppressLint("VisibleForTests") AdManagerAdRequest adManagerAdRequest = new AdManagerAdRequest.Builder().build();
                        com.google.android.gms.ads.appopen.AppOpenAd.load(activity, adManagerAppOpenId, adManagerAdRequest, new com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback() {
                            @Override
                            public void onAdLoaded(@NonNull com.google.android.gms.ads.appopen.AppOpenAd ad) {
                                appOpenAd = ad;
                                showAppOpenAd(onShowAdCompleteListener);
                                Log.d(TAG, "[" + adNetwork + "] " + "[on start] app open ad loaded");
                            }

                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                appOpenAd = null;
                                loadBackupAppOpenAd(onShowAdCompleteListener);
                                Log.d(TAG, "[" + adNetwork + "] " + "[on start] failed to load app open ad: " + loadAdError.getMessage());
                            }
                        });
                        break;

                    default:
                        onShowAdCompleteListener.onShowAdComplete();
                        break;
                }
            } else {
                onShowAdCompleteListener.onShowAdComplete();
            }
        }

        public void showAppOpenAd(OnShowAdCompleteListener onShowAdCompleteListener) {
            switch (adNetwork) {
                case ADMOB:
                case FAN_BIDDING_ADMOB:
                case GOOGLE_AD_MANAGER:
                case FAN_BIDDING_AD_MANAGER:
                    if (appOpenAd != null) {
                        appOpenAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                appOpenAd = null;
                                onShowAdCompleteListener.onShowAdComplete();
                                Log.d(TAG, "[" + adNetwork + "] " + "[on start] close app open ad");
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                appOpenAd = null;
                                onShowAdCompleteListener.onShowAdComplete();
                                Log.d(TAG, "[" + adNetwork + "] " + "[on start] failed to show app open ad: " + adError.getMessage());
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                Log.d(TAG, "[" + adNetwork + "] " + "[on start] show app open ad");
                            }
                        });
                        appOpenAd.show(activity);
                    } else {
                        onShowAdCompleteListener.onShowAdComplete();
                    }
                    break;

                default:
                    onShowAdCompleteListener.onShowAdComplete();
                    break;
            }
        }

        public void loadAppOpenAd() {
            if (adStatus.equals(AD_STATUS_ON)) {
                switch (adNetwork) {
                    case ADMOB:
                    case FAN_BIDDING_ADMOB:
                        AdRequest adRequest = new AdRequest.Builder().build();
                        com.google.android.gms.ads.appopen.AppOpenAd.load(activity, adMobAppOpenId, adRequest, new com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback() {
                            @Override
                            public void onAdLoaded(@NonNull com.google.android.gms.ads.appopen.AppOpenAd ad) {
                                appOpenAd = ad;
                                isAppOpenAdLoaded = true;
                                Log.d(TAG, "[" + adNetwork + "] " + "[on resume] app open ad loaded");
                            }

                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                appOpenAd = null;
                                isAppOpenAdLoaded = false;
                                loadBackupAppOpenAd();
                                Log.d(TAG, "[" + adNetwork + "] " + "[on resume] failed to load app open ad : " + loadAdError.getMessage());
                            }
                        });
                        break;

                    case GOOGLE_AD_MANAGER:
                    case FAN_BIDDING_AD_MANAGER:
                        @SuppressLint("VisibleForTests") AdManagerAdRequest adManagerAdRequest = new AdManagerAdRequest.Builder().build();
                        com.google.android.gms.ads.appopen.AppOpenAd.load(activity, adManagerAppOpenId, adManagerAdRequest, new com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback() {
                            @Override
                            public void onAdLoaded(@NonNull com.google.android.gms.ads.appopen.AppOpenAd ad) {
                                appOpenAd = ad;
                                isAppOpenAdLoaded = true;
                                Log.d(TAG, "[" + adNetwork + "] " + "[on resume] app open ad loaded");
                            }

                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                appOpenAd = null;
                                isAppOpenAdLoaded = false;
                                loadBackupAppOpenAd();
                                Log.d(TAG, "[" + adNetwork + "] " + "[on resume] failed to load app open ad : " + loadAdError.getMessage());
                            }
                        });
                        break;

                    default:
                        break;
                }
            }
        }

        public void showAppOpenAd() {
            switch (adNetwork) {
                case ADMOB:
                case FAN_BIDDING_ADMOB:
                case GOOGLE_AD_MANAGER:
                case FAN_BIDDING_AD_MANAGER:
                    if (appOpenAd != null) {
                        appOpenAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                appOpenAd = null;
                                loadAppOpenAd();
                                Log.d(TAG, "[" + adNetwork + "] " + "[on resume] close app open ad");
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                appOpenAd = null;
                                loadAppOpenAd();
                                Log.d(TAG, "[" + adNetwork + "] " + "[on resume] failed to show app open ad: " + adError.getMessage());
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                Log.d(TAG, "[" + adNetwork + "] " + "[on resume] show app open ad");
                            }
                        });
                        appOpenAd.show(activity);
                    } else {
                        showBackupAppOpenAd();
                    }
                    break;

                default:
                    break;
            }
        }

        //backup ads
        public void loadBackupAppOpenAd(OnShowAdCompleteListener onShowAdCompleteListener) {
            if (adStatus.equals(AD_STATUS_ON)) {
                switch (backupAdNetwork) {
                    case ADMOB:
                    case FAN_BIDDING_ADMOB:
                        AdRequest adRequest = new AdRequest.Builder().build();
                        com.google.android.gms.ads.appopen.AppOpenAd.load(activity, adMobAppOpenId, adRequest, new com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback() {
                            @Override
                            public void onAdLoaded(@NonNull com.google.android.gms.ads.appopen.AppOpenAd ad) {
                                appOpenAd = ad;
                                showBackupAppOpenAd(onShowAdCompleteListener);
                                Log.d(TAG, "[" + backupAdNetwork + "] " + "[on start] [backup] app open ad loaded");
                            }

                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                appOpenAd = null;
                                showBackupAppOpenAd(onShowAdCompleteListener);
                                Log.d(TAG, "[" + backupAdNetwork + "] " + "[on start] [backup] failed to load app open ad: " + loadAdError.getMessage());
                            }
                        });
                        break;

                    case GOOGLE_AD_MANAGER:
                    case FAN_BIDDING_AD_MANAGER:
                        @SuppressLint("VisibleForTests") AdManagerAdRequest adManagerAdRequest = new AdManagerAdRequest.Builder().build();
                        com.google.android.gms.ads.appopen.AppOpenAd.load(activity, adManagerAppOpenId, adManagerAdRequest, new com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback() {
                            @Override
                            public void onAdLoaded(@NonNull com.google.android.gms.ads.appopen.AppOpenAd ad) {
                                appOpenAd = ad;
                                showBackupAppOpenAd(onShowAdCompleteListener);
                                Log.d(TAG, "[" + backupAdNetwork + "] " + "[on start] [backup] app open ad loaded");
                            }

                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                appOpenAd = null;
                                showBackupAppOpenAd(onShowAdCompleteListener);
                                Log.d(TAG, "[" + backupAdNetwork + "] " + "[on start] [backup] failed to load app open ad: " + loadAdError.getMessage());
                            }
                        });
                        break;

                    default:
                        onShowAdCompleteListener.onShowAdComplete();
                        break;
                }
            } else {
                onShowAdCompleteListener.onShowAdComplete();
            }
        }

        public void showBackupAppOpenAd(OnShowAdCompleteListener onShowAdCompleteListener) {
            switch (backupAdNetwork) {
                case ADMOB:
                case FAN_BIDDING_ADMOB:
                case GOOGLE_AD_MANAGER:
                case FAN_BIDDING_AD_MANAGER:
                    if (appOpenAd != null) {
                        appOpenAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                appOpenAd = null;
                                onShowAdCompleteListener.onShowAdComplete();
                                Log.d(TAG, "[" + backupAdNetwork + "] " + "[on start] [backup] close app open ad");
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                appOpenAd = null;
                                onShowAdCompleteListener.onShowAdComplete();
                                Log.d(TAG, "[" + backupAdNetwork + "] " + "[on start] [backup] failed to show app open ad: " + adError.getMessage());
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                Log.d(TAG, "[" + backupAdNetwork + "] " + "[on start] [backup] show app open ad");
                            }
                        });
                        appOpenAd.show(activity);
                    } else {
                        onShowAdCompleteListener.onShowAdComplete();
                    }
                    break;

                default:
                    onShowAdCompleteListener.onShowAdComplete();
                    break;
            }
        }

        public void loadBackupAppOpenAd() {
            if (adStatus.equals(AD_STATUS_ON)) {
                switch (backupAdNetwork) {
                    case ADMOB:
                    case FAN_BIDDING_ADMOB:
                        AdRequest adRequest = new AdRequest.Builder().build();
                        com.google.android.gms.ads.appopen.AppOpenAd.load(activity, adMobAppOpenId, adRequest, new com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback() {
                            @Override
                            public void onAdLoaded(@NonNull com.google.android.gms.ads.appopen.AppOpenAd ad) {
                                appOpenAd = ad;
                                isAppOpenAdLoaded = true;
                                Log.d(TAG, "[" + backupAdNetwork + "] " + "[on resume] [backup] app open ad loaded");
                            }

                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                appOpenAd = null;
                                isAppOpenAdLoaded = false;
                                loadBackupAppOpenAd();
                                Log.d(TAG, "[" + backupAdNetwork + "] " + "[on resume] [backup] failed to load app open ad : " + loadAdError.getMessage());
                            }
                        });
                        break;

                    case GOOGLE_AD_MANAGER:
                    case FAN_BIDDING_AD_MANAGER:
                        @SuppressLint("VisibleForTests") AdManagerAdRequest adManagerAdRequest = new AdManagerAdRequest.Builder().build();
                        com.google.android.gms.ads.appopen.AppOpenAd.load(activity, adManagerAppOpenId, adManagerAdRequest, new com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback() {
                            @Override
                            public void onAdLoaded(@NonNull com.google.android.gms.ads.appopen.AppOpenAd ad) {
                                appOpenAd = ad;
                                isAppOpenAdLoaded = true;
                                Log.d(TAG, "[" + backupAdNetwork + "] " + "[on resume] [backup] app open ad loaded");
                            }

                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                appOpenAd = null;
                                isAppOpenAdLoaded = false;
                                loadBackupAppOpenAd();
                                Log.d(TAG, "[" + backupAdNetwork + "] " + "[on resume] [backup] failed to load app open ad : " + loadAdError.getMessage());
                            }
                        });
                        break;

                    default:
                        break;
                }
            }
        }

        public void showBackupAppOpenAd() {
            switch (backupAdNetwork) {
                case ADMOB:
                case FAN_BIDDING_ADMOB:
                case GOOGLE_AD_MANAGER:
                case FAN_BIDDING_AD_MANAGER:
                    if (appOpenAd != null) {
                        appOpenAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                appOpenAd = null;
                                loadBackupAppOpenAd();
                                Log.d(TAG, "[" + backupAdNetwork + "] " + "[on resume] [backup] close app open ad");
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                appOpenAd = null;
                                loadBackupAppOpenAd();
                                Log.d(TAG, "[" + backupAdNetwork + "] " + "[on resume] [backup] failed to show app open ad: " + adError.getMessage());
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                Log.d(TAG, "[" + backupAdNetwork + "] " + "[on resume] [backup] show app open ad");
                            }
                        });
                        appOpenAd.show(activity);
                    }
                    break;

                default:
                    break;
            }
        }

    }

}
