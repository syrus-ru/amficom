/*-
 * $Id: AdditionalPropertiesFrame.java,v 1.5 2005/09/09 18:54:27 arseniy Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.UI;


/**
 * @author $Author: arseniy $
 * @version $Revision: 1.5 $, $Date: 2005/09/09 18:54:27 $
 * @module commonclient
 */

public class AdditionalPropertiesFrame extends AbstractPropertiesFrame {
	private static final long serialVersionUID = 3906083473134270773L;

	public static final String NAME = "additionalFrame";

	public AdditionalPropertiesFrame(final String title) {
		super(title);
		super.setName(NAME);
	}

	/**
	 * @param manager
	 * @return AdditionalPropertiesPanel
	 * @see AbstractPropertiesFrame#getEditor VisualManager)
	 */
	@Override
	public StorableObjectEditor getEditor(final VisualManager manager) {
		return manager.getAdditionalPropertiesPanel();
	}	
	
}

