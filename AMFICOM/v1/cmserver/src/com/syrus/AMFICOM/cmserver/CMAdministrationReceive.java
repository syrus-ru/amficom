/*
 * $Id: CMAdministrationReceive.java,v 1.9 2005/05/03 14:27:01 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.cmserver;

import java.util.HashSet;
import java.util.Set;

import com.syrus.AMFICOM.administration.AdministrationDatabaseContext;
import com.syrus.AMFICOM.administration.AdministrationStorableObjectPool;
import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.DomainDatabase;
import com.syrus.AMFICOM.administration.MCM;
import com.syrus.AMFICOM.administration.MCMDatabase;
import com.syrus.AMFICOM.administration.Server;
import com.syrus.AMFICOM.administration.ServerDatabase;
import com.syrus.AMFICOM.administration.ServerProcess;
import com.syrus.AMFICOM.administration.ServerProcessDatabase;
import com.syrus.AMFICOM.administration.User;
import com.syrus.AMFICOM.administration.UserDatabase;
import com.syrus.AMFICOM.administration.corba.Domain_Transferable;
import com.syrus.AMFICOM.administration.corba.MCM_Transferable;
import com.syrus.AMFICOM.administration.corba.ServerProcess_Transferable;
import com.syrus.AMFICOM.administration.corba.Server_Transferable;
import com.syrus.AMFICOM.administration.corba.User_Transferable;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CompletionStatus;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.SessionKey_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.util.Log;
/**
 * @version $Revision: 1.9 $, $Date: 2005/05/03 14:27:01 $
 * @author $Author: arseniy $
 * @module cmserver_v1
 */

public abstract class CMAdministrationReceive extends CMGeneralReceive {

	private static final long serialVersionUID = 1181253000011968750L;	



	public StorableObject_Transferable[] receiveUsers(User_Transferable[] transferables,
			boolean force,
			SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		Identifier modifierId = super.validateAccess(sessionKeyT);

		Set objects = new HashSet(transferables.length);
		for (int i = 0; i < transferables.length; i++) {
			User object = null;
			try {
				final Identifier id = new Identifier(transferables[i].header.id);
				object = (User) AdministrationStorableObjectPool.fromTransferable(id, transferables[i]);
				if (object == null)
					object = new User(transferables[i]);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}

			if (object != null)
				objects.add(object);
		}

		UserDatabase database = AdministrationDatabaseContext.getUserDatabase();
		try {
			database.update(objects, modifierId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
			return StorableObject.createHeadersTransferable(objects);
		}
		catch (UpdateObjectException uoe) {
			Log.errorException(uoe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, uoe.getMessage());
		}
		catch (VersionCollisionException vce) {
			Log.errorException(vce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, vce.getMessage());
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, throwable.getMessage());
		}
	}

	public StorableObject_Transferable[] receiveDomains(Domain_Transferable[] transferables,
			boolean force,
			SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		Identifier modifierId = super.validateAccess(sessionKeyT);

		Set objects = new HashSet(transferables.length);
		for (int i = 0; i < transferables.length; i++) {
			Domain object = null;
			try {
				final Identifier id = new Identifier(transferables[i].header.id);
				object = (Domain) AdministrationStorableObjectPool.fromTransferable(id, transferables[i]);
				if (object == null)
					object = new Domain(transferables[i]);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}

			if (object != null)
				objects.add(object);
		}

		DomainDatabase database = AdministrationDatabaseContext.getDomainDatabase();
		try {
			database.update(objects, modifierId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
			return StorableObject.createHeadersTransferable(objects);
		}
		catch (UpdateObjectException uoe) {
			Log.errorException(uoe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, uoe.getMessage());
		}
		catch (VersionCollisionException vce) {
			Log.errorException(vce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, vce.getMessage());
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, throwable.getMessage());
		}
	}

	public StorableObject_Transferable[] receiveServers(Server_Transferable[] transferables,
			boolean force,
			SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		Identifier modifierId = super.validateAccess(sessionKeyT);

		Set objects = new HashSet(transferables.length);
		for (int i = 0; i < transferables.length; i++) {
			Server object = null;
			try {
				final Identifier id = new Identifier(transferables[i].header.id);
				object = (Server) AdministrationStorableObjectPool.fromTransferable(id, transferables[i]);
				if (object == null)
					object = new Server(transferables[i]);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}

			if (object != null)
				objects.add(object);
		}

		ServerDatabase database = AdministrationDatabaseContext.getServerDatabase();
		try {
			database.update(objects, modifierId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
			return StorableObject.createHeadersTransferable(objects);
		}
		catch (UpdateObjectException uoe) {
			Log.errorException(uoe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, uoe.getMessage());
		}
		catch (VersionCollisionException vce) {
			Log.errorException(vce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, vce.getMessage());
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, throwable.getMessage());
		}
	}

	public StorableObject_Transferable[] receiveMCMs(MCM_Transferable[] transferables,
			boolean force,
			SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		Identifier modifierId = super.validateAccess(sessionKeyT);

		Set objects = new HashSet(transferables.length);
		for (int i = 0; i < transferables.length; i++) {
			MCM object = null;
			try {
				final Identifier id = new Identifier(transferables[i].header.id);
				object = (MCM) AdministrationStorableObjectPool.fromTransferable(id, transferables[i]);
				if (object == null)
					object = new MCM(transferables[i]);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}

			if (object != null)
				objects.add(object);
		}

		MCMDatabase database = AdministrationDatabaseContext.getMCMDatabase();
		try {
			database.update(objects, modifierId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
			return StorableObject.createHeadersTransferable(objects);
		}
		catch (UpdateObjectException uoe) {
			Log.errorException(uoe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, uoe.getMessage());
		}
		catch (VersionCollisionException vce) {
			Log.errorException(vce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, vce.getMessage());
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, throwable.getMessage());
		}
	}

	public StorableObject_Transferable[] receiveServerProcesses(ServerProcess_Transferable[] transferables,
			boolean force,
			SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		Identifier modifierId = super.validateAccess(sessionKeyT);

		Set objects = new HashSet(transferables.length);
		for (int i = 0; i < transferables.length; i++) {
			ServerProcess object = null;
			try {
				final Identifier id = new Identifier(transferables[i].header.id);
				object = (ServerProcess) AdministrationStorableObjectPool.fromTransferable(id, transferables[i]);
				if (object == null)
					object = new ServerProcess(transferables[i]);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}

			if (object != null)
				objects.add(object);
		}

		ServerProcessDatabase database = AdministrationDatabaseContext.getServerProcessDatabase();
		try {
			database.update(objects, modifierId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
			return StorableObject.createHeadersTransferable(objects);
		}
		catch (UpdateObjectException uoe) {
			Log.errorException(uoe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, uoe.getMessage());
		}
		catch (VersionCollisionException vce) {
			Log.errorException(vce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, vce.getMessage());
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, throwable.getMessage());
		}
	}

}
