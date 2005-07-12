/*
 * $Id: DatabaseCommonTest.java,v 1.4 2005/07/12 15:54:12 arseniy Exp $
 * 
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */
package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.administration.AdministrationStorableObjectPool;
import com.syrus.AMFICOM.administration.DatabaseAdministrationObjectLoader;
import com.syrus.AMFICOM.administration.DomainDatabase;
import com.syrus.AMFICOM.administration.MCMDatabase;
import com.syrus.AMFICOM.administration.ServerDatabase;
import com.syrus.AMFICOM.administration.ServerProcessDatabase;
import com.syrus.AMFICOM.administration.SystemUser;
import com.syrus.AMFICOM.administration.SystemUserDatabase;
import com.syrus.AMFICOM.administration.SystemUserWrapper;
import com.syrus.AMFICOM.configuration.CableLinkTypeDatabase;
import com.syrus.AMFICOM.configuration.CableThreadDatabase;
import com.syrus.AMFICOM.configuration.CableThreadTypeDatabase;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.DatabaseConfigurationObjectLoader;
import com.syrus.AMFICOM.configuration.EquipmentDatabase;
import com.syrus.AMFICOM.configuration.EquipmentTypeDatabase;
import com.syrus.AMFICOM.configuration.KISDatabase;
import com.syrus.AMFICOM.configuration.LinkDatabase;
import com.syrus.AMFICOM.configuration.LinkTypeDatabase;
import com.syrus.AMFICOM.configuration.MeasurementPortDatabase;
import com.syrus.AMFICOM.configuration.MeasurementPortTypeDatabase;
import com.syrus.AMFICOM.configuration.MonitoredElementDatabase;
import com.syrus.AMFICOM.configuration.PortDatabase;
import com.syrus.AMFICOM.configuration.PortTypeDatabase;
import com.syrus.AMFICOM.configuration.TransmissionPathDatabase;
import com.syrus.AMFICOM.configuration.TransmissionPathTypeDatabase;
import com.syrus.AMFICOM.event.DatabaseEventObjectLoader;
import com.syrus.AMFICOM.event.EventDatabase;
import com.syrus.AMFICOM.event.EventSourceDatabase;
import com.syrus.AMFICOM.event.EventStorableObjectPool;
import com.syrus.AMFICOM.event.EventTypeDatabase;
import com.syrus.AMFICOM.measurement.AnalysisDatabase;
import com.syrus.AMFICOM.measurement.AnalysisTypeDatabase;
import com.syrus.AMFICOM.measurement.CronTemporalPatternDatabase;
import com.syrus.AMFICOM.measurement.DatabaseMeasurementObjectLoader;
import com.syrus.AMFICOM.measurement.EvaluationDatabase;
import com.syrus.AMFICOM.measurement.EvaluationTypeDatabase;
import com.syrus.AMFICOM.measurement.IntervalsTemporalPatternDatabase;
import com.syrus.AMFICOM.measurement.MeasurementDatabase;
import com.syrus.AMFICOM.measurement.MeasurementSetupDatabase;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.AMFICOM.measurement.MeasurementTypeDatabase;
import com.syrus.AMFICOM.measurement.ModelingDatabase;
import com.syrus.AMFICOM.measurement.ModelingTypeDatabase;
import com.syrus.AMFICOM.measurement.ParameterSetDatabase;
import com.syrus.AMFICOM.measurement.PeriodicalTemporalPatternDatabase;
import com.syrus.AMFICOM.measurement.ResultDatabase;
import com.syrus.AMFICOM.measurement.TestDatabase;
import com.syrus.AMFICOM.scheme.CableChannelingItemDatabase;
import com.syrus.AMFICOM.scheme.DatabaseSchemeObjectLoader;
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
import com.syrus.AMFICOM.scheme.SchemeStorableObjectPool;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.4 $, $Date: 2005/07/12 15:54:12 $
 * @author $Author: arseniy $
 * @module test
 */
public class DatabaseCommonTest extends SQLCommonTest {
	private static SystemUser sysUser;


	public static SystemUser getSysUser() {
		return sysUser;
	}

	@Override
	void oneTimeSetUp() {
		super.oneTimeSetUp();
		initDatabaseContext();
		initStorableObjectPools();
	}

