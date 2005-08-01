
package com.syrus.AMFICOM.manager;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.util.Wrapper;

/*-
 * $Id: UserBeanWrapper.java,v 1.3 2005/08/01 11:32:03 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

/**
 * @version $Revision: 1.3 $, $Date: 2005/08/01 11:32:03 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class UserBeanWrapper implements Wrapper {

	public static final String		KEY_FULL_NAME			= "fullName";
	public static final String		KEY_USER_NATURE			= "nature";
	public static final String		KEY_USER_POSITION		= "position";
	public static final String		KEY_USER_DEPARTEMENT	= "departement";
	public static final String		KEY_USER_COMPANY		= "company";
	public static final String		KEY_USER_ROOM_NO		= "roomNo";
	public static final String		KEY_USER_CITY			= "city";
	public static final String		KEY_USER_STREET			= "street";
	public static final String		KEY_USER_BUILDING		= "building";
	public static final String		KEY_USER_EMAIL			= "email";
	public static final String		KEY_USER_PHONE			= "phone";
	public static final String		KEY_USER_CELLULAR		= "cellular";

	private static UserBeanWrapper	instance;

	private List<String>					keys;

	public String getKey(int index) {
		return this.keys.get(index);
	}

	private UserBeanWrapper() {
		// empty private constructor
		String[] keysArray = new String[] { KEY_FULL_NAME, 
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
				KEY_USER_CELLULAR};

		this.keys = Collections.unmodifiableList(Arrays.asList(keysArray));
	}

	public static UserBeanWrapper getInstance() {
		if (instance == null) {
			instance = new UserBeanWrapper();
		}
		return instance;
	}

	public List<String> getKeys() {
		return this.keys;
	}

	public String getName(final String key) {
		if (key.equals(KEY_FULL_NAME)) {
			return LangModelManager.getString("Entity.User.attributes.FullName");
		} else if (key.equals(KEY_USER_NATURE)) { 
			return LangModelManager.getString("Entity.User.attributes.Type"); 
		} else if (key.equals(KEY_USER_POSITION)) { 
			return LangModelManager.getString("Entity.User.attributes.Position"); 
		} else if (key.equals(KEY_USER_DEPARTEMENT)) { 
			return LangModelManager.getString("Entity.User.attributes.Departement"); 
		} else if (key.equals(KEY_USER_COMPANY)) { 
			return LangModelManager.getString("Entity.User.attributes.Company"); 
		} else if (key.equals(KEY_USER_ROOM_NO)) { 
			return LangModelManager.getString("Entity.User.attributes.RoomNo"); 
		} else if (key.equals(KEY_USER_CITY)) { 
			return LangModelManager.getString("Entity.User.attributes.City"); 
		} else if (key.equals(KEY_USER_STREET)) { 
			return LangModelManager.getString("Entity.User.attributes.Street");
		} else if (key.equals(KEY_USER_BUILDING)) { 
			return LangModelManager.getString("Entity.User.attributes.Building");
		} else if (key.equals(KEY_USER_EMAIL)) { 
			return LangModelManager.getString("Entity.User.attributes.EMail"); 
		} else if (key.equals(KEY_USER_PHONE)) { 
			return LangModelManager.getString("Entity.User.attributes.Phone"); 
		} else if (key.equals(KEY_USER_CELLULAR)) { 
			return LangModelManager.getString("Entity.User.attributes.Cellular"); 
		}
		
		return null;
	}

	public Class getPropertyClass(String key) {
		if (key.equals(KEY_FULL_NAME) || 
				key.equals(KEY_USER_NATURE) ||
				key.equals(KEY_USER_POSITION) ||
				key.equals(KEY_USER_DEPARTEMENT) ||
				key.equals(KEY_USER_COMPANY) ||
				key.equals(KEY_USER_ROOM_NO) ||
				key.equals(KEY_USER_CITY) ||
				key.equals(KEY_USER_STREET) ||
				key.equals(KEY_USER_BUILDING) ||
				key.equals(KEY_USER_EMAIL) ||
				key.equals(KEY_USER_PHONE) ||
				key.equals(KEY_USER_CELLULAR)) { 
			return String.class; 
		}
		return null;
	}

	public Object getPropertyValue(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getValue(	Object object,
							String key) {
		if (object instanceof UserBean) {
			UserBean userBean = (UserBean) object;
			if (key.equals(KEY_FULL_NAME)) {
				return userBean.getFullName();
			} else if (key.equals(KEY_USER_NATURE)) { 
				return userBean.getNature(); 
			} else if (key.equals(KEY_USER_POSITION)) { 
				return userBean.getPosition(); 
			} else if (key.equals(KEY_USER_DEPARTEMENT)) { 
				return userBean.getDepartement(); 
			} else if (key.equals(KEY_USER_COMPANY)) { 
				return userBean.getCompany(); 
			} else if (key.equals(KEY_USER_ROOM_NO)) { 
				return userBean.getRoomNo(); 
			} else if (key.equals(KEY_USER_CITY)) { 
				return userBean.getCity(); 
			} else if (key.equals(KEY_USER_STREET)) { 
				return userBean.getStreet(); 
			} else if (key.equals(KEY_USER_BUILDING)) { 
				return userBean.getBuilding(); 
			} else if (key.equals(KEY_USER_EMAIL)) { 
				return userBean.getEmail(); 
			} else if (key.equals(KEY_USER_PHONE)) { 
				return userBean.getPhone(); 
			} else if (key.equals(KEY_USER_CELLULAR)) { 
				return userBean.getCellular(); 
			}
		}
		return null;
	}

	public boolean isEditable(String key) {
		return !key.equals(KEY_USER_NATURE);
	}

	public void setPropertyValue(	String key,
									Object objectKey,
									Object objectValue) {
		// TODO Auto-generated method stub

	}

	public void setValue(	Object object,
							String key,
							Object value) {
		if (object instanceof UserBean) {
			UserBean userBean = (UserBean) object;
			if (key.equals(KEY_FULL_NAME)) {
				userBean.setFullName((String) value);
			} else if (key.equals(KEY_USER_NATURE)) {
				userBean.setNature((String) value);
			} else if (key.equals(KEY_USER_POSITION)) {
				userBean.setPosition((String) value);
			} else if (key.equals(KEY_USER_DEPARTEMENT)) { 
				userBean.setDepartement((String) value); 
			} else if (key.equals(KEY_USER_COMPANY)) { 
				userBean.setCompany((String) value); 
			} else if (key.equals(KEY_USER_ROOM_NO)) { 
				userBean.setRoomNo((String) value); 
			} else if (key.equals(KEY_USER_CITY)) { 
				userBean.setCity((String) value); 
			} else if (key.equals(KEY_USER_STREET)) { 
				userBean.setStreet((String) value); 
			} else if (key.equals(KEY_USER_BUILDING)) { 
				userBean.setBuilding((String) value); 
			} else if (key.equals(KEY_USER_EMAIL)) { 
				userBean.setEmail((String) value); 
			} else if (key.equals(KEY_USER_PHONE)) { 
				userBean.setPhone((String) value); 
			} else if (key.equals(KEY_USER_CELLULAR)) { 
				userBean.setCellular((String) value); 
			}
		}
	}

}
