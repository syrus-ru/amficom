package com.syrus.util.corba;

import org.omg.CORBA.ORB;
import org.omg.CORBA.Policy;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import org.omg.PortableServer.Servant;
import org.omg.PortableServer.POAPackage.AdapterAlreadyExists;
import org.omg.PortableServer.POAPackage.InvalidPolicy;

import org.omg.PortableServer.ThreadPolicy;
import org.omg.PortableServer.ThreadPolicyValue;
import org.omg.PortableServer.LifespanPolicy;
import org.omg.PortableServer.LifespanPolicyValue;
import org.omg.PortableServer.IdUniquenessPolicy;
import org.omg.PortableServer.IdUniquenessPolicyValue;
import org.omg.PortableServer.IdAssignmentPolicy;
import org.omg.PortableServer.IdAssignmentPolicyValue;
import org.omg.PortableServer.ServantRetentionPolicy;
import org.omg.PortableServer.ServantRetentionPolicyValue;
import org.omg.PortableServer.RequestProcessingPolicy;
import org.omg.PortableServer.RequestProcessingPolicyValue;
import org.omg.PortableServer.ImplicitActivationPolicy;
import org.omg.PortableServer.ImplicitActivationPolicyValue;

import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NameComponent;
import java.util.Properties;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;

public class CORBAServer /*extends Thread */{
	public static final String DEFAULT_ORB_INITIAL_HOST = "127.0.0.1";
	public static final int DEFAULT_ORB_INITIAL_PORT = 1050;

	private ORB orb;
	private POA poa;
	private NamingContextExt namingContext;

	public CORBAServer() throws Exception {
		Properties properties = System.getProperties();
		String host = ApplicationProperties.getString("ORBInitialHost", DEFAULT_ORB_INITIAL_HOST);
		int port = ApplicationProperties.getInt("ORBInitialPort", DEFAULT_ORB_INITIAL_PORT);
		System.out.println("host: " + host + ", port: " + Integer.parseInt(Integer.toString(port)));
		properties.setProperty("org.omg.CORBA.ORBInitialHost", host);
		properties.setProperty("org.omg.CORBA.ORBInitialPort", Integer.toString(port));
		String[] args = null;
		this.orb = ORB.init(args, properties);

		this.initPOA();
		this.initNamingContext();

		this.poa.the_POAManager().activate();
	}

	public void activateServant(org.omg.CORBA.Object reference, String name) {
		Servant servant = null;
		try {
			servant = this.poa.reference_to_servant(reference);
		}
		catch (org.omg.PortableServer.POAPackage.ObjectNotActive ona) {
			Log.errorException(ona);
			return;
		}
		catch (org.omg.PortableServer.POAPackage.WrongPolicy wp) {
			Log.errorException(wp);
			return;
		}
		catch (org.omg.PortableServer.POAPackage.WrongAdapter wa) {
			Log.errorException(wa);
			return;
		}
		
		this.activateServant(servant, name);
	}
	
	public void activateServant(Servant servant, String name) {
		NameComponent[] nameComponents = null;
		try {
			nameComponents = this.namingContext.to_name(name);
		}
		catch (org.omg.CosNaming.NamingContextPackage.InvalidName in) {
			Log.errorException(in);
			return;
		}

		try {
			this.poa.activate_object(servant);
		}
		catch (org.omg.PortableServer.POAPackage.ServantAlreadyActive saa) {
			Log.errorException(saa);
			return;
		}
		catch (org.omg.PortableServer.POAPackage.WrongPolicy wp) {
			Log.errorException(wp);
			return;
		}
		
		org.omg.CORBA.Object reference = null;
		try {
			reference = this.poa.servant_to_reference(servant);
		}
		catch (org.omg.PortableServer.POAPackage.ServantNotActive sna) {
			Log.errorException(sna);
			return;
		}
		catch (org.omg.PortableServer.POAPackage.WrongPolicy wp) {
			Log.errorException(wp);
			return;
		}
		

		try {
			this.namingContext.rebind(nameComponents, reference);
		}
		catch (org.omg.CosNaming.NamingContextPackage.InvalidName in) {
			Log.errorException(in);
			return;
		}
		catch (org.omg.CosNaming.NamingContextPackage.NotFound nf) {
			Log.errorException(nf);
			return;
		}
		catch (org.omg.CosNaming.NamingContextPackage.CannotProceed cp) {
			Log.errorException(cp);
			return;
		}
	}

	public org.omg.CORBA.Object resolveReference(String name) throws Exception {
		try {
			return this.namingContext.resolve_str(name);
		}
		catch (org.omg.CosNaming.NamingContextPackage.NotFound nf) {
			throw new Exception("Name '" + name + "' not found; " + nf.why);
		}
	}

	public ORB getORB() {
		return this.orb;
	}

	public void run() {
		this.orb.run();
	}

	public void shutdown() throws Exception {
		this.poa.the_POAManager().deactivate(false, false);
		this.poa.destroy(false, false);
		this.orb.shutdown(true);
	}

	private void initPOA() {
		Policy[] policies = new Policy[7];
		POA rootPoa;
		try {
			rootPoa = POAHelper.narrow(this.orb.resolve_initial_references("RootPOA"));
		}
		catch (org.omg.CORBA.ORBPackage.InvalidName in) {
			Log.errorException(in);
			return;
		}
		policies[0] = rootPoa.create_thread_policy(ThreadPolicyValue.ORB_CTRL_MODEL);
		policies[1] = rootPoa.create_lifespan_policy(LifespanPolicyValue.PERSISTENT);
		policies[2] = rootPoa.create_id_uniqueness_policy(IdUniquenessPolicyValue.UNIQUE_ID);
		policies[3] = rootPoa.create_id_assignment_policy(IdAssignmentPolicyValue.SYSTEM_ID);
		policies[4] = rootPoa.create_servant_retention_policy(ServantRetentionPolicyValue.RETAIN);
		policies[5] = rootPoa.create_request_processing_policy(RequestProcessingPolicyValue.USE_ACTIVE_OBJECT_MAP_ONLY);
		policies[6] = rootPoa.create_implicit_activation_policy(ImplicitActivationPolicyValue.IMPLICIT_ACTIVATION);
		try {
			this.poa = rootPoa.create_POA("poa1", rootPoa.the_POAManager(), policies);
		}
		catch (AdapterAlreadyExists aae) {
			Log.errorException(aae);
		}
		catch (InvalidPolicy ip) {
			Log.errorException(ip);
		}
	}

	private void initNamingContext() throws Exception {
		try {
			this.namingContext = NamingContextExtHelper.narrow(this.orb.resolve_initial_references("NameService"));
		}
		catch (org.omg.CORBA.ORBPackage.InvalidName in) {
			throw new Exception("Cannot resolve NameService -- invalid name: " + in.getMessage());
		}
	}
}
