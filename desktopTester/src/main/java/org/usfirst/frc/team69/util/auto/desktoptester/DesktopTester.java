package org.usfirst.frc.team69.util.auto.desktoptester;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;

import org.usfirst.frc.team69.util.auto.AutonomousInfo;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.HLUsageReporting;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DesktopTester {

    public static void runTester() {
        HLUsageReporting.SetImplementation(new HLUsageReporting.Null());
        NetworkTableInstance inst = NetworkTableInstance.getDefault();
        inst.setNetworkIdentity("Robot");
        inst.startServer("/home/james/networktables.ini");
        
        AutonomousInfo.Builder builder = new AutonomousInfo.Builder();
        ExampleAutoPicker.declareStrategies(builder);
        AutonomousInfo info = builder.build();
        SmartDashboard.putData(info);
        
        VisionSystem visionSystem = new VisionSystem();
        
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
