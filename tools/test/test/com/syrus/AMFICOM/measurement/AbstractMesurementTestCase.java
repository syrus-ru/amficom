/*
 * $Id: AbstractMesurementTestCase.java,v 1.1 2004/08/16 14:23:50 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package test.com.syrus.AMFICOM.measurement;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.mserver.DatabaseContextSetup;
import com.syrus.util.Application;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.database.DatabaseConnection;


/**
 * @version $Revision: 1.1 $, $Date: 2004/08/16 14:23:50 $
 * @author $Author: bob $
 * @module tools
 */
public abstract class AbstractMesurementTestCase extends TestCase{

	public static final int		DB_CONNECTION_TIMEOUT	= 120;

	public static final String	DB_SID					= "amficom";
	
	public static Identifier domainId;

	protected static Identifier	creatorId;	
	
	public AbstractMesurementTestCase(String name){
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
		AbstractMesurementTestCase.creatorId = new Identifier("Users_9");
		AbstractMesurementTestCase.domainId = new Identifier("Domain_26");
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
