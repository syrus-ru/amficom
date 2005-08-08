/*-
 * $Id: SchemeCableLinkPropertiesManager.java,v 1.5 2005/08/08 11:58:07 arseniy Exp $
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
import com.syrus.AMFICOM.scheme.SchemeCableLinkWrapper;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.5 $, $Date: 2005/08/08 11:58:07 $
 * @module schemeclient
 */

public class SchemeCableLinkPropertiesManager implements VisualManager {
	private static SchemeCableLinkPropertiesManager instance;
	private SchemeCableLinkGeneralPanel generalPanel;
	private SchemeCableLinkCharacteristicsPanel charPanel;
	private SchemeCableLinkLayout layout;
	
	private SchemeCableLinkPropertiesManager() {
		// empty
	}
	
	public static SchemeCableLinkPropertiesManager getInstance(ApplicationContext aContext) {
		if (instance == null)
			instance = new SchemeCableLinkPropertiesManager();
		instance.setContext(aContext);
		return instance;
	}

	public void setContext(ApplicationContext aContext) {
		if (this.generalPanel == null) {
			this.generalPanel = new SchemeCableLinkGeneralPanel();
		}
		this.generalPanel.setContext(aContext);
		if (this.charPanel == null) {
			this.charPanel = new SchemeCableLinkCharacteristicsPanel();
		}
		this.charPanel.setContext(aContext);
		if (this.layout == null) {
			this.layout = new SchemeCableLinkLayout();
		}
		this.layout.setContext(aContext);
	}
	
	/**
	 * @return SchemeCableLinkGeneralPanel
	 * @see VisualManager#getGeneralPropertiesPanel()
	 */
	public StorableObjectEditor getGeneralPropertiesPanel() {
		return this.generalPanel;
	}

	/**
	 * @return SchemeCableLinkCharacteristicsPanel
	 * @see VisualManager#getCharacteristicPropertiesPanel()
	 */
	public StorableObjectEditor getCharacteristicPropertiesPanel() {
		return this.charPanel;
	}

	/**
	 * @return SchemeCableLinkController
	 * @see VisualManager#getController()
	 */
	public StorableObjectWrapper getController() {
		return SchemeCableLinkWrapper.getInstance();
	}

	/**
	 * @return SchemeCableLinkLayout
	 * @see VisualManager#getAdditionalPropertiesPanel()
	 */
	public StorableObjectEditor getAdditionalPropertiesPanel() {
		return this.layout;
	}
}
