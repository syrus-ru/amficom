/**
 * $Id: TopologicalNodeState.java,v 1.2 2005/01/17 15:05:24 bob Exp $
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
 * @version $Revision: 1.2 $, $Date: 2005/01/17 15:05:24 $
 * @module map_v1
 * @author $Author: bob $
 */
public final class TopologicalNodeState extends NodeState
{
	public boolean active;
	public Identifier physicalLinkId;
	
	public TopologicalNodeState(TopologicalNode topologicalNode) {
		super(topologicalNode);
		this.active = topologicalNode.isActive();
		this.physicalLinkId = topologicalNode.getPhysicalLink().getId();
	}

	public boolean equals(Object object){
		TopologicalNodeState mpnes = (TopologicalNodeState)object;
		return super.equals(object)
			&& this.physicalLinkId.equals(mpnes.physicalLinkId)
			&& this.active == mpnes.active;
	}
	
	public int hashCode() {
		HashCodeGenerator codeGenerator = new HashCodeGenerator();
		codeGenerator.addInt(super.hashCode());
		codeGenerator.addObject(this.physicalLinkId);
		codeGenerator.addBoolean(this.active);
		return codeGenerator.getResult();
	}
}
