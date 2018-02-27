package com.srishti.wallpaperclient.pixabay.service;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.srishti.wallpaperclient.MainActivity;
import com.srishti.wallpaperclient.pixabay.event.ErrorEvent;
import com.srishti.wallpaperclient.pixabay.event.EventManager;
import com.srishti.wallpaperclient.pixabay.model.PixabayResponse;

import java.io.IOException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.TlsVersion;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.srishti.wallpaperclient.pixabay.util.Constatns.Pixabay.PIXABAY_BASE_URL;


public class PixabayServiceProvider {
    private static final String TAG = MainActivity.class.getSimpleName();

    private PixabayService pixabayService;
    private EventManager eventManager;

    private PixabayServiceProvider() {
        final TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {

                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }
                }
        };


        try {

            ConnectionSpec spec = new
                    ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                    .tlsVersions(TlsVersion.TLS_1_2)
                    .cipherSuites(
                            CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                            CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                            CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256)
                    .build();

            OkHttpClient.Builder client = new OkHttpClient.Builder();
            client.connectionSpecs(Collections.singletonList(spec));

            Gson gson = new GsonBuilder().setLenient().create();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(PIXABAY_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client.build())
                    .build();
            pixabayService = retrofit.create(PixabayService.class);

        } catch (Exception e) {

        }
   /*     HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);*/


        this.eventManager = EventManager.getInstance();
    }

    private static class Singleton {
        private static final PixabayServiceProvider INSTANCE = new PixabayServiceProvider();
    }

    public static PixabayServiceProvider getInstance() {
        return Singleton.INSTANCE;
    }

    public void imageSearch(String keywords) {
        Call<PixabayResponse> call = pixabayService.imageSearch(keywords);
        call.enqueue(imageSearchCallback);
    }

    public void editorsChoice() {
        Call<PixabayResponse> call = pixabayService.editorsChoice();
        call.enqueue(imageSearchCallback);
    }

    public void flower() {
        Call<PixabayResponse> call = pixabayService.flower();
        call.enqueue(imageSearchCallback);
    }

    public void painting() {
        Call<PixabayResponse> call = pixabayService.painting();
        call.enqueue(imageSearchCallback);
    }

    public void nature() {
        Call<PixabayResponse> call = pixabayService.nature();
        call.enqueue(imageSearchCallback);
    }

    public void abstractArt() {
        Call<PixabayResponse> call = pixabayService.abstractArt();
        call.enqueue(imageSearchCallback);
    }

    private Callback imageSearchCallback = new Callback<PixabayResponse>() {
        @Override
        public void onResponse(Call<PixabayResponse> call, Response<PixabayResponse> response) {
            int statusCode = response.code();
            if (statusCode == 200) {
                PixabayResponse pixabayResponse = response.body();
                eventManager.postImageSearchResult(pixabayResponse);
            } else {
                try {
                    eventManager.postErrorEvent(new ErrorEvent(response.errorBody().string()));
                } catch (IOException e) {
                    eventManager.postErrorEvent(new ErrorEvent(e.getMessage()));
                }
            }
        }

        @Override
        public void onFailure(Call<PixabayResponse> call, Throwable t) {
            eventManager.postErrorEvent(new ErrorEvent("Unable to perform image search"));
        }
    };
}
