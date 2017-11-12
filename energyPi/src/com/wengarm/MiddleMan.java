package com.wengarm;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MiddleMan {

    public static int getInterval() throws IOException, URISyntaxException{
        String temp = "";

        CloseableHttpClient httpClient = HttpClients.createDefault();

        URIBuilder builder = new URIBuilder();
        URI uri = builder.setScheme("http").setHost("localhost").setPort(8080).setPath("/mainServlet")
            .setParameter("get", "interval").build();

        HttpGet httpGet = new HttpGet(uri);

        CloseableHttpResponse response = httpClient.execute(httpGet);
        int returnedCode;
        try {
            HttpEntity entity = response.getEntity();

            temp = EntityUtils.toString(entity);
            returnedCode = response.getStatusLine().getStatusCode();
        }finally {
            response.close();
        }

        if (returnedCode != 200)
            return -1;

        return Integer.parseInt(temp);
    }

    public static void postStack(LinkedList<?> stack, String type) throws UnsupportedEncodingException, IOException{
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://localhost:8080/mainServlet");

        Gson gson = new GsonBuilder().disableHtmlEscaping().create();

        List<BasicNameValuePair> param = new ArrayList<>();
        param.add(new BasicNameValuePair("updateStack", "true"));
        param.add(new BasicNameValuePair("stack", gson.toJson(stack)));
        param.add(new BasicNameValuePair("type", type));
        httpPost.setEntity(new UrlEncodedFormEntity(param));

        CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
        httpClient.close();
    }

    public static void postStack(String type) throws UnsupportedEncodingException, IOException{
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://localhost:8080/mainServlet");

        List<BasicNameValuePair> param = new ArrayList<>();
        param.add(new BasicNameValuePair("updateStack", "true"));
        param.add(new BasicNameValuePair("type", type));
        httpPost.setEntity(new UrlEncodedFormEntity(param));

        CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
        httpClient.close();
    }
}
