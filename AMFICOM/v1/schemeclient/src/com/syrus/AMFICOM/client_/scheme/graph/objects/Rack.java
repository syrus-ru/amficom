/*-
 * $Id: Rack.java,v 1.2 2005/10/10 11:43:39 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph.objects;

import com.jgraph.graph.DefaultGraphCell;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.util.Log;

public class Rack extends DefaultGraphCell implements IdentifiableCell {
	private static final long serialVersionUID = -2595641930540047586L;
	private Identifier seId;

	public static Rack createInstance(Object userObject, Identifier elementId) {
		Rack cell = new Rack(userObject);
		cell.seId = elementId;
		return cell;
	}

	private Rack(Object userObject) {
		super(userObject);
	}

	public SchemeElement getSchemeElement() {
		try {
			return (SchemeElement)StorableObjectPool.getStorableObject(this.seId, true);
		} catch (ApplicationException e) {
			Log.errorException(e);
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
