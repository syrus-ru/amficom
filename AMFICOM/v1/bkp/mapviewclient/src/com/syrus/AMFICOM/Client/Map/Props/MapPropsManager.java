package com.syrus.AMFICOM.Client.Map.Props;

import com.syrus.AMFICOM.Client.General.UI.ObjectResourcePropertiesPane;
import com.syrus.AMFICOM.Client.Resource.Map.MapElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapMarkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeProtoElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPipePathElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapSiteNodeElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapCablePathElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapMarker;
import com.syrus.AMFICOM.Client.Resource.MapView.MapMeasurementPathElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapSelection;
import com.syrus.AMFICOM.Client.Resource.MapView.MapUnboundLinkElement;
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
		propsMap.put(VoidMapElement.class,
			MapViewPanel.getInstance());

		propsCtlMap.put(MapPhysicalNodeElement.class,
			MapPhysicalNodePropertiesController.getInstance());
		propsCtlMap.put(MapSiteNodeElement.class,
			MapSiteNodePropertiesController.getInstance());
		propsCtlMap.put(MapNodeLinkElement.class,
			MapNodeLinkPropertiesController.getInstance());
		propsCtlMap.put(MapPhysicalLinkElement.class,
			MapPhysicalLinkPropertiesController.getInstance());
		propsCtlMap.put(MapMarkElement.class,
			MapMarkPropertiesController.getInstance());
		propsCtlMap.put(VoidMapElement.class,
			MapVoidElementPropertiesController.getInstance());
		propsCtlMap.put(MapCablePathElement.class,
			MapCablePathPropertiesController.getInstance());
		propsCtlMap.put(MapMeasurementPathElement.class,
			MapMeasurementPathPropertiesController.getInstance());
		propsCtlMap.put(MapPipePathElement.class,
			MapCollectorPropertiesController.getInstance());
		propsCtlMap.put(MapMarker.class,
			MapMarkerPropertiesController.getInstance());
		propsCtlMap.put(MapUnboundLinkElement.class,
			MapUnboundLinkPropertiesController.getInstance());
		propsCtlMap.put(MapSelection.class,
			MapSelectionPropertiesController.getInstance());
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
