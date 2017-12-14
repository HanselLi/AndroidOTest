package com.example.liyangos3323.androidotest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import javax.net.ssl.TrustManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Main2Activity extends AppCompatActivity {
 // respond code is 404 maybe the certificate 's matter , ignore this, just have achieved the intention to override
    // trustManager and host verifier to load a https url with a  custom cert
    private OkHttpClient okHttpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
       /* try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManager[] trustManagers = {new MyTrustManager()};
            sslContext.init(null,trustManagers,new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new MyHostVerifier());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }*/
        initSSLContext();
    }

    public static final String TEST_URL = "https://dynamic.12306.cn/otsweb";

    public void click2(View view) {
        /*new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    URL url = new URL(TEST_URL);
                    HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                    connection.setReadTimeout(1000);
                    connection.setConnectTimeout(6000);
                    connection.connect();
                    Log.d("tag", " code  " + connection.getResponseCode());
                    if (connection.getResponseCode() == 200) {

                    }
                } catch (Exception e) {
                    Log.d("tag", " error msg  " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }.start();*/
        try {
            okHttpClient.newCall(new Request.Builder().url(TEST_URL).build()).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("tag", "ok failure " + e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    int code = response.code();
                    Log.d("tag", " respond code is ==  " + code);
                }
            });
        } catch (Exception e) {
            Log.d("tag", " error  is ==  " + e.getMessage());
            e.printStackTrace();
        }
    }

    private KeyStore getCAKeyStore() {
        InputStream is = null;
        try {
            //读取证书
            is = getAssets().open("srca.cer");
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            Certificate certificate = certificateFactory.generateCertificate(is);
            // 创建证书库
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);//不设置密码
            keyStore.setCertificateEntry("trust", certificate);//设置证书，账户名“trust”,
            return keyStore;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private Certificate getServerCert() {
        InputStream is = null;
        try {
            //读取证书
            is = getAssets().open("srca.cer");
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            Certificate certificate = certificateFactory.generateCertificate(is);
            // 创建证书库
            return certificate;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


    private void initSSLContext() {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(getCAKeyStore());
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            sslContext.init(null, new TrustManager[]{trustManagers[0]}, new SecureRandom()); // last one param may be null
//            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            okHttpClient = new OkHttpClient.Builder().sslSocketFactory(sslContext.getSocketFactory()).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class MyTrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
           /* if (chain == null) {
                throw new IllegalArgumentException("check server certificate is null");
            }
            if (chain.length < 0) {
                throw new IllegalArgumentException("check server certificate is empty");
            }
            for (X509Certificate cert : chain) {
                cert.checkValidity();// 校验证书有效
                try {
                    cert.verify(getServerCert().getPublicKey());
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (NoSuchProviderException e) {
                    e.printStackTrace();
                } catch (SignatureException e) {
                    e.printStackTrace();
                }
            }*/
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }


    class MyHostVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

}
