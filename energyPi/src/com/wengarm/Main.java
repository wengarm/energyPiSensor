package com.wengarm;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.trigger.GpioSyncStateTrigger;

import java.io.*;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class Main {

    public static void main(String[] args) throws URISyntaxException, IOException{
	    final GpioController gpio = GpioFactory.getInstance();

        final GpioPinDigitalInput lightSensor = gpio.provisionDigitalInputPin(RaspiPin.GPIO_07);
        lightSensor.setShutdownOptions(false);

        LightSensorListener lightSensorListener = new LightSensorListener();
        lightSensor.addListener(lightSensorListener);

        String workDir = "/home/wengarm/energyPi/Log";
        System.out.println("Working directory : " + workDir + "\n");

        int interval = 15;

        StackBlink stackBlinks = new StackBlink();
        StackName stackNames = new StackName();
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();

        while(true) {
            int newInterval = 0;
            try {
                newInterval = (int) MiddleMan.getInterval();
            }catch(Exception e){

            }
            if (newInterval > 0 && newInterval != interval) {
                interval = newInterval;
                System.out.println("Interval sa zmenil na " + interval);
            }else{
                interval = 10;
            }
            System.out.println("Status:");
            System.out.println("Interval: " + interval);

            Date date = new Date();
            String year = new SimpleDateFormat("yyyy").format(date);
            String month = new SimpleDateFormat("MMMM", Locale.forLanguageTag("sk-SK")).format(date);
            String dateAndTime = new SimpleDateFormat("dd-MM_HH-mm-ss").format(date);

            File file = new File(workDir + "/" + year + "/" + month + "/" + dateAndTime + ".txt");
            file.getParentFile().mkdirs();
            file.createNewFile();

            lightSensorListener.setFile(file);
            lightSensorListener.setGlobalValue(0);

            while(true){
                try {
                    Thread.sleep(interval * 1000 * 60);
                }catch (InterruptedException e){
                    System.out.println("Interrupted by exception while sleeping");
                    System.out.println(e.getMessage());
                }
                break;
            }
            stackBlinks.insert(lightSensorListener.getGlobalValue());
            stackNames.insert(year + ", " + month + ",  " + dateAndTime.substring(6).replaceAll("-",":"));
            Collections.reverse(stackBlinks.getStack());
            Collections.reverse(stackNames.getStack());
            try {
                MiddleMan.postStack(stackBlinks.getStack(), "blink");
                MiddleMan.postStack(stackNames.getStack(), "name");
            }catch(Exception e){

            }
            Collections.reverse(stackBlinks.getStack());
            Collections.reverse(stackNames.getStack());
            try {
                MiddleMan.postStack("build");
            }catch (Exception e){

            }
            System.out.println("Ubehlo " + interval*1000 / 1000 + " minut a spotreba za danu dobu bola: " + lightSensorListener.getGlobalValue());
        }

    }
}
