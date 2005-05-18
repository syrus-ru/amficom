/*
 * $Id: ARServerImpl.java,v 1.14 2005/05/18 12:56:28 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.arserver;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.arserver.corba.ARServerOperations;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.ServerCore;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectConditionBuilder;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CompletionStatus;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.Identifier_TransferableHolder;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.AMFICOM.resource.AbstractImageResource;
import com.syrus.AMFICOM.resource.BitmapImageResource;
import com.syrus.AMFICOM.resource.FileImageResource;
import com.syrus.AMFICOM.resource.ResourceDatabaseContext;
import com.syrus.AMFICOM.resource.SchemeImageResource;
import com.syrus.AMFICOM.resource.corba.ImageResource_Transferable;
import com.syrus.AMFICOM.resource.corba.ImageResource_TransferablePackage.ImageResourceDataPackage.ImageResourceSort;
import com.syrus.AMFICOM.security.corba.SessionKey_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.14 $, $Date: 2005/05/18 12:56:28 $
 * @author $Author: bass $
 * @module arserver_v1
 */
public final class ARServerImpl extends ServerCore implements ARServerOperations {
	private static final long serialVersionUID = 3257291335395782967L;

	/**
	 * Can not be generalized.
	 *
	 * @param transferables
	 * @param force
	 * @param sessionKey
	 * @throws AMFICOMRemoteException
	 * @see com.syrus.AMFICOM.arserver.corba.ARServerOperations#receiveImageResources(com.syrus.AMFICOM.resource.corba.ImageResource_Transferable[], boolean, com.syrus.AMFICOM.security.corba.SessionKey_Transferable)
	 */
	public void receiveImageResources(
			final ImageResource_Transferable transferables[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		final Identifier_TransferableHolder userIdHolder = new Identifier_TransferableHolder();
		this.validateAccess(sessionKey, userIdHolder,
				new Identifier_TransferableHolder());
		final Identifier_Transferable userId = userIdHolder.value;

		Log.debugMessage("ARServerImpl.receiveImageResources | Received "
				+ transferables.length
				+ " ImageResources", Log.DEBUGLEVEL07);
		final Set imageResources = new HashSet(transferables.length);
		try {
			for (int i = 0; i < transferables.length; i++) {
				final ImageResource_Transferable transferable = transferables[i];
				transferable.header.modifier_id = userId;
				AbstractImageResource abstractImageResource;
				final ImageResourceSort sort = transferable.data.discriminator();
				switch (sort.value()) {
					case ImageResourceSort._BITMAP:
						abstractImageResource = new BitmapImageResource(transferable);
						break;
					case ImageResourceSort._FILE:
						abstractImageResource = new FileImageResource(transferable);
						break;
					case ImageResourceSort._SCHEME:
						abstractImageResource = new SchemeImageResource(transferable);
						break;
					default:
						throw new AMFICOMRemoteException(
								ErrorCode.ERROR_RETRIEVE,
								CompletionStatus.COMPLETED_NO,
								"Unsupported sort value " + sort.value());
				}
				StorableObjectPool.putStorableObject(abstractImageResource);
				imageResources.add(abstractImageResource);
			}
			ResourceDatabaseContext.getImageResourceDatabase().update(
					imageResources,
					new Identifier(userId),
					force
							? StorableObjectDatabase.UPDATE_FORCE
							: StorableObjectDatabase.UPDATE_CHECK);
		} catch (final UpdateObjectException uoe) {
			Log.errorException(uoe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE,
					CompletionStatus.COMPLETED_NO,
					uoe.getMessage());
		} catch (final IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(
					ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
					CompletionStatus.COMPLETED_NO,
					ioee.getMessage());
		} catch (final VersionCollisionException vce) {
			Log.errorException(vce);
			throw new AMFICOMRemoteException(
					ErrorCode.ERROR_VERSION_COLLISION,
					CompletionStatus.COMPLETED_NO,
					vce.getMessage());
		} catch (final Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(
					ErrorCode.ERROR_RETRIEVE,
					CompletionStatus.COMPLETED_NO,
					t.getMessage());
		}
	}

	public ImageResource_Transferable[] transmitImageResources(
			final Identifier_Transferable ids[],
			SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		try {
			final Identifier_TransferableHolder domainId = new Identifier_TransferableHolder();
			this.validateAccess(sessionKey,
					new Identifier_TransferableHolder(),
					domainId);
			Log.debugMessage("ARServerImpl.transmitImageResources | requiere "
					+ (ids.length == 0 ? "all" : Integer
							.toString(ids.length))
					+ " item(s) in domain: " + domainId.value, Log.DEBUGLEVEL07);
			final Set storableObjects = (ids.length == 0
					? StorableObjectPool.getStorableObjectsByCondition(new EquivalentCondition(ObjectEntities.IMAGE_RESOURCE_ENTITY_CODE), true)
					: StorableObjectPool.getStorableObjects(Identifier.fromTransferables(ids), true));

			final ImageResource_Transferable transferables[] = new ImageResource_Transferable[storableObjects.size()];
			int i = 0;
			for (final Iterator storableObjectIterator = storableObjects.iterator(); storableObjectIterator.hasNext(); i++) {
				AbstractImageResource abstractImageResource = (AbstractImageResource) storableObjectIterator.next();
				transferables[i] = (ImageResource_Transferable) abstractImageResource.getTransferable();
			}
			return transferables;
		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
					.getMessage());
		} catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
					.getMessage());
		} catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
					.getMessage());
		} catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}
	
	public ImageResource_Transferable[] transmitImageResourcesButIdsCondition(
			Identifier_Transferable[] identifier_Transferables,
			SessionKey_Transferable sessionKey,
			StorableObjectCondition_Transferable storableObjectCondition_Transferable)
			throws AMFICOMRemoteException {
		Log.debugMessage("ARServerImpl.transmitImageResourcesButIdsCondition | requiere "
				+ (identifier_Transferables.length == 0 ? "all"
						: Integer.toString(identifier_Transferables.length))
				+ " item(s) ", Log.DEBUGLEVEL07);
		try {
			Collection list;
			StorableObjectCondition condition = StorableObjectConditionBuilder.restoreCondition(storableObjectCondition_Transferable);
			if (identifier_Transferables.length > 0) {
				final Set ids = new HashSet(identifier_Transferables.length);
				for (int i = 0; i < identifier_Transferables.length; i++)
					ids.add(new Identifier(identifier_Transferables[i]));

				list = StorableObjectPool.getStorableObjectsByConditionButIds(ids,
					condition , true);
			} else
				list = StorableObjectPool.getStorableObjectsByCondition(condition, true);

			ImageResource_Transferable[] transferables = new ImageResource_Transferable[list
					.size()];
			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				AbstractImageResource abstractImageResource = (AbstractImageResource) it.next();
				transferables[i] = (ImageResource_Transferable) abstractImageResource.getTransferable();
			}

			return transferables;

		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE,
					CompletionStatus.COMPLETED_NO, roe.getMessage());
		} catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE,
					CompletionStatus.COMPLETED_NO, ide.getMessage());
		} catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE,
					CompletionStatus.COMPLETED_NO, ioee.getMessage());
		} catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE,
					CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE,
					CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}
	
	/**
	 * @param sessionKey
	 * @param userId
	 * @param domainId
	 * @throws AMFICOMRemoteException
	 * @see com.syrus.AMFICOM.general.ServerCore#validateAccess(com.syrus.AMFICOM.security.corba.SessionKey_Transferable, com.syrus.AMFICOM.general.corba.Identifier_TransferableHolder, com.syrus.AMFICOM.general.corba.Identifier_TransferableHolder)
	 */
	protected void validateAccess(final SessionKey_Transferable sessionKey,
			final Identifier_TransferableHolder userId,
			final Identifier_TransferableHolder domainId)
			throws AMFICOMRemoteException {
		try {
			ARServerSessionEnvironment.getInstance()
					.getARServerServantManager()
					.getLoginServerReference()
					.validateAccess(sessionKey, userId, domainId);
		} catch (final CommunicationException ce) {
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ACCESS_VALIDATION, CompletionStatus.COMPLETED_NO, ce.getMessage());
		} catch (final Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ACCESS_VALIDATION, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	/**
	 * @param headers
	 * @param sessionKey
	 * @throws AMFICOMRemoteException
	 * @see com.syrus.AMFICOM.arserver.corba.ARServerOperations#transmitRefreshedResourceObjects(com.syrus.AMFICOM.general.corba.StorableObject_Transferable[], com.syrus.AMFICOM.security.corba.SessionKey_Transferable)
	 */
	public Identifier_Transferable[] transmitRefreshedResourceObjects(
			final StorableObject_Transferable headers[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		this.validateAccess(sessionKey,
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
			Log.errorException(ae);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_PARTIALLY, ae.getMessage());
		} catch (final Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_PARTIALLY, t.getMessage());
		}
	}
}
