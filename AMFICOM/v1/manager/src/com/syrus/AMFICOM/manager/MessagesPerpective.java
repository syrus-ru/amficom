/*-
* $Id: MessagesPerpective.java,v 1.1 2005/11/09 15:08:45 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager;

import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JToolBar;

import com.syrus.AMFICOM.administration.Role;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.manager.UI.GraphRoutines;
import com.syrus.AMFICOM.manager.UI.ManagerMainFrame;


/**
 * @version $Revision: 1.1 $, $Date: 2005/11/09 15:08:45 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class MessagesPerpective extends AbstractPerspective {

	public MessagesPerpective(final ManagerMainFrame managerMainFrame) {
		super(managerMainFrame);
	}
	
	public void addEntities(final JToolBar entityToolBar) 
	throws ApplicationException {
		
		final PerspectiveData perspectiveData = this.getPerspectiveData();
		final MessageBeanFactory messageBeanFactory = 
			(MessageBeanFactory) perspectiveData.getBeanFactory(MessageBeanFactory.MESSAGE_CODENAME);
		
		this.managerMainFrame.addAction(
			this.createGetTheSameOrCreateNewAction(messageBeanFactory, 
				new MessageCheckable("Simple"), 
				null));
		
		final Set<Role> roles = StorableObjectPool.getStorableObjectsByCondition(
			new EquivalentCondition(ObjectEntities.ROLE_CODE),
			true);
		
		final RoleBeanFactory roleBeanFactory = 
			(RoleBeanFactory) perspectiveData.getBeanFactory(ObjectEntities.ROLE);
		
		for (final Role role : roles) {
			final Identifier id = role.getId();
			final AbstractAction action = this.createGetTheSameOrCreateNewAction(roleBeanFactory, 
					new RoleCheckable(id), 
					null,
					this.getIdentifierString(id));
			action.putValue(Action.SHORT_DESCRIPTION, role.getName());
			this.managerMainFrame.addAction(
				action);
		}
	}
	
	public String getCodename() {
		return "messages";
	}
	
	public String getName() {		
		return I18N.getString("Manager.Entity.Messages");
	}
	
	public boolean isValid() {
		return true;
	}

	@SuppressWarnings("unchecked")
	public void perspectiveApplied() 
	throws ApplicationException {		
		final GraphRoutines graphRoutines = this.managerMainFrame.getGraphRoutines();
		
		graphRoutines.showLayerName(this.getCodename());
	}

	public void createNecessaryItems() throws ApplicationException {
		
	}
	
	private class MessageCheckable implements Chechable {
		
		private final String message;
		
		public MessageCheckable(final String message) {
			this.message = message;
		}
		
		public boolean isNeedIn(final AbstractBean bean) {
			return bean.getName().equals(this.message);
		}			
	}
	
	private class RoleCheckable implements Chechable {
		
		private final Identifier roleId;
		
		public RoleCheckable(final Identifier roleId) {
			this.roleId = roleId;
		}
		
		public boolean isNeedIn(final AbstractBean bean) {
			if (bean instanceof RoleBean) {
				final RoleBean roleBean = (RoleBean)bean;
				return roleBean.getIdentifier().equals(this.roleId);
			}
			return false;
		}			
	}
}

