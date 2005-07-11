/*-
 * $Id: SchemePropertiesManager.java,v 1.2 2005/07/11 12:31:39 stas Exp $
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
import com.syrus.AMFICOM.scheme.SchemeWrapper;


/**
 * @author $Author: stas $
 * @version $Revision: 1.2 $, $Date: 2005/07/11 12:31:39 $
 * @module schemeclient_v1
 */

public class SchemePropertiesManager implements VisualManager {
	private static SchemePropertiesManager instance;
	private SchemeGeneralPanel generalPanel;
	private EmptyStorableObjectEditor charPanel;
	private SchemeCellPanel ugoPanel;
	
	private SchemePropertiesManager() {
		// empty
	}
	
	public static SchemePropertiesManager getInstance(ApplicationContext aContext) {
		if (instance == null)
			instance = new SchemePropertiesManager();
		instance.setContext(aContext);
		return instance;
	}
	
	public void setContext(ApplicationContext aContext) {
		if (generalPanel == null)
			generalPanel = new SchemeGeneralPanel();
		generalPanel.setContext(aContext);
		if (charPanel == null)
			charPanel = new EmptyStorableObjectEditor();
		if (ugoPanel == null)
			ugoPanel = new SchemeCellPanel();
		ugoPanel.setContext(aContext);
	}
	
	/**
	 * @return SchemeGeneralPanel
	 * @see VisualManager#getGeneralPropertiesPanel()
	 */
	public StorableObjectEditor getGeneralPropertiesPanel() {
		return generalPanel;
	}

	/**
	 * @return EmptyStorableObjectEditor
	 * @see VisualManager#getCharacteristicPropertiesPanel()
	 */
	public StorableObjectEditor getCharacteristicPropertiesPanel() {
		return charPanel;
	}

	/**
	 * @return SchemeController
	 * @see VisualManager#getController()
	 */
	public StorableObjectWrapper getController() {
		return SchemeWrapper.getInstance();
	}

	/**
	 * @return SchemeUgoPanel
	 * @see VisualManager#getAdditionalPropertiesPanel()
	 */
	public StorableObjectEditor getAdditionalPropertiesPanel() {
		return ugoPanel;
	}
}
