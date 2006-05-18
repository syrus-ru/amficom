/*-
 * $Id: SchemePropertiesManager.java,v 1.4 2005/08/08 11:58:08 arseniy Exp $
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
 * @author $Author: arseniy $
 * @version $Revision: 1.4 $, $Date: 2005/08/08 11:58:08 $
 * @module schemeclient
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
		if (this.generalPanel == null) {
			this.generalPanel = new SchemeGeneralPanel();
		}
		this.generalPanel.setContext(aContext);
		if (this.charPanel == null) {
			this.charPanel = new EmptyStorableObjectEditor();
		}
		if (this.ugoPanel == null) {
			this.ugoPanel = new SchemeCellPanel();
		}
		this.ugoPanel.setContext(aContext);
	}
	
	/**
	 * @return SchemeGeneralPanel
	 * @see VisualManager#getGeneralPropertiesPanel()
	 */
	public StorableObjectEditor getGeneralPropertiesPanel() {
		return this.generalPanel;
	}

	/**
	 * @return EmptyStorableObjectEditor
	 * @see VisualManager#getCharacteristicPropertiesPanel()
	 */
	public StorableObjectEditor getCharacteristicPropertiesPanel() {
		return this.charPanel;
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
		return this.ugoPanel;
	}
}
