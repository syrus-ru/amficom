/*
 * WindowCommand.java
 * Created on 05.08.2004 9:47:24
 * 
 */
package com.syrus.AMFICOM.client.UI;

import java.beans.PropertyVetoException;
import java.util.logging.Level;

import javax.swing.JInternalFrame;

import com.syrus.AMFICOM.client.model.Command;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.5 $, $Date: 2005/11/29 08:17:23 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module commonclient
 */
public class WindowCommand implements Command {

	private Object	source;

	public WindowCommand(Object source) {
		this.source = source;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public void commitExecute() {
		//		 nothing
	}

	public void commitUndo() {
		//		 nothing
	}

	public void execute() {
		if (this.source instanceof JInternalFrame) {
			//System.out.println("exec JInternalFrame");
			JInternalFrame frame = (JInternalFrame) this.source;
			if (!frame.isVisible()) {
				frame.setVisible(true);
			}
			try {
				frame.setSelected(true);
			} catch (PropertyVetoException pve) {
				// nothing
			}
		}
	}

	public int getResult() {
		return RESULT_OK;
	}

	public Object getSource() {
		return this.source;
	}

	public void redo() {
		//		 nothing
	}

	public void setParameter(String field, Object value) {
		System.out.println("Set for Void command paramenter " + field + " to value " + value.toString() + " - ignored");
		try {
			throw new Exception("dummy");
		} catch (Exception e) {
			Log.debugMessage(e, Level.FINE);
		}
	}

	public void undo() {
		// nothing
	}
	
	
	public Command getNext() {
		// TODO Auto-generated method stub
		return null;
	}	
	
	public void setNext(@SuppressWarnings("unused") Command next) {
		// TODO Auto-generated method stub

	}

	
	/* (non-Javadoc)
	 * @see com.syrus.AMFICOM.Client.General.Command.Command#getPrevious()
	 */
	public Command getPrevious() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.syrus.AMFICOM.Client.General.Command.Command#setPrevious(com.syrus.AMFICOM.Client.General.Command.Command)
	 */
	public void setPrevious(@SuppressWarnings("unused") Command previous) {
		// TODO Auto-generated method stub

	}
}
