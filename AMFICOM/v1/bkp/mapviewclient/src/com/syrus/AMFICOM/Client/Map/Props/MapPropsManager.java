package com.syrus.AMFICOM.Client.Map.Props;

import com.syrus.AMFICOM.Client.General.UI.ObjectResourcePropertiesPane;

import com.syrus.AMFICOM.Client.Map.Props.MapCablePathPane;
import com.syrus.AMFICOM.Client.Map.Props.MapLinkPane;
import com.syrus.AMFICOM.Client.Map.Props.MapSitePane;
import com.syrus.AMFICOM.Client.Resource.Map.MapElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeProtoElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapSiteNodeElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapCablePathElement;

import java.util.HashMap;

public final class MapPropsManager 
{
	private MapPropsManager()
	{
	}

	private static java.util.Map propsMap = new HashMap();

	static
	{
		propsMap.put(MapCablePathElement.class,
			MapCablePathPane.getInstance());
		propsMap.put(MapPhysicalLinkElement.class,
			MapLinkPane.getInstance());
		propsMap.put(MapSiteNodeElement.class,
			MapSitePane.getInstance());
		propsMap.put(MapNodeProtoElement.class,
			MapProtoPane.getInstance());
//		propsMap.put(MapMarkElement.class,
//		propsMap.put(MapNodeLinkElement.class,
//		propsMap.put(MapPhysicalNodeElement.class,
//		propsMap.put(MapSelection.class,
//		propsMap.put(MapUnboundNodeElement.class,
//		propsMap.put(VoidMapElement.class,
//		propsMap.put(MapAlarmMarker.class,
//		propsMap.put(MapEventMarker.class,
//		propsMap.put(MapMarker.class,
//		propsMap.put(MapPathElement.class,
	}
	
	public static ObjectResourcePropertiesPane getPropsPane(MapElement me)
	{
		ObjectResourcePropertiesPane pane = (ObjectResourcePropertiesPane )propsMap.get(me.getClass());
//		menu.setMapElement(me);
		return pane;
	}
	
}
