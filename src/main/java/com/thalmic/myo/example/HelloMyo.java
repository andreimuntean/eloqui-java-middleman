package com.thalmic.myo.example;

import com.thalmic.myo.DeviceListener;
import com.thalmic.myo.Hub;
import com.thalmic.myo.Myo;

/*
 * JVM Arguments to help debug.
 -Xcheck:jni
 -XX:+ShowMessageBoxOnError
 -XX:+UseOSErrorReporting
 */
public class HelloMyo {
    public static void main(String[] args) {
        try {
            Hub hub = new Hub("com.example.hello-myo");

            System.out.println("Attempting to find a Myo...");
            Myo myo = hub.waitForMyo(10000);


            if (myo == null) {
                throw new RuntimeException("Unable to find a Myo!");
            }else{
                System.out.println("Attempting to find a second Myo...");
                Myo myo2 = hub.waitForMyo(10000);
            }

            System.out.println("Connected to a Myo armband!");
            Server.start("/feed",3000);
            DataCollector dataCollector = new DataCollector();
            hub.addListener(dataCollector);

            while (true) {
                hub.run(1000 / 20);

                //System.out.print(dataCollector);
            }

        } catch (Exception e) {
            System.err.println("Error: ");
            e.printStackTrace();
            System.exit(1);
        }
    }
}