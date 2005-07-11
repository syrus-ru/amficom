/*-
 * $Id: SchemeElementPropertiesManager.java,v 1.3 2005/07/11 12:31:39 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import com.syrus.AMFICOM.client.UI.StorableObjectEditor;
import com.syrus.AMFICOM.client.UI.VisualManager;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.scheme.SchemeElementWrapper;


/**
 * @author $Author: stas $
 * @version $Revision: 1.3 $, $Date: 2005/07/11 12:31:39 $
 * @module schemeclient_v1
 */

public class SchemeElementPropertiesManager implements VisualManager {
	private static SchemeElementPropertiesManager instance;
	private SchemeElementGeneralPanel generalPanel;
	private SchemeElementCharacteristicsPanel charPanel;
	private SchemeCellPanel ugoPanel;
	
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
			ugoPanel = new SchemeCellPanel();
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
