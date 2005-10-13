/*-
* $Id: UserBeanFactory.java,v 1.24 2005/10/13 15:28:14 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager;

import java.beans.PropertyChangeEvent;
import java.util.List;
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
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.manager.UI.ManagerMainFrame;
import com.syrus.util.WrapperComparator;


/**
 * @version $Revision: 1.24 $, $Date: 2005/10/13 15:28:14 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class UserBeanFactory extends TabledBeanFactory {

	private static UserBeanFactory instance;
	
	private SortedSet<Role> roles;

	private UserBeanFactory(final ManagerMainFrame graphText) {
		super("Manager.Entity.User", 
			"Manager.Entity.User", 
			"com/syrus/AMFICOM/manager/resources/icons/user.gif", 
			"com/syrus/AMFICOM/manager/resources/user.gif");
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
	public AbstractBean createBean(Perspective perspective) 
	throws CreateObjectException, IllegalObjectEntityException {
		
		DomainPerpective domainPerpective = (DomainPerpective) perspective;
		
		String login = I18N.getString("Manager.Entity.User") + "-" + (++super.count);
		
		SystemUser user = SystemUser.createInstance(LoginManager.getUserId(),
			login,
			SystemUserSort.USER_SORT_REGULAR,
			login,
			"");
		
		
		try {			
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
		} catch (final ApplicationException e) {
			throw new CreateObjectException(e);
		}

	}
	
	@Override
	protected AbstractBean createBean(Identifier identifier) {
		final UserBean bean = new UserBean(this.roles);
		++super.count;
		bean.setGraphText(super.graphText);
		bean.setCodeName(identifier.getIdentifierString());
		bean.setValidator(this.getValidator());		

		bean.setId(identifier);	
		
		final UserBeanWrapper userBeanWrapper = UserBeanWrapper.getInstance();
		final List<String> keys = userBeanWrapper.getKeys();
		
		bean.table = super.getTable(bean, 
			userBeanWrapper,
			keys.toArray(new String[keys.size()]));
		
		bean.addPropertyChangeListener(this.listener);
		
		bean.setPropertyPanel(this.panel);		
		
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

