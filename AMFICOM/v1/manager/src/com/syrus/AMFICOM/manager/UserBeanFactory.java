/*-
* $Id: UserBeanFactory.java,v 1.11 2005/08/10 14:02:25 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager;

import static com.syrus.AMFICOM.manager.UserBeanWrapper.FULL_NAME;
import static com.syrus.AMFICOM.manager.UserBeanWrapper.LOGIN;
import static com.syrus.AMFICOM.manager.UserBeanWrapper.NAME;
import static com.syrus.AMFICOM.manager.UserBeanWrapper.USER_BUILDING;
import static com.syrus.AMFICOM.manager.UserBeanWrapper.USER_CELLULAR;
import static com.syrus.AMFICOM.manager.UserBeanWrapper.USER_CITY;
import static com.syrus.AMFICOM.manager.UserBeanWrapper.USER_COMPANY;
import static com.syrus.AMFICOM.manager.UserBeanWrapper.USER_DEPARTEMENT;
import static com.syrus.AMFICOM.manager.UserBeanWrapper.USER_EMAIL;
import static com.syrus.AMFICOM.manager.UserBeanWrapper.USER_NATURE;
import static com.syrus.AMFICOM.manager.UserBeanWrapper.USER_PHONE;
import static com.syrus.AMFICOM.manager.UserBeanWrapper.USER_POSITION;
import static com.syrus.AMFICOM.manager.UserBeanWrapper.USER_ROOM_NO;
import static com.syrus.AMFICOM.manager.UserBeanWrapper.USER_STREET;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;

import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.PermissionAttributes;
import com.syrus.AMFICOM.administration.SystemUser;
import com.syrus.AMFICOM.administration.corba.IdlSystemUserPackage.SystemUserSort;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.manager.UI.JGraphText;


/**
 * @version $Revision: 1.11 $, $Date: 2005/08/10 14:02:25 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class UserBeanFactory extends TabledBeanFactory {

	private static UserBeanFactory instance;
	
	private List<String> names;
	private UserBeanFactory() {
		super("Entity.User", 
			"Entity.User", 
			"com/syrus/AMFICOM/manager/resources/icons/user.gif", 
			"com/syrus/AMFICOM/manager/resources/user.gif");
		
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
	
	public static final UserBeanFactory getInstance() {
		if (instance == null) {
			synchronized (UserBeanFactory.class) {
				if (instance == null) {
					instance = new UserBeanFactory();
				}				
			}
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
			Identifier userId = user.getId();
			Identifier domainId = domainPerpective.getDomainId();		

			Domain domain = StorableObjectPool.getStorableObject(domainId, true);			
			
			PermissionAttributes permissionAttributes = domain.getPermissionAttributes(userId);
			
			if (permissionAttributes == null) {
				permissionAttributes = PermissionAttributes.createInstance(
					LoginManager.getUserId(),
					domainId,
					userId,
					0L);
			}
			
			return this.createBean(user.getId());
		} catch (ApplicationException e) {
			throw new CreateObjectException(e);
		}

	}

	@Override
	public AbstractBean createBean(Identifier identifier) {
		UserBean bean = new UserBean(this.names);
		bean.setCodeName("User");
		bean.setValidator(this.getValidator());		

		bean.setId(identifier);	
		
		bean.table = super.getTable(bean, 
			UserBeanWrapper.getInstance(),
			new String[] {NAME,
				LOGIN,
				FULL_NAME, 
				USER_NATURE, 
				USER_POSITION,
				USER_DEPARTEMENT,
				USER_COMPANY,
				USER_ROOM_NO,
				USER_CITY,
				USER_STREET,
				USER_BUILDING,
				USER_EMAIL,
				USER_PHONE,
				USER_CELLULAR});
		
		bean.addPropertyChangeListener(this.listener);
		
		bean.setPropertyPanel(this.panel);
		
		JGraphText.entityDispatcher.firePropertyChange(
			new PropertyChangeEvent(this, ObjectEntities.SYSTEMUSER, null, bean));

		
		return bean;
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
						sourceBean.getCodeName().equals("User") &&
						targetBean.getCodeName().equals("ARM");
				}
			};
		}
		return this.validator;
	}
	
}

