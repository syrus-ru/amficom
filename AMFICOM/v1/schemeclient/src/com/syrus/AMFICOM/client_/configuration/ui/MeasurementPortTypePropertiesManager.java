/*
 * $Id: MeasurementPortTypePropertiesManager.java,v 1.10 2005/08/20 19:58:10 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.configuration.ui;

import com.syrus.AMFICOM.client.UI.StorableObjectEditor;
import com.syrus.AMFICOM.client.UI.VisualManager;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client_.scheme.ui.EmptyStorableObjectEditor;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.measurement.MeasurementPortTypeWrapper;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.10 $, $Date: 2005/08/20 19:58:10 $
 * @module schemeclient
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
		if (this.generalPanel == null) {
			this.generalPanel = new MeasurementPortTypeGeneralPanel();
		}
		this.generalPanel.setContext(aContext);
		if (this.charPanel == null) {
			this.charPanel = new MeasurementPortTypeCharacteristicsPanel();
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
	 * @return MeasurementPortTypeController
	 * @see VisualManager#getController()
	 */
	public StorableObjectWrapper getController() {
		return MeasurementPortTypeWrapper.getInstance();
	}

	/**
	 * @return EmptyStorableObjectEditor
	 * @see VisualManager#getAdditionalPropertiesPanel()
	 */
	public StorableObjectEditor getAdditionalPropertiesPanel() {
		return this.emptyPanel;
	}
}
