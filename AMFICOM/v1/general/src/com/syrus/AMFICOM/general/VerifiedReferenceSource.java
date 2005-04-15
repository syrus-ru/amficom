/*
 * $Id: VerifiedReferenceSource.java,v 1.1 2005/04/15 22:06:37 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import org.omg.CORBA.SystemException;

import com.syrus.AMFICOM.general.corba.IdentifierGeneratorServerHelper;
import com.syrus.AMFICOM.general.corba.Verifiable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.1 $, $Date: 2005/04/15 22:06:37 $
 * @author $Author: arseniy $
 * @module general_v1
 */
public class VerifiedReferenceSource {
	private CORBAServer corbaServer;
	private String servantName;

	private Verifiable reference;
	private Object lock;

	public VerifiedReferenceSource(CORBAServer corbaServer, String servantName) {
		assert corbaServer != null : "corbaServer is NULL";
		assert servantName != null : "Servant name is NULL";

		this.corbaServer = corbaServer;
		this.servantName = servantName;

		this.lock = new Object();
	}

	public Verifiable getVerifiedReference() throws CommunicationException {
		synchronized (this.lock) {

			if (this.reference == null) {
				this.resetConnection();
			}

			if (this.reference != null) {
				try {
					this.reference.verify((byte) 1);
				}
				catch (SystemException se) {
					this.resetConnection();
				}
			}

			if (this.reference != null)
				return this.reference;
			throw new CommunicationException("Cannot establish connection with '" + this.servantName + "'");
		}
	}

	protected void resetConnection() {
		try {
			this.reference = IdentifierGeneratorServerHelper.narrow(this.corbaServer.resolveReference(this.servantName));
		}
		catch (CommunicationException ce) {
			Log.errorMessage("Cannot resolve '" + this.servantName + "'");
			Log.errorException(ce);
			this.reference = null;
		}
	}

}
