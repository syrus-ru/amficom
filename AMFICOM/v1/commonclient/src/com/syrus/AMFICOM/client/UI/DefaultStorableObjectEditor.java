/*-
 * $Id: DefaultStorableObjectEditor.java,v 1.4 2005/09/09 18:54:27 arseniy Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.UI;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.4 $, $Date: 2005/09/09 18:54:27 $
 * @module commonclient
 */

public abstract class DefaultStorableObjectEditor implements StorableObjectEditor {
	List<ChangeListener> changeListeners = new LinkedList<ChangeListener>(); 
	UndoableKeyAdapter keyAdapter;
	
	protected DefaultStorableObjectEditor() {
		this.keyAdapter = new UndoableKeyAdapter(this);
	}
	
	public void addChangeListener(final ChangeListener listener) {
		this.changeListeners.add(listener);
	}
	
	public void removeChangeListener(final ChangeListener listener) {
		this.changeListeners.remove(listener);
	}
	
	public Collection<ChangeListener> getChangeListeners() {
		return Collections.unmodifiableList(this.changeListeners);
	}
	
	protected void addToUndoableListener(final JComponent component) {
		component.addKeyListener(this.keyAdapter);
	}
	
	public void commitChanges() {
		final Object obj = getObject();
		if (obj != null) {
			for (final ChangeListener changeListener : this.changeListeners) {
				changeListener.stateChanged(new ChangeEvent(obj));
			}
		}
	}
	
	protected class UndoableKeyAdapter extends KeyAdapter {
		StorableObjectEditor editor;

		UndoableKeyAdapter(final StorableObjectEditor editor) {
			this.editor = editor;
		}
		
		@Override
		public void keyPressed(final KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_ENTER) {
				this.editor.commitChanges();
			} else if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				this.editor.setObject(this.editor.getObject());
			}
		}
	}
}
