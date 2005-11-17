/*-
 * $Id: UserBeanWrapper.java,v 1.1 2005/11/17 09:00:32 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.manager.beans;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.util.PropertyChangeException;
import com.syrus.util.Wrapper;



/**
 * @version $Revision: 1.1 $, $Date: 2005/11/17 09:00:32 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class UserBeanWrapper implements Wrapper<UserBean> {

	public static final String		NAME				= "name";
	public static final String		LOGIN				= "login";
	public static final String		FULL_NAME			= "fullName";
	public static final String		USER_POSITION		= "position";
	public static final String		USER_DEPARTEMENT	= "departement";
	public static final String		USER_COMPANY		= "company";
	public static final String		USER_ROOM_NO		= "roomNo";
	public static final String		USER_CITY			= "city";
	public static final String		USER_STREET			= "street";
	public static final String		USER_BUILDING		= "building";
	public static final String		USER_EMAIL			= "email";
	public static final String		USER_PHONE			= "phone";
	public static final String		USER_CELLULAR		= "cellular";

	private static UserBeanWrapper	instance;

	private List<String>					keys;

	public String getKey(int index) {
		return this.keys.get(index);
	}

	private UserBeanWrapper() {
		String[] keysArray = new String[] { NAME, 
				LOGIN,
				FULL_NAME, 
				USER_POSITION,
				USER_DEPARTEMENT,
				USER_COMPANY,
				USER_ROOM_NO,
				USER_CITY,
				USER_STREET,
				USER_BUILDING,
				USER_EMAIL,
				USER_PHONE,
				USER_CELLULAR};
		
		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}

	public static synchronized UserBeanWrapper getInstance() {
		if (instance == null) {
			instance = new UserBeanWrapper();
		}
		return instance;
	}

	public List<String> getKeys() {
		return this.keys;
	}

	public String getName(String key) {
		key = key.intern();
		if (key == NAME) {
			return I18N.getString("Manager.Entity.User.attributes.Name");
		}
		if (key == LOGIN) {
			return I18N.getString("Manager.Entity.User.attributes.Login");
		}
		if (key == FULL_NAME) {
			return I18N.getString("Manager.Entity.User.attributes.FullName");
		} else if (key == USER_POSITION) { 
			return I18N.getString("Manager.Entity.User.attributes.Position"); 
		} else if (key == USER_DEPARTEMENT) { 
			return I18N.getString("Manager.Entity.User.attributes.Departement"); 
		} else if (key == USER_COMPANY) { 
			return I18N.getString("Manager.Entity.User.attributes.Company"); 
		} else if (key == USER_ROOM_NO) { 
			return I18N.getString("Manager.Entity.User.attributes.RoomNo"); 
		} else if (key == USER_CITY) { 
			return I18N.getString("Manager.Entity.User.attributes.City"); 
		} else if (key == USER_STREET) { 
			return I18N.getString("Manager.Entity.User.attributes.Street");
		} else if (key == USER_BUILDING) { 
			return I18N.getString("Manager.Entity.User.attributes.Building");
		} else if (key == USER_EMAIL) { 
			return I18N.getString("Manager.Entity.User.attributes.EMail"); 
		} else if (key == USER_PHONE) { 
			return I18N.getString("Manager.Entity.User.attributes.Phone"); 
		} else if (key == USER_CELLULAR) { 
			return I18N.getString("Manager.Entity.User.attributes.Cellular"); 
		}
		return null;
	}

	public Class getPropertyClass(String key) {
		key = key.intern();
		if (key == NAME ||
				key == LOGIN || 
				key == FULL_NAME || 
				key == USER_POSITION ||
				key == USER_DEPARTEMENT ||
				key == USER_COMPANY ||
				key == USER_ROOM_NO ||
				key == USER_CITY ||
				key == USER_STREET ||
				key == USER_BUILDING ||
				key == USER_EMAIL ||
				key == USER_PHONE ||
				key == USER_CELLULAR) { 
			return String.class; 
		}
		
		return null;
	}

	public Object getPropertyValue(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getValue(	final UserBean userBean,
	                       	String key) {
		try {
			if (userBean != null) {
				key = key.intern();
				if (key == NAME) {
					return userBean.getName();
				} else if (key == LOGIN) {
					return userBean.getLogin();
				} else if (key == FULL_NAME) {
					return userBean.getFullName();
				} else if (key == USER_POSITION) { 
					return userBean.getPosition(); 
				} else if (key == USER_DEPARTEMENT) { 
					return userBean.getDepartement(); 
				} else if (key == USER_COMPANY) { 
					return userBean.getCompany(); 
				} else if (key == USER_ROOM_NO) { 
					return userBean.getRoomNo(); 
				} else if (key == USER_CITY) { 
					return userBean.getCity(); 
				} else if (key == USER_STREET) { 
					return userBean.getStreet(); 
				} else if (key == USER_BUILDING) { 
					return userBean.getBuilding(); 
				} else if (key == USER_EMAIL) { 
					return userBean.getEmail(); 
				} else if (key == USER_PHONE) { 
					return userBean.getPhone(); 
				} else if (key == USER_CELLULAR) { 
					return userBean.getCellular(); 
				}
			}
		} catch (final ApplicationException e) {
			throw new PropertyChangeException(e);
		}
		return null;
	}

	public boolean isEditable(final String key) {
		return true;
	} 

	public void setPropertyValue(	final String key,
	                             	final Object objectKey,
	                             	final Object objectValue) {
		// TODO Auto-generated method stub

	}

	public void setValue(	final UserBean userBean,
	                     	String key,
	                     	final Object value) {
		if (userBean != null) {
			try {
				key = key.intern();
				if (key == NAME) {
					userBean.setName((String) value);
				} else if (key == LOGIN) {
					userBean.setLogin((String) value);
				} else if (key == FULL_NAME) {
					userBean.setFullName((String) value);
				} else if (key == USER_POSITION) {
					userBean.setPosition((String) value);
				} else if (key == USER_DEPARTEMENT) { 
					userBean.setDepartement((String) value); 
				} else if (key == USER_COMPANY) { 
					userBean.setCompany((String) value); 
				} else if (key == USER_ROOM_NO) { 
					userBean.setRoomNo((String) value); 
				} else if (key == USER_CITY) { 
					userBean.setCity((String) value); 
				} else if (key == USER_STREET) { 
					userBean.setStreet((String) value); 
				} else if (key == USER_BUILDING) { 
					userBean.setBuilding((String) value); 
				} else if (key == USER_EMAIL) { 
					userBean.setEmail((String) value); 
				} else if (key == USER_PHONE) { 
					userBean.setPhone((String) value); 
				} else if (key == USER_CELLULAR) { 
					userBean.setCellular((String) value); 
				} 
			} catch (final ApplicationException e) {
				throw new PropertyChangeException(e);
			}
		}
	}

}
