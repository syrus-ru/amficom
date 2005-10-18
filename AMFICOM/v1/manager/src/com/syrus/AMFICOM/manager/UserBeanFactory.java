/*-
* $Id: UserBeanFactory.java,v 1.25 2005/10/18 15:10:38 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager;

import java.beans.PropertyChangeEvent;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.PermissionAttributes;
import com.syrus.AMFICOM.administration.Role;
import com.syrus.AMFICOM.administration.RoleWrapper;
import com.syrus.AMFICOM.administration.SystemUser;
import com.syrus.AMFICOM.administration.PermissionAttributes.Module;
import com.syrus.AMFICOM.administration.corba.IdlSystemUserPackage.SystemUserSort;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.manager.UI.ManagerMainFrame;
import com.syrus.util.WrapperComparator;


/**
 * @version $Revision: 1.25 $, $Date: 2005/10/18 15:10:38 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class UserBeanFactory extends IdentifiableBeanFactory<UserBean> {

	private static UserBeanFactory instance;
	
	private SortedSet<Role> roles;

	private UserBeanFactory(final ManagerMainFrame graphText) {
		super("Manager.Entity.User", 
			"Manager.Entity.User");
		super.graphText = graphText;
		this.roles = 
			new TreeSet<Role>(
				new WrapperComparator<Role>(RoleWrapper.getInstance(), StorableObjectWrapper.COLUMN_DESCRIPTION));
		
		try {
			final Set<Role> roles1 = 
				StorableObjectPool.getStorableObjectsByCondition(
					new EquivalentCondition(ObjectEntities.ROLE_CODE), true);
			this.roles.addAll(roles1);
		} catch (final ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static final synchronized UserBeanFactory getInstance(final ManagerMainFrame graphText) {
		if (instance == null) {
			instance = new UserBeanFactory(graphText);
		}
		return instance;
	}

	
	@Override
	public UserBean createBean(final Perspective perspective) 
	throws ApplicationException {
		
		final DomainPerpective domainPerpective = (DomainPerpective) perspective;
		
		final String login = I18N.getString("Manager.Entity.User") + "-" + (++super.count);
		
		final SystemUser user = SystemUser.createInstance(LoginManager.getUserId(),
			login,
			SystemUserSort.USER_SORT_REGULAR,
			login,
			"");
		
		
		final Identifier userId = user.getId();
		final Identifier domainId = domainPerpective.getDomainId();		

		final Domain domain = StorableObjectPool.getStorableObject(domainId, true);			
		
		for(final Module module : Module.getValueList()) {
			if (!module.isEnable()) {
				continue;
			}
			final PermissionAttributes permissionAttributes = 
				domain.getPermissionAttributes(userId, module);
			
			if (permissionAttributes == null) {
				PermissionAttributes.createInstance(
					LoginManager.getUserId(),
					domainId,
					userId,
					module);
			}
		}
		
		return this.createBean(user.getId());

	}
	
	@Override
	protected UserBean createBean(final Identifier identifier) 
	throws ApplicationException {
		final UserBean bean = new UserBean(this.roles);
		++super.count;
		bean.setGraphText(super.graphText);
		bean.setCodeName(identifier.getIdentifierString());
		bean.setValidator(this.getValidator());	
		
		bean.setId(identifier);
		
		super.graphText.getDispatcher().firePropertyChange(
			new PropertyChangeEvent(this, ObjectEntities.SYSTEMUSER, null, bean));

		
		return bean;
	}
	
	@Override
	public String getCodename() {
		return ObjectEntities.SYSTEMUSER;
	}
	
	private Validator getValidator() {
		if (this.validator == null) {
			this.validator = new Validator() {
				
				public boolean isValid(	AbstractBean sourceBean,
										AbstractBean targetBean) {
					System.out.println("UserBeanFactory.Validator$1.isValid() | " 
						+ sourceBean.getName() 
						+ " -> " 
						+ targetBean.getName());
					return sourceBean != null && 
						targetBean != null && 
						sourceBean.getCodeName().startsWith(ObjectEntities.SYSTEMUSER) &&
						targetBean.getCodeName().startsWith(WorkstationBeanFactory.WORKSTATION_CODENAME);
				}
			};
		}
		return this.validator;
	}
	
}

