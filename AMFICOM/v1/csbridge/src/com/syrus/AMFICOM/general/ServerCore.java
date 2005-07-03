/*-
 * $Id: ServerCore.java,v 1.22 2005/06/25 17:07:53 bass Exp $
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

import org.omg.CORBA.ORB;
import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.StorableObjectDatabase.UpdateKind;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CommonServer;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.IdlIdentifierHolder;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.CompletionStatus;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.ErrorCode;
import com.syrus.AMFICOM.security.corba.IdlSessionKey;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.22 $, $Date: 2005/06/25 17:07:53 $
 * @module csbridge_v1
 * @todo Refactor ApplicationException descendants to be capable of generating
 *       an AMFICOMRemoteException.
 */
public abstract class ServerCore implements CommonServer {
	private static final long serialVersionUID = 2873567194611284256L;

	private ORB orb;

	protected ServerCore(final ORB orb) {
		this.orb = orb;
	}

	/**
	 * @param sessionKeyT an "in" parameter.
	 * @param userIdH an "out" parameter.
	 * @param domainIdH an "out" parameter.
	 * @throws AMFICOMRemoteException
	 * @todo Move method body here (declaring method itself as final)
	 */
	protected abstract void validateAccess(final IdlSessionKey sessionKeyT,
			final IdlIdentifierHolder userIdH,
			final IdlIdentifierHolder domainIdH)
			throws AMFICOMRemoteException;

	/**
	 * @param idsT
	 * @param sessionKeyT
	 * @throws AMFICOMRemoteException
	 */
	public final void delete(final IdlIdentifier[] idsT,
			final IdlSessionKey sessionKeyT)
			throws AMFICOMRemoteException {
		try {
			this.validateAccess(sessionKeyT,
					new IdlIdentifierHolder(),
					new IdlIdentifierHolder());
	
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
	public final IdlIdentifier[] getGeneratedIdentifierRange(
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

	public final IdlIdentifier getGeneratedIdentifier(
			final short entityCode) throws AMFICOMRemoteException {
		try {
			Log.debugMessage("ServerCore.getGeneratedIdentifier() | Generating an identifier of type: "
					+ ObjectEntities.codeToString(entityCode),
					Log.CONFIG);
			return IdentifierGenerator.generateIdentifier(entityCode).getTransferable();
		} catch (final IllegalObjectEntityException ioee) {
			throw this.processDefaultIllegalObjectEntityException(ioee, entityCode);
		} catch (final IdentifierGenerationException ige) {
			throw this.processDefaultIdentifierGenerationException(ige, entityCode);
		} catch (final Throwable t) {
			throw this.processDefaultThrowable(t);
		}
	}

	protected final IDLEntity[] transmitStorableObjects(final IdlIdentifier[] idsT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		try {
			assert idsT != null && sessionKeyT != null: ErrorMessages.NON_NULL_EXPECTED;
			final int length = idsT.length;
			assert length != 0: ErrorMessages.NON_EMPTY_EXPECTED;
			assert StorableObject.hasSingleTypeEntities(idsT);
	
			final IdlIdentifierHolder userIdH = new IdlIdentifierHolder();
			final IdlIdentifierHolder domainIdH = new IdlIdentifierHolder();
			this.validateAccess(sessionKeyT, userIdH, domainIdH);

			Log.debugMessage("ServerCore.transmitStorableObjects() | Requested " + length + " storable object(s) of type: "
					+ ObjectEntities.codeToString(StorableObject.getEntityCodeOfIdentifiables(idsT)), Log.FINEST);

			final Set storableObjects = StorableObjectPool.getStorableObjects(Identifier.fromTransferables(idsT), true);
			final IDLEntity[] transferables = new IDLEntity[storableObjects.size()];
			int i = 0;
			for (final Iterator storableObjectIterator = storableObjects.iterator(); storableObjectIterator.hasNext(); i++)
				transferables[i] = ((StorableObject) storableObjectIterator.next()).getTransferable(this.orb);
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

	protected final IDLEntity[] transmitStorableObjectsButIdsByCondition(final IdlIdentifier[] idsT,
			final IdlSessionKey sessionKeyT,
			final IdlStorableObjectCondition conditionT) throws AMFICOMRemoteException {
		try {
			assert idsT != null && sessionKeyT != null && conditionT != null : ErrorMessages.NON_NULL_EXPECTED;

			final StorableObjectCondition condition = StorableObjectConditionBuilder.restoreCondition(conditionT);
			final short entityCode = condition.getEntityCode().shortValue();

			assert StorableObject.hasSingleTypeEntities(idsT);
			assert idsT.length == 0 || entityCode == StorableObject.getEntityCodeOfIdentifiables(idsT);
			assert ObjectEntities.isEntityCodeValid(entityCode);

			final IdlIdentifierHolder userId = new IdlIdentifierHolder();
			final IdlIdentifierHolder domainId = new IdlIdentifierHolder();
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
				transferables[i] = ((StorableObject) storableObjectIterator.next()).getTransferable(this.orb);
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

	protected final IdlStorableObject[] receiveStorableObjects(final short entityCode,
			final IDLEntity[] transferables,
			final boolean force,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		try {
			final IdlIdentifierHolder userIdH = new IdlIdentifierHolder();
			final IdlIdentifierHolder domainIdH = new IdlIdentifierHolder();
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
					force ? UpdateKind.UPDATE_FORCE : UpdateKind.UPDATE_CHECK);
			return StorableObject.createHeadersTransferable(this.orb, storableObjects);
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
	 * @see com.syrus.AMFICOM.general.corba.CommonServer#transmitRefreshedStorableObjects(com.syrus.AMFICOM.general.corba.IdlStorableObject[], com.syrus.AMFICOM.security.corba.IdlSessionKey)
	 */
	public final IdlIdentifier[] transmitRefreshedStorableObjects(
			final IdlStorableObject headers[],
			final IdlSessionKey sessionKeyT)
			throws AMFICOMRemoteException {
		this.validateAccess(sessionKeyT,
				new IdlIdentifierHolder(),
				new IdlIdentifierHolder());

		final Map<Identifier, IdlStorableObject> headerMap = new HashMap<Identifier, IdlStorableObject>();
		for (int i = 0; i < headers.length; i++)
			headerMap.put(new Identifier(headers[i].id), headers[i]);

		try {
			StorableObjectPool.refresh();

			final Set storableObjects = StorableObjectPool.getStorableObjects(headerMap.keySet(), true);
			for (final Iterator storableObjectIterator = storableObjects.iterator(); storableObjectIterator.hasNext();) {
				final StorableObject storableObject = (StorableObject) storableObjectIterator.next();
				final IdlStorableObject header = headerMap.get(storableObject.getId());
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
