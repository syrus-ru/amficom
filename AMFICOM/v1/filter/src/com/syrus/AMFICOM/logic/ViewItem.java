/*
 * $Id: ViewItem.java,v 1.7 2005/03/21 08:41:34 bob Exp $
 *
 * Copyright ? 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.logic;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/**
 * @version $Revision: 1.7 $, $Date: 2005/03/21 08:41:34 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module filter_v1
 */
public class ViewItem extends AbstractItem implements Item, ItemListener {

	private int			width				= -1;
	private int			height				= -1;
	int					x					= 0;
	int					y					= 0;

	static int			edge				= 0;

	private int			minY				= 0;
	private int			maxY				= 0;

	private boolean		sortedChildren		= false;

	static Comparator	ycomparator			= new Comparator() {

												public int compare(Object o1, Object o2) {
													if (o1 == o2) return 0;
													if (o1 == null) return -1;
													if (o2 == null) return 1;
													if (o1 instanceof ViewItem && o2 instanceof ViewItem) {
														ViewItem item1 = (ViewItem) o1;
														ViewItem item2 = (ViewItem) o2;
														return item1.y - item2.y;
													}
													return 0;
												}
											};

	private Item		sourceItem;

	public static Map	item2ItemViewMap	= new HashMap();

	// public ViewItem(int inputCount, int outputCount, String name, Object
	// object, int x, int y) {
	// this.maxChildrenCount = inputCount;
	// this.maxParentCount = outputCount;
	// this.name = name;
	// this.object = object;
	// this.x = x;
	// this.y = y;
	// }

	ViewItem(Item item) {
		this.sourceItem = item;
		item2ItemViewMap.put(item, this);
		Collection itemChildren = item.getChildren();
		if (itemChildren != null) {
			this.children = new LinkedList();
			for (Iterator it = itemChildren.iterator(); it.hasNext();) {
				Item item2 = (Item) it.next();
				ViewItem viewItem;
				if (item2 instanceof ViewItem) {
					viewItem = (ViewItem) item2;
				} else {
					viewItem = (ViewItem) item2ItemViewMap.get(item2);
					if (viewItem == null) {
						viewItem = new ViewItem(item2);
					}
				}
				this.addChild(viewItem, false);
			}
		}
		this.sourceItem.addChangeListener(this);
	}

	private void addChild(Item childItem, boolean addToSource) {

		System.out.println("ViewItem.addChild | this.name: " + this.sourceItem.getName() + "\n\t name: "
				+ childItem.getName());
		if (this.children == null) this.children = new LinkedList();

		ViewItem viewItem;
		if (childItem instanceof ViewItem) {
			viewItem = (ViewItem) childItem;
		} else
			viewItem = (ViewItem) item2ItemViewMap.get(childItem);

		super.addChild(viewItem);

		if (this.children.contains(viewItem)) return;

		if (this.getChildrenCount() < this.sourceItem.getMaxChildrenCount()) {

			this.children.add(viewItem);
			this.sortedChildren = false;
			if (addToSource) this.sourceItem.addChild(viewItem.getSourceItem());

		} else
			throw new UnsupportedOperationException("There cannot be more than "
					+ this.sourceItem.getMaxChildrenCount() + " linked input items");
	}

	public void addChild(Item childItem) {
		this.addChild(childItem, true);
	}

	public void removeChild(Item childItem) {
		ViewItem viewItem;
		if (childItem instanceof ViewItem) {
			viewItem = (ViewItem) childItem;
		} else
			viewItem = (ViewItem) item2ItemViewMap.get(childItem);

		if (this.children != null) {

			System.out.println("ViewItem.removeChild | this.name: " + this.sourceItem.getName() + "\n\t name: "
					+ childItem.getName());
			this.children.remove(viewItem);
		}
	}

	public void setParent(Item parent) {
		System.out.println("ViewItem.setParent | this.name: " + this.sourceItem.getName() + " \n\t name: "
				+ (parent == null ? "'null'" : parent.getName()));
		ViewItem viewItem;
		if (parent instanceof ViewItem) {
			viewItem = (ViewItem) parent;
		} else
			viewItem = (ViewItem) item2ItemViewMap.get(parent);

		super.setParent(viewItem);

		this.sourceItem.setParent(viewItem == null ? null : viewItem.sourceItem);
	}

	public int getChildrenCount() {
		int count = 0;
		if (this.children != null && !this.children.isEmpty()) {
			for (Iterator it = this.children.iterator(); it.hasNext();) {
				ViewItem viewItem = (ViewItem) it.next();
				count += viewItem.isService() ? 0 : 1;
			}
		}
		return count;
	}

	public void setParentPerformed(Item item, Item oldParent, Item newParent) {
		item.setParent(newParent);
	}

	public boolean isParentAllow() {
		return this.sourceItem.isParentAllow();
	}

	public int getMaxChildrenCount() {
		return this.sourceItem.getMaxChildrenCount();
	}

