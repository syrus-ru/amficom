package com.syrus.AMFICOM.Client.Map.Props;

import com.syrus.AMFICOM.Client.General.UI.ObjectResourcePropertiesPane;
import com.syrus.AMFICOM.Client.Resource.Map.MapElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeProtoElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapSiteNodeElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapCablePathElement;
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

		propsCtlMap.put(MapPhysicalNodeElement.class,
			MapPhysicalNodeController.getInstance());
		propsCtlMap.put(MapSiteNodeElement.class,
			MapSiteNodeController.getInstance());
		propsCtlMap.put(MapNodeLinkElement.class,
			MapNodeLinkController.getInstance());
	}
	
	public static ObjectResourcePropertiesPane getPropsPane(MapElement me)
	{
		ObjectResourcePropertiesPane pane = (ObjectResourcePropertiesPane )propsMap.get(me.getClass());
//		menu.setMapElement(me);
		return pane;
	}
/*
	public static MapElementPropertiesTableModel getPropertiesTableModel(MapElement me)
	{
		MapElementPropertiesTableModel model = null;
//		if(me.getClass().equals(VoidMapElement.class))
//			model = new MapViewPropertiesTableModel((VoidMapElement )me);
//		else
		if(me.getClass().equals(MapNodeLinkElement.class))
			model = new MapNodeLinkPropertiesTableModel((MapNodeLinkElement )me);
		else
		if(me.getClass().equals(MapPhysicalNodeElement.class))
			model = new MapPhysicalNodePropertiesTableModel((MapPhysicalNodeElement )me);
		else
		if(me.getClass().equals(MapSiteNodeElement.class))
			model = new MapSiteNodePropertiesTableModel((MapSiteNodeElement )me);
//		else
//		if(me.getClass().equals(MapPhysicalLinkElement.class))
//			model = new MapPhysicalLinkPropertiesTableModel((MapPhysicalLinkElement )me);
//		else
//		if(me.getClass().equals(MapMarkElement.class))
//			model = new MapMarkPropertiesTableModel((MapMarkElement )me);
//		else
//		if(me.getClass().equals(MapSelection.class))
//			model = new MapSelectionPropertiesTableModel((MapSelection )me);
//		else
//		if(me.getClass().equals(MapPipePathElement.class))
//			model = new MapPipePathPropertiesTableModel((MapPipePathElement )me);
//		else
//		if(me.getClass().equals(MapCablePathElement.class))
//			model = new MapCablePathPropertiesTableModel((MapCablePathElement )me);
//		else
//		if(me.getClass().equals(MapUnboundNodeElement.class))
//			model = new MapUnboundNodePropertiesTableModel((MapUnboundNodeElement )me);
//		else
//		if(me.getClass().equals(MapMeasurementPathElement.class))
//			model = new MapMeasurementPathPropertiesTableModel((MapMeasurementPathElement )me);
//		else
//		if(me.getClass().equals(MapMarker.class))
//			model = new MapMarkerPropertiesTableModel((MapMarker )me);
//		else
//		if(me.getClass().equals(MapAlarmMarker.class))
//			model = new MapAlarmMarkerPropertiesTableModel((MapAlarmMarker )me);
//		else
//		if(me.getClass().equals(MapEventMarker.class))
//			model = new MapEventMarkerPropertiesTableModel((MapEventMarker )me);
//		else
//		if(me.getClass().equals(.class))
//			model = new PropertiesTableModel(( )me);

		return model;
	}
*/
	public static ObjectResourcePropertiesController getPropertiesController(MapElement me)
	{
		ObjectResourcePropertiesController controller = (ObjectResourcePropertiesController )propsCtlMap.get(me.getClass());
		return controller;
	}


}
