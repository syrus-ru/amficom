/*-
 * $Id: SchemePath.java,v 1.7 2005/03/28 08:24:52 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import com.syrus.AMFICOM.configuration.TransmissionPath;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import java.util.*;

/**
 * #14 in hierarchy.
 *
 * @author $Author: bass $
 * @version $Revision: 1.7 $, $Date: 2005/03/28 08:24:52 $
 * @module scheme_v1
 */
public final class SchemePath extends AbstractCloneableStorableObject implements
		Describable, Characterizable {
	private static final long serialVersionUID = 3257567312831132469L;

	private Collection characteristics;

	private String description;

	private Identifier endSchemeElementId;

	private String name;

	private Identifier parentSchemeMonitoringSolutionId;

	private Identifier startSchemeElementId;

	private Identifier transmissionPathId;

	/**
	 * @param id
	 */
	protected SchemePath(final Identifier id) {
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
	protected SchemePath(final Identifier id, final Date created,
			final Date modified, final Identifier creatorId,
			final Identifier modifierId, final long version) {
		super(id, created, modified, creatorId, modifierId, version);
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

	public Collection getPathElements() {
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
	public Object getTransferable() {
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

	public void removePathElement(final PathElement pathElement) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param characteristics
	 * @see com.syrus.AMFICOM.general.Characterizable#setCharacteristics(java.util.Collection)
	 */
	public void setCharacteristics(Collection characteristics) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param characteristics
	 * @see com.syrus.AMFICOM.general.Characterizable#setCharacteristics0(java.util.Collection)
	 */
	public void setCharacteristics0(Collection characteristics) {
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

	public void setPathElements(final Collection pathElements) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @deprecated
	 */
	public void setPathElementsAsArray(final PathElement pathElements[]) {
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
}
