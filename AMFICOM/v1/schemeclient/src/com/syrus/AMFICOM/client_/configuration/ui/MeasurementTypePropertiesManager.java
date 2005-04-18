/*
 * $Id: MeasurementTypePropertiesManager.java,v 1.5 2005/04/18 10:45:17 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.configuration.ui;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.client_.general.ui_.*;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;
import com.syrus.AMFICOM.measurement.MeasurementTypeController;

/**
 * @author $Author: stas $
 * @version $Revision: 1.5 $, $Date: 2005/04/18 10:45:17 $
 * @module schemeclient_v1
 */

public class MeasurementTypePropertiesManager implements VisualManager {
	private static MeasurementTypePropertiesManager instance;
	private MeasurementTypeGeneralPanel generalPanel;
	private MeasurementTypeCharacteristicsPanel charPanel;
	
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

}
