/*-
 * $Id: EmptyStorableObjectEditor.java,v 1.5 2005/07/11 12:31:39 stas Exp $
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
 * @author $Author: stas $
 * @version $Revision: 1.5 $, $Date: 2005/07/11 12:31:39 $
 * @module schemeclient_v1
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
		pnPanel0.setBackground(Color.WHITE);
	}

	public JComponent getGUI() {
		return pnPanel0;
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
