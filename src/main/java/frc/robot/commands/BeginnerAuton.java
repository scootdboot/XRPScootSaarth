package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.subsystems.XRPDrivetrain;

public class BeginnerAuton extends Command {
    private final XRPDrivetrain m_xrpDrivetrain;
        
    public BeginnerAuton(XRPDrivetrain xrpDrivetrain) {
        m_xrpDrivetrain = xrpDrivetrain;
    }

    public void initialize() {
        System.out.println("run auton command");
        Commands.sequence(
            Commands.race(
                Commands.run(() -> m_xrpDrivetrain.tankDrive(1, 1), m_xrpDrivetrain),
                Commands.waitSeconds(2)
            ),
            Commands.race(
                Commands.run(() -> m_xrpDrivetrain.tankDrive(-1, 1), m_xrpDrivetrain),
                Commands.waitSeconds(0.7)
            ),
            Commands.race(
                Commands.run(() -> m_xrpDrivetrain.tankDrive(1, 1), m_xrpDrivetrain),
                Commands.waitSeconds(2)
            )
        ).schedule();
    }
}
