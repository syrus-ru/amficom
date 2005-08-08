/**
 * $Id: MapLibraryVisualManager.java,v 1.2 2005/08/08 13:07:20 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.client.map.props;

import com.syrus.AMFICOM.client.UI.StorableObjectEditor;
import com.syrus.AMFICOM.client.UI.VisualManager;
import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.2 $, $Date: 2005/08/08 13:07:20 $
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
