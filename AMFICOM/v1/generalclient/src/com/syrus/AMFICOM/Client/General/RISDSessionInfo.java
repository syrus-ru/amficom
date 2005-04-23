/*
 * $Id: RISDSessionInfo.java,v 1.40 2005/04/23 13:58:26 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.Client.General;

import java.io.File;
import java.util.Collection;
import java.util.Date;

import org.omg.CORBA.ORB;
import org.omg.CORBA.SystemException;
import org.omg.CORBA.UserException;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.InvalidName;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import org.omg.PortableServer.POAManagerPackage.AdapterInactive;

import com.syrus.AMFICOM.CORBA.AMFICOM;
import com.syrus.AMFICOM.CORBA.Constants;
//import com.syrus.AMFICOM.CORBA.Admin.AccessIdentity_Transferable;
import com.syrus.AMFICOM.CORBA.Admin.AccessIdentity_TransferableHolder;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.administration.AdministrationStorableObjectPool;
import com.syrus.AMFICOM.administration.ClientAdministrationObjectLoader;
import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.User;
import com.syrus.AMFICOM.administration.XMLAdministrationObjectLoader;
import com.syrus.AMFICOM.administration.corba.UserSort;
import com.syrus.AMFICOM.cmserver.corba.CMServer;
import com.syrus.AMFICOM.configuration.ClientConfigurationObjectLoader;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.XMLConfigurationObjectLoader;
import com.syrus.AMFICOM.corba.portable.client.Client;
import com.syrus.AMFICOM.corba.portable.client.ClientImpl;
import com.syrus.AMFICOM.corba.portable.client.ClientPOATie;
import com.syrus.AMFICOM.general.AccessIdentity;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.ClientGeneralObjectLoader;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.LocalIdentifierGeneratorServer;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.SessionContext;
import com.syrus.AMFICOM.general.StorableObjectResizableLRUMap;
import com.syrus.AMFICOM.general.XMLGeneralObjectLoader;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.AccessIdentity_Transferable;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.map.EmptyClientMapObjectLoader;
import com.syrus.AMFICOM.map.MapStorableObjectPool;
import com.syrus.AMFICOM.mapview.EmptyClientMapViewObjectLoader;
import com.syrus.AMFICOM.mapview.MapViewStorableObjectPool;
import com.syrus.AMFICOM.measurement.ClientMeasurementObjectLoader;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.AMFICOM.measurement.XMLMeasurementObjectLoader;
import com.syrus.AMFICOM.resource.EmptyClientResourceObjectLoader;
import com.syrus.AMFICOM.resource.ResourceStorableObjectPool;
import com.syrus.AMFICOM.scheme.EmptyClientSchemeObjectLoader;
import com.syrus.AMFICOM.scheme.SchemeStorableObjectPool;
import com.syrus.io.Rewriter;
import com.syrus.util.Log;
import com.syrus.util.corba.JavaSoftORBUtil;
import com.syrus.util.prefs.IIOPConnectionManager;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.40 $, $Date: 2005/04/23 13:58:26 $
 * @module generalclient_v1
 */
public final class RISDSessionInfo extends SessionInterface {
	/**
	 * Пользователь системы.
	 */
	public String ISMuser;

	/**
	 * Пароль пользователя.
	 */
	public String ISMpassword;

	/**
	 * Соединение сессии.
	 */
	public RISDConnectionInfo ci;

	/**
	 * Old-style session id.
	 *
	 * @deprecated Use {@link #getAccessIdentity()} instead.
	 */
	public com.syrus.AMFICOM.CORBA.Admin.AccessIdentity_Transferable accessIdentity;

	/**
	 * New-style session id.
	 */
	private AccessIdentity_Transferable accessIdentifier;

	private Identifier domainId;

	private Identifier userId;

	/**
	 * Время начала сессии.
	 */
	public long LogonTime;

	/**
	 * Состояние сессии.
	 */
	public int session_state = SESSION_CLOSED;

	/**
	 * The client-side CORBA object.
	 */
	private Client client = null;
	
	/**
	 * Конструктор - новая сессия для соединения.
	 */
	public RISDSessionInfo(ConnectionInterface ci) {
	}

