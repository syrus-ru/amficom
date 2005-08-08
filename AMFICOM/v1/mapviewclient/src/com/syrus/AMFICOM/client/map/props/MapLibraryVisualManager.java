/**
 * $Id: MapLibraryVisualManager.java,v 1.1 2005/08/08 09:59:41 krupenn Exp $
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
 * @version $Revision: 1.1 $, $Date: 2005/08/08 09:59:41 $
 * @author $Author: krupenn $
 * @module mapviewclient
 */
public class MapLibraryVisualManager implements VisualManager {

	public static final int DEF_HEIGHT = 24;
	public static final int DEF_WIDTH = 150;

	static MapLibraryVisualManager instance;

	private static MapLibraryEditor generalPanel;

	protected MapLibraryVisualManager() {
		// empty
	}
	
	public static MapLibraryVisualManager getInstance() {
		if(instance == null)
			instance = new MapLibraryVisualManager();
		return instance;
	}

	public StorableObjectEditor getGeneralPropertiesPanel() {
		if (generalPanel == null)
			generalPanel = new MapLibraryEditor();
		return generalPanel;
	}

	public StorableObjectEditor getCharacteristicPropertiesPanel() {
		return null;
	}

	public StorableObjectEditor getAdditionalPropertiesPanel() {
		return null;
	}

	public StorableObjectWrapper getController() {
		return null;
	}

}
