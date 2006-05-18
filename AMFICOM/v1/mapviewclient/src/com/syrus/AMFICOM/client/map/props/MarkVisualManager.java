/*-
 * $$Id: MarkVisualManager.java,v 1.5 2005/09/30 16:08:40 krupenn Exp $$
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
 * @version $Revision: 1.5 $, $Date: 2005/09/30 16:08:40 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class MarkVisualManager implements VisualManager {

	private static MarkVisualManager instance;

	private static MarkEditor generalPanel;
	private static MapElementCharacteristicsEditor charPanel;

	public static MarkVisualManager getInstance() {
		if (instance == null) 
			instance = new MarkVisualManager();
		return instance;
	}

	public StorableObjectEditor getGeneralPropertiesPanel() {
		if (generalPanel == null)
			generalPanel = new MarkEditor();
		return generalPanel;
	}

	public StorableObjectEditor getCharacteristicPropertiesPanel() {
		if (charPanel == null)
			charPanel = new MapElementCharacteristicsEditor();
		return charPanel;
	}

	public StorableObjectWrapper getController() {
		return null;
	}

	public StorableObjectEditor getAdditionalPropertiesPanel() {
		// TODO Auto-generated method stub
		return null;
	}

}
