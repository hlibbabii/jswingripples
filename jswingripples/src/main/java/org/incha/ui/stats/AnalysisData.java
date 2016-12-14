package org.incha.ui.stats;

import org.incha.core.ModuleConfiguration;
import org.incha.core.jswingripples.eig.JSwingRipplesEIG;

import java.io.File;

/**
 * Data holder class for analysis action.
 */
public class AnalysisData {
    public String projectName;
    public File mainClass;
    public String dependencyGraphModule;
    public ModuleConfiguration.AnalysisModule analysisModule;
    public JSwingRipplesEIG analysisEIG;

    public AnalysisData(
            String projectName,
            File mainClass,
            String dependencyGraphModule,
            ModuleConfiguration.AnalysisModule analysisModule,
            JSwingRipplesEIG eig){
        this.projectName = projectName;
        this.mainClass = mainClass;
        this.dependencyGraphModule = dependencyGraphModule;
        this.analysisModule = analysisModule;
        this.analysisEIG = eig;
    }
}
