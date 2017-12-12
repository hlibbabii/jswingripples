package org.incha.ui.jripples;

import java.awt.*;

public class EIGStatusMarks {
	public enum Mark {
		BLANK("", "icons/icicons/blank1.gif"),
		VISITED("Unchanged", "icons/icicons/visited1.gif"),
		VISITED_CONTINUE("Propagating", "icons/icicons/through1.gif"),
		CHANGED("Changed", "icons/icicons/changed1.gif"),
		NEXT_VISIT("Next", "icons/icicons/nextvisit1.gif"),
		IMPACTED("Impacted", "icons/icicons/changed1.gif"), // to continue from from
		LOCATED("Located", "icons/icicons/changed1.gif");

		private static final String PATH_TO_DEFAULT_IMAGE = "icons/Class.gif";

		private final String value;
		private final String pathToImage;

		Mark(String value, String pathToImage) {
			this.value = value;
			this.pathToImage = pathToImage;
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

		public Image getImageDescriptorForMark() {
			return JRipplesResources.getImage(pathToImage);
		}
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
