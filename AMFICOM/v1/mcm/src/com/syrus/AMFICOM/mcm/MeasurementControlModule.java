/*-
 * $Id: MeasurementControlModule.java,v 1.146.2.8 2006/03/21 09:43:30 arseniy Exp $
 *
 * Copyright © 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.mcm;

import static com.syrus.AMFICOM.general.ErrorMessages.ILLEGAL_ENTITY_CODE;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ObjectEntities.KIS_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MCM_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SERVER_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SYSTEMUSER_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.TEST_CODE;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort.AND;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort.OR;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort.OPERATION_EQUALS;
import static com.syrus.AMFICOM.measurement.Test.TestStatus.TEST_STATUS_ABORTED;
import static com.syrus.AMFICOM.measurement.Test.TestStatus.TEST_STATUS_NEW;
import static com.syrus.AMFICOM.measurement.Test.TestStatus.TEST_STATUS_PROCESSING;
import static com.syrus.AMFICOM.measurement.Test.TestStatus.TEST_STATUS_SCHEDULED;
import static com.syrus.AMFICOM.measurement.Test.TestStatus.TEST_STATUS_STOPPED;
import static com.syrus.AMFICOM.measurement.Test.TestStatus.TEST_STATUS_STOPPING;
import static com.syrus.AMFICOM.measurement.TestWrapper.COLUMN_STATUS;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.administration.MCM;
import com.syrus.AMFICOM.administration.Server;
import com.syrus.AMFICOM.administration.SystemUser;
import com.syrus.AMFICOM.eventv2.Event;
import com.syrus.AMFICOM.eventv2.corba.IdlEvent;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LoginException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.LoginRestorer;
import com.syrus.AMFICOM.general.SleepButWorkThread;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.measurement.KIS;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.Test.TestStatus;
import com.syrus.util.Application;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;

/**
 * @version $Revision: 1.146.2.8 $, $Date: 2006/03/21 09:43:30 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module mcm
 */
final class MeasurementControlModule extends SleepButWorkThread {
	public static final String APPLICATION_NAME = "mcm";
	public static final String SETUP_OPTION = "-setup";


	/*-********************************************************************
	 * Keys.                                                              *
	 **********************************************************************/

	public static final String KEY_MCM_ID = "MCMID";
	public static final String KEY_DB_HOST_NAME = "DBHostName";
	public static final String KEY_DB_SID = "DBSID";
	public static final String KEY_DB_CONNECTION_TIMEOUT = "DBConnectionTimeout";
	public static final String KEY_DB_LOGIN_NAME = "DBLoginName";
	public static final String KEY_TICK_TIME = "TickTime";
	public static final String KEY_MAX_FALLS = "MaxFalls";
	public static final String KEY_FORWARD_PROCESSING = "ForwardProcessing";
	public static final String KEY_KIS_TICK_TIME = "KISTickTime";
	public static final String KEY_KIS_MAX_FALLS = "KISMaxFalls";
	public static final String KEY_KIS_MAX_OPENED_CONNECTIONS = "KISMaxOpenedConnections";
	public static final String KEY_KIS_CONNECTION_TIMEOUT = "KISConnectionTimeout";
	public static final String KEY_KIS_HOST_NAME = "KISHostName";
	public static final String KEY_KIS_TCP_PORT = "KISTCPPort";


	/*-********************************************************************
	 * Default values.                                                    *
	 **********************************************************************/

	public static final String MCM_ID = "MCM_1";
	public static final String DB_SID = "amficom";
	public static final int DB_CONNECTION_TIMEOUT = 120;	//sec
	public static final String DB_LOGIN_NAME = "amficom";
	public static final int TICK_TIME = 5;	//sec
	public static final int FORWARD_PROCESSING = 0;	//sec
	public static final int KIS_TICK_TIME = 1;	//sec
	public static final int KIS_MAX_FALLS = 10;
	public static final int KIS_MAX_OPENED_CONNECTIONS = 1;
	public static final int KIS_CONNECTION_TIMEOUT = 120;	//sec
	public static final String KIS_HOST_NAME = "127.0.0.1";
	public static final short KIS_TCP_PORT = 7501;


