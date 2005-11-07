/*-
* $Id: DomainsPerspectiveCommand.java,v 1.3 2005/11/07 15:24:19 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.UI;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.JToolBar;

import org.jgraph.JGraph;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;
import org.jgraph.graph.Port;

import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.MCM;
import com.syrus.AMFICOM.administration.PermissionAttributes;
import com.syrus.AMFICOM.administration.Server;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.resource.I18N;
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
import com.syrus.AMFICOM.manager.AbstractPerspective;
import com.syrus.AMFICOM.manager.MPort;
import com.syrus.AMFICOM.manager.ManagerHandler;
import com.syrus.AMFICOM.manager.NetBeanFactory;
import com.syrus.AMFICOM.manager.Perspective;
import com.syrus.AMFICOM.measurement.KIS;
import com.syrus.AMFICOM.resource.LayoutItem;
import com.syrus.AMFICOM.resource.LayoutItemWrapper;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.3 $, $Date: 2005/11/07 15:24:19 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class DomainsPerspectiveCommand extends AbstractCommand {

	private class DomainsPerspective extends AbstractPerspective {
		
		
		public DomainsPerspective() {
			super(DomainsPerspectiveCommand.this.graphText);
		}
		
		public String getCodename() {
			return "domains";
		}
		
		public String getName() {
			return I18N.getString("Manager.Label.DomainsLevel");
		}
		
		public void addEntities(final JToolBar entityToolBar) {
			final ManagerHandler managerHandler = this.managerMainFrame.getManagerHandler();
			this.managerMainFrame.addAction(super.createAction(managerHandler.getBeanFactory(NetBeanFactory.NET_CODENAME)));
			entityToolBar.addSeparator();
			this.managerMainFrame.addAction(super.createAction(managerHandler.getBeanFactory(ObjectEntities.DOMAIN)));
		}
		
		public boolean isValid() {
			final JGraph graph = DomainsPerspectiveCommand.this.graphText.getGraph();
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
			DomainsPerspectiveCommand.this.graphText.getTreeModel().setRoot(null);
			final GraphRoutines graphRoutines = 
				DomainsPerspectiveCommand.this.graphText.getGraphRoutines();
			graphRoutines.showLayerName(this.getCodename());
			
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
				LayoutItem.createInstance(LoginManager.getUserId(), 
					parentLayoutItemId, 
					this.getCodename(), 
					domainId.getIdentifierString());
			
			assert Log.debugMessage("create domain layout for " + domainId, 
				Log.DEBUGLEVEL10);
			
			existsLayoutItems.put(domainId, layoutItem);
			
			return layoutItem;
		}
		
		private LayoutItem getNetworkLayoutItem(final Identifier domainId,
 			final Map<Identifier, LayoutItem> existsDomainLayoutItems,
 			final Map<Identifier, LayoutItem> existsNetworkLayoutItems) 
 		throws ApplicationException {
			
			if (existsNetworkLayoutItems.containsKey(domainId)) {
				return existsNetworkLayoutItems.get(domainId);
			}
			
			final Identifier parentLayoutItemId = 
				existsDomainLayoutItems.get(domainId).getId();
 			
 			final LayoutItem layoutItem = 
 				LayoutItem.createInstance(LoginManager.getUserId(), 
 					parentLayoutItemId, 
 					this.getCodename(), 
 					NetBeanFactory.NET_CODENAME);
 			
 			assert Log.debugMessage("create network item for " + parentLayoutItemId , 
 				Log.DEBUGLEVEL10);
 			
 			existsNetworkLayoutItems.put(domainId, layoutItem);
 			
 			return layoutItem;
 		}
		
		@Override
		protected String getIdentifierString(final Identifier memberId) {
			return memberId.getIdentifierString();
		}
		
		public void createNecessaryItems() throws ApplicationException {
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
			setOfConditions.add(new CompoundCondition(domainsTypicalConditions, 
				CompoundConditionSort.OR));
			
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
			
			// create necessary network items
			final Set<KIS> kiss = 
				StorableObjectPool.getStorableObjectsByCondition(
					new LinkedIdsCondition(copyDomainIds, ObjectEntities.KIS_CODE), 
					true);
			
			//	create domain networks accorning to exist RTUs
			for (final KIS kis : kiss) {
				this.getNetworkLayoutItem(kis.getDomainId(), 
					existsDomainLayoutItems, 
					existsNetworkLayoutItems);
			}
			
			final Set<Server> servers = 
				StorableObjectPool.getStorableObjectsByCondition(
					new LinkedIdsCondition(copyDomainIds, ObjectEntities.SERVER_CODE), 
					true);
			
			// create domain networks accorning to exist servers
			for (final Server server : servers) {
				this.getNetworkLayoutItem(server.getDomainId(), 
					existsDomainLayoutItems, 
					existsNetworkLayoutItems);
			}
			
			final Set<MCM> mcms = 
				StorableObjectPool.getStorableObjectsByCondition(
					new LinkedIdsCondition(copyDomainIds, ObjectEntities.MCM_CODE), 
					true);
			
//			 create domain networks accorning to exist mcms
			for (final MCM mcm : mcms) {
				this.getNetworkLayoutItem(mcm.getDomainId(), 
					existsDomainLayoutItems, 
					existsNetworkLayoutItems);
			}
			
			final Set<PermissionAttributes> permissionAttributes =
				StorableObjectPool.getStorableObjectsByCondition(				
					new LinkedIdsCondition(copyDomainIds, ObjectEntities.PERMATTR_CODE),
					true);

//			 create domain networks accorning to exist user permissions
			for (final PermissionAttributes attributes : permissionAttributes) {
				this.getNetworkLayoutItem(attributes.getDomainId(), 
					existsDomainLayoutItems, 
					existsNetworkLayoutItems);
			}
		}
	}
	
	
	
	final ManagerMainFrame graphText;
	private Perspective	perspective;
	
	public DomainsPerspectiveCommand(final ManagerMainFrame graphText) {
		this.graphText = graphText;
	}
	
	@Override
	public void execute() {

		if (this.perspective == null) {
			this.perspective = new DomainsPerspective();
		}
		
		this.graphText.setPerspective(this.perspective);	

	}
	
	
}

