/**
 * $Id: MapUnboundLinkElement.java,v 1.9 2004/12/08 16:20:22 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Resource.MapView;

import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.Map.MapLinkProtoElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;

import java.io.Serializable;

/**
 * элемент линии 
 * 
 * 
 * 
 * @version $Revision: 1.9 $, $Date: 2004/12/08 16:20:22 $
 * @module
 * @author $Author: krupenn $
 * @see
 */

//MapPhysicalPathElement
public class MapUnboundLinkElement extends MapPhysicalLinkElement implements Serializable
{
	protected MapCablePathElement cablePath;
	
	public MapUnboundLinkElement(
			String id,
			MapNodeElement stNode, 
			MapNodeElement eNode, 
			Map map,
			MapLinkProtoElement proto)
	{
		super(id, stNode, eNode, map, proto);
	}

	private static final String PROPERTY_PANE_CLASS_NAME = 
			"";

	public static String getPropertyPaneClassName()
	{
		return PROPERTY_PANE_CLASS_NAME;
	}

	public void setCablePath(MapCablePathElement cablePath)
	{
		this.cablePath = cablePath;
	}


	public MapCablePathElement getCablePath()
	{
		return cablePath;
	}
}
