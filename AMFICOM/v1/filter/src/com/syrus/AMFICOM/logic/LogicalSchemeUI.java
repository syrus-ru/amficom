/*
 * $Id: LogicalSchemeUI.java,v 1.3 2005/02/28 16:06:46 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
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
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

/**
 * @version $Revision: 1.3 $, $Date: 2005/02/28 16:06:46 $
 * @author $Author: bob $
 * @module filter_v1
 */
public class LogicalSchemeUI extends JComponent implements MouseListener, MouseMotionListener {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 3258408430686581815L;

	static final Color			EDGE_COLOR			= Color.GRAY;
	static final Color			SELECTED_EDGE_COLOR	= Color.BLACK;
	static final Color			ITEM_BG_COLOR		= new Color(220, 220, 220);
	static final Color			TEXT_COLOR			= Color.BLACK;

	Collection					items;

	static final int			SELECT_AREA			= 10;
	static final int EDGE_THICK = 20;

	static final double			ARROW_LENGTH		= 15.0;
	static final double			ARROW_WEIGHT		= 4.0;

	private Font				regularFont;
	private Font				boldFont;

	private Stroke				regularStroke;
	private Stroke				boldStroke;

	private ViewItem			newItem;
	private ViewItem			selectedItem;
	private ViewItem			linkItem;

	private Collection			selectedItems;

	private ViewItem			firstSelectedLineItem;
	private ViewItem			secondSelectedLineItem;

	private int					x;
	private int					y;

	private int					dx;
	private int					dy;

	private int					fontXOffset			= -1;

	private int					countInitItems		= 0;

	private boolean				mouseDragging		= false;
	private boolean				mouseMoving			= false;

	private Map					itemMap				= new HashMap();

	public LogicalSchemeUI(Collection items) {
		this.addMouseListener(this);
		this.addMouseMotionListener(this);

		this.selectedItems = new HashSet();

		Collection rootItems = new HashSet();
		// ear's feint to find root itemz
		for (Iterator it = items.iterator(); it.hasNext();) {
			Item item = (Item) it.next();
			boolean parent = true;

			for (Iterator iter = items.iterator(); iter.hasNext();) {
				Item item2 = (Item) iter.next();
				Collection children = item2.getChildren();
				if (children != null && children.contains(item)) {
					parent = false;
					break;
				}
			}
			if (parent)
				rootItems.add(item);

		}
		this.items = new LinkedList();
		this.addViewItems(rootItems);
	}

	private void addViewItems(Collection collection) {
		for (Iterator it = collection.iterator(); it.hasNext();) {
			Item item = (Item) it.next();
			ViewItem viewItem;
			if (item instanceof ViewItem) {
				viewItem = (ViewItem) item;
			} else {
				viewItem = (ViewItem) this.itemMap.get(item);
				if (viewItem == null) {
					viewItem = new ViewItem(item);
					this.itemMap.put(item, viewItem);
				}
			}
			// System.out.println("LogicalSchemeUI.addViewItems | add " +
			// viewItem.name);
			this.items.add(viewItem);
			if (viewItem.getChildren() != null)
				this.addViewItems(viewItem.getChildren());
		}

	}

	private static class Point {

		int						x;
		int						y;
		private static Point	instance;

		private Point() {
			// singleton
		}

		static Point getInstance() {
			if (instance == null)
				instance = new Point();
			return instance;
		}
	}

	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		// Determine if antialiasing is enabled
		RenderingHints rhints = g2d.getRenderingHints();
		boolean antialiasOn = rhints.containsValue(RenderingHints.VALUE_ANTIALIAS_ON);
		if (!this.mouseDragging && !this.mouseMoving && !antialiasOn) {
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			antialiasOn = true;
		}

