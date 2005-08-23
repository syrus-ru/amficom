/*-
 * $Id: NetBeanFactory.java,v 1.12 2005/08/23 07:52:33 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.manager;

import java.util.Set;

import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.DomainMember;
import com.syrus.AMFICOM.administration.PermissionAttributes;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.resource.LayoutItem;



/**
 * @version $Revision: 1.12 $, $Date: 2005/08/23 07:52:33 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class NetBeanFactory extends AbstractBeanFactory {
	
	public static final String NET_CODENAME = "Net";
	
	private static NetBeanFactory instance;
	
	private Validator validator;
	
	private NetBeanFactory() {
		super("Entity.Net", 
			"Entity.Net", 
			"com/syrus/AMFICOM/manager/resources/icons/cloud.gif", 
			"com/syrus/AMFICOM/manager/resources/cloud.png");
	}
	
	public static final NetBeanFactory getInstance() {
		if(instance == null) {
			synchronized (NetBeanFactory.class) {
				if(instance == null) {
					instance = new NetBeanFactory();
				}
			}
		}		
		return instance;
	}

	@Override
	public AbstractBean createBean(Perspective perspective) {
		return this.createBean(NET_CODENAME + super.count);
	}

	@Override
	public AbstractBean createBean(final String codename) {
		++super.count;
		final AbstractBean bean = new NonStorableBean() {
			@Override
			public void applyTargetPort(MPort oldPort, MPort newPort) {
				System.out.println("NetBeanFactory.createBean() | " + oldPort + ", " + newPort);
				
				try {
					
					Identifier parentDomainId = Identifier.VOID_IDENTIFIER;
					
					if (newPort != null) {
						parentDomainId = ((DomainBean)newPort.getUserObject()).getId();
					}
					
					Identifier oldParentDomainId = Identifier.VOID_IDENTIFIER;
					
					if (oldPort != null) {
						oldParentDomainId = ((DomainBean)oldPort.getUserObject()).getId();
					}

					System.out.println("NetBeanFactory.applyTargetPort() | oldParentDomainId:"
						+ oldParentDomainId + ", parentDomainId: " + parentDomainId);
					
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
						String name1 = layoutItem.getName();
						layoutItem.setLayoutName(parentDomainId.getIdentifierString());
						System.out.println(".applyTargetPort() | setLayoutName " + parentDomainId.getIdentifierString() + " to " + layoutItem.getId());
						System.out.println(".applyTargetPort() | getLayoutName: " + layoutItem.getLayoutName());
						System.out.println("NetBeanFactory.applyTargetPort() | name1 " + name1);
						if (name1.startsWith(ARMBeanFactory.ARM_CODENAME)) {
							
							System.out.println(".applyTargetPort() | arm:" + name1);
							LinkedIdsCondition linkedIdsCondition2 = 
								new LinkedIdsCondition(layoutItem.getId(),
									ObjectEntities.LAYOUT_ITEM_CODE);
							
							Set<LayoutItem> armChildrenLayoutItems =  StorableObjectPool.getStorableObjectsByCondition(
								linkedIdsCondition2, 
								true, 
								true);

							
							if (oldParentDomainId.isVoid()) {
								for(LayoutItem item : armChildrenLayoutItems) {
									System.out.println(".applyTargetPort() | setLayoutName " + parentDomainId.getIdentifierString() + " to " + item.getId());
									item.setLayoutName(parentDomainId.getIdentifierString());
									Identifier userId = new Identifier(item.getName());
									PermissionAttributes attributes = 
										PermissionAttributes.createInstance(LoginManager.getUserId(),
										parentDomainId,
										userId, 
										0);
									
									System.out.println(".applyTargetPort() | create new PermissionAttributes: " + attributes.getId());
								}
							} else {
								 Domain oldDomain = 
									(Domain) StorableObjectPool.getStorableObject(oldParentDomainId, true); 
								for(LayoutItem item : armChildrenLayoutItems) {
									System.out.println(".applyTargetPort() | setLayoutName " + parentDomainId.getIdentifierString() + " to " + item.getId());
									item.setLayoutName(parentDomainId.getIdentifierString());
									PermissionAttributes permissionAttributes = 
										oldDomain.getPermissionAttributes(new Identifier(item.getName()));
									
									System.out.println(".applyTargetPort() | " + item.getName());
									
									if (permissionAttributes == null) {
										System.err.println(".applyTargetPort() | permissionAttributes null");
										continue;
									}
									
									if (parentDomainId.isVoid()) {
										System.out.println(".applyTargetPort() | delete " + permissionAttributes.getId());
										StorableObjectPool.delete(permissionAttributes.getId());
									} else {
										System.out.println(".applyTargetPort() | setDomainId " + parentDomainId
											+ " to "
											+ permissionAttributes.getId() );
										permissionAttributes.setDomainId(parentDomainId);
									}
								}
							}
						} else {
							Identifier identifier = new Identifier(name1);
							StorableObject storableObject = StorableObjectPool.getStorableObject(identifier, true);
							if (storableObject instanceof DomainMember) {
								DomainMember domainMember = (DomainMember)storableObject;
								System.out.println(".applyTargetPort() | setDomainId " + parentDomainId 
									+ " to " + domainMember.getId());
								domainMember.setDomainId(parentDomainId);
							}
						}
						
						
					}
				} catch (ApplicationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		bean.setCodeName(codename);
		bean.setValidator(this.getValidator());
		bean.setId(Identifier.VOID_IDENTIFIER);
		return bean;
	}
	
	private final Validator getValidator() {
		if (this.validator == null) {
			 this.validator = new Validator() {
					
					public boolean isValid(	AbstractBean sourceBean,
											AbstractBean targetBean) {
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
