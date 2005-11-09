/*-
 * $Id: WorkstationBeanFactory.java,v 1.4 2005/11/09 15:09:48 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.manager;

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
 * @version $Revision: 1.4 $, $Date: 2005/11/09 15:09:48 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public final class WorkstationBeanFactory extends AbstractBeanFactory<NonStorableBean> {
	
	public static final String WORKSTATION_CODENAME = "Workstation";
	
	private Validator validator;
	
	public WorkstationBeanFactory(final ManagerMainFrame graphText) {
		super("Manager.Entity.Workstation", 
			"Manager.Entity.Workstation.acronym");
		super.graphText = graphText;
	}
	
	@Override
	public NonStorableBean createBean(Perspective perspective) throws ApplicationException {
		return this.createBean(WORKSTATION_CODENAME + this.count);
	}
	
	@Override
	public NonStorableBean createBean(final String codename) 
	throws ApplicationException {
		++super.count;
		final WorkstationBean bean = new WorkstationBean();
		bean.setName(this.getName());
		bean.setGraphText(super.graphText);
		bean.setValidator(this.getValidator());
		bean.setId(codename);
		bean.setIdentifier(Identifier.VOID_IDENTIFIER);		
		return bean;
	}
	
	private Validator getValidator() {
		if (this.validator == null) {
			this.validator = new Validator() {
				
				public boolean isValid(	AbstractBean sourceBean,
										AbstractBean targetBean) {
					Log.debugMessage(sourceBean.getName() 
							+ " -> " 
							+ targetBean.getName(), 
						Log.DEBUGLEVEL10);
					return sourceBean != null && 
						targetBean != null && 
						sourceBean.getId().startsWith(WORKSTATION_CODENAME) &&
						targetBean.getId().startsWith(NetBeanFactory.NET_CODENAME);
				}
			};
		}
		return this.validator;
	}	
	
	private class WorkstationBean extends NonStorableBean implements DomainNetworkItem {
		
		private Set<LayoutItem> getBeanChildrenLayoutItems() 
		throws ApplicationException{
			final TypicalCondition typicalCondition = 
				new TypicalCondition(this.getId(), 
					OperationSort.OPERATION_EQUALS, 
					ObjectEntities.LAYOUT_ITEM_CODE, 
					StorableObjectWrapper.COLUMN_NAME);

			final Set<LayoutItem> beanLayoutItems = StorableObjectPool.getStorableObjectsByCondition(
				typicalCondition, 
				true, 
				true);
			
			final LinkedIdsCondition linkedIdsCondition = 
				new LinkedIdsCondition(Identifier.createIdentifiers(beanLayoutItems),
					ObjectEntities.LAYOUT_ITEM_CODE);
			
			return StorableObjectPool.getStorableObjectsByCondition(
				linkedIdsCondition, 
				true, 
				true);
		}
		
		@Override
		public void dispose() throws ApplicationException {			
			final GraphRoutines graphRoutines = this.graphText.getGraphRoutines();
			for(final LayoutItem layoutItem : this.getBeanChildrenLayoutItems()) {
				if (layoutItem.getLayoutName().startsWith(ObjectEntities.DOMAIN)) {					
					Log.debugMessage(layoutItem.getId() + ", "
						+ layoutItem.getName() 
						+ ", layoutName:" 
						+ layoutItem.getLayoutName(), 
					Log.DEBUGLEVEL10);			
					AbstractBean childBean = graphRoutines.getBean(layoutItem);
					childBean.dispose();
					childBean.disposeLayoutItem();
				}					
			}
			
			super.disposeLayoutItem();
		}

		@Override
		public String getCodename() {
			return WorkstationBeanFactory.WORKSTATION_CODENAME;
		}
		
		public void setDomainId(Identifier oldDomainId,
								Identifier newDomainId) {
			try {
				final GraphRoutines graphRoutines = this.graphText.getGraphRoutines();
				for(final LayoutItem layoutItem : this.getBeanChildrenLayoutItems()) {
					if (layoutItem.getLayoutName().startsWith(ObjectEntities.DOMAIN)) {
						final String layoutName = !newDomainId.isVoid() ? 
								newDomainId.getIdentifierString() : 
								ObjectEntities.DOMAIN;
						Log.debugMessage(layoutItem.getId() + ", "
							+ layoutItem.getName() 
							+ ", layoutName:" 
							+ layoutName, 
						Log.DEBUGLEVEL10);						
						layoutItem.setLayoutName(layoutName);
						DomainNetworkItem portBean = 
							(DomainNetworkItem) graphRoutines.getBean(layoutItem);
						portBean.setDomainId(oldDomainId, newDomainId);
					}					
				}
			} catch (ApplicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
	@Override
	public String getCodename() {
		return WorkstationBeanFactory.WORKSTATION_CODENAME;
	}
}
