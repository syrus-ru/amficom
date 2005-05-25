/*-
 * $Id: CharacteristicPropertiesFrame.java,v 1.1 2005/05/25 07:55:08 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.UI;

/**
 * @author $Author: bob $
 * @version $Revision: 1.1 $, $Date: 2005/05/25 07:55:08 $
 * @module commonclient_v1
 */

public class CharacteristicPropertiesFrame extends AbstractPropertiesFrame {
	private static final long serialVersionUID = 3906931166434244400L;

	public CharacteristicPropertiesFrame(String title) {
		super(title);
	}
	
	/**
	 * @param manager
	 * @return CharacteristicPropertiesPane
	 * @see AbstractPropertiesFrame#getEditor(VisualManager)
	 */
	protected StorableObjectEditor getEditor(VisualManager manager) {
		return manager.getCharacteristicPropertiesPanel();
	}
}
