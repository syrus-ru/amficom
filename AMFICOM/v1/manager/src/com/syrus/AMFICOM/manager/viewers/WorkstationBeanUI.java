/*-
* $Id: WorkstationBeanUI.java,v 1.1 2005/10/18 15:10:39 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.viewers;

import com.syrus.AMFICOM.manager.NonStorableBean;
import com.syrus.AMFICOM.manager.UI.ManagerMainFrame;


/**
 * @version $Revision: 1.1 $, $Date: 2005/10/18 15:10:39 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
class WorkstationBeanUI extends AbstractBeanUI<NonStorableBean> {

	@SuppressWarnings("unused")
	public WorkstationBeanUI(final ManagerMainFrame managerMainFrame) {
		super(managerMainFrame, 
			"com/syrus/AMFICOM/manager/resources/icons/arm.gif", 
			"com/syrus/AMFICOM/manager/resources/arm.gif");
	}
}

