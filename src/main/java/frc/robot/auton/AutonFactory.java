package frc.robot.auton;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
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
    static AnalogInput sensed = new AnalogInput(1);

    public static Command test(XRPDrivetrain xrpDrivetrain) {
        return Commands.run(() -> sensed.getValue());
    }

}
