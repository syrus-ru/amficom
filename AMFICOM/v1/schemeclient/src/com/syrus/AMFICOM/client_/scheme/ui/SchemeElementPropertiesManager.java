/*-
 * $Id: SchemeElementPropertiesManager.java,v 1.5 2005/08/08 11:58:07 arseniy Exp $
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
 * @author $Author: arseniy $
 * @version $Revision: 1.5 $, $Date: 2005/08/08 11:58:07 $
 * @module schemeclient
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
		if (this.generalPanel == null) {
			this.generalPanel = new SchemeElementGeneralPanel();
		}
		this.generalPanel.setContext(aContext);
		if (this.charPanel == null) {
			this.charPanel = new SchemeElementCharacteristicsPanel();
		}
		this.charPanel.setContext(aContext);
		if (this.ugoPanel == null) {
			this.ugoPanel = new SchemeCellPanel();
		}
		this.ugoPanel.setContext(aContext);
	}
	
	/**
	 * @return SchemeElementGeneralPanel
	 * @see VisualManager#getGeneralPropertiesPanel()
	 */
	public StorableObjectEditor getGeneralPropertiesPanel() {
		return this.generalPanel;
	}

	/**
	 * @return SchemeElementCharacteristicsPanel
	 * @see VisualManager#getCharacteristicPropertiesPanel()
	 */
	public StorableObjectEditor getCharacteristicPropertiesPanel() {
		return this.charPanel;
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
		return this.ugoPanel;
	}
}
