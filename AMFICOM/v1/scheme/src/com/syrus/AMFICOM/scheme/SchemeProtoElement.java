/*
 * $Id: SchemeProtoElement.java,v 1.5 2005/03/18 19:21:26 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import com.syrus.AMFICOM.configuration.EquipmentType;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import com.syrus.AMFICOM.resource.*;
import com.syrus.util.Log;
import java.util.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.5 $, $Date: 2005/03/18 19:21:26 $
 * @module scheme_v1
 */
public final class SchemeProtoElement extends AbstractCloneableStorableObject
		implements Describable, SchemeCellContainer, Characterizable {
	private static final long serialVersionUID = 3689348806202569782L;

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

	protected String thisLabel = null;

	/**
	 * Takes non-null value at pack time.
	 */
	protected Identifier ugoCellId = null;

	private String description;

	private String name;

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
	protected SchemeProtoElement(Identifier id, Date created,
			Date modified, Identifier creatorId,
			Identifier modifierId, long version) {
		super(id, created, modified, creatorId, modifierId, version);
	}

	/**
	 * @deprecated Use {@link #createInstance(Identifier)}instead.
	 */
	public static SchemeProtoElement createInstance() {
		throw new UnsupportedOperationException();
	}

	public static SchemeProtoElement createInstance(
			final Identifier creatorId)
			throws CreateObjectException {
		assert creatorId != null;
		try {
			final Date created = new Date();
			final SchemeProtoElement schemeProtoElement = new SchemeProtoElement(
					IdentifierPool
							.getGeneratedIdentifier(ObjectEntities.SCHEME_PROTO_ELEMENT_ENTITY_CODE),
					created, created, creatorId, creatorId,
					0L);
			schemeProtoElement.changed = true;
			return schemeProtoElement;
		} catch (final IllegalObjectEntityException ioee) {
			throw new CreateObjectException(
					"SchemeProtoElement.createInstance | cannot generate identifier ", ioee); //$NON-NLS-1$
		}
	}

	/**
	 * @param characteristic
	 * @see com.syrus.AMFICOM.general.Characterizable#addCharacteristic(Characteristic)
	 */
	public void addCharacteristic(final Characteristic characteristic) {
		throw new UnsupportedOperationException();
	}

	public Object clone() {
		final SchemeProtoElement schemeProtoElement = (SchemeProtoElement) super
				.clone();
		/**
		 * @todo Update the newly created object.
		 */
		return schemeProtoElement;
	}

	public SchemeDevice[] devices() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newDevices
	 * @see com.syrus.AMFICOM.scheme.SchemeProtoElement#devices(com.syrus.AMFICOM.scheme.corba.SchemeDevice[])
	 */
	public void devices(SchemeDevice[] newDevices) {
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

	/**
	 * @see StorableObject#getDependencies()
	 */
	public List getDependencies() {
		throw new UnsupportedOperationException();
	}

	public EquipmentType getEquipmentType() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.scheme.SchemeCellContainer#getSchemeCell()
	 */
	public SchemeImageResource getSchemeCell() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.scheme.SchemeSymbolContainer#getSymbol()
	 */
	public BitmapImageResource getSymbol() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable()
	 */
	public Object getTransferable() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return <code>ugoCell</code> associated with this
	 *         <code>SchemeProtoElement</code>, or <code>null</code> if
	 *         none.
	 * @see SchemeCellContainer#getUgoCell()
	 */
	public SchemeImageResource getUgoCell() {
		if (this.ugoCellId.equals(Identifier.VOID_IDENTIFIER))
			return null;
		try {
			return (SchemeImageResource) ResourceStorableObjectPool
					.getStorableObject(this.ugoCellId, true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return null;
		}
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
	 * @see com.syrus.AMFICOM.scheme.SchemeProtoElement#links(com.syrus.AMFICOM.scheme.corba.SchemeLink[])
	 */
	public void links(SchemeLink[] newLinks) {
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
	 * @see com.syrus.AMFICOM.scheme.SchemeProtoElement#protoElements(com.syrus.AMFICOM.scheme.corba.SchemeProtoElement[])
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

	/**
	 * @param newEquipmentTypeImpl
	 */
	public void setEquipmentType(EquipmentType newEquipmentTypeImpl) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param schemeCellImpl
	 * @see com.syrus.AMFICOM.scheme.SchemeCellContainer#setSchemeCell(SchemeImageResource)
	 */
	public void setSchemeCell(SchemeImageResource schemeCellImpl) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param symbolImpl
	 * @see com.syrus.AMFICOM.scheme.SchemeSymbolContainer#setSymbol(BitmapImageResource)
	 */
	public void setSymbol(BitmapImageResource symbolImpl) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ugoCell can be <code>null</code>.
	 * @see SchemeCellContainer#setUgoCell(SchemeImageResource)
	 */
	public void setUgoCell(final SchemeImageResource ugoCell) {
		Identifier newUgoCellId;
		if (ugoCell == null)
			newUgoCellId = Identifier.VOID_IDENTIFIER;
		else
			newUgoCellId = ugoCell.getId();
		if (!this.ugoCellId.equals(newUgoCellId)) {
			this.ugoCellId = newUgoCellId;
			this.changed = true;
		}
	}

	/**
	 * @see Describable#setDescription(String)
	 */
	public void setDescription(final String description) {
		assert this.description != null : ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert description != null : ErrorMessages.NON_NULL_EXPECTED;
		if (description.equals(this.description))
			return;
		this.description = description;
		this.changed = true;
	}

	/**
	 * @see Describable#getDescription()
	 */
	public String getDescription() {
		assert this.description != null : ErrorMessages.OBJECT_NOT_INITIALIZED;
		return this.description;
	}

	/**
	 * @see Namable#setName(String)
	 */
	public void setName(final String name) {
		assert this.name != null && this.name.length() != 0 : ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert name != null && name.length() != 0 : ErrorMessages.NON_EMPTY_EXPECTED;
		if (name.equals(this.name))
			return;
		this.name = name;
		this.changed = true;
	}

	/**
	 * @see Namable#getName()
	 */
	public String getName() {
		assert this.name != null && this.name.length() != 0 : ErrorMessages.OBJECT_NOT_INITIALIZED;
		return this.name;
	}
}
