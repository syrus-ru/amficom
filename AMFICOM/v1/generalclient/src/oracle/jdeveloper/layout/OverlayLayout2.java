/*
 * $Id: OverlayLayout2.java,v 1.1 2005/03/18 09:44:58 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package oracle.jdeveloper.layout;

import java.awt.*;
import java.io.Serializable;
import javax.swing.OverlayLayout;

/**
 * @version $Revision: 1.1 $, $Date: 2005/03/18 09:44:58 $
 * @author $Author: bass $
 * @module generalclient_v1
 * @deprecated
 */
public class OverlayLayout2 implements LayoutManager2, Serializable {
	private static final long serialVersionUID = 3256718472657253687L;

	OverlayLayout layout;

	public OverlayLayout2() {
		// empty
	}

	public OverlayLayout2(Container container) {
		this.layout = new OverlayLayout(container);
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

	void verifyInstance(Container container) {
		if (this.layout == null)
			this.layout = new OverlayLayout(container);
	}
}
