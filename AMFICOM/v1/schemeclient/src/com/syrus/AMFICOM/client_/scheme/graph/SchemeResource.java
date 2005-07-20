/*
 * $Id: SchemeResource.java,v 1.6 2005/07/20 15:01:32 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.syrus.AMFICOM.client_.scheme.SchemeObjectsFactory;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DefaultCableLink;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DefaultLink;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DeviceGroup;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.resource.SchemeImageResource;
import com.syrus.AMFICOM.scheme.PathElement;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeCellContainer;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemePath;
import com.syrus.AMFICOM.scheme.SchemeProtoElement;
import com.syrus.util.Log;

/**
 * 
 * @author $Author: bass $
 * @version $Revision: 1.6 $, $Date: 2005/07/20 15:01:32 $
 * @module schemeclient_v1
 */

public class SchemeResource {
	public static final int NON_INITIALIZED = 0;
	public static final int SCHEME = 1;
	public static final int SCHEME_ELEMENT = 2;
	public static final int SCHEME_PROTO_ELEMENT = 3;
	
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
	
	public void setCellContainerType(int cellContainerType) {
		this.objectType = cellContainerType;
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
				ir = SchemeObjectsFactory.createSchemeImageResource();
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
		Set pes = path.getPathMembers();
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
