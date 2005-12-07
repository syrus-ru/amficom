/*-
 * $Id: CableThreadType.java,v 1.82 2005/12/07 17:16:25 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.configuration;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_VOID_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_BADLY_INITIALIZED;
import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.Identifier.XmlConversionMode.MODE_RETURN_VOID_IF_ABSENT;
import static com.syrus.AMFICOM.general.Identifier.XmlConversionMode.MODE_THROW_IF_ABSENT;
import static com.syrus.AMFICOM.general.ObjectEntities.CABLELINK_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.CABLETHREAD_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.LINK_TYPE_CODE;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CODENAME;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort.OPERATION_EQUALS;
import static java.util.logging.Level.WARNING;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.configuration.corba.IdlCableThreadType;
import com.syrus.AMFICOM.configuration.corba.IdlCableThreadTypeHelper;
import com.syrus.AMFICOM.configuration.xml.XmlCableThreadType;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.LocalXmlIdentifierPool;
import com.syrus.AMFICOM.general.Namable;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.util.Log;
import com.syrus.util.Shitlet;
import com.syrus.util.transport.xml.XmlConversionException;
import com.syrus.util.transport.xml.XmlTransferableObject;

/**
 * <code>CableThreadType</code>, among other fields, contain references to
 * {@link LinkType} and {@link CableLinkType}. While the former is a type of
 * optical fiber (or an <i>abstract </i> optical fiber), the latter is a type of
 * cable (or an <i>abstract </i> cable containing this thread).
 *
 * @version $Revision: 1.82 $, $Date: 2005/12/07 17:16:25 $
 * @author $Author: bass $
 * @module config
 */

