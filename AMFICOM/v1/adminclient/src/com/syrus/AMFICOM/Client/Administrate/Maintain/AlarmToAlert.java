package com.syrus.AMFICOM.Client.Administrate.Maintain;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Resource.Alarm.ui.JAlertingConfigPanel;
import java.util.ResourceBundle;
import javax.swing.*;

/**
 * @version $Revision: 1.1 $, $Date: 2004/05/27 11:32:35 $
 * @author $Author: bass $
 */
public final class AlarmToAlert extends JInternalFrame {
	private ApplicationContext applicationContext;

	public AlarmToAlert(JDesktopPane jDesktopPane, ApplicationContext applicationContext, JFrame ownerWindow) {
		this.applicationContext = applicationContext;
		initComponents();
	}
	
	private void initComponents() {//GEN-BEGIN:initComponents

		jAlertingConfigPanel1 = new JAlertingConfigPanel();
		
		setClosable(true);
		setResizable(true);
		setTitle(ResourceBundle.getBundle("com.syrus.AMFICOM.Client.Resource.Alarm.util.MessageModelPropertyResourceBundle").getString("AlarmToAlert.Title"));
		setFrameIcon(new javax.swing.ImageIcon("images/general.gif"));
		setMinimumSize(new java.awt.Dimension(258, 536));
		getContentPane().add(jAlertingConfigPanel1, java.awt.BorderLayout.CENTER);
		
		setSize(new java.awt.Dimension(258, 536));

	}//GEN-END:initComponents
	
	// Variables declaration - do not modify//GEN-BEGIN:variables
	private com.syrus.AMFICOM.Client.Resource.Alarm.ui.JAlertingConfigPanel jAlertingConfigPanel1;
	// End of variables declaration//GEN-END:variables
}
