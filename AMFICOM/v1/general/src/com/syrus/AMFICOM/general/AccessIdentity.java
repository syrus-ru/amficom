/*
* $Id: AccessIdentity.java,v 1.1 2005/02/11 09:10:42 bob Exp $
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
 * @version $Revision: 1.1 $, $Date: 2005/02/11 09:10:42 $
 * @author $Author: bob $
 * @module general_v1
 */
public class AccessIdentity implements TransferableObject {

	private Date start;
	private Identifier domainId;
	private Identifier userId;
	private Identifier sessionId;
	
	public AccessIdentity(Date start, 
	                      Identifier domainId,
	                      Identifier userId,
	                      Identifier sessionId) {
		this.start = start;
		this.domainId = domainId;
		this.userId = userId;
		this.sessionId = sessionId;
	}
	
	public AccessIdentity(AccessIdentifier_Transferable transferable) {
		this.start = new Date(transferable.started);
		this.domainId = new Identifier(transferable.domain_id);
		this.userId = new Identifier(transferable.user_id);
		this.sessionId = new Identifier(transferable.session_id);
	}
	
	public Identifier getUserId() {
		return this.userId;
	}	
	
	public Object getTransferable() {		
		return new AccessIdentifier_Transferable(this.start.getTime(),
			(Identifier_Transferable)this.domainId.getTransferable(),
			(Identifier_Transferable)this.userId.getTransferable(),
			(Identifier_Transferable)this.sessionId.getTransferable());
	}
}
