/*-
* $Id: FlushCommand.java,v 1.2 2005/08/24 14:05:47 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.UI;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.manager.LangModelManager;


/**
 * @version $Revision: 1.2 $, $Date: 2005/08/24 14:05:47 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class FlushCommand extends AbstractCommand {

	@Override
	public void execute() {
		try {
			StorableObjectPool.flush(ObjectEntities.LAYOUT_ITEM_CODE, LoginManager.getUserId(), true);
			StorableObjectPool.flush(ObjectEntities.CHARACTERISTIC_CODE, LoginManager.getUserId(), true);
			StorableObjectPool.flush(ObjectEntities.PERMATTR_CODE, LoginManager.getUserId(), true);
			StorableObjectPool.flush(ObjectEntities.DOMAIN_CODE, LoginManager.getUserId(), true);
			StorableObjectPool.flush(ObjectEntities.SYSTEMUSER_CODE, LoginManager.getUserId(), true);
			StorableObjectPool.flush(ObjectEntities.KIS_CODE, LoginManager.getUserId(), true);
			StorableObjectPool.flush(ObjectEntities.SERVER_CODE, LoginManager.getUserId(), true);
			StorableObjectPool.flush(ObjectEntities.MCM_CODE, LoginManager.getUserId(), true);
		} catch (ApplicationException e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(null, 
				e1.getMessage(), 
				LangModelManager.getString("Error"),
				JOptionPane.ERROR_MESSAGE);
		}
	}
	
}

