/*
 * $Id: DomainMember.java,v 1.23 2005/08/10 11:30:57 bob Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */

package com.syrus.AMFICOM.administration;

import java.util.Date;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;

/**
 * @version $Revision: 1.23 $, $Date: 2005/08/10 11:30:57 $
 * @author $Author: bob $
 * @module administration
 */

public abstract class DomainMember extends StorableObject {
	private static final long serialVersionUID = -5921180280594867215L;

	public static final String	COLUMN_DOMAIN_ID	= "domain_id";

	Identifier domainId;

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected DomainMember(final Identifier id) {
		super(id);
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected DomainMember() {
		super();
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
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected synchronized void fromTransferable(final IdlStorableObject header, final Identifier domainId1) {
		try {
			super.fromTransferable(header);
		}
		catch (final ApplicationException ae) {
			/*
			 * Never.
			 */
			assert false;
		}
		this.domainId = domainId1;
	}

	/* (non-Javadoc)
	 * @see com.syrus.AMFICOM.general.StorableObject#isValid()
	 */
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	protected boolean isValid() {
		/* XXX : fix checking domainId w/o check id for concrete impementation as domain
		 * Domain entity can have null domain id what's why its doesnt must check domainId for null */
		return super.isValid() && (this.id.getMajor() == ObjectEntities.DOMAIN_CODE || !this.domainId.isVoid());
	}

	public final Identifier getDomainId() {
		return this.domainId;
	}
	
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected final void setDomainId0(final Identifier domainId) {
		assert domainId.isVoid() || 
			domainId.getMajor() == ObjectEntities.DOMAIN_CODE : 
				"Domain id expected";
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
