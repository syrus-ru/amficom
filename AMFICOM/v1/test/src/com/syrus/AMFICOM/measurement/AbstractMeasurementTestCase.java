/*
 * $Id: AbstractMeasurementTestCase.java,v 1.1.1.1 2005/04/16 21:14:03 cvsadmin Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Collection;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.syrus.AMFICOM.administration.AdministrationDatabaseContext;
import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.DomainDatabase;
import com.syrus.AMFICOM.administration.User;
import com.syrus.AMFICOM.administration.UserDatabase;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseIdentifierGeneratorServer;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.util.Application;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.database.DatabaseConnection;

/**
 * @version $Revision: 1.1.1.1 $, $Date: 2005/04/16 21:14:03 $
 * @author $Author: cvsadmin $
 * @module tools
 */
public abstract class AbstractMeasurementTestCase extends TestCase {

	public static final int		DB_CONNECTION_TIMEOUT	= 120;

	public static final String	DB_SID			= "mcm";

	public static Identifier	domainId;

	protected static Identifier	creatorId;

	public AbstractMeasurementTestCase(String name) {
		super(name);
	}

	public static Test suiteWrapper(Class clazz) {
		TestSuite suite = new TestSuite(clazz);
		TestSetup wrapper = new TestSetup(suite) {

			protected void setUp() {
				oneTimeSetUp();
			}

			protected void tearDown() throws ApplicationException {
				oneTimeTearDown();
			}
		};
		return wrapper;
	}

	static void oneTimeSetUp() {
		Application.init("measurement");
		establishDatabaseConnection();
		DatabaseContextSetup.initDatabaseContext();
		DatabaseContextSetup.initObjectPools();
		UserDatabase userDatabase = AdministrationDatabaseContext.getUserDatabase();
		DomainDatabase domainDatabase = AdministrationDatabaseContext.getDomainDatabase();
		Collection userCollection = null;
		Collection domainCollection = null;

		try {
			userCollection = userDatabase.retrieveByIdsByCondition(null, null);
			domainCollection = domainDatabase.retrieveByIdsByCondition(null, null);
		} catch (RetrieveObjectException roe) {
			roe.printStackTrace();
		} catch (IllegalDataException ide) {
			ide.printStackTrace();
		}

		if ((userCollection == null) || (userCollection.isEmpty()))
			fail("must be at less one user at db");

		if ((domainCollection == null) || (domainCollection.isEmpty()))
			fail("must be at less one domain at db");

		//AbstractMeasurementTestCase.creatorId = new
		// Identifier("Users_1");
		AbstractMeasurementTestCase.creatorId = ((User) userCollection.iterator().next()).getId();
		//AbstractMeasurementTestCase.domainId = new Identifier("Domain_1");
		AbstractMeasurementTestCase.domainId = ((Domain) domainCollection.iterator().next()).getId();
		
		 IdentifierPool.init(new DatabaseIdentifierGeneratorServer());
//		IdentifierPool.init(new XMLIdentifierGeneratorServer());
	}

	static void oneTimeTearDown() throws ApplicationException {
		MeasurementStorableObjectPool.flush(true);
		DatabaseConnection.closeConnection();
	}

	private static void establishDatabaseConnection() {
		String dbHostName = ApplicationProperties.getString("DBHostName", Application.getInternetAddress());
		String dbSid = ApplicationProperties.getString("DBSID", DB_SID);
		String loginName = ApplicationProperties.getString("DBLoginName", "amficom");
		long dbConnTimeout = ApplicationProperties.getInt("DBConnectionTimeout", DB_CONNECTION_TIMEOUT) * 1000;
		try {
			DatabaseConnection.establishConnection(dbHostName, dbSid, dbConnTimeout,loginName, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
