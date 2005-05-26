/*-
 * $Id: SchemeElementPropertiesManager.java,v 1.2 2005/05/26 07:40:52 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import com.syrus.AMFICOM.client.UI.*;
import com.syrus.AMFICOM.client.UI.VisualManager;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.scheme.SchemeElementWrapper;


/**
 * @author $Author: stas $
 * @version $Revision: 1.2 $, $Date: 2005/05/26 07:40:52 $
 * @module schemeclient_v1
 */

public class SchemeElementPropertiesManager implements VisualManager {
	private static SchemeElementPropertiesManager instance;
	private SchemeElementGeneralPanel generalPanel;
	private SchemeElementCharacteristicsPanel charPanel;
	private SchemeElementUgoPanel ugoPanel;
	
	private SchemeElementPropertiesManager() {
		// empty
	}
	
	public static SchemeElementPropertiesManager getInstance(ApplicationContext aContext) {
		if (instance == null)
			instance = new SchemeElementPropertiesManager();
		instance.setContext(aContext);
		return instance;
	}
	
	public void setContext(ApplicationContext aContext) {
		if (generalPanel == null)
			generalPanel = new SchemeElementGeneralPanel();
		generalPanel.setContext(aContext);
		if (charPanel == null)
			charPanel = new SchemeElementCharacteristicsPanel();
		charPanel.setContext(aContext);
		if (ugoPanel == null)
			ugoPanel = new SchemeElementUgoPanel();
		ugoPanel.setContext(aContext);
	}
	
	/**
	 * @return SchemeElementGeneralPanel
	 * @see VisualManager#getGeneralPropertiesPanel()
	 */
	public StorableObjectEditor getGeneralPropertiesPanel() {
		return generalPanel;
	}

	/**
	 * @return SchemeElementCharacteristicsPanel
	 * @see VisualManager#getCharacteristicPropertiesPanel()
	 */
	public StorableObjectEditor getCharacteristicPropertiesPanel() {
		return charPanel;
	}

	/**
	 * @return SchemeElementController
	 * @see VisualManager#getController()
	 */
	public StorableObjectWrapper getController() {
		return SchemeElementWrapper.getInstance();
	}

	/**
	 * @return SchemeElementUgoPanel
	 * @see VisualManager#getAdditionalPropertiesPanel()
	 */
	public StorableObjectEditor getAdditionalPropertiesPanel() {
		return ugoPanel;
	}
}
