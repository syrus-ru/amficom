/*-
* $Id: WorkstationBeanUI.java,v 1.4 2005/11/17 09:00:35 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.viewers;

import com.syrus.AMFICOM.manager.UI.ManagerMainFrame;
import com.syrus.AMFICOM.manager.beans.NonStorableBean;


/**
 * @version $Revision: 1.4 $, $Date: 2005/11/17 09:00:35 $
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

