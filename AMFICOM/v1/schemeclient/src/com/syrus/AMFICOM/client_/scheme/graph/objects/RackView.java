/*-
 * $Id: RackView.java,v 1.3 2006/03/17 10:29:10 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph.objects;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.jgraph.JGraph;
import com.jgraph.graph.CellMapper;
import com.jgraph.graph.CellView;
import com.jgraph.graph.CellViewRenderer;
import com.jgraph.graph.GraphConstants;
import com.jgraph.graph.VertexRenderer;
import com.jgraph.graph.VertexView;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeGraph;
import com.syrus.AMFICOM.client_.scheme.graph.actions.GraphActions;

public class RackView extends VertexView {
	private static final long serialVersionUID = 3763093055304970547L;
	static final Font FONT = new Font("Dialog", Font.PLAIN, 12);
	static FontMetrics fm;
	
	protected Rectangle _bounds;
	private static VertexRenderer grouprenderer = new GroupVertexRenderer();

	public RackView(Object cell, JGraph graph, CellMapper mapper) {
		super(cell, graph, mapper);
		fm = graph.getFontMetrics(FONT);
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
		private static final String EMPTY = ""; //$NON-NLS-1$
		static String name = EMPTY;
		
		public GroupVertexRenderer() {
			super();
			// show bounds
//			super.hideGroups = false;
		}
		
		@Override
		public Component getRendererComponent(JGraph graph1, CellView view1, boolean sel, boolean focus, boolean preview1) {
			Object cell = view1.getCell();
			if (SchemeGraph.isShowTitles() && cell instanceof Rack) {
				Rack group = (Rack)cell;
				final Object userObject = group.getUserObject();
				if (!(userObject instanceof String) || (((String)userObject).equals(""))) {
					group.setUserObject(group.getSchemeElement().getLabel());
					name = (String)group.getUserObject();
				} else {
					name = (String)userObject;
				}
			} else {
				name = EMPTY;
			} 
			return super.getRendererComponent(graph1, view1, sel, focus, preview1);
		}

		
		@Override
		protected void paintSelectionBorder(Graphics g) {
			if (SchemeGraph.isShowTitles()) {
				setFont(FONT);
				int textHeight = fm.getHeight();
				g.drawString(name, 1, textHeight);
			}
			
			((Graphics2D) g).setStroke(new BasicStroke(1));
			g.setColor(Color.LIGHT_GRAY);
			Dimension d = getSize();
			g.drawRect(0, 0, d.width - 1, d.height - 1);
			((Graphics2D) g).setStroke(GraphConstants.SELECTION_STROKE);
			if (this.childrenSelected)
				g.setColor(this.graph.getGridColor());
			//		else if (hasFocus && selected)
			//			g.setColor(graph.getLockedHandleColor());
			else if (this.selected)
				g.setColor(this.graph.getHighlightColor());
			if (this.childrenSelected || this.selected) {
				g.drawRect(0, 0, d.width - 1, d.height - 1);
			} else {
				
			}
		}
	}
}

