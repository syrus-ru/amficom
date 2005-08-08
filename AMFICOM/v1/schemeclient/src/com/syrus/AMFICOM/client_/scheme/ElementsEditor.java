/*-
 * $Id: ElementsEditor.java,v 1.5 2005/08/08 11:58:06 arseniy Exp $
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
 * @author $Author: arseniy $
 * @version $Revision: 1.5 $, $Date: 2005/08/08 11:58:06 $
 * @module schemeclient
 */

public class ElementsEditor extends AbstractApplication {
	
	public static final String APPLICATION_NAME = "elements";
	
	public ElementsEditor() {
		super(APPLICATION_NAME);
	}		

	@Override
	protected void init() {
		super.init();		
		super.aContext.setApplicationModel(new DefaultSchematicsApplicationModelFactory().create());
		super.startMainFrame(new ElementsEditorMainFrame(this.aContext), (Image) UIManager.get(SchemeResourceKeys.ICON_COMPONENTS));
	}
	
	public static void main(String[] args) {
		new ElementsEditor();
	}
}
