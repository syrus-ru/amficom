/*
 * $Id: ARServerImpl.java,v 1.9 2005/04/04 13:55:21 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.arserver;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.syrus.AMFICOM.arserver.corba.ARServerPOA;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierGenerator;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.AccessIdentifier_Transferable;
import com.syrus.AMFICOM.general.corba.CompletionStatus;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_TransferablePackage.StorableObjectConditionSort;
import com.syrus.AMFICOM.resource.AbstractImageResource;
import com.syrus.AMFICOM.resource.BitmapImageResource;
import com.syrus.AMFICOM.resource.FileImageResource;
import com.syrus.AMFICOM.resource.ImageResourceDatabase;
import com.syrus.AMFICOM.resource.ResourceDatabaseContext;
import com.syrus.AMFICOM.resource.ResourceStorableObjectPool;
import com.syrus.AMFICOM.resource.SchemeImageResource;
import com.syrus.AMFICOM.resource.corba.ImageResource_Transferable;
import com.syrus.AMFICOM.resource.corba.ImageResource_TransferablePackage.ImageResourceDataPackage.ImageResourceSort;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.9 $, $Date: 2005/04/04 13:55:21 $
 * @author $Author: bass $
 * @module arserver_v1
 */
public class ARServerImpl extends ARServerPOA {
	
	private static final long	serialVersionUID	= 3257291335395782967L;
	
	protected StorableObjectCondition restoreCondition(StorableObjectCondition_Transferable transferable)
			throws IllegalDataException {
		StorableObjectCondition condition = null;
		switch (transferable.discriminator().value()) {
			case StorableObjectConditionSort._COMPOUND:
				condition = new CompoundCondition(transferable.compoundCondition());
				break;
			case StorableObjectConditionSort._LINKED_IDS:
				condition = new LinkedIdsCondition(transferable.linkedIdsCondition());
				break;
			case StorableObjectConditionSort._TYPICAL:
				condition = new TypicalCondition(transferable.typicalCondition());
				break;
			case StorableObjectConditionSort._EQUIVALENT:
				condition = new EquivalentCondition(transferable.equialentCondition());
				break;
			default:
				String msg = "ARServerImpl.restoreCondition | condition class " + transferable.getClass().getName() //$NON-NLS-1$
						+ " is not suppoted"; //$NON-NLS-1$
				Log.errorMessage(msg);
				throw new IllegalDataException(msg);

		}
		return condition;
	}
	
	// delete methods
	public void delete(Identifier_Transferable id_Transferable, 
			AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		Identifier id = new Identifier(id_Transferable);
		Log.debugMessage("ARServerImplementation.delete | trying to delete " + id, Log.DEBUGLEVEL03); //$NON-NLS-1$
		ResourceStorableObjectPool.delete(id);
	}
	
