package org.incha.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import org.kohsuke.github.*;

/**
 * Created by Amoreno on 11/23/16.
 */
public class GithubSettings {

    private static final JPanel configPanel = new JPanel();
    private static final JTextField url = new JTextField(20);
    //private static final JLabel crntRepo = new JLabel("Current Repository: ");
    private static final JButton testConnection = new JButton("Test");
    private static final JButton connect = new JButton("Connect");

    public static JPanel getConfigPanel(){

        configPanel.setLayout(new BoxLayout(configPanel, BoxLayout.Y_AXIS));
        //JPanel north = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel center = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel south = new JPanel(new FlowLayout(FlowLayout.LEFT));

        //north.add(new JLabel("Connect to Github repository"),BorderLayout.WEST);
        center.add(new JLabel("Github URL repository: "), BorderLayout.WEST);
        center.add(url, BorderLayout.EAST);
        center.add(testConnection, BorderLayout.EAST);
        center.add(connect, BorderLayout.EAST);
        //center.add()
        //south.add(crntRepo,BorderLayout.WEST);

        configPanel.add(center);
        configPanel.add(south);

        //addButtonListeners(testConnection,connect);

        return configPanel;
    }

/*    private static void addButtonListeners(JButton testConnection, JButton connect){

        testConnection.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        connect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GitHub github = null;
                try {
                    github = GitHub.connectAnonymously();
                    GHRepository myGHRep = github.getRepository("JSwingRipples2016/jswingripples");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                System.out.println(myGHRep.getDescription());
            }
        });
    }*/
}

