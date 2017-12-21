package org.incha.ui.table.column.renderer;

import org.incha.core.search.Highlight;
import org.incha.core.search.Searcher;

import javax.swing.*;
import java.awt.*;

public class SearchHitsColumnRenderer extends ColumnRenderer<String> {

    @Override
    public Component getComponent(JLabel label, Object value) {

        String shortName = (String) value;
        if (!"".equals(shortName)) {
            label.setBackground(Highlight.getColor(shortName));
            label.setText(renderSearchResults(shortName));
        } else {
            label.setText("");
        }
        return label;
    }

    /**
     * Creates a string containing the number of appearances of the last searched term in the
     * given node.
     *
     * @return string containing the total search hits. If there are no search hits, the empty
     * string is returned.
     */
    private String renderSearchResults(String shortName) {
        int hits = Searcher.getInstance().totalHits(shortName);
        return (hits == 0 ? "" : (" (" + hits + ")"));
    }
}
