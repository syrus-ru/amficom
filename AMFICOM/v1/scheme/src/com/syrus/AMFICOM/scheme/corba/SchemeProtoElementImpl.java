/*
 * $Id: SchemeProtoElementImpl.java,v 1.4 2004/12/06 08:31:44 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme.corba;

import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.configuration.corba.*;
import com.syrus.AMFICOM.general.corba.*;
import com.syrus.AMFICOM.resource.*;
import com.syrus.AMFICOM.resource.corba.ImageResource_Transferable;
import com.syrus.util.logging.ErrorHandler;
import java.util.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.4 $, $Date: 2004/12/06 08:31:44 $
 * @module schemecommon_v1
 */
final class SchemeProtoElementImpl extends SchemeProtoElement implements Cloneable {
	private static final ErrorHandler ERROR_HANDLER = ErrorHandler.getInstance();

	SchemeProtoElementImpl() {
	}

	public Characteristic_Transferable[] characteristics() {
		throw new UnsupportedOperationException();
	}

	public void characteristics(
			Characteristic_Transferable[] characteristics) {
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

	public SchemeProtoElement cloneInstance() {
		try {
			return (SchemeProtoElement) this.clone();
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

	public SchemeDevice[] devices() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newDevices
	 * @see com.syrus.AMFICOM.scheme.corba.SchemeProtoElement#devices(com.syrus.AMFICOM.scheme.corba.SchemeDevice[])
	 */
	public void devices(SchemeDevice[] newDevices) {
		throw new UnsupportedOperationException();
	}

	public Domain_Transferable domain() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newDomain
	 * @see com.syrus.AMFICOM.configuration.ComSyrusAmficomConfigurationSchemeProtoElement#domain(com.syrus.AMFICOM.configuration.corba.Domain_Transferable)
	 */
	public void domain(Domain_Transferable newDomain) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return
	 * @see com.syrus.AMFICOM.configuration.ComSyrusAmficomConfigurationSchemeProtoElement#domainImpl()
	 */
	public Domain domainImpl() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newDomainImpl
	 * @see com.syrus.AMFICOM.configuration.ComSyrusAmficomConfigurationSchemeProtoElement#domainImpl(com.syrus.AMFICOM.configuration.Domain)
	 */
	public void domainImpl(Domain newDomainImpl) {
		throw new UnsupportedOperationException();
	}

	public EquipmentType_Transferable equipmentType() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newEquipmentType
	 * @see com.syrus.AMFICOM.configuration.ComSyrusAmficomConfigurationSchemeProtoElement#equipmentType(com.syrus.AMFICOM.configuration.corba.EquipmentType_Transferable)
	 */
	public void equipmentType(EquipmentType_Transferable newEquipmentType) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return
	 * @see com.syrus.AMFICOM.configuration.ComSyrusAmficomConfigurationSchemeProtoElement#equipmentTypeImpl()
	 */
	public EquipmentType equipmentTypeImpl() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newEquipmentTypeImpl
	 * @see com.syrus.AMFICOM.configuration.ComSyrusAmficomConfigurationSchemeProtoElement#equipmentTypeImpl(com.syrus.AMFICOM.configuration.EquipmentType)
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

	/**
	 * @return
	 * @see com.syrus.AMFICOM.general.corba.IStorableObject#isChanged()
	 */
	public boolean isChanged() {
		throw new UnsupportedOperationException();
	}

	public String label() {
		throw new UnsupportedOperationException();
	}

	public void label(String label) {
		throw new UnsupportedOperationException();
	}

	public SchemeLink[] links() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newLinks
	 * @see com.syrus.AMFICOM.scheme.corba.SchemeProtoElement#links(com.syrus.AMFICOM.scheme.corba.SchemeLink[])
	 */
	public void links(SchemeLink[] newLinks) {
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

	public SchemeProtoElement[] protoElements() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newProtoElements
	 * @see com.syrus.AMFICOM.scheme.corba.SchemeProtoElement#protoElements(com.syrus.AMFICOM.scheme.corba.SchemeProtoElement[])
	 */
	public void protoElements(SchemeProtoElement[] newProtoElements) {
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
	public void ugoCellImpl(SchemeImageResource ugoCellImpl) {
		throw new UnsupportedOperationException();
	}

	public long version() {
		throw new UnsupportedOperationException();
	}

	protected Object clone() throws CloneNotSupportedException {
		throw new UnsupportedOperationException();
	}
}
