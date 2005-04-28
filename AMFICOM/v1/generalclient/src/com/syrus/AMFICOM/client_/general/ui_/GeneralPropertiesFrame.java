/*
 * $Id: GeneralPropertiesFrame.java,v 1.1 2005/04/28 11:53:17 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.general.ui_;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/04/28 11:53:17 $
 * @module generalclient_v1
 */

public class GeneralPropertiesFrame extends AbstractPropertiesFrame {
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3257849861649807412L;

	public GeneralPropertiesFrame (String title) {
		super(title);
	}
	
	/**
	 * @param manager
	 * @return GeneralPropertiesPane
	 * @see com.syrus.AMFICOM.client_.general.ui_.AbstractPropertiesFrame#getEditor VisualManager)
	 */
	protected StorableObjectEditor getEditor(VisualManager manager) {
		return manager.getGeneralPropertiesPanel();
	}
}
