/*
 * $Id: AccessIdentity.java,v 1.2 2005/02/11 11:34:11 bob Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.util.Date;

import com.syrus.AMFICOM.general.corba.AccessIdentifier_Transferable;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;

/**
 * @version $Revision: 1.2 $, $Date: 2005/02/11 11:34:11 $
 * @author $Author: bob $
 * @module general_v1
 */
public class AccessIdentity implements TransferableObject {

	private Identifier	domainId;
	private Identifier	sessionId;

	private Date		start;
	private Identifier	userId;

	public AccessIdentity(AccessIdentifier_Transferable transferable) {
		this.start = new Date(transferable.started);
		this.domainId = new Identifier(transferable.domain_id);
		this.userId = new Identifier(transferable.user_id);
		this.sessionId = new Identifier(transferable.session_id);
	}

	public AccessIdentity(Date start, Identifier domainId, Identifier userId, Identifier sessionId) {
		this.start = start;
		this.domainId = domainId;
		this.userId = userId;
		this.sessionId = sessionId;
	}

	public Identifier getDomainId() {
		return this.domainId;
	}

	public Identifier getSessionId() {
		return this.sessionId;
	}

	public Date getStart() {
		return this.start;
	}

	public Object getTransferable() {
		return new AccessIdentifier_Transferable(this.start.getTime(), (Identifier_Transferable) this.domainId
				.getTransferable(), (Identifier_Transferable) this.userId.getTransferable(),
													(Identifier_Transferable) this.sessionId.getTransferable());
	}

	public Identifier getUserId() {
		return this.userId;
	}
}
