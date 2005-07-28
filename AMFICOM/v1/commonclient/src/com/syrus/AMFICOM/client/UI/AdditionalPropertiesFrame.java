/*-
 * $Id: AdditionalPropertiesFrame.java,v 1.3 2005/07/28 10:03:02 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.UI;


/**
 * @author $Author: bob $
 * @version $Revision: 1.3 $, $Date: 2005/07/28 10:03:02 $
 * @module commonclient_v1
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

