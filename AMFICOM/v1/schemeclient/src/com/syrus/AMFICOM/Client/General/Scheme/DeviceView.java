package com.syrus.AMFICOM.Client.General.Scheme;

import java.util.*;

import java.awt.*;
import java.awt.event.MouseEvent;

import com.jgraph.JGraph;
import com.jgraph.graph.*;

public class DeviceView extends VertexView
{
	DeviceCell cell;
	Rectangle _bounds;
	public static VertexRenderer schemerenderer = new SchemeVertexRenderer();

	public DeviceView(Object cell, JGraph graph, CellMapper mapper)
	{
		super(cell, graph, mapper);
		this.cell = (DeviceCell)cell;
	}

	public CellViewRenderer getRenderer()
	{
		return schemerenderer;
	}

	public CellHandle getHandle(GraphContext context)
	{
		if (GraphConstants.isSizeable(getAllAttributes())
				&& context.getGraph().isSizeable())
			return new DeviceSizeHandle(this, context);
		return null;
	}

	public Map setAttributes(Map map)
	{
		Map undo = super.setAttributes(map);
		return undo;
	}

	public class DeviceSizeHandle extends VertexView.SizeHandle
	{
		public DeviceSizeHandle (DeviceView vertexview, GraphContext ctx)
		{
			super (vertexview, ctx);
		}

		public void mousePressed(MouseEvent event)
		{
			super.mousePressed(event);
			_bounds = initialBounds;
		}

		public void mouseDragged(MouseEvent event)
		{
			super.mouseDragged(event);

			Rectangle bounds1 = computeBounds(event);

			if (!bounds1.equals(_bounds))
			{
				double u = GraphConstants.PERCENT;
				java.util.List list = cell.getChildren();
				Iterator iterator = list.iterator();
				while (iterator.hasNext())
				{
					Port port = (Port)iterator.next();
					Point pos = GraphConstants.getOffset(port.getAttributes());
					if (pos != null)
					{
						if (port.edges().hasNext())
						{
							Edge edge = (Edge)port.edges().next();
							if (edge != null)
							{
								DefaultPort targetport = (DefaultPort)edge.getTarget();
								if (targetport != null)
								{
									DefaultGraphCell visualPort = (DefaultGraphCell)targetport.getParent();
									if (visualPort != null)
									{
										Rectangle rect = GraphConstants.getBounds(visualPort.getAttributes());

										Point newpos = new Point(pos.x, (int)(u * ( (double)(rect.y + 4 - bounds1.y) / (double)bounds1.height)));
										GraphConstants.setOffset(port.getAttributes(), newpos);
									}
								}
							}
						}
						/*Point newpos = new Point (pos.x,
						(int) ((double)_bounds.height/(double)bounds.height * ((double)pos.y - u) + u));
						GraphConstants.setOffset(port.getAttributes(), newpos);
						m.put(port, port.getAttributes());*/
					}
				}
				_bounds = bounds1;
			}
		}
	}

	public static class SchemeVertexRenderer extends VertexRenderer
	{
		protected void paintSelectionBorder(Graphics g)
		{
			((Graphics2D) g).setStroke(GraphConstants.SELECTION_STROKE);
			if (childrenSelected)
				g.setColor(graph.getGridColor());
//		else if (hasFocus && selected)
//			g.setColor(graph.getLockedHandleColor());
			else if (selected)
				g.setColor(graph.getHighlightColor());
			if (childrenSelected || selected) {
				Dimension d = getSize();
				g.drawRect(0, 0, d.width - 1, d.height - 1);
			}
		}
}
}

