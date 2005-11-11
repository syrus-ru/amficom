/*-
* $Id: SystemUserPermissionPopupMenu.java,v 1.1 2005/11/11 10:58:02 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.UI;

import java.awt.event.ActionEvent;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
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
import com.syrus.AMFICOM.manager.MPort;
import com.syrus.AMFICOM.manager.RoleBean;
import com.syrus.AMFICOM.manager.RolePerpective;
import com.syrus.AMFICOM.manager.UserBean;
import com.syrus.AMFICOM.manager.viewers.TableBeanUI;
import com.syrus.AMFICOM.resource.LayoutItem;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.1 $, $Date: 2005/11/11 10:58:02 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class SystemUserPermissionPopupMenu extends AbstractItemPopupMenu {	
	
	@Override
	public JPopupMenu getPopupMenu(final DefaultGraphCell cell,
			final ManagerMainFrame managerMainFrame) {
		
		final MPort port = (MPort) cell.getChildAt(0);
		final UserBean userBean = (UserBean) port.getBean();
		

		final JPopupMenu popupMenu = new JPopupMenu();

		final Icon enterIcon = UIManager.getIcon(TableBeanUI.ENTER_ICON);

		final GraphRoutines graphRoutines = 
			managerMainFrame.getGraphRoutines();
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
//									{
//									@Override
//									public boolean isNeedMore(Set< ? extends Identifiable> storableObjects) {
//										return storableObjects.isEmpty();
//									}
//								}
									);
						
						final Set<LayoutItem> layoutItems = 
							StorableObjectPool.getStorableObjectsByCondition(compoundCondition, true);	
						
						if (layoutItems.isEmpty()) {									
							managerMainFrame.setPerspective(new RolePerpective(role));
						} else {
							final LayoutItem layoutItem = layoutItems.iterator().next();
							final RoleBean roleBean = (RoleBean) graphRoutines.getBean(layoutItem);
							managerMainFrame.setPerspective(
								new RolePerpective(roleBean));
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

