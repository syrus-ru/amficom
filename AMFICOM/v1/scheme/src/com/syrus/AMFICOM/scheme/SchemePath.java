/*-
 * $Id: SchemePath.java,v 1.12 2005/04/08 09:26:11 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import com.syrus.AMFICOM.configuration.TransmissionPath;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import com.syrus.AMFICOM.scheme.corba.PathElementKind;
import com.syrus.AMFICOM.scheme.corba.SchemePath_Transferable;

import java.util.*;

import org.omg.CORBA.portable.IDLEntity;

/**
 * #14 in hierarchy.
 *
 * @author $Author: bass $
 * @version $Revision: 1.12 $, $Date: 2005/04/08 09:26:11 $
 * @module scheme_v1
 */
public final class SchemePath extends AbstractCloneableStorableObject implements
		Describable, Characterizable {
	private static final long serialVersionUID = 3257567312831132469L;

	private Set characteristics;

	private String description;

	private Identifier endSchemeElementId;

	private String name;

	private Identifier parentSchemeMonitoringSolutionId;

	private Identifier startSchemeElementId;

	private Identifier transmissionPathId;

	private SchemePathDatabase schemePathDatabase;

	/**
	 * @param id
	 * @throws RetrieveObjectException
	 * @throws ObjectNotFoundException
	 */
	SchemePath(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);
		
		this.characteristics = new HashSet();
		this.schemePathDatabase = SchemeDatabaseContext.getSchemePathDatabase();
		try {
			this.schemePathDatabase.retrieve(this);
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
	SchemePath(final Identifier id, final Date created,
			final Date modified, final Identifier creatorId,
			final Identifier modifierId, final long version) {
		super(id, created, modified, creatorId, modifierId, version);
	}

	/**
	 * @param transferable
	 * @throws CreateObjectException
	 */
	SchemePath(final SchemePath_Transferable transferable) throws CreateObjectException {
		this.schemePathDatabase = SchemeDatabaseContext.getSchemePathDatabase();
		fromTransferable(transferable);
	}

	/**
	 * @deprecated Use {@link #createInstance(Identifier)}instead.
	 */
	public static SchemePath createInstance() {
		throw new UnsupportedOperationException();
	}

	public static SchemePath createInstance(final Identifier creatorId)
			throws CreateObjectException {
		assert creatorId != null;
		try {
			final Date created = new Date();
			final SchemePath schemePath = new SchemePath(
					IdentifierPool
							.getGeneratedIdentifier(ObjectEntities.SCHEME_PATH_ENTITY_CODE),
					created, created, creatorId, creatorId,
					0L);
			schemePath.changed = true;
			return schemePath;
		} catch (final IllegalObjectEntityException ioee) {
			throw new CreateObjectException(
					"SchemePath.createInstance | cannot generate identifier ", ioee); //$NON-NLS-1$
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
	 * Adds <code>PathElement</code> to the end of this
	 * <code>SchemePath</code>, adjusting its
	 * <code>sequentialNumber</code> accordingly.
	 *
	 * @param pathElement
	 */
	public void addPathElement(final PathElement pathElement) {
		throw new UnsupportedOperationException();
	}

	public Object clone() {
		final SchemePath schemePath = (SchemePath) super.clone();
		/**
		 * @todo Update the newly created object.
		 */
		return schemePath;
	}

	/**
	 * @see com.syrus.AMFICOM.general.Characterizable#getCharacteristics()
	 */
	public Set getCharacteristics() {
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

	public SchemeElement getEndSchemeElement() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see Namable#getName()
	 */
	public String getName() {
		assert this.name != null && this.name.length() != 0 : ErrorMessages.OBJECT_NOT_INITIALIZED;
		return this.name;
	}

	public SchemeMonitoringSolution getParentSchemeMonitoringSolution() {
		throw new UnsupportedOperationException();
	}

	public SortedSet getPathElements() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @deprecated
	 */
	public PathElement[] getPathElementsAsArray() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Getter for a transient property <code>scheme</code>.
	 */
	public Scheme getScheme() {
		throw new UnsupportedOperationException();
	}

	public SchemeElement getStartSchemeElement() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable()
	 */
	public IDLEntity getTransferable() {
		throw new UnsupportedOperationException();
	}

	public TransmissionPath getTransmissionPath() {
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
	 * Removes the <code>PathElement</code> from this
	 * <code>SchemePath</code>, changing its
	 * <code>sequentialNumber</code> to <code>0</code> and removing all
	 * its subsequent <code>PathElement</code>s.
	 *
	 * @param pathElement
	 */
	public void removePathElement(final PathElement pathElement) {
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

	public void setEndSchemeElement(final SchemeElement endSchemeElement) {
		throw new UnsupportedOperationException();
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

	public void setParentSchemeMonitoringSolution(final SchemeMonitoringSolution parentSchemeMonitoringSolution) {
		throw new UnsupportedOperationException();
	}

	public void setPathElements(final SortedSet pathElements) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Setter for a transient property <code>scheme</code>.
	 */
	public void setScheme(final Scheme scheme) {
		throw new UnsupportedOperationException();
	}

	public void setStartSchemeElement(final SchemeElement startSchemeElement) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newPathImpl
	 */
	public void setTransmissionPath(TransmissionPath newPathImpl) {
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

	/*-********************************************************************
	 * Non-model methods.                                                 *
	 **********************************************************************/

	/**
	 * @param pathElement
	 * @deprecated
	 */
	public PathElement getNextNode(final PathElement pathElement) {
		assert assertContains(pathElement): ErrorMessages.CHILDREN_ALIEN;

		for (final Iterator pathElementIterator = getPathElements().tailSet(pathElement) .iterator(); pathElementIterator.hasNext();) {
			final PathElement pathElement1 = (PathElement) pathElementIterator.next();
			if (pathElement1.getPathElementKind().value() == PathElementKind._SCHEME_ELEMENT && pathElement1.hasOpticalPort())
				return pathElement1;
		}
		return null;
	}

	/**
	 * @param pathElement
	 */
	public PathElement getNextPathElement(final PathElement pathElement) {
		assert assertContains(pathElement): ErrorMessages.CHILDREN_ALIEN;

		final SortedSet pathElements  = getPathElements().tailSet(pathElement);
		if (pathElements.size() == 1)
			return null;
		final Iterator pathElementIterator = pathElements.iterator();
		pathElementIterator.next();
		return (PathElement) pathElementIterator.next();
	}

	/**
	 * @param physicalDistance
	 */
	public double getOpticalDistance(final double physicalDistance) {
		double opticalDistance = .0;
		double d = .0;
		for (final Iterator pathElementIterator = getPathElements().iterator(); pathElementIterator.hasNext();) {
			final PathElement pathElement = (PathElement) pathElementIterator.next();
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
		assert assertContains(pathElement): ErrorMessages.CHILDREN_ALIEN;

		double opticalDistanceFromStart = 0;
		final SortedSet pathElements = getPathElements();
		for (final Iterator pathElementIterator = pathElements.iterator(); pathElementIterator.hasNext();) {
			final PathElement pathElement1 = (PathElement) pathElementIterator.next();
			if (pathElement1 == pathElement)
				return new double[]{opticalDistanceFromStart, opticalDistanceFromStart + SchemeUtils.getOpticalLength(pathElement1)};
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
		final SortedSet pathElements = getPathElements();
		if (pathElements.isEmpty())
			return null;

		double opticalLength = 0;
		for (final Iterator pathElementIterator = pathElements.iterator(); pathElementIterator.hasNext();) {
			final PathElement pathElement = (PathElement) pathElementIterator.next();
			opticalLength += SchemeUtils.getOpticalLength(pathElement);
			if (opticalLength >= opticalDistance)
				return pathElement;
		}
		return (PathElement) pathElements.last();
	}

	/**
	 * @param physicalDistance
	 * @deprecated
	 */
	public PathElement getPathElementByPhysicalDistance(final double physicalDistance) {
		final SortedSet pathElements = getPathElements();
		if (pathElements.isEmpty())
			return null;

		double physicalLength = 0;
		for (final Iterator pathElementIterator = pathElements.iterator(); pathElementIterator.hasNext();) {
			final PathElement pathElement = (PathElement) pathElementIterator.next();
			physicalLength += SchemeUtils.getPhysicalLength(pathElement);
			if (physicalLength >= physicalDistance)
				return pathElement;
		}
		return (PathElement) pathElements.last();
	}

	/**
	 * @param opticalDistance
	 */
	public double getPhysicalDistance(final double opticalDistance) {
		double physicalDistance = .0;
		double d = .0;
		for (final Iterator pathElementIterator = getPathElements().iterator(); pathElementIterator.hasNext();) {
			final PathElement pathElement = (PathElement) pathElementIterator.next();
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
	public double[] getPhysicalDistanceFromStart(final PathElement pathElement) {
		assert assertContains(pathElement): ErrorMessages.CHILDREN_ALIEN;

		double physicalDistanceFromStart = 0;
		final SortedSet pathElements = getPathElements();
		for (final Iterator pathElementIterator = pathElements.iterator(); pathElementIterator.hasNext();) {
			final PathElement pathElement1 = (PathElement) pathElementIterator.next();
			if (pathElement1 == pathElement)
				return new double[]{physicalDistanceFromStart, physicalDistanceFromStart + SchemeUtils.getPhysicalLength(pathElement1)};
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
	public PathElement getPreviousNode(final PathElement pathElement) {
		assert assertContains(pathElement): ErrorMessages.CHILDREN_ALIEN;

		if (pathElement.getPathElementKind().value() == PathElementKind._SCHEME_ELEMENT && pathElement.hasOpticalPort())
			return pathElement;

		PathElement previousNode = null;
		for (final Iterator pathElementIterator = getPathElements().headSet(pathElement).iterator(); pathElementIterator.hasNext();) {
			final PathElement pathElement1 = (PathElement) pathElementIterator.next();
			if (pathElement1.getPathElementKind().value() == PathElementKind._SCHEME_ELEMENT && pathElement1.hasOpticalPort())
				previousNode = pathElement1;
		}
		return previousNode;
	}

	/**
	 * @param pathElement
	 */
	public PathElement getPreviousPathElement(final PathElement pathElement) {
		assert assertContains(pathElement): ErrorMessages.CHILDREN_ALIEN;
		final SortedSet pathElements = getPathElements().headSet(pathElement); 
		return pathElements.isEmpty() ? null : (PathElement) pathElements.last();
	}

	/**
	 * @param pathElement
	 */
	public boolean hasNextPathElement(final PathElement pathElement) {
		assert assertContains(pathElement): ErrorMessages.CHILDREN_ALIEN;
		return pathElement.getSequentialNumber() < getPathElements().size() - 1;
	}

	/**
	 * @param pathElement
	 */
	public boolean hasPreviousPathElement(final PathElement pathElement) {
		assert assertContains(pathElement): ErrorMessages.CHILDREN_ALIEN;
		return pathElement.getSequentialNumber() > 0;
	}

	/**
	 * @param totalOpticalength
	 * @deprecated
	 */
	public void setTotalOpticalLength(final double totalOpticalength) {
		final SortedSet pathElements = getPathElements();
		if (pathElements.isEmpty())
			return;
		setOpticalLength((PathElement) pathElements.first(), (PathElement) pathElements.last(), totalOpticalength);
	}

	/**
	 * @param pathElement
	 */
	private boolean assertContains(final PathElement pathElement) {
		final SortedSet pathElements = getPathElements();
		return pathElements.contains(pathElement)
				&& pathElements.headSet(pathElement).size() == pathElement.getSequentialNumber();
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
		final SortedSet pathElements = getPathElements();
		assert assertContains(startPathElement): ErrorMessages.CHILDREN_ALIEN;
		assert assertContains(endPathElement): ErrorMessages.CHILDREN_ALIEN;

		double oldOpticalLength = 0;
		for (final Iterator pathElementIterator = pathElements.tailSet(startPathElement).iterator(); pathElementIterator.hasNext();) {
			final PathElement pathElement = (PathElement) pathElementIterator.next();
			oldOpticalLength += SchemeUtils.getOpticalLength(pathElement);
			if (pathElement == endPathElement)
				break;
		}
		if (oldOpticalLength == 0)
			return;
		
		final double k = opticalLength / oldOpticalLength;
		if (Math.abs(k - 1) < .001)
			return;
		for (final Iterator pathElementIterator = pathElements.tailSet(startPathElement).iterator(); pathElementIterator.hasNext();) {
			final PathElement pathElement = (PathElement) pathElementIterator.next();
			SchemeUtils.setOpticalLength(pathElement, SchemeUtils.getOpticalLength(pathElement) * k);
			if (pathElement == endPathElement)
				break;
		}
	}
}
