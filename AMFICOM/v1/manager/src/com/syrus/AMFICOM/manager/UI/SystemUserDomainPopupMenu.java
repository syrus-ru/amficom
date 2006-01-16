/*-
* $Id: SystemUserDomainPopupMenu.java,v 1.12 2006/01/16 11:04:27 bob Exp $
*
* Copyright © 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.UI;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import org.jgraph.graph.DefaultGraphCell;

import com.syrus.AMFICOM.administration.PermissionAttributes;
import com.syrus.AMFICOM.administration.Role;
import com.syrus.AMFICOM.administration.SystemUser;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Checker;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.manager.beans.UserBean;
import com.syrus.AMFICOM.manager.graph.MPort;
import com.syrus.AMFICOM.manager.perspective.DomainPerpective;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.12 $, $Date: 2006/01/16 11:04:27 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class SystemUserDomainPopupMenu extends AbstractItemPopupMenu<DomainPerpective> {	
	
	@Override
	public JPopupMenu getPopupMenu(final DefaultGraphCell cell,
			final DomainPerpective perpective) throws ApplicationException {
		
		final boolean permitted = 
			Checker.isPermitted(PermissionAttributes.PermissionCodename.ADMINISTRATION_CHANGE_USER);
		if (!permitted) {
			return null;
		}
		
		final MPort port = (MPort) cell.getChildAt(0);
		final UserBean userBean = (UserBean) port.getBean();
		final SystemUser user = userBean.getUser();
		
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
		
		final StorableObjectVersion version = user.getVersion();
		if (StorableObjectVersion.INITIAL_VERSION != version) {
			popupMenu.addSeparator();
			final AbstractAction changePasswordAction = 
				new AbstractAction(I18N.getString("Manager.Dialog.ChangeUserPassword")) {
	
				public void actionPerformed(final ActionEvent e) {
					final PasswordSetter setter = new PasswordSetter(user);
					setter.initUI();
				}
			};
			popupMenu.add(changePasswordAction);
			
		}
		
		return popupMenu;
	}
	
	private final class PasswordSetter extends PasswordDialog {
		
		private final SystemUser	systemUser;

		public PasswordSetter(final SystemUser systemUser) {
			this.systemUser = systemUser;
		}
		
		@Override
		protected String getTitle() {
			return this.systemUser.getName()
				+ ':'
				+ I18N.getString("Common.ChangePassword.ChangingPassword");
		}
		
		@Override
		protected void applyPassword(final char[] password) {
			try {
				LoginManager.setPassword(this.systemUser.getId(), new String(password));	
				JOptionPane.showMessageDialog(null, 
					I18N.getString("Manager.Label.PasswordChangedSuccessful"), 
					I18N.getString("Manager.Label.Info"), 
					JOptionPane.INFORMATION_MESSAGE);
			} catch (final CommunicationException e) {
				Log.errorMessage(e);
				JOptionPane.showMessageDialog(null, 
					I18N.getString("Manager.Error.ErrorDuringPasswordChanging"), 
					I18N.getString("Error"), 
					JOptionPane.ERROR_MESSAGE);
			} catch (AMFICOMRemoteException e) {
				Log.errorMessage(e);
				JOptionPane.showMessageDialog(null, 
					I18N.getString("Manager.Error.ErrorDuringPasswordChanging"), 
					I18N.getString("Error"), 
					JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}

