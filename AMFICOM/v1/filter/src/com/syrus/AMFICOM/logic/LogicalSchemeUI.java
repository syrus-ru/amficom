/*
 * $Id: LogicalSchemeUI.java,v 1.26 2005/09/20 12:33:06 bob Exp $
 *
 * Copyright ? 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.logic;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * @version $Revision: 1.26 $, $Date: 2005/09/20 12:33:06 $
 * @author $Author: bob $
 * @module filter
 */
public class LogicalSchemeUI extends JComponent implements MouseListener, MouseMotionListener, SelectionListener, ItemListener {
	private static final long serialVersionUID = 3258408430686581815L;

	static final Color EDGE_COLOR = Color.GRAY;
	static final Color SELECTED_EDGE_COLOR = Color.BLACK;
	static final Color ITEM_BG_COLOR = new Color(220, 220, 220);
	static final Color TEXT_COLOR = Color.BLACK;

	private Set<ViewItem> items;

	static final int SELECT_AREA = 10;
	static final int EDGE_THICK = 20;

	static final double ARROW_LENGTH = 15.0;
	static final double ARROW_WEIGHT = 4.0;

	private Font regularFont;
	private Font boldFont;

	private FontMetrics fontMetrics;

	private Stroke regularStroke;
	private Stroke boldStroke;

	private ViewItem newItem;
	private ViewItem selectedItem;
	private ViewItem linkItem;

	private ViewItem rootServiceItem;

	private ViewItem preSelectedItem;

	private Set<ViewItem> selectedItems;

	private ViewItem firstSelectedLineItem;
	private ViewItem secondSelectedLineItem;

	private int x;
	private int y;

	private int dx;
	private int dy;

	private int fontXOffset = -1;

	private boolean mouseDragging = false;
	private boolean mouseMoving = false;

	private boolean firstPaint = true;

	private SelectionListener[] selectionListeners = new SelectionListener[0];

// private AddDeleteItems[] addDeleteItems = new AddDeleteItems[0];

	public LogicalSchemeUI(Item rootItem) {
		if (!rootItem.isService()) {
			if (this.rootServiceItem == null) {
				final Item rootItem2 = new ServiceItem();
				rootItem2.addChild(rootItem);
				rootItem = rootItem2;
			} else {
				Item rootItem2 = this.rootServiceItem.getSourceItem();
				List<Item> children = new ArrayList<Item>(rootItem2.getChildren());
				for (final Item item : children) {
					item.setParent(null);
				}
				rootItem2.addChild(rootItem);
			}
		}
		if (this.rootServiceItem == null) {
			this.rootServiceItem = new ViewItem(rootItem);
			rootItem.addChangeListener(this);
		}
		this.addMouseListener(this);
		this.addMouseMotionListener(this);

		this.selectedItems = new HashSet<ViewItem>();

		this.items = new HashSet<ViewItem>();
		this.addItem(rootItem, false, false);
		this.newItem = null;

	}

	private static class Point {
		int x;
		int y;
		private static Point instance;

		private Point() {
			// singleton
		}

		static Point getInstance() {
			if (instance == null) {
				instance = new Point();
			}
			return instance;
		}
	}

	public void setParentPerformed(final Item item, final Item oldParent, final Item newParent) {
		if (newParent != null) {
			if (!(item instanceof ViewItem)) {
				if (newParent != null) {
					addItem(item, false, false);
				} else {
					final ViewItem viewItem = (ViewItem) ViewItem.item2ItemViewMap.get(item);
					this.items.remove(viewItem);
				}
			}
		}
	}
	
	public void setObjectNameChanged(final Item item, final String oldName, final String newName) {
		// ViewItem viewItem;
		// if (item instanceof ViewItem) {
		// viewItem = (ViewItem)item;
		// } else {
		// viewItem = (ViewItem)ViewItem.item2ItemViewMap.get(item);
		// }
		// viewItem.getName();
		this.repaint();
		this.revalidate();
	}

