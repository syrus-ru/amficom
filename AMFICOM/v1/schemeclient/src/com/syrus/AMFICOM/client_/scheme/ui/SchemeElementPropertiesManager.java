/*-
 * $Id: SchemeElementPropertiesManager.java,v 1.1 2005/04/18 10:45:18 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.client_.general.ui_.*;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;
import com.syrus.AMFICOM.scheme.SchemeElementController;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/04/18 10:45:18 $
 * @module schemeclient_v1
 */

public class SchemeElementPropertiesManager implements VisualManager {
	private static SchemeElementPropertiesManager instance;
	private SchemeElementGeneralPanel generalPanel;
	private SchemeElementCharacteristicsPanel charPanel;
	
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
	public ObjectResourceController getController() {
		return SchemeElementController.getInstance();
	}
}
