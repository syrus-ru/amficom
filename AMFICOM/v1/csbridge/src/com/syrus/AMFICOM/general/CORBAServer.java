/*-
* $Id: CORBAServer.java,v 1.27 2005/11/28 12:21:52 arseniy Exp $
*
* Copyright ? 2004-2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/


package com.syrus.AMFICOM.general;

import java.util.Collections;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.omg.CORBA.ORB;
import org.omg.CORBA.Policy;
import org.omg.CORBA.SystemException;
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
 * @version $Revision: 1.27 $, $Date: 2005/11/28 12:21:52 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module csbridge
 */

public class CORBAServer {
	public static final String DEFAULT_ORB_INITIAL_HOST = "127.0.0.1";
	public static final int DEFAULT_ORB_INITIAL_PORT = 1050;

	ORB orb;
	private POA poa;
	
	private String	rootContextNameString;
	
	private NamingContextExt namingContext;

	/*	Names of bound servants. Need for unbound all servants on shutdown*/
	private Set<String> servantNames;

	/*	Wrapper class for shutdown hook*/
	private class WrappedHook {
		Thread hook;

		WrappedHook(final Thread hook) {
			this.hook = hook;
		}

		@Override
		public int hashCode() {
	    return System.identityHashCode(this.hook);
		}

		@Override
		public boolean equals(final Object object) {
			if (object instanceof WrappedHook) {
				return (((WrappedHook) object).hook == this.hook);
			}
			return false;
		}
	}

	/*	This field is set to false on shutdown just before running hooks*/
	private boolean running;

	/*	Hooks themselves*/
	private Set<WrappedHook> hooks;	

	public CORBAServer(final String rootContextName) throws CommunicationException {
		this.initORB();

		try {
			this.initPOA();
		} catch (UserException ue) {
			throw new CommunicationException("Cannot activate POA", ue);
		}

		this.initNamingContext(rootContextName);

		this.runORB();

		this.running = true;
	}

	private void initORB() {
		final Properties properties = System.getProperties();
		final String host = ApplicationProperties.getString("ORBInitialHost", DEFAULT_ORB_INITIAL_HOST);
		final int port = ApplicationProperties.getInt("ORBInitialPort", DEFAULT_ORB_INITIAL_PORT);
		Log.debugMessage("host: " + host + ", port: " + port, Log.DEBUGLEVEL09);
		properties.setProperty("org.omg.CORBA.ORBInitialHost", host);
		properties.setProperty("org.omg.CORBA.ORBInitialPort", Integer.toString(port));
		final String[] args = null;
		this.orb = ORB.init(args, properties);
	}

	private void initPOA() throws UserException {
		POA rootPoa;
		try {
			rootPoa = POAHelper.narrow(this.orb.resolve_initial_references("RootPOA"));
		} catch (org.omg.CORBA.ORBPackage.InvalidName in) {
			Log.errorMessage(in);
			return;
		}
		final Policy[] policies = new Policy[7];
		policies[0] = rootPoa.create_thread_policy(ThreadPolicyValue.ORB_CTRL_MODEL);
		policies[1] = rootPoa.create_lifespan_policy(LifespanPolicyValue.TRANSIENT);
		policies[2] = rootPoa.create_id_uniqueness_policy(IdUniquenessPolicyValue.UNIQUE_ID);
		policies[3] = rootPoa.create_id_assignment_policy(IdAssignmentPolicyValue.SYSTEM_ID);
		policies[4] = rootPoa.create_servant_retention_policy(ServantRetentionPolicyValue.RETAIN);
		policies[5] = rootPoa.create_request_processing_policy(RequestProcessingPolicyValue.USE_ACTIVE_OBJECT_MAP_ONLY);
		policies[6] = rootPoa.create_implicit_activation_policy(ImplicitActivationPolicyValue.IMPLICIT_ACTIVATION);

		this.poa = rootPoa.create_POA("poa1", rootPoa.the_POAManager(), policies);
		for (final Policy policy : policies) {
			policy.destroy();
		}

		this.poa.the_POAManager().activate();
	}

	private void initNamingContext(final String rootContextNameStr) throws CommunicationException {
		this.bindIfNonExistingNamingContext(rootContextNameStr);
		this.servantNames = Collections.synchronizedSet(new HashSet<String>());
	}

