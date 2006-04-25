/*-
 * $Id: TestConcurrent.java,v 1.2 2006/04/25 10:16:32 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.util;

import static com.syrus.AMFICOM.general.ObjectEntities.CHARACTERISTIC_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.CHARACTERISTIC_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.DOMAIN_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MCM_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PARAMETER_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SERVERPROCESS_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SERVER_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SYSTEMUSER_CODE;

import java.util.HashSet;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.general.CORBACommonTest;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;

/**
 * @version $Revision: 1.2 $, $Date: 2006/04/25 10:16:32 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module test
 */
public final class TestConcurrent extends TestCase {

	public TestConcurrent(final String name) {
		super(name);
	}

	public static Test suite() {
		final CORBACommonTest commonTest = new CORBACommonTest();
		commonTest.addTestSuite(TestConcurrent.class);
		return commonTest.createTestSetup();
	}

	public void testIdentifierGeneration() {
		final Set<Thread> threads = new HashSet<Thread>();
		for (int i = 0; i < 1000; i++) {
			final short entityCode;
			if (i % 8 == 0) {
				entityCode = PARAMETER_TYPE_CODE;
			} else if (i % 8 == 1) {
				entityCode = CHARACTERISTIC_TYPE_CODE;
			} else if (i % 8 == 2) {
				entityCode = CHARACTERISTIC_CODE;
			} else if (i % 8 == 3) {
				entityCode = SYSTEMUSER_CODE;
			} else if (i % 8 == 4) {
				entityCode = DOMAIN_CODE;
			} else if (i % 8 == 5) {
				entityCode = SERVER_CODE;
			} else if (i % 8 == 6) {
				entityCode = MCM_CODE;
			} else {
				entityCode = SERVERPROCESS_CODE;
			}

			final Thread thread = new Thread("Thread " + i) {
				@Override
				public void run() {
					for (int j = 0; j < 100; j++) {
						try {
							IdentifierPool.getGeneratedIdentifier(entityCode);
						} catch (IdentifierGenerationException ige) {
							fail(ige.getMessage());
						}
					}
				}
			};
			thread.start();
			threads.add(thread);
		}

		for (final Thread thread : threads) {
			try {
				thread.join();
			} catch (InterruptedException ie) {
				System.err.println(ie);
				ie.printStackTrace();
			}
		}
	}

}
