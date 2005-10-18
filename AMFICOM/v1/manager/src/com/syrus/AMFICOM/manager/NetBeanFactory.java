/*-
 * $Id: NetBeanFactory.java,v 1.24 2005/10/18 15:10:38 bob Exp $
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
import com.syrus.AMFICOM.manager.UI.ManagerMainFrame;
import com.syrus.AMFICOM.resource.LayoutItem;
import com.syrus.util.Log;



/**
 * @version $Revision: 1.24 $, $Date: 2005/10/18 15:10:38 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class NetBeanFactory extends AbstractBeanFactory<NonStorableBean> {
	
	public static final String NET_CODENAME = "Net";
	
	private static NetBeanFactory instance;
	
	private Validator validator;
	
	private NetBeanFactory(final ManagerMainFrame graphText) {
		super("Manager.Entity.Net", 
			"Manager.Entity.Net");
		super.graphText = graphText;
	}
	
	public static final synchronized NetBeanFactory getInstance(final ManagerMainFrame graphText) {
		if(instance == null) {
			instance = new NetBeanFactory(graphText);
		}		
		return instance;
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
			private static final String UI_CLASS_ID = "NetBeanUI";
			
			@Override
			public String getUIClassID() {
				return UI_CLASS_ID;
			}
			
			private Set<LayoutItem> getBeanChildrenLayoutItems() 
			throws ApplicationException{
				final TypicalCondition typicalCondition = 
					new TypicalCondition(this.getCodeName(), 
						OperationSort.OPERATION_EQUALS, 
						ObjectEntities.LAYOUT_ITEM_CODE, 
						StorableObjectWrapper.COLUMN_NAME);

				final Set<LayoutItem> beanLayoutItems = StorableObjectPool.getStorableObjectsByCondition(
					typicalCondition, 
					true, 
					true);
				
				for(final Iterator<LayoutItem> it = beanLayoutItems.iterator(); it.hasNext();) {
					final LayoutItem layoutItem = it.next();
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
						
						AbstractBean childBean = this.graphText.getCell(layoutItem);
						System.out.println(".dispose() | " + childBean);
						childBean.dispose();
						childBean.disposeLayoutItem();
					}
				}
				
				super.disposeLayoutItem();
				
				this.graphText.valueChanged(null);
			}
			
			@Override
			public void applyTargetPort(MPort oldPort, MPort newPort) throws ApplicationException {
				assert Log.debugMessage("NetBeanFactory.createBean() | " + oldPort + ", " + newPort, Log.DEBUGLEVEL10);

				Identifier parentDomainId = Identifier.VOID_IDENTIFIER;
				
				if (newPort != null) {
					parentDomainId = ((DomainBean)newPort.getUserObject()).getId();
				}
				
				Identifier oldParentDomainId = Identifier.VOID_IDENTIFIER;
				
				if (oldPort != null) {
					oldParentDomainId = ((DomainBean)oldPort.getUserObject()).getId();
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
						
						AbstractBean cell = this.graphText.getCell(layoutItem);
						if (cell instanceof DomainNetworkItem) {
							DomainNetworkItem portBean = (DomainNetworkItem) cell;						
							portBean.setDomainId(oldParentDomainId, parentDomainId);
						}
					}
				}
				
				this.graphText.valueChanged(null); 
			}
		};
		bean.setGraphText(super.graphText);
		bean.setCodeName(codename);
		bean.setValidator(this.getValidator());
		bean.setId(Identifier.VOID_IDENTIFIER);
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
							sourceBean.getCodeName().startsWith(NET_CODENAME) &&
							targetBean.getCodeName().startsWith(ObjectEntities.DOMAIN);
					}
				};
		}
		return this.validator;
	}
}
