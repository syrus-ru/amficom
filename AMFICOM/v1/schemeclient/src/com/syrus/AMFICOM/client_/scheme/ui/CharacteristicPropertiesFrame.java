/*
 * $Id: CharacteristicPropertiesFrame.java,v 1.2 2005/03/14 13:36:19 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.client_.general.ui_.StorableObjectEditor;

/**
 * @author $Author: stas $
 * @version $Revision: 1.2 $, $Date: 2005/03/14 13:36:19 $
 * @module schemeclient_v1
 */

public class CharacteristicPropertiesFrame extends AbstractPropertiesFrame {
	
	public CharacteristicPropertiesFrame(String title, ApplicationContext aContext) {
		super(title, aContext);
	}
	
	/**
	 * @param manager
	 * @return CharacteristicPropertiesPane
	 * @see com.syrus.AMFICOM.client_.scheme.ui.AbstractPropertiesFrame#getEditor(PropertiesMananager)
	 */
	protected StorableObjectEditor getEditor(PropertiesMananager manager) {
		return manager.getCharacteristicPropertiesPanel();
	}
}
