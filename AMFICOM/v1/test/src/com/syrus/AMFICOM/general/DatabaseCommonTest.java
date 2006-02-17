/*
 * $Id: DatabaseCommonTest.java,v 1.17 2006/02/17 11:48:02 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.ObjectGroupEntities.ADMINISTRATION_GROUP_CODE;
import static com.syrus.AMFICOM.general.ObjectGroupEntities.CONFIGURATION_GROUP_CODE;
import static com.syrus.AMFICOM.general.ObjectGroupEntities.EVENT_GROUP_CODE;
import static com.syrus.AMFICOM.general.ObjectGroupEntities.GENERAL_GROUP_CODE;
import static com.syrus.AMFICOM.general.ObjectGroupEntities.MEASUREMENT_GROUP_CODE;
import static com.syrus.AMFICOM.general.ObjectGroupEntities.SCHEME_GROUP_CODE;
import static com.syrus.AMFICOM.general.PoolContext.ADMINISTRATION_POOL_SIZE;
import static com.syrus.AMFICOM.general.PoolContext.ADMINISTRATION_POOL_TIME_TO_LIVE;
import static com.syrus.AMFICOM.general.PoolContext.CONFIGURATION_POOL_SIZE;
import static com.syrus.AMFICOM.general.PoolContext.CONFIGURATION_POOL_TIME_TO_LIVE;
import static com.syrus.AMFICOM.general.PoolContext.EVENT_POOL_SIZE;
import static com.syrus.AMFICOM.general.PoolContext.EVENT_POOL_TIME_TO_LIVE;
import static com.syrus.AMFICOM.general.PoolContext.GENERAL_POOL_SIZE;
import static com.syrus.AMFICOM.general.PoolContext.GENERAL_POOL_TIME_TO_LIVE;
import static com.syrus.AMFICOM.general.PoolContext.KEY_ADMINISTRATION_POOL_SIZE;
import static com.syrus.AMFICOM.general.PoolContext.KEY_ADMINISTRATION_POOL_TIME_TO_LIVE;
import static com.syrus.AMFICOM.general.PoolContext.KEY_CONFIGURATION_POOL_SIZE;
import static com.syrus.AMFICOM.general.PoolContext.KEY_CONFIGURATION_POOL_TIME_TO_LIVE;
import static com.syrus.AMFICOM.general.PoolContext.KEY_EVENT_POOL_SIZE;
import static com.syrus.AMFICOM.general.PoolContext.KEY_EVENT_POOL_TIME_TO_LIVE;
import static com.syrus.AMFICOM.general.PoolContext.KEY_GENERAL_POOL_SIZE;
import static com.syrus.AMFICOM.general.PoolContext.KEY_GENERAL_POOL_TIME_TO_LIVE;
import static com.syrus.AMFICOM.general.PoolContext.KEY_MEASUREMENT_POOL_SIZE;
import static com.syrus.AMFICOM.general.PoolContext.KEY_MEASUREMENT_POOL_TIME_TO_LIVE;
import static com.syrus.AMFICOM.general.PoolContext.KEY_SCHEME_POOL_SIZE;
import static com.syrus.AMFICOM.general.PoolContext.KEY_SCHEME_POOL_TIME_TO_LIVE;
import static com.syrus.AMFICOM.general.PoolContext.MEASUREMENT_POOL_SIZE;
import static com.syrus.AMFICOM.general.PoolContext.MEASUREMENT_POOL_TIME_TO_LIVE;
import static com.syrus.AMFICOM.general.PoolContext.SCHEME_POOL_SIZE;
import static com.syrus.AMFICOM.general.PoolContext.SCHEME_POOL_TIME_TO_LIVE;

import com.syrus.AMFICOM.administration.DomainDatabase;
import com.syrus.AMFICOM.administration.MCMDatabase;
import com.syrus.AMFICOM.administration.PermissionAttributesDatabase;
import com.syrus.AMFICOM.administration.RoleDatabase;
import com.syrus.AMFICOM.administration.ServerDatabase;
import com.syrus.AMFICOM.administration.ServerProcessDatabase;
import com.syrus.AMFICOM.administration.SystemUser;
import com.syrus.AMFICOM.administration.SystemUserDatabase;
import com.syrus.AMFICOM.administration.SystemUserWrapper;
import com.syrus.AMFICOM.configuration.CableLinkTypeDatabase;
import com.syrus.AMFICOM.configuration.CableThreadDatabase;
import com.syrus.AMFICOM.configuration.CableThreadTypeDatabase;
import com.syrus.AMFICOM.configuration.EquipmentDatabase;
import com.syrus.AMFICOM.configuration.LinkDatabase;
import com.syrus.AMFICOM.configuration.LinkTypeDatabase;
import com.syrus.AMFICOM.configuration.PortDatabase;
import com.syrus.AMFICOM.configuration.PortTypeDatabase;
import com.syrus.AMFICOM.configuration.ProtoEquipmentDatabase;
import com.syrus.AMFICOM.configuration.TransmissionPathDatabase;
import com.syrus.AMFICOM.configuration.TransmissionPathTypeDatabase;
import com.syrus.AMFICOM.event.DeliveryAttributesDatabase;
import com.syrus.AMFICOM.event.EventDatabase;
import com.syrus.AMFICOM.event.EventSourceDatabase;
import com.syrus.AMFICOM.event.EventTypeDatabase;
import com.syrus.AMFICOM.measurement.AnalysisDatabase;
import com.syrus.AMFICOM.measurement.CronTemporalPatternDatabase;
import com.syrus.AMFICOM.measurement.IntervalsTemporalPatternDatabase;
import com.syrus.AMFICOM.measurement.KISDatabase;
import com.syrus.AMFICOM.measurement.MeasurementDatabase;
import com.syrus.AMFICOM.measurement.MeasurementPortDatabase;
import com.syrus.AMFICOM.measurement.MeasurementPortTypeDatabase;
import com.syrus.AMFICOM.measurement.MeasurementSetupDatabase;
import com.syrus.AMFICOM.measurement.ModelingDatabase;
import com.syrus.AMFICOM.measurement.MonitoredElementDatabase;
import com.syrus.AMFICOM.measurement.ParameterSetDatabase;
import com.syrus.AMFICOM.measurement.PeriodicalTemporalPatternDatabase;
import com.syrus.AMFICOM.measurement.ResultDatabase;
import com.syrus.AMFICOM.measurement.TestDatabase;
import com.syrus.AMFICOM.scheme.CableChannelingItemDatabase;
import com.syrus.AMFICOM.scheme.PathElementDatabase;
import com.syrus.AMFICOM.scheme.SchemeCableLinkDatabase;
import com.syrus.AMFICOM.scheme.SchemeCablePortDatabase;
import com.syrus.AMFICOM.scheme.SchemeCableThreadDatabase;
import com.syrus.AMFICOM.scheme.SchemeDatabase;
import com.syrus.AMFICOM.scheme.SchemeDeviceDatabase;
import com.syrus.AMFICOM.scheme.SchemeElementDatabase;
import com.syrus.AMFICOM.scheme.SchemeLinkDatabase;
import com.syrus.AMFICOM.scheme.SchemeMonitoringSolutionDatabase;
import com.syrus.AMFICOM.scheme.SchemeOptimizeInfoDatabase;
import com.syrus.AMFICOM.scheme.SchemeOptimizeInfoRtuDatabase;
import com.syrus.AMFICOM.scheme.SchemeOptimizeInfoSwitchDatabase;
import com.syrus.AMFICOM.scheme.SchemePathDatabase;
import com.syrus.AMFICOM.scheme.SchemePortDatabase;
import com.syrus.AMFICOM.scheme.SchemeProtoElementDatabase;
import com.syrus.AMFICOM.scheme.SchemeProtoGroupDatabase;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.17 $, $Date: 2006/02/17 11:48:02 $
 * @author $Author: arseniy $
 * @module test
 */
