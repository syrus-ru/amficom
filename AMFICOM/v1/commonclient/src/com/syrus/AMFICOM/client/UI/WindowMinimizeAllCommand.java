/*-
 * $Id: WindowMinimizeAllCommand.java,v 1.2 2005/09/09 18:54:27 arseniy Exp $
 *
 * Copyright © 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.UI;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

import com.syrus.AMFICOM.client.model.AbstractCommand;

/**
 * @version $Revision: 1.2 $, $Date: 2005/09/09 18:54:27 $
 * @author $Author: arseniy $
 * @author Vladimir Dolzhenko
 * @module commonclient
 */
public class WindowMinimizeAllCommand extends AbstractCommand {

	private JDesktopPane desktop;

	public WindowMinimizeAllCommand(final JDesktopPane desktop) {
		this.desktop = desktop;
	}

	@Override
	public void setParameter(final String field, final Object value) {
		if (field.equals("desktop")) {
			this.setDesktop((JDesktopPane) value);
		}
	}

	public void setDesktop(final JDesktopPane desktop) {
		this.desktop = desktop;
	}

	@Override
	public void execute() {
		if (this.desktop == null) {
			return;
		}

		for (final JInternalFrame internalFrame : this.desktop.getAllFrames()) {
			if (internalFrame.isIconifiable()) {
				try {
					internalFrame.setIcon(true);
				} catch (java.beans.PropertyVetoException pve) {
					pve.printStackTrace();
				}
			}
		}
	}
}
