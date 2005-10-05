/*-
 * $Id: SchemePath.java,v 1.103 2005/10/05 13:43:35 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import static com.syrus.AMFICOM.configuration.EquipmentType.MUFF;
import static com.syrus.AMFICOM.general.ErrorMessages.CHILDREN_ALIEN;
import static com.syrus.AMFICOM.general.ErrorMessages.EXACTLY_ONE_PARENT_REQUIRED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_EMPTY_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_VOID_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_NOT_INITIALIZED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_STATE_ILLEGAL;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_WILL_DELETE_ITSELF_FROM_POOL;
import static com.syrus.AMFICOM.general.ErrorMessages.REMOVAL_OF_AN_ABSENT_PROHIBITED;
import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.ObjectEntities.CHARACTERISTIC_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PATHELEMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEMONITORINGSOLUTION_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEPATH_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.TRANSMISSIONPATH_CODE;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Level.WARNING;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.configuration.TransmissionPath;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Describable;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ReverseDependencyContainer;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.XmlBeansTransferable;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.scheme.corba.IdlSchemePath;
import com.syrus.AMFICOM.scheme.corba.IdlSchemePathHelper;
import com.syrus.AMFICOM.scheme.corba.IdlPathElementPackage.IdlDataPackage.IdlKind;
import com.syrus.AMFICOM.scheme.xml.XmlSchemePath;
import com.syrus.util.Log;
import com.syrus.util.Shitlet;

/**
 * #16 in hierarchy.
 *
 * @author $Author: bass $
 * @version $Revision: 1.103 $, $Date: 2005/10/05 13:43:35 $
 * @module scheme
 */
