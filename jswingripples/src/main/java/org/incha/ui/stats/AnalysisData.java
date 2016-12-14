package org.incha.ui.stats;

import org.incha.core.jswingripples.eig.JSwingRipplesEIG;

import java.io.File;

/**
 * Data holder class for analysis action.
 */
public class AnalysisData {
    public String projectName;
    public File mainClass;
    public String dependencyGraphModule;
    public JSwingRipplesEIG analysisEIG;

    public AnalysisData(String projectName, File mainClass, String dependencyGraphModule, JSwingRipplesEIG eig){
        this.projectName = projectName;
        this.mainClass = mainClass;
        this.dependencyGraphModule = dependencyGraphModule;
        this.analysisEIG = eig;
    }
}
