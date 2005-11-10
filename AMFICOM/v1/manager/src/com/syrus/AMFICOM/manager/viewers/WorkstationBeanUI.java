/*-
* $Id: WorkstationBeanUI.java,v 1.3 2005/11/10 13:59:01 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.viewers;

import com.syrus.AMFICOM.manager.NonStorableBean;
import com.syrus.AMFICOM.manager.UI.ManagerMainFrame;


/**
 * @version $Revision: 1.3 $, $Date: 2005/11/10 13:59:01 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class WorkstationBeanUI extends AbstractBeanUI<NonStorableBean> {

	public WorkstationBeanUI(final ManagerMainFrame managerMainFrame) {
		super(managerMainFrame, 
			"com/syrus/AMFICOM/manager/resources/icons/arm.gif", 
			"com/syrus/AMFICOM/manager/resources/arm.gif");
	}
}

