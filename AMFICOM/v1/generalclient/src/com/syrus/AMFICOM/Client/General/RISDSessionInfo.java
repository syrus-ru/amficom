/*
 * $Id: RISDSessionInfo.java,v 1.11 2004/10/20 11:54:19 bass Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */

package com.syrus.AMFICOM.Client.General;

import com.syrus.AMFICOM.CORBA.*;
import com.syrus.AMFICOM.CORBA.Admin.*;
import com.syrus.AMFICOM.cmserver.corba.CMServer;
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
 * @author $Author: bass $
 * @version $Revision: 1.11 $, $Date: 2004/10/20 11:54:19 $
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
		if (ci instanceof RISDConnectionInfo)
			this.ci = (RISDConnectionInfo) ci;
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

			final Class clazz = ClientLRUMap.class;
			final int size = 200;

			final ClientMeasurementObjectLoader clientMeasurementObjectLoader = new ClientMeasurementObjectLoader(cmServer);
			clientMeasurementObjectLoader.setAccessIdentifierTransferable(this.accessIdentifier);
			MeasurementStorableObjectPool.init(clientMeasurementObjectLoader, clazz, size);

			final ClientConfigurationObjectLoader clientConfigurationObjectLoader = new ClientConfigurationObjectLoader(cmServer);
			clientConfigurationObjectLoader.setAccessIdentifierTransferable(this.accessIdentifier);
			ConfigurationStorableObjectPool.init(clientConfigurationObjectLoader, clazz, size);

			IdentifierPool.init(cmServer);


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

	public AccessIdentity_Transferable getAccessIdentity() {
		return this.accessIdentity;
	}

	public AccessIdentifier_Transferable getAccessIdentifier() {
		return this.accessIdentifier;
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
		return ci;
	}

	public String getUserId() {
		return accessIdentity.user_id;
	}

	public void setDomainId(String domain_id) {
		accessIdentity.domain_id = domain_id;
	}

	public String getDomainId() {
		return (this.accessIdentity == null) ? null : this.accessIdentity.domain_id;
	}
}
