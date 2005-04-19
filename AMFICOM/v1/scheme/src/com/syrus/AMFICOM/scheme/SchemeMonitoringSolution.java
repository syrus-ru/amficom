/*-
 * $Id: SchemeMonitoringSolution.java,v 1.18 2005/04/19 17:45:16 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.AbstractCloneableStorableObject;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
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
import com.syrus.AMFICOM.scheme.corba.SchemeMonitoringSolution_Transferable;
import com.syrus.util.Log;

/**
 * #06 in hierarchy.
 *
 * @author $Author: bass $
 * @version $Revision: 1.18 $, $Date: 2005/04/19 17:45:16 $
 * @module scheme_v1
 */
public final class SchemeMonitoringSolution extends
		AbstractCloneableStorableObject implements Describable {
	private static final long serialVersionUID = 3906364939487949361L;

	private String description;

	private String name;

	private Identifier parentSchemeOptimizeInfoId;

	private double price;

	private SchemeMonitoringSolutionDatabase schemeMonitoringSolutionDatabase; 

	/**
	 * @param id
	 * @throws RetrieveObjectException
	 * @throws ObjectNotFoundException
	 */
	SchemeMonitoringSolution(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);
	
		this.schemeMonitoringSolutionDatabase = SchemeDatabaseContext.getSchemeMonitoringSolutionDatabase();
		try {
			this.schemeMonitoringSolutionDatabase.retrieve(this);
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
	SchemeMonitoringSolution(Identifier id, Date created,
			Date modified, Identifier creatorId,
			Identifier modifierId, long version) {
		super(id, created, modified, creatorId, modifierId, version);
	}

	/**
	 * @param transferable
	 * @throws CreateObjectException
	 */
	SchemeMonitoringSolution(final SchemeMonitoringSolution_Transferable transferable) throws CreateObjectException {
		this.schemeMonitoringSolutionDatabase = SchemeDatabaseContext.getSchemeMonitoringSolutionDatabase();
		fromTransferable(transferable);
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
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException(
					"SchemeMonitoringSolution.createInstance | cannot generate identifier ", ige); //$NON-NLS-1$
		}
	}

	public void addSchemePath(final SchemePath schemePath) {
		assert schemePath != null: ErrorMessages.NON_NULL_EXPECTED;
		schemePath.setParentSchemeMonitoringSolution(this);
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
	 * @see com.syrus.AMFICOM.general.StorableObject#getDependencies()
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
	 * @see com.syrus.AMFICOM.general.Namable#getName()
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
		try {
			return Collections.unmodifiableSet(SchemeStorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(this.id, ObjectEntities.SCHEME_PATH_ENTITY_CODE), true));
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return Collections.EMPTY_SET;
		}
	}

	/**
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable()
	 */
	public IDLEntity getTransferable() {
		throw new UnsupportedOperationException();
	}

	public void removeSchemePath(final SchemePath schemePath) {
		assert schemePath != null: ErrorMessages.NON_NULL_EXPECTED;
		assert getSchemePaths().contains(schemePath): ErrorMessages.REMOVAL_OF_AN_ABSENT_PROHIBITED;
		schemePath.setParentSchemeMonitoringSolution(null);
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

	public void setParentSchemeOptimizeInfo(final SchemeOptimizeInfo parentSchemeOptimizeInfo) {
		throw new UnsupportedOperationException();
	}

	public void setPrice(double price) {
		throw new UnsupportedOperationException();
	}

	public void setSchemePaths(final Set schemePaths) {
		assert schemePaths != null: ErrorMessages.NON_NULL_EXPECTED;
		for (final Iterator oldSchemePathIterator = getSchemePaths().iterator(); oldSchemePathIterator.hasNext();) {
			final SchemePath oldSchemePath = (SchemePath) oldSchemePathIterator.next();
			/*
			 * Check is made to prevent SchemePaths from
			 * permanently losing their parents.
			 */
			assert !schemePaths.contains(oldSchemePath);
			removeSchemePath(oldSchemePath);
		}
		for (final Iterator schemePathIterator = schemePaths.iterator(); schemePathIterator.hasNext();)
			addSchemePath((SchemePath) schemePathIterator.next());
	}

	/**
	 * @param transferable
	 * @throws CreateObjectException
	 * @see com.syrus.AMFICOM.general.StorableObject#fromTransferable(IDLEntity)
	 */
	protected void fromTransferable(final IDLEntity transferable) throws CreateObjectException {
		throw new UnsupportedOperationException();
	}
}
