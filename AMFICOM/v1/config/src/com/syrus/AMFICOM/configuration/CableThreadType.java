/*
 * $Id: CableThreadType.java,v 1.60 2005/09/05 17:43:15 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_VOID_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_BADLY_INITIALIZED;
import static com.syrus.AMFICOM.general.ObjectEntities.CABLELINK_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.CABLETHREAD_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.LINK_TYPE_CODE;
import static java.util.logging.Level.SEVERE;

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
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.Namable;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.XmlBeansTransferable;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.util.Log;
import com.syrus.util.Shitlet;

/**
 * <code>CableThreadType</code>, among other fields, contain references to
 * {@link LinkType} and {@link CableLinkType}. While the former is a type of
 * optical fiber (or an <i>abstract </i> optical fiber), the latter is a type of
 * cable (or an <i>abstract </i> cable containing this thread).
 *
 * @version $Revision: 1.60 $, $Date: 2005/09/05 17:43:15 $
 * @author $Author: bass $
 * @module config
 */

public final class CableThreadType extends StorableObjectType implements Namable, XmlBeansTransferable<XmlCableThreadType> {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long  serialVersionUID	= 3689355429075628086L;

	private String name;
	private int color;
	private LinkType linkType;
	private CableLinkType cableLinkType;

	CableThreadType(final Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		try {
			DatabaseContext.getDatabase(CABLETHREAD_TYPE_CODE).retrieve(this);
		} catch (final IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

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
			final LinkType linkType,
			final CableLinkType cableLinkType) {
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
		this.linkType = linkType;
		this.cableLinkType = cableLinkType;
	}

	/**
	 * Minimalistic constructor used when importing from XML.
	 *
	 * @param id
	 * @param created
	 * @param creatorId
	 */
	private CableThreadType(final Identifier id,
			final Date created,
			final Identifier creatorId) {
		super(id,
				created,
				created,
				creatorId,
				creatorId,
				StorableObjectVersion.createInitial(),
				"",
				"");
	}

	/**
	 * @param creatorId
	 * @param importType
	 * @param xmlCableThreadType
	 * @throws CreateObjectException
	 */
	public static CableThreadType createInstance(
			final Identifier creatorId,
			final String importType,
			final XmlCableThreadType xmlCableThreadType)
	throws CreateObjectException {
		try {
			final Identifier id = Identifier.fromXmlTransferable(xmlCableThreadType.getId(), CABLETHREAD_TYPE_CODE, importType);
			CableThreadType cableThreadType = StorableObjectPool.getStorableObject(id, true);
			if (cableThreadType == null) {
				cableThreadType = new CableThreadType(id, new Date(), creatorId);
			}
			cableThreadType.fromXmlTransferable(xmlCableThreadType, importType);
			assert cableThreadType.isValid() : OBJECT_BADLY_INITIALIZED;
			cableThreadType.markAsChanged();
			return cableThreadType;
		} catch (final CreateObjectException coe) {
			throw coe;
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			throw new CreateObjectException(ae);
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
					StorableObjectVersion.createInitial(),
					codename,
					description,
					name,
					color,
					linkType,
					cableLinkType);

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
		this.setLinkTypeId(new Identifier(idlCableThreadType.linkTypeId));
		this.setCableLinkTypeId(new Identifier(idlCableThreadType.cableLinkTypeId));
	}

	/**
	 * @param xmlCableThreadType
	 * @param importType
	 * @throws ApplicationException
	 * @see XmlBeansTransferable#fromXmlTransferable(com.syrus.AMFICOM.general.xml.XmlStorableObject, String)
	 */
	@Shitlet
	public void fromXmlTransferable(final XmlCableThreadType xmlCableThreadType,
			final String importType)
	throws ApplicationException {
		this.name = xmlCableThreadType.getName();
		this.codename = xmlCableThreadType.getCodename();
		this.description = xmlCableThreadType.getDescription();
		this.color = Integer.parseInt(xmlCableThreadType.getColor());
		this.setLinkTypeId(Identifier.fromXmlTransferable(xmlCableThreadType.getLinkTypeId(), LINK_TYPE_CODE, importType));
		this.setCableLinkTypeId(Identifier.fromXmlTransferable(xmlCableThreadType.getCableLinkTypeId(), CABLELINK_TYPE_CODE, importType));
	}

	/**
	 * @param orb
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlCableThreadType getTransferable(final ORB orb) {
		return IdlCableThreadTypeHelper.init(orb,
				super.id.getTransferable(),
				super.created.getTime(),
				super.modified.getTime(),
				super.creatorId.getTransferable(),
				super.modifierId.getTransferable(),
				super.version.longValue(),
				super.codename,
				super.description != null ? super.description : "",
				this.name != null ? this.name : "",
				this.color,
				this.linkType.getId().getTransferable(),
				this.cableLinkType.getId().getTransferable());
	}

	/**
	 * @see XmlBeansTransferable#getXmlTransferable(String)
	 */
	@Shitlet
	public XmlCableThreadType getXmlTransferable(final String importType) {
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
			final LinkType linkType,
			final CableLinkType cableLinkType) {
		super.setAttributes(created,
			modified,
			creatorId,
			modifierId,
			version,
			codename,
			description);
		this.name = name;
		this.color = color;
		this.linkType = linkType;
		this.cableLinkType = cableLinkType;
	}

	public LinkType getLinkType() {
		return this.linkType;
	}

	public CableLinkType getCableLinkType() {
		return this.cableLinkType;
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
	 * @throws ApplicationException
	 */
	private void setLinkTypeId(final Identifier linkTypeId)
	throws ApplicationException {
		assert linkTypeId != null : NON_NULL_EXPECTED;
		assert !linkTypeId.isVoid() : NON_VOID_EXPECTED;
		assert linkTypeId.getMajor() == LINK_TYPE_CODE;
		if (Identifier.possiblyVoid(this.linkType).equals(linkTypeId)) {
			return;
		}
		this.setLinkType(StorableObjectPool.<LinkType>getStorableObject(linkTypeId, true));
	}

	public void setLinkType(final LinkType linkType) {
		assert linkType != null;
		this.linkType = linkType;
		super.markAsChanged();
	}

	/**
	 * @param cableLinkTypeId
	 * @throws ApplicationException
	 */
	private void setCableLinkTypeId(final Identifier cableLinkTypeId)
	throws ApplicationException {
		assert cableLinkTypeId != null : NON_NULL_EXPECTED;
		assert !cableLinkTypeId.isVoid() : NON_VOID_EXPECTED;
		assert cableLinkTypeId.getMajor() == CABLELINK_TYPE_CODE;
		if (Identifier.possiblyVoid(this.cableLinkType).equals(cableLinkTypeId)) {
			return;
		}
		this.setCableLinkType(StorableObjectPool.<CableLinkType>getStorableObject(cableLinkTypeId, true));
	}

	public void setCableLinkType(final CableLinkType cableLinkType) {
		assert cableLinkType != null;
		this.cableLinkType = cableLinkType;
		super.markAsChanged();
	}

	@Override
	public Set<Identifiable> getDependencies() {
		assert this.isValid() : OBJECT_BADLY_INITIALIZED;

		final Set<Identifiable> dependencies = new HashSet<Identifiable>(2);
		dependencies.add(this.linkType);
		dependencies.add(this.cableLinkType);
		return Collections.unmodifiableSet(dependencies);
	}
}
