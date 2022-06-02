package org.firstinspires.ftc.teamcode.hardware

import com.qualcomm.robotcore.hardware.*
import org.firstinspires.ftc.teamcode.hardware.Outtake.Companion.servo1Close
import org.firstinspires.ftc.teamcode.hardware.Outtake.Companion.servo1Open
import org.firstinspires.ftc.teamcode.hardware.Outtake.Companion.servo2Close


class CustomElement(hwMap: HardwareMap) {
    val servoClaw = hwMap.servo["customServo1"] ?: throw Exception("Failed to find servo customElementServo1")
    val servoArm = hwMap.servo["customServo2"] ?: throw Exception("Failed to find servo customElementServo2")
    companion object {
        const val servoOpenClaw = 0.60
        const val servoCloseClaw = 0.80
        const val servoOpenArm = 0.60
        const val servoCloseArm = 0.80
        const val modifier = 0.03
    }

    var position = servoCloseClaw

    private fun openServoX() {
        setServoPositionsClaw(servoOpenClaw)
    }

    fun openServoZ() {
        setServoPositionsArm(servoOpenArm)
    }

    fun closeServoX() {
        setServoPositionsClaw(servoCloseClaw)
    }

    fun closeServoZ() {
        setServoPositionsArm(servoCloseArm)
    }

    fun openClaw() {
        openServoX()
    }

    fun closeClaw() {
        closeServoX()
    }

    fun setServoPositionsClaw(pos: Double) {
        servoClaw.position = pos
    }

    fun setServoPositionsArm(pos: Double) {
        servoArm.position = pos
    }

    fun moveOneUp()
    {
        position = servoArm.position
        position += modifier

        if(position > servoCloseArm)
            position = servoCloseArm

        servoArm.position = position
    }

    fun moveOneDown()
    {
        position = servoArm.position
        position -= modifier

        if(position < servoOpenArm)
            position = servoOpenArm

        servoArm.position = position
    }

    fun stop() {

    }
}
