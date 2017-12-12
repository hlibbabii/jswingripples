package org.incha.ui.jripples;

import java.awt.*;

public class EIGStatusMarks {
	public enum Mark {
		BLANK("", "icons/icicons/blank1.gif", null, 1),
		VISITED("Unchanged", "icons/icicons/visited1.gif", new Color(192,192,192), 3),
		VISITED_CONTINUE("Propagating", "icons/icicons/through1.gif", new Color(255,140,0), 4),
		CHANGED("Changed", "icons/icicons/changed1.gif", new Color(255,128,128), 7),
		NEXT_VISIT("Next", "icons/icicons/nextvisit1.gif", new Color(0,170,85), 2),
		IMPACTED("Impacted", "icons/icicons/changed1.gif", new Color(255,128,128), 6), // to continue from from
		LOCATED("Located", "icons/icicons/changed1.gif", new Color(255,128,128), 5);

		private static final String PATH_TO_DEFAULT_IMAGE = "icons/Class.gif";

		private final String value;
		private final String pathToImage;
        private Color color;
		private int importance;

		Mark(String value, String pathToImage, Color color, int importance) {
			this.value = value;
			this.pathToImage = pathToImage;
			this.color = color;
			this.importance = importance;
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

        public Color getColorForMark() {
            return this.color;
        }

		public int getImportance() {
			return this.importance;
		}
	}
}
