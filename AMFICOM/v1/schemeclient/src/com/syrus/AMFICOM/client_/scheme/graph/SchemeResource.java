/*
 * $Id: SchemeResource.java,v 1.1 2005/04/05 14:07:53 stas Exp $
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
 * @version $Revision: 1.1 $, $Date: 2005/04/05 14:07:53 $
 * @module schemeclient_v1
 */

public class SchemeResource {
	private SchemeGraph graph;
	private Scheme scheme;
	private SchemeElement schemeElement;
	private SchemeProtoElement schemeProtoElement;
	private SchemePath schemePath;

	public SchemeResource(SchemeGraph graph) {
		this.graph = graph;
	}

	public void setSchemePath(SchemePath path) {
		this.schemePath = path;
	}

	public Scheme getScheme() {
		return scheme;
	}
	
	public void setScheme(Scheme scheme) {
		this.scheme = scheme;
	}
	
	public SchemeElement getSchemeElement() {
		return schemeElement;
	}
	
	public SchemeProtoElement getSchemeProtoElement() {
		return schemeProtoElement;
	}
	
	public void setSchemeElement(SchemeElement schemeElement) {
		this.schemeElement = schemeElement;
	}
	
	public void setSchemeProtoElement(SchemeProtoElement schemeProtoElement) {
		this.schemeProtoElement = schemeProtoElement;
	}
	
	public SchemePath getSchemePath() {
		return schemePath;
	}
	
	public void updateScheme() {
		SchemeImageResource ir = this.scheme.getUgoCell();
		this.scheme.setUgoCell(updateElement(ir));
	}
	
	public void updateSchemeElement() {
		SchemeImageResource ir = this.schemeElement.getUgoCell();
		this.schemeElement.setUgoCell(updateElement(ir));
	}
	
	public void updateSchemeProtoElement() {
		SchemeImageResource ir = this.schemeProtoElement.getUgoCell();
		this.schemeProtoElement.setUgoCell(updateElement(ir));
	}
	
	private SchemeImageResource updateElement(SchemeImageResource ir) {
		if (ir == null) {
			try {
				ir = new SchemeImageResource(IdentifierPool.getGeneratedIdentifier(ObjectEntities.IMAGE_RESOURCE_ENTITY_CODE));
			} catch (ApplicationException e) {
				Log.errorException(e);
				return null;
			} 
		}
		ir.setData((List)this.graph.getArchiveableState(this.graph.getRoots()));
		return ir;
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
