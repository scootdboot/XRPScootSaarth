package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.XRPDrivetrain;

public class TankDrive extends Command {
    private final XRPDrivetrain m_drivetrain;

    private final DoubleSupplier m_leftYSupplier;
    private final DoubleSupplier m_rightYSupplier;

    public TankDrive(XRPDrivetrain drivetrain, DoubleSupplier leftYSupplier, DoubleSupplier rightYSupplier) {
        m_drivetrain = drivetrain;
        m_leftYSupplier = leftYSupplier;
        m_rightYSupplier = rightYSupplier;

        addRequirements(m_drivetrain);
    }

    public void execute() {
        m_drivetrain.tankDrive(-m_leftYSupplier.getAsDouble(), -m_rightYSupplier.getAsDouble());
    }
}
