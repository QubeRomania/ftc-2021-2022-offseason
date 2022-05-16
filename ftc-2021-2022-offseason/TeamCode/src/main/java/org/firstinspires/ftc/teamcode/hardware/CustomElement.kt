package org.firstinspires.ftc.teamcode.hardware

import com.qualcomm.robotcore.hardware.*


class CustomElement(hwMap: HardwareMap) {
    private val servo1 = hwMap.servo["intakeServo1"] ?: throw Exception("Failed to find servo outtakeServo1")
    private val servo2 = hwMap.servo["intakeServo2"] ?: throw Exception("Failed to find servo outtakeServo2")
    companion object {
        const val servo1Open = 0.60
        const val servo1Close = 0.80
        const val servo2Open = 0.60
        const val servo2Close = 0.80
    }

    fun setServoOpen1() {
        setServoPositions1(servo1Open)
    }
    
    fun setServoOpen2() {
        setServoPositions2(servo2Open)
    }

    fun setServoClose1() {
        setServoPositions1(servo1Close)
    }
    fun setServoClose2() {
        setServoPositions2(servo2Close)
    }

    private fun setServoPositions1(pos: Double) {
        servo1.position = pos
    }
    
    private fun setServoPositions2(pos: Double) {
        servo2.position = pos
    }
}
