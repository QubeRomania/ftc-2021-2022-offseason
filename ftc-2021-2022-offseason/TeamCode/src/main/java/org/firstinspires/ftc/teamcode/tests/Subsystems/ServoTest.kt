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

@TeleOp(name = "ServoTest", group = "Main")
class ServoTest: LinearOpMode() {

    override fun runOpMode() {
        val gp1 = Gamepad(gamepad1)
        val gp2 = Gamepad(gamepad2)

        var outtakeServo1 = hardwareMap.servo["outtakeServo2"] ?: throw Exception("Failed to find servo outtakeServo1")
        outtakeServo1.position = 0.0

        waitForStart()

        while(opModeIsActive())
        {
            if (gp1.checkToggle(Gamepad.Button.A))
                outtakeServo1.position += 0.03
            if(gp1.checkToggle(Gamepad.Button.B))
                outtakeServo1.position -= 0.03
            if(gp1.checkToggle(Gamepad.Button.X))
                outtakeServo1.position = 1.0
            if(gp1.checkToggle(Gamepad.Button.Y))
                outtakeServo1.position = 0.0
            telemetry.addData("position",outtakeServo1.position)
            telemetry.update()
        }
    }
}