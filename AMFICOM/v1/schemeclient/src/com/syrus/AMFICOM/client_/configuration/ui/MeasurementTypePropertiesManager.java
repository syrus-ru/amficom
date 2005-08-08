/*
 * $Id: MeasurementTypePropertiesManager.java,v 1.10 2005/08/08 11:58:06 arseniy Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.configuration.ui;

import com.syrus.AMFICOM.client.UI.StorableObjectEditor;
import com.syrus.AMFICOM.client.UI.VisualManager;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client_.scheme.ui.EmptyStorableObjectEditor;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.measurement.MeasurementTypeWrapper;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.10 $, $Date: 2005/08/08 11:58:06 $
 * @module schemeclient
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
		if (this.generalPanel == null) {
			this.generalPanel = new MeasurementTypeGeneralPanel();
		}
		this.generalPanel.setContext(aContext);
		if (this.charPanel == null) {
			this.charPanel = new MeasurementTypeCharacteristicsPanel();
		}
		this.charPanel.setContext(aContext);
		if (this.emptyPanel == null) {
			this.emptyPanel = new EmptyStorableObjectEditor();
		}
	}
	
	/**
	 * @return AbstractLinkTypeGeneralPanel
	 * @see VisualManager#getGeneralPropertiesPanel()
	 */
	public StorableObjectEditor getGeneralPropertiesPanel() {
		return this.generalPanel;
	}

	/**
	 * @return LinkTypeCharacteristicsPanel
	 * @see VisualManager#getCharacteristicPropertiesPanel()
	 */
	public StorableObjectEditor getCharacteristicPropertiesPanel() {
		return this.charPanel;
	}

	/**
	 * @return MeasurementTypeController
	 * @see VisualManager#getController()
	 */
	public StorableObjectWrapper getController() {
		return MeasurementTypeWrapper.getInstance();
	}

	/**
	 * @return EmptyStorableObjectEditor
	 * @see VisualManager#getAdditionalPropertiesPanel()
	 */
	public StorableObjectEditor getAdditionalPropertiesPanel() {
		return this.emptyPanel;
	}
}
