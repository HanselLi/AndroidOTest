package com.example.liyangos3323.androidotest;

import android.app.Activity;
import android.app.AppOpsManager;
import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private WebView web;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.img);
        Intent intent = getIntent();
        if (intent != null) {
            String lala = (String) intent.getSerializableExtra("lala");
        }
        web = (WebView) findViewById(R.id.webview);
        web.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("intent://")) {
                    Intent intent;
                    try {
                        intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
                        intent.setComponent(null);
                        intent.setSelector(null);
                        List<ResolveInfo> resolveInfos = getPackageManager().queryIntentActivities(intent, 0);
                        Log.d("tag", "start actvirty --- " + resolveInfos.size());
                        if (resolveInfos.size() > 0) {
                            startActivityIfNeeded(intent, -1);
                            Log.d("tag", "start actvirty --- ");
                        }
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    return true;
                }
                view.loadUrl(url);
                return true;
            }
        });
        web.loadUrl("http://www.baidu.com");
        Log.d("tag", "" + (1 << 1 & (1 << 0 | 1 << 1)));
    }


    public static String replacer(StringBuffer outBuffer) {
        String data = outBuffer.toString();
        try {
            data = data.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
            data = data.replaceAll("\\+", "%2B");
            data = URLDecoder.decode(data, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static boolean checkAppExist(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return false;
        }
        try {
            context.getPackageManager().getApplicationInfo(
                    packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static void goTo8848AppManager(Context context) {
        if (checkAppExist(context, "com.sina.weibo")) {
            /*try {
                Intent intent = new Intent();
                intent.setClassName(APPNAME, APP_COMPONENT);
                context.startActivity(intent);
            } catch (Exception e) {

            }*/
            Intent launchIntentForPackage = context.getPackageManager().getLaunchIntentForPackage("com.tencent.mtt");
            context.startActivity(launchIntentForPackage);
        } else {
            Toast.makeText(context, "应用未安装", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 使用协议点击跳转app
     **/
    String url = "intent://scan/#Intent;scheme=liyang;package=com.example.liyangos3323.testme;end";

    // 原理都是设置action_View 携带data：scheme等
    public void click(View view) {
        /**
         * 方法一  intent:// 协议
         * */
        Intent intent = new Intent();
        try {
            /**
             * 这个类型的按照文档的规则进行解析
             **/
//             intent = Intent.parseUri("intent://scan/#Intent;scheme=liyang;package=com.example.liyangos3323.testme;end", Intent.URI_INTENT_SCHEME);
            /**
             *这个类型的按照文档的规则进行解析
             */
            String s2 = "android-app://com.ebensz.appmanager/#Intent;component=com.ebensz.appmanager/com.ebensz.appmanager.MainActivity;S.PushPage=list;" + "S.ListType=topic;S.ListName=腾讯系列游戏;" +
                    "S.TopicLogo=http://appmarket.ebensz.com/repo/newrepo/block/1213block31.jpg;end";
            intent = Intent.parseUri(s2, Intent.URI_ANDROID_APP_SCHEME);
            /**
             *这个自定义的协议：scheme是冒号 ：前面的东西，需要响应的app在manifest中intent_filter注册下对应的scheme即可
             */
//            intent = Intent.parseUri("dianping://tuandeal?id=25023058&_fb_=&utm=w_mtuan_auto:default:Chrome:m&utm_source=467b277b-ac3e-0180-d463-54f32cd86c2f.1512023403",Intent.URI_INTENT_SCHEME);
        } catch (Exception e) {
            Log.d("liyang", "exception is == " + e.getMessage());
            e.printStackTrace();
        }
        List<ResolveInfo> resolveInfos = getPackageManager().queryIntentActivities(intent, 0);
        if (resolveInfos != null && resolveInfos.size() > 0) {
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            startActivityIfNeeded(intent, -1);
//            web.loadUrl(url);
        } else {
            Toast.makeText(this, "没有找到对应的Activity", Toast.LENGTH_SHORT).show();
        }
        /**
         * 方法二 指定component
         **/
       /* Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setClassName("com.example.liyangos3323.testme", "com.example.liyangos3323.testme.MainActivity");
        startActivity(intent);*/
        /**
         * 方法三 自定义action
         */
        /*Intent intent = new Intent("myAction");
        startActivity(intent);*/
        showSystemNotifycation(this, Intent.ACTION_VIEW, "下载管理", "8848太和资金手机8848太和资金手机8848太和资金手机8848太和资金手机", BitmapFactory.decodeResource(getResources(), R.mipmap.weather_thundershowerhail),
                11);
    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    private void makeShortCutOnO() {
        ShortcutManager shortcutManager = (ShortcutManager) getSystemService(Context.SHORTCUT_SERVICE);
        ArrayList<ShortcutInfo> list = new ArrayList<>();
        Intent scIntent = new Intent(this, Main2Activity.class);
        scIntent.setAction(Intent.ACTION_VIEW);
        ShortcutInfo shortcutInfo = new ShortcutInfo.Builder(this, "fuck")
                .setIcon(Icon.createWithBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.weather_heavysnowstorm)))
                .setShortLabel("fuck")
                .setIntent(scIntent)
                .build();
        list.add(shortcutInfo);
        shortcutManager.addDynamicShortcuts(list);
    }

    private void removeItemUsingForEach() {
        List<String> list = new ArrayList();
        list.add("1");
        list.add("2");
        for (String l : list) {
            if ("1".equals(l)) {
                list.remove(l);
            }
        }

        for (int i = 0; i < list.size(); i++) {
            Log.d("tag", list.get(i));
        }
    }

    private void removeByIterator() {
        List<String> list = new ArrayList();
        list.add("1");
        list.add("2");
        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            String next = iterator.next();
            if (next.equals("1")) {
                iterator.remove();
            }
        }
        for (int i = 0; i < list.size(); i++) {
            Log.d("tag", list.get(i));
        }
    }


    public static String string2Unicode(String string) {

        StringBuffer unicode = new StringBuffer();

        for (int i = 0; i < string.length(); i++) {

            // 取出每一个字符
            char c = string.charAt(i);

            // 转换为unicode
            unicode.append("\\u" + Integer.toHexString(c));
        }

        return unicode.toString();
    }

    private static void showSystemNotifycation(Context context, String action, String title, String msg, Bitmap iconBitmap,
                                               int notificationId) {
        try {
            Intent intent = new Intent(action);
            intent.putExtras(new Bundle());
            PendingIntent pendingIntent = PendingIntent
                    .getBroadcast(context, 0, intent,
                            PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            Notification n = new NotificationCompat.Builder(context)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setContentTitle(title)
                    .setContentText(msg)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(msg).setBigContentTitle(title))
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setSmallIcon(R.mipmap.push)
                    .setProgress(100, 40, true)
                    .setContentIntent(pendingIntent)
                    .build();
            nm.notify(notificationId, n);
        } catch (Exception e) {
        }
       /* String url = "http://mse.sogou.com/app/bazinga/ad.php?source=dh&adUrl=http%3a%2f%2frd%2ee%2esogou%2ecom%2fck%3fst%3dtX%252Ff6FVcfrgs4wbA9UqwehLPN%252FtMDeFqBY9VNncLzI3AJwnd2G%252BwUi6Gf%252F1uk6M1BUW28HmFTzQd2wA7DB4Bh43TN2mKGFlfRCXsanJB11yf5OsZf7RN%252FNVHkXzTEpafiGLDQcIgFPafvgGP%252FqDet7%252BLTbO2nzawTx%252BfZSDgfCM%253D%26ch%3d122044001%26fp%3daa7aeec2c3fe69633ee7b7d895756c62%26url%3dhttp%253a%252f%252fadstream%252e123%252esogoucdn%252ecom%252flst%252fpkg%252f68_1512532381344%252eapk%253fsemob_jump%253dfalse%26deviceId%3d00000000%2d302d%2d4d47%2db226%2defda0033c587%26posid%3d42%26semob_jump%3dfalse&v=5.4.0&h=00000000-302d-4d47-b226-efda0033c587&groupidx=3&semob_jump=false";
        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle("下载管理");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "test");
        long id = manager.enqueue(request);*/
       if (isNotificationEnabled(context)){
            sendNotificationMessage(context, "同步成功", R.mipmap.push_transparent);
        }else {
           Toast.makeText(context,"通知被禁止",Toast.LENGTH_SHORT).show();
           Intent settings = new Intent(Settings.ACTION_SETTINGS);

       }
    }

    public static void showDownloadNotification(Context context, String msg) {
        NotificationManager mn = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
//        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(msg));
        builder.setSmallIcon(R.mipmap.push);
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setPriority(Notification.PRIORITY_HIGH);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.download_notification);
        remoteViews.setTextViewText(R.id.contentTv, "下载中......");
        remoteViews.setTextViewText(R.id.progressTv, "33%");
        remoteViews.setProgressBar(R.id.progress, 100, 55, false);
        remoteViews.setImageViewResource(R.id.img, R.mipmap.notification_small_icon_downloading);
        builder.setContent(remoteViews);
        Notification notification = builder.build();
        mn.notify(11, notification);

    }

    public static void sendNotificationMessage(Context context, CharSequence text, int icon) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(icon)
                .setWhen(System.currentTimeMillis())
                .setTicker(text);
//        Notification notification = new Notification();
//        notification.icon = icon;
//        notification.when = System.currentTimeMillis();
//        notification.tickerText = text;
        if (Build.VERSION.SDK_INT > 17) {
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(text));
        }
        Intent intent = new Intent(context, context.getClass());
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        builder.setContentIntent(pendingIntent);
        Notification no = builder.build();
        no.flags |= Notification.FLAG_AUTO_CANCEL;
//        notification.setLatestEventInfo(context, text, text, pendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(22, no);
    }

    private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";
    private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

    public static boolean isNotificationEnabled(Context context) {

        AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);

        ApplicationInfo appInfo = context.getApplicationInfo();

        String pkg = context.getApplicationContext().getPackageName();

        int uid = appInfo.uid;

        Class appOpsClass = null; /* Context.APP_OPS_MANAGER */

        try {

            appOpsClass = Class.forName(AppOpsManager.class.getName());

            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE, String.class);

            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);
            int value = (int) opPostNotificationValue.get(Integer.class);

            return ((int) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }
}
