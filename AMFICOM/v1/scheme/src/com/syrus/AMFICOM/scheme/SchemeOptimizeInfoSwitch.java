/*-
 * $Id: SchemeOptimizeInfoSwitch.java,v 1.44 2006/03/15 20:28:23 bass Exp $
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
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEOPTIMIZEINFOSWITCH_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEOPTIMIZEINFO_CODE;
import static com.syrus.AMFICOM.general.StorableObjectVersion.INITIAL_VERSION;
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
import com.syrus.AMFICOM.scheme.corba.IdlSchemeOptimizeInfoSwitch;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeOptimizeInfoSwitchHelper;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeOptimizeInfoSwitch;
import com.syrus.util.Log;
import com.syrus.util.transport.idl.IdlConversionException;
import com.syrus.util.transport.idl.IdlTransferableObjectExt;
import com.syrus.util.transport.xml.XmlConversionException;
import com.syrus.util.transport.xml.XmlTransferableObject;

/**
 * #06 in hierarchy.
 *
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.44 $, $Date: 2006/03/15 20:28:23 $
 * @module scheme
 */
public final class SchemeOptimizeInfoSwitch
		extends StorableObject
		implements Namable, ReverseDependencyContainer,
		XmlTransferableObject<XmlSchemeOptimizeInfoSwitch>,
		IdlTransferableObjectExt<IdlSchemeOptimizeInfoSwitch> {
	private static final long serialVersionUID = 2583191675321445786L;

	private String name;

	private int priceUsd;

	private byte noOfPorts;

	Identifier parentSchemeOptimizeInfoId;

	SchemeOptimizeInfoSwitch(final Identifier id,
			final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final String name,
			final int priceUsd,
			final byte noOfPorts,
			final SchemeOptimizeInfo parentSchemeOptimizeInfo) {
		super(id, created, modified, creatorId, modifierId, version);
		this.name = name;
		this.priceUsd = priceUsd;
		this.noOfPorts = noOfPorts;
		this.parentSchemeOptimizeInfoId = Identifier.possiblyVoid(parentSchemeOptimizeInfo);
	}

	public SchemeOptimizeInfoSwitch(final IdlSchemeOptimizeInfoSwitch transferable) throws CreateObjectException {
		try {
			this.fromIdlTransferable(transferable);
		} catch (final IdlConversionException ice) {
			throw new CreateObjectException(ice);
		}
	}

	/**
	 * A shorthand for
	 * {@link #createInstance(Identifier, String, int, byte, SchemeOptimizeInfo)}.
	 *
	 * @param creatorId
	 * @param name
	 * @param parentSchemeOptimizeInfo
	 * @throws CreateObjectException
	 */
	public static SchemeOptimizeInfoSwitch createInstance(final Identifier creatorId,
			final String name,
			final SchemeOptimizeInfo parentSchemeOptimizeInfo)
	throws CreateObjectException {
		return createInstance(creatorId, name, 0, (byte) 0, parentSchemeOptimizeInfo);
	}

	/**
	 * @param creatorId
	 * @param name
	 * @param priceUsd
	 * @param noOfPorts
	 * @param parentSchemeOptimizeInfo
	 * @throws CreateObjectException
	 */
	@ParameterizationPending(value = {"final boolean usePool"})
	public static SchemeOptimizeInfoSwitch createInstance(final Identifier creatorId,
			final String name,
			final int priceUsd,
			final byte noOfPorts,
			final SchemeOptimizeInfo parentSchemeOptimizeInfo)
	throws CreateObjectException {
		final boolean usePool = false;

		assert name != null && name.length() != 0 : NON_EMPTY_EXPECTED;
		assert parentSchemeOptimizeInfo != null : NON_NULL_EXPECTED;

		try {
			final Date created = new Date();
			final SchemeOptimizeInfoSwitch schemeOptimizeInfoSwitch = new SchemeOptimizeInfoSwitch(IdentifierPool.getGeneratedIdentifier(SCHEMEOPTIMIZEINFOSWITCH_CODE),
					created,
					created,
					creatorId,
					creatorId,
					INITIAL_VERSION,
					name,
					priceUsd,
					noOfPorts,
					parentSchemeOptimizeInfo);
			parentSchemeOptimizeInfo.getSchemeOptimizeInfoSwitchContainerWrappee().addToCache(schemeOptimizeInfoSwitch, usePool);

			schemeOptimizeInfoSwitch.markAsChanged();
			return schemeOptimizeInfoSwitch;
		} catch (final CreateObjectException coe) {
			throw coe;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException("SchemeOptimizeInfoSwitch.createInstance() | cannot generate identifier ", ige);
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

	public byte getNoOfPorts() {
		return this.noOfPorts;
	}

	Identifier getParentSchemeOptimizeInfoId() {
		assert this.parentSchemeOptimizeInfoId != null : OBJECT_NOT_INITIALIZED;
		assert !this.parentSchemeOptimizeInfoId.isVoid() : EXACTLY_ONE_PARENT_REQUIRED;
		assert this.parentSchemeOptimizeInfoId.getMajor() == SCHEMEOPTIMIZEINFOSWITCH_CODE;
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
	public IdlSchemeOptimizeInfoSwitch getIdlTransferable(final ORB orb) {
		return IdlSchemeOptimizeInfoSwitchHelper.init(orb,
				super.id.getIdlTransferable(),
				super.created.getTime(),
				super.modified.getTime(),
				super.creatorId.getIdlTransferable(),
				super.modifierId.getIdlTransferable(),
				super.version.longValue(),
				this.name,
				this.priceUsd,
				this.noOfPorts,
				this.parentSchemeOptimizeInfoId.getIdlTransferable());
	}

	/**
	 * @param schemeOptimizeInfoSwitch
	 * @param importType
	 * @param usePool
	 * @throws XmlConversionException
	 * @see com.syrus.util.transport.xml.XmlTransferableObject#getXmlTransferable(org.apache.xmlbeans.XmlObject, String, boolean)
	 */
	public void getXmlTransferable(
			final XmlSchemeOptimizeInfoSwitch schemeOptimizeInfoSwitch,
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
	 * @param noOfPorts
	 */
	public void setNoOfPorts(final byte noOfPorts) {
		if (this.noOfPorts == noOfPorts) {
			return;
		}
		this.noOfPorts = noOfPorts;
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

		this.getParentSchemeOptimizeInfo().getSchemeOptimizeInfoSwitchContainerWrappee().removeFromCache(this, usePool);

		if (parentSchemeOptimizeInfo == null) {
			Log.debugMessage(OBJECT_WILL_DELETE_ITSELF_FROM_POOL, WARNING);
			StorableObjectPool.delete(this.getReverseDependencies(usePool));
		} else {
			parentSchemeOptimizeInfo.getSchemeOptimizeInfoSwitchContainerWrappee().addToCache(this, usePool);
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
	 * @param schemeOptimizeInfoSwitch
	 * @see com.syrus.AMFICOM.general.StorableObject#fromIdlTransferable(com.syrus.AMFICOM.general.corba.IdlStorableObject)
	 */
	public void fromIdlTransferable(final IdlSchemeOptimizeInfoSwitch schemeOptimizeInfoSwitch)
	throws IdlConversionException {
		synchronized (this) {
			super.fromIdlTransferable(schemeOptimizeInfoSwitch);
			this.name = schemeOptimizeInfoSwitch.name;
			this.priceUsd = schemeOptimizeInfoSwitch.priceUsd;
			this.noOfPorts = schemeOptimizeInfoSwitch.noOfPorts;
			this.parentSchemeOptimizeInfoId = new Identifier(schemeOptimizeInfoSwitch.parentSchemeOptimizeInfoId);
		}
	}

	/**
	 * @param xmlSchemeOptimizeInfoSwitch
	 * @param importType
	 * @throws XmlConversionException
	 * @see XmlTransferableObject#fromXmlTransferable(org.apache.xmlbeans.XmlObject, String)
	 */
	public void fromXmlTransferable(
			final XmlSchemeOptimizeInfoSwitch xmlSchemeOptimizeInfoSwitch,
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
	 * @param noOfPorts
	 * @param parentSchemeOptimizeInfoId
	 */
	void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final String name,
			final int priceUsd,
			final byte noOfPorts,
			final Identifier parentSchemeOptimizeInfoId) {
		synchronized (this) {
			super.setAttributes(created, modified, creatorId, modifierId, version);

			assert name != null && name.length() != 0 : NON_EMPTY_EXPECTED;
			assert parentSchemeOptimizeInfoId != null && !parentSchemeOptimizeInfoId.isVoid() : NON_VOID_EXPECTED;

			this.name = name;
			this.priceUsd = priceUsd;
			this.noOfPorts = noOfPorts;
			this.parentSchemeOptimizeInfoId = parentSchemeOptimizeInfoId;
		}
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getWrapper()
	 */
	@Override
	protected SchemeOptimizeInfoSwitchWrapper getWrapper() {
		return SchemeOptimizeInfoSwitchWrapper.getInstance();
	}
}
