/*-
 * $Id: TopologicalNodeState.java,v 1.6 2005/08/24 15:00:28 bass Exp $
 *
 * Copyright ї 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.util.HashCodeGenerator;

/**
 * состояние топологического узла
 *
 *
 *
 * @version $Revision: 1.6 $, $Date: 2005/08/24 15:00:28 $
 * @module map
 * @author $Author: bass $
 */
public final class TopologicalNodeState extends NodeState {
	public boolean active;
	public Identifier physicalLinkId;

	public TopologicalNodeState(final TopologicalNode topologicalNode) {
		super(topologicalNode);
		this.active = topologicalNode.isActive();
		this.physicalLinkId = topologicalNode.getPhysicalLink().getId();
	}

	@Override
	public boolean equals(final Object object) {
		final TopologicalNodeState mpnes = (TopologicalNodeState) object;
		return super.equals(object) && this.physicalLinkId.equals(mpnes.physicalLinkId) && this.active == mpnes.active;
	}

	@Override
	public int hashCode() {
		final HashCodeGenerator codeGenerator = new HashCodeGenerator();
		codeGenerator.addInt(super.hashCode());
		codeGenerator.addObject(this.physicalLinkId);
		codeGenerator.addBoolean(this.active);
		return codeGenerator.getResult();
	}
}
