/*-
* $Id: MCMBeanUI.java,v 1.4 2005/11/11 13:47:08 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
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

import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.manager.MCMBean;
import com.syrus.AMFICOM.manager.MCMBeanWrapper;
import com.syrus.AMFICOM.manager.UI.ManagerMainFrame;
import com.syrus.AMFICOM.manager.UI.ManagerModel;


/**
 * @version $Revision: 1.4 $, $Date: 2005/11/11 13:47:08 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class MCMBeanUI extends TableBeanUI<MCMBean> {	
	
	public MCMBeanUI(final ManagerMainFrame managerMainFrame) {
		super(managerMainFrame,
			MCMBeanWrapper.getInstance(((ManagerModel)managerMainFrame.getModel()).getDispatcher()),
			new String[] { KEY_NAME, 
				KEY_DESCRIPTION, 
				KEY_HOSTNAME,
				KEY_SERVER_ID,
				KEY_USER_ID}, 
				"com/syrus/AMFICOM/manager/resources/icons/mcm.gif", 
				"com/syrus/AMFICOM/manager/resources/mcm.png");
		
		final ManagerModel managerModel = (ManagerModel)this.managerMainFrame.getModel();
		final Dispatcher dispatcher = managerModel.getDispatcher();
		dispatcher.addPropertyChangeListener(
			MCMBeanWrapper.PROPERTY_USERS_REFRESHED,
			new PropertyChangeListener() {

				@SuppressWarnings("unqualified-field-access")
				public void propertyChange(PropertyChangeEvent evt) {
					table.updateModel();
				}
			});
		
		dispatcher.addPropertyChangeListener(
			MCMBeanWrapper.PROPERTY_SERVERS_REFRESHED,
			new PropertyChangeListener() {

				@SuppressWarnings("unqualified-field-access")
				public void propertyChange(PropertyChangeEvent evt) {
					table.updateModel();
				}
			});
	}
}

