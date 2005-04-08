/*
 * $Id: GeneralTestCase.java,v 1.1 2005/04/08 16:44:42 cvsadmin Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package test.com.syrus.AMFICOM.general;

import java.util.Date;
import java.util.Set;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.syrus.AMFICOM.administration.AdministrationStorableObjectPool;
import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.User;
import com.syrus.AMFICOM.cmserver.DatabaseContextSetup;
import com.syrus.AMFICOM.general.AccessIdentity;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DefaultIdentifierGeneratorServer;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.SessionContext;
import com.syrus.util.Application;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.database.DatabaseConnection;

/**
 * @version $Revision: 1.1 $, $Date: 2005/04/08 16:44:42 $
 * @author $Author: cvsadmin $
 * @module tools
 */
public class GeneralTestCase extends TestCase {

	public static final int		DB_CONNECTION_TIMEOUT	= 120;

	public static final String	DB_SID					= "mcm";

	public static Identifier	domainId;

	protected static Identifier	creatorId;

	public GeneralTestCase(String name) {
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
		Application.init("general");
		establishDatabaseConnection();
		DatabaseContextSetup.initDatabaseContext();
		DatabaseContextSetup.initObjectPools();

		EquivalentCondition equivalentCondition = new EquivalentCondition(ObjectEntities.USER_ENTITY_CODE);
		try {
			Set users = AdministrationStorableObjectPool.getStorableObjectsByCondition(equivalentCondition, true);
			equivalentCondition.setEntityCode(ObjectEntities.DOMAIN_ENTITY_CODE);
			Set domains = AdministrationStorableObjectPool.getStorableObjectsByCondition(equivalentCondition, true);

			GeneralTestCase.creatorId = ((User) users.iterator().next()).getId();
			GeneralTestCase.domainId = ((Domain) domains.iterator().next()).getId();

			IdentifierPool.init(new DefaultIdentifierGeneratorServer());
			
			SessionContext.init(new AccessIdentity(new Date(), domainId, creatorId, "Session_0"), "");
		} catch (ApplicationException e) {
			fail();
		}

	}

	static void oneTimeTearDown() {
		try {
			GeneralStorableObjectPool.flush(true);
			AdministrationStorableObjectPool.flush(true);
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
