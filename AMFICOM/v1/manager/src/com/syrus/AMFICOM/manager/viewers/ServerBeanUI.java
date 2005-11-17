/*-
* $Id: ServerBeanUI.java,v 1.3 2005/11/17 09:00:35 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.viewers;

import static com.syrus.AMFICOM.manager.beans.ServerBeanWrapper.KEY_DESCRIPTION;
import static com.syrus.AMFICOM.manager.beans.ServerBeanWrapper.KEY_HOSTNAME;
import static com.syrus.AMFICOM.manager.beans.ServerBeanWrapper.KEY_NAME;

import com.syrus.AMFICOM.manager.UI.ManagerMainFrame;
import com.syrus.AMFICOM.manager.beans.ServerBean;
import com.syrus.AMFICOM.manager.beans.ServerBeanWrapper;


/**
 * @version $Revision: 1.3 $, $Date: 2005/11/17 09:00:35 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class ServerBeanUI extends TableBeanUI<ServerBean> {

	public ServerBeanUI(final ManagerMainFrame managerMainFrame) {
		super(managerMainFrame,
			ServerBeanWrapper.getInstance(),
			new String[] { KEY_NAME, 
				KEY_DESCRIPTION, 
				KEY_HOSTNAME}, 
				"com/syrus/AMFICOM/manager/resources/icons/server.gif", 
				"com/syrus/AMFICOM/manager/resources/server.png");
	}
}
