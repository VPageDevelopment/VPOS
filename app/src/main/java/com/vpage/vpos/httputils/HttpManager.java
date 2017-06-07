package com.vpage.vpos.httputils;

import android.os.Looper;
import android.util.Log;
import com.loopj.android.http.*;
import com.vpage.vpos.tools.VPOSApplication;
import com.loopj.android.http.RequestParams;
import com.vpage.vpos.tools.utils.LogFlag;

import java.security.KeyStore;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.StringEntity;

public class HttpManager {

    private static final String TAG = HttpManager.class.getName();
    private static int DEFAULT_TIMEOUT = 20 * 1000;

    // release api
/*    private static final String BASE_URL_SIGNIN = "http://vpageinc.com/pos/public_html/";
    private static final String BASE_URL = "http://vpageinc.com/pos/public_html/api/v1/";*/

    // developer api
    private static final String BASE_URL_SIGNIN = "http://vpageinc.com/pos/public_html/";
    private static final String BASE_URL = "http://vpageinc.com/pos/public_html/api/v1/";

    private static final String CONTENT_TYPE_JSON = "application/json";

    // A SyncHttpClient is an AsyncHttpClient
    public static AsyncHttpClient syncHttpClient = new SyncHttpClient(true, 80, 8443);
    public static AsyncHttpClient asyncHttpClient = new AsyncHttpClient(true, 80, 8443);

    public static void setCookieStore(PersistentCookieStore cookieStore) {
        getClient().setCookieStore(cookieStore);
    }

    public static void setBasicAuthData(String userName,String password) {
        getClient().setBasicAuth(userName,password);
    }

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        getClient().get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void postSignIn(String url, StringEntity parsedJsonParams, AsyncHttpResponseHandler responseHandler) {
        getClient().post(VPOSApplication.getContext(), getAbsoluteSignInUrl(url), parsedJsonParams, CONTENT_TYPE_JSON, responseHandler);
    }

    public static void post(String url, StringEntity parsedJsonParams, AsyncHttpResponseHandler responseHandler) {
        getClient().post(VPOSApplication.getContext(), getAbsoluteUrl(url), parsedJsonParams, CONTENT_TYPE_JSON, responseHandler);
    }

    public static void putMethod(String url, StringEntity parsedJsonParams, AsyncHttpResponseHandler responseHandler) {
        getClient().put(VPOSApplication.getContext(), getAbsoluteUrl(url), parsedJsonParams, CONTENT_TYPE_JSON, responseHandler);
    }

    public static void put(String url, RequestParams params, ResponseHandlerInterface responseHandler) {
        getClient().put(VPOSApplication.getContext(), getAbsoluteUrl(url), params, responseHandler);
    }

    public static void putWithEntity(String url, HttpEntity httpEntity, ResponseHandlerInterface responseHandler) {
        getClient().put(VPOSApplication.getContext(), getAbsoluteUrl(url), httpEntity, CONTENT_TYPE_JSON, responseHandler);
    }


    public static void delete(String url, ResponseHandlerInterface responseHandler) {
        getClient().delete(VPOSApplication.getContext(), getAbsoluteUrl(url), responseHandler);
    }

    /**
     * @return an async client when calling from the main thread, otherwise a sync client.
     */
    public static AsyncHttpClient getClient() {
        // Return the synchronous HTTP client when the thread is not prepared
        if (Looper.myLooper() == null) {
            syncHttpClient.setTimeout(DEFAULT_TIMEOUT);
            syncHttpClient.setConnectTimeout(DEFAULT_TIMEOUT);
            syncHttpClient.setResponseTimeout(DEFAULT_TIMEOUT);

         /*   // SSL
            VPOSSSLSocketFactory vpossslSocketFactory = null;
            try {

                KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                trustStore.load(null, null);
                vpossslSocketFactory = new VPOSSSLSocketFactory(trustStore);
                vpossslSocketFactory.setHostnameVerifier(VPOSSSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            } catch (Exception ex) {
                Log.e(TAG, ex.toString());
            }
            syncHttpClient.setSSLSocketFactory(vpossslSocketFactory);*/

            return syncHttpClient;
        }

        asyncHttpClient.setTimeout(DEFAULT_TIMEOUT);
        asyncHttpClient.setConnectTimeout(DEFAULT_TIMEOUT);
        asyncHttpClient.setResponseTimeout(DEFAULT_TIMEOUT);

      /*  // SSL
        VPOSSSLSocketFactory vpossslSocketFactory = null;
        try {

            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            vpossslSocketFactory = new VPOSSSLSocketFactory(trustStore);
            vpossslSocketFactory.setHostnameVerifier(VPOSSSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
        }
        asyncHttpClient.setSSLSocketFactory(vpossslSocketFactory);*/

        return asyncHttpClient;
    }


    private static String getAbsoluteSignInUrl(String relativeUrl) {
        if (LogFlag.bLogOn)Log.d(TAG, BASE_URL_SIGNIN + relativeUrl);
        return BASE_URL_SIGNIN + relativeUrl;
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        if (LogFlag.bLogOn)Log.d(TAG, BASE_URL + relativeUrl);
        return BASE_URL + relativeUrl;
    }

}
