/*-
 * $Id: NetBeanFactory.java,v 1.25 2005/11/07 15:24:19 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.manager;

import java.util.Iterator;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.manager.UI.GraphRoutines;
import com.syrus.AMFICOM.manager.UI.ManagerMainFrame;
import com.syrus.AMFICOM.resource.LayoutItem;
import com.syrus.util.Log;



/**
 * @version $Revision: 1.25 $, $Date: 2005/11/07 15:24:19 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public final class NetBeanFactory extends AbstractBeanFactory<NonStorableBean> {
	
	public static final String NET_CODENAME = "Net";
	
	private Validator validator;
	
	public NetBeanFactory(final ManagerMainFrame graphText) {
		super("Manager.Entity.Net", 
			"Manager.Entity.Net");
		super.graphText = graphText;
	}
	
	@Override
	public NonStorableBean createBean(Perspective perspective) 
	throws ApplicationException {
		return this.createBean(NET_CODENAME + super.count);
	}

	@Override
	public NonStorableBean createBean(final String codename) 
	throws ApplicationException {
		++super.count;
		final NonStorableBean bean = new NonStorableBean() {
			
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
				
				for(final Iterator<LayoutItem> it = beanLayoutItems.iterator(); it.hasNext();) {
					final LayoutItem layoutItem = it.next();
					assert Log.debugMessage("" + layoutItem.getLayoutName() + ", " + layoutItem.getName(),
						Log.DEBUGLEVEL04);
					if (!layoutItem.getLayoutName().startsWith(ObjectEntities.DOMAIN)) {
						it.remove();
					}
				}
				
				final LinkedIdsCondition linkedIdsCondition = 
					new LinkedIdsCondition(Identifier.createIdentifiers(beanLayoutItems),
						ObjectEntities.LAYOUT_ITEM_CODE);
				
				final Set<LayoutItem> beanChildrenLayoutItems =  StorableObjectPool.getStorableObjectsByCondition(
					linkedIdsCondition, 
					true, 
					true);
				
				beanChildrenLayoutItems.addAll(beanLayoutItems);
				return beanChildrenLayoutItems;				
			}
			
			@Override
			public void dispose() throws ApplicationException {
				super.dispose();
				
				for(final LayoutItem layoutItem : this.getBeanChildrenLayoutItems()) {
					if(layoutItem.getName().startsWith(NET_CODENAME)) {
						continue;
					}
					if (layoutItem.getLayoutName().startsWith(ObjectEntities.DOMAIN)) {
						Log.debugMessage("NetBean.dispose | " 
							+ layoutItem.getId() + ", "
							+ layoutItem.getName() 
							+ ", layoutName:" 
							+ layoutItem.getLayoutName(),							
						Log.DEBUGLEVEL10);		
						
						final GraphRoutines graphRoutines = this.graphText.getGraphRoutines();
						AbstractBean childBean = graphRoutines.getBean(layoutItem);
						System.out.println(".dispose() | " + childBean);
						childBean.dispose();
						childBean.disposeLayoutItem();
					}
				}
				
				super.disposeLayoutItem();
				
				this.graphText.getGraph().getSelectionModel().clearSelection();
			}
			
			@Override
			public void applyTargetPort(MPort oldPort, MPort newPort) throws ApplicationException {
				assert Log.debugMessage("NetBeanFactory.createBean() | " + oldPort + ", " + newPort, Log.DEBUGLEVEL10);

				Identifier parentDomainId = Identifier.VOID_IDENTIFIER;
				
				if (newPort != null) {
					parentDomainId = ((DomainBean)newPort.getUserObject()).getIdentifier();
				}
				
				Identifier oldParentDomainId = Identifier.VOID_IDENTIFIER;
				
				if (oldPort != null) {
					oldParentDomainId = ((DomainBean)oldPort.getUserObject()).getIdentifier();
				}

				assert Log.debugMessage("NetBeanFactory.applyTargetPort() | oldParentDomainId:"
					+ oldParentDomainId + ", parentDomainId: " + parentDomainId, Log.DEBUGLEVEL10);
				
				for(LayoutItem layoutItem : this.getBeanChildrenLayoutItems()) {
					if (layoutItem.getLayoutName().startsWith(ObjectEntities.DOMAIN)) {
						final String layoutName = !parentDomainId.isVoid() ? 
								parentDomainId.getIdentifierString() : 
								ObjectEntities.DOMAIN;
						Log.debugMessage("NetBean.setDomainId | " 
							+ layoutItem.getId() + ", "
							+ layoutItem.getName() 
							+ ", layoutName:" 
							+ layoutName 
							+ "\n\toldParentDomainId:" 
							+ oldParentDomainId
							+ "\n\tparentDomainId:" 
							+ parentDomainId,
							
						Log.DEBUGLEVEL10);		
						layoutItem.setLayoutName(layoutName);
						
						final GraphRoutines graphRoutines = this.graphText.getGraphRoutines();
						AbstractBean cell = graphRoutines.getBean(layoutItem);
						if (cell instanceof DomainNetworkItem) {
							DomainNetworkItem portBean = (DomainNetworkItem) cell;						
							portBean.setDomainId(oldParentDomainId, parentDomainId);
						}
					}
				}
				
				this.graphText.getGraph().getSelectionModel().clearSelection();
			}
			
			@Override
			public String getCodename() {
				return NET_CODENAME;
			}
		};
		bean.setName(this.getName());
		bean.setGraphText(super.graphText);
		bean.setId(codename);
		bean.setValidator(this.getValidator());
		bean.setIdentifier(Identifier.VOID_IDENTIFIER);
		return bean;
	}
	
	@Override
	public String getCodename() {
		return NET_CODENAME;
	}
	
	private final Validator getValidator() {
		if (this.validator == null) {
			 this.validator = new Validator() {
					
					public boolean isValid(	AbstractBean sourceBean,
											AbstractBean targetBean) {
						Log.debugMessage("NetBeanFactory.getValidator() | " 
								+ sourceBean 
								+ " > " 
								+ targetBean,
							Log.DEBUGLEVEL10);
						return sourceBean != null && 
							targetBean != null && 
							sourceBean.getId().startsWith(NET_CODENAME) &&
							targetBean.getId().startsWith(ObjectEntities.DOMAIN);
					}
				};
		}
		return this.validator;
	}
}
