
package com.syrus.AMFICOM.client.UI;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.client.model.AbstractCommand;

public class WindowListCommand extends AbstractCommand {

	private JDesktopPane	desktop;

	public WindowListCommand(final JDesktopPane desktop) {
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
		if (this.desktop == null)
			return;

	}
}
