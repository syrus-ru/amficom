/**
 * $Id: TopologicalNodeState.java,v 1.3 2005/05/18 11:48:20 bass Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 */

package com.syrus.AMFICOM.map;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.util.HashCodeGenerator;

/**
 * ��������� ��������������� ����
 *
 *
 *
 * @version $Revision: 1.3 $, $Date: 2005/05/18 11:48:20 $
 * @module map_v1
 * @author $Author: bass $
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
