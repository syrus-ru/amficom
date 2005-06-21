/*-
 * $Id: WindowCascadeCommand.java,v 1.1 2005/06/21 14:25:54 bob Exp $
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

public class WindowCascadeCommand extends AbstractCommand {
	private JDesktopPane	desktop;

	public WindowCascadeCommand(final JDesktopPane desktop) {
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

		final int spacer = 30;
		final double scale = 0.6;
		final int deskWidth = this.desktop.getWidth();
		final int deskHeight = this.desktop.getHeight();
		final int frameWidth = (int) (deskWidth * scale);
		final int frameHeight = (int) ((deskHeight - (areIcons ? 50 : 0)) * scale);
		
		int xpos = 0;
		int ypos = 0;
		
		for (final JInternalFrame frame : frameList) {			
			if (frame.isResizable()) {
				frame.reshape(xpos, ypos, frameWidth, frameHeight);
			} else {
				frame.setLocation(xpos, ypos);
			}
			frame.moveToFront();
			xpos = xpos + spacer;
			ypos = ypos + spacer;
			if ((xpos + frameWidth > deskWidth)
					|| (ypos + frameHeight > deskHeight - 50)) {
				xpos = 0;
				ypos = 0;
			}
		}
	}	
}
