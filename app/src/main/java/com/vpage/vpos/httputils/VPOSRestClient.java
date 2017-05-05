package com.vpage.vpos.httputils;

import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.vpage.vpos.tools.VTools;
import com.vpage.vpos.tools.utils.LogFlag;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class VPOSRestClient {

    private static final String TAG =  VPOSRestClient.class.getName();

    private static final String CONTENT_TYPE_JSON = "application/json";

    JSONObject jsonParams = null;
    StringEntity parsedJsonParams = null;

    RequestParams requestParams = null;


    public static void cancelAllRequests() {
        HttpManager.asyncHttpClient.cancelAllRequests(true);
        HttpManager.syncHttpClient.cancelAllRequests(true);

    }

    public boolean doAction(final String url, String httpMethod, Object request) throws UnsupportedEncodingException {
        final boolean[] response = new boolean[1];
        if (httpMethod.equals(HttpMethod.GET)) {
            HttpManager.get(url, null, new JsonHttpResponseHandler() {


                public void onSuccess(int statusCode, Header[] headers, JSONObject resultData) {
                    response[0] = true;
                }


                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    if (LogFlag.bLogOn)Log.d(TAG, "onFailure: GET REST Call: " + url + " Failed");
                    response[0] = false;
                }
            });
        } else if (httpMethod.equals(HttpMethod.POST)) {
            StringEntity parsedJsonParams = null;
            if (request != null) {
                Gson gson = new GsonBuilder().create();
                String jsonParams = gson.toJson(request);
                parsedJsonParams = new StringEntity(VTools.getRequestWithAppVersion(jsonParams));

            }
            HttpManager.post(url, parsedJsonParams, new JsonHttpResponseHandler() {

                public void onSuccess(int statusCode, Header[] headers, JSONObject resultData) {
                    response[0] = true;
                }


                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    if (LogFlag.bLogOn)Log.d(TAG, "onFailure: POST REST Call: " + url + " Failed");
                    response[0] = false;
                }

            });
        } else if (httpMethod.equals(HttpMethod.PUT)) {
            HttpManager.put(url, null, new JsonHttpResponseHandler() {

                public void onSuccess(int statusCode, Header[] headers, JSONObject resultData) {
                    response[0] = true;
                }


                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    if (LogFlag.bLogOn)Log.d(TAG, "onFailure: PUT REST Call: " + url + " Failed");
                    response[0] = false;
                }
            });
        }
        return response[0];
    }




}

