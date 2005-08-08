/*
 * $Id: DeviceView.java,v 1.5 2005/08/08 11:58:07 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph.objects;

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

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.5 $, $Date: 2005/08/08 11:58:07 $
 * @module schemeclient
 */

public class DeviceView extends VertexView {
	private static final long serialVersionUID = 3763093055304970547L;

	protected DeviceCell cell;
	protected Rectangle _bounds;
	private static VertexRenderer schemerenderer = new SchemeVertexRenderer();

	public DeviceView(Object cell, JGraph graph, CellMapper mapper) {
		super(cell, graph, mapper);
		this.cell = (DeviceCell) cell;
	}

	public CellViewRenderer getRenderer() {
		return schemerenderer;
	}

	public CellHandle getHandle(GraphContext context) {
		if (GraphConstants.isSizeable(getAllAttributes())
				&& context.getGraph().isSizeable())
			return new DeviceSizeHandle(this, context);
		return null;
	}

	public Map setAttributes(Map map) {
		Map undo = super.setAttributes(map);
		return undo;
	}

	public class DeviceSizeHandle extends VertexView.SizeHandle {
		private static final long serialVersionUID = 3257004337124685618L;

		public DeviceSizeHandle(DeviceView vertexview, GraphContext ctx) {
			super(vertexview, ctx);
		}

		public void mousePressed(MouseEvent event) {
			super.mousePressed(event);
			DeviceView.this._bounds = this.initialBounds;
		}

		public void mouseDragged(MouseEvent event) {
			super.mouseDragged(event);

			Rectangle bounds1 = computeBounds(event);

			if (!bounds1.equals(DeviceView.this._bounds)) {
				double u = GraphConstants.PERCENT;
				java.util.List list = DeviceView.this.cell.getChildren();
				Iterator iterator = list.iterator();
				while (iterator.hasNext()) {
					Port port = (Port) iterator.next();
					Point pos = GraphConstants.getOffset(port.getAttributes());
					if (pos != null) {
						if (port.edges().hasNext()) {
							Edge edge = (Edge) port.edges().next();
							if (edge != null) {
								DefaultPort targetport = (DefaultPort) edge.getTarget();
								if (targetport != null) {
									DefaultGraphCell visualPort = (DefaultGraphCell) targetport.getParent();
									if (visualPort != null) {
										Rectangle rect = GraphConstants.getBounds(visualPort.getAttributes());

										Point newpos = new Point(pos.x,
												(int) (u * ((double) (rect.y + 4 - bounds1.y) / (double) bounds1.height)));
										GraphConstants.setOffset(port.getAttributes(), newpos);
									}
								}
							}
						}
					}
				}
				DeviceView.this._bounds = bounds1;
			}
		}
	}

	public static class SchemeVertexRenderer extends VertexRenderer {
		private static final long serialVersionUID = 3257003246202466869L;

		protected void paintSelectionBorder(Graphics g) {
			((Graphics2D) g).setStroke(GraphConstants.SELECTION_STROKE);
			if (this.childrenSelected)
				g.setColor(this.graph.getGridColor());
			//		else if (hasFocus && selected)
			//			g.setColor(graph.getLockedHandleColor());
			else if (this.selected)
				g.setColor(this.graph.getHighlightColor());
			if (this.childrenSelected || this.selected) {
				Dimension d = getSize();
				g.drawRect(0, 0, d.width - 1, d.height - 1);
			}
		}
	}
}
