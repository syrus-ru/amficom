/*
 * $Id: BoxLayout2.java,v 1.1 2005/03/18 09:44:58 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package oracle.jdeveloper.layout;

import java.awt.*;
import java.io.Serializable;
import javax.swing.BoxLayout;

/**
 * @version $Revision: 1.1 $, $Date: 2005/03/18 09:44:58 $
 * @author $Author: bass $
 * @module generalclient_v1
 * @deprecated
 */
public class BoxLayout2 implements LayoutManager2, Serializable {
	private static final long serialVersionUID = 3258126972906385972L;

	int axis;

	BoxLayout layout;

	public BoxLayout2() {
		this.axis = 0;
	}

	public BoxLayout2(Container container, int i) {
		this.axis = 0;
		this.axis = i;
		this.layout = new BoxLayout(container, i);
	}

	public void addLayoutComponent(Component component, Object obj) {
		try {
			component.getParent().invalidate();
		} catch (Exception exception) {
			// empty
		}
	}

	public void addLayoutComponent(String s, Component component) {
		try {
			component.getParent().invalidate();
		} catch (Exception exception) {
			// empty
		}
	}

	public int getAxis() {
		return this.axis;
	}

	public float getLayoutAlignmentX(Container container) {
		verifyInstance(container);
		return this.layout.getLayoutAlignmentX(container);
	}

	public float getLayoutAlignmentY(Container container) {
		verifyInstance(container);
		return this.layout.getLayoutAlignmentY(container);
	}

	public void invalidateLayout(Container container) {
		verifyInstance(container);
		this.layout.invalidateLayout(container);
	}

	public void layoutContainer(Container container) {
		verifyInstance(container);
		this.layout.layoutContainer(container);
	}

	public Dimension maximumLayoutSize(Container container) {
		verifyInstance(container);
		return this.layout.maximumLayoutSize(container);
	}

	public Dimension minimumLayoutSize(Container container) {
		verifyInstance(container);
		return this.layout.minimumLayoutSize(container);
	}

	public Dimension preferredLayoutSize(Container container) {
		verifyInstance(container);
		return this.layout.preferredLayoutSize(container);
	}

	public void removeLayoutComponent(Component component) {
		try {
			component.getParent().invalidate();
		} catch (Exception exception) {
			// empty
		}
	}

	public void setAxis(int i) {
		if (i != this.axis) {
			this.layout = null;
			this.axis = i;
		}
	}

	void verifyInstance(Container container) {
		if (this.layout == null)
			this.layout = new BoxLayout(container, this.axis);
	}
}
