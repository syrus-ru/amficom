/*-
 * $Id: SchemeLinkPropertiesManager.java,v 1.5 2005/08/05 12:40:00 stas Exp $
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
import com.syrus.AMFICOM.scheme.SchemeLinkWrapper;


/**
 * @author $Author: stas $
 * @version $Revision: 1.5 $, $Date: 2005/08/05 12:40:00 $
 * @module schemeclient_v1
 */

public class SchemeLinkPropertiesManager implements VisualManager {
	private static SchemeLinkPropertiesManager instance;
	private SchemeLinkGeneralPanel generalPanel;
	private SchemeLinkCharacteristicsPanel charPanel;
	private EmptyStorableObjectEditor emptyPanel;
	
	private SchemeLinkPropertiesManager() {
		// empty
	}
	
	public static SchemeLinkPropertiesManager getInstance(ApplicationContext aContext) {
		if (instance == null)
			instance = new SchemeLinkPropertiesManager();
		instance.setContext(aContext);
		return instance;
	}
	
	public void setContext(ApplicationContext aContext) {
		if (this.generalPanel == null) {
			this.generalPanel = new SchemeLinkGeneralPanel();
		}
		this.generalPanel.setContext(aContext);
		if (this.charPanel == null) {
			this.charPanel = new SchemeLinkCharacteristicsPanel();
		}
		this.charPanel.setContext(aContext);
		if (this.emptyPanel == null) {
			this.emptyPanel = new EmptyStorableObjectEditor();
		}
	}

	/**
	 * @return SchemeLinkGeneralPanel
	 * @see VisualManager#getGeneralPropertiesPanel()
	 */
	public StorableObjectEditor getGeneralPropertiesPanel() {
		return this.generalPanel;
	}

	/**
	 * @return SchemeLinkCharacteristicsPanel
	 * @see VisualManager#getCharacteristicPropertiesPanel()
	 */
	public StorableObjectEditor getCharacteristicPropertiesPanel() {
		return this.charPanel;
	}

	/**
	 * @return SchemeLinkController
	 * @see VisualManager#getController()
	 */
	public StorableObjectWrapper getController() {
		return SchemeLinkWrapper.getInstance();
	}
	
	/**
	 * @return EmptyStorableObjectEditor
	 * @see VisualManager#getAdditionalPropertiesPanel()
	 */
	public StorableObjectEditor getAdditionalPropertiesPanel() {
		return this.emptyPanel;
	}
}
