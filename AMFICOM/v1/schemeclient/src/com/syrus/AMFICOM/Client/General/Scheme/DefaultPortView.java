package com.syrus.AMFICOM.Client.General.Scheme;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Map;

import com.jgraph.JGraph;
import com.jgraph.graph.CellMapper;
import com.jgraph.graph.CellViewRenderer;
import com.jgraph.graph.GraphConstants;
import com.jgraph.pad.EllipseView;
import com.jgraph.pad.EllipseView$EllipseRenderer;

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


	static class SchemeEllipseRenderer extends EllipseRenderer
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

