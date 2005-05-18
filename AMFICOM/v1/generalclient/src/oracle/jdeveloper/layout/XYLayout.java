/*
 * $Id: XYLayout.java,v 1.2 2005/05/18 14:01:18 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package oracle.jdeveloper.layout;

import java.awt.*;
import java.io.Serializable;
import java.util.Hashtable;

/**
 * @version $Revision: 1.2 $, $Date: 2005/05/18 14:01:18 $
 * @author $Author: bass $
 * @module generalclient_v1
 * @deprecated
 */
public class XYLayout implements LayoutManager2, Serializable {
	static final XYConstraints defaultConstraints = new XYConstraints();

	private static final long serialVersionUID = 200L;

	int height;

	Hashtable info;

	int width;

	public XYLayout() {
		this.info = new Hashtable();
	}

	public XYLayout(int i, int j) {
		this.info = new Hashtable();
		this.width = i;
		this.height = j;
	}

	public void addLayoutComponent(Component component, Object obj) {
		if (obj instanceof XYConstraints)
			this.info.put(component, obj);
	}

	public void addLayoutComponent(String s, Component component) {
		// empty
	}

	public int getHeight() {
		return this.height;
	}

	public float getLayoutAlignmentX(Container container) {
		return 0.5F;
	}

	public float getLayoutAlignmentY(Container container) {
		return 0.5F;
	}

	public int getWidth() {
		return this.width;
	}

	public void invalidateLayout(Container container) {
		// empty
	}

	public void layoutContainer(Container container) {
		Insets insets = container.getInsets();
		int i = container.getComponentCount();
		for (int j = 0; j < i; j++) {
			Component component = container.getComponent(j);
			if (component.isVisible()) {
				Rectangle rectangle = getComponentBounds(
						component, true);
				component.setBounds(insets.left + rectangle.x,
						insets.top + rectangle.y,
						rectangle.width,
						rectangle.height);
			}
		}

	}

	public Dimension maximumLayoutSize(Container container) {
		return new Dimension(0x7fffffff, 0x7fffffff);
	}

	public Dimension minimumLayoutSize(Container container) {
		return getLayoutSize(container, false);
	}

	public Dimension preferredLayoutSize(Container container) {
		return getLayoutSize(container, true);
	}

	public void removeLayoutComponent(Component component) {
		this.info.remove(component);
	}

	public void setHeight(int i) {
		this.height = i;
	}

	public void setWidth(int i) {
		this.width = i;
	}

	public String toString() {
		return "XYLayout[width=" + this.width + ",height=" + this.height + "]";
	}

	Rectangle getComponentBounds(Component component, boolean flag) {
		XYConstraints xyconstraints = (XYConstraints) this.info
				.get(component);
		if (xyconstraints == null)
			xyconstraints = defaultConstraints;
		Rectangle rectangle = new Rectangle(xyconstraints.x,
				xyconstraints.y, xyconstraints.width,
				xyconstraints.height);
		if (rectangle.width <= 0 || rectangle.height <= 0) {
			Dimension dimension = flag ? component
					.getPreferredSize() : component
					.getMinimumSize();
			if (rectangle.width <= 0)
				rectangle.width = dimension.width;
			if (rectangle.height <= 0)
				rectangle.height = dimension.height;
		}
		return rectangle;
	}

	Dimension getLayoutSize(Container container, boolean flag) {
		Dimension dimension = new Dimension(0, 0);
		if (this.width <= 0 || this.height <= 0) {
			int i = container.getComponentCount();
			for (int j = 0; j < i; j++) {
				Component component = container.getComponent(j);
				if (component.isVisible()) {
					Rectangle rectangle = getComponentBounds(
							component, flag);
					dimension.width = Math
							.max(
									dimension.width,
									rectangle.x
											+ rectangle.width);
					dimension.height = Math
							.max(
									dimension.height,
									rectangle.y
											+ rectangle.height);
				}
			}

		}
		if (this.width > 0)
			dimension.width = this.width;
		if (this.height > 0)
			dimension.height = this.height;
		Insets insets = container.getInsets();
		dimension.width += insets.left + insets.right;
		dimension.height += insets.top + insets.bottom;
		return dimension;
	}

}
