/*
 * $Id: LinkView.java,v 1.6 2006/02/15 12:18:11 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph.objects;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Map;

import com.jgraph.JGraph;
import com.jgraph.graph.CellHandle;
import com.jgraph.graph.CellMapper;
import com.jgraph.graph.EdgeView;
import com.jgraph.graph.GraphConstants;
import com.jgraph.graph.GraphContext;
import com.jgraph.graph.Port;
import com.jgraph.graph.PortView;

/**
 * @author $Author: stas $
 * @version $Revision: 1.6 $, $Date: 2006/02/15 12:18:11 $
 * @module schemeclient
 */

public class LinkView extends EdgeView {
	private static final long serialVersionUID = 3257562906278114617L;

	public LinkView(Object cell, JGraph graph, CellMapper mapper) {
		super(cell, graph, mapper);
	}

	@Override
	public CellHandle getHandle(GraphContext context) {
		return new ShemeEdgeHandle(this, context);
	}

	public class ShemeEdgeHandle extends EdgeView.EdgeHandle {
		private static final long serialVersionUID = 3760567502849061174L;

		public ShemeEdgeHandle(EdgeView edge, GraphContext ctx) {
			super(edge, ctx);
		}

		// Update and paint control points
		@Override
		public void paint(Graphics g) {
			invalidate();
			for (int i = 0; i < this.r.length; i++) {
				if (this.isEdgeConnectable)
					g.setColor(this.graph.getHandleColor());
				else
					g.setColor(this.graph.getLockedHandleColor());
				g.fill3DRect(this.r[i].x, this.r[i].y, this.r[i].width, this.r[i].height, true);
				PortView port = null;
				if (i == 0 && this.edge.getSource() != null)
					port = this.edge.getSource();
				else if (i == this.r.length - 1 && this.edge.getTarget() != null)
					port = this.edge.getTarget();
				if (port != null) {
					g.setColor(this.graph.getLockedHandleColor());
					Point tmp = GraphConstants.getOffset(port.getAllAttributes());
					if (tmp != null) {
						g.drawLine(this.r[i].x + 1, this.r[i].y + 1, this.r[i].x + this.r[i].width - 3, this.r[i].y
								+ this.r[i].height - 3);
						g.drawLine(this.r[i].x + 1, this.r[i].y + this.r[i].height - 3, this.r[i].x
								+ this.r[i].width - 3, this.r[i].y + 1);
					} else
						g.drawRect(this.r[i].x + 2, this.r[i].y + 2, this.r[i].width - 5, this.r[i].height - 5);
				}
			}
		}

		@Override
		protected void paintPort(Graphics g, PortView p) {
			Port p1 = (Port) p.getCell();
			Map map = getModel().getAttributes(p1);
			if (map != null)
				if (!GraphConstants.isConnectable(map))
					return;

			boolean offset = (GraphConstants.getOffset(p.getAllAttributes()) != null);
			Rectangle rect = (offset) ? p.getBounds() : p.getParentView().getBounds();
			rect = this.graph.toScreen(new Rectangle(rect));
			int s = 3;
			rect.translate(-s, -s);
			rect.setSize(rect.width + 2 * s, rect.height + 2 * s);
			this.graph.getUI().paintCell(g, p, rect, true);
		}
	}
}
