package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

import java.util.*;
import java.util.List;

import java.awt.*;
import java.awt.event.MouseEvent;

import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.Resource.Scheme.PathDecompositor;
import com.syrus.AMFICOM.scheme.corba.*;
import com.syrus.AMFICOM.scheme.corba.PathElementPackage.Type;

public class PathElementsPanel extends AnalysisPanel
{
	protected boolean paint_path_elements = false;
	private boolean setting_active_pe = false;

	SchemePath path;
	PathDecompositor decompositor;
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
		decompositor = new PathDecompositor(path);
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
			startPE = decompositor.getPathElementByOpticalDistance(start * deltaX);
			if (decompositor.hasPreviousPathElement(startPE))
				startPE = decompositor.getPreviousPathElement(startPE);
			endPE = decompositor.getPathElementByOpticalDistance(end * deltaX);
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
				double optd = deltaX * coord2index(currpos.x);
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

			List links = Arrays.asList(path.links());
			for (Iterator it = links.listIterator(links.indexOf(startPE)); it.hasNext();)
			{
				PathElement pe = (PathElement)it.next();
				if (pe.equals(endPE))
					break;

				if (pe.equals(activePE))
					g.setColor(Color.RED);
				else if (pe.type().equals(Type.SCHEME_CABLE_LINK))
					g.setColor(Color.GREEN);
				else
					g.setColor(Color.BLUE);

				double[] d = decompositor.getOpticalDistanceFromStart(pe);
				int st = index2coord((int)Math.round(d[0] / deltaX));
				int en = index2coord((int)Math.round(d[1] / deltaX));
				g.drawLine(st, 3, st, 5);
				g.drawLine(st, 5, en, 5);
				g.drawLine(en, 5, en, 3);
			}
		}
	}
}