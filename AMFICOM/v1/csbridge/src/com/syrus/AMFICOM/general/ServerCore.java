/*-
 * $Id: ServerCore.java,v 1.16 2005/06/07 16:34:04 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CommonServer;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.Identifier_TransferableHolder;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.CompletionStatus;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.ErrorCode;
import com.syrus.AMFICOM.security.corba.SessionKey_Transferable;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.16 $, $Date: 2005/06/07 16:34:04 $
 * @module csbridge_v1
 * @todo Refactor ApplicationException descendants to be capable of generating
 *       an AMFICOMRemoteException.
 */
public abstract class ServerCore implements CommonServer {
	private static final long serialVersionUID = 2873567194611284256L;

	/**
	 * @param sessionKeyT an "in" parameter.
	 * @param userIdH an "out" parameter.
	 * @param domainIdH an "out" parameter.
	 * @throws AMFICOMRemoteException
	 * @todo Move method body here (declaring method itself as final)
	 */
	protected abstract void validateAccess(final SessionKey_Transferable sessionKeyT,
			final Identifier_TransferableHolder userIdH,
			final Identifier_TransferableHolder domainIdH)
			throws AMFICOMRemoteException;

	/**
	 * @param idsT
	 * @param sessionKeyT
	 * @throws AMFICOMRemoteException
	 */
	public final void delete(final Identifier_Transferable[] idsT,
			final SessionKey_Transferable sessionKeyT)
			throws AMFICOMRemoteException {
		try {
			this.validateAccess(sessionKeyT,
					new Identifier_TransferableHolder(),
					new Identifier_TransferableHolder());
	
			Log.debugMessage("ServerCore.delete() | Trying to delete... ", Log.INFO);
			StorableObjectPool.delete(Identifier.fromTransferables(idsT));
		} catch (final AMFICOMRemoteException are) {
			throw are;
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

	protected final IDLEntity[] transmitStorableObjects(final Identifier_Transferable[] idsT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		try {
			assert idsT != null && sessionKeyT != null: ErrorMessages.NON_NULL_EXPECTED;
			final int length = idsT.length;
			assert length != 0: ErrorMessages.NON_EMPTY_EXPECTED;
			assert StorableObject.hasSingleTypeEntities(idsT);
	
			final Identifier_TransferableHolder userIdH = new Identifier_TransferableHolder();
			final Identifier_TransferableHolder domainIdH = new Identifier_TransferableHolder();
			this.validateAccess(sessionKeyT, userIdH, domainIdH);

			Log.debugMessage("ServerCore.transmitStorableObjects() | Requested " + length + " storable object(s) of type: "
					+ ObjectEntities.codeToString(StorableObject.getEntityCodeOfIdentifiables(idsT)), Log.FINEST);

			final Set storableObjects = StorableObjectPool.getStorableObjects(Identifier.fromTransferables(idsT), true);
			final IDLEntity[] transferables = new IDLEntity[storableObjects.size()];
			int i = 0;
			for (final Iterator storableObjectIterator = storableObjects.iterator(); storableObjectIterator.hasNext(); i++)
				transferables[i] = ((StorableObject) storableObjectIterator.next()).getTransferable();
			return transferables;
		}
		catch (final ApplicationException ae) {
			throw this.processDefaultApplicationException(ae, ErrorCode.ERROR_RETRIEVE);
		}
		catch (final AMFICOMRemoteException are) {
			throw are;
		}
		catch (final Throwable t) {
			throw this.processDefaultThrowable(t);
		}
	}

	protected final IDLEntity[] transmitStorableObjectsButIdsByCondition(final Identifier_Transferable[] idsT,
			final SessionKey_Transferable sessionKeyT,
			final StorableObjectCondition_Transferable conditionT) throws AMFICOMRemoteException {
		try {
			assert idsT != null && sessionKeyT != null && conditionT != null : ErrorMessages.NON_NULL_EXPECTED;

			final StorableObjectCondition condition = StorableObjectConditionBuilder.restoreCondition(conditionT);
			final short entityCode = condition.getEntityCode().shortValue();

			assert StorableObject.hasSingleTypeEntities(idsT);
			assert idsT.length == 0 || entityCode == StorableObject.getEntityCodeOfIdentifiables(idsT);
			assert ObjectEntities.isEntityCodeValid(entityCode);

			final Identifier_TransferableHolder userId = new Identifier_TransferableHolder();
			final Identifier_TransferableHolder domainId = new Identifier_TransferableHolder();
			this.validateAccess(sessionKeyT, userId, domainId);
			Log.debugMessage("ServerCore.transmitStorableObjectsButIdsCondition() | Requested storable object(s) of type: "
					+ ObjectEntities.codeToString(entityCode), Log.FINEST);

			/**
			 * NOTE: If it is impossible to load objects by Loader - return only those from Pool
			 */
			final Set storableObjects = StorableObjectPool.getStorableObjectsByConditionButIds(Identifier.fromTransferables(idsT),
					condition,
					true,
					false);
			final IDLEntity[] transferables = new IDLEntity[storableObjects.size()];
			int i = 0;
			for (final Iterator storableObjectIterator = storableObjects.iterator(); storableObjectIterator.hasNext(); i++)
				transferables[i] = ((StorableObject) storableObjectIterator.next()).getTransferable();
			return transferables;
		}
		catch (final ApplicationException ae) {
			throw this.processDefaultApplicationException(ae, ErrorCode.ERROR_RETRIEVE);
		}
		catch (final AMFICOMRemoteException are) {
			throw are;
		}
		catch (final Throwable t) {
			throw this.processDefaultThrowable(t);
		}
	}

	protected final StorableObject_Transferable[] receiveStorableObjects(final short entityCode,
			final IDLEntity[] transferables,
			final boolean force,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		try {
			final Identifier_TransferableHolder userIdH = new Identifier_TransferableHolder();
			final Identifier_TransferableHolder domainIdH = new Identifier_TransferableHolder();
			this.validateAccess(sessionKeyT, userIdH, domainIdH);

			final Set storableObjects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				try {
					storableObjects.add(StorableObjectPool.fromTransferable(entityCode, transferables[i]));
				}
				catch (final ApplicationException ae) {
					Log.debugException(ae, Log.SEVERE);
				}
			}

			final StorableObjectDatabase database = DatabaseContext.getDatabase(entityCode);
			database.update(storableObjects,
					new Identifier(userIdH.value),
					force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
			return StorableObject.createHeadersTransferable(storableObjects);
		}
		catch (final VersionCollisionException vce) {
			throw this.processDefaultApplicationException(vce, ErrorCode.ERROR_VERSION_COLLISION);
		}
		catch (final UpdateObjectException uoe) {
			throw this.processDefaultApplicationException(uoe, ErrorCode.ERROR_UPDATE);
		}
		catch (final AMFICOMRemoteException are) {
			throw are;
		}
		catch (final Throwable t) {
			throw this.processDefaultThrowable(t);
		}
	}

	/**
	 * Accepts headers of a <em>single</em> <em>solitary</em> group.
	 *
	 * @param headers
	 * @param sessionKeyT
	 * @throws AMFICOMRemoteException
	 * @see com.syrus.AMFICOM.general.corba.CommonServer#transmitRefreshedStorableObjects(com.syrus.AMFICOM.general.corba.StorableObject_Transferable[], com.syrus.AMFICOM.security.corba.SessionKey_Transferable)
	 */
	public final Identifier_Transferable[] transmitRefreshedStorableObjects(
			final StorableObject_Transferable headers[],
			final SessionKey_Transferable sessionKeyT)
			throws AMFICOMRemoteException {
		this.validateAccess(sessionKeyT,
				new Identifier_TransferableHolder(),
				new Identifier_TransferableHolder());

		final Map headerMap = new HashMap();
		for (int i = 0; i < headers.length; i++)
			headerMap.put(new Identifier(headers[i].id), headers[i]);

		try {
			StorableObjectPool.refresh();

			final Set storableObjects = StorableObjectPool.getStorableObjects(headerMap.keySet(), true);
			for (final Iterator storableObjectIterator = storableObjects.iterator(); storableObjectIterator.hasNext();) {
				final StorableObject storableObject = (StorableObject) storableObjectIterator.next();
				final StorableObject_Transferable header = (StorableObject_Transferable) headerMap.get(storableObject.getId());
				/*
				 * Remove objects with older versions as well as objects with the same versions.
				 * Not only with older ones!
				 */
				if (!storableObject.hasNewerVersion(header.version))
					storableObjectIterator.remove();
			}

			return Identifier.createTransferables(storableObjects);
		} catch (final ApplicationException ae) {
			throw this.processDefaultApplicationException(ae, ErrorCode.ERROR_RETRIEVE);
		} catch (final Throwable t) {
			throw this.processDefaultThrowable(t);
		}
	}

	protected final AMFICOMRemoteException processDefaultThrowable(final Throwable t) {
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
