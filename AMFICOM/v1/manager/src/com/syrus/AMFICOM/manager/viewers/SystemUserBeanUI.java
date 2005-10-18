/*-
* $Id: SystemUserBeanUI.java,v 1.1 2005/10/18 15:10:39 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.viewers;

import java.awt.event.ActionEvent;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JPopupMenu;

import com.syrus.AMFICOM.administration.Role;
import com.syrus.AMFICOM.administration.SystemUser;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.manager.SystemUserPerpective;
import com.syrus.AMFICOM.manager.UserBean;
import com.syrus.AMFICOM.manager.UserBeanWrapper;
import com.syrus.AMFICOM.manager.UI.ManagerMainFrame;


/**
 * @version $Revision: 1.1 $, $Date: 2005/10/18 15:10:39 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class SystemUserBeanUI extends TableBeanUI<UserBean> {
	
	public SystemUserBeanUI(final ManagerMainFrame managerMainFrame) {
		super(managerMainFrame,
			UserBeanWrapper.getInstance(),
			UserBeanWrapper.getInstance().getKeys().toArray(new String[0]), 
			"com/syrus/AMFICOM/manager/resources/icons/user.gif", 
			"com/syrus/AMFICOM/manager/resources/user.gif");
	}
	
	@Override
	public JPopupMenu getPopupMenu(final UserBean bean,
			final Object cell) {


		if (cell != null) {
			final JPopupMenu popupMenu = new JPopupMenu();

			final SystemUser user = bean.getUser();
			final Set<Identifier> roleIds = user.getRoleIds();
			
			for(final Role role : bean.getRoles()) {

				
				final Action action = new AbstractAction(role.getDescription()){
					
					public void actionPerformed(final ActionEvent e) {
						final JCheckBoxMenuItem checkBoxMenuItem = 
							(JCheckBoxMenuItem) e.getSource();
						
						if (checkBoxMenuItem.isSelected()) {
							user.addRole(role);
						} else {
							user.removeRole(role);
						}
						
					}
				};
				
				final JCheckBoxMenuItem checkBoxMenuItem =
					new JCheckBoxMenuItem(action);
				checkBoxMenuItem.setSelected(roleIds.contains(role));
				
				popupMenu.add(checkBoxMenuItem);
			}
			
			popupMenu.addSeparator();
			
			popupMenu.add(new AbstractAction(I18N.getString("Manager.Dialog.GotoUserPermissions")) {

				public void actionPerformed(ActionEvent e) {					
					managerMainFrame.setPerspective(new SystemUserPerpective(managerMainFrame, bean, cell));
				}
			});
			
			return popupMenu;
		}

		return null;	
	}
}