	public void deleteList(Identifier_Transferable[] id_Transferables,
			AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		Log.debugMessage("ARServerImplementation.deleteList | Trying to delete... ", Log.DEBUGLEVEL03); //$NON-NLS-1$
		final Set idList = new HashSet(id_Transferables.length);
		for (int i = 0; i < id_Transferables.length; i++)
			idList.add(new Identifier(id_Transferables[i]));			
		try {
        	ResourceStorableObjectPool.delete(idList);
        } catch (IllegalDataException e) {
        	 Log.errorException(e);
             throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e
                     .getMessage());
		}
	}
	
	// receive methods
	public void receiveImageResource(ImageResource_Transferable imageResource_Transferable,
			boolean force,
            AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		/**
		 * TODO check user for access
		 */
		Log.debugMessage("ARServerImplementation.receiveImageResource | Received " + " imageResource", Log.DEBUGLEVEL07); //$NON-NLS-1$ //$NON-NLS-2$
		try {
			imageResource_Transferable.header.modifier_id = accessIdentifier.user_id;
			ImageResourceSort sort = imageResource_Transferable.data.discriminator();
			AbstractImageResource abstractImageResource;
			switch (sort.value()) {
			case ImageResourceSort._BITMAP:
				abstractImageResource = new BitmapImageResource(imageResource_Transferable);
				break;
			case ImageResourceSort._FILE:
				abstractImageResource = new FileImageResource(imageResource_Transferable);
				break;
			case ImageResourceSort._SCHEME:
				abstractImageResource = new SchemeImageResource(imageResource_Transferable);
				break;
			default:
				throw new AMFICOMRemoteException("Unsupported sort value " + sort.value(), ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO,"Unsupported sort value"); //$NON-NLS-1$ //$NON-NLS-2$
			}         	
			ResourceStorableObjectPool.putStorableObject(abstractImageResource);
			ImageResourceDatabase database = ResourceDatabaseContext.getImageResourceDatabase();
			database.update(abstractImageResource, new Identifier(accessIdentifier.user_id), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
		} catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
					.getMessage());
		} catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(
					ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
					CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (VersionCollisionException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION,
					CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE,
					CompletionStatus.COMPLETED_NO, t.getMessage());
		}
    }
	
	public void receiveImageResources( ImageResource_Transferable[] imageResource_Transferables, 
			boolean force,
            AccessIdentifier_Transferable accessIdentifier)
            throws AMFICOMRemoteException {
        /**
        * TODO check user for access
        */
        Log.debugMessage("ARServerImplementation.receiveImageResources | Received " //$NON-NLS-1$
            + imageResource_Transferables.length + " ImageResources", Log.DEBUGLEVEL07); //$NON-NLS-1$
        final Set imageResources = new HashSet(imageResource_Transferables.length);
        try {
			for (int i = 0; i < imageResource_Transferables.length; i++) {
				imageResource_Transferables[i].header.modifier_id = accessIdentifier.user_id;
				AbstractImageResource abstractImageResource;
				ImageResourceSort sort = imageResource_Transferables[i].data
						.discriminator();
				switch (sort.value()) {
				case ImageResourceSort._BITMAP:
					abstractImageResource = new BitmapImageResource(
							imageResource_Transferables[i]);
					break;
				case ImageResourceSort._FILE:
					abstractImageResource = new FileImageResource(
							imageResource_Transferables[i]);
					break;
				case ImageResourceSort._SCHEME:
					abstractImageResource = new SchemeImageResource(
							imageResource_Transferables[i]);
					break;
				default:
					throw new AMFICOMRemoteException("Unsupported sort value " + sort.value(), ErrorCode.ERROR_RETRIEVE, //$NON-NLS-1$
							CompletionStatus.COMPLETED_NO,
							"Unsupported sort value " + sort.value()); //$NON-NLS-1$
				}
				ResourceStorableObjectPool
						.putStorableObject(abstractImageResource);
				imageResources.add(abstractImageResource);
			}
			ImageResourceDatabase database = ResourceDatabaseContext
					.getImageResourceDatabase();
			database.update(imageResources,
					new Identifier(accessIdentifier.user_id),
					force ? StorableObjectDatabase.UPDATE_FORCE
							: StorableObjectDatabase.UPDATE_CHECK);
		} catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE,
					CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(
					ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
					CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (VersionCollisionException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION,
					CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE,
					CompletionStatus.COMPLETED_NO, t.getMessage());
		}
    }
	
	// trannsmit methods
	public ImageResource_Transferable transmitImageResource(Identifier_Transferable id_Transferable,
			AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		Identifier id = new Identifier(id_Transferable);
        Log.debugMessage("ARServerImpl.transmitImageResource | require " + id.toString(), Log.DEBUGLEVEL07); //$NON-NLS-1$
        try {
            AbstractImageResource abstractImageResource = (AbstractImageResource) ResourceStorableObjectPool.getStorableObject(id, true);
            return (ImageResource_Transferable) abstractImageResource.getTransferable();
        } catch (ObjectNotFoundException onfe) {
            Log.errorException(onfe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES,
                                onfe.getMessage());
        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (CommunicationException ce) {
            Log.errorException(ce);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce
                    .getMessage());
        } catch (DatabaseException de) {
            Log.errorException(de);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de
                    .getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}
	
	public ImageResource_Transferable[] transmitImageResources(Identifier_Transferable[] ids_Transferable,
			AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		try {
			Identifier domainId = new Identifier(accessIdentifier.domain_id);
			Log.debugMessage("ARServerImpl.transmitImageResources | requiere " //$NON-NLS-1$
					+ (ids_Transferable.length == 0 ? "all" : Integer //$NON-NLS-1$
							.toString(ids_Transferable.length))
					+ " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07); //$NON-NLS-1$
			Collection list;
			if (ids_Transferable.length > 0) {
				final Set ids = new HashSet(ids_Transferable.length);
				for (int i = 0; i < ids_Transferable.length; i++)
					ids.add(new Identifier(ids_Transferable[i]));
				list = ResourceStorableObjectPool.getStorableObjects(ids, true);
			} else
				list = ResourceStorableObjectPool.getStorableObjectsByCondition(new EquivalentCondition(ObjectEntities.IMAGE_RESOURCE_ENTITY_CODE), true);

			ImageResource_Transferable[] transferables = new ImageResource_Transferable[list.size()];
			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				AbstractImageResource abstractImageResource = (AbstractImageResource) it.next();
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
	
	public ImageResource_Transferable[] transmitImageResourcesButIds(
            Identifier_Transferable[] identifier_Transferables,
            AccessIdentifier_Transferable accessIdentifier)
            throws AMFICOMRemoteException {
        try {
            Identifier domainId = new Identifier(accessIdentifier.domain_id);
            Log.debugMessage("ARServerImpl.transmitImageResourcesButIds | requiere " //$NON-NLS-1$
                    + (identifier_Transferables.length == 0 ? "all" : Integer //$NON-NLS-1$
                            .toString(identifier_Transferables.length))
                    + " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07); //$NON-NLS-1$
            Collection list;
            if (identifier_Transferables.length > 0) {
                final Set ids = new HashSet(identifier_Transferables.length);
                for (int i = 0; i < identifier_Transferables.length; i++)
                    ids.add(new Identifier(identifier_Transferables[i]));

                list = ResourceStorableObjectPool.getStorableObjectsByConditionButIds(ids, new EquivalentCondition(ObjectEntities.IMAGE_RESOURCE_ENTITY_CODE), true);

            } else
                list = ResourceStorableObjectPool.getStorableObjectsByCondition(new EquivalentCondition(ObjectEntities.IMAGE_RESOURCE_ENTITY_CODE), true);

            ImageResource_Transferable[] transferables = new ImageResource_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                AbstractImageResource abstractImageResource = (AbstractImageResource) it.next();
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
			AccessIdentifier_Transferable accessIdentifier,
			StorableObjectCondition_Transferable storableObjectCondition_Transferable)
			throws AMFICOMRemoteException {
		Log.debugMessage("ARServerImpl.transmitImageResourcesButIdsCondition | requiere " //$NON-NLS-1$
				+ (identifier_Transferables.length == 0 ? "all" //$NON-NLS-1$
						: Integer.toString(identifier_Transferables.length))
				+ " item(s) ", Log.DEBUGLEVEL07); //$NON-NLS-1$
		try {
			Collection list;
			StorableObjectCondition condition = this.restoreCondition(storableObjectCondition_Transferable);
			if (identifier_Transferables.length > 0) {
				final Set ids = new HashSet(identifier_Transferables.length);
				for (int i = 0; i < identifier_Transferables.length; i++)
					ids.add(new Identifier(identifier_Transferables[i]));

				list = ResourceStorableObjectPool.getStorableObjectsByConditionButIds(ids,
					condition , true);
			} else
				list = ResourceStorableObjectPool.getStorableObjectsByCondition(condition, true);

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
	
	public Identifier_Transferable getGeneratedIdentifier(short entityCode) throws AMFICOMRemoteException {
		try {
			Log.debugMessage("ARServerImpl.getGeneratedIdentifier | generate new Identifer for " //$NON-NLS-1$
					+ ObjectEntities.codeToString(entityCode), Log.DEBUGLEVEL07);
			Identifier identifier = IdentifierGenerator.generateIdentifier(entityCode);
			return (Identifier_Transferable) identifier.getTransferable();
		} catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(
								ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
								CompletionStatus.COMPLETED_NO,
								"Illegal object entity: '" //$NON-NLS-1$
										+ ObjectEntities
												.codeToString(entityCode)
										+ "'"); //$NON-NLS-1$
		} catch (IdentifierGenerationException ige) {
			Log.errorException(ige);
			throw new AMFICOMRemoteException(
								ErrorCode.ERROR_RETRIEVE,
								CompletionStatus.COMPLETED_NO,
								"Cannot create major/minor entries of identifier for entity: '" //$NON-NLS-1$
										+ ObjectEntities
												.codeToString(entityCode)
										+ "' -- " + ige.getMessage()); //$NON-NLS-1$
		}
	}
	
	public Identifier_Transferable[] getGeneratedIdentifierRange(
			short entityCode, int size) throws AMFICOMRemoteException {
		Log.debugMessage(
				"ARServerImpl.getGeneratedIdentifierRange | generate new Identifer range " //$NON-NLS-1$
						+ size + " for " //$NON-NLS-1$
						+ ObjectEntities.codeToString(entityCode),
				Log.DEBUGLEVEL07);
		try {
			Identifier[] identifiers = IdentifierGenerator
					.generateIdentifierRange(entityCode, size);
			Identifier_Transferable[] identifiersT = new Identifier_Transferable[identifiers.length];
			for (int i = 0; i < identifiersT.length; i++)
				identifiersT[i] = (Identifier_Transferable) identifiers[i]
						.getTransferable();
			return identifiersT;
		} catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(
					ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
					CompletionStatus.COMPLETED_NO, "Illegal object entity: '" //$NON-NLS-1$
							+ ObjectEntities.codeToString(entityCode) + "'"); //$NON-NLS-1$
		} catch (IdentifierGenerationException ige) {
			Log.errorException(ige);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE,
					CompletionStatus.COMPLETED_NO,
					"Cannot create major/minor entries of identifier for entity: '" //$NON-NLS-1$
							+ ObjectEntities.codeToString(entityCode) + "' -- " //$NON-NLS-1$
							+ ige.getMessage());
		}
	}	
}
