/*
 * $Id: VerticalFlowLayout.java,v 1.1 2005/03/18 09:44:58 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package oracle.jdeveloper.layout;

import java.awt.*;
import java.io.Serializable;

/**
 * @version $Revision: 1.1 $, $Date: 2005/03/18 09:44:58 $
 * @author $Author: bass $
 * @module generalclient_v1
 * @deprecated
 */
public class VerticalFlowLayout extends FlowLayout implements Serializable {

	public static final int BOTTOM = 2;

	public static final int MIDDLE = 1;

	public static final int TOP = 0;

	private static final long serialVersionUID = 3618700786004408375L;

	boolean hfill;

	int hgap;

	boolean vfill;

	int vgap;

	public VerticalFlowLayout() {
		this(0, 5, 5, true, false);
	}

	public VerticalFlowLayout(boolean flag, boolean flag1) {
		this(0, 5, 5, flag, flag1);
	}

	public VerticalFlowLayout(int i) {
		this(i, 5, 5, true, false);
	}

	public VerticalFlowLayout(int i, boolean flag, boolean flag1) {
		this(i, 5, 5, flag, flag1);
	}

	public VerticalFlowLayout(int i, int j, int k, boolean flag,
			boolean flag1) {
		setAlignment(i);
		this.hgap = j;
		this.vgap = k;
		this.hfill = flag;
		this.vfill = flag1;
	}

	public boolean getHorizontalFill() {
		return this.hfill;
	}

	public boolean getVerticalFill() {
		return this.vfill;
	}

	public void layoutContainer(Container container) {
		Insets insets = container.getInsets();
		int i = container.getSize().height
				- (insets.top + insets.bottom + this.vgap * 2);
		int j = container.getSize().width
				- (insets.left + insets.right + this.hgap * 2);
		int k = container.getComponentCount();
		int l = insets.left + this.hgap;
		int i1 = 0;
		int j1 = 0;
		int k1 = 0;
		for (int l1 = 0; l1 < k; l1++) {
			Component component = container.getComponent(l1);
			if (component.isVisible()) {
				Dimension dimension = component
						.getPreferredSize();
				if (this.vfill && l1 == k - 1)
					dimension.height = Math
							.max(
									i - i1,
									component
											.getPreferredSize().height);
				if (this.hfill) {
					component.setSize(j, dimension.height);
					dimension.width = j;
				} else {
					component.setSize(dimension.width,
							dimension.height);
				}
				if (i1 + dimension.height > i) {
					placethem(container, l, insets.top
							+ this.vgap, j1,
							i - i1, k1, l1);
					i1 = dimension.height;
					l += this.hgap + j1;
					j1 = dimension.width;
					k1 = l1;
				} else {
					if (i1 > 0)
						i1 += this.vgap;
					i1 += dimension.height;
					j1 = Math.max(j1, dimension.width);
				}
			}
		}

		placethem(container, l, insets.top + this.vgap, j1, i - i1, k1,
				k);
	}

	public Dimension minimumLayoutSize(Container container) {
		Dimension dimension = new Dimension(0, 0);
		for (int i = 0; i < container.getComponentCount(); i++) {
			Component component = container.getComponent(i);
			if (component.isVisible()) {
				Dimension dimension1 = component
						.getMinimumSize();
				dimension.width = Math.max(dimension.width,
						dimension1.width);
				if (i > 0)
					dimension.height += this.vgap;
				dimension.height += dimension1.height;
			}
		}

		Insets insets = container.getInsets();
		dimension.width += insets.left + insets.right + this.hgap * 2;
		dimension.height += insets.top + insets.bottom + this.vgap * 2;
		return dimension;
	}

	public Dimension preferredLayoutSize(Container container) {
		Dimension dimension = new Dimension(0, 0);
		for (int i = 0; i < container.getComponentCount(); i++) {
			Component component = container.getComponent(i);
			if (component.isVisible()) {
				Dimension dimension1 = component
						.getPreferredSize();
				dimension.width = Math.max(dimension.width,
						dimension1.width);
				if (i > 0)
					dimension.height += this.hgap;
				dimension.height += dimension1.height;
			}
		}

		Insets insets = container.getInsets();
		dimension.width += insets.left + insets.right + this.hgap * 2;
		dimension.height += insets.top + insets.bottom + this.vgap * 2;
		return dimension;
	}

	public void setHorizontalFill(boolean flag) {
		this.hfill = flag;
	}

	public void setVerticalFill(boolean flag) {
		this.vfill = flag;
	}

	private void placethem(Container container, int i, int j, int k, int l,
			int i1, int j1) {
		int k1 = getAlignment();
		if (k1 == 1)
			j += l / 2;
		if (k1 == 2)
			j += l;
		for (int l1 = i1; l1 < j1; l1++) {
			Component component = container.getComponent(l1);
			Dimension dimension = component.getSize();
			if (component.isVisible()) {
				int i2 = i + (k - dimension.width) / 2;
				component.setLocation(i2, j);
				j += this.vgap + dimension.height;
			}
		}

	}
}
