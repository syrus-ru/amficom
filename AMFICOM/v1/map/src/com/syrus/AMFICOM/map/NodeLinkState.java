/*-
 * $Id: NodeLinkState.java,v 1.6 2005/08/24 15:00:28 bass Exp $
 *
 * Copyright ї 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.util.HashCodeGenerator;

/**
 * состояние фрагмента линии
 *
 *
 *
 * @version $Revision: 1.6 $, $Date: 2005/08/24 15:00:28 $
 * @module map
 * @author $Author: bass $
 */
public final class NodeLinkState extends MapElementState {
	public String name;

	public AbstractNode startNode;
	public AbstractNode endNode;

	public Identifier physicalLinkId;

	public NodeLinkState(NodeLink nodeLink) {
		super();
		this.name = nodeLink.getName();
		this.startNode = nodeLink.getStartNode();
		this.endNode = nodeLink.getEndNode();

		this.physicalLinkId = nodeLink.getPhysicalLink().getId();
	}

	@Override
	public boolean equals(final Object object) {
		NodeLinkState mnles = (NodeLinkState) object;
		return (this.name.equals(mnles.name) && this.startNode.equals(mnles.startNode) && this.endNode.equals(mnles.endNode) && this.physicalLinkId.equals(mnles.physicalLinkId));
	}

	@Override
	public int hashCode() {
		final HashCodeGenerator codeGenerator = new HashCodeGenerator();
		codeGenerator.addInt(super.hashCode());
		codeGenerator.addObject(this.name);
		codeGenerator.addObject(this.startNode);
		codeGenerator.addObject(this.endNode);
		codeGenerator.addObject(this.physicalLinkId);

		return codeGenerator.getResult();
	}
}
