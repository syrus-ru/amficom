/*-
 * $Id: ServerCore.java,v 1.1 2005/05/18 12:52:59 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CompletionStatus;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.IdentifierGeneratorServer;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.Identifier_TransferableHolder;
import com.syrus.AMFICOM.general.corba.Verifiable;
import com.syrus.AMFICOM.security.corba.SessionKey_Transferable;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/05/18 12:52:59 $
 * @module csbridge_v1
 * @todo Refactor ApplicationException descendants to be capable of generating
 *       an AMFICOMRemoteException.
 */
public abstract class ServerCore implements IdentifierGeneratorServer, Verifiable {
	/**
	 * @param sessionKey an "in" parameter.
	 * @param userId an "out" parameter.
	 * @param domainId an "out" parameter.
	 * @throws AMFICOMRemoteException
	 * @todo Move method body here (declaring method itself as final)
	 */
	protected abstract void validateAccess(final SessionKey_Transferable sessionKey,
			final Identifier_TransferableHolder userId,
			final Identifier_TransferableHolder domainId)
			throws AMFICOMRemoteException;

	/**
	 * @param ids
	 * @param sessionKey
	 * @throws AMFICOMRemoteException
	 */
	public final void delete(final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		try {
			this.validateAccess(sessionKey,
					new Identifier_TransferableHolder(),
					new Identifier_TransferableHolder());
	
			Log.debugMessage("ServerCore.delete() | Trying to delete... ", Log.INFO);
			StorableObjectPool.delete(Identifier.fromTransferables(ids));
		} catch (final Throwable t) {
			throw this.processDefaultThrowable(t);
		}
	}

	/**
	 * @param b
	 * @see com.syrus.AMFICOM.general.corba.Verifiable#verify(byte)
	 */
	public final void verify(final byte b) {
		try {
			Log.debugMessage("ServerCore.verify() | Verifying value: " + b, Log.CONFIG);
		} catch (final Throwable t) {
			Log.debugException(t, Log.SEVERE);
		}
	}

	/**
	 * @param entityCode
	 * @param size
	 * @throws AMFICOMRemoteException
	 * @see com.syrus.AMFICOM.general.corba.IdentifierGeneratorServer#getGeneratedIdentifierRange(short, int)
	 */
	public final Identifier_Transferable[] getGeneratedIdentifierRange(
			final short entityCode, final int size)
			throws AMFICOMRemoteException {
		try {
			Log.debugMessage("ServerCore.getGeneratedIdentifierRange() | Generating "
					+ size + " identifiers of type: "
					+ ObjectEntities.codeToString(entityCode),
					Log.CONFIG);
			return Identifier.createTransferables(IdentifierGenerator.generateIdentifierRange(entityCode, size));
		} catch (final IllegalObjectEntityException ioee) {
			throw this.processDefaultIllegalObjectEntityException(ioee, entityCode);
		} catch (final IdentifierGenerationException ige) {
			throw this.processDefaultIdentifierGenerationException(ige, entityCode);
		} catch (final Throwable t) {
			throw this.processDefaultThrowable(t);
		}
	}

	public final Identifier_Transferable getGeneratedIdentifier(short entityCode) throws AMFICOMRemoteException {
		try {
			Log.debugMessage("ServerCore.getGeneratedIdentifier() | Generating an identifer of type: "
					+ ObjectEntities.codeToString(entityCode),
					Log.CONFIG);
			return (Identifier_Transferable) IdentifierGenerator.generateIdentifier(entityCode).getTransferable();
		} catch (final IllegalObjectEntityException ioee) {
			throw this.processDefaultIllegalObjectEntityException(ioee, entityCode);
		} catch (final IdentifierGenerationException ige) {
			throw this.processDefaultIdentifierGenerationException(ige, entityCode);
		} catch (final Throwable t) {
			throw this.processDefaultThrowable(t);
		}
	}

	private AMFICOMRemoteException processDefaultThrowable(final Throwable t) {
		return new AMFICOMRemoteException(
				ErrorCode.ERROR_UNKNOWN,
				CompletionStatus.COMPLETED_PARTIALLY,
				t.getMessage());
	}

	private AMFICOMRemoteException processDefaultIllegalObjectEntityException(
			final IllegalObjectEntityException ioee,
			final short entityCode) {
		Log.errorException(ioee);
		return new AMFICOMRemoteException(
				ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
				CompletionStatus.COMPLETED_NO,
				"Illegal object entity: '"
				+ ObjectEntities.codeToString(entityCode)
				+ '\'');
	}

	private AMFICOMRemoteException processDefaultIdentifierGenerationException(final IdentifierGenerationException ige, final short entityCode) {
		Log.errorException(ige);
		return new AMFICOMRemoteException(
				ErrorCode.ERROR_RETRIEVE,
				CompletionStatus.COMPLETED_NO,
				"Cannot create major/minor entries of identifier for entity: '"
				+ ObjectEntities.codeToString(entityCode)
				+ "' -- "
				+ ige.getMessage());
	}
}
