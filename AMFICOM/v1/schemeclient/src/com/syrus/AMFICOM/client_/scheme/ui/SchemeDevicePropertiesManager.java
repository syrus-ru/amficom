/*-
 * $Id: SchemeDevicePropertiesManager.java,v 1.2 2005/04/22 07:32:50 stas Exp $
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
 * @version $Revision: 1.2 $, $Date: 2005/04/22 07:32:50 $
 * @module schemeclient_v1
 */

public class SchemeDevicePropertiesManager implements VisualManager {
	private static SchemeDevicePropertiesManager instance;
	private EmptyStorableObjectEditor generalPane;
	private EmptyStorableObjectEditor charPane;
	
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
		if (generalPane == null)
			generalPane = new EmptyStorableObjectEditor();
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
	 * @return null
	 * @see VisualManager#getController()
	 */
	public ObjectResourceController getController() {
		return null;
	}
}
