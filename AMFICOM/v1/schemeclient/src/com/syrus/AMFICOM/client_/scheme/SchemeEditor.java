/*-
 * $Id: SchemeEditor.java,v 1.1 2005/04/05 14:10:45 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme;

import java.awt.Toolkit;

import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Model.*;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/04/05 14:10:45 $
 * @module schemeclient_v1
 */

public class SchemeEditor {
	ApplicationContext aContext = new ApplicationContext();

	public SchemeEditor(SchematicsApplicationModelFactory factory) {
		if (!Environment.canRun(Environment.MODULE_SCHEMATICS))
			return;

		aContext.setApplicationModel(factory.create());
		aContext.setDispatcher(new Dispatcher());
		SchemeEditorMainFrame frame = new SchemeEditorMainFrame(aContext);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(
				"images/main/schematics_mini.gif"));
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		new SchemeEditor(new DefaultSchematicsApplicationModelFactory());
	}
}


