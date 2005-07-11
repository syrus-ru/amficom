/*-
 * $Id: SchemeDevicePropertiesManager.java,v 1.5 2005/07/11 12:31:39 stas Exp $
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
 * @author $Author: stas $
 * @version $Revision: 1.5 $, $Date: 2005/07/11 12:31:39 $
 * @module schemeclient_v1
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
		if (generalPane == null)
			generalPane = new SchemeDeviceGeneralPanel();
		generalPane.setContext(aContext);
	}
	
	/**
	 * @return EmptyStorableObjectEditor
	 * @see VisualManager#getGeneralPropertiesPanel()
	 */
	public StorableObjectEditor getGeneralPropertiesPanel() {
		return generalPane;
	}

	/**
	 * @return EmptyStorableObjectEditor
	 * @see VisualManager#getCharacteristicPropertiesPanel()
	 */
	public StorableObjectEditor getCharacteristicPropertiesPanel() {
		if (charPane == null)
			charPane = new EmptyStorableObjectEditor();
		return charPane;
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
		if (emptyPanel == null)
			emptyPanel = new EmptyStorableObjectEditor();
		return emptyPanel;
	}
}
