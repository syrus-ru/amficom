package com.syrus.AMFICOM.Client.Map.Props;

import com.syrus.AMFICOM.Client.Map.mapview.CablePath;
import com.syrus.AMFICOM.Client.Map.mapview.UnboundLink;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;

public class SiteCrossingPanel extends JPanel 
{
	SiteNode site;
	
	public static final int WELL_RADIUS = 50;
	public static final int ARROW_LENGTH = 20;
	public static final int SPIKE_LENGTH = 8;
	public static final int TUNNEL_LENGTH = 100;
	public static final int TUNNEL_WIDTH = 20;
	public static final int TEXT_DELTA = 30;
	
	public static final Color FILL_COLOR = new Color(240, 240, 240);
	public static final Color LINE_COLOR = Color.BLACK;
	public static final Color PATH_COLOR = Color.GREEN;

	static final Stroke SIPMLE_STROKE = new BasicStroke(1);
	static final Stroke PATH_STROKE = new BasicStroke(4);

	List tunnels = new LinkedList();
	
	CablePath cpath;
	
	int index1 = -1;
	int index2 = -1;

	public SiteCrossingPanel()
	{
	}

	public void setSite(SiteNode site)
	{
		this.site = site;
		this.cpath = null;
		index1 = -1;
		index2 = -1;
		tunnels.clear();
		if(site != null)
		{
			for(Iterator it = site.getMap().getPhysicalLinksAt(site).iterator(); it.hasNext();)
			{
				Object tunnel = it.next();
				if(!(tunnel instanceof UnboundLink))
					tunnels.add(tunnel);
			}
		}
		repaint();
	}

	public void setCable(CablePath cpath)
	{
		this.cpath = cpath;

		index1 = -1;
		index2 = -1;
		
		if(cpath != null)
		{
			for(Iterator it = cpath.getLinks().iterator(); it.hasNext();)
			{
				PhysicalLink link = (PhysicalLink)it.next();
				if(tunnels.contains(link))
				{
					if(index1 == -1)
						index1 = tunnels.indexOf(link);
					else
						index2 = tunnels.indexOf(link);
				}
			}
		}

		repaint();
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D )g;
		
		Color color = g2.getColor();
		Stroke stroke = g2.getStroke();
		
		int width = getWidth();
		int height = getHeight();
		
