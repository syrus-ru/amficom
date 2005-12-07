/*-
* $Id: ServerBeanUI.java,v 1.4 2005/12/07 14:08:02 bob Exp $
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
 * @version $Revision: 1.4 $, $Date: 2005/12/07 14:08:02 $
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
				"server");
	}
}
