/*-
 * $Id: EmptyStorableObjectEditor.java,v 1.3 2005/04/27 08:47:29 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import java.awt.Color;

import javax.swing.*;

import com.syrus.AMFICOM.client_.general.ui_.DefaultStorableObjectEditor;

/**
 * @author $Author: stas $
 * @version $Revision: 1.3 $, $Date: 2005/04/27 08:47:29 $
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