public class DatabaseCommonTest extends SQLCommonTest {

	@Override
	void oneTimeSetUp() {
		super.oneTimeSetUp();
		initDatabaseContext();
		initStorableObjectPool();
		login();
	}

	@Override
	void oneTimeTearDown() {
		logout();
		super.oneTimeTearDown();
	}

	private static void initDatabaseContext() {
		DatabaseContext.registerDatabase(new CharacteristicTypeDatabase());
		DatabaseContext.registerDatabase(new CharacteristicDatabase());

		DatabaseContext.registerDatabase(new SystemUserDatabase());
		DatabaseContext.registerDatabase(new DomainDatabase());
		DatabaseContext.registerDatabase(new ServerDatabase());
		DatabaseContext.registerDatabase(new MCMDatabase());
		DatabaseContext.registerDatabase(new ServerProcessDatabase());
		DatabaseContext.registerDatabase(new PermissionAttributesDatabase());
		DatabaseContext.registerDatabase(new RoleDatabase());
		DatabaseContext.registerDatabase(new DeliveryAttributesDatabase());

		DatabaseContext.registerDatabase(new PortTypeDatabase());
		DatabaseContext.registerDatabase(new TransmissionPathTypeDatabase());
		DatabaseContext.registerDatabase(new LinkTypeDatabase());
		DatabaseContext.registerDatabase(new CableLinkTypeDatabase());
		DatabaseContext.registerDatabase(new CableThreadTypeDatabase());
		DatabaseContext.registerDatabase(new ProtoEquipmentDatabase());
		DatabaseContext.registerDatabase(new EquipmentDatabase());
		DatabaseContext.registerDatabase(new PortDatabase());
		DatabaseContext.registerDatabase(new TransmissionPathDatabase());
		DatabaseContext.registerDatabase(new LinkDatabase());
		DatabaseContext.registerDatabase(new CableThreadDatabase());

		DatabaseContext.registerDatabase(new MeasurementPortTypeDatabase());
		DatabaseContext.registerDatabase(new MeasurementDatabase());
		DatabaseContext.registerDatabase(new AnalysisDatabase());
		DatabaseContext.registerDatabase(new ModelingDatabase());
		DatabaseContext.registerDatabase(new MeasurementSetupDatabase());
		DatabaseContext.registerDatabase(new ResultDatabase());
		DatabaseContext.registerDatabase(new ParameterSetDatabase());
		DatabaseContext.registerDatabase(new TestDatabase());
		DatabaseContext.registerDatabase(new CronTemporalPatternDatabase());
		DatabaseContext.registerDatabase(new IntervalsTemporalPatternDatabase());
		DatabaseContext.registerDatabase(new PeriodicalTemporalPatternDatabase());
		DatabaseContext.registerDatabase(new MeasurementPortDatabase());
		DatabaseContext.registerDatabase(new KISDatabase());
		DatabaseContext.registerDatabase(new MonitoredElementDatabase());

		DatabaseContext.registerDatabase(new EventTypeDatabase());
		DatabaseContext.registerDatabase(new EventSourceDatabase());
		DatabaseContext.registerDatabase(new EventDatabase());

		DatabaseContext.registerDatabase(new SchemeProtoGroupDatabase());
		DatabaseContext.registerDatabase(new SchemeProtoElementDatabase());
		DatabaseContext.registerDatabase(new SchemeDatabase());
		DatabaseContext.registerDatabase(new SchemeElementDatabase());
		DatabaseContext.registerDatabase(new SchemeOptimizeInfoDatabase());
		DatabaseContext.registerDatabase(new SchemeOptimizeInfoSwitchDatabase());
		DatabaseContext.registerDatabase(new SchemeOptimizeInfoRtuDatabase());
		DatabaseContext.registerDatabase(new SchemeMonitoringSolutionDatabase());
		DatabaseContext.registerDatabase(new SchemeDeviceDatabase());
		DatabaseContext.registerDatabase(new SchemePortDatabase());
		DatabaseContext.registerDatabase(new SchemeCablePortDatabase());
		DatabaseContext.registerDatabase(new SchemeLinkDatabase());
		DatabaseContext.registerDatabase(new SchemeCableLinkDatabase());
		DatabaseContext.registerDatabase(new SchemeCableThreadDatabase());
		DatabaseContext.registerDatabase(new CableChannelingItemDatabase());
		DatabaseContext.registerDatabase(new SchemePathDatabase());
		DatabaseContext.registerDatabase(new PathElementDatabase());

		//More database drivers...
	}

