/*-
 * $Id: MultipleSelectionGeneralPanel.java,v 1.1 2006/06/01 14:36:32 stas Exp $
 *
 * Copyright ¿ 2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import javax.swing.JComponent;
import javax.swing.JLabel;

import com.syrus.AMFICOM.client.UI.DefaultStorableObjectEditor;
import com.syrus.AMFICOM.client.model.ApplicationContext;

public class MultipleSelectionGeneralPanel extends DefaultStorableObjectEditor {
	ApplicationContext aContext;
	
	public void setContext(final ApplicationContext aContext) {
		this.aContext = aContext;
	}
	
	public JComponent getGUI() {
		return new JLabel("X");
	}

	public void setObject(Object object) {
		// TODO Auto-generated method stub
		
	}

	public Object getObject() {
		// TODO Auto-generated method stub
		return null;
	}
}
