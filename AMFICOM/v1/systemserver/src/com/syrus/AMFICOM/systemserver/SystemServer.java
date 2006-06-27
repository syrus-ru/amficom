/*-
 * $Id: SystemServer.java,v 1.1.1.1 2006/06/27 10:30:18 cvsadmin Exp $
 *
 * Copyright © 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.systemserver;

import static com.syrus.AMFICOM.administration.ServerWrapper.EVENT_SERVER_SERVICE_NAME;
import static com.syrus.AMFICOM.administration.ServerWrapper.IDENTIFIER_GENERATOR_SERVER_SERVICE_NAME;
import static com.syrus.AMFICOM.administration.ServerWrapper.KEY_EVENT_SERVER_SERVICE_NAME;
import static com.syrus.AMFICOM.administration.ServerWrapper.KEY_IDENTIFIER_GENERATOR_SERVER_SERVICE_NAME;
import static com.syrus.AMFICOM.administration.ServerWrapper.KEY_LOGIN_SERVER_SERVICE_NAME;
import static com.syrus.AMFICOM.administration.ServerWrapper.KEY_MAP_SERVER_SERVICE_NAME;
import static com.syrus.AMFICOM.administration.ServerWrapper.KEY_STORABLE_OBJECT_SERVER_SERVICE_NAME;
import static com.syrus.AMFICOM.administration.ServerWrapper.LOGIN_SERVER_SERVICE_NAME;
import static com.syrus.AMFICOM.administration.ServerWrapper.MAP_SERVER_SERVICE_NAME;
import static com.syrus.AMFICOM.administration.ServerWrapper.STORABLE_OBJECT_SERVER_SERVICE_NAME;
import static com.syrus.AMFICOM.general.ObjectEntities.SERVER_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SYSTEMUSER_CODE;

import com.syrus.AMFICOM.administration.Server;
import com.syrus.AMFICOM.administration.SystemUser;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CORBAServer;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.systemserver.SystemServerSessionEnvironment.SystemServerLoginRestorer;
import com.syrus.AMFICOM.systemserver.corba.EventServerPOATie;
import com.syrus.AMFICOM.systemserver.corba.IdentifierGeneratorServerPOATie;
import com.syrus.AMFICOM.systemserver.corba.LoginServerPOATie;
import com.syrus.AMFICOM.systemserver.corba.MapServerPOATie;
import com.syrus.AMFICOM.systemserver.corba.StorableObjectServerPOATie;
import com.syrus.util.Application;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;

/**
 * @version $Revision: 1.1.1.1 $, $Date: 2006/06/27 10:30:18 $
 * @author $Author: cvsadmin $
 * @author Tashoyan Arseniy Feliksovich
 * @module systemserver
 */
final class SystemServer {
	private static final String APPLICATION_NAME = "systemserver";

	/*-********************************************************************
	 * Ключи.                                                             *
	 **********************************************************************/

	private static final String KEY_DB_HOST_NAME = "DBHostName";
	private static final String KEY_DB_SID = "DBSID";
	private static final String KEY_DB_CONNECTION_TIMEOUT = "DBConnectionTimeout";
	private static final String KEY_DB_LOGIN_NAME = "DBLoginName";
	private static final String KEY_SERVER_ID = "ServerID";

	/*-********************************************************************
	 * Значения по умолчанию.                                             *
	 **********************************************************************/

	private static final String DB_SID = "amficom";
	private static final int DB_CONNECTION_TIMEOUT = 120; //sec
	private static final String DB_LOGIN_NAME = "amficom";
	private static final String SERVER_ID = "Server_1";

	private static SystemServer instance;

	/**
	 * Идентификатор сервера.
	 */
	private final Identifier serverId;


