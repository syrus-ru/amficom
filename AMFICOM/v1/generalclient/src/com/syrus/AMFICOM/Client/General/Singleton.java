package com.syrus.AMFICOM.Client.General;

import com.syrus.AMFICOM.Client.General.Log.LogInterface;

public class Singleton
{
	protected static Singleton singleton;

	static
	{
		if(singleton == null)
			singleton = new Singleton();
	}

	protected Singleton()
	{
	}

	public static LogInterface getLog()
	{
		return null;
	}

	public static void log(String s)
	{
		getLog().println(s);
	}

	public static void log(Exception exception)
	{
		getLog().println(exception);
	}

	public static Singleton singleton()
	{
		return singleton;
	}

	public static void setSingleton(Singleton singleton)
	{
		Singleton.singleton = singleton;
	}

	public static boolean singletonIsNull()
	{
		return singleton == null;
	}
}
