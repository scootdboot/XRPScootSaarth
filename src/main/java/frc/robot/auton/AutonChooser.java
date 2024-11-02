package frc.robot.auton;

import java.util.EnumMap;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;

/**
 * Static class
 */
public class AutonChooser {
    public enum AutonOption {
        DO_NOTHING("Do nothing"),
        FIRST_AUTON("First auton"),
        TEST("Test");

        private String m_name;

        private AutonOption(String name) {
            m_name = name;
        }

        public String getName() {
            return m_name;
        }
    }

    private static EnumMap<AutonOption, Command> m_autonCommandMap = new EnumMap<>(AutonOption.class);
    private static SendableChooser<AutonOption> m_autonChooser = new SendableChooser<>();

    public static void mapAutonCommand(AutonOption autonOption, Command auton) {
        m_autonCommandMap.put(autonOption, auton);
        m_autonChooser.addOption(autonOption.name(), autonOption);
    }

    public static void putChooser() {
        SmartDashboard.putData(m_autonChooser);
    }

    public static Command getSelectedAuton() {
        return m_autonCommandMap.get(m_autonChooser.getSelected());
    }
}