/**
 * $Id: MapPhysicalLinkElementState.java,v 1.2 2004/10/26 13:25:36 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Resource.Map;

import java.util.ArrayList;

/**
 * состояние линии 
 * 
 * 
 * 
 * @version $Revision: 1.2 $, $Date: 2004/10/26 13:25:36 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class MapPhysicalLinkElementState extends MapLinkElementState
{
	
	ArrayList nodeLinks = new ArrayList();
	String mapProtoId;

	public MapPhysicalLinkElementState(MapPhysicalLinkElement mple)
	{
		super(mple);

		nodeLinks.addAll(mple.nodeLinks);
		mapProtoId = mple.mapProtoId;
	}

	public boolean equals(Object obj)
	{
		MapPhysicalLinkElementState mples = (MapPhysicalLinkElementState )obj;
		return super.equals(obj)
			&& this.mapProtoId.equals(mples.mapProtoId)
			&& this.nodeLinks.equals(mples.nodeLinks);
	}
}
