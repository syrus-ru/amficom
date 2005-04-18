/*-
 * $Id: SchemeProtoElementPropertiesManager.java,v 1.1 2005/04/18 10:45:18 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.client_.general.ui_.*;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/04/18 10:45:18 $
 * @module schemeclient_v1
 */

public class SchemeProtoElementPropertiesManager implements VisualManager {
	private static SchemeProtoElementPropertiesManager instance;
	private static SchemeProtoElementGeneralPanel generalPanel;
	private static SchemeProtoElementCharacteristicsPanel charPanel;
	
	private SchemeProtoElementPropertiesManager() {
		// empty
	}
	
	public static SchemeProtoElementPropertiesManager getInstance(ApplicationContext aContext) {
		if (instance == null)
			instance = new SchemeProtoElementPropertiesManager();
		instance.setContext(aContext);
		return instance;
	}
	
	public void setContext(ApplicationContext aContext) {
		if (generalPanel == null)
			generalPanel = new SchemeProtoElementGeneralPanel();
		generalPanel.setContext(aContext);
		if (charPanel == null)
			charPanel = new SchemeProtoElementCharacteristicsPanel();
		charPanel.setContext(aContext);
	}
	
	/**
	 * @return SchemeProtoElementGeneralPanel
	 * @see VisualManager#getGeneralPropertiesPanel()
	 */
	public StorableObjectEditor getGeneralPropertiesPanel() {
		return generalPanel;
	}

	/**
	 * @return SchemeProtoElementCharacteristicsPanel
	 * @see VisualManager#getCharacteristicPropertiesPanel()
	 */
	public StorableObjectEditor getCharacteristicPropertiesPanel() {
		return charPanel;
	}

	/**
	 * @return SchemeProtoElementController
	 * @see VisualManager#getController()
	 */
	public ObjectResourceController getController() {
		return null;
		// TODO write SchemeProtoElementController
		//return SchemeProtoElementController.getInstance();
	}
}

