/*
 * $Id: ConfigureTestCase.java,v 1.8 2005/04/08 14:43:56 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package test.com.syrus.AMFICOM.configuration;

import java.util.Set;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.syrus.AMFICOM.administration.AdministrationStorableObjectPool;
import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.User;
import com.syrus.AMFICOM.cmserver.DatabaseContextSetup;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DefaultIdentifierGeneratorServer;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.util.Application;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.database.DatabaseConnection;

/**
 * @version $Revision: 1.8 $, $Date: 2005/04/08 14:43:56 $
 * @author $Author: bob $
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

	static void oneTimeSetUp() {
		Application.init("config");
		establishDatabaseConnection();
		DatabaseContextSetup.initDatabaseContext();
		DatabaseContextSetup.initObjectPools();

		EquivalentCondition equivalentCondition = new EquivalentCondition(ObjectEntities.USER_ENTITY_CODE);
		try {
			Set users = AdministrationStorableObjectPool.getStorableObjectsByCondition(equivalentCondition, true);
			equivalentCondition.setEntityCode(ObjectEntities.DOMAIN_ENTITY_CODE);
			Set domains = AdministrationStorableObjectPool.getStorableObjectsByCondition(equivalentCondition, true);

			ConfigureTestCase.creatorId = ((User) users.iterator().next()).getId();
			ConfigureTestCase.domainId = ((Domain) domains.iterator().next()).getId();

			IdentifierPool.init(new DefaultIdentifierGeneratorServer());
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
