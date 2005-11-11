/*-
* $Id: RoleBeanUI.java,v 1.2 2005/11/11 10:58:02 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.viewers;

import com.syrus.AMFICOM.manager.RoleBean;
import com.syrus.AMFICOM.manager.RoleBeanWrapper;
import com.syrus.AMFICOM.manager.UI.ManagerMainFrame;


/**
 * @version $Revision: 1.2 $, $Date: 2005/11/11 10:58:02 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class RoleBeanUI extends TableBeanUI<RoleBean> {
	
	public RoleBeanUI(final ManagerMainFrame managerMainFrame) {
		super(managerMainFrame, 
			RoleBeanWrapper.getInstance(), 
			RoleBeanWrapper.getInstance().getKeys().toArray(new String[0]), 
			"com/syrus/AMFICOM/manager/resources/icons/user.gif", 
			"com/syrus/AMFICOM/manager/resources/user.gif");
	}
}

