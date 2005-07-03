/*
 * $Id: PaneNode.java,v 1.2 2005/05/18 14:01:18 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package oracle.jdeveloper.layout;

import java.awt.*;
import java.io.*;

/**
 * @version $Revision: 1.2 $, $Date: 2005/05/18 14:01:18 $
 * @author $Author: bass $
 * @module generalclient_v1
 * @deprecated
 */
class PaneNode implements Serializable {
	private static final long serialVersionUID = 3257282535158658100L;

	transient Component childComponent;

	PaneNode childNodeA;

	PaneNode childNodeB;

	float heightDivide;

	boolean horizontal;

	Rectangle location;

	String name;

	boolean reverse;

	float widthDivide;

	int xOffset;

	int yOffset;

	public PaneNode(PaneNode panenode, PaneNode panenode1, String s, float f) {
		this.location = new Rectangle();
		this.childNodeA = panenode;
		this.childNodeB = panenode1;
		if (s.equals("Top") || s.equals("Bottom")) {
			this.widthDivide = 1.0F;
			this.heightDivide = f;
			this.horizontal = true;
		} else {
			this.widthDivide = f;
			this.heightDivide = 1.0F;
			this.horizontal = false;
		}
		if (s.equals("Top") || s.equals("Left"))
			this.reverse = true;
		else
			this.reverse = false;
	}

	public PaneNode(String s, Component component, String s1) {
		this.location = new Rectangle();
		this.childComponent = component;
		this.name = s;
		this.widthDivide = 1.0F;
		this.heightDivide = 0.5F;
		this.horizontal = s1.equals("Top") || s1.equals("Bottom");
		this.reverse = s1.equals("Top") || s1.equals("Left");
	}

	public void addChild(String s, Component component, String s1, float f) {
		if (this.childComponent != null) {
			this.childNodeA = new PaneNode(this.name,
					this.childComponent, "Root");
			this.childComponent = null;
			this.name = null;
			this.childNodeB = new PaneNode(s, component, "Root");
			f = 1.0F - f;
			if (f > 1.0F || f < 0.0F)
				f = 0.5F;
			if (s1 == null) {
				if (this.horizontal)
					s1 = "Right";
				else
					s1 = "Bottom";
			} else if (s1.equals("Right") || s1.equals("Bottom")) {
				this.reverse = false;
			} else {
				this.reverse = true;
				if (!s1.equals("Left") && !s1.equals("Top")
						&& !s1.equals("Root"))
					f = 0.5F;
			}
			if (s1.equals("Left") || s1.equals("Right")) {
				this.widthDivide = f;
				this.heightDivide = 1.0F;
				this.horizontal = false;
			} else {
				this.widthDivide = 1.0F;
				this.heightDivide = f;
				this.horizontal = true;
			}
		} else {
			if (this.horizontal)
				s1 = "Right";
			else
				s1 = "Bottom";
			this.childNodeB.addChild(s, component, s1, f);
		}
	}

	public boolean addChildSplit(String s, String s1, String s2,
			Component component, float f) {
		if (this.childComponent != null) {
			if (this.name != null) {
				if (this.name.equals(s1)) {
					addChild(s, component, s2, f);
					return true;
				}
				return false;
			}
			return false;
		}
		if (!this.childNodeA.addChildSplit(s, s1, s2, component, f))
			return this.childNodeB.addChildSplit(s, s1, s2,
					component, f);
		return true;
	}

	public PaneNode getImmediateParent(Component component) {
		if (this.childComponent != null) {
			if (this.childComponent == component)
				return this;
			return null;
		}
		if (this.childNodeA.childComponent == component)
			return this;
		if (this.childNodeB.childComponent == component)
			return this;
		PaneNode panenode = this.childNodeA
				.getImmediateParent(component);
		if (panenode != null)
			return panenode;
		return this.childNodeB.getImmediateParent(component);
	}

	public String getNodeAComponent() {
		if (this.childComponent != null)
			return this.name;
		return this.childNodeA.getNodeAComponent();
	}

	public PaneNode getParentNode(Component component, PaneNode panenode) {
		if (this.childComponent != null) {
			if (this.childComponent == component)
				return this;
			return null;
		}
		if (this.childNodeA.childComponent == component) {
			if (panenode != null)
				return panenode;
			return this.childNodeA;
		}
		if (this.childNodeB.childComponent == component)
			return this;
		PaneNode panenode1 = this.childNodeA.getParentNode(component,
				panenode);
		if (panenode1 != null)
			return panenode1;
		return this.childNodeB.getParentNode(component, this);
	}

