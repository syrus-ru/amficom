/*-
 * $Id: AdditionalPropertiesFrame.java,v 1.4 2005/08/02 13:03:21 arseniy Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.UI;


/**
 * @author $Author: arseniy $
 * @version $Revision: 1.4 $, $Date: 2005/08/02 13:03:21 $
 * @module commonclient
 */

public class AdditionalPropertiesFrame extends AbstractPropertiesFrame {
	public static final String	NAME = "additionalFrame";
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3906083473134270773L;

	public AdditionalPropertiesFrame (String title) {
		super(title);
		setName(NAME);
	}
	
	/**
	 * @param manager
	 * @return AdditionalPropertiesPanel
	 * @see AbstractPropertiesFrame#getEditor VisualManager)
	 */
	public StorableObjectEditor getEditor(VisualManager manager) {
		return manager.getAdditionalPropertiesPanel();
	}	
	
}

