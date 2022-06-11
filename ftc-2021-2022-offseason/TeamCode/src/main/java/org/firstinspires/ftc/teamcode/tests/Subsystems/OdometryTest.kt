package org.firstinspires.ftc.teamcode.tests.Subsystems

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import java.lang.Exception

@TeleOp
class OdometryTest : LinearOpMode() {

    var leftEncoder : DcMotor? = null
    var rightEncoder : DcMotor? = null
    var strafeEncoder : DcMotor? = null

    val hwMap = hardwareMap

    override fun runOpMode() {
        leftEncoder = hwMap.dcMotor["leftEncoder"] ?: throw Exception("Failed to find leftEncoder")
        rightEncoder = hwMap.dcMotor["rightEncoder"] ?: throw  Exception("Failed to find rightEncoder")
        strafeEncoder = hwMap.dcMotor["strafeEncoder"] ?: throw  Exception("Failed to find strafeEncoder")

        waitForStart()

        while (opModeIsActive())
        {
            telemetry.addData("leftEncoder", leftEncoder!!.currentPosition)
            telemetry.addData("rightEncoder", rightEncoder!!.currentPosition)
            telemetry.addData("strafeEncoder", strafeEncoder!!.currentPosition)
        }
    }
}