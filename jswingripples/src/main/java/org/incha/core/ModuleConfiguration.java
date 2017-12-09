package org.incha.core;

import org.incha.core.jswingripples.JRipplesICModule;
import org.incha.core.jswingripples.JRipplesModule;
import org.incha.core.jswingripples.analysis.JRipplesModuleAnalysisDefaultImpactSetConnections;
import org.incha.core.jswingripples.eig.JSwingRipplesEIG;
import org.incha.core.jswingripples.parser.MethodGranularityDependencyBuilder;
import org.incha.core.jswingripples.parser.MethodGranularityDependencyBuilderPolymorphic;
import org.incha.core.jswingripples.rules.JRipplesModuleICChangePropagation;
import org.incha.core.jswingripples.rules.JRipplesModuleICChangePropagationRelaxed;
import org.incha.core.jswingripples.rules.JRipplesModuleICConceptLocationRelaxed;
import org.incha.core.jswingripples.rules.JRipplesModuleICDefaultConceptLocation;
import org.incha.core.jswingripples.rules.JRipplesModuleICImpactAnalysis;
import org.incha.core.jswingripples.rules.JRipplesModuleICImpactAnalysisRelaxed;
import org.incha.ui.stats.AnalysisData;

import java.util.LinkedList;
import java.util.List;



/**
 * Manages information about the type of Analysis made by JRipples, doesn't include the actual procedures.
 */
public class ModuleConfiguration {
    public enum AnalysisModule{
        MODULE_IMPACT_ANALYSIS("Impact Analysis"),
        MODULE_IMPACT_ANALYSIS_RELAXED("Impact Analysis"),
        MODULE_CHANGE_PROPAGATION_RELAXED("Change Propagation"),
        MODULE_CHANGE_PROPAGATION("Change Propagation"),
        MODULE_CONCEPT_LOCATION("Concept Location"),
        MODULE_CONCEPT_LOCATION_RELAXED("Concept Location"),

        MODULE_DEPENDENCY_BUILDER("Dependency Building");

        AnalysisModule(String formattedName) {
            this.formattedName = formattedName;
        }

        private String formattedName;

        public String getFormattedName() {
            return formattedName;
        }
    }

    public static final int MODULE_DEPENDENCY_BUILDER = 0;
    public static final int MODULE_DEPENDENCY_BUILDER_WITH_POLYMORPHIC = 1;

    public static final int MODULE_VIEW_HIERARCHY = 0;
    public static final int MODULE_VIEW_TREE = 1;

    private int dependencyGraphModule;
    private AnalysisModule incrementalChange = AnalysisModule.MODULE_CONCEPT_LOCATION;

    public ModuleConfiguration() {
        super();
    }

    public void setDependencyGraphModule(final int type) {
        this.dependencyGraphModule = type;
    }

    public int getDependencyGraphModule() {
        return dependencyGraphModule;
    }

    public void setIncrementalChange(final AnalysisModule type) {
        this.incrementalChange = type;
    }

    public AnalysisModule getIncrementalChange() {
        return incrementalChange;
    }

    public List<JRipplesModule> buildModules(final AnalysisData eig) {
        final List<JRipplesModule> modules = new LinkedList<>();
        if (eig.analysisModule == AnalysisModule.MODULE_DEPENDENCY_BUILDER) {
            modules.add(createDependencyBuilderModule(eig.analysisEIG).withPriority(JRipplesModule.Priority.HIGH));
        } else {
            modules.add(createIncrementalChangeModule(eig.analysisEIG).withPriority(JRipplesModule.Priority.LOW));

            //analysis
            if (isAnalysisDefaultImpactSetConnections()) {
                modules.add(new JRipplesModuleAnalysisDefaultImpactSetConnections(eig.analysisEIG)
                        .withPriority(JRipplesModule.Priority.LOW));
            }
        }


        return modules;
    }

    /**
     * @return
     */
    public boolean isAnalysisDefaultImpactSetConnections() {
        return getIncrementalChange() != AnalysisModule.MODULE_CONCEPT_LOCATION;
    }

    /**
     * @param eig
     * @return
     */
    public JRipplesICModule createIncrementalChangeModule(
            final JSwingRipplesEIG eig) {
        return createIncrementalChangeModule(getIncrementalChange(), eig);
    }

    /**
     * @param eig
     * @return
     */
    public JRipplesModule createDependencyBuilderModule(
            final JSwingRipplesEIG eig) {
        return createDependencyBuilderModule(getDependencyGraphModule(), eig);
    }

    /**
     * @param moduleType
     * @param eig
     * @return
     */
    public static JRipplesModule createDependencyBuilderModule(
            final int moduleType, final JSwingRipplesEIG eig) {
        if (moduleType == MODULE_DEPENDENCY_BUILDER) {
            return new MethodGranularityDependencyBuilder(eig);
        } else {
            return new MethodGranularityDependencyBuilderPolymorphic(eig);
        }
    }

    /**
     * @param type
     * @param eig
     * @return
     */
    public static JRipplesICModule createIncrementalChangeModule(
            final AnalysisModule type, final JSwingRipplesEIG eig) {
        switch (type) {
            case MODULE_IMPACT_ANALYSIS:
                return new JRipplesModuleICImpactAnalysis(eig);
            case MODULE_IMPACT_ANALYSIS_RELAXED:
                return new JRipplesModuleICImpactAnalysisRelaxed(eig);
            case MODULE_CHANGE_PROPAGATION_RELAXED:
                return new JRipplesModuleICChangePropagationRelaxed(eig);
            case MODULE_CHANGE_PROPAGATION:
                return new JRipplesModuleICChangePropagation(eig);
            case MODULE_CONCEPT_LOCATION:
                return new JRipplesModuleICDefaultConceptLocation(eig);
            case MODULE_CONCEPT_LOCATION_RELAXED:
                return new JRipplesModuleICConceptLocationRelaxed(eig);
        }
        return null;
    }
}
