package org.firstinspires.ftc.teamcode.Components.RFModules.Devices;

import com.qualcomm.robotcore.hardware.HardwareMap;

import static org.firstinspires.ftc.teamcode.Components.RFModules.System.Logger.df;
import static org.firstinspires.ftc.teamcode.Robots.BasicRobot.LOGGER;
import static org.firstinspires.ftc.teamcode.Robots.BasicRobot.op;
import static org.firstinspires.ftc.teamcode.Robots.BasicRobot.logger;
import static org.firstinspires.ftc.teamcode.Robots.BasicRobot.time;


import static java.lang.Math.abs;

import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;

import org.firstinspires.ftc.teamcode.Components.RFModules.System.RFLogger;

/**
 * William
 */
public class RFServo implements Servo {
    /* Basic Servo device class with reusable code */

    private final Servo rfServo;

    private double plastTime = -100;

    private double target = -100;

    final double SERVO_LIMIT;

    double FLIP_TIME = 0.2;

    boolean flipped = false;

    public double position = 0;

    private final String rfServoName;

    boolean disabled = false;

    /**
     * Constructor with name, direction, and limit
     *
     * @param p_deviceName name of the device
     * @param p_direction  direction of servo spinning
     * @param p_limit      physical limit of servo
     */

    public RFServo(String p_deviceName, Servo.Direction p_direction, double p_limit) {
        rfServo = op.hardwareMap.get(Servo.class, p_deviceName);
        rfServoName = p_deviceName;
        rfServo.setDirection(p_direction);

        logger.createFile("/ServoLogs/RFServo", "Runtime    Component               " +
                "Function               Action");

        SERVO_LIMIT = p_limit;
        plastTime = -100;
        position = -1;
        disabled = false;
    }

    /**
     * Constructor with name and limit
     *
     * @param p_deviceName name of the device
     * @param p_limit      physical limit of servo
     */
    public RFServo(String p_deviceName, double p_limit) {
        this(p_deviceName, Direction.FORWARD, p_limit);
    }

    /* Updating the last time the servo flipped */

    /**
     * @param p_flipTime time it takes to flip from one position to the other
     */

    public void setFlipTime(double p_flipTime) {
        FLIP_TIME = p_flipTime;
    }

    /**
     * Setting position of the servo
     *
     * @param p_position target position
     */

    public void setPosition(double p_position) {

        if (abs(time - plastTime )> FLIP_TIME && abs(p_position - position) > 0.01) {
            logger.log("/ServoLogs/RFServo", rfServoName + ",setPosition(),Setting Position: "
                    + df.format(p_position), true);
            LOGGER.log("moving to:" + p_position);
            rfServo.setPosition(p_position);
            plastTime = time;
            LOGGER.log(RFLogger.Severity.INFO, "POGGERS?");
            target = p_position;
            position = p_position;
        }
        if(time - plastTime <= FLIP_TIME){
            target = p_position;
        }

    }

    public void superSetPosition(double p_position) {
        rfServo.setPosition(p_position);
        plastTime = time;
        LOGGER.log(RFLogger.Severity.INFO, "POGGERS?");
        target = p_position;
        position = p_position;
    }

    public double getTarget() {
        return target;
    }

    /**
     * Flipping the servo between two positions inside the max range
     *
     * @param p_lowerPos lower target position for flipping
     * @param p_upperPos upper target position for flipping
     * @return if there was enough time since the last flip for the servo to flip
     */

    public boolean flipServoInterval(double p_lowerPos, double p_upperPos) {
        if (time - plastTime > FLIP_TIME) {
            if (flipped) {
                rfServo.setPosition(p_lowerPos);
                logger.log("/ServoLogs/RFServo", rfServoName + ",flipServoInterval(),Setting Position: "
                        + df.format(p_lowerPos), true);
                flipped = false;
            } else {
                rfServo.setPosition(p_upperPos);
                logger.log("/ServoLogs/RFServo", rfServoName + ",flipServoInterval(),Setting Position: "
                        + df.format(p_upperPos), true);
                flipped = true;
            }
        }
        return time - plastTime > FLIP_TIME;
    }

    /**
     * Flipping the servo in the max range
     */

    public void flipServoMax() {
        if (time - plastTime > FLIP_TIME) {
            if (flipped) {
                rfServo.setPosition(0);
                logger.log("/ServoLogs/RFServo", rfServoName + ",flipServoMax(),Setting Position: "
                        + df.format(0), true);
                flipped = false;
            } else {
                rfServo.setPosition(SERVO_LIMIT);
                logger.log("/ServoLogs/RFServo", rfServoName + ",flipServoMax(),Setting Position: "
                        + df.format(SERVO_LIMIT), true);
                flipped = true;
            }
            plastTime = time;
        }
    }

    public void update(){
        if(position != target && target !=-100){
            setPosition(target);
        }
    }

    /**
     * Returns current position of the servo
     *
     * @return current position of servo
     */

    public double getPosition() {
        return position;
    }

    /**
     * Returns last time the servo was flipped
     *
     * @return last time the servo was flipped
     */

    public double getLastTime() {
        return plastTime;
    }

    /**
     * Manual update of last time servo was flipped
     *
     * @param p_lastTime target last time servo was flipped
     */

    public void setLastTime(double p_lastTime) {
        plastTime = p_lastTime;
    }

    /* Overridden functions from implemented Servo class */

    @Override
    public void scaleRange(double p_min, double p_max) {
    }

    @Override
    public ServoController getController() {
        return null;
    }

    @Override
    public int getPortNumber() {
        return 0;
    }

    public void setDirection(Servo.Direction p_direction) {
        rfServo.setDirection(p_direction);
    }

    @Override
    public Direction getDirection() {
        return null;
    }

    @Override
    public Manufacturer getManufacturer() {
        return null;
    }

    @Override
    public String getDeviceName() {
        return rfServoName;
    }

    @Override
    public String getConnectionInfo() {
        return null;
    }

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public void resetDeviceConfigurationForOpMode() {

    }

    public void disable() {
//        if(!disabled) {
//            rfServo.getController().pwmDisable();
//            disabled = true;
//        }

    }

    public void enable() {
//        if(disabled) {
//            rfServo.getController().pwmEnable();
//            disabled = false;
//        }
    }


    @Override
    public void close() {

    }
}
