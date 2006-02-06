/*-
 * $Id: WindowListCommand.java,v 1.3 2005/09/09 18:54:27 arseniy Exp $
 *
 * Copyright © 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.UI;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.client.model.AbstractCommand;

/**
 * @version $Revision: 1.3 $, $Date: 2005/09/09 18:54:27 $
 * @author $Author: arseniy $
 * @author Vladimir Dolzhenko
 * @module commonclient
 */
public class WindowListCommand extends AbstractCommand {

	private JDesktopPane desktop;

	public WindowListCommand(final JDesktopPane desktop) {
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
	}
}
