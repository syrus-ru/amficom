/**
 * $Id: CloseAllInternalCommand.java,v 1.2 2005/08/02 13:03:22 arseniy Exp $
 * Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.client.model;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

/**
 * @version $Revision: 1.2 $
 * @author $Author: arseniy $
 * @module commonclient
 */
public class CloseAllInternalCommand extends AbstractCommand {
	public JDesktopPane desktop;

	public CloseAllInternalCommand(JDesktopPane desktop) {
		this.desktop = desktop;
	}

	public void execute() {

		JInternalFrame frame = null;
		for(int i = 0; i < this.desktop.getComponents().length; i++) {
			try {
				frame = (JInternalFrame)this.desktop.getComponent(i);
				frame.setVisible(false);
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}
