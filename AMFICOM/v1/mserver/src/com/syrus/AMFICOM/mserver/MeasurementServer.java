/*-
 * $Id: MeasurementServer.java,v 1.95.2.5 2006/03/30 12:10:58 arseniy Exp $
 *
 * Copyright © 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.mserver;

import static com.syrus.AMFICOM.administration.ServerProcessWrapper.KEY_MSERVER_PROCESS_CODENAME;
import static com.syrus.AMFICOM.administration.ServerProcessWrapper.MSERVER_PROCESS_CODENAME;
import static com.syrus.AMFICOM.general.ErrorMessages.ILLEGAL_ENTITY_CODE;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_EMPTY_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ObjectEntities.MCM_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SERVERPROCESS_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SERVER_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SYSTEMUSER_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.TEST_CODE;
import static com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.IdlErrorCode._ERROR_NOT_LOGGED_IN;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort.AND;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort.OR;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort.OPERATION_EQUALS;
import static com.syrus.AMFICOM.measurement.Test.TestStatus.TEST_STATUS_ABORTED;
import static com.syrus.AMFICOM.measurement.Test.TestStatus.TEST_STATUS_NEW;
import static com.syrus.AMFICOM.measurement.Test.TestStatus.TEST_STATUS_STOPPING;
import static com.syrus.AMFICOM.measurement.TestWrapper.COLUMN_STATUS;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.administration.MCM;
import com.syrus.AMFICOM.administration.MCMDatabase;
import com.syrus.AMFICOM.administration.Server;
import com.syrus.AMFICOM.administration.ServerProcess;
import com.syrus.AMFICOM.administration.ServerProcessDatabase;
import com.syrus.AMFICOM.administration.SystemUser;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LoginException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.LoginRestorer;
import com.syrus.AMFICOM.general.SleepButWorkThread;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.Test.TestStatus;
import com.syrus.AMFICOM.security.corba.IdlSessionKey;
import com.syrus.util.Application;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;

/**
 * @version $Revision: 1.95.2.5 $, $Date: 2006/03/30 12:10:58 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module mserver
 */
final class MeasurementServer extends SleepButWorkThread {
	private static final String APPLICATION_NAME = "mserver";


	/*-********************************************************************
	 * Keys.                                                              *
	 **********************************************************************/

	private static final String KEY_SERVER_ID = "ServerID";
	private static final String KEY_DB_HOST_NAME = "DBHostName";
	private static final String KEY_DB_SID = "DBSID";
	private static final String KEY_DB_CONNECTION_TIMEOUT = "DBConnectionTimeout";
	private static final String KEY_DB_LOGIN_NAME = "DBLoginName";
	private static final String KEY_TICK_TIME = "TickTime";
	private static final String KEY_MAX_FALLS = "MaxFalls";


	/*-********************************************************************
	 * Default values.                                                    *
	 **********************************************************************/

	private static final String SERVER_ID = "Server_1";
	private static final String DB_SID = "amficom";
	private static final int DB_CONNECTION_TIMEOUT = 120;	//sec
	private static final String DB_LOGIN_NAME = "amficom";
	private static final int TICK_TIME = 5;	//sec


	private static final String PASSWORD = "MServer";


	private static final int FALL_CODE_MCM_DISCONNECTED = 1;


	private static class MServerLoginRestorer implements LoginRestorer {

		public boolean restoreLogin() {
			return true;
		}

		public String getLogin() {
			return getInstance().getSystemUserLogin();
		}

		public String getPassword() {
			return PASSWORD;
		}

	}


	/**
	 * Единственный в пределах приложения объект данного класса. Таким образом,
	 * {@link MeasurementServer} является "Одиночкой" (Singletone).
	 */
	private static MeasurementServer instance;

	/**
	 * Имя пользователя, от имени которого данный процесс работает в системе.
	 * Нужно для восстановления сеанса средствами
	 * {@link com.syrus.AMFICOM.general.LoginRestorer}. См.
	 * {@link MServerLoginRestorer#getLogin()}.
	 */
	private final String systemUserLogin;

	/**
	 * Набор идентификаторов измерительных модулей, работающих с данным
	 * сервером.
	 */
	private final Set<Identifier> mcmIds;

