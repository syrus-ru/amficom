/*-
 * $Id: ElementsEditor.java,v 1.1 2005/04/05 14:10:45 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme;

import java.awt.Toolkit;

import com.syrus.AMFICOM.Client.General.Model.*;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/04/05 14:10:45 $
 * @module schemeclient_v1
 */

public class ElementsEditor {
	ApplicationContext aContext = new ApplicationContext();

	public ElementsEditor(SchematicsApplicationModelFactory factory) {
		if (!Environment.canRun(Environment.MODULE_COMPONENTS))
			return;

		aContext.setApplicationModel(factory.create());
		ElementsEditorMainFrame frame = new ElementsEditorMainFrame(aContext);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(
				"images/main/components_mini.gif"));
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		new ElementsEditor(new DefaultSchematicsApplicationModelFactory());
	}
}