	public Dimension getPreferredSize(int i) {
		Dimension dimension = null;
		try {
			if (this.childComponent != null) {
				dimension = this.childComponent
						.getPreferredSize();
				Dimension dimension1 = dimension;
				return dimension1;
			}
			Dimension dimension2 = this.childNodeA
					.getPreferredSize(i);
			Dimension dimension3 = this.childNodeB
					.getPreferredSize(i);
			float f = this.widthDivide;
			if (!this.horizontal) {
				if (f >= 0.999D)
					dimension = dimension2;
				if (f == 0.0D)
					dimension = dimension3;
				else
					dimension = new Dimension(
							(int) Math
									.max(
											dimension2.width
													/ f,
											dimension3.width
													/ (1.0D - f)),
							Math
									.max(
											dimension2.height,
											dimension3.height));
				dimension.width += i + i + 1;
			} else {
				float f1 = this.heightDivide;
				if (f1 >= 0.999D)
					dimension = dimension2;
				else if (f1 == 0.0D)
					dimension = dimension3;
				else
					dimension = new Dimension(
							Math
									.max(
											dimension2.width,
											dimension3.width),
							(int) Math
									.max(
											dimension2.height
													/ f1,
											dimension3.height
													/ (1.0D - f1)));
				dimension.height += i + i + 1;
			}
		} catch (Exception exception) {
			// empty
		}
		return dimension;
	}

	public PaneNode removeChild(Component component) {
		PaneNode panenode = null;
		if (this.childNodeA.childComponent == component) {
			panenode = this.childNodeA;
			panenode.widthDivide = this.widthDivide;
			panenode.heightDivide = this.heightDivide;
			panenode.reverse = this.reverse;
			panenode.horizontal = this.horizontal;
			absorbChildNode(this.childNodeB);
			panenode.childNodeA = this;
			panenode.childNodeB = null;
		} else if (this.childNodeB.childComponent == component) {
			panenode = this.childNodeB;
			panenode.widthDivide = this.widthDivide;
			panenode.heightDivide = this.heightDivide;
			panenode.reverse = this.reverse;
			panenode.horizontal = this.horizontal;
			absorbChildNode(this.childNodeA);
			panenode.childNodeA = null;
			panenode.childNodeB = this;
		}
		return panenode;
	}

	public void setConstraints(Component component,
			PaneConstraints paneconstraints) {
		if (this.childComponent != null)
			return;
		if (paneconstraints.position.equals("Top")
				|| paneconstraints.position.equals("Left"))
			this.reverse = true;
		else
			this.reverse = false;
		if (paneconstraints.position.equals("Bottom")) {
			this.heightDivide = 1.0F - paneconstraints.proportion;
			this.widthDivide = 1.0F;
			this.horizontal = true;
		} else if (paneconstraints.position.equals("Top")) {
			this.heightDivide = 1.0F - paneconstraints.proportion;
			this.widthDivide = 1.0F;
			this.horizontal = true;
		} else if (paneconstraints.position.equals("Right")) {
			this.widthDivide = 1.0F - paneconstraints.proportion;
			this.heightDivide = 1.0F;
			this.horizontal = false;
		} else if (paneconstraints.position.equals("Left")) {
			this.widthDivide = 1.0F - paneconstraints.proportion;
			this.heightDivide = 1.0F;
			this.horizontal = false;
		}
	}

	void absorbChildNode(PaneNode panenode) {
		if (panenode.childComponent != null) {
			this.childComponent = panenode.childComponent;
			this.name = panenode.name;
		} else {
			this.childComponent = null;
			this.childNodeA = panenode.childNodeA;
			this.childNodeB = panenode.childNodeB;
			this.widthDivide = panenode.widthDivide;
			this.heightDivide = panenode.heightDivide;
			this.reverse = panenode.reverse;
			this.horizontal = panenode.horizontal;
		}
	}

	void assertLocation(Rectangle rectangle, int i) {
		this.location.x = rectangle.x;
		this.location.y = rectangle.y;
		this.location.width = rectangle.width;
		this.location.height = rectangle.height;
		if (this.childComponent != null) {
			if (!this.childComponent.getBounds().equals(
					this.location)) {
				this.childComponent.setBounds(this.location.x,
						this.location.y,
						this.location.width,
						this.location.height);
				if (this.childComponent instanceof Container)
					((Container) this.childComponent)
							.doLayout();
			}
		} else {
			Rectangle rectangle1 = new Rectangle();
			Rectangle rectangle2 = new Rectangle();
			calculateLocations(this.location, rectangle1,
					rectangle2, i);
			this.childNodeA.assertLocation(rectangle1, i);
			this.childNodeB.assertLocation(rectangle2, i);
		}
	}