	private void initItems() {
		this.regularFont = this.getFont();
		this.boldFont = new Font(this.regularFont.getName(), Font.BOLD, this.regularFont.getSize());

		this.fontMetrics = this.getFontMetrics(this.regularFont);
		if (this.fontXOffset == -1) this.fontXOffset = this.fontMetrics.stringWidth("XX");
		if (this.items != null) {
			for (final ViewItem item : this.items) {
				item.setWidth(this.fontMetrics.stringWidth(item.getName()) + 2 * this.fontXOffset);
				item.setHeight((int) (1.5 * this.fontMetrics.getHeight()));
			}
		}
		// init misc graphical features

		if (this.boldStroke == null) {
			this.boldStroke = new BasicStroke(2.0f);
		}

		if (this.regularStroke == null) {
			this.regularStroke = new BasicStroke(1.0f);
		}

		this.arrange();
		this.newItem = null;
	}

	@Override
	public void paint(final Graphics g) {
		if (this.firstPaint) {
			this.initItems();
			this.firstPaint = false;
		}

		super.paint(g);
		final Graphics2D g2d = (Graphics2D) g;
		// Determine if antialiasing is enabled
		final RenderingHints rhints = g2d.getRenderingHints();
		boolean antialiasOn = rhints.containsValue(RenderingHints.VALUE_ANTIALIAS_ON);
		if (!this.mouseDragging && !this.mouseMoving && !antialiasOn) {
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			antialiasOn = true;
		}

		if (this.items != null) {
			for (final ViewItem item : this.items) {
				this.paintItem(g2d, item);
			}
		}

		/* no selected items, mouse dragging -- draw selection region */
		if (this.mouseDragging && this.selectedItem == null && this.selectedItems.isEmpty()) {
			int x1 = this.x;
			int y1 = this.y;
			int w = this.dx;
			int h = this.dy;
			if (w < 0) {
				x1 += w;
				w *= -1;
			}
			if (h < 0) {
				y1 += h;
				h *= -1;
			}
			g2d.drawRect(x1, y1, w, h);
		}

		// Disable antialiasing
		if (antialiasOn) {
			g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		}
	}

	private void paintItem(final Graphics2D g2d, final ViewItem item) {
		if (item.isService()) {
			return;
		}

		final boolean selected = (this.selectedItems.contains(item))
				|| ((this.selectedItem != null && this.selectedItem.equals(item)));

		if (item.children != null) {
			for (final Iterator<Item> it = item.children.iterator(); it.hasNext();) {
				final ViewItem item2 = (ViewItem) it.next();
				this.paintItem(g2d, item2);
				g2d.setColor(EDGE_COLOR);
				if (item.equals(this.firstSelectedLineItem) && item2.equals(this.secondSelectedLineItem)) {
					g2d.setColor(SELECTED_EDGE_COLOR);
					g2d.setStroke(this.boldStroke);
				} else
					g2d.setStroke(this.regularStroke);
				this.drawLineFromItemToItem(g2d, item, item2);
			}
		}

		if (this.linkItem != null) {
			g2d.setColor(EDGE_COLOR);
			this.drawLineFromItem(g2d, this.linkItem, this.x, this.y);

		}

		g2d.setColor(ITEM_BG_COLOR);
		g2d.fillRect(item.x, item.y, item.getWidth(), item.getHeight());

		if (selected) {
			g2d.setColor(SELECTED_EDGE_COLOR);
			g2d.setFont(this.boldFont);
			g2d.setStroke(this.boldStroke);
		} else {
			g2d.setFont(this.regularFont);
			g2d.setStroke(this.regularStroke);
			g2d.setColor(EDGE_COLOR);
		}

		g2d.drawRect(item.x, item.y, item.getWidth(), item.getHeight());

		g2d.setColor(TEXT_COLOR);
		g2d.drawString(item.getName(), this.fontXOffset + item.x, item.y + g2d.getFontMetrics().getHeight());
	}

	private void drawLineFromItemToItem(final Graphics2D g2d, final ViewItem item, final ViewItem otherItem) {
		final Point lineFromItem = this.getLineFromItem(otherItem, item.x + item.getWidth() / 2, item.y + item.getHeight()
				/ 2);
		this.drawLineFromItem(g2d, item, lineFromItem.x, lineFromItem.y);
	}

