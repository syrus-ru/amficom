/*-
 * $Id: DefaultStorableObjectEditor.java,v 1.1 2005/05/25 07:55:08 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.UI;

import java.awt.event.*;
import java.util.*;

import javax.swing.JComponent;
import javax.swing.event.*;

/**
 * @author $Author: bob $
 * @version $Revision: 1.1 $, $Date: 2005/05/25 07:55:08 $
 * @module commonclient_v1
 */

public abstract class DefaultStorableObjectEditor implements StorableObjectEditor {
	List changeListeners = new LinkedList(); 
	UndoableKeyAdapter keyAdapter;
	
	protected DefaultStorableObjectEditor() {
		this.keyAdapter = new UndoableKeyAdapter(this);
	}
	
	public void addChangeListener(ChangeListener listener) {
		this.changeListeners.add(listener);
	}
	
	public void removeChangeListener(ChangeListener listener) {
		this.changeListeners.remove(listener);
	}
	
	protected void addToUndoableListener(JComponent component) {
		component.addKeyListener(this.keyAdapter);
	}
	
	protected class UndoableKeyAdapter extends KeyAdapter {
		StorableObjectEditor editor;
		UndoableKeyAdapter(StorableObjectEditor editor) {
			this.editor = editor;
		}
		
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_ENTER) {
				this.editor.commitChanges();
				Object obj = this.editor.getObject();
				if (obj != null)
					for (Iterator it = DefaultStorableObjectEditor.this.changeListeners.iterator(); it.hasNext();) {
						((ChangeListener)it.next()).stateChanged(new ChangeEvent(obj));
					}
			}
			if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
				this.editor.setObject(this.editor.getObject());
		}
	}
}
