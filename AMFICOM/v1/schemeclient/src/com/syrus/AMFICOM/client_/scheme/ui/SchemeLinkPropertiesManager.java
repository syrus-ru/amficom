/*-
 * $Id: SchemeLinkPropertiesManager.java,v 1.1 2005/04/18 10:45:18 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.client_.general.ui_.*;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;
import com.syrus.AMFICOM.scheme.SchemeLinkController;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/04/18 10:45:18 $
 * @module schemeclient_v1
 */

public class SchemeLinkPropertiesManager implements VisualManager {
	private static SchemeLinkPropertiesManager instance;
	private SchemeLinkGeneralPanel generalPanel;
	private SchemeLinkCharacteristicsPanel charPanel;
	
	private SchemeLinkPropertiesManager() {
		// empty
	}
	
	public static SchemeLinkPropertiesManager getInstance(ApplicationContext aContext) {
		if (instance == null)
			instance = new SchemeLinkPropertiesManager();
		instance.setContext(aContext);
		return instance;
	}
	
	public void setContext(ApplicationContext aContext) {
		if (generalPanel == null)
			generalPanel = new SchemeLinkGeneralPanel();
		generalPanel.setContext(aContext);
		if (charPanel == null)
			charPanel = new SchemeLinkCharacteristicsPanel();
		charPanel.setContext(aContext);
	}

	/**
	 * @return SchemeLinkGeneralPanel
	 * @see VisualManager#getGeneralPropertiesPanel()
	 */
	public StorableObjectEditor getGeneralPropertiesPanel() {
		return generalPanel;
	}

	/**
	 * @return SchemeLinkCharacteristicsPanel
	 * @see VisualManager#getCharacteristicPropertiesPanel()
	 */
	public StorableObjectEditor getCharacteristicPropertiesPanel() {
		return charPanel;
	}

	/**
	 * @return SchemeLinkController
	 * @see VisualManager#getController()
	 */
	public ObjectResourceController getController() {
		return SchemeLinkController.getInstance();
	}
}