	private void drawLineFromItem(final Graphics2D g2d, final ViewItem item, final int toX, final int toY) {
		if (!(toX > item.x - ARROW_LENGTH / 2 && toX < item.x + item.getWidth() + ARROW_LENGTH / 2
				&& toY > item.y - ARROW_LENGTH / 2 && toY < item.y + item.getHeight() + ARROW_LENGTH / 2)) {
			final Point lineFromItem = this.getLineFromItem(item, toX, toY);
			this.drawArrow(g2d, lineFromItem.x, lineFromItem.y, toX, toY);
			g2d.drawLine(lineFromItem.x, lineFromItem.y, toX, toY);
		}
	}

	/**
	 * &copy by Stratonnikov Alexey aka saa
	 */
	private Point getLineFromItem(final ViewItem item, int toX, int toY) {
		int x1 = item.x + item.getWidth() / 2;
		int y1 = item.y + item.getHeight() / 2;
		toX -= x1;
		toY -= y1;
		final double ratio = Math.abs(item.getWidth() * toY) > Math.abs(item.getHeight() * toX) ? item.getHeight()
				/ Math.abs(2.0 * toY) : item.getWidth() / Math.abs(2.0 * toX);
		toX = (int) Math.round(ratio * toX);
		toY = (int) Math.round(ratio * toY);

		x1 += toX;
		y1 += toY;

		final Point point = Point.getInstance();
		point.x = x1;
		point.y = y1;
		return point;
	}

	public synchronized void arrange() {
		if (this.items != null) {
			final List<ViewItem> deepItems = new LinkedList<ViewItem>();
			for (final ViewItem item : this.items) {
				if (item.getChildren() == null || item.getChildren().isEmpty()) {
					deepItems.add(item);
				}
			}

			int w = 10;

			final int componentWidth = this.getWidth();
			int componentHeight = this.getHeight();
			{
				for (final ViewItem viewItem : deepItems) {
					int width = viewItem.getHierarchicalWidth();
					int count = viewItem.getHierarchicalCount();
					w = (componentWidth - width) / count;
					for (final ViewItem viewItem2 : deepItems) {
						width = viewItem2.getHierarchicalWidth();
						count = viewItem2.getHierarchicalCount();

						if (count * w + width > componentWidth) {
							w = (componentWidth - width) / count;
						}
					}
				}
			}

			w = (w > EDGE_THICK) ? w : EDGE_THICK;
			this.rootServiceItem.print(0);
			this.rootServiceItem.separateChildrenY();
			this.rootServiceItem.separateChildrenX(w);

			final int minX = this.rootServiceItem.getMinX();
			final int minY = this.rootServiceItem.getMinY();
			final int maxY = this.rootServiceItem.getMaxY();
			this.rootServiceItem.move(-minX + w / 2, (componentHeight - maxY - minY) / 2);

			this.resize();
			this.repaint();

		}
	}

	/**
	 * &copy by Vitaliy Shiryaev
	 *
	 * @param g2d
	 * @param startX
	 * @param startY
	 * @param endX
	 * @param endY
	 */
	private void drawArrow(final Graphics2D g2d, final double startX, final double startY, final double endX, final double endY) {

		final double len = Math.sqrt((endX - startX) * (endX - startX) + (endY - startY) * (endY - startY));
		final double cos = (endX - startX) / len;
		final double sin = (endY - startY) / len;

		final double arrowPos = 0.0;

		final double x0 = endX - len * arrowPos * cos;
		final double y0 = endY - len * arrowPos * sin;
		final double x3 = endX - (ARROW_LENGTH + len * arrowPos) * cos;
		final double y3 = endY - (ARROW_LENGTH + len * arrowPos) * sin;
		final int[] xArray = new int[] { (int) (x3 + ARROW_WEIGHT * sin + 0.5), (int) (x0 + 0.5),
				(int) (x3 - ARROW_WEIGHT * sin + 0.5)};
		final int[] yArray = new int[] { (int) (y3 - ARROW_WEIGHT * cos + 0.5), (int) (y0 + 0.5),
				(int) (y3 + ARROW_WEIGHT * cos + 0.5)};
		g2d.fillPolygon(xArray, yArray, 3);
		// g2d.draw(new Polygon(xArray, yArray, 2));
	}

