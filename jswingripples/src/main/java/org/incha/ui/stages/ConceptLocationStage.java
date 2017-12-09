package org.incha.ui.stages;

import org.incha.core.ModuleConfiguration;
import org.incha.core.StatisticsManager;
import org.incha.core.jswingripples.eig.JSwingRipplesEIG;
import org.incha.ui.stats.StartAnalysisAction;
import org.incha.ui.stats.StartAnalysisDialog;

import static org.incha.core.ModuleConfiguration.AnalysisModule.MODULE_CONCEPT_LOCATION;

public class ConceptLocationStage extends AnalysisStage {

    public ConceptLocationStage(StartAnalysisDialog startAnalysisDialog, AnalysisStage nextStage) {
        super(startAnalysisDialog, nextStage);
    }

    @Override
    protected StartAnalysisAction.SuccessfulAnalysisAction getStageCallback() {
        return new StartAnalysisAction.SuccessfulAnalysisAction() {
            @Override
            public void execute(ModuleConfiguration config, final JSwingRipplesEIG eig) {
                nextStage.setEig(eig);
                application.setProceedButtonText("Proceed to " + nextStage.getAnalysisModule().getFormattedName());
                application.showProceedButton();
                StatisticsManager.getInstance().addStatistics(config, eig);
                application.setProceedButtonListener(nextStage.getButtonListener());
            }
        };
    }

    @Override
    protected ModuleConfiguration.AnalysisModule getAnalysisModule() {
        return MODULE_CONCEPT_LOCATION;
    }
}
