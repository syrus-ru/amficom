/*
 * $Id: CORBAServer.java,v 1.9 2005/06/01 15:59:42 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import org.omg.CORBA.ORB;
import org.omg.CORBA.Policy;
import org.omg.CORBA.UserException;
import org.omg.CosNaming.Binding;
import org.omg.CosNaming.BindingIteratorHolder;
import org.omg.CosNaming.BindingListHolder;
import org.omg.CosNaming.BindingType;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.AlreadyBound;
import org.omg.CosNaming.NamingContextPackage.InvalidName;
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
 * @version $Revision: 1.9 $, $Date: 2005/06/01 15:59:42 $
 * @author $Author: arseniy $
 * @module csbridge_v1
 */

public class CORBAServer {
	public static final String DEFAULT_ORB_INITIAL_HOST = "127.0.0.1";
	public static final int DEFAULT_ORB_INITIAL_PORT = 1050;

	ORB orb;
	private POA poa;
	private NamingContextExt namingContext;

	/*	Names of bound servants. Need for unbound all servants on shutdown*/
	private Set servantNames;	//Set <String servantName>

	/*	Wrapper class for shutdown hook*/
	private class WrappedHook {
		Thread hook;

		WrappedHook(Thread hook) {
			this.hook = hook;
		}

		public int hashCode() {
	    return System.identityHashCode(this.hook);
		}

		public boolean equals(Object object) {
			if (object instanceof WrappedHook)
				return (((WrappedHook) object).hook == this.hook);
			return false;
		}
	}

	/*	This field is set to false on shutdown just before running hooks*/
	private boolean running;

	/*	Hooks themselves*/
	private Set hooks;	//Set <WrappedHook hook>

