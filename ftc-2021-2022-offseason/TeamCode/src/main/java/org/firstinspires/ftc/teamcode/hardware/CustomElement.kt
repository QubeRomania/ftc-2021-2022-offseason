package org.firstinspires.ftc.teamcode.hardware

import com.qualcomm.robotcore.hardware.*


class CustomElement(hwMap: HardwareMap) {
    private val outtakeServo1 = hwMap.servo["intakeServo1"] ?: throw Exception("Failed to find servo outtakeServo1")
    private val outtakeServo2 = hwMap.servo["intakeServo2"] ?: throw Exception("Failed to find servo outtakeServo2")
    companion object {
        const val servo1Open = 0.60
        const val servo1Close = 0.80
        const val servo2Open = 0.60
        const val servo2Close = 0.80
    }

    fun setServoOpen() {
        setServoPositions(servo1Open)
        setServoPositions(servo2Open)
    }

    fun setServoClose() {
        setServoPositions(servo1Close)
        setServoPositions(servo2Close)
    }

    private fun setServoPositions(pos: Double) {
        outtakeServo1.position = pos
        outtakeServo2.position = pos
    }
}
