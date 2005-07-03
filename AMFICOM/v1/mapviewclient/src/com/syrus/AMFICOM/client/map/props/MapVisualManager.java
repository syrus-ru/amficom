/**
 * $Id: MapVisualManager.java,v 1.7 2005/06/23 14:28:17 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.client.map.props;

import java.util.HashMap;

import com.syrus.AMFICOM.client.UI.StorableObjectEditor;
import com.syrus.AMFICOM.client.UI.VisualManager;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.map.Collector;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.Mark;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.mapview.AlarmMarker;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.EventMarker;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.Marker;
import com.syrus.AMFICOM.mapview.MeasurementPath;
import com.syrus.AMFICOM.mapview.Selection;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.mapview.UnboundNode;
import com.syrus.AMFICOM.mapview.VoidElement;

/**
 * @version $Revision: 1.7 $, $Date: 2005/06/23 14:28:17 $
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
		vmMap.put(MapView.class,
				MapViewVisualManager.getInstance());
		vmMap.put(Map.class,
				MapVisualManager.getInstance());

		vmMap.put(TopologicalNode.class,
				TopologicalNodeVisualManager.getInstance());
		vmMap.put(SiteNode.class,
				SiteNodeVisualManager.getInstance());
		vmMap.put(NodeLink.class,
				NodeLinkVisualManager.getInstance());
		vmMap.put(PhysicalLink.class,
			PhysicalLinkVisualManager.getInstance());
		vmMap.put(Collector.class,
				CollectorVisualManager.getInstance());
		vmMap.put(Mark.class,
				MarkVisualManager.getInstance());

		vmMap.put(VoidElement.class,
				MapVisualManager.getInstance());
		vmMap.put(CablePath.class,
				CablePathVisualManager.getInstance());
		vmMap.put(MeasurementPath.class,
				MeasurementPathVisualManager.getInstance());
		vmMap.put(Marker.class,
				MarkerVisualManager.getInstance());
		vmMap.put(AlarmMarker.class,
				MarkerVisualManager.getInstance());
		vmMap.put(EventMarker.class,
				MarkerVisualManager.getInstance());
		vmMap.put(Selection.class,
				SelectionVisualManager.getInstance());
		vmMap.put(UnboundLink.class,
				UnboundLinkVisualManager.getInstance());
		vmMap.put(UnboundNode.class,
				UnboundNodeVisualManager.getInstance());
	}

	public static VisualManager getVisualManager(Object element) {
		return (VisualManager) vmMap.get(element.getClass());
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

	public StorableObjectEditor getAdditionalPropertiesPanel() {
		// TODO Auto-generated method stub
		return null;
	}

	public StorableObjectWrapper getController() {
		// TODO Auto-generated method stub
		return null;
	}

}
