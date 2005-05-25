/*-
 * $Id: AdditionalPropertiesFrame.java,v 1.1 2005/05/25 07:55:08 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.UI;

import javax.swing.event.ChangeEvent;

/**
 * @author $Author: bob $
 * @version $Revision: 1.1 $, $Date: 2005/05/25 07:55:08 $
 * @module commonclient_v1
 */

public class AdditionalPropertiesFrame extends AbstractPropertiesFrame {
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3906083473134270773L;

	public AdditionalPropertiesFrame (String title) {
		super(title);
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

