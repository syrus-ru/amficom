/*-
* $Id: PermissionBeanFactory.java,v 1.2 2005/11/28 14:47:05 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.beans;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
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
import com.syrus.AMFICOM.manager.perspective.Perspective;
import com.syrus.AMFICOM.manager.perspective.RolePerpective;
import com.syrus.AMFICOM.manager.perspective.SystemUserPerpective;


/**
 * @version $Revision: 1.2 $, $Date: 2005/11/28 14:47:05 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public final class PermissionBeanFactory extends AbstractBeanFactory<PermissionBean> {

	private Map<Module, PermissionBeanFactory> userInstanceMap;
	
	private final Module module;
	
	public PermissionBeanFactory(final ManagerMainFrame graphText) {
		super(null, null);
		super.graphText = graphText;
		this.module = null;
		this.userInstanceMap = new HashMap<Module, PermissionBeanFactory>();
	}
	
	private PermissionBeanFactory(final ManagerMainFrame graphText, 
			final Module module) {
		super(module.getDescription(), module.getDescription());
		super.graphText = graphText;
		this.module = module;
	}
	
	public PermissionBeanFactory getUserInstance(final Module module) {
		PermissionBeanFactory factory = 
			this.userInstanceMap.get(module);
		if (factory == null) {
			factory = new PermissionBeanFactory(this.graphText, module);
			this.userInstanceMap.put(module, factory);
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
	public PermissionBean createBean(final Perspective perspective) 
	throws ApplicationException {
		if (perspective instanceof SystemUserPerpective) {		
			final SystemUserPerpective userPerpective = (SystemUserPerpective) perspective;
			
			final PermissionAttributes permissionAttributes = 
				PermissionAttributes.createInstance(LoginManager.getUserId(),
					userPerpective.getDomainId(),
					userPerpective.getUserId(),
					this.module);
			
			return this.createBean(permissionAttributes.getId());
		}

		final RolePerpective rolePerpective = (RolePerpective) perspective;
		
		final PermissionAttributes permissionAttributes = 
			PermissionAttributes.createInstance(LoginManager.getUserId(),
				Identifier.VOID_IDENTIFIER,
				rolePerpective.getRoleId(),
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
			final PermissionBeanFactory factory = this.getUserInstance(module2);	
			return factory.createBean(identifier);
		}
				
		final PermissionBean bean = new PermissionBean();
		++super.count;
		bean.setManagerMainFrame(super.graphText);
		bean.setId(identifier.getIdentifierString());

		bean.setIdentifier(identifier);
		final ManagerModel managerModel = (ManagerModel)this.graphText.getModel();
		Dispatcher dispatcher = managerModel.getDispatcher();
		dispatcher.firePropertyChange(
			new PropertyChangeEvent(this, ObjectEntities.PERMATTR, null, bean));
		dispatcher.addPropertyChangeListener(ObjectEntities.ROLE, new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				try {
					bean.updateRolePermissions();
				} catch (ApplicationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}});
		
		return bean;
	}
	
	@Override
	public String getCodename() {
		return this.module != null ? 
				this.module.getCodename():
				ObjectEntities.PERMATTR;
	}

	public final Module getModule() {
		return this.module;
	}	
}
