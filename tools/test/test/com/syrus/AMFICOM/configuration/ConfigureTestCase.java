/*
 * $Id: ConfigureTestCase.java,v 1.4 2004/09/09 14:28:26 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package test.com.syrus.AMFICOM.configuration;

import java.util.List;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.syrus.AMFICOM.configuration.ConfigurationDatabaseContext;
import com.syrus.AMFICOM.configuration.Domain;
import com.syrus.AMFICOM.configuration.DomainDatabase;
import com.syrus.AMFICOM.configuration.User;
import com.syrus.AMFICOM.configuration.UserDatabase;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.mserver.DatabaseContextSetup;
import com.syrus.util.Application;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.database.DatabaseConnection;


/**
 * @version $Revision: 1.4 $, $Date: 2004/09/09 14:28:26 $
 * @author $Author: bob $
 * @module tools
 */
public class ConfigureTestCase extends TestCase{

	public static final int		DB_CONNECTION_TIMEOUT	= 120;

	public static final String	DB_SID					= "amficom";
	
	public static Identifier domainId;

	protected static Identifier	creatorId;	
	
	public ConfigureTestCase(String name){
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
		UserDatabase userDatabase = (UserDatabase) ConfigurationDatabaseContext.getUserDatabase();
		DomainDatabase domainDatabase = (DomainDatabase) ConfigurationDatabaseContext.getDomainDatabase();
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

		ConfigureTestCase.creatorId = ((User) userList.get(0)).getId();
		ConfigureTestCase.domainId = ((Domain) domainList.get(0)).getId();
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
