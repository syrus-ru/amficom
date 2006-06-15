/*-
 * $Id: SchemePathPropertiesManager.java,v 1.2 2006/06/15 06:32:00 stas Exp $
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
import com.syrus.AMFICOM.scheme.SchemePathWrapper;

public class SchemePathPropertiesManager implements VisualManager {
	private static SchemePathPropertiesManager instance;
	private SchemePathGeneralPanel generalPanel;
	private SchemePathCharacteristicsPanel charPanel;
	private SchemePathAdditionalPanel ugoPanel;
	
	private SchemePathPropertiesManager() {
		// empty
	}
	
	public static SchemePathPropertiesManager getInstance(ApplicationContext aContext) {
		if (instance == null)
			instance = new SchemePathPropertiesManager();
		instance.setContext(aContext);
		return instance;
	}
	
	public void setContext(ApplicationContext aContext) {
		if (this.generalPanel == null) {
			this.generalPanel = new SchemePathGeneralPanel();
		}
		this.generalPanel.setContext(aContext);
		if (this.charPanel == null) {
			this.charPanel = new SchemePathCharacteristicsPanel();
		}
		this.charPanel.setContext(aContext);
		if (this.ugoPanel == null) {
			this.ugoPanel = new SchemePathAdditionalPanel();
		}
	}
	
	/**
	 * @return SchemeGeneralPanel
	 * @see VisualManager#getGeneralPropertiesPanel()
	 */
	public StorableObjectEditor getGeneralPropertiesPanel() {
		return this.generalPanel;
	}

	/**
	 * @return EmptyStorableObjectEditor
	 * @see VisualManager#getCharacteristicPropertiesPanel()
	 */
	public StorableObjectEditor getCharacteristicPropertiesPanel() {
		return this.charPanel;
	}

	/**
	 * @return SchemeController
	 * @see VisualManager#getController()
	 */
	public StorableObjectWrapper getController() {
		return SchemePathWrapper.getInstance();
	}

	/**
	 * @return SchemeUgoPanel
	 * @see VisualManager#getAdditionalPropertiesPanel()
	 */
	public StorableObjectEditor getAdditionalPropertiesPanel() {
		return this.ugoPanel;
	}
}
