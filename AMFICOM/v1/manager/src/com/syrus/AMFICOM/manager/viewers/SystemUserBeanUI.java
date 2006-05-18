/*-
* $Id: SystemUserBeanUI.java,v 1.5 2005/12/07 14:08:02 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.viewers;

import com.syrus.AMFICOM.manager.UI.ManagerMainFrame;
import com.syrus.AMFICOM.manager.beans.UserBean;
import com.syrus.AMFICOM.manager.beans.UserBeanWrapper;


/**
 * @version $Revision: 1.5 $, $Date: 2005/12/07 14:08:02 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class SystemUserBeanUI extends TableBeanUI<UserBean> {
	
	public SystemUserBeanUI(final ManagerMainFrame managerMainFrame) {
		super(managerMainFrame,
			UserBeanWrapper.getInstance(),
			UserBeanWrapper.getInstance().getKeys().toArray(new String[0]), 
			"user");
	}	
}

