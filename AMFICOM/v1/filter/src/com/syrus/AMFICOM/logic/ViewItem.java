/*
 * $Id: ViewItem.java,v 1.2 2005/03/10 15:17:48 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
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
import java.util.List;
import java.util.Map;

/**
 * @version $Revision: 1.2 $, $Date: 2005/03/10 15:17:48 $
 * @author $Author: bob $
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

												public int compare(	Object o1,
																	Object o2) {
													if (o1 == o2)
														return 0;
													if (o1 == null)
														return -1;
													if (o2 == null)
														return 1;
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
						item2ItemViewMap.put(item2, viewItem);
					}
				}
				this.addChild(viewItem, false);
			}
		}
		this.sourceItem.addChangeListener(this);
	}

	private void addChild(	Item childItem,
							boolean addToSource) {

		System.out.println("ViewItem.addChild | this.name: " + this.sourceItem.getName() + "\n\t name: "
				+ childItem.getName());
		if (this.children == null)
			this.children = new LinkedList();
		if (this.children.size() <= this.sourceItem.getMaxChildrenCount()) {
			ViewItem viewItem;
			if (childItem instanceof ViewItem) {
				viewItem = (ViewItem) childItem;
			} else
				viewItem = (ViewItem) item2ItemViewMap.get(childItem);

			if (checkForRecursion(viewItem, this)) { throw new UnsupportedOperationException(
																								"Recursion isn't supported."); }
			if (this.children.contains(viewItem))
				return;

			this.children.add(viewItem);
			this.sortedChildren = false;

			if (addToSource)
				this.sourceItem.addChild(viewItem.getSourceItem());

			Collection parents1 = viewItem.getParents();
			if (parents1 == null || !parents1.contains(this)) {
				viewItem.addParent(this, addToSource);
			}

		} else
			throw new UnsupportedOperationException("There cannot be more than "
					+ this.sourceItem.getMaxChildrenCount() + " linked input items");
	}

	public void addChild(Item childItem) {
		this.addChild(childItem, true);
	}

	public void addChildPerformed(	Item source,
									Item childItem) {
		this.addChild(childItem, false);

	}

	public List getChildren() {
		return this.children;
	}

	private void removeChild(	Item childItem,
								boolean removeFromSource) {
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

		Collection parents1 = childItem.getParents();
		if (parents1 != null && parents1.contains(this))
			viewItem.removeParent(this, removeFromSource);
		if (removeFromSource)
			this.sourceItem.removeChild(viewItem.getSourceItem());
	}

	public void removeChild(Item childItem) {
		this.removeChild(childItem, true);
	}

	public void removeChildPerformed(	Item source,
										Item childItem) {
		this.removeChild(childItem, false);
	}

	public List getParents() {
		return this.parents;
	}

	private void addParent(	Item parent,
							boolean addToSource) {
		System.out.println("ViewItem.addParent | this.name: " + this.sourceItem.getName() + " \n\t name: "
				+ parent.getName());
		ViewItem viewItem;
		if (parent instanceof ViewItem) {
			viewItem = (ViewItem) parent;
		} else
			viewItem = (ViewItem) item2ItemViewMap.get(parent);

		if ((this.parents == null && this.sourceItem.getMaxParentCount() == 0)
				|| (this.parents != null && this.parents.size() > this.sourceItem.getMaxParentCount()))
			throw new UnsupportedOperationException("There cannot be more than " + this.sourceItem.getMaxParentCount()
					+ " parent items at item '" + this.sourceItem.getName() + "', parent item '" + viewItem.getName()
					+ '\'');
		if (this.parents == null)
			this.parents = new LinkedList();

		if (this.parents.contains(viewItem))
			return;

		this.parents.add(viewItem);

		if (addToSource)
			this.sourceItem.addParent(viewItem.sourceItem);

		Collection children1 = viewItem.getChildren();
		if (children1 == null || !children1.contains(this))
			viewItem.addChild(this, addToSource);
	}

	public void addParent(Item parent) {
		this.addParent(parent, true);
	}

	public void addParentPerformed(	Item source,
									Item parent) {
		this.addParent(parent, false);
	}

	private void removeParent(	Item parent,
								boolean removeFromSource) {
		ViewItem viewItem;
		if (parent instanceof ViewItem) {
			viewItem = (ViewItem) parent;
		} else
			viewItem = (ViewItem) item2ItemViewMap.get(parent);

		if (this.parents != null) {
			System.out.println("ViewItem.removeParent | this.name: " + this.sourceItem.getName() + " \n\t name: "
					+ parent.getName());
			this.parents.remove(viewItem);
		}
		if (removeFromSource)
			this.sourceItem.removeParent(viewItem.sourceItem);

		Collection children1 = viewItem.getChildren();
		if (children1 != null && children1.contains(this))
			viewItem.removeChild(this, removeFromSource);
	}

	public void removeParent(Item parent) {
		this.removeParent(parent, true);
	}

	public void removeParentPerformed(	Item source,
										Item parent) {
		this.removeParent(parent, false);
	}

	public int getMaxChildrenCount() {
		return this.sourceItem.getMaxChildrenCount();
	}

	public int getMaxParentCount() {
		return this.sourceItem.getMaxParentCount();
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
		if (this.parents != null && !this.parents.isEmpty()) {
			for (Iterator it = this.parents.iterator(); it.hasNext();) {
				ViewItem viewItem = (ViewItem) it.next();
				int hw = viewItem.getHierarchicalWidth();
				w = w > hw ? w : hw;
			}
		}
		return w + this.width;
	}

	public int getHierarchicalCount() {
		int count = 0;
		int w = 0;
		if (this.parents != null && !this.parents.isEmpty()) {
			for (Iterator it = this.parents.iterator(); it.hasNext();) {
				ViewItem viewItem = (ViewItem) it.next();
				int hw = viewItem.getHierarchicalWidth();
				if (hw > w) {
					w = hw;
					count = viewItem.getHierarchicalCount();
				}

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
					viewItem.move(0, vMaxH -vMinH2 + edge);
				}

				vMaxH = viewItem.getMaxY();

			}
		}
	}
	
	public void separateChildrenX(ViewItem rootItem, int width1) {
		if (rootItem != null)
			this.x = rootItem.x - width1 - rootItem.getWidth();
		if (this.children != null && !this.children.isEmpty()) {
			for (Iterator it = this.children.iterator(); it.hasNext();) {
				ViewItem viewItem = (ViewItem) it.next();
				viewItem.separateChildrenX(this, width1);
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

	public void move(	int dx,
						int dy) {
		if (dx == 0 && dy == 0)
			return;
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

}
