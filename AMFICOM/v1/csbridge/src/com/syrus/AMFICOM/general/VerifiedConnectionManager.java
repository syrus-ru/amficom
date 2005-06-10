/*-
 * $Id: VerifiedConnectionManager.java,v 1.7 2005/06/10 14:48:21 arseniy Exp $
 *
 * Copyright Ώ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.omg.CORBA.SystemException;

import com.syrus.AMFICOM.general.corba.Verifiable;
import com.syrus.AMFICOM.general.corba.VerifiableHelper;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.7 $, $Date: 2005/06/10 14:48:21 $
 * @author $Author: arseniy $
 * @module csbridge_v1
 */
public class VerifiedConnectionManager {
	private CORBAServer corbaServer;

	Map referencesMap; //Map <String servantName, Verifiable reference>
	private Set disconnectedServants; //Set <String servantName>
	
	private List connectionListeners;

	public VerifiedConnectionManager(final CORBAServer corbaServer, final String[] servantNames) {
		this(corbaServer, new HashSet(Arrays.asList(servantNames)));
	}

	public VerifiedConnectionManager(final CORBAServer corbaServer, final Set servantNames) {
		assert corbaServer != null: "corbaServer is NULL";
		assert servantNames != null: "Servant names is NULL";
//		assert !servantNames.isEmpty(): ErrorMessages.θυμι_πυστοκ;

		this.corbaServer = corbaServer;

		this.referencesMap = Collections.synchronizedMap(new HashMap(servantNames.size()));
		Object servantName;
		for (Iterator it = servantNames.iterator(); it.hasNext();) {
			servantName = it.next();
			assert (servantName instanceof String): "Name of servant must be of type String";
			this.referencesMap.put(servantName, null);
		}
		this.disconnectedServants = Collections.synchronizedSet(new HashSet(servantNames));
	}

	public Verifiable getVerifiableReference(final String servantName) throws CommunicationException, IllegalDataException {
		if (this.referencesMap.containsKey(servantName)) {
			Verifiable reference = (Verifiable) this.referencesMap.get(servantName);

			if (reference == null)
				reference = this.activateAndGet(servantName);

			try {
				Log.debugMessage("VerifiedConnectionManager.getVerifiableReference | 1 ", Log.FINEST);
				reference.verify((byte) 1);
				Log.debugMessage("VerifiedConnectionManager.getVerifiableReference | 2 ", Log.FINEST);
			}
			catch (SystemException se) {
				reference = this.activateAndGet(servantName);
			}

			return reference;
		}
		throw new IllegalDataException("Servant '" + servantName + "' not registered for this manager");
	}

	public void addServantName(final String servantName) {
		if (!this.referencesMap.containsKey(servantName)) {
			this.referencesMap.put(servantName, null);
			this.disconnectedServants.add(servantName);
		}
	}

	public void removeServantName(final String servantName) {
		if (this.referencesMap.containsKey(servantName)) {
			this.referencesMap.remove(servantName);
			this.disconnectedServants.remove(servantName);
		}
	}

	public CORBAServer getCORBAServer() {
		return this.corbaServer;
	}

	private Verifiable activateAndGet(final String servantName) throws CommunicationException {
		this.activateVerifiableReference(servantName);
		Verifiable reference = (Verifiable) this.referencesMap.get(servantName);
		if (reference != null)
			return reference;
		throw new CommunicationException("Cannot establish connection with  '" + servantName + "'");
	}

	private void activateVerifiableReference(final String servantName) {
		try {
			Verifiable reference = VerifiableHelper.narrow(this.corbaServer.resolveReference(servantName));
			this.referencesMap.put(servantName, reference);
			if (this.disconnectedServants.remove(servantName))
				this.onRestoreConnection(servantName);
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
			this.disconnectedServants.add(servantName);
			this.onLoseConnection(servantName);
		}
	}

	/**
	 * @todo Make final. In subcalsses register application-specific listeners instead of method overridding
	 * @param servantName
	 */
	protected void onLoseConnection(final String servantName) {
		Log.debugMessage("VerifiedConnectionManager.onLoseConnection | Connection with '" + servantName + "' lost", Log.DEBUGLEVEL08);
		this.firePropertyChangeListners(new PropertyChangeEvent(this, servantName, Boolean.TRUE, Boolean.FALSE));
	}

	/**
	 * @todo Make final. In subcalsses register application-specific listeners instead of method overridding
	 * @param servantName
	 */
	protected void onRestoreConnection(final String servantName) {
		Log.debugMessage("VerifiedConnectionManager.onRestoreConnection | Connection with '" + servantName + "' restored",
				Log.DEBUGLEVEL08);
		
		this.firePropertyChangeListners(new PropertyChangeEvent(this, servantName, Boolean.FALSE, Boolean.TRUE));
	}
	
	private void firePropertyChangeListners(PropertyChangeEvent propertyChangeEvent) {
		if (this.connectionListeners != null && !this.connectionListeners.isEmpty()) {
			for (Iterator iterator = this.connectionListeners.iterator(); iterator.hasNext();) {
				PropertyChangeListener listener = (PropertyChangeListener) iterator.next();
				listener.propertyChange(propertyChangeEvent);
			}
		}
	}

	public void addPropertyListener(PropertyChangeListener listener) {
		if (this.connectionListeners == null) {
			this.connectionListeners = new LinkedList();
		}
		
		if (!this.connectionListeners.contains(listener)) {
			this.connectionListeners.add(listener);
		}
	}
	
	public void removePropertyListener(PropertyChangeListener listener) {
		if (this.connectionListeners == null) {
			return;
		}
		
		if (this.connectionListeners.contains(listener)) {
			this.connectionListeners.remove(listener);
		}
	}
}
