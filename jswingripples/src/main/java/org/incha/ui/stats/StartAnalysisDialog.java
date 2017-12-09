package org.incha.ui.stats;

import org.incha.core.JavaProject;
import org.incha.core.JavaProjectsModel;
import org.incha.core.ModuleConfiguration;
import org.incha.core.Statistics;
import org.incha.core.jswingripples.eig.JSwingRipplesEIG;
import org.incha.core.jswingripples.parser.InteractiveTask;
import org.incha.ui.jripples.JRipplesDefaultModulesConstants;
import org.incha.ui.stages.AnalysisStage;
import org.incha.ui.stages.ChangePropagationStage;
import org.incha.ui.stages.ConceptLocationStage;
import org.incha.ui.stages.DependencyBuilderStage;
import org.incha.ui.stages.ImpactAnalysisStage;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class StartAnalysisDialog extends JDialog {
    private static final long serialVersionUID = 6788138046337076311L;
    private final org.slf4j.Logger log = LoggerFactory.getLogger(getClass());

    private final JTextField classNameTextField = new JTextField(30);
    private final JButton startConceptLocationButton = new JButton("Start Concept Location");
    private final StartAnalysisAction startAnalysisCallback;
    private AnalysisStage firstAnalysisStage;
    private File mainClassFile;
    private JavaProject project;
    final Window ownerWindow;
    final JComboBox<String> projects;

    JComboBox<String> dependencyGraph = new JComboBox<String>(new DefaultComboBoxModel<String>(
        new String[]{
            JRipplesDefaultModulesConstants.MODULE_DEPENDENCY_BUILDER,
            JRipplesDefaultModulesConstants.MODULE_DEPENDENCY_BUILDER_WITH_POLYMORPHIC
        }
    ));
    
    public StartAnalysisDialog(final Window owner, final StartAnalysisAction callback) {
        super(owner);
        ownerWindow = owner;
        startAnalysisCallback = callback;
        setModal(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout(0, 5));

        //create projects combobox.
        final List<JavaProject> prg = JavaProjectsModel.getInstance().getProjects();
        final String[] prgArray = new String[prg.size()];
        for (int i = 0; i < prgArray.length; i++) {
            prgArray[i] = prg.get(i).getName();
        }
        projects = new JComboBox<>(new DefaultComboBoxModel<>(prgArray));
        projects.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                projectChanged();
            }
        });

        final JPanel center = new JPanel(new FlowLayout(FlowLayout.LEADING));
        final JPanel projectAndType = createCenterPanel();
        center.add(projectAndType);

        getContentPane().add(center, BorderLayout.CENTER);

        setupAnalysisStagesOrder(
                DependencyBuilderStage.class,
                ConceptLocationStage.class,
                ImpactAnalysisStage.class,
                ChangePropagationStage.class
        );
        firstAnalysisStage.setEig(null);
        //south pane
        startConceptLocationButton.setEnabled(false);
        startConceptLocationButton.addActionListener(firstAnalysisStage.getButtonListener());

        final JPanel south = new JPanel(new FlowLayout(FlowLayout.CENTER));
        south.add(startConceptLocationButton);

        final JButton cancel = new JButton("Cancel");
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                dispose();
            }
        });
        south.add(cancel);
        getContentPane().add(south, BorderLayout.SOUTH);

        // select proper project
        if (startAnalysisCallback.getProjectSelected() != null){        	
        	projects.setSelectedItem(startAnalysisCallback.getProjectSelected());        	
        }
        //set up default values
        projectChanged();
    }

    private void setupAnalysisStagesOrder(Class<? extends AnalysisStage>... analysisStagesClasses) {
        if (analysisStagesClasses.length == 0) {
            log.warn("No analysis stages specified");
        }
        try {
            AnalysisStage nextStage = null;
            for (int i = analysisStagesClasses.length -1; i >= 0; i--) {
                nextStage = analysisStagesClasses[i]
                        .getConstructor(StartAnalysisDialog.class, AnalysisStage.class)
                        .newInstance(this, nextStage);
            }
            firstAnalysisStage = nextStage;
            if (firstAnalysisStage != null) {
                firstAnalysisStage.setFirstStage();
            }
        } catch (InstantiationException |
                NoSuchMethodException |
                InvocationTargetException |
                IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    protected void projectChanged() {
        project = JavaProjectsModel.getInstance().getProject((String) projects.getSelectedItem());
        
        if (project != null) {
            //set current module configuration

            //dependency graph module
            final ModuleConfiguration cfg = project.getModuleConfiguration();
            final Statistics stats = project.getCurrentStatistics();
            classNameTextField.setText(stats != null ? stats.getEIG().getMainClass() : null);

            switch (cfg.getDependencyGraphModule()) {
                case ModuleConfiguration.MODULE_DEPENDENCY_BUILDER:
                    dependencyGraph.setSelectedItem(JRipplesDefaultModulesConstants.MODULE_DEPENDENCY_BUILDER);
                break;
                default://MODULE_DEPENDENCY_BUILDER_WITH_POLYMORPHIC
                    dependencyGraph.setSelectedItem(
                            JRipplesDefaultModulesConstants.MODULE_DEPENDENCY_BUILDER_WITH_POLYMORPHIC);
            }
        }
    }
    
    private void verifyMainClassFileExtension(){ 
        final Integer sizeExtension = 5;
        String classname = classNameTextField.getText();
            if (classname.length()>sizeExtension && 
                    classname.substring(classname.length()-sizeExtension, 
                            classname.length()).toUpperCase().equals(".JAVA")){
                startConceptLocationButton.setEnabled(true);
            }
            else {
                startConceptLocationButton.setEnabled(false);
            }
    }

    private JPanel createCenterPanel() {
        final JPanel panel = new JPanel(new GridLayout(5, 2));
        panel.setBorder(new CompoundBorder(new BevelBorder(BevelBorder.RAISED),
                new EmptyBorder(3, 3, 3, 3)));
        panel.add(new JLabel("Java project:"));
        panel.add(projects);
        panel.add(new JLabel("Class name:"));
        panel.add(createMainClassFinderPanel());
        panel.add(new JLabel("Dependency Graph"));
        panel.add(dependencyGraph);
        dependencyGraph.setEditable(false);
        projects.setEditable(false);
        addClassNameTextFieldListener();
        return panel;
    }

    protected void setClassName(final String classNameParam, String fileName){
        mainClassFile = new File(fileName);
        classNameTextField.setText(classNameParam);
    }
    
    protected void enableButtonOk(){
        startConceptLocationButton.setEnabled(true);
    }

    private void addClassNameTextFieldListener() {
        classNameTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
                verifyMainClassFileExtension();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                verifyMainClassFileExtension();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                verifyMainClassFileExtension();
            }
        });
    }

    private JPanel createMainClassFinderPanel() {
        JPanel mainClassFinderPanel = new JPanel();
        mainClassFinderPanel.setLayout(new FlowLayout(FlowLayout.LEADING,0,0));
        mainClassFinderPanel.add(classNameTextField);
        mainClassFinderPanel.add(createBrowseButton());
        mainClassFinderPanel.add(createAutomaticButton());

        return mainClassFinderPanel;
    }

    private JButton createAutomaticButton() {
        final JButton automaticButton = new JButton("Auto");
        automaticButton.setEnabled(projects.getItemCount() != 0);
        automaticButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                new InteractiveTask(new InteractiveTask.TaskListener() {
                    @Override
                    public void taskSuccessful() {
                        automaticButton.setEnabled(true);
                    }

                    @Override
                    public void taskFailure() {
                        Logger.getLogger(StartAnalysisDialog.class.getName())
                              .log(Level.SEVERE, "Could not find main classes");
                    }
                }) {
                    @Override
                    public void run() {
                        try {
                            automaticButton.setEnabled(false);
                            // dialog creation is a heavy operation, since it looks for main classes
                            // in the entire project
                            MainClassSearchDialog dialog = new MainClassSearchDialog(StartAnalysisDialog.this, project);
                            dialog.pack();
                            dialog.setLocationRelativeTo(ownerWindow);
                            dialog.setTitle("Select entry point");
                            dialog.setVisible(true);
                            listener.taskSuccessful();
                        } catch (IOException ex) {
                            listener.taskFailure();
                        }
                    }
                }.start();
            }
        });
        return automaticButton;
    }

    private JButton createBrowseButton() {
        JButton browseButton = new JButton("Browse");
        browseButton.setEnabled(projects.getItemCount() != 0);
        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final JFileChooser chooser = new JFileChooser(project.getBuildPath().getFirstPath());
                chooser.setMultiSelectionEnabled(false);
                if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    final File selectedFile = chooser.getSelectedFile();
                    if (selectedFile != null) {
                        mainClassFile = selectedFile;
                        classNameTextField.setText(selectedFile.getName());
                    }
                }
            }
        });
        return browseButton;
    }

    public AnalysisData createAnalysisData(ModuleConfiguration.AnalysisModule analysisModule,
                                            JSwingRipplesEIG previousStageEIG) {
        JSwingRipplesEIG eig = previousStageEIG != null ?
                previousStageEIG :
                new JSwingRipplesEIG(JavaProjectsModel.getInstance().getProject((String) projects.getSelectedItem()));

        return new AnalysisData(
                (String) projects.getSelectedItem(),
                mainClassFile,
                (String) dependencyGraph.getSelectedItem(),
                analysisModule,
                eig);
    }

    public StartAnalysisAction getStartAnalysisCallback() {
        return startAnalysisCallback;
    }
}
