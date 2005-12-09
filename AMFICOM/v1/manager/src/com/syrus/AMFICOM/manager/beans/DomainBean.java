/*-
 * $Id: DomainBean.java,v 1.5 2005/12/09 16:14:31 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.manager.beans;

import static com.syrus.AMFICOM.manager.beans.DomainBeanWrapper.KEY_DESCRIPTION;
import static com.syrus.AMFICOM.manager.beans.DomainBeanWrapper.KEY_NAME;

import java.beans.PropertyChangeEvent;
import java.util.Set;

import org.jgraph.graph.GraphModel;

import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.DomainMember;
import com.syrus.AMFICOM.administration.PermissionAttributes;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.manager.ManagerHandler;
import com.syrus.AMFICOM.manager.UI.GraphRoutines;
import com.syrus.AMFICOM.manager.graph.MPort;
import com.syrus.AMFICOM.manager.graph.ManagerGraphCell;
import com.syrus.AMFICOM.manager.perspective.Perspective;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.5 $, $Date: 2005/12/09 16:14:31 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public final class DomainBean extends Bean {
	
	private Domain domain;
	
	@Override
	protected void setIdentifier(Identifier storableObject) throws ApplicationException {
		super.setIdentifier(storableObject);
		this.domain = StorableObjectPool.getStorableObject(this.identifier, true);
	}
	
	public final String getDescription() {
		return this.domain.getDescription();
	}

	@Override
	public final String getName() {
		return this.domain.getName();
	}
	
	public final void setDescription(String description) {
		String description2 = this.domain.getDescription();
		if (description2 != description &&
				(description2 != null && !description2.equals(description) ||
				!description.equals(description2))) {
			this.domain.setDescription(description);
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, KEY_DESCRIPTION, description2, description));
		}	
	}
	
	@Override
	public final void setName(final String name) {
		String name2 = this.domain.getName().intern();
		if (name2 != name.intern() ) {
			assert Log.debugMessage("was:" + name2 + ", now:" + name, Log.DEBUGLEVEL09);
			this.domain.setName(name);
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, KEY_NAME, name2, name));
		}
	}
	
	@Override
	public void applyTargetPort(final MPort oldPort, final MPort newPort) 
	throws ApplicationException {
		assert Log.debugMessage("oldPort:" 
				+ oldPort
				+ ", newPort:"
				+ newPort, 
			Log.DEBUGLEVEL10);
		Identifier parentId = Identifier.VOID_IDENTIFIER;
		if (newPort != null) {
			parentId = ((DomainBean) newPort.getUserObject()).getIdentifier();
		}
		
		final Identifier parentDomainId = this.domain.getParentDomainId();
		
		assert Log.debugMessage(this.domain.getId() 
			+ ", set parent " 
			+ parentId,
		Log.DEBUGLEVEL10); 
		this.domain.setDomainId(parentId);		
		
		final ManagerHandler managerHandler = this.managerMainFrame.getManagerHandler();
		if (!parentId.isVoid()) {
			final Perspective newPerspective = managerHandler.getPerspective(parentId.getIdentifierString());
			final Perspective currentPerspective = this.managerMainFrame.getPerspective();			
			final GraphRoutines graphRoutines = this.managerMainFrame.getGraphRoutines();
			final String netName = NetBeanFactory.NET_CODENAME + this.domain.getId().getIdentifierString();
			
			if (parentDomainId.isVoid()) {
				newPerspective.createNecessaryItems();
				graphRoutines.arrangeLayoutItems(newPerspective);
				graphRoutines.showLayerName(currentPerspective.getCodename(), true);				
				final ManagerGraphCell defaultGraphCell = 
					graphRoutines.getDefaultGraphCell(netName,
						newPerspective.getCodename(),
						false);
				newPerspective.addLayoutBean(defaultGraphCell.getAbstractBean());
				graphRoutines.arrangeLayoutItems();
			} else {
				final Perspective oldPerspective = 
					managerHandler.getPerspective(parentDomainId.getIdentifierString());
				final ManagerGraphCell networkCell = graphRoutines.getDefaultGraphCell(netName, 
					oldPerspective.getCodename(), 
					false);
				
				final GraphModel model = this.managerMainFrame.getGraph().getModel();
				
				final MPort port = networkCell.getMPort();
				final Set edges = port.getEdges();
				
				for (final Object edge : edges) {
					port.removeEdge(edge);
				}
				model.remove(edges.toArray());
				
				final AbstractBean netBean = networkCell.getAbstractBean();
				netBean.disposeLayoutItem();
				
				assert Log.debugMessage(networkCell
						+ ", oldPerspective:"
						+ oldPerspective
						+ ", newPerspective:"
						+ newPerspective, Log.DEBUGLEVEL10);
				
				oldPerspective.removeLayoutBean(netBean);
				networkCell.setPerspective(newPerspective);
				newPerspective.addLayoutBean(netBean);
				
				newPerspective.createNecessaryItems();
				graphRoutines.arrangeLayoutItems(oldPerspective);
				graphRoutines.arrangeLayoutItems(newPerspective);
				graphRoutines.showLayerName(currentPerspective.getCodename(), true);				
				graphRoutines.arrangeLayoutItems();
				this.managerMainFrame.getTreeModel().reload();
			}
		}
		
		assert Log.debugMessage("was parentDomainId: " 
				+ parentDomainId
				+ ", now:"
				+ parentId, 
			Log.DEBUGLEVEL10);
	}	

	@Override
	public void dispose() throws ApplicationException {
		assert Log.debugMessage("DomainBean.dispose | " + this.identifier, Log.DEBUGLEVEL03);
		
//		final CompoundCondition compoundCondition = 
//			new CompoundCondition(new TypicalCondition(
//				this.getId(), 
//				OperationSort.OPERATION_EQUALS,
//				ObjectEntities.LAYOUT_ITEM_CODE,
//				LayoutItemWrapper.COLUMN_LAYOUT_NAME),
//				CompoundConditionSort.AND,
//				new LinkedIdsCondition(
//					LoginManager.getUserId(),
//					ObjectEntities.LAYOUT_ITEM_CODE) {
//					@Override
//					public boolean isNeedMore(Set< ? extends Identifiable> storableObjects) {
//						return storableObjects.isEmpty();
//					}
//				});
//		
//		final Set<LayoutItem> layoutItems = 
//			StorableObjectPool.getStorableObjectsByCondition(compoundCondition, true);	
//		
//		for(final LayoutItem layoutItem : layoutItems) {
//			StorableObjectPool.delete(layoutItem.getCharacteristics(false));
//		}
//		
//		StorableObjectPool.delete(layoutItems);
//		
//		this.disposeDomainMember(ObjectEntities.KIS_CODE);
//		this.disposeDomainMember(ObjectEntities.SERVER_CODE);
//		this.disposeDomainMember(ObjectEntities.MCM_CODE);		
//		this.disposeUsers();
		
		final GraphRoutines graphRoutines = this.managerMainFrame.getGraphRoutines();
		final Set<ManagerGraphCell> netDomainCells = 
			graphRoutines.getDefaultGraphCells(NetBeanFactory.NET_CODENAME + this.id);
		final GraphModel model = this.managerMainFrame.getGraph().getModel();
		for (final ManagerGraphCell cell : netDomainCells) {
			assert Log.debugMessage(cell, Log.DEBUGLEVEL03);			
		}
		model.remove(netDomainCells.toArray());
		
		StorableObjectPool.delete(this.identifier);		
		
		super.disposeLayoutItem();
	}
	
	@SuppressWarnings("unchecked")
	private void disposeDomainMember(final short domainMemberCode) 
	throws ApplicationException {
		final Set<? extends DomainMember> kiss = 
			StorableObjectPool.getStorableObjectsByCondition(
				new LinkedIdsCondition(this.identifier, domainMemberCode), 
				true);
		for (final DomainMember member : kiss) {
			if (member.getDomainId().equals(this.identifier)) {
				StorableObjectPool.delete(member.getId());
			}
		}
		
	}
	
	@SuppressWarnings("unchecked")
	private void disposeUsers() throws ApplicationException {
		final Set<PermissionAttributes> permissionAttributes = 
			StorableObjectPool.getStorableObjectsByCondition(
				new LinkedIdsCondition(this.identifier, ObjectEntities.PERMATTR_CODE), 
				true);
		for (final PermissionAttributes attributes : permissionAttributes) {
			if (attributes.getDomainId().equals(this.identifier)) {
				StorableObjectPool.delete(attributes.getParentId());
				StorableObjectPool.delete(attributes.getId());
			}
		}
		
	}
	
	@Override
	public final String getCodename() {
		return ObjectEntities.DOMAIN;
	}
	
	public final Domain getDomain() {
		return this.domain;
	}
}
