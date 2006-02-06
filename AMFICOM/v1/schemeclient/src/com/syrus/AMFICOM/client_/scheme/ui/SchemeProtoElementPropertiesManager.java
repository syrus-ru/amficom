/*-
 * $Id: SchemeProtoElementPropertiesManager.java,v 1.5 2005/08/08 11:58:08 arseniy Exp $
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
import com.syrus.AMFICOM.scheme.SchemeProtoElementWrapper;


/**
 * @author $Author: arseniy $
 * @version $Revision: 1.5 $, $Date: 2005/08/08 11:58:08 $
 * @module schemeclient
 */

public class SchemeProtoElementPropertiesManager implements VisualManager {
	private static SchemeProtoElementPropertiesManager instance;
	private SchemeProtoElementGeneralPanel generalPanel;
	private SchemeProtoElementCharacteristicsPanel charPanel;
	private SchemeCellPanel ugoPanel;
	
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
		if (this.generalPanel == null) {
			this.generalPanel = new SchemeProtoElementGeneralPanel();
		}
		this.generalPanel.setContext(aContext);
		if (this.charPanel == null) {
			this.charPanel = new SchemeProtoElementCharacteristicsPanel();
		}
		this.charPanel.setContext(aContext);
		if (this.ugoPanel == null) {
			this.ugoPanel = new SchemeCellPanel();
		}
		this.ugoPanel.setContext(aContext);
	}
	
	/**
	 * @return SchemeProtoElementGeneralPanel
	 * @see VisualManager#getGeneralPropertiesPanel()
	 */
	public StorableObjectEditor getGeneralPropertiesPanel() {
		return this.generalPanel;
	}

	/**
	 * @return SchemeProtoElementCharacteristicsPanel
	 * @see VisualManager#getCharacteristicPropertiesPanel()
	 */
	public StorableObjectEditor getCharacteristicPropertiesPanel() {
		return this.charPanel;
	}

	/**
	 * @return SchemeProtoElementController
	 * @see VisualManager#getController()
	 */
	public StorableObjectWrapper getController() {
		return SchemeProtoElementWrapper.getInstance();
	}

	/**
	 * @return SchemeProtoElementUgoPanel
	 * @see VisualManager#getAdditionalPropertiesPanel()
	 */
	public StorableObjectEditor getAdditionalPropertiesPanel() {
		return this.ugoPanel;
	}
}

