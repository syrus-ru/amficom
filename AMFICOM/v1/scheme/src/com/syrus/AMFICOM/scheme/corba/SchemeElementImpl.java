/*
 * $Id: SchemeElementImpl.java,v 1.5 2004/12/06 08:31:44 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.                                              
 * Dept. of Science & Technology.                                               
 * Project: AMFICOM.                                                            
 */

package com.syrus.AMFICOM.scheme.corba;

import com.syrus.AMFICOM.CORBA.Map.MapSiteElement_Transferable;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.configuration.corba.*;
import com.syrus.AMFICOM.general.corba.*;
import com.syrus.AMFICOM.resource.*;
import com.syrus.AMFICOM.resource.corba.ImageResource_Transferable;
import com.syrus.util.logging.ErrorHandler;

import java.util.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.5 $, $Date: 2004/12/06 08:31:44 $
 * @module schemecommon_v1
 */
public final class SchemeElementImpl extends SchemeElement implements Cloneable {
	private static final ErrorHandler ERROR_HANDLER = ErrorHandler.getInstance();

	SchemeElementImpl() {
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

	public Characteristic_Transferable[] characteristics() {
		throw new UnsupportedOperationException();
	}

	public void characteristics(Characteristic_Transferable[] characteristics) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return
	 * @see java.util.JavaUtilCharacterizable#characteristicsImpl()
	 */
	public List characteristicsImpl() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newCharacteristicsImpl
	 * @see java.util.JavaUtilCharacterizable#characteristicsImpl(java.util.List)
	 */
	public void characteristicsImpl(List newCharacteristicsImpl) {
		throw new UnsupportedOperationException();
	}

	public SchemeElement cloneInstance() {
		try {
			return (SchemeElement) this.clone();
		} catch (CloneNotSupportedException cnse) {
			ERROR_HANDLER.error(cnse);
			return null;
		}
	}

	public long created() {
		throw new UnsupportedOperationException();
	}

	public String description() {
		throw new UnsupportedOperationException();
	}

	public void description(String description) {
		throw new UnsupportedOperationException();
	}

	public Equipment_Transferable equipment() {
		throw new UnsupportedOperationException();
	}

	public void equipment(Equipment_Transferable equipment) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return
	 * @see com.syrus.AMFICOM.configuration.ComSyrusAmficomConfigurationSchemeElement#equipmentImpl()
	 */
	public Equipment equipmentImpl() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newEquipmentImpl
	 * @see com.syrus.AMFICOM.configuration.ComSyrusAmficomConfigurationSchemeElement#equipmentImpl(com.syrus.AMFICOM.configuration.Equipment)
	 */
	public void equipmentImpl(Equipment newEquipmentImpl) {
		throw new UnsupportedOperationException();
	}

	public EquipmentType_Transferable equipmentType() {
		throw new UnsupportedOperationException();
	}

	public void equipmentType(EquipmentType_Transferable equipmentType) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return
	 * @see com.syrus.AMFICOM.configuration.ComSyrusAmficomConfigurationSchemeElement#equipmentTypeImpl()
	 */
	public EquipmentType equipmentTypeImpl() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newEquipmentTypeImpl
	 * @see com.syrus.AMFICOM.configuration.ComSyrusAmficomConfigurationSchemeElement#equipmentTypeImpl(com.syrus.AMFICOM.configuration.EquipmentType)
	 */
	public void equipmentTypeImpl(EquipmentType newEquipmentTypeImpl) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return
	 * @see java.util.JavaUtilIStorableObject#getCreated()
	 */
	public Date getCreated() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return
	 * @see com.syrus.AMFICOM.general.ComSyrusAmficomGeneralIStorableObject#getCreatorId()
	 */
	public com.syrus.AMFICOM.general.Identifier getCreatorId() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return
	 * @see java.util.JavaUtilIStorableObject#getDependencies()
	 */
	public List getDependencies() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return
	 * @see com.syrus.AMFICOM.general.corba.IStorableObject#getHeaderTransferable()
	 */
	public StorableObject_Transferable getHeaderTransferable() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return
	 * @see com.syrus.AMFICOM.general.ComSyrusAmficomGeneralIStorableObject#getId()
	 */
	public com.syrus.AMFICOM.general.Identifier getId() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return
	 * @see java.util.JavaUtilIStorableObject#getModified()
	 */
	public Date getModified() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return
	 * @see com.syrus.AMFICOM.general.ComSyrusAmficomGeneralIStorableObject#getModifierId()
	 */
	public com.syrus.AMFICOM.general.Identifier getModifierId() {
		throw new UnsupportedOperationException();
	}

	public Identifier id() {
		throw new UnsupportedOperationException();
	}

	public Scheme internalScheme() {
		throw new UnsupportedOperationException();
	}

	public void internalScheme(Scheme internalScheme) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return
	 * @see com.syrus.AMFICOM.general.corba.IStorableObject#isChanged()
	 */
	public boolean isChanged() {
		throw new UnsupportedOperationException();
	}

	public long modified() {
		throw new UnsupportedOperationException();
	}

	public String name() {
		throw new UnsupportedOperationException();
	}

	public void name(String name) {
		throw new UnsupportedOperationException();
	}

	public KIS_Transferable rtu() {
		throw new UnsupportedOperationException();
	}

	public void rtu(KIS_Transferable rtu) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return
	 * @see com.syrus.AMFICOM.configuration.ComSyrusAmficomConfigurationSchemeElement#rtuImpl()
	 */
	public KIS rtuImpl() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newRtuImpl
	 * @see com.syrus.AMFICOM.configuration.ComSyrusAmficomConfigurationSchemeElement#rtuImpl(com.syrus.AMFICOM.configuration.KIS)
	 */
	public void rtuImpl(KIS newRtuImpl) {
		throw new UnsupportedOperationException();
	}

	public Scheme scheme() {
		throw new UnsupportedOperationException();
	}

	public void scheme(Scheme scheme) {
		throw new UnsupportedOperationException();
	}

	public ImageResource_Transferable schemeCell() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newSchemeCell
	 * @see com.syrus.AMFICOM.resource.SchemeCellContainer#schemeCell(com.syrus.AMFICOM.resource.corba.ImageResource_Transferable)
	 */
	public void schemeCell(com.syrus.AMFICOM.resource.corba.ImageResource_Transferable newSchemeCell) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return
	 * @see com.syrus.AMFICOM.resource.SchemeCellContainer#schemeCellImpl()
	 */
	public SchemeImageResource schemeCellImpl() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param schemeCellImpl
	 * @see com.syrus.AMFICOM.resource.SchemeCellContainer#schemeCellImpl(SchemeImageResource)
	 */
	public void schemeCellImpl(SchemeImageResource schemeCellImpl) {
		throw new UnsupportedOperationException();
	}

	public SchemeDevice[] schemeDevices() {
		throw new UnsupportedOperationException();
	}

	public void schemeDevices(SchemeDevice[] schemeDevices) {
		throw new UnsupportedOperationException();
	}

	public SchemeElement[] schemeElements() {
		throw new UnsupportedOperationException();
	}

	public void schemeElements(SchemeElement[] schemeElements) {
		throw new UnsupportedOperationException();
	}

	public SchemeLink[] schemeLinks() {
		throw new UnsupportedOperationException();
	}

	public void schemeLinks(SchemeLink[] schemeLinks) {
		throw new UnsupportedOperationException();
	}

	public SchemeProtoElement schemeProtoElement() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newSchemeProtoElement
	 * @see com.syrus.AMFICOM.scheme.corba.SchemeElement#schemeProtoElement(com.syrus.AMFICOM.scheme.corba.SchemeProtoElement)
	 */
	public void schemeProtoElement(SchemeProtoElement newSchemeProtoElement) {
		throw new UnsupportedOperationException();
	}

	public MapSiteElement_Transferable site() {
		throw new UnsupportedOperationException();
	}

	public void site(MapSiteElement_Transferable site) {
		throw new UnsupportedOperationException();
	}

	public ImageResource_Transferable symbol() {
		throw new UnsupportedOperationException();
	}

	public void symbol(ImageResource_Transferable symbol) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return
	 * @see com.syrus.AMFICOM.resource.SchemeSymbolContainer#symbolImpl()
	 */
	public BitmapImageResource symbolImpl() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param symbolImpl
	 * @see com.syrus.AMFICOM.resource.SchemeSymbolContainer#symbolImpl(BitmapImageResource)
	 */
	public void symbolImpl(BitmapImageResource symbolImpl) {
		throw new UnsupportedOperationException();
	}

	public ImageResource_Transferable ugoCell() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newUgoCell
	 * @see com.syrus.AMFICOM.resource.SchemeCellContainer#ugoCell(com.syrus.AMFICOM.resource.corba.ImageResource_Transferable)
	 */
	public void ugoCell(com.syrus.AMFICOM.resource.corba.ImageResource_Transferable newUgoCell) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return
	 * @see com.syrus.AMFICOM.resource.SchemeCellContainer#ugoCellImpl()
	 */
	public SchemeImageResource ugoCellImpl() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ugoCellImpl
	 * @see com.syrus.AMFICOM.resource.SchemeCellContainer#ugoCellImpl(SchemeImageResource)
	 */
	public void ugoCellImpl(final SchemeImageResource ugoCellImpl) {
		throw new UnsupportedOperationException();
	}

	public long version() {
		throw new UnsupportedOperationException();
	}

	protected Object clone() throws CloneNotSupportedException {
		throw new UnsupportedOperationException();
	}
}
