/*-
* $Id: DomainBeanUI.java,v 1.3 2005/11/11 10:58:02 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.viewers;

import com.syrus.AMFICOM.manager.DomainBean;
import com.syrus.AMFICOM.manager.DomainBeanWrapper;
import com.syrus.AMFICOM.manager.UI.ManagerMainFrame;


/**
 * @version $Revision: 1.3 $, $Date: 2005/11/11 10:58:02 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class DomainBeanUI extends TableBeanUI<DomainBean> {
	
	public DomainBeanUI(final ManagerMainFrame managerMainFrame) {
		super(managerMainFrame,
			DomainBeanWrapper.getInstance(),
			new String[] {
				DomainBeanWrapper.KEY_NAME,
				DomainBeanWrapper.KEY_DESCRIPTION},
				"com/syrus/AMFICOM/manager/resources/icons/domain.gif", 
				"com/syrus/AMFICOM/manager/resources/domain2.png");
	}

}
