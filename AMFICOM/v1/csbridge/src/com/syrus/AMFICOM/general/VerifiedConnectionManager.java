/*-
 * $Id: VerifiedConnectionManager.java,v 1.3 2005/05/13 17:38:43 bass Exp $
 *
 * Copyright Ώ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
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
 * @version $Revision: 1.3 $, $Date: 2005/05/13 17:38:43 $
 * @author $Author: bass $
 * @module csbridge_v1
 */
public class VerifiedConnectionManager {
	private CORBAServer corbaServer;

	Map referencesMap; //Map <String servantName, Verifiable reference>
	private Set disconnectedServants; //Set <String servantName>

	public VerifiedConnectionManager(CORBAServer corbaServer, String[] servantNames) {
		this(corbaServer, new HashSet(Arrays.asList(servantNames)));
	}

	public VerifiedConnectionManager(final CORBAServer corbaServer, final Set servantNames) {
		assert corbaServer != null: "corbaServer is NULL"; //$NON-NLS-1$
		assert servantNames != null: "Servant names is NULL"; //$NON-NLS-1$
//		assert !servantNames.isEmpty(): ErrorMessages.θυμι_πυστοκ;

		this.corbaServer = corbaServer;

		this.referencesMap = Collections.synchronizedMap(new HashMap(servantNames.size()));
		Object servantName;
		for (Iterator it = servantNames.iterator(); it.hasNext();) {
			servantName = it.next();
			assert (servantName instanceof String): "Name of servant must be of type String"; //$NON-NLS-1$
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
		throw new IllegalDataException("Servant '" + servantName + "' not registered for this manager"); //$NON-NLS-1$ //$NON-NLS-2$
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
		throw new CommunicationException("Cannot establish connection with  '" + servantName + "'"); //$NON-NLS-1$ //$NON-NLS-2$
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
		Log.debugMessage("VerifiedConnectionManager.onLoseConnection | Connection with '" + servantName + "' lost", Log.DEBUGLEVEL08); //$NON-NLS-1$ //$NON-NLS-2$
	}

	protected void onRestoreConnection(String servantName) {
		Log.debugMessage("VerifiedConnectionManager.onRestoreConnection | Connection with '" + servantName + "' restored", //$NON-NLS-1$ //$NON-NLS-2$
				Log.DEBUGLEVEL08);
	}

}
