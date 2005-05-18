/*
 * $Id: PaneLayout.java,v 1.2 2005/05/18 14:01:18 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package oracle.jdeveloper.layout;

import java.awt.*;
import java.io.Serializable;

/**
 * @version $Revision: 1.2 $, $Date: 2005/05/18 14:01:18 $
 * @author $Author: bass $
 * @module generalclient_v1
 * @deprecated
 */
public class PaneLayout implements LayoutManager2, Serializable {

	private static final long serialVersionUID = 200L;

	int gap;

	PaneNode lastSelected;

	PaneNode rootNode;

	private int addCount;

	private String lastComponentAdded;

	private PaneNode lastDeletion;

	public PaneLayout() {
		// empty
	}

	public void addLayoutComponent(Component component, Object obj) {
		if (obj == null
				&& component.getName() != null
				&& component.getName().equals(
						"SplitPanel.splitter"))
			return;
		if (obj instanceof PaneConstraints) {
			if (!justDeleted(component, (PaneConstraints) obj)) {
				if (((PaneConstraints) obj).name == null)
					this.addCount++;
				if (setComponentConstraints(component,
						(PaneConstraints) obj)) {
					this.lastDeletion = null;
					return;
				}
				PaneConstraints paneconstraints = (PaneConstraints) obj;
				addChild(
						paneconstraints.name,
						paneconstraints.splitComponentName,
						paneconstraints.position,
						component,
						paneconstraints.proportion);
				this.lastComponentAdded = paneconstraints.name;
			}
		} else {
			addChild(component, 0.5F);
		}
		this.lastDeletion = null;
	}

	public void addLayoutComponent(String s, Component component) {
		PaneConstraints paneconstraints = new PaneConstraints(s, "",
				"", 0.5F);
		addLayoutComponent(component, paneconstraints);
	}

	public void dragDivider(int i, int j) {
		if (this.lastSelected != null)
			this.lastSelected.drag(i, j);
	}

	public String[] getAddOrder(Container container) {
		Component acomponent[] = container.getComponents();
		String as[] = new String[acomponent.length];
		Point point = new Point(0, 0);
		if (this.rootNode != null)
			if (this.rootNode.childComponent == null)
				this.rootNode.getComponents(point, as, true);
			else
				as[0] = this.rootNode.name;
		return as;
	}

	public PaneConstraints getConstraints(Component component) {
		PaneConstraints paneconstraints = null;
		if (this.rootNode != null) {
			PaneNode panenode = this.rootNode.getParentNode(
					component, null);
			if (panenode != null)
				if (panenode.childComponent == component) {
					paneconstraints = new PaneConstraints(
							panenode.name,
							panenode.name, "Root",
							0.5F);
				} else {
					float f = panenode.heightDivide
							* panenode.widthDivide;
					String s = panenode.childNodeA
							.getNodeAComponent();
					String s1 = panenode.childNodeB
							.getNodeAComponent();
					if (panenode.childNodeA.childComponent == component)
						s1 = panenode.childNodeB.name;
					if (panenode.horizontal) {
						if (panenode.reverse)
							paneconstraints = new PaneConstraints(
									s1,
									s,
									"Top",
									1.0F - f);
						else
							paneconstraints = new PaneConstraints(
									s1,
									s,
									"Bottom",
									1.0F - f);
					} else if (panenode.reverse)
						paneconstraints = new PaneConstraints(
								s1, s, "Left",
								1.0F - f);
					else
						paneconstraints = new PaneConstraints(
								s1, s, "Right",
								1.0F - f);
					if (s1.equals(s))
						paneconstraints.position = "Root";
				}
		}
		return paneconstraints;
	}

	public Rectangle getDividerBounds() {
		if (this.lastSelected != null)
			return this.lastSelected.location;
		return null;
	}

	public Rectangle getDividerRect(int i, int j) {
		if (this.rootNode != null)
			this.lastSelected = this.rootNode.hitTest(i, j,
					this.gap * 2);
		if (this.lastSelected != null)
			return this.lastSelected.getDividerRect(this.gap * 2);
		return null;
	}

	public int getGap() {
		return this.gap;
	}

	public float getLayoutAlignmentX(Container container) {
		return 0.5F;
	}

	public float getLayoutAlignmentY(Container container) {
		return 0.5F;
	}

	public void invalidateLayout(Container container) {
		// empty
	}

	public void layoutContainer(Container container) {
		if (this.rootNode != null) {
			Dimension dimension = container.getSize();
			Insets insets = container.getInsets();
			Rectangle rectangle = new Rectangle(insets.left,
					insets.top, dimension.width
							- insets.left
							- insets.right,
					dimension.height - insets.top
							- insets.bottom);
			this.rootNode.assertLocation(rectangle, this.gap);
		}
	}

	public Dimension maximumLayoutSize(Container container) {
		return new Dimension(500, 500);
	}

	public Dimension minimumLayoutSize(Container container) {
		return preferredLayoutSize(container);
	}

	public Dimension preferredLayoutSize(Container container) {
		Insets insets = container.getInsets();
		if (this.rootNode != null) {
			Dimension dimension = this.rootNode
					.getPreferredSize(this.gap);
			dimension.width += insets.right + insets.left;
			dimension.height += insets.top + insets.bottom;
			return dimension;
		}
		return new Dimension(10, 10);
	}

