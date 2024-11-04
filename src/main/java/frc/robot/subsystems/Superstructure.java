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

    // these are triggers that activate when different states are active
    // they are just convenient because we can AND together other triggers
    // and these so specific things only happen in specific states
    private final Trigger stateTrg_idle = new Trigger(m_stateTrgEventLoop, () -> m_currentState == xrpState.IDLE);
    private final Trigger stateTrg_movingForwardBeforeLine = 
        new Trigger(m_stateTrgEventLoop, () -> m_currentState == xrpState.MOVING_BEFORE_LINE);
    private final Trigger stateTrg_spinning = new Trigger(m_stateTrgEventLoop, () -> m_currentState == xrpState.SPINNING);
    private final Trigger stateTrg_movingForwardAfterLine =
        new Trigger(m_stateTrgEventLoop, () -> m_currentState == xrpState.MOVING_AFTER_LINE);

    // this defines the reflectance sensors
    private final AnalogInput m_leftReflectanceSensor = new AnalogInput(0);
    private final AnalogInput m_rightReflectanceSensor = new AnalogInput(1);
    // this is a trigger which essentially goes true when the reflectance sensors see black
    private final Trigger trg_reflectanceSensorSeesBlack;
    private final Trigger trg_finishFirstMovement;

    // all triggers here rely on things in the constructor (things that must be passed to the superstructure
    // from the robot container)
    private final Trigger trg_finishSpin, trg_finishFinalMovement;

    // TODO: UNDO SEE BLACK FUCKERY
    public Superstructure(XRPArm xrpArm, XRPDrivetrain xrpDrivetrain, Trigger finishSpinButton, Trigger seeBlackButton) {
        m_xrpArm = xrpArm;
        m_xrpDrivetrain = xrpDrivetrain;

        trg_finishSpin = new Trigger(m_stateUpdateEventLoop, finishSpinButton.and(stateTrg_spinning));
        trg_reflectanceSensorSeesBlack = new Trigger(m_sensorEventLoop, seeBlackButton);
        // we should not need to mess with this in here after we are able to get an xrp with a working sensor
        trg_finishFirstMovement = new Trigger(m_stateUpdateEventLoop, trg_reflectanceSensorSeesBlack.and(stateTrg_movingForwardBeforeLine));
        trg_finishFinalMovement = new Trigger(m_stateUpdateEventLoop, finishSpinButton.negate().and(stateTrg_movingForwardAfterLine));

        configureTriggerBindings();

        System.out.println("construct superstructure");
    }

    public void start() {
        if (stateTrg_idle.getAsBoolean()) {
            m_currentState = xrpState.MOVING_BEFORE_LINE;
        }

        System.out.println("superstructure kickstart");
    }

    private void configureTriggerBindings() {
        // these ones should always check that they are in the correct state
        // before activating as they will not check here - it will just
        // set state to their following state
        trg_finishFirstMovement.onTrue(
            Commands.runOnce(() -> m_currentState = xrpState.SPINNING)
        );
        trg_finishSpin.onTrue(
            Commands.runOnce(() -> m_currentState = xrpState.MOVING_AFTER_LINE)
        );
        trg_finishFinalMovement.onTrue(
            Commands.runOnce(() -> m_currentState = xrpState.IDLE)
        );

        // below this point everything should actually get handled based on what
        // state the robot is in - everything about should just handle changing between states
        // TODO: CHECK IF THIS ACTUALLY CONSISTENTLY MOVES FOWARD
        stateTrg_movingForwardBeforeLine.onTrue(
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

        stateTrg_movingForwardAfterLine.onTrue(
            Commands.run(() -> m_xrpDrivetrain.arcadeDrive(1, 0))
        ).onFalse(
            Commands.runOnce(() -> m_xrpDrivetrain.arcadeDrive(0, 0))
        );
    }

    public void fastPeriodic() {
        // remember that this update order could cause issues!
        // if you see anything really weird it could be that
        // this isn't updated in an order that works
        m_sensorEventLoop.poll();
        m_stateTrgEventLoop.poll();
        m_stateUpdateEventLoop.poll();
    }

    private enum xrpState {
        IDLE(0),
        MOVING_BEFORE_LINE(1),
        SPINNING(2),
        MOVING_AFTER_LINE(3);
        
        private final int m_idx;

        private xrpState(int idx) {
            m_idx = idx;
        }

        public int getId() {
            return m_idx;
        }
    }
}
