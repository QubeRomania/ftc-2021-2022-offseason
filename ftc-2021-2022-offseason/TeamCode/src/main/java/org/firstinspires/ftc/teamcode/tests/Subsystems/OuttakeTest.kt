package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.firstinspires.ftc.teamcode.hardware.Hardware
import org.firstinspires.ftc.teamcode.hardware.Outtake
import java.lang.Exception
import java.lang.Math.atan2
import kotlin.math.absoluteValue

@TeleOp(name = "OuttakeTest", group = "Main")
class OuttakeTest: LinearOpMode() {

    override fun runOpMode() {
        val gp1 = Gamepad(gamepad1)
        val gp2 = Gamepad(gamepad2)

        var outtakeSlider = hardwareMap.dcMotor["outtakeMotor"] ?: throw Exception("Failed to find outtakeMotor")

        var intakeMotor = hardwareMap.dcMotor["intakeMotor"] ?: throw Exception("Failed to find intakeMotor")

        outtakeSlider.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT
        outtakeSlider.direction = DcMotorSimple.Direction.FORWARD
        outtakeSlider.mode = DcMotor.RunMode.RUN_USING_ENCODER
        outtakeSlider.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER

        outtakeSlider.power = 0.0

        val SLIDER_HIGH = 1000
        var SLIDER_CLOSE = 0
        var outtakePosition: Int = 0

        var outtakeServo1 = hardwareMap.servo["outtakeServo"] ?: throw Exception("Failed to find servo outtakeServo1")
        outtakeServo1.position = 0.0

        waitForStart()

        while(opModeIsActive())
        {
            intakeMotor.power = (gp1.right_trigger-gp1.left_trigger.toDouble())

            if(gp1.checkToggle(Gamepad.Button.RIGHT_BUMPER)) {
                outtakePosition = SLIDER_HIGH
                outtakeSlider.targetPosition = outtakePosition
                outtakeSlider.mode = DcMotor.RunMode.RUN_TO_POSITION
                outtakeSlider.power = 1.0
            }
            if(gp1.checkToggle(Gamepad.Button.LEFT_BUMPER))
            {
                outtakePosition = SLIDER_CLOSE
                outtakeSlider.targetPosition = outtakePosition
                outtakeSlider.mode = DcMotor.RunMode.RUN_TO_POSITION
                outtakeSlider.power = 1.0

            }

            if(gp1.checkToggle(Gamepad.Button.A))
            {
                outtakeServo1.position = 0.75
            }
            if(gp1.checkToggle(Gamepad.Button.B))
            {
                outtakeServo1.position = 0.0
            }
        }
    }
}