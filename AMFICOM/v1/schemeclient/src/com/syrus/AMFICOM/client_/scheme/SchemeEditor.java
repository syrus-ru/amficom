/*-
 * $Id: SchemeEditor.java,v 1.8 2005/09/21 14:20:17 stas Exp $
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
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.resource.SchemeResourceKeys;

/**
 * @author $Author: stas $
 * @version $Revision: 1.8 $, $Date: 2005/09/21 14:20:17 $
 * @module schemeclient
 */

public class SchemeEditor extends AbstractApplication {
	public static final String APPLICATION_NAME = "scheme";
	ApplicationContext aContext = new ApplicationContext();

	public SchemeEditor() {
		super(APPLICATION_NAME);
	}
//		SplashScreen screen = SplashScreen.getDefaultSplashScreen();
//		screen.setVisible(true);
//screen.dispose();
	
	@Override
	protected void init() {
		super.aContext.setApplicationModel(new DefaultSchematicsApplicationModelFactory().create());
		super.startMainFrame(new SchemeEditorMainFrame(super.aContext), (Image)UIManager.get(SchemeResourceKeys.ICON_SCHEMATICS));
	}

	public static void main(String[] args) {
		new SchemeEditor();
	}
}


