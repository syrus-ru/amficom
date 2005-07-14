/*-
 * $Id: SchemeDevice.java,v 1.49 2005/07/14 19:25:47 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import static com.syrus.AMFICOM.general.ErrorMessages.ACTION_WILL_RESULT_IN_NOTHING;
import static com.syrus.AMFICOM.general.ErrorMessages.EXACTLY_ONE_PARENT_REQUIRED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_EMPTY_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_VOID_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_BADLY_INITIALIZED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_NOT_INITIALIZED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_WILL_DELETE_ITSELF_FROM_POOL;
import static com.syrus.AMFICOM.general.ErrorMessages.REMOVAL_OF_AN_ABSENT_PROHIBITED;
import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMECABLEPORT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEDEVICE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEELEMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEPORT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEPROTOELEMENT_CODE;
import static java.util.logging.Level.FINE;
import static java.util.logging.Level.INFO;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Level.WARNING;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.AbstractCloneableStorableObject;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.Describable;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeDevice;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeDeviceHelper;
import com.syrus.util.Log;

/**
 * #07 in hierarchy.
 *
 * @author $Author: bass $
 * @version $Revision: 1.49 $, $Date: 2005/07/14 19:25:47 $
 * @module scheme_v1
 */
public final class SchemeDevice extends AbstractCloneableStorableObject
		implements Describable, Characterizable {
	private static final long serialVersionUID = 3762529027398644793L;

	private String name;

	private String description;

	Identifier parentSchemeElementId;
	
	Identifier parentSchemeProtoElementId;

	private Set<Characteristic> characteristics;

	private boolean parentSet = false;

	/**
	 * @param id
	 * @throws RetrieveObjectException
	 * @throws ObjectNotFoundException
	 */
	SchemeDevice(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.characteristics = new HashSet<Characteristic>();
		try {
			DatabaseContext.getDatabase(SCHEMEDEVICE_CODE).retrieve(this);
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

		assert parentSchemeProtoElement == null || parentSchemeElement == null: EXACTLY_ONE_PARENT_REQUIRED;		
		this.parentSchemeProtoElementId = Identifier.possiblyVoid(parentSchemeProtoElement);
		this.parentSchemeElementId = Identifier.possiblyVoid(parentSchemeElement);

		this.characteristics = new HashSet<Characteristic>();
	}

	/**
	 * @param transferable
	 * @throws CreateObjectException
	 */
	public SchemeDevice(final IdlSchemeDevice transferable) throws CreateObjectException {
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
		assert creatorId != null && !creatorId.isVoid(): NON_VOID_EXPECTED;
		assert name != null && name.length() != 0: NON_EMPTY_EXPECTED;
		assert description != null: NON_NULL_EXPECTED;

		try {
			final Date created = new Date();
			final SchemeDevice schemeDevice = new SchemeDevice(
					IdentifierPool
							.getGeneratedIdentifier(SCHEMEDEVICE_CODE),
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
		assert creatorId != null && !creatorId.isVoid(): NON_VOID_EXPECTED;
		assert name != null && name.length() != 0: NON_EMPTY_EXPECTED;
		assert description != null: NON_NULL_EXPECTED;
		assert parentSchemeProtoElement != null: NON_NULL_EXPECTED;

		try {
			final Date created = new Date();
			final SchemeDevice schemeDevice = new SchemeDevice(
					IdentifierPool
							.getGeneratedIdentifier(SCHEMEDEVICE_CODE),
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
		assert creatorId != null && !creatorId.isVoid(): NON_VOID_EXPECTED;
		assert name != null && name.length() != 0: NON_EMPTY_EXPECTED;
		assert description != null: NON_NULL_EXPECTED;
		assert parentSchemeElement != null: NON_NULL_EXPECTED;

		try {
			final Date created = new Date();
			final SchemeDevice schemeDevice = new SchemeDevice(
					IdentifierPool
							.getGeneratedIdentifier(SCHEMEDEVICE_CODE),
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
		assert characteristic != null: NON_NULL_EXPECTED;
		this.characteristics.add(characteristic);
		super.markAsChanged();
	}

	/**
	 * @param schemeCablePort cannot be <code>null</code>.
	 */
	public void addSchemeCablePort(final SchemeCablePort schemeCablePort) {
		assert schemeCablePort != null: NON_NULL_EXPECTED;
		schemeCablePort.setParentSchemeDevice(this);
	}

	/**
	 * @param schemePort cannot be <code>null</code>.
	 */
	public void addSchemePort(final SchemePort schemePort) {
		assert schemePort != null: NON_NULL_EXPECTED;
		schemePort.setParentSchemeDevice(this);
	}

	@Override
	public SchemeDevice clone() {
		final SchemeDevice schemeDevice = (SchemeDevice) super.clone();
		/**
		 * @todo Update the newly created object.
		 */
		return schemeDevice;
	}

	/**
	 * @see com.syrus.AMFICOM.general.Characterizable#getCharacteristics()
	 */
	public Set<Characteristic> getCharacteristics() {
		return Collections.unmodifiableSet(this.characteristics);
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getDependencies()
	 */
	@Override
	public Set<Identifiable> getDependencies() {
		assert this.parentSchemeElementId != null
				&& this.parentSchemeProtoElementId != null: OBJECT_NOT_INITIALIZED;
		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.add(this.parentSchemeElementId);
		dependencies.add(this.parentSchemeProtoElementId);
		dependencies.remove(null);
		dependencies.remove(VOID_IDENTIFIER);
		return Collections.unmodifiableSet(dependencies);
	}

	/**
	 * @see Describable#getDescription()
	 */
	public String getDescription() {
		assert this.description != null : OBJECT_NOT_INITIALIZED;
		return this.description;
	}

	/**
	 * @see com.syrus.AMFICOM.general.Namable#getName()
	 */
	public String getName() {
		assert this.name != null && this.name.length() != 0 : OBJECT_NOT_INITIALIZED;
		return this.name;
	}

	/**
	 * @throws IllegalStateException
	 */
	Identifier getParentSchemeElementId() {
//		assert this.assertParentSetStrict(): OBJECT_BADLY_INITIALIZED;
		if (!this.assertParentSetStrict()) {
			throw new IllegalStateException(OBJECT_BADLY_INITIALIZED);
		}
		final boolean parentSchemeElementIdVoid = this.parentSchemeElementId.isVoid();
		assert parentSchemeElementIdVoid || this.parentSchemeElementId.getMajor() == SCHEMEELEMENT_CODE;
		if (parentSchemeElementIdVoid) {
			Log.debugMessage("SchemeDevice.getParentSchemeElementId() | Parent SchemeElement was requested, while parent is a SchemeProtoElement; returning null.",
					FINE);
		}
		return this.parentSchemeElementId;
	}

	/**
	 * A wrapper around {@link #getParentSchemeElementId()}.
	 *
	 * @throws IllegalStateException
	 */
	public SchemeElement getParentSchemeElement() {
		try {
			return (SchemeElement) StorableObjectPool.getStorableObject(this.getParentSchemeElementId(), true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return null;
		}
	}

	/**
	 * @throws IllegalStateException
	 */
	Identifier getParentSchemeProtoElementId() {
//		assert this.assertParentSetStrict(): OBJECT_BADLY_INITIALIZED;
		if (!this.assertParentSetStrict()) {
			throw new IllegalStateException(OBJECT_BADLY_INITIALIZED);
		}
		final boolean parentSchemeProtoElementIdVoid = this.parentSchemeProtoElementId.isVoid();
		assert parentSchemeProtoElementIdVoid || this.parentSchemeProtoElementId.getMajor() == SCHEMEPROTOELEMENT_CODE;
		if (parentSchemeProtoElementIdVoid) {
			Log.debugMessage("SchemeDevice.getParentSchemeProtoElementId() | Parent SchemeProtoElement was requested, while parent is a SchemeElement; returning null.",
					FINE);
		}
		return this.parentSchemeProtoElementId;
	}

	/**
	 * A wrapper around {@link #getParentSchemeProtoElementId()}.
	 *
	 * @throws IllegalStateException
	 */
	public SchemeProtoElement getParentSchemeProtoElement() {
		try {
			return (SchemeProtoElement) StorableObjectPool.getStorableObject(this.getParentSchemeProtoElementId(), true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return null;
		}
	}

	/**
	 * @todo parameter breakOnLoadError to StorableObjectPool.getStorableObjectsByCondition
	 * @return an immutable set.
	 */
	public Set<SchemeCablePort> getSchemeCablePorts() {
		try {
			final Set<SchemeCablePort> schemecablePorts = StorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(this.id, SCHEMECABLEPORT_CODE), true, true);
			return Collections.unmodifiableSet(schemecablePorts);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return Collections.emptySet();
		}
	}

	/**
	 * @todo parameter breakOnLoadError to StorableObjectPool.getStorableObjectsByCondition
	 * @return an immutable set.
	 */
	public Set<SchemePort> getSchemePorts() {
		try {
			final Set<SchemePort> schemePorts = StorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(this.id, SCHEMEPORT_CODE), true, true);
			return Collections.unmodifiableSet(schemePorts);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return Collections.emptySet();
		}
	}

	/**
	 * @param orb
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlSchemeDevice getTransferable(final ORB orb) {
		return IdlSchemeDeviceHelper.init(orb,
				this.id.getTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getTransferable(),
				this.modifierId.getTransferable(),
				this.version, this.name,
				this.description,
				this.parentSchemeProtoElementId.getTransferable(),
				this.parentSchemeElementId.getTransferable(),
				Identifier.createTransferables(this.characteristics));
	}

	/**
	 * @param characteristic
	 * @see com.syrus.AMFICOM.general.Characterizable#removeCharacteristic(Characteristic)
	 */
	public void removeCharacteristic(final Characteristic characteristic) {
		assert characteristic != null: NON_NULL_EXPECTED;
		assert characteristic.getCharacterizableId().equals(super.id) : REMOVAL_OF_AN_ABSENT_PROHIBITED;
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
		assert schemeCablePort != null: NON_NULL_EXPECTED;
		assert schemeCablePort.getParentSchemeDeviceId().equals(super.id) : REMOVAL_OF_AN_ABSENT_PROHIBITED;
		schemeCablePort.setParentSchemeDevice(null);
	}

	/**
	 * The <code>SchemePort</code> must belong to this
	 * <code>SchemeDevice</code>, or crap will meet the fan.
	 *
	 * @param schemePort
	 */
	public void removeSchemePort(final SchemePort schemePort) {
		assert schemePort != null: NON_NULL_EXPECTED;
		assert schemePort.getParentSchemeDeviceId().equals(super.id) : REMOVAL_OF_AN_ABSENT_PROHIBITED;
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

		assert name != null && name.length() != 0: NON_EMPTY_EXPECTED;
		assert description != null: NON_NULL_EXPECTED;
		assert parentSchemeProtoElementId != null: NON_NULL_EXPECTED;
		assert parentSchemeElementId != null: NON_NULL_EXPECTED;
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
	public void setCharacteristics(final Set<Characteristic> characteristics) {
		setCharacteristics0(characteristics);
		super.markAsChanged();
	}

	/**
	 * @param characteristics
	 * @see com.syrus.AMFICOM.general.Characterizable#setCharacteristics0(Set)
	 */
	public void setCharacteristics0(final Set<Characteristic> characteristics) {
		assert characteristics != null: NON_NULL_EXPECTED;
		if (this.characteristics == null)
			this.characteristics = new HashSet<Characteristic>(characteristics.size());
		else
			this.characteristics.clear();
		this.characteristics.addAll(characteristics);
	}

	/**
	 * @see Describable#setDescription(String)
	 */
	public void setDescription(final String description) {
		assert this.description != null : OBJECT_NOT_INITIALIZED;
		assert description != null : NON_NULL_EXPECTED;
		if (this.description.equals(description))
			return;
		this.description = description;
		super.markAsChanged();
	}

	/**
	 * @see com.syrus.AMFICOM.general.Namable#setName(String)
	 */
	public void setName(final String name) {
		assert this.name != null && this.name.length() != 0 : OBJECT_NOT_INITIALIZED;
		assert name != null && name.length() != 0 : NON_EMPTY_EXPECTED;
		if (this.name.equals(name))
			return;
		this.name = name;
		super.markAsChanged();
	}

	/**
	 * @todo skip check if parentless.
	 */
	public void setParentSchemeElement(final SchemeElement parentSchemeElement) {
		assert this.assertParentSetNonStrict(): OBJECT_BADLY_INITIALIZED;

		Identifier newParentSchemeElementId;
		if (this.parentSchemeProtoElementId.isVoid()) {
			/*
			 * Moving from an element to another element.
			 */
			if (parentSchemeElement == null) {
				Log.debugMessage(OBJECT_WILL_DELETE_ITSELF_FROM_POOL, WARNING);
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
				Log.debugMessage(ACTION_WILL_RESULT_IN_NOTHING, INFO);
				return;
			}
			newParentSchemeElementId = parentSchemeElement.getId();
			this.parentSchemeProtoElementId = VOID_IDENTIFIER;
		}
		this.parentSchemeElementId = newParentSchemeElementId;
		super.markAsChanged();
	}

	/**
	 * @todo skip check if parentless.
	 */
	public void setParentSchemeProtoElement(final SchemeProtoElement parentSchemeProtoElement) {
		assert this.assertParentSetNonStrict(): OBJECT_BADLY_INITIALIZED;

		Identifier newParentSchemeProtoElementId;
		if (this.parentSchemeElementId.isVoid()) {
			/*
			 * Moving from a protoelement to another protoelement.
			 */
			if (parentSchemeProtoElement == null) {
				Log.debugMessage(OBJECT_WILL_DELETE_ITSELF_FROM_POOL, WARNING);
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
				Log.debugMessage(ACTION_WILL_RESULT_IN_NOTHING, INFO);
				return;
			}
			newParentSchemeProtoElementId = parentSchemeProtoElement.getId();
			this.parentSchemeElementId = VOID_IDENTIFIER;
		}
		this.parentSchemeProtoElementId = newParentSchemeProtoElementId;
		super.markAsChanged();
	}

	public void setSchemeCablePorts(final Set<SchemeCablePort> schemeCablePorts) {
		assert schemeCablePorts != null: NON_NULL_EXPECTED;
		for (final SchemeCablePort oldSchemeCablePort : getSchemeCablePorts()) {
			/*
			 * Check is made to prevent SchemeCablePorts from
			 * permanently losing their parents.
			 */
			assert !schemeCablePorts.contains(oldSchemeCablePort);
			removeSchemeCablePort(oldSchemeCablePort);
		}
		for (final SchemeCablePort schemeCablePort : schemeCablePorts) {
			addSchemeCablePort(schemeCablePort);
		}
	}

	public void setSchemePorts(final Set<SchemePort> schemePorts) {
		assert schemePorts != null: NON_NULL_EXPECTED;
		for (final SchemePort oldSchemePort : getSchemePorts()) {
			/*
			 * Check is made to prevent SchemePorts from
			 * permanently losing their parents.
			 */
			assert !schemePorts.contains(oldSchemePort);
			removeSchemePort(oldSchemePort);
		}
		for (final SchemePort schemePort : schemePorts) {
			addSchemePort(schemePort);
		}
	}

	/**
	 * @param transferable
	 * @throws CreateObjectException
	 * @see com.syrus.AMFICOM.general.StorableObject#fromTransferable(IdlStorableObject)
	 */
	@Override
	protected void fromTransferable(final IdlStorableObject transferable) throws CreateObjectException {
		final IdlSchemeDevice schemeDevice = (IdlSchemeDevice) transferable;
		try {
			super.fromTransferable(schemeDevice);
			final Set<Characteristic> characteristics0 = StorableObjectPool.getStorableObjects(Identifier.fromTransferables(schemeDevice.characteristicIds), true);
			this.setCharacteristics0(characteristics0);
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
