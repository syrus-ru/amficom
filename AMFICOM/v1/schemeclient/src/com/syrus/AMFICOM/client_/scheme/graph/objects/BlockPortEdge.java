/*-
 * $Id: BlockPortEdge.java,v 1.1 2005/07/11 12:31:38 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph.objects;

import com.jgraph.graph.DefaultEdge;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/07/11 12:31:38 $
 * @module schemeclient_v1
 */

public class BlockPortEdge extends DefaultEdge {
	// just used for identification of BlockPortCell connection
	public BlockPortEdge() {
		super();
	}
	
	public BlockPortEdge(Object object) {
		super(object);
	}
}
