/*-
 * $$Id: MapLibraryVisualManager.java,v 1.3 2005/09/30 16:08:40 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.props;

import com.syrus.AMFICOM.client.UI.StorableObjectEditor;
import com.syrus.AMFICOM.client.UI.VisualManager;
import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.3 $, $Date: 2005/09/30 16:08:40 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
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
