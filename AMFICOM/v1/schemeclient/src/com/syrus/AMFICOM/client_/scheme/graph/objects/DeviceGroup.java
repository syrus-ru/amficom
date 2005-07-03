/*
 * $Id: DeviceGroup.java,v 1.1 2005/04/05 14:07:54 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph.objects;

import java.util.Map;

import com.jgraph.graph.*;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.scheme.*;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/04/05 14:07:54 $
 * @module schemeclient_v1
 */

public class DeviceGroup extends DefaultGraphCell {
	private Identifier protoElementId;
	private Identifier schemeElementId;

	public static DeviceGroup createInstance(Object userObject,
			Map viewMap, SchemeElement element) {
		DeviceGroup cell = new DeviceGroup(userObject, viewMap);
		cell.schemeElementId = element.getId();
		return cell;
	}

	public static DeviceGroup createInstance(Object userObject,
			Map viewMap, SchemeProtoElement proto) {
		DeviceGroup cell = new DeviceGroup(userObject, viewMap);
		cell.protoElementId = proto.getId();
		return cell;
	}

	private DeviceGroup(Object userObject, Map viewMap) {
		super(userObject);
		
		// make group created unresizable
		Map m = GraphConstants.createMap();
		GraphConstants.setSizeable(m, false);
		viewMap.put(this, m);
	}

	public SchemeProtoElement getProtoElement() {
		if (protoElementId != null) {
			try {
				return (SchemeProtoElement) SchemeStorableObjectPool.getStorableObject(protoElementId, true);
			} catch (ApplicationException e) {
				Log.errorException(e);
			}
		}
		return null;
	}

	public Identifier getProtoElementId() {
		return protoElementId;
	}

	public void setProtoElementId(Identifier id) {
		protoElementId = id;
	}

	public SchemeElement getSchemeElement() {
		if (schemeElementId != null) {
			try {
				return (SchemeElement) SchemeStorableObjectPool.getStorableObject(schemeElementId, true);
			} catch (ApplicationException e) {
				Log.errorException(e);
			}
		}
		return null;
	}

	public Scheme getScheme() {
		SchemeElement element = getSchemeElement();
		if (element != null)
			return element.getScheme();
		return null;
	}

	public Identifier getSchemeElementId() {
		return schemeElementId;
	}

	public void setSchemeElementId(Identifier id) {
		schemeElementId = id;
	}
}
