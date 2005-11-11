/*-
* $Id: RolePerpective.java,v 1.5 2005/11/11 10:58:02 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager;

import java.util.Set;

import javax.swing.JToolBar;

import org.jgraph.JGraph;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;
import org.jgraph.graph.Port;

import com.syrus.AMFICOM.administration.PermissionAttributes;
import com.syrus.AMFICOM.administration.Role;
import com.syrus.AMFICOM.administration.PermissionAttributes.Module;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CompoundCondition;
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
import com.syrus.AMFICOM.resource.LayoutItem;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.5 $, $Date: 2005/11/11 10:58:02 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class RolePerpective extends AbstractPerspective {

	private Role	role;
	private LayoutItem layoutItem;  
	
	public RolePerpective(final RoleBean roleBean) {
		this(roleBean.getRole());		
	}
	
	public RolePerpective(final Role role) {
		this.role = role;
	}
	
	public void addEntities(final JToolBar entityToolBar) 
	throws ApplicationException {
		final RolePermissionBeanFactory factory = 
			(RolePermissionBeanFactory) this.getPerspectiveData().getBeanFactory(ObjectEntities.PERMATTR);
		
		final GraphRoutines graphRoutines = this.managerMainFrame.getGraphRoutines();
		final DefaultGraphCell parentCell = graphRoutines.getDefaultGraphCell(this.layoutItem);
		
		class ModuleCheckable implements Chechable {
			
			private final Module module;
			
			public ModuleCheckable(final Module module) {
				this.module = module;
			}
			
			public boolean isNeedIn(final AbstractBean bean) {
				if (bean instanceof RolePermissionBean) {
					final RolePermissionBean permissionBean = (RolePermissionBean) bean;
					return permissionBean.getPermissionAttributes().getModule() == this.module;
				}
				return false;
			}			
		}
		
		
		
		for(final Module module : Module.getValueList()) {
			if (!module.isEnable()) {
				continue;
			}
			
			final ModuleCheckable moduleCheckable = new ModuleCheckable(module);
			
			this.managerMainFrame.addAction(
				this.createGetTheSameOrCreateNewAction(factory.getInstance(module), 
					moduleCheckable, 
					parentCell));
		}
	}
	
	public String getCodename() {
		return this.role.getId().getIdentifierString();
	}
	
	public String getName() {		
		return I18N.getString("Manager.Entity.Role") + ':' 
			+ this.role.getName();
	}

	public final Identifier getRoleId() {
		return this.role.getId();
	}
	
	public boolean isValid() {
		JGraph graph = this.managerMainFrame.getGraph();
		final GraphLayoutCache graphLayoutCache = graph.getGraphLayoutCache();
		final GraphModel model = graph.getModel();
		final String identifierString = this.getRoleId().getIdentifierString();

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
						!port.getBean().getId().startsWith(identifierString)) {
					return false;
				}
				
			}
			
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	public void perspectiveApplied() 
	throws ApplicationException {
		final GraphRoutines graphRoutines = this.managerMainFrame.getGraphRoutines();
		graphRoutines.showLayerName(this.getCodename());
		final DefaultGraphCell userCell = 
			graphRoutines.getDefaultGraphCell(this.layoutItem);
		
		this.managerMainFrame.getTreeModel().setRoot(userCell);
	}

	public void createNecessaryItems() throws ApplicationException {
		final String identifierString = this.role.getId().getIdentifierString();
		final Set<LayoutItem> parentItems = StorableObjectPool.getStorableObjectsByCondition(
			new CompoundCondition(
				new TypicalCondition(identifierString,
					OperationSort.OPERATION_EQUALS,					
					ObjectEntities.LAYOUT_ITEM_CODE,
					StorableObjectWrapper.COLUMN_NAME),
				CompoundConditionSort.AND,
				new LinkedIdsCondition(LoginManager.getUserId(), ObjectEntities.LAYOUT_ITEM_CODE)),
			true);
		
		if (parentItems.isEmpty()) {
			this.layoutItem = 
				LayoutItem.createInstance(LoginManager.getUserId(), 
					Identifier.VOID_IDENTIFIER, 
					identifierString, 
					identifierString);
			assert Log.debugMessage("create layout item for " 
					+ this.role
					+ " as "
					+ this.layoutItem.getName()
					+ '@'
					+ this.layoutItem.getLayoutName(), 
				Log.DEBUGLEVEL03);
		} else {
			this.layoutItem = parentItems.iterator().next();
		}
		
		final Set<PermissionAttributes> permissionAttributes = 
			StorableObjectPool.getStorableObjectsByCondition(
				new LinkedIdsCondition(this.role.getId(), 
					ObjectEntities.PERMATTR_CODE), 
			true);
		
		final Identifier parentId = this.layoutItem.getId();
		
		final Set<LayoutItem> permissionAttributeItems = StorableObjectPool.getStorableObjectsByCondition(
			new CompoundCondition(
				new LinkedIdsCondition(parentId, ObjectEntities.LAYOUT_ITEM_CODE),
				CompoundConditionSort.AND,
				new LinkedIdsCondition(LoginManager.getUserId(), ObjectEntities.LAYOUT_ITEM_CODE)),
			true);
		
		for(final PermissionAttributes permissionAttribute : permissionAttributes) {
			final String id = permissionAttribute.getId().getIdentifierString();
			
			boolean found = false;
			
			for (final LayoutItem item : permissionAttributeItems) {
				if (item.getName().equals(id)) {
					found = true;
					break;
				}
			}
			
			if (!found) {
				final LayoutItem item = LayoutItem.createInstance(LoginManager.getUserId(), 
						parentId, 
						this.role.getId().getIdentifierString(), 
						id);
				assert Log.debugMessage("create layout item for " 
					+ id
					+ " as "
					+ item.getName()
					+ '@'
					+ item.getLayoutName(), 
				Log.DEBUGLEVEL03);
			}
		}
	}
}

