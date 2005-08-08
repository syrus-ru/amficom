/*
 * $Id: DefaultPortView.java,v 1.5 2005/08/08 11:58:07 arseniy Exp $
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
 * @author $Author: arseniy $
 * @version $Revision: 1.5 $, $Date: 2005/08/08 11:58:07 $
 * @module schemeclient
 */

public class DefaultPortView extends EllipseView {
	private static final long serialVersionUID = 3977014041950565430L;

	private static SchemeEllipseRenderer schemerenderer = new SchemeEllipseRenderer();

	public DefaultPortView(Object cell, JGraph jgraph, CellMapper mapper) {
		super(cell, jgraph, mapper);
	}

	public CellViewRenderer getRenderer() {
		return schemerenderer;
	}

	static class SchemeEllipseRenderer extends EllipseView.EllipseRenderer {
		private static final long serialVersionUID = -5509316073878497199L;

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