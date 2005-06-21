/*-
 * $Id: ShowWindowCommand.java,v 1.2 2005/06/21 14:24:56 bob Exp $
 *
 * Copyright © 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */


package com.syrus.AMFICOM.client.model;

import java.beans.PropertyVetoException;

import javax.swing.JInternalFrame;

/**
 * @version $Revision: 1.2 $, $Date: 2005/06/21 14:24:56 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module commonclient_v1
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