	private ViewItem getItem(final int pointX, final int pointY) {
		ViewItem item = null;
		if (this.items != null) {
			for (final ViewItem item2 : this.items) {
				if (item2.isService()) {
					continue;
				}
				if (item2.x - SELECT_AREA <= pointX && pointX <= item2.x + item2.getWidth() + SELECT_AREA
						&& item2.y - SELECT_AREA <= pointY && pointY <= item2.y + item2.getHeight() + SELECT_AREA) {
					item = item2;
					break;
				}
			}
		}
		return item;
	}

	private void selectItemsInRect(int x1, int y1, int x2, int y2) {
		x2 += x1;
		y2 += y1;
		if (x2 < x1) {
			int t = x1;
			x1 = x2;
			x2 = t;
		}
		if (y2 < y1) {
			int t = y1;
			y1 = y2;
			y2 = t;
		}
		this.selectedItems.clear();
		for (final ViewItem item : this.items) {
			if (item.isService()) {
				continue;
			}
			if ( // top left
			(x1 < item.x && item.x < x2 && y1 < item.y && item.y < y2)
					||
					// top right
					(x1 < item.x + item.getWidth() && item.x + item.getWidth() < x2 && y1 < item.y && item.y < y2)
					||
					// bottom left
					(x1 < item.x && item.x < x2 && y1 < item.y + item.getHeight() && item.y + item.getHeight() < y2)
					||
					// botton right
					(x1 < item.x + item.getWidth() && item.x + item.getWidth() < x2 && y1 < item.y + item.getHeight() && item.y
							+ item.getHeight() < y2)
					// crossing one side by one of side of selection box
					|| (item.x < x1 && x1 < item.x + item.getWidth() && y1 < item.y && (y2 > item.y + item.getHeight() || y2 > item.y))
					|| (item.y < y1 && y1 < item.y + item.getHeight() && x1 < item.x && (x2 > item.x + item.getWidth() || x2 > item.x))
					|| (item.x < x1 && x1 < item.x + item.getWidth() && item.y < y1 && y1 < item.y + item.getHeight())) {
				this.selectedItems.add(item);
			}

		}
		if (!this.selectedItems.isEmpty()) {
			this.fireSelectionChanged();

			this.selectedItem = this.selectedItems.iterator().next();
		}
	}

	private void resize() {
		int width = this.getWidth();
		int height = this.getHeight();
		if (this.items != null) {
			for (final ViewItem item : this.items) {
				if (item.x + item.getWidth() > width) {
					width = item.x + item.getWidth();
				}
				if (item.y + item.getHeight() > height) {
					height = item.y + item.getHeight();
				}
			}
		}
		if (width != this.getWidth() || height != this.getHeight()) {
			final Dimension dimension = new Dimension(width, height);
			super.setMinimumSize(dimension);
			super.setPreferredSize(dimension);
			super.revalidate();
		}
	}

	private void detectLine(final int x1, final int y1) {
		if (this.items != null) {
			this.firstSelectedLineItem = null;
			this.secondSelectedLineItem = null;
			for (final Iterator<ViewItem> it = this.items.iterator(); this.firstSelectedLineItem == null && it.hasNext();) {
				final ViewItem item = it.next();
				if (item.children != null) {
					for (final Iterator<Item> iter = item.children.iterator(); iter.hasNext();) {
						final ViewItem otherItem = (ViewItem) iter.next();
						Point lineFromItem = this.getLineFromItem(otherItem, item.x + item.getWidth() / 2, item.y + item.getHeight() / 2);
						final int x2 = lineFromItem.x;
						final int y2 = lineFromItem.y;
						lineFromItem = this.getLineFromItem(item, x2, y2);
						final int x3 = lineFromItem.x;
						final int y3 = lineFromItem.y;
						final double a = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
						final double b = Math.sqrt((x1 - x3) * (x1 - x3) + (y1 - y3) * (y1 - y3));
						final double c = Math.sqrt((x2 - x3) * (x2 - x3) + (y2 - y3) * (y2 - y3));

						int h = Integer.MAX_VALUE;
						if (a < c && b < c) {
							final double c2 = (c * c + b * b - a * a) / (2.0 * c);
							h = (int) (Math.sqrt(b * b - c2 * c2) + 0.5);
						}

						if (h <= SELECT_AREA) {
							this.firstSelectedLineItem = item;
							this.secondSelectedLineItem = otherItem;
							break;
						}

					}
				}
			}
		}
	}

