/*
 * $Id: DefaultPortView.java,v 1.2 2005/07/11 12:31:38 stas Exp $
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

/**
 * @author $Author: stas $
 * @version $Revision: 1.2 $, $Date: 2005/07/11 12:31:38 $
 * @module schemeclient_v1
 */

public class DefaultPortView extends EllipseView {
	private static SchemeEllipseRenderer schemerenderer = new SchemeEllipseRenderer();

	public DefaultPortView(Object cell, JGraph jgraph, CellMapper mapper) {
		super(cell, jgraph, mapper);
	}

	public CellViewRenderer getRenderer() {
		return schemerenderer;
	}

	static class SchemeEllipseRenderer extends EllipseView.EllipseRenderer {
		protected void paintSelectionBorder(Graphics g) {
			((Graphics2D) g).setStroke(GraphConstants.SELECTION_STROKE);
			if (childrenSelected)
				g.setColor(graph.getGridColor());
			//      else if (hasFocus && selected)
			//       g.setColor(graph.getLockedHandleColor());
			else if (selected)
				g.setColor(graph.getHighlightColor());
			if (childrenSelected || selected) {
				Dimension d = getSize();
				g.drawRect(0, 0, d.width - 1, d.height - 1);
			}
		}
	}
}