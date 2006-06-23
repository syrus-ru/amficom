/*-
 * $Id: IdentifierGeneratorServerImpl.java,v 1.1.1.1 2006/06/23 13:53:41 cvsadmin Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.systemserver;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.IdlCompletionStatus.COMPLETED_NO;
import static com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.IdlErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY;
import static com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.IdlErrorCode.ERROR_RETRIEVE;
import static java.util.logging.Level.CONFIG;
import static java.util.logging.Level.SEVERE;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierGenerator;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.security.SessionKey;
import com.syrus.AMFICOM.security.corba.IdlSessionKey;
import com.syrus.AMFICOM.systemserver.corba.IdentifierGeneratorServerOperations;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.1.1.1 $, $Date: 2006/06/23 13:53:41 $
 * @author $Author: cvsadmin $
 * @author Tashoyan Arseniy Feliksovich
 * @module systemserver
 */
final class IdentifierGeneratorServerImpl extends ServerCore implements IdentifierGeneratorServerOperations {


	// /////////////////////////////// IdentifierGeneratorServer /////////////////////////////////////////////

	public IdlIdentifier getGeneratedIdentifier(final short entityCode, final IdlSessionKey idlSessionKey)
			throws AMFICOMRemoteException {
		try {
			assert idlSessionKey != null : NON_NULL_EXPECTED;

			this.validateLogin(SessionKey.valueOf(idlSessionKey));

			Log.debugMessage("Generating an identifier for '" + ObjectEntities.codeToString(entityCode) + "'", CONFIG);
			return IdentifierGenerator.generateIdentifier(entityCode).getIdlTransferable();
		} catch (final IllegalObjectEntityException ioee) {
			throw this.processDefaultIllegalObjectEntityException(ioee, entityCode);
		} catch (final IdentifierGenerationException ige) {
			throw this.processDefaultIdentifierGenerationException(ige, entityCode);
		} catch (final Throwable t) {
			throw this.processDefaultThrowable(t);
		}
	}

	public IdlIdentifier[] getGeneratedIdentifierRange(final short entityCode, final int size, final IdlSessionKey idlSessionKey)
			throws AMFICOMRemoteException {
		try {
			assert idlSessionKey != null : NON_NULL_EXPECTED;

			this.validateLogin(SessionKey.valueOf(idlSessionKey));

			Log.debugMessage("Generating " + size + " identifiers for '" + ObjectEntities.codeToString(entityCode) + "'", CONFIG);
			return Identifier.createTransferables(IdentifierGenerator.generateIdentifierRange(entityCode, size));
		} catch (final IllegalObjectEntityException ioee) {
			throw this.processDefaultIllegalObjectEntityException(ioee, entityCode);
		} catch (final IdentifierGenerationException ige) {
			throw this.processDefaultIdentifierGenerationException(ige, entityCode);
		} catch (final Throwable t) {
			throw this.processDefaultThrowable(t);
		}
	}



	// /////////////////////////// helper methods /////////////////////////////////////////////////

	private final AMFICOMRemoteException processDefaultIllegalObjectEntityException(final IllegalObjectEntityException ioee,
			final short entityCode) {
		Log.debugMessage(ioee, SEVERE);
		return new AMFICOMRemoteException(ERROR_ILLEGAL_OBJECT_ENTITY,
				COMPLETED_NO,
				"Illegal object entity:" + ObjectEntities.asString(entityCode));
	}

	private final AMFICOMRemoteException processDefaultIdentifierGenerationException(final IdentifierGenerationException ige,
			final short entityCode) {
		Log.debugMessage(ige, SEVERE);
		return new AMFICOMRemoteException(ERROR_RETRIEVE,
				COMPLETED_NO,
				"Cannot create identifier for entity:" + ObjectEntities.asString(entityCode) + " -- " + ige.getMessage());
	}

}