	public synchronized void deleteSelectedItems() {
		if (this.selectedItem != null) {
			/* TODO: perfomance problem */
			final List<ViewItem> list = new ArrayList<ViewItem>(this.selectedItems);
			final List<ViewItem> moveToRoot = new LinkedList<ViewItem>();
			for (final ViewItem item : list) {
				final List<Item> children = item.getChildren();
				for (final Iterator<Item> iter = children.iterator(); iter.hasNext();) {
					final ViewItem childItem = (ViewItem) iter.next();
					if (!list.contains(childItem)) {
						moveToRoot.add(childItem);
					}
				}
			}

			for (final ViewItem item2 : moveToRoot) {
				list.remove(item2);
				/* TODO what the problem ? why not adding child when setParent performed ? */
				this.rootServiceItem.addChild(item2);
				item2.setParent(this.rootServiceItem);
			}

			for (final ViewItem item : list) {
				this.items.remove(item);
				item.setParent(null);
//				for (int i = 0; i < this.addDeleteItems.length; i++) {
//					this.addDeleteItems[i].deleteItem(item.getSourceItem());
//				}
			}


			this.selectedItem = null;
			this.newItem = null;
			this.selectedItems.clear();
			this.repaint();
		} else {
			if (this.firstSelectedLineItem != null && this.secondSelectedLineItem != null) {
				/* TODO what the problem ? why not adding child when setParent performed ? */
				this.rootServiceItem.addChild(this.secondSelectedLineItem);
				this.secondSelectedLineItem.setParent(this.rootServiceItem);
				this.repaint();
			}
		}
	}

	public void addItem(final Item item) {
		this.addItem(item, true, false);
	}

	private void addItem(final Item item, final boolean toRoot, final boolean toParent) {
		if (item instanceof ViewItem) {
			this.newItem = (ViewItem) item;
		} else {
			this.newItem = (ViewItem) ViewItem.item2ItemViewMap.get(item);
			if (this.newItem == null) {
				this.newItem = new ViewItem(item);
			}
		}

		if (this.fontMetrics != null) {
			this.newItem.setWidth(this.fontMetrics.stringWidth(this.newItem.getName()) + 2 * this.fontXOffset);
			this.newItem.setHeight((int) (1.5 * this.fontMetrics.getHeight()));
		}

		ViewItem parent2 = (ViewItem) this.newItem.getParent();
		final Item parent3 = item.getParent();
		final ViewItem item2 = this.newItem;

		final Item sourceParentItem2 = parent2 == null ? null : parent2.getSourceItem();
		if (parent3 != null && (sourceParentItem2 == null || !sourceParentItem2.equals(parent3))) {
			this.addItem(parent3, toRoot, true);
			this.newItem = item2;
			parent2 = (ViewItem) ViewItem.item2ItemViewMap.get(parent3);
			parent2.addChild(this.newItem);
			this.newItem.setParent(parent2);
		}

		this.items.add(this.newItem);

		if (toRoot) {
			this.rootServiceItem.addChild(this.newItem);
		}

		// for (int i = 0; i < this.addDeleteItems.length; i++) {
		// this.addDeleteItems[i].addItem(this.newItem.getSourceItem());
		// }

		if (toParent) {
			return;
		}

		final List<Item> children = item.getChildren();
		if (children != null && !children.isEmpty()) {
			for (final Item item3 : children) {
				this.addItem(item3, toRoot, toParent);
			}
		}
	}

	private void fireSelectionChanged() {
		if (this.selectionListeners.length > 0) {
			final Collection<Item> selections = this.getSelectedItems();
			for (int i = 0; i < this.selectionListeners.length; i++) {
				this.selectionListeners[i].selectedItems(selections);
			}
		}
	}

