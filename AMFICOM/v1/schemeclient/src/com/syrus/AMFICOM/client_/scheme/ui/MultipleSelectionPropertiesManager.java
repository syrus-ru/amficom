/*-
 * $Id: MultipleSelectionPropertiesManager.java,v 1.1 2006/06/01 14:36:32 stas Exp $
 *
 * Copyright ¿ 2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.client.UI.StorableObjectEditor;
import com.syrus.AMFICOM.client.UI.VisualManager;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.PropertyChangeException;

public class MultipleSelectionPropertiesManager implements VisualManager {
	private static MultipleSelectionPropertiesManager instance;
	private MultipleSelectionGeneralPanel generalPanel;
	private EmptyStorableObjectEditor charPanel;
	private EmptyStorableObjectEditor ugoPanel;
	private static StorableObjectWrapper emptyWrapper;

	@SuppressWarnings("unused")
	private MultipleSelectionPropertiesManager() {
		emptyWrapper = new StorableObjectWrapper<StorableObject>() {
			@Override
			public void setValue(StorableObject storableObject, String key, Object value) throws PropertyChangeException {
				// nothing
			}
			public List<String> getKeys() {
				return Collections.emptyList();
			}
			public String getName(String key) {
				return key;
			}
			public Object getPropertyValue(String key) {
				return key;
			}
			public void setPropertyValue(String key, Object objectKey, Object objectValue) {
				// nothing
			}
			public boolean isEditable(String key) {
				return false;
			}
		};
	}
	
	public static MultipleSelectionPropertiesManager getInstance(ApplicationContext aContext) {
		if (instance == null)
			instance = new MultipleSelectionPropertiesManager();
		instance.setContext(aContext);
		return instance;
	}
	
	public void setContext(ApplicationContext aContext) {
		if (this.generalPanel == null) {
			this.generalPanel = new MultipleSelectionGeneralPanel();
		}
		this.generalPanel.setContext(aContext);
	}
	
	/**
	 * @return SchemeElementGeneralPanel
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
		if (this.charPanel == null) {
			this.charPanel = new EmptyStorableObjectEditor();
		}
		return this.charPanel;
	}

	/**
	 * @return emptyWrapper
	 * @see VisualManager#getController()
	 */
	public StorableObjectWrapper getController() {
		return emptyWrapper;
	}

	/**
	 * @return SchemeElementUgoPanel
	 * @see VisualManager#getAdditionalPropertiesPanel()
	 */
	public StorableObjectEditor getAdditionalPropertiesPanel() {
		if (this.ugoPanel == null) {
			this.ugoPanel = new EmptyStorableObjectEditor();
		}
		return this.ugoPanel;

	}
}
