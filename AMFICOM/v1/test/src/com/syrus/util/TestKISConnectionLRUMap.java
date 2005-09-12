/*-
 * $Id: TestKISConnectionLRUMap.java,v 1.1 2005/09/12 19:54:03 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.util;

import java.util.HashSet;
import java.util.Set;

import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.mcm.KISConnection;
import com.syrus.AMFICOM.mcm.KISReport;
import com.syrus.AMFICOM.measurement.Measurement;

import junit.framework.TestCase;

/**
 * @version $Revision: 1.1 $, $Date: 2005/09/12 19:54:03 $
 * @author $Author: arseniy $
 * @module test
 */
public final class TestKISConnectionLRUMap extends TestCase {

	public TestKISConnectionLRUMap(final String name) {
		super(name);
	}

	public void testExtraSize() {
		final int N = 300;

		final Set<Identifier> kisIds = new HashSet<Identifier>();
		for (int i = 0; i < N; i++) {
			kisIds.add(new Identifier(ObjectEntities.KIS + Identifier.SEPARATOR + Integer.toString(i)));
		}

		final KISConnectionLRUMap connectionLRUMap = new KISConnectionLRUMap(2);

		boolean isEstablished = false;
		for (final Identifier kisId : kisIds) {
			isEstablished = !isEstablished;
			final boolean isE = isEstablished;
			final KISConnection kisConnection = new KISConnection() {
				public boolean isEstablished() {
					return isE;
				}
				public void establish(long kisConnectionTimeout) throws CommunicationException {
					this.establish(kisConnectionTimeout, true);
				}
				public void establish(long kisConnectionTimeout, boolean dropIfAlreadyEstablished) throws CommunicationException {
					System.out.println(this.getKISId() + " -- establishing connection");
				}
				public void drop() {
					System.out.println(this.getKISId() + " -- dropping connection");
				}
				public void transmitMeasurement(Measurement measurement, long timewait) throws CommunicationException {
					System.out.println(this.getKISId() + " -- transmitting");
				}
				public KISReport receiveKISReport(long timewait) throws CommunicationException {
					System.out.println(this.getKISId() + " -- receiving");
					return null;
				}
				public Identifier getKISId() {
					return kisId;
				}
			};
			connectionLRUMap.put(kisId, kisConnection);
		}
	}
}
