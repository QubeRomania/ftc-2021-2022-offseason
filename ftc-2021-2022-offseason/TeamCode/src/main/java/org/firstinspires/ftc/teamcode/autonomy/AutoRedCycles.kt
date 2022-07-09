package org.firstinspires.ftc.teamcode.autonomy

import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.geometry.Vector2d
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference
import org.firstinspires.ftc.teamcode.drive.DriveConstants
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive
import org.firstinspires.ftc.teamcode.hardware.Hardware

@Autonomous
@Config
class AutoRedRight : AutoBase() {
    private val startPose = Pose2d(0.0, 0.0, Math.toRadians(-90.0))
    private val wallPose = Pose2d(0.0,0.0,Math.toRadians(0.0))
    private val shippingHub = Pose2d(-8.0,10.0,Math.toRadians(115.0))
    private val freightPose = Pose2d(32.0,0.0,Math.toRadians(0.0))

    override fun preInit() {
        super.preInit()
        telemetry.addLine("Initializing...")
        telemetry.update()
        drive.poseEstimate = startPose
        hw.customElement.initArm()
    }

    override fun Hardware.run() {
        //Stop streaming
        //webcam.stopStreaming()

        drive.followTrajectory(
                drive.trajectoryBuilder(startPose,true)
                        .addDisplacementMarker{
                            outtake.holdFreight()
                            outtake.openSlider()
                        }
                        .addTemporalMarker(0.5) {
                            outtake.releaseServo()
                        }
                        .splineTo(Vector2d(shippingHub.x+1.5,shippingHub.y),shippingHub.heading,
                                SampleMecanumDrive.getVelocityConstraint(60.0, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                                SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL)
                        )
                        .build()
        )
        sleep(50)

        outtake.releaseFreight()

        sleep(200)

        cycleFreight(4)

        //Park in warehouse
        drive.followTrajectory(
                drive.trajectoryBuilder(drive.poseEstimate)
                        .addDisplacementMarker{
                            hw.outtake.closeFlicker()
                            hw.outtake.closeServo()
                        }
                        .addTemporalMarker(0.1){
                            hw.outtake.closeSlider()
                        }
                        .lineToLinearHeading(Pose2d(startPose.x,startPose.y-4.5,Math.toRadians(0.0)))
                        .build()
        )

        telemetry.addData("Normal Orientation", Math.toDegrees(drive.poseEstimate.heading))
        //telemetry.addData("Orientation IMU", Math.toDegrees(drive.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.RADIANS).firstAngle.toDouble()))
        telemetry.update()

        drive.followTrajectory(
                drive.trajectoryBuilder(drive.poseEstimate)
                        .lineTo(Vector2d(freightPose.x, freightPose.y-4.8),
                                SampleMecanumDrive.getVelocityConstraint(60.0, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                                SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                        .build()
        )

        telemetry.addData("Current Pose", "x : %.2f, y : %.2f, heading %.2f", drive.poseEstimate.x, drive.poseEstimate.y, drive.poseEstimate.heading)
        telemetry.update()
    }

    fun cycleFreight(cycles : Int)
    {
        var k = 0.0
        var off = 0.0
        var otime = 0.0
        for (i in 1..cycles) {
            when(i)
            {
                1 -> {
                    k = 1.7
                    off = 0.0
                    otime = 0.0
                }
                2 -> {
                    k = 2.3
                    off = 4.5
                    otime = 0.1
                }
                3 -> {
                    k = 3.2
                    off = 8.0
                    otime = 0.2
                }
                4 -> {
                    k = 4.4
                    off = 9.0
                    otime = 0.3
                }
            }
            drive.followTrajectory(
                    drive.trajectoryBuilder(drive.poseEstimate)
                            .addDisplacementMarker{
                                hw.outtake.closeFlicker()
                                hw.outtake.closeServo()
                            }
                            .addTemporalMarker(0.1){
                                hw.outtake.closeSlider()
                            }
                            .lineToLinearHeading(Pose2d(startPose.x,startPose.y-k,Math.toRadians(3.0)))
                            .build()
            )

            telemetry.addData("Normal Orientation", Math.toDegrees(drive.poseEstimate.heading))
            //telemetry.addData("Orientation IMU", Math.toDegrees(drive.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.RADIANS).firstAngle.toDouble()))
            telemetry.update()

            drive.followTrajectory(
                    drive.trajectoryBuilder(drive.poseEstimate)
                            .addDisplacementMarker{
                                startIntake()
                            }
                            .lineTo(Vector2d(freightPose.x+off, freightPose.y-k),
                            SampleMecanumDrive.getVelocityConstraint(40.0, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                            SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                            .build()
            )

            drive.followTrajectory(
                    drive.trajectoryBuilder(drive.poseEstimate, true)
                            .addDisplacementMarker{
                                hw.outtake.holdFreight()
                                reverseIntake()
                            }
                            .lineTo(Vector2d(startPose.x, startPose.y-k))
                            .addDisplacementMarker{
                                hw.outtake.holdFreight()
                                hw.outtake.openSlider()
                            }
                            .addTemporalMarker(1.5) {
                                hw.outtake.releaseServo()
                            }
                            .splineTo(Vector2d(shippingHub.x-i*1.8, shippingHub.y), shippingHub.heading)
                            .build()

            )
            telemetry.addData("Normal Orientation", drive.poseEstimate.heading)
            //telemetry.addData("Orientation IMU",(6.0 + drive.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.RADIANS).firstAngle) % 6.0)
            telemetry.update()

            hw.outtake.releaseFreight()
            sleep(150)

            //drive.poseEstimate = Pose2d(drive.poseEstimate.x,drive.poseEstimate.y,(6.0 + drive.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.RADIANS).firstAngle) % 6.0)
        }
    }
}