/*
 * $Id: ConfigureTestCase.java,v 1.2 2005/06/02 14:31:02 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Set;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.User;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseIdentifierGeneratorServer;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.measurement.DatabaseContextSetup;
import com.syrus.util.Application;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.database.DatabaseConnection;

/**
 * @version $Revision: 1.2 $, $Date: 2005/06/02 14:31:02 $
 * @author $Author: arseniy $
 * @module tools
 */
public class ConfigureTestCase extends TestCase {

	public static final int		DB_CONNECTION_TIMEOUT	= 120;

	public static final String	DB_SID					= "mcm";

	public static Identifier	domainId;

	protected static Identifier	creatorId;

	public ConfigureTestCase(String name) {
		super(name);
	}

	public static Test suiteWrapper(Class clazz) {
		TestSuite suite = new TestSuite(clazz);
		TestSetup wrapper = new TestSetup(suite) {

			protected void setUp() {
				oneTimeSetUp();
			}

			protected void tearDown() {
				oneTimeTearDown();
			}
		};
		return wrapper;
	}

	public static Test suite() {
		return suiteWrapper(ConfigureTestCase.class);
	}

	static void oneTimeSetUp() {
		Application.init("config");
		establishDatabaseConnection();
		DatabaseContextSetup.initDatabaseContext();
		DatabaseContextSetup.initObjectPools();

		EquivalentCondition equivalentCondition = new EquivalentCondition(ObjectEntities.USER_ENTITY_CODE);
		try {
			Set users = StorableObjectPool.getStorableObjectsByCondition(equivalentCondition, true);
			equivalentCondition.setEntityCode(ObjectEntities.DOMAIN_ENTITY_CODE);
			Set domains = StorableObjectPool.getStorableObjectsByCondition(equivalentCondition, true);

			ConfigureTestCase.creatorId = ((User) users.iterator().next()).getId();
			ConfigureTestCase.domainId = ((Domain) domains.iterator().next()).getId();

			IdentifierPool.init(new DatabaseIdentifierGeneratorServer());
		} catch (ApplicationException e) {
			fail();
		}

	}

	static void oneTimeTearDown() {

		DatabaseConnection.closeConnection();
	}

	private static void establishDatabaseConnection() {
		String dbHostName = ApplicationProperties.getString("DBHostName", Application.getInternetAddress());
		String dbSid = ApplicationProperties.getString("DBSID", DB_SID);
		long dbConnTimeout = ApplicationProperties.getInt("DBConnectionTimeout", DB_CONNECTION_TIMEOUT) * 1000;
		try {
			DatabaseConnection.establishConnection(dbHostName, dbSid, dbConnTimeout);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
