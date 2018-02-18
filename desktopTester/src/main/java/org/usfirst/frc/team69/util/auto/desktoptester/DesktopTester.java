package org.usfirst.frc.team69.util.auto.desktoptester;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;

import org.usfirst.frc.team69.util.CommandBuilder;
import org.usfirst.frc.team69.util.auto.AutonomousInfo;
import org.usfirst.frc.team69.util.auto.AutonomousRoutine;

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
        
        
        AutonomousInfo data = new AutonomousInfo();
        data.addDefault(rtn);
        data.addRoutine(sub);
        
        SmartDashboard.putData(data);
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
