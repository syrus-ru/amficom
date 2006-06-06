/*-
 * $Id: EmptyStorableObjectEditor.java,v 1.9 2006/06/06 12:41:55 stas Exp $
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
import com.syrus.AMFICOM.general.Identifiable;


/**
 * @author $Author: stas $
 * @version $Revision: 1.9 $, $Date: 2006/06/06 12:41:55 $
 * @module schemeclient
 */

public class EmptyStorableObjectEditor extends DefaultStorableObjectEditor<Identifiable> {
	JPanel pnPanel0 = new JPanel();
	
	public EmptyStorableObjectEditor() {
		this.pnPanel0.setBackground(Color.WHITE);
	}
	
	public JComponent getGUI() {
		return this.pnPanel0;
	}

	public void setObject(Identifiable object) {
		// nothing
	}

	public Identifiable getObject() {
		return null;
	}

	@Override
	public void commitChanges() {
		// nothing
	}
}
