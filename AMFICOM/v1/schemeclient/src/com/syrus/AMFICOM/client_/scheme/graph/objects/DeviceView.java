/*
 * $Id: DeviceView.java,v 1.10 2006/06/06 12:52:08 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph.objects;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.Map;

import com.jgraph.JGraph;
import com.jgraph.graph.CellHandle;
import com.jgraph.graph.CellMapper;
import com.jgraph.graph.DefaultGraphCell;
import com.jgraph.graph.DefaultPort;
import com.jgraph.graph.Edge;
import com.jgraph.graph.GraphConstants;
import com.jgraph.graph.GraphContext;
import com.jgraph.graph.Port;
import com.jgraph.graph.VertexView;
import com.syrus.AMFICOM.client_.scheme.graph.actions.GraphActions;

/**
 * @author $Author: stas $
 * @version $Revision: 1.10 $, $Date: 2006/06/06 12:52:08 $
 * @module schemeclient
 */

public class DeviceView extends SchemeVertexView {
	private static final long serialVersionUID = 3763093055304970547L;

	protected Rectangle _bounds;

	public DeviceView(Object cell, JGraph graph, CellMapper mapper) {
		super(cell, graph, mapper);
	}

	@Override
	public CellHandle getHandle(GraphContext context) {
		if (GraphConstants.isSizeable(getAllAttributes())
				&& context.getGraph().isSizeable())
			return new DeviceSizeHandle(this, context);
		return null;
	}

	@Override
	public Map setAttributes(Map map) {
		Map undo = super.setAttributes(map);
		return undo;
	}
	
	@Override
	protected void updateAllAttributes() {
		this.allAttributes = getModel().getAttributes(this.cell);
		if (this.allAttributes != null) {
			this.allAttributes = GraphActions.cloneMap(this.allAttributes);
		} else {
			this.allAttributes = GraphConstants.createMap();
		}
		this.allAttributes.putAll(this.attributes);
	}

	public class DeviceSizeHandle extends VertexView.SizeHandle {
		private static final long serialVersionUID = 3257004337124685618L;

		public DeviceSizeHandle(DeviceView vertexview, GraphContext ctx) {
			super(vertexview, ctx);
		}

		@Override
		public void mousePressed(MouseEvent event) {
			super.mousePressed(event);
			DeviceView.this._bounds = this.initialBounds;
		}

		@Override
		public void mouseDragged(MouseEvent event) {
			super.mouseDragged(event);

			Rectangle bounds1 = computeBounds(event);

			if (!bounds1.equals(DeviceView.this._bounds)) {
				double u = GraphConstants.PERCENT;
				java.util.List list = ((DefaultGraphCell)DeviceView.this.cell).getChildren();
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
												(int) (u * ((double) (rect.y + 3 - bounds1.y) / (double) bounds1.height)));
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
}