	/**
	 * Набор идентификаторов заданий, которые уже подгружены из БД вызовом
	 * {@link #searchNewAndStoppingTests()}. Это поле нужно только для того,
	 * чтобы в методе {@link #searchNewAndStoppingTests()} не создавать каждый
	 * раз новый объект.
	 */
	private final Set<Identifier> alreadyLoadedTestIds;

	/**
	 * Условие для поиска новых и останавливаемых заданий. Создаётся в первый
	 * раз при запуске процесса. Создаётся методом
	 * {@link #createTestLoadCondition()}. Используется в методе
	 * {@link #searchNewAndStoppingTests()}. Изначально этот объект создаётся в
	 * конструкторе {@link #MeasurementServer(String, Set)}. При изменении набора
	 * измерительных модулей {@link #mcmIds} он должен быть создан заново -- см.
	 * методы {@link #addMCM(Identifier)} и {@link #removeMCM(Identifier)}.
	 */
	private StorableObjectCondition testLoadCondition;

	/**
	 * Карта новых заданий, разложенных по соответствующим измерительным
	 * модулям. Ключ - идентификатор измерительного модуля, величина - набор
	 * идентификаторов заданий, предназначенных для отправки на этот модуль. Все
	 * эти задания должны иметь состояние НОВЫЙ. Эта карта заполняется в методе
	 * {@link #searchNewAndStoppingTests()}, а затем опустошается в методе
	 * {@link #sendTestsToMCMs()}.
	 */
	private final Map<Identifier, Set<Identifier>> mcmNewTestMap;

	/**
	 * Карта останавливаемых заданий, разложенных по соответствующим
	 * измерительным модулям. Ключ - идентификатор измерительного модуля,
	 * величина - набор идентификаторов заданий, предназначенных для отправки на
	 * этот модуль. Все эти задания должны иметь состояние ОСТАНАВЛИВАЕТСЯ. Эта
	 * карта заполняется в методе {@link #searchNewAndStoppingTests()}, а затем
	 * опустошается в методе {@link #sendTestsToMCMs()}.
	 */
	private final Map<Identifier, Set<Identifier>> mcmStoppingTestMap;

	/**
	 * Знак работы главного цикла {@link #run()}.
	 */
	private boolean running;

	/**
	 * Восстановитель сессии. См. {@link #getLoginRestorer()}.
	 */
	private static MServerLoginRestorer loginRestorer;

	private final Set<Identifier> disconnectedMCMIds;


	public static void main(String[] args) {
		Application.init(APPLICATION_NAME);

		/* Поготовка к работе. */
		try {
			startup();
		} catch (ApplicationException ae) {
			Log.errorMessage(ae);
			Log.errorMessage("Cannot start -- exiting");
			System.exit(0);
		}

		/* Запустить главный поток. */
		instance.start();

		/* Обеспечить правильное выключение в случае прерывания приложения. */
		Runtime.getRuntime().addShutdownHook(new Thread("MeasurementServer -- shutdown hook") {
			@Override
			public void run() {
				getInstance().shutdown();
			}
		});
	}

	/**
	 * Возвращает ссылку на объект-одиночку, представляющий данный процесс.
	 * 
	 * @return {@link instance}
	 */
	static MeasurementServer getInstance() {
		return instance;
	}

