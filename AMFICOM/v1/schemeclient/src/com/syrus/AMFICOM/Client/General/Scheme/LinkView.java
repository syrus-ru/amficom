package com.syrus.AMFICOM.Client.General.Scheme;

import java.util.Map;

import java.awt.*;
import java.awt.event.MouseEvent;

import com.jgraph.JGraph;
import com.jgraph.graph.*;
import com.jgraph.graph.EdgeView.*;

public class LinkView extends EdgeView
{
	public static LinkRenderer renderer = new LinkRenderer();

	public LinkView(Object cell, JGraph graph, CellMapper mapper, int i)
	{
		super(cell, graph, mapper);
	}

	public CellHandle getHandle(GraphContext context)
	{
		return new ShemeEdgeHandle(this, context);
	}

	public EdgeRenderer getEdgeRenderer()
	{
		return (EdgeRenderer) getRenderer();
	}

	public CellViewRenderer getRenderer()
	{
		return renderer;
	}

	public Map setAttributes(Map change)
	{
		return super.setAttributes(change);
	}

	public Point getPoint(int index)
	{
		return super.getPoint(index);
	}

	public java.util.List getPoints()
	{
		return points;
	}

	public int getPointCount()
	{
		return points.size();
	}

	public class ShemeEdgeHandle extends EdgeView.EdgeHandle
	{
		public ShemeEdgeHandle(EdgeView edge, GraphContext ctx)
		{
			super(edge, ctx);
		}

		public void mouseReleased(MouseEvent ev)
		{
			super.mouseReleased(ev);
			/*trigger = !trigger;
			if (trigger)
				((SchemeGraph)graph).setSelectionPath(getCell());
			else
				graph.setSelectionCell(getCell());*/
		}

		public void mouseDragged(MouseEvent event)
		{
	//		if (!event.isShiftDown())
				super.mouseDragged(event);
				Point p = labelPosition;
			if (cell instanceof DefaultLink)
			{
				DefaultLink link = (DefaultLink)cell;
			//	link.routed[0] = new Point(link.routed[0].x + counter++, link.routed[0].y + counter);
			}
		}

		// Update and paint control points
		public void paint(Graphics g)
		{
			invalidate();
			for (int i = 0; i < r.length; i++)
			{
				if (isEdgeConnectable)
					g.setColor(graph.getHandleColor());
				else
					g.setColor(graph.getLockedHandleColor());
				g.fill3DRect(r[i].x, r[i].y, r[i].width, r[i].height, true);
				PortView port = null;
				if (i == 0 && edge.getSource() != null)
					port = edge.getSource();
				else if (i == r.length - 1 && edge.getTarget() != null)
					port = edge.getTarget();
				if (port != null)
				{
					g.setColor(graph.getLockedHandleColor());
					Point tmp = GraphConstants.getOffset(port.getAllAttributes());
					if (tmp != null)
					{
						g.drawLine(r[i].x + 1,
											 r[i].y + 1,
											 r[i].x + r[i].width - 3,
											 r[i].y + r[i].height - 3);
						g.drawLine(r[i].x + 1,
											 r[i].y + r[i].height - 3,
											 r[i].x + r[i].width - 3,
											 r[i].y + 1);
					}
					else
						g.drawRect(r[i].x + 2,
											 r[i].y + 2,
											 r[i].width - 5,
											 r[i].height - 5);
				}
			}
		}

		protected void paintPort(Graphics g, PortView p)
		{
			Port p1 = (Port)p.getCell();
			Map map = getModel().getAttributes(p1);
			if (map != null)
				if (!GraphConstants.isConnectable(map))
					return;

			boolean offset = (GraphConstants.getOffset(p.getAllAttributes()) != null);
			Rectangle r = (offset) ? p.getBounds() : p.getParentView().getBounds();
			r = graph.toScreen(new Rectangle(r));
			int s = 3;
			r.translate(-s, -s);
			r.setSize(r.width + 2 * s, r.height + 2 * s);
			graph.getUI().paintCell(g, p, r, true);
		}
	}
}

