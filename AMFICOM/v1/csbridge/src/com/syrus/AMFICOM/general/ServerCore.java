/*-
 * $Id: ServerCore.java,v 1.6 2005/05/26 11:49:51 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CompletionStatus;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.IdentifierGeneratorServer;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.Identifier_TransferableHolder;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.AMFICOM.general.corba.Verifiable;
import com.syrus.AMFICOM.security.corba.SessionKey_Transferable;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: arseniy $
 * @version $Revision: 1.6 $, $Date: 2005/05/26 11:49:51 $
 * @module csbridge_v1
 * @todo Refactor ApplicationException descendants to be capable of generating
 *       an AMFICOMRemoteException.
 */
public abstract class ServerCore implements IdentifierGeneratorServer, Verifiable {
	private static final long serialVersionUID = 2873567194611284256L;

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

	public final Identifier_Transferable getGeneratedIdentifier(
			final short entityCode) throws AMFICOMRemoteException {
		try {
			Log.debugMessage("ServerCore.getGeneratedIdentifier() | Generating an identifier of type: "
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

	protected final IDLEntity[] transmitStorableObjects(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		try {
			assert ids != null
					&& sessionKey != null: ErrorMessages.NON_NULL_EXPECTED;
			final int length = ids.length;
			assert length != 0: ErrorMessages.NON_EMPTY_EXPECTED;
			assert StorableObject.hasSingleTypeEntities(ids);
	
			final Identifier_TransferableHolder userId = new Identifier_TransferableHolder();
			final Identifier_TransferableHolder domainId = new Identifier_TransferableHolder();
			this.validateAccess(sessionKey, userId, domainId);
			Log.debugMessage("ServerCore.transmitStorableObjects() | "
					+ length
					+ " storable object(s) of type: "
					+ ObjectEntities.codeToString(StorableObject.getEntityCodeOfIdentifiables(ids))
					+ " requested",
					Log.FINEST);
			final Set storableObjects = StorableObjectPool.getStorableObjects(Identifier.fromTransferables(ids), true);
			final IDLEntity transferables[] = new IDLEntity[storableObjects.size()];
			int i = 0;
			for (final Iterator storableObjectIterator = storableObjects.iterator(); storableObjectIterator.hasNext(); i++)
				transferables[i] = ((StorableObject) storableObjectIterator.next()).getTransferable();
			return transferables;
		} catch (final ApplicationException ae) {
			throw this.processDefaultApplicationException(ae, ErrorCode.ERROR_RETRIEVE);
		} catch (final AMFICOMRemoteException are) {
			throw are;
		} catch (final Throwable t) {
			throw this.processDefaultThrowable(t);
		}
	}

	protected final IDLEntity[] transmitStorableObjectsButIdsCondition(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey,
			final StorableObjectCondition_Transferable storableObjectCondition)
			throws AMFICOMRemoteException {
		try {
			assert ids != null
					&& sessionKey != null
					&& storableObjectCondition != null: ErrorMessages.NON_NULL_EXPECTED;

			final StorableObjectCondition storableObjectCondition2 = StorableObjectConditionBuilder.restoreCondition(storableObjectCondition);
			final short entityCode = storableObjectCondition2.getEntityCode().shortValue();

			assert StorableObject.hasSingleTypeEntities(ids);
			assert ids.length == 0 || entityCode == StorableObject.getEntityCodeOfIdentifiables(ids);
			assert ObjectEntities.isEntityCodeValid(entityCode);

			final Identifier_TransferableHolder userId = new Identifier_TransferableHolder();
			final Identifier_TransferableHolder domainId = new Identifier_TransferableHolder();
			this.validateAccess(sessionKey, userId, domainId);
			Log.debugMessage("ServerCore.transmitStorableObjectsButIdsCondition() | Storable object(s) of type: "
					+ ObjectEntities.codeToString(entityCode)
					+ " requested",
					Log.FINEST);

			final Set storableObjects = StorableObjectPool.getStorableObjectsByConditionButIds(Identifier.fromTransferables(ids), storableObjectCondition2, true);
			final IDLEntity transferables[] = new IDLEntity[storableObjects.size()];
			int i = 0;
			for (final Iterator storableObjectIterator = storableObjects.iterator(); storableObjectIterator.hasNext(); i++)
				transferables[i] = ((StorableObject) storableObjectIterator.next()).getTransferable();
			return transferables;
		} catch (final ApplicationException ae) {
			throw this.processDefaultApplicationException(ae, ErrorCode.ERROR_RETRIEVE);
		} catch (final AMFICOMRemoteException are) {
			throw are;
		} catch (final Throwable t) {
			throw this.processDefaultThrowable(t);
		}
	}

	protected final StorableObject_Transferable[] receiveStorableObjects(
			final short entityCode,
			final IDLEntity transferables[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		try {
			final Identifier_TransferableHolder userId = new Identifier_TransferableHolder();
			final Identifier_TransferableHolder domainId = new Identifier_TransferableHolder();
			this.validateAccess(sessionKey, userId, domainId);

			final Set storableObjects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++)
				try {
					storableObjects.add(StorableObjectPool.fromTransferable(ObjectEntities.EQUIPMENTTYPE_ENTITY_CODE, transferables[i]));
				} catch (final ApplicationException ae) {
					Log.debugException(ae, Log.SEVERE);
				}

			DatabaseContext.getDatabase(entityCode).update(storableObjects, new Identifier(userId.value), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
			return StorableObject.createHeadersTransferable(storableObjects);
		} catch (final VersionCollisionException vce) {
			throw this.processDefaultApplicationException(vce, ErrorCode.ERROR_VERSION_COLLISION);
		} catch (final UpdateObjectException uoe) {
			throw this.processDefaultApplicationException(uoe, ErrorCode.ERROR_UPDATE);
		} catch (final AMFICOMRemoteException are) {
			throw are;
		} catch (final Throwable t) {
			throw this.processDefaultThrowable(t);
		}
	}

	private AMFICOMRemoteException processDefaultThrowable(final Throwable t) {
		Log.debugException(t, Log.SEVERE);
		return new AMFICOMRemoteException(
				ErrorCode.ERROR_UNKNOWN,
				CompletionStatus.COMPLETED_PARTIALLY,
				t.getMessage());
	}

	private AMFICOMRemoteException processDefaultApplicationException(
			final ApplicationException ae,
			final ErrorCode errorCode) {
		Log.debugException(ae, Log.SEVERE);
		return new AMFICOMRemoteException(errorCode,
				CompletionStatus.COMPLETED_NO,
				ae.getMessage());
	}

	private AMFICOMRemoteException processDefaultIllegalObjectEntityException(
			final IllegalObjectEntityException ioee,
			final short entityCode) {
		Log.debugException(ioee, Log.SEVERE);
		return new AMFICOMRemoteException(
				ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
				CompletionStatus.COMPLETED_NO,
				"Illegal object entity: '"
				+ ObjectEntities.codeToString(entityCode)
				+ '\'');
	}

	private AMFICOMRemoteException processDefaultIdentifierGenerationException(final IdentifierGenerationException ige, final short entityCode) {
		Log.debugException(ige, Log.SEVERE);
		return new AMFICOMRemoteException(
				ErrorCode.ERROR_RETRIEVE,
				CompletionStatus.COMPLETED_NO,
				"Cannot create major/minor entries of identifier for entity: '"
				+ ObjectEntities.codeToString(entityCode)
				+ "' -- "
				+ ige.getMessage());
	}
}
