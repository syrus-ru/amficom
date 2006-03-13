/*-
 * $Id: SchemeOptimizeInfoRtu.java,v 1.40 2006/03/13 13:54:01 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import static com.syrus.AMFICOM.general.ErrorMessages.EXACTLY_ONE_PARENT_REQUIRED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_EMPTY_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_VOID_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_NOT_INITIALIZED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_WILL_DELETE_ITSELF_FROM_POOL;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEOPTIMIZEINFORTU_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEOPTIMIZEINFO_CODE;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Level.WARNING;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.Namable;
import com.syrus.AMFICOM.general.ReverseDependencyContainer;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeOptimizeInfoRtu;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeOptimizeInfoRtuHelper;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeOptimizeInfoRtu;
import com.syrus.util.Log;
import com.syrus.util.transport.xml.XmlConversionException;
import com.syrus.util.transport.xml.XmlTransferableObject;

/**
 * #07 in hierarchy.
 *
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.40 $, $Date: 2006/03/13 13:54:01 $
 * @module scheme
 */
public final class SchemeOptimizeInfoRtu
		extends StorableObject
		implements Namable, ReverseDependencyContainer,
		XmlTransferableObject<XmlSchemeOptimizeInfoRtu> {
	private static final long serialVersionUID = 6687067380421014690L;

	private String name;

	private int priceUsd;

	private float rangeDb;

	Identifier parentSchemeOptimizeInfoId;

	SchemeOptimizeInfoRtu(final Identifier id,
			final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final String name,
			final int priceUsd,
			final float rangeDb,
			final SchemeOptimizeInfo parentSchemeOptimizeInfo) {
		super(id, created, modified, creatorId, modifierId, version);
		this.name = name;
		this.priceUsd = priceUsd;
		this.rangeDb = rangeDb;
		this.parentSchemeOptimizeInfoId = Identifier.possiblyVoid(parentSchemeOptimizeInfo);
	}

	public SchemeOptimizeInfoRtu(final IdlSchemeOptimizeInfoRtu transferable) {
		this.fromTransferable(transferable);
	}

	/**
	 * A shorthand for
	 * {@link #createInstance(Identifier, String, int, float, SchemeOptimizeInfo)}.
	 *
	 * @param creatorId
	 * @param name
	 * @param parentSchemeOptimizeInfo
	 * @throws CreateObjectException
	 */
	public static SchemeOptimizeInfoRtu createInstance(final Identifier creatorId,
			final String name,
			final SchemeOptimizeInfo parentSchemeOptimizeInfo)
	throws CreateObjectException {
		return createInstance(creatorId, name, 0, 0, parentSchemeOptimizeInfo);
	}

	/**
	 * @param creatorId
	 * @param name
	 * @param priceUsd
	 * @param rangeDb
	 * @param parentSchemeOptimizeInfo
	 * @throws CreateObjectException
	 */
	@ParameterizationPending(value = {"final boolean usePool"})
	public static SchemeOptimizeInfoRtu createInstance(final Identifier creatorId,
			final String name,
			final int priceUsd,
			final float rangeDb,
			final SchemeOptimizeInfo parentSchemeOptimizeInfo)
	throws CreateObjectException {
		final boolean usePool = false;

		assert name != null && name.length() != 0 : NON_EMPTY_EXPECTED;
		assert parentSchemeOptimizeInfo != null : NON_NULL_EXPECTED;

		try {
			final Date created = new Date();
			final SchemeOptimizeInfoRtu schemeOptimizeInfoRtu = new SchemeOptimizeInfoRtu(IdentifierPool.getGeneratedIdentifier(SCHEMEOPTIMIZEINFORTU_CODE),
					created,
					created,
					creatorId,
					creatorId,
					StorableObjectVersion.INITIAL_VERSION,
					name,
					priceUsd,
					rangeDb,
					parentSchemeOptimizeInfo);
			parentSchemeOptimizeInfo.getSchemeOptimizeInfoRtuContainerWrappee().addToCache(schemeOptimizeInfoRtu, usePool);

			schemeOptimizeInfoRtu.markAsChanged();
			return schemeOptimizeInfoRtu;
		} catch (final CreateObjectException coe) {
			throw coe;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException("SchemeOptimizeInfoRtu.createInstance() | cannot generate identifier ", ige);
		} catch (final ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	@Override
	protected Set<Identifiable> getDependenciesTmpl() {
		assert this.parentSchemeOptimizeInfoId != null : OBJECT_NOT_INITIALIZED;
		assert !this.parentSchemeOptimizeInfoId.isVoid() : EXACTLY_ONE_PARENT_REQUIRED;
		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.add(this.parentSchemeOptimizeInfoId);
		return dependencies;
	}

	/**
	 * @see com.syrus.AMFICOM.general.ReverseDependencyContainer#getReverseDependencies(boolean)
	 */
	public Set<Identifiable> getReverseDependencies(final boolean usePool) {
		return Collections.<Identifiable>singleton(super.id);
	}

	/**
	 * @see Namable#getName()
	 */
	public String getName() {
		assert this.name != null && this.name.length() != 0 : OBJECT_NOT_INITIALIZED;
		return this.name;
	}

	public float getRangeDb() {
		return this.rangeDb;
	}

	Identifier getParentSchemeOptimizeInfoId() {
		assert this.parentSchemeOptimizeInfoId != null : OBJECT_NOT_INITIALIZED;
		assert !this.parentSchemeOptimizeInfoId.isVoid() : EXACTLY_ONE_PARENT_REQUIRED;
		assert this.parentSchemeOptimizeInfoId.getMajor() == SCHEMEOPTIMIZEINFORTU_CODE;
		return this.parentSchemeOptimizeInfoId;
	}

	/**
	 * A wrapper around {@link #getParentSchemeOptimizeInfoId()}.
	 */
	public SchemeOptimizeInfo getParentSchemeOptimizeInfo() {
		try {
			return StorableObjectPool.getStorableObject(this.getParentSchemeOptimizeInfoId(), true);
		} catch (final ApplicationException ae) {
			Log.debugMessage(ae, SEVERE);
			return null;
		}
	}

	public int getPriceUsd() {
		return this.priceUsd;
	}

	/**
	 * @param orb
	 * @see com.syrus.util.transport.idl.IdlTransferableObject#getIdlTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlSchemeOptimizeInfoRtu getIdlTransferable(final ORB orb) {
		return IdlSchemeOptimizeInfoRtuHelper.init(orb,
				super.id.getIdlTransferable(),
				super.created.getTime(),
				super.modified.getTime(),
				super.creatorId.getIdlTransferable(),
				super.modifierId.getIdlTransferable(),
				super.version.longValue(),
				this.name,
				this.priceUsd,
				this.rangeDb,
				this.parentSchemeOptimizeInfoId.getIdlTransferable());
	}

	/**
	 * @param schemeOptimizeInfoRtu
	 * @param importType
	 * @param usePool
	 * @throws XmlConversionException
	 * @see com.syrus.util.transport.xml.XmlTransferableObject#getXmlTransferable(org.apache.xmlbeans.XmlObject, String, boolean)
	 */
	public void getXmlTransferable(
			final XmlSchemeOptimizeInfoRtu schemeOptimizeInfoRtu,
			final String importType,
			final boolean usePool)
	throws XmlConversionException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see Namable#setName(String)
	 */
	public void setName(final String name) {
		assert this.name != null && this.name.length() != 0: OBJECT_NOT_INITIALIZED;
		assert name != null && name.length() != 0: NON_EMPTY_EXPECTED;
		if (this.name.equals(name)) {
			return;
		}
		this.name = name;
		super.markAsChanged();
	}

	/**
	 * @param rangeDb
	 */
	public void setRangeDb(final float rangeDb) {
		if (this.rangeDb == rangeDb) {
			return;
		}
		this.rangeDb = rangeDb;
		super.markAsChanged();
	}

	/**
	 * A wrapper around {@link #setParentSchemeOptimizeInfo(SchemeOptimizeInfo, boolean)}.
	 *
	 * @param parentSchemeOptimizeInfoId
	 * @param usePool
	 * @throws ApplicationException
	 */
	void setParentSchemeOptimizeInfoId(
			final Identifier parentSchemeOptimizeInfoId,
			final boolean usePool)
	throws ApplicationException {
		assert parentSchemeOptimizeInfoId != null : NON_NULL_EXPECTED;
		assert parentSchemeOptimizeInfoId.isVoid() || parentSchemeOptimizeInfoId.getMajor() == SCHEMEOPTIMIZEINFO_CODE;

		if (this.parentSchemeOptimizeInfoId.equals(parentSchemeOptimizeInfoId)) {
			return;
		}

		this.setParentSchemeOptimizeInfo(
				StorableObjectPool.<SchemeOptimizeInfo>getStorableObject(parentSchemeOptimizeInfoId, true),
				usePool);
	}

	/**
	 * @param parentSchemeOptimizeInfo
	 * @param usePool
	 * @throws ApplicationException
	 */
	public void setParentSchemeOptimizeInfo(
			final SchemeOptimizeInfo parentSchemeOptimizeInfo,
			final boolean usePool)
	throws ApplicationException {
		assert this.parentSchemeOptimizeInfoId != null : OBJECT_NOT_INITIALIZED;
		assert !this.parentSchemeOptimizeInfoId.isVoid() : EXACTLY_ONE_PARENT_REQUIRED;

		final Identifier newParentSchemeOptimizeInfoId = Identifier.possiblyVoid(parentSchemeOptimizeInfo);
		if (this.parentSchemeOptimizeInfoId.equals(newParentSchemeOptimizeInfoId)) {
			return;
		}

		this.getParentSchemeOptimizeInfo().getSchemeOptimizeInfoRtuContainerWrappee().removeFromCache(this, usePool);

		if (parentSchemeOptimizeInfo == null) {
			Log.debugMessage(OBJECT_WILL_DELETE_ITSELF_FROM_POOL, WARNING);
			StorableObjectPool.delete(this.getReverseDependencies(usePool));
		} else {
			parentSchemeOptimizeInfo.getSchemeOptimizeInfoRtuContainerWrappee().addToCache(this, usePool);
		}

		this.parentSchemeOptimizeInfoId = newParentSchemeOptimizeInfoId;
		super.markAsChanged();
	}

	/**
	 * @param priceUsd
	 */
	public void setPriceUsd(final int priceUsd) {
		if (this.priceUsd == priceUsd) {
			return;
		}
		this.priceUsd = priceUsd;
		super.markAsChanged();
	}

	/**
	 * @param transferable
	 * @see com.syrus.AMFICOM.general.StorableObject#fromTransferable(com.syrus.AMFICOM.general.corba.IdlStorableObject)
	 */
	@Override
	protected void fromTransferable(final IdlStorableObject transferable) {
		synchronized (this) {
			final IdlSchemeOptimizeInfoRtu schemeOptimizeInfoRtu = (IdlSchemeOptimizeInfoRtu) transferable;
			try {
				super.fromTransferable(schemeOptimizeInfoRtu);
			} catch (final ApplicationException ae) {
				/*
				 * Never.
				 */
				assert false;
			}
			this.name = schemeOptimizeInfoRtu.name;
			this.priceUsd = schemeOptimizeInfoRtu.priceUsd;
			this.rangeDb = schemeOptimizeInfoRtu.rangeDb;
			this.parentSchemeOptimizeInfoId = new Identifier(schemeOptimizeInfoRtu.parentSchemeOptimizeInfoId);
		}
	}

	/**
	 * @param xmlSchemeOptimizeInfoRtu
	 * @param importType
	 * @throws XmlConversionException
	 * @see XmlTransferableObject#fromXmlTransferable(org.apache.xmlbeans.XmlObject, String)
	 */
	public void fromXmlTransferable(
			final XmlSchemeOptimizeInfoRtu xmlSchemeOptimizeInfoRtu,
			final String importType)
	throws XmlConversionException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param created
	 * @param modified
	 * @param creatorId
	 * @param modifierId
	 * @param version
	 * @param name
	 * @param priceUsd
	 * @param rangeDb
	 * @param parentSchemeOptimizeInfoId
	 */
	void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final String name,
			final int priceUsd,
			final float rangeDb,
			final Identifier parentSchemeOptimizeInfoId) {
		synchronized (this) {
			super.setAttributes(created, modified, creatorId, modifierId, version);

			assert name != null && name.length() != 0 : NON_EMPTY_EXPECTED;
			assert parentSchemeOptimizeInfoId != null && !parentSchemeOptimizeInfoId.isVoid() : NON_VOID_EXPECTED;

			this.name = name;
			this.priceUsd = priceUsd;
			this.rangeDb = rangeDb;
			this.parentSchemeOptimizeInfoId = parentSchemeOptimizeInfoId;
		}
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getWrapper()
	 */
	@Override
	protected SchemeOptimizeInfoRtuWrapper getWrapper() {
		return SchemeOptimizeInfoRtuWrapper.getInstance();
	}
}
