/*-
 * $Id: SchemeVertexView.java,v 1.1 2005/04/05 14:07:54 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph.objects;

import java.awt.Rectangle;

import com.jgraph.graph.*;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeGraph;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/04/05 14:07:54 $
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