	/**
	 * Выполнить все необходимые приготовления к работе. Сюда входят:
	 * установление соединения с базой данных, создание контекста драйверов базы
	 * данных, извлечение из БД сведений о данном модуле, создание сеанса CORBA
	 * и вход в систему AMFICOM. Также этот метод создаёт единственный в
	 * пределах приложения экземпляр класса {@link MeasurementServer}.
	 * 
	 * @throws ApplicationException
	 */
	private static void startup() throws ApplicationException {

		/* Установить соединение с базой данных. */
		establishDatabaseConnection();

		/* Создать контекст драйверов базы данных. */
		DatabaseContextSetup.initDatabaseContext();

		/*
		 * Получить из БД данные о самом процессе, его сервере и пользователе,
		 * от имени которого он работает в системе, а также о измерительных
		 * модулях, подключенных к этому серверу.
		 */
		final Identifier serverId = new Identifier(ApplicationProperties.getString(KEY_SERVER_ID, SERVER_ID));
		final String processCodename = ApplicationProperties.getString(KEY_MSERVER_PROCESS_CODENAME, MSERVER_PROCESS_CODENAME);

		final StorableObjectDatabase<Server> serverDatabase = DatabaseContext.getDatabase(SERVER_CODE);
		final Server server = serverDatabase.retrieveForId(serverId);

		final StorableObjectDatabase<ServerProcess> serverProcessDatabase = DatabaseContext.getDatabase(SERVERPROCESS_CODE);
		final ServerProcess serverProcess = ((ServerProcessDatabase) serverProcessDatabase).retrieveForServerAndCodename(serverId,
				processCodename);

		final StorableObjectDatabase<SystemUser> systemUserDatabase = DatabaseContext.getDatabase(SYSTEMUSER_CODE);
		final SystemUser systemUser = systemUserDatabase.retrieveForId(serverProcess.getUserId());

		final StorableObjectDatabase<MCM> mcmDatabase = DatabaseContext.getDatabase(MCM_CODE);
		final Set<Identifier> mcmIds = Identifier.createIdentifiers(((MCMDatabase) mcmDatabase).retrieveForServer(serverId));

		/* Создать окружение сеанса. */
		MServerSessionEnvironment.createInstance(server.getHostName(), mcmIds, processCodename);

		/* Войти в систему. */
		final String systemUserLogin = systemUser.getLogin();
		try {
			MServerSessionEnvironment.getInstance().login(systemUserLogin, PASSWORD, server.getDomainId());
		} catch (final LoginException le) {
			Log.errorMessage(le);
		}

		/* Создать объект главного класса. */
		instance = new MeasurementServer(systemUserLogin, mcmIds);
	}

	/**
	 * Установить соединение с базой данных.
	 */
	private static void establishDatabaseConnection() {
		final String dbHostName = ApplicationProperties.getString(KEY_DB_HOST_NAME, Application.getInternetAddress());
		final String dbSid = ApplicationProperties.getString(KEY_DB_SID, DB_SID);
		final long dbConnTimeout = ApplicationProperties.getInt(KEY_DB_CONNECTION_TIMEOUT, DB_CONNECTION_TIMEOUT)*1000;
		final String dbLoginName = ApplicationProperties.getString(KEY_DB_LOGIN_NAME, DB_LOGIN_NAME);
		try {
			DatabaseConnection.establishConnection(dbHostName, dbSid, dbConnTimeout, dbLoginName);
		} catch (Exception e) {
			Log.errorMessage(e);
			System.exit(0);
		}
	}


	/**
	 * Закрытый конструктор. Предназначен для создания "одиночки". Создаёт и
	 * подготавливает все объекты, необходимые для работы.
	 * 
	 * @param systemUserLogin
	 *        Имя пользователя, под которым данный модуль должен работать в
	 *        системе.
	 * @param mcmIds
	 *        Набор Идентификаторов измерительных модулей, которые работают с
	 *        данным сервером.
	 */
	private MeasurementServer(final String systemUserLogin, final Set<Identifier> mcmIds) {
		super(ApplicationProperties.getInt(KEY_TICK_TIME, TICK_TIME) * 1000, ApplicationProperties.getInt(KEY_MAX_FALLS, MAX_FALLS));
		super.setName("MeasurementServer");

		this.systemUserLogin = systemUserLogin;
		this.mcmIds = mcmIds;

		this.alreadyLoadedTestIds = new HashSet<Identifier>();
		this.createTestLoadCondition();

		this.mcmNewTestMap = new HashMap<Identifier, Set<Identifier>>();
		this.mcmStoppingTestMap = new HashMap<Identifier, Set<Identifier>>();

		this.disconnectedMCMIds = new HashSet<Identifier>();
	}

	/**
	 * Создать условие для подгрузки заданий в состояниях НОВЫЙ и
	 * ОСТАНАВЛИВАЕТСЯ, для измерительных модулей данного сервера.
	 */
	private void createTestLoadCondition() {
		final LinkedIdsCondition mcmTestCondition = new LinkedIdsCondition(this.getMCMIds(), TEST_CODE);
		final TypicalCondition newTestCondition = new TypicalCondition(TEST_STATUS_NEW,
				OPERATION_EQUALS,
				TEST_CODE,
				COLUMN_STATUS);
		final TypicalCondition stoppingTestCondition = new TypicalCondition(TEST_STATUS_STOPPING,
				OPERATION_EQUALS,
				TEST_CODE,
				COLUMN_STATUS);
		this.testLoadCondition = new CompoundCondition(mcmTestCondition,
				AND,
				new CompoundCondition(newTestCondition, OR, stoppingTestCondition));
	}

