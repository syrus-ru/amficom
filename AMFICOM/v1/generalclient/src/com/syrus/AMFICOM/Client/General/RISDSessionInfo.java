/*
 * $Id: RISDSessionInfo.java,v 1.24 2005/02/16 13:38:54 stas Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */

package com.syrus.AMFICOM.Client.General;

import java.io.File;
import java.util.Collection;

import org.omg.CORBA.ORB;
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
import com.syrus.AMFICOM.CORBA.Admin.AccessIdentity_Transferable;
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
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.ParameterTypeCodenames;
import com.syrus.AMFICOM.general.SessionContext;
import com.syrus.AMFICOM.general.XMLGeneralObjectLoader;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.DataType;
import com.syrus.AMFICOM.general.corba.AccessIdentifier_Transferable;
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
import com.syrus.io.Rewriter;
import com.syrus.util.ClientLRUMap;
import com.syrus.util.corba.JavaSoftORBUtil;
import com.syrus.util.prefs.IIOPConnectionManager;

/**
 * @author $Author: stas $
 * @version $Revision: 1.24 $, $Date: 2005/02/16 13:38:54 $
 * @module generalclient_v1
 */
public final class RISDSessionInfo extends SessionInterface {
	/**
	 * ������������ �������.
	 */
	public String ISMuser;

	/**
	 * ������ ������������.
	 */
	public String ISMpassword;

	/**
	 * ���������� ������.
	 */
	public RISDConnectionInfo ci;

	/**
	 * Old-style session id.
	 *
	 * @deprecated Use {@link #getAccessIdentity()} instead.
	 */
	public AccessIdentity_Transferable accessIdentity;

	/**
	 * New-style session id.
	 */
	private AccessIdentifier_Transferable accessIdentifier;

	private Identifier domainId;

	private Identifier userId;

	/**
	 * ����� ������ ������.
	 */
	public long LogonTime;

	/**
	 * ��������� ������.
	 */
	public int session_state = SESSION_CLOSED;

	/**
	 * The client-side CORBA object.
	 */
	private Client client = null;
	
	/**
	 * ����������� - ����� ������ ��� ����������.
	 */
	public RISDSessionInfo(ConnectionInterface ci) {
	}

	/**
	 * ������� ����� ������ � ����� ��������� ���������� ������.
	 */
	public static SessionInterface OpenSession(ConnectionInterface ci, String u, String p) {
		try {
			/*
			 * ������� ����� ��������� ������ � ������� ��
			 */
			RISDSessionInfo si = new RISDSessionInfo(ci);
			si.ISMuser = new String(u);
			si.ISMpassword = new String(p);
			/*
			 * ���� ������ �� �������, �� ������������ null, � ��������� �����
			 * ��������� ������������� ������������
			 */
			return si.OpenSession();
		} catch (Exception e) {
			e.printStackTrace();
			setActiveSession(null);
			return null;
		}
	}

	/**
	 * ������� ������ � �������������� ��� ��� �����������.
	 */
	public SessionInterface OpenSession() {
		if(Environment.getConnectionType().equals(Environment.CONNECTION_EMPTY))
			return OpenLocalSession();
		return OpenRemoteSession();
	}
	
