/**
 * $Id: NodeLinkState.java,v 1.1 2004/12/20 12:36:01 krupenn Exp $
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

/**
 * состояние фрагмента линии 
 * 
 * 
 * 
 * @version $Revision: 1.1 $, $Date: 2004/12/20 12:36:01 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public final class NodeLinkState extends MapElementState
{
	public String name;

	public AbstractNode startNode;
	public AbstractNode endNode;

	public Identifier physicalLinkId;

	public NodeLinkState(NodeLink mnle)
	{
		super();
		name = mnle.getName();
		startNode = mnle.getStartNode();
		endNode = mnle.getEndNode();
		
		physicalLinkId = mnle.getPhysicalLink().getId();
	}

	public boolean equals(Object obj)
	{
		NodeLinkState mnles = (NodeLinkState)obj;
		return (this.name.equals(mnles.name)
			&& this.startNode.equals(mnles.startNode)
			&& this.endNode.equals(mnles.endNode)
			&& this.physicalLinkId.equals(mnles.physicalLinkId));
	}
}
