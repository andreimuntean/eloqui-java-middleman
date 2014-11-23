package com.thalmic.myo.example;

import com.thalmic.myo.*;
import com.thalmic.myo.enums.Arm;
import com.thalmic.myo.enums.PoseType;
import com.thalmic.myo.enums.VibrationType;
import com.thalmic.myo.enums.XDirection;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Time;
import java.util.ArrayList;



public class DataCollector implements DeviceListener {
    private static final int SCALE = 18;
    private double rollW;
    private double pitchW;
    private double yawW;
    private Pose currentPose;
    private Arm whichArm;
    private int counter = 0;
    private int counter2 = 0;
    private int counter3 = 0;
    private ArrayList<PoseType> poseList = new ArrayList<PoseType>();
    private ComparatorClass comparator = new ComparatorClass();
    private PoseType lastType = null;




    private Timer timer = new Timer(2000, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            //for(int i=0;i<10;i++)
            //    System.out.println("Seems to work!");
            System.out.println("Size of poseList outside of compare: " + poseList.size());

            comparator.compare(poseList);
            poseList.clear();
            timer.stop();

        }
    });


    public DataCollector() {

        rollW = 0;
        pitchW = 0;
        yawW = 0;
        currentPose = new Pose();
    }

    @Override
    public void onOrientationData(Myo myo, long timestamp, Quaternion rotation) {
        Quaternion normalized = rotation.normalized();

        //motionList.add(rotation);
        if(counter==100) {
            //System.out.println("Orientation Data = (" + rotation.getX() + "," + rotation.getY() + "," + rotation.getZ() + "," + rotation.getZ() + ")");
            counter=0;
        }
        counter++;


        double roll = Math.atan2(2.0f * (normalized.getW() * normalized.getX() + normalized.getY() * normalized.getZ()), 1.0f - 2.0f * (normalized.getX() * normalized.getX() + normalized.getY() * normalized.getY()));
        double pitch = Math.asin(2.0f * (normalized.getW() * normalized.getY() - normalized.getZ() * normalized.getX()));
        double yaw = Math.atan2(2.0f * (normalized.getW() * normalized.getZ() + normalized.getX() * normalized.getY()), 1.0f - 2.0f * (normalized.getY() * normalized.getY() + normalized.getZ() * normalized.getZ()));

        rollW = ((roll + Math.PI) / (Math.PI * 2.0) * SCALE);
        pitchW = ((pitch + Math.PI / 2.0) / Math.PI * SCALE);
        yawW = ((yaw + Math.PI) / (Math.PI * 2.0) * SCALE);
    }

    @Override
    public void onPose(Myo myo, long timestamp, Pose pose) {

        System.out.println("Pose : " + pose.getType() + " from Myo " + myo.toString());

        System.out.println("PoseList size : " + poseList.size());
        currentPose = pose;
        if(!pose.getType().equals(lastType) && !pose.getType().equals(PoseType.REST)){
            poseList.add(pose.getType());
            lastType = pose.getType();
        }

        if(timer.isRunning())
            timer.stop();

        timer.start();


        if (currentPose.getType() == PoseType.FIST) {
            myo.vibrate(VibrationType.VIBRATION_MEDIUM);
        }
        if (currentPose.getType() == PoseType.FINGERS_SPREAD){
            myo.vibrate(VibrationType.VIBRATION_LONG);
        }
        if (currentPose.getType() == PoseType.WAVE_IN || currentPose.getType() == PoseType.WAVE_OUT){
            myo.vibrate(VibrationType.VIBRATION_SHORT);
        }
    }

    @Override
    public void onArmSync(Myo myo, long timestamp, Arm arm, XDirection xDirection) {
        whichArm = arm;
    }

    @Override
    public void onArmUnsync(Myo myo, long timestamp) {
        whichArm = null;
    }

    @Override
    public void onAccelerometerData(Myo myo, long timestamp, Vector3 accel) {
        if(counter2==100) {
            //System.out.println("Accel data ... X:" + accel.getX() + " |  Y:" + accel.getY() + " | Z:" + accel.getZ());
            counter2=0;
        }
        counter2++;
    }

    @Override
    public void onConnect(Myo myo, long timestamp, FirmwareVersion firmwareVersion) {
    }

    @Override
    public void onDisconnect(Myo myo, long timestamp) {
    }

    @Override
    public void onPair(Myo myo, long timestamp, FirmwareVersion firmwareVersion) {
    }

    @Override
    public void onUnpair(Myo myo, long timestamp) {
    }

    @Override
    public void onGyroscopeData(Myo myo, long timestamp, Vector3 gyro) {
        if(counter3==100){
            //System.out.println("Gyro data ... X:" + gyro.getX() + " |  Y:" + gyro.getY() + " | Z:" + gyro.getZ());
            counter3=0;
        }
        counter3++;
    }

    @Override
    public void onRssi(Myo myo, long timestamp, int rssi) {
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("\r");

        String xDisplay = String.format("[%s%s]", repeatCharacter('*', (int) rollW), repeatCharacter(' ', (int) (SCALE - rollW)));
        String yDisplay = String.format("[%s%s]", repeatCharacter('*', (int) pitchW), repeatCharacter(' ', (int) (SCALE - pitchW)));
        String zDisplay = String.format("[%s%s]", repeatCharacter('*', (int) yawW), repeatCharacter(' ', (int) (SCALE - yawW)));

        String armString = null;
        if (whichArm != null) {
            armString = String.format("[%s]", whichArm == Arm.ARM_LEFT ? "L" : "R");
        } else {
            armString = String.format("[?]");
        }
        String poseString = null;
        if (currentPose != null) {
            String poseTypeString = currentPose.getType()
                    .toString();
            poseString = String.format("[%s%" + (SCALE - poseTypeString.length()) + "s]", poseTypeString, " ");
        } else {
            poseString = String.format("[%14s]", " ");
        }
        builder.append(xDisplay);
        builder.append(yDisplay);
        builder.append(zDisplay);
        builder.append(armString);
        builder.append(poseString);
        return builder.toString();
    }

    private String repeatCharacter(char character, int numOfTimes) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < numOfTimes; i++) {
            builder.append(character);
        }
        return builder.toString();
    }
}