public final class CableThreadType extends StorableObjectType<CableThreadType>
		implements Namable, XmlTransferableObject<XmlCableThreadType> {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long  serialVersionUID	= 3689355429075628086L;

	private String name;
	private int color;
	private Identifier linkTypeId;
	private Identifier cableLinkTypeId;

	public CableThreadType(final IdlCableThreadType cttt) throws CreateObjectException {
		try {
			this.fromTransferable(cttt);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	CableThreadType(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final String codename,
			final String description,
			final String name,
			final int color,
			final Identifier linkTypeId,
			final Identifier cableLinkTypeId) {
		super(id,
			new Date(),
			new Date(),
			creatorId,
			creatorId,
			version,
			codename,
			description);

		this.name = name;
		this.color = color;
		this.linkTypeId = linkTypeId;
		this.cableLinkTypeId = cableLinkTypeId;
	}

	/**
	 * Minimalistic constructor used when importing from XML.
	 *
	 * @param id
	 * @param importType
	 * @param created
	 * @param creatorId
	 * @throws IdentifierGenerationException
	 */
	private CableThreadType(final XmlIdentifier id,
			final String importType,
			final Date created,
			final Identifier creatorId)
	throws IdentifierGenerationException {
		super(id, importType, CABLETHREAD_TYPE_CODE, created, creatorId);
	}

	/**
	 * @param creatorId
	 * @param xmlCableThreadType
	 * @param importType
	 * @throws CreateObjectException
	 */
	public static CableThreadType createInstance(
			final Identifier creatorId,
			final XmlCableThreadType xmlCableThreadType,
			final String importType)
	throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid() : NON_VOID_EXPECTED;

		try {
			final String newCodename = xmlCableThreadType.getCodename();
			final Set<CableThreadType> cableThreadTypes = StorableObjectPool.getStorableObjectsByCondition(
					new TypicalCondition(newCodename, OPERATION_EQUALS, CABLETHREAD_TYPE_CODE, COLUMN_CODENAME),
					true);

			assert cableThreadTypes.size() <= 1;

			final XmlIdentifier xmlId = xmlCableThreadType.getId();
			final Identifier expectedId = Identifier.fromXmlTransferable(xmlId, importType, MODE_RETURN_VOID_IF_ABSENT);

			CableThreadType cableThreadType;
			if (cableThreadTypes.isEmpty()) {
				/*
				 * No objects found with the specified codename.
				 * Continue normally.
				 */
				final Date created = new Date();
				if (expectedId.isVoid()) {
					/*
					 * First import.
					 */
					cableThreadType = new CableThreadType(xmlId,
							importType,
							created,
							creatorId);
				} else {
					cableThreadType = StorableObjectPool.getStorableObject(expectedId, true);
					if (cableThreadType == null) {
						Log.debugMessage("WARNING: expected counterpart ("
								+ expectedId
								+ ") for XML identifier: " + xmlId.getStringValue()
								+ " and actual one (" + VOID_IDENTIFIER
								+ ") do not match; expected one will be deleted",
								WARNING);
						LocalXmlIdentifierPool.remove(xmlId, importType);
						cableThreadType = new CableThreadType(xmlId,
								importType,
								created,
								creatorId);
					} else {
						final String oldCodename = cableThreadType.getCodename();
						if (!oldCodename.equals(newCodename)) {
							Log.debugMessage("WARNING: "
									+ expectedId + " will change its codename from ``"
									+ oldCodename + "'' to ``"
									+ newCodename + "''",
									WARNING);
						}
					}
				}
			} else {
				cableThreadType = cableThreadTypes.iterator().next();
				if (expectedId.isVoid()) {
					/*
					 * First import.
					 */
					cableThreadType.insertXmlMapping(xmlId, importType);
				} else {
					final Identifier actualId = cableThreadType.getId();
					if (!actualId.equals(expectedId)) {
						/*
						 * Arghhh, no match.
						 */
						Log.debugMessage("WARNING: expected counterpart ("
								+ expectedId
								+ ") for XML identifier: " + xmlId.getStringValue()
								+ " and actual one (" + actualId
								+ ") do not match; expected one will be deleted",
								WARNING);
						LocalXmlIdentifierPool.remove(xmlId, importType);
						cableThreadType.insertXmlMapping(xmlId, importType);
					}
				}
			}
			cableThreadType.fromXmlTransferable(xmlCableThreadType, importType);
			assert cableThreadType.isValid() : OBJECT_BADLY_INITIALIZED;
			cableThreadType.markAsChanged();
			return cableThreadType;
		} catch (final CreateObjectException coe) {
			throw coe;
		} catch (final ApplicationException ae) {
			throw new CreateObjectException(ae);
		} catch (final XmlConversionException xce) {
			throw new CreateObjectException(xce);
		}
	}

	/**
	 * create new instance for client
	 * @throws CreateObjectException
	 */
	public static CableThreadType createInstance(final Identifier creatorId,
			final String codename,
			final String description,
			final String name,
			final int color,
			final LinkType linkType,
			final CableLinkType cableLinkType) throws CreateObjectException {
		assert creatorId != null
				&& codename != null
				&& description != null
				&& name != null
				&& linkType != null
				&& cableLinkType != null;
		try {
			final CableThreadType cableThreadType = new CableThreadType(IdentifierPool.getGeneratedIdentifier(CABLETHREAD_TYPE_CODE),
					creatorId,
					StorableObjectVersion.INITIAL_VERSION,
					codename,
					description,
					name,
					color,
					linkType.getId(),
					cableLinkType.getId());

			assert cableThreadType.isValid() : OBJECT_BADLY_INITIALIZED;

			cableThreadType.markAsChanged();

			return cableThreadType;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	@Override
	protected synchronized void fromTransferable(
			final IdlStorableObject transferable)
	throws ApplicationException {
		final IdlCableThreadType idlCableThreadType = (IdlCableThreadType) transferable;
		super.fromTransferable(idlCableThreadType, idlCableThreadType.codename, idlCableThreadType.description);
		this.name = idlCableThreadType.name;
		this.color = idlCableThreadType.color;
		this.linkTypeId = new Identifier(idlCableThreadType.linkTypeId);
		this.cableLinkTypeId = new Identifier(idlCableThreadType.cableLinkTypeId);
	}

	/**
	 * @param cableThreadType
	 * @param importType
	 * @throws XmlConversionException
	 * @see XmlTransferableObject#fromXmlTransferable(org.apache.xmlbeans.XmlObject, String)
	 */
	@Shitlet
	public void fromXmlTransferable(final XmlCableThreadType cableThreadType,
			final String importType)
	throws XmlConversionException {
		try {
			this.name = cableThreadType.getName();
			this.codename = cableThreadType.getCodename();
			this.description = cableThreadType.isSetDescription()
					? cableThreadType.getDescription()
					: "";
			this.color = cableThreadType.isSetColor()
					? Integer.parseInt(cableThreadType.getColor())
					: -1;
			this.linkTypeId = Identifier.fromXmlTransferable(cableThreadType.getLinkTypeId(), importType, MODE_THROW_IF_ABSENT);
			this.cableLinkTypeId = Identifier.fromXmlTransferable(cableThreadType.getCableLinkTypeId(), importType, MODE_THROW_IF_ABSENT);
		} catch (final ObjectNotFoundException onfe) {
			throw new XmlConversionException(onfe);
		}
	}

	/**
	 * @param orb
	 * @see com.syrus.util.transport.idl.IdlTransferableObject#getIdlTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlCableThreadType getIdlTransferable(final ORB orb) {
		return IdlCableThreadTypeHelper.init(orb,
				super.id.getIdlTransferable(),
				super.created.getTime(),
				super.modified.getTime(),
				super.creatorId.getIdlTransferable(),
				super.modifierId.getIdlTransferable(),
				super.version.longValue(),
				super.codename,
				super.description != null ? super.description : "",
				this.name != null ? this.name : "",
				this.color,
				this.linkTypeId.getIdlTransferable(),
				this.cableLinkTypeId.getIdlTransferable());
	}

	/**
	 * @param cableThreadType
	 * @param importType
	 * @param usePool
	 * @throws XmlConversionException
	 * @see com.syrus.util.transport.xml.XmlTransferableObject#getXmlTransferable(org.apache.xmlbeans.XmlObject, String, boolean)
	 */
	@Shitlet
	public void getXmlTransferable(
			final XmlCableThreadType cableThreadType,
			final String importType,
			final boolean usePool)
	throws XmlConversionException {
		throw new UnsupportedOperationException();
	}

	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final String codename,
			final String description,
			final String name,
			final int color,
			final Identifier linkTypeId,
			final Identifier cableLinkTypeId) {
		super.setAttributes(created,
			modified,
			creatorId,
			modifierId,
			version,
			codename,
			description);
		this.name = name;
		this.color = color;
		this.linkTypeId = linkTypeId;
		this.cableLinkTypeId = cableLinkTypeId;
	}

	protected Identifier getLinkTypeId() {
		return this.linkTypeId;
	}
	
	protected Identifier getCableLinkTypeId() {
		return this.cableLinkTypeId;
	}
	
	public LinkType getLinkType() {
		try {
			return StorableObjectPool.<LinkType>getStorableObject(this.linkTypeId, true);
		} catch (ApplicationException e) {
			Log.errorMessage(e);
			return null;
		}
	}

	public CableLinkType getCableLinkType() {
		try {
			return StorableObjectPool.<CableLinkType>getStorableObject(this.cableLinkTypeId, true);
		} catch (ApplicationException e) {
			Log.errorMessage(e);
			return null;
		}
	}

	public void setColor(final int color) {
		this.color = color;
		super.markAsChanged();
	}

	public int getColor() {
		return this.color;
	}	

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		assert name != null;
		this.name = name;
		super.markAsChanged();
	}

	/**
	 * @param linkTypeId
	 */
	protected void setLinkTypeId(final Identifier linkTypeId) {
		assert linkTypeId != null : NON_NULL_EXPECTED;
		assert !linkTypeId.isVoid() : NON_VOID_EXPECTED;
		assert linkTypeId.getMajor() == LINK_TYPE_CODE;
		this.linkTypeId = linkTypeId;
	}

	public void setLinkType(final LinkType linkType) {
		assert linkType != null;
		this.setLinkTypeId(linkType.getId());
		super.markAsChanged();
	}

	/**
	 * @param cableLinkTypeId
	 */
	protected void setCableLinkTypeId(final Identifier cableLinkTypeId) {
		assert cableLinkTypeId != null : NON_NULL_EXPECTED;
		assert !cableLinkTypeId.isVoid() : NON_VOID_EXPECTED;
		assert cableLinkTypeId.getMajor() == CABLELINK_TYPE_CODE;
		this.cableLinkTypeId = cableLinkTypeId;
	}

	public void setCableLinkType(final CableLinkType cableLinkType) {
		assert cableLinkType != null;
		setCableLinkTypeId(cableLinkType.getId());
		super.markAsChanged();
	}

	@Override
	public Set<Identifiable> getDependencies() {
		assert this.isValid() : OBJECT_BADLY_INITIALIZED;

		final Set<Identifiable> dependencies = new HashSet<Identifiable>(2);
		dependencies.add(this.linkTypeId);
		dependencies.add(this.cableLinkTypeId);
		return Collections.unmodifiableSet(dependencies);
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getWrapper()
	 */
	@Override
	protected CableThreadTypeWrapper getWrapper() {
		return CableThreadTypeWrapper.getInstance();
	}
}
