/*-
 * $Id: SchemeDevicePropertiesManager.java,v 1.7 2005/08/08 11:58:07 arseniy Exp $
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
import com.syrus.AMFICOM.scheme.SchemeDeviceWrapper;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.7 $, $Date: 2005/08/08 11:58:07 $
 * @module schemeclient
 */

public class SchemeDevicePropertiesManager implements VisualManager {
	private static SchemeDevicePropertiesManager instance;
	private SchemeDeviceGeneralPanel generalPane;
	private EmptyStorableObjectEditor charPane;
	private EmptyStorableObjectEditor emptyPanel;
	
	private SchemeDevicePropertiesManager() {
		// empty
	}
	
	public static SchemeDevicePropertiesManager getInstance(ApplicationContext aContext) {
		if (instance == null)
			instance = new SchemeDevicePropertiesManager();
		instance.setContext(aContext);
		return instance;
	}

	public void setContext(ApplicationContext aContext) {
		if (this.generalPane == null) {
			this.generalPane = new SchemeDeviceGeneralPanel();
		}
		this.generalPane.setContext(aContext);
	}
	
	/**
	 * @return EmptyStorableObjectEditor
	 * @see VisualManager#getGeneralPropertiesPanel()
	 */
	public StorableObjectEditor getGeneralPropertiesPanel() {
		return this.generalPane;
	}

	/**
	 * @return EmptyStorableObjectEditor
	 * @see VisualManager#getCharacteristicPropertiesPanel()
	 */
	public StorableObjectEditor getCharacteristicPropertiesPanel() {
		if (this.charPane == null) {
			this.charPane = new EmptyStorableObjectEditor();
		}
		return this.charPane;
	}

	/**
	 * @return SchemeDeviceController
	 * @see VisualManager#getController()
	 */
	public StorableObjectWrapper getController() {
		return SchemeDeviceWrapper.getInstance();
	}
	
	/**
	 * @return EmptyStorableObjectEditor
	 * @see VisualManager#getAdditionalPropertiesPanel()
	 */
	public StorableObjectEditor getAdditionalPropertiesPanel() {
		if (this.emptyPanel == null) {
			this.emptyPanel = new EmptyStorableObjectEditor();
		}
		return this.emptyPanel;
	}
}