	private static final String PASSWORD = "mcm";


	/**
	 * Реализация интерфейса {@link Comparator} для сравнения заданий по времени
	 * начала. Используется в {@link #sortTestsByStartTime(List)}.
	 */
	private static class TestStartTimeComparator implements Comparator<Test> {

		public int compare(final Test test1, final Test test2) {
			return test1.getStartTime().compareTo(test2.getStartTime());
		}
	}

	/**
	 * Реализация интерфейса {@link LoginRestorer} для восстановления сессии в
	 * системе.
	 */
	private static class MCMLoginRestorer implements LoginRestorer {

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
	 * {@link MeasurementControlModule} является "Одиночкой" (Singletone).
	 */
	private static MeasurementControlModule instance;


	/**
	 * Уникальный идентификатор данного модуля в системе.
	 */
	private final Identifier moduleId;

	/**
	 * Имя пользователя, от имени которого данный модуль работает в системе.
	 * Нужно для восстановления сеанса средствами
	 * {@link com.syrus.AMFICOM.general.LoginRestorer}. См.
	 * {@link MCMLoginRestorer#getLogin()}.
	 */
	private final String systemUserLogin;

	/**
	 * Набор идентификаторов заданий, которые необходимо поставить в очередь на
	 * исполнение, согласно времени начала каждого из них. Состояние каждого из
	 * этих заданий - НОВЫЙ.
	 */
	private final Set<Identifier> newTestIds;

	/**
	 * Набор идентификаторов заданий, исполнение которых необходимо прекратить.
	 */
	private final Set<Identifier> stoppingTestIds;

	/**
	 * Список заданий, ожидающих в очереди своего начала. Состояние каждого из
	 * них - НАЗНАЧЕН. Они упорядочены по времени начала. Когда подходит время,
	 * крайнее заданее из очереди отправляется на исполнение.
	 */
	private final List<Test> scheduledTests;

	/**
	 * Карта исполнителей заданий. Ключ - идентификатор задания, величина -
	 * исполнитель.
	 */
	private final Map<Identifier, TestProcessor> testProcessors;

	/**
	 * Управляющий соединениями с контрольно-измерительными средствами (КИС).
	 */
	private final KISConnectionManager kisConnectionManager;

	/**
	 * Карта приёмопередатчиков, обеспечивающих связь с
	 * контрольно-измерительными средствами (КИС). Ключ - идентификатор КИС,
	 * величина - приёмопередатчик.
	 */
	private final Map<Identifier, Transceiver> transceivers;

	/**
	 * Синхронизатор объектов. Отвечает за подгрузку объектов с сервера.
	 */
	private final MCMObjectSynchronizer objectSynchronizer;

	/**
	 * Очередь событий, а также поток, обрабатывающий эту очередь.
	 */
	private final EventQueue eventQueue;

	/**
	 * Поправка на "заторможенность" - систематическую погрешность времени
	 * начала выполнения заданий. Если вам кажется, что выполнение заданий
	 * постоянно запаздывает на какую-то постоянную величину, попробуйте
	 * выставить <code>{@link #forwardProcessing} > 0</code>. Измеряется в
	 * секундах.
	 */
	private final long forwardProcessing;

	/**
	 * Знак работы главного цикла {@link #run()}.
	 */
	private boolean running;

	/**
	 * Переменная для реализации интерфейса {@link Comparator}. Нужна для того,
	 * чтобы не создавать новый объект при каждом вызове
	 * {@link #sortTestsByStartTime(List)}.
	 */
	private static TestStartTimeComparator testStartTimeComparator;

	/**
	 * Восстановитель сессии. См. {@link #getLoginRestorer()}.
	 */
	private static MCMLoginRestorer loginRestorer;


