/*
 * $Id: SchemeElementImpl.java,v 1.1 2004/11/23 09:52:42 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.                                              
 * Dept. of Science & Technology.                                               
 * Project: AMFICOM.                                                            
 */

package com.syrus.AMFICOM.scheme.corba;

import com.syrus.AMFICOM.CORBA.Map.MapSiteElement_Transferable;
import com.syrus.AMFICOM.CORBA.Resource.ImageResource_Transferable;
import com.syrus.AMFICOM.configuration.corba.*;
import com.syrus.AMFICOM.general.corba.Identifier;

/**
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2004/11/23 09:52:42 $
 * @module schemecommon_v1
 */
public final class SchemeElementImpl extends SchemeElement {
	SchemeElementImpl() {
	}
	
	SchemeElementImpl(final Identifier id,
			final SchemeProtoElement schemeProtoElement,
			final Scheme internalScheme,
			final Scheme scheme) {
		this.thisId = id;
		this.schemeProtoElementId = schemeProtoElement.id();
		this.internalSchemeId = internalScheme.id();
		this.schemeId = scheme.id();
	}

	public SchemeProtoElement schemeProtoElement() {
		throw new UnsupportedOperationException();
	}

	public EquipmentType_Transferable equipmentType() {
		throw new UnsupportedOperationException();
	}

	public void equipmentType(EquipmentType_Transferable newEquipmentType) {
		throw new UnsupportedOperationException();
	}

	public Equipment_Transferable equipment() {
		throw new UnsupportedOperationException();
	}

	public void equipment(Equipment_Transferable newEquipment) {
		throw new UnsupportedOperationException();
	}

	public KIS_Transferable rtu() {
		throw new UnsupportedOperationException();
	}

	public void rtu(KIS_Transferable newRtu) {
		throw new UnsupportedOperationException();
	}

	public SchemeDevice[] schemeDevices() {
		throw new UnsupportedOperationException();
	}

	public void schemeDevices(SchemeDevice[] newSchemeDevices) {
		throw new UnsupportedOperationException();
	}

	public SchemeLink[] schemeLinks() {
		throw new UnsupportedOperationException();
	}

	public void schemeLinks(SchemeLink[] newSchemeLinks) {
		throw new UnsupportedOperationException();
	}

	public SchemeElement[] schemeElements() {
		throw new UnsupportedOperationException();
	}

	public void schemeElements(SchemeElement[] newSchemeElements) {
		throw new UnsupportedOperationException();
	}

	public Scheme internalScheme() {
		throw new UnsupportedOperationException();
	}

	public void internalScheme(Scheme newInternalScheme) {
		throw new UnsupportedOperationException();
	}

	public ImageResource_Transferable ugoCell() {
		throw new UnsupportedOperationException();
	}

	public ImageResource_Transferable schemeCell() {
		throw new UnsupportedOperationException();
	}

	public MapSiteElement_Transferable site() {
		throw new UnsupportedOperationException();
	}

	public void site(MapSiteElement_Transferable newSite) {
		throw new UnsupportedOperationException();
	}

	public ImageResource_Transferable symbol() {
		throw new UnsupportedOperationException();
	}

	public void symbol(ImageResource_Transferable newSymbol) {
		throw new UnsupportedOperationException();
	}

	public SchemeElement cloneInstance() {
		throw new UnsupportedOperationException();
	}

	public Scheme scheme() {
		throw new UnsupportedOperationException();
	}

	public void scheme(Scheme newScheme) {
		throw new UnsupportedOperationException();
	}

	public Characteristic_Transferable[] characteristics() {
		throw new UnsupportedOperationException();
	}

	public void characteristics(Characteristic_Transferable[] newCharacteristics) {
		throw new UnsupportedOperationException();
	}

	public long created() {
		throw new UnsupportedOperationException();
	}

	public long modified() {
		throw new UnsupportedOperationException();
	}

	public long version() {
		throw new UnsupportedOperationException();
	}

	public String name() {
		throw new UnsupportedOperationException();
	}

	public void name(String newName) {
		throw new UnsupportedOperationException();
	}

	public String description() {
		throw new UnsupportedOperationException();
	}

	public void description(String newDescription) {
		throw new UnsupportedOperationException();
	}

	public Identifier id() {
		throw new UnsupportedOperationException();
	}

	public boolean alarmed() {
		throw new UnsupportedOperationException();
	}

	public void alarmed(boolean alarmed) {
		throw new UnsupportedOperationException();
	}

	public SchemePath alarmedPath() {
		throw new UnsupportedOperationException();
	}

	public void alarmedPath(SchemePath alarmedPath) {
		throw new UnsupportedOperationException();
	}

	public PathElement alarmedPathElement() {
		throw new UnsupportedOperationException();
	}

	public void alarmedPathElement(PathElement alarmedPathElement) {
		throw new UnsupportedOperationException();
	}
}
