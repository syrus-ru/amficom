package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

import java.util.Iterator;

import java.awt.*;
import java.awt.event.*;

import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;

public class PathElementsPanel extends AnalysisPanel
{
	protected boolean paint_path_elements = false;
	private boolean setting_active_pe = false;

	SchemePath path;
	PathDecompositor decompositor;
	PathElement startPE;
	PathElement endPE;
	PathElement activePE;

	public PathElementsPanel(PathElementsLayeredPanel panel, Dispatcher dispatcher, double y[], double delta_x)
	{
		super(panel, dispatcher, y, delta_x);
	}

	public void setPath(SchemePath path)
	{
		this.path = path;
		decompositor = new PathDecompositor(path);
//		if (events != null)
//			decompositor.setTotalOpticalLength(events[events.length - 1].last_point * delta_x);
//		else if (ep != null)
//			decompositor.setTotalOpticalLength(ep[ep.length - 1].end * delta_x);
	}

	protected void setGraphBounds(int start, int end)
	{
		super.setGraphBounds(start, end);

		if (path != null)
		{
			startPE = decompositor.getPathElementByOpticalDistance(start * delta_x);
			if (decompositor.hasPreviousPathElement(startPE))
				startPE = decompositor.getPreviousPathElement(startPE);
			endPE = decompositor.getPathElementByOpticalDistance(end * delta_x);
			if (decompositor.hasNextPathElement(endPE))
				endPE = decompositor.getNextPathElement(endPE);
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
				double optd = delta_x * coord2index(currpos.x);
				activePE = decompositor.getPathElementByOpticalDistance(optd);
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

			for (Iterator it = path.links.listIterator(path.links.indexOf(startPE)); it.hasNext();)
			{
				PathElement pe = (PathElement)it.next();
				if (pe.equals(endPE))
					break;

				if (pe.equals(activePE))
					g.setColor(Color.RED);
				else if (pe.getType() == PathElement.CABLE_LINK)
					g.setColor(Color.GREEN);
				else
					g.setColor(Color.BLUE);

				double[] d = decompositor.getOpticalDistanceFromStart(pe);
				int st = index2coord((int)Math.round(d[0] / delta_x));
				int en = index2coord((int)Math.round(d[1] / delta_x));
				g.drawLine(st, 3, st, 5);
				g.drawLine(st, 5, en, 5);
				g.drawLine(en, 5, en, 3);
			}
		}
	}
}