package com.syrus.AMFICOM.Client.Map.Props;

import java.util.HashMap;

import com.syrus.AMFICOM.client_.general.ui_.ObjectResourcePropertiesController;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourcePropertiesPane;
import com.syrus.AMFICOM.map.MapElement;

public final class MapPropsManager 
{
	private MapPropsManager()
	{
	}

	private static java.util.Map propsMap = new HashMap();

	private static java.util.Map propsCtlMap = new HashMap();

	static
	{
		propsMap.put(com.syrus.AMFICOM.mapview.CablePath.class,
			MapCablePathPane.getInstance());
		propsMap.put(com.syrus.AMFICOM.map.PhysicalLink.class,
			MapLinkPane.getInstance());
		propsMap.put(com.syrus.AMFICOM.map.SiteNode.class,
			MapSitePane.getInstance());
		propsMap.put(com.syrus.AMFICOM.map.SiteNodeType.class,
			MapProtoPane.getInstance());
		propsMap.put(com.syrus.AMFICOM.mapview.VoidElement.class,
			MapViewPanel.getInstance());
		propsMap.put(com.syrus.AMFICOM.mapview.MapView.class,
			MapViewPanel.getInstance());
		propsMap.put(com.syrus.AMFICOM.map.Map.class,
			MapPanel.getInstance());

		propsCtlMap.put(com.syrus.AMFICOM.map.TopologicalNode.class,
			MapPhysicalNodePropertiesController.getInstance());
		propsCtlMap.put(com.syrus.AMFICOM.map.SiteNode.class,
			MapSiteNodePropertiesController.getInstance());
		propsCtlMap.put(com.syrus.AMFICOM.map.NodeLink.class,
			MapNodeLinkPropertiesController.getInstance());
		propsCtlMap.put(com.syrus.AMFICOM.map.PhysicalLink.class,
			MapPhysicalLinkPropertiesController.getInstance());
		propsCtlMap.put(com.syrus.AMFICOM.map.Mark.class,
			MapMarkPropertiesController.getInstance());
		propsCtlMap.put(com.syrus.AMFICOM.mapview.VoidElement.class,
			MapVoidElementPropertiesController.getInstance());
		propsCtlMap.put(com.syrus.AMFICOM.mapview.CablePath.class,
			MapCablePathPropertiesController.getInstance());
		propsCtlMap.put(com.syrus.AMFICOM.mapview.MeasurementPath.class,
			MapMeasurementPathPropertiesController.getInstance());
		propsCtlMap.put(com.syrus.AMFICOM.map.Collector.class,
			MapCollectorPropertiesController.getInstance());
		propsCtlMap.put(com.syrus.AMFICOM.mapview.Marker.class,
			MapMarkerPropertiesController.getInstance());
		propsCtlMap.put(com.syrus.AMFICOM.mapview.UnboundLink.class,
			MapUnboundLinkPropertiesController.getInstance());
		propsCtlMap.put(com.syrus.AMFICOM.mapview.Selection.class,
			MapSelectionPropertiesController.getInstance());
	}
	
	public static ObjectResourcePropertiesPane getPropsPane(Object me)
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
