package com.wengarm;

import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LightSensorListener implements GpioPinListenerDigital {
    private File file;
    private int globalValue = 0;

    @Override
    public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
        if(file==null)
            return;
        if (event.getState().isHigh()) {
            int value = globalValue;
            try {
                BufferedWriter fileWriter = new BufferedWriter(new FileWriter(file));
                fileWriter.write("" + (value + 1));
                fileWriter.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            globalValue++;

            try {
                postGlobalValue();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void postGlobalValue() throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://localhost:8080/mainServlet");

        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("updateGlobal", "true"));
        parameters.add(new BasicNameValuePair("globalValue", "" + globalValue));
        httpPost.setEntity(new UrlEncodedFormEntity(parameters));

        CloseableHttpResponse response = client.execute(httpPost);
        client.close();
    }

    public void setFile(File file) {
        this.file = file;
        try{
            zeroFile(file);
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    public void zeroFile(File file) throws IOException {
        BufferedWriter fileWriter = new BufferedWriter(new FileWriter(file));
        fileWriter.write("" + 0);
        fileWriter.close();
    }

    public void setGlobalValue(int globalValue) {
        this.globalValue = globalValue;
    }

    public int getGlobalValue(){
        return globalValue;
    }
}
