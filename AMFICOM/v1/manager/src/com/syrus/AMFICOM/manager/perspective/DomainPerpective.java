/*-
* $Id: DomainPerpective.java,v 1.2 2005/11/28 14:47:05 bob Exp $
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

import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.DomainMember;
import com.syrus.AMFICOM.administration.MCM;
import com.syrus.AMFICOM.administration.PermissionAttributes;
import com.syrus.AMFICOM.administration.Server;
import com.syrus.AMFICOM.administration.SystemUser;
import com.syrus.AMFICOM.administration.PermissionAttributes.Module;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.manager.UI.GraphRoutines;
import com.syrus.AMFICOM.manager.beans.AbstractBean;
import com.syrus.AMFICOM.manager.beans.DomainBean;
import com.syrus.AMFICOM.manager.beans.NetBeanFactory;
import com.syrus.AMFICOM.manager.beans.UserBean;
import com.syrus.AMFICOM.manager.beans.WorkstationBeanFactory;
import com.syrus.AMFICOM.manager.graph.MPort;
import com.syrus.AMFICOM.measurement.KIS;
import com.syrus.AMFICOM.resource.LayoutItem;
import com.syrus.AMFICOM.resource.LayoutItemWrapper;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.2 $, $Date: 2005/11/28 14:47:05 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public final class DomainPerpective extends AbstractPerspective {

	private final DomainBean domainBean;
	private LayoutItem	domainNetworkItem;
	private final Map<Identifier, SystemUserPerpective> userPerspectiveMap;
	private boolean	firstStart;
	
	public DomainPerpective(final DomainBean domainBean) {
		assert Log.debugMessage(domainBean.getIdentifier(), Log.DEBUGLEVEL03);
		this.domainBean = domainBean;
		this.userPerspectiveMap = new HashMap<Identifier, SystemUserPerpective>();
		this.firstStart = true;
	}	
	
	@Override
	protected void createActions() throws ApplicationException {
		this.actions = new ArrayList<AbstractAction>();
		final GraphRoutines graphRoutines = this.managerMainFrame.getGraphRoutines();

		final DefaultGraphCell networkCell = 
			graphRoutines.getDefaultGraphCell(this.domainNetworkItem);
		
		this.actions.add(
			super.createAction(this.perspectiveData.getBeanFactory(ObjectEntities.SYSTEMUSER)));

		this.actions.add(
			super.createAction(this.perspectiveData.getBeanFactory(WorkstationBeanFactory.WORKSTATION_CODENAME), 
				networkCell));
		this.actions.add(
			super.createAction(this.perspectiveData.getBeanFactory(ObjectEntities.KIS), 
				networkCell));
		this.actions.add(
			super.createAction(this.perspectiveData.getBeanFactory(ObjectEntities.SERVER), 
				networkCell));
		this.actions.add(
			super.createAction(this.perspectiveData.getBeanFactory(ObjectEntities.MCM), 
				networkCell));
	}
	
	public String getCodename() {
		return this.getDomainId().getIdentifierString();
	}
	
	public String getName() {		
		return this.domainBean.getName();
	}
	
	public final Identifier getDomainId() {
		return this.domainBean.getIdentifier();
	}
	
	public boolean isValid() {
		final JGraph graph = this.managerMainFrame.getGraph();
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
						!port.getBean().getId().startsWith(NetBeanFactory.NET_CODENAME)) {
					return false;
				}
				
			}
			
		}
		return true;
	}

	public void insertBean(AbstractBean bean) {
		Identifier beanId = bean.getIdentifier();
		if (beanId != null) {
			try {
				final StorableObject storableObject = 
					StorableObjectPool.getStorableObject(beanId, true);
				if (storableObject instanceof DomainMember) {
					DomainMember  domainMember = (DomainMember)storableObject;
					domainMember.setDomainId(this.getDomainId());
				}
				if (storableObject instanceof SystemUser) {
					final SystemUser systemUser = (SystemUser) storableObject;
					final Identifier userId = systemUser.getId();
					
					for(final Module module : Module.getValueList()) {
						if (!module.isEnable()) {
							continue;
						}
						final Domain domain = this.domainBean.getDomain();
						PermissionAttributes permissionAttributes = 
							domain.getPermissionAttributes(userId, module);
						
						if (permissionAttributes == null) {
							permissionAttributes = 
								PermissionAttributes.createInstance(
									LoginManager.getUserId(),
									domain.getId(),
									userId,
									module);
						}
					}
				}
			} catch (final ApplicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public final LayoutItem getParentLayoutItem() {
		return this.domainNetworkItem;
	}
	
	private LayoutItem getDomainNetworkItem(final LinkedIdsCondition currentUserCondition,
			final TypicalCondition layoutCondition) 
	throws ApplicationException {

		final Set<StorableObjectCondition> domainsTypicalConditions = 
			new HashSet<StorableObjectCondition>(3);
		
		domainsTypicalConditions.add(currentUserCondition);
		domainsTypicalConditions.add(layoutCondition);
		
		final Set<LayoutItem> domainNetworkItems = 
			StorableObjectPool.getStorableObjectsByCondition(
				new CompoundCondition(domainsTypicalConditions,
					CompoundConditionSort.AND), 
				true);
		
		if (domainNetworkItems.isEmpty()) {
			return LayoutItem.createInstance(LoginManager.getUserId(), 
				Identifier.VOID_IDENTIFIER, 
				this.getCodename(), 
				NetBeanFactory.NET_CODENAME);
		}
		
		assert domainNetworkItems.size() == 1;
		return domainNetworkItems.iterator().next();
	
	}
	
	private LayoutItem getNetworkWorkstationItem(final Identifier networkId,
			final LinkedIdsCondition currentUserCondition,
			final TypicalCondition layoutCondition) 
	throws ApplicationException {

		final LinkedIdsCondition networkCondition = 
			new LinkedIdsCondition(networkId, ObjectEntities.LAYOUT_ITEM_CODE);
		
		final TypicalCondition typicalCondition = 
			new TypicalCondition(WorkstationBeanFactory.WORKSTATION_CODENAME,
				OperationSort.OPERATION_SUBSTRING,
				ObjectEntities.LAYOUT_ITEM_CODE,
				StorableObjectWrapper.COLUMN_NAME);
		
		final Set<StorableObjectCondition> workstationConditions = 
			new HashSet<StorableObjectCondition>(4);
		
		workstationConditions.add(currentUserCondition);
		workstationConditions.add(layoutCondition);
		workstationConditions.add(networkCondition);
		workstationConditions.add(typicalCondition);
		
		final CompoundCondition compoundCondition = 
			new CompoundCondition(workstationConditions,
					CompoundConditionSort.AND);
		
		assert Log.debugMessage(this.toString() + " > " + compoundCondition, Log.DEBUGLEVEL10);
		
		final Set<LayoutItem> workstationNetworkItems = 
			StorableObjectPool.getStorableObjectsByCondition(
				compoundCondition, 
				true);
		
		
		if (workstationNetworkItems.isEmpty()) {
			assert Log.debugMessage("create workstation in " 
					+ networkId,
				Log.DEBUGLEVEL10);
			return LayoutItem.createInstance(
				LoginManager.getUserId(), 
				networkId, 
				this.getCodename(), 
				WorkstationBeanFactory.WORKSTATION_CODENAME);
		}
		
		assert workstationNetworkItems.size() == 1;
		return workstationNetworkItems.iterator().next();
	}

	public void createNecessaryItems() throws ApplicationException {
		final Identifier domainId = this.domainBean.getIdentifier();

		assert Log.debugMessage(domainId , Log.DEBUGLEVEL10);
		
		final LinkedIdsCondition currentUserCondition = 
			new LinkedIdsCondition(LoginManager.getUserId(), ObjectEntities.LAYOUT_ITEM_CODE);
		
		final TypicalCondition layoutCondition = 
			new TypicalCondition(this.getCodename(),
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.LAYOUT_ITEM_CODE,
				LayoutItemWrapper.COLUMN_LAYOUT_NAME);
		
		this.domainNetworkItem = 
			this.getDomainNetworkItem(currentUserCondition,
				layoutCondition);

		// acquire network item
		final Identifier domainNetworkItemId = this.domainNetworkItem.getId();
		
		assert Log.debugMessage("domainNetworkItem:" 
				+ this.domainNetworkItem,
			Log.DEBUGLEVEL03);
		
		
		final Set<LayoutItem> domainLayoutItems = 
			StorableObjectPool.getStorableObjectsByCondition(
				new CompoundCondition(currentUserCondition,
					CompoundConditionSort.AND,
					layoutCondition), 
				true);
		
		// create necessary network items
		
		final Map<Identifier, LayoutItem> existsNetworkLayoutItems = new HashMap<Identifier, LayoutItem>();
		
		final Set<KIS> kiss = 
			StorableObjectPool.getStorableObjectsByCondition(
				new LinkedIdsCondition(domainId, ObjectEntities.KIS_CODE), 
				true);
//		for (final Iterator<KIS> iterator = kiss.iterator();iterator.hasNext();) {
//			final KIS kis = iterator.next(); 
//			if (!kis.getDomainId().equals(domainId)) {
//				iterator.remove();
//			}
//		}
		assert Log.debugMessage("kiss:" + kiss , Log.DEBUGLEVEL10);
		
		this.addItems(kiss, existsNetworkLayoutItems, domainLayoutItems);
		//	create domain networks accorning to exist RTUs
		for (final KIS kis : kiss) {
			this.getLayoutItem(kis.getId(), 
				domainNetworkItemId, 
				existsNetworkLayoutItems);
		}
		
		final Set<Server> servers = 
			StorableObjectPool.getStorableObjectsByCondition(
				new LinkedIdsCondition(domainId, ObjectEntities.SERVER_CODE), 
				true);
//		for (final Iterator<Server> iterator = servers.iterator();iterator.hasNext();) {
//			final Server server = iterator.next(); 
//			if (!server.getDomainId().equals(domainId)) {
//				iterator.remove();
//			}
//		}

		assert Log.debugMessage("servers:" + servers , Log.DEBUGLEVEL10);
		this.addItems(servers, existsNetworkLayoutItems, domainLayoutItems);
		// create domain networks accorning to exist servers
		for (final Server server : servers) {
			this.getLayoutItem(server.getId(), 
				domainNetworkItemId, 
				existsNetworkLayoutItems);
		}
		
		final Set<MCM> mcms = 
			StorableObjectPool.getStorableObjectsByCondition(
				new LinkedIdsCondition(domainId, ObjectEntities.MCM_CODE), 
				true);
//		for (final Iterator<MCM> iterator = mcms.iterator();iterator.hasNext();) {
//			final MCM mcm = iterator.next(); 
//			if (!mcm.getDomainId().equals(domainId)) {
//				iterator.remove();
//			}
//		}
		this.addItems(mcms, existsNetworkLayoutItems, domainLayoutItems);
		assert Log.debugMessage("mcms:" + mcms , Log.DEBUGLEVEL10);
		
//		 create domain networks accorning to exist mcms
		for (final MCM mcm : mcms) {
			this.getLayoutItem(mcm.getId(), 
				domainNetworkItemId, 
				existsNetworkLayoutItems);
		}
		
		final Set<PermissionAttributes> permissionAttributes =
			StorableObjectPool.getStorableObjectsByCondition(				
				new LinkedIdsCondition(domainId, ObjectEntities.PERMATTR_CODE),
				true);
//		for (final Iterator<PermissionAttributes> iterator = permissionAttributes.iterator();iterator.hasNext();) {
//			final PermissionAttributes attributes = iterator.next(); 
//			if (!attributes.getDomainId().equals(domainId)) {
//				iterator.remove();
//			}
//		}
		assert Log.debugMessage("permissionAttributes:" + permissionAttributes , 
			Log.DEBUGLEVEL10);
		
		if (!permissionAttributes.isEmpty()) {
			final LayoutItem networkWorkstationItem = 
				this.getNetworkWorkstationItem(domainNetworkItemId, 
					currentUserCondition, 
					layoutCondition);
			
			final Identifier networkWorkstationItemId = networkWorkstationItem.getId();
			
			assert Log.debugMessage("networkWorkstationItemId:" 
					+ networkWorkstationItemId
					+ ", " 
					+ networkWorkstationItem.getName()
					+ '@'
					+ networkWorkstationItem.getLayoutName(), 
				Log.DEBUGLEVEL10);			
			//	create workstation items accorning to exist permissionAttributes
			
			final Set<Identifier> userIds = 
				new HashSet<Identifier>(permissionAttributes.size());
			for (final PermissionAttributes attributes : permissionAttributes) {
				userIds.add(attributes.getParentId());
			}

			this.addItems(userIds, existsNetworkLayoutItems, domainLayoutItems);
			for (final Identifier identifier : userIds) {
				this.getLayoutItem(identifier, 
					networkWorkstationItemId, 
					existsNetworkLayoutItems);
			}
		}
	}
	
	public SystemUserPerpective getSystemUserPerspective(final UserBean userBean) {
		final Identifier id = userBean.getIdentifier();
		
		final SystemUserPerpective systemUserPerpective = 
			this.userPerspectiveMap.get(id);
		
		// if this perspective exists
		if (systemUserPerpective != null) {
			return systemUserPerpective;
		}	
		
		// otherwise create perspective
		final SystemUserPerpective perpective = new SystemUserPerpective(userBean, this);
		perpective.setManagerMainFrame(this.managerMainFrame);
		final PerspectiveData subperspectiveData = 
			this.perspectiveData.getSubperspectiveData(ObjectEntities.SYSTEMUSER);
		perpective.setPerspectiveData(subperspectiveData);
		this.userPerspectiveMap.put(id, perpective);
		assert Log.debugMessage("User perspective for " 
				+ id 
				+ " created", 
			Log.DEBUGLEVEL10);
		return perpective;
	}
	
	public void perspectiveApplied() throws ApplicationException {
		final GraphRoutines graphRoutines = this.managerMainFrame.getGraphRoutines();
		
		final String codename = this.getCodename();
		graphRoutines.showLayerName(codename, false);
	
		if (this.firstStart) {
		for (final AbstractBean bean : this.getLayoutBeans()) {
			if (bean instanceof UserBean) {
				SystemUserPerpective systemUserPerpective = 
					this.getSystemUserPerspective((UserBean) bean);
				this.addSubPerspective(systemUserPerpective);
				this.managerMainFrame.putPerspective(systemUserPerpective);
			}
		}
		this.firstStart = false;
		}
	}

	public Perspective getSuperPerspective() {
		return this.managerMainFrame.getManagerHandler().getPerspective("domains");
	}
	
	public Perspective getSubPerspective(final AbstractBean bean) {
		if (bean.getCodename().equals(ObjectEntities.SYSTEMUSER)) {
			final UserBean userBean = (UserBean) bean;
			final DomainsPerspective domainsPerspective  = 
				(DomainsPerspective) this.managerMainFrame.getManagerHandler().getPerspective("domains");
			final String identifierString = userBean.getIdentifier().getIdentifierString();
			final Perspective perspective = domainsPerspective.getPerspective(identifierString);
			return perspective;
		}
		return null;
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " for " + this.domainBean.getIdentifier().getIdentifierString();
	}
}