	private void bindIfNonExistingNamingContext(final String rootContextNameStr) throws CommunicationException {
		this.rootContextNameString = rootContextNameStr;
		try {
			final NamingContextExt rootNamingContext = NamingContextExtHelper.narrow(this.orb.resolve_initial_references("NameService"));

			final NameComponent[] contextName = rootNamingContext.to_name(rootContextNameStr);
			try {
				Log.debugMessage("Creating naming context: '" + rootContextNameStr + "'", Log.DEBUGLEVEL08);
				this.namingContext = NamingContextExtHelper.narrow(rootNamingContext.bind_new_context(contextName));
			} catch (AlreadyBound ab) {
				Log.debugMessage("Naming context: '" + rootContextNameStr + "' already bound; trying to resolve", Log.DEBUGLEVEL08);
				this.namingContext = NamingContextExtHelper.narrow(rootNamingContext.resolve_str(rootContextNameStr));
			}
		} catch (final UserException ue) {
			throw new CommunicationException(I18N.getString("Error.CannotCreateContext"), ue);
		} catch (final SystemException se) {
			throw new CommunicationException(I18N.getString("Error.CannotCreateContext"), se);
		}
	}

	private void runORB() {
		final Thread thread = new Thread("ORB") {
			@Override
			public void run() {
				CORBAServer.this.orb.run();
			}
		};
		thread.setDaemon(true);
		thread.setName(this.orb.getClass().getName());
		thread.start();
		Runtime.getRuntime().addShutdownHook(new Thread("CORBAServer -- shutdown hook") {
			@Override
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
/*
	public void activateServant(final Servant servant) throws CommunicationException {
		if (this.running) {
			try {
				this.poa.activate_object(servant);
			} catch (UserException ue) {
				throw new CommunicationException("Cannot activate servant '" + servant + "' -- " + ue.getMessage(), ue);
			}
		} else {
			throw new IllegalStateException("Cannot activate '" + servant + "' due to shutdown");
		}
	}
*/
	public void activateServant(final Servant servant, final String name) throws CommunicationException {
		if (this.running) {
			try {
				this.poa.activate_object(servant);

				final org.omg.CORBA.Object reference = this.poa.servant_to_reference(servant);
				final NameComponent[] nameComponents = this.namingContext.to_name(name);
				this.namingContext.rebind(nameComponents, reference);
				this.servantNames.add(name);
				Log.debugMessage("Activated servant '" + name + "'", Log.DEBUGLEVEL05);
			} catch (UserException ue) {
				throw new CommunicationException("Cannot activate servant '" + name + "' -- " + ue.getMessage(), ue);
			}
		} else {
			throw new IllegalStateException("Cannot activate '" + name + "' due to shutdown");
		}
	}

	/**
	 * 
	 * @param name - name of servant to deactivate
	 * @param isOwner -- if caller is owner of this servant, i. e. can deactivate it from it's POA.
	 * @throws CommunicationException
	 */
	public void deactivateServant(final String name, final boolean isOwner) throws CommunicationException {
		if (this.running) {
			try {
				this.unbindServant(name);

				if (isOwner) {
					final NameComponent[] nameComponents = this.namingContext.to_name(name);
					final org.omg.CORBA.Object reference = this.namingContext.resolve(nameComponents);
					final byte[] id = this.poa.reference_to_id(reference);
					this.poa.deactivate_object(id);
				}

				Log.debugMessage("Deactivated servant '" + name + "'", Log.DEBUGLEVEL05);
			} catch (UserException ue) {
				throw new CommunicationException("Cannot deactivate servant '" + name + "' -- " + ue.getMessage(), ue);
			}
		} else {
			throw new IllegalStateException("Cannot deactivate '" + name + "' due to shutdown");
		}
	}

	public org.omg.CORBA.Object resolveReference(final String name) throws CommunicationException {
		try {
			Log.debugMessage("Resolving name: " + name, Log.DEBUGLEVEL08);
			final org.omg.CORBA.Object ref = this.namingContext.resolve_str(name);
			Log.debugMessage("Resolved reference: " + this.orb.object_to_string(ref), Log.DEBUGLEVEL10);
			return ref;
		} catch (UserException nf) {
			throw new CommunicationException('\'' + name + "' " + I18N.getString("Error.Text.NotFound"), nf);
		}
	}

	public String objectToString(final org.omg.CORBA.Object ref) {
		return this.orb.object_to_string(ref);
	}

	public org.omg.CORBA.Object stringToObject(final String ior) {
		return this.orb.string_to_object(ior);
	}

	/* TODO Maybe return set of CORBA references instead of strings */
	public String[] getSubContextReferences(final String subContextName) throws CommunicationException {
		org.omg.CORBA.Object subContextRef = null;
		try {
			subContextRef = this.namingContext.resolve_str(subContextName);
		} catch (UserException ue) {
			throw new CommunicationException("Cannot resolve subcontext '" + subContextName + "' -- " + ue.getMessage(), ue);
		}

		NamingContextExt subContext = null;
		try {
			subContext = NamingContextExtHelper.narrow(subContextRef);
		} catch (Exception e) {
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
				} else {
					Log.errorMessage("Binding '" + bindingName + "' not of type object");
				}
			} catch (InvalidName in) {
				Log.errorMessage(in);
			}
		}
		return ret;
	}

	public void addShutdownHook(final Thread hook) {
		if (this.running) {
			if (!hook.isAlive()) {
				if (this.hooks == null) {
					this.hooks = new HashSet<WrappedHook>(1);
					this.hooks.add(new WrappedHook(hook));
				} else {
					WrappedHook wrappedHook = new WrappedHook(hook);
					if (!this.hooks.contains(wrappedHook)) {
						this.hooks.add(wrappedHook);
					} else {
						Log.errorMessage("Cannot add shutdown hook -- it is already added");
					}
				}
			} else {
				Log.errorMessage("Cannot add shutdown hook -- it is already running");
			}
		} else {
			Log.errorMessage("CORBAServer | Cannot add shutdown hook -- shutting down");
		}
	}

	public boolean removeShutdownHook(final Thread hook) {
		if (this.hooks == null) {
			return false;
		}

		if (this.running) {
			if (hook != null) {
				boolean ret = this.hooks.remove(new WrappedHook(hook));
				if (ret && this.hooks.isEmpty()) {
					this.hooks = null;
				}
				return ret;
			}
			Log.errorMessage("Cannot remove NULL shutdown hook");
			return false;
		}
		Log.errorMessage("Cannot remove shutdown hook -- shutting down");
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

		final Set<String> servantnames = new HashSet<String>(this.servantNames);
		for (final String servantName : servantnames) {
			try {
				this.unbindServant(servantName);
			} catch (CommunicationException ce) {
				Log.errorMessage(ce);
			}
		}

		try {
			this.poa.the_POAManager().deactivate(false, false);
		} catch (UserException ue) {
			Log.errorMessage(ue);
		}
		this.poa.destroy(false, false);
		this.orb.shutdown(true);
	}

	private void unbindServant(final String name) throws CommunicationException {
		try {
			final NameComponent[] nameComponents = this.namingContext.to_name(name);
			this.namingContext.unbind(nameComponents);
			this.servantNames.remove(name);
			Log.debugMessage("Unbinded servant '" + name + "'", Log.DEBUGLEVEL05);
		}
		catch (NotFound nf) {
			Log.errorMessage("Cannot unbind servant '" + name + "' -- no such servant");
		}
		catch (UserException ue) {
			throw new CommunicationException("Cannot deactivate servant '" + name + "'", ue);
		}
	}

	private void runHooks() {
		if (this.hooks == null) {
			return;
		}

		/*
		 * No synchronization on hooks. The value false of field running guarantees,
		 * that hooks cannot be modified
		 */
		for (final WrappedHook wrappedHook : this.hooks) {
			wrappedHook.hook.start();
		}

		for (final WrappedHook wrappedHook : this.hooks) {
			try {
				wrappedHook.hook.join();
			} catch (final InterruptedException ie) {
				continue;
			}
		}
	}

	public void printNamingContext() {
		final StringBuffer stringBuffer = new StringBuffer();

		final BindingListHolder bindingListHolder = new BindingListHolder();
		final BindingIteratorHolder bindingIteratorHolder = new BindingIteratorHolder();
		this.namingContext.list(Integer.MAX_VALUE, bindingListHolder, bindingIteratorHolder);
		final Binding[] bindings = bindingListHolder.value;
		for (int i = 0; i < bindings.length; i++) {
			final Binding binding = bindings[i];
			final NameComponent[] nameComponents = binding.binding_name;
			try {
				final String name = this.namingContext.to_string(nameComponents);
				if (binding.binding_type.value() == BindingType._nobject) {
					stringBuffer.append(" ---- ");
				} else {
					stringBuffer.append(" + ");
				}
				stringBuffer.append(name);
				stringBuffer.append('\n');
			} catch (org.omg.CosNaming.NamingContextPackage.InvalidName in) {
				Log.errorMessage(in);
			}
		}

		System.out.println(stringBuffer);
	}
	
	public String getRootContextName() {
		return this.rootContextNameString;
	}
}
