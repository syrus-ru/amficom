/*-
 * $Id: IdentifierGeneratorServerCore.java,v 1.5 2005/10/31 12:29:52 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.general;

import java.util.logging.Level;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.IdentifierGeneratorServer;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.IdlCompletionStatus;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.IdlErrorCode;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.5 $, $Date: 2005/10/31 12:29:52 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module csbridge
 */
public abstract class IdentifierGeneratorServerCore extends ServerCore implements IdentifierGeneratorServer {

	protected IdentifierGeneratorServerCore(final LoginServerConnectionManager loginServerConnectionManager, final ORB orb) {
		super(loginServerConnectionManager, orb);
	}



	// /////////////////////////////// IdentifierGeneratorServer /////////////////////////////////////////////

	public final IdlIdentifier getGeneratedIdentifier(final short entityCode) throws AMFICOMRemoteException {
		try {
			Log.debugMessage("Generating an identifier for '"
					+ ObjectEntities.codeToString(entityCode) + "'", Level.CONFIG);
			return IdentifierGenerator.generateIdentifier(entityCode).getTransferable();
		} catch (final IllegalObjectEntityException ioee) {
			throw this.processDefaultIllegalObjectEntityException(ioee, entityCode);
		} catch (final IdentifierGenerationException ige) {
			throw this.processDefaultIdentifierGenerationException(ige, entityCode);
		} catch (final Throwable t) {
			throw this.processDefaultThrowable(t);
		}
	}

	public final IdlIdentifier[] getGeneratedIdentifierRange(final short entityCode, final int size) throws AMFICOMRemoteException {
		try {
			Log.debugMessage("Generating " + size
					+ " identifiers for '" + ObjectEntities.codeToString(entityCode) + "'", Level.CONFIG);
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
		Log.debugMessage(ioee, Level.SEVERE);
		return new AMFICOMRemoteException(IdlErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
				IdlCompletionStatus.COMPLETED_NO,
				"Illegal object entity: '" + ObjectEntities.codeToString(entityCode) + "'");
	}

	private final AMFICOMRemoteException processDefaultIdentifierGenerationException(final IdentifierGenerationException ige,
			final short entityCode) {
		Log.debugMessage(ige, Level.SEVERE);
		return new AMFICOMRemoteException(IdlErrorCode.ERROR_RETRIEVE,
				IdlCompletionStatus.COMPLETED_NO,
				"Cannot create identifier for entity: '" + ObjectEntities.codeToString(entityCode) + "' -- " + ige.getMessage());
	}
	
}
