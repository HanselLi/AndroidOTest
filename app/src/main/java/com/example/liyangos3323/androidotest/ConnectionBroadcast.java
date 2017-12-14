package com.example.liyangos3323.androidotest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by liyangos3323 on 2017/11/10.
 */

public class ConnectionBroadcast extends BroadcastReceiver {
    private static final String TAG = "lilei";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null){
            return;
        }
        String action = intent.getAction();
        Log.d(TAG,action);
        switch (action){
            case WifiManager.WIFI_STATE_CHANGED_ACTION:
                dealWifiState(intent);
                break;
            case WifiManager.NETWORK_STATE_CHANGED_ACTION:
                dealWifiNetState(intent);
                break;
            case ConnectivityManager.CONNECTIVITY_ACTION:
                dealConnectivityState(intent,context);
                break;
            default:
        }
    }

    private void dealConnectivityState(Intent intent,Context context) {
        /*ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()){
            if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE){
                Log.d("lala"," =TYPE_MOBILE== ");
            }else if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI){
                Log.d("lala"," =TYPE_WIFI== ");
            }
        }*/
        NetworkInfo info = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
        boolean connected = info.isConnected();
        if (connected){
            if (isMobileConnected(context)){
                Log.d("lala","isMobileConnected");
            }else if (isWifiConnected(context)){
                Log.d("lala","isWifiConnected");
            }
        }
    }
    // wifi is real connected , and will receive this when wifi state is enabled
    /*
    * 这个监听wifi的连接状态即是否连上了一个有效无线路由，当上边广播的状态是WifiManager.WIFI_STATE_DISABLING，
    * 和WIFI_STATE_DISABLED的时候，根本不会接到这个广播。在上边广播接到广播是WifiManager.WIFI_STATE_ENABLED状态
    * 的同时也会接到这个广播，
    * 当然刚打开wifi肯定还没有连接到有效的无线
    * */
    private void dealWifiNetState(Intent intent) {
        Parcelable netWorkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
        if (null != netWorkInfo) {
            NetworkInfo info = (NetworkInfo) netWorkInfo;
            NetworkInfo.State state = info.getState();
            if(state == NetworkInfo.State.CONNECTED){
                Log.d(TAG,"dealWifiNetState " + state.name());
            }
        }
    }
    // wifi open or close state
    private void dealWifiState(Intent intent) {
        int intExtra = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
        switch (intExtra){
            case WifiManager.WIFI_STATE_DISABLED:
                Log.d(TAG,intExtra+" ==WIFI_STATE_DISABLED== ");
                break;
            case WifiManager.WIFI_STATE_DISABLING:
                Log.d(TAG,intExtra+" ==WIFI_STATE_DISABLING== ");
                break;
            case WifiManager.WIFI_STATE_ENABLING:
                Log.d(TAG,intExtra+" ==WIFI_STATE_ENABLING== ");
                break;
            case WifiManager.WIFI_STATE_ENABLED:
                Log.d(TAG,intExtra+" ==WIFI_STATE_ENABLED== ");
                break;
            case WifiManager.WIFI_STATE_UNKNOWN:
                Log.d(TAG,intExtra+" ==WIFI_STATE_UNKNOWN== ");
                break;
            default:
        }
    }

    // to judge whether aviiable network is mobile or wifi
    public static int connectedStatusWifiOrMobile(Context context){
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
        boolean b = activeNetworkInfo.isAvailable() && activeNetworkInfo.isConnected();
        if (b){
            if (activeNetworkInfo.getType() ==ConnectivityManager.TYPE_MOBILE){
                Log.d(TAG,"TYPE_MOBILE" + activeNetworkInfo.getExtraInfo());
            }else if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI){
                Log.d(TAG,"TYPE_WIFI" + activeNetworkInfo.getExtraInfo());
            }
        }else {
            Log.d(TAG,"NO netWork avaliable ");
        }
        return -1;
    }

    public static boolean isWifiConnected(Context context) {
        boolean isWifiConnected = false;
        try {
            if (context != null) {
                ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mWiFiNetworkInfo = mConnectivityManager
                        .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                if (mWiFiNetworkInfo != null) {
                    isWifiConnected = mWiFiNetworkInfo.isConnected() && mWiFiNetworkInfo.isAvailable();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isWifiConnected;
    }

    /**
     * indicates whether mobile connectivity is available.
     */
    public static boolean isMobileConnected(Context context) {
        boolean isMobileConnected = false;
        try {
            if (context != null) {
                ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mMobileNetworkInfo = mConnectivityManager
                        .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                if (mMobileNetworkInfo != null) {
                    isMobileConnected = mMobileNetworkInfo.isConnected();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isMobileConnected;
    }
}
