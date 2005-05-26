/*-
 * $Id: SchemeCableLinkPropertiesManager.java,v 1.2 2005/05/26 07:40:52 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import com.syrus.AMFICOM.client.UI.*;
import com.syrus.AMFICOM.client.UI.VisualManager;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.scheme.SchemeCableLinkWrapper;

/**
 * @author $Author: stas $
 * @version $Revision: 1.2 $, $Date: 2005/05/26 07:40:52 $
 * @module schemeclient_v1
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
		if (generalPanel == null)
			generalPanel = new SchemeCableLinkGeneralPanel();
		generalPanel.setContext(aContext);
		if (charPanel == null)
			charPanel = new SchemeCableLinkCharacteristicsPanel();
		charPanel.setContext(aContext);
		if (layout == null)
			layout = new SchemeCableLinkLayout();
		layout.setContext(aContext);
	}
	
	/**
	 * @return SchemeCableLinkGeneralPanel
	 * @see VisualManager#getGeneralPropertiesPanel()
	 */
	public StorableObjectEditor getGeneralPropertiesPanel() {
		return generalPanel;
	}

	/**
	 * @return SchemeCableLinkCharacteristicsPanel
	 * @see VisualManager#getCharacteristicPropertiesPanel()
	 */
	public StorableObjectEditor getCharacteristicPropertiesPanel() {
		return charPanel;
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
		return layout;
	}
}
