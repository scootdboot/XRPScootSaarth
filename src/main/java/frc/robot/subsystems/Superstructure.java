package frc.robot.subsystems;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.event.EventLoop;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants;

public class Superstructure {
    private final XRPArm m_xrpArm;
    private final XRPDrivetrain m_xrpDrivetrain;

    public xrpState m_currentState = xrpState.IDLE;
    
    private final EventLoop m_sensorEventLoop = new EventLoop();
    private final EventLoop m_stateTrgEventLoop = new EventLoop();
    private final EventLoop m_stateUpdateEventLoop = new EventLoop();

    private final Trigger stateTrg_idle = new Trigger(m_stateTrgEventLoop, () -> m_currentState == xrpState.IDLE);
    private final Trigger stateTrg_movingForward = new Trigger(m_stateTrgEventLoop, () -> m_currentState == xrpState.MOVING_FOWARD);
    private final Trigger stateTrg_spinning = new Trigger(m_stateTrgEventLoop, () -> m_currentState == xrpState.SPINNING);

    // this defines the reflectance sensors
    private final AnalogInput m_leftReflectanceSensor = new AnalogInput(0);
    private final AnalogInput m_rightReflectanceSensor = new AnalogInput(1);
    // this is a trigger which essentially goes true when the reflectance sensors see black
    private final Trigger trg_reflectanceSensorSeesBlack = new Trigger(m_sensorEventLoop,
        () -> (m_leftReflectanceSensor.getVoltage() + m_rightReflectanceSensor.getVoltage()) / 2 > Constants.LineSensors.blackThreshold);
    private final Trigger trg_finishMovement = new Trigger(m_stateUpdateEventLoop, trg_reflectanceSensorSeesBlack.and(stateTrg_movingForward));

    // defined here because it is a button in the constructor!
    private final Trigger trg_finishSpin;

    public Superstructure(XRPArm xrpArm, XRPDrivetrain xrpDrivetrain, Trigger finishSpin) {
        m_xrpArm = xrpArm;
        m_xrpDrivetrain = xrpDrivetrain;

        trg_finishSpin = new Trigger(m_stateUpdateEventLoop, finishSpin.and(stateTrg_spinning));

        configureTriggerBindings();

        System.out.println("construct superstructure");
    }

    public void start() {
        if (stateTrg_idle.getAsBoolean()) {
            m_currentState = xrpState.MOVING_FOWARD;
        }

        System.out.println("superstructure kickstart");
    }

    private void configureTriggerBindings() {
        trg_finishMovement.onTrue(increaseState());
        trg_finishSpin.onTrue(increaseState());

        // TODO: CHECK IF THIS ACTUALLY CONSISTENTLY MOVES FOWARD
        stateTrg_movingForward.onTrue(
            Commands.run(() -> m_xrpDrivetrain.arcadeDrive(1, 0))
        ).onFalse(
            Commands.runOnce(() -> m_xrpDrivetrain.arcadeDrive(0, 0))
        );

        // TODO: CHECK IF THIS ACTUALLY SPINS PROPERLY
        stateTrg_spinning.onTrue(
            Commands.run(() -> m_xrpDrivetrain.arcadeDrive(1, 1))
        ).onFalse(
            Commands.runOnce(() -> m_xrpDrivetrain.arcadeDrive(0, 0))
        );
    }

    private Command increaseState() {
        return Commands.runOnce(() -> {
            switch (m_currentState) {
                case IDLE:
                    m_currentState = xrpState.MOVING_FOWARD;
                    break;
                case MOVING_FOWARD:
                    m_currentState = xrpState.SPINNING;
                    break;
                case SPINNING:
                    m_currentState = xrpState.IDLE;
            }
        }
        );
    }

    public void fastPeriodic() {
        m_sensorEventLoop.poll();
        m_stateTrgEventLoop.poll();
        m_stateUpdateEventLoop.poll();
    }

    private enum xrpState {
        IDLE(0),
        MOVING_FOWARD(1),
        SPINNING(2);
        
        private final int m_idx;

        private xrpState(int idx) {
            m_idx = idx;
        }

        public int getId() {
            return m_idx;
        }
    }
}