	/**
	 * Получить имя пользователя, под которым данный модуль работает в системе.
	 * 
	 * @return Имя пользователя
	 */
	String getSystemUserLogin() {
		return this.systemUserLogin;
	}

	/**
	 * Получить набор идентификаторов измерительных модулей, работающих с данным
	 * сервером.
	 * 
	 * @return Неизменяемый набор идентификаторов.
	 */
	Set<Identifier> getMCMIds() {
		return Collections.unmodifiableSet(this.mcmIds);
	}

	/**
	 * Проверить, зарегистрирован ли измерительный модуль с идентификатором
	 * <code>mcmId</code> для работы с данным сервером.
	 * 
	 * @param mcmId
	 *        Идентификатор измерительного модуля.
	 * @return <code>true</code>, если измерительный модуль зарегистрирован
	 *         для работы с данным сервером.
	 */
	boolean hasMCM(final Identifier mcmId) {
		assert mcmId != null : NON_NULL_EXPECTED;
		assert mcmId.getMajor() == MCM_CODE : ILLEGAL_ENTITY_CODE;

		final boolean hasMCM = this.mcmIds.contains(mcmId);
		if (!hasMCM) {
			/* Дополнительная проверка целостности данных */
			assert !this.mcmNewTestMap.containsKey(mcmId);
			assert !this.mcmStoppingTestMap.containsKey(mcmId);
		}
		return hasMCM;
	}

	/**
	 * Проверить, нет ли на измерительном модуле с идентификатором
	 * <code>mcmId</code> заданий в состоянии НОВЫЙ.
	 * 
	 * @param mcmId
	 *        Идентификатор измерительного модуля.
	 * @return <code>true</code>, если на измерительном модуле есть новые
	 *         задания. <code>false</code>, если таких заданий на
	 *         измерительном модуле нет, либо если измерительный модуль с таким
	 *         идентификатором не зарегистрирован для работы с данным сервером.
	 */
	boolean hasNewTestsOnMCM(final Identifier mcmId) {
		if (!this.hasMCM(mcmId)) {
			return false;
		}
		return this.mcmNewTestMap.containsKey(mcmId);
	}

	/**
	 * Проверить, нет ли на измерительном модуле с идентификатором
	 * <code>mcmId</code> заданий в состоянии ОСТАНАВЛИВАЕТСЯ.
	 * 
	 * @param mcmId
	 *        Идентификатор измерительного модуля.
	 * @return <code>true</code>, если на измерительном модуле есть
	 *         останавливаемые задания. <code>false</code>, если таких
	 *         заданий на измерительном модуле нет, либо если измерительный
	 *         модуль с таким идентификатором не зарегистрирован для работы с
	 *         данным сервером.
	 */
	boolean hasStoppingTestsOnMCM(final Identifier mcmId) {
		if (!this.hasMCM(mcmId)) {
			return false;
		}
		return this.mcmStoppingTestMap.containsKey(mcmId);
	}

	/**
	 * Добавить измерительный модуль с идентификатором <code>mcmId</code> в
	 * спискок зарегистрированных для работы с данным сервером. Если
	 * измерительный модуль с таким идентификатором найден в списке
	 * зарегистрированных, то метод сразу возвращается. Условие для подгрузки
	 * заданий {@link #testLoadCondition} пересоздаётся заново.
	 * 
	 * @param mcmId
	 *        Идентификатор измерительного модуля.
	 */
	void addMCM(final Identifier mcmId) {
		if (this.hasMCM(mcmId)) {
			Log.errorMessage("MCM already added");
			return;
		}

		this.mcmIds.add(mcmId);

		this.createTestLoadCondition();
	}

