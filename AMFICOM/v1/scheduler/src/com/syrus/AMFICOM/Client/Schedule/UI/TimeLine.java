/*-
 * $Id: TimeLine.java,v 1.21 2005/10/31 12:29:59 bass Exp $
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
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.JComponent;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.Schedule.UI.TestLine.TestTimeItem;

/**
 * @version $Revision: 1.21 $, $Date: 2005/10/31 12:29:59 $
 * @author $Author: bass $
 * @author Vladimir Dolzhenko
 * @module scheduler
 */
public abstract class TimeLine extends JComponent {

	int			minimalWidth;
	int			titleHeight;

	String		title;

	long		start = 0;
	long		end = 0;
	double		scale = 0.0;

	protected SortedSet<TestTimeItem>	timeItems	= new TreeSet<TestTimeItem>();

	public TimeLine() {
		
		this.createComponentListener();
		
		Font font2 = UIManager.getLookAndFeelDefaults().getFont("Button.font");
		FontMetrics fontMetrics = this.getFontMetrics(font2);
		
		this.minimalWidth = fontMetrics.charWidth('W');

		this.titleHeight = fontMetrics.getHeight();
	}

	private void createComponentListener() {
		this.addComponentListener(new ComponentAdapter() {

			@Override
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
//		this.refreshTimeItems();
	}

	/**
	 * @param start
	 *            The start to set.
	 */
	public void setStart(long start) {		
		this.start = start;
//		this.refreshTimeItems();
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
	
	@Override
	protected void paintComponent(final Graphics g) {
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
	
	protected final void drawItemRect(final Graphics g, 
	                                  final int x, 
	                                  final int y, 
	                                  final int width, 
	                                  final int height, 
	                                  final Color c) {
		g.setColor(c);
//		Log.debugMessage("x: " + x + ", width: " + width, Log.FINEST);
		g.fillRect(x + 2, y + 2, width - 3, height - 3);
		g.draw3DRect(x, y, width, height, true);
	}

}
