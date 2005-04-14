/*-
 * $Id: SchemeDevice.java,v 1.12 2005/04/14 11:15:52 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import com.syrus.AMFICOM.general.AbstractCloneableStorableObject;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Describable;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import com.syrus.AMFICOM.scheme.corba.SchemeDevice_Transferable;
import com.syrus.util.Log;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

/**
 * #07 in hierarchy.
 *
 * @author $Author: bass $
 * @version $Revision: 1.12 $, $Date: 2005/04/14 11:15:52 $
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
		try {
			this.schemeDeviceDatabase = SchemeDatabaseContext.getSchemeDeviceDatabase();
			fromTransferable(transferable);
		} catch (final ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
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

	/**
	 * @param schemeCablePort cannot be <code>null</code>.
	 */
	public void addSchemeCablePort(final SchemeCablePort schemeCablePort) {
		assert schemeCablePort != null: ErrorMessages.NON_NULL_EXPECTED;
		schemeCablePort.setParentSchemeDevice(this);
	}

	/**
	 * @param schemePort cannot be <code>null</code>.
	 */
	public void addSchemePort(final SchemePort schemePort) {
		assert schemePort != null: ErrorMessages.NON_NULL_EXPECTED;
		schemePort.setParentSchemeDevice(this);
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
	 * @see com.syrus.AMFICOM.general.StorableObject#getDependencies()
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
	 * @see com.syrus.AMFICOM.general.Namable#getName()
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

	/**
	 * @return an immutable set.
	 */
	public Set getSchemeCablePorts() {
		try {
			return Collections.unmodifiableSet(SchemeStorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(this.id, ObjectEntities.SCHEME_CABLE_PORT_ENTITY_CODE), true));
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return Collections.EMPTY_SET;
		}
	}

	/**
	 * @return an immutable set.
	 */
	public Set getSchemePorts() {
		try {
			return Collections.unmodifiableSet(SchemeStorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(this.id, ObjectEntities.SCHEME_PORT_ENTITY_CODE), true));
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return Collections.EMPTY_SET;
		}
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

	/**
	 * The <code>SchemeCablePort</code> must belong to this
	 * <code>SchemeDevice</code>, or crap will meet the fan.
	 *
	 * @param schemeCablePort
	 */
	public void removeSchemeCablePort(final SchemeCablePort schemeCablePort) {
		assert schemeCablePort != null: ErrorMessages.NON_NULL_EXPECTED;
		assert getSchemeCablePorts().contains(schemeCablePort): ErrorMessages.REMOVAL_OF_AN_ABSENT_PROHIBITED;
		schemeCablePort.setParentSchemeDevice(null);
	}

	/**
	 * The <code>SchemePort</code> must belong to this
	 * <code>SchemeDevice</code>, or crap will meet the fan.
	 *
	 * @param schemePort
	 */
	public void removeSchemePort(final SchemePort schemePort) {
		assert schemePort != null: ErrorMessages.NON_NULL_EXPECTED;
		assert getSchemePorts().contains(schemePort): ErrorMessages.REMOVAL_OF_AN_ABSENT_PROHIBITED;
		schemePort.setParentSchemeDevice(null);
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
		if (this.description.equals(description))
			return;
		this.description = description;
		this.changed = true;
	}

	/**
	 * @see com.syrus.AMFICOM.general.Namable#setName(String)
	 */
	public void setName(final String name) {
		assert this.name != null && this.name.length() != 0 : ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert name != null && name.length() != 0 : ErrorMessages.NON_EMPTY_EXPECTED;
		if (this.name.equals(name))
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

	public void setSchemePorts(final Set schemePorts) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param transferable
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.StorableObject#fromTransferable(IDLEntity)
	 */
	protected void fromTransferable(final IDLEntity transferable) throws ApplicationException {
		throw new UnsupportedOperationException();
	}
}
