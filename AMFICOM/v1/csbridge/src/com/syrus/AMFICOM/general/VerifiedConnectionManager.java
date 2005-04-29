/*
 * $Id: VerifiedConnectionManager.java,v 1.2 2005/04/29 10:02:43 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.omg.CORBA.SystemException;

import com.syrus.AMFICOM.general.corba.Verifiable;
import com.syrus.AMFICOM.general.corba.VerifiableHelper;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.2 $, $Date: 2005/04/29 10:02:43 $
 * @author $Author: arseniy $
 * @module csbridge_v1
 */
public class VerifiedConnectionManager {
	private CORBAServer corbaServer;

	Map referencesMap; //Map <String servantName, Verifiable reference>
	private Set disconnectedServants; //Set <String servantName>

	public VerifiedConnectionManager(CORBAServer corbaServer, String[] servantNames) {
		this(corbaServer, new HashSet(Arrays.asList(servantNames)));
	}

	public VerifiedConnectionManager(CORBAServer corbaServer, Set servantNames) {
		assert corbaServer != null : "corbaServer is NULL";
		assert servantNames != null : "Servant names is NULL";
		//assert !servantNames.isEmpty() : "Хули пустой-то, а?";

		this.corbaServer = corbaServer;

		this.referencesMap = Collections.synchronizedMap(new HashMap(servantNames.size()));
		Object servantName;
		for (Iterator it = servantNames.iterator(); it.hasNext();) {
			servantName = it.next();
			assert (servantName instanceof String) : "Name of servant must be of type String";
			this.referencesMap.put(servantName, null);
		}
		this.disconnectedServants = Collections.synchronizedSet(new HashSet(servantNames));
	}

	public Verifiable getVerifiableReference(String servantName) throws CommunicationException, IllegalDataException {
		if (this.referencesMap.containsKey(servantName)) {
			Verifiable reference = (Verifiable) this.referencesMap.get(servantName);

			if (reference == null)
				reference = this.activateAndGet(servantName);

			try {
				reference.verify((byte) 1);
			}
			catch (SystemException se) {
				reference = this.activateAndGet(servantName);
			}

			return reference;
		}
		throw new IllegalDataException("Servant '" + servantName + "' not registered for this manager");
	}

	public void addServantName(String servantName) {
		if (!this.referencesMap.containsKey(servantName)) {
			this.referencesMap.put(servantName, null);
			this.disconnectedServants.add(servantName);
		}
	}

	public void removeServantName(String servantName) {
		if (this.referencesMap.containsKey(servantName)) {
			this.referencesMap.remove(servantName);
			this.disconnectedServants.remove(servantName);
		}
	}

	public CORBAServer getCORBAServer() {
		return this.corbaServer;
	}

	private Verifiable activateAndGet(String servantName) throws CommunicationException {
		this.activateVerifiableReference(servantName);
		Verifiable reference = (Verifiable) this.referencesMap.get(servantName);
		if (reference != null)
			return reference;
		throw new CommunicationException("Cannot establish connection with  '" + servantName + "'");
	}

	private void activateVerifiableReference(String servantName) {
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

	protected void onLoseConnection(String servantName) {
		Log.debugMessage("VerifiedConnectionManager.onLoseConnection | Connection with '" + servantName + "' lost", Log.DEBUGLEVEL08);
	}

	protected void onRestoreConnection(String servantName) {
		Log.debugMessage("VerifiedConnectionManager.onRestoreConnection | Connection with '" + servantName + "' restored",
				Log.DEBUGLEVEL08);
	}

}
