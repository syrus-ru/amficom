package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.Iterator;
import java.util.logging.Level;

import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.configuration.EquipmentTypeCodename;
import com.syrus.AMFICOM.scheme.PathElement;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemePath;
import com.syrus.AMFICOM.scheme.corba.IdlPathElementPackage.IdlDataPackage.IdlKind;
import com.syrus.util.Log;

public final class PathElementsPanel extends AnalysisPanel
{
	protected boolean paint_path_elements = false;
	private boolean setting_active_pe = false;

	SchemePath path;
	private PathElement startPathElement;
	private PathElement endPathElement;
	private PathElement activePathElement;

	public PathElementsPanel(PathElementsLayeredPanel panel, Dispatcher dispatcher, double y[], double deltaX)
	{
		super(panel, dispatcher, y, deltaX);
	}

	public void setPath(SchemePath path)
	{
		this.path = path;
//		if (events != null)
//			decompositor.setTotalOpticalLength(events[events.length - 1].last_point * deltaX);
//		else if (ep != null)
//			decompositor.setTotalOpticalLength(ep[ep.length - 1].end * deltaX);
	}

	protected void setGraphBounds(final int start, final int end) {
		super.setGraphBounds(start, end);
		if (this.path != null) {
			this.startPathElement = this.path.getPathElementByOpticalDistance(start * this.deltaX);
			if (this.path.hasPreviousPathElement(this.startPathElement))
				this.startPathElement = this.path.getPreviousPathElement(this.startPathElement);
			this.endPathElement = this.path.getPathElementByOpticalDistance(end * this.deltaX);
			if (this.path.hasNextPathElement(this.endPathElement))
				this.endPathElement = this.path.getNextPathElement(this.endPathElement);
		}
	}

	protected void this_mousePressed(MouseEvent e) {
		this.startpos = e.getPoint();
		this.currpos = e.getPoint();

		if (this.paint_path_elements && this.path != null) {
			if (this.currpos.y > 4 && this.currpos.y < 14) {
				this.setting_active_pe = true;
				double distance = this.deltaX * coord2index(this.currpos.x);
				this.activePathElement = this.path.getPathElementByOpticalDistance(distance);
				double[] d = this.path.getOpticalDistanceFromStart(this.activePathElement);
				
				if (this.activePathElement.getKind() != IdlKind.SCHEME_ELEMENT &&
						Math.abs(distance - d[0]) < 3 / scaleX * deltaX && 
						this.path.hasPreviousPathElement(this.activePathElement)) {
					this.activePathElement = this.path.getPreviousPathElement(this.activePathElement);
					Log.debugMessage("Set previous pathElement : " + this.activePathElement.getName() + "(" + (3 / scaleX * deltaX) + ")", Level.FINER);
				} else if (this.activePathElement.getKind() != IdlKind.SCHEME_ELEMENT &&
						Math.abs(distance - d[1]) < 3 / scaleX * deltaX && 
						this.path.hasNextPathElement(this.activePathElement)) {
					this.activePathElement = this.path.getNextPathElement(this.activePathElement);
					Log.debugMessage("Set next pathElement : " + this.activePathElement.getName() + "(" + (3 / scaleX * deltaX) + ")", Level.FINER);
				}
				return;
			}
		}
		super.this_mousePressed(e);
	}

	protected void this_mouseDragged(MouseEvent e)
	{
		if (setting_active_pe) {
			if (this.activePathElement.getKind() == IdlKind.SCHEME_ELEMENT) {
				setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
				Log.debugMessage("PathElement " + this.activePathElement.getName() + " moved on " + ((currpos.x - startpos.x) / scaleX * deltaX) + " m", Level.FINER);
				upd_currpos(e);
				paintMovingPE(getGraphics().create());
			}
			return;
		}
		super.this_mouseDragged(e);
	}

