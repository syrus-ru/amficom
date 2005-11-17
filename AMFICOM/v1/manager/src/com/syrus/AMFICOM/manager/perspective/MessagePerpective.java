/*-
* $Id: MessagePerpective.java,v 1.1 2005/11/17 09:00:35 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.perspective;

import static com.syrus.AMFICOM.event.DeliveryAttributesWrapper.COLUMN_SEVERITY;
import static com.syrus.AMFICOM.general.ObjectEntities.DELIVERYATTRIBUTES_CODE;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort.OPERATION_EQUALS;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JToolBar;

import org.jgraph.graph.DefaultGraphCell;

import com.syrus.AMFICOM.administration.Role;
import com.syrus.AMFICOM.event.DeliveryAttributes;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.manager.UI.GraphRoutines;
import com.syrus.AMFICOM.manager.beans.AbstractBean;
import com.syrus.AMFICOM.manager.beans.RoleBean;
import com.syrus.AMFICOM.manager.beans.RoleBeanFactory;
import com.syrus.AMFICOM.manager.perspective.AbstractPerspective.Chechable;
import com.syrus.AMFICOM.reflectometry.ReflectogramMismatch.Severity;
import com.syrus.AMFICOM.resource.LayoutItem;


/**
 * @version $Revision: 1.1 $, $Date: 2005/11/17 09:00:35 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public abstract class MessagePerpective extends AbstractPerspective {

	private LayoutItem layoutItem;  
	
	protected abstract Severity getSeverity();
	
//	private void addSeverityAction(final Severity severity) throws ApplicationException {
//		final MessageBeanFactory messageBeanFactory = 
//			MessageBeanFactory.getInstance(this.managerMainFrame, severity);
//		
//		final AbstractAction action = 
//			this.createGetTheSameOrCreateNewAction(messageBeanFactory, 
//				new MessageCheckable(severity), 
//				null);
//		this.managerMainFrame.addAction(
//			action);
//	}
	
	public void addEntities(final JToolBar entityToolBar) 
	throws ApplicationException {
		
//		this.addSeverityAction(this.getSeverity());
//		
//		entityToolBar.addSeparator();
		
		final Set<Role> roles = StorableObjectPool.getStorableObjectsByCondition(
			new EquivalentCondition(ObjectEntities.ROLE_CODE),
			true);
		
		final RoleBeanFactory roleBeanFactory = 
			(RoleBeanFactory) this.perspectiveData.getBeanFactory(ObjectEntities.ROLE);
		
		final GraphRoutines graphRoutines = this.managerMainFrame.getGraphRoutines();
		
		final DefaultGraphCell parentCell = graphRoutines.getDefaultGraphCell(this.layoutItem);
		
		for (final Role role : roles) {
			final Identifier id = role.getId();
			final AbstractAction action = this.createGetTheSameOrCreateNewAction(roleBeanFactory, 
					new RoleCheckable(id), 
					parentCell,
					this.getIdentifierString(id));
			action.putValue(Action.SHORT_DESCRIPTION, role.getName());
			this.managerMainFrame.addAction(
				action);
		}
	}
	
	public String getCodename() {
		return this.getSeverity().name().replaceAll("_", "");
	}
	
	public String getName() {		
		return this.getSeverity().getLocalizedName();
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
		Set<DeliveryAttributes> deliveryAttributes = 
			StorableObjectPool.getStorableObjectsByCondition(
				new TypicalCondition(
					this.getSeverity(),
					OPERATION_EQUALS,
					DELIVERYATTRIBUTES_CODE,
					COLUMN_SEVERITY), 
				true);
		
		assert deliveryAttributes.size() <= 1;
		
		if (deliveryAttributes.isEmpty()) {
			final DeliveryAttributes attributes = 
				DeliveryAttributes.createInstance(LoginManager.getUserId(), this.getSeverity());
			deliveryAttributes = Collections.singleton(attributes);
		}
		
		final String codename = this.getCodename();
		final Set<LayoutItem> items = StorableObjectPool.getStorableObjectsByCondition(
			new CompoundCondition(
				new TypicalCondition(codename,
					OperationSort.OPERATION_EQUALS,					
					ObjectEntities.LAYOUT_ITEM_CODE,
					StorableObjectWrapper.COLUMN_NAME),
				CompoundConditionSort.AND,
				new LinkedIdsCondition(LoginManager.getUserId(), ObjectEntities.LAYOUT_ITEM_CODE)),
			true);
		
		final Map<Identifier, LayoutItem> existsNetworkLayoutItems = new HashMap<Identifier, LayoutItem>();
		
		this.addItems(deliveryAttributes, existsNetworkLayoutItems, items);
		
		for (final DeliveryAttributes attributes : deliveryAttributes) {			
			this.addItems(attributes.getRoles(), existsNetworkLayoutItems, items);
			
			final LayoutItem item = this.getLayoutItem(attributes.getId(), 
				Identifier.VOID_IDENTIFIER, 
				existsNetworkLayoutItems);
			if (this.layoutItem == null) {
				this.layoutItem = item;
			}
			final Identifier itemId = item.getId();
			final Set<Identifier> roleIds = attributes.getRoleIds();			
			for (final Identifier identifier : roleIds) {
				this.getLayoutItem(identifier, 
					itemId, 
					existsNetworkLayoutItems);

			}
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