	public static void main(String[] args) {
		Application.init(APPLICATION_NAME);

		/* Разобрать параметры командной строки. */
		if (args.length > 0) {
			if (args[0].equalsIgnoreCase(SETUP_OPTION)) {
				MCMSetup.setup();
				System.exit(0);
			} else {
				Log.errorMessage("Illegal options -- " + args);
				System.exit(0);
			}
		}

		/* Поготовка к работе. */
		try {
			startup();
		} catch (ApplicationException ae) {
			Log.errorMessage(ae);
			System.exit(0);
		}

		/* Запустить главный поток. */
		instance.start();

		/* Обеспечить правильное выключение в случае прерывания приложения. */
		Runtime.getRuntime().addShutdownHook(new Thread("MeasurementControlModule -- shutdown hook") {
			@Override
			public void run() {
				getInstance().shutdown();
			}
		});
	}

	/**
	 * Возвращает ссылку на объект-одиночку, представляющий данный модуль.
	 * 
	 * @return {@link #instance}.
	 */
	static MeasurementControlModule getInstance() {
		return instance;
	}

	/**
	 * Выполнить все необходимые приготовления к работе. Сюда входят:
	 * установление соединения с базой данных, создание контекста драйверов базы
	 * данных, извлечение из БД сведений о данном модуле, создание сеанса CORBA
	 * и вход в систему AMFICOM. Также этот метод создаёт единственный в
	 * пределах приложения экземпляр класса {@link MeasurementControlModule}.
	 * 
	 * @throws ApplicationException
	 */
	private static void startup() throws ApplicationException {

		/* Установить соединение с базой данных. */
		establishDatabaseConnection();

		/* Создать контекст драйверов базы данных. */
		DatabaseContextSetup.initDatabaseContext();

		/*
		 * Получить из БД данные о самом модуле, его сервере и пользователе, от
		 * имени которого он работает в системе.
		 */
		final Identifier mcmId = new Identifier(ApplicationProperties.getString(KEY_MCM_ID, MCM_ID));

		final StorableObjectDatabase<MCM> mcmDatabase = DatabaseContext.getDatabase(MCM_CODE);
		final MCM mcm = mcmDatabase.retrieveForId(mcmId);

		final StorableObjectDatabase<Server> serverDatabase = DatabaseContext.getDatabase(SERVER_CODE);
		final Server server = serverDatabase.retrieveForId(mcm.getServerId());

		final StorableObjectDatabase<SystemUser> systemUserDatabase = DatabaseContext.getDatabase(SYSTEMUSER_CODE);
		final SystemUser systemUser = systemUserDatabase.retrieveForId(mcm.getUserId());

		/* Создать окружение сеанса. */
		MCMSessionEnvironment.createInstance(server.getHostName(), mcmId.toString());

		/* Войти в систему. */
		final String systemUserLogin = systemUser.getLogin();
		try {
			MCMSessionEnvironment.getInstance().login(systemUserLogin, PASSWORD, mcm.getDomainId());
		} catch (final LoginException le) {
			Log.errorMessage(le);
		}

		/* Создать объект главного класса. */
		instance = new MeasurementControlModule(mcmId, systemUserLogin);
	}

