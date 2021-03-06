/*-
 * $Id: DefaultStorableObjectEditor.java,v 1.5 2005/05/18 14:01:19 bass Exp $
 *
 * Copyright ? 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.general.ui_;

import java.awt.event.*;
import java.awt.event.KeyAdapter;
import java.util.*;
import java.util.LinkedList;

import javax.swing.JComponent;
import javax.swing.event.*;
import javax.swing.event.ChangeListener;

/**
 * @author $Author: bass $
 * @version $Revision: 1.5 $, $Date: 2005/05/18 14:01:19 $
 * @module generalclient_v1
 */

public abstract class DefaultStorableObjectEditor implements StorableObjectEditor {
	List changeListeners = new LinkedList();
	UndoableKeyAdapter keyAdapter;
	
	protected DefaultStorableObjectEditor() {
		keyAdapter = new UndoableKeyAdapter(this);
	}
	
	public void addChangeListener(ChangeListener listener) {
		changeListeners.add(listener);
	}
	
	public void removeChangeListener(ChangeListener listener) {
		changeListeners.remove(listener);
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
			if(e.getKeyCode() == KeyEvent.VK_ENTER) {
				editor.commitChanges();
				Object obj = editor.getObject();
				if (obj != null)
					for (Iterator it = changeListeners.iterator(); it.hasNext();)
						((ChangeListener)it.next()).stateChanged(new ChangeEvent(obj));
			}
			if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
				editor.setObject(editor.getObject());
		}
	}
}
