/*
 * $Id: SchemeProtoElement.java,v 1.2 2005/03/10 15:06:08 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme.corba;

import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.configuration.corba.EquipmentType_Transferable;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.general.corba.*;
import com.syrus.AMFICOM.general.corba.StorableObject;
import com.syrus.AMFICOM.resource.*;
import com.syrus.AMFICOM.resource.corba.ImageResource_Transferable;
import com.syrus.util.Log;
import java.util.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2005/03/10 15:06:08 $
 * @module scheme_v1
 */
public final class SchemeProtoElement extends StorableObject implements Namable, Describable, SchemeCellContainer, Characterizable, ComSyrusAmficomConfigurationSchemeProtoElement {

	private static final long serialVersionUID = 3258411712091862070L;
	protected Identifier characteristicIds[] = null;
	protected Identifier deviceIds[] = null;
	protected Identifier equipmentTypeId = null;
	protected Identifier linkIds[] = null;
	protected Identifier parentId = null;
	protected Identifier protoElementIds[] = null;

 /**
 						 * Takes non-null value at pack time.
 						 */
	protected Identifier schemeCellId = null;
	protected Identifier symbolId = null;
	protected String thisDescription = null;
	protected String thisLabel = null;

	protected String thisName = null;

 /**
 						 * Takes non-null value at pack time.
 						 */
	protected Identifier ugoCellId = null;

	/**
	 * @param id
	 */
	protected SchemeProtoElement(Identifier id) {
		super(id);
	}
	/**
	 * @param id
	 * @param created
	 * @param modified
	 * @param creatorId
	 * @param modifierId
	 * @param version
	 */
	protected SchemeProtoElement(Identifier id, Date created, Date modified, Identifier creatorId, Identifier modifierId, long version) {
		super(id, created, modified, creatorId, modifierId, version);
	}

	/**
	 * @param characteristic
	 * @see com.syrus.AMFICOM.general.Characterizable#addCharacteristic(Characteristic)
	 */
	public void addCharacteristic(final Characteristic characteristic) {
		throw new UnsupportedOperationException();
	}

	public Object clone() {
		try {
			final SchemeProtoElement schemeProtoElement = (SchemeProtoElement) super.clone();
			/**
			 * @todo Update the newly created object.
			 */
			return schemeProtoElement;
		} catch (final CloneNotSupportedException cnse) {
			Log.debugException(cnse, Log.SEVERE);
			return null;
		}
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
	 * @see com.syrus.AMFICOM.general.Characterizable#getCharacteristics()
	 */
	public Collection getCharacteristics() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.general.Characterizable#getCharacteristicSort()
	 */
	public CharacteristicSort getCharacteristicSort() {
		throw new UnsupportedOperationException();
	}

	public Date getCreated() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#getCreatorId()
	 */
	public Identifier getCreatorId() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#getDependencies()
	 */
	public Identifier[] getDependencies() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#getHeaderTransferable()
	 */
	public StorableObject_Transferable getHeaderTransferable() {
		throw new UnsupportedOperationException();
	}

	public Identifier getId() {
		throw new UnsupportedOperationException();
	}

	public Date getModified() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#getModifierId()
	 */
	public Identifier getModifierId() {
		throw new UnsupportedOperationException();
	}

	public long getVersion() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see IStorableObject#isChanged()
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

	public String name() {
		throw new UnsupportedOperationException();
	}

	public void name(String name) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see SchemeProtoElement#parent()
	 */
	public SchemeProtoGroup parent() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newParent
	 * @see SchemeProtoElement#parent(SchemeProtoGroup)
	 */
	public void parent(final SchemeProtoGroup newParent) {
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

	/**
	 * @param characteristic
	 * @see com.syrus.AMFICOM.general.Characterizable#removeCharacteristic(Characteristic)
	 */
	public void removeCharacteristic(final Characteristic characteristic) {
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

	/**
	 * @param storableObjectFactory
	 * @param changed
	 * @see IStorableObject#setChanged(StorableObjectFactory, boolean)
	 */
	public void setChanged(final StorableObjectFactory storableObjectFactory, final boolean changed) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param characteristics
	 * @see com.syrus.AMFICOM.general.Characterizable#setCharacteristics(java.util.Collection)
	 */
	public void setCharacteristics(final Collection characteristics) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param characteristics
	 * @see com.syrus.AMFICOM.general.Characterizable#setCharacteristics0(java.util.Collection)
	 */
	public void setCharacteristics0(final Collection characteristics) {
		throw new UnsupportedOperationException();
	}

	public ImageResource_Transferable symbol() {
		throw new UnsupportedOperationException();
	}

	public void symbol(ImageResource_Transferable symbol) {
		throw new UnsupportedOperationException();
	}

	/**
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
}
