/*-
* $Id: KISBeanUI.java,v 1.3 2005/11/17 09:00:35 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.viewers;

import static com.syrus.AMFICOM.manager.beans.RTUBeanWrapper.KEY_DESCRIPTION;
import static com.syrus.AMFICOM.manager.beans.RTUBeanWrapper.KEY_HOSTNAME;
import static com.syrus.AMFICOM.manager.beans.RTUBeanWrapper.KEY_MCM_ID;
import static com.syrus.AMFICOM.manager.beans.RTUBeanWrapper.KEY_NAME;
import static com.syrus.AMFICOM.manager.beans.RTUBeanWrapper.KEY_PORT;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.syrus.AMFICOM.manager.UI.ManagerMainFrame;
import com.syrus.AMFICOM.manager.UI.ManagerModel;
import com.syrus.AMFICOM.manager.beans.RTUBean;
import com.syrus.AMFICOM.manager.beans.RTUBeanWrapper;


/**
 * @version $Revision: 1.3 $, $Date: 2005/11/17 09:00:35 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class KISBeanUI extends TableBeanUI<RTUBean> {	
	
	public KISBeanUI(final ManagerMainFrame managerMainFrame) {
		super(managerMainFrame,
			RTUBeanWrapper.getInstance(((ManagerModel)managerMainFrame.getModel()).getDispatcher()),
			new String[] { KEY_NAME, 
				KEY_DESCRIPTION, 
				KEY_MCM_ID,
				KEY_HOSTNAME,
				KEY_PORT}, 
				"com/syrus/AMFICOM/manager/resources/icons/rtu.gif", 
				"com/syrus/AMFICOM/manager/resources/rtu.png");
		final ManagerModel managerModel = (ManagerModel)managerMainFrame.getModel();

		managerModel.getDispatcher().addPropertyChangeListener(
			RTUBeanWrapper.PROPERTY_MCMS_REFRESHED,
			new PropertyChangeListener() {

				public void propertyChange(PropertyChangeEvent evt) {
					table.updateModel();
				}
			});
	}	
}

