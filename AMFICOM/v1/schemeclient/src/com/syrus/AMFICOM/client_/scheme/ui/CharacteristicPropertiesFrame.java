/*
 * $Id: CharacteristicPropertiesFrame.java,v 1.4 2005/04/22 07:32:50 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.client_.general.ui_.*;

/**
 * @author $Author: stas $
 * @version $Revision: 1.4 $, $Date: 2005/04/22 07:32:50 $
 * @module schemeclient_v1
 */

public class CharacteristicPropertiesFrame extends AbstractSchemePropertiesFrame {
	private static final long serialVersionUID = 3906931166434244400L;

	public CharacteristicPropertiesFrame(String title, ApplicationContext aContext) {
		super(title, aContext);
	}
	
	/**
	 * @param manager
	 * @return CharacteristicPropertiesPane
	 * @see com.syrus.AMFICOM.client_.general.ui_.AbstractPropertiesFrame#getEditor(VisualManager)
	 */
	protected StorableObjectEditor getEditor(VisualManager manager) {
		return manager.getCharacteristicPropertiesPanel();
	}
}
