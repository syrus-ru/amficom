/*-
 * $Id: WindowArrangeCommand.java,v 1.1 2005/06/21 14:25:54 bob Exp $
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

public class WindowArrangeCommand extends AbstractCommand {

	private JDesktopPane	desktop;

	public WindowArrangeCommand(final JDesktopPane desktop) {
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
		System.out.println("perform windows arrange ");
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

		final int desktopWidth = this.desktop.getWidth();

		final int frameCount = frameList.size();
		int row = (int) Math.round(Math.sqrt(frameCount));
		if (row == 0) { 
			return; 
		}
		int col = frameCount / row;
		if (col == 0) {
			return;
		}
		int rem = frameCount % row;
		int rowCount = 1;
		int frameWidth = this.desktop.getWidth() / col;
		final int frameHeight = 
			(this.desktop.getHeight() - (areIcons ? 50 : 0)) / row;

		int xpos = 0;
		int ypos = 0;

		for (final JInternalFrame internalFrame : frameList) {
			if (rowCount <= row - rem) {
				if (internalFrame.isResizable()) {
					internalFrame.reshape(xpos, ypos, frameWidth, frameHeight);
				} else {
					internalFrame.setLocation(xpos, ypos);
				}
				if (xpos + 10 < desktopWidth - frameWidth) {
					xpos = xpos + frameWidth;
				} else {
					ypos = ypos + frameHeight;
					xpos = 0;
					rowCount++;
				}
			} else {
				frameWidth = desktopWidth / (col + 1);
				if (internalFrame.isResizable()) {
					internalFrame.reshape(xpos, ypos, frameWidth, frameHeight);
				} else {
					internalFrame.setLocation(xpos, ypos);
				}
				if (xpos + 10 < desktopWidth - frameWidth) {
					xpos = xpos + frameWidth;
				} else {
					ypos = ypos + frameHeight;
					xpos = 0;
				}
			}
		}
	}
}
