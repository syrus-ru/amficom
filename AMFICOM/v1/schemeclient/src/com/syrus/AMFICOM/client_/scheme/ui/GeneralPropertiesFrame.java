/*
 * $Id: GeneralPropertiesFrame.java,v 1.3 2005/03/30 13:33:39 stas Exp $
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
 * @version $Revision: 1.3 $, $Date: 2005/03/30 13:33:39 $
 * @module schemeclient_v1
 */

public class GeneralPropertiesFrame extends AbstractPropertiesFrame {
	
	public GeneralPropertiesFrame (String title, ApplicationContext aContext) {
		super(title, aContext);
	}
	
	/**
	 * @param manager
	 * @return GeneralPropertiesPane
	 * @see com.syrus.AMFICOM.client_.scheme.ui.AbstractPropertiesFrame#getEditor VisualManager)
	 */
	protected StorableObjectEditor getEditor(VisualManager manager) {
		return manager.getGeneralPropertiesPanel();
	}
}
