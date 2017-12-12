package org.incha.ui.jripples;

import java.awt.*;

public class EIGStatusMarks {
	public enum Mark {
		BLANK(""),
		VISITED("Unchanged"),
		VISITED_CONTINUE("Propagating"),
		CHANGED("Changed"),
		NEXT_VISIT("Next"),
		IMPACTED("Impacted"), // to continue from from
		LOCATED("Located");

		private final String value;

		Mark(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		public static Mark of(String value) {
			for (Mark mark : Mark.values()) {
				if (mark.getValue().equals(value)) {
					return mark;
				}
			}
			return null;
		}
	}

	public static Image getImageDescriptorForMark(final String mark) {

		if (mark == null)
			return JRipplesResources.getImage("icons/Class.gif");

		if (mark.equals(Mark.BLANK.getValue())) {
			return JRipplesResources.getImage("icons/icicons/blank1.gif");
		} else if (mark.equals(Mark.NEXT_VISIT.getValue())) {
			return JRipplesResources.getImage("icons/icicons/nextvisit1.gif");

		} else if (mark.equals(Mark.VISITED.getValue())) {
			return JRipplesResources.getImage("icons/icicons/visited1.gif");

		} else if (mark.equals(Mark.VISITED_CONTINUE.getValue())) {
			return JRipplesResources.getImage("icons/icicons/through1.gif");

		} else if ((mark.equals(Mark.LOCATED.getValue()))
				|| (mark.equals(Mark.IMPACTED.getValue()))
				|| (mark.equals(Mark.CHANGED.getValue()))) {
			return JRipplesResources.getImage("icons/icicons/changed1.gif");

		}
		return null;
	}


	public static Color getColorForMark(final String mark) {
		if (mark==null)
			return null;

		if (mark.equals(Mark.BLANK.getValue())) {
			return null;
		} else if (mark.equals(Mark.NEXT_VISIT.getValue())) {
			return  new Color(0,170,85);

		} else if (mark.equals(Mark.VISITED.getValue())) {
			return  new Color(192,192,192);

		} else if (mark.equals(Mark.VISITED_CONTINUE.getValue())) {
			return  new Color(255,140,0);

		} else if ((mark.equals(Mark.LOCATED.getValue()))
		        || (mark.equals(Mark.IMPACTED.getValue()))
		        || (mark.equals(Mark.CHANGED.getValue()))) {
			return  new Color(255,128,128);
		}
		return null;
	}
}
