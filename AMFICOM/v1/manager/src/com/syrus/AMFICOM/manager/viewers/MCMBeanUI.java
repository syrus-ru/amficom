/*-
* $Id: MCMBeanUI.java,v 1.1 2005/10/18 15:10:39 bob Exp $
*
* Copyright � 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.viewers;

import static com.syrus.AMFICOM.manager.MCMBeanWrapper.KEY_DESCRIPTION;
import static com.syrus.AMFICOM.manager.MCMBeanWrapper.KEY_HOSTNAME;
import static com.syrus.AMFICOM.manager.MCMBeanWrapper.KEY_NAME;
import static com.syrus.AMFICOM.manager.MCMBeanWrapper.KEY_SERVER_ID;
import static com.syrus.AMFICOM.manager.MCMBeanWrapper.KEY_USER_ID;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPopupMenu;

import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.manager.MCMBean;
import com.syrus.AMFICOM.manager.MCMBeanWrapper;
import com.syrus.AMFICOM.manager.UI.ManagerMainFrame;


/**
 * @version $Revision: 1.1 $, $Date: 2005/10/18 15:10:39 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
class MCMBeanUI extends TableBeanUI<MCMBean> {	
	
	public MCMBeanUI(final ManagerMainFrame managerMainFrame) {
		super(managerMainFrame,
			MCMBeanWrapper.getInstance(managerMainFrame.getDispatcher()),
			new String[] { KEY_NAME, 
				KEY_DESCRIPTION, 
				KEY_HOSTNAME,
				KEY_SERVER_ID,
				KEY_USER_ID}, 
				"com/syrus/AMFICOM/manager/resources/icons/mcm.gif", 
				"com/syrus/AMFICOM/manager/resources/mcm.png");
		
		Dispatcher dispatcher = managerMainFrame.getDispatcher();
		dispatcher.addPropertyChangeListener(
			MCMBeanWrapper.PROPERTY_USERS_REFRESHED,
			new PropertyChangeListener() {

				public void propertyChange(PropertyChangeEvent evt) {
					table.updateModel();
				}
			});
		
		dispatcher.addPropertyChangeListener(
			MCMBeanWrapper.PROPERTY_SERVERS_REFRESHED,
			new PropertyChangeListener() {

				public void propertyChange(PropertyChangeEvent evt) {
					table.updateModel();
				}
			});
	}
	
	public JPopupMenu getPopupMenu(	final MCMBean bean,
									final Object cell) {
		return null;
	}
}

