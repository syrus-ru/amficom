/**
 * $Id: PhysicalLinkState.java,v 1.4 2005/05/18 11:48:20 bass Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.map;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.util.HashCodeGenerator;

import java.util.LinkedList;
import java.util.List;

/**
 * ��������� �����
 *
 *
 *
 * @author $Author: bass $
 * @version $Revision: 1.4 $, $Date: 2005/05/18 11:48:20 $
 * @module map_v1
 */
public class PhysicalLinkState extends MapElementState
{
	public String name;

	public AbstractNode startNode;
	public AbstractNode endNode;
	
	public List nodeLinks = new LinkedList();
	public Identifier mapProtoId;
	public String description;

	public PhysicalLinkState(PhysicalLink physicalLink)
	{
		super();
		this.name = physicalLink.getName();
		this.startNode = physicalLink.getStartNode();
		this.endNode = physicalLink.getEndNode();

		this.nodeLinks.addAll(physicalLink.getNodeLinks());
		this.mapProtoId = physicalLink.getType().getId();
		this.description = physicalLink.getDescription();
	}

	public boolean equals(Object object){
		PhysicalLinkState physicalLinkState = (PhysicalLinkState)object;
		return (this.name.equals(physicalLinkState.name)
			&& this.startNode.equals(physicalLinkState.startNode)
			&& this.endNode.equals(physicalLinkState.endNode)
			&& this.description.equals(physicalLinkState.description)
			&& this.mapProtoId.equals(physicalLinkState.mapProtoId)
			&& this.nodeLinks.equals(physicalLinkState.nodeLinks));
	}
	
	public int hashCode() {
		HashCodeGenerator codeGenerator = new HashCodeGenerator();
		codeGenerator.addObject(this.name);
		codeGenerator.addObject(this.startNode);
		codeGenerator.addObject(this.endNode);
		codeGenerator.addObjectArray(this.nodeLinks.toArray());
		codeGenerator.addObject(this.mapProtoId);
		codeGenerator.addObject(this.description);
		return codeGenerator.getResult();
	}
}
