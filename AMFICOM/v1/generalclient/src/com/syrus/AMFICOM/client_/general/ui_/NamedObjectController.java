package com.syrus.AMFICOM.client_.general.ui_;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NamedObjectController implements ObjectResourceController 
{
	public static final String KEY_NAME = "name";

	private static NamedObjectController instance;

	private List keys;

	private NamedObjectController()
	{
		// empty private constructor
		String[] keysArray = new String[] { KEY_NAME};
	
		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
	}

	public static NamedObjectController getInstance() 
	{
		if (instance == null)
			instance = new NamedObjectController();
		return instance;
	}

	public List getKeys() 
	{
		return this.keys;
	}

	public String getName(final String key)
	{
		String name = null;
		if (key.equals(KEY_NAME))
			name = LangModel.getString("Name");
		return name;
	}

	public Object getValue(final Object object, final String key)
	{
		Object result = null;

		if (key.equals(KEY_NAME))
		{
			Class clazz = object.getClass();
			String methodName = "getName";
			String name = "";
			try
			{
				Method method = clazz.getMethod(methodName, new Class[0]);
				name = (String )(method.invoke(object, new Object[0]));
				result = name;
			}
			catch (InvocationTargetException iae)
			{
			}
			catch (IllegalAccessException iae)
			{
			}
			catch (NoSuchMethodException nsme)
			{
			}
			
			if(result == null)
			{
				methodName = "name";
				try
				{
					Method method = clazz.getMethod(methodName, new Class[0]);
					name = (String )(method.invoke(object, new Object[0]));
					result = name;
				}
				catch (InvocationTargetException iae)
				{
				}
				catch (IllegalAccessException iae)
				{
				}
				catch (NoSuchMethodException nsme)
				{
				}
			}
		}
		return result;
	}

	public boolean isEditable(final String key)
	{
		return false;
	}

	public void setValue(Object object, final String key, final Object value)
	{
	}

	public String getKey(final int index) 
	{
		return (String )this.keys.get(index);
	}

	public Object getPropertyValue(final String key) 
	{
		Object result = "";
		return result;
	}

	public void setPropertyValue(String key, Object objectKey, Object objectValue) 
	{
	}

	public Class getPropertyClass(String key) 
	{
		Class clazz = String.class;
		return clazz;
	}
}