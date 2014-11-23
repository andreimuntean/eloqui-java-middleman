package com.thalmic.myo.example;

import com.thalmic.myo.*;
import com.thalmic.myo.enums.*;

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
    private Pose poseDummie = new Pose();
    private MyoState currentLeftArmState = new MyoState();
    private MyoState currentRightArmState = new MyoState();
    private MyoState lastLeftArmState = new MyoState();
    private MyoState lastRightArmState = new MyoState();
    private Arm whichArm = Arm.ARM_UNKNOWN;
    private int counter = 0;
    private int counter2 = 0;
    private int counter3 = 0;
   // private ArrayList<PoseType> poseList = new ArrayList<PoseType>();
    private ArrayList<MyoState> myoStateList = new ArrayList<MyoState>();
    private Rotation rotation;
    private Orientation orientation;
    private Direction direction;


    private ComparatorClass comparator = new ComparatorClass();
    /*private Timer timer = new Timer(100, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            //for(int i=0;i<10;i++)
            //    System.out.println("Seems to work!");
            System.out.println("Size of myoStateList outside of compare: " + myoStateList.size());
            comparator.compare(myoStateList);

            timer.stop();
            myoStateList.clear();

        }
    });*/


    public DataCollector() {

        rollW = 0;
        pitchW = 0;
        yawW = 0;
    }
    public void print(Myo myo){
       // System.out.print("Myo " + myo.toString());
       System.out.print(" X AXIS: ");
        if((rollW > 7)&&(rollW<13)){
            System.out.print("NORMAL");
        }else if(rollW <8){

            System.out.print("CLOCKWISE");
        }else{

            System.out.print("COUNTER");
        }

       System.out.print("(Y AXIS) :");
        if(pitchW < 6) {

            System.out.print("DOWN");
        }else if (pitchW < 12) {

            System.out.print("MEDIUM");

            }else {

            System.out.print("UP");
            }

        System.out.print("(Z AXIS) :");
        if(yawW < 6) {

            System.out.print("OUTWARDS");
        }else if (yawW < 12){
            System.out.print("SIDEWAYS");
            }else{
            System.out.print("INWARDS");
        }

        System.out.print("POSE : " + this.poseDummie.toString());
        System.out.println();
    }

    @Override
    public void onOrientationData(Myo myo, long timestamp, Quaternion rotationQ) {
        Quaternion normalized = rotationQ.normalized();
        if((rollW > 7)&&(rollW<13)){
            rotation = Rotation.NORMAL;
        }else if(rollW <8){
            rotation = Rotation.CLOCKWISE;
        }else{
            rotation = Rotation.COUNTER;
        }
        if(pitchW < 6) {
            direction = Direction.DOWN;
        }else if (pitchW < 12) {
            direction = Direction.MEDIUM;

        }else {

            direction = Direction.UP;
        }

        if(yawW < 6) {
            orientation = Orientation.OUTWARDS;

        }else if (yawW < 12){
            orientation = Orientation.SIDEWAYS;
        }else{
            orientation = Orientation.INWARDS;
        }
    //motionList.add(rotation);
        if(counter==20) {
            //this.print(myo);
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

        if (poseDummie.getType() == PoseType.REST)
        {
            comparator.compare(myoStateList);
            // myoStateList.clear();
        }


    }

    @Override
    public void onPose(Myo myo, long timestamp, Pose pose) {
        poseDummie = pose;
        //System.out.println("Pose : " + pose.getType() + " from Myo " + myo.toString());

        // myoStateList.add(new MyoState(MyoManager.getArm(myo.toString()),pose.getType(), rotation, direction, orientation ));
        // System.out.println("PoseList size : " + poseList.size());

        Arm currentArm = MyoManager.getArm(myo.toString());

        if (pose.getType() != PoseType.REST && currentArm != Arm.ARM_UNKNOWN) {
            if (currentArm == Arm.ARM_LEFT) {
                currentLeftArmState = new MyoState(Arm.ARM_LEFT, pose.getType(), rotation, direction, orientation);

                //if (!currentLeftArmState.equals(lastLeftArmState))
                {
                    myoStateList.add(new MyoState(currentArm, pose.getType(), rotation, direction, orientation));
                    lastLeftArmState = currentLeftArmState;

                }
            } else if (currentArm == Arm.ARM_RIGHT) {
                currentRightArmState = new MyoState(Arm.ARM_RIGHT, pose.getType(), rotation, direction, orientation);
                //if (!currentRightArmState.equals(lastRightArmState))
                {
                    myoStateList.add(new MyoState(currentArm, pose.getType(), rotation, direction, orientation));
                    lastRightArmState = currentRightArmState;
                }
            }
        }

        /*if(timer.isRunning())
            timer.stop();

        timer.start();*/
    }
/*
        if(!pose.getType().equals(lastType) && !pose.getType().equals(PoseType.REST)){
            poseList.add(pose.getType());
            lastType = pose.getType();
        }

        if (currentMyoState.getPoseType){
            poseList.add(pose.getType());
            lastType = pose.getType();
        }
*/


/*
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
*/
    @Override
    public void onArmSync(Myo myo, long timestamp, Arm arm, XDirection xDirection) {

        whichArm = arm;
        MyoManager.setArmBinding(myo.toString(), arm);
    }

    @Override
    public void onArmUnsync(Myo myo, long timestamp) {
        whichArm = Arm.ARM_UNKNOWN;
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
/*
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
      /*  if (currentPose != null) {
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
    */

    private String repeatCharacter(char character, int numOfTimes) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < numOfTimes; i++) {
            builder.append(character);
        }
        return builder.toString();
    }
}
