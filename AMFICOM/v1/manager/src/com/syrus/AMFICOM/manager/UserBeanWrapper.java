/*-
 * $Id: UserBeanWrapper.java,v 1.11 2005/10/11 15:34:53 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.manager;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.util.Wrapper;



/**
 * @version $Revision: 1.11 $, $Date: 2005/10/11 15:34:53 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class UserBeanWrapper implements Wrapper {

	public static final String		NAME				= "name";
	public static final String		LOGIN				= "login";
	public static final String		FULL_NAME			= "fullName";
	public static final String		USER_NATURE			= "nature";
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

	public String getName(final String key) {
		if (key.equals(NAME)) {
			return I18N.getString("Manager.Entity.User.attributes.Name");
		}
		if (key.equals(LOGIN)) {
			return I18N.getString("Manager.Entity.User.attributes.Login");
		}
		if (key.equals(FULL_NAME)) {
			return I18N.getString("Manager.Entity.User.attributes.FullName");
		} else if (key.equals(USER_NATURE)) { 
			return I18N.getString("Manager.Entity.User.attributes.Type"); 
		} else if (key.equals(USER_POSITION)) { 
			return I18N.getString("Manager.Entity.User.attributes.Position"); 
		} else if (key.equals(USER_DEPARTEMENT)) { 
			return I18N.getString("Manager.Entity.User.attributes.Departement"); 
		} else if (key.equals(USER_COMPANY)) { 
			return I18N.getString("Manager.Entity.User.attributes.Company"); 
		} else if (key.equals(USER_ROOM_NO)) { 
			return I18N.getString("Manager.Entity.User.attributes.RoomNo"); 
		} else if (key.equals(USER_CITY)) { 
			return I18N.getString("Manager.Entity.User.attributes.City"); 
		} else if (key.equals(USER_STREET)) { 
			return I18N.getString("Manager.Entity.User.attributes.Street");
		} else if (key.equals(USER_BUILDING)) { 
			return I18N.getString("Manager.Entity.User.attributes.Building");
		} else if (key.equals(USER_EMAIL)) { 
			return I18N.getString("Manager.Entity.User.attributes.EMail"); 
		} else if (key.equals(USER_PHONE)) { 
			return I18N.getString("Manager.Entity.User.attributes.Phone"); 
		} else if (key.equals(USER_CELLULAR)) { 
			return I18N.getString("Manager.Entity.User.attributes.Cellular"); 
		}
		return null;
	}

	public Class getPropertyClass(final String key) {
		if (key.equals(NAME) ||
				key.equals(LOGIN) || 
				key.equals(FULL_NAME) || 
				key.equals(USER_NATURE) ||
				key.equals(USER_POSITION) ||
				key.equals(USER_DEPARTEMENT) ||
				key.equals(USER_COMPANY) ||
				key.equals(USER_ROOM_NO) ||
				key.equals(USER_CITY) ||
				key.equals(USER_STREET) ||
				key.equals(USER_BUILDING) ||
				key.equals(USER_EMAIL) ||
				key.equals(USER_PHONE) ||
				key.equals(USER_CELLULAR)) { 
			return String.class; 
		}
		
		return null;
	}

	public Object getPropertyValue(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getValue(	final Object object,
	                       	final String key) {
		try {
			if (object instanceof UserBean) {
				UserBean userBean = (UserBean) object;
				if (key.equals(NAME)) {
					return userBean.getName();
				} else if (key.equals(LOGIN)) {
					return userBean.getLogin();
				} else if (key.equals(FULL_NAME)) {
					return userBean.getFullName();
				} else if (key.equals(USER_NATURE)) { 
					return userBean.getNature(); 
				} else if (key.equals(USER_POSITION)) { 
					return userBean.getPosition(); 
				} else if (key.equals(USER_DEPARTEMENT)) { 
					return userBean.getDepartement(); 
				} else if (key.equals(USER_COMPANY)) { 
					return userBean.getCompany(); 
				} else if (key.equals(USER_ROOM_NO)) { 
					return userBean.getRoomNo(); 
				} else if (key.equals(USER_CITY)) { 
					return userBean.getCity(); 
				} else if (key.equals(USER_STREET)) { 
					return userBean.getStreet(); 
				} else if (key.equals(USER_BUILDING)) { 
					return userBean.getBuilding(); 
				} else if (key.equals(USER_EMAIL)) { 
					return userBean.getEmail(); 
				} else if (key.equals(USER_PHONE)) { 
					return userBean.getPhone(); 
				} else if (key.equals(USER_CELLULAR)) { 
					return userBean.getCellular(); 
				}

			}
		} catch (ApplicationException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, 
				e.getMessage(), 
				I18N.getString("Manager.Error"),
				JOptionPane.ERROR_MESSAGE);
		}
		return null;
	}

	public boolean isEditable(final String key) {
		return !key.equals(USER_NATURE);
	} 

	public void setPropertyValue(	final String key,
	                             	final Object objectKey,
	                             	final Object objectValue) {
		// TODO Auto-generated method stub

	}

	public void setValue(	final Object object,
	                     	final String key,
	                     	final Object value) {
		if (object instanceof UserBean) {
			try {
				UserBean userBean = (UserBean) object;
				if (key.equals(NAME)) {
					userBean.setName((String) value);
				} else if (key.equals(LOGIN)) {
					userBean.setLogin((String) value);
				} else if (key.equals(FULL_NAME)) {
					userBean.setFullName((String) value);
				} else if (key.equals(USER_NATURE)) {
					userBean.setNature((String) value);
				} else if (key.equals(USER_POSITION)) {
					userBean.setPosition((String) value);
				} else if (key.equals(USER_DEPARTEMENT)) { 
					userBean.setDepartement((String) value); 
				} else if (key.equals(USER_COMPANY)) { 
					userBean.setCompany((String) value); 
				} else if (key.equals(USER_ROOM_NO)) { 
					userBean.setRoomNo((String) value); 
				} else if (key.equals(USER_CITY)) { 
					userBean.setCity((String) value); 
				} else if (key.equals(USER_STREET)) { 
					userBean.setStreet((String) value); 
				} else if (key.equals(USER_BUILDING)) { 
					userBean.setBuilding((String) value); 
				} else if (key.equals(USER_EMAIL)) { 
					userBean.setEmail((String) value); 
				} else if (key.equals(USER_PHONE)) { 
					userBean.setPhone((String) value); 
				} else if (key.equals(USER_CELLULAR)) { 
					userBean.setCellular((String) value); 
				} 
			} catch (ApplicationException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, 
					e.getMessage(), 
					I18N.getString("Manager.Error"),
					JOptionPane.ERROR_MESSAGE);
			}
		}
	}

}
