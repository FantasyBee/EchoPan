package com.echopan.utils;

import com.echopan.entity.enums.ResponseCodeEnum;
import com.echopan.exception.BusinessException;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class OKHttpUtils {
    /**
     * 请求超时时间5秒
     */
    private static final int TIME_OUT_SECONDS = 8;

    private static Logger logger = LoggerFactory.getLogger(OKHttpUtils.class);

    private static OkHttpClient.Builder getClientBuilder() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder().followRedirects(false).retryOnConnectionFailure(false);
        clientBuilder.connectTimeout(TIME_OUT_SECONDS, TimeUnit.SECONDS).readTimeout(TIME_OUT_SECONDS, TimeUnit.SECONDS);
        return clientBuilder;
    }

    private static Request.Builder getRequestBuilder(Map<String, String> header) {
        Request.Builder requestBuilder = new Request.Builder();
        if (null != header) {
            for (Map.Entry<String, String> map : header.entrySet()) {
                String key = map.getKey();
                String value;
                if (map.getValue() == null) {
                    value = "";
                } else {
                    value = map.getValue();
                }
                requestBuilder.addHeader(key, value);
            }
        }
        return requestBuilder;
    }

    private static FormBody.Builder getBuilder(Map<String, String> params) {
        FormBody.Builder builder = new FormBody.Builder();
        if (params == null) {
            return builder;
        }
        for (Map.Entry<String, String> map : params.entrySet()) {
            String key = map.getKey();
            String value;
            if (map.getValue() == null) {
                value = "";
            } else {
                value = map.getValue();
            }
            builder.add(key, value);
        }
        return builder;
    }

    public static String getRequest(String url) throws BusinessException {
        ResponseBody responseBody = null;
        try {
            OkHttpClient.Builder clientBuilder = getClientBuilder();
            Request.Builder requestBuilder = getRequestBuilder(null);
            OkHttpClient client = clientBuilder.build();
            Request request = requestBuilder.url(url).build();
            Response response = client.newCall(request).execute();
            responseBody = response.body();
            String responseStr = responseBody.string();
            logger.info("postRequest请求地址:{},返回信息:{}", url, responseStr);
            return responseStr;
        } catch (SocketTimeoutException | ConnectException e) {
            logger.error("OKhttp POST 请求超时,url:{}", url, e);
            throw new BusinessException(ResponseCodeEnum.CODE_500);
        } catch (Exception e) {
            logger.error("OKhttp GET 请求异常", e);
            return null;
        } finally {
            if (responseBody != null) {
                responseBody.close();
            }
        }
    }

    public static String postRequest(String url, Map<String, String> params) throws BusinessException {
        ResponseBody body = null;
        try {
            if (params == null) {
                params = new HashMap<>();
            }
            OkHttpClient.Builder clientBuilder =
                    new OkHttpClient.Builder().followRedirects(false).retryOnConnectionFailure(false);
            clientBuilder.connectTimeout(TIME_OUT_SECONDS, TimeUnit.SECONDS).readTimeout(TIME_OUT_SECONDS, TimeUnit.SECONDS);
            OkHttpClient client = clientBuilder.build();
            FormBody.Builder builder = new FormBody.Builder();
            RequestBody requestBody = null;
            for (Map.Entry<String, String> map : params.entrySet()) {
                String key = map.getKey();
                String value;
                if (map.getValue() == null) {
                    value = "";
                } else {
                    value = map.getValue();
                }
                builder.add(key, value);
            }
            requestBody = builder.build();

            Request.Builder requestBuilder = new Request.Builder();
            Request request = requestBuilder.url(url).post(requestBody).build();
            Response response = client.newCall(request).execute();
            body = response.body();
            String responseStr = body.string();
            logger.info("postRequest请求地址:{},参数:{},返回信息:{}", url, JsonUtils.convertObj2Json(params), responseStr);
            return responseStr;
        } catch (SocketTimeoutException | ConnectException e) {
            logger.error("OKhttp POST 请求超时,url:{},请求参数：{}", url, JsonUtils.convertObj2Json(params), e);
            throw new BusinessException(ResponseCodeEnum.CODE_500);
        } catch (Exception e) {
            logger.error("OKhttp POST 请求异常,url:{},请求参数：{}", url, JsonUtils.convertObj2Json(params), e);
            return null;
        } finally {
            if (body != null) {
                body.close();
            }
        }
    }
}