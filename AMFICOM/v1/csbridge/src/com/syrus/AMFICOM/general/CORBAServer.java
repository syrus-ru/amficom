/*
 * $Id: CORBAServer.java,v 1.2 2005/04/27 15:30:20 arseniy Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */

package com.syrus.AMFICOM.general;

import java.util.Properties;

import org.omg.CORBA.ORB;
import org.omg.CORBA.Policy;
import org.omg.CORBA.UserException;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.AlreadyBound;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.omg.PortableServer.IdAssignmentPolicyValue;
import org.omg.PortableServer.IdUniquenessPolicyValue;
import org.omg.PortableServer.ImplicitActivationPolicyValue;
import org.omg.PortableServer.LifespanPolicyValue;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import org.omg.PortableServer.RequestProcessingPolicyValue;
import org.omg.PortableServer.Servant;
import org.omg.PortableServer.ServantRetentionPolicyValue;
import org.omg.PortableServer.ThreadPolicyValue;

import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.2 $, $Date: 2005/04/27 15:30:20 $
 * @author $Author: arseniy $
 * @module csbridge_v1
 */

public class CORBAServer /*extends Thread */{
	public static final String DEFAULT_ORB_INITIAL_HOST = "127.0.0.1";
	public static final int DEFAULT_ORB_INITIAL_PORT = 1050;

	ORB orb;
	private POA poa;
	private NamingContextExt namingContext;

	public CORBAServer(String rootContextName) throws CommunicationException {
		this.initORB();

		try {
			this.initPOA();
		}
		catch (UserException ue) {
			throw new CommunicationException("Cannot activate POA", ue);
		}

		this.initNamingContext(rootContextName);

		this.runORB();
	}

/*
	public void activateServant(org.omg.CORBA.Object reference, String name) throws CommunicationException {
		try {
			Servant servant = this.poa.reference_to_servant(reference);
			this.poa.activate_object(servant);

			NameComponent[] nameComponents = this.namingContext.to_name(name);
			this.namingContext.rebind(nameComponents, reference);
		}
		catch (UserException ue) {
			throw new CommunicationException("Cannot activate servant", ue);
		}
	}
*/
	public void activateServant(Servant servant, String name) throws CommunicationException {
		try {
			this.poa.activate_object(servant);

			org.omg.CORBA.Object reference = this.poa.servant_to_reference(servant);
			NameComponent[] nameComponents = this.namingContext.to_name(name);
			this.namingContext.rebind(nameComponents, reference);
			Log.debugMessage("Activated servant '" + name + "'", Log.DEBUGLEVEL05);
		}
		catch (UserException ue) {
			throw new CommunicationException("Cannot activate servant '" + name + "'", ue);
		}
	}

	public void deactivateServant(String name) throws CommunicationException {
		try {
			NameComponent[] nameComponents = this.namingContext.to_name(name);
			this.namingContext.unbind(nameComponents);
			Log.debugMessage("Deactivated servant '" + name + "'", Log.DEBUGLEVEL05);
		}
		catch (NotFound nf) {
			Log.errorMessage("Cannot deactivate servant '" + name + "' -- no such servant");
		}
		catch (UserException ue) {
			throw new CommunicationException("Cannot deactivate servant '" + name + "'", ue);
		}
	}

	public org.omg.CORBA.Object resolveReference(String name) throws CommunicationException {
		try {
			Log.debugMessage("Resolving name: " + name, Log.DEBUGLEVEL08);
			org.omg.CORBA.Object ref = this.namingContext.resolve_str(name);
			Log.debugMessage("Resolved reference: " + this.orb.object_to_string(ref), Log.DEBUGLEVEL10);
			return ref;
		}
		catch (UserException nf) {
			throw new CommunicationException("Name '" + name + "' not found", nf);
		}
	}

	public ORB getORB() {
		return this.orb;
	}

	private void initORB() {
		Properties properties = System.getProperties();
		String host = ApplicationProperties.getString("ORBInitialHost", DEFAULT_ORB_INITIAL_HOST);
		int port = ApplicationProperties.getInt("ORBInitialPort", DEFAULT_ORB_INITIAL_PORT);
		Log.debugMessage("CORBAServer.initORB | host: " + host + ", port: " + port, Log.DEBUGLEVEL09);
		properties.setProperty("org.omg.CORBA.ORBInitialHost", host);
		properties.setProperty("org.omg.CORBA.ORBInitialPort", Integer.toString(port));
		String[] args = null;
		this.orb = ORB.init(args, properties);
	}

	private void initPOA() throws UserException {
		POA rootPoa;
		try {
			rootPoa = POAHelper.narrow(this.orb.resolve_initial_references("RootPOA"));
		}
		catch (InvalidName in) {
			Log.errorException(in);
			return;
		}
		Policy[] policies = new Policy[7];
		policies[0] = rootPoa.create_thread_policy(ThreadPolicyValue.ORB_CTRL_MODEL);
		policies[1] = rootPoa.create_lifespan_policy(LifespanPolicyValue.TRANSIENT);
		policies[2] = rootPoa.create_id_uniqueness_policy(IdUniquenessPolicyValue.UNIQUE_ID);
		policies[3] = rootPoa.create_id_assignment_policy(IdAssignmentPolicyValue.SYSTEM_ID);
		policies[4] = rootPoa.create_servant_retention_policy(ServantRetentionPolicyValue.RETAIN);
		policies[5] = rootPoa.create_request_processing_policy(RequestProcessingPolicyValue.USE_ACTIVE_OBJECT_MAP_ONLY);
		policies[6] = rootPoa.create_implicit_activation_policy(ImplicitActivationPolicyValue.IMPLICIT_ACTIVATION);

		this.poa = rootPoa.create_POA("poa1", rootPoa.the_POAManager(), policies);

		this.poa.the_POAManager().activate();
	}

	private void initNamingContext(String rootContextNameStr) throws CommunicationException {
		this.bindIfNonExistingNamingContext(rootContextNameStr);
	}

	private void bindIfNonExistingNamingContext(String rootContextNameStr) throws CommunicationException {
		try {
			NamingContextExt rootNamingContext = NamingContextExtHelper.narrow(this.orb.resolve_initial_references("NameService"));

			NameComponent[] contextName = rootNamingContext.to_name(rootContextNameStr);
			try {
				Log.debugMessage("Creating naming context: '" + rootContextNameStr + "'", Log.DEBUGLEVEL08);
				this.namingContext = NamingContextExtHelper.narrow(rootNamingContext.bind_new_context(contextName));
			}
			catch (AlreadyBound ab) {
				Log.debugMessage("Naming context: '" + rootContextNameStr + "' already bound; trying to resolve", Log.DEBUGLEVEL08);
				this.namingContext = NamingContextExtHelper.narrow(rootNamingContext.resolve_str(rootContextNameStr));
			}
		}
		catch (UserException ue) {
			throw new CommunicationException("Cannot create context", ue);
		}
	}

	private void runORB() {
		Thread thread = new Thread() {
			public void run() {
				CORBAServer.this.orb.run();
			}
		};
		thread.setDaemon(true);
		thread.setName(this.orb.getClass().getName());
		thread.start();
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				CORBAServer.this.shutdown();
			}
		});
	}

	void shutdown() {
		try {
			this.poa.the_POAManager().deactivate(false, false);
		}
		catch (UserException ue) {
			Log.errorException(ue);
		}
		this.poa.destroy(false, false);
		this.orb.shutdown(true);
	}
}
