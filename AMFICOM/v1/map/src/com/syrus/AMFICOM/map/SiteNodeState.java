/**
 * $Id: SiteNodeState.java,v 1.1 2004/12/20 12:36:01 krupenn Exp $
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
 * состояние узла
 * 
 * 
 * 
 * @version $Revision: 1.1 $, $Date: 2004/12/20 12:36:01 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class SiteNodeState extends NodeState
{
	public Identifier mapProtoId;
	
	public SiteNodeState(SiteNode msne)
	{
		super(msne);

		mapProtoId = msne.getType().getId();
	}

	public boolean equals(Object obj)
	{
		SiteNodeState msnes = (SiteNodeState)obj;
		return super.equals(obj)
			&& this.mapProtoId.equals(msnes.mapProtoId);
	}
}