	/**
	 * Открыть новую сессию с явным указанием параметров сессии.
	 */
	public static SessionInterface openSession(ConnectionInterface ci, String u, String p) {
		try {
			/*
			 * создать новый экземпляр сессии и открыть ее
			 */
			RISDSessionInfo si = new RISDSessionInfo(ci);
			si.ISMuser = u;
			si.ISMpassword = p;
			/*
			 * если сессия не открыта, то возвращается null, и созданный здесь
			 * экземпляр автоматически уничтожается
			 */
			return si.openSession();
		} catch (Exception e) {
			e.printStackTrace();
			setActiveSession(null);
			return null;
		}
	}

	/**
	 * Открыть сессию с установленными для нее параметрами.
	 * @throws ApplicationException 
	 */
	public SessionInterface openSession() throws ApplicationException {
		if(Environment.getConnectionType().equals(Environment.CONNECTION_EMPTY))
			return openLocalSession();
		return openRemoteSession();
	}
	
	private SessionInterface openRemoteSession() throws ApplicationException {
			/*
			 * параметр для возврата идентификатора сессии
			 */
			AccessIdentity_TransferableHolder accessIdentityHolder = new AccessIdentity_TransferableHolder();
			int ecode = Constants.ERROR_NO_CONNECT;

			if (ci == null)
				ci = (RISDConnectionInfo) (RISDConnectionInfo.getInstance());
			if (!ci.isConnected()) {
					try {
						ci.setConnected(true);
					} catch (SystemException e) {
						// TODO Auto-generated catch block
						throw new ApplicationException(e);
					} catch (UserException e) {
						// TODO Auto-generated catch block
						throw new ApplicationException(e);
					}
			}

			try {
				clientStartup();
			} catch (org.omg.CORBA.ORBPackage.InvalidName e1) {
				// TODO Auto-generated catch block
				throw new ApplicationException(e1);
			} catch (AdapterInactive e1) {
				// TODO Auto-generated catch block
				throw new ApplicationException(e1);
			} catch (CannotProceed e1) {
				// TODO Auto-generated catch block
				throw new ApplicationException(e1);
			} catch (InvalidName e1) {
				// TODO Auto-generated catch block
				throw new ApplicationException(e1);
			} catch (NotFound e1) {
				// TODO Auto-generated catch block
				throw new ApplicationException(e1);
			}
			String ior = JavaSoftORBUtil.getInstance().getORB().object_to_string(client);

			/*
			 * открыть сессию
			 */
			try {
				ecode = this.ci.getServer().Logon(getUser(), Rewriter.write(getPassword()), ior, accessIdentityHolder);
			} catch (NullPointerException npe) {
				npe.printStackTrace();
			}
			catch (com.syrus.AMFICOM.CORBA.General.AMFICOMRemoteException e) {
//				 TODO Auto-generated catch block
				e.printStackTrace();
//				throw new ApplicationException(e);
				return null;
			} catch (SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new ApplicationException(e);
			} 
			if (ecode != Constants.ERROR_NO_ERROR) {
				Log("Failed Logon! status = " + ecode);
				/*
				 * Second unsuccessful return point after client activation.
				 */
				try {
					clientShutdown(true);
				} catch (org.omg.CORBA.ORBPackage.InvalidName e) {
					e.printStackTrace();
					throw new ApplicationException(e);
				} catch (CannotProceed e) {
					e.printStackTrace();
					throw new ApplicationException(e);
				} catch (InvalidName e) {
					e.printStackTrace();
					throw new ApplicationException(e);
				} catch (NotFound e) {
					e.printStackTrace();
					throw new ApplicationException(e);
				}				
			}

			this.LogonTime = System.currentTimeMillis();
			this.session_state = SESSION_OPENED;
			this.accessIdentity = accessIdentityHolder.value;

			AMFICOM server = this.ci.getServer();
			CMServer cmServer = this.ci.getCmServer();
			if (server == null) { throw new ApplicationException("AMFICOM isn't resolved"); }
			if (cmServer == null) { throw new ApplicationException("AMFICOM CMServer isn't resolved"); }

			final String oldUserId = this.accessIdentity.user_id;
			try {
			final Identifier_Transferable userId
				= cmServer.reverseLookupUserLogin(
				server.lookupUserLogin(
				new Identifier_Transferable(oldUserId)));
			assert userId
				.identifier_string
				.equals(
				cmServer.reverseLookupUserName(
				server.lookupUserName(
				new Identifier_Transferable(oldUserId)))
				.identifier_string);
			this.accessIdentifier = new AccessIdentity_Transferable(
				System.currentTimeMillis(),

				cmServer.reverseLookupDomainName(
				server.lookupDomainName(
				new Identifier_Transferable(
				this.accessIdentity.domain_id))),

				userId,

				"нахЪ");
			this.domainId = new Identifier(this.accessIdentifier.domain_id);
			this.userId = new Identifier(this.accessIdentifier.user_id);
			final Class clazz = StorableObjectResizableLRUMap.class;
			final int size = 200;
			/* TODO really hostname can be 'null' ? */
			SessionContext.init(new AccessIdentity(this.accessIdentifier), null);
			//			ClientConfigurationObjectLoader.setAccessIdentifierTransferable(this.accessIdentifier);
			ConfigurationStorableObjectPool.init(new ClientConfigurationObjectLoader(cmServer), clazz, size);
			//			ClientMeasurementObjectLoader.setAccessIdentifierTransferable(this.accessIdentifier);
			MeasurementStorableObjectPool.init(new ClientMeasurementObjectLoader(cmServer), clazz, size);
			//			ClientAdministrationObjectLoader.setAccessIdentifierTransferable(this.accessIdentifier);
			AdministrationStorableObjectPool.init(new ClientAdministrationObjectLoader(cmServer), clazz, size);
			//			ClientGeneralObjectLoader.setAccessIdentifierTransferable(this.accessIdentifier);
			GeneralStorableObjectPool.init(new ClientGeneralObjectLoader(cmServer), clazz, size);
			MapStorableObjectPool.init(new EmptyClientMapObjectLoader(), clazz, size);
			SchemeStorableObjectPool.init(new EmptyClientSchemeObjectLoader(), clazz, size);
			MapViewStorableObjectPool.init(new EmptyClientMapViewObjectLoader(), clazz, size);
			ResourceStorableObjectPool.init(new EmptyClientResourceObjectLoader(), clazz, size);


			// Vse po umu
			IdentifierPool.init(this.ci.getCMServerConnectionManager());
//			IdentifierPool.init(new LocalIdentifierGeneratorServer());

			System.err.println("domainId: " + this.accessIdentifier.domain_id.identifier_string);
			System.err.println("sessionId: " + this.accessIdentifier.session_code);
			System.err.println("started: " + new java.util.Date(this.accessIdentifier.startup_date));
			System.err.println("userId: " + this.accessIdentifier.user_id.identifier_string);
			add(this);
			setActiveSession(this);
			return this;
		} catch (AMFICOMRemoteException e) {
			e.printStackTrace();
			throw new ApplicationException(e);
		}		
		catch (SystemException e) {
			e.printStackTrace();
			throw new ApplicationException(e);
		} 
	}
	
	
	private SessionInterface openLocalSession() {
		
			final Class clazz = StorableObjectResizableLRUMap.class;
			final int size = 200;

//			ClientConfigurationObjectLoader.setAccessIdentifierTransferable(this.accessIdentifier);
			File configPath = new File("/catalog");
			ConfigurationStorableObjectPool.init(new XMLConfigurationObjectLoader(configPath), clazz, size);

//			ClientMeasurementObjectLoader.setAccessIdentifierTransferable(this.accessIdentifier);
			MeasurementStorableObjectPool.init(new XMLMeasurementObjectLoader(configPath), clazz, size);

//			ClientAdministrationObjectLoader.setAccessIdentifierTransferable(this.accessIdentifier);
			AdministrationStorableObjectPool.init(new XMLAdministrationObjectLoader(configPath), clazz, size);

//			ClientGeneralObjectLoader.setAccessIdentifierTransferable(this.accessIdentifier);
			GeneralStorableObjectPool.init(new XMLGeneralObjectLoader(configPath), clazz, size);

			MapStorableObjectPool.init(new EmptyClientMapObjectLoader(), clazz, size);

			SchemeStorableObjectPool.init(new EmptyClientSchemeObjectLoader(), clazz, size);

			MapViewStorableObjectPool.init(new EmptyClientMapViewObjectLoader(), clazz, size);

			ResourceStorableObjectPool.init(new EmptyClientResourceObjectLoader(), clazz, size);

			IdentifierPool.init(new LocalIdentifierGeneratorServer());

			try {
				EquivalentCondition condition = new EquivalentCondition(ObjectEntities.USER_ENTITY_CODE);
				Collection users = AdministrationStorableObjectPool.getStorableObjectsByCondition(condition, true);
				User user;
				if (users.isEmpty()) {
					user = User.createInstance(new Identifier("User_0"), "sys", UserSort.USER_SORT_REGULAR, "sysuser", "");	
				}
				else {
					user = (User)users.iterator().next();
				}
				condition = new EquivalentCondition(ObjectEntities.DOMAIN_ENTITY_CODE);
				Collection domains = AdministrationStorableObjectPool.getStorableObjectsByCondition(condition, true);
				Domain domain;
				if (domains.isEmpty()) {
					domain = Domain.createInstance(user.getId(), new Identifier("Domain_0"), "LocalDomain", "");	
				}
				else {
					domain = (Domain)domains.iterator().next();
				}
				
				this.domainId = domain.getId();
				this.userId = user.getId();
				this.LogonTime = System.currentTimeMillis();
				this.accessIdentity = new com.syrus.AMFICOM.CORBA.Admin.AccessIdentity_Transferable(
						this.LogonTime,
						user.getName(),
						this.userId.getIdentifierString(),
						"нахЪ",
						this.domainId.getIdentifierString());
				this.session_state = SESSION_OPENED;
				this.accessIdentifier = new AccessIdentity_Transferable(
						this.LogonTime,
						(Identifier_Transferable)this.domainId.getTransferable(),
						(Identifier_Transferable)this.userId.getTransferable(),
						"нахЪ");
				
				/* TODO really hostname can be 'null' ? */
				SessionContext.init(new AccessIdentity(this.accessIdentifier), null);
				
				AdministrationStorableObjectPool.putStorableObject(domain);
				AdministrationStorableObjectPool.putStorableObject(user);
				AdministrationStorableObjectPool.flush(true);

				
				condition = new EquivalentCondition(ObjectEntities.PARAMETERTYPE_ENTITY_CODE);
				Collection paramaterTypes = GeneralStorableObjectPool.getStorableObjectsByCondition(condition, true);
				if (paramaterTypes.isEmpty()) {
					// кг/ам remove this chit НАХУЙ тоже
					 // creating some default ParameterTypes 
//					Identifier creatorId = user.getId();
//					String codename = ParameterTypeCodenames.DADARA_EVENTS;
//					String name = "Dadara events";
//					String description = "List of analysis events";
//					DataType dataType = DataType.DATA_TYPE_RAW;
//					ParameterType type = ParameterType.createInstance(creatorId, codename, name, description, dataType);
//					GeneralStorableObjectPool.putStorableObject(type);
//					
//					codename = ParameterTypeCodenames.DADARA_THRESHOLDS;
//					name = "Dadara thresholds";
//					description = "List of thresholds";
//					dataType = DataType.DATA_TYPE_RAW;
//					type = ParameterType.createInstance(creatorId, codename, name, description, dataType);
//					GeneralStorableObjectPool.putStorableObject(type);
//					
//					codename = ParameterTypeCodenames.MAX_NOISE_LEVEL;
//					name = "Maximal noise level";
//					description = "Maximal noise level";
//					dataType = DataType.DATA_TYPE_DOUBLE;
//					type = ParameterType.createInstance(creatorId, codename, name, description, dataType);
//					GeneralStorableObjectPool.putStorableObject(type);
//					
//					codename = ParameterTypeCodenames.MIN_CONNECTOR;
//					name = "Minimal connector level";
//					description = "Minimal connector level";
//					dataType = DataType.DATA_TYPE_DOUBLE;
//					type = ParameterType.createInstance(creatorId, codename, name, description, dataType);
//					GeneralStorableObjectPool.putStorableObject(type);
//					
//					codename = ParameterTypeCodenames.MIN_SPLICE;
//					name = "Minimal splice level";
//					description = "Minimal splice level";
//					dataType = DataType.DATA_TYPE_DOUBLE;
//					type = ParameterType.createInstance(creatorId, codename, name, description, dataType);
//					GeneralStorableObjectPool.putStorableObject(type);
//					
//					codename = ParameterTypeCodenames.MIN_END_LEVEL;
//					name = "Minimal trace end level";
//					description = "Minimal trace end level";
//					dataType = DataType.DATA_TYPE_DOUBLE;
//					type = ParameterType.createInstance(creatorId, codename, name, description, dataType);
//					GeneralStorableObjectPool.putStorableObject(type);
//					
//					codename = ParameterTypeCodenames.MIN_EVENT_LEVEL;
//					name = "Minimal event level";
//					description = "Minimal event level";
//					dataType = DataType.DATA_TYPE_DOUBLE;
//					type = ParameterType.createInstance(creatorId, codename, name, description, dataType);
//					GeneralStorableObjectPool.putStorableObject(type);
//					GeneralStorableObjectPool.flush(true);
				}

			Log.debugMessage("RISDSessionInfo.openLocalSession | domainId: "
				+ this.accessIdentifier.domain_id.identifier_string, Log.FINEST);
			Log.debugMessage("RISDSessionInfo.openLocalSession | sessionId: " + this.accessIdentifier.session_code,
				Log.FINEST);
			Log.debugMessage("RISDSessionInfo.openLocalSession | started: " + new Date(this.accessIdentifier.startup_date),
				Log.FINEST);
			Log.debugMessage("RISDSessionInfo.openLocalSession | userId: "
					+ this.accessIdentifier.user_id.identifier_string, Log.FINEST);
			
			add(this);
			setActiveSession(this);
			
			} 
			catch (ApplicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return this;
	}

	
	/**
	 * Закрыть сессию.
	 */
	public void closeSession() {
		/*
		 * если сессия открыта, то закрыть
		 */
		if (contains(this)) {
			/**
			 * @todo Later, show a confirmation dialog.
			 */
			try {
				GeneralStorableObjectPool.flush(true);
			} catch (ApplicationException ae) {
				ae.printStackTrace();
			}
			try {
				MeasurementStorableObjectPool.flush(true);
			} catch (ApplicationException ae) {
				ae.printStackTrace();
			}
			try {
				ConfigurationStorableObjectPool.flush(true);
			} catch (ApplicationException ae) {
				ae.printStackTrace();
			}
			if(!Environment.getConnectionType().equals(Environment.CONNECTION_EMPTY))
			{
				try {
					ci.getServer().Logoff(accessIdentity);
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					clientShutdown();
				} catch (UserException ue) {
					ue.printStackTrace();
			}
			}
		}
		session_state = SESSION_CLOSED;
		/*
		 * удалить из списка открытых сессий
		 */
		remove(this);
		if (isEmpty())
			setActiveSession(null);
	}

	/**
	 * Starts the client object up.
	 */
	private void clientStartup()
			throws org.omg.CORBA.ORBPackage.InvalidName,
			AdapterInactive,
			CannotProceed,
			InvalidName,
			NotFound {
		if (client != null)
			return;
		ORB orb = JavaSoftORBUtil.getInstance().getORB();
		POA rootPOA = POAHelper.narrow(
			orb.resolve_initial_references("RootPOA"));
		rootPOA.the_POAManager().activate();
		client = (new ClientPOATie(new ClientImpl(), rootPOA))._this(orb);
		NamingContextExt namingContextExt = NamingContextExtHelper.narrow(
			orb.resolve_initial_references("NameService"));
		namingContextExt.rebind(namingContextExt.to_name(
			"Client-" + IIOPConnectionManager.getClientId().replace('.', '_')),
			client);
	}

	private void clientShutdown()
			throws org.omg.CORBA.ORBPackage.InvalidName,
			CannotProceed,
			InvalidName,
			NotFound {
		clientShutdown(false);
	}

	/**
	 * Shuts the client object down.
	 *
	 * @todo Can we somehow use org.omg.CORBA.portable.ObjectImpl._non_existent()?
	 * @see org.omg.CORBA.portable.ObjectImpl#_non_existent()
	 */
	private void clientShutdown(boolean abnormalSessionTermination)
			throws org.omg.CORBA.ORBPackage.InvalidName,
			CannotProceed,
			InvalidName,
			NotFound {
		if (client != null) {
			NamingContextExt namingContextExt = NamingContextExtHelper.narrow(
				JavaSoftORBUtil.getInstance().getORB().
					resolve_initial_references("NameService"));
			namingContextExt.unbind(namingContextExt.to_name(
				"Client-" + IIOPConnectionManager.getClientId().
					replace('.', '_')));
			/*
			 * As mentioned in the API documentation,
			 *
			 * "signals that the caller is done using this object reference, so
			 * internal ORB resources associated with this object reference can be
			 * released. Note that the object implementation is not involved in
			 * this operation, and other references to the same object are not affected."
			 *
			 * In org.omg.CORBA.portable.ObjectImpl this method is implemented
			 * in the following way (the API documentation just says it
			 * "releases the resources associated with this ObjectImpl object."):
			 *
			 *	public void _release() {
			 *		_get_delegate().release(this);
			 *	}
			 *
			 * In case of Sun's JavaSoft ORB, the "vendor-specific
			 * implementation" of org.omg.CORBA.portable.Delegate is
			 * com.sun.corba.se.internal.POA.GenericPOAClientSC, whose release()
			 * method inherited from com.sun.corba.se.internal.corba.ClientDelegate
			 * just does nothing, with the following comment:
			 *
			 * "DONT clear out internal variables to release memory !!
			 * This delegate may be pointed-to by other objrefs !"
			 *
			 * In case of Borland VisiBroker 3.4, the implementation of
			 * Delegate is com.visigenic.vbroker.orb.SkeletonDelegateImpl.
			 */
			client._release();
			client = null;
		}
	}


	/**
	 * @deprecated
	 * Getter for {@link #accessIdentity} property.
	 * @see #accessIdentity
	 */
	public com.syrus.AMFICOM.CORBA.Admin.AccessIdentity_Transferable getAccessIdentity() {
		return this.accessIdentity;
	}

	/**
	 * Getter for {@link #accessIdentifier} property.
	 * @see #accessIdentifier
	 */
	public AccessIdentity_Transferable getAccessIdentifier() {
		return this.accessIdentifier;
	}

	/**
	 * Getter for {@link #domainId} property. To be renamed.
	 * @see #domainId
	 */
	public Identifier getDomainIdentifier() {
		return this.domainId;
	}

	/**
	 * Getter for {@link #userId} property. To be renamed.
	 * @see #userId
	 */
	public Identifier getUserIdentifier() {
		return this.userId;
	}

	/**
	 * Есть ли открытая сессия.
	 */
	public boolean isOpened() {
		return (session_state == SESSION_OPENED);
	}

	public void setUser(String ISMuser) {
		this.ISMuser = ISMuser;
	}

	public String getUser() {
		return ISMuser;
	}

	public void setPassword(String ISMpassword) {
		this.ISMpassword = ISMpassword;
	}

	public String getPassword() {
		return ISMpassword;
	}

	public void setLogonTime(long LogonTime) {
		this.LogonTime = LogonTime;
	}

	public long getLogonTime() {
		return LogonTime;
	}

	public ConnectionInterface getConnectionInterface() {
		return null;
	}

	/**
	 * @deprecated
	 */
	public String getUserId() {
		return accessIdentity.user_id;
	}

	/**
	 * @deprecated
	 */
	public void setDomainId(String domain_id) {
		accessIdentity.domain_id = domain_id;
	}

	/**
	 * @deprecated
	 */
	public String getDomainId() {
		return (this.accessIdentity == null) ? null : this.accessIdentity.domain_id;
	}
}
