/**
 * $Id: PhysicalLinkState.java,v 1.2 2005/01/17 15:05:24 bob Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.map;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.util.HashCodeGenerator;

import java.util.LinkedList;
import java.util.List;

/**
 * состояние линии 
 * 
 * 
 * 
 * @version $Revision: 1.2 $, $Date: 2005/01/17 15:05:24 $
 * @module
 * @author $Author: bob $
 * @see
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