	private SystemServer() throws ApplicationException {
		/*
		 * Восстановить идентификатор данного сервера.
		 */
		this.serverId = Identifier.valueOf(ApplicationProperties.getString(KEY_SERVER_ID, SERVER_ID));

		/*
		 * Достать из базы данных сведения о сервере.
		 */
		final StorableObjectDatabase<Server> serverDatabase = DatabaseContext.getDatabase(SERVER_CODE);
		final Server server = serverDatabase.retrieveForId(this.serverId);
		final Identifier systemUserId = server.getSystemUserId();

		final StorableObjectDatabase<SystemUser> systemUserDatabase = DatabaseContext.getDatabase(SYSTEMUSER_CODE);
		final SystemUser systemUser = systemUserDatabase.retrieveForId(systemUserId);
		final String login = systemUser.getLogin();
		final String password = APPLICATION_NAME;

		/*
		 * Создать и запустить обработчик пользовательских сессий.
		 */
		LoginProcessor.createInstance();
		LoginProcessor.getInstance().start();

		/*
		 * Создать окружение пользовательской сессии.
		 */
		final SystemServerLoginRestorer systemServerLoginRestorer = new SystemServerLoginRestorer() {

			public boolean restoreLogin() {
				return true;
			}

			public String getLogin() {
				return login;
			}

			public String getPassword() {
				return password;
			}
		};
		SystemServerSessionEnvironment.createInstance(server.getHostName(), systemServerLoginRestorer);

		/*
		 * Создать корбовые службы.
		 */
		final String loginServerServiceName = ApplicationProperties.getString(KEY_LOGIN_SERVER_SERVICE_NAME,
				LOGIN_SERVER_SERVICE_NAME);
		final String eventServerServiceName = ApplicationProperties.getString(KEY_EVENT_SERVER_SERVICE_NAME,
				EVENT_SERVER_SERVICE_NAME);
		final String identifierGeneratorServerServiceName = ApplicationProperties.getString(KEY_IDENTIFIER_GENERATOR_SERVER_SERVICE_NAME,
				IDENTIFIER_GENERATOR_SERVER_SERVICE_NAME);
		final String mapServerServiceName = ApplicationProperties.getString(KEY_MAP_SERVER_SERVICE_NAME,
				MAP_SERVER_SERVICE_NAME);
		final String storableObjectServerServiceName = ApplicationProperties.getString(KEY_STORABLE_OBJECT_SERVER_SERVICE_NAME,
				STORABLE_OBJECT_SERVER_SERVICE_NAME);
		final CORBAServer corbaServer = SystemServerSessionEnvironment.getInstance().getConnectionManager().getCORBAServer();
		corbaServer.activateServant(new LoginServerPOATie(new LoginServerImpl(), corbaServer.getPoa()),
				loginServerServiceName);
		corbaServer.activateServant(new EventServerPOATie(new EventServerImpl(), corbaServer.getPoa()),
				eventServerServiceName);
		corbaServer.activateServant(new IdentifierGeneratorServerPOATie(new IdentifierGeneratorServerImpl(), corbaServer.getPoa()),
				identifierGeneratorServerServiceName);
		corbaServer.activateServant(new MapServerPOATie(new MapServerImpl(), corbaServer.getPoa()),
				mapServerServiceName);
		corbaServer.activateServant(new StorableObjectServerPOATie(new StorableObjectServerImpl(), corbaServer.getPoa()),
				storableObjectServerServiceName);
		corbaServer.printNamingContext();

		/*
		 * Войти в систему.
		 */
		SystemServerSessionEnvironment.getInstance().login(login, password, server.getDomainId());
	}


	public static void main(String[] args) {
		Application.init(APPLICATION_NAME);

		establishDatabaseConnection();

		DatabaseContextSetup.initDatabaseContext();

		try {
			instance = new SystemServer();
		} catch (ApplicationException ae) {
			Log.errorMessage(ae);
			Log.errorMessage("Cannot start -- exiting");
			System.exit(0);
		}

		Runtime.getRuntime().addShutdownHook(new Thread("SystemServer -- shutdown hook") {
			@Override
			public void run() {
				shutdown();
			}
		});

	}

	private static void establishDatabaseConnection() {
		final String dbHostName = ApplicationProperties.getString(KEY_DB_HOST_NAME, Application.getInternetAddress());
		final String dbSid = ApplicationProperties.getString(KEY_DB_SID, DB_SID);
		final long dbConnTimeout = ApplicationProperties.getInt(KEY_DB_CONNECTION_TIMEOUT, DB_CONNECTION_TIMEOUT) * 1000;
		final String dbLoginName = ApplicationProperties.getString(KEY_DB_LOGIN_NAME, DB_LOGIN_NAME);
		try {
			DatabaseConnection.establishConnection(dbHostName, dbSid, dbConnTimeout, dbLoginName);
		} catch (Exception e) {
			Log.errorMessage(e);
			System.exit(0);
		}
	}

	static synchronized void shutdown() {
		//DatabaseConnection.closeConnection();
	}

}