	/**
	 * Удалить измерительный модуль с идентификатором <code>mcmId</code> из
	 * списка зарегистрированных для работы с данным сервером. Если
	 * измерительный модуль с таким идентификатором не найден, то метод сразу
	 * возвращается. Если он найден и на нём есть либо новые, либо
	 * останавливаемые задания, кидается исключение выполнения
	 * {@link IllegalStateException}. В остальных случаях, идентификатор модуля
	 * <code>mcmId</code> удаляется из набора {@link #mcmIds} и из карт
	 * {@link #mcmNewTestMap} и {@link #mcmStoppingTestMap}. Условие для
	 * подгрузки заданий {@link #testLoadCondition} пересоздаётся заново.
	 * 
	 * @param mcmId
	 *        Идентификатор измерительного модуля.
	 */
	void removeMCM(final Identifier mcmId) {
		if (!this.hasMCM(mcmId)) {
			Log.errorMessage("MCM not found");
			return;
		}
		if (this.mcmNewTestMap.containsKey(mcmId)) {
			throw new IllegalStateException("Cannot remove MCM with new tests on it");
		}
		if (this.mcmStoppingTestMap.containsKey(mcmId)) {
			throw new IllegalStateException("Cannot remove MCM with stopping tests on it");
		}

		this.mcmIds.remove(mcmId);
		this.mcmNewTestMap.remove(mcmId);
		this.mcmStoppingTestMap.remove(mcmId);

		this.createTestLoadCondition();
	}

	/**
	 * Получить ссылку на восстановитель сессии. Объект создаётся только один
	 * раз при первом обращении.
	 * 
	 * @return {@link #loginRestorer}
	 */
	static MServerLoginRestorer getLoginRestorer() {
		if (loginRestorer == null) {
			loginRestorer = new MServerLoginRestorer();
		}
		return loginRestorer;
	}

	/**
	 * Главный цикл. В настоящее время измерительный сервер ищет задания для
	 * отправки на измерительные модули только в своей БД. Возможно, в будущем
	 * придётся это поведение изменить.
	 */
	@Override
	public void run() {
		while (this.running) {
			this.searchNewAndStoppingTests();

			this.sendTestIdsToMCMs();

			try {
				sleep(super.initialTimeToSleep);
			} catch (InterruptedException ie) {
				Log.errorMessage(ie);
			}
		}
	}

	/**
	 * Выполнить поиск по БД заданий в состояниях НОВЫЙ или ОСТАНАВЛИВАЕТСЯ.
	 * Задания в состоянии НОВЫЙ добавляются в {@link #mcmNewTestMap};
	 * задания в состоянии ОСТАНАВЛИВАЕТСЯ - в {@link #mcmStoppingTestMap}. В
	 * случае ошибки подгрузки метод сразу же возвращается.
	 */
	private void searchNewAndStoppingTests() {
		this.alreadyLoadedTestIds.clear();
		synchronized (this.mcmNewTestMap) {
			for (final Set<Identifier> testIds : this.mcmNewTestMap.values()) {
				Identifier.addToIdentifiers(this.alreadyLoadedTestIds, testIds);
			}
		}
		synchronized (this.mcmStoppingTestMap) {
			for (final Set<Identifier> testIds : this.mcmStoppingTestMap.values()) {
				Identifier.addToIdentifiers(this.alreadyLoadedTestIds, testIds);
			}
		}

		final Set<Test> tests;
		try {
			//StorableObjectPool.refresh(alreadyLoadedTests);//Зачем?
			tests = StorableObjectPool.getStorableObjectsButIdsByCondition(this.alreadyLoadedTestIds, this.testLoadCondition, true);
		} catch (ApplicationException ae) {
			Log.errorMessage(ae);
			return;
		} finally {
			this.alreadyLoadedTestIds.clear();
		}
		
		for (final Test test : tests) {
			final TestStatus testStatus = test.getStatus();
			switch (testStatus) {
				case TEST_STATUS_NEW:
					this.addToMCMNewTestMap(test);
					break;
				case TEST_STATUS_STOPPING:
					this.addToMCMStoppingTestMap(test);
					break;
				default:
					Log.errorMessage("Illegal status: " + testStatus + " of test '" + test.getId() + "'");
			}
		}
	}

	/**
	 * Добавить задание в карту новых заданий. Состояние задания
	 * <code>test</code> должно быть НОВЫЙ.
	 * 
	 * @param test
	 *        Задание в состоянии НОВЫЙ.
	 */
	private void addToMCMNewTestMap(final Test test) {
		this.addToMCMTestMap(test, this.mcmNewTestMap);
	}

