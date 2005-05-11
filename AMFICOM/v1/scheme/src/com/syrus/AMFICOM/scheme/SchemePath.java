/*-
 * $Id: SchemePath.java,v 1.25 2005/05/11 13:10:18 bass Exp $
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
import java.util.SortedSet;
import java.util.TreeSet;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.TransmissionPath;
import com.syrus.AMFICOM.general.AbstractCloneableStorableObject;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Describable;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.scheme.corba.SchemePath_Transferable;
import com.syrus.AMFICOM.scheme.corba.PathElement_TransferablePackage.DataPackage.Kind;
import com.syrus.util.Log;

/**
 * #14 in hierarchy.
 *
 * @author $Author: bass $
 * @version $Revision: 1.25 $, $Date: 2005/05/11 13:10:18 $
 * @module scheme_v1
 */
public final class SchemePath extends AbstractCloneableStorableObject implements
		Describable, Characterizable {
	private static final long serialVersionUID = 3257567312831132469L;

	private String name;

	private String description;

	private Identifier transmissionPathId;

	private Identifier parentSchemeMonitoringSolutionId;

	private Set characteristics;

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
	 * @param name
	 * @param description
	 * @param transmissionPath
	 * @param parentSchemeMonitoringSolution
	 */
	SchemePath(final Identifier id, final Date created,
			final Date modified, final Identifier creatorId,
			final Identifier modifierId, final long version,
			final String name, final String description,
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
	SchemePath(final SchemePath_Transferable transferable) throws CreateObjectException {
		this.schemePathDatabase = SchemeDatabaseContext.getSchemePathDatabase();
		fromTransferable(transferable);
	}

	/**
	 * A shorthand for
	 * {@link #createInstance(Identifier, String, String, TransmissionPath, SchemeMonitoringSolution)}.
	 *
	 * @param creatorId
	 * @param name
	 * @throws CreateObjectException
	 */
	public static SchemePath createInstance(final Identifier creatorId,
			final String name)
			throws CreateObjectException {
		return createInstance(creatorId, name, "", null, null); //$NON-NLS-1$
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
			final String name, final String description,
			final TransmissionPath transmissionPath,
			final SchemeMonitoringSolution parentSchemeMonitoringSolution)
			throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid(): ErrorMessages.NON_VOID_EXPECTED;
		assert name != null && name.length() != 0: ErrorMessages.NON_EMPTY_EXPECTED;
		assert description != null: ErrorMessages.NON_NULL_EXPECTED;

		try {
			final Date created = new Date();
			final SchemePath schemePath = new SchemePath(
					IdentifierPool
							.getGeneratedIdentifier(ObjectEntities.SCHEME_PATH_ENTITY_CODE),
					created, created, creatorId, creatorId,
					0L, name, description,
					transmissionPath,
					parentSchemeMonitoringSolution);
			schemePath.changed = true;
			return schemePath;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException(
					"SchemePath.createInstance | cannot generate identifier ", ige); //$NON-NLS-1$
		}
	}

	/**
	 * @param characteristic
	 * @see com.syrus.AMFICOM.general.Characterizable#addCharacteristic(Characteristic)
	 */
	public void addCharacteristic(final Characteristic characteristic) {
		assert characteristic != null: ErrorMessages.NON_NULL_EXPECTED;
		this.characteristics.add(characteristic);
		this.changed = true;
	}

	/**
	 * Adds <code>PathElement</code> to the end of this
	 * <code>SchemePath</code>, adjusting its
	 * <code>sequentialNumber</code> accordingly.
	 *
	 * @param pathElement
	 */
	public void addPathElement(final PathElement pathElement) {
		assert pathElement != null: ErrorMessages.NON_NULL_EXPECTED;
		pathElement.setParentSchemePath(this);
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
		return Collections.unmodifiableSet(this.characteristics);
	}

	/**
	 * @see com.syrus.AMFICOM.general.Characterizable#getCharacteristicSort()
	 */
	public CharacteristicSort getCharacteristicSort() {
		return CharacteristicSort.CHARACTERISTIC_SORT_SCHEMEPATH;
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getDependencies()
	 */
	public Set getDependencies() {
		assert this.transmissionPathId != null
				&& this.parentSchemeMonitoringSolutionId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		final Set dependencies = new HashSet();
		dependencies.add(this.transmissionPathId);
		dependencies.add(this.parentSchemeMonitoringSolutionId);
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

	public SchemeMonitoringSolution getParentSchemeMonitoringSolution() {
		assert this.parentSchemeMonitoringSolutionId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		try {
			return (SchemeMonitoringSolution) SchemeStorableObjectPool.getStorableObject(this.parentSchemeMonitoringSolutionId, true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return null;
		}
	}

	public SortedSet getPathElements() {
		return Collections.unmodifiableSortedSet(new TreeSet(getPathElements0()));
	}

	/**
	 * @return child <code>PathElement</code>s in an unsorted manner.
	 */
	private Set getPathElements0() {
		try {
			return SchemeStorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(this.id, ObjectEntities.PATH_ELEMENT_ENTITY_CODE), true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return Collections.EMPTY_SET;
		}
	}

	/**
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable()
	 */
	public IDLEntity getTransferable() {
		return new SchemePath_Transferable(
				super.getHeaderTransferable(), this.name,
				this.description,
				(Identifier_Transferable) this.transmissionPathId.getTransferable(),
				(Identifier_Transferable) this.parentSchemeMonitoringSolutionId.getTransferable(),
				Identifier.createTransferables(this.characteristics));
	}

	public TransmissionPath getTransmissionPath() {
		assert this.transmissionPathId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		try {
			return (TransmissionPath) ConfigurationStorableObjectPool.getStorableObject(this.transmissionPathId, true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return null;
		}
	}

	/**
	 * @param characteristic
	 * @see com.syrus.AMFICOM.general.Characterizable#removeCharacteristic(Characteristic)
	 */
	public void removeCharacteristic(final Characteristic characteristic) {
		assert characteristic != null: ErrorMessages.NON_NULL_EXPECTED;
		assert getCharacteristics().contains(characteristic): ErrorMessages.REMOVAL_OF_AN_ABSENT_PROHIBITED;
		this.characteristics.remove(characteristic);
		this.changed = true;
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
		assert pathElement != null: ErrorMessages.NON_NULL_EXPECTED;
		assert getPathElements().contains(pathElement): ErrorMessages.REMOVAL_OF_AN_ABSENT_PROHIBITED;
		pathElement.setParentSchemePath(null);
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
			final Date modified, final Identifier creatorId,
			final Identifier modifierId, final long version,
			final String name, final String description,
			final Identifier transmissionPathId,
			final Identifier parentSchemeMonitoringSolutionId) {
		super.setAttributes(created, modified, creatorId, modifierId, version);
		
		assert name != null && name.length() != 0: ErrorMessages.NON_EMPTY_EXPECTED;
		assert description != null: ErrorMessages.NON_NULL_EXPECTED;
		assert transmissionPathId != null: ErrorMessages.NON_NULL_EXPECTED;
		assert parentSchemeMonitoringSolutionId != null: ErrorMessages.NON_NULL_EXPECTED;

		this.name = name;
		this.description = description;
		this.transmissionPathId = transmissionPathId;
		this.parentSchemeMonitoringSolutionId = parentSchemeMonitoringSolutionId;
	}

	/**
	 * @param characteristics
	 * @see com.syrus.AMFICOM.general.Characterizable#setCharacteristics(Set)
	 */
	public void setCharacteristics(final Set characteristics) {
		setCharacteristics0(characteristics);
		this.changed = true;
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

	public void setParentSchemeMonitoringSolution(final SchemeMonitoringSolution parentSchemeMonitoringSolution) {
		final Identifier newParentSchemeMonitoringSolutionId = Identifier.possiblyVoid(parentSchemeMonitoringSolution);
		if (this.parentSchemeMonitoringSolutionId.equals(newParentSchemeMonitoringSolutionId))
			return;
		this.parentSchemeMonitoringSolutionId = newParentSchemeMonitoringSolutionId;
		this.changed = true;
	}

	public void setPathElements(final SortedSet pathElements) {
		assert pathElements != null: ErrorMessages.NON_NULL_EXPECTED;
		final SortedSet oldPathElements = this.getPathElements();
		for (final Iterator oldPathElementIterator = oldPathElements.iterator(); oldPathElementIterator.hasNext();)
			/*
			 * Check is made to prevent PathElements from
			 * permanently losing their parents.
			 */
			assert !pathElements.contains(oldPathElementIterator.next());
		/*
		 * It's enough to remove the first PathElement only.
		 */
		if (!oldPathElements.isEmpty())
			this.removePathElement((PathElement) oldPathElements.first());
		for (final Iterator pathElementIterator = pathElements.iterator(); pathElementIterator.hasNext();)
			this.addPathElement((PathElement) pathElementIterator.next());
	}

	/**
	 * @param transmissionPath
	 */
	public void setTransmissionPath(final TransmissionPath transmissionPath) {
		final Identifier newTransmissionPathId = Identifier.possiblyVoid(transmissionPath);
		if (this.transmissionPathId.equals(newTransmissionPathId))
			return;
		this.transmissionPathId = newTransmissionPathId;
		this.changed = true;
	}

	/**
	 * @param transferable
	 * @throws CreateObjectException
	 * @see com.syrus.AMFICOM.general.StorableObject#fromTransferable(IDLEntity)
	 */
	protected void fromTransferable(final IDLEntity transferable) throws CreateObjectException {
		final SchemePath_Transferable schemePath = (SchemePath_Transferable) transferable;
		try {
			super.fromTransferable(schemePath.header);
			this.setCharacteristics0(GeneralStorableObjectPool.getStorableObjects(Identifier.fromTransferables(schemePath.characteristicIds), true));
		} catch (final ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
		this.name = schemePath.name;
		this.description = schemePath.description;
		this.transmissionPathId = new Identifier(schemePath.transmissionPathId);
		this.parentSchemeMonitoringSolutionId = new Identifier(schemePath.parentSchemeMonitoringSolutionId);
	}

	/*-********************************************************************
	 * Non-model members.                                                 *
	 **********************************************************************/

	/**
	 * Getter for a transient property <code>scheme</code>.
	 */
	public Scheme getScheme() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Setter for a transient property <code>scheme</code>.
	 */
	public void setScheme(final Scheme scheme) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return <code>SchemeElement</code> associated with the first
	 *         <code>PathElement</code> in this <code>SchemePath</code>.
	 */
	public SchemeElement getStartSchemeElement() {
		final SortedSet pathElements = this.getPathElements();
		assert !pathElements.isEmpty(): ErrorMessages.NON_EMPTY_EXPECTED;
		final PathElement startPathElement = (PathElement) pathElements.first();
		assert startPathElement.getKind().value() == Kind._SCHEME_ELEMENT: ErrorMessages.OBJECT_STATE_ILLEGAL;
		return startPathElement.getSchemeElement();
	}

	/**
	 * @return <code>SchemeElement</code> associated with the last
	 *         <code>PathElement</code> in this <code>SchemePath</code>.
	 */
	public SchemeElement getEndSchemeElement() {
		final SortedSet pathElements = this.getPathElements();
		assert !pathElements.isEmpty(): ErrorMessages.NON_EMPTY_EXPECTED;
		final PathElement endPathElement = (PathElement) pathElements.last();
		assert endPathElement.getKind().value() == Kind._SCHEME_ELEMENT: ErrorMessages.OBJECT_STATE_ILLEGAL;
		return endPathElement.getSchemeElement();
	}

	/**
	 * @param pathElement
	 * @deprecated
	 */
	public PathElement getNextNode(final PathElement pathElement) {
		assert assertContains(pathElement): ErrorMessages.CHILDREN_ALIEN;

		for (final Iterator pathElementIterator = getPathElements().tailSet(pathElement) .iterator(); pathElementIterator.hasNext();) {
			final PathElement pathElement1 = (PathElement) pathElementIterator.next();
			if (pathElement1.getKind().value() == Kind._SCHEME_ELEMENT && pathElement1.hasOpticalPort())
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

		if (pathElement.getKind().value() == Kind._SCHEME_ELEMENT && pathElement.hasOpticalPort())
			return pathElement;

		PathElement previousNode = null;
		for (final Iterator pathElementIterator = getPathElements().headSet(pathElement).iterator(); pathElementIterator.hasNext();) {
			final PathElement pathElement1 = (PathElement) pathElementIterator.next();
			if (pathElement1.getKind().value() == Kind._SCHEME_ELEMENT && pathElement1.hasOpticalPort())
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
	boolean assertContains(final PathElement pathElement) {
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
