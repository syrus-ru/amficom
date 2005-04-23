/*
 * $Id: AccessIdentity.java,v 1.6 2005/04/23 13:32:53 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.util.Date;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.corba.AccessIdentity_Transferable;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;

/**
 * @version $Revision: 1.6 $, $Date: 2005/04/23 13:32:53 $
 * @author $Author: arseniy $
 * @module general_v1
 */
public class AccessIdentity implements TransferableObject {


	private Date startupDate;
	private Identifier domainId;
	private Identifier userId;
	private String sessionCode;

	public AccessIdentity(AccessIdentity_Transferable transferable) {
		this.startupDate = new Date(transferable.startup_date);
		this.domainId = new Identifier(transferable.domain_id);
		this.userId = new Identifier(transferable.user_id);
		this.sessionCode = transferable.session_code;
	}

	public AccessIdentity(Date start, Identifier domainId, Identifier userId, String sessionCode) {
		this.startupDate = start;
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

	public Date getStartupDate() {
		return this.startupDate;
	}

	public IDLEntity getTransferable() {
		return new AccessIdentity_Transferable(this.startupDate.getTime(),
				(Identifier_Transferable) this.domainId.getTransferable(),
				(Identifier_Transferable) this.userId.getTransferable(),
				this.sessionCode);
	}

	public Identifier getUserId() {
		return this.userId;
	}
}
