/*-
 * $Id: WindowTileHorizontalCommand.java,v 1.2 2005/06/21 14:26:44 bob Exp $
 *
 * Copyright © 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.UI;

import java.util.LinkedList;
import java.util.List;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

import com.syrus.AMFICOM.client.model.AbstractCommand;

public class WindowTileHorizontalCommand extends AbstractCommand {

	private JDesktopPane	desktop;

	public WindowTileHorizontalCommand(final JDesktopPane desktop) {
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

		final List<JInternalFrame> frameList = new LinkedList<JInternalFrame>();
		boolean areIcons = false;

		for (final JInternalFrame frame : this.desktop.getAllFrames()) {
			if (frame.isVisible() && !frame.isIcon() && frame.isIconifiable()) {
				frameList.add(frame);
			} else if (frame.isIcon()) {
				areIcons = true;
			}
		}

		final int frameWidth = this.desktop.getWidth();
		final int frameHeight = (this.desktop.getHeight() - (areIcons ? 50 : 0)) / frameList.size();
		
		int xpos = 0;
		int ypos = 0;
		
		for (final JInternalFrame frame : frameList) {			
			if (frame.isResizable()) {
				frame.reshape(xpos, ypos, frameWidth, frameHeight);
			} else {
				frame.setLocation(xpos, ypos);
			}
			ypos = ypos + frameHeight;
			frame.moveToFront();
		}
	}
}