	private SessionInterface OpenRemoteSession() {
		try {
			/*
			 * �������� ��� �������� �������������� ������
			 */
			AccessIdentity_TransferableHolder accessIdentityHolder = new AccessIdentity_TransferableHolder();
			int ecode = Constants.ERROR_NO_CONNECT;

			if (ci == null)
				ci = (RISDConnectionInfo) (RISDConnectionInfo.getInstance());
			if (!ci.isConnected()) {
				try {
					ci.setConnected(true);
				} catch (Exception e) {
					/**
					 * @todo Catch different exceptions separately.
					 */
					return null;
				}
			}

			clientStartup();
			String ior = JavaSoftORBUtil.getInstance().getORB().object_to_string(client);

			/*
			 * ������� ������
			 */
			try {
				ecode = ci.getServer().Logon(getUser(), Rewriter.write(getPassword()), ior, accessIdentityHolder);
			} catch (com.syrus.AMFICOM.CORBA.General.AMFICOMRemoteException e) {
				System.err.println("Error " + e.message);
				e.printStackTrace();
				/*
				 * Another unsuccessful return point after client activation.
				 */
				clientShutdown(true);
				return null;
			} catch (Exception e) {
				System.err.println("Error " + e.getMessage());
				e.printStackTrace();
				/*
				 * First unsuccessful return point after client activation.
				 */
				clientShutdown(true);
				return null;
			}
			if (ecode != Constants.ERROR_NO_ERROR) {
				Log("Failed Logon! status = " + ecode);
				/*
				 * Second unsuccessful return point after client activation.
				 */
				clientShutdown(true);
				return null;
			}

			this.LogonTime = System.currentTimeMillis();
			this.session_state = SESSION_OPENED;
			this.accessIdentity = accessIdentityHolder.value;

			AMFICOM server = this.ci.getServer();
			CMServer cmServer = this.ci.getCmServer();

			final String oldUserId = this.accessIdentity.user_id;
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

			this.accessIdentifier = new AccessIdentifier_Transferable(
				System.currentTimeMillis(),

				cmServer.reverseLookupDomainName(
				server.lookupDomainName(
				new Identifier_Transferable(
				this.accessIdentity.domain_id))),

				userId,

				new Identifier_Transferable("Null_0"));

			this.domainId = new Identifier(this.accessIdentifier.domain_id);
			this.userId = new Identifier(this.accessIdentifier.user_id);

			final Class clazz = ClientLRUMap.class;
			final int size = 200;
			SessionContext.init(new AccessIdentity(this.accessIdentifier));

//			ClientConfigurationObjectLoader.setAccessIdentifierTransferable(this.accessIdentifier);
			ConfigurationStorableObjectPool.init(new ClientConfigurationObjectLoader(cmServer), clazz, size);

//			ClientMeasurementObjectLoader.setAccessIdentifierTransferable(this.accessIdentifier);
			MeasurementStorableObjectPool.init(new ClientMeasurementObjectLoader(cmServer), clazz, size);

//			ClientAdministrationObjectLoader.setAccessIdentifierTransferable(this.accessIdentifier);
			AdministrationStorableObjectPool.init(new ClientAdministrationObjectLoader(cmServer), clazz, size);

//			ClientGeneralObjectLoader.setAccessIdentifierTransferable(this.accessIdentifier);
			GeneralStorableObjectPool.init(new ClientGeneralObjectLoader(cmServer), clazz, size);

			MapStorableObjectPool.init(new EmptyClientMapObjectLoader(), clazz, size);

			MapViewStorableObjectPool.init(new EmptyClientMapViewObjectLoader(), clazz, size);

			ResourceStorableObjectPool.init(new EmptyClientResourceObjectLoader(), clazz, size);

			IdentifierPool.init(cmServer);
//			IdentifierPool.init(new LocalIdentifierGeneratorServer());

			System.err.println("domainId: " + this.accessIdentifier.domain_id.identifier_string);
			System.err.println("sessionId: " + this.accessIdentifier.session_id.identifier_string);
			System.err.println("started: " + new java.util.Date(this.accessIdentifier.started));
			System.err.println("userId: " + this.accessIdentifier.user_id.identifier_string);

			add(this);
			setActiveSession(this);

			/**
			 * @todo Later, we'll send client's loglevel to the server.
			 */

			return this;
		} catch (AMFICOMRemoteException e) {
			System.out.println(e.message);
			e.printStackTrace();
			setActiveSession(null);
			/*
			 * Another unsuccessful return point after client activation.
			 */
			try {
				clientShutdown(true);
			} catch (UserException ue) {
				ue.printStackTrace();
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			setActiveSession(null);
			/*
			 * Third unsuccessful return point after client activation.
			 */
			try {
				clientShutdown(true);
			} catch (UserException ue) {
				ue.printStackTrace();
			}
			return null;
		}
	}
	
	
	private SessionInterface OpenLocalSession() {
		
			final Class clazz = ClientLRUMap.class;
			final int size = 200;

			SessionContext.init(new AccessIdentity(this.accessIdentifier));

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

			MapViewStorableObjectPool.init(new EmptyClientMapViewObjectLoader(), clazz, size);

			ResourceStorableObjectPool.init(new EmptyClientResourceObjectLoader(), clazz, size);

			IdentifierPool.init(new LocalIdentifierGeneratorServer());

			try {
				EquivalentCondition condition = new EquivalentCondition(ObjectEntities.USER_ENTITY_CODE);
				Collection users = AdministrationStorableObjectPool.getStorableObjectsByCondition(condition, true);
				User user;
				if (users.isEmpty()) {
					user = User.createInstance(new Identifier("User_0"), "sys", UserSort.USER_SORT_REGULAR, "sysuser", "");	
					AdministrationStorableObjectPool.putStorableObject(user);
					AdministrationStorableObjectPool.flush(true);

				}
				else {
					user = (User)users.iterator().next();
				}
				condition = new EquivalentCondition(ObjectEntities.DOMAIN_ENTITY_CODE);
				Collection domains = AdministrationStorableObjectPool.getStorableObjectsByCondition(condition, true);
				Domain domain;
				if (domains.isEmpty()) {
					domain = Domain.createInstance(user.getId(), new Identifier("Domain_0"), "LocalDomain", "");	
					AdministrationStorableObjectPool.putStorableObject(domain);
					AdministrationStorableObjectPool.flush(true);
				}
				else {
					domain = (Domain)domains.iterator().next();
				}
				
				condition = new EquivalentCondition(ObjectEntities.PARAMETERTYPE_ENTITY_CODE);
				Collection paramaterTypes = GeneralStorableObjectPool.getStorableObjectsByCondition(condition, true);
				if (paramaterTypes.isEmpty()) {
					 // creating some default ParameterTypes 
					Identifier creatorId = user.getId();
					String codename = ParameterTypeCodenames.DADARA_EVENTS;
					String name = "Dadara events";
					String description = "List of analysis events";
					DataType dataType = DataType.DATA_TYPE_RAW;
					ParameterType type = ParameterType.createInstance(creatorId, codename, name, description, dataType);
					GeneralStorableObjectPool.putStorableObject(type);
					
					codename = ParameterTypeCodenames.DADARA_THRESHOLDS;
					name = "Dadara thresholds";
					description = "List of thresholds";
					dataType = DataType.DATA_TYPE_RAW;
					type = ParameterType.createInstance(creatorId, codename, name, description, dataType);
					GeneralStorableObjectPool.putStorableObject(type);
					
					codename = ParameterTypeCodenames.MAX_NOISE_LEVEL;
					name = "Maximal noise level";
					description = "Maximal noise level";
					dataType = DataType.DATA_TYPE_DOUBLE;
					type = ParameterType.createInstance(creatorId, codename, name, description, dataType);
					GeneralStorableObjectPool.putStorableObject(type);
					
					codename = ParameterTypeCodenames.MIN_CONNECTOR;
					name = "Minimal connector level";
					description = "Minimal connector level";
					dataType = DataType.DATA_TYPE_DOUBLE;
					type = ParameterType.createInstance(creatorId, codename, name, description, dataType);
					GeneralStorableObjectPool.putStorableObject(type);
					
					codename = ParameterTypeCodenames.MIN_SPLICE;
					name = "Minimal splice level";
					description = "Minimal splice level";
					dataType = DataType.DATA_TYPE_DOUBLE;
					type = ParameterType.createInstance(creatorId, codename, name, description, dataType);
					GeneralStorableObjectPool.putStorableObject(type);
					
					codename = ParameterTypeCodenames.MIN_END_LEVEL;
					name = "Minimal trace end level";
					description = "Minimal trace end level";
					dataType = DataType.DATA_TYPE_DOUBLE;
					type = ParameterType.createInstance(creatorId, codename, name, description, dataType);
					GeneralStorableObjectPool.putStorableObject(type);
					
					codename = ParameterTypeCodenames.MIN_EVENT_LEVEL;
					name = "Minimal event level";
					description = "Minimal event level";
					dataType = DataType.DATA_TYPE_DOUBLE;
					type = ParameterType.createInstance(creatorId, codename, name, description, dataType);
					GeneralStorableObjectPool.putStorableObject(type);
					GeneralStorableObjectPool.flush(true);
				}

				this.domainId = domain.getId();
				this.userId = user.getId();
				
			this.LogonTime = System.currentTimeMillis();
			this.session_state = SESSION_OPENED;
			this.accessIdentity = new AccessIdentity_Transferable(
					this.LogonTime,
					user.getName(),
					this.userId.getIdentifierString(),
					"Null_0",
					this.domainId.getIdentifierString());

			this.accessIdentifier = new AccessIdentifier_Transferable(
					this.LogonTime,
					(Identifier_Transferable)this.domainId.getTransferable(),
					(Identifier_Transferable)this.userId.getTransferable(),
					new Identifier_Transferable("Null_0"));


			System.err.println("domainId: " + this.accessIdentifier.domain_id.identifier_string);
			System.err.println("sessionId: " + this.accessIdentifier.session_id.identifier_string);
			System.err.println("started: " + new java.util.Date(this.accessIdentifier.started));
			System.err.println("userId: " + this.accessIdentifier.user_id.identifier_string);
			
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
	 * ������� ������.
	 */
	public void CloseSession() {
		/*
		 * ���� ������ �������, �� �������
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
		 * ������� �� ������ �������� ������
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
	 * Getter for {@link #accessIdentity} property.
	 * @see #accessIdentity
	 */
	public AccessIdentity_Transferable getAccessIdentity() {
		return this.accessIdentity;
	}

	/**
	 * Getter for {@link #accessIdentifier} property.
	 * @see #accessIdentifier
	 */
	public AccessIdentifier_Transferable getAccessIdentifier() {
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
	 * ���� �� �������� ������.
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
