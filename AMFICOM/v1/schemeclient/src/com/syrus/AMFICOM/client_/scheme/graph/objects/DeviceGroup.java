/*
 * $Id: DeviceGroup.java,v 1.16 2005/10/30 14:49:21 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph.objects;

import static java.util.logging.Level.SEVERE;

import java.util.Map;

import com.jgraph.graph.DefaultGraphCell;
import com.jgraph.graph.GraphConstants;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemeProtoElement;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeElementPackage.IdlSchemeElementKind;
import com.syrus.util.Log;

/**
 * @author $Author: bass $
 * @version $Revision: 1.16 $, $Date: 2005/10/30 14:49:21 $
 * @module schemeclient
 */

public class DeviceGroup extends DefaultGraphCell implements IdentifiableCell {
	private static final long serialVersionUID = 3256445819543369017L;

	public static final int PROTO_ELEMENT = 0;
	public static final int SCHEME_ELEMENT = 1;
	public static final int SCHEME = 2;
	
	private Identifier elementId;
	private int type;

	public static DeviceGroup createInstance(Object userObject,
			Map<Object, Map> viewMap, Identifier elementId, int type) {
		DeviceGroup cell = new DeviceGroup(userObject, viewMap);
		cell.elementId = elementId;
		cell.type = type;
		return cell;
	}

	private DeviceGroup(Object userObject, Map<Object, Map> viewMap) {
		super(userObject);
		
		// make group created unresizable
		Map m = GraphConstants.createMap();
		GraphConstants.setSizeable(m, false);
		viewMap.put(this, m);
	}

	public int getType() {
		return this.type;
	}
	
	public SchemeProtoElement getProtoElement() {
		if (this.type == PROTO_ELEMENT) {
			try {
				return (SchemeProtoElement) StorableObjectPool.getStorableObject(this.elementId, true);
			} catch (ApplicationException e) {
				Log.errorMessage(e);
			}
		}
		return null;
	}

	public Identifier getElementId() {
		return this.elementId;
	}

	public void setProtoElementId(Identifier id) {
		assert id != null;
		this.elementId = id;
		this.type = PROTO_ELEMENT;
	}

	public SchemeElement getSchemeElement() {
		if (this.type == SCHEME_ELEMENT) {
			try {
				return (SchemeElement)StorableObjectPool.getStorableObject(this.elementId, true);
			} catch (ApplicationException e) {
				Log.errorMessage(e);
			}
		}
		return null;
	}

	public Scheme getScheme() {
		try {
			if (this.type == SCHEME) {
				return StorableObjectPool.getStorableObject(this.elementId, true);
			} else if (this.type == SCHEME_ELEMENT) { 
				SchemeElement element = getSchemeElement();
				if (element != null && element.getKind() == IdlSchemeElementKind.SCHEME_CONTAINER) {
					return element.getScheme(false);
				}
			}
		} catch (final ApplicationException ae) {
			Log.debugMessage(ae, SEVERE);
		}
		return null;
	}

	public void setSchemeElementId(Identifier id) {
		assert id != null;
		this.elementId = id;
		this.type = SCHEME_ELEMENT;
	}

	public void setId(Identifier id) {
		assert id != null;
		this.elementId = id;
	}

	public Identifier getId() {
		return this.elementId;
	}
}
