/*
 * $Id: ImageCatalogue.java,v 1.5 2004/09/27 16:06:49 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.Client.Resource;

import com.syrus.AMFICOM.Client.General.Model.ApplicationModel;
import com.syrus.AMFICOM.Client.General.SessionInterface;
import java.util.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.5 $, $Date: 2004/09/27 16:06:49 $
 * @module generalclient_v1
 */
public final class ImageCatalogue {
	private static final Hashtable HASHTABLE = new Hashtable();

	static {
		add("pc", new ImageResource("pc", "pc", "images/pc.gif"));
		add("net", new ImageResource("net", "net", "images/net.gif"));
	}

	private ImageCatalogue() {
	}
	
	public static ImageResource get(String name) {
		ImageResource ir = null;
		try {
			ir = (ImageResource) HASHTABLE.get(name);
		} catch (Exception e) {
			ir = null;
		}
		if (ir == null)
			ir = load(name);
		return ir;
	}

	private static ImageResource load(String name) {
		ImageResource ir = null;
		new DataSourceImage(ApplicationModel.getInstance().getDataSource(SessionInterface.getActiveSession())).LoadImages(new String[]{name});
		try {
			ir = (ImageResource) HASHTABLE.get(name);
		} catch(Exception e) {
			ir = null;
		}

		if (ir == null) {
			if (name.equals("pc"))
				ir = null;
			else if (name.equals("net"))
				ir = get("pc");
			else
				ir = get("net");
		}
		return ir;
	}

	public static void add(String name, ImageResource ir) {
		HASHTABLE.put(name, ir);
	}

	public static void remove(String name) {
		try {
			((ImageResource) HASHTABLE.get(name)).free();
			HASHTABLE.remove(name);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Enumeration getAll() {
		return HASHTABLE.elements();
	}

	public static void removeAll() {
		HASHTABLE.clear();
	}

	public static Hashtable getHash() {
		return HASHTABLE;
	}
}
