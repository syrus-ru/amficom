/*-
* $Id: SystemUserDomainPopupMenu.java,v 1.1 2005/11/11 10:58:02 bob Exp $
*
* Copyright © 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.UI;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.UIManager;

import org.jgraph.graph.DefaultGraphCell;

import com.syrus.AMFICOM.administration.Role;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.manager.MPort;
import com.syrus.AMFICOM.manager.SystemUserPerpective;
import com.syrus.AMFICOM.manager.UserBean;
import com.syrus.AMFICOM.manager.viewers.TableBeanUI;


/**
 * @version $Revision: 1.1 $, $Date: 2005/11/11 10:58:02 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class SystemUserDomainPopupMenu extends AbstractItemPopupMenu {	
	
	@Override
	public JPopupMenu getPopupMenu(final DefaultGraphCell cell,
			final ManagerMainFrame managerMainFrame) {
		
		final MPort port = (MPort) cell.getChildAt(0);
		final UserBean userBean = (UserBean) port.getBean();
		
		final JPopupMenu popupMenu = new JPopupMenu();
		
		for(final Role role : userBean.getRoles()) {

			
			final Action action = new AbstractAction(role.getDescription()){
				
				public void actionPerformed(final ActionEvent e) {
					final JCheckBoxMenuItem checkBoxMenuItem = 
						(JCheckBoxMenuItem) e.getSource();
					
					try {
						if (checkBoxMenuItem.isSelected()) {								
							// XXX : сбрасывать deny mask при пересечении с ролями
							userBean.addRole(role);
						} else {
							userBean.removeRole(role);
						}
					} catch (final ApplicationException ae) {
						// TODO: handle exception
					}
					
				}
			};
			
			final JCheckBoxMenuItem checkBoxMenuItem =
				new JCheckBoxMenuItem(action);
			checkBoxMenuItem.setSelected(userBean.containsRole(role));
			
			popupMenu.add(checkBoxMenuItem);
		}
		
		popupMenu.addSeparator();
		
		final AbstractAction enterAction = new AbstractAction(I18N.getString("Manager.Dialog.GotoUserPermissions")) {

			public void actionPerformed(ActionEvent e) {					
				managerMainFrame.setPerspective(
					new SystemUserPerpective(userBean, 
						cell));
			}
		};
		
		final Icon enterIcon = UIManager.getIcon(TableBeanUI.ENTER_ICON);
		if (enterIcon != null) {
			enterAction.putValue(Action.SMALL_ICON, enterIcon);
		}
		
		
		popupMenu.add(enterAction);
		return popupMenu;
	}
	
}

