package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.firstinspires.ftc.teamcode.hardware.Carousel
import org.firstinspires.ftc.teamcode.hardware.Hardware
import org.firstinspires.ftc.teamcode.hardware.Intake
import java.lang.Math.atan2
import kotlin.math.absoluteValue
import kotlin.math.sqrt

@TeleOp(name = "CompleteDrive", group = "Main")
class CompleteDrive: OpMode() {
    override fun preInit() {
        hw.CustomElement.servo1.position = 0.38
        hw.CustomElement.servo2.position = 0.62
    }

    override fun preInitLoop() {
        telemetry.addLine("Waiting for start...")
        telemetry.update()
        idle()
    }
    override fun Hardware.run() {
        val gp1 = Gamepad(gamepad1)
        val gp2 = Gamepad(gamepad2)

        var isPlacing = false
        var isDelivering = false
        var isUp = false
        var isMid = false
        var isLow = false
        var isOpenedArm = false

        var intakeScale = 0.8

        var driveScale = 0.8
        var slowScale = 0.2

        var mod = 1
        var normal = 1
        var manual = 2
        var endgame = 2


        while(opModeIsActive()){
            val power = speed
            val rotPower = rotation


            if(gp2.left_trigger > 0.2) {
                outtake.moveSlider((gp2.right_trigger - gp2.left_trigger).toDouble())
                mod = manual
            } else {
                if(mod == manual)
                    outtake.outtakeSlider1.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
                    outtake.outtakeSlider2.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
                mod = normal
            }


            if(gp1.checkHold(Gamepad.Button.RIGHT_BUMPER))
                hw.motors.move(direction, power*slowScale, rotPower*slowScale)
            else
                hw.motors.move(direction, power*driveScale, rotPower*driveScale)

            if(gp2.checkToggle(Gamepad.Button.A) && outtake.outtakePosition > 10) {
                if(!isPlacing) {
                    isPlacing = true
                    outtake.releaseServo()
                }
                else {
                    isPlacing = false
                    outtake.closeServo()
                    //outtake.closeSlider()
                }
            }

            if(gp2.checkToggle(Gamepad.Button.Y)) {
                mod = normal
            }
            if(gp2.checkToggle(Gamepad.Button.START)) {
                mod = if(mod == manual)
                    endgame
                else
                    manual
            }

            if(mod == normal) {
                var lx= gp1.left_stick_x
                var ry = gp1.right_stick_y
                hw.motors.move(direction, power*slowScale, rotPower*slowScale)
                intake.setIntakePower(gp1.right_trigger-gp1.left_trigger.toDouble())
                intake.setIntakePower(gp2.right_trigger-gp2.left_trigger.toDouble())

                if(gp1.checkToggle(Gamepad.Button.X))
                {
                    if(!isDelivering)
                    {
                        carousel.moveCarousel(-0.8)
                        isDelivering = true
                    }
                    else
                    {
                        carousel.moveCarousel(0.0)
                        isDelivering = false
                    }
                }
                if(gp1.checkToggle(Gamepad.Button.A))
                    intake.setServoOpen()
                if(gp1.checkToggle(Gamepad.Button.B))
                    CustomElement.openServos()

                if(gp2.checkToggle(Gamepad.Button.A))
                    intake.setServoOpen()

                if(gp2.checkToggle(Gamepad.Button.RIGHT_BUMPER)) {
                    if(!isUp) {
                        outtake.openSlider()
                        isUp = true
                    }
                }
                if(gp2.checkToggle(Gamepad.Button.LEFT_BUMPER)) {
                    if(!isLow) {
                        outtake.openLowSlider()
                        isLow = true
                        isUp = true
                        isMid = false
                    }
                }
                if(gp2.checkToggle(Gamepad.Button.Y)) {
                    if(!isMid){
                        outtake.openMidSlider()
                        isMid = true
                        isLow = false
                        isUp = true
                    }
                }
                if(gp2.checkToggle(Gamepad.Button.B)) {
                    if(!isLow) {
                        outtake.openLowSlider()
                        isLow = true
                        isUp = true
                        isMid = false
                    }
                }
            }

            if(mod == manual) {
                if(gp1.checkToggle(Gamepad.Button.RIGHT_BUMPER))
                    outtake.openSlider()
                if(gp1.checkToggle(Gamepad.Button.LEFT_BUMPER))
                    outtake.openLowSlider()
            }

            if(mod == endgame) {
                val ly = gp2.left_stick_y
                carousel.moveCarousel(gp1.right_trigger-gp1.left_trigger.toDouble())
                if(ly > 0.1)
                    CustomElement.openServos()


            }

            //hw.motors.move(direction, power*driveScale, rotPower*driveScale)

            telemetry.addData("Outtake target", outtake.outtakePosition)
            telemetry.addData("Outtake power", outtake.powerSlider())
            outtake.printPosition(telemetry)
            telemetry.addData("TouchSensor",outtake.touchSensor.isPressed)
            telemetry.addData("ArmY",CustomElement.servo1.position)
            telemetry.addData("ArmZ",CustomElement.servo2.position)
            telemetry.update()
        }
    }

    ///The direction in which the robot is translating
    private val direction: Double
        get() {
            val x = gamepad1.left_stick_x.toDouble()
            val y = -gamepad1.left_stick_y.toDouble()

            return atan2(y, x) / Math.PI * 180.0 - 90.0
        }

    /// Rotation around the robot's Z axis.
    private val rotation: Double
        get() = -gamepad1.right_stick_x.toDouble()

    /// Translation speed.
    private val speed: Double
        get() {
            val x = gamepad1.left_stick_x.toDouble()
            val y = gamepad1.left_stick_y.toDouble()

            return Math.sqrt((x * x) + (y * y))
        }

}
