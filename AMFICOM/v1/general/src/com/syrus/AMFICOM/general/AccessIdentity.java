/*
 * $Id: AccessIdentity.java,v 1.5 2005/04/08 15:28:34 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.util.Date;
import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.corba.AccessIdentifier_Transferable;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;

/**
 * @version $Revision: 1.5 $, $Date: 2005/04/08 15:28:34 $
 * @author $Author: arseniy $
 * @module general_v1
 */
public class AccessIdentity implements TransferableObject {


	private Date start;
	private Identifier domainId;
	private Identifier userId;
	private String sessionCode;

	public AccessIdentity(AccessIdentifier_Transferable transferable) {
		this.start = new Date(transferable.started);
		this.domainId = new Identifier(transferable.domain_id);
		this.userId = new Identifier(transferable.user_id);
		this.sessionCode = transferable.session_code;
	}

	public AccessIdentity(Date start, Identifier domainId, Identifier userId, String sessionCode) {
		this.start = start;
		this.domainId = domainId;
		this.userId = userId;
		this.sessionCode = sessionCode;
	}

	public Identifier getDomainId() {
		return this.domainId;
	}

	public String getSessionCode() {
		return this.sessionCode;
	}

	public Date getStart() {
		return this.start;
	}

	public IDLEntity getTransferable() {
		return new AccessIdentifier_Transferable(this.start.getTime(),
				(Identifier_Transferable) this.domainId.getTransferable(),
				(Identifier_Transferable) this.userId.getTransferable(),
				this.sessionCode);
	}

	public Identifier getUserId() {
		return this.userId;
	}
}
