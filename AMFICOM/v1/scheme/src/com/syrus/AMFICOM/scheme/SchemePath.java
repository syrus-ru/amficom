/*-
 * $Id: SchemePath.java,v 1.69 2005/08/08 14:25:23 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

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
import static com.syrus.AMFICOM.general.ObjectEntities.PATHELEMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEMONITORINGSOLUTION_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEPATH_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.TRANSPATH_CODE;
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
import com.syrus.AMFICOM.general.CharacterizableDelegate;
import com.syrus.AMFICOM.general.CompoundCondition;
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
import com.syrus.AMFICOM.general.ReverseDependencyContainer;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.scheme.corba.IdlSchemePath;
import com.syrus.AMFICOM.scheme.corba.IdlSchemePathHelper;
import com.syrus.AMFICOM.scheme.corba.IdlPathElementPackage.IdlDataPackage.IdlKind;
import com.syrus.util.Log;

/**
 * #16 in hierarchy.
 *
 * @author $Author: arseniy $
 * @version $Revision: 1.69 $, $Date: 2005/08/08 14:25:23 $
 * @module scheme
 */
public final class SchemePath extends StorableObject
		implements Describable, Characterizable, PathOwner<PathElement>,
		ReverseDependencyContainer {
	private static final long serialVersionUID = 3257567312831132469L;

	private String name;

	private String description;

	private Identifier transmissionPathId;

	Identifier parentSchemeMonitoringSolutionId;

	private transient CharacterizableDelegate characterizableDelegate;

	/**
	 * @param id
	 * @throws RetrieveObjectException
	 * @throws ObjectNotFoundException
	 */
	SchemePath(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);
		try {
			DatabaseContext.getDatabase(SCHEMEPATH_CODE).retrieve(this);
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
	public static SchemePath createInstance(final Identifier creatorId,
			final String name,
			final String description,
			final TransmissionPath transmissionPath,
			final SchemeMonitoringSolution parentSchemeMonitoringSolution) throws CreateObjectException {
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
			schemePath.markAsChanged();
			return schemePath;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException("SchemePath.createInstance | cannot generate identifier ", ige);
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
	 * @see Characterizable#getCharacteristics()
	 */
	public Set<Characteristic> getCharacteristics(final boolean usePool) throws ApplicationException {
		if (this.characterizableDelegate == null) {
			this.characterizableDelegate = new CharacterizableDelegate(this.id);
		}
		return this.characterizableDelegate.getCharacteristics(usePool);
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
	 * @see com.syrus.AMFICOM.general.ReverseDependencyContainer#getReverseDependencies()
	 */
	public Set<Identifiable> getReverseDependencies() throws ApplicationException {
		final Set<Identifiable> reverseDependencies = new HashSet<Identifiable>();
		reverseDependencies.add(super.id);
		for (final ReverseDependencyContainer reverseDependencyContainer : this.getCharacteristics(true)) {
			reverseDependencies.addAll(reverseDependencyContainer.getReverseDependencies());
		}
		for (final ReverseDependencyContainer reverseDependencyContainer : this.getPathMembers0()) {
			reverseDependencies.addAll(reverseDependencyContainer.getReverseDependencies());
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
			return Collections.unmodifiableSortedSet(new TreeSet<PathElement>(getPathMembers0()));
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return Collections.unmodifiableSortedSet(new TreeSet<PathElement>(Collections.<PathElement>emptySet()));
		}
	}

	/**
	 * @return child <code>PathElement</code>s in an unsorted manner.
	 */
	Set<PathElement> getPathMembers0() throws ApplicationException {
		return StorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(this.id, PATHELEMENT_CODE), true);
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

	Identifier getTransmissionPathId() {
		assert this.transmissionPathId != null: OBJECT_NOT_INITIALIZED;
		assert this.transmissionPathId.isVoid() || this.transmissionPathId.getMajor() == TRANSPATH_CODE;
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
		assert pathElement.getParentSchemePathId().equals(super.id) : REMOVAL_OF_AN_ABSENT_PROHIBITED;
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
	synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final String name,
			final String description,
			final Identifier transmissionPathId,
			final Identifier parentSchemeMonitoringSolutionId) {
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

	public void setParentSchemeMonitoringSolution(final SchemeMonitoringSolution parentSchemeMonitoringSolution) {
		assert this.parentSchemeMonitoringSolutionId != null : OBJECT_NOT_INITIALIZED;
		assert !this.parentSchemeMonitoringSolutionId.isVoid() : EXACTLY_ONE_PARENT_REQUIRED;
		if (parentSchemeMonitoringSolution == null) {
			Log.debugMessage(OBJECT_WILL_DELETE_ITSELF_FROM_POOL, WARNING);
			StorableObjectPool.delete(super.id);
			return;
		}
		final Identifier newParentSchemeMonitoringSolutionId = Identifier.possiblyVoid(parentSchemeMonitoringSolution);
		if (this.parentSchemeMonitoringSolutionId.equals(newParentSchemeMonitoringSolutionId)) {
			return;
		}
		this.parentSchemeMonitoringSolutionId = newParentSchemeMonitoringSolutionId;
		super.markAsChanged();
	}

	/**
	 * @param transmissionPath
	 */
	public void setTransmissionPath(final TransmissionPath transmissionPath) {
		final Identifier newTransmissionPathId = Identifier.possiblyVoid(transmissionPath);
		if (this.transmissionPathId.equals(newTransmissionPathId))
			return;
		this.transmissionPathId = newTransmissionPathId;
		super.markAsChanged();
	}

	/**
	 * @param transferable
	 * @throws CreateObjectException
	 * @see com.syrus.AMFICOM.general.StorableObject#fromTransferable(IdlStorableObject)
	 */
	@Override
	protected synchronized void fromTransferable(final IdlStorableObject transferable) throws CreateObjectException {
		final IdlSchemePath schemePath = (IdlSchemePath) transferable;
		try {
			super.fromTransferable(schemePath);
		} catch (final ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
		this.name = schemePath.name;
		this.description = schemePath.description;
		this.transmissionPathId = new Identifier(schemePath.transmissionPathId);
		this.parentSchemeMonitoringSolutionId = new Identifier(schemePath.parentSchemeMonitoringSolutionId);
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
			@Override
			public boolean isNeedMore(final Set<? extends StorableObject> storableObjects) {
				return false;
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
		assert startPathElement.getKind().value() == IdlKind._SCHEME_ELEMENT: OBJECT_STATE_ILLEGAL;
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
		assert endPathElement.getKind().value() == IdlKind._SCHEME_ELEMENT: OBJECT_STATE_ILLEGAL;
		return endPathElement.getSchemeElement();
	}

	/**
	 * @param pathElement
	 * @deprecated
	 */
	@Deprecated
	public PathElement getNextNode(final PathElement pathElement) {
		assert assertContains(pathElement): CHILDREN_ALIEN;

		for (final PathElement pathElement1 : getPathMembers().tailSet(pathElement)) {
			if (pathElement1.getKind().value() == IdlKind._SCHEME_ELEMENT && pathElement1.hasOpticalPort()) {
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
		if (pathElements.size() == 1)
			return null;
		final Iterator<PathElement> pathElementIterator = pathElements.iterator();
		pathElementIterator.next();
		return pathElementIterator.next();
	}

	/**
	 * @param physicalDistance
	 */
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
	 */
	public double[] getOpticalDistanceFromStart(final PathElement pathElement) {
		assert assertContains(pathElement): CHILDREN_ALIEN;

		double opticalDistanceFromStart = 0;
		final SortedSet<PathElement> pathElements = getPathMembers();
		for (final PathElement pathElement1 : pathElements) {
//			if (pathElement1 == pathElement) {}
			if (pathElement1.getId().equals(pathElement.getId())) {
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
	 */
	public PathElement getPathElementByOpticalDistance(final double opticalDistance) {
		final SortedSet<PathElement> pathElements = getPathMembers();
		if (pathElements.isEmpty())
			return null;

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
	@Deprecated
	public PathElement getPathElementByPhysicalDistance(final double physicalDistance) {
		final SortedSet<PathElement> pathElements = getPathMembers();
		if (pathElements.isEmpty())
			return null;

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
	 */
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
	@Deprecated
	public double[] getPhysicalDistanceFromStart(final PathElement pathElement) {
		assert assertContains(pathElement): CHILDREN_ALIEN;

		double physicalDistanceFromStart = 0;
		final SortedSet<PathElement> pathElements = getPathMembers();
		for (final PathElement pathElement1 : pathElements) {
//			if (pathElement1 == pathElement) {}
			if (pathElement1.getId().equals(pathElement.getId())) {
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
	 * @deprecated
	 */
	@Deprecated
	public PathElement getPreviousNode(final PathElement pathElement) {
		assert assertContains(pathElement): CHILDREN_ALIEN;

		if (pathElement.getKind().value() == IdlKind._SCHEME_ELEMENT && pathElement.hasOpticalPort())
			return pathElement;

		PathElement previousNode = null;
		for (final PathElement pathElement1 : getPathMembers().headSet(pathElement)) {
			if (pathElement1.getKind().value() == IdlKind._SCHEME_ELEMENT && pathElement1.hasOpticalPort()) {
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
	 * @deprecated
	 */
	@Deprecated
	public void setTotalOpticalLength(final double totalOpticalength) {
		final SortedSet<PathElement> pathElements = getPathMembers();
		if (pathElements.isEmpty())
			return;
		setOpticalLength(pathElements.first(), pathElements.last(), totalOpticalength);
	}

	/**
	 * @param pathElement
	 * @see SchemeCableLink#assertContains(CableChannelingItem)
	 */
	boolean assertContains(final PathElement pathElement) {
		final SortedSet<PathElement> pathElements = this.getPathMembers();
		return pathElement.getParentSchemePathId().equals(super.id)
				&& pathElements.headSet(pathElement).size() == pathElement.sequentialNumber;
	}

	/**
	 * <code>startPathElement</code> and <code>endPathElement</code> may
	 * be swapped, but must belong to this <code>SchemePath</code>.
	 *
	 * @param startPathElement
	 * @param endPathElement
	 * @param opticalLength
	 */
	private void setOpticalLength(final PathElement startPathElement, final PathElement endPathElement, final double opticalLength) {
		final int greaterThan = endPathElement.compareTo(startPathElement);
		assert greaterThan != 0;
		if (greaterThan < 0) {
			setOpticalLength(endPathElement, startPathElement, opticalLength);
			return;
		}
		final SortedSet<PathElement> pathElements = getPathMembers();
		assert assertContains(startPathElement): CHILDREN_ALIEN;
		assert assertContains(endPathElement): CHILDREN_ALIEN;

		double oldOpticalLength = 0;
		for (final PathElement pathElement : pathElements.tailSet(startPathElement)) {
			oldOpticalLength += SchemeUtils.getOpticalLength(pathElement);
//			if (pathElement == endPathElement) {}
			if (pathElement.getId().equals(endPathElement.getId())) {
				break;
			}
		}
		if (oldOpticalLength == 0)
			return;
		
		final double k = opticalLength / oldOpticalLength;
		if (Math.abs(k - 1) < .001)
			return;
		for (final PathElement pathElement : pathElements.tailSet(startPathElement)) {
			SchemeUtils.setOpticalLength(pathElement, SchemeUtils.getOpticalLength(pathElement) * k);
//			if (pathElement == endPathElement) {}
			if (pathElement.getId().equals(endPathElement.getId())) {
				break;
			}
		}
	}
}
