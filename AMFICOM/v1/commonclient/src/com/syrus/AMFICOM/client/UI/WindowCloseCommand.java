/*-
 * $Id: WindowCloseCommand.java,v 1.1 2005/06/21 14:25:54 bob Exp $
 *
 * Copyright © 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.UI;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.client.model.AbstractCommand;

public class WindowCloseCommand extends AbstractCommand {

	private JDesktopPane	desktop;

	public WindowCloseCommand(final JDesktopPane desktop) {
		this.desktop = desktop;
	}

	public void setParameter(	final String field,
								final Object value) {
		if (field.equals("desktop")) {
			setDesktop((JDesktopPane) value);
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
		
		this.desktop.getSelectedFrame().dispose();
	}
}
