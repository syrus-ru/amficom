/**
 * $Id: TopologicalNodeState.java,v 1.1 2004/12/20 12:36:01 krupenn Exp $
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
 * состояние топологического узла 
 * 
 * 
 * 
 * @version $Revision: 1.1 $, $Date: 2004/12/20 12:36:01 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public final class TopologicalNodeState extends NodeState
{
	public boolean active;
	public Identifier physicalLinkId;
	
	public TopologicalNodeState(TopologicalNode mpne)
	{
		super(mpne);

		active = mpne.isActive();
		physicalLinkId = mpne.getPhysicalLink().getId();
	}

	public boolean equals(Object obj)
	{
		TopologicalNodeState mpnes = (TopologicalNodeState)obj;
		return super.equals(obj)
			&& this.physicalLinkId.equals(mpnes.physicalLinkId)
			&& this.active == mpnes.active;
	}
}