	/**
	 * Установить соединение с базой данных.
	 */
	static void establishDatabaseConnection() {
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


	/**
	 * Закрытый конструктор. Предназначен для создания "одиночки". Создаёт и
	 * подготавливает все объекты, необходимые для работы. Выполняет поиск
	 * заданий, запланированных и выполнявшихся во время предыдущей остановки
	 * модуля. Устанавливает соединение с каждым из КИС. Запускает потоки
	 * синхронизатора объектов и очереди событий.
	 * 
	 * @param moduleId
	 *        Идентификатор данного модуля в системе.
	 * @param systemUserLogin
	 *        Имя пользователя, под которым данный модуль должен работать в
	 *        системе.
	 */
	private MeasurementControlModule(final Identifier moduleId, final String systemUserLogin) {
		super(ApplicationProperties.getInt(KEY_TICK_TIME, TICK_TIME) * 1000, ApplicationProperties.getInt(KEY_MAX_FALLS, MAX_FALLS));
		super.setName("MeasurementControlModule");

		this.moduleId = moduleId;
		this.systemUserLogin = systemUserLogin;

		this.newTestIds = Collections.synchronizedSet(new HashSet<Identifier>());
		this.stoppingTestIds = Collections.synchronizedSet(new HashSet<Identifier>());

		this.scheduledTests = Collections.synchronizedList(new LinkedList<Test>());
		this.searchScheduledAndProcessingTests();

		this.testProcessors = Collections.synchronizedMap(new HashMap<Identifier, TestProcessor>());

		this.kisConnectionManager = new KISConnectionManager();

		this.transceivers = Collections.synchronizedMap(new HashMap<Identifier, Transceiver>());
		this.setupKISTransceivers();

		this.objectSynchronizer = new MCMObjectSynchronizer(MCMSessionEnvironment.getInstance().getMCMServantManager());
		this.objectSynchronizer.start();

		this.eventQueue = new EventQueue();
		this.eventQueue.start();

		this.forwardProcessing = ApplicationProperties.getInt(KEY_FORWARD_PROCESSING, FORWARD_PROCESSING) * 1000;
		this.running = true;
	}

	/**
	 * Найти в БД задания в состояниях НАЗНАЧЕН и ВЫПОЛНЯЕТСЯ. Первые -
	 * поставить в очередь назначенных заданий {@link #scheduledTests} (с
	 * обязательным упорядочиванием по времени начала), а вторые - запустить на
	 * исполнение.
	 */
	private void searchScheduledAndProcessingTests() {
		final LinkedIdsCondition mcmTestCondition = new LinkedIdsCondition(this.moduleId, TEST_CODE);
		final TypicalCondition scheduledTestCondition = new TypicalCondition(TEST_STATUS_SCHEDULED,
				OPERATION_EQUALS,
				TEST_CODE,
				COLUMN_STATUS);
		final TypicalCondition processingTestCondition = new TypicalCondition(TEST_STATUS_PROCESSING,
				OPERATION_EQUALS,
				TEST_CODE,
				COLUMN_STATUS);
		final CompoundCondition testCondition = new CompoundCondition(mcmTestCondition,
				AND,
				new CompoundCondition(scheduledTestCondition, OR, processingTestCondition));

		final Set<Test> tests;
		try {
			tests = StorableObjectPool.getStorableObjectsByCondition(testCondition, true, false);
		} catch (ApplicationException ae) {
			Log.errorMessage(ae);
			return;
		}
		if (tests.isEmpty()) {
			return;
		}

		for (final Test test : tests) {
			switch (test.getStatus()) {
				case TEST_STATUS_SCHEDULED:
					this.scheduledTests.add(test);
					break;
				case TEST_STATUS_PROCESSING:
					this.startTestProcessor(test);
					break;
				default:
					Log.errorMessage("Illegal status: " + test.getStatus() + " of test '" + test.getId() + "'");
			}
		}

		/*
		 * Не забыть упорядочить по времени очередь заданий в состоянии
		 * НАЗНАЧЕН.
		 */
		if (!this.scheduledTests.isEmpty()) {
			sortTestsByStartTime(this.scheduledTests);
		}
	}

	/**
	 * Создать и запустить поток приёмопередатчика для каждого КИС.
	 */
	private void setupKISTransceivers() {
		final LinkedIdsCondition kisCondition = new LinkedIdsCondition(this.moduleId, KIS_CODE);
		final Set<KIS> kiss;
		try {
			kiss = StorableObjectPool.getStorableObjectsByCondition(kisCondition, true, false);
		} catch (ApplicationException ae) {
			Log.errorMessage(ae);
			return;
		}

		for (final KIS kis : kiss) {
			if (!kis.isOnService()) {
				continue;
			}

			final Identifier kisId = kis.getId();
			try {
				final Transceiver transceiver = new Transceiver(kisId);
				transceiver.start();
				Log.debugMessage("Started transceiver for KIS '" + kisId + "'", Log.DEBUGLEVEL07);
				this.transceivers.put(kisId, transceiver);
			} catch (ApplicationException ae) {
				Log.errorMessage(ae);
			}
		}
	}

	/**
	 * Получить уникальный идентификатор данного модуля в системе.
	 * 
	 * @return Идентификатор данного модуля
	 */
	Identifier getModuleId() {
		return this.moduleId;
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
	 * Получить ссылку на объект-исполнитель данного задания.
	 * 
	 * @param testId
	 *        Идентификатор задания
	 * @return Исполнитель задания
	 */
	TestProcessor getTestProcessor(final Identifier testId) {
		assert testId.getMajor() == TEST_CODE : ILLEGAL_ENTITY_CODE;
		return this.testProcessors.get(testId);
	}

	/**
	 * Получить объект соединения с данным КИС.
	 * 
	 * @param kisId
	 *        Идентификатор КИС
	 * @return Соединение с КИС
	 * @throws ApplicationException
	 *         {@link com.syrus.AMFICOM.general.CommunicationException}, если
	 *         нет возможности установить соединение;
	 *         {@link ApplicationException}, если не удалось подгрузить данные
	 *         из БД.
	 */
	KISConnection getKISConnection(final Identifier kisId) throws ApplicationException {
		assert kisId.getMajor() == KIS_CODE : ILLEGAL_ENTITY_CODE;

		final KIS kis = StorableObjectPool.getStorableObject(kisId, true);
		return this.kisConnectionManager.getConnection(kis);
	}

	/**
	 * Получить ссылку на приёмопередатчик данного КИС.
	 * 
	 * @param kisId
	 *        Идентификатор КИС
	 * @return Приёмопередатчик
	 */
	Transceiver getTransceiver(final Identifier kisId) {
		assert kisId.getMajor() == KIS_CODE : ILLEGAL_ENTITY_CODE;
		return this.transceivers.get(kisId);
	}

	/**
	 * Добавить идентификаторы объектов, которые должен подгрузить с сервера
	 * синхронизатор.
	 * 
	 * @param ids
	 */
	void addIdsToSynchronizer(final Set<Identifier> ids) {
		this.objectSynchronizer.addIdentifiers(ids);
	}

	/**
	 * Добавить событие в очереть.
	 * 
	 * @param event
	 * @throws EventQueueFullException
	 */
	void addEventToQueue(final Event<? extends IdlEvent> event) throws EventQueueFullException {
		assert event != null : NON_NULL_EXPECTED;
		this.eventQueue.addEvent(event);
	}

	/**
	 * Добавить идентификаторы новых заданий. Все эти задания должны иметь
	 * состояние НОВЫЙ. См. {@link #scheduleNewTests()}.
	 * 
	 * @param testIds
	 */
	synchronized void addNewTestIds(final Set<Identifier> testIds) {
		assert testIds != null : NON_NULL_EXPECTED;
		assert StorableObject.getEntityCodeOfIdentifiables(testIds) == TEST_CODE : ILLEGAL_ENTITY_CODE;

		this.newTestIds.addAll(testIds);
		this.notifyAll();
	}

	/**
	 * Добавить идентификаторы заданий, подлежащих остановке. Все эти задания
	 * должны иметь состояние ОСТАНАВЛИВАЕТСЯ. См. {@link #stopTests()}.
	 * 
	 * @param testIds
	 */
	synchronized void addStoppingTestIds(final Set<Identifier> testIds) {
		assert testIds != null : NON_NULL_EXPECTED;
		assert StorableObject.getEntityCodeOfIdentifiables(testIds) == TEST_CODE : ILLEGAL_ENTITY_CODE;

		this.stoppingTestIds.addAll(testIds);
		this.notifyAll();
	}

	/**
	 * Получить ссылку на восстановитель сессии. Объект создаётся только один
	 * раз при первом обращении.
	 * 
	 * @return {@link #loginRestorer}
	 */
	static MCMLoginRestorer getLoginRestorer() {
		if (loginRestorer == null) {
			loginRestorer = new MCMLoginRestorer();
		}
		return loginRestorer;
	}

	/**
	 * Удалить исполнитель задания из карты исполнителей {@link #testProcessors}.
	 * Предназначен для вызова из {@link TestProcessor#shutdown()} и только.
	 * 
	 * @param testId
	 */
	void testProcessorShutdown(final Identifier testId) {
		assert testId != null : NON_NULL_EXPECTED;
		assert testId.getMajor() == TEST_CODE : ILLEGAL_ENTITY_CODE;

		this.testProcessors.remove(testId);
	}

	/**
	 * Главный цикл.
	 */
	@Override
	public void run() {
		while (this.running) {

			synchronized (this) {
				while (this.newTestIds.isEmpty() && this.stoppingTestIds.isEmpty() && this.scheduledTests.isEmpty()) {
					try {
						sleep(super.initialTimeToSleep);
					} catch (InterruptedException ie) {
						Log.errorMessage(ie);
					}
				}
			}

			this.scheduleNewTests();

			this.stopTests();

			this.startScheduledTest();
		}
	}

	/**
	 * Новые задания упорядочиваются по времени начала, а затем помещаются в
	 * очередь назначенных заданий. При этом очередь назначенных заданий ({@link #scheduledTests})
	 * сохраняет свою упорядоченность по времени начала. Каждому добавленному в
	 * очередь заданию выставляется состояние НАЗНАЧЕН.
	 */
	private void scheduleNewTests() {
		if (this.newTestIds.isEmpty()) {
			return;
		}

		final Set<Test> newTests;
		try {
			newTests = StorableObjectPool.getStorableObjects(this.newTestIds, true);
		} catch (ApplicationException ae) {
			Log.errorMessage(ae);
			return;
		}

		final List<Test> newTestsList = new LinkedList<Test>(newTests);
		sortTestsByStartTime(newTestsList);

		synchronized (this.scheduledTests) {
			final ListIterator<Test> scheduledTestIterator = this.scheduledTests.listIterator();
			final ListIterator<Test> newTestIterator = newTestsList.listIterator();
			Test scheduledTest = scheduledTestIterator.hasNext() ? scheduledTestIterator.next() : null;
			Test newTest = newTestIterator.hasNext() ? newTestIterator.next() : null;
			while (newTest != null) {
				while (scheduledTest != null && scheduledTest.getStartTime().before(newTest.getStartTime())) {
					scheduledTest = scheduledTestIterator.hasNext() ? scheduledTestIterator.next() : null;
				}

				if (scheduledTest != null) {
					scheduledTestIterator.previous();
				}

				if (newTest.getStatus() == TEST_STATUS_NEW) {
					scheduledTestIterator.add(newTest);
					newTest.setStatus(TEST_STATUS_SCHEDULED);
				} else {
					Log.errorMessage("Status of new test '" + newTest.getId() + "': " + newTest.getStatus() + " -- not TEST_STATUS_NEW");
				}

				newTest = newTestIterator.hasNext() ? newTestIterator.next() : null;
			}
		}

		try {
			StorableObjectPool.flush(this.newTestIds, LoginManager.getUserId(), false);
		} catch (ApplicationException ae) {
			Log.errorMessage(ae);
		}
		this.newTestIds.clear();
	}

	/**
	 * Упорядочить задания по времени начала.
	 * 
	 * @param tests
	 */
	private static void sortTestsByStartTime(final List<Test> tests) {
		if (testStartTimeComparator == null) {
			testStartTimeComparator = new TestStartTimeComparator();
		}
		Collections.sort(tests, testStartTimeComparator);
	}

	/**
	 * Задания, помеченные на остановку ищутся в списке назначенных заданий
	 * {@link #scheduledTests} и в карте исполнителей заданий
	 * {@link #testProcessors}, в случае обнаружения - удаляются. Все задания,
	 * обрабатываемые этим методом, должны иметь состояние ОСТАНАВЛИВАЕТСЯ.
	 * Каждому заданию проставляется состояние ОСТАНОВЛЕН.
	 */
	private void stopTests() {
		if (this.stoppingTestIds.isEmpty()) {
			return;
		}

		final Set<Test> stoppingTests;
		try {
			stoppingTests = StorableObjectPool.getStorableObjects(this.stoppingTestIds, true);
		} catch (ApplicationException ae) {
			Log.errorMessage(ae);
			return;
		}

		for (final Test test : stoppingTests) {
			final Identifier testId = test.getId();
			final TestStatus testStatus = test.getStatus();
			if (this.scheduledTests.contains(test)) {
				Log.debugMessage("Test '" + testId + "': removing from list of scheduled tests", Log.DEBUGLEVEL07);
				this.scheduledTests.remove(test);
			} else if (this.testProcessors.containsKey(testId)) {
				Log.debugMessage("Test '" + testId + "': stopping test processor", Log.DEBUGLEVEL07);
				final TestProcessor testProcessor = this.testProcessors.get(testId);
				testProcessor.finishTest();
			} else {
				Log.errorMessage("Test '" + testId + "', status " + testStatus
						+ " -- not found scheduled list and in processing list. Will stop anyway.");
			}

			if (testStatus != TEST_STATUS_STOPPING) {
				Log.errorMessage("Illegal status: " + testStatus + " of test '" + testId + "' -- not STOPPING. Will stop anyway.");
			}
			test.setStatus(TEST_STATUS_STOPPED);
		}

		try {
			StorableObjectPool.flush(this.stoppingTestIds, LoginManager.getUserId(), false);
		} catch (ApplicationException ae) {
			Log.errorMessage(ae);
		}
		this.stoppingTestIds.clear();
	}

	/**
	 * Проверить, время начало ближайшего по времени назначенного задания. Если
	 * оно меньше текущего времени - запустить это задание на выполнение.
	 */
	private void startScheduledTest() {
		if (this.scheduledTests.isEmpty()) {
			return;
		}

		if (this.scheduledTests.get(0).getStartTime().getTime() <= System.currentTimeMillis() + this.forwardProcessing) {
			final Test test = this.scheduledTests.remove(0);
			this.startTestProcessor(test);
		}
	}

	/**
	 * Создать для данного задания исполнитель и запустить его. При этом
	 * создаётся новая запись в {@link #testProcessors}. Если создать
	 * исполнитель задания не удалось, задание прерывается.
	 * 
	 * @param test
	 */
	private void startTestProcessor(final Test test) {
		final Identifier testId = test.getId();
		if (this.testProcessors.containsKey(testId)) {
			Log.errorMessage("Test processor for test '" + testId + "' already started");
			return;
		}

		try {
			final TestProcessor testProcessor;
			switch (test.getTemporalType()) {
				case TEST_TEMPORAL_TYPE_ONETIME:
					testProcessor = new OnetimeTestProcessor(test);
					break;
				case TEST_TEMPORAL_TYPE_PERIODICAL:
					testProcessor = new PeriodicalTestProcessor(test);
					break;
				default:
					Log.errorMessage("Incorrect temporal type " + test.getTemporalType() + " of test '" + testId.toString() + "'");
					return;
			}
			this.testProcessors.put(testId, testProcessor);
			testProcessor.start();
		} catch (TestProcessingException tpe) {
			Log.errorMessage(tpe);
			test.setStatus(TEST_STATUS_ABORTED);
			try {
				StorableObjectPool.flush(test, LoginManager.getUserId(), true);
			} catch (ApplicationException ae) {
				Log.errorMessage(ae);
			}
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
			default:
				Log.errorMessage("Unknown error code: " + super.fallCode);
		}
	}

	/**
	 * Выключение главного цикла.
	 */
	void shutdown() {
		this.running = false;

		this.eventQueue.shutdown();

		this.objectSynchronizer.shutdown();

		synchronized (this.transceivers) {
			for (final Transceiver transceiver : this.transceivers.values()) {
				transceiver.shutdown();
			}
		}

		synchronized (this.testProcessors) {
			for (final TestProcessor testProcessor : this.testProcessors.values()) {
				testProcessor.shutdown();
			}
		}

		DatabaseConnection.closeConnection();
	}

}
