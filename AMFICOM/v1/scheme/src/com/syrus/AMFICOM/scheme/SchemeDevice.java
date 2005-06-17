/*-
 * $Id: SchemeDevice.java,v 1.34 2005/06/17 13:06:54 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.AbstractCloneableStorableObject;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.Describable;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.scheme.corba.SchemeDevice_Transferable;
import com.syrus.util.Log;

/**
 * #07 in hierarchy.
 *
 * @author $Author: bass $
 * @version $Revision: 1.34 $, $Date: 2005/06/17 13:06:54 $
 * @module scheme_v1
 */
public final class SchemeDevice extends AbstractCloneableStorableObject
		implements Describable, Characterizable {
	private static final long serialVersionUID = 3762529027398644793L;

	private String name;

	private String description;

	private Identifier parentSchemeElementId;
	
	private Identifier parentSchemeProtoElementId;

	private SchemeDeviceDatabase schemeDeviceDatabase;

	private Set characteristics;

	private boolean parentSet = false;

	/**
	 * @param id
	 * @throws RetrieveObjectException
	 * @throws ObjectNotFoundException
	 */
	SchemeDevice(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.characteristics = new HashSet();
		this.schemeDeviceDatabase = (SchemeDeviceDatabase) DatabaseContext.getDatabase(ObjectEntities.SCHEMEDEVICE_CODE);
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
	 * @param name
	 * @param description
	 * @param parentSchemeProtoElement
	 * @param parentSchemeElement
	 */
	SchemeDevice(final Identifier id, final Date created,
			final Date modified, final Identifier creatorId,
			final Identifier modifierId, final long version,
			final String name, final String description,
			final SchemeProtoElement parentSchemeProtoElement,
			final SchemeElement parentSchemeElement) {
		super(id, created, modified, creatorId, modifierId, version);
		this.name = name;
		this.description = description;

		assert parentSchemeProtoElement == null || parentSchemeElement == null: ErrorMessages.EXACTLY_ONE_PARENT_REQUIRED;		
		this.parentSchemeProtoElementId = Identifier.possiblyVoid(parentSchemeProtoElement);
		this.parentSchemeElementId = Identifier.possiblyVoid(parentSchemeElement);

		this.characteristics = new HashSet();
		this.schemeDeviceDatabase = (SchemeDeviceDatabase) DatabaseContext.getDatabase(ObjectEntities.SCHEMEDEVICE_CODE);
	}

	/**
	 * @param transferable
	 * @throws CreateObjectException
	 */
	SchemeDevice(final SchemeDevice_Transferable transferable) throws CreateObjectException {
		this.schemeDeviceDatabase = (SchemeDeviceDatabase) DatabaseContext.getDatabase(ObjectEntities.SCHEMEDEVICE_CODE);
		fromTransferable(transferable);
	}

	/**
	 * A shorthand for {@link #createInstance(Identifier, String, String)}.
	 * This method breaks some assertions, so clients should consider using
	 * other ones to create a new instance.
	 *
	 * @param creatorId
	 * @param name
	 * @throws CreateObjectException
	 */
	public static SchemeDevice createInstance(final Identifier creatorId,
			final String name) throws CreateObjectException {
		return createInstance(creatorId, name, "");
	}

	/**
	 * A shorthand for
	 * {@link #createInstance(Identifier, String, String, SchemeProtoElement)}.
	 *
	 * @param creatorId
	 * @param name
	 * @param parentSchemeProtoElement
	 * @throws CreateObjectException
	 */
	public static SchemeDevice createInstance(final Identifier creatorId,
			final String name,
			final SchemeProtoElement parentSchemeProtoElement)
			throws CreateObjectException {
		return createInstance(creatorId, name, "", parentSchemeProtoElement);
		
	}

	/**
	 * A shorthand for
	 * {@link #createInstance(Identifier, String, String, SchemeElement)}
	 *
	 * @param creatorId
	 * @param name
	 * @param parentSchemeElement
	 * @throws CreateObjectException
	 */
	public static SchemeDevice createInstance(final Identifier creatorId,
			final String name,
			final SchemeElement parentSchemeElement)
			throws CreateObjectException {
		return createInstance(creatorId, name, "", parentSchemeElement);
	}

	/**
	 * This method breaks some assertions, so clients should consider using
	 * other ones to create a new instance.
	 *
	 * @param creatorId
	 * @param name can be neither <code>null</code> nor empty.
	 * @param description cannot be <code>null</code>, but can be empty.
	 * @throws CreateObjectException
	 */
	public static SchemeDevice createInstance(final Identifier creatorId,
			final String name, final String description)
			throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid(): ErrorMessages.NON_VOID_EXPECTED;
		assert name != null && name.length() != 0: ErrorMessages.NON_EMPTY_EXPECTED;
		assert description != null: ErrorMessages.NON_NULL_EXPECTED;

		try {
			final Date created = new Date();
			final SchemeDevice schemeDevice = new SchemeDevice(
					IdentifierPool
							.getGeneratedIdentifier(ObjectEntities.SCHEMEDEVICE_CODE),
					created, created, creatorId, creatorId,
					0L, name, description,
					null, null);
			schemeDevice.markAsChanged();
			return schemeDevice;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException(
					"SchemeDevice.createInstance | cannot generate identifier ", ige);
		}
	}

	/**
	 * @param creatorId
	 * @param name can be neither <code>null</code> nor empty.
	 * @param description cannot be <code>null</code>, but can be empty.
	 * @param parentSchemeProtoElement
	 * @throws CreateObjectException
	 */
	public static SchemeDevice createInstance(final Identifier creatorId,
			final String name, final String description,
			final SchemeProtoElement parentSchemeProtoElement)
			throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid(): ErrorMessages.NON_VOID_EXPECTED;
		assert name != null && name.length() != 0: ErrorMessages.NON_EMPTY_EXPECTED;
		assert description != null: ErrorMessages.NON_NULL_EXPECTED;
		assert parentSchemeProtoElement != null: ErrorMessages.NON_NULL_EXPECTED;

		try {
			final Date created = new Date();
			final SchemeDevice schemeDevice = new SchemeDevice(
					IdentifierPool
							.getGeneratedIdentifier(ObjectEntities.SCHEMEDEVICE_CODE),
					created, created, creatorId, creatorId,
					0L, name, description,
					parentSchemeProtoElement, null);
			schemeDevice.markAsChanged();
			schemeDevice.parentSet = true;
			return schemeDevice;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException(
					"SchemeDevice.createInstance | cannot generate identifier ", ige);
		}
	}

	/**
	 * @param creatorId
	 * @param name can be neither <code>null</code> nor empty.
	 * @param description cannot be <code>null</code>, but can be empty.
	 * @param parentSchemeElement
	 * @throws CreateObjectException
	 */
	public static SchemeDevice createInstance(final Identifier creatorId,
			final String name, final String description,
			final SchemeElement parentSchemeElement)
			throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid(): ErrorMessages.NON_VOID_EXPECTED;
		assert name != null && name.length() != 0: ErrorMessages.NON_EMPTY_EXPECTED;
		assert description != null: ErrorMessages.NON_NULL_EXPECTED;
		assert parentSchemeElement != null: ErrorMessages.NON_NULL_EXPECTED;

		try {
			final Date created = new Date();
			final SchemeDevice schemeDevice = new SchemeDevice(
					IdentifierPool
							.getGeneratedIdentifier(ObjectEntities.SCHEMEDEVICE_CODE),
					created, created, creatorId, creatorId,
					0L, name, description,
					null, parentSchemeElement);
			schemeDevice.markAsChanged();
			schemeDevice.parentSet = true;
			return schemeDevice;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException(
					"SchemeDevice.createInstance | cannot generate identifier ", ige);
		}
	}

	/**
	 * @param characteristic
	 * @see com.syrus.AMFICOM.general.Characterizable#addCharacteristic(Characteristic)
	 */
	public void addCharacteristic(final Characteristic characteristic) {
		assert characteristic != null: ErrorMessages.NON_NULL_EXPECTED;
		this.characteristics.add(characteristic);
		super.markAsChanged();
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
		return Collections.unmodifiableSet(this.characteristics);
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getDependencies()
	 */
	public Set getDependencies() {
		assert this.parentSchemeElementId != null
				&& this.parentSchemeProtoElementId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		final Set dependencies = new HashSet();
		dependencies.add(this.parentSchemeElementId);
		dependencies.add(this.parentSchemeProtoElementId);
		dependencies.remove(null);
		dependencies.remove(Identifier.VOID_IDENTIFIER);
		return Collections.unmodifiableSet(dependencies);
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

	/**
	 * @throws IllegalStateException
	 */
	public SchemeElement getParentSchemeElement() {
//		assert this.assertParentSetStrict(): ErrorMessages.OBJECT_BADLY_INITIALIZED;
		if (!this.assertParentSetStrict())
			throw new IllegalStateException(ErrorMessages.OBJECT_BADLY_INITIALIZED);

		if (this.parentSchemeElementId.isVoid()) {
			Log.debugMessage("SchemeDevice.getParentSchemeElement() | Parent SchemeElement was requested, while parent is a SchemeProtoElement; returning null.",
					Log.FINE);
			return null;
		}

		try {
			return (SchemeElement) StorableObjectPool.getStorableObject(this.parentSchemeElementId, true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return null;
		}
	}

	/**
	 * @throws IllegalStateException
	 */
	public SchemeProtoElement getParentSchemeProtoElement() {
//		assert this.assertParentSetStrict(): ErrorMessages.OBJECT_BADLY_INITIALIZED;
		if (!this.assertParentSetStrict())
			throw new IllegalStateException(ErrorMessages.OBJECT_BADLY_INITIALIZED);

		if (this.parentSchemeProtoElementId.isVoid()) {
			Log.debugMessage("SchemeDevice.getParentSchemeProtoElement() | Parent SchemeProtoElement was requested, while parent is a SchemeElement; returning null.",
					Log.FINE);
		}

		try {
			return (SchemeProtoElement) StorableObjectPool.getStorableObject(this.parentSchemeProtoElementId, true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return null;
		}
	}

	/**
	 * @todo parameter breakOnLoadError to StorableObjectPool.getStorableObjectsByCondition
	 * @return an immutable set.
	 */
	public Set getSchemeCablePorts() {
		try {
			return Collections.unmodifiableSet(StorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(this.id, ObjectEntities.SCHEMECABLEPORT_CODE), true, true));
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return Collections.EMPTY_SET;
		}
	}

	/**
	 * @todo parameter breakOnLoadError to StorableObjectPool.getStorableObjectsByCondition
	 * @return an immutable set.
	 */
	public Set getSchemePorts() {
		try {
			return Collections.unmodifiableSet(StorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(this.id, ObjectEntities.SCHEMEPORT_CODE), true, true));
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return Collections.EMPTY_SET;
		}
	}

	/**
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable()
	 */
	public IDLEntity getTransferable() {
		return new SchemeDevice_Transferable(
				super.getHeaderTransferable(), this.name,
				this.description,
				(IdlIdentifier) this.parentSchemeProtoElementId.getTransferable(),
				(IdlIdentifier) this.parentSchemeElementId.getTransferable(),
				Identifier.createTransferables(this.characteristics));
	}

	/**
	 * @param characteristic
	 * @see com.syrus.AMFICOM.general.Characterizable#removeCharacteristic(Characteristic)
	 */
	public void removeCharacteristic(final Characteristic characteristic) {
		assert characteristic != null: ErrorMessages.NON_NULL_EXPECTED;
		assert getCharacteristics().contains(characteristic): ErrorMessages.REMOVAL_OF_AN_ABSENT_PROHIBITED;
		this.characteristics.remove(characteristic);
		super.markAsChanged();
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
	 * @param created
	 * @param modified
	 * @param creatorId
	 * @param modifierId
	 * @param version
	 * @param name
	 * @param description
	 * @param parentSchemeProtoElementId
	 * @param parentSchemeElementId
	 */
	synchronized void setAttributes(final Date created, final Date modified,
			final Identifier creatorId,
			final Identifier modifierId, final long version,
			final String name, final String description,
			final Identifier parentSchemeProtoElementId,
			final Identifier parentSchemeElementId) {
		super.setAttributes(created, modified, creatorId, modifierId, version);

		assert name != null && name.length() != 0: ErrorMessages.NON_EMPTY_EXPECTED;
		assert description != null: ErrorMessages.NON_NULL_EXPECTED;
		assert parentSchemeProtoElementId != null: ErrorMessages.NON_NULL_EXPECTED;
		assert parentSchemeElementId != null: ErrorMessages.NON_NULL_EXPECTED;
		assert parentSchemeProtoElementId.isVoid() ^ parentSchemeElementId.isVoid();

		this.name = name;
		this.description = description;
		this.parentSchemeProtoElementId = parentSchemeProtoElementId;
		this.parentSchemeElementId = parentSchemeElementId;
	}

	/**
	 * @param characteristics
	 * @see com.syrus.AMFICOM.general.Characterizable#setCharacteristics(Set)
	 */
	public void setCharacteristics(final Set characteristics) {
		setCharacteristics0(characteristics);
		super.markAsChanged();
	}

	/**
	 * @param characteristics
	 * @see com.syrus.AMFICOM.general.Characterizable#setCharacteristics0(Set)
	 */
	public void setCharacteristics0(final Set characteristics) {
		assert characteristics != null: ErrorMessages.NON_NULL_EXPECTED;
		if (this.characteristics == null)
			this.characteristics = new HashSet(characteristics.size());
		else
			this.characteristics.clear();
		this.characteristics.addAll(characteristics);
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
		super.markAsChanged();
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
		super.markAsChanged();
	}

	/**
	 * @todo skip check if parentless.
	 */
	public void setParentSchemeElement(final SchemeElement parentSchemeElement) {
		assert this.assertParentSetNonStrict(): ErrorMessages.OBJECT_BADLY_INITIALIZED;

		Identifier newParentSchemeElementId;
		if (this.parentSchemeProtoElementId.isVoid()) {
			/*
			 * Moving from an element to another element.
			 */
			if (parentSchemeElement == null) {
				Log.debugMessage(ErrorMessages.OBJECT_WILL_DELETE_ITSELF_FROM_POOL, Log.WARNING);
				StorableObjectPool.delete(super.id);
				return;
			}
			newParentSchemeElementId = parentSchemeElement.getId();
			if (this.parentSchemeElementId.equals(newParentSchemeElementId))
				return;
		} else {
			/*
			 * Moving from a protoelement to an element.
			 */
			if (parentSchemeElement == null) {
				Log.debugMessage(ErrorMessages.ACTION_WILL_RESULT_IN_NOTHING, Log.INFO);
				return;
			}
			newParentSchemeElementId = parentSchemeElement.getId();
			this.parentSchemeProtoElementId = Identifier.VOID_IDENTIFIER;
		}
		this.parentSchemeElementId = newParentSchemeElementId;
		super.markAsChanged();
	}

	/**
	 * @todo skip check if parentless.
	 */
	public void setParentSchemeProtoElement(final SchemeProtoElement parentSchemeProtoElement) {
		assert this.assertParentSetNonStrict(): ErrorMessages.OBJECT_BADLY_INITIALIZED;

		Identifier newParentSchemeProtoElementId;
		if (this.parentSchemeElementId.isVoid()) {
			/*
			 * Moving from a protoelement to another protoelement.
			 */
			if (parentSchemeProtoElement == null) {
				Log.debugMessage(ErrorMessages.OBJECT_WILL_DELETE_ITSELF_FROM_POOL, Log.WARNING);
				StorableObjectPool.delete(super.id);
				return;
			}
			newParentSchemeProtoElementId = parentSchemeProtoElement.getId();
			if (this.parentSchemeProtoElementId.equals(newParentSchemeProtoElementId))
				return;
		} else {
			/*
			 * Moving from an element to a protoelement.
			 */
			if (parentSchemeProtoElement == null) {
				Log.debugMessage(ErrorMessages.ACTION_WILL_RESULT_IN_NOTHING, Log.INFO);
				return;
			}
			newParentSchemeProtoElementId = parentSchemeProtoElement.getId();
			this.parentSchemeElementId = Identifier.VOID_IDENTIFIER;
		}
		this.parentSchemeProtoElementId = newParentSchemeProtoElementId;
		super.markAsChanged();
	}

	public void setSchemeCablePorts(final Set schemeCablePorts) {
		assert schemeCablePorts != null: ErrorMessages.NON_NULL_EXPECTED;
		for (final Iterator oldSchemeCablePortIterator = getSchemeCablePorts().iterator(); oldSchemeCablePortIterator.hasNext();) {
			final SchemeCablePort oldSchemeCablePort = (SchemeCablePort) oldSchemeCablePortIterator.next();
			/*
			 * Check is made to prevent SchemeCablePorts from
			 * permanently losing their parents.
			 */
			assert !schemeCablePorts.contains(oldSchemeCablePort);
			removeSchemeCablePort(oldSchemeCablePort);
		}
		for (final Iterator schemeCablePortIterator = schemeCablePorts.iterator(); schemeCablePortIterator.hasNext();)
			addSchemeCablePort((SchemeCablePort) schemeCablePortIterator.next());
	}

	public void setSchemePorts(final Set schemePorts) {
		assert schemePorts != null: ErrorMessages.NON_NULL_EXPECTED;
		for (final Iterator oldSchemePortIterator = getSchemePorts().iterator(); oldSchemePortIterator.hasNext();) {
			final SchemePort oldSchemePort = (SchemePort) oldSchemePortIterator.next();
			/*
			 * Check is made to prevent SchemePorts from
			 * permanently losing their parents.
			 */
			assert !schemePorts.contains(oldSchemePort);
			removeSchemePort(oldSchemePort);
		}
		for (final Iterator schemePortIterator = schemePorts.iterator(); schemePortIterator.hasNext();)
			addSchemePort((SchemePort) schemePortIterator.next());
	}

	/**
	 * @param transferable
	 * @throws CreateObjectException
	 * @see com.syrus.AMFICOM.general.StorableObject#fromTransferable(IDLEntity)
	 */
	protected void fromTransferable(final IDLEntity transferable) throws CreateObjectException {
		final SchemeDevice_Transferable schemeDevice = (SchemeDevice_Transferable) transferable;
		try {
			super.fromTransferable(schemeDevice.header);
			this.setCharacteristics0(StorableObjectPool.getStorableObjects(Identifier.fromTransferables(schemeDevice.characteristicIds), true));
		} catch (final ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
		this.name = schemeDevice.name;
		this.description = schemeDevice.description;
		this.parentSchemeProtoElementId = new Identifier(schemeDevice.parentSchemeProtoElementId);
		this.parentSchemeElementId = new Identifier(schemeDevice.parentSchemeElementId);
	}

	/*-********************************************************************
	 * Non-model members.                                                 *
	 **********************************************************************/

	/**
	 * Invoked by modifier methods.
	 */
	private boolean assertParentSetNonStrict() {
		if (this.parentSet)
			return this.assertParentSetStrict();
		this.parentSet = true;
		return this.parentSchemeElementId != null
				&& this.parentSchemeProtoElementId != null
				&& this.parentSchemeElementId.isVoid()
				&& this.parentSchemeProtoElementId.isVoid();
	}

	/**
	 * Invoked by accessor methods (it is assumed that object is already
	 * initialized).
	 */
	private boolean assertParentSetStrict() {
		return this.parentSchemeElementId != null
				&& this.parentSchemeProtoElementId != null
				&& (this.parentSchemeElementId.isVoid() ^ this.parentSchemeProtoElementId.isVoid());
	}
}
