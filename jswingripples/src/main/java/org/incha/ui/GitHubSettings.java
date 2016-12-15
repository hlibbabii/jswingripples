package org.incha.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.incha.core.jswingripples.parser.InteractiveTask;
import org.kohsuke.github.*;

import org.incha.core.JavaProject;
import org.incha.core.ModelSerializer;

/**
 * Created by Amoreno on 11/23/16.
 */
public class GitHubSettings extends JPanel{

    private static final long serialVersionUID = 3049936845151322175L;
    private final JTextField urlTextField;
    private final JLabel currentRepoLabel;
    private final JButton connectButton;
    private final JavaProject project;
    private final JButton retrieveIssuesBtn;
    private static final String WRONG_FORMAT = "wrong_format";
    private static final String CONNECT_BUTTON_TEXT = "Connect";
    private  GitHub github = null;
   
    /**
     * Creates JPanel for project's GitHub settings
     * @param project
     * @return
     */    
    public GitHubSettings(JavaProject project){
        super(new BorderLayout());
        this.project = project;
        urlTextField = new JTextField(20);
        connectButton = new JButton("Connect");
        currentRepoLabel = new JLabel("Current Repository: ");
        currentRepoLabel.setText(currentRepoLabel.getText() + obtainCurrentRepo(this.project));
        retrieveIssuesBtn = new JButton("Retrieve open issues");

        configureLayout();        
        configListeners();
    }
    
    private void configureLayout(){
        JPanel north = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel center = new JPanel(new FlowLayout(FlowLayout.LEFT));
        north.add(new JLabel("GitHub URL repository: "), BorderLayout.WEST);
        north.add(urlTextField, BorderLayout.EAST);
        
        north.add(connectButton, BorderLayout.EAST);
        
        center.add(currentRepoLabel,BorderLayout.WEST);
        center.add(retrieveIssuesBtn,BorderLayout.WEST);
        
        this.add(north, BorderLayout.NORTH);
        this.add(center, BorderLayout.CENTER);
    }

    private void resetConnectButton() {
        connectButton.setEnabled(true);
        connectButton.setText(CONNECT_BUTTON_TEXT);
    }
    
    private void configListeners(){
        
        performConnection();
        
        /**
         * Listener to the button that connectButton the App with GitHub
         */
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new InteractiveTask(new InteractiveTask.TaskListener() {
                    @Override
                    public void taskSuccessful() {
                        resetConnectButton();
                    }

                    @Override
                    public void taskFailure() {
                        resetConnectButton();
                    }
                }) {
                    @Override
                    public void run() {
                        try {
                            connectButton.setEnabled(false);
                            connectButton.setText("Connecting...");
                            String repo = formatURLRepo(urlTextField.getText());
                            urlTextField.setText("");
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
                            project.getGHRepo().replaceRepository(repo);
                            currentRepoLabel.setText("Current Repository: " + repo);
                        } catch (IOException e1) {
                            JOptionPane.showMessageDialog(null, "Check your internet connection or\n"
                                            + "if the requested repository is public",
                                    "Connection error", JOptionPane.ERROR_MESSAGE);
                            listener.taskFailure();
                        } catch (Exception e2) {
                            JOptionPane.showMessageDialog(null, "Please check existence of the repository",
                                    "Unknown exception", JOptionPane.ERROR_MESSAGE);
                            listener.taskFailure();
                        }
                        listener.taskSuccessful();
                    }
                }.start();
            }
        });
        
        /**
         * Listener to the button that retrieve the issues list from GitHub
         */
        retrieveIssuesBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    //Retrieve the current repository
                    String repository = project.getGHRepo().getCurrentRepository();
                    //Retrieve the open issues as a list from reporitory                    
                    List<GHIssue> openIssuesList = new LinkedList<GHIssue>(github.getRepository(repository).getIssues(GHIssueState.OPEN));                    
                    ModelSerializer.generate(openIssuesList, project.getName());
                } catch (IOException e1) {
                	JOptionPane.showMessageDialog(null, "Check your internet connection or\n"
                			+ "if the requested repository is public",
                			"Connection error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception e2){
                	JOptionPane.showMessageDialog(null, "Please check existence of the repository",
                			"Unknown exception", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
    
    /**
     * This method looks for the project's current repository.
     * If there is no, return empty String.
     * @param project
     * @return
     */
    private String obtainCurrentRepo(JavaProject project){
    	return project.getGHRepo().getCurrentRepository();
    }
    
    /**
     * Formats an urlTextField to make it compatible with kohsuke GitHub API.
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
    
    /**
     * Method to stablish the connection between the App and GitHub
     */
    public void performConnection() {
        try {
            github = GitHub.connectAnonymously();
        } catch (IOException e1) {
            JOptionPane.showMessageDialog(null, "Check your internet connection or\n"
                			+ "if the requested repository is public",
                			"Connection error", JOptionPane.ERROR_MESSAGE);
        }
    }
}