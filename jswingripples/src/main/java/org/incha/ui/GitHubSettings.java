package org.incha.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.kohsuke.github.*;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import org.incha.core.JavaProject;

/**
 * Created by Amoreno on 11/23/16.
 */
public class GitHubSettings extends JPanel{

    private static final long serialVersionUID = 3049936845151322175L;
    private final JTextField url;
    private final JLabel crntRepo;   
    private final JButton connect;    
    private final JavaProject pjct;
    private final JButton retrieveIssuesBtn;
    private final JLabel issuesRetrieveOk = new JLabel("Issues list retrieved successfully");
    private final JLabel issuesRetrieveWrong = new JLabel("The issues list cannot be retrieval");
    private final static String WRONG_FORMAT = "wrong_format";
   
    /**
     * Creates JPanel for project's GitHub settings
     * @param project
     * @return
     */    
    public GitHubSettings(JavaProject project){
        super(new BorderLayout());
        this.pjct = project;
        url = new JTextField(20);
        connect = new JButton("Connect");
        crntRepo = new JLabel("Current Repository: ");
        crntRepo.setText(crntRepo.getText() + obtainCurrentRepo(pjct));
        retrieveIssuesBtn = new JButton("Retrieve open issues");

        configureLayout();        
        configListeners();
    }
    
    private void configureLayout(){
        JPanel north = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel center = new JPanel(new FlowLayout(FlowLayout.LEFT));
        north.add(new JLabel("GitHub URL repository: "), BorderLayout.WEST);
        north.add(url, BorderLayout.EAST);
        
        north.add(connect, BorderLayout.EAST);
        
        center.add(crntRepo,BorderLayout.WEST);
        center.add(retrieveIssuesBtn,BorderLayout.WEST);
        
        this.add(north, BorderLayout.NORTH);
        this.add(center, BorderLayout.CENTER);
    }
    
    private void configListeners(){  	   
        
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
                    
                    pjct.getGHRepo().replaceRepository(repo);                    
                    crntRepo.setText("Current Repository: " + repo);
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
        
        retrieveIssuesBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    //establis connection with github
                    GitHub github = GitHub.connectAnonymously();
                    //Retrieve the current repository
                    String repository = pjct.getGHRepo().getCurrentRepository();
                    //Retrieve the open issues as a list from reporitory                    
                    List<GHIssue> openIssuesList = new LinkedList<GHIssue>(github.getRepository(repository).getIssues(GHIssueState.OPEN));                    
                    generate(openIssuesList);
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
    
    
    public void generate(List<GHIssue> openIssuesList) throws Exception {        
        String xmlFileName = pjct.getName();        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        DOMImplementation implementation = builder.getDOMImplementation();
        Document document = implementation.createDocument(null, "project", null);
        document.setXmlVersion("1.0");        
        //Main Node
        Element root = document.getDocumentElement();
        //Name Node
        Element nameNode = document.createElement("name");
        Text nameValue = document.createTextNode(xmlFileName);
        nameNode.appendChild(nameValue);
        //List Issue Node
        Element listIssuesNode = document.createElement("list_issues");        
        for (GHIssue gHIssue : openIssuesList) {            
            //Issue Node
            Element issueNode = document.createElement("issue");
            issueNode.setAttribute("id", Integer.toString(gHIssue.getId()));
            //Number Node
            Element numberIssueNode = document.createElement("number"); 
            Text numberIssueValue = document.createTextNode(Integer.toString(gHIssue.getNumber()));
            numberIssueNode.appendChild(numberIssueValue);
            //Title Node
            Element titleIssueNode = document.createElement("title"); 
            Text titleIssueValue = document.createTextNode(gHIssue.getTitle());
            titleIssueNode.appendChild(titleIssueValue);            
            //append numberIssueNode to issueNode
            issueNode.appendChild(numberIssueNode);
            //append titleIssueNode to issueNode
            issueNode.appendChild(titleIssueNode);
            //append issueNode to listIssuesNode
            listIssuesNode.appendChild(issueNode);
        }
        //append nameNode to raiz
        root.appendChild(nameNode);
        //append itemNode to raiz
        root.appendChild(listIssuesNode);        
        //Generate XML
        Source source = new DOMSource(document);
        //Indicamos donde lo queremos almacenar
        Result result = new StreamResult(new java.io.File(JSwingRipplesApplication.getHome() + File.separator +xmlFileName+".xml")); //nombre del archivo
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.transform(source, result);
    }    
}