	public void removeLayoutComponent(Component component) {
		if (component == null) {
			this.lastDeletion = null;
			return;
		}
		if (this.rootNode != null)
			if (this.rootNode.childComponent == component) {
				this.rootNode = null;
			} else {
				PaneNode panenode = this.rootNode
						.getImmediateParent(component);
				if (panenode != null)
					this.lastDeletion = panenode
							.removeChild(component);
			}
	}

	public void setConstraints(Component component,
			PaneConstraints paneconstraints) {
		setComponentConstraints(component, paneconstraints);
		this.lastDeletion = null;
	}

	public void setGap(int i) {
		this.gap = i;
	}

	public String toString() {
		return "PaneLayout";
	}

	void addChild(Component component, float f) {
		this.addCount++;
		String s = "component" + this.addCount;
		if (this.rootNode == null)
			this.rootNode = new PaneNode(s, component, "Top");
		else
			this.rootNode.addChild(s, component, "Bottom", f);
		this.lastComponentAdded = s;
	}

	void addChild(String s, String s1, String s2, Component component,
			float f) {
		if (this.rootNode == null) {
			this.rootNode = new PaneNode(s, component, "Top");
		} else {
			boolean flag = true;
			if (s1 == null || s1.length() == 0) {
				if (s2 == null)
					this.rootNode.addChild(s, component,
							null, f);
				else
					flag = this.rootNode
							.addChildSplit(
									s,
									this.lastComponentAdded,
									s2,
									component,
									f);
			} else if (!this.rootNode.addChildSplit(s, s1, s2,
					component, f))
				flag = this.rootNode.addChildSplit(s,
						this.lastComponentAdded, s2,
						component, f);
			if (!flag)
				this.rootNode.addChild(s, component, s2, f);
		}
	}

	boolean justDeleted(Component component, PaneConstraints paneconstraints) {
		if (this.lastDeletion == null || paneconstraints == null)
			return false;
		if (this.lastDeletion.childComponent == component) {
			if (this.lastDeletion.childNodeA == null) {
				PaneNode panenode = this.lastDeletion.childNodeB;
				if (panenode == null)
					return false;
				if (paneconstraints.splitComponentName != null
						&& paneconstraints.splitComponentName
								.equals(panenode
										.getNodeAComponent())
						|| !this.lastDeletion.name
								.equals(paneconstraints.name)) {
					PaneNode panenode2 = new PaneNode(
							panenode.childNodeA,
							panenode.childNodeB,
							"", 0.5F);
					panenode2.widthDivide = panenode.widthDivide;
					panenode2.heightDivide = panenode.heightDivide;
					panenode2.name = panenode.name;
					this.lastDeletion.name = paneconstraints.name;
					panenode2.childComponent = panenode.childComponent;
					panenode2.reverse = panenode.reverse;
					panenode2.horizontal = panenode.horizontal;
					panenode.childNodeA = panenode2;
					panenode.childNodeB = this.lastDeletion;
					panenode.name = null;
					panenode.childComponent = null;
					panenode.widthDivide = this.lastDeletion.widthDivide;
					panenode.heightDivide = this.lastDeletion.heightDivide;
					panenode.reverse = this.lastDeletion.reverse;
					panenode.horizontal = this.lastDeletion.horizontal;
					this.lastDeletion = null;
					setConstraints(component,
							paneconstraints);
					return true;
				}
			} else {
				PaneNode panenode1 = this.lastDeletion.childNodeA;
				PaneNode panenode3 = new PaneNode(
						panenode1.childNodeA,
						panenode1.childNodeB, "", 0.5F);
				panenode3.widthDivide = panenode1.widthDivide;
				panenode3.heightDivide = panenode1.heightDivide;
				panenode3.name = panenode1.name;
				this.lastDeletion.name = paneconstraints.name;
				panenode3.childComponent = panenode1.childComponent;
				panenode3.reverse = panenode1.reverse;
				panenode3.horizontal = panenode1.horizontal;
				panenode1.childNodeA = this.lastDeletion;
				panenode1.childNodeB = panenode3;
				panenode1.name = null;
				panenode1.childComponent = null;
				panenode1.widthDivide = this.lastDeletion.widthDivide;
				panenode1.heightDivide = this.lastDeletion.heightDivide;
				panenode1.reverse = this.lastDeletion.reverse;
				panenode1.horizontal = this.lastDeletion.horizontal;
				this.lastDeletion = null;
				setConstraints(component, paneconstraints);
				return true;
			}
			this.lastDeletion = null;
		}
		return false;
	}

	private boolean setComponentConstraints(Component component,
			PaneConstraints paneconstraints) {
		if (this.rootNode == null) {
			this.rootNode = new PaneNode(paneconstraints.name,
					component, "Root");
		} else {
			PaneNode panenode = this.rootNode.getParentNode(
					component, null);
			if (panenode != null) {
				panenode.setConstraints(component,
						paneconstraints);
				return true;
			}
			return false;
		}
		return true;
	}
}