public final class SchemePath extends StorableObject
		implements Describable, Characterizable,
		PathOwner<PathElement>, ReverseDependencyContainer,
		XmlBeansTransferable<XmlSchemePath> {
	private static final long serialVersionUID = 3257567312831132469L;

	private String name;

	private String description;

	private Identifier transmissionPathId;

	Identifier parentSchemeMonitoringSolutionId;

	/**
	 * @param id
	 * @param created
	 * @param modified
	 * @param creatorId
	 * @param modifierId
	 * @param version
	 * @param name
	 * @param description
	 * @param transmissionPath
	 * @param parentSchemeMonitoringSolution
	 */
	SchemePath(final Identifier id,
			final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final String name,
			final String description,
			final TransmissionPath transmissionPath,
			final SchemeMonitoringSolution parentSchemeMonitoringSolution) {
		super(id, created, modified, creatorId, modifierId, version);
		this.name = name;
		this.description = description;
		this.transmissionPathId = Identifier.possiblyVoid(transmissionPath);
		this.parentSchemeMonitoringSolutionId = Identifier.possiblyVoid(parentSchemeMonitoringSolution);
	}

	/**
	 * @param transferable
	 * @throws CreateObjectException
	 */
	public SchemePath(final IdlSchemePath transferable) throws CreateObjectException {
		fromTransferable(transferable);
	}

	/**
	 * A shorthand for
	 * {@link #createInstance(Identifier, String, String, TransmissionPath, SchemeMonitoringSolution)}.
	 *
	 * @param creatorId
	 * @param name
	 * @param parentSchemeMonitoringSolution
	 * @throws CreateObjectException
	 */
	public static SchemePath createInstance(final Identifier creatorId,
			final String name,
			final SchemeMonitoringSolution parentSchemeMonitoringSolution)
	throws CreateObjectException {
		return createInstance(creatorId, name, "", null,
				parentSchemeMonitoringSolution);
	}

	/**
	 * @param creatorId
	 * @param name
	 * @param description
	 * @param transmissionPath
	 * @param parentSchemeMonitoringSolution
	 * @throws CreateObjectException
	 */
	@ParameterizationPending(value = {"final boolean usePool"})
	public static SchemePath createInstance(final Identifier creatorId,
			final String name,
			final String description,
			final TransmissionPath transmissionPath,
			final SchemeMonitoringSolution parentSchemeMonitoringSolution)
	throws CreateObjectException {
		final boolean usePool = false;

		assert creatorId != null && !creatorId.isVoid() : NON_VOID_EXPECTED;
		assert name != null && name.length() != 0 : NON_EMPTY_EXPECTED;
		assert description != null : NON_NULL_EXPECTED;
		assert parentSchemeMonitoringSolution != null : NON_NULL_EXPECTED;
		try {
			final Date created = new Date();
			final SchemePath schemePath = new SchemePath(IdentifierPool.getGeneratedIdentifier(SCHEMEPATH_CODE),
					created,
					created,
					creatorId,
					creatorId,
					StorableObjectVersion.createInitial(),
					name,
					description,
					transmissionPath,
					parentSchemeMonitoringSolution);
			parentSchemeMonitoringSolution.getSchemePathContainerWrappee().addToCache(schemePath, usePool);

			schemePath.markAsChanged();
			return schemePath;
		} catch (final CreateObjectException coe) {
			throw coe;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException("SchemePath.createInstance | cannot generate identifier ", ige);
		} catch (final ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	/**
	 * Adds <code>PathElement</code> to the end of this
	 * <code>SchemePath</code>, adjusting its
	 * <code>sequentialNumber</code> accordingly.
	 *
	 * @param pathElement
	 * @param processSubsequentSiblings
	 */
	public void addPathMember(final PathElement pathElement, final boolean processSubsequentSiblings) {
		assert pathElement != null: NON_NULL_EXPECTED;
		pathElement.setParentPathOwner(this, processSubsequentSiblings);
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getDependencies()
	 */
	@Override
	public Set<Identifiable> getDependencies() {
		assert this.transmissionPathId != null
				&& this.parentSchemeMonitoringSolutionId != null : OBJECT_NOT_INITIALIZED;
		assert !this.parentSchemeMonitoringSolutionId.isVoid() : EXACTLY_ONE_PARENT_REQUIRED;
		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.add(this.transmissionPathId);
		dependencies.add(this.parentSchemeMonitoringSolutionId);
		dependencies.remove(null);
		dependencies.remove(VOID_IDENTIFIER);
		return Collections.unmodifiableSet(dependencies);
	}

	/**
	 * @see com.syrus.AMFICOM.general.ReverseDependencyContainer#getReverseDependencies(boolean)
	 */
	public Set<Identifiable> getReverseDependencies(final boolean usePool) throws ApplicationException {
		final Set<Identifiable> reverseDependencies = new HashSet<Identifiable>();
		reverseDependencies.add(super.id);
		for (final ReverseDependencyContainer reverseDependencyContainer : this.getCharacteristics0(usePool)) {
			reverseDependencies.addAll(reverseDependencyContainer.getReverseDependencies(usePool));
		}
		for (final ReverseDependencyContainer reverseDependencyContainer : this.getPathMembers0()) {
			reverseDependencies.addAll(reverseDependencyContainer.getReverseDependencies(usePool));
		}
		reverseDependencies.remove(null);
		reverseDependencies.remove(VOID_IDENTIFIER);
		return Collections.unmodifiableSet(reverseDependencies);
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

	Identifier getParentSchemeMonitoringSolutionId() {
		assert this.parentSchemeMonitoringSolutionId != null : OBJECT_NOT_INITIALIZED;
		assert !this.parentSchemeMonitoringSolutionId.isVoid() : EXACTLY_ONE_PARENT_REQUIRED;
		assert this.parentSchemeMonitoringSolutionId.getMajor() == SCHEMEMONITORINGSOLUTION_CODE;
		return this.parentSchemeMonitoringSolutionId;
	}

	/**
	 * A wrapper around {@link #getParentSchemeMonitoringSolutionId()}.
	 */
	public SchemeMonitoringSolution getParentSchemeMonitoringSolution() {
		try {
			return StorableObjectPool.getStorableObject(this.getParentSchemeMonitoringSolutionId(), true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return null;
		}
	}

	public SortedSet<PathElement> getPathMembers() {
		try {
			return Collections.unmodifiableSortedSet(this.getPathMembers0());
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return Collections.unmodifiableSortedSet(new TreeSet<PathElement>(Collections.<PathElement>emptySet()));
		}
	}

	/**
	 * @return child <code>PathElement</code>s in an unsorted manner.
	 */
	SortedSet<PathElement> getPathMembers0() throws ApplicationException {
		return new TreeSet<PathElement>(
				StorableObjectPool.<PathElement>getStorableObjectsByCondition(
						new LinkedIdsCondition(this.id, PATHELEMENT_CODE),
						true));
	}

	/**
	 * @param orb
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlSchemePath getTransferable(final ORB orb) {
		return IdlSchemePathHelper.init(orb,
				this.id.getTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getTransferable(),
				this.modifierId.getTransferable(),
				this.version.longValue(),
				this.name,
				this.description,
				this.transmissionPathId.getTransferable(),
				this.parentSchemeMonitoringSolutionId.getTransferable());
	}

	/**
	 * @param schemePath
	 * @param importType
	 * @param usePool
	 * @throws ApplicationException
	 * @see XmlBeansTransferable#getXmlTransferable(com.syrus.AMFICOM.general.xml.XmlStorableObject, String, boolean)
	 */
	public void getXmlTransferable(final XmlSchemePath schemePath,
			final String importType,
			final boolean usePool)
	throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	Identifier getTransmissionPathId() {
		assert this.transmissionPathId != null: OBJECT_NOT_INITIALIZED;
		assert this.transmissionPathId.isVoid() || this.transmissionPathId.getMajor() == TRANSMISSIONPATH_CODE;
		return this.transmissionPathId;
	}

	/**
	 * A wrapper around {@link #getTransmissionPathId()}.
	 */
	public TransmissionPath getTransmissionPath() {
		try {
			return StorableObjectPool.getStorableObject(this.getTransmissionPathId(), true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return null;
		}
	}

	/**
	 * Removes the <code>PathElement</code> from this
	 * <code>SchemePath</code>, changing its
	 * <code>sequentialNumber</code> to <code>-1</code> and removing all
	 * its subsequent <code>PathElement</code>s.
	 *
	 * @param pathElement
	 * @param processSubsequentSiblings
	 */
	public void removePathMember(final PathElement pathElement, final boolean processSubsequentSiblings) {
		assert pathElement != null: NON_NULL_EXPECTED;
		assert pathElement.getParentSchemePathId().equals(this) : REMOVAL_OF_AN_ABSENT_PROHIBITED;
		pathElement.setParentPathOwner(null, processSubsequentSiblings);
	}

	/**
	 * @param created
	 * @param modified
	 * @param creatorId
	 * @param modifierId
	 * @param version
	 * @param name
	 * @param description
	 * @param transmissionPathId
	 * @param parentSchemeMonitoringSolutionId
	 */
	void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final String name,
			final String description,
			final Identifier transmissionPathId,
			final Identifier parentSchemeMonitoringSolutionId) {
		synchronized (this) {
			super.setAttributes(created, modified, creatorId, modifierId, version);
	
			assert name != null && name.length() != 0 : NON_EMPTY_EXPECTED;
			assert description != null : NON_NULL_EXPECTED;
			assert transmissionPathId != null : NON_NULL_EXPECTED;
			assert parentSchemeMonitoringSolutionId != null && !parentSchemeMonitoringSolutionId.isVoid() : NON_NULL_EXPECTED;
	
			this.name = name;
			this.description = description;
			this.transmissionPathId = transmissionPathId;
			this.parentSchemeMonitoringSolutionId = parentSchemeMonitoringSolutionId;
		}
	}

	/**
	 * @see Describable#setDescription(String)
	 */
	public void setDescription(final String description) {
		assert this.description != null : OBJECT_NOT_INITIALIZED;
		assert description != null : NON_NULL_EXPECTED;
		if (this.description.equals(description)) {
			return;
		}
		this.description = description;
		super.markAsChanged();
	}

	/**
	 * @see com.syrus.AMFICOM.general.Namable#setName(String)
	 */
	public void setName(final String name) {
		assert this.name != null && this.name.length() != 0 : OBJECT_NOT_INITIALIZED;
		assert name != null && name.length() != 0 : NON_EMPTY_EXPECTED;
		if (this.name.equals(name)) {
			return;
		}
		this.name = name;
		super.markAsChanged();
	}

	/**
	 * A wrapper around {@link #setParentSchemeMonitoringSolution(SchemeMonitoringSolution, boolean)}.
	 *
	 * @param parentSchemeMonitoringSolutionId
	 * @param usePool
	 * @throws ApplicationException
	 */
	void setParentSchemeMonitoringSolutionId(
			final Identifier parentSchemeMonitoringSolutionId,
			final boolean usePool)
	throws ApplicationException {
		assert parentSchemeMonitoringSolutionId != null : NON_NULL_EXPECTED;
		assert parentSchemeMonitoringSolutionId.isVoid() || parentSchemeMonitoringSolutionId.getMajor() == SCHEMEMONITORINGSOLUTION_CODE;

		if (this.parentSchemeMonitoringSolutionId.equals(parentSchemeMonitoringSolutionId)) {
			return;
		}

		this.setParentSchemeMonitoringSolution(
				StorableObjectPool.<SchemeMonitoringSolution>getStorableObject(parentSchemeMonitoringSolutionId, true),
				usePool);
	}

	/**
	 * @param parentSchemeMonitoringSolution
	 * @param usePool
	 * @throws ApplicationException
	 */
	public void setParentSchemeMonitoringSolution(
			final SchemeMonitoringSolution parentSchemeMonitoringSolution,
			final boolean usePool)
	throws ApplicationException {
		assert this.parentSchemeMonitoringSolutionId != null : OBJECT_NOT_INITIALIZED;
		assert !this.parentSchemeMonitoringSolutionId.isVoid() : EXACTLY_ONE_PARENT_REQUIRED;

		final Identifier newParentSchemeMonitoringSolutionId = Identifier.possiblyVoid(parentSchemeMonitoringSolution);
		if (this.parentSchemeMonitoringSolutionId.equals(newParentSchemeMonitoringSolutionId)) {
			return;
		}

		this.getParentSchemeMonitoringSolution().getSchemePathContainerWrappee().removeFromCache(this, usePool);

		if (parentSchemeMonitoringSolution == null) {
			Log.debugMessage(OBJECT_WILL_DELETE_ITSELF_FROM_POOL, WARNING);
			StorableObjectPool.delete(this.getReverseDependencies(usePool));
		} else {
			parentSchemeMonitoringSolution.getSchemePathContainerWrappee().addToCache(this, usePool);
		}

		this.parentSchemeMonitoringSolutionId = newParentSchemeMonitoringSolutionId;
		super.markAsChanged();
	}

	/**
	 * @param transmissionPathId
	 */
	void setTransmissionPathId(final Identifier transmissionPathId) {
		assert transmissionPathId != null : NON_NULL_EXPECTED;
		assert transmissionPathId.isVoid() || transmissionPathId.getMajor() == TRANSMISSIONPATH_CODE;

		if (this.transmissionPathId.equals(transmissionPathId)) {
			return;
		}
		this.transmissionPathId = transmissionPathId;
		super.markAsChanged();
	}

	/**
	 * A wrapper around {@link #setTransmissionPathId(Identifier)}.
	 *
	 * @param transmissionPath
	 */
	public void setTransmissionPath(final TransmissionPath transmissionPath) {
		this.setTransmissionPathId(Identifier.possiblyVoid(transmissionPath));
	}

	/**
	 * @param transferable
	 * @throws CreateObjectException
	 * @see com.syrus.AMFICOM.general.StorableObject#fromTransferable(IdlStorableObject)
	 */
	@Override
	protected void fromTransferable(final IdlStorableObject transferable)
	throws CreateObjectException {
		synchronized (this) {
			final IdlSchemePath schemePath = (IdlSchemePath) transferable;
			try {
				super.fromTransferable(schemePath);
			} catch (final CreateObjectException coe) {
				throw coe;
			} catch (final ApplicationException ae) {
				throw new CreateObjectException(ae);
			}
			this.name = schemePath.name;
			this.description = schemePath.description;
			this.transmissionPathId = new Identifier(schemePath.transmissionPathId);
			this.parentSchemeMonitoringSolutionId = new Identifier(schemePath.parentSchemeMonitoringSolutionId);
		}
	}

	/**
	 * @param xmlSchemePath
	 * @param importType
	 * @throws ApplicationException
	 * @see XmlBeansTransferable#fromXmlTransferable(com.syrus.AMFICOM.general.xml.XmlStorableObject, String)
	 */
	public void fromXmlTransferable(final XmlSchemePath xmlSchemePath,
			final String importType)
	throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param sequentialNumber
	 * @throws ApplicationException
	 * @see PathOwner#getPathMember(int)
	 */
	public PathElement getPathMember(final int sequentialNumber) throws ApplicationException {
		if (sequentialNumber < 0) {
			throw new IndexOutOfBoundsException("sequential numbers usually start with 0");
		}
		final StorableObjectCondition typicalCondition = new TypicalCondition(
				sequentialNumber,
				sequentialNumber,
				OperationSort.OPERATION_EQUALS,
				PATHELEMENT_CODE,
				PathElementWrapper.COLUMN_SEQUENTIAL_NUMBER) {
			private static final long serialVersionUID = -9024929076725678350L;

			@Override
			public boolean isNeedMore(final Set<? extends Identifiable> identifiables) {
				return identifiables.isEmpty();
			}
		};
		final StorableObjectCondition linkedIdsCondition = new LinkedIdsCondition(
				super.id,
				PATHELEMENT_CODE);
		final StorableObjectCondition compoundCondition = new CompoundCondition(
				typicalCondition,
				CompoundConditionSort.AND,
				linkedIdsCondition);
		final Set<PathElement> pathMembers = StorableObjectPool.getStorableObjectsByCondition(compoundCondition, true);
		if (pathMembers.isEmpty()) {
			throw new NoSuchElementException("no path member found with sequential number: " + sequentialNumber);
		}
		assert pathMembers.size() == 1;
		return pathMembers.iterator().next();
	}

	/*-********************************************************************
	 * Children manipulation: characteristics                             *
	 **********************************************************************/

	private StorableObjectContainerWrappee<Characteristic> characteristicContainerWrappee;

	/**
	 * @see Characterizable#getCharacteristicContainerWrappee()
	 */
	public StorableObjectContainerWrappee<Characteristic> getCharacteristicContainerWrappee() {
		return (this.characteristicContainerWrappee == null)
				? this.characteristicContainerWrappee = new StorableObjectContainerWrappee<Characteristic>(this, CHARACTERISTIC_CODE)
				: this.characteristicContainerWrappee;
	}

	/**
	 * @param characteristic
	 * @param usePool
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.Characterizable#addCharacteristic(com.syrus.AMFICOM.general.Characteristic, boolean)
	 */
	public void addCharacteristic(final Characteristic characteristic,
			final boolean usePool)
	throws ApplicationException {
		assert characteristic != null : NON_NULL_EXPECTED;
		characteristic.setParentCharacterizable(this, usePool);
	}

	/**
	 * @param characteristic
	 * @param usePool
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.Characterizable#removeCharacteristic(com.syrus.AMFICOM.general.Characteristic, boolean)
	 */
	public void removeCharacteristic(
			final Characteristic characteristic,
			final boolean usePool)
	throws ApplicationException {
		assert characteristic != null : NON_NULL_EXPECTED;
		assert characteristic.getParentCharacterizableId().equals(this) : REMOVAL_OF_AN_ABSENT_PROHIBITED;
		characteristic.setParentCharacterizable(this, usePool);
	}

	/**
	 * @param usePool
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.Characterizable#getCharacteristics(boolean)
	 */
	public Set<Characteristic> getCharacteristics(boolean usePool)
	throws ApplicationException {
		return Collections.unmodifiableSet(this.getCharacteristics0(usePool));
	}

	/**
	 * @param usePool
	 * @throws ApplicationException
	 */
	Set<Characteristic> getCharacteristics0(final boolean usePool)
	throws ApplicationException {
		return this.getCharacteristicContainerWrappee().getContainees(usePool);
	}

	/**
	 * @param characteristics
	 * @param usePool
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.Characterizable#setCharacteristics(Set, boolean)
	 */
	public void setCharacteristics(final Set<Characteristic> characteristics,
			final boolean usePool)
	throws ApplicationException {
		assert characteristics != null : NON_NULL_EXPECTED;

		final Set<Characteristic> oldCharacteristics = this.getCharacteristics0(usePool);

		final Set<Characteristic> toRemove = new HashSet<Characteristic>(oldCharacteristics);
		toRemove.removeAll(characteristics);
		for (final Characteristic characteristic : toRemove) {
			this.removeCharacteristic(characteristic, usePool);
		}

		final Set<Characteristic> toAdd = new HashSet<Characteristic>(characteristics);
		toAdd.removeAll(oldCharacteristics);
		for (final Characteristic characteristic : toAdd) {
			this.addCharacteristic(characteristic, usePool);
		}
	}

	/*-********************************************************************
	 * Non-model members.                                                 *
	 **********************************************************************/

	/**
	 * @return <code>SchemeElement</code> associated with the first
	 *         <code>PathElement</code> in this <code>SchemePath</code>.
	 */
	public SchemeElement getStartSchemeElement() {
		final SortedSet<PathElement> pathElements = this.getPathMembers();
		assert !pathElements.isEmpty(): NON_EMPTY_EXPECTED;
		final PathElement startPathElement = pathElements.first();
		assert startPathElement.getKind() == IdlKind.SCHEME_ELEMENT: OBJECT_STATE_ILLEGAL;
		return startPathElement.getSchemeElement();
	}

	/**
	 * @return <code>SchemeElement</code> associated with the last
	 *         <code>PathElement</code> in this <code>SchemePath</code>.
	 */
	public SchemeElement getEndSchemeElement() {
		final SortedSet<PathElement> pathElements = this.getPathMembers();
		assert !pathElements.isEmpty(): NON_EMPTY_EXPECTED;
		final PathElement endPathElement = pathElements.last();
		assert endPathElement.getKind() == IdlKind.SCHEME_ELEMENT: OBJECT_STATE_ILLEGAL;
		return endPathElement.getSchemeElement();
	}

	/**
	 * @param pathElement
	 * @throws ApplicationException
	 */
	@Shitlet
	public PathElement getNextNode(final PathElement pathElement) throws ApplicationException {
		assert assertContains(pathElement): CHILDREN_ALIEN;

		final Iterator<PathElement> pathElementIterator = this.getPathMembers().tailSet(pathElement).iterator();
		assert pathElementIterator.hasNext();
		pathElementIterator.next();
		
		while (pathElementIterator.hasNext()) {
			final PathElement pathElement1 = pathElementIterator.next();
			if (pathElement1.getKind() == IdlKind.SCHEME_ELEMENT
					&& pathElement1.getSchemeElement().getProtoEquipment().getType() != MUFF) {
				return pathElement1;
			}
		}
		return null;
	}

	/**
	 * @param pathElement
	 */
	public PathElement getNextPathElement(final PathElement pathElement) {
		assert assertContains(pathElement): CHILDREN_ALIEN;

		final SortedSet<PathElement> pathElements  = getPathMembers().tailSet(pathElement);
		if (pathElements.size() == 1) {
			return null;
		}
		final Iterator<PathElement> pathElementIterator = pathElements.iterator();
		pathElementIterator.next();
		return pathElementIterator.next();
	}

	/**
	 * @param physicalDistance
	 * @deprecated
	 */
	@Shitlet
	@Deprecated
	public double getOpticalDistance(final double physicalDistance) {
		double opticalDistance = .0;
		double d = .0;
		for (final PathElement pathElement : getPathMembers()) {
			final double physicalLength = SchemeUtils.getPhysicalLength(pathElement);
			if (d + physicalLength < physicalDistance) {
				d += physicalLength;
				opticalDistance += SchemeUtils.getOpticalLength(pathElement);
			} else {
				opticalDistance += (physicalDistance - d) * SchemeUtils.getKu(pathElement);
				break;
			}
			
		}
		return opticalDistance;
	}

	/**
	 * @param pathElement
	 * @deprecated
	 */
	@Shitlet
	@Deprecated
	public double[] getOpticalDistanceFromStart(final PathElement pathElement) {
		assert assertContains(pathElement): CHILDREN_ALIEN;

		double opticalDistanceFromStart = 0;
		final SortedSet<PathElement> pathElements = getPathMembers();
		for (final PathElement pathElement1 : pathElements) {
//			if (pathElement1 == pathElement) {}
			if (pathElement1.equals(pathElement)) {
				return new double[]{opticalDistanceFromStart, opticalDistanceFromStart + SchemeUtils.getOpticalLength(pathElement1)};
			}
			opticalDistanceFromStart += SchemeUtils.getOpticalLength(pathElement1);
		}
		/*
		 * Never.
		 */
		return new double[2];
	}

	/**
	 * @param opticalDistance
	 * @deprecated
	 */
	@Shitlet
	@Deprecated
	public PathElement getPathElementByOpticalDistance(final double opticalDistance) {
		final SortedSet<PathElement> pathElements = getPathMembers();
		if (pathElements.isEmpty()) {
			return null;
		}

		double opticalLength = 0;
		for (final PathElement pathElement : pathElements) {
			opticalLength += SchemeUtils.getOpticalLength(pathElement);
			if (opticalLength >= opticalDistance) {
				return pathElement;
			}
		}
		return pathElements.last();
	}

	/**
	 * @param physicalDistance
	 * @deprecated
	 */
	@Shitlet
	@Deprecated
	public PathElement getPathElementByPhysicalDistance(final double physicalDistance) {
		final SortedSet<PathElement> pathElements = getPathMembers();
		if (pathElements.isEmpty()) {
			return null;
		}

		double physicalLength = 0;
		for (final PathElement pathElement : pathElements) {
			physicalLength += SchemeUtils.getPhysicalLength(pathElement);
			if (physicalLength >= physicalDistance) {
				return pathElement;
			}
		}
		return pathElements.last();
	}

	/**
	 * @param opticalDistance
	 * @deprecated
	 */
	@Shitlet
	@Deprecated
	public double getPhysicalDistance(final double opticalDistance) {
		double physicalDistance = .0;
		double d = .0;
		for (final PathElement pathElement : getPathMembers()) {
			final double opticalLength = SchemeUtils.getOpticalLength(pathElement);
			if (d + opticalLength < opticalDistance) {
				d += opticalLength;
				physicalDistance += SchemeUtils.getPhysicalLength(pathElement);
			} else {
				physicalDistance += (opticalDistance - d) / SchemeUtils.getKu(pathElement);
				break;
			}
		}
		return physicalDistance;
	}

	/**
	 * @param pathElement
	 * @deprecated
	 */
	@Shitlet
	@Deprecated
	public double[] getPhysicalDistanceFromStart(final PathElement pathElement) {
		assert assertContains(pathElement): CHILDREN_ALIEN;

		double physicalDistanceFromStart = 0;
		final SortedSet<PathElement> pathElements = getPathMembers();
		for (final PathElement pathElement1 : pathElements) {
//			if (pathElement1 == pathElement) {}
			if (pathElement1.equals(pathElement)) {
				return new double[]{physicalDistanceFromStart, physicalDistanceFromStart + SchemeUtils.getPhysicalLength(pathElement1)};
			}
			physicalDistanceFromStart += SchemeUtils.getPhysicalLength(pathElement1);
		}
		/*
		 * Never.
		 */
		return new double[2];
	}

	/**
	 * @param pathElement
	 * @throws ApplicationException
	 */
	@Shitlet
	public PathElement getPreviousNode(final PathElement pathElement) throws ApplicationException {
		assert assertContains(pathElement): CHILDREN_ALIEN;

		PathElement previousNode = null;
		for (final PathElement pathElement1 : getPathMembers().headSet(pathElement)) {
			if (pathElement1.getKind() == IdlKind.SCHEME_ELEMENT && 
					pathElement1.getSchemeElement().getProtoEquipment().getType() != MUFF) {
				previousNode = pathElement1;
			}
		}
		return previousNode;
	}

	/**
	 * @param pathElement
	 */
	public PathElement getPreviousPathElement(final PathElement pathElement) {
		assert assertContains(pathElement): CHILDREN_ALIEN;
		final SortedSet<PathElement> pathElements = getPathMembers().headSet(pathElement);
		return pathElements.isEmpty() ? null : pathElements.last();
	}

	/**
	 * @param pathElement
	 */
	public boolean hasNextPathElement(final PathElement pathElement) {
		assert assertContains(pathElement): CHILDREN_ALIEN;
		return pathElement.getSequentialNumber() < getPathMembers().size() - 1;
	}

	/**
	 * @param pathElement
	 */
	public boolean hasPreviousPathElement(final PathElement pathElement) {
		assert assertContains(pathElement): CHILDREN_ALIEN;
		return pathElement.getSequentialNumber() > 0;
	}

	/**
	 * @param totalOpticalength
	 */
	public void setTotalOpticalLength(final double totalOpticalength) {
		final SortedSet<PathElement> pathElements = getPathMembers();
		if (pathElements.isEmpty()) {
			return;
		}
		setOpticalLength(pathElements.first(), pathElements.last(), totalOpticalength);
	}

	/**
	 * @param pathElement
	 * @see SchemeCableLink#assertContains(CableChannelingItem)
	 */
	boolean assertContains(final PathElement pathElement) {
		/*
		 * The second precondition is intentionally turned off since
		 * getPathMembers() cannot always return the correct number of
		 * path members when the code is executed server-side (path
		 * members preceding the one in question may be not saved yet).
		 *
		 * Making a path member depend on its precursor (if any) may be
		 * a solution, but it'll complicate the code too much.
		 */
		return pathElement.getParentSchemePathId().equals(this)
				&& (true || this.getPathMembers().headSet(pathElement).size() == pathElement.sequentialNumber);
	}

	/**
	 * @param pathElements
	 * @param startPathElement
	 * @param endPathElement
	 * @deprecated
	 */
	@Shitlet
	@Deprecated
	private double getOpticalLength(final SortedSet<PathElement> pathElements, final PathElement startPathElement, final PathElement endPathElement) {
		double oldOpticalLength = 0;
		for (final PathElement pathElement : pathElements.tailSet(startPathElement)) {
			oldOpticalLength += SchemeUtils.getOpticalLength(pathElement);
//			if (pathElement == endPathElement) {}
			if (pathElement.equals(endPathElement)) {
				break;
			}
		}
		return oldOpticalLength;
	}

	/**
	 * @param pathElements
	 * @param startPathElement
	 * @param endPathElement
	 * @param coeff
	 * @deprecated
	 */
	@Shitlet
	@Deprecated
	private void changeOpticalLength(final SortedSet<PathElement> pathElements, final PathElement startPathElement, final PathElement endPathElement, double coeff) {
		if (Math.abs(coeff - 1) < .001) {
			return;
		}
		for (final PathElement pathElement : pathElements.tailSet(startPathElement)) {
			SchemeUtils.setOpticalLength(pathElement, SchemeUtils.getOpticalLength(pathElement) * coeff);
			if (pathElement.equals(endPathElement)) {
				break;
			}
		}
	}

	/**
	 * <code>startPathElement</code> and <code>endPathElement</code> may
	 * be swapped, but must belong to this <code>SchemePath</code>.
	 *
	 * @param startPathElement
	 * @param endPathElement
	 * @param opticalLength
	 */
	@Shitlet
	private void setOpticalLength(final PathElement startPathElement, final PathElement endPathElement, final double opticalLength) {
		final int greaterThan = endPathElement.compareTo(startPathElement);
		assert greaterThan != 0;
		if (greaterThan < 0) {
			setOpticalLength(endPathElement, startPathElement, opticalLength);
			return;
		}
		assert assertContains(startPathElement): CHILDREN_ALIEN;
		assert assertContains(endPathElement): CHILDREN_ALIEN;
		
		final SortedSet<PathElement> pathElements = getPathMembers();
		double oldOpticalLength = getOpticalLength(pathElements, startPathElement, endPathElement);
		if (oldOpticalLength == 0) {
			return;
		}
		
		final double k = opticalLength / oldOpticalLength;
		changeOpticalLength(pathElements, startPathElement, endPathElement, k);
	}

	/**
	 * <code>startPathElement</code> and <code>endPathElement</code> may
	 * be swapped, but must belong to this <code>SchemePath</code>.
	 *
	 * @param startPathElement
	 * @param endPathElement
	 * @param increment
	 */
	@Shitlet
	public void changeOpticalLength(final PathElement startPathElement, final PathElement endPathElement, final double increment) {
		final int greaterThan = endPathElement.compareTo(startPathElement);
		assert greaterThan != 0;
		if (greaterThan < 0) {
			changeOpticalLength(endPathElement, startPathElement, increment);
			return;
		}
		assert assertContains(startPathElement): CHILDREN_ALIEN;
		assert assertContains(endPathElement): CHILDREN_ALIEN;
		
		final SortedSet<PathElement> pathElements = getPathMembers();

		double oldOpticalLength = getOpticalLength(pathElements, startPathElement, endPathElement);
		if (oldOpticalLength == 0) {
			return;
		}

		final double k = (oldOpticalLength + increment) / oldOpticalLength;
		changeOpticalLength(pathElements, startPathElement, endPathElement, k);
	}
}
