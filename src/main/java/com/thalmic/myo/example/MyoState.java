package com.thalmic.myo.example;

import com.thalmic.myo.enums.*;

public class MyoState
{
    private Arm arm = Arm.ARM_UNKNOWN;
    private PoseType poseType = PoseType.REST;
    private Rotation rotation = Rotation.ROTATION_UNKNOWN;
    private Direction direction = Direction.DIRECTION_UNKNOWN;
    private Orientation orientation = Orientation.ORIENTATION_UNKNOWN;

    public void showMyo()
    {
        System.out.println("ARM " + arm);
        System.out.println("Rotation " + rotation);
        System.out.println("Direction " + direction);
        System.out.println("Orientation " + orientation);
        System.out.println("poseType " + poseType);
    }

    public MyoState()
    {
        this.arm = Arm.ARM_UNKNOWN;
        this.poseType = PoseType.REST;
        this.rotation = Rotation.ROTATION_UNKNOWN;
        this.direction = Direction.DIRECTION_UNKNOWN;
        this.orientation = Orientation.ORIENTATION_UNKNOWN;
    }

    public MyoState(MyoState myoState)
    {
        this.arm = myoState.arm;
        this.poseType = myoState.poseType;
        this.rotation = myoState.rotation;
        this.direction = myoState.direction;
        this.orientation = myoState.orientation;
    }

    public MyoState(Arm arm, PoseType poseType, Rotation rotation, Direction direction, Orientation orientation)
    {
        this.arm = arm;
        this.poseType = poseType;
        this.rotation = Rotation.ROTATION_UNKNOWN;
        this.direction = direction;
        this.orientation = Orientation.ORIENTATION_UNKNOWN;
    }

    public Arm getArm()
    {
        return arm;
    }

    public PoseType getPoseType()
    {
        return poseType;
    }

    public Rotation getRotation()
    {
        return rotation;
    }

    public Direction getDirection()
    {
        return direction;
    }

    public Orientation getOrientation()
    {
        return orientation;
    }

    public void setPoseType(PoseType poseType)
    {
        this.poseType = poseType;
    }

    public void setRotation(Rotation rotation)
    {
        // this.rotation = rotation;
    }

    public void setDirection(Direction direction)
    {
        this.direction = direction;
    }

    public void setOrientation(Orientation orientation)
    {
        // this.orientation = orientation;
    }

    public boolean equals(MyoState myoState)
    {
        return arm.equals( myoState.arm)
                && poseType.equals( myoState.poseType)
                && rotation.equals(myoState.rotation)
                && direction.equals(myoState.direction)
                && orientation.equals(myoState.orientation);
    }
}
