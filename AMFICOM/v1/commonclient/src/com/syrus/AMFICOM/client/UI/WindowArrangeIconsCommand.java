/*-
 * $Id: WindowArrangeIconsCommand.java,v 1.1 2005/06/21 14:25:54 bob Exp $
 *
 * Copyright © 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.UI;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

import com.syrus.AMFICOM.client.model.AbstractCommand;

public class WindowArrangeIconsCommand extends AbstractCommand {

	private JDesktopPane	desktop;

	public WindowArrangeIconsCommand(final JDesktopPane desktop) {
		this.desktop = desktop;
	}

	public void setParameter(	final String field,
								final Object value) {
		if (field.equals("desktop"))
			setDesktop((JDesktopPane) value);
	}

	public void setDesktop(final JDesktopPane desktop) {
		this.desktop = desktop;
	}

	@Override
	public void execute() {
		if (this.desktop == null) { 
			return; 
		}

		final int height = this.desktop.getBounds().height;
		final int width = this.desktop.getBounds().width;
		
		int xPos = 0;
		int yPos = height;
		
		for (final JInternalFrame frame : this.desktop.getAllFrames()) {
			if ((frame.isVisible()) && (frame.isIcon())) {
				int iWidth = frame.getWidth();
				int iHeight = frame.getHeight();
				if (yPos >= height) {
					yPos = height - iHeight;
				}
				if ((xPos + iWidth > width) && (xPos != 0)) {
					xPos = 0;
					yPos -= iHeight;
				}
				frame.getDesktopIcon().setLocation(xPos, yPos);
				xPos += iWidth;
			} 
		} 
	}
}
