/**
 * $Id: SiteNodeState.java,v 1.2 2005/01/17 15:05:24 bob Exp $
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
 * состояние узла
 * 
 * @version $Revision: 1.2 $, $Date: 2005/01/17 15:05:24 $
 * @module map_v1
 * @author $Author: bob $
 */
public class SiteNodeState extends NodeState
{
	public Identifier mapProtoId;
	
	public SiteNodeState(SiteNode msne)
	{
		super(msne);

		this.mapProtoId = msne.getType().getId();
	}

	public boolean equals(Object obj)
	{
		SiteNodeState msnes = (SiteNodeState)obj;
		return super.equals(obj)
			&& this.mapProtoId.equals(msnes.mapProtoId);
	}
	
	public int hashCode() {
		HashCodeGenerator codeGenerator = new HashCodeGenerator();
		codeGenerator.addInt(super.hashCode());
		codeGenerator.addObject(this.mapProtoId);
		return codeGenerator.getResult();
	}
}
