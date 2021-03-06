/*-
* $Id: FlushCommand.java,v 1.11 2006/03/06 13:32:40 saa Exp $
*
* Copyright ? 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.UI;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.client.UI.ProcessingDialogDummy;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.manager.perspective.Perspective;


/**
 * @version $Revision: 1.11 $, $Date: 2006/03/06 13:32:40 $
 * @author $Author: saa $
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
		
			final Perspective perspective = this.managerMainFrame.getPerspective();
			if (perspective != null) {				
				if (perspective.isValid()) {
					final CellBuffer cellBuffer = this.managerMainFrame.getCellBuffer();

					new ProcessingDialogDummy(new Runnable() {
						public void run() {
							try {
								// clear cache
								cellBuffer.putCells(null);
								
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
							} catch (final ApplicationException e1) {
								e1.printStackTrace();
								JOptionPane.showMessageDialog(null, 
									e1.getMessage(), 
									I18N.getString("Error"),
									JOptionPane.ERROR_MESSAGE);
							}
							}
					}, I18N.getString("Common.ProcessingDialog.PlsWait"));
					
				} else {
					JOptionPane.showMessageDialog(this.managerMainFrame.getGraph(), 
						I18N.getString("Manager.Error.LayoutIsInvalid"),
						I18N.getString("Error"),
						JOptionPane.ERROR_MESSAGE);
				}
			}
		
	}
	
}

