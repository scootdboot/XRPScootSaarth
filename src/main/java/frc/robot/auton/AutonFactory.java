package frc.robot.auton;

import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.Constants;
import frc.robot.subsystems.XRPDrivetrain;

public class AutonFactory {
    public static Command firstAuton(XRPDrivetrain xrpDrivetrain) {
        return Commands.sequence(
            Commands.runOnce(() -> System.out.println("FIRST_AUTON")),
            Commands.race(
                Commands.run(() -> xrpDrivetrain.tankDrive(1, 1), xrpDrivetrain),
                Commands.waitSeconds(2)
            ),
            Commands.race(
                Commands.run(() -> xrpDrivetrain.tankDrive(-1, 1), xrpDrivetrain),
                Commands.waitSeconds(0.7)
            ),
            Commands.race(
                Commands.run(() -> xrpDrivetrain.tankDrive(1, 1), xrpDrivetrain),
                Commands.waitSeconds(2)
            )
        ).withName("First Auton");
    }

    public static Command blackLineSensorAuton(XRPDrivetrain xrpDrivetrain, AnalogInput m_leftReflectanceSensor, AnalogInput m_rightReflectanceSensor) {
        final double blackThreshold = Constants.LineSensor.blackThreshold;
        BooleanSupplier seesBlack = () -> m_leftReflectanceSensor.getVoltage() + m_rightReflectanceSensor.getVoltage() / 2 >= blackThreshold;
        return Commands.race(
            Commands.run(() -> xrpDrivetrain.tankDrive(1, 1), xrpDrivetrain),
            Commands.waitUntil(seesBlack)
        ).withName("Black Line Sensor Auton");
    }
}
