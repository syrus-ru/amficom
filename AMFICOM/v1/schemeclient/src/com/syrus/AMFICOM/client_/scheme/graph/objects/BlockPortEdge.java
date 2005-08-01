/*-
 * $Id: BlockPortEdge.java,v 1.2 2005/08/01 07:52:28 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph.objects;

import com.jgraph.graph.DefaultEdge;

/**
 * @author $Author: stas $
 * @version $Revision: 1.2 $, $Date: 2005/08/01 07:52:28 $
 * @module schemeclient_v1
 */

public class BlockPortEdge extends DefaultEdge {
	private static final long serialVersionUID = 3761413022979141689L;

	// just used for identification of BlockPortCell connection
	public BlockPortEdge() {
		super();
	}
	
	public BlockPortEdge(Object object) {
		super(object);
	}
}
