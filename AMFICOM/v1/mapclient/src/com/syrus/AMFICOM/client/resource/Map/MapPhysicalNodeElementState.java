/**
 * $Id: MapPhysicalNodeElementState.java,v 1.3 2004/10/26 13:25:36 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Resource.Map;

/**
 * состояние топологического узла 
 * 
 * 
 * 
 * @version $Revision: 1.3 $, $Date: 2004/10/26 13:25:36 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public final class MapPhysicalNodeElementState extends MapNodeElementState
{
	boolean active;
	String physicalLinkId;
	
	public MapPhysicalNodeElementState(MapPhysicalNodeElement mpne)
	{
		super(mpne);

		active = mpne.isActive();
		physicalLinkId = mpne.physicalLinkId;
	}

	public boolean equals(Object obj)
	{
		MapPhysicalNodeElementState mpnes = (MapPhysicalNodeElementState )obj;
		return super.equals(obj)
			&& this.physicalLinkId.equals(mpnes.physicalLinkId)
			&& this.active == mpnes.active;
	}
}
