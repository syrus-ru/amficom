/**
 * $Id: CloseAllInternalCommand.java,v 1.3 2005/09/08 14:25:57 bob Exp $
 * Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.client.model;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

/**
 * @version $Revision: 1.3 $
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
