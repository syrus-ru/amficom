/*-
 * $Id: SchemeMonitoringSolution.java,v 1.24 2005/05/10 17:07:52 bass Exp $
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
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.scheme.corba.SchemeMonitoringSolution_Transferable;
import com.syrus.util.Log;

/**
 * #06 in hierarchy.
 *
 * @author $Author: bass $
 * @version $Revision: 1.24 $, $Date: 2005/05/10 17:07:52 $
 * @module scheme_v1
 */
public final class SchemeMonitoringSolution extends
		AbstractCloneableStorableObject implements Describable {
	private static final long serialVersionUID = 3906364939487949361L;

	private String name;

	private String description;

	private int price;

	/**
	 * May be void, as <code>SchemeMonitoringSolution</code> may be used
	 * just as a storage for {@link SchemePath}s.
	 */
	private Identifier parentSchemeOptimizeInfoId;

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
	 * @param name
	 * @param description
	 * @param price
	 * @param parentSchemeOptimizeInfo
	 */
	SchemeMonitoringSolution(final Identifier id, final Date created,
			final Date modified, final Identifier creatorId,
			final Identifier modifierId, final long version,
			final String name, final String description,
			final int price,
			final SchemeOptimizeInfo parentSchemeOptimizeInfo) {
		super(id, created, modified, creatorId, modifierId, version);
		this.name = name;
		this.description = description;
		this.price = price;
		this.parentSchemeOptimizeInfoId = Identifier.possiblyVoid(parentSchemeOptimizeInfo);
	}

	/**
	 * @param transferable
	 */
	SchemeMonitoringSolution(final SchemeMonitoringSolution_Transferable transferable) {
		this.schemeMonitoringSolutionDatabase = SchemeDatabaseContext.getSchemeMonitoringSolutionDatabase();
		fromTransferable(transferable);
	}

	/**
	 * A shorthand for
	 * {@link #createInstance(Identifier, String, String, int, SchemeOptimizeInfo)}.
	 *
	 * @param creatorId
	 * @param name
	 * @throws CreateObjectException
	 */
	public static SchemeMonitoringSolution createInstance(
			final Identifier creatorId, final String name)
			throws CreateObjectException {
		return createInstance(creatorId, name, "", 0, null); //$NON-NLS-1$
	}	

	/**
	 * @param creatorId
	 * @param name
	 * @param description
	 * @param price
	 * @param parentSchemeOptimizeInfo can be <code>null</code>.
	 * @throws CreateObjectException
	 */
	public static SchemeMonitoringSolution createInstance(
			final Identifier creatorId, final String name,
			final String description, final int price,
			final SchemeOptimizeInfo parentSchemeOptimizeInfo)
			throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid(): ErrorMessages.NON_VOID_EXPECTED;
		assert name != null && name.length() != 0: ErrorMessages.NON_EMPTY_EXPECTED;
		assert description != null: ErrorMessages.NON_NULL_EXPECTED;

		try {
			final Date created = new Date();
			final SchemeMonitoringSolution schemeMonitoringSolution = new SchemeMonitoringSolution(
					IdentifierPool
							.getGeneratedIdentifier(ObjectEntities.SCHEME_MONITORING_SOLUTION_ENTITY_CODE),
					created, created, creatorId, creatorId,
					0L, name, description, price, parentSchemeOptimizeInfo);
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
		assert this.parentSchemeOptimizeInfoId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		final Set dependencies = new HashSet();
		dependencies.add(this.parentSchemeOptimizeInfoId);
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

	/**
	 * @return <code>SchemeOptimizeInfo</code> parent for this
	 *         <code>SchemeMonitoringSolution</code>, or <code>null</code>
	 *         if none.
	 */
	public SchemeOptimizeInfo getParentSchemeOptimizeInfo() {
		assert this.parentSchemeOptimizeInfoId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;

		try {
			return (SchemeOptimizeInfo) SchemeStorableObjectPool.getStorableObject(this.parentSchemeOptimizeInfoId, true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return null;
		}
	}

	public int getPrice() {
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
		return new SchemeMonitoringSolution_Transferable(
				super.getHeaderTransferable(), this.name,
				this.description, this.price,
				(Identifier_Transferable) this.parentSchemeOptimizeInfoId.getTransferable());
	}

	public void removeSchemePath(final SchemePath schemePath) {
		assert schemePath != null: ErrorMessages.NON_NULL_EXPECTED;
		assert getSchemePaths().contains(schemePath): ErrorMessages.REMOVAL_OF_AN_ABSENT_PROHIBITED;
		schemePath.setParentSchemeMonitoringSolution(null);
	}

	/**
	 * @param created
	 * @param modified
	 * @param creatorId
	 * @param modifierId
	 * @param version
	 * @param name
	 * @param description
	 * @param price
	 * @param parentSchemeOptimizeInfoId
	 */
	synchronized void setAttributes(final Date created,
			final Date modified, final Identifier creatorId,
			final Identifier modifierId, final long version,
			final String name, final String description,
			final int price,
			final Identifier parentSchemeOptimizeInfoId) {
		super.setAttributes(created, modified, creatorId, modifierId, version);

		assert name != null && name.length() != 0: ErrorMessages.NON_EMPTY_EXPECTED;
		assert description != null: ErrorMessages.NON_NULL_EXPECTED;
		assert parentSchemeOptimizeInfoId != null: ErrorMessages.NON_NULL_EXPECTED;

		this.name = name;
		this.description = description;
		this.price = price;
		this.parentSchemeOptimizeInfoId = parentSchemeOptimizeInfoId;
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
		final Identifier newParentSchemeOptimizeInfoId = Identifier.possiblyVoid(parentSchemeOptimizeInfo);
		if (this.parentSchemeOptimizeInfoId.equals(newParentSchemeOptimizeInfoId))
			return;
		this.parentSchemeOptimizeInfoId = newParentSchemeOptimizeInfoId;
		this.changed = true;
	}

	public void setPrice(final int price) {
		if (this.price == price)
			return;
		this.price = price;
		this.changed = true;
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
	 * @see com.syrus.AMFICOM.general.StorableObject#fromTransferable(IDLEntity)
	 */
	protected void fromTransferable(final IDLEntity transferable) {
		final SchemeMonitoringSolution_Transferable schemeMonitoringSolution = (SchemeMonitoringSolution_Transferable) transferable;
		try {
			super.fromTransferable(schemeMonitoringSolution.header);
		} catch (final ApplicationException ae) {
			/*
			 * Never.
			 */
			assert false;
		}
		this.name = schemeMonitoringSolution.name;
		this.description = schemeMonitoringSolution.description;
		this.price = schemeMonitoringSolution.priceUsd;
		this.parentSchemeOptimizeInfoId = new Identifier(schemeMonitoringSolution.parentSchemeOptimizeInfoId);
	}
}