	protected void this_mouseReleased(MouseEvent e)
	{
		if (setting_active_pe)
		{
			double d = ((currpos.x - startpos.x) / scaleX * deltaX);
			if (Math.abs(d) > deltaX) {
				PathElement lastNode = this.path.getPreviousNode(this.activePathElement);
				PathElement nextNode = this.path.getNextNode(this.activePathElement);
				
				if (lastNode == null) {
					lastNode = this.path.getPreviousPathElement(this.activePathElement);
				}
				if (nextNode == null) {
					nextNode = this.path.getNextPathElement(this.activePathElement);
				}
				if (lastNode != null) {
					this.path.changeOpticalLength(lastNode, this.activePathElement, d);
				}
				if (nextNode != null) {
					this.path.changeOpticalLength(this.activePathElement, nextNode, -d);
				}
			}
			
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			setting_active_pe = false;
			parent.jLayeredPane.repaint();
			return;
		}
		super.this_mouseReleased(e);
	}

	public void paint(final Graphics g) {
		super.paint(g);

		if (this.paint_path_elements && this.path != null) {
			if (this.startPathElement == null || this.endPathElement == null)
				setGraphBounds(this.start, this.end);

			for (final Iterator pathElementIterator = this.path.getPathMembers().tailSet(this.startPathElement).iterator(); pathElementIterator.hasNext();) {
				final PathElement pathElement = (PathElement) pathElementIterator.next();
				
				if (pathElement == this.activePathElement)
					g.setColor(Color.RED);
				else if (pathElement.getKind() == IdlKind.SCHEME_CABLE_LINK)
					g.setColor(Color.CYAN);
				else if (pathElement.getKind() == IdlKind.SCHEME_LINK)
					g.setColor(Color.BLUE);
				else 
					g.setColor(Color.GREEN);

				double d[] = this.path.getOpticalDistanceFromStart(pathElement);
				final int start1 = index2coord((int)Math.round(d[0] / this.deltaX));
				final int end1 = index2coord((int)Math.round(d[1] / this.deltaX));
				Log.debugMessage("PathElement " + pathElement.getName() + " from " + start1 + " to " + end1, Level.FINER);
				if (pathElement.getKind() == IdlKind.SCHEME_ELEMENT) {
					SchemeElement se = pathElement.getSchemeElement();
					// if muff - paint only small box and dashed line
					if (se.getEquipmentType().getCodename().equals(EquipmentTypeCodename.MUFF.toString())) {
						g.fill3DRect(start1 - 1, 6, 2, 8, true);
						((Graphics2D)g).setStroke(ScaledGraphPanel.DASHED_STROKE);
						g.drawLine(start1, 6, start1, getHeight());
						((Graphics2D)g).setStroke(ScaledGraphPanel.DEFAULT_STROKE);
					} else { // paint large box, dashed line and name of SE
						g.fill3DRect(start1 - 4, 6, 8, 8, true);
						((Graphics2D)g).setStroke(ScaledGraphPanel.DASHED_STROKE);
						g.drawLine(start1, 6, start1, getHeight());
						((Graphics2D)g).setStroke(ScaledGraphPanel.DEFAULT_STROKE);
						
						String text = pathElement.getName();
						FontMetrics fm = this.parent.getFontMetrics(this.parent.getFont());
						int height = fm.stringWidth(text);
						int width = fm.getHeight();
						int y1 = 6;
						Graphics2D g2 = (Graphics2D) g;
						AffineTransform t = g2.getTransform();
						g2.translate(0, height + 10);
						g2.rotate (Math.toRadians(270), start1 + width - 5, y1);
						g2.drawString(text, start1 + width - 5, y1);
						g2.setTransform(t);
					}
				} else {
					g.drawLine(start1, 3, start1, 5);
					g.drawLine(start1, 5, end1, 5);
					g.drawLine(end1, 5, end1, 3);
				}
			}
		}
	}
	
	void paintMovingPE(Graphics g) {
		g.setXORMode(Color.CYAN);
		g.drawRect (currpos.x - 4, 6, 8, 8);
		g.drawRect (tmppos.x - 4, 6, 8, 8);
		((Graphics2D)g).setStroke(ScaledGraphPanel.DASHED_STROKE);
		g.drawLine(currpos.x, 16, currpos.x, getHeight());
		g.drawLine(tmppos.x, 16, tmppos.x, getHeight());
		((Graphics2D)g).setStroke(ScaledGraphPanel.DEFAULT_STROKE);
	}
}
