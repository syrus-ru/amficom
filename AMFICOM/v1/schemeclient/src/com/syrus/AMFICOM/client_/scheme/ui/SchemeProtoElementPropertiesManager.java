/*-
 * $Id: SchemeProtoElementPropertiesManager.java,v 1.3 2005/07/11 12:31:39 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import javax.swing.JComponent;

import com.syrus.AMFICOM.client.UI.StorableObjectEditor;
import com.syrus.AMFICOM.client.UI.VisualManager;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client_.scheme.graph.UgoPanel;
import com.syrus.AMFICOM.client_.scheme.graph.UgoToolBar;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.scheme.SchemeProtoElementWrapper;


/**
 * @author $Author: stas $
 * @version $Revision: 1.3 $, $Date: 2005/07/11 12:31:39 $
 * @module schemeclient_v1
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
		if (generalPanel == null)
			generalPanel = new SchemeProtoElementGeneralPanel();
		generalPanel.setContext(aContext);
		if (charPanel == null)
			charPanel = new SchemeProtoElementCharacteristicsPanel();
		charPanel.setContext(aContext);
		if (ugoPanel == null)
			ugoPanel = new SchemeCellPanel();
		ugoPanel.setContext(aContext);
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
	public StorableObjectWrapper getController() {
		return SchemeProtoElementWrapper.getInstance();
	}

	/**
	 * @return SchemeProtoElementUgoPanel
	 * @see VisualManager#getAdditionalPropertiesPanel()
	 */
	public StorableObjectEditor getAdditionalPropertiesPanel() {
		return ugoPanel;
	}
}

