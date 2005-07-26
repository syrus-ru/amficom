/**
 * $Id: NodeLinkState.java,v 1.4 2005/07/26 10:20:09 arseniy Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 */

package com.syrus.AMFICOM.map;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.util.HashCodeGenerator;

/**
 * состояние фрагмента линии
 *
 *
 *
 * @version $Revision: 1.4 $, $Date: 2005/07/26 10:20:09 $
 * @module map_v1
 * @author $Author: arseniy $
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
