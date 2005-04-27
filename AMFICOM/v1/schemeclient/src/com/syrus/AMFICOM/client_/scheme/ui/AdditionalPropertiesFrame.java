/*-
 * $Id: AdditionalPropertiesFrame.java,v 1.1 2005/04/27 08:47:29 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.client_.general.ui_.*;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/04/27 08:47:29 $
 * @module schemeclient_v1
 */

public class AdditionalPropertiesFrame extends AbstractSchemePropertiesFrame {

	public AdditionalPropertiesFrame (String title, ApplicationContext aContext) {
		super(title, aContext);
	}
	
	/**
	 * @param manager
	 * @return AdditionalPropertiesPanel
	 * @see AbstractPropertiesFrame#getEditor VisualManager)
	 */
	protected StorableObjectEditor getEditor(VisualManager manager) {
		return manager.getAdditionalPropertiesPanel();
	}
}
