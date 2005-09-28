/*-
* $Id: UserBeanFactory.java,v 1.22 2005/09/28 14:05:25 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager;

import java.beans.PropertyChangeEvent;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.PermissionAttributes;
import com.syrus.AMFICOM.administration.SystemUser;
import com.syrus.AMFICOM.administration.PermissionAttributes.Module;
import com.syrus.AMFICOM.administration.corba.IdlSystemUserPackage.SystemUserSort;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.manager.UI.ManagerMainFrame;


/**
 * @version $Revision: 1.22 $, $Date: 2005/09/28 14:05:25 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class UserBeanFactory extends TabledBeanFactory {

	private static UserBeanFactory instance;
	
	private List<String> names;

	private UserBeanFactory(final ManagerMainFrame graphText) {
		super("Entity.User", 
			"Entity.User", 
			"com/syrus/AMFICOM/manager/resources/icons/user.gif", 
			"com/syrus/AMFICOM/manager/resources/user.gif");
		super.graphText = graphText;
		this.names = new ArrayList<String>();
		this.names.add(LangModelManager.getString("Entity.User.Subscriber"));
		this.names.add(null);
		this.names.add(LangModelManager.getString("Entity.User.SystemAdministator"));
		this.names.add(LangModelManager.getString("Entity.User.MediaMonitoringAdministator"));
		this.names.add(LangModelManager.getString("Entity.User.Analyst"));
		this.names.add(LangModelManager.getString("Entity.User.Operator"));
		this.names.add(null);
		this.names.add(LangModelManager.getString("Entity.User.Planner"));
		this.names.add(LangModelManager.getString("Entity.User.Specialist"));
		this.names.add(null);
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
		
		String login = LangModelManager.getString("Entity.User") + "-" + (++super.count);
		
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
				final PermissionAttributes permissionAttributes = 
					domain.getPermissionAttributes(userId, module);
				
				if (permissionAttributes == null) {
					PermissionAttributes.createInstance(
						LoginManager.getUserId(),
						domainId,
						userId,
						module,
						new BigInteger("0"));
				}
			}
			
			return this.createBean(user.getId());
		} catch (final ApplicationException e) {
			throw new CreateObjectException(e);
		}

	}
	
	@Override
	protected AbstractBean createBean(Identifier identifier) {
		final UserBean bean = new UserBean(this.names);
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
						targetBean.getCodeName().startsWith(ARMBeanFactory.ARM_CODENAME);
				}
			};
		}
		return this.validator;
	}
	
}

