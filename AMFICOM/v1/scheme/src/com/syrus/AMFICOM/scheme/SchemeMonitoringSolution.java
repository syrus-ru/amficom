/*-
 * $Id: SchemeMonitoringSolution.java,v 1.6 2005/03/25 10:15:12 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import com.syrus.AMFICOM.general.*;
import java.util.*;

/**
 * #06 in hierarchy.
 *
 * @author $Author: bass $
 * @version $Revision: 1.6 $, $Date: 2005/03/25 10:15:12 $
 * @module scheme_v1
 */
public final class SchemeMonitoringSolution extends
		AbstractCloneableStorableObject implements Describable {
	private static final long serialVersionUID = 3906364939487949361L;

	protected Identifier schemeId = null;

	protected Identifier schemePathIds[] = null;

	protected double thisPrice = 0;

	private String description;

	private String name;

	private Identifier parentSchemeOptimizeInfoId;

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

	/**
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable()
	 */
	public Object getTransferable() {
		throw new UnsupportedOperationException();
	}

	public double price() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newPrice
	 * @see com.syrus.AMFICOM.scheme.SchemeMonitoringSolution#price(double)
	 */
	public void price(double newPrice) {
		throw new UnsupportedOperationException();
	}

	public Scheme scheme() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newScheme
	 * @see com.syrus.AMFICOM.scheme.SchemeMonitoringSolution#setParentScheme(com.syrus.AMFICOM.scheme.corba.Scheme)
	 */
	public void scheme(Scheme newScheme) {
		throw new UnsupportedOperationException();
	}

	public SchemePath[] schemePaths() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newSchemePaths
	 * @see com.syrus.AMFICOM.scheme.SchemeMonitoringSolution#schemePaths(com.syrus.AMFICOM.scheme.corba.SchemePath[])
	 */
	public void schemePaths(SchemePath[] newSchemePaths) {
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
}
