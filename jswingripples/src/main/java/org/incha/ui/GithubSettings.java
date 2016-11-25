package org.incha.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

import org.incha.core.JavaProject;
import org.kohsuke.github.*;

/**
 * Created by Amoreno on 11/23/16.
 */
public class GithubSettings {

    private static final JPanel configPanel = new JPanel(new BorderLayout());
    private static final JTextField url = new JTextField(20);
    private static final JLabel crntRepo = new JLabel("Current Repository: ");
    private static final JButton testConnection = new JButton("Test");
    private static final JButton connect = new JButton("Connect");    
    private static JavaProject pr;
   

    public static JPanel getConfigPanel(JavaProject project){
    	pr = project;
    	crntRepo.setText(crntRepo.getText() + obtainCurrentRepo(pr));

        JPanel north = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel center = new JPanel(new FlowLayout(FlowLayout.LEFT));

        north.add(new JLabel("Github URL repository: "), BorderLayout.WEST);
        north.add(url, BorderLayout.EAST);
        north.add(testConnection, BorderLayout.EAST);
        north.add(connect, BorderLayout.EAST);
        
        center.add(crntRepo,BorderLayout.WEST);

        configPanel.add(north, BorderLayout.NORTH);
        configPanel.add(center, BorderLayout.CENTER);

        addButtonListeners(testConnection,connect);

        return configPanel;
    }

    private static void addButtonListeners(JButton testConnection, JButton connect){

        testConnection.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        connect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GitHub github = null;
                GHRepository myGHRep;
                try {
                    github = GitHub.connectAnonymously();
                    String repo = formatURLRepo(url.getText());
                    url.setText("");
                    System.out.println(repo);
                    if (repo.compareTo("Error") == 0){
                    	return;
                    }
                    myGHRep = github.getRepository(repo);
                    
                    /* ---- Just for demo2 ----*/
//                    List<GHIssue> issues = myGHRep.getIssues(GHIssueState.ALL);
//                    for (GHIssue issue : issues){
//                    	System.out.println(issue.getTitle());
//                    } 
                    
                    pr.getGHRepo().replaceRepository(repo);                    
                    crntRepo.setText("Current Repository: " + repo);
                    /* ----------------------- */
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                
            }
        });
    }
    
    /**
     * This method should look for the current repository for the project
     * If there is no, return empty String 
     */
    private static String obtainCurrentRepo(JavaProject project){
    	return project.getGHRepo().getCurrentRepository();
    }
    
    private static String formatURLRepo (String urlRepo){
    	if(urlRepo.startsWith("https://github.com/")){
    		if (urlRepo.endsWith(".git")){
    			return urlRepo.split("https://github.com/")[1].split(".git")[0];
    		}
    		return urlRepo.split("https://github.com/")[1];
    	} else if (urlRepo.startsWith("http://github.com/")){
    		if (urlRepo.endsWith(".git")){
    			return urlRepo.split("http://github.com/")[1].split(".git")[0];
    		}
    		return urlRepo.split("http://github.com/")[1];
    	}
    	return "Error";
    			 
    }
}