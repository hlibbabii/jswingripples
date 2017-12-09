package org.incha.ui.stages;

import org.incha.core.ModuleConfiguration;
import org.incha.core.jswingripples.eig.JSwingRipplesEIG;
import org.incha.ui.stats.StartAnalysisAction;
import org.incha.ui.stats.StartAnalysisDialog;

import static org.incha.core.ModuleConfiguration.AnalysisModule.MODULE_IMPACT_ANALYSIS;

public class ImpactAnalysisStage extends AnalysisStage {

    public ImpactAnalysisStage(StartAnalysisDialog startAnalysisDialog, AnalysisStage nextStage) {
        super(startAnalysisDialog, nextStage);
    }

    @Override
    public String getButtonText() {
        return "Proceed to Impact Analysis";
    }

    @Override
    protected StartAnalysisAction.SuccessfulAnalysisAction getStageCallback() {
        return new StartAnalysisAction.SuccessfulAnalysisAction() {
            @Override
            public void execute(ModuleConfiguration config, final JSwingRipplesEIG eig) {
                nextStage.setEig(eig);
                application.enableProceedButton(true);
                application.refreshViewArea();
                application.setProceedButtonText(nextStage.getButtonText());
                application.setProceedButtonListener(nextStage.getButtonListener());
            }
        };
    }

    @Override
    protected ModuleConfiguration.AnalysisModule getAnalysisModule() {
        return MODULE_IMPACT_ANALYSIS;
    }
}
