/**
 * $Id: TopologicalNodeState.java,v 1.5 2005/08/08 11:35:11 arseniy Exp $
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
 * состояние топологического узла
 *
 *
 *
 * @version $Revision: 1.5 $, $Date: 2005/08/08 11:35:11 $
 * @module map
 * @author $Author: arseniy $
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
