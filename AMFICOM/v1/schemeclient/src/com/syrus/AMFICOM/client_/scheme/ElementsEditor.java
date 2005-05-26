/*-
 * $Id: ElementsEditor.java,v 1.2 2005/05/26 07:40:51 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme;

import java.awt.Toolkit;

import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.client.model.*;

/**
 * @author $Author: stas $
 * @version $Revision: 1.2 $, $Date: 2005/05/26 07:40:51 $
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
