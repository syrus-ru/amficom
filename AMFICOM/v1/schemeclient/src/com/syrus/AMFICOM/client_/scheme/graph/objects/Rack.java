/*-
 * $Id: Rack.java,v 1.6 2005/10/31 12:30:29 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph.objects;

import java.util.Map;

import com.jgraph.graph.DefaultGraphCell;
import com.jgraph.graph.GraphConstants;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.util.Log;

public class Rack extends DefaultGraphCell implements IdentifiableCell {
	private static final long serialVersionUID = -2595641930540047586L;
	private Identifier seId;

	public static Rack createInstance(Object userObject, 
			Map<Object, Map> viewMap, Identifier elementId) {
		Rack cell = new Rack(userObject, viewMap);
		cell.seId = elementId;
		return cell;
	}

	private Rack(Object userObject, Map<Object, Map> viewMap) {
		super(userObject);
		
		// make group created unresizable
		Map m = GraphConstants.createMap();
		GraphConstants.setSizeable(m, false);
		viewMap.put(this, m);
	}

	public SchemeElement getSchemeElement() {
		try {
			return (SchemeElement)StorableObjectPool.getStorableObject(this.seId, true);
		} catch (ApplicationException e) {
			Log.errorMessage(e);
		}
		return null;
	}

	public void setSchemeElementId(Identifier id) {
		assert id != null;
		this.seId = id;
	}

	public void setId(Identifier id) {
		assert id != null;
		this.seId = id;
	}

	public Identifier getId() {
		return this.seId;
	}
}
