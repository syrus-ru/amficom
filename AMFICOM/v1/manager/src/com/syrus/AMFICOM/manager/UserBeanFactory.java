/*-
* $Id: UserBeanFactory.java,v 1.10 2005/08/02 14:42:06 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager;

import static com.syrus.AMFICOM.manager.UserBeanWrapper.*;

import java.util.ArrayList;
import java.util.List;

import com.syrus.AMFICOM.administration.SystemUser;
import com.syrus.AMFICOM.administration.corba.IdlSystemUserPackage.SystemUserSort;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;


/**
 * @version $Revision: 1.10 $, $Date: 2005/08/02 14:42:06 $
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
	public AbstractBean createBean() 
	throws CreateObjectException, IllegalObjectEntityException {
		UserBean bean = new UserBean(this.names);
		bean.setCodeName("User");
		bean.setValidator(this.getValidator());		

		
		SystemUser kis = SystemUser.createInstance(LoginManager.getUserId(),
			"",
			SystemUserSort.USER_SORT_REGULAR,
			"",
			"");
		StorableObjectPool.putStorableObject(kis);
		bean.setId(kis.getId());	
		
		bean.table = super.getTable(bean, 
			UserBeanWrapper.getInstance(),
			new String[] {KEY_NAME,
				KEY_FULL_NAME, 
				KEY_USER_NATURE, 
				KEY_USER_POSITION,
				KEY_USER_DEPARTEMENT,
				KEY_USER_COMPANY,
				KEY_USER_ROOM_NO,
				KEY_USER_CITY,
				KEY_USER_STREET,
				KEY_USER_BUILDING,
				KEY_USER_EMAIL,
				KEY_USER_PHONE,
				KEY_USER_CELLULAR});
		
		bean.addPropertyChangeListener(this.listener);
		
		bean.setPropertyPanel(this.panel);
		
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

