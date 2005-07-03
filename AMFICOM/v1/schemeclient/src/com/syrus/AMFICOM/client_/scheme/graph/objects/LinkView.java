/*
 * $Id: LinkView.java,v 1.1 2005/04/05 14:07:54 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph.objects;

import java.awt.*;
import java.util.Map;

import com.jgraph.JGraph;
import com.jgraph.graph.*;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/04/05 14:07:54 $
 * @module schemeclient_v1
 */

public class LinkView extends EdgeView
{
	public LinkView(Object cell, JGraph graph, CellMapper mapper, int i) {
		super(cell, graph, mapper);
	}

	public CellHandle getHandle(GraphContext context) {
		return new ShemeEdgeHandle(this, context);
	}

	public class ShemeEdgeHandle extends EdgeView.EdgeHandle {
		public ShemeEdgeHandle(EdgeView edge, GraphContext ctx) {
			super(edge, ctx);
		}

		// Update and paint control points
		public void paint(Graphics g) {
			invalidate();
			for (int i = 0; i < r.length; i++) {
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
				if (port != null) {
					g.setColor(graph.getLockedHandleColor());
					Point tmp = GraphConstants.getOffset(port.getAllAttributes());
					if (tmp != null) {
						g.drawLine(r[i].x + 1, r[i].y + 1, r[i].x + r[i].width - 3, r[i].y
								+ r[i].height - 3);
						g.drawLine(r[i].x + 1, r[i].y + r[i].height - 3, r[i].x
								+ r[i].width - 3, r[i].y + 1);
					} else
						g.drawRect(r[i].x + 2, r[i].y + 2, r[i].width - 5, r[i].height - 5);
				}
			}
		}

		protected void paintPort(Graphics g, PortView p) {
			Port p1 = (Port) p.getCell();
			Map map = getModel().getAttributes(p1);
			if (map != null)
				if (!GraphConstants.isConnectable(map))
					return;

			boolean offset = (GraphConstants.getOffset(p.getAllAttributes()) != null);
			Rectangle rect = (offset) ? p.getBounds() : p.getParentView().getBounds();
			rect = graph.toScreen(new Rectangle(rect));
			int s = 3;
			rect.translate(-s, -s);
			rect.setSize(rect.width + 2 * s, rect.height + 2 * s);
			graph.getUI().paintCell(g, p, rect, true);
		}
	}
}
