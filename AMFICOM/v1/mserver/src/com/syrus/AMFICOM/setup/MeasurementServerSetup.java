package com.syrus.AMFICOM.setup;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.IdentifierGenerator;
import com.syrus.AMFICOM.configuration.User;
import com.syrus.AMFICOM.configuration.Domain;
import com.syrus.AMFICOM.configuration.corba.User_Transferable;
import com.syrus.AMFICOM.configuration.corba.UserSort;
import com.syrus.AMFICOM.configuration.corba.Domain_Transferable;
import com.syrus.AMFICOM.measurement.ParameterType;
import com.syrus.AMFICOM.measurement.MeasurementType;
import com.syrus.AMFICOM.measurement.AnalysisType;
import com.syrus.AMFICOM.measurement.EvaluationType;
import com.syrus.AMFICOM.mserver.DatabaseContextSetup;
import com.syrus.util.Application;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;

public class MeasurementServerSetup {
	public static final String DB_SID = "amficom";
	public static final int DB_CONNECTION_TIMEOUT = 120;

	private MeasurementServerSetup() {
	}

	public static void main(String[] args) {
		Application.init("mserver");
		establishDatabaseConnection();
		DatabaseContextSetup.initDatabaseContext();
		DatabaseContextSetup.initObjectPools();

		//createUser();

		//Identifier userId = new Identifier("Users_9");
		//createDomain(userId);
		checkDomain();

		DatabaseConnection.closeConnection();
	}

	private static void createUser() {
		try {
			Identifier id = IdentifierGenerator.generateIdentifier(ObjectEntities.USER_ENTITY_CODE);
			System.out.println("Id: '" + id.toString() + "', major: " + id.getMajor() + ", majorString: '" + id.getMajorString() + "', minor: " + id.getMinor());
			User user = User.createInstance(id,
																			id,
																			"sys",
																			UserSort.USER_SORT_REGULAR,
																			"sys",
																			"System administrator");
																			
			User user1 = new User((User_Transferable)user.getTransferable());
		}
		catch (Exception e) {
			Log.errorException(e);
		}
	}

	private static void checkUser() {
		try {
			Identifier id = new Identifier("Users_9");
			User user = new User(id);
			System.out.println("login: " + user.getLogin() + ", name: " + user.getName() + ", description: " + user.getDescription());
		}
		catch (Exception e) {
			Log.errorException(e);
		}
	}

	private static void createDomain(Identifier creatorId) {
		try {
			Identifier id = IdentifierGenerator.generateIdentifier(ObjectEntities.DOMAIN_ENTITY_CODE);
			System.out.println("Id: '" + id.toString() + "', major: " + id.getMajor() + ", majorString: '" + id.getMajorString() + "', minor: " + id.getMinor());
			Domain domain = Domain.createInstance(id,
																						creatorId,
																						null,
																						"domain 1",
																						"System domain");
			Domain domain1 = new Domain((Domain_Transferable)domain.getTransferable());
		}
		catch (Exception e) {
			Log.errorException(e);
		}
	}

	private static void checkDomain() {
		try {
			Identifier id = new Identifier("Domain_26");
			Domain domain = new Domain(id);
			System.out.println("creator: " + domain.getCreatorId() + ", domain_id: " + domain.getDomainId() + ", name: " + domain.getName() + ", description: " + domain.getDescription());
		}
		catch (Exception e) {
			Log.errorException(e);
		}
	}

	private static void createServer(Identifier creatorId) {
		try {
			Identifier id = IdentifierGenerator.generateIdentifier(ObjectEntities.SERVER_ENTITY_CODE);
			System.out.println("Id: '" + id.toString() + "', major: " + id.getMajor() + ", majorString: '" + id.getMajorString() + "', minor: " + id.getMinor());
			
		}
		catch (Exception e) {
			Log.errorException(e);
		}
	}

	private static void createParameterTypes() {
		
	}

	private static void createMeasurementTypes() {
		
	}

	private static void createAnalysisTypes() {
		
	}

	private static void createEvaluationTypes() {
		
	}

	private static void establishDatabaseConnection() {
		String dbHostName = ApplicationProperties.getString("DBHostName", Application.getInternetAddress());
		String dbSid = ApplicationProperties.getString("DBSID", DB_SID);
		long dbConnTimeout = ApplicationProperties.getInt("DBConnectionTimeout", DB_CONNECTION_TIMEOUT)*1000;
		try {
			DatabaseConnection.establishConnection(dbHostName, dbSid, dbConnTimeout);
		}
		catch (Exception e) {
			Log.errorException(e);
			System.exit(-1);
		}
	}
}
