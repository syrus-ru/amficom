/*
 * $Id: SchemeEllipseView.java,v 1.2 2006/02/15 12:18:11 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph.objects;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import com.jgraph.JGraph;
import com.jgraph.graph.CellMapper;
import com.jgraph.graph.CellViewRenderer;
import com.jgraph.graph.GraphConstants;
import com.jgraph.pad.EllipseView;
import com.syrus.AMFICOM.client_.scheme.graph.actions.GraphActions;

/**
 * @author $Author: stas $
 * @version $Revision: 1.2 $, $Date: 2006/02/15 12:18:11 $
 * @module schemeclient
 */

public class SchemeEllipseView extends EllipseView {
	private static final long serialVersionUID = 3977014041950565430L;

	private static SchemeEllipseRenderer schemerenderer = new SchemeEllipseRenderer();

	public SchemeEllipseView(Object cell, JGraph jgraph, CellMapper mapper) {
		super(cell, jgraph, mapper);
	}

	@Override
	public CellViewRenderer getRenderer() {
		return schemerenderer;
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
	
	static class SchemeEllipseRenderer extends EllipseView.EllipseRenderer {
		private static final long serialVersionUID = -5509316073878497199L;

		@Override
		protected void paintSelectionBorder(Graphics g) {
			((Graphics2D) g).setStroke(GraphConstants.SELECTION_STROKE);
			if (this.childrenSelected)
				g.setColor(this.graph.getGridColor());
			//      else if (hasFocus && selected)
			//       g.setColor(graph.getLockedHandleColor());
			else if (this.selected)
				g.setColor(this.graph.getHighlightColor());
			if (this.childrenSelected || this.selected) {
				Dimension d = getSize();
				g.drawRect(0, 0, d.width - 1, d.height - 1);
			}
		}
	}
}