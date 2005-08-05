/*-
 * $Id: SchemeProtoGroupPropertiesManager.java,v 1.2 2005/08/05 12:40:00 stas Exp $
 *
 * Copyright � 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import com.syrus.AMFICOM.client.UI.StorableObjectEditor;
import com.syrus.AMFICOM.client.UI.VisualManager;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.scheme.SchemeProtoGroupWrapper;

/**
 * @author $Author: stas $
 * @version $Revision: 1.2 $, $Date: 2005/08/05 12:40:00 $
 * @module schemeclient_v1
 */

public class SchemeProtoGroupPropertiesManager implements VisualManager {
	private static SchemeProtoGroupPropertiesManager instance;
	private SchemeProtoGroupGeneralPanel generalPanel;
	private EmptyStorableObjectEditor charPanel;
	private EmptyStorableObjectEditor ugoPanel;
	
	private SchemeProtoGroupPropertiesManager() {
		// empty
	}
	
	public static SchemeProtoGroupPropertiesManager getInstance(ApplicationContext aContext) {
		if (instance == null)
			instance = new SchemeProtoGroupPropertiesManager();
		instance.setContext(aContext);
		return instance;
	}
	
	public void setContext(ApplicationContext aContext) {
		if (this.generalPanel == null) {
			this.generalPanel = new SchemeProtoGroupGeneralPanel();
		}
		this.generalPanel.setContext(aContext);
		if (this.charPanel == null) {
			this.charPanel = new EmptyStorableObjectEditor();
		}
		if (this.ugoPanel == null) {
			this.ugoPanel = new EmptyStorableObjectEditor();
		}
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
		return SchemeProtoGroupWrapper.getInstance();
	}

	/**
	 * @return SchemeProtoElementUgoPanel
	 * @see VisualManager#getAdditionalPropertiesPanel()
	 */
	public StorableObjectEditor getAdditionalPropertiesPanel() {
		return this.ugoPanel;
	}
}


