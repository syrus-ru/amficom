/*
 * $Id: DomainMember.java,v 1.38 2006/03/20 12:39:40 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.administration;

import static com.syrus.AMFICOM.general.ObjectEntities.DOMAIN_CODE;

import java.util.Date;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.general.xml.XmlIdentifier;

/**
 * @version $Revision: 1.38 $, $Date: 2006/03/20 12:39:40 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module administration
 */

public abstract class DomainMember extends StorableObject {

	public static final String COLUMN_DOMAIN_ID = "domain_id";

	Identifier domainId;

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected DomainMember(/*IdlDomainMember*/) {
		// super();
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected DomainMember(final Identifier id,
			final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final Identifier domainId) {
		super(id, created, modified, creatorId, modifierId, version);
		this.domainId = domainId;
	}

	/**
	 * Minimalistic constructor used when importing from XML.
	 * 
	 * @param id
	 * @param importType
	 * @param entityCode
	 * @param created
	 * @param creatorId
	 * @throws IdentifierGenerationException
	 */
	protected DomainMember(final XmlIdentifier id,
			final String importType,
			final short entityCode,
			final Date created,
			final Identifier creatorId) throws IdentifierGenerationException {
		super(id, importType, entityCode, created, creatorId);
	}

	/**
	 * <p>Clients must never explicitly call this method.</p>
	 *
	 * <p>
	 * Non-synchronized.
	 * Non-overriding.
	 * Non-overridable.
	 * </p>
	 *
	 * @param header
	 * @param domainId1
	 */
	protected final void fromIdlTransferable(final IdlStorableObject header, final Identifier domainId1) {
		super.fromIdlTransferable(header);
		this.domainId = domainId1;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 *
	 * @see com.syrus.AMFICOM.general.StorableObject#isValid()
	 */
	@Override
	protected boolean isValid() {
		/* XXX : fix checking domainId w/o check id for concrete impementation as domain
		 * Domain entity can have null domain id what's why its doesnt must check domainId for null */
		return super.isValid() && (this.id.getMajor() == DOMAIN_CODE || !this.domainId.isVoid());
	}

	public final Identifier getDomainId() {
		return this.domainId;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected final void setDomainId0(final Identifier domainId) {
		assert domainId.isVoid() || domainId.getMajor() == DOMAIN_CODE : "Domain id expected";
		this.domainId = domainId;
	}

	public final void setDomainId(final Identifier domainId) {
		this.setDomainId0(domainId);
		super.markAsChanged();
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected final synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final Identifier domainId) {
		super.setAttributes(created,
				modified,
				creatorId,
				modifierId,
				version);
		this.domainId = domainId;
	}
}
