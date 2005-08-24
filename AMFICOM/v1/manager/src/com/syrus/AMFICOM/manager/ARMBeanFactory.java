/*-
 * $Id: ARMBeanFactory.java,v 1.12 2005/08/24 14:05:47 bob Exp $
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
import com.syrus.AMFICOM.manager.UI.JGraphText;
import com.syrus.AMFICOM.resource.LayoutItem;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.12 $, $Date: 2005/08/24 14:05:47 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class ARMBeanFactory extends AbstractBeanFactory {
	
	public static final String ARM_CODENAME = "ARM";
	
	private static ARMBeanFactory instance;
	
	private Validator validator;
	
	private ARMBeanFactory(final JGraphText graphText) {
		super("Entity.AutomatedWorkplace", 
			"Entity.AutomatedWorkplace.acronym", 
			"com/syrus/AMFICOM/manager/resources/icons/arm.gif", 
			"com/syrus/AMFICOM/manager/resources/arm.gif");
		super.graphText = graphText;
	}
	
	public static final ARMBeanFactory getInstance(final JGraphText graphText) {
		if(instance == null) {
			synchronized (ARMBeanFactory.class) {
				if(instance == null) {
					instance = new ARMBeanFactory(graphText);
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
		bean.setGraphText(super.graphText);
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
					if (layoutItem.getLayoutName().startsWith(ObjectEntities.DOMAIN)) {
						final String layoutName = !newDomainId.isVoid() ? 
								newDomainId.getIdentifierString() : 
								ObjectEntities.DOMAIN;
						Log.debugMessage("ARMBean.setDomainId | "
							+ layoutItem.getId() + ", "
							+ layoutItem.getName() 
							+ ", layoutName:" 
							+ layoutName, 
						Log.DEBUGLEVEL09);						
						layoutItem.setLayoutName(layoutName);
						DomainNetworkItem portBean = 
							(DomainNetworkItem) this.graphText.getCell(layoutItem);
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
		return ARMBeanFactory.ARM_CODENAME;
	}
}
