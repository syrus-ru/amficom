/*-
 * $Id: ShowWindowCommand.java,v 1.5 2005/09/08 14:25:57 bob Exp $
 *
 * Copyright � 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */


package com.syrus.AMFICOM.client.model;

import java.beans.PropertyVetoException;

import javax.swing.JInternalFrame;

/**
 * @version $Revision: 1.5 $, $Date: 2005/09/08 14:25:57 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module commonclient
 */
public class ShowWindowCommand extends AbstractCommand {

	private final JInternalFrame	internalFrame;

	public ShowWindowCommand(final JInternalFrame internalFrame) {
		this.internalFrame = internalFrame;
	}

	@Override
	public void execute() {
		if (this.internalFrame != null) {
			if (!this.internalFrame.isVisible()) {
				this.internalFrame.setVisible(true);
			}
			this.internalFrame.toFront();
			try {
				this.internalFrame.setSelected(true);
			} catch (PropertyVetoException pve) {
				// nothing
			}
		}
	}

	@Override
	public JInternalFrame getSource() {
		return this.internalFrame;
	}
}
