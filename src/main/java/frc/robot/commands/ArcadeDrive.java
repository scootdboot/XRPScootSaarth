package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.XRPDrivetrain;

public class ArcadeDrive extends Command {
    private final XRPDrivetrain m_drivetrain;

    private final DoubleSupplier m_leftYSupplier;
    private final DoubleSupplier m_rightXSupplier;

    public ArcadeDrive(XRPDrivetrain drivetrain, DoubleSupplier leftYSupplier, DoubleSupplier rightXSupplier) {
        m_drivetrain = drivetrain;

        m_leftYSupplier = leftYSupplier;
        m_rightXSupplier = rightXSupplier;

        addRequirements(m_drivetrain);
    }

    public void execute() {
        m_drivetrain.arcadeDrive(-m_leftYSupplier.getAsDouble(), -m_rightXSupplier.getAsDouble());
    }
}
