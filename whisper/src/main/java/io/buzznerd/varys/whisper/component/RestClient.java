package io.buzznerd.varys.whisper.component;

import android.content.Context;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.io.IOException;
import java.lang.reflect.Field;
import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.buzznerd.varys.whisper.Whisper;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created on 16/10/5
 *
 * @author Xingye
 * @since 1.0.0
 */

public final class RestClient {

    /**
     * response code: success
     */
    public static final String RESPONSE_CODE_OK = "0000";

    private static final int CONNECT_TIMEOUT = 6; // seconds

    private static ApiService sApiService;

    private RestClient() {

    }

    public static ApiService getApiService(final Context context, Whisper.LogLevel logLevel) {

        if (sApiService != null) {
            return sApiService;
        }

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder = builder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);

        Interceptor headerInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                String token = "96179ce8939c4cdfacba65baab1d5ff8";
                Request languageRequest = chain.request().newBuilder()
                        .header("Authorization", token)
                        .build();
                return chain.proceed(languageRequest);
            }
        };
        builder.addInterceptor(headerInterceptor);

        if (logLevel == Whisper.LogLevel.FULL) {
            builder.addNetworkInterceptor(new StethoInterceptor());
        }
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        if (logLevel == Whisper.LogLevel.FULL) {
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        }
        builder.addInterceptor(interceptor);

        // no https validation
        OkHttpClient sClient = builder.build();

        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType)
                        throws java.security.cert.CertificateException {

                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType)
                        throws java.security.cert.CertificateException {

                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            }}, new SecureRandom());

            HostnameVerifier hv1 = new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
            String workerClassName = "okhttp3.OkHttpClient";
            Class workerClass = Class.forName(workerClassName);
            Field hostnameVerifier = workerClass.getDeclaredField("hostnameVerifier");
            hostnameVerifier.setAccessible(true);
            hostnameVerifier.set(sClient, hv1);

            Field sslSocketFactory = workerClass.getDeclaredField("sslSocketFactory");
            sslSocketFactory.setAccessible(true);
            sslSocketFactory.set(sClient, sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WhisperConfig.getServerUrl(context))
                .addConverterFactory(GsonConverterFactory.create())
                .client(sClient)
                .build();
        sApiService = retrofit.create(ApiService.class);
        return sApiService;
    }

}
