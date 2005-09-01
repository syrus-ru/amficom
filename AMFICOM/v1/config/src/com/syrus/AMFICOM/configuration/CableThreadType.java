/*
 * $Id: CableThreadType.java,v 1.59 2005/09/01 10:33:12 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import static com.syrus.AMFICOM.general.ErrorMessages.*;
import static com.syrus.AMFICOM.general.ObjectEntities.CABLETHREAD_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.*;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.configuration.corba.IdlCableThreadType;
import com.syrus.AMFICOM.configuration.corba.IdlCableThreadTypeHelper;
import com.syrus.AMFICOM.configuration.xml.XmlCableThreadType;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.ClonedIdsPool;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ImportUidMapDatabase;
import com.syrus.AMFICOM.general.Namable;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.XmlBeansTransferable;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.util.Shitlet;

/**
 * <code>CableThreadType</code>, among other fields, contain references to
 * {@link LinkType} and {@link CableLinkType}. While the former is a type of
 * optical fiber (or an <i>abstract </i> optical fiber), the latter is a type of
 * cable (or an <i>abstract </i> cable containing this thread).
 *
 * @version $Revision: 1.59 $, $Date: 2005/09/01 10:33:12 $
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

	@Shitlet
	private CableThreadType(final Identifier creatorId,
			final StorableObjectVersion version,
			final XmlCableThreadType xmlCableThreadType,
			final ClonedIdsPool clonedIdsPool,
			final String importType) throws CreateObjectException, ApplicationException {

		super(clonedIdsPool.getClonedId(LINK_TYPE_CODE, xmlCableThreadType.getId()),
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version,
				"",
				"");
		this.fromXmlTransferable(xmlCableThreadType, clonedIdsPool, importType);
	}

	@Shitlet
	static CableThreadType createInstance(
			final Identifier creatorId,
			final String importType,
			final XmlCableThreadType xmlCableThreadType,
			final ClonedIdsPool clonedIdsPool) throws CreateObjectException {

		try {
			final XmlIdentifier xmlId = xmlCableThreadType.getId();
			Identifier existingIdentifier = Identifier.fromXmlTransferable(xmlId, importType);
			CableThreadType cableThreadType = null;
			if(existingIdentifier != null) {
				cableThreadType = StorableObjectPool.getStorableObject(existingIdentifier, true);
				if(cableThreadType != null) {
					cableThreadType.fromXmlTransferable(xmlCableThreadType, clonedIdsPool, importType);
				}
				else{
					ImportUidMapDatabase.delete(importType, xmlId);
				}
			}
			if(cableThreadType == null) {
				cableThreadType = cableThreadType = new CableThreadType(
						creatorId,
						StorableObjectVersion.createInitial(),
						xmlCableThreadType,
						clonedIdsPool,
						importType);
				ImportUidMapDatabase.insert(importType, xmlId, cableThreadType.id);
			}
			assert cableThreadType.isValid() : OBJECT_STATE_ILLEGAL;
			cableThreadType.markAsChanged();
			return cableThreadType;
		} catch (Exception e) {
			System.out.println(xmlCableThreadType);
			throw new CreateObjectException("CableThreadType.createInstance |  ", e);
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

			assert cableThreadType.isValid() : OBJECT_STATE_ILLEGAL;

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
	 * @param clonedIdsPool
	 * @param importType
	 * @throws ApplicationException
	 * @see XmlBeansTransferable#fromXmlTransferable(com.syrus.AMFICOM.general.xml.XmlStorableObject, ClonedIdsPool, String)
	 */
	@Shitlet
	public void fromXmlTransferable(final XmlCableThreadType xmlCableThreadType,
			final ClonedIdsPool clonedIdsPool,
			final String importType)
	throws ApplicationException {
		this.name = xmlCableThreadType.getName();
		this.codename = xmlCableThreadType.getCodename();
		this.description = xmlCableThreadType.getDescription();
		this.color = Integer.parseInt(xmlCableThreadType.getColor());
		this.setLinkTypeId(Identifier.fromXmlTransferable(xmlCableThreadType.getLinkTypeId(), importType));
		this.setCableLinkTypeId(Identifier.fromXmlTransferable(xmlCableThreadType.getCableLinkTypeId(), importType));
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
	 * @see XmlBeansTransferable#getXmlTransferable()
	 */
	@Shitlet
	public XmlCableThreadType getXmlTransferable() {
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
		assert this.isValid() : OBJECT_STATE_ILLEGAL;

		final Set<Identifiable> dependencies = new HashSet<Identifiable>(2);
		dependencies.add(this.linkType);
		dependencies.add(this.cableLinkType);
		return Collections.unmodifiableSet(dependencies);
	}
}
