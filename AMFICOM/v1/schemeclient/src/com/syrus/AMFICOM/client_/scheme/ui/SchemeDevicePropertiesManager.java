/*-
 * $Id: SchemeDevicePropertiesManager.java,v 1.1 2005/04/18 10:45:17 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.client_.general.ui_.*;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/04/18 10:45:17 $
 * @module schemeclient_v1
 */

public class SchemeDevicePropertiesManager implements VisualManager {
	private static SchemeDevicePropertiesManager instance;
	private EmptyStorableObjectEditor emptyEditor;
	
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
	}
	
	/**
	 * @return EmptyStorableObjectEditor
	 * @see VisualManager#getGeneralPropertiesPanel()
	 */
	public StorableObjectEditor getGeneralPropertiesPanel() {
		if (emptyEditor == null)
			emptyEditor = new EmptyStorableObjectEditor();
		return emptyEditor;
	}

	/**
	 * @return EmptyStorableObjectEditor
	 * @see VisualManager#getCharacteristicPropertiesPanel()
	 */
	public StorableObjectEditor getCharacteristicPropertiesPanel() {
		if (emptyEditor == null)
			emptyEditor = new EmptyStorableObjectEditor();
		return emptyEditor;
	}

	/**
	 * @return null
	 * @see VisualManager#getController()
	 */
	public ObjectResourceController getController() {
		return null;
	}
}
