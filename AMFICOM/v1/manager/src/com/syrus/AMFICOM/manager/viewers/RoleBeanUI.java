/*-
* $Id: RoleBeanUI.java,v 1.3 2005/11/17 09:00:35 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.viewers;

import com.syrus.AMFICOM.manager.UI.ManagerMainFrame;
import com.syrus.AMFICOM.manager.beans.RoleBean;
import com.syrus.AMFICOM.manager.beans.RoleBeanWrapper;


/**
 * @version $Revision: 1.3 $, $Date: 2005/11/17 09:00:35 $
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

