package com.syrus.AMFICOM.client_.general.ui_;
import java.lang.reflect.InvocationTargetException;

import com.syrus.AMFICOM.Client.General.Filter.ObjectResourceFilter;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;

public class DefaultCatalogController implements ObjectResourceCatalogController
{
	public DefaultCatalogController()
	{
	}

	public ObjectResourcePropertiesPane getPropertiesPane(Class orclass)
	{
		ObjectResourcePropertiesPane pane = new GeneralPanel();
		try
		{
			final String methodName1 = "getPropertyPaneClassName";
			try
			{
				Class clazz = Class.forName((String) (orclass.getMethod(methodName1, new Class[0]).invoke(orclass, new Object[0])));
				final String methodName2 = "getInstance";
				try
				{
					pane = (ObjectResourcePropertiesPane) (clazz.getMethod(methodName2, new Class[0]).invoke(clazz, new Object[0]));
				}
				catch (NoSuchMethodException nsme)
				{
					if (true)
						System.err.println("WARNING: " + clazz.getName() + '.' + methodName2 + "() not found.");
				}
			}
			catch (NoSuchMethodException nsme)
			{
				if (true)
					System.err.println("WARNING: " + orclass.getName() + '.' + methodName1 + "() not found.");
			}
		}
		catch (InvocationTargetException ite)
		{
			ite.printStackTrace();
			ite.getTargetException().printStackTrace();
		}
		catch (Throwable t)
		{
			t.printStackTrace();
		}
		return pane;
	}

	public ObjectResourceController getController(Class clazz)
	{
		return null;
	}

	public ObjectResourceFilter getFilter(Class orclass)
	{
		ObjectResourceFilter fil = null;
		try
		{
			java.lang.reflect.Method filMethod = orclass.getMethod("getFilter", new Class [0]);
			fil = (ObjectResourceFilter)filMethod.invoke(orclass, new Object[0]);
		}
		catch(IllegalAccessException iae)
		{
			System.out.println("getFilter method not found in class " + orclass.getName());
			fil = null;
		}
		catch(Exception e)
		{
			System.out.println("Метод getFilter не определен для " + orclass.getName());
			fil = null;
		}
		return fil;
	}
}