/*
 * $Id: Singleton.java,v 1.3 2004/09/27 09:53:52 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.Client.General;

import com.syrus.AMFICOM.Client.General.Log.LogInterface;

/**
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2004/09/27 09:53:52 $
 * @module generalclient_v1
 * @deprecated
 */
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
