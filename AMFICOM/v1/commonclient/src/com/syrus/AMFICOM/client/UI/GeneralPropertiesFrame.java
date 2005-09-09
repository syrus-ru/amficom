/*-
 * $Id: GeneralPropertiesFrame.java,v 1.4 2005/09/09 18:54:27 arseniy Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.UI;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.4 $, $Date: 2005/09/09 18:54:27 $
 * @module commonclient
 */

public class GeneralPropertiesFrame extends AbstractPropertiesFrame {
	private static final long serialVersionUID = 3257849861649807412L;

	public static final String	NAME	= "generalFrame";

	public GeneralPropertiesFrame (final String title) {
		super(title);
		super.setName(NAME);
	}
	
	/**
	 * @param manager
	 * @return GeneralPropertiesPane
	 * @see AbstractPropertiesFrame#getEditor VisualManager)
	 */
	@Override
	protected StorableObjectEditor getEditor(final VisualManager manager) {
		return manager.getGeneralPropertiesPanel();
	}
}
