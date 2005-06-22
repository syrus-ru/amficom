/*
 * $Id: SchemeResource.java,v 1.3 2005/06/22 10:16:06 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph;

import java.util.*;

import com.syrus.AMFICOM.client_.scheme.graph.objects.*;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.resource.SchemeImageResource;
import com.syrus.AMFICOM.scheme.*;
import com.syrus.util.Log;

/**
 * 
 * @author $Author: stas $
 * @version $Revision: 1.3 $, $Date: 2005/06/22 10:16:06 $
 * @module schemeclient_v1
 */

public class SchemeResource {
	public static int SCHEME = 0;
	public static int SCHEME_ELEMENT = 1;
	public static int SCHEME_PROTO_ELEMENT = 2;
	
	private SchemeGraph graph;
	private SchemeCellContainer object;
	private int objectType;
	private SchemePath schemePath;

	public SchemeResource(SchemeGraph graph) {
		this.graph = graph;
	}

	public void setSchemePath(SchemePath path) {
		this.schemePath = path;
	}
	
	public SchemeCellContainer getCellContainer() {
		return this.object;
	}
	
	public int getCellContainerType() {
		return this.objectType;
	}

	public Scheme getScheme() {
		if (this.objectType == SCHEME)
			return (Scheme)this.object;
		return null;
	}
	
	public void setScheme(Scheme scheme) {
		this.object = scheme;
		this.objectType = SCHEME;
	}
	
	public SchemeElement getSchemeElement() {
		if (this.objectType == SCHEME_ELEMENT)
			return (SchemeElement)this.object;
		return null;
	}
	
	public SchemeProtoElement getSchemeProtoElement() {
		if (this.objectType == SCHEME_PROTO_ELEMENT)
			return (SchemeProtoElement)this.object;
		return null;
	}
	
	public void setSchemeElement(SchemeElement schemeElement) {
		this.object = schemeElement;
		this.objectType = SCHEME_ELEMENT;
	}
	
	public void setSchemeProtoElement(SchemeProtoElement schemeProtoElement) {
		this.object = schemeProtoElement;
		this.objectType = SCHEME_PROTO_ELEMENT;
	}
	
	public SchemePath getSchemePath() {
		return schemePath;
	}
	
	private void updateObject() {
		SchemeImageResource ir = object.getUgoCell();
		if (ir == null) {
			try {
				ir = SchemeImageResource.createInstance(LoginManager.getUserId());
				object.setUgoCell(ir);
			} catch (ApplicationException e) {
				Log.errorException(e);
				return;
			}
		}
		ir.setData((List)this.graph.getArchiveableState(this.graph.getRoots()));
	}
	
	public void updateScheme() {
		if (this.objectType == SCHEME) {
			updateObject();
		}
	}
	
	public void updateSchemeElement() {
		if (this.objectType == SCHEME_ELEMENT) {
			updateObject();
		}
	}
	
	public void updateSchemeProtoElement() {
		if (this.objectType == SCHEME_PROTO_ELEMENT) {
			updateObject();
		}
	}
	
	public Object[] getPathElements(SchemePath path) {
		Object[] cells = graph.getAll();
		ArrayList new_cells = new ArrayList();
		Set pes = path.getPathElements();
		ArrayList links = new ArrayList(pes.size());
		for (Iterator it = pes.iterator(); it.hasNext();)
			links.add(((PathElement)it.next()).getAbstractSchemeElement());

		for (int i = 0; i < cells.length; i++) {
			if (cells[i] instanceof DefaultCableLink) {
				DefaultCableLink cable = (DefaultCableLink) cells[i];
				if (links.contains(cable.getSchemeCableLink()))
					new_cells.add(cable);
			} else if (cells[i] instanceof DefaultLink) {
				DefaultLink link = (DefaultLink) cells[i];
				if (links.contains(link.getSchemeLink()))
					new_cells.add(link);
			} else if (cells[i] instanceof DeviceGroup) {
				DeviceGroup group = (DeviceGroup) cells[i];
				if (links.contains(group.getSchemeElement()))
					new_cells.add(group);
			}
		}
		return new_cells.toArray();
	}
}