	private static void initStorableObjectPool() {
		final ObjectLoader objectLoader = new DatabaseObjectLoader();

		final int generalPoolSize = ApplicationProperties.getInt(KEY_GENERAL_POOL_SIZE, GENERAL_POOL_SIZE);
		final int administrationPoolSize = ApplicationProperties.getInt(KEY_ADMINISTRATION_POOL_SIZE, ADMINISTRATION_POOL_SIZE);
		final int configurationPoolSize = ApplicationProperties.getInt(KEY_CONFIGURATION_POOL_SIZE, CONFIGURATION_POOL_SIZE);
		final int measurementPoolSize = ApplicationProperties.getInt(KEY_MEASUREMENT_POOL_SIZE, MEASUREMENT_POOL_SIZE);
		final int eventPoolSize = ApplicationProperties.getInt(KEY_EVENT_POOL_SIZE, EVENT_POOL_SIZE);
		final int schemePoolSize = ApplicationProperties.getInt(KEY_SCHEME_POOL_SIZE, SCHEME_POOL_SIZE);

		// All convert to ns
		final long generalPoolTimeToLive = ApplicationProperties.getInt(KEY_GENERAL_POOL_TIME_TO_LIVE, GENERAL_POOL_TIME_TO_LIVE) * 60000000000L;
		final long administrationPoolTimeToLive = ApplicationProperties.getInt(KEY_ADMINISTRATION_POOL_TIME_TO_LIVE, ADMINISTRATION_POOL_TIME_TO_LIVE) * 60000000000L;
		final long configurationPoolTimeToLive = ApplicationProperties.getInt(KEY_CONFIGURATION_POOL_TIME_TO_LIVE, CONFIGURATION_POOL_TIME_TO_LIVE) * 60000000000L;
		final long measurementPoolTimeToLive = ApplicationProperties.getInt(KEY_MEASUREMENT_POOL_TIME_TO_LIVE, MEASUREMENT_POOL_TIME_TO_LIVE) * 60000000000L;
		final long eventPoolTimeToLive = ApplicationProperties.getInt(KEY_EVENT_POOL_TIME_TO_LIVE, EVENT_POOL_TIME_TO_LIVE) * 60000000000L;
		final long schemePoolTimeToLive = ApplicationProperties.getInt(KEY_SCHEME_POOL_TIME_TO_LIVE, SCHEME_POOL_TIME_TO_LIVE) * 60000000000L;

		StorableObjectPool.init(objectLoader);
		StorableObjectPool.addObjectPoolGroup(GENERAL_GROUP_CODE, generalPoolSize, generalPoolTimeToLive);
		StorableObjectPool.addObjectPoolGroup(ADMINISTRATION_GROUP_CODE, administrationPoolSize, administrationPoolTimeToLive);
		StorableObjectPool.addObjectPoolGroup(CONFIGURATION_GROUP_CODE, configurationPoolSize, configurationPoolTimeToLive);
		StorableObjectPool.addObjectPoolGroup(MEASUREMENT_GROUP_CODE, measurementPoolSize, measurementPoolTimeToLive);
		StorableObjectPool.addObjectPoolGroup(EVENT_GROUP_CODE, eventPoolSize, eventPoolTimeToLive);
		StorableObjectPool.addObjectPoolGroup(SCHEME_GROUP_CODE, schemePoolSize, schemePoolTimeToLive);
		//More pools...
	}

	private static void login() {
		LoginManager.init(new DatabaseLoginPerformer(), null);
		try {
			LoginManager.login(SystemUserWrapper.SYS_LOGIN, "", VOID_IDENTIFIER);
		} catch (ApplicationException ae) {
			Log.errorMessage(ae);
			try {
				LoginManager.logout();
			} catch (ApplicationException ae1) {
				Log.errorMessage(ae1);
			}
			System.exit(0);
		}
	}

	private static void logout() {
		try {
			LoginManager.logout();
		} catch (ApplicationException ae) {
			Log.errorMessage(ae);
		}
	}
}
