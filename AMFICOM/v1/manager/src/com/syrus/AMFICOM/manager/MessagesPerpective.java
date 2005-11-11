/*-
* $Id: MessagesPerpective.java,v 1.4 2005/11/11 13:47:08 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JToolBar;

import com.syrus.AMFICOM.administration.Role;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.event.DeliveryAttributes;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.manager.UI.GraphRoutines;
import com.syrus.AMFICOM.measurement.KIS;
import com.syrus.AMFICOM.reflectometry.ReflectogramMismatch.Severity;
import com.syrus.AMFICOM.resource.LayoutItem;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.4 $, $Date: 2005/11/11 13:47:08 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class MessagesPerpective extends AbstractPerspective {

	private void addSeverityAction(final Severity severity) throws ApplicationException {
		final MessageBeanFactory messageBeanFactory = 
			MessageBeanFactory.getInstance(this.managerMainFrame, severity);
		
		final AbstractAction action = 
			this.createGetTheSameOrCreateNewAction(messageBeanFactory, 
				new MessageCheckable(severity), 
				null);
		this.managerMainFrame.addAction(
			action);
	}
	
	public void addEntities(final JToolBar entityToolBar) 
	throws ApplicationException {
		
		final PerspectiveData perspectiveData = this.getPerspectiveData();
		
		
		this.addSeverityAction(Severity.SEVERITY_SOFT);
		this.addSeverityAction(Severity.SEVERITY_HARD);
		
		entityToolBar.addSeparator();
		
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
		final Set<DeliveryAttributes> deliveryAttributes = 
			StorableObjectPool.getStorableObjectsByCondition(
				new EquivalentCondition(ObjectEntities.DELIVERYATTRIBUTES_CODE), 
				true);
		
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
			final Identifier itemId = item.getId();
			final Set<Identifier> roleIds = attributes.getRoleIds();			
			for (final Identifier identifier : roleIds) {
				this.getLayoutItem(identifier, 
					itemId, 
					existsNetworkLayoutItems);

			}
		}
	}
	
	private class MessageCheckable implements Chechable {
		
		private final Severity severity;
		
		public MessageCheckable(final Severity severity) {
			this.severity = severity;
		}
		
		public boolean isNeedIn(final AbstractBean bean) {
			if (bean instanceof MessageBean) {
				MessageBean messageBean = (MessageBean)bean;
				final Severity severity2 = messageBean.getSeverity();
				assert Log.debugMessage(severity2 
					+ ", expected: " + this.severity, Log.DEBUGLEVEL10);

				return severity2 == this.severity; 
			}
			return false;
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