	public void mouseClicked(final MouseEvent e) {
		this.mouseDragging = false;
		if (SwingUtilities.isLeftMouseButton(e)) {
			/* just moving put new item */
			if (this.newItem != null) {
				this.newItem = null;
			} else {
				final int mouseX = e.getX();
				final int mouseY = e.getY();
				this.selectedItem = this.getItem(mouseX, mouseY);

				if (this.selectedItem != null) {
					//System.out.println(this.selectedItem.getName() + "::" + this.selectedItem.getWidth() + "\t minY:"
					//		+ this.selectedItem.getMinY() + ", maxY:" + this.selectedItem.getMaxY());
					if (!e.isControlDown()) {
						this.selectedItems.clear();
					}
					this.dx = this.selectedItem.x - mouseX;
					this.dy = this.selectedItem.y - mouseY;

					if (this.selectedItems.contains(this.selectedItem)) {
						this.selectedItems.remove(this.selectedItem);
						if (!this.selectedItems.isEmpty()) {
							this.selectedItem = this.selectedItems.iterator().next();
						}
					} else {
						this.selectedItems.add(this.selectedItem);
					}

					this.fireSelectionChanged();
				} else {
					this.selectedItems.clear();
					this.fireSelectionChanged();
					/* this is candidate for line */
					this.detectLine(mouseX, mouseY);
				}

			}
			this.repaint();
		} else {
			this.selectedItem = null;
			this.selectedItems.clear();
			this.fireSelectionChanged();
		}
	}

	public void mousePressed(final MouseEvent e) {
		final int mouseX = e.getX();
		final int mouseY = e.getY();
		this.preSelectedItem = this.getItem(mouseX, mouseY);
	}

	public void mouseDragged(final MouseEvent e) {
		final int mouseX = e.getX();
		final int mouseY = e.getY();

		if (this.selectedItem != null) {
			if (SwingUtilities.isRightMouseButton(e)
					&& this.preSelectedItem != null
					&& (this.preSelectedItem.equals(this.selectedItem) || this.selectedItems.contains(this.preSelectedItem))) {
				if (!this.mouseDragging) {
					this.dx = this.selectedItem.x - mouseX;
					this.dy = this.selectedItem.y - mouseY;
				}

				int sDx = (mouseX + this.dx) - this.selectedItem.x;
				int sDy = (mouseY + this.dy) - this.selectedItem.y;
				this.selectedItem.x += sDx;
				this.selectedItem.y += sDy;
				if (!this.selectedItems.isEmpty()) {
					for (final ViewItem item : this.selectedItems) {
						if (item.equals(this.selectedItem)) {
							continue;
						}
						item.x += sDx;
						item.y += sDy;
					}
				}
				this.mouseDragging = true;
			} else {
				if (SwingUtilities.isLeftMouseButton(e) && this.preSelectedItem != null && this.preSelectedItem.equals(this.selectedItem)) {
					if (this.selectedItem.canHaveChildren()) {
						this.linkItem = this.selectedItem;
						this.x = mouseX;
						this.y = mouseY;
						this.dx = this.linkItem.x - mouseX;
						this.dy = this.linkItem.y - mouseY;
					}
				} else {
					this.selectedItem = null;
					this.selectedItems.clear();
					this.mouseDragging = false;
					this.mouseMoving = false;
				}
			}

			this.repaint();

		} else if (SwingUtilities.isLeftMouseButton(e)) {
			if (!this.mouseDragging) {
				this.x = mouseX;
				this.y = mouseY;
			}
			this.dx = mouseX - this.x;
			this.dy = mouseY - this.y;
			this.mouseDragging = true;
			this.repaint();
		}
	}

	public void mouseEntered(final MouseEvent e) {
		// System.out.println("mouseEntered");
	}

	public void mouseExited(final MouseEvent e) {
		// System.out.println("mouseExited");
	}

	public void mouseMoved(final MouseEvent e) {
		// System.out.println("mouseMoved");
		this.mouseMoving = true;
		if (this.newItem != null) {
			this.newItem.x = e.getX() - this.newItem.getWidth() / 2;
			this.newItem.y = e.getY() - this.newItem.getHeight() / 2;
			this.repaint();
		} else if (this.linkItem != null) {
			this.x = e.getX();
			this.y = e.getY();
			this.repaint();
		}
	}