	/**
	 * Добавить задание в карту останавливаемых заданий. Состояние задания
	 * <code>test</code> должно быть ОСТАНАВЛИВАЕТСЯ.
	 * 
	 * @param test
	 *        Задание в состоянии ОСТАНАВЛИВАЕТСЯ.
	 */
	private void addToMCMStoppingTestMap(final Test test) {
		this.addToMCMTestMap(test, this.mcmStoppingTestMap);
	}

	/**
	 * Вспомогательный метод для помещения задания в карту либо новых, либо
	 * останавливаемых заданий. Имеет смысл использовать только из методов
	 * {@link #addToMCMNewTestMap(Test)} и
	 * {@link #addToMCMStoppingTestMap(Test)}. Для задания <code>test</code>
	 * вычисляется идентификатор измерительного модуля. Если этот модуль
	 * зарегистрирован для данного сервера, то задание помещается в предложенную
	 * карту <code>mcmTestMap</code>. В противном случае метод возвращается.
	 * 
	 * @param test
	 *        Задание в состоянии либо НОВЫЙ, либо ОСТАНАВЛИВАЕТСЯ.
	 * @param mcmTestMap
	 *        Либо {@link #mcmNewTestMap}, либо {@link #mcmStoppingTestMap}.
	 */
	private void addToMCMTestMap(final Test test, final Map<Identifier, Set<Identifier>> mcmTestMap) {
		assert test != null : NON_NULL_EXPECTED;
		assert mcmTestMap != null : NON_NULL_EXPECTED;

		final Identifier testId = test.getId();
		final Identifier mcmId;
		try {
			mcmId = test.getMCMId();
		} catch (ApplicationException ae) {
			Log.errorMessage(ae);
			return;
		}

		Set<Identifier> testIds = mcmTestMap.get(mcmId);
		if (testIds == null) {
			if (this.hasMCM(mcmId)) {
				testIds = new HashSet<Identifier>();
				mcmTestMap.put(mcmId, testIds);
			} else {
				Log.errorMessage("MCM '" + mcmId + "' not registered for this server");
				return;
			}
		}
		testIds.add(testId);
	}

	/**
	 * Отправить на измерительные модули идентификаторы новых и останавливаемых
	 * заданий. Перебор идентификаторов измерительных модулей проводится сначала
	 * по карте новых заданий {@link #mcmNewTestMap}, а затем, дополнительно,
	 * по карте останавливаемых заданий {@link #mcmStoppingTestMap}.
	 */
	private void sendTestIdsToMCMs() {
		synchronized (this.mcmNewTestMap) {
			for (final Identifier mcmId : this.mcmNewTestMap.keySet()) {
				final Set<Identifier> newTestIds = this.mcmNewTestMap.get(mcmId);
				final Set<Identifier> stoppingTestIds = this.mcmStoppingTestMap.get(mcmId);
				
				if (newTestIds != null && !newTestIds.isEmpty()) {
					Log.debugMessage("Starting on MCM '" + mcmId + "' " + newTestIds.size() + " test(s): " + newTestIds,
							Log.DEBUGLEVEL08);
					this.startOrStopTestsOnMCM(mcmId, newTestIds, true);
				}
				if (stoppingTestIds != null && !stoppingTestIds.isEmpty()) {
					Log.debugMessage("Stopping on MCM '"
							+ mcmId + "' " + stoppingTestIds.size() + " test(s): " + stoppingTestIds, Log.DEBUGLEVEL08);
					this.startOrStopTestsOnMCM(mcmId, stoppingTestIds, false);
				}
			}
		}

		synchronized (this.mcmStoppingTestMap) {
			for (final Identifier mcmId : this.mcmStoppingTestMap.keySet()) {
				final Set<Identifier> stoppingTestIds = this.mcmStoppingTestMap.get(mcmId);
				if (stoppingTestIds != null && !stoppingTestIds.isEmpty()) {
					Log.debugMessage("Stopping on MCM '"
							+ mcmId + "' " + stoppingTestIds.size() + " test(s): " + stoppingTestIds, Log.DEBUGLEVEL08);
					this.startOrStopTestsOnMCM(mcmId, stoppingTestIds, false);
				}
			}
		}

	}

