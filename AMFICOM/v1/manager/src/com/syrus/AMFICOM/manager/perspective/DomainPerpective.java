/*-
* $Id: DomainPerpective.java,v 1.9 2005/12/09 16:13:46 bob Exp $
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
import com.syrus.AMFICOM.administration.MCM;
import com.syrus.AMFICOM.administration.PermissionAttributes;
import com.syrus.AMFICOM.administration.Server;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.EquivalentCondition;
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
import com.syrus.AMFICOM.manager.beans.DomainBean;
import com.syrus.AMFICOM.manager.beans.DomainNetworkItem;
import com.syrus.AMFICOM.manager.beans.NetBeanFactory;
import com.syrus.AMFICOM.manager.beans.UserBean;
import com.syrus.AMFICOM.manager.beans.WorkstationBeanFactory;
import com.syrus.AMFICOM.manager.graph.MPort;
import com.syrus.AMFICOM.manager.graph.ManagerGraphCell;
import com.syrus.AMFICOM.measurement.KIS;
import com.syrus.AMFICOM.resource.LayoutItem;
import com.syrus.AMFICOM.resource.LayoutItemWrapper;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.9 $, $Date: 2005/12/09 16:13:46 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public final class DomainPerpective extends AbstractPerspective {

	private final DomainBean domainBean;
	private LayoutItem	domainNetworkItem;
	private final Map<Identifier, SystemUserPerpective> userPerspectiveMap;
	private boolean	firstStart;
	private DomainValidator domainValidator;
	
	public DomainPerpective(final DomainBean domainBean) {
		assert Log.debugMessage(domainBean.getIdentifier(), Log.DEBUGLEVEL10);
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
		
		final PostBeanCreationAction<UserBean> addUserBeanPerspectiveAction = 
			new PostBeanCreationAction<UserBean>() {

				public void postActionPerform(final UserBean userBean) throws ApplicationException {
					addUserPerspective(userBean);			
					final DomainPerpective perspective = DomainPerpective.this;
					graphRoutines.arrangeLayoutItems();
					graphRoutines.showLayerName(perspective.getCodename(), true);
					graphRoutines.fixLayoutItemCharacteristics();
				}
			};
		
		this.actions.add(super.createAction(this.perspectiveData.getBeanFactory(ObjectEntities.SYSTEMUSER),
			addUserBeanPerspectiveAction, null));

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

				final String portBeanId = port.getBean().getId();
				if (visibleTarget == 0 && 
						!portBeanId.startsWith(NetBeanFactory.NET_CODENAME)) {
					return false;
				}
				
			}
			
		}
		return true;
	}

	
	public final LayoutItem getParentLayoutItem() {
		return this.domainNetworkItem;
	}
	
	private LayoutItem getDomainNetworkItem(final Map<Identifier, LayoutItem> domainNetLayoutItemMap,
			final LinkedIdsCondition currentUserCondition,
			final TypicalCondition layoutCondition,
			final Identifier domainId) 
	throws ApplicationException {
		final Domain domain = StorableObjectPool.getStorableObject(domainId, true);
		final Identifier parentNetLayoutId;
		final Identifier parentDomainId = domain.getDomainId();		
		if (domainId.equals(this.domainBean.getIdentifier()) || parentDomainId.isVoid()) {
			parentNetLayoutId =  Identifier.VOID_IDENTIFIER;
		} else {
			final LayoutItem item = domainNetLayoutItemMap.get(parentDomainId);
			if (item == null) {				
				final LayoutItem domainNetworkItem2 = 
					this.getDomainNetworkItem(domainNetLayoutItemMap, 
						currentUserCondition, 
						layoutCondition, 
						parentDomainId);
				parentNetLayoutId = domainNetworkItem2.getId();
			} else {
				parentNetLayoutId = item.getId();
			}
		}
		
		final String netId = NetBeanFactory.NET_CODENAME + domainId.getIdentifierString();
		
		final Set<StorableObjectCondition> domainsTypicalConditions = 
			new HashSet<StorableObjectCondition>(3);
		
		domainsTypicalConditions.add(currentUserCondition);
		domainsTypicalConditions.add(layoutCondition);		
		domainsTypicalConditions.add(new TypicalCondition(netId,
			OperationSort.OPERATION_EQUALS,
			ObjectEntities.LAYOUT_ITEM_CODE,
			StorableObjectWrapper.COLUMN_NAME));
		
		final Set<LayoutItem> domainNetworkItems = 
			StorableObjectPool.getStorableObjectsByCondition(
				new CompoundCondition(domainsTypicalConditions,
					CompoundConditionSort.AND), 
				true);
		
		if (domainNetworkItems.isEmpty()) {
			final LayoutItem layoutItem = 
				this.getLayoutItem(netId, this.getCodename(), parentNetLayoutId);
			domainNetLayoutItemMap.put(domainId, layoutItem);
			return layoutItem;
		}
		
		assert Log.debugMessage(domainNetworkItems, Log.DEBUGLEVEL10);
		assert domainNetworkItems.size() == 1;
		final LayoutItem layoutItem = domainNetworkItems.iterator().next();
		domainNetLayoutItemMap.put(domainId, layoutItem);
		return layoutItem;
	
	}
	
	private LayoutItem getNetworkWorkstationItem(final LayoutItem networkItem,
			final LinkedIdsCondition currentUserCondition,
			final TypicalCondition layoutCondition) 
	throws ApplicationException {

		final Identifier networkId = networkItem.getId();
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
			final String name = 
				WorkstationBeanFactory.WORKSTATION_CODENAME 
				+ networkItem.getName().replaceFirst("[A-Za-z]+", "");
			assert Log.debugMessage(name, Log.DEBUGLEVEL10);
			return LayoutItem.createInstance(
				LoginManager.getUserId(), 
				networkId, 
				this.getCodename(), 
				name);
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
		
		final Map<Identifier, LayoutItem> domainNetLayoutItemMap = new HashMap<Identifier, LayoutItem>();
		
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

		assert Log.debugMessage("kiss:" + kiss , Log.DEBUGLEVEL10);
		
		this.addItems(kiss, existsNetworkLayoutItems, domainLayoutItems);
		//	create domain networks accorning to exist RTUs
		for (final KIS kis : kiss) {
			final Identifier domainId2 = kis.getDomainId();
			final LayoutItem domainNetworkItem2 = 
				this.getDomainNetworkItem(domainNetLayoutItemMap, 
					currentUserCondition, 
					layoutCondition, 
					domainId2);
			if (domainId.equals(domainId2)) {
				this.getLayoutItem(kis.getId(), 
					domainNetworkItem2.getId(), 
					existsNetworkLayoutItems);
			}
		}
		
		final Set<Server> servers = 
			StorableObjectPool.getStorableObjectsByCondition(
				new LinkedIdsCondition(domainId, ObjectEntities.SERVER_CODE), 
				true);

		assert Log.debugMessage("servers:" + servers , Log.DEBUGLEVEL10);
		this.addItems(servers, existsNetworkLayoutItems, domainLayoutItems);
		// create domain networks accorning to exist servers
		for (final Server server : servers) {
			final Identifier domainId2 = server.getDomainId();
			final LayoutItem domainNetworkItem2 = 
				this.getDomainNetworkItem(domainNetLayoutItemMap, 
					currentUserCondition, 
					layoutCondition, 
					domainId2);
			if (domainId.equals(domainId2)) {
				this.getLayoutItem(server.getId(), 
					domainNetworkItem2.getId(),
					existsNetworkLayoutItems);
			}
		}
		
		final Set<MCM> mcms = 
			StorableObjectPool.getStorableObjectsByCondition(
				new LinkedIdsCondition(domainId, ObjectEntities.MCM_CODE), 
				true);

		this.addItems(mcms, existsNetworkLayoutItems, domainLayoutItems);
		assert Log.debugMessage("mcms:" + mcms , Log.DEBUGLEVEL10);
		
//		 create domain networks accorning to exist mcms
		for (final MCM mcm : mcms) {
			final Identifier domainId2 = mcm.getDomainId();
			final LayoutItem domainNetworkItem2 = 
				this.getDomainNetworkItem(domainNetLayoutItemMap, 
					currentUserCondition, 
					layoutCondition, 
					domainId2);
			if (domainId.equals(domainId2)) {
				this.getLayoutItem(mcm.getId(), 
					domainNetworkItem2.getId(), 
					existsNetworkLayoutItems);
			}
		}
		
		final Set<PermissionAttributes> permissionAttributes =
			StorableObjectPool.getStorableObjectsByCondition(				
				new LinkedIdsCondition(domainId, ObjectEntities.PERMATTR_CODE),
				true);

		assert Log.debugMessage("permissionAttributes:" + permissionAttributes , 
			Log.DEBUGLEVEL10);
		
		if (!permissionAttributes.isEmpty()) {
			
			Map<Identifier, Identifier> domainIdNetworkWorkstationItemIdMap = 
				new HashMap<Identifier, Identifier>();
			
			for (final PermissionAttributes attributes : permissionAttributes) {
				final Identifier domainId2 = attributes.getDomainId();
				final LayoutItem domainNetworkItem2 = 
					this.getDomainNetworkItem(domainNetLayoutItemMap, 
						currentUserCondition, 
						layoutCondition, 
						domainId2);
				if (domainId.equals(domainId2)) {
					final LayoutItem networkWorkstationItem = 
						this.getNetworkWorkstationItem(domainNetworkItem2, 
							currentUserCondition, 
							layoutCondition);
					final Identifier networkWorkstationItemId = networkWorkstationItem.getId();
					domainIdNetworkWorkstationItemIdMap.put(domainId2, 
						networkWorkstationItemId);
					assert Log.debugMessage(domainId2 + " > " + networkWorkstationItem, 
						Log.DEBUGLEVEL10);
					assert Log.debugMessage("networkWorkstationItemId:" 
						+ networkWorkstationItemId
						+ ", " 
						+ networkWorkstationItem.getName()
						+ '@'
						+ networkWorkstationItem.getLayoutName(), 
					Log.DEBUGLEVEL10);			
				}
			}
			//	create workstation items accorning to exist permissionAttributes
			
			final Set<Identifier> userIds = 
				new HashSet<Identifier>(permissionAttributes.size());
			for (final PermissionAttributes attributes : permissionAttributes) {
				userIds.add(attributes.getParentId());
			}
			this.addItems(userIds, existsNetworkLayoutItems, domainLayoutItems);
			for (final PermissionAttributes attributes : permissionAttributes) {
				final Identifier domainId2 = attributes.getDomainId();
				if (domainId.equals(domainId2)) {
					final Identifier parentLayoutItemId = 
						domainIdNetworkWorkstationItemIdMap.get(domainId2);
					this.getLayoutItem(attributes.getParentId(), 
						parentLayoutItemId, 
						existsNetworkLayoutItems);
				}
			}
		}
		
		
		// XXX bug due to cuncurrentmodification at lru map during search
		final Set<Domain> domains = 
			StorableObjectPool.getStorableObjectsByCondition(
//				new LinkedIdsCondition(domainId, ObjectEntities.DOMAIN_CODE),
				new EquivalentCondition(ObjectEntities.DOMAIN_CODE),
				true);
		
//		 create domain networks accorning to exist mcms
		for (final Domain domain : domains) {
			final Identifier domainId2 = domain.getDomainId();
			if (domainId2.equals(domainId)) {				
				final LayoutItem domainNetworkItem2 = 
					this.getDomainNetworkItem(domainNetLayoutItemMap, 
						currentUserCondition, 
						layoutCondition, 
						domain.getId());
				assert Log.debugMessage("Get network item " 
						+ domainNetworkItem2 
						+ " for " 
						+ domain, 
						Log.DEBUGLEVEL10);
			}
		}
		
		this.domainNetworkItem = domainNetLayoutItemMap.get(domainId);		
		// create network if domain is empty 
		if (this.domainNetworkItem == null) {
			this.domainNetworkItem = this.getDomainNetworkItem(domainNetLayoutItemMap, 
				currentUserCondition, 
				layoutCondition, 
				domainId);
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
					this.addUserPerspective((UserBean)bean);
				}
			}
			this.firstStart = false;
		}
	}
	
	public void addUserPerspective(final UserBean userBean) throws ApplicationException {
		final SystemUserPerpective systemUserPerpective = 
			this.getSystemUserPerspective(userBean);
		this.addSubPerspective(systemUserPerpective);		
		this.managerMainFrame.putPerspective(systemUserPerpective);
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
		return this.getClass().getSimpleName() 
			+ " for " 
			+ this.domainBean.getIdentifier().getIdentifierString();
	}
	
	@Override
	public final void putBean(final AbstractBean abstractBean) 
	throws ApplicationException {
		if (abstractBean instanceof DomainNetworkItem) {
			final GraphRoutines graphRoutines = 
				this.managerMainFrame.getGraphRoutines();
			final ManagerGraphCell defaultGraphCell = 
				graphRoutines.getDefaultGraphCell(abstractBean, this);
			assert Log.debugMessage(defaultGraphCell, Log.DEBUGLEVEL10);
			final DomainNetworkItem networkItem = 
				(DomainNetworkItem) abstractBean;
			networkItem.setDomainId(Identifier.VOID_IDENTIFIER, 
				this.domainBean.getIdentifier());
			
			if (abstractBean instanceof UserBean) {
				final LinkedIdsCondition currentUserCondition = 
					new LinkedIdsCondition(LoginManager.getUserId(), ObjectEntities.LAYOUT_ITEM_CODE);
				
				final TypicalCondition layoutCondition = 
					new TypicalCondition(this.getCodename(),
						OperationSort.OPERATION_EQUALS,
						ObjectEntities.LAYOUT_ITEM_CODE,
						LayoutItemWrapper.COLUMN_LAYOUT_NAME);
				
				final LayoutItem networkWorkstationItem = 
					this.getNetworkWorkstationItem(this.domainNetworkItem, 
						currentUserCondition, 
						layoutCondition);
				
				final LayoutItem layoutItem = this.getLayoutItem(abstractBean.getId(), 
					this.getCodename(), 
					networkWorkstationItem.getId());
			} else {
				final LayoutItem layoutItem = this.getLayoutItem(abstractBean.getId(), 
					this.getCodename(), 
					this.domainNetworkItem.getId());
			}
			
			graphRoutines.arrangeLayoutItems();
		}
		
	}
	
	@Override
	public Validator getValidator() {
		if (this.domainValidator == null) {
			this.domainValidator = new DomainValidator(super.getValidator());
		}
		return this.domainValidator;
	}
	
	private final class DomainValidator implements Validator {

		private final Validator validator;
		
		
		public DomainValidator(final Validator validator) {
			this.validator = validator;
		}
		
		private String getCodename(final String id) {
			final int index = id.indexOf(Identifier.SEPARATOR);
			return index >= 0 ? id.substring(0, index) : id;
		}
		
		
		public boolean isValid(final String source,
				final String target) {
			final String targetCodename = this.getCodename(target);
			if (!targetCodename.equals(NetBeanFactory.NET_CODENAME + ObjectEntities.DOMAIN)) {
				return this.validator.isValid(source, target);
			} 
			final String string = NetBeanFactory.NET_CODENAME + domainBean.getId();			
			return target.equals(string) &&
				this.validator.isValid(source, target);
		}
	}
}

