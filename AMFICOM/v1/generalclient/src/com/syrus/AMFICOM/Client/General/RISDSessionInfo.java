//////////////////////////////////////////////////////////////////////////////
// *                                                                      * //
// * Syrus Systems                                                        * //
// * ����������� ��������� ������������ � ����������                      * //
// *                                                                      * //
// * ������: ������� - ������� ������������������� ��������������������   * //
// *         ����������������� �������� � ���������� �����������          * //
// *                                                                      * //
// *         ���������� ��������������� ������� �����������               * //
// *                                                                      * //
// * ��������: ����� �������� ���������� � ������ ������ ������������     * //
// *           � ��������                                                 * //
// *                                                                      * //
// * ���: Java 1.2.2                                                      * //
// *                                                                      * //
// * �����: ����������� �.�.                                              * //
// *                                                                      * //
// * ������: 0.1                                                          * //
// * ��: 22 jan 2002                                                      * //
// * ������������: ISM\prog\java\AMFICOMConfigure\com\syrus\AMFICOM\      * //
// *        Client\General\SessionInfo.java                               * //
// *                                                                      * //
// * ����� ����������: Oracle JDeveloper 3.2.2 (Build 915)                * //
// *                                                                      * //
// * ����������: Oracle javac (Java 2 SDK, Standard Edition, ver 1.2.2)   * //
// *                                                                      * //
// * ������: ����������                                                   * //
// *                                                                      * //
// * ���������:                                                           * //
// *  ���         ����   �����      �����������                           * //
// * -----------  ----- ---------- -------------------------------------- * //
// *                                                                      * //
// * ��������:                                                            * //
// *                                                                      * //
//////////////////////////////////////////////////////////////////////////////

package com.syrus.AMFICOM.Client.General;

import com.syrus.AMFICOM.CORBA.Constants;
import com.syrus.AMFICOM.CORBA.Admin.*;
import com.syrus.AMFICOM.corba.portable.client.*;
import com.syrus.io.*;
import com.syrus.util.corba.JavaSoftORBUtil;
import com.syrus.util.prefs.IIOPConnectionManager;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;
import org.omg.CORBA.*;
import org.omg.CosNaming.*;
import org.omg.CosNaming.NamingContextPackage.*;
import org.omg.PortableServer.*;
import org.omg.PortableServer.POAManagerPackage.AdapterInactive;

/**
 * @version $Revision: 1.1 $, $Date: 2004/05/27 11:24:21 $
 * @author $Author: bass $
 */
public class RISDSessionInfo extends SessionInterface {
	/**
	 * ����������������� ����.
	 */
	private static IniFile iniFile;

	/**
	 * ��� ������������������ �����.
	 */
	private static final String iniFileName = "Session.properties";

	/*
	 * ���������� ��� ����������� ������
	 */
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
	 * ���������� ������������� ������.
	 */
	public AccessIdentity_Transferable accessIdentity;

	/**
	 * ����� ������ ������.
	 */
	public long LogonTime;

	/**
	 * ��������� ������.
	 */
	public int session_state = SESSION_CLOSED;

	private Handler handler;

	/**
	 * The client-side CORBA object.
	 */
	private Client client = null;

	/**
	 * Instance initializer
	 */
	{
		handler = new ConsoleHandler();
		handler.setLevel(Level.CONFIG);

		LogRecord logRecord;
		logRecord = new LogRecord(Level.FINEST, "Method entry");
		logRecord.setSourceClassName(getClass().getName());
		logRecord.setSourceMethodName("<init>");
		handler.publish(logRecord);

		/*
		 * ���������� ��������� �������� ���������� ������
		 */
		initialize();

		logRecord = new LogRecord(Level.FINEST, "Method exit");
		logRecord.setSourceClassName(getClass().getName());
		logRecord.setSourceMethodName("<init>");
		handler.publish(logRecord);
	}

	/**
	 * ������� ����������� ��� ����������.
	 */
	public RISDSessionInfo() {
		LogRecord logRecord;
		logRecord = new LogRecord(Level.FINEST, "Method entry");
		logRecord.setSourceClassName(getClass().getName());
		logRecord.setSourceMethodName("RISDSessionInfo()");
		handler.publish(logRecord);

		logRecord = new LogRecord(Level.FINEST, "Method exit");
		logRecord.setSourceClassName(getClass().getName());
		logRecord.setSourceMethodName("RISDSessionInfo()");
		handler.publish(logRecord);
	}

	/**
	 * ����������� - ����� ������ ��� ����������.
	 */
	public RISDSessionInfo(ConnectionInterface ci) {
		LogRecord logRecord;
		logRecord = new LogRecord(Level.FINEST, "Method entry");
		logRecord.setSourceClassName(getClass().getName());
		logRecord.setSourceMethodName("RISDSessionInfo(ConnectionInterface)");
		handler.publish(logRecord);

		if (ci instanceof RISDConnectionInfo)
			this.ci = (RISDConnectionInfo) ci;

		logRecord = new LogRecord(Level.FINEST, "Method exit");
		logRecord.setSourceClassName(getClass().getName());
		logRecord.setSourceMethodName("RISDSessionInfo(ConnectionInterface)");
		handler.publish(logRecord);
	}

