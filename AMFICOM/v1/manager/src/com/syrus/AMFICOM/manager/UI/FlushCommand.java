/*-
* $Id: FlushCommand.java,v 1.7 2005/12/01 14:03:28 bob Exp $
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
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.manager.perspective.Perspective;


/**
 * @version $Revision: 1.7 $, $Date: 2005/12/01 14:03:28 $
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
					final Identifier userId = LoginManager.getUserId();
					StorableObjectPool.flush(ObjectEntities.LAYOUT_ITEM_CODE, userId, true);
					StorableObjectPool.flush(ObjectEntities.CHARACTERISTIC_CODE, userId, true);
					StorableObjectPool.flush(ObjectEntities.PERMATTR_CODE, userId, true);
					StorableObjectPool.flush(ObjectEntities.DOMAIN_CODE, userId, true);
					StorableObjectPool.flush(ObjectEntities.SYSTEMUSER_CODE, userId, true);
					StorableObjectPool.flush(ObjectEntities.KIS_CODE, userId, true);
					StorableObjectPool.flush(ObjectEntities.SERVER_CODE, userId, true);
					StorableObjectPool.flush(ObjectEntities.MCM_CODE, userId, true);
					StorableObjectPool.flush(ObjectEntities.DELIVERYATTRIBUTES_CODE, userId, true);
				} else {
					JOptionPane.showMessageDialog(this.managerMainFrame.getGraph(), 
						I18N.getString("Manager.Error.LayoutIsInvalid"),
						I18N.getString("Error"),
						JOptionPane.ERROR_MESSAGE);
				}
			}
		} catch (final ApplicationException e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(null, 
				e1.getMessage(), 
				I18N.getString("Error"),
				JOptionPane.ERROR_MESSAGE);
		}
	}
	
}

