/*-
 * $Id: TestKISConnectionLRUMap.java,v 1.2 2005/09/13 19:47:53 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.mcm;

import java.util.HashSet;
import java.util.Set;

import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.mcm.KISConnection;
import com.syrus.AMFICOM.mcm.KISReport;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.util.KISConnectionLRUMap;

import junit.framework.TestCase;

/**
 * @version $Revision: 1.2 $, $Date: 2005/09/13 19:47:53 $
 * @author $Author: arseniy $
 * @module test
 */
public final class TestKISConnectionLRUMap extends TestCase {
	static final KISConnectionLRUMap CONNECTION_LRU_MAP = new KISConnectionLRUMap(2);

	private class Transceiver extends Thread {
		private Identifier kisId;
		private KISConnection kisConnection;

		Transceiver(final Identifier kisId) {
			super(kisId.toString());

			this.kisId = kisId;
			
			this.kisConnection = CONNECTION_LRU_MAP.get(this.kisId);
			if (this.kisConnection == null) {
				this.kisConnection = new KISConnection() {
					public boolean isEstablished() {
						return true;
					}
					public synchronized void establish(final long kisConnectionTimeout) throws CommunicationException {
						this.establish(kisConnectionTimeout, true);
					}
					public synchronized void establish(final long kisConnectionTimeout, final boolean dropIfAlreadyEstablished)
					throws CommunicationException {
						System.out.println(this.getKISId() + " -- establishing connection");
					}
					public synchronized void drop() {
						System.out.println(this.getKISId() + " -- dropping connection");
					}
					public synchronized void transmitMeasurement(final Measurement measurement, final long timewait) throws CommunicationException {
						System.out.println(this.getKISId() + " -- transmitting");
					}
					public synchronized KISReport receiveKISReport(final long timewait) throws CommunicationException {
						System.out.println(this.getKISId() + " -- receiving");
						try {
							Thread.sleep(timewait);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						return null;
					}
					public Identifier getKISId() {
						return kisId;
					}
				};
				CONNECTION_LRU_MAP.put(this.kisId, this.kisConnection);
			}
		}

		@Override
		public void run() {
			while (true) {
				if (this.kisConnection.isEstablished()) {
					System.out.println("KIS " + this.kisId + ", established");
					try {
						this.kisConnection.receiveKISReport(5000);
					} catch (CommunicationException e) {
						//Never
						e.printStackTrace();
					}
				}
			}
		}
	}

	public TestKISConnectionLRUMap(final String name) {
		super(name);
	}

	public void testExtraSize() {
		final int N = 3;

		final Set<Identifier> kisIds = new HashSet<Identifier>();
		for (int i = 0; i < N; i++) {
			kisIds.add(new Identifier(ObjectEntities.KIS + Identifier.SEPARATOR + Integer.toString(i)));
		}

		for (final Identifier kisId : kisIds) {
			final Transceiver transceiver = new Transceiver(kisId);
			transceiver.start();
			System.out.println("Started transceiver for KIS " + kisId);
		}

		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
