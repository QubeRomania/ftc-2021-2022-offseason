package org.firstinspires.ftc.teamcode.tests.DriveTest

import com.qualcomm.robotcore.hardware.*

class ServoTestHardware(hwMap: HardwareMap) {

    companion object
    {
        const val servoClose = 0.0
        const val servoOpen = 1.0
        const val name = ""
        const val modifier = 0.02
    }

    val testServo = hwMap.servo[name] ?: throw Exception("Failed to find servo $name")

    var testPosition = servoClose

    init {
        closeServo()
    }

    fun moveDown()
    {
        testPosition--
        if(testPosition < 0.0)
            testPosition = 0.0

        setServoPositions(testPosition)
    }

    fun moveUp()
    {
        testPosition++
        if(testPosition > 1.0)
            testPosition = 1.0

        setServoPositions(testPosition)
    }

    fun openServo()
    {
        setServoPositions(servoOpen)
    }

    fun closeServo()
    {
        setServoPositions(servoClose)
    }

    fun setServoPositions(pos: Double) {
        testServo.position = pos
        testPosition = pos
    }

}