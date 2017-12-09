package org.incha.ui.stages;

import org.incha.core.ModuleConfiguration;
import org.incha.core.jswingripples.eig.JSwingRipplesEIG;
import org.incha.ui.stats.StartAnalysisAction;
import org.incha.ui.stats.StartAnalysisDialog;

import static org.incha.core.ModuleConfiguration.AnalysisModule.MODULE_DEPENDENCY_BUILDER;

public class DependencyBuilderStage extends AnalysisStage {
    public DependencyBuilderStage(StartAnalysisDialog startAnalysisDialog, AnalysisStage nextStage) {
        super(startAnalysisDialog, nextStage);
    }

    @Override
    protected String getButtonText() {
        return "Start Dependency Building";
    }

    @Override
    protected StartAnalysisAction.SuccessfulAnalysisAction getStageCallback() {
        return new StartAnalysisAction.SuccessfulAnalysisAction() {
            @Override
            public void execute(ModuleConfiguration config, JSwingRipplesEIG eig) {
                nextStage.setEig(eig);
                nextStage.startStage();
            }
        };
    }

    @Override
    protected ModuleConfiguration.AnalysisModule getAnalysisModule() {
        return MODULE_DEPENDENCY_BUILDER;
    }
}
