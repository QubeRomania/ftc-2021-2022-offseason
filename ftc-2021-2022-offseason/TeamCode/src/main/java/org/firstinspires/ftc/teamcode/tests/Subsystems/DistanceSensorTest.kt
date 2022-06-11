package org.firstinspires.ftc.teamcode.tests.Subsystems

import com.qualcomm.hardware.bosch.BNO055IMU
import com.qualcomm.hardware.rev.Rev2mDistanceSensor
import com.qualcomm.hardware.rev.RevTouchSensor
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import java.lang.Exception

@TeleOp
class DistanceSensorTest : LinearOpMode() {

    val hwMap = hardwareMap

    override fun runOpMode() {
        val distanceSensorLeft = hwMap.get(Rev2mDistanceSensor::class.java,"distanceSensorLeft") ?: throw Exception("Failed to find Rev2MDistanceSensor distanceSensorLeft")
        val distanceSensorRight = hwMap.get(Rev2mDistanceSensor::class.java,"distanceSensorRight") ?: throw Exception("Failed to find Rev2MDistanceSensor distanceSensorRight")
        val distanceSensorFront = hwMap.get(Rev2mDistanceSensor::class.java,"distanceSensorFront") ?: throw Exception("Failed to find Rev2MDistanceSensor distanceSensorFront")

        val imu = hwMap.get(BNO055IMU::class.java,"imu") ?: throw Exception("Failed to find IMU")
        val parameters = BNO055IMU.Parameters()
        imu.initialize(parameters)

        waitForStart()

        while (opModeIsActive())
        {
            var angles = imu.getAngularOrientation(AxesReference.INTRINSIC,AxesOrder.ZYX,AngleUnit.DEGREES)
            telemetry.addData("Heading", angles.firstAngle)
            telemetry.addData("distanceSensorLeft", distanceSensorLeft.getDistance(DistanceUnit.INCH))
            telemetry.addData("distanceSensorRight", distanceSensorRight.getDistance(DistanceUnit.INCH))
            telemetry.addData("distanceSensorFront", distanceSensorFront.getDistance(DistanceUnit.INCH))
            telemetry.update()
        }
    }
}