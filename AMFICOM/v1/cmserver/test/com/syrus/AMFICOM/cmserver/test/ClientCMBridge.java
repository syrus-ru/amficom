/*
 * $Id: ClientCMBridge.java,v 1.7 2005/03/04 12:08:27 bass Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */

package com.syrus.AMFICOM.cmserver.test;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.omg.CORBA.ORB;
import org.omg.CORBA.UserException;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.User;
import com.syrus.AMFICOM.cmserver.corba.CMServer;
import com.syrus.AMFICOM.cmserver.corba.CMServerHelper;
import com.syrus.AMFICOM.general.corba.AccessIdentifier_Transferable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.util.corba.JavaSoftORBUtil;

/**
 * @version $Revision: 1.7 $, $Date: 2005/03/04 12:08:27 $
 * @author $Author: bass $
 * @module module
 */
public class ClientCMBridge {

	private static CMServer				server;

	private static AccessIdentifier_Transferable	accessIdentifier;

	private ClientCMBridge() {
		// empty
	}
	
	public static void main(String[] args) {
		init("desperado.science.syrus.ru");
	}

	public static void init(final String hostName) {
		server = resolveCMServer(hostName);

		//MeasurementStorableObjectPool.init(new ClientMeasurementObjectLoader(server), ClientLRUMap.class, 200);
		//ConfigurationStorableObjectPool.init(new ClientConfigurationObjectLoader(server), ClientLRUMap.class,	200);
		// IdentifierPool.init(server);
		

		accessIdentifier = new AccessIdentifier_Transferable();
		try {
			server.transmitDomain(new Identifier_Transferable("Domain_19"), accessIdentifier);
		} catch (AMFICOMRemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void setAccessIdentifier(AccessIdentifier_Transferable accessIdentifier){
		//ClientMeasurementObjectLoader.setAccessIdentifierTransferable(accessIdentifier);
		//ClientConfigurationObjectLoader.setAccessIdentifierTransferable(accessIdentifier);
	}
	
	public static void setDomain(Domain domain) {
		accessIdentifier.domain_id = (Identifier_Transferable) domain.getId().getTransferable();
		setAccessIdentifier(accessIdentifier);
	}

	public static void setUser(User user) {
		accessIdentifier.user_id = (Identifier_Transferable) user.getId().getTransferable();
		setAccessIdentifier(accessIdentifier);
	}

	public static void setSessionId(Identifier sessionId) {
		accessIdentifier.session_id = (Identifier_Transferable) sessionId.getTransferable();
		setAccessIdentifier(accessIdentifier);
	}

	private static CMServer resolveCMServer(final String hostName) {
		CMServer cmserver = null;
		try {
			ORB orb = JavaSoftORBUtil.getInstance().getORB();

			POA rootPOA = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
			rootPOA.the_POAManager().activate();
			NamingContextExt rootNamingCtx = NamingContextExtHelper.narrow(orb
					.resolve_initial_references("NameService"));

			String hName = hostName;			
			if (hName == null)
				hName = InetAddress.getByName(hostName).getCanonicalHostName().replaceAll("\\.", "_");

			final String finalHostName = hName;

			cmserver = CMServerHelper.narrow(rootNamingCtx.resolve_str(finalHostName + "/" + "CMServer"));
		} catch (UserException ue) {
			ue.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		return cmserver;
	}

}
