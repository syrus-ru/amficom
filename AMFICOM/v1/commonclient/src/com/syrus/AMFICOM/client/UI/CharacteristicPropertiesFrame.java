/*-
 * $Id: CharacteristicPropertiesFrame.java,v 1.3 2005/08/02 13:03:21 arseniy Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.UI;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.3 $, $Date: 2005/08/02 13:03:21 $
 * @module commonclient
 */

public class CharacteristicPropertiesFrame extends AbstractPropertiesFrame {
	public static final String	NAME = "characteristicFrame";
	private static final long serialVersionUID = 3906931166434244400L;

	public CharacteristicPropertiesFrame(String title) {
		super(title);
		setName(NAME);
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
