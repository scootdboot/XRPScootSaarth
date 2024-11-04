// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.auton.AutonChooser;
import frc.robot.auton.AutonFactory;
import frc.robot.auton.AutonChooser.AutonOption;
import frc.robot.subsystems.Superstructure;
import frc.robot.subsystems.XRPArm;
import frc.robot.subsystems.XRPDrivetrain;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandPS4Controller;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
    // The robot's subsystems and commands are defined here...
    private final XRPDrivetrain m_xrpDrivetrain = new XRPDrivetrain();
    private final XRPArm m_xrpArm = new XRPArm(4);
    
    private final CommandXboxController m_controller = new CommandXboxController(0);

    private final Superstructure m_superstructure = new Superstructure(m_xrpArm, m_xrpDrivetrain, m_controller.a(), m_controller.b());

    /** The container for the robot. Contains subsystems, OI devices, and commands. */
    public RobotContainer() {
        // Configure the button bindings
        configureButtonBindings();
        mapAutonOptions();
        AutonChooser.putChooser();
    }

    /**
     * Use this method to define your button->command mappings. Buttons can be created by
    * instantiating a {@link edu.wpi.first.wpilibj.GenericHID} or one of its subclasses ({@link
    * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
    * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
    */
    private void configureButtonBindings() {
        m_xrpDrivetrain.setDefaultCommand(getDriveCommand());
        m_xrpArm.setDefaultCommand(getArmCommand());
    }

    private void mapAutonOptions() {
        
    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand() {
        return AutonChooser.getSelectedAuton();
    }

    public Command getDriveCommand() {
        return Commands.run(() -> m_xrpDrivetrain.tankDrive(-m_controller.getLeftY(), -m_controller.getRightY()), m_xrpDrivetrain);
    }

    public Command getArmCommand() {
        return XRPArm.getTriggersMoveArm(m_xrpArm, m_controller::getLeftTriggerAxis, m_controller::getRightTriggerAxis, 
            m_controller.a());
    }

    public void startSuperstructure() {
        m_superstructure.start();
    }

    public Runnable superstructureFastPeriodic() {
        return m_superstructure::fastPeriodic;
    }
}
