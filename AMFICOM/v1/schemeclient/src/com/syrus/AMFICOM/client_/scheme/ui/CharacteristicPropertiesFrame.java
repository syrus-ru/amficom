/*
 * $Id: CharacteristicPropertiesFrame.java,v 1.1 2005/03/10 08:09:08 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourcePropertiesPane;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/03/10 08:09:08 $
 * @module schemeclient_v1
 */

public class CharacteristicPropertiesFrame extends AbstractPropertiesFrame {
	
	public CharacteristicPropertiesFrame(String title, ApplicationContext aContext) {
		super(title, aContext);
	}
	
	/**
	 * @param manager
	 * @return CharacteristicPropertiesPane
	 * @see com.syrus.AMFICOM.client_.scheme.ui.AbstractPropertiesFrame#getPropertiesPane(com.syrus.AMFICOM.client_.scheme.ui.PropertiesFrameMananager)
	 */
	protected ObjectResourcePropertiesPane getPropertiesPane(
			PropertiesMananager manager) {
		return manager.getCharacteristicPropertiesPanel();
	}
}
