/*
 * $Id: JAlertingConfigDialog.java,v 1.1 2004/06/24 10:53:57 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.Client.Resource.Alarm.ui;

import java.awt.*;
import java.util.*;
import javax.swing.*;

/**
 * @version $Revision: 1.1 $, $Date: 2004/06/24 10:53:57 $
 * @author $Author: bass $
 */
public final class JAlertingConfigDialog extends JDialog {
	public JAlertingConfigDialog(Frame parent, boolean modal) {
		super(parent, modal);
		initComponents();
	}

	private void initComponents() {//GEN-BEGIN:initComponents
		jAlertingConfigPanel1 = new com.syrus.AMFICOM.Client.Resource.Alarm.ui.JAlertingConfigPanel();
		
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle(ResourceBundle.getBundle("com.syrus.AMFICOM.Client.Resource.Alarm.util.MessageModelPropertyResourceBundle").getString("AlarmToAlert.Title"));
		getContentPane().add(jAlertingConfigPanel1, java.awt.BorderLayout.CENTER);
		
		java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		setBounds((screenSize.width-563)/2, (screenSize.height-600)/2, 563, 600);
	}//GEN-END:initComponents
	
	public static void main(String args[]) {
		new JAlertingConfigDialog(new JFrame(), true).show();
		System.exit(0);
	}
	
	// Variables declaration - do not modify//GEN-BEGIN:variables
	private com.syrus.AMFICOM.Client.Resource.Alarm.ui.JAlertingConfigPanel jAlertingConfigPanel1;
	// End of variables declaration//GEN-END:variables
}
