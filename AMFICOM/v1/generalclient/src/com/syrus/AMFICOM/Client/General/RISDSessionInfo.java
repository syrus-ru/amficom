/*
 * $Id: RISDSessionInfo.java,v 1.15 2004/11/15 13:23:54 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.Client.General;

import com.syrus.AMFICOM.CORBA.*;
import com.syrus.AMFICOM.CORBA.Admin.*;
import com.syrus.AMFICOM.cmserver.corba.CMServer;
import com.syrus.AMFICOM.configuration.ClientConfigurationObjectLoader;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.corba.AccessIdentifier_Transferable;
import com.syrus.AMFICOM.corba.portable.client.*;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.*;
import com.syrus.io.Rewriter;
import com.syrus.util.ClientLRUMap;
import com.syrus.util.corba.JavaSoftORBUtil;
import com.syrus.util.prefs.IIOPConnectionManager;
import org.omg.CORBA.*;
import org.omg.CosNaming.*;
import org.omg.CosNaming.NamingContextPackage.*;
import org.omg.PortableServer.*;
import org.omg.PortableServer.POAManagerPackage.AdapterInactive;

/**
 * @author $Author: bob $
 * @version $Revision: 1.15 $, $Date: 2004/11/15 13:23:54 $
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
	public AccessIdentity_Transferable accessIdentity;

	/**
	 * New-style session id.
	 */
	private AccessIdentifier_Transferable accessIdentifier;

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
		if (ci instanceof RISDConnectionInfo)
			this.ci = (RISDConnectionInfo) ci;
	}

	/**
	 * Открыть новую сессию с явным указанием параметров сессии.
	 */
	public static SessionInterface OpenSession(ConnectionInterface ci, String u, String p) {
		try {
			/*
			 * создать новый экземпляр сессии и открыть ее
			 */
			RISDSessionInfo si = new RISDSessionInfo(ci);
			si.ISMuser = new String(u);
			si.ISMpassword = new String(p);
			/*
			 * если сессия не открыта, то возвращается null, и созданный здесь
			 * экземпляр автоматически уничтожается
			 */
			return si.OpenSession();
		} catch (Exception e) {
			e.printStackTrace();
			setActiveSession(null);
			return null;
		}
	}

	/**
	 * Открыть сессию с установленными для нее параметрами.
	 */
	public SessionInterface OpenSession() {
		try {
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
			 * открыть сессию
			 */
			try {
				ecode = ci.getServer().Logon(getUser(), Rewriter.write(getPassword()), ior, accessIdentityHolder);
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

			ClientConfigurationObjectLoader.setAccessIdentifierTransferable(this.accessIdentifier);
			ConfigurationStorableObjectPool.init(new ClientConfigurationObjectLoader(cmServer), clazz, size);

			ClientMeasurementObjectLoader.setAccessIdentifierTransferable(this.accessIdentifier);
			MeasurementStorableObjectPool.init(new ClientMeasurementObjectLoader(cmServer), clazz, size);

			IdentifierPool.init(cmServer);

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

	/**
	 * Закрыть сессию.
	 */
	public void CloseSession() {
		/*
		 * если сессия открыта, то закрыть
		 */
		if (contains(this)) {
			/**
			 * @todo Later, show a confirmation dialog.
			 */
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
		return ci;
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
