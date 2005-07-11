/*-
 * $Id: SchemeVertexView.java,v 1.2 2005/07/11 12:31:39 stas Exp $
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
 * @version $Revision: 1.2 $, $Date: 2005/07/11 12:31:39 $
 * @module schemeclient_v1
 */

public class SchemeVertexView extends VertexView {

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

