/*
 * $Id: SchemeResource.java,v 1.20 2006/02/15 12:18:10 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;

import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client_.scheme.SchemeObjectsFactory;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DefaultCableLink;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DefaultLink;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DeviceGroup;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectPool;
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
 * @author $Author: stas $
 * @version $Revision: 1.20 $, $Date: 2006/02/15 12:18:10 $
 * @module schemeclient
 */

public class SchemeResource {
	public static final int NON_INITIALIZED = 0;
	public static final int SCHEME = 1;
	public static final int SCHEME_ELEMENT = 2;
	public static final int SCHEME_PROTO_ELEMENT = 3;
	
	private SchemeGraph graph;
//	private SchemeCellContainer object;
	private Identifier objectId;
	private int objectType;
	
	private static SchemePath schemePath;
	private static boolean isEditing = false;
	private static ApplicationContext aContext = null;
	/**
	 * Set of AbstractSchemeElements Identifiers
	 */
//	private static SortedSet<Identifier> pmIds;
//	private static Identifier pmStartId;
	private static Identifier pmEndId;

	public SchemeResource(SchemeGraph graph) {
		this.graph = graph;
		aContext = graph.aContext;
	}

	public static void setSchemePath(SchemePath path, boolean forEdit) {
		schemePath = path;
		isEditing = forEdit;
		
		if (aContext != null) {
			ApplicationModel aModel = aContext.getApplicationModel();
			if (path == null) {
				aModel.setEnabled("menuPathNew", true);
				aModel.setEnabled("menuPathEdit", false);
				aModel.setEnabled("menuPathSave", false);
				aModel.setEnabled("menuPathCancel", false);
			} else if (forEdit) {
				aModel.setEnabled("menuPathNew", false);
				aModel.setEnabled("menuPathEdit", false);
				aModel.setEnabled("menuPathSave", true);
				aModel.setEnabled("menuPathCancel", true);
			} else {
				aModel.setEnabled("menuPathNew", true);
				aModel.setEnabled("menuPathEdit", true);
				aModel.setEnabled("menuPathSave", false);
				aModel.setEnabled("menuPathCancel", false);
			}
			aModel.fireModelChanged();
		}
	}
	
//	public static void setCashedPathStart(Identifier pathMemberId) {
//		pmStartId = pathMemberId;
//	}
	
	public static void setCashedPathEnd(Identifier pathMemberId) {
		pmEndId = pathMemberId;
	}
//	
//	public static Identifier getCashedPathStart() {
//		return pmStartId;
//	}
	
	public static Identifier getCashedPathEnd() {
		return pmEndId;
	}
	
	/**
	 * @param pathMemberIds Set of AbstractSchemeElements Identifiers
	 */
//	public static void setCashedPathMemberIds(SortedSet<Identifier> pathMemberIds) {
//		pmIds = pathMemberIds;
//	}
	
	/**
	 * @return Set of AbstractSchemeElements Identifiers
	 * @throws ApplicationException 
	 */
//	public static SortedSet<Identifier> getCashedPathElementIds() {
//		return pmIds;
//	}
	
	public Identifier getCellContainerId() {
		return this.objectId;
	}
	
	public SchemeCellContainer getCellContainer() throws ApplicationException {
		if (this.objectType == SCHEME) {
			return getScheme();
		}
		if (this.objectType == SCHEME_ELEMENT) {
			return getSchemeElement();
		}
		if (this.objectType == SCHEME_PROTO_ELEMENT) {
			return getSchemeProtoElement();
		}
		Log.debugMessage("SchemeResource not initialyzed yet", Level.FINEST);
		return null;
	}
	
	public int getCellContainerType() {
		return this.objectType;
	}
	
	public void setCellContainerType(int cellContainerType) {
		this.objectType = cellContainerType;
	}

	public Scheme getScheme() throws ApplicationException {
		if (this.objectType == SCHEME) {
			return StorableObjectPool.getStorableObject(this.objectId, false);
		}
		return null;
	}
	
	public void setScheme(Scheme scheme) {
		this.objectId = scheme.getId();
		this.objectType = SCHEME;
	}
	
	public SchemeElement getSchemeElement() throws ApplicationException {
		if (this.objectType == SCHEME_ELEMENT) {
			return StorableObjectPool.getStorableObject(this.objectId, false);
		}
		return null;
	}
	
	public SchemeProtoElement getSchemeProtoElement() throws ApplicationException {
		if (this.objectType == SCHEME_PROTO_ELEMENT) {
			return StorableObjectPool.getStorableObject(this.objectId, false);
		}
		return null;
	}
	
	public void setSchemeElement(SchemeElement schemeElement) {
		this.objectId = schemeElement.getId();
		this.objectType = SCHEME_ELEMENT;
	}
	
	public void setSchemeProtoElement(SchemeProtoElement schemeProtoElement) {
		this.objectId = Identifier.possiblyVoid(schemeProtoElement);
		this.objectType = SCHEME_PROTO_ELEMENT;
	}
	
	public static SchemePath getSchemePath() {
		return schemePath;
	}
	
	public static boolean isPathEditing() {
		return isEditing;
	}
	
	private void updateObject() throws ApplicationException {
		SchemeCellContainer cellContainer = this.getCellContainer();
		SchemeImageResource ir = cellContainer.getUgoCell();
		if (ir == null) {
			try {
				ir = SchemeObjectsFactory.createSchemeImageResource();
				cellContainer.setUgoCell(ir);
			} catch (ApplicationException e) {
				Log.errorMessage(e);
				return;
			}
		}
		ir.setData((List)this.graph.getArchiveableState(this.graph.getRoots()));
	}
	
	public void updateScheme() throws ApplicationException {
		if (this.objectType == SCHEME) {
			updateObject();
		}
	}
	
	public void updateSchemeElement() throws ApplicationException {
		if (this.objectType == SCHEME_ELEMENT) {
			updateObject();
		}
	}
	
	public void updateSchemeProtoElement() throws ApplicationException {
		if (this.objectType == SCHEME_PROTO_ELEMENT) {
			updateObject();
		}
	}
	
	public Object[] getPathElements(SchemePath path) {
		Object[] cells = this.graph.getRoots();
		List<Object> new_cells = new ArrayList<Object>();
		SortedSet<Identifier> pmIds = null;
		
		if (pmIds == null) {
			Set<PathElement> pes;
			try {
				pes = path.getPathMembers();
			} catch (ApplicationException e) {
				Log.errorMessage(e);
				return new Object[0];
			}	
			pmIds = new TreeSet<Identifier>();
			for(PathElement pe : pes) {
				pmIds.add(pe.getAbstractSchemeElement().getId());
			}
		}

		for (int i = 0; i < cells.length; i++) {
			if (cells[i] instanceof DefaultCableLink) {
				DefaultCableLink cable = (DefaultCableLink) cells[i];
				if (pmIds.contains(cable.getSchemeCableLinkId()))
					new_cells.add(cable);
			} else if (cells[i] instanceof DefaultLink) {
				DefaultLink link = (DefaultLink) cells[i];
				if (pmIds.contains(link.getSchemeLinkId()))
					new_cells.add(link);
			} else if (cells[i] instanceof DeviceGroup) {
				DeviceGroup group = (DeviceGroup) cells[i];
				if (pmIds.contains(group.getElementId()))
					new_cells.add(group);
			}
		}
		return new_cells.toArray();
	}
}
