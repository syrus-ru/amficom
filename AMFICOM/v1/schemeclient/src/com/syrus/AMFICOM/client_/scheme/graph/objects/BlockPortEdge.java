/*-
 * $Id: BlockPortEdge.java,v 1.3 2005/08/08 11:58:07 arseniy Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph.objects;

import com.jgraph.graph.DefaultEdge;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.3 $, $Date: 2005/08/08 11:58:07 $
 * @module schemeclient
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