		int centerx = width / 2;
		int centery = height / 2;

		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, width, height);

		g2.setStroke(SIPMLE_STROKE);

		g2.setColor(FILL_COLOR);
		
		Arc2D arc = new Arc2D.Double();
		arc.setArc(
				centerx - WELL_RADIUS, 
				centery - WELL_RADIUS, 
				WELL_RADIUS * 2, 
				WELL_RADIUS * 2,
				0,
				360,
				Arc2D.OPEN);

		g2.fillOval(
				centerx - WELL_RADIUS, 
				centery - WELL_RADIUS, 
				WELL_RADIUS * 2, 
				WELL_RADIUS * 2);
		g2.setColor(LINE_COLOR);
		g2.drawOval(
				centerx - WELL_RADIUS, 
				centery - WELL_RADIUS, 
				WELL_RADIUS * 2, 
				WELL_RADIUS * 2);

		int cnt = tunnels.size();
		if(cnt > 0)
		{
			double a = Math.PI;
			Iterator it = tunnels.iterator();
			double added = 2 * Math.PI / cnt;
			for (int i = 0; i < cnt; i++) 
			{
				drawTunnel(g2, centerx, centery, a, (PhysicalLink)it.next(), arc);
				a += added;
			}
		}
		
		if(cpath != null && cnt > 0)
		{
			double added = 2 * Math.PI / cnt;
			if(index1 != -1)
			{
				double a = Math.PI + index1 * added;
				drawPath(g2, centerx, centery, a);
			}
			if(index2 != -1)
			{
				double a = Math.PI + index2 * added;
				drawPath(g2, centerx, centery, a);
			}
		}

		g2.setColor(color);
		g2.setStroke(stroke);
	}

	private void drawPath(
			Graphics2D g2,
			int centerx, 
			int centery, 
			double a)
	{
		double sinA = Math.sin(a);

		double cosA = Math.cos(a);

		// смещение по x и по y для линии выделения
		int lxshift = (int )(TUNNEL_WIDTH * sinA);
		int lyshift = (int )(TUNNEL_WIDTH * cosA);
		
		int endx = (int )(centerx + TUNNEL_LENGTH * cosA);
		int endy = (int )(centery + TUNNEL_LENGTH * sinA);

		g2.setColor(PATH_COLOR);
		g2.setStroke(PATH_STROKE);

		g2.drawLine(
				centerx, 
				centery,
				endx,
				endy);
	}
	
	private void drawTunnel(
			Graphics2D g2, 
			int centerx, 
			int centery, 
			double a, 
			PhysicalLink link,
			Arc2D arc)
	{
		double sinA = Math.sin(a);

		double cosA = Math.cos(a);

		// смещение по x и по y для линии выделения
		int lxshift = (int )(TUNNEL_WIDTH * sinA);
		int lyshift = (int )(TUNNEL_WIDTH * cosA);
		
		int endx = (int )(centerx + TUNNEL_LENGTH * cosA);
		int endy = (int )(centery + TUNNEL_LENGTH * sinA);

		int[] coordx = new int[] {
				centerx + lxshift,
				endx + lxshift,
				endx - lxshift,
				centerx - lxshift};
		int[] coordy = new int[] {
				centery - lyshift,
				endy - lyshift,
				endy + lyshift,
				centery + lyshift};
		
		g2.setColor(FILL_COLOR);
		g2.fillPolygon(coordx, coordy, 4);
//		g2.drawPolygon(coordx, coordy, 4);

		g2.setColor(LINE_COLOR);

		Line2D line1 = new Line2D.Double();
		line1.setLine(
				centerx + lxshift, 
				centery - lyshift,
				endx + lxshift,
				endy - lyshift);
		
		Line2D line2 = new Line2D.Double();
		line2.setLine(
				centerx - lxshift, 
				centery + lyshift,
				endx - lxshift,
				endy + lyshift);
		
		Point pt1 = findIntersection(line1, arc);
		g2.drawLine(pt1.x, pt1.y, (int )line1.getX2(), (int )line1.getY2());

		Point pt2 = findIntersection(line2, arc);
		g2.drawLine(pt2.x, pt2.y, (int )line2.getX2(), (int )line2.getY2());

		int arrowstartx = (int )(
			centerx 
			+ (TUNNEL_LENGTH - ARROW_LENGTH) * cosA
			+ (TUNNEL_WIDTH + 4) * sinA);
		int arrowstarty = (int )(
			centery 
			+ (TUNNEL_LENGTH - ARROW_LENGTH) * sinA
			- (TUNNEL_WIDTH + 4) * cosA);

		int arrowendx = (int )(arrowstartx + ARROW_LENGTH * cosA);
		int arrowendy = (int )(arrowstarty + ARROW_LENGTH * sinA);

		g2.drawLine(arrowstartx, arrowstarty, arrowendx, arrowendy);
		g2.drawLine(
				arrowendx, 
				arrowendy, 
				(int )(arrowendx - SPIKE_LENGTH * Math.cos(a + 0.2)), 
				(int )(arrowendy - SPIKE_LENGTH * Math.sin(a + 0.2)));
		g2.drawLine(
				arrowendx, 
				arrowendy, 
				(int )(arrowendx - SPIKE_LENGTH * Math.cos(a - 0.2)), 
				(int )(arrowendy - SPIKE_LENGTH * Math.sin(a - 0.2)));

		AbstractNode node = link.getOtherNode(site);
		
		String text = "к узлу \"" + node.getName() + "\"";

		g2.setColor(LINE_COLOR);

		Font font = this.getFont();
		FontMetrics fm = this.getFontMetrics(font);
		int textwidth = fm.stringWidth(text);
		int textheight = fm.getHeight();
		int ydelta = (sinA > 0) ? TEXT_DELTA + textheight : -TEXT_DELTA;
		g2.drawString(text, endx - textwidth / 2, endy + ydelta);
	}
	
	protected Point findIntersection(Line2D line, Arc2D arc)
	{
		int x1 = (int )line.getX1();
		int x2 = (int )line.getX2();
		int y1 = (int )line.getY1();
		int y2 = (int )line.getY2();

		int divx = Math.abs(x2 - x1);
		int divy = Math.abs(y2 - y1);
		
		int dx = (x2 < x1) ? 1 : -1;
		int dy = (y2 < y1) ? 1 : -1;

		Point2D.Double pt = (Point2D.Double )line.getP2();
		
		while(true)
		{
			if(arc.contains(pt))
				break;
			if(divx > divy)
			{
				pt.x += dx;
				double coef = (pt.x - x1) / (x2 - x1);
				pt.y = coef * (y2 - y1) + y1;
			}
			else
			{
				pt.y += dy;
				double coef = (pt.y - y1) / (y2 - y1);
				pt.x = coef * (x2 - x1) + x1;
			}
		}
		
		return new Point((int )pt.getX(), (int )pt.getY());
	}
}