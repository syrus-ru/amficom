/*-
* $Id: DomainBeanUI.java,v 1.4 2005/11/17 09:00:35 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.viewers;

import com.syrus.AMFICOM.manager.UI.ManagerMainFrame;
import com.syrus.AMFICOM.manager.beans.DomainBean;
import com.syrus.AMFICOM.manager.beans.DomainBeanWrapper;


/**
 * @version $Revision: 1.4 $, $Date: 2005/11/17 09:00:35 $
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
