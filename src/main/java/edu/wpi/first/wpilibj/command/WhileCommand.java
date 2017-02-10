package edu.wpi.first.wpilibj.command;

import java.util.Collections;
import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj.command.Command;

/**
 * A command which loops repeatedly while checking a condition.
 * 
 * Given a command as a body and a condition to check, this command will
 * repeatedly check the condition, and execute the command if it is met.
 * 
 * The requirements of the body command are removed, and transferred over
 * to this command at construction.  This allows CommandGroups and cancelling
 * to work properly.
 * 
 * If this command is interrupted (cancelled or booted by another command),
 * then the body command will be cancelled.  If the body command is cancelled,
 * then it will start again on the next iteration.
 * 
 * Because HyperLib is currently in pre-release, these semantics may change
 * based on what we consider "reasonable defaults".
 * 
 * @author James Hagborg
 *
 */
public class WhileCommand extends Command {

    private final BooleanSupplier m_condition;
    private final Command m_body;
    
    private boolean m_hasFinished;
    
    /**
     * Construct a new WhileCommand from a condition and a body.
     * 
     * After construction, the body command's requirements are moved to this
     * command.  That means you should not re-use the body command after
     * passing it to this method.  Instead, create a new one.
     * 
     * @param condition The condition to check
     * @param body The command to run as the body
     */
    public WhileCommand(BooleanSupplier condition, Command body) {
        if (body == null) {
            throw new NullPointerException("body == null");
        }
        if (condition == null) {
            throw new NullPointerException("condition == null");
        }
        
        m_condition = condition;
        m_body = body;
        transferRequirements();
    }
    
    /**
     * Construct a new WhileCommand from a condition and a body, with the
     * given name.
     * 
     * After construction, the body command's requirements are moved to this
     * command.  That means you should not re-use the body command after
     * passing it to this method.  Instead, create a new one.
     * 
     * @param name The name of the command
     * @param condition The condition to check
     * @param body The command to run as the body
     */
    public WhileCommand(String name, BooleanSupplier condition, Command body) {
        super(name);
        if (body == null) {
            throw new NullPointerException("body == null");
        }
        if (condition == null) {
            throw new NullPointerException("condition == null");
        }
        
        m_condition = condition;
        m_body = body;
        transferRequirements();
    }
    
    @SuppressWarnings("unchecked")
    private void transferRequirements() {
        for (Object req : Collections.list(m_body.getRequirements())) {
            requires((Subsystem) req);
        }
        
        m_body.clearRequirements();
    }
    
    private void checkCondition() {
        m_hasFinished = !m_condition.getAsBoolean();
    }
    
    /**
     * Reset the state as if this command has not yet been run.
     * 
     * We don't call checkCondition() here, because the condition is already
     * checked in execute(), and it makes sense semantically to check the
     * condition once per iteration.  This might matter if the condition
     * passed in has side effects.
     */
    @Override
    protected void initialize() {
        m_hasFinished = false;
    }
    
    /**
     * If the loop has already ended, do nothing.  Otherwise, check if the
     * body command is running.  If it is not, check the condition and start
     * it if necessary.
     */
    @Override
    protected void execute() {
        if (m_hasFinished) {
            return;
        }
        
        if (!m_body.isRunning()) {
            checkCondition();
            if (!m_hasFinished) {
                m_body.start();
            }
        }
    }
    
    /**
     * Cancel the body command.
     */
    @Override
    protected void interrupted() {
        m_body.cancel();
    }
    
    /**
     * Check if the loop has finished.  This is only true if the body command
     * has ended and the condition returned false on most recent check.
     */
    @Override
    protected boolean isFinished() {
        return m_hasFinished;
    }

}
