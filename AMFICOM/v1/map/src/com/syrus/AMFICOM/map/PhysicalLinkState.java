/**
 * $Id: PhysicalLinkState.java,v 1.1 2004/12/20 12:36:01 krupenn Exp $
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
import java.util.LinkedList;
import java.util.List;

/**
 * состояние линии 
 * 
 * 
 * 
 * @version $Revision: 1.1 $, $Date: 2004/12/20 12:36:01 $
 * @module
 * @author $Author: krupenn $
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

	public PhysicalLinkState(PhysicalLink mple)
	{
		super();
		name = mple.getName();
		startNode = mple.getStartNode();
		endNode = mple.getEndNode();

		nodeLinks.addAll(mple.getNodeLinks());
		mapProtoId = mple.getType().getId();
		description = mple.getDescription();
	}

	public boolean equals(Object obj)
	{
		PhysicalLinkState mples = (PhysicalLinkState)obj;
		return (this.name.equals(mples.name)
			&& this.startNode.equals(mples.startNode)
			&& this.endNode.equals(mples.endNode)
			&& this.description.equals(mples.description)
			&& this.mapProtoId.equals(mples.mapProtoId)
			&& this.nodeLinks.equals(mples.nodeLinks));
	}
}
