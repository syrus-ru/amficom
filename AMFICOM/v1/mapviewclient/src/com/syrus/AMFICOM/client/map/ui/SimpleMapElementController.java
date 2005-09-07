package com.syrus.AMFICOM.client.map.ui;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.PhysicalLinkType;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.util.Wrapper;

public final class SimpleMapElementController implements Wrapper 
{

	public static final String KEY_NAME = "name";
	public static final String KEY_TYPE = "type";

	private static SimpleMapElementController instance;

	private List keys;
	private String[] keysArray;

	private SimpleMapElementController() {
		// empty private constructor
		this.keysArray = new String[] { KEY_NAME, KEY_TYPE};
	
		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(this.keysArray)));
	}

	public static SimpleMapElementController getInstance() {
		if(instance == null)
			instance = new SimpleMapElementController();
		return instance;
	}

	public List getKeys() {
		return this.keys;
	}

	public String[] getKeysArray() {
		return this.keysArray;
	}

	public String getName(final String key) {
		String name = null;
		if (key.equals(KEY_NAME))
			name = LangModelGeneral.getString("Name");
		else
		if (key.equals(KEY_TYPE))
			name = LangModelGeneral.getString("Type");
		return name;
	}

	public Object getValue(final Object object, final String key) {
		Object result = null;

		if(object == null) {
			result = " ";
		}
		else if(key.equals(KEY_NAME)) {
			Class clazz = object.getClass();
			String methodName = "getName";
			String name = "";
			try {
				Method method = clazz.getMethod(methodName, new Class[0]);
				name = (String )(method.invoke(object, new Object[0]));
				result = name;
			} catch(InvocationTargetException iae) {
				result = " ";
			} catch(IllegalAccessException iae) {
				result = " ";
			} catch(NoSuchMethodException nsme) {
				result = " ";
			}

			if(result == null) {
				methodName = "name";
				try {
					Method method = clazz.getMethod(
							methodName,
							new Class[0]);
					name = (String )(method.invoke(object, new Object[0]));
					result = name;
				} catch(InvocationTargetException iae) {
					result = " ";
				} catch(IllegalAccessException iae) {
					result = " ";
				} catch(NoSuchMethodException nsme) {
					result = " ";
				}
			}
		}
		else if(object instanceof MapElement) {
			MapElement me = (MapElement )object;
			if(key.equals(KEY_TYPE)) {
				if(me instanceof SiteNode) {
					SiteNodeType siteNodeType = (((SiteNode)me).getType());
					result = siteNodeType.getName();
				}
				else if(me instanceof PhysicalLink) {
					PhysicalLinkType physicalLinkType = (((PhysicalLink)me).getType());
					result = physicalLinkType.getName();
				}
			}
		}
		else {
			result = " ";
		}
		return result + " ";
	}

	public boolean isEditable(final String key) {
		return false;
	}

	public void setValue(Object object, final String key, final Object value) {
		//empty
	}

	public String getKey(final int index) {
		return (String )this.keys.get(index);
	}

	public Object getPropertyValue(final String key) {
		Object result = "";
		return result;
	}

	public void setPropertyValue(
			String key,
			Object objectKey,
			Object objectValue) {
		//empty
	}

	public Class getPropertyClass(String key) {
		Class clazz = String.class;
		return clazz;
	}
}
