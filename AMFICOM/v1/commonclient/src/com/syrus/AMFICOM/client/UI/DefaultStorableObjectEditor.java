/*-
 * $Id: DefaultStorableObjectEditor.java,v 1.3 2005/08/19 12:45:55 bob Exp $
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
 * @version $Revision: 1.3 $, $Date: 2005/08/19 12:45:55 $
 * @module commonclient
 */

public abstract class DefaultStorableObjectEditor implements StorableObjectEditor {
	List<ChangeListener> changeListeners = new LinkedList<ChangeListener>(); 
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
	
	public Collection<ChangeListener> getChangeListeners() {
		return Collections.unmodifiableList(this.changeListeners);
	}
	
	protected void addToUndoableListener(JComponent component) {
		component.addKeyListener(this.keyAdapter);
	}
	
	public void commitChanges() {
		Object obj = getObject();
		if (obj != null) {
			for (ChangeListener changeListener : this.changeListeners) {
				changeListener.stateChanged(new ChangeEvent(obj));
			}
		}
	}
	
	protected class UndoableKeyAdapter extends KeyAdapter {
		StorableObjectEditor editor;
		UndoableKeyAdapter(StorableObjectEditor editor) {
			this.editor = editor;
		}
		
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_ENTER) {
				this.editor.commitChanges();
			} else if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				this.editor.setObject(this.editor.getObject());
			}
		}
	}
}
