package com.syrus.AMFICOM.Client.General.Scheme;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.Map;

import com.jgraph.JGraph;
import com.jgraph.graph.CellHandle;
import com.jgraph.graph.CellMapper;
import com.jgraph.graph.CellViewRenderer;
import com.jgraph.graph.DefaultGraphCell;
import com.jgraph.graph.DefaultPort;
import com.jgraph.graph.Edge;
import com.jgraph.graph.GraphConstants;
import com.jgraph.graph.GraphContext;
import com.jgraph.graph.Port;
import com.jgraph.graph.VertexRenderer;
import com.jgraph.graph.VertexView;
import com.jgraph.graph.VertexView$SizeHandle;

public class DeviceView extends VertexView
{
	DeviceCell cell;
	Rectangle _bounds;
	public static VertexRenderer renderer = new SchemeVertexRenderer();

	public DeviceView(Object cell, JGraph graph, CellMapper mapper)
	{
		super(cell, graph, mapper);
		this.cell = (DeviceCell)cell;
	}

	public CellViewRenderer getRenderer()
	{
		return renderer;
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

	public class DeviceSizeHandle extends SizeHandle
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

			Rectangle bounds = computeBounds(event);

			if (!bounds.equals(_bounds))
			{
				double u = (double)GraphConstants.PERCENT;
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

										Point newpos = new Point(pos.x, (int)(u * ( (double)(rect.y + 4 - bounds.y) / (double)bounds.height)));
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
				_bounds = bounds;
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

