/*-
 * $Id: SchemeVertexView.java,v 1.5 2005/08/08 11:58:07 arseniy Exp $
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
 * @author $Author: arseniy $
 * @version $Revision: 1.5 $, $Date: 2005/08/08 11:58:07 $
 * @module schemeclient
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
		return this.bounds;
	}
}

