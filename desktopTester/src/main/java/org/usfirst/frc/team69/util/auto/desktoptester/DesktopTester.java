package org.usfirst.frc.team69.util.auto.desktoptester;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;

import org.usfirst.frc.team69.util.CommandBuilder;
import org.usfirst.frc.team69.util.auto.AutoPref;
import org.usfirst.frc.team69.util.auto.AutonomousInfo;
import org.usfirst.frc.team69.util.auto.AutonomousRoutine;
import org.usfirst.frc.team69.util.auto.AutonomousStrategy;
import org.usfirst.frc.team69.util.pref.DoublePreference;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.HLUsageReporting;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DesktopTester {

    static class MySubroutine extends AutonomousRoutine {
        {
            addDoublePreference("someSubthing");
            addDoublePreference("bar");
        }
        
        @Override
        public void build(CommandBuilder build) {
            
        }
        
    }
    
    static class MyRoutine extends AutonomousRoutine {
        {
            addDoublePreference("foo");
            addDoublePreference("bar");
            addSubroutine(new MySubroutine());
        }
        
        @Override
        public void build(CommandBuilder build) {
            
        }
    }
    
    public static class ShoulderUpParallel extends AutonomousRoutine {        
        DoublePreference upTime = addDoublePreference("upTime");
        
        @Override
        public void build(CommandBuilder builder) {
            /*
            builder.parallel(Robot.shoulder.upCmd(), upTime.get());
            */
        }
    }
    
    public static class UnGrip extends AutonomousRoutine {
        DoublePreference unGripTime = addDoublePreference("unGripTime");
        
        @Override
        public void build(CommandBuilder builder) {
            /*
            builder.sequential(Robot.grippah.unGrip(), unGripTime.get());
            */
        }
    }
    
    public static class F1RightSwitch extends AutonomousRoutine {
        @AutoPref 
        public DoublePreference 
                offWallNudgeSpeed,
                offWallNudgeTime,
                fwd1Speed,
                fwd1Time,
                fwd2Speed,
                fwd2Time,
                fwd3Speed,
                fwd3Time,
                fwd4Speed,
                fwd4Time;
        
        AutonomousRoutine shldrUp = addSubroutine(new ShoulderUpParallel());
        AutonomousRoutine unGrip = addSubroutine(new UnGrip());
        
        @Override
        public void build(CommandBuilder builder) {
            /*
            builder.sequential(AutoDrive.forwardCmd(offWallNudgeSpeed), offWallNudgeTime.get());
            builder.sequential(AutoDrive.rotate30Cmd());
            builder.sequential(AutoDrive.forwardCmd(fwd1Speed), fwd1Time.get());
            builder.sequential(AutoDrive.rotateNegative30Cmd());
            // 1st 3 lines = Right Side Line Up to Get Around Switch
            builder.sequential(AutoDrive.forwardCmd(fwd2Speed), fwd2Time.get());
            builder.sequential(AutoDrive.rotateNegative90Cmd());
            builder.sequential(AutoDrive.forwardCmd(fwd3Speed), fwd3Time.get());
            builder.parallel(AutoDrive.rotateNegative90Cmd());
            shldrUp.build(builder);
            builder.sequential(AutoDrive.forwardCmd(fwd4Speed), fwd4Time.get());
            unGrip.build(builder);
            */
        }
    }
    
    public static void runTester() {
        HLUsageReporting.SetImplementation(new HLUsageReporting.Null());
        NetworkTableInstance inst = NetworkTableInstance.getDefault();
        inst.setNetworkIdentity("Robot");
        inst.startServer("networktables.ini");
        
        MyRoutine rtn = new MyRoutine();
        MySubroutine sub = new MySubroutine();
        
        
        AutonomousStrategy strat1 = new AutonomousStrategy.Builder("Some strategy")
                .addScenario("LRL", rtn)
                .addScenario("RRL", rtn)
                .addDefault(sub)
                .build();
        AutonomousStrategy strat2 = new AutonomousStrategy.Builder("Some other strategy")
                .addDefault(rtn)
                .build();
        
        AutonomousStrategy realStrat = new AutonomousStrategy.Builder("Real life strategy")
                .addScenario("LRL", new F1RightSwitch())
                .build();
                
        AutonomousInfo info = new AutonomousInfo.Builder()
                .addStrategy(strat1)
                .addStrategy(realStrat)
                .addDefault(strat2)
                .build();
        
        SmartDashboard.putData(info);
        
        while (true) {
            System.out.println(info.getSelection().getName());
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void main(String[] args) {
        
        JFrame jframe = new JFrame("HYPERLib desktop tester");
        jframe.setSize(500, 300);
        jframe.setLocationRelativeTo(null);
        jframe.setLayout(new BorderLayout());
        jframe.add(new JLabel("Tester is running.  Close this window when you're done."), BorderLayout.CENTER);
        jframe.setVisible(true);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        runTester();
    }
}
