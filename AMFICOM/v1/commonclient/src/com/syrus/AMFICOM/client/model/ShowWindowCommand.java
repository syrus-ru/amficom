/*-
 * $Id: ShowWindowCommand.java,v 1.4 2005/08/02 13:03:22 arseniy Exp $
 *
 * Copyright © 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */


package com.syrus.AMFICOM.client.model;

import java.beans.PropertyVetoException;

import javax.swing.JInternalFrame;

/**
 * @version $Revision: 1.4 $, $Date: 2005/08/02 13:03:22 $
 * @author $Author: arseniy $
 * @author Vladimir Dolzhenko
 * @module commonclient
 */
public class ShowWindowCommand extends AbstractCommand {

	private final JInternalFrame	internalFrame;

	public ShowWindowCommand(final JInternalFrame internalFrame) {
		this.internalFrame = internalFrame;
	}

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