	/**
	 * В зависимости от третьего аргумента, либо начать выполнение заданий на
	 * измерительном модуле, либо остановить.
	 * 
	 * @param mcmId
	 *        Идентификатор измерительного модуля.
	 * @param testIds
	 *        Набор идентификаторов заданий для запуска/остановки.
	 * @param start
	 *        если <code>true</code> - начать, если <code>false</code> -
	 *        остановить.
	 */
	void startOrStopTestsOnMCM(final Identifier mcmId, final Set<Identifier> testIds, final boolean start) {
		assert mcmId != null : NON_NULL_EXPECTED;
		assert testIds != null : NON_NULL_EXPECTED;
		assert !testIds.isEmpty() : NON_EMPTY_EXPECTED;
		assert mcmId.getMajor() == MCM_CODE : ILLEGAL_ENTITY_CODE;
		assert StorableObject.getEntityCodeOfIdentifiables(testIds) == TEST_CODE : ILLEGAL_ENTITY_CODE;

		final MServerServantManager servantManager = MServerSessionEnvironment.getInstance().getMServerServantManager();
		final IdlSessionKey idlSessionKey = LoginManager.getSessionKey().getIdlTransferable();
		try {
			final com.syrus.AMFICOM.mcm.corba.MCM corbaMCMRef = servantManager.getVerifiedMCMReference(mcmId);
			if (start) {
				corbaMCMRef.startTests(Identifier.createTransferables(testIds), idlSessionKey);
			} else {
				corbaMCMRef.stopTests(Identifier.createTransferables(testIds), idlSessionKey);
			}
		} catch (CommunicationException ce) {
			Log.errorMessage(ce);
			super.fallCode = FALL_CODE_MCM_DISCONNECTED;
			this.disconnectedMCMIds.add(mcmId);
			super.sleepCauseOfFall();
		} catch (AMFICOMRemoteException are) {
			if (are.errorCode.value() == _ERROR_NOT_LOGGED_IN) {
				try {
					LoginManager.restoreLogin();
				} catch (ApplicationException ae) {
					Log.errorMessage(ae);
				}
			}
			Log.errorMessage("Cannot transmit: " + are.message + "; sleeping cause of fall");
			super.fallCode = FALL_CODE_MCM_DISCONNECTED;
			this.disconnectedMCMIds.add(mcmId);
			super.sleepCauseOfFall();
		}
	}

	/**
	 * @inheritDoc
	 */
	@Override
	protected void processFall() {
		switch (super.fallCode) {
			case FALL_CODE_NO_ERROR:
				break;
			case FALL_CODE_MCM_DISCONNECTED:
				this.abortDisconnectedMCMTests();
				break;
			default:
				Log.errorMessage("Unknown error code: " + super.fallCode);
		}
	}

	/**
	 * Прервать задания для измерительных модулей из списка
	 * {@link #disconnectedMCMIds}. Удаляются идентификаторы заданий только из
	 * карты новых тестов {@link #mcmNewTestMap}, и только эти задания
	 * переводятся в состояние ПЕРВАН. Карта останавливаемых заданий не
	 * затрагивается, поскольку прервать уже выполняющееся на измерительном
	 * модуле задание можно потом, когда связь с модулем восстановится.
	 * Синхронизация на поле {@link #disconnectedMCMIds} не нужна, т. к. все
	 * обращения к этому полю происходят из одного потока.
	 */
	private void abortDisconnectedMCMTests() {
		if (!this.disconnectedMCMIds.isEmpty()) {
			for(final Identifier mcmId : this.disconnectedMCMIds) {
				final Set<Identifier> newTestIds = this.mcmNewTestMap.get(mcmId);
				if (newTestIds == null || newTestIds.isEmpty()) {
					continue;
				}

				final Set<Test> tests;
				try {
					tests = StorableObjectPool.getStorableObjects(newTestIds, true);
				} catch (ApplicationException ae) {
					Log.errorMessage(ae);
					continue;
				}
				for (final Test test : tests) {
					if (test.getStatus() != TEST_STATUS_ABORTED) {
						test.setStatus(TEST_STATUS_ABORTED);
					}
				}

				try {
					StorableObjectPool.flush(tests, LoginManager.getUserId(), false);
				} catch (ApplicationException ae) {
					Log.errorMessage(ae);
				}
				newTestIds.clear();
			}

			this.disconnectedMCMIds.clear();
		} else {
			Log.errorMessage("Nothing to abort");
		}
	}

	/**
	 * Выключение главного цикла.
	 */
	void shutdown() {
		this.running = false;
		DatabaseConnection.closeConnection();
	}

}
