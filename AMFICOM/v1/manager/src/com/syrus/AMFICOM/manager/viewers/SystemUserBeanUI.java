/*-
* $Id: SystemUserBeanUI.java,v 1.3 2005/11/11 10:58:02 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.viewers;

import com.syrus.AMFICOM.manager.UserBean;
import com.syrus.AMFICOM.manager.UserBeanWrapper;
import com.syrus.AMFICOM.manager.UI.ManagerMainFrame;


/**
 * @version $Revision: 1.3 $, $Date: 2005/11/11 10:58:02 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class SystemUserBeanUI extends TableBeanUI<UserBean> {
	
	public SystemUserBeanUI(final ManagerMainFrame managerMainFrame) {
		super(managerMainFrame,
			UserBeanWrapper.getInstance(),
			UserBeanWrapper.getInstance().getKeys().toArray(new String[0]), 
			"com/syrus/AMFICOM/manager/resources/icons/user.gif", 
			"com/syrus/AMFICOM/manager/resources/user.gif");
	}	
}

