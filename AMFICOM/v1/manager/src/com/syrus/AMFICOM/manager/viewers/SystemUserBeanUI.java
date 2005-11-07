/*-
* $Id: SystemUserBeanUI.java,v 1.2 2005/11/07 15:24:19 bob Exp $
*
* Copyright © 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.viewers;

import java.awt.event.ActionEvent;
import java.util.Set;

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
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.manager.Perspective;
import com.syrus.AMFICOM.manager.RoleBean;
import com.syrus.AMFICOM.manager.RolePerpective;
import com.syrus.AMFICOM.manager.SystemUserPerpective;
import com.syrus.AMFICOM.manager.UserBean;
import com.syrus.AMFICOM.manager.UserBeanWrapper;
import com.syrus.AMFICOM.manager.UI.GraphRoutines;
import com.syrus.AMFICOM.manager.UI.ManagerMainFrame;
import com.syrus.AMFICOM.resource.LayoutItem;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.2 $, $Date: 2005/11/07 15:24:19 $
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
	public JPopupMenu getPopupMenu(final UserBean userBean,
			final Object cell) {

		final ManagerMainFrame managerMainFrame1 = this.managerMainFrame;

		final Perspective perspective = this.managerMainFrame.getPerspective();

		if (cell != null) {
			if (!(perspective instanceof SystemUserPerpective)) {
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
						managerMainFrame1.setPerspective(
							new SystemUserPerpective(managerMainFrame1, 
								userBean, 
								(DefaultGraphCell) cell));
					}
				};
				
				final Icon enterIcon = UIManager.getIcon(ENTER_ICON);
				if (enterIcon != null) {
					enterAction.putValue(Action.SMALL_ICON, enterIcon);
				}
				
				
				popupMenu.add(enterAction);
				return popupMenu;
			} 
			
			// otherwise only SystemUserPerpective supported
			
			{
				final JPopupMenu popupMenu = new JPopupMenu();

				final Icon enterIcon = UIManager.getIcon(ENTER_ICON);

				final GraphRoutines graphRoutines = 
					this.managerMainFrame.getGraphRoutines();
				popupMenu.addSeparator();
				popupMenu.add(I18N.getString("Manager.Dialog.GotoPermissions") + ":");
				
				for(final Role role : userBean.getRoles()) {				
					final String title = role.getDescription();
					
					final Action action = new AbstractAction(title){
						
						@SuppressWarnings("unchecked")
						public void actionPerformed(final ActionEvent e) {	
							try {
								final String roleIdentifier = role.getId().getIdentifierString();
								assert Log.debugMessage( roleIdentifier, Log.DEBUGLEVEL03);
								final CompoundCondition compoundCondition = 
									new CompoundCondition(new TypicalCondition(
										roleIdentifier, 
										OperationSort.OPERATION_EQUALS,
										ObjectEntities.LAYOUT_ITEM_CODE,
										StorableObjectWrapper.COLUMN_NAME),
										CompoundConditionSort.AND,
										new LinkedIdsCondition(
											LoginManager.getUserId(),
											ObjectEntities.LAYOUT_ITEM_CODE) 
//											{
//											@Override
//											public boolean isNeedMore(Set< ? extends Identifiable> storableObjects) {
//												return storableObjects.isEmpty();
//											}
//										}
											);
								
								final Set<LayoutItem> layoutItems = 
									StorableObjectPool.getStorableObjectsByCondition(compoundCondition, true);	
								
								if (layoutItems.isEmpty()) {									
									managerMainFrame1.setPerspective(
										new RolePerpective(managerMainFrame1, role));
								} else {
									final LayoutItem layoutItem = layoutItems.iterator().next();
									final RoleBean roleBean = (RoleBean) graphRoutines.getBean(layoutItem);
									managerMainFrame1.setPerspective(
										new RolePerpective(managerMainFrame1, roleBean));
								}
								
								
							} catch (ApplicationException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}							
						}
					};
					
					if (enterIcon != null) {
						action.putValue(Action.SMALL_ICON, enterIcon);
					}
					
					popupMenu.add(action);
				}
				popupMenu.addSeparator();
				return popupMenu;
			}

			
			
		}

		return null;	
	}
}

