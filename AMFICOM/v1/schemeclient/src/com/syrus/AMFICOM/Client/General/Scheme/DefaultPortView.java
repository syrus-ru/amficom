package com.syrus.AMFICOM.Client.General.Scheme;

import java.util.Map;

import java.awt.*;

import com.jgraph.JGraph;
import com.jgraph.graph.*;
import com.jgraph.pad.*;
import com.jgraph.pad.EllipseView.*;

class DefaultPortView extends EllipseView
{
	public static SchemeEllipseRenderer renderer = new SchemeEllipseRenderer();

	public DefaultPortView(Object cell, JGraph jgraph, CellMapper mapper)
	{
		super(cell, jgraph, mapper);
	}

	public CellViewRenderer getRenderer()
	{
		return renderer;
	}

	public void update()
	{
		super.update();
	}

	public Map setAttributes(Map map)
	{
		Map undo = super.setAttributes(map);
		return undo;
	}


	static class SchemeEllipseRenderer extends EllipseView.EllipseRenderer
	{
		protected void paintSelectionBorder(Graphics g)
		{
			((Graphics2D) g).setStroke(GraphConstants.SELECTION_STROKE);
			if (childrenSelected)
				g.setColor(graph.getGridColor());
//      else if (hasFocus && selected)
 //       g.setColor(graph.getLockedHandleColor());
			else if (selected)
				g.setColor(graph.getHighlightColor());
			if (childrenSelected || selected) {
				Dimension d = getSize();
				g.drawRect(0, 0, d.width - 1, d.height - 1);
			}
		}
	}
}

