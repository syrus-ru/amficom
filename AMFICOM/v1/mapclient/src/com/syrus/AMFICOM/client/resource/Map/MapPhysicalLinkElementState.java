/**
 * $Id: MapPhysicalLinkElementState.java,v 1.4 2004/12/07 17:02:03 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Resource.Map;

import java.util.LinkedList;
import java.util.List;

/**
 * состояние линии 
 * 
 * 
 * 
 * @version $Revision: 1.4 $, $Date: 2004/12/07 17:02:03 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class MapPhysicalLinkElementState extends MapLinkElementState
{
	
	List nodeLinks = new LinkedList();
	String mapProtoId;

	public MapPhysicalLinkElementState(MapPhysicalLinkElement mple)
	{
		super(mple);

		nodeLinks.addAll(mple.getNodeLinks());
		mapProtoId = mple.getMapProtoId();
	}

	public boolean equals(Object obj)
	{
		MapPhysicalLinkElementState mples = (MapPhysicalLinkElementState )obj;
		return super.equals(obj)
			&& this.mapProtoId.equals(mples.mapProtoId)
			&& this.nodeLinks.equals(mples.nodeLinks);
	}
}
