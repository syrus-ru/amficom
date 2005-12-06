/*-
* $Id: MessagePerpective.java,v 1.5 2005/12/06 15:14:39 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.perspective;

import static com.syrus.AMFICOM.event.DeliveryAttributesWrapper.COLUMN_SEVERITY;
import static com.syrus.AMFICOM.general.ObjectEntities.DELIVERYATTRIBUTES_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.LAYOUT_ITEM_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.ROLE;
import static com.syrus.AMFICOM.general.ObjectEntities.ROLE_CODE;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort.OPERATION_EQUALS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Action;

import org.jgraph.graph.DefaultGraphCell;

import com.syrus.AMFICOM.administration.Role;
import com.syrus.AMFICOM.event.DeliveryAttributes;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.manager.UI.GraphRoutines;
import com.syrus.AMFICOM.manager.beans.AbstractBean;
import com.syrus.AMFICOM.manager.beans.RoleBean;
import com.syrus.AMFICOM.manager.beans.RoleBeanFactory;
import com.syrus.AMFICOM.reflectometry.ReflectogramMismatch.Severity;
import com.syrus.AMFICOM.resource.LayoutItem;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.5 $, $Date: 2005/12/06 15:14:39 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public abstract class MessagePerpective extends AbstractPerspective {

	private LayoutItem layoutItem;  
	
	protected abstract Severity getSeverity();
	
	public final LayoutItem getParentLayoutItem() {
		return this.layoutItem;
	}
	
	@Override
	protected final void createActions() throws ApplicationException {
		this.actions = new ArrayList<AbstractAction>();
		final Set<Role> roles = StorableObjectPool.getStorableObjectsByCondition(
			new EquivalentCondition(ROLE_CODE),
			true);
		
		final RoleBeanFactory roleBeanFactory = 
			(RoleBeanFactory) this.perspectiveData.getBeanFactory(ROLE);
		
		final GraphRoutines graphRoutines = this.managerMainFrame.getGraphRoutines();
		
		final DefaultGraphCell parentCell = graphRoutines.getDefaultGraphCell(this.layoutItem);
		
		for (final Role role : roles) {
			final Identifier id = role.getId();
			final AbstractAction action = this.createGetTheSameOrCreateNewAction(roleBeanFactory, 
					new RoleCheckable(id), 
					parentCell,
					this.getIdentifierString(id));
			action.putValue(Action.SHORT_DESCRIPTION, role.getName());
			this.actions.add(action);
		}
	}
	
	public final String getCodename() {
		return this.getSeverity().name().replaceAll("_", "");
	}
	
	public String getName() {		
		return this.getSeverity().getLocalizedName();
	}
	
	public boolean isValid() {
		return true;
	}

	@SuppressWarnings("unchecked")
	public final void perspectiveApplied() 
	throws ApplicationException {		
		final GraphRoutines graphRoutines = this.managerMainFrame.getGraphRoutines();
		
		graphRoutines.showLayerName(this.getCodename(), false);
	}

	public final void createNecessaryItems() throws ApplicationException {
		assert Log.debugMessage(this.getCodename(), Log.DEBUGLEVEL10);
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
				DeliveryAttributes.getInstance(LoginManager.getUserId(), this.getSeverity());
			deliveryAttributes = Collections.singleton(attributes);
		}
		
		final String codename = this.getCodename();
		final Set<LayoutItem> items = StorableObjectPool.getStorableObjectsByCondition(
			new CompoundCondition(
				new TypicalCondition(codename,
					OperationSort.OPERATION_EQUALS,					
					LAYOUT_ITEM_CODE,
					StorableObjectWrapper.COLUMN_NAME),
				CompoundConditionSort.AND,
				new LinkedIdsCondition(LoginManager.getUserId(), LAYOUT_ITEM_CODE)),
			true);
		
		final Map<Identifier, LayoutItem> existsNetworkLayoutItems = new HashMap<Identifier, LayoutItem>();
		
		this.addItems(deliveryAttributes, existsNetworkLayoutItems, items);
		
		assert Log.debugMessage(deliveryAttributes, Log.DEBUGLEVEL10);
		
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
			assert Log.debugMessage(roleIds, Log.DEBUGLEVEL10);
			for (final Identifier identifier : roleIds) {				
				this.getLayoutItem(identifier, 
					itemId, 
					existsNetworkLayoutItems);

			}
		}
	}
	
	public final Perspective getSuperPerspective() {
		return null;
	}
	
	public final Perspective getSubPerspective(AbstractBean bean) {
		return null;
	}
	
	private class RoleCheckable implements Chechable<RoleBean> {
		
		private final Identifier roleId;
		
		public RoleCheckable(final Identifier roleId) {
			this.roleId = roleId;
		}
		
		public boolean isNeedIn(final RoleBean bean) {
			return bean.getIdentifier().equals(this.roleId);
		}			
	}
}

