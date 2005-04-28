/**
 * $Id: MapVisualManager.java,v 1.2 2005/04/28 12:57:09 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 */
package com.syrus.AMFICOM.Client.Map.Props;

import java.util.HashMap;

import com.syrus.AMFICOM.client_.general.ui_.StorableObjectEditor;
import com.syrus.AMFICOM.client_.general.ui_.VisualManager;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;
import com.syrus.AMFICOM.map.MapElement;

/**
 * @version $Revision: 1.2 $, $Date: 2005/04/28 12:57:09 $
 * @author $Author: krupenn $
 * @module mapviewclient
 */
public class MapVisualManager implements VisualManager {

	public static final int DEF_HEIGHT = 24;
	public static final int DEF_WIDTH = 150;

	static MapVisualManager instance;

	private static MapEditor generalPanel;

	private static java.util.Map vmMap = new HashMap();

	static
	{
//		vmMap.put(com.syrus.AMFICOM.mapview.MapView.class,
//				MapViewVisualManager.getInstance());
//		vmMap.put(com.syrus.AMFICOM.map.Map.class,
//				MapVisualManager.getInstance());

		vmMap.put(com.syrus.AMFICOM.map.TopologicalNode.class,
				TopologicalNodeVisualManager.getInstance());
		vmMap.put(com.syrus.AMFICOM.map.SiteNode.class,
				SiteNodeVisualManager.getInstance());
		vmMap.put(com.syrus.AMFICOM.map.NodeLink.class,
				NodeLinkVisualManager.getInstance());
		vmMap.put(com.syrus.AMFICOM.map.PhysicalLink.class,
			PhysicalLinkVisualManager.getInstance());
		vmMap.put(com.syrus.AMFICOM.map.Collector.class,
				CollectorVisualManager.getInstance());
		vmMap.put(com.syrus.AMFICOM.map.Mark.class,
				MarkVisualManager.getInstance());

		vmMap.put(com.syrus.AMFICOM.mapview.VoidElement.class,
				MapVisualManager.getInstance());
		vmMap.put(com.syrus.AMFICOM.mapview.CablePath.class,
				CablePathVisualManager.getInstance());
		vmMap.put(com.syrus.AMFICOM.mapview.MeasurementPath.class,
				MeasurementPathVisualManager.getInstance());
		vmMap.put(com.syrus.AMFICOM.mapview.Marker.class,
				MarkerVisualManager.getInstance());
		vmMap.put(com.syrus.AMFICOM.mapview.AlarmMarker.class,
				MarkerVisualManager.getInstance());
		vmMap.put(com.syrus.AMFICOM.mapview.EventMarker.class,
				MarkerVisualManager.getInstance());
		vmMap.put(com.syrus.AMFICOM.mapview.Selection.class,
				SelectionVisualManager.getInstance());
		vmMap.put(com.syrus.AMFICOM.mapview.UnboundLink.class,
				UnboundLinkVisualManager.getInstance());
		vmMap.put(com.syrus.AMFICOM.mapview.UnboundNode.class,
				UnboundNodeVisualManager.getInstance());
	}

	public static VisualManager getVisualManager(MapElement mapElement) {
		return (VisualManager) vmMap.get(mapElement.getClass());
	}
	
	protected MapVisualManager() {
		// empty
	}
	
	public static MapVisualManager getInstance() {
		if(instance == null)
			instance = new MapVisualManager();
		return instance;
	}

	public StorableObjectEditor getGeneralPropertiesPanel() {
		if (generalPanel == null)
			generalPanel = new MapEditor();
		return generalPanel;
	}

	public StorableObjectEditor getCharacteristicPropertiesPanel() {
		return null;
	}

	public ObjectResourceController getController() {
		return null;
	}

	public StorableObjectEditor getAdditionalPropertiesPanel() {
		// TODO Auto-generated method stub
		return null;
	}

}
