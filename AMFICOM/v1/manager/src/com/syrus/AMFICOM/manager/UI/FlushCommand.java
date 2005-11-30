/*-
* $Id: FlushCommand.java,v 1.6 2005/11/30 13:15:27 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.UI;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.manager.perspective.Perspective;


/**
 * @version $Revision: 1.6 $, $Date: 2005/11/30 13:15:27 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class FlushCommand extends AbstractCommand {

	private final ManagerMainFrame	managerMainFrame;

	public FlushCommand(final ManagerMainFrame managerMainFrame) {
		this.managerMainFrame = managerMainFrame;		
	}
	
	@Override
	public void execute() {
		try {
			final Perspective perspective = this.managerMainFrame.getPerspective();
			if (perspective != null) {				
				if (perspective.isValid()) {
//					StorableObjectPool.flush(ObjectEntities.LAYOUT_ITEM_CODE, LoginManager.getUserId(), true);
					StorableObjectPool.flush(ObjectEntities.CHARACTERISTIC_CODE, LoginManager.getUserId(), true);
					StorableObjectPool.flush(ObjectEntities.PERMATTR_CODE, LoginManager.getUserId(), true);
					StorableObjectPool.flush(ObjectEntities.DOMAIN_CODE, LoginManager.getUserId(), true);
					StorableObjectPool.flush(ObjectEntities.SYSTEMUSER_CODE, LoginManager.getUserId(), true);
					StorableObjectPool.flush(ObjectEntities.KIS_CODE, LoginManager.getUserId(), true);
					StorableObjectPool.flush(ObjectEntities.SERVER_CODE, LoginManager.getUserId(), true);
					StorableObjectPool.flush(ObjectEntities.MCM_CODE, LoginManager.getUserId(), true);
					StorableObjectPool.flush(ObjectEntities.DELIVERYATTRIBUTES_CODE, LoginManager.getUserId(), true);
				} else {
					JOptionPane.showMessageDialog(this.managerMainFrame.getGraph(), 
						I18N.getString("Manager.Error.LayoutIsInvalid"),
						I18N.getString("Error"),
						JOptionPane.ERROR_MESSAGE);
				}
			}
		} catch (ApplicationException e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(null, 
				e1.getMessage(), 
				I18N.getString("Error"),
				JOptionPane.ERROR_MESSAGE);
		}
	}
	
}

