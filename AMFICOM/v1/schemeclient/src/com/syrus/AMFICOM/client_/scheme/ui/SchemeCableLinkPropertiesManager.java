/*-
 * $Id: SchemeCableLinkPropertiesManager.java,v 1.1 2005/04/18 10:45:17 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.client_.general.ui_.*;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;
import com.syrus.AMFICOM.scheme.SchemeCableLinkController;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/04/18 10:45:17 $
 * @module schemeclient_v1
 */

public class SchemeCableLinkPropertiesManager implements VisualManager {
	private static SchemeCableLinkPropertiesManager instance;
	private SchemeCableLinkGeneralPanel generalPanel;
	private SchemeCableLinkCharacteristicsPanel charPanel;
	
	private SchemeCableLinkPropertiesManager() {
		// empty
	}
	
	public static SchemeCableLinkPropertiesManager getInstance(ApplicationContext aContext) {
		if (instance == null)
			instance = new SchemeCableLinkPropertiesManager();
		instance.setContext(aContext);
		return instance;
	}

	public void setContext(ApplicationContext aContext) {
		if (generalPanel == null)
			generalPanel = new SchemeCableLinkGeneralPanel();
		generalPanel.setContext(aContext);
		if (charPanel == null)
			charPanel = new SchemeCableLinkCharacteristicsPanel(); 
		charPanel.setContext(aContext);
	}
	
	/**
	 * @return SchemeCableLinkGeneralPanel
	 * @see VisualManager#getGeneralPropertiesPanel()
	 */
	public StorableObjectEditor getGeneralPropertiesPanel() {
		return generalPanel;
	}

	/**
	 * @return SchemeCableLinkCharacteristicsPanel
	 * @see VisualManager#getCharacteristicPropertiesPanel()
	 */
	public StorableObjectEditor getCharacteristicPropertiesPanel() {
		return charPanel;
	}

	/**
	 * @return SchemeCableLinkController
	 * @see VisualManager#getController()
	 */
	public ObjectResourceController getController() {
		return SchemeCableLinkController.getInstance();
	}
}
