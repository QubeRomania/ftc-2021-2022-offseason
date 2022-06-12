package org.firstinspires.ftc.teamcode.hardware

import com.qualcomm.hardware.rev.RevTouchSensor
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.teamcode.Timing
import java.io.File
import java.io.FileOutputStream
import java.io.PrintWriter
import java.lang.Exception
import java.util.*
import kotlin.math.absoluteValue


/**
 * OutTake subsystem.
 *
 * This class controls the hardware for placing freight
 */
class Outtake(hwMap: HardwareMap) {
    companion object {
        const val SLIDER_LOW = 320
        const val SLIDER_MEDIUM = 800
        const val SLIDER_HIGH = 1100
        var SLIDER_CLOSE = 0
        const val SLIDER_AUX = -100
        var SLIDER_START_POSITION = 0
        const val MULTIPLIER = 3

        const val OUTTAKE_POWER = 1.0

        const val servo1Open = 0.41 // right
        const val servo1Close = 1.0 // right
        const val servo2Open = 0.41 // left
        const val servo2Close = 1.0 // left

        const val flickerOpen = 0.42
        const val flickerHold = 0.0
        const val flickerClose = 0.18

    }

    private val outtakeSlider1 = hwMap.dcMotor["outtakeSlider1"] ?: throw Exception("Failed to find motor outtakeSlider1")
    private val outtakeSlider2 = hwMap.dcMotor["outtakeSlider2"] ?: throw Exception("Failed to find motor outtakeSlider2")


    //val touchSensor = hwMap.get(RevTouchSensor::class.java,"touchSensor") ?: throw Exception("Failed to find RevTouchSensor touchSensor")

    var scoreTimer = Timing.Timer(200)

    var outtakePosition: Int = 0

    private val outtakeServo1 = hwMap.servo["outtakeServo1"] ?: throw Exception("Failed to find servo outtakeServo1")
    private val outtakeServo2 = hwMap.servo["outtakeServo2"] ?: throw Exception("Failed to find servo outtakeServo2")

    private val flickerServo = hwMap.servo["flickerServo"] ?: throw Exception("Failed to find servo flickerServo")

    init {
        outtakeSlider1.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT
        outtakeSlider1.direction = DcMotorSimple.Direction.FORWARD
        outtakeSlider1.mode = DcMotor.RunMode.RUN_USING_ENCODER
        outtakeSlider1.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER

        outtakeSlider2.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT
        outtakeSlider2.direction = DcMotorSimple.Direction.REVERSE
        outtakeSlider2.mode = DcMotor.RunMode.RUN_USING_ENCODER
        outtakeSlider2.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER


        outtakeSlider1.power = 0.0
        outtakeSlider2.power = 0.0

        outtakePosition = 0

        closeServo()
        closeFlicker()
    }

    fun openSlider() {
        holdFreight()

        outtakePosition = SLIDER_HIGH
        outtakeSlider1.targetPosition = outtakePosition
        outtakeSlider1.mode = DcMotor.RunMode.RUN_TO_POSITION
        outtakeSlider1.power = OUTTAKE_POWER

        outtakeSlider2.targetPosition = outtakePosition
        outtakeSlider2.mode = DcMotor.RunMode.RUN_TO_POSITION
        outtakeSlider2.power = OUTTAKE_POWER

        scoreTimer.start()

        while (!scoreTimer.done()) {
        }
        scoreTimer.pause()

        releaseServo()

    }

    fun closeSlider() {
        closeFlicker()

        scoreTimer = Timing.Timer(800)

        closeServo()

        scoreTimer.start()

        while (!scoreTimer.done()){

        }
        scoreTimer.pause()

        outtakePosition = SLIDER_CLOSE
        outtakeSlider1.targetPosition = outtakePosition
        outtakeSlider1.mode = DcMotor.RunMode.RUN_TO_POSITION
        outtakeSlider1.power = OUTTAKE_POWER

        outtakeSlider2.targetPosition = outtakePosition
        outtakeSlider2.mode = DcMotor.RunMode.RUN_TO_POSITION
        outtakeSlider2.power = OUTTAKE_POWER
    }

    fun openLowSlider() {
        outtakePosition = SLIDER_LOW
        outtakeSlider1.targetPosition = outtakePosition
        outtakeSlider1.mode = DcMotor.RunMode.RUN_TO_POSITION
        outtakeSlider1.power = OUTTAKE_POWER

        outtakeSlider2.targetPosition = outtakePosition
        outtakeSlider2.mode = DcMotor.RunMode.RUN_TO_POSITION
        outtakeSlider2.power = OUTTAKE_POWER
    }

    fun openMidSlider() {
        outtakePosition = SLIDER_MEDIUM
        outtakeSlider1.targetPosition = outtakePosition
        outtakeSlider1.mode = DcMotor.RunMode.RUN_TO_POSITION
        outtakeSlider1.power = OUTTAKE_POWER

        outtakeSlider2.targetPosition = outtakePosition
        outtakeSlider2.mode = DcMotor.RunMode.RUN_TO_POSITION
        outtakeSlider2.power = OUTTAKE_POWER
    }

    fun moveSlider(power: Double) {
        outtakePosition += (power * MULTIPLIER).toInt()
        outtakePosition += SLIDER_START_POSITION

        if(outtakePosition > SLIDER_HIGH)
            outtakePosition = SLIDER_HIGH
        if(outtakePosition < SLIDER_AUX)
            outtakePosition = SLIDER_AUX

        outtakeSlider1.targetPosition = outtakePosition
        outtakeSlider1.mode = DcMotor.RunMode.RUN_TO_POSITION
        outtakeSlider1.power = OUTTAKE_POWER

        outtakeSlider2.targetPosition = outtakePosition
        outtakeSlider2.mode = DcMotor.RunMode.RUN_TO_POSITION
        outtakeSlider2.power = OUTTAKE_POWER
    }

    fun releaseServo() {
        setServoPositions(servo1Open)
        setServoPositions(servo2Open)
    }

    fun closeServo(){
        setServoPositions(servo1Close)
        setServoPositions(servo2Close)
    }

    fun releaseFreight(){
        flickerServo.position = flickerOpen
    }

    fun holdFreight(){
        flickerServo.position = flickerHold
    }

    fun closeFlicker(){
        flickerServo.position = flickerClose
    }

    fun stop(){
        outtakeSlider1.power = 0.0
        outtakeSlider2.power = 0.0

    }

    fun printPosition(telemetry: Telemetry) {
        telemetry.addLine("Position outtake 1")
            .addData("Slider 1","%d", outtakeSlider1.currentPosition)
        telemetry.addLine("Position outtake 2")
            .addData("Slider 2","%d", outtakeSlider2.currentPosition)
    }

    fun setPower(v: Double) {
        outtakeSlider1.power = v
        outtakeSlider2.power = v
    }

    fun setServoPositions(pos: Double) {
        outtakeServo1.position = pos
        outtakeServo2.position = pos
    }

}
