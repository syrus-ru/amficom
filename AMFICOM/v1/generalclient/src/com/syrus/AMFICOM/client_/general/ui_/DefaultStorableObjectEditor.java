/*-
 * $Id: DefaultStorableObjectEditor.java,v 1.1 2005/04/18 10:49:04 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.general.ui_;

import java.awt.event.*;
import java.awt.event.KeyAdapter;

import javax.swing.JComponent;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/04/18 10:49:04 $
 * @module generalclient_v1
 */

public abstract class DefaultStorableObjectEditor implements StorableObjectEditor {
	UndoableKeyAdapter keyAdapter;
	
	protected DefaultStorableObjectEditor() {
		keyAdapter = new UndoableKeyAdapter(this);
	}
	
	protected void addToUndoableListener(JComponent component) {
		component.addKeyListener(keyAdapter);
	}
	
	protected class UndoableKeyAdapter extends KeyAdapter {
		StorableObjectEditor editor;
		UndoableKeyAdapter(StorableObjectEditor editor) {
			this.editor = editor;
		}
		
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_ENTER)
				editor.commitChanges();
			if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
				editor.setObject(editor.getObject());
		}
	}
}
