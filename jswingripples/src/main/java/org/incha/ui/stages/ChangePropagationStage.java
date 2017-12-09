package org.incha.ui.stages;

import org.incha.core.ModuleConfiguration;
import org.incha.core.jswingripples.eig.JSwingRipplesEIG;
import org.incha.ui.stats.StartAnalysisAction;
import org.incha.ui.stats.StartAnalysisDialog;

import static org.incha.core.ModuleConfiguration.AnalysisModule.MODULE_CHANGE_PROPAGATION;

public class ChangePropagationStage extends AnalysisStage {

    public ChangePropagationStage(StartAnalysisDialog startAnalysisDialog, AnalysisStage nextStage) {
        super(startAnalysisDialog, nextStage);
    }

    @Override
    protected StartAnalysisAction.SuccessfulAnalysisAction getStageCallback() {
        return new StartAnalysisAction.SuccessfulAnalysisAction() {
            @Override
            public void execute(ModuleConfiguration config, JSwingRipplesEIG eig) {
                application.enableProceedButton(true);
                application.hideProceedButton();
                application.resetProceedButton();
                application.refreshViewArea();
            }
        };
    }

    @Override
    protected ModuleConfiguration.AnalysisModule getAnalysisModule() {
        return MODULE_CHANGE_PROPAGATION;
    }
}