/*-
 * $Id: ElementsEditor.java,v 1.3 2005/06/22 10:16:05 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme;

import java.awt.Image;

import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.Model.DefaultSchematicsApplicationModelFactory;
import com.syrus.AMFICOM.client.model.AbstractApplication;
import com.syrus.AMFICOM.resource.SchemeResourceKeys;

/**
 * @author $Author: stas $
 * @version $Revision: 1.3 $, $Date: 2005/06/22 10:16:05 $
 * @module schemeclient_v1
 */

public class ElementsEditor extends AbstractApplication {
	
	public static final String APPLICATION_NAME = "elements";
	
	public ElementsEditor() {
		super(APPLICATION_NAME);
	}		

	protected void init() {
		super.init();		
		super.aContext.setApplicationModel(new DefaultSchematicsApplicationModelFactory().create());
		super.startMainFrame(new ElementsEditorMainFrame(this.aContext), (Image) UIManager.get(SchemeResourceKeys.ICON_COMPONENTS));
	}
	
	public static void main(String[] args) {
		new ElementsEditor();
	}
}
