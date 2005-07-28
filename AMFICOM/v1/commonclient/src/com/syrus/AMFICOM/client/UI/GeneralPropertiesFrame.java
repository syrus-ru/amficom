/*-
 * $Id: GeneralPropertiesFrame.java,v 1.2 2005/07/28 10:03:02 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.UI;

/**
 * @author $Author: bob $
 * @version $Revision: 1.2 $, $Date: 2005/07/28 10:03:02 $
 * @module commonclient_v1
 */

public class GeneralPropertiesFrame extends AbstractPropertiesFrame {
	public static final String	NAME	= "generalFrame";
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3257849861649807412L;

	public GeneralPropertiesFrame (String title) {
		super(title);
		setName(NAME);
	}
	
	/**
	 * @param manager
	 * @return GeneralPropertiesPane
	 * @see AbstractPropertiesFrame#getEditor VisualManager)
	 */
	protected StorableObjectEditor getEditor(VisualManager manager) {
		return manager.getGeneralPropertiesPanel();
	}
}
