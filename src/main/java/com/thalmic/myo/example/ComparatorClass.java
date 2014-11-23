package com.thalmic.myo.example;

import com.thalmic.myo.enums.PoseType;
import javafx.geometry.Pos;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Alex on 22.11.2014.
 */
public class ComparatorClass {


    private ArrayList<ArrayList<PoseType>> poses = new ArrayList<ArrayList<PoseType>>();
    private HashMap<ArrayList<PoseType>,String> messageHashMap = new HashMap<ArrayList<PoseType>, String>();

    public ComparatorClass(){
        ArrayList<PoseType> pattern1= new ArrayList<PoseType>();
        pattern1.add(PoseType.FIST);
        pattern1.add(PoseType.FINGERS_SPREAD);

        poses.add(pattern1);


        messageHashMap.put(pattern1,"Tocmai am am strans si am deschis pumnul!");

        ArrayList<PoseType> pattern2= new ArrayList<PoseType>();

        pattern2.add(PoseType.WAVE_IN);
        pattern2.add(PoseType.WAVE_OUT);


        poses.add(pattern2);


        messageHashMap.put(pattern2,"Iti dau doua palme!");
    }
    public void compare(ArrayList argPattern){
        System.out.println("Checking pattern list");

        for(int i=0;i<poses.size();i++){
            System.out.println("Size of  pattern: " + poses.get(i).size());
            System.out.println("Size of argPattenr: " + argPattern.size());
            if(argPattern.size() == poses.get(i).size()){
                System.out.println("There is a possible pattern ");
                int j=0;
                for(j=0;j<argPattern.size();j++){
                    if(!argPattern.get(j).equals(poses.get(i).get(j))){
                        break;
                    }
                }
                if(j==argPattern.size()){
                    String message = messageHashMap.get(poses.get(i));
                    System.out.println(message);
                    Server.addStringToQueue(message);
                    System.out.printf("Pattern found!");
                }
            }
        }
    }
}