	public String getName() {
		return this.sourceItem.getName();
	}

	public Object getObject() {
		return this.sourceItem.getObject();
	}

	public Item getSourceItem() {
		return this.sourceItem;
	}

	public int getHierarchicalWidth() {
		int w = 0;
		if (super.parent != null) {
			ViewItem viewItem = (ViewItem) super.parent;
			int hw = viewItem.getHierarchicalWidth();
			w = w > hw ? w : hw;
		}
		return w + this.width;
	}

	public int getHierarchicalCount() {
		int count = 0;
		int w = 0;
		if (super.parent != null) {
			ViewItem viewItem = (ViewItem) super.parent;
			int hw = viewItem.getHierarchicalWidth();
			if (hw > w) {
				w = hw;
				count = viewItem.getHierarchicalCount();
			}
		}
		return count + 1;
	}

	public int getMinY() {
		this.minY = Integer.MAX_VALUE;
		this.maxY = -Integer.MAX_VALUE;
		// if (this.minY == this.maxY) {
		if (this.children != null && !this.children.isEmpty()) {
			if (!this.sortedChildren) {
				Collections.sort(this.children, ycomparator);
				this.sortedChildren = true;
			}
			for (Iterator it = this.children.iterator(); it.hasNext();) {
				ViewItem viewItem = (ViewItem) it.next();
				int iminY = viewItem.getMinY();
				int imaxY = viewItem.getMaxY();
				this.minY = this.minY < iminY ? this.minY : iminY;
				this.maxY = this.maxY > imaxY ? this.maxY : imaxY;
			}
		} else {
			this.minY = this.y;
			this.maxY = this.y + this.height;
		}
		// }
		// System.out.println(this.getName() + " minY: " + this.minY);
		return this.minY;
	}

	public int getMaxY() {
		// if (this.minY == this.maxY) {
		this.getMinY();
		// }
		// System.out.println(this.getName() + " maxY: " + this.maxY);
		return this.maxY;
	}

	public void separateChildrenY() {
		// System.out.println("separateChildrenY>" + this.getName());
		if (this.children != null && !this.children.isEmpty()) {
			if (!this.sortedChildren) {
				Collections.sort(this.children, ycomparator);
				this.sortedChildren = true;
			}
			boolean firstItem = true;
			int vMaxH = 0;
			for (Iterator it = this.children.iterator(); it.hasNext();) {
				ViewItem viewItem = (ViewItem) it.next();
				viewItem.separateChildrenY();

				if (firstItem) {
					firstItem = false;
					// System.out.println();
					// System.out.println("name>" + this.getName());
					// System.out.println("(this.children.size() - 1):" +
					// (this.children.size() - 1));
					// System.out.println("this.getChildrenHeight():" +
					// this.getChildrenHeight());
					viewItem.y = this.y + (this.height - this.getChildrenHeight() - (this.children.size() - 1) * edge)
							/ 2;
					// System.out.println(">" + (this.height -
					// this.getChildrenHeight() - (this.children.size() - 1) *
					// edge) / 2);
				} else {
					int vMinH2 = viewItem.getMinY();
					viewItem.move(0, vMaxH - vMinH2 + edge);
				}

				vMaxH = viewItem.getMaxY();

			}
		}
	}

	public void separateChildrenX(int width1) {
		if (this.children != null && !this.children.isEmpty()) {
			for (Iterator it = this.children.iterator(); it.hasNext();) {
				ViewItem viewItem = (ViewItem) it.next();
				viewItem.x = this.x - width1 - this.getWidth();
				viewItem.separateChildrenX(width1);
			}
		}
	}

	public int getMinX() {
		int minX = this.x;
		if (this.children != null && !this.children.isEmpty()) {
			for (Iterator it = this.children.iterator(); it.hasNext();) {
				ViewItem viewItem = (ViewItem) it.next();
				int minX2 = viewItem.getMinX();
				minX = minX < minX2 ? minX : minX2;
			}
		}
		return minX;
	}

	public void move(int dx, int dy) {
		if (dx == 0 && dy == 0) return;
		this.minY = 0;
		this.maxY = 0;
		this.x += dx;
		this.y += dy;
		if (this.children != null && !this.children.isEmpty()) {
			for (Iterator it = this.children.iterator(); it.hasNext();) {
				ViewItem viewItem = (ViewItem) it.next();
				viewItem.move(dx, dy);
			}
		}
	}

	public int getChildrenHeight() {
		int h = 0;
		if (this.children != null && !this.children.isEmpty()) {
			for (Iterator it = this.children.iterator(); it.hasNext();) {
				ViewItem viewItem = (ViewItem) it.next();
				h += viewItem.height;
			}
		}
		return h;
	}

	public int getHeight() {
		return this.height;
	}

	public void setHeight(int height) {
		this.height = height;
		edge = edge > 6 * height / 5 ? edge : 6 * height / 5;
	}

	public int getWidth() {
		return this.width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public boolean isService() {
		return this.sourceItem.isService();
	}

}
