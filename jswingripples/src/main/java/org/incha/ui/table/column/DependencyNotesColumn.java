package org.incha.ui.table.column;

import org.incha.core.jswingripples.eig.JSwingRipplesEIGNode;
import org.incha.ui.dependency.ClassDependencyView;
import org.incha.ui.table.column.renderer.ColumnRenderer;
import org.incha.ui.table.column.renderer.DependencyNoteColumnRenderer;

public class DependencyNotesColumn extends Column<JSwingRipplesEIGNode> {
    private static final String NAME = "Dependency Notes";

    private ClassDependencyView classDependencyView;

    public DependencyNotesColumn(ClassDependencyView classDependencyView) {
        this.classDependencyView = classDependencyView;
    }

    @Override
    public JSwingRipplesEIGNode getValue(JSwingRipplesEIGNode node) {
        return node;
    }

    @Override
    public Class<?> getColumnClass() {
        return String.class;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public ColumnRenderer<?> getColumnRenderer() {
        return new DependencyNoteColumnRenderer(classDependencyView);
    }
}
