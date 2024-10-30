package frc.robot.subsystems;

import edu.wpi.first.wpilibj.xrp.XRPServo;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class XRPArm extends SubsystemBase {
    private final XRPServo m_xrpServo;

    public XRPArm() {
        m_xrpServo = new XRPServo(Constants.Arm.port);
    }

    public double getAngle() {
        return m_xrpServo.getAngle();
    }

    public void setAngle(double angle) {
        m_xrpServo.setAngle(angle);
    }
}