	public CORBAServer(final String rootContextName) throws CommunicationException {
		this.initORB();

		try {
			this.initPOA();
		}
		catch (UserException ue) {
			throw new CommunicationException("Cannot activate POA", ue);
		}

		this.initNamingContext(rootContextName);

		this.runORB();

		this.running = true;
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
		catch (org.omg.CORBA.ORBPackage.InvalidName in) {
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

	private void initNamingContext(final String rootContextNameStr) throws CommunicationException {
		this.bindIfNonExistingNamingContext(rootContextNameStr);
		this.servantNames = Collections.synchronizedSet(new HashSet());
	}

	private void bindIfNonExistingNamingContext(final String rootContextNameStr) throws CommunicationException {
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

	public void activateServant(final Servant servant, final String name) throws CommunicationException {
		if (this.running) {
			try {
				this.poa.activate_object(servant);

				org.omg.CORBA.Object reference = this.poa.servant_to_reference(servant);
				NameComponent[] nameComponents = this.namingContext.to_name(name);
				this.namingContext.rebind(nameComponents, reference);
				this.servantNames.add(name);
				Log.debugMessage("Activated servant '" + name + "'", Log.DEBUGLEVEL05);
			}
			catch (UserException ue) {
				throw new CommunicationException("Cannot activate servant '" + name + "' -- " + ue.getMessage(), ue);
			}
		}
		else
			throw new IllegalStateException("Cannot resolve reference '" + name + "' -- shutting down");
	}

	public org.omg.CORBA.Object resolveReference(final String name) throws CommunicationException {
		try {
			Log.debugMessage("Resolving name: " + name, Log.DEBUGLEVEL08);
			final org.omg.CORBA.Object ref = this.namingContext.resolve_str(name);
			Log.debugMessage("Resolved reference: " + this.orb.object_to_string(ref), Log.DEBUGLEVEL10);
			return ref;
		}
		catch (UserException nf) {
			throw new CommunicationException("Name '" + name + "' not found", nf);
		}
	}

	/*	TODO Maybe return set of CORBA references instead of strings*/
	public String[] getSubContextReferences(final String subContextName) throws CommunicationException {
		org.omg.CORBA.Object subContextRef = null;
		try {
			subContextRef = this.namingContext.resolve_str(subContextName);
		}
		catch (UserException ue) {
			throw new CommunicationException("Cannot resolve subcontext '" + subContextName + "' -- " + ue.getMessage(), ue);
		}

		NamingContextExt subContext = null;
		try {
			subContext = NamingContextExtHelper.narrow(subContextRef);
		}
		catch (Exception e) {
			throw new CommunicationException("Cannot narrow reference '" + subContextName + "' to naming context -- " + e.getMessage(),
					e);
		}

		final BindingListHolder bindingListHolder = new BindingListHolder();
		final BindingIteratorHolder bindingIteratorHolder = new BindingIteratorHolder();
		subContext.list(Integer.MAX_VALUE, bindingListHolder, bindingIteratorHolder);
		final Binding[] bindings = bindingListHolder.value;
		final String[] ret = new String[bindings.length];
		for (int i = 0; i < bindings.length; i++) {
			final Binding binding = bindings[i];
			try {
				final String bindingName = subContext.to_string(binding.binding_name);
				if (binding.binding_type.value() == BindingType._nobject) {
					ret[i] = bindingName;
				}
				else
					Log.errorMessage("Binding '" + bindingName + "' not of type object");
			}
			catch (InvalidName in) {
				Log.errorException(in);
			}
		}
		return ret;
	}

	public void addShutdownHook(final Thread hook) {
		if (this.running) {
			if (!hook.isAlive()) {
				if (this.hooks == null) {
					this.hooks = new HashSet(1);
					this.hooks.add(new WrappedHook(hook));
				}
				else {
					WrappedHook wrappedHook = new WrappedHook(hook);
					if (!this.hooks.contains(wrappedHook))
						this.hooks.add(wrappedHook);
					else
						Log.errorMessage("CORBAServer | Cannot add shutdown hook -- it is already added");
				}
			}
			else
				Log.errorMessage("CORBAServer | Cannot add shutdown hook -- it is already running");
		}
		else
			Log.errorMessage("CORBAServer | Cannot add shutdown hook -- shutting down");
	}

	public boolean removeShutdownHook(final Thread hook) {
		if (this.hooks == null)
			return false;

		if (this.running) {
			if (hook != null) {
				boolean ret = this.hooks.remove(new WrappedHook(hook));
				if (ret && this.hooks.isEmpty())
					this.hooks = null;
				return ret;
			}
			Log.errorMessage("CORBAServer | Cannot remove NULL shutdown hook");
			return false;
		}
		Log.errorMessage("CORBAServer | Cannot remove shutdown hook -- shutting down");
		return false;
	}

	public ORB getOrb() {
		return this.orb;
	}

	public POA getPoa() {
		return this.poa;
	}

	void shutdown() {
		this.running = false;

		this.runHooks();

		synchronized (this.servantNames) {
			for (Iterator it = this.servantNames.iterator(); it.hasNext();) {
				try {
					this.unbindServant((String) it.next());
					it.remove();
				}
				catch (CommunicationException ce) {
					Log.errorException(ce);
				}
			}
		}

		try {
			this.poa.the_POAManager().deactivate(false, false);
		}
		catch (UserException ue) {
			Log.errorException(ue);
		}
		this.poa.destroy(false, false);
		this.orb.shutdown(true);
	}

	private void unbindServant(final String name) throws CommunicationException {
		try {
			NameComponent[] nameComponents = this.namingContext.to_name(name);
			this.namingContext.unbind(nameComponents);
			//this.servantNames.remove(name);
			Log.debugMessage("Deactivated servant '" + name + "'", Log.DEBUGLEVEL05);
		}
		catch (NotFound nf) {
			Log.errorMessage("Cannot deactivate servant '" + name + "' -- no such servant");
		}
		catch (UserException ue) {
			throw new CommunicationException("Cannot deactivate servant '" + name + "'", ue);
		}
	}

	private void runHooks() {
		if (this.hooks == null)
			return;

		/*	No synchronization on hooks.
		 *	The value false of field running guarantees,
		 *	that hooks cannot be modified*/
		for (final Iterator it = this.hooks.iterator(); it.hasNext();)
			((WrappedHook) it.next()).hook.start();

		for (final Iterator it = this.hooks.iterator(); it.hasNext();)
			try {
				((WrappedHook) it.next()).hook.join();
			}
			catch (final InterruptedException ie) {
				continue;
			}
	}

	public void printNamingContext() {
		BindingListHolder bindingListHolder = new BindingListHolder();
		BindingIteratorHolder bindingIteratorHolder = new BindingIteratorHolder();
		this.namingContext.list(Integer.MAX_VALUE, bindingListHolder, bindingIteratorHolder);
		Binding[] bindings = bindingListHolder.value;
		for (int i = 0; i < bindings.length; i++) {
			final Binding binding = bindings[i];
			final NameComponent[] nameComponents = binding.binding_name;
			try {
				final String name = this.namingContext.to_string(nameComponents);
				if (binding.binding_type.value() == BindingType._nobject)
					Log.debugMessage("---- " + name, Log.DEBUGLEVEL08);
				else
					Log.debugMessage("+ " + name, Log.DEBUGLEVEL08);
			}
			catch (org.omg.CosNaming.NamingContextPackage.InvalidName in) {
				Log.errorException(in);
			}
		}
	}
}
