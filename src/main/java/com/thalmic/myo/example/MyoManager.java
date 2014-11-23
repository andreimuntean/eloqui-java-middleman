package com.thalmic.myo.example;

import com.thalmic.myo.enums.Arm;

import java.util.HashMap;

public class MyoManager
{
    private static HashMap<String, Arm> armBindings = new HashMap<String, Arm>();

    public static Arm getArm(String id)
    {
        if (armBindings.containsKey(id))
        {
            return armBindings.get(id);
        }
        else
        {
            return Arm.ARM_UNKNOWN;
        }
    }

    public static void setArmBinding(String id, Arm arm)
    {
        armBindings.put(id, arm);
        System.out.println("Linked " + id + " to arm " + arm.toString());
    }
}
