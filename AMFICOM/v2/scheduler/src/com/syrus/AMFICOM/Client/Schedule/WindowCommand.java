/*
 * WindowCommand.java
 * Created on 05.08.2004 9:47:24
 * 
 */
package com.syrus.AMFICOM.Client.Schedule;

import java.beans.PropertyVetoException;

import javax.swing.JInternalFrame;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Model.Environment;

/**
 * @author Vladimir Dolzhenko
 */
public class WindowCommand implements Command {

	private Object	source;

	public WindowCommand(Object source) {
		this.source = source;
	}

	public Object clone() {
		return new WindowCommand(this.source);
	}

	public void commit_execute() {
		//		 nothing
	}

	public void commit_undo() {
		//		 nothing
	}

	public void execute() {
		if (this.source instanceof JInternalFrame) {
			//System.out.println("exec JInternalFrame");
			JInternalFrame frame = (JInternalFrame) this.source;
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
			Environment.log(Environment.LOG_LEVEL_FINE, "current execution point with call stack:", null, null, e);
		}
	}

	public void undo() {
		// nothing
	}

}