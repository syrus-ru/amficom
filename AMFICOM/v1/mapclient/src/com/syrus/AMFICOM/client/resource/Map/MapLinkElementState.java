/**
 * $Id: MapLinkElementState.java,v 1.2 2004/09/21 14:56:16 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Resource.Map;

import java.util.HashMap;

/**
 * Состояние линейного элемента карты 
 * 
 * 
 * 
 * @version $Revision: 1.2 $, $Date: 2004/09/21 14:56:16 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class MapLinkElementState extends MapElementState
{
	String name;
	String description;

	MapNodeElement startNode;//начало
	MapNodeElement endNode;//конец

	java.util.Map attributes = new HashMap();

	public MapLinkElementState(MapLinkElement mle)
	{
		super();
		name = mle.getName();
		description = mle.getDescription();
		startNode = mle.getStartNode();
		endNode = mle.getEndNode();

		attributes.putAll(mle.attributes);
	}
}
