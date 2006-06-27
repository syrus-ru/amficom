/*-
 * $Id: CORBAIdentifierFactory.java,v 1.1.2.1 2006/06/27 15:50:38 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.general;

import static com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.IdlErrorCode._ERROR_ILLEGAL_OBJECT_ENTITY;

import java.util.Set;

import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.systemserver.corba.IdentifierGeneratorServer;

/**
 * @version $Revision: 1.1.2.1 $, $Date: 2006/06/27 15:50:38 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module csbridge
 */
public final class CORBAIdentifierFactory implements IdentifierFactory {
	private final IdentifierGeneratorServerConnectionManager igsConnectionManager;

	public CORBAIdentifierFactory(final IdentifierGeneratorServerConnectionManager igsConnectionManager) {
		this.igsConnectionManager = igsConnectionManager;
	}

	public Identifier getGeneratedIdentifier(final short entityCode)
			throws CommunicationException,
				IdentifierGenerationException,
				IllegalObjectEntityException {
		final IdentifierGeneratorServer identifierGeneratorServer = this.igsConnectionManager.getIdentifierGeneratorServerReference();
		try {
			return Identifier.valueOf(identifierGeneratorServer.getGeneratedIdentifier(entityCode,
					LoginManager.getSessionKey().getIdlTransferable()));
		} catch (AMFICOMRemoteException are) {
			switch (are.errorCode.value()) {
				case _ERROR_ILLEGAL_OBJECT_ENTITY:
					throw new IllegalObjectEntityException(are.message, entityCode);
				default:
					throw new IdentifierGenerationException(are.message);
			}
		}
	}

	public Set<Identifier> getGeneratedIdentifierRange(final short entityCode, final int size)
			throws CommunicationException,
				IdentifierGenerationException,
				IllegalObjectEntityException {
		final IdentifierGeneratorServer identifierGeneratorServer = this.igsConnectionManager.getIdentifierGeneratorServerReference();
		try {
			return Identifier.fromTransferables(identifierGeneratorServer.getGeneratedIdentifierRange(entityCode,
					size,
					LoginManager.getSessionKey().getIdlTransferable()));
		} catch (AMFICOMRemoteException are) {
			switch (are.errorCode.value()) {
				case _ERROR_ILLEGAL_OBJECT_ENTITY:
					throw new IllegalObjectEntityException(are.message, entityCode);
				default:
					throw new IdentifierGenerationException(are.message);
			}
		}
	}

}
