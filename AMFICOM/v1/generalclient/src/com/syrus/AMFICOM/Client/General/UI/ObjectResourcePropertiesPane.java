/*
 * $Id: ObjectResourcePropertiesPane.java,v 1.4 2005/05/13 19:05:46 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.Client.General.UI;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.corba.portable.reflect.common.ObjectResource;
import com.syrus.AMFICOM.general.StorableObject;

/**
 * If a class <code style = "background-color: #fff7e9;">T</code> realizes
 * <code style = "background-color: #fff7e9;">ObjectResourcePropertiesPane</code>
 * interface, it must: <ul><li>implement method
 * <code style = "background-color: #fff7e9;"><b>public static</b> T getInstance()</code>,
 * and</li><li>keep its construcrors
 * <code style = "background-color: #fff7e9;"><b>private</b></code> (or <em>at least</em>
 * <code style = "background-color: #fff7e9;"><b>protected</b></code> if the class is not
 * <code style = "background-color: #fff7e9;"><b>final</b></code>).</li></ul>
 *
 * @author $Author: bass $
 * @version $Revision: 1.4 $, $Date: 2005/05/13 19:05:46 $
 * @module generalclient_v1
 */
public interface ObjectResourcePropertiesPane
{
	int DEF_HEIGHT = 24;

	int DEF_WIDTH = 150;

	StorableObject getObjectResource();

	void setObjectResource(ObjectResource objectResource);

	void setContext(ApplicationContext aContext);

	boolean modify();

	boolean create();

	boolean delete();

	boolean open();

	boolean save();

	boolean cancel();
}
