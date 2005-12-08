/*-
* $Id: NetBean.java,v 1.3 2005/12/08 16:05:58 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.beans;

import static com.syrus.AMFICOM.manager.beans.NetBeanFactory.NET_CODENAME;

import java.util.Set;

import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.manager.UI.GraphRoutines;
import com.syrus.AMFICOM.manager.graph.MPort;
import com.syrus.AMFICOM.resource.LayoutItem;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.3 $, $Date: 2005/12/08 16:05:58 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class NetBean extends NonStorableBean {
	
	private final Domain	domain;

	public NetBean(final Domain domain) {
		this.domain = domain;				
	} 
	
	private Set<LayoutItem> getBeanChildrenLayoutItems() 
	throws ApplicationException{
		final TypicalCondition typicalCondition = 
			new TypicalCondition(this.getId(), 
				OperationSort.OPERATION_EQUALS, 
				ObjectEntities.LAYOUT_ITEM_CODE, 
				StorableObjectWrapper.COLUMN_NAME);

		final Set<LayoutItem> beanLayoutItems = 
			StorableObjectPool.getStorableObjectsByCondition(
				typicalCondition, 
				true, 
				true);
		
		final LinkedIdsCondition linkedIdsCondition = 
			new LinkedIdsCondition(Identifier.createIdentifiers(beanLayoutItems),
				ObjectEntities.LAYOUT_ITEM_CODE);
		
		final Set<LayoutItem> beanChildrenLayoutItems =  
			StorableObjectPool.getStorableObjectsByCondition(
				linkedIdsCondition, 
				true, 
				true);

		assert Log.debugMessage(beanChildrenLayoutItems, Log.DEBUGLEVEL03);
		
		return beanChildrenLayoutItems; 
	}
	
	@Override
	public void dispose() throws ApplicationException {
		assert Log.debugMessage(Log.DEBUGLEVEL03);
		super.dispose();
		
		for(final LayoutItem layoutItem : this.getBeanChildrenLayoutItems()) {
			if(layoutItem.getName().startsWith(NET_CODENAME)) {
				continue;
			}
			if (layoutItem.getLayoutName().startsWith(ObjectEntities.DOMAIN)) {
				Log.debugMessage(layoutItem.getId() + ", "
					+ layoutItem.getName() 
					+ ", layoutName:" 
					+ layoutItem.getLayoutName(),							
				Log.DEBUGLEVEL10);		
				
				final GraphRoutines graphRoutines = this.managerMainFrame.getGraphRoutines();
				AbstractBean childBean = graphRoutines.getBean(layoutItem);
//				System.out.println(".dispose() | " + childBean);
				childBean.dispose();
				childBean.disposeLayoutItem();
			}
		}
		
		super.disposeLayoutItem();
		
		this.managerMainFrame.getGraph().getSelectionModel().clearSelection();
	}
	
	@Override
	public void applyTargetPort(MPort oldPort, MPort newPort) throws ApplicationException {
		assert Log.debugMessage(this.getBeanChildrenLayoutItems(), Log.DEBUGLEVEL03);
		assert Log.debugMessage(oldPort + ", " + newPort, Log.DEBUGLEVEL03);

//		Identifier parentDomainId = Identifier.VOID_IDENTIFIER;
//		
//		if (newPort != null) {
//			parentDomainId = ((DomainBean)newPort.getUserObject()).getIdentifier();
//		}
//		
//		Identifier oldParentDomainId = Identifier.VOID_IDENTIFIER;
//		
//		if (oldPort != null) {
//			oldParentDomainId = ((DomainBean)oldPort.getUserObject()).getIdentifier();
//		}
//
//		assert Log.debugMessage("oldParentDomainId:"
//			+ oldParentDomainId + ", parentDomainId: " + parentDomainId, Log.DEBUGLEVEL03);
//		
//		if (!parentDomainId.isVoid()) {
//			assert Log.debugMessage(this.getBeanChildrenLayoutItems(), Log.DEBUGLEVEL03);
//			for(LayoutItem layoutItem : this.getBeanChildrenLayoutItems()) {
//				if (layoutItem.getLayoutName().startsWith(ObjectEntities.DOMAIN)) {
//					final String layoutName = !parentDomainId.isVoid() ? 
//							parentDomainId.getIdentifierString() : 
//							ObjectEntities.DOMAIN;
//					Log.debugMessage(layoutItem.getId() + ", "
//						+ layoutItem.getName() 
//						+ ", layoutName:" 
//						+ layoutName 
//						+ "\n\toldParentDomainId:" 
//						+ oldParentDomainId
//						+ "\n\tparentDomainId:" 
//						+ parentDomainId,
//						
//					Log.DEBUGLEVEL10);		
//					layoutItem.setLayoutName(layoutName);
//					
//					final GraphRoutines graphRoutines = this.managerMainFrame.getGraphRoutines();
//					AbstractBean cell = graphRoutines.getBean(layoutItem);
//					if (cell instanceof DomainNetworkItem) {
//						DomainNetworkItem portBean = (DomainNetworkItem) cell;						
//						portBean.setDomainId(oldParentDomainId, parentDomainId);
//					}
//				}
//			}
//		}
		
		this.managerMainFrame.getGraph().getSelectionModel().clearSelection();
	}
	
	@Override
	public String getCodename() {
		return NetBeanFactory.NET_CODENAME + ObjectEntities.DOMAIN;
	}
	
	public final Domain getDomain() {
		return this.domain;
	}

}

