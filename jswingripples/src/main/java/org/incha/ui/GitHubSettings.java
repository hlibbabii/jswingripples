package org.incha.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import org.incha.core.JavaProject;
import org.kohsuke.github.*;

/**
 * Created by Amoreno on 11/23/16.
 */
public class GitHubSettings {

    private static final JPanel configPanel = new JPanel(new BorderLayout());
    private static final JTextField url = new JTextField(20);
    private static final JLabel crntRepo = new JLabel("Current Repository: ");   
    private static final JButton connect = new JButton("Connect");    
    private static JavaProject pr;
    private static final String WRONG_FORMAT = "wrong_format";
   
    /**
     * Creates JPanel for project's GitHub settings
     * @param project
     * @return
     */
    public static JPanel getConfigPanel(JavaProject project){
    	pr = project;
    	crntRepo.setText(crntRepo.getText() + obtainCurrentRepo(pr));

        JPanel north = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel center = new JPanel(new FlowLayout(FlowLayout.LEFT));

        north.add(new JLabel("GitHub URL repository: "), BorderLayout.WEST);
        north.add(url, BorderLayout.EAST);
        
        north.add(connect, BorderLayout.EAST);
        
        center.add(crntRepo,BorderLayout.WEST);

        configPanel.add(north, BorderLayout.NORTH);
        configPanel.add(center, BorderLayout.CENTER);    
        
        connect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GitHub github = null;                
                try {
                    github = GitHub.connectAnonymously();
                    String repo = formatURLRepo(url.getText());
                    url.setText("");                    
                    if (repo.compareTo(WRONG_FORMAT) == 0){
                    	JOptionPane.showMessageDialog(null, "Incorrect URL format. Accepted formats are:\n"
                    			+ "https://github.com/*.git\n"
                    			+ "https://github.com/*",
                    			"Format error", JOptionPane.ERROR_MESSAGE);
                    	return;
                    }
                    /* Getting the repository is just used to verify its availability,
                     * since there is no another way to do this */
                    github.getRepository(repo);                      

                    pr.getGHRepo().replaceRepository(repo);                    
                    crntRepo.setText("Current Repository: " + repo);
                } catch (IOException e1) {
                	JOptionPane.showMessageDialog(null, "Check your internet connection or\n"
                			+ "if the requested repository is public",
                			"Connection error", JOptionPane.ERROR_MESSAGE);
                }
                
            }
        });

        return configPanel;
    }
    
    /**
     * This method should look for the project's current repository.
     * If there is no, return empty String.
     * @param project
     * @return
     */
    private static String obtainCurrentRepo(JavaProject project){
    	return project.getGHRepo().getCurrentRepository();
    }
    
    /**
     * Formats an url to make it compatible with kohsuke GitHub API. 
     * @param urlRepo
     * @return
     */
    protected static String formatURLRepo (String urlRepo){
    	if(urlRepo.startsWith("https://github.com/")){
    		if (urlRepo.endsWith(".git")){
    			return urlRepo.split("https://github.com/")[1].split(".git")[0];
    		}
    		return urlRepo.split("https://github.com/")[1];
    	} 
    	return WRONG_FORMAT;
    			 
    }
}