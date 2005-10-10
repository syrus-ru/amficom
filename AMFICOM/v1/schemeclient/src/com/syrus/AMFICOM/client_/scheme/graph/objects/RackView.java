/*-
 * $Id: RackView.java,v 1.1 2005/10/10 11:08:13 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph.objects;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.jgraph.JGraph;
import com.jgraph.graph.CellMapper;
import com.jgraph.graph.CellViewRenderer;
import com.jgraph.graph.GraphConstants;
import com.jgraph.graph.VertexRenderer;
import com.jgraph.graph.VertexView;
import com.syrus.AMFICOM.client_.scheme.graph.actions.GraphActions;

public class RackView extends VertexView {
	private static final long serialVersionUID = 3763093055304970547L;

	protected Rectangle _bounds;
	private static VertexRenderer grouprenderer = new GroupVertexRenderer();

	public RackView(Object cell, JGraph graph, CellMapper mapper) {
		super(cell, graph, mapper);
	}

	@Override
	public CellViewRenderer getRenderer() {
		return grouprenderer;
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

	public static class GroupVertexRenderer extends VertexRenderer {
		private static final long serialVersionUID = 3257003246202466869L;

		@Override
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
			} else {
				g.setColor(Color.BLUE);
				Dimension d = getSize();
				g.drawRect(0, 0, d.width - 1, d.height - 1);
			}
		}
	}
}

