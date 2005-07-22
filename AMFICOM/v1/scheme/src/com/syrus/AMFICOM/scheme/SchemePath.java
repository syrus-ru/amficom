/*-
 * $Id: SchemePath.java,v 1.56 2005/07/22 15:09:40 bass Exp $
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
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEME_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.TRANSPATH_CODE;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Level.WARNING;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.configuration.TransmissionPath;
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
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.scheme.corba.IdlSchemePath;
import com.syrus.AMFICOM.scheme.corba.IdlSchemePathHelper;
import com.syrus.AMFICOM.scheme.corba.IdlPathElementPackage.DataPackage.Kind;
import com.syrus.util.Log;

/**
 * #14 in hierarchy.
 *
 * @author $Author: bass $
 * @version $Revision: 1.56 $, $Date: 2005/07/22 15:09:40 $
 * @module scheme_v1
 */
public final class SchemePath extends StorableObject
		implements Describable, Characterizable, Cloneable, PathOwner<PathElement> {
	private static final long serialVersionUID = 3257567312831132469L;

	private String name;

	private String description;

	private Identifier transmissionPathId;

	private Identifier parentSchemeMonitoringSolutionId;

	Identifier parentSchemeId;

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
	 * @param parentScheme
	 */
	SchemePath(final Identifier id, final Date created,
			final Date modified, final Identifier creatorId,
			final Identifier modifierId, final long version,
			final String name, final String description,
			final TransmissionPath transmissionPath,
			final SchemeMonitoringSolution parentSchemeMonitoringSolution,
			final Scheme parentScheme) {
		super(id, created, modified, creatorId, modifierId, version);
		this.name = name;
		this.description = description;
		this.transmissionPathId = Identifier.possiblyVoid(transmissionPath);
		this.parentSchemeMonitoringSolutionId = Identifier.possiblyVoid(parentSchemeMonitoringSolution);
		this.parentSchemeId = Identifier.possiblyVoid(parentScheme);
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
	 * {@link #createInstance(Identifier, String, String, TransmissionPath, SchemeMonitoringSolution, Scheme)}.
	 *
	 * @param creatorId
	 * @param name
	 * @param parentScheme
	 * @throws CreateObjectException
	 */
	public static SchemePath createInstance(final Identifier creatorId,
			final String name, final Scheme parentScheme)
			throws CreateObjectException {
		return createInstance(creatorId, name, "", null, null, parentScheme);
	}

	/**
	 * @param creatorId
	 * @param name
	 * @param description
	 * @param transmissionPath
	 * @param parentSchemeMonitoringSolution
	 * @param parentScheme
	 * @throws CreateObjectException
	 */
	public static SchemePath createInstance(final Identifier creatorId,
			final String name, final String description,
			final TransmissionPath transmissionPath,
			final SchemeMonitoringSolution parentSchemeMonitoringSolution,
			final Scheme parentScheme)
			throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid(): NON_VOID_EXPECTED;
		assert name != null && name.length() != 0: NON_EMPTY_EXPECTED;
		assert description != null: NON_NULL_EXPECTED;
		assert parentScheme != null : NON_NULL_EXPECTED;
		assert parentSchemeMonitoringSolution == null
				|| parentSchemeMonitoringSolution.getParentSchemeOptimizeInfo() == null
				|| parentSchemeMonitoringSolution.getParentSchemeOptimizeInfo()
						.getParentScheme().getId()
						.equals(parentScheme.getId());

		try {
			final Date created = new Date();
			final SchemePath schemePath = new SchemePath(
					IdentifierPool.getGeneratedIdentifier(SCHEMEPATH_CODE),
					created, created, creatorId, creatorId,
					0L, name, description,
					transmissionPath,
					parentSchemeMonitoringSolution,
					parentScheme);
			schemePath.markAsChanged();
			return schemePath;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException(
					"SchemePath.createInstance | cannot generate identifier ", ige);
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

	@Override
	public SchemePath clone() throws CloneNotSupportedException {
		final SchemePath schemePath = (SchemePath) super.clone();
		/**
		 * @todo Update the newly created object.
		 */
		return schemePath;
	}

	/**
	 * @throws ApplicationException 
	 * @see com.syrus.AMFICOM.general.Characterizable#getCharacteristics()
	 */
	public Set<Characteristic> getCharacteristics() throws ApplicationException {
		final LinkedIdsCondition lic = new LinkedIdsCondition(this.id, ObjectEntities.CHARACTERISTIC_CODE);
		final Set<Characteristic> characteristics = StorableObjectPool.getStorableObjectsByCondition(lic, true);
		return characteristics;
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getDependencies()
	 */
	@Override
	public Set<Identifiable> getDependencies() {
		assert this.transmissionPathId != null
				&& this.parentSchemeMonitoringSolutionId != null: OBJECT_NOT_INITIALIZED;
		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.add(this.transmissionPathId);
		dependencies.add(this.parentSchemeMonitoringSolutionId);
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

	Identifier getParentSchemeMonitoringSolutionId() {
		assert this.parentSchemeMonitoringSolutionId != null: OBJECT_NOT_INITIALIZED;
		assert this.parentSchemeMonitoringSolutionId.isVoid() || this.parentSchemeMonitoringSolutionId.getMajor() == SCHEMEMONITORINGSOLUTION_CODE;
		return this.parentSchemeMonitoringSolutionId;
	}

	/**
	 * A wrapper around {@link #getParentSchemeMonitoringSolutionId()}.
	 */
	public SchemeMonitoringSolution getParentSchemeMonitoringSolution() {
		try {
			return (SchemeMonitoringSolution) StorableObjectPool.getStorableObject(this.getParentSchemeMonitoringSolutionId(), true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return null;
		}
	}

	Identifier getParentSchemeId() {
		assert this.parentSchemeId != null : OBJECT_NOT_INITIALIZED;
		assert !this.parentSchemeId.isVoid() : EXACTLY_ONE_PARENT_REQUIRED;
		assert this.parentSchemeId.getMajor() == SCHEME_CODE;
		return this.parentSchemeId;
	}

	/**
	 * A wrapper around {@link #getParentSchemeId()}.
	 */
	public Scheme getParentScheme() {
		try {
			return (Scheme) StorableObjectPool.getStorableObject(this.getParentSchemeId(), true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return null;
		}
	}

	public SortedSet<PathElement> getPathMembers() {
		return Collections.unmodifiableSortedSet(new TreeSet<PathElement>(getPathMembers0()));
	}

	/**
	 * @return child <code>PathElement</code>s in an unsorted manner.
	 */
	private Set<PathElement> getPathMembers0() {
		try {
			return StorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(this.id, PATHELEMENT_CODE), true, true);
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
	public IdlSchemePath getTransferable(final ORB orb) {
		return IdlSchemePathHelper.init(orb,
				this.id.getTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getTransferable(),
				this.modifierId.getTransferable(),
				this.version, this.name,
				this.description,
				this.transmissionPathId.getTransferable(),
				this.parentSchemeMonitoringSolutionId.getTransferable(),
				this.parentSchemeId.getTransferable());
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
			return (TransmissionPath) StorableObjectPool.getStorableObject(this.getTransmissionPathId(), true);
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
	 * @param parentSchemeId
	 */
	synchronized void setAttributes(final Date created,
			final Date modified, final Identifier creatorId,
			final Identifier modifierId, final long version,
			final String name, final String description,
			final Identifier transmissionPathId,
			final Identifier parentSchemeMonitoringSolutionId,
			final Identifier parentSchemeId) {
		super.setAttributes(created, modified, creatorId, modifierId, version);
		
		assert name != null && name.length() != 0: NON_EMPTY_EXPECTED;
		assert description != null: NON_NULL_EXPECTED;
		assert transmissionPathId != null: NON_NULL_EXPECTED;
		assert parentSchemeMonitoringSolutionId != null: NON_NULL_EXPECTED;
		assert parentSchemeId != null && !parentSchemeId.isVoid() : NON_VOID_EXPECTED;

		this.name = name;
		this.description = description;
		this.transmissionPathId = transmissionPathId;
		this.parentSchemeMonitoringSolutionId = parentSchemeMonitoringSolutionId;
		this.parentSchemeId = parentSchemeId;
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
		final Identifier newParentSchemeMonitoringSolutionId = Identifier.possiblyVoid(parentSchemeMonitoringSolution);
		if (this.parentSchemeMonitoringSolutionId.equals(newParentSchemeMonitoringSolutionId))
			return;
		this.parentSchemeMonitoringSolutionId = newParentSchemeMonitoringSolutionId;
		super.markAsChanged();
	}

	public void setParentScheme(final Scheme parentScheme) {
		assert this.parentSchemeId != null : OBJECT_NOT_INITIALIZED;
		assert !this.parentSchemeId.isVoid() : EXACTLY_ONE_PARENT_REQUIRED;
		if (parentScheme == null) {
			Log.debugMessage(OBJECT_WILL_DELETE_ITSELF_FROM_POOL, WARNING);
			StorableObjectPool.delete(super.id);
			return;
		}
		final Identifier newParentSchemeId = Identifier.possiblyVoid(parentScheme);
		if (this.parentSchemeId.equals(newParentSchemeId)) {
			return;
		}
		this.parentSchemeId = newParentSchemeId;
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
	protected void fromTransferable(final IdlStorableObject transferable) throws CreateObjectException {
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
		this.parentSchemeId = new Identifier(schemePath.parentSchemeId);
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
		assert startPathElement.getKind().value() == Kind._SCHEME_ELEMENT: OBJECT_STATE_ILLEGAL;
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
		assert endPathElement.getKind().value() == Kind._SCHEME_ELEMENT: OBJECT_STATE_ILLEGAL;
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
			if (pathElement1.getKind().value() == Kind._SCHEME_ELEMENT && pathElement1.hasOpticalPort()) {
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

		if (pathElement.getKind().value() == Kind._SCHEME_ELEMENT && pathElement.hasOpticalPort())
			return pathElement;

		PathElement previousNode = null;
		for (final PathElement pathElement1 : getPathMembers().headSet(pathElement)) {
			if (pathElement1.getKind().value() == Kind._SCHEME_ELEMENT && pathElement1.hasOpticalPort()) {
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
		return pathElements.isEmpty() ? null : (PathElement) pathElements.last();
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
		int greaterThan = endPathElement.compareTo(startPathElement);
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
			if (pathElement == endPathElement)
				break;
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
