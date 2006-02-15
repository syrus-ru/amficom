/*-
 * $Id: SchemeVertexView.java,v 1.7 2006/02/15 12:18:11 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph.objects;

import java.awt.Rectangle;

import com.jgraph.graph.CellMapper;
import com.jgraph.graph.CellViewRenderer;
import com.jgraph.graph.GraphConstants;
import com.jgraph.graph.VertexView;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeGraph;
import com.syrus.AMFICOM.client_.scheme.graph.actions.GraphActions;

/**
 * @author $Author: stas $
 * @version $Revision: 1.7 $, $Date: 2006/02/15 12:18:11 $
 * @module schemeclient
 */

public class SchemeVertexView extends VertexView {
	private static final long serialVersionUID = 3617569397179494963L;

	public SchemeVertexView(Object v, SchemeGraph graph, CellMapper cm) {
		super(v, graph, cm);
	}

	@Override
	public CellViewRenderer getRenderer() {
		return VertexView.renderer;
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
}

