package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.util.*;

import java.awt.*;
import java.awt.event.MouseEvent;

import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.scheme.*;
import com.syrus.AMFICOM.scheme.PathDecomposer;
import com.syrus.AMFICOM.scheme.corba.PathElementKind;

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
		PathDecomposer decomposer = new PathDecomposer(path);
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
			if (this.currpos.y < 10) {
				this.setting_active_pe = true;
				this.activePathElement = this.path.getPathElementByOpticalDistance(this.deltaX * coord2index(this.currpos.x));
				return;
			}
		}
		super.this_mousePressed(e);
	}

	protected void this_mouseDragged(MouseEvent e)
	{
		if (setting_active_pe)
			return;
		super.this_mouseDragged(e);
	}

	protected void this_mouseReleased(MouseEvent e)
	{
		if (setting_active_pe)
		{
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

			for (final Iterator pathElementIterator = this.path.getPathElements().tailSet(this.startPathElement).iterator(); pathElementIterator.hasNext();) {
				final PathElement pathElement = (PathElement) pathElementIterator.next();
				if (pathElement == this.endPathElement)
					break;

				if (pathElement == this.activePathElement)
					g.setColor(Color.RED);
				else if (pathElement.getPathElementKind().value() == PathElementKind._SCHEME_CABLE_LINK)
					g.setColor(Color.GREEN);
				else
					g.setColor(Color.BLUE);

				double d[] = this.path.getOpticalDistanceFromStart(pathElement);
				final int start1 = index2coord((int)Math.round(d[0] / this.deltaX));
				final int end1 = index2coord((int)Math.round(d[1] / this.deltaX));
				g.drawLine(start1, 3, start1, 5);
				g.drawLine(start1, 5, end1, 5);
				g.drawLine(end1, 5, end1, 3);
			}
		}
	}
}