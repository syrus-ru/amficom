/*
 * $Id: AbstractMesurementTestCase.java,v 1.1 2005/02/04 14:18:32 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.List;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.syrus.AMFICOM.administration.AdministrationDatabaseContext;
import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.DomainDatabase;
import com.syrus.AMFICOM.administration.User;
import com.syrus.AMFICOM.administration.UserDatabase;
import com.syrus.AMFICOM.general.DefaultIdentifierGeneratorServer;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.util.Application;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.database.DatabaseConnection;

/**
 * @version $Revision: 1.1 $, $Date: 2005/02/04 14:18:32 $
 * @author $Author: bob $
 * @module tools
 */
public abstract class AbstractMesurementTestCase extends TestCase {

	public static final int		DB_CONNECTION_TIMEOUT	= 120;

	public static final String	DB_SID			= "mcm";

	public static Identifier	domainId;

	protected static Identifier	creatorId;

	public AbstractMesurementTestCase(String name) {
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
		Application.init("measurement");
		establishDatabaseConnection();
		DatabaseContextSetup.initDatabaseContext();
		DatabaseContextSetup.initObjectPools();
		UserDatabase userDatabase = (UserDatabase) AdministrationDatabaseContext.getUserDatabase();
		DomainDatabase domainDatabase = (DomainDatabase) AdministrationDatabaseContext.getDomainDatabase();
		List userList = null;
		List domainList = null;

		try {
			userList = userDatabase.retrieveByIds(null, null);
			domainList = domainDatabase.retrieveByIds(null, null);
		} catch (RetrieveObjectException roe) {
			roe.printStackTrace();
		} catch (IllegalDataException ide) {
			ide.printStackTrace();
		}

		if ((userList == null) || (userList.isEmpty()))
			fail("must be at less one user at db");

		if ((domainList == null) || (domainList.isEmpty()))
			fail("must be at less one domain at db");

		//AbstractMesurementTestCase.creatorId = new
		// Identifier("Users_1");
		AbstractMesurementTestCase.creatorId = ((User) userList.get(0)).getId();
		//AbstractMesurementTestCase.domainId = new Identifier("Domain_1");
		AbstractMesurementTestCase.domainId = ((Domain) domainList.get(0)).getId();
		
		 IdentifierPool.init(new DefaultIdentifierGeneratorServer());
//		IdentifierPool.init(new XMLIdentifierGeneratorServer());
	}

	static void oneTimeTearDown() {

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
