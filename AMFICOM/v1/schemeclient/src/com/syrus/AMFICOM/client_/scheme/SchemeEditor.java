/*-
 * $Id: SchemeEditor.java,v 1.3 2005/05/26 07:40:51 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme;

import java.awt.Toolkit;

import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.client.UI.SplashScreen;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.model.*;

/**
 * @author $Author: stas $
 * @version $Revision: 1.3 $, $Date: 2005/05/26 07:40:51 $
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


