/**
 * $Id: MapNodeLinkElementState.java,v 1.3 2004/10/26 13:25:36 krupenn Exp $
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
 * состояние фрагмента линии 
 * 
 * 
 * 
 * @version $Revision: 1.3 $, $Date: 2004/10/26 13:25:36 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public final class MapNodeLinkElementState extends MapLinkElementState
{
	String physicalLinkId;

	public MapNodeLinkElementState(MapNodeLinkElement mnle)
	{
		super(mnle);
		
		physicalLinkId = mnle.physicalLinkId;
	}

	public boolean equals(Object obj)
	{
		MapNodeLinkElementState mnles = (MapNodeLinkElementState )obj;
		return super.equals(obj)
			&& this.physicalLinkId.equals(mnles.physicalLinkId);
	}
}
