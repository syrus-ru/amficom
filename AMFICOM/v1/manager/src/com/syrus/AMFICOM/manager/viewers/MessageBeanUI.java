/*-
* $Id: MessageBeanUI.java,v 1.1 2005/11/09 15:08:45 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.viewers;

import com.syrus.AMFICOM.manager.NonStorableBean;
import com.syrus.AMFICOM.manager.UI.ManagerMainFrame;


/**
 * @version $Revision: 1.1 $, $Date: 2005/11/09 15:08:45 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class MessageBeanUI extends AbstractBeanUI<NonStorableBean> {

	@SuppressWarnings("unused")
	public MessageBeanUI(final ManagerMainFrame managerMainFrame) {
		super(managerMainFrame, 
			"com/syrus/AMFICOM/manager/resources/icons/envelope.gif", 
			"com/syrus/AMFICOM/manager/resources/envelope.gif");
	}
}