	void calculateLocations(Rectangle rectangle, Rectangle rectangle1,
			Rectangle rectangle2, int i) {
		i += i;
		if (this.heightDivide == 1.0F) {
			float f = this.widthDivide;
			if (this.reverse)
				f = 1.0F - f;
			this.xOffset = (int) ((rectangle.width - i) * f);
			rectangle1.x = rectangle.x;
			rectangle1.y = rectangle.y;
			rectangle1.width = this.xOffset;
			rectangle1.height = rectangle.height;
			rectangle2.x = rectangle.x + this.xOffset + i;
			rectangle2.y = rectangle.y;
			rectangle2.width = rectangle.width - this.xOffset - i;
			rectangle2.height = rectangle.height;
		} else {
			float f1 = this.heightDivide;
			if (this.reverse)
				f1 = 1.0F - f1;
			this.yOffset = (int) ((rectangle.height - i) * f1);
			rectangle1.x = rectangle.x;
			rectangle1.y = rectangle.y;
			rectangle1.width = rectangle.width;
			rectangle1.height = this.yOffset;
			rectangle2.x = rectangle.x;
			rectangle2.y = rectangle.y + this.yOffset + i;
			rectangle2.width = rectangle.width;
			rectangle2.height = rectangle.height - this.yOffset - i;
		}
		if (this.reverse) {
			Rectangle rectangle3 = new Rectangle(rectangle1.x,
					rectangle1.y, rectangle1.width,
					rectangle1.height);
			rectangle1.x = rectangle2.x;
			rectangle1.y = rectangle2.y;
			rectangle1.width = rectangle2.width;
			rectangle1.height = rectangle2.height;
			rectangle2.x = rectangle3.x;
			rectangle2.y = rectangle3.y;
			rectangle2.width = rectangle3.width;
			rectangle2.height = rectangle3.height;
		}
	}

	void drag(int i, int j) {
		if (this.childComponent == null)
			if (this.heightDivide == 1.0F) {
				this.widthDivide = (float) (i - this.location.x)
						/ (float) this.location.width;
				if (this.reverse)
					this.widthDivide = 1.0F - this.widthDivide;
				if (this.widthDivide < 0.0F) {
					this.widthDivide = 0.0F;
				} else if (this.widthDivide >= 1.0F) {
					this.widthDivide = 0.999F;
				}
			} else {
				if (this.reverse)
					this.heightDivide = 1.0F - this.heightDivide;
				this.heightDivide = (float) (j - this.location.y)
						/ (float) this.location.height;
				if (this.reverse)
					this.heightDivide = 1.0F - this.heightDivide;
				if (this.heightDivide < 0.0F) {
					this.heightDivide = 0.0F;
				} else if (this.heightDivide >= 1.0F) {
					this.heightDivide = 0.999F;
				}
			}
	}

	void getComponents(Point point, String as[], boolean flag) {
		if (this.childComponent == null) {
			if (flag) {
				as[point.x] = this.childNodeA
						.getNodeAComponent();
				point.x++;
			}
			as[point.x] = this.childNodeB.getNodeAComponent();
			point.x++;
			this.childNodeA.getComponents(point, as, false);
			this.childNodeB.getComponents(point, as, false);
		}
	}

	Rectangle getDividerRect(int i) {
		if (this.childComponent == null) {
			if (this.heightDivide == 1.0F) {
				if (this.reverse)
					return new Rectangle(
							this.childNodeB.location.x
									+ this.childNodeB.location.width,
							this.childNodeB.location.y,
							i,
							this.childNodeB.location.height);
				return new Rectangle(
						this.childNodeA.location.x
								+ this.childNodeA.location.width,
						this.childNodeA.location.y, i,
						this.childNodeA.location.height);
			}
			if (this.reverse)
				return new Rectangle(
						this.childNodeB.location.x,
						this.childNodeB.location.y
								+ this.childNodeB.location.height,
						this.childNodeB.location.width,
						i);
			return new Rectangle(
					this.childNodeA.location.x,
					this.childNodeA.location.y
							+ this.childNodeA.location.height,
					this.childNodeA.location.width, i);
		}
		return new Rectangle(this.location.x - i,
				(this.yOffset + this.location.y) - i,
				this.location.width + i, i);
	}

	PaneNode hitTest(int i, int j, int k) {
		if (this.location != null && this.childComponent == null) {
			int l;
			if (this.heightDivide == 1.0F)
				l = i - (this.location.x + this.xOffset);
			else
				l = j - (this.location.y + this.yOffset);
			if (Math.abs(l) <= k)
				return this;
			if ((l < 0) ^ this.reverse)
				return this.childNodeA.hitTest(i, j, k);
			return this.childNodeB.hitTest(i, j, k);
		}
		return null;
	}

	private void readObject(ObjectInputStream objectinputstream)
			throws ClassNotFoundException, IOException {
		objectinputstream.defaultReadObject();
		Object obj = objectinputstream.readObject();
		if (obj instanceof Component)
			this.childComponent = (Component) obj;
	}

	private void writeObject(ObjectOutputStream objectoutputstream)
			throws IOException {
		objectoutputstream.defaultWriteObject();
		objectoutputstream.writeObject(this.childComponent);
	}
}
