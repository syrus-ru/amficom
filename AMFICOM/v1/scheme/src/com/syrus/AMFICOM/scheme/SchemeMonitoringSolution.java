/*-
 * $Id: SchemeMonitoringSolution.java,v 1.10 2005/04/04 13:17:21 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import com.syrus.AMFICOM.general.*;
import java.util.*;
import org.omg.CORBA.portable.IDLEntity;

/**
 * #06 in hierarchy.
 *
 * @author $Author: bass $
 * @version $Revision: 1.10 $, $Date: 2005/04/04 13:17:21 $
 * @module scheme_v1
 */
public final class SchemeMonitoringSolution extends
		AbstractCloneableStorableObject implements Describable {
	private static final long serialVersionUID = 3906364939487949361L;

	private String description;

	private String name;

	private Identifier parentSchemeOptimizeInfoId;

	private double price;

	/**
	 * @param id
	 */
	protected SchemeMonitoringSolution(Identifier id) {
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
	protected SchemeMonitoringSolution(Identifier id, Date created,
			Date modified, Identifier creatorId,
			Identifier modifierId, long version) {
		super(id, created, modified, creatorId, modifierId, version);
	}

	public static SchemeMonitoringSolution createInstance(
			final Identifier creatorId)
			throws CreateObjectException {
		assert creatorId != null;
		try {
			final Date created = new Date();
			final SchemeMonitoringSolution schemeMonitoringSolution = new SchemeMonitoringSolution(
					IdentifierPool
							.getGeneratedIdentifier(ObjectEntities.SCHEME_MONITORING_SOLUTION_ENTITY_CODE),
					created, created, creatorId, creatorId,
					0L);
			schemeMonitoringSolution.changed = true;
			return schemeMonitoringSolution;
		} catch (final IllegalObjectEntityException ioee) {
			throw new CreateObjectException(
					"SchemeMonitoringSolution.createInstance | cannot generate identifier ", ioee); //$NON-NLS-1$
		}
	}

	public void addSchemePath(final SchemePath schemePath) {
		throw new UnsupportedOperationException();
	}

	public Object clone() {
		final SchemeMonitoringSolution schemeMonitoringSolution = (SchemeMonitoringSolution) super
				.clone();
		/**
		 * @todo Update the newly created object.
		 */
		return schemeMonitoringSolution;
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

	public SchemeOptimizeInfo getParentSchemeOptimizeInfo() {
		throw new UnsupportedOperationException();
	}

	public double getPrice() {
		return this.price;
	}

	public Set getSchemePaths() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @deprecated
	 */
	public SchemePath[] getSchemePathsAsArray() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see TransferableObject#getTransferable()
	 */
	public IDLEntity getTransferable() {
		throw new UnsupportedOperationException();
	}

	public void removeSchemePath(final SchemePath schemePath) {
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

	public void setParentSchemeOptimizeInfo(final SchemeOptimizeInfo parentSchemeOptimizeInfo) {
		throw new UnsupportedOperationException();
	}

	public void setPrice(double price) {
		throw new UnsupportedOperationException();
	}

	public void setSchemePaths(final Set schemePaths) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @deprecated
	 */
	public void setSchemePathsAsArray(final SchemePath schemePaths[]) {
		throw new UnsupportedOperationException();
	}
}
