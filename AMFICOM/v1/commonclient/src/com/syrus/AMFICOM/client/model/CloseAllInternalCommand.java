/**
 * $Id: CloseAllInternalCommand.java,v 1.4 2005/09/12 06:39:17 bob Exp $
 * Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.client.model;

import java.awt.Component;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

/**
 * @version $Revision: 1.4 $
 * @author $Author: bob $
 * @module commonclient
 */
public class CloseAllInternalCommand extends AbstractCommand {
	public JDesktopPane desktop;

	public CloseAllInternalCommand(JDesktopPane desktop) {
		this.desktop = desktop;
	}

	@Override
	public void execute() {
		for(int i = 0; i < this.desktop.getComponents().length; i++) {
			try {
				Component component = this.desktop.getComponent(i);
				if (component instanceof JInternalFrame) {
					component.setVisible(false);
				}
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}
