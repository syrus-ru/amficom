/*
 * $Id: ObjectResourcePropertiesPane.java,v 1.2 2005/03/11 16:10:18 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.client_.general.ui_;

import javax.swing.JComponent;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;

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
 * @author $Author: stas $
 * @version $Revision: 1.2 $, $Date: 2005/03/11 16:10:18 $
 * @module generalclient_v1
 */
public interface ObjectResourcePropertiesPane {
	int DEF_HEIGHT = 24;

	int DEF_WIDTH = 150;
	
	Object getObject();

//	JComponent getGUI();
	
	void setObject(Object object);

	void setContext(ApplicationContext aContext);

	boolean modify();

	boolean create();

	boolean delete();

	boolean open();

	boolean save();

	boolean cancel();
}
