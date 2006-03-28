/*-
 * $Id: SchemeVertexView.java,v 1.9 2006/03/28 09:52:16 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph.objects;

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
import com.syrus.AMFICOM.client_.scheme.graph.Constants;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeGraph;
import com.syrus.AMFICOM.client_.scheme.graph.actions.GraphActions;
import com.syrus.AMFICOM.client_.scheme.graph.actions.SchemeActions;

/**
 * @author $Author: stas $
 * @version $Revision: 1.9 $, $Date: 2006/03/28 09:52:16 $
 * @module schemeclient
 */

public class SchemeVertexView extends VertexView {
	private static final long serialVersionUID = 3617569397179494963L;
	private static VertexRenderer schemerenderer = new SchemeVertexRenderer();
	static final Font FONT = new Font("Dialog", Font.PLAIN, 12);
	static FontMetrics fm;
		
	public SchemeVertexView(Object v, JGraph graph, CellMapper cm) {
		super(v, graph, cm);
		fm = graph.getFontMetrics(FONT);
	}

	@Override
	public CellViewRenderer getRenderer() {
		return schemerenderer;
	}

	@Override
	public Rectangle getBounds() {
		final Rectangle bounds1 = super.getBounds();
		if (super.cell instanceof DeviceGroup) {
			int deltaX = 0;
			int textHeight = 0;
			if (SchemeGraph.isShowTitles()) {
				String name = (String)((DeviceGroup)super.cell).getUserObject();
				if (name.length() != 0) {
					int textWidth = fm.stringWidth(name);
					if (textWidth > bounds1.width) {
						deltaX = textWidth - bounds1.width;
					}
					textHeight = fm.getHeight();
				}
			}
			return new Rectangle(bounds1.x - 1 - deltaX / 2, bounds1.y - 1 - textHeight, 
					bounds1.width + 2 + deltaX, bounds1.height + 2 + textHeight);
		}
		return bounds1;
	}
	
	public Rectangle getPureBounds() {
		return this.bounds;
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
	
	public static class SchemeVertexRenderer extends VertexRenderer {
		private static final long serialVersionUID = 3257003246202466869L;
		private static final String EMPTY = ""; //$NON-NLS-1$
		static String name = EMPTY;
		
		@Override
		public Component getRendererComponent(JGraph graph1, CellView view1, boolean sel, boolean focus, boolean preview1) {
			Object cell = view1.getCell();
			if (SchemeGraph.isShowTitles() && cell instanceof DeviceGroup) {
				DeviceGroup group = (DeviceGroup)cell;
				final Object userObject = group.getUserObject();
				if (!(userObject instanceof String) || (((String)userObject).equals(""))) {
					SchemeActions.fixImage((SchemeGraph)super.graph, group);
				} else {
					SchemeActions.fixText(group);
				}
				name = (String)userObject;
			} else {
				name = EMPTY;
			} 
			return super.getRendererComponent(graph1, view1, sel, focus, preview1);
		}

		@Override
		protected void paintSelectionBorder(Graphics g) {
			Dimension d = getSize();
			if (SchemeGraph.isShowTitles()) {
				setFont(FONT);
				int textWidth = fm.stringWidth(name);
				int textHeight = fm.getHeight();
				g.drawString(name, (d.width - textWidth) / 2, textHeight);
			}
			
			((Graphics2D) g).setStroke(Constants.PRIMARY_SELECTION_STROKE);
			if (this.childrenSelected) {
				((Graphics2D) g).setStroke(Constants.SECONDARY_SELECTION_STROKE);
//				g.setColor(this.graph.getGridColor());
				g.setColor(this.graph.getHighlightColor());
			}
				
			//		else if (hasFocus && selected)
			//			g.setColor(graph.getLockedHandleColor());
			else if (this.selected)
				g.setColor(this.graph.getHighlightColor());
			if (this.childrenSelected || this.selected) {
				g.drawRect(0, 0, d.width - 1, d.height - 1);
			}
		}
	}
}

