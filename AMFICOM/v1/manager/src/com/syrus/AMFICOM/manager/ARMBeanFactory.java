/*-
 * $Id: ARMBeanFactory.java,v 1.11 2005/08/23 15:02:14 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.manager;

import java.util.Set;

import org.jgraph.graph.DefaultGraphCell;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.resource.LayoutItem;

/**
 * @version $Revision: 1.11 $, $Date: 2005/08/23 15:02:14 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class ARMBeanFactory extends AbstractBeanFactory {
	
	public static final String ARM_CODENAME = "ARM";
	
	private static ARMBeanFactory instance;
	
	private Validator validator;
	
	private ARMBeanFactory() {
		super("Entity.AutomatedWorkplace", 
			"Entity.AutomatedWorkplace.acronym", 
			"com/syrus/AMFICOM/manager/resources/icons/arm.gif", 
			"com/syrus/AMFICOM/manager/resources/arm.gif");
	}
	
	public static final ARMBeanFactory getInstance() {
		if(instance == null) {
			synchronized (ARMBeanFactory.class) {
				if(instance == null) {
					instance = new ARMBeanFactory();
				}
			}
		}		
		return instance;
	}

	@Override
	public AbstractBean createBean(Perspective perspective) {
		return this.createBean(ARM_CODENAME + this.count);
	}
	
	@Override
	public AbstractBean createBean(final String codename) {
		++super.count;
		AbstractBean bean = new ARMBean();
		bean.setValidator(this.getValidator());
		bean.setCodeName(codename);
		bean.setId(Identifier.VOID_IDENTIFIER);		
		return bean;
	}
	
	private Validator getValidator() {
		if (this.validator == null) {
			this.validator = new Validator() {
				
				public boolean isValid(	AbstractBean sourceBean,
										AbstractBean targetBean) {
					System.out.println("ARMBeanFactory.Validator$1.isValid() | " 
						+ sourceBean.getName() 
						+ " -> " 
						+ targetBean.getName());
					return sourceBean != null && 
						targetBean != null && 
						sourceBean.getCodeName().startsWith(ARM_CODENAME) &&
						targetBean.getCodeName().startsWith(NetBeanFactory.NET_CODENAME);
				}
			};
		}
		return this.validator;
	}	
	
	private class ARMBean extends NonStorableBean implements DomainNetworkItem {
		
		public void setDomainId(Identifier oldDomainId,
								Identifier newDomainId) {
			try {
				TypicalCondition typicalCondition = 
					new TypicalCondition(this.getCodeName(), 
						OperationSort.OPERATION_EQUALS, 
						ObjectEntities.LAYOUT_ITEM_CODE, 
						StorableObjectWrapper.COLUMN_NAME);

				Set<LayoutItem> beanLayoutItems = StorableObjectPool.getStorableObjectsByCondition(
					typicalCondition, 
					true, 
					true);
				
				LinkedIdsCondition linkedIdsCondition = 
					new LinkedIdsCondition(Identifier.createIdentifiers(beanLayoutItems),
						ObjectEntities.LAYOUT_ITEM_CODE);
				
				Set<LayoutItem> beanChildrenLayoutItems =  StorableObjectPool.getStorableObjectsByCondition(
					linkedIdsCondition, 
					true, 
					true);
				
				for(LayoutItem layoutItem : beanChildrenLayoutItems) {
					layoutItem.setLayoutName(newDomainId.getIdentifierString());
					DefaultGraphCell cell = this.graphText.getCell(layoutItem);
					MPort port = (MPort) cell.getChildAt(0);
					
					AbstractBean portBean = port.getUserObject();
					if (portBean instanceof ARMItem) {
						ARMItem item = (ARMItem) portBean;
						item.setDomainId(oldDomainId, newDomainId);
					}
				}
			} catch (ApplicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
}
