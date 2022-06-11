package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.firstinspires.ftc.teamcode.hardware.Hardware
import java.lang.Exception
import java.lang.Math.atan2
import kotlin.math.absoluteValue

@TeleOp(name = "IntakeTest", group = "Main")
class IntakeTest: LinearOpMode() {

    override fun runOpMode() {
        val gp1 = Gamepad(gamepad1)
        val gp2 = Gamepad(gamepad2)

        var intakeMotor = hardwareMap.dcMotor["intakeMotor"] ?: throw Exception("Failed to find leftEncoder")

        waitForStart()

        while(opModeIsActive())
        {
            intakeMotor.power = (gp1.right_trigger-gp1.left_trigger.toDouble())
        }
    }
}