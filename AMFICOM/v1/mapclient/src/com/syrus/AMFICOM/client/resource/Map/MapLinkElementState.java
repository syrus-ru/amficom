/**
 * $Id: MapLinkElementState.java,v 1.4 2004/10/26 13:25:36 krupenn Exp $
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
 * @version $Revision: 1.4 $, $Date: 2004/10/26 13:25:36 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class MapLinkElementState extends MapElementState
{
	String name;
	String description;

	MapNodeElement startNode;
	MapNodeElement endNode;

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
	
	public boolean equals(Object obj)
	{
		MapLinkElementState mles = (MapLinkElementState )obj;
		return (this.name.equals(mles.name)
			&& this.description.equals(mles.description)
			&& this.startNode.equals(mles.startNode)
			&& this.endNode.equals(mles.endNode));
	}

}
