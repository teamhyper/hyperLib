package org.usfirst.frc.team69.util.auto.desktoptester;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;

import org.usfirst.frc.team69.util.CommandBuilder;
import org.usfirst.frc.team69.util.auto.AutonomousInfo;
import org.usfirst.frc.team69.util.auto.AutonomousRoutine;
import org.usfirst.frc.team69.util.auto.AutonomousStrategy;

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
                
        AutonomousInfo info = new AutonomousInfo.Builder()
                .addStrategy(strat1)
                .addDefault(strat2)
                .build();
        
        SmartDashboard.putData(info);
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
