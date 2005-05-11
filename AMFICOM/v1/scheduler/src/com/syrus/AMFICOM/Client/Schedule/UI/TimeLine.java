/*-
 * $Id: TimeLine.java,v 1.4 2005/05/11 15:34:12 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.JComponent;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.Resource.ResourceKeys;
import com.syrus.AMFICOM.Client.Schedule.UI.TestLine.TestTimeItem;

/**
 * @version $Revision: 1.4 $, $Date: 2005/05/11 15:34:12 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module scheduler_v1
 */
public abstract class TimeLine extends JComponent {

	int			minimalWidth;
	int			titleHeight;

	String		title;

	long		start;
	long		end;
	double		scale = 0.0;

	SortedSet	timeItems	= new TreeSet();

	public TimeLine() {
		
		this.createComponentListener();
		
		Font font2 = UIManager.getLookAndFeelDefaults().getFont("Button.font");
		FontMetrics fontMetrics = this.getFontMetrics(font2);
		
		this.minimalWidth = fontMetrics.charWidth('W');

		this.titleHeight = fontMetrics.getHeight();
	}

	private void createComponentListener() {
		this.addComponentListener(new ComponentAdapter() {

			public void componentResized(ComponentEvent e) {
				updateScale();
			}
		});
	}	
	
	
	/**
	 * @param end
	 *            The end to set.
	 */
	public void setEnd(long end) {
		this.end = end;
		this.refreshTimeItems();
	}

	/**
	 * @param start
	 *            The start to set.
	 */
	public void setStart(long start) {		
		this.start = start;
		this.refreshTimeItems();
	}

	/**
	 * @return Returns the end.
	 */
	public long getEnd() {
		return this.end;
	}

	/**
	 * @return Returns the start.
	 */
	public long getStart() {
		return this.start;
	}
	
	
	public double getScale() {
		return this.scale;
	}

	abstract void refreshTimeItems();
	
	public String getToolTipText(MouseEvent event) {
		int x = event.getX();
		if (!this.timeItems.isEmpty()) {
			for (Iterator it = this.timeItems.iterator(); it.hasNext();) {
				TestTimeItem testTimeItem = (TestTimeItem) it.next();
				if (testTimeItem.x < x && x < testTimeItem.x + testTimeItem.width) {
					SimpleDateFormat sdf = (SimpleDateFormat) UIManager.get(ResourceKeys.SIMPLE_DATE_FORMAT);
					return sdf
							.format(new Date((long) (this.start + ((testTimeItem.x - PlanPanel.MARGIN / 2) / this.scale))));
				}
			}
		}
		return this.title;
	}
	
	final void updateScale() {
		double scale1 = (double) (this.getWidth() - PlanPanel.MARGIN) / (double) (this.end - this.start);
		double currentScale = this.scale;
		this.scale = 0.0;
		if (Math.abs(currentScale - scale1) > Double.MIN_VALUE) {
			this.scale = scale1;
			this.refreshTimeItems();
		} else {
			this.scale = currentScale;
		}
		super.repaint();
		super.revalidate();
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		int height = getHeight();
		int width = this.getWidth();
		
		g.setColor(Color.GRAY);
		FontMetrics fontMetrics = g.getFontMetrics();
		g.clearRect(0, 0, fontMetrics.stringWidth(this.title), this.titleHeight / 2 + 3);
		g.setColor(Color.BLACK);
		g.drawString(this.title, 5, this.titleHeight / 2 + 2);
		g.drawLine(0, this.titleHeight / 2 + 3, width, this.titleHeight / 2 + 3);
		g.drawLine(0, height - 1, width, height - 1);
	}
	
	protected final void drawItemRect(Graphics g, int x, int y, int width, int height, Color c) {
		g.setColor(c);
		g.fillRect(x + 2, y + 2, width - 3, height - 3);
		g.draw3DRect(x, y, width, height, true);
	}

}
