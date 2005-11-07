/*-
* $Id: RoleBeanUI.java,v 1.1 2005/11/07 15:21:45 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.viewers;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JPopupMenu;
import javax.swing.UIManager;

import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.manager.RoleBean;
import com.syrus.AMFICOM.manager.RoleBeanWrapper;
import com.syrus.AMFICOM.manager.UI.ManagerMainFrame;


/**
 * @version $Revision: 1.1 $, $Date: 2005/11/07 15:21:45 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class RoleBeanUI extends TableBeanUI<RoleBean> {
	
	public RoleBeanUI(final ManagerMainFrame managerMainFrame) {
		super(managerMainFrame, 
			RoleBeanWrapper.getInstance(), 
			RoleBeanWrapper.getInstance().getKeys().toArray(new String[0]), 
			"com/syrus/AMFICOM/manager/resources/icons/user.gif", 
			"com/syrus/AMFICOM/manager/resources/user.gif");
	}
	
	@Override
	public JPopupMenu getPopupMenu(final RoleBean bean,
			final Object cell) {


		if (cell != null) {
			final JPopupMenu popupMenu = new JPopupMenu();

			final AbstractAction enterAction = new AbstractAction(I18N.getString("Manager.Dialog.GotoRolePermissions")) {

				public void actionPerformed(ActionEvent e) {					
//					managerMainFrame.setPerspective(new SystemUserPerpective(managerMainFrame, bean, cell));
				}
			};
			
			final Icon enterIcon = UIManager.getIcon(ENTER_ICON);
			if (enterIcon != null) {
				enterAction.putValue(Action.SMALL_ICON, enterIcon);
			}
			
			popupMenu.add(enterAction);

			
			return popupMenu;
		}

		return null;	
	}
}

