/*-
 * $Id: RoleBean.java,v 1.2 2005/11/28 14:47:05 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.manager.beans;

import java.beans.PropertyChangeEvent;
import java.util.Set;

import com.syrus.AMFICOM.administration.Role;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.manager.UI.ManagerModel;
import com.syrus.AMFICOM.manager.graph.MPort;
import com.syrus.AMFICOM.resource.LayoutItem;
import com.syrus.AMFICOM.resource.LayoutItemWrapper;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.2 $, $Date: 2005/11/28 14:47:05 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class RoleBean extends Bean {

	private Role	role;

	RoleBean() {
		// nothing
	}
	
	@Override
	public void applyTargetPort(MPort oldPort, 
		MPort newPort) {
		assert Log.debugMessage("was:" + oldPort + ", now: "
			+ newPort, Log.DEBUGLEVEL03);
		AbstractBean oldPortBean = oldPort != null ? oldPort.getBean() : null;
		AbstractBean newPortBean = newPort != null ? newPort.getBean() : null;
		if (newPortBean instanceof MessageBean) {
			MessageBean messageBean = (MessageBean) newPortBean;
			messageBean.addRole(this.role);
		}   
		
		if (newPortBean == null && oldPortBean instanceof MessageBean) {
			MessageBean messageBean = (MessageBean) oldPortBean;
			messageBean.removeRole(this.role);
		}
		
	}	

	@Override
	public void dispose() throws ApplicationException {
		assert Log.debugMessage(this.identifier, Log.DEBUGLEVEL03);		
//		StorableObjectPool.delete(this.identifier);		
//		
//		final GraphRoutines graphRoutines = this.graphText.getGraphRoutines();
//		for(final LayoutItem layoutItem : this.getBeanChildrenLayoutItems()) {
//			Bean portBean = 
//				(Bean) graphRoutines.getBean(layoutItem);
//			portBean.dispose();
//		}
//
		super.disposeLayoutItem();
	}
	
	@Override
	protected void setIdentifier(final Identifier identifier) 
	throws ApplicationException {
		super.setIdentifier(identifier);
		this.role = StorableObjectPool.getStorableObject(this.identifier, true);
	}

	@Override
	public final String getName() {
		return this.role.getName();
	}

	@Override
	public final void setName(final String name) {
		String name2 = this.role.getName();
		if (name2 != name &&
				(name2 != null && !name2.equals(name) ||
				!name.equals(name2))) {
			String oldValue = name2;
			this.role.setName(name);
			final ManagerModel managerModel = (ManagerModel)this.managerMainFrame.getModel();
			final Dispatcher dispatcher = managerModel.getDispatcher();
			dispatcher.firePropertyChange(
				new PropertyChangeEvent(this, ObjectEntities.ROLE, null, this));
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, UserBeanWrapper.NAME, oldValue, name));
		}
	}
	
	private Set<LayoutItem> getBeanChildrenLayoutItems() 
	throws ApplicationException {
		final TypicalCondition typicalCondition = 
			new TypicalCondition(this.id, 
				OperationSort.OPERATION_EQUALS, 
				ObjectEntities.LAYOUT_ITEM_CODE, 
				LayoutItemWrapper.COLUMN_LAYOUT_NAME);

		final Set<LayoutItem> beanLayoutItems = StorableObjectPool.getStorableObjectsByCondition(
			typicalCondition, 
			true, 
			true);
		
		final LinkedIdsCondition linkedIdsCondition = 
			new LinkedIdsCondition(Identifier.createIdentifiers(beanLayoutItems),
				ObjectEntities.LAYOUT_ITEM_CODE);
		
		return StorableObjectPool.getStorableObjectsByCondition(
			linkedIdsCondition, 
			true, 
			true);
	}	

	@Override
	public String getCodename() {
		return ObjectEntities.ROLE;
	}

	
	public final Role getRole() {
		return this.role;
	}	
}
