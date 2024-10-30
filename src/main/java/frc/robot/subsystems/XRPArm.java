package frc.robot.subsystems;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj.xrp.XRPServo;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class XRPArm extends SubsystemBase {
    private final XRPServo m_xrpServo;

    public static Command getTriggerMapToArmCommand(XRPArm xrpArm, DoubleSupplier trigger) {
        return Commands.run(() -> xrpArm.setAngle(trigger.getAsDouble()*135), xrpArm);
    }

    public static Command getTriggersMoveArm(XRPArm xrpArm, DoubleSupplier leftTrigger, DoubleSupplier rightTrigger) {
        final double maxMoveSpeed = Constants.Arm.maxTriggerMoveSpeed;
        return Commands.run(() -> xrpArm.setAngle(
            Math.max(Math.min(xrpArm.getAngle() - rightTrigger.getAsDouble()*maxMoveSpeed + leftTrigger.getAsDouble()*maxMoveSpeed, 135), 0)), xrpArm);
    }

    public XRPArm(int port) {
        m_xrpServo = new XRPServo(port);
    }

    public double getAngle() {
        return m_xrpServo.getAngle();
    }

    public void setAngle(double angle) {
        m_xrpServo.setAngle(angle);
    }
}
