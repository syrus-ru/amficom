/*-
* $Id: KISBeanUI.java,v 1.1 2005/10/18 15:10:39 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.viewers;

import static com.syrus.AMFICOM.manager.RTUBeanWrapper.KEY_DESCRIPTION;
import static com.syrus.AMFICOM.manager.RTUBeanWrapper.KEY_HOSTNAME;
import static com.syrus.AMFICOM.manager.RTUBeanWrapper.KEY_MCM_ID;
import static com.syrus.AMFICOM.manager.RTUBeanWrapper.KEY_NAME;
import static com.syrus.AMFICOM.manager.RTUBeanWrapper.KEY_PORT;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.syrus.AMFICOM.manager.RTUBean;
import com.syrus.AMFICOM.manager.RTUBeanWrapper;
import com.syrus.AMFICOM.manager.UI.ManagerMainFrame;


/**
 * @version $Revision: 1.1 $, $Date: 2005/10/18 15:10:39 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
class KISBeanUI extends TableBeanUI<RTUBean> {	
	
	public KISBeanUI(final ManagerMainFrame managerMainFrame) {
		super(managerMainFrame,
			RTUBeanWrapper.getInstance(managerMainFrame.getDispatcher()),
			new String[] { KEY_NAME, 
				KEY_DESCRIPTION, 
				KEY_MCM_ID,
				KEY_HOSTNAME,
				KEY_PORT}, 
				"com/syrus/AMFICOM/manager/resources/icons/rtu.gif", 
				"com/syrus/AMFICOM/manager/resources/rtu.png");
		
		managerMainFrame.getDispatcher().addPropertyChangeListener(
			RTUBeanWrapper.PROPERTY_MCMS_REFRESHED,
			new PropertyChangeListener() {

				public void propertyChange(PropertyChangeEvent evt) {
					table.updateModel();
				}
			});
	}	
}

