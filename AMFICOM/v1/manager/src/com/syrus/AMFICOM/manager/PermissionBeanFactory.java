/*-
* $Id: PermissionBeanFactory.java,v 1.3 2005/10/18 15:10:39 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager;

import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Map;

import com.syrus.AMFICOM.administration.PermissionAttributes;
import com.syrus.AMFICOM.administration.PermissionAttributes.Module;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.manager.UI.ManagerMainFrame;


/**
 * @version $Revision: 1.3 $, $Date: 2005/10/18 15:10:39 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class PermissionBeanFactory extends AbstractBeanFactory<PermissionBean> {

	private Validator validator;
	
	private static Map<Module, PermissionBeanFactory> instanceMap;

	private final Module module;
	
	private PermissionBeanFactory(final ManagerMainFrame graphText, 
	                              final Module module) {
		super(module.getDescription(), module.getDescription());
		super.graphText = graphText;
		this.module = module;
	}
	
	@Override
	public String getShortName() {
		return this.module.getDescription();
	}
	
	@Override
	public String getName() {
		return this.module.getDescription();
	}
	
	public static final synchronized PermissionBeanFactory getInstance(final ManagerMainFrame graphText,
			final Module module) {
		if (instanceMap == null) {
			instanceMap = new HashMap<Module, PermissionBeanFactory>();				
		}
		
		PermissionBeanFactory factory = instanceMap.get(module);
		if (factory == null) {
			factory = new PermissionBeanFactory(graphText, module);
			instanceMap.put(module, factory);
		}
		return factory;
	}

	
	@Override
	public PermissionBean createBean(final Perspective perspective) 
	throws ApplicationException {
		
		final SystemUserPerpective userPerpective = (SystemUserPerpective) perspective;
		
		final PermissionAttributes permissionAttributes = 
			PermissionAttributes.createInstance(LoginManager.getUserId(),
				userPerpective.getDomainId(),
				userPerpective.getUserId(),
				this.module);
		
		return this.createBean(permissionAttributes.getId());

	}
	
	@Override
	public PermissionBean createBean(final String codename) 
	throws ApplicationException {
		return this.createBean(new Identifier(codename));
	}
	
	protected PermissionBean createBean(final Identifier identifier) 
	throws ApplicationException {
		
		final PermissionAttributes permissionAttributes = 
			StorableObjectPool.getStorableObject(identifier, true);
		final Module module2 = permissionAttributes.getModule();
		if (module2 != this.module) {
			final PermissionBeanFactory factory = 
				instanceMap.get(module2);
			return factory.createBean(identifier);
		}
				
		final PermissionBean bean = new PermissionBean();
		++super.count;
		bean.setGraphText(super.graphText);
		bean.setCodeName(identifier.getIdentifierString());
		bean.setValidator(this.getValidator());		

		bean.setId(identifier);
		
		super.graphText.getDispatcher().firePropertyChange(
			new PropertyChangeEvent(this, ObjectEntities.PERMATTR, null, bean));
		
		return bean;
	}
	
	@Override
	public String getCodename() {
		return ObjectEntities.PERMATTR;
	}
	
	private Validator getValidator() {
		if (this.validator == null) {
			this.validator = new Validator() {
				
				public boolean isValid(	AbstractBean sourceBean,
										AbstractBean targetBean) {
					System.out.println("PermissionBeanFactory.Validator$1.isValid() | " 
						+ sourceBean.getName() 
						+ " -> " 
						+ targetBean.getName());
					return sourceBean != null && 
						targetBean != null && 
						sourceBean.getCodeName().startsWith(ObjectEntities.PERMATTR) &&
						targetBean.getCodeName().startsWith(ObjectEntities.SYSTEMUSER);
				}
			};
		}
		return this.validator;
	}
	
	public final Module getModule() {
		return this.module;
	}	
}
