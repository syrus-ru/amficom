/*-
 * $Id: EmptyStorableObjectEditor.java,v 1.7 2005/08/08 11:58:07 arseniy Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import java.awt.Color;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.syrus.AMFICOM.client.UI.DefaultStorableObjectEditor;


/**
 * @author $Author: arseniy $
 * @version $Revision: 1.7 $, $Date: 2005/08/08 11:58:07 $
 * @module schemeclient
 */

public class EmptyStorableObjectEditor extends DefaultStorableObjectEditor {
	JPanel pnPanel0 = new JPanel();
	
	public EmptyStorableObjectEditor() {
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void jbInit() throws Exception {
		this.pnPanel0.setBackground(Color.WHITE);
	}

	public JComponent getGUI() {
		return this.pnPanel0;
	}

	public void setObject(Object object) {
		// nothing
	}

	public Object getObject() {
		return null;
	}

	public void commitChanges() {
		// nothing
	}
}
