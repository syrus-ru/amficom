/*-
 * $$Id: PhysicalLinkTypeVisualManager.java,v 1.3 2006/06/08 10:28:36 stas Exp $$
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
 * @version $Revision: 1.3 $, $Date: 2006/06/08 10:28:36 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class PhysicalLinkTypeVisualManager implements VisualManager {

	private static PhysicalLinkTypeVisualManager instance;

	private static PhysicalLinkTypeEditor generalPanel;
	private static CharacterizableCharacteristicsEditor charPanel;

	public static PhysicalLinkTypeVisualManager getInstance() {
		if (instance == null) 
			instance = new PhysicalLinkTypeVisualManager();
		return instance;
	}

	public StorableObjectEditor getGeneralPropertiesPanel() {
		if (generalPanel == null)
			generalPanel = new PhysicalLinkTypeEditor();
		return generalPanel;
	}

	public StorableObjectEditor getCharacteristicPropertiesPanel() {
		if (charPanel == null)
			charPanel = new CharacterizableCharacteristicsEditor();
		return charPanel;
	}

	public StorableObjectWrapper getController() {
		return null;
	}

	public StorableObjectEditor getAdditionalPropertiesPanel() {
		return null;
	}

}
