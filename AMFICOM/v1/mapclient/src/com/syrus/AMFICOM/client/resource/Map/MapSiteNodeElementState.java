/**
 * $Id: MapSiteNodeElementState.java,v 1.3 2004/12/07 17:02:03 krupenn Exp $
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
 * состояние узла
 * 
 * 
 * 
 * @version $Revision: 1.3 $, $Date: 2004/12/07 17:02:03 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class MapSiteNodeElementState extends MapNodeElementState
{
	String mapProtoId;
	
	public MapSiteNodeElementState(MapSiteNodeElement msne)
	{
		super(msne);

		mapProtoId = msne.getMapProtoId();
	}

	public boolean equals(Object obj)
	{
		MapSiteNodeElementState msnes = (MapSiteNodeElementState )obj;
		return super.equals(obj)
			&& this.mapProtoId.equals(msnes.mapProtoId);
	}
}
