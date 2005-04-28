/*-
 * $Id: SchemeEditor.java,v 1.2 2005/04/28 16:02:36 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme;

import java.awt.Toolkit;

import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.client_.general.ui_.SplashScreen;

/**
 * @author $Author: stas $
 * @version $Revision: 1.2 $, $Date: 2005/04/28 16:02:36 $
 * @module schemeclient_v1
 */

public class SchemeEditor {
	ApplicationContext aContext = new ApplicationContext();

	public SchemeEditor(SchematicsApplicationModelFactory factory) {
		if (!Environment.canRun(Environment.MODULE_SCHEMATICS))
			return;

		SplashScreen screen = SplashScreen.getDefaultSplashScreen();
		screen.setVisible(true);
		
		aContext.setApplicationModel(factory.create());
		aContext.setDispatcher(new Dispatcher());
		SchemeEditorMainFrame frame = new SchemeEditorMainFrame(aContext);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(
				"images/main/schematics_mini.gif"));
		screen.dispose();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		new SchemeEditor(new DefaultSchematicsApplicationModelFactory());
	}
}