	/**
	 * ������������� ���������� ������.
	 * ��������� ������� �� ����� �������� ��� �� ���������.
	 */
	public void initialize() {
		/*
		 * ��������� ��������� �� ����� ��������
		 */
		try {
			iniFile = new IniFile(iniFileName);
			System.out.println("read ini file " + iniFileName);
			/*
			 * ���� ���� �������� ������, �� ������ �� ���� �������� ����������
			 */
			ISMuser = iniFile.getValue("userName");
		} catch (IOException ioe) {
			System.out.println("Error opening " + iniFileName + " - setting defaults");
			/*
			 * ���� ������ �������� ����� ��������, �� ������ �� ������
			 */
			SetDefaults();
		}
	}

	public void SetDefaults() {
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
		LogRecord logRecord;
		logRecord = new LogRecord(Level.FINEST, "Method entry");
		logRecord.setSourceClassName(getClass().getName());
		logRecord.setSourceMethodName("OpenSession()");
		handler.publish(logRecord);

		try {
			/*
			 * �������� ��� �������� �������������� ������
			 */
			AccessIdentity_TransferableHolder accessIdentityHolder = new AccessIdentity_TransferableHolder();
			int ecode = Constants.ERROR_NO_CONNECT;

			/*
			 * ���� �� ������� ���������� �� ����� ���������� �� ���������
			 */
			if (ci == null) {
				System.out.println("ci for si " + getUser() + " was null");
//				return null;
				ci = new RISDConnectionInfo();
			}

			/*
			 * ���� ���������� �� ����������� �� ���������� ����������
			 * � ��������
			 */
			if (! ci.isConnected()) {
				System.out.println("ci " + ci.getServiceURL() + ci.getObjectName() + " for si " + ISMuser + " not connected");
				if (ConnectionInterface.Connect(ci) == null)
					return null;
			}
//			} else
//				ci = ConnectionInfo.active_connection;

			clientStartup();
			String ior = JavaSoftORBUtil.getInstance().getORB().object_to_string(client);

			logRecord = new LogRecord(Level.CONFIG, "Started client object up and obtained a reference to it: " + ior);
			logRecord.setSourceClassName(getClass().getName());
			logRecord.setSourceMethodName("OpenSession()");
			handler.publish(logRecord);

			/*
			 * ������� ������
			 */
			try {
				ecode = ci.server.Logon(getUser(), Rewriter.write(getPassword()), ior, accessIdentityHolder);
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

			/*
			 * ��������� ���������� ������������� ������
			 */
			accessIdentity = accessIdentityHolder.value;

			logRecord = new LogRecord(Level.CONFIG, "accessIdentity information:" + ((accessIdentity == null) ? ("\n\taccessIdentity: null") : ("\n\tstarted: " + accessIdentity.started + "\n\tdomain_id: " + accessIdentity.domain_id + "\n\tsess_id: " + accessIdentity.sess_id + "\n\tuser_id: " + accessIdentity.user_id + "\n\tusername: " + accessIdentity.username)));
			logRecord.setSourceClassName(getClass().getName());
			logRecord.setSourceMethodName("OpenSession()");
			handler.publish(logRecord);

			/*
			 * ���������� ����� ������ ������
			 */
			LogonTime = System.currentTimeMillis();
			Log("Logged on on " + new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date(LogonTime)) + " as " + ISMuser + " (sessid = " + accessIdentity.sess_id + ")");

			/*
			 * �������� � ������ �������� ������
			 */
			add(this);
			/*
			 * ��������� ������ - �������
			 */
			session_state = SESSION_OPENED;
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
		LogRecord logRecord;
		logRecord = new LogRecord(Level.FINEST, "Method entry");
		logRecord.setSourceClassName(getClass().getName());
		logRecord.setSourceMethodName("CloseSession()");
		handler.publish(logRecord);

		/*
		 * ���� ������ �������, �� �������
		 */
		if (contains(this)) {
			try {
				System.out.println("closing session for " + accessIdentity.sess_id);
				ci.server.Logoff(accessIdentity);
		    } catch (Exception e) {
		        e.printStackTrace();
		    } 
			ci.sessions--;

			try {
				clientShutdown();
			} catch (UserException ue) {
				ue.printStackTrace();
			}

			logRecord = new LogRecord(Level.CONFIG, "Shut client object down");
			logRecord.setSourceClassName(getClass().getName());
			logRecord.setSourceMethodName("CloseSession()");
			handler.publish(logRecord);
		}
		session_state = SESSION_CLOSED;
		/*
		 * ������� �� ������ �������� ������
		 */
		remove(this);
		if (isEmpty()) {
			setActiveSession(null);

			logRecord = new LogRecord(Level.INFO, "The last session has been closed; closing connection...");
			logRecord.setSourceClassName(getClass().getName());
			logRecord.setSourceMethodName("CloseSession()");
			handler.publish(logRecord);

//			si.ci.Disconnect();
			ci.Disconnect();
		}
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
		if (abnormalSessionTermination) {
			LogRecord logRecord;
			logRecord = new LogRecord(Level.WARNING,
				"Failed to open session; shutting down client object...");
			logRecord.setSourceClassName(getClass().getName());
			logRecord.setSourceMethodName("clientShutdown(boolean)");
			handler.publish(logRecord);
		}
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
	 * ���� �� �������� ������.
	 */
	public boolean isOpened() {
		return (session_state == SESSION_OPENED);
//		return ! (sessions.isEmpty());
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
		return accessIdentity.domain_id;
	}

	public String toString() {
		return "SessionInfo for user " + getUser() + " opened " + isOpened();
	}
}