		if (this.items != null) {
			for (Iterator it = this.items.iterator(); it.hasNext();) {
				ViewItem item = (ViewItem) it.next();
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

	private void paintItem(	Graphics2D g2d,
							ViewItem item) {

		if (item.width == -1 || item.height == -1) {
			FontMetrics fontMetrics = g2d.getFontMetrics();
			if (this.fontXOffset == -1)
				this.fontXOffset = fontMetrics.stringWidth("XX");
			item.width = fontMetrics.stringWidth(item.name) + 2 * this.fontXOffset;
			item.height = (int) (1.5 * fontMetrics.getHeight());
			this.countInitItems++;
			if (this.items != null && this.countInitItems == this.items.size())
				this.arrange();

			// init misc graphical features

			if (this.boldStroke == null)
				this.boldStroke = new BasicStroke(2.0f);

			if (this.regularStroke == null)
				this.regularStroke = new BasicStroke(1.0f);

			if (this.regularFont == null)
				this.regularFont = g2d.getFont();

			if (this.boldFont == null)
				this.boldFont = new Font(this.regularFont.getName(), Font.BOLD, this.regularFont.getSize());

		}

		boolean selected = (this.selectedItems.contains(item))
				|| ((this.selectedItem != null && this.selectedItem.equals(item)));

		if (item.children != null) {
			for (Iterator it = item.children.iterator(); it.hasNext();) {
				ViewItem item2 = (ViewItem) it.next();
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
		g2d.fillRect(item.x, item.y, item.width, item.height);

		if (selected) {
			g2d.setColor(SELECTED_EDGE_COLOR);
			g2d.setFont(this.boldFont);
			g2d.setStroke(this.boldStroke);
		} else {
			g2d.setFont(this.regularFont);
			g2d.setStroke(this.regularStroke);
			g2d.setColor(EDGE_COLOR);
		}

		g2d.drawRect(item.x, item.y, item.width, item.height);

		g2d.setColor(TEXT_COLOR);
		g2d.drawString(item.name, this.fontXOffset + item.x, item.y + g2d.getFontMetrics().getHeight());
	}

	private void drawLineFromItemToItem(Graphics2D g2d,
										ViewItem item,
										ViewItem otherItem) {
		Point lineFromItem = this.getLineFromItem(otherItem, item.x + item.width / 2, item.y + item.height / 2);
		this.drawLineFromItem(g2d, item, lineFromItem.x, lineFromItem.y);
	}

	private void drawLineFromItem(	Graphics2D g2d,
									ViewItem item,
									int toX,
									int toY) {
		if (!(toX > item.x - ARROW_LENGTH / 2 && toX < item.x + item.width + ARROW_LENGTH / 2
				&& toY > item.y - ARROW_LENGTH / 2 && toY < item.y + item.height + ARROW_LENGTH / 2)) {
			Point lineFromItem = this.getLineFromItem(item, toX, toY);
			this.drawArrow(g2d, lineFromItem.x, lineFromItem.y, toX, toY);
			g2d.drawLine(lineFromItem.x, lineFromItem.y, toX, toY);
		}
	}

	/**
	 * &copy by Stratonnikov Alexey aka saa
	 */
	private Point getLineFromItem(	ViewItem item,
									int toX,
									int toY) {
		int x1 = item.x + item.width / 2;
		int y1 = item.y + item.height / 2;
		toX -= x1;
		toY -= y1;
		double ratio = Math.abs(item.width * toY) > Math.abs(item.height * toX) ? item.height / Math.abs(2.0 * toY)
				: item.width / Math.abs(2.0 * toX);
		toX = (int) Math.round(ratio * toX);
		toY = (int) Math.round(ratio * toY);

		x1 += toX;
		y1 += toY;

		Point point = Point.getInstance();
		point.x = x1;
		point.y = y1;
		return point;
	}
	
	private void maxWidthCount(Collection rootItem, Map map, int count, int width) {
		for (Iterator it = rootItem.iterator(); it.hasNext();) {
			ViewItem item = (ViewItem) it.next();
//			System.out.println("count:" + count);
//			System.out.println("width:" + width);
//			System.out.println("item:" + item.name);
			if (item.children != null)
				maxWidthCount(item.children, map, count + 1, width + item.width);
			else {
				width += item.width;
				Integer iCount = new Integer(count + 1);
				Integer iWidth = (Integer) map.get(iCount);
				if (iWidth == null)
					map.put(iCount, new Integer(width));
				else {
					if (width > iWidth.intValue())
						map.put(iCount, new Integer(width));
				}
				return;
			}
			
		}
	}

	public void arrange() {
		if (this.items != null) {
			Map itemLevelMap = new HashMap();
			Map levelItemVertical = new HashMap();
			int deep = 0;
			Integer rootDeep = new Integer(deep);

			List rootItems = new LinkedList();
			for (Iterator it = this.items.iterator(); it.hasNext();) {
				ViewItem item = (ViewItem) it.next();
				if (item.maxParentCount == deep) {
					rootItems.add(item);
				} else {
					boolean isRoot = true;
					for (Iterator iter = this.items.iterator(); isRoot && iter.hasNext();) {
						ViewItem item2 = (ViewItem) iter.next();
						if (item2.children != null) {
							for (Iterator iterator = item2.children.iterator(); iterator.hasNext();) {
								ViewItem element = (ViewItem) iterator.next();
								if (element.equals(item)) {
									isRoot = false;
									break;
								}
							}
						}
					}
					if (isRoot)
						rootItems.add(item);
				}
			}

			int minX = Integer.MAX_VALUE;
			int minY = Integer.MAX_VALUE;
			int w = 10;
			int h = 10;

			this.startArrange(w, h, rootItems, itemLevelMap, levelItemVertical);

			/* calculate maximum level polulate */
			{
				int summHeight = 0;
				List maxVerticalLevelItems = null;
				for (Iterator it = levelItemVertical.keySet().iterator(); it.hasNext();) {
					Integer iDeep = (Integer) it.next();
					List levelItems = (List) levelItemVertical.get(iDeep);

					int tmpSummHeight = 0;
					for (Iterator iter = levelItems.iterator(); iter.hasNext();) {
						ViewItem viewItem = (ViewItem) iter.next();
						tmpSummHeight += viewItem.height;
					}
					if (tmpSummHeight > summHeight) {
						maxVerticalLevelItems = levelItems;
						summHeight = tmpSummHeight;
					}

				}

				if (maxVerticalLevelItems != null) {
					Collections.sort(maxVerticalLevelItems, new Comparator() {

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
					});
					ViewItem firstItem = null;
					ViewItem prevItem = null;
					for (Iterator it = maxVerticalLevelItems.iterator(); it.hasNext();) {
						ViewItem itemImpl = (ViewItem) it.next();
						if (firstItem == null)
							firstItem = itemImpl;
						if (prevItem == null)
							prevItem = itemImpl;
						else {
							prevItem = itemImpl;
						}
					}
					
					Map map = new HashMap();
					this.maxWidthCount(rootItems, map, 0, 0);
					int maxSummWidth = 0;
					int countAtMaxWidth = 0;
					for (Iterator it = map.keySet().iterator(); it.hasNext();) {
						Integer count = (Integer) it.next();
						Integer width = (Integer) map.get(count);
						if (width.intValue() > maxSummWidth) {
							maxSummWidth = width.intValue();
							countAtMaxWidth = count.intValue();
						}
					}

					if (firstItem != null && !firstItem.equals(prevItem)) {
						h = (this.getHeight()  - 2 * EDGE_THICK - summHeight) / maxVerticalLevelItems.size();
					}

					System.out.println(this.getWidth() + "x" + this.getHeight());
					w = (this.getWidth() - 2 * EDGE_THICK - maxSummWidth) / countAtMaxWidth ;
					System.out.println("maxWidth:" + maxSummWidth);
					System.out.println("w:" + w);
					System.out.println("countAtMaxWidth:" + countAtMaxWidth);

					w = (w > EDGE_THICK) ? w : EDGE_THICK;
					h = (h > EDGE_THICK) ? h : EDGE_THICK;

					this.startArrange(w, h, rootItems, itemLevelMap, levelItemVertical);

				}

			}

			for (Iterator it = this.items.iterator(); it.hasNext();) {
				ViewItem item = (ViewItem) it.next();
				if (minX > item.x)
					minX = item.x;
				if (minY > item.y)
					minY = item.y;
			}
			// minX -= (minX < 0 ? w / 2 : 0);
			// minY -= (minY < 0 ? h / 2 : 0);
			for (Iterator it = this.items.iterator(); it.hasNext();) {
				ViewItem item = (ViewItem) it.next();
				item.x += -minX + w/2;
				item.y += -minY + h/2;
			}

			this.resize();
			this.repaint();

		}
	}

	private void startArrange(	int w,
								int h,
								List rootItems,
								Map itemLevelMap,
								Map levelItemVertical) {
		int i = 0;
		int deep = 0;
		Integer rootDeep = new Integer(deep);
		++deep;
		for (Iterator iter = rootItems.iterator(); iter.hasNext(); i++) {
			ViewItem rootItem = (ViewItem) iter.next();
			levelItemVertical.clear();
			itemLevelMap.clear();

			levelItemVertical.put(rootDeep, rootItems);

			int count = rootItems.size();
			itemLevelMap.put(rootDeep, new Integer(count));

			rootItem.y = -(i - count / 2) * h;
			this.arrangeXY(w, h, rootItem, itemLevelMap, levelItemVertical, deep);
		}

	}

	private void arrangeXY(	int w,
							int h,
							ViewItem itemImpl,
							Map itemLevelMap,
							Map levelItemVertical,
							int deep) {
		Integer iDeep = new Integer(deep);

		List verticalItems = (List) levelItemVertical.get(iDeep);
		if (verticalItems == null) {
			verticalItems = new LinkedList();
			levelItemVertical.put(iDeep, verticalItems);
		}
		verticalItems.add(itemImpl);

		if (itemImpl.children != null) {
			int i = 0;
			int count = itemImpl.children.size();
			Integer deepCount = (Integer) itemLevelMap.get(iDeep);
			if (deepCount == null)
				deepCount = new Integer(count);
			else
				deepCount = new Integer(deepCount.intValue() + count);
			itemLevelMap.put(iDeep, deepCount);

			for (Iterator iterator = itemImpl.children.iterator(); iterator.hasNext(); i++) {
				ViewItem itemImpl2 = (ViewItem) iterator.next();
				itemImpl2.y = itemImpl.y + ((count % 2 == 0) ? ((1 + i - count / 2) * h - h / 2) : (i - count / 2) * h);
				itemImpl2.x = itemImpl.x - w - itemImpl2.width;
				this.arrangeXY(w, h, itemImpl2, itemLevelMap, levelItemVertical, deep + 1);
			}
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
	private void drawArrow(	Graphics2D g2d,
							double startX,
							double startY,
							double endX,
							double endY) {

		double len = Math.sqrt((endX - startX) * (endX - startX) + (endY - startY) * (endY - startY));
		double cos = (endX - startX) / len;
		double sin = (endY - startY) / len;

		double arrowPos = 0.0;

		double x0 = endX - len * arrowPos * cos;
		double y0 = endY - len * arrowPos * sin;
		double x3 = endX - (ARROW_LENGTH + len * arrowPos) * cos;
		double y3 = endY - (ARROW_LENGTH + len * arrowPos) * sin;
		int[] xArray = new int[] { (int) (x3 + ARROW_WEIGHT * sin + 0.5), (int) (x0 + 0.5),
				(int) (x3 - ARROW_WEIGHT * sin + 0.5)};
		int[] yArray = new int[] { (int) (y3 - ARROW_WEIGHT * cos + 0.5), (int) (y0 + 0.5),
				(int) (y3 + ARROW_WEIGHT * cos + 0.5)};
		g2d.fillPolygon(xArray, yArray, 3);
		// g2d.draw(new Polygon(xArray, yArray, 2));
	}

	private ViewItem getItem(	int pointX,
								int pointY) {
		ViewItem item = null;
		if (this.items != null) {
			for (Iterator it = this.items.iterator(); it.hasNext();) {
				ViewItem item2 = (ViewItem) it.next();
				if (item2.x - SELECT_AREA <= pointX && pointX <= item2.x + item2.width + SELECT_AREA
						&& item2.y - SELECT_AREA <= pointY && pointY <= item2.y + item2.height + SELECT_AREA) {
					item = item2;
					break;
				}
			}
		}
		return item;
	}

	private void selectItemsInRect(	int x1,
									int y1,
									int x2,
									int y2) {
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
		for (Iterator it = this.items.iterator(); it.hasNext();) {
			ViewItem item = (ViewItem) it.next();
			if ( // top left
			(x1 < item.x && item.x < x2 && y1 < item.y && item.y < y2)
					||
					// top right
					(x1 < item.x + item.width && item.x + item.width < x2 && y1 < item.y && item.y < y2)
					||
					// bottom left
					(x1 < item.x && item.x < x2 && y1 < item.y + item.height && item.y + item.height < y2)
					||
					// botton right
					(x1 < item.x + item.width && item.x + item.width < x2 && y1 < item.y + item.height && item.y
							+ item.height < y2)
					// crossing one side by one of side of selection box
					|| (item.x < x1 && x1 < item.x + item.width && y1 < item.y && (y2 > item.y + item.height || y2 > item.y))
					|| (item.y < y1 && y1 < item.y + item.height && x1 < item.x && (x2 > item.x + item.width || x2 > item.x))
					|| (item.x < x1 && x1 < item.x + item.width && item.y < y1 && y1 < item.y + item.height)) {
				this.selectedItems.add(item);
			}

		}
		if (!this.selectedItems.isEmpty())
			this.selectedItem = (ViewItem) this.selectedItems.iterator().next();
	}

	private void resize() {
		int width = this.getWidth();
		int height = this.getHeight();
		if (this.items != null) {
			for (Iterator it = this.items.iterator(); it.hasNext();) {
				ViewItem item = (ViewItem) it.next();
				if (item.x + item.width > width)
					width = item.x + item.width;
				if (item.y + item.height > height)
					height = item.y + item.height;
			}
		}
		if (width != this.getWidth() || height != this.getHeight()) {
			Dimension dimension = new Dimension(width, height);
			super.setMinimumSize(dimension);
			super.setPreferredSize(dimension);
			super.revalidate();
		}
	}

	private void detectLine(int x1,
							int y1) {
		if (this.items != null) {
			this.firstSelectedLineItem = null;
			this.secondSelectedLineItem = null;
			for (Iterator it = this.items.iterator(); this.firstSelectedLineItem == null && it.hasNext();) {
				ViewItem item = (ViewItem) it.next();
				if (item.children != null) {
					for (Iterator iter = item.children.iterator(); iter.hasNext();) {
						ViewItem otherItem = (ViewItem) iter.next();
						Point lineFromItem = this.getLineFromItem(otherItem, item.x + item.width / 2, item.y
								+ item.height / 2);
						int x2 = lineFromItem.x;
						int y2 = lineFromItem.y;
						lineFromItem = this.getLineFromItem(item, x2, y2);
						int x3 = lineFromItem.x;
						int y3 = lineFromItem.y;
						double a = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
						double b = Math.sqrt((x1 - x3) * (x1 - x3) + (y1 - y3) * (y1 - y3));
						double c = Math.sqrt((x2 - x3) * (x2 - x3) + (y2 - y3) * (y2 - y3));

						int h = Integer.MAX_VALUE;
						if (a < c && b < c) {
							double c2 = (c * c + b * b - a * a) / (2.0 * c);
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

	public void deleteSelectedItem() {
		if (this.selectedItem != null) {
			for (Iterator it = this.items.iterator(); it.hasNext();) {
				ViewItem item = (ViewItem) it.next();
				item.removeChild(this.selectedItem);
			}
			this.items.remove(this.selectedItem);
			this.selectedItem = null;
			this.repaint();
		} else {
			if (this.firstSelectedLineItem != null && this.secondSelectedLineItem != null) {
				this.firstSelectedLineItem.removeChild(this.secondSelectedLineItem);
				this.repaint();
			}
		}
	}

	public void addItem(Item item) {
		this.newItem = new ViewItem(item);
		this.items.add(this.newItem);
	}

	public void mouseClicked(MouseEvent e) {
		this.mouseDragging = false;
		if (SwingUtilities.isLeftMouseButton(e)) {
			/* just moving put new item */
			if (this.newItem != null) {
				this.newItem = null;
			} else {
				int mouseX = e.getX();
				int mouseY = e.getY();
				this.selectedItem = this.getItem(mouseX, mouseY);
				if (this.selectedItem != null) {
					System.out.println("::" + this.selectedItem.width);
					if (!e.isControlDown()) {
						this.selectedItems.clear();
					}
					this.dx = this.selectedItem.x - mouseX;
					this.dy = this.selectedItem.y - mouseY;

					if (this.selectedItems.contains(this.selectedItem)) {
						this.selectedItems.remove(this.selectedItem);
						this.selectedItem = (ViewItem) this.selectedItems.iterator().next();
					} else
						this.selectedItems.add(this.selectedItem);

					if (!e.isControlDown()) {
						this.firstSelectedLineItem = null;
						this.secondSelectedLineItem = null;
						if (this.linkItem != null) {
							if ((this.linkItem.maxChildrenCount != 0)
									&& (this.linkItem.children == null || this.linkItem.maxChildrenCount > this.linkItem.children
											.size())) {
								/* calculate output link for selectedItem */
								int outputCount = 0;
								for (Iterator it = this.items.iterator(); it.hasNext();) {
									ViewItem item = (ViewItem) it.next();
									if (item.children != null) {
										for (Iterator iter = item.children.iterator(); iter.hasNext();) {
											ViewItem item2 = (ViewItem) iter.next();
											if (item2.equals(this.selectedItem))
												outputCount++;
										}
									}
								}
								/*
								 * add selected item to linkItem if total output
								 * count less than its limit
								 */
								if (outputCount < this.selectedItem.maxParentCount)
									this.linkItem.addChild(this.selectedItem);
							}
							this.linkItem = null;
						}
					}
				} else {
					this.selectedItems.clear();
					/* this is candidate for line */
					this.detectLine(mouseX, mouseY);
				}

			}
			this.repaint();
		} else if (SwingUtilities.isRightMouseButton(e)) {
			this.selectedItems.clear();
			int mouseX = e.getX();
			int mouseY = e.getY();
			this.selectedItem = this.getItem(mouseX, mouseY);
			if (this.selectedItem != null) {
				this.linkItem = this.selectedItem;
				this.x = mouseX;
				this.y = mouseY;
				this.dx = this.linkItem.x - mouseX;
				this.dy = this.linkItem.y - mouseY;
			} else if (this.linkItem != null) {
				this.linkItem = null;
			}
		}
	}

	public void mousePressed(MouseEvent e) {
		// System.out.println("mousePressed");
		// this.mouseClicked(e);
	}

	public void mouseDragged(MouseEvent e) {
		int mouseX = e.getX();
		int mouseY = e.getY();

		if (this.selectedItem != null) {
			if (!this.mouseDragging) {
				this.dx = this.selectedItem.x - mouseX;
				this.dy = this.selectedItem.y - mouseY;
			}

			int sDx = (mouseX + this.dx) - this.selectedItem.x;
			int sDy = (mouseY + this.dy) - this.selectedItem.y;
			this.selectedItem.x += sDx;
			this.selectedItem.y += sDy;
			if (!this.selectedItems.isEmpty()) {
				for (Iterator it = this.selectedItems.iterator(); it.hasNext();) {
					ViewItem item = (ViewItem) it.next();
					if (item.equals(this.selectedItem))
						continue;
					item.x += sDx;
					item.y += sDy;
				}
			}
			this.mouseDragging = true;
			this.repaint();

		} else {
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

	public void mouseEntered(MouseEvent e) {
		// System.out.println("mouseEntered");
	}

	public void mouseExited(MouseEvent e) {
		// System.out.println("mouseExited");
	}

	public void mouseMoved(MouseEvent e) {
		// System.out.println("mouseMoved");
		this.mouseMoving = true;
		if (this.newItem != null) {
			this.newItem.x = e.getX() - this.newItem.width / 2;
			this.newItem.y = e.getY() - this.newItem.height / 2;
			this.repaint();
		} else if (this.linkItem != null) {
			this.x = e.getX();
			this.y = e.getY();
			this.repaint();
		}
	}

	public void mouseReleased(MouseEvent e) {
		// System.out.println("mouseReleased");
		// this.selectedItem = null;
		if (this.mouseDragging && this.selectedItem == null && this.selectedItems.isEmpty()) {
			this.selectItemsInRect(this.x, this.y, this.dx, this.dy);
		}
		this.mouseDragging = false;
		this.mouseMoving = false;
		this.resize();
		this.repaint();
	}
}
