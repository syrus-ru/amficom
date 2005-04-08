/*-
 * $Id: SchemeDevice.java,v 1.9 2005/04/08 09:26:11 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import com.syrus.AMFICOM.scheme.corba.SchemeDevice_Transferable;

import java.util.*;

import org.omg.CORBA.portable.IDLEntity;

/**
 * #07 in hierarchy.
 *
 * @author $Author: bass $
 * @version $Revision: 1.9 $, $Date: 2005/04/08 09:26:11 $
 * @module scheme_v1
 */
public final class SchemeDevice extends AbstractCloneableStorableObject
		implements Describable, Characterizable {
	private static final long serialVersionUID = 3762529027398644793L;

	private Set characteristics;

	private String description;

	private String name;

	private Identifier parentSchemeElementId;
	
	private Identifier parentSchemeProtoElementId;

	private SchemeDeviceDatabase schemeDeviceDatabase;

	/**
	 * @param id
	 * @throws RetrieveObjectException
	 * @throws ObjectNotFoundException
	 */
	SchemeDevice(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.characteristics = new HashSet();
		this.schemeDeviceDatabase = SchemeDatabaseContext.getSchemeDeviceDatabase();
		try {
			this.schemeDeviceDatabase.retrieve(this);
		} catch (final IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	/**
	 * @param id
	 * @param created
	 * @param modified
	 * @param creatorId
	 * @param modifierId
	 * @param version
	 */
	SchemeDevice(Identifier id, Date created, Date modified,
			Identifier creatorId, Identifier modifierId,
			long version) {
		super(id, created, modified, creatorId, modifierId, version);
	}

	/**
	 * @param transferable
	 * @throws CreateObjectException
	 */
	SchemeDevice(final SchemeDevice_Transferable transferable) throws CreateObjectException {
		this.schemeDeviceDatabase = SchemeDatabaseContext.getSchemeDeviceDatabase();
		fromTransferable(transferable);
	}
	
	/**
	 * @deprecated Use {@link #createInstance(Identifier)}instead.
	 */
	public static SchemeDevice createInstance() {
		throw new UnsupportedOperationException();
	}

	public static SchemeDevice createInstance(final Identifier creatorId)
			throws CreateObjectException {
		assert creatorId != null;
		try {
			final Date created = new Date();
			final SchemeDevice schemeDevice = new SchemeDevice(
					IdentifierPool
							.getGeneratedIdentifier(ObjectEntities.SCHEME_DEVICE_ENTITY_CODE),
					created, created, creatorId, creatorId,
					0L);
			schemeDevice.changed = true;
			return schemeDevice;
		} catch (final IllegalObjectEntityException ioee) {
			throw new CreateObjectException(
					"SchemeDevice.createInstance | cannot generate identifier ", ioee); //$NON-NLS-1$
		}
	}

	/**
	 * @param characteristic
	 * @see com.syrus.AMFICOM.general.Characterizable#addCharacteristic(Characteristic)
	 */
	public void addCharacteristic(final Characteristic characteristic) {
		throw new UnsupportedOperationException();
	}

	public void addSchemeCablePort(final SchemeCablePort schemeCablePort) {
		throw new UnsupportedOperationException();
	}

	public void addSchemePort(final SchemePort schemePort) {
		throw new UnsupportedOperationException();
	}

	public Object clone() {
		final SchemeDevice schemeDevice = (SchemeDevice) super.clone();
		/**
		 * @todo Update the newly created object.
		 */
		return schemeDevice;
	}

	/**
	 * @see com.syrus.AMFICOM.general.Characterizable#getCharacteristics()
	 */
	public Set getCharacteristics() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see Characterizable#getCharacteristicSort()
	 */
	public CharacteristicSort getCharacteristicSort() {
		return CharacteristicSort.CHARACTERISTIC_SORT_SCHEMEDEVICE;
	}

	/**
	 * @see StorableObject#getDependencies()
	 */
	public Set getDependencies() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see Describable#getDescription()
	 */
	public String getDescription() {
		assert this.description != null : ErrorMessages.OBJECT_NOT_INITIALIZED;
		return this.description;
	}

	/**
	 * @see Namable#getName()
	 */
	public String getName() {
		assert this.name != null && this.name.length() != 0 : ErrorMessages.OBJECT_NOT_INITIALIZED;
		return this.name;
	}

	public SchemeElement getParentSchemeElement() {
		throw new UnsupportedOperationException();
	}

	public SchemeProtoElement getParentSchemeProtoElement() {
		throw new UnsupportedOperationException();
	}

	public Set getSchemeCablePorts() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @deprecated
	 */
	public SchemeCablePort[] getSchemeCablePortsAsArray() {
		throw new UnsupportedOperationException();
	}

	public Set getSchemePorts() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @deprecated
	 */
	public SchemePort[] getSchemePortsAsArray() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable()
	 */
	public IDLEntity getTransferable() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param characteristic
	 * @see com.syrus.AMFICOM.general.Characterizable#removeCharacteristic(Characteristic)
	 */
	public void removeCharacteristic(final Characteristic characteristic) {
		throw new UnsupportedOperationException();
	}

	public void removeSchemeCablePort(final SchemeCablePort schemeCablePort) {
		throw new UnsupportedOperationException();
	}

	public void removeSchemePort(final SchemePort schemePort) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param characteristics
	 * @see com.syrus.AMFICOM.general.Characterizable#setCharacteristics(Set)
	 */
	public void setCharacteristics(final Set characteristics) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param characteristics
	 * @see com.syrus.AMFICOM.general.Characterizable#setCharacteristics0(Set)
	 */
	public void setCharacteristics0(final Set characteristics) {
		throw new UnsupportedOperationException();
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

	public void setParentSchemeElement(final SchemeElement parentSchemeElement) {
		throw new UnsupportedOperationException();
	}

	public void setParentSchemeProtoElement(final SchemeProtoElement parentSchemeProtoElement) {
		throw new UnsupportedOperationException();
	}

	public void setSchemeCablePorts(final Set schemeCablePorts) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @deprecated
	 */
	public void setSchemeCablePortsAsArray(final SchemeCablePort schemeCablePorts[]) {
		throw new UnsupportedOperationException();
	}

	public void setSchemePorts(final Set schemePorts) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @deprecated
	 */
	public void setSchemePortsAsArray(final SchemePort schemePorts[]) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param transferable
	 * @throws CreateObjectException
	 * @see StorableObject#fromTransferable(IDLEntity)
	 */
	protected void fromTransferable(final IDLEntity transferable) throws CreateObjectException {
		throw new UnsupportedOperationException();
	}
}
