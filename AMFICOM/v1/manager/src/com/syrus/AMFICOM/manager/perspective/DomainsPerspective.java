/*-
* $Id: DomainsPerspective.java,v 1.10 2005/12/27 10:52:32 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.perspective;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractAction;

import org.jgraph.JGraph;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;
import org.jgraph.graph.Port;

import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.PermissionAttributes;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Checker;
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
import com.syrus.AMFICOM.manager.graph.MPort;
import com.syrus.AMFICOM.resource.LayoutItem;
import com.syrus.AMFICOM.resource.LayoutItemWrapper;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.10 $, $Date: 2005/12/27 10:52:32 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public final class DomainsPerspective extends AbstractPerspective {

	private final Map<Identifier, DomainPerpective> domainPerspectiveMap;
	private boolean	firstStart;
	
	public DomainsPerspective() {
		this.domainPerspectiveMap = new HashMap<Identifier, DomainPerpective>();
		this.firstStart = true;
	}
	
	@Override
	protected void createActions() throws ApplicationException {
		this.actions = new ArrayList<AbstractAction>(1);
		
		final GraphRoutines graphRoutines = this.managerMainFrame.getGraphRoutines();
		
		final PostBeanCreationAction<DomainBean> addDomainBeanPerspectiveAction = 
			new PostBeanCreationAction<DomainBean>() {

				public void postActionPerform(final DomainBean domainBean) throws ApplicationException {
					addDomainPerspective(domainBean);			
					final DomainsPerspective perspective = DomainsPerspective.this;
					graphRoutines.arrangeLayoutItems();
					graphRoutines.showLayerName(perspective.getCodename(), true);
//					graphRoutines.fixLayoutItemCharacteristics();
				}
			};
		final AbstractAction addDomainAction = 
			super.createAction(this.perspectiveData.getBeanFactory(ObjectEntities.DOMAIN),
				addDomainBeanPerspectiveAction, null);
		addDomainAction.setEnabled(Checker.isPermitted(PermissionAttributes.PermissionCodename.ADMINISTRATION_CREATE_DOMAIN));
		this.actions.add(addDomainAction);
	}
	
	public void addDomainPerspective(final DomainBean domainBean) throws ApplicationException {
		final DomainPerpective domainPerpective = 
			this.getDomainPerspective(domainBean);
		this.addSubPerspective(domainPerpective);		
		this.managerMainFrame.putPerspective(domainPerpective);
	}
	
	public String getCodename() {
		return "domains";
	}
	
	public String getName() {
		return I18N.getString("Manager.Label.DomainsLevel");
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
						!port.getBean().getId().startsWith(ObjectEntities.DOMAIN)) {
					return false;
				}
				
			}
			
		}
		return true;
	}

	public void perspectiveApplied() throws ApplicationException {
		final GraphRoutines graphRoutines = 
			this.managerMainFrame.getGraphRoutines();
		final String codename = this.getCodename();
		graphRoutines.showLayerName(codename, false);
		
		if (this.firstStart) {
			for (final AbstractBean bean : this.getLayoutBeans()) {
				if (bean instanceof DomainBean) {
					this.addDomainPerspective((DomainBean) bean);
				}
			}
			
			this.firstStart = false;
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, "layoutBeans", null, null));
		}
	}

	public DomainPerpective getDomainPerspective(final DomainBean domainBean) {
		final Identifier id = domainBean.getIdentifier();
		
		final DomainPerpective domainPerpective = 
			this.domainPerspectiveMap.get(id);
		
		// if this perspective exists
		if (domainPerpective != null) {
			return domainPerpective;
		}	
		
		// otherwise create perspective
		final DomainPerpective perpective = new DomainPerpective(domainBean);
		perpective.setManagerMainFrame(this.managerMainFrame);
		final PerspectiveData subperspectiveData = 
			this.perspectiveData.getSubperspectiveData(ObjectEntities.DOMAIN);
		perpective.setPerspectiveData(subperspectiveData);
		this.domainPerspectiveMap.put(id, perpective);
		assert Log.debugMessage("Domain perspective for " 
				+ id 
				+ " created", 
			Log.DEBUGLEVEL10);
		return perpective;
	}
	
	private LayoutItem getDomainLayoutItem(final Identifier domainId,
		final Map<Identifier, LayoutItem> existsLayoutItems) 
	throws ApplicationException {
		final Domain domain = StorableObjectPool.getStorableObject(domainId, true);
		final Identifier parentDomainId = domain.getDomainId();
		final Identifier parentLayoutItemId;
		if (parentDomainId.isVoid()) {
			parentLayoutItemId = Identifier.VOID_IDENTIFIER;
		} else {
			parentLayoutItemId = this.getDomainLayoutItem(parentDomainId, existsLayoutItems).getId();
		}
		
		if (existsLayoutItems.containsKey(domainId)) {
			return existsLayoutItems.get(domainId);
		}
		
		final LayoutItem layoutItem = 
			this.getLayoutItem(domainId.getIdentifierString(), 
				this.getCodename(), 
				parentLayoutItemId);
		
		assert Log.debugMessage("create domain layout for " + domainId, 
			Log.DEBUGLEVEL10);
		
		existsLayoutItems.put(domainId, layoutItem);
		
		return layoutItem;
	}
	
//	private LayoutItem getNetworkLayoutItem(final Identifier domainId,
//			final Map<Identifier, LayoutItem> existsDomainLayoutItems,
//			final Map<Identifier, LayoutItem> existsNetworkLayoutItems) 
//		throws ApplicationException {
//		
//		if (existsNetworkLayoutItems.containsKey(domainId)) {
//			return existsNetworkLayoutItems.get(domainId);
//		}
//		
//		final Identifier parentLayoutItemId = 
//			existsDomainLayoutItems.get(domainId).getId();
//			
//			final LayoutItem layoutItem = 
//				LayoutItem.createInstance(LoginManager.getUserId(), 
//					parentLayoutItemId, 
//					this.getCodename(), 
//					NetBeanFactory.NET_CODENAME);
//			
//			assert Log.debugMessage("create network item for " + parentLayoutItemId , 
//				Log.DEBUGLEVEL10);
//			
//			existsNetworkLayoutItems.put(domainId, layoutItem);
//			
//			return layoutItem;
//		}
	
	
	public void createNecessaryItems() throws ApplicationException {
		this.sendCreatingItemsMessage();
		final Set<Domain> domains = StorableObjectPool.getStorableObjectsByCondition(
			new EquivalentCondition(ObjectEntities.DOMAIN_CODE), 
			true);				
		
		final LinkedIdsCondition currentUserCondition = 
			new LinkedIdsCondition(LoginManager.getUserId(), ObjectEntities.LAYOUT_ITEM_CODE);
		
		final TypicalCondition layoutCondition = 
			new TypicalCondition(this.getCodename(),
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.LAYOUT_ITEM_CODE,
				LayoutItemWrapper.COLUMN_LAYOUT_NAME);
		
		final Set<Identifier> domainIds = Identifier.createIdentifiers(domains);
		final Set<Identifier> copyDomainIds = new HashSet<Identifier>(domainIds);
		
		final Set<StorableObjectCondition> domainsTypicalConditions = 
			new HashSet<StorableObjectCondition>(domains.size());
		for (final Identifier domainId : domainIds) {
			domainsTypicalConditions.add(
				new TypicalCondition(domainId.getIdentifierString(),
					OperationSort.OPERATION_EQUALS,
					ObjectEntities.LAYOUT_ITEM_CODE,
					StorableObjectWrapper.COLUMN_NAME));
		}
		
		final Set<StorableObjectCondition> setOfConditions = new HashSet<StorableObjectCondition>(3);
		setOfConditions.add(currentUserCondition);
		setOfConditions.add(layoutCondition);
		if (domainsTypicalConditions.size() != 1) {
			setOfConditions.add(new CompoundCondition(domainsTypicalConditions, 
				CompoundConditionSort.OR));
		} else {
			setOfConditions.add(domainsTypicalConditions.iterator().next());
		}
		
		final Set<LayoutItem> domainLayoutItems = 
			StorableObjectPool.getStorableObjectsByCondition(
				new CompoundCondition(setOfConditions,
					CompoundConditionSort.AND), 
				true);
		
		final Map<Identifier, LayoutItem> existsDomainLayoutItems = 
			new HashMap<Identifier, LayoutItem>();			
		
		// remove all found objects
		for (final LayoutItem item : domainLayoutItems) {
			final String name = item.getName();
			for(final Iterator<Identifier> iterator = domainIds.iterator(); iterator.hasNext();) {
				final Identifier identifier = iterator.next();
				if (identifier.getIdentifierString().equals(name)) {
					existsDomainLayoutItems.put(identifier, item);
					iterator.remove();
				}
			}
		}
		
		// create domain layout items
		for (final Identifier domainId : domainIds) {
			this.getDomainLayoutItem(domainId, existsDomainLayoutItems);
		}

		final Map<Identifier, LayoutItem> existsNetworkLayoutItems = 
			new HashMap<Identifier, LayoutItem>();

		// acquire exist network items
		if (!domainLayoutItems.isEmpty()) {
			final Map<LayoutItem, Identifier> reverseExistsDomainLayoutItems = 
				new HashMap<LayoutItem, Identifier>();

			for (final Identifier domainId : existsDomainLayoutItems.keySet()) {
				reverseExistsDomainLayoutItems.put(
					existsDomainLayoutItems.get(domainId), 
					domainId);
			}
			
			final Set<StorableObjectCondition> networkConditions = 
				new HashSet<StorableObjectCondition>(3);
			networkConditions.add(currentUserCondition);
			networkConditions.add(layoutCondition);
			networkConditions.add(new LinkedIdsCondition(
					Identifier.createIdentifiers(domainLayoutItems), 
					ObjectEntities.LAYOUT_ITEM_CODE));
			
			final Set<LayoutItem> networkItems = 
				StorableObjectPool.getStorableObjectsByCondition(
					new CompoundCondition(networkConditions,
						CompoundConditionSort.AND), 
					true);
			
			
			for (final LayoutItem item : networkItems) {				
				final Identifier parentId = item.getParentId();
				final Identifier domainId = reverseExistsDomainLayoutItems.get(parentId);
				if (domainId != null) {
					existsNetworkLayoutItems.put(domainId, item);
				}
			}
		}
		
//		// create necessary network items
//		final Set<KIS> kiss = 
//			StorableObjectPool.getStorableObjectsByCondition(
//				new LinkedIdsCondition(copyDomainIds, ObjectEntities.KIS_CODE), 
//				true);
//		
//		//	create domain networks accorning to exist RTUs
//		for (final KIS kis : kiss) {
//			this.getNetworkLayoutItem(kis.getDomainId(), 
//				existsDomainLayoutItems, 
//				existsNetworkLayoutItems);
//		}
//		
//		final Set<Server> servers = 
//			StorableObjectPool.getStorableObjectsByCondition(
//				new LinkedIdsCondition(copyDomainIds, ObjectEntities.SERVER_CODE), 
//				true);
//		
//		// create domain networks accorning to exist servers
//		for (final Server server : servers) {
//			this.getNetworkLayoutItem(server.getDomainId(), 
//				existsDomainLayoutItems, 
//				existsNetworkLayoutItems);
//		}
//		
//		final Set<MCM> mcms = 
//			StorableObjectPool.getStorableObjectsByCondition(
//				new LinkedIdsCondition(copyDomainIds, ObjectEntities.MCM_CODE), 
//				true);
//		
////		 create domain networks accorning to exist mcms
//		for (final MCM mcm : mcms) {
//			this.getNetworkLayoutItem(mcm.getDomainId(), 
//				existsDomainLayoutItems, 
//				existsNetworkLayoutItems);
//		}
//		
//		final Set<PermissionAttributes> permissionAttributes =
//			StorableObjectPool.getStorableObjectsByCondition(				
//				new LinkedIdsCondition(copyDomainIds, ObjectEntities.PERMATTR_CODE),
//				true);
//
////		 create domain networks accorning to exist user permissions
//		for (final PermissionAttributes attributes : permissionAttributes) {
//			this.getNetworkLayoutItem(attributes.getDomainId(), 
//				existsDomainLayoutItems, 
//				existsNetworkLayoutItems);
//		}
	}

	public Perspective getSuperPerspective() {
		return null;
	}
	
	public Perspective getSubPerspective(final AbstractBean bean) {
//		assert Log.debugMessage(bean, Log.DEBUGLEVEL03);
//		if (bean.getCodename().equals(NetBeanFactory.NET_CODENAME)) {
//			Identifier parentDomainId = null;
//			final GraphRoutines graphRoutines = this.managerMainFrame.getGraphRoutines();
//			final DefaultGraphCell defaultGraphCell = 
//				graphRoutines.getDefaultGraphCell(bean, this);
////				assert Log.debugMessage("defaultGraphCell:" + defaultGraphCell, Log.DEBUGLEVEL03);
//			
//			final MPort port = (MPort) defaultGraphCell.getChildAt(0);
//			final List<Port> targets = port.getTargets();
////				assert Log.debugMessage("targets:" + targets + ",\n\t" + targets.size(), Log.DEBUGLEVEL03);
//			if (targets.size() == 1) {
//				final MPort port2 = (MPort) targets.get(0);
//				final DomainBean domainBean = (DomainBean) port2.getBean();
//				parentDomainId = domainBean.getIdentifier();
////					assert Log.debugMessage("this.parentDomainId:" + this.parentDomainId, Log.DEBUGLEVEL03);
//			}
//			
//			
//			
//			if (parentDomainId != null && !parentDomainId.isVoid()) {
//				final DomainsPerspective domainsPerspective  = 
//					(DomainsPerspective) this.managerMainFrame.getManagerHandler().getPerspective("domains");
//				final String identifierString = parentDomainId.getIdentifierString();
//				final Perspective perspective = domainsPerspective.getPerspective(identifierString);
////				assert Log.debugMessage(identifierString + ", " + (perspective != null ? perspective.getLayoutBeans() : null) , Log.DEBUGLEVEL03);
//				return perspective;
//			}
//		}
		if (bean.getCodename().equals(ObjectEntities.DOMAIN)) {
			final DomainBean domainBean = (DomainBean)bean;		
			final Identifier parentDomainId = domainBean.getIdentifier();
			final DomainsPerspective domainsPerspective  = 
				(DomainsPerspective) this.managerMainFrame.getManagerHandler().getPerspective("domains");
			final String identifierString = parentDomainId.getIdentifierString();
			final Perspective perspective = domainsPerspective.getPerspective(identifierString);
			return perspective;
		}
		return null;
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

	public final LayoutItem getParentLayoutItem() {
		return null;
	}
}

