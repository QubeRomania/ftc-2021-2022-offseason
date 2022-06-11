package org.firstinspires.ftc.teamcode.hardware

import com.qualcomm.robotcore.hardware.*


class Intake(hwMap: HardwareMap) {
    val intakeMotor = hwMap.dcMotor.get("intakeMotor") ?: throw Exception("failed to find motor intakeMotor")
    //val sensor = hwMap.colorSensor.get("colorSensor") ?: throw Exception("failed to find sensor colorSensor")
    companion object {
        //val servoOpen = 0.60
        //val servoClose = 0.80
    }

    init {
        intakeMotor.direction = DcMotorSimple.Direction.FORWARD
        stopIntake()
    }


    fun setIntakePower(power: Double){
        intakeMotor.power = power
    }

    fun stopIntake() {
        setIntakePower(0.0)
    }

    fun stop() {
        stopIntake()
    }
}
