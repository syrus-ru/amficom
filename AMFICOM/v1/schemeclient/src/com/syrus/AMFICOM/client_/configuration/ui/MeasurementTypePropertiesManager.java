/*
 * $Id: MeasurementTypePropertiesManager.java,v 1.6 2005/04/28 16:02:36 stas Exp $
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
import com.syrus.AMFICOM.measurement.MeasurementTypeController;

/**
 * @author $Author: stas $
 * @version $Revision: 1.6 $, $Date: 2005/04/28 16:02:36 $
 * @module schemeclient_v1
 */

public class MeasurementTypePropertiesManager implements VisualManager {
	private static MeasurementTypePropertiesManager instance;
	private MeasurementTypeGeneralPanel generalPanel;
	private MeasurementTypeCharacteristicsPanel charPanel;
	private EmptyStorableObjectEditor emptyPanel;
	
	private MeasurementTypePropertiesManager() {
		// empty
	}
	
	public static MeasurementTypePropertiesManager getInstance(ApplicationContext aContext) {
		if (instance == null) 
			instance = new MeasurementTypePropertiesManager();
		instance.setContext(aContext);
		return instance;
	}
	
	public void setContext(ApplicationContext aContext) {
		if (generalPanel == null)
			generalPanel = new MeasurementTypeGeneralPanel();
		generalPanel.setContext(aContext);
		if (charPanel == null)
			charPanel = new MeasurementTypeCharacteristicsPanel();
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
	 * @return MeasurementTypeController
	 * @see com.syrus.AMFICOM.client_.general.ui_.VisualManager#getController()
	 */
	public ObjectResourceController getController() {
		return MeasurementTypeController.getInstance();
	}

	/**
	 * @return EmptyStorableObjectEditor
	 * @see VisualManager#getAdditionalPropertiesPanel()
	 */
	public StorableObjectEditor getAdditionalPropertiesPanel() {
		return emptyPanel;
	}
}