	public void mouseReleased(final MouseEvent e) {
		final int mouseX = e.getX();
		final int mouseY = e.getY();
		if (this.mouseDragging && this.selectedItem == null && this.selectedItems.isEmpty()) {
			this.selectItemsInRect(this.x, this.y, this.dx, this.dy);
		}
		//

		if (this.linkItem != null) {
			this.selectedItem = this.getItem(mouseX, mouseY);
			if (this.selectedItem != null) {
				try {
					this.linkItem.addChild(this.selectedItem);
				} catch (UnsupportedOperationException uoe) {
					// uoe.printStackTrace();
					JOptionPane.showMessageDialog(this, uoe.getMessage(), "Error", JOptionPane.OK_OPTION);
				}
			}
			this.linkItem = null;
			this.selectedItems.clear();
			this.firstSelectedLineItem = null;
			this.secondSelectedLineItem = null;

			this.fireSelectionChanged();
		}

		this.mouseDragging = false;
		this.mouseMoving = false;
		this.resize();
		this.repaint();
	}

	public void addSelectionListener(final SelectionListener selectionListener) {
		final SelectionListener[] selectionListeners1 = new SelectionListener[this.selectionListeners.length + 1];
		System.arraycopy(this.selectionListeners, 0, selectionListeners1, 1, this.selectionListeners.length);
		selectionListeners1[0] = selectionListener;
		this.selectionListeners = selectionListeners1;
	}

	public void removeSelectionListener(final SelectionListener selectionListener) {
		int index = -1;
		for (int i = 0; i < this.selectionListeners.length; i++) {
			if (this.selectionListeners[i].equals(selectionListener)) {
				index = i;
				break;
			}
		}
		if (index >= -1) {
			final SelectionListener[] selectionListeners1 = new SelectionListener[this.selectionListeners.length - 1];
			System.arraycopy(this.selectionListeners, 0, selectionListeners1, 0, index);
			System.arraycopy(this.selectionListeners, index + 1, selectionListeners1, index, selectionListeners1.length - index);
			this.selectionListeners = selectionListeners1;
		}
	}

// public void addAddDeleteItemListener(AddDeleteItems addDeleteItems) {
//		AddDeleteItems[] addDeleteItems1 = new AddDeleteItems[this.addDeleteItems.length + 1];
//		System.arraycopy(this.addDeleteItems, 0, addDeleteItems1, 1, this.addDeleteItems.length);
//		addDeleteItems1[0] = addDeleteItems;
//		this.addDeleteItems = addDeleteItems1;
//	}
//
//	public void removeAddDeleteItemListener(AddDeleteItems addDeleteItems) {
//		int index = -1;
//		for (int i = 0; i < this.addDeleteItems.length; i++) {
//			if (this.addDeleteItems[i].equals(addDeleteItems)) {
//				index = i;
//				break;
//			}
//		}
//		if (index >= -1) {
//			AddDeleteItems[] addDeleteItems2 = new AddDeleteItems[this.addDeleteItems.length - 1];
//			System.arraycopy(this.addDeleteItems, 0, addDeleteItems2, 0, index);
//			System.arraycopy(this.addDeleteItems, index + 1, addDeleteItems2, index, addDeleteItems2.length - index);
//			this.addDeleteItems = addDeleteItems2;
//		}
//	}

	public void selectedItems(Collection<Item> selectedItems1) {
		this.selectedItems.clear();
		this.firstSelectedLineItem = null;
		this.secondSelectedLineItem = null;
		for (final Item item : selectedItems1) {
			if (item.isService()) {
				continue;
			}
			ViewItem viewItem;
			if (item instanceof ViewItem) {
				viewItem = (ViewItem) item;
			}
			else {
				viewItem = (ViewItem) ViewItem.item2ItemViewMap.get(item);
			}

			this.selectedItems.add(viewItem);
		}
		if (!this.selectedItems.isEmpty()) this.selectedItem = this.selectedItems.iterator().next();
		this.repaint();
	}

	public Collection<Item> getSelectedItems() {
		Collection<Item> selections;
		if (this.selectedItems != null && !this.selectedItems.isEmpty()) {
			selections = new ArrayList<Item>(this.selectedItems.size());
			for (ViewItem element : this.selectedItems) {
				selections.add(element.getSourceItem());
			}

		} else {
			if (this.selectedItem != null) {
				selections = Collections.singletonList(this.selectedItem.getSourceItem());
			}
			else {
				selections = Collections.emptyList();
			}
		}
		return selections;
	}
}