	@Override
	void oneTimeTearDown() {
		super.oneTimeTearDown();
	}

	private static void initDatabaseContext() {
		DatabaseContext.registerDatabase(new ParameterTypeDatabase());
		DatabaseContext.registerDatabase(new CharacteristicTypeDatabase());
		DatabaseContext.registerDatabase(new CharacteristicDatabase());

		DatabaseContext.registerDatabase(new SystemUserDatabase());
		DatabaseContext.registerDatabase(new DomainDatabase());
		DatabaseContext.registerDatabase(new ServerDatabase());
		DatabaseContext.registerDatabase(new MCMDatabase());
		DatabaseContext.registerDatabase(new ServerProcessDatabase());

		DatabaseContext.registerDatabase(new EquipmentTypeDatabase());
		DatabaseContext.registerDatabase(new PortTypeDatabase());
		DatabaseContext.registerDatabase(new MeasurementPortTypeDatabase());
		DatabaseContext.registerDatabase(new TransmissionPathTypeDatabase());
		DatabaseContext.registerDatabase(new LinkTypeDatabase());
		DatabaseContext.registerDatabase(new CableLinkTypeDatabase());
		DatabaseContext.registerDatabase(new CableThreadTypeDatabase());
		DatabaseContext.registerDatabase(new EquipmentDatabase());
		DatabaseContext.registerDatabase(new PortDatabase());
		DatabaseContext.registerDatabase(new MeasurementPortDatabase());
		DatabaseContext.registerDatabase(new TransmissionPathDatabase());
		DatabaseContext.registerDatabase(new KISDatabase());
		DatabaseContext.registerDatabase(new MonitoredElementDatabase());
		DatabaseContext.registerDatabase(new LinkDatabase());
		DatabaseContext.registerDatabase(new CableThreadDatabase());

		DatabaseContext.registerDatabase(new MeasurementTypeDatabase());
		DatabaseContext.registerDatabase(new AnalysisTypeDatabase());
		DatabaseContext.registerDatabase(new EvaluationTypeDatabase());
		DatabaseContext.registerDatabase(new ModelingTypeDatabase());
		DatabaseContext.registerDatabase(new MeasurementDatabase());
		DatabaseContext.registerDatabase(new AnalysisDatabase());
		DatabaseContext.registerDatabase(new EvaluationDatabase());
		DatabaseContext.registerDatabase(new ModelingDatabase());
		DatabaseContext.registerDatabase(new MeasurementSetupDatabase());
		DatabaseContext.registerDatabase(new ResultDatabase());
		DatabaseContext.registerDatabase(new ParameterSetDatabase());
		DatabaseContext.registerDatabase(new TestDatabase());
		DatabaseContext.registerDatabase(new CronTemporalPatternDatabase());
		DatabaseContext.registerDatabase(new IntervalsTemporalPatternDatabase());
		DatabaseContext.registerDatabase(new PeriodicalTemporalPatternDatabase());

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

	private static void initStorableObjectPools() {
		try {
			SystemUserDatabase userDatabase = (SystemUserDatabase) DatabaseContext.getDatabase(ObjectEntities.SYSTEMUSER_CODE);
			sysUser = userDatabase.retrieveForLogin(SystemUserWrapper.SYS_LOGIN);
			DatabaseObjectLoader.init(sysUser.getId());
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			System.exit(0);
		}
		GeneralStorableObjectPool.init(new DatabaseGeneralObjectLoader(), StorableObjectResizableLRUMap.class);
		AdministrationStorableObjectPool.init(new DatabaseAdministrationObjectLoader(), StorableObjectResizableLRUMap.class);
		ConfigurationStorableObjectPool.init(new DatabaseConfigurationObjectLoader(), StorableObjectResizableLRUMap.class);
		MeasurementStorableObjectPool.init(new DatabaseMeasurementObjectLoader(), StorableObjectResizableLRUMap.class);
		EventStorableObjectPool.init(new DatabaseEventObjectLoader(), StorableObjectResizableLRUMap.class);
		SchemeStorableObjectPool.init(new DatabaseSchemeObjectLoader(), StorableObjectResizableLRUMap.class);
		//More pools...
	}

}
