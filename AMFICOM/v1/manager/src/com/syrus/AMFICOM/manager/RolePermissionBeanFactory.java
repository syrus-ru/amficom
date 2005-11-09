/*-
* $Id: RolePermissionBeanFactory.java,v 1.2 2005/11/09 15:09:48 bob Exp $
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
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.manager.UI.ManagerMainFrame;
import com.syrus.AMFICOM.manager.UI.ManagerModel;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.2 $, $Date: 2005/11/09 15:09:48 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public final class RolePermissionBeanFactory extends AbstractBeanFactory<RolePermissionBean> {

	private Validator validator;
	
	private Map<Module, RolePermissionBeanFactory> instanceMap;

	private final Module module;
	
	public RolePermissionBeanFactory(final ManagerMainFrame graphText) {
		super(null, null);
		super.graphText = graphText;
		this.module = null;
		this.instanceMap = new HashMap<Module, RolePermissionBeanFactory>();
	}
	
	private RolePermissionBeanFactory(final ManagerMainFrame graphText, 
			final Module module) {
		super(module.getDescription(), module.getDescription());
		super.graphText = graphText;
		this.module = module;
	}
	
	public RolePermissionBeanFactory getInstance(final Module module) {
		RolePermissionBeanFactory factory = 
			this.instanceMap.get(module);
		if (factory == null) {
			factory = new RolePermissionBeanFactory(this.graphText, module);
			this.instanceMap.put(module, factory);
		}
		return factory;
	}
	
	@Override
	public String getShortName() {
		return this.module.getDescription();
	}
	
	@Override
	public String getName() {
		return this.module.getDescription();
	}
	
	@Override
	public RolePermissionBean createBean(final Perspective perspective) 
	throws ApplicationException {
		
		final RolePerpective rolePerpective = (RolePerpective) perspective;
		
		final PermissionAttributes permissionAttributes = 
			PermissionAttributes.createInstance(LoginManager.getUserId(),
				Identifier.VOID_IDENTIFIER,
				rolePerpective.getRoleId(),
				this.module);
		
		return this.createBean(permissionAttributes.getId());

	}
	
	@Override
	public RolePermissionBean createBean(final String codename) 
	throws ApplicationException {
		return this.createBean(new Identifier(codename));
	}
	
	protected RolePermissionBean createBean(final Identifier identifier) 
	throws ApplicationException {
		
		final PermissionAttributes permissionAttributes = 
			StorableObjectPool.getStorableObject(identifier, true);
		final Module module2 = permissionAttributes.getModule();
		if (module2 != this.module) {
			final RolePermissionBeanFactory factory = this.getInstance(module2);
			return factory.createBean(identifier);
		}
				
		final RolePermissionBean bean = new RolePermissionBean();
		++super.count;
		bean.setGraphText(super.graphText);
		bean.setId(identifier.getIdentifierString());
		bean.setValidator(this.getValidator());		

		bean.setIdentifier(identifier);
		final ManagerModel managerModel = (ManagerModel)this.graphText.getModel();
		Dispatcher dispatcher = managerModel.getDispatcher();
		dispatcher.firePropertyChange(
			new PropertyChangeEvent(this, ObjectEntities.PERMATTR, null, bean));
		
		return bean;
	}
	
	@Override
	public String getCodename() {
		return (this.module != null ? this.module.getCodename() : ObjectEntities.PERMATTR);
	}
	
	private Validator getValidator() {
		if (this.validator == null) {
			this.validator = new Validator() {
				
				public boolean isValid(	AbstractBean sourceBean,
										AbstractBean targetBean) {
					assert Log.debugMessage(
						sourceBean.getName() + sourceBean.getId()
						+ " -> " 
						+ targetBean.getName() + targetBean.getId(), Log.DEBUGLEVEL10);
					return sourceBean != null && 
						targetBean != null && 
						sourceBean.getId().startsWith(ObjectEntities.PERMATTR) &&
						targetBean.getId().startsWith(ObjectEntities.ROLE);
				}
			};
		}
		return this.validator;
	}
	
	public final Module getModule() {
		return this.module;
	}	
}
