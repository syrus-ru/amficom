package com.syrus.AMFICOM.Client.Map.UI;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.PhysicalLinkType;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class SimpleMapElementController implements ObjectResourceController 
{

	public static final String KEY_NAME = "name";
	public static final String KEY_TYPE = "type";

	private static SimpleMapElementController instance;

	private List keys;

	private SimpleMapElementController() 
	{
		// empty private constructor
		String[] keysArray = new String[] { KEY_NAME, KEY_TYPE};
	
		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
	}

	public static SimpleMapElementController getInstance() 
	{
		if (instance == null)
			instance = new SimpleMapElementController();
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
		else
		if (key.equals(KEY_TYPE))
			name = LangModel.getString("Type");
		return name;
	}

	public Object getValue(final Object object, final String key)
	{
		Object result = null;

		if(object == null)
		{
			result = null;
		}
		else
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
				result = null;
			}
			catch (IllegalAccessException iae)
			{
				result = null;
			}
			catch (NoSuchMethodException nsme)
			{
				result = null;
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
					result = null;
				}
				catch (IllegalAccessException iae)
				{
					result = null;
				}
				catch (NoSuchMethodException nsme)
				{
					result = null;
				}
			}
		}
		else
		if (object instanceof MapElement) 
		{
			MapElement me = (MapElement)object;
			if (key.equals(KEY_TYPE))
			{
				if(me instanceof SiteNode)
				{
					SiteNodeType mnpe = (SiteNodeType )(((SiteNode)me).getType());
					result = mnpe.getName();
				}
				else
				if(me instanceof PhysicalLink)
				{
					PhysicalLinkType mlpe = (PhysicalLinkType )(((PhysicalLink)me).getType());
					result = mlpe.getName();
				}
//				else
//					result = 
//						LangModel.getString("node" + ((ObjectResource )object).getTyp());
			}
		}
		else
		{
			result = null;
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
