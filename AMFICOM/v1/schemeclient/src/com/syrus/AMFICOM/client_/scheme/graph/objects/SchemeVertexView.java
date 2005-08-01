/*-
 * $Id: SchemeVertexView.java,v 1.3 2005/08/01 07:52:28 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph.objects;

import java.awt.Rectangle;

import com.jgraph.graph.CellMapper;
import com.jgraph.graph.CellViewRenderer;
import com.jgraph.graph.VertexView;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeGraph;

/**
 * @author $Author: stas $
 * @version $Revision: 1.3 $, $Date: 2005/08/01 07:52:28 $
 * @module schemeclient_v1
 */

public class SchemeVertexView extends VertexView {
	private static final long serialVersionUID = 3617569397179494963L;

	public SchemeVertexView(Object v, SchemeGraph graph, CellMapper cm) {
		super(v, graph, cm);
	}

	public CellViewRenderer getRenderer() {
		return VertexView.renderer;
	}

	public Rectangle getPureBounds() {
		return bounds;
	}
}

