package com.syrus.AMFICOM.Client.Map.Props;

import com.syrus.AMFICOM.Client.General.UI.ObjectResourcePropertiesPane;
import com.syrus.AMFICOM.Client.Map.Props.MapPhysicalLinkController;
import com.syrus.AMFICOM.Client.Resource.Map.MapElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapMarkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeProtoElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPipePathElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapSiteNodeElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapCablePathElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapMeasurementPathElement;
import com.syrus.AMFICOM.Client.Resource.MapView.VoidMapElement;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourcePropertiesController;

import java.util.HashMap;

public final class MapPropsManager 
{
	private MapPropsManager()
	{
	}

	private static java.util.Map propsMap = new HashMap();

	private static java.util.Map propsCtlMap = new HashMap();

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

		propsCtlMap.put(MapPhysicalNodeElement.class,
			MapPhysicalNodeController.getInstance());
		propsCtlMap.put(MapSiteNodeElement.class,
			MapSiteNodeController.getInstance());
		propsCtlMap.put(MapNodeLinkElement.class,
			MapNodeLinkController.getInstance());
		propsCtlMap.put(MapPhysicalLinkElement.class,
			MapPhysicalLinkController.getInstance());
		propsCtlMap.put(MapMarkElement.class,
			MapMarkController.getInstance());
		propsCtlMap.put(VoidMapElement.class,
			MapVoidElementController.getInstance());
		propsCtlMap.put(MapCablePathElement.class,
			MapCablePathController.getInstance());
		propsCtlMap.put(MapMeasurementPathElement.class,
			MapMeasurementPathController.getInstance());
		propsCtlMap.put(MapPipePathElement.class,
			MapCollectorController.getInstance());
	}
	
	public static ObjectResourcePropertiesPane getPropsPane(MapElement me)
	{
		ObjectResourcePropertiesPane pane = (ObjectResourcePropertiesPane )propsMap.get(me.getClass());
		return pane;
	}

	public static ObjectResourcePropertiesController getPropertiesController(MapElement me)
	{
		ObjectResourcePropertiesController controller = (ObjectResourcePropertiesController )propsCtlMap.get(me.getClass());
		return controller;
	}


}
