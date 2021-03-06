package org.incha.ui;

import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.incha.core.JavaProject;

public class ProjectSettingsEditor extends JTabbedPane {
    
    private static final long serialVersionUID = 6521933918476824719L;
    
    /**
     * The java project.
     */
    private final JavaProject project;

    /**
     * Default constructor.
     */
    public ProjectSettingsEditor(final JavaProject project) {
        super(LEFT);
        setTabLayoutPolicy(WRAP_TAB_LAYOUT);
        this.project = project;
        addTab("Project", createProjectSettingsTab());
        addTab("Build Path", createBuildPathTab());
        addTab("Github", createGithubTab());
    }

    /**
     * @return project settings tab, like as 'name', 'location', etc.
     */
    private JPanel createProjectSettingsTab() {
        final JPanel root = new JPanel(new FlowLayout(FlowLayout.LEADING));
        final JPanel panel = new JPanel(new GridLayout(2, 1));
        //project name
        panel.add(new JLabel("Name: " + project.getName()));
        root.add(panel);
        return root;
    }
    
    /**
     * @return
     */
    private JTabbedPane createGithubTab() {
        final JTabbedPane tabPane = new JTabbedPane(TOP, WRAP_TAB_LAYOUT);
        tabPane.addTab("GitHub Settings", new GitHubSettings(project));
        return tabPane;
    }

    /**
     * @return
     */
    private JTabbedPane createBuildPathTab() {
        final JTabbedPane tabPane = new JTabbedPane(TOP, WRAP_TAB_LAYOUT);
        tabPane.addTab("Sources", new SourcesEditor(project));
        return tabPane;
    }
}