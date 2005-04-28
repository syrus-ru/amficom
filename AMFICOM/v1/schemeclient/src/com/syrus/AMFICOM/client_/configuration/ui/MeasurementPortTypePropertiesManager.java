/*
 * $Id: MeasurementPortTypePropertiesManager.java,v 1.5 2005/04/28 16:02:36 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.configuration.ui;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.client_.general.ui_.*;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;
import com.syrus.AMFICOM.client_.scheme.ui.EmptyStorableObjectEditor;
import com.syrus.AMFICOM.configuration.MeasurementPortTypeController;

/**
 * @author $Author: stas $
 * @version $Revision: 1.5 $, $Date: 2005/04/28 16:02:36 $
 * @module schemeclient_v1
 */

public class MeasurementPortTypePropertiesManager implements VisualManager {
	private static MeasurementPortTypePropertiesManager instance;
	private MeasurementPortTypeGeneralPanel generalPanel;
	private MeasurementPortTypeCharacteristicsPanel charPanel;
	private EmptyStorableObjectEditor emptyPanel;
	
	private MeasurementPortTypePropertiesManager() {
		// empty
	}
	
	public static MeasurementPortTypePropertiesManager getInstance(ApplicationContext aContext) {
		if (instance == null) 
			instance = new MeasurementPortTypePropertiesManager();
		instance.setContext(aContext);
		return instance;
	}
	
	public void setContext(ApplicationContext aContext) {
		if (generalPanel == null)
			generalPanel = new MeasurementPortTypeGeneralPanel();
		generalPanel.setContext(aContext);
		if (charPanel == null)
			charPanel = new MeasurementPortTypeCharacteristicsPanel();
		charPanel.setContext(aContext);
		if (emptyPanel == null)
			emptyPanel = new EmptyStorableObjectEditor();
	}
	
	/**
	 * @return AbstractLinkTypeGeneralPanel
	 * @see VisualManager#getGeneralPropertiesPanel()
	 */
	public StorableObjectEditor getGeneralPropertiesPanel() {
		return generalPanel;
	}

	/**
	 * @return LinkTypeCharacteristicsPanel
	 * @see VisualManager#getCharacteristicPropertiesPanel()
	 */
	public StorableObjectEditor getCharacteristicPropertiesPanel() {
		return charPanel;
	}

	/**
	 * @return MeasurementPortTypeController
	 * @see com.syrus.AMFICOM.client_.general.ui_.VisualManager#getController()
	 */
	public ObjectResourceController getController() {
		return MeasurementPortTypeController.getInstance();
	}

	/**
	 * @return EmptyStorableObjectEditor
	 * @see VisualManager#getAdditionalPropertiesPanel()
	 */
	public StorableObjectEditor getAdditionalPropertiesPanel() {
		return emptyPanel;
	}
}
