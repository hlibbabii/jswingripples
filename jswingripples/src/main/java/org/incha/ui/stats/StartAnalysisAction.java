package org.incha.ui.stats;

import org.eclipse.jdt.core.IPackageDeclaration;
import org.eclipse.jdt.core.JavaModelException;
import org.incha.compiler.dom.JavaDomBuilder;
import org.incha.core.JavaProject;
import org.incha.core.JavaProjectsModel;
import org.incha.core.ModuleConfiguration;
import org.incha.core.StatisticsManager;
import org.incha.core.jswingripples.JRipplesModuleRunner;
import org.incha.core.jswingripples.eig.JSwingRipplesEIG;
import org.incha.core.search.Indexer;
import org.incha.ui.JSwingRipplesApplication;
import org.incha.ui.jripples.JRipplesDefaultModulesConstants;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class StartAnalysisAction implements ActionListener {
    public class AnalysisFailedException extends Exception {
        public AnalysisFailedException(String message) { super(message); }
    }
	private String projectSelected;
	
    /**
     * Default constructor.
     */
	
    public StartAnalysisAction() {
        super();
        setProjectSelected(null);
    }
    
    /**
     * Constructor made to launch start analysis dialog with project withProject selected
     * @param withProject
     */
    public StartAnalysisAction(String withProject){
    	super();
    	setProjectSelected(withProject);    	
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(final ActionEvent e) {
        final JSwingRipplesApplication app = JSwingRipplesApplication.getInstance();
        final StartAnalysisDialog dialog = new StartAnalysisDialog(app, this);
        dialog.pack();
        dialog.setLocationRelativeTo(app);
        dialog.setVisible(true);
    }

    public void startAnalysis(AnalysisData data) throws AnalysisFailedException {
        if (data.projectName == null) {
            return;
        }
        final JavaProject project = JavaProjectsModel.getInstance().getProject(data.projectName);
        final JSwingRipplesEIG eig = data.analysisEIG;

        String packageName;
        try {
            if ((packageName = getPackage(data.mainClass)) != null) {
                eig.setMainClass(packageName + "." + data.mainClass.getName().replace(".java", ""));
            }
        } catch (JavaModelException e) {
            throw new AnalysisFailedException("Could not retrieve main class package");
        }
        final ModuleConfiguration config = new ModuleConfiguration();
        //module dependency builder
        if (JRipplesDefaultModulesConstants.MODULE_DEPENDENCY_BUILDER.equals(data.dependencyGraphModule)) {
            config.setDependencyGraphModule(ModuleConfiguration.MODULE_DEPENDENCY_BUILDER);
        } else {
            config.setDependencyGraphModule(ModuleConfiguration.MODULE_DEPENDENCY_BUILDER_WITH_POLYMORPHIC);
        }

        config.setIncrementalChange(ModuleConfiguration.MODULE_CONCEPT_LOCATION);
        config.setAnalysis(ModuleConfiguration.MODULE_IMPACT_ANALYSIS);

        project.setModuleConfiguration(config);

        new JRipplesModuleRunner(new JRipplesModuleRunner.ModuleRunnerListener() {
            @Override
            public void runSuccessful() {
                try {
                    Indexer.getInstance().indexEIG(eig);
                    JSwingRipplesApplication.getInstance().enableSearchMenuButtons();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                StatisticsManager.getInstance().addStatistics(config, eig);
                JSwingRipplesApplication.getInstance().showProceedButton();
            }

            @Override
            public void runFailure() {

            }
        }).runModulesWithPriority(config.buildModules(eig));
    }

    
    /**
     * projectSelected getter
     * @return projectSelected
     */
    protected String getProjectSelected() {
		return projectSelected;
	}

    /**
     * projectSelected setter
     * @param projectSelected
     */
	protected void setProjectSelected(String projectSelected) {
		this.projectSelected = projectSelected;
	}

    private String getPackage(File file) throws JavaModelException {
        JavaDomBuilder builder = new JavaDomBuilder(file.getAbsolutePath());
        for (IPackageDeclaration declaration : builder.build(file).getPackageDeclarations()) {
            if (declaration.getElementName() != null) {
                return declaration.getElementName();
            }
        }
        return null;
    }
}
