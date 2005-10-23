/*-
 * $Id: VerifiedConnectionManager.java,v 1.19 2005/10/23 17:04:25 arseniy Exp $
 *
 * Copyright � 2004-2005 Syrus Systems.
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
 * @version $Revision: 1.19 $, $Date: 2005/10/23 17:04:25 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module csbridge
 */
public class VerifiedConnectionManager {
	private CORBAServer corbaServer;

	Map<String, Verifiable> referencesMap;
	private Set<String> disconnectedServants;
	
	private List<PropertyChangeListener> connectionListeners;

	public VerifiedConnectionManager(final CORBAServer corbaServer, final String[] servantNames) {
		this(corbaServer, new HashSet<String>(Arrays.asList(servantNames)));
	}

	public VerifiedConnectionManager(final CORBAServer corbaServer, final Set<String> servantNames) {
		assert corbaServer != null: "corbaServer is NULL";
		assert servantNames != null: "Servant names is NULL";
		assert !servantNames.isEmpty(): ErrorMessages.����_������;

		this.corbaServer = corbaServer;

		this.referencesMap = Collections.synchronizedMap(new HashMap<String, Verifiable>(servantNames.size()));
		for (Iterator<String> it = servantNames.iterator(); it.hasNext();) {
			final String servantName = it.next();
			this.referencesMap.put(servantName, null);
		}
		this.disconnectedServants = Collections.synchronizedSet(new HashSet<String>(servantNames));
	}

	public final Verifiable getVerifiableReference(final String servantName) throws CommunicationException {
		if (servantName == null) {
			throw new NullPointerException();
		}
		if (!this.referencesMap.containsKey(servantName)) {
			throw new IllegalArgumentException("Servant '" + servantName + "' not registered for this manager");
		}

		Verifiable reference = this.referencesMap.get(servantName);

		if (reference == null) {
			reference = this.activateAndGet(servantName);
		}

		try {
			reference.verify((byte) 1);
		} catch (final SystemException se) {
			reference = this.activateAndGet(servantName);
		}

		return reference;
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
		final Verifiable reference = this.referencesMap.get(servantName);
		if (reference != null) {
			return reference;
		}
		throw new CommunicationException(I18N.getString("Error.CannotEstablishConnectionWith") + " '" + servantName + '\'');
	}

	private void activateVerifiableReference(final String servantName) {
		try {
			final Verifiable reference = VerifiableHelper.narrow(this.corbaServer.resolveReference(servantName));
			this.referencesMap.put(servantName, reference);
			if (this.disconnectedServants.remove(servantName)) {
				this.onRestoreConnection(servantName);
			}
		} catch (CommunicationException ce) {
			Log.errorException(ce);
			this.referencesMap.put(servantName, null);
			this.disconnectedServants.add(servantName);
			this.onLoseConnection(servantName);
		}
	}

	/**
	 * @todo Make final. In subcalsses register application-specific listeners instead of method overridding
	 * @param servantName
	 */
	protected void onLoseConnection(final String servantName) {
		Log.debugMessage("Connection with '" + servantName + "' lost", Log.DEBUGLEVEL08);
		this.firePropertyChangeListners(new PropertyChangeEvent(this, servantName, Boolean.TRUE, Boolean.FALSE));
	}

	/**
	 * @todo Make final. In subcalsses register application-specific listeners instead of method overridding
	 * @param servantName
	 */
	protected void onRestoreConnection(final String servantName) {
		Log.debugMessage("Connection with '" + servantName + "' restored", Log.DEBUGLEVEL08);
		
		this.firePropertyChangeListners(new PropertyChangeEvent(this, servantName, Boolean.FALSE, Boolean.TRUE));
	}
	
	private void firePropertyChangeListners(PropertyChangeEvent propertyChangeEvent) {
		if (this.connectionListeners != null && !this.connectionListeners.isEmpty()) {
			for (Iterator<PropertyChangeListener> iterator = this.connectionListeners.iterator(); iterator.hasNext();) {
				PropertyChangeListener listener = iterator.next();
				listener.propertyChange(propertyChangeEvent);
			}
		}
	}

	public void addPropertyListener(PropertyChangeListener listener) {
		if (this.connectionListeners == null) {
			this.connectionListeners = new LinkedList<PropertyChangeListener>();
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
