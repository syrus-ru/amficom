/**
 * $Id: MapNodeLinkElementState.java,v 1.1 2004/09/13 12:02:01 krupenn Exp $
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
 * @version $Revision: 1.1 $, $Date: 2004/09/13 12:02:01 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class MapNodeLinkElementState extends MapLinkElementState
{
	String physicalLinkId;

	public MapNodeLinkElementState(MapNodeLinkElement mnle)
	{
		super(mnle);
		
		physicalLinkId = mnle.physicalLinkId;
	}
}
