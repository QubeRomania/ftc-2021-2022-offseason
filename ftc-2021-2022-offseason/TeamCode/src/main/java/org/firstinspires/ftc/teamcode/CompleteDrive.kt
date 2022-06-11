package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.hardware.Hardware
import kotlin.math.sqrt

@TeleOp(name = "CompleteDrive", group = "Main")
class CompleteDrive: OpMode() {
    override fun preInit() {
        //hw.customElement.servoClaw.position = 0.38
        //hw.customElement.servoArm.position = 0.62
    }

    override fun preInitLoop() {
        telemetry.addLine("Waiting for start...")
        telemetry.update()
        idle()
    }
    override fun Hardware.run() {
        val gp1 = Gamepad(gamepad1)
        val gp2 = Gamepad(gamepad2)

        var customOk = false
        var intakeOk = false
        var isDelivering = false
        var isUp = false
        var isMid = false
        var isLow = false
        var isClosed = false

        var isReleased = false

        val slowScale = 0.2

        var mod = 1
        val normal = 1
        val manual = 2
        val endgame = 2


        while(opModeIsActive()){
            val power = speed
            val rotPower = rotation

            // Mod selection: normal, manual, endgame
            if(gp2.checkToggle(Gamepad.Button.Y)) {
                mod = normal
            }
            if(gp2.checkToggle(Gamepad.Button.START)) {
                mod = if(mod == manual)
                    endgame
                else
                    manual
            }

            // Chassis rotation
            hw.motors.move(direction, power, rotPower)

            // Mod normal
            if(mod == normal) {

                // Intake forward and backward gp1 and gp2
                intake.setIntakePower(gp1.right_trigger-gp1.left_trigger.toDouble())
                intake.setIntakePower(gp2.right_trigger-gp2.left_trigger.toDouble())

                // Duck deliver gp1
                if(gp1.checkToggle(Gamepad.Button.X)) {
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

                /*
                // Open/close trap gp1
                if(gp1.checkToggle(Gamepad.Button.A)) {
                    if(!intakeOk)
                    {
                        intake.openTrap()
                        intakeOk = true
                    }
                    else
                    {
                        intake.closeTrap()
                        intakeOk = false
                    }
                }
                 */

                /*
                // Open/close trap gp2
                if(gp2.checkToggle(Gamepad.Button.A)) {
                    if(!intakeOk)
                    {
                        intake.openTrap()
                        intakeOk = true
                    }
                    else
                    {
                        intake.closeTrap()
                        intakeOk = false
                    }
                }
                */

                if(gp2.checkToggle(Gamepad.Button.A))
                {
                    isReleased = if(!isReleased) {
                        outtake.releaseFreight()
                        true
                    } else {
                        outtake.closeFlicker()
                        false
                    }
                }


                // Up box gp2
                if(gp2.checkToggle(Gamepad.Button.RIGHT_BUMPER)) {
                    if(!isUp) {
                        outtake.openSlider()
                        isUp = true
                        isClosed = false
                    }
                }

                // Close box gp2
                if(gp2.checkToggle(Gamepad.Button.LEFT_BUMPER)) {
                    if(!isClosed) {
                        outtake.closeSlider()
                        isUp = false
                        isClosed = true
                    }
                }

                // Mid box gp2
                if(gp2.checkToggle(Gamepad.Button.Y)) {
                    if(!isMid){
                        outtake.openMidSlider()
                        isMid = true
                        isLow = false
                        isUp = true
                    }
                }

                // Low box gp2
                if(gp2.checkToggle(Gamepad.Button.B)) {
                    if(!isLow) {
                        outtake.openLowSlider()
                        isLow = true
                        isUp = true
                        isMid = false
                    }
                }
                /*

                // Open/close claw gp2
                if(gp2.checkToggle(Gamepad.Button.X)) {
                    if(!customOk)
                    {
                        customElement.openClaw()
                        customOk = true
                    }
                    else
                    {
                        customElement.closeClaw()
                        customOk = false
                    }
                }
                */
            }

            // Mod manual
            if(mod == manual) {

                // Up/down slider gp1
                if(gp1.checkToggle(Gamepad.Button.RIGHT_BUMPER))
                    outtake.openSlider()
                if(gp1.checkToggle(Gamepad.Button.LEFT_BUMPER))
                    outtake.openLowSlider()
            }

            // Mod endgame
            if(mod == endgame) {

                // Forward and backward duck delivery gp1
                carousel.moveCarousel(gp1.right_trigger-gp1.left_trigger.toDouble())

                /*
                // Move arm gp2
                if(gp2.left_stick_y > 0.1)
                    customElement.moveOneUp()

                if(gp2.left_stick_y < -0.1)
                    customElement.moveOneDown()

                // Preset arm positions for element delivery and pickup gp2
                if(gp2.dpad_up)
                {
                    customElement.openServoArm()
                }
                if(gp2.dpad_down)
                {
                    customElement.closeServoArm()
                }

                 */

            }

            telemetry.addData("Outtake target", outtake.outtakePosition)
            outtake.printPosition(telemetry)
            //telemetry.addData("TouchSensor",outtake.touchSensor.isPressed)
            //telemetry.addData("ArmY",customElement.servoClaw.position)
            //telemetry.addData("ArmZ",customElement.servoArm.position)
            telemetry.update()
        }
    }


    ///The direction in which the robot is translating
    private val direction: Double
        get() {
            val x = gamepad1.left_stick_x.toDouble()
            val y = -gamepad1.left_stick_y.toDouble()

            return kotlin.math.atan2(y, x) / Math.PI * 180.0 - 90.0
        }

    /// Rotation around the robot's Z axis.
    private val rotation: Double
        get() = -gamepad1.right_stick_x.toDouble()

    /// Translation speed.
    private val speed: Double
        get() {
            val x = gamepad1.left_stick_x.toDouble()
            val y = gamepad1.left_stick_y.toDouble()

            return sqrt((x * x) + (y * y))
        }

}