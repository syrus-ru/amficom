/*-
* $Id: SystemUserPerpective.java,v 1.7 2005/12/16 09:36:07 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.perspective;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractAction;

import org.jgraph.JGraph;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;
import org.jgraph.graph.Port;

import com.syrus.AMFICOM.administration.PermissionAttributes;
import com.syrus.AMFICOM.administration.PermissionAttributes.Module;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Checker;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.manager.UI.GraphRoutines;
import com.syrus.AMFICOM.manager.beans.AbstractBean;
import com.syrus.AMFICOM.manager.beans.PermissionBean;
import com.syrus.AMFICOM.manager.beans.PermissionBeanFactory;
import com.syrus.AMFICOM.manager.beans.UserBean;
import com.syrus.AMFICOM.manager.graph.MPort;
import com.syrus.AMFICOM.resource.LayoutItem;
import com.syrus.AMFICOM.resource.LayoutItemWrapper;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.7 $, $Date: 2005/12/16 09:36:07 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public final class SystemUserPerpective extends AbstractPerspective {

	private UserBean		userBean;
	private final Perspective	superPerspective;

	private LayoutItem	userLayoutItem;
	
	public SystemUserPerpective(final UserBean		userBean,
			final Perspective superPerspective) {
		this.superPerspective = superPerspective;
		this.userBean = userBean;
	}
	
	@Override
	protected void createActions() throws ApplicationException {
		this.actions = new ArrayList<AbstractAction>();
		
		final PermissionBeanFactory factory = 
			(PermissionBeanFactory) this.perspectiveData.
					getBeanFactory(ObjectEntities.PERMATTR);
		
		class ModuleCheckable implements Checkable {
			
			private final Module module;
			
			public ModuleCheckable(final Module module) {
				this.module = module;
			}
			
			public boolean isNeedIn(final AbstractBean bean) {
				if (bean instanceof PermissionBean) {
					final PermissionBean permissionBean = (PermissionBean) bean;
					return permissionBean.getPermissionAttributes().getModule() == this.module;
				}
				return false;
			}
			
		}		
		final GraphRoutines graphRoutines = 
			this.managerMainFrame.getGraphRoutines();
		
		DefaultGraphCell cell = null;
		for (final AbstractBean bean : this.getLayoutBeans()) {
			if (bean instanceof UserBean) {
				cell = 
					graphRoutines.getDefaultGraphCell(bean, this);
				break;
			}
		}
		
		final boolean permitted = 
			Checker.isPermitted(PermissionAttributes.PermissionCodename.ADMINISTRATION_CHANGE_USER);

		for(final Module module : Module.getValueList()) {
			if (!module.isEnable()) {
				continue;
			}
			
			final ModuleCheckable moduleCheckable = new ModuleCheckable(module);
			
			final AbstractAction action = 
				this.createGetTheSameOrCreateNewAction(factory.getUserInstance(module), 
					moduleCheckable, 
					cell);
			action.setEnabled(permitted);
			this.actions.add(action);
		}
	}
	
	public final LayoutItem getParentLayoutItem() {
		return this.userLayoutItem;
	}
	
	public String getCodename() {
		return this.getUserId().getIdentifierString();
	}
	
	public String getName() {		
		return this.userBean.getName();
	}
	
	public final Identifier getDomainId() {
		return this.userBean.getIdentifier();
	}
	
	public final Identifier getUserId() {
		return this.userBean.getIdentifier();
	}
	
	public boolean isValid() {
		JGraph graph = this.managerMainFrame.getGraph();
		final GraphLayoutCache graphLayoutCache = graph.getGraphLayoutCache();
		final GraphModel model = graph.getModel();
		for(final Object root : graph.getRoots()) {
			if (!model.isPort(root) 
					&& !model.isEdge(root) 
					&& graphLayoutCache.isVisible(root)) {
				MPort port = (MPort) ((DefaultGraphCell)root).getChildAt(0);
				int visibleTarget = 0;
				for(final Port targetPort : port.getTargets()) {
					visibleTarget += graphLayoutCache.isVisible(targetPort) ? 1 : 0;
				}

				if (visibleTarget == 0 && 
						!port.getBean().getId().startsWith(ObjectEntities.SYSTEMUSER)) {
					return false;
				}
				
			}
			
		}
		return true;
	}

	public void perspectiveApplied() throws ApplicationException {
		final GraphRoutines graphRoutines = this.managerMainFrame.getGraphRoutines();
		graphRoutines.showLayerName(this.getCodename(), false);
	}
	
	private LayoutItem getUserItem(final Identifier userId,
		final LinkedIdsCondition currentUserCondition,
		final TypicalCondition layoutCondition) 
	throws ApplicationException {

		final TypicalCondition userLayoutItemCondition = 
			new TypicalCondition(userId.getIdentifierString(),
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.LAYOUT_ITEM_CODE,
				StorableObjectWrapper.COLUMN_NAME);
		
		
		final Set<StorableObjectCondition> conditions = 
			new HashSet<StorableObjectCondition>();
		
		conditions.add(currentUserCondition);
		conditions.add(layoutCondition);
		conditions.add(userLayoutItemCondition);
		
		final Set<LayoutItem> userLayoutItems = 
			StorableObjectPool.getStorableObjectsByCondition(
				new CompoundCondition(conditions,
					CompoundConditionSort.AND), 
				true);
		
		final LayoutItem userItem;
		if (userLayoutItems.isEmpty()) {
			userItem = 
				this.getLayoutItem(userId.getIdentifierString(), 
					this.getCodename(), 
					Identifier.VOID_IDENTIFIER);
			assert Log.debugMessage("create layout item for " 
					+ userId
					+" as " 
					+ userItem.getName()
					+ '@'
					+ userItem.getLayoutName(), 
				Log.DEBUGLEVEL10);
		} else {
			assert userLayoutItems.size() == 1;			
			userItem = userLayoutItems.iterator().next();			
		}
		
		return userItem;
	
	}

	public void createNecessaryItems() throws ApplicationException {
		final Identifier userId = this.getUserId();

		assert Log.debugMessage(userId , Log.DEBUGLEVEL10);
		
		final LinkedIdsCondition currentUserCondition = 
			new LinkedIdsCondition(LoginManager.getUserId(), ObjectEntities.LAYOUT_ITEM_CODE);
		
		final TypicalCondition layoutCondition = 
			new TypicalCondition(this.getCodename(),
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.LAYOUT_ITEM_CODE,
				LayoutItemWrapper.COLUMN_LAYOUT_NAME);
		
		final Set<LayoutItem> userLayoutItems = 
			StorableObjectPool.getStorableObjectsByCondition(
				new CompoundCondition(currentUserCondition,
					CompoundConditionSort.AND,
					layoutCondition), 
				true);
		
		this.userLayoutItem = 
			this.getUserItem(userId, 
				currentUserCondition,
				layoutCondition);
		
		final Identifier userLayoutItemId = this.userLayoutItem.getId();
		
		// acquire permission attributes
		final LinkedIdsCondition domainCondition = 
			new LinkedIdsCondition(this.getDomainId(), ObjectEntities.PERMATTR_CODE);
		
		final LinkedIdsCondition userCondition = 
			new LinkedIdsCondition(userId, ObjectEntities.PERMATTR_CODE);
		
		final CompoundCondition compoundCondition 
			= new CompoundCondition(
				domainCondition, 
				CompoundConditionSort.AND,
				userCondition);
		
		final Set<PermissionAttributes> permissionAttributes = 
			StorableObjectPool.getStorableObjectsByCondition(compoundCondition, true);
		
		final Map<Identifier, LayoutItem> existsNetworkLayoutItems = 
			new HashMap<Identifier, LayoutItem>();

		this.addItems(permissionAttributes, existsNetworkLayoutItems, userLayoutItems);
		// create user items accorning to exist permission attributes		
		for (final PermissionAttributes attributes : permissionAttributes) {
			this.getLayoutItem(attributes.getId(), 
				userLayoutItemId, 
				existsNetworkLayoutItems);
		}
	}
	
	public Perspective getSuperPerspective() {
		return this.superPerspective;
	}
	
	public Perspective getSubPerspective(AbstractBean bean) {
		return null;
	}
	
	@Override
	public String toString() {
		return "SystemUserPerpective for " + this.getCodename();
	}
}

