package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.util.*;
import java.util.List;

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
	private PathDecomposer decomposer;
	PathElement startPE;
	PathElement endPE;
	PathElement activePE;

	public PathElementsPanel(PathElementsLayeredPanel panel, Dispatcher dispatcher, double y[], double deltaX)
	{
		super(panel, dispatcher, y, deltaX);
	}

	public void setPath(SchemePath path)
	{
		this.path = path;
		decomposer = new PathDecomposer(path);
//		if (events != null)
//			decompositor.setTotalOpticalLength(events[events.length - 1].last_point * deltaX);
//		else if (ep != null)
//			decompositor.setTotalOpticalLength(ep[ep.length - 1].end * deltaX);
	}

	protected void setGraphBounds(int start, int end)
	{
		super.setGraphBounds(start, end);

		if (path != null)
		{
			startPE = decomposer.getPathElementByOpticalDistance(start * deltaX);
			if (decomposer.hasPreviousPathElement(startPE))
				startPE = decomposer.getPreviousPathElement(startPE);
			endPE = decomposer.getPathElementByOpticalDistance(end * deltaX);
			if (decomposer.hasNextPathElement(endPE))
				endPE = decomposer.getNextPathElement(endPE);
		}
	}

	protected void this_mousePressed(MouseEvent e)
	{
		startpos = e.getPoint();
		currpos = e.getPoint();

		if (paint_path_elements && path != null)
		{
			if (currpos.y < 10)
			{
				setting_active_pe = true;
				double optd = deltaX * coord2index(currpos.x);
				activePE = decomposer.getPathElementByOpticalDistance(optd);
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


	public void paint(Graphics g)
	{
		super.paint(g);

		if (paint_path_elements && path != null)
		{
			if (startPE == null || endPE == null)
				setGraphBounds(start, end);

			List links = Arrays.asList(path.links());
			for (Iterator it = links.listIterator(links.indexOf(startPE)); it.hasNext();)
			{
				PathElement pe = (PathElement)it.next();
				if (pe.equals(endPE))
					break;

				if (pe.equals(activePE))
					g.setColor(Color.RED);
				else if (pe.getPathElementKind().value() == PathElementKind._SCHEME_CABLE_LINK)
					g.setColor(Color.GREEN);
				else
					g.setColor(Color.BLUE);

				double[] d = this.decomposer.getOpticalDistanceFromStart(pe);
				if (d.length != 2)
					continue;
				final int start1 = index2coord((int)Math.round(d[0] / this.deltaX));
				final int end1 = index2coord((int)Math.round(d[1] / this.deltaX));
				g.drawLine(start1, 3, start1, 5);
				g.drawLine(start1, 5, end1, 5);
				g.drawLine(end1, 5, end1, 3);
			}
		}
	}
}