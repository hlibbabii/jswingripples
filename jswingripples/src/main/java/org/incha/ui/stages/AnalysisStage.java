package org.incha.ui.stages;

import org.incha.core.ModuleConfiguration;
import org.incha.core.jswingripples.eig.JSwingRipplesEIG;
import org.incha.ui.JSwingRipplesApplication;
import org.incha.ui.stats.AnalysisData;
import org.incha.ui.stats.StartAnalysisAction;
import org.incha.ui.stats.StartAnalysisDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class AnalysisStage {
    private final StartAnalysisDialog startAnalysisDialog;
    private final StartAnalysisAction startAnalysisCallback;

    private JSwingRipplesEIG eig;
    private boolean firstStage;

    protected JSwingRipplesApplication application = JSwingRipplesApplication.getInstance();
    protected AnalysisStage nextStage;

    public AnalysisStage(StartAnalysisDialog startAnalysisDialog, AnalysisStage nextStage) {
        this.startAnalysisDialog = startAnalysisDialog;
        this.nextStage = nextStage;

        this.startAnalysisCallback = startAnalysisDialog.getStartAnalysisCallback();
    }

    public void setEig(JSwingRipplesEIG eig) {
        this.eig = eig;
    }

    public void setFirstStage() {
        this.firstStage = true;
    }

    public ActionListener getButtonListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                if (firstStage) {
                    if (!confirmStartAnalysis()) {
                        return;
                    }
                }
                application.enableProceedButton(true);
                startStage();
            }
        };
    }

    protected void startStage() {
        AnalysisData previousAnalysisData = startAnalysisDialog
                .createAnalysisData(getAnalysisModule(), eig);
        startAnalysisCallback.startAnalysis(
                previousAnalysisData,
                getStageCallback()
        );
    }

    protected abstract StartAnalysisAction.SuccessfulAnalysisAction getStageCallback();
    protected abstract ModuleConfiguration.AnalysisModule getAnalysisModule();

    private boolean confirmStartAnalysis() {
        if(application.isAnotherProjectOpen()) {
            String[] options = new String[]{"Yes", "Cancel"};
            int result = JOptionPane.showOptionDialog(startAnalysisDialog,
                    new String[]{"There is another analysis in progress.\n " +
                            "Are you sure you want to begin a start a new one? " +
                            "All progress from the last analysis will be lost."},
                    "Another analysis in progress",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE,
                    null,
                    options,
                    options[0]);
            if (result != JOptionPane.YES_OPTION) {
                return false;
            }
        }
        startAnalysisDialog.dispose();
        return true;
    }
}