/*
 * WindowCommand.java
 * Created on 05.08.2004 9:47:24
 * 
 */

package com.syrus.AMFICOM.client.model;

import java.beans.PropertyVetoException;

import javax.swing.JInternalFrame;

/**
 * @version $Revision: 1.1 $, $Date: 2005/05/19 14:06:42 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module commonclient_v1
 */
public class ShowWindowCommand extends AbstractCommand {

	private Object	source;

	public ShowWindowCommand(Object source) {
		this.source = source;
	}

	public void execute() {
		if (this.source instanceof JInternalFrame) {
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

	public Object getSource() {
		return this.source;
	}
}
