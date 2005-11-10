/*
 * $Id: XMLSessionEnvironment.java,v 1.11 2005/11/10 13:24:50 bob Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import java.io.File;

import com.syrus.AMFICOM.administration.DomainXML;
import com.syrus.AMFICOM.administration.MCMXML;
import com.syrus.AMFICOM.administration.PermissionAttributesXML;
import com.syrus.AMFICOM.administration.RoleXML;
import com.syrus.AMFICOM.administration.ServerXML;
import com.syrus.AMFICOM.administration.SystemUserXML;
import com.syrus.AMFICOM.event.DeliveryAttributesXML;
import com.syrus.AMFICOM.measurement.KISXML;
import com.syrus.AMFICOM.resource.LayoutItemXML;
import com.syrus.util.ApplicationProperties;


/**
 * @version $Revision: 1.11 $, $Date: 2005/11/10 13:24:50 $
 * @author $Author: bob $
 * @module commonclient
 */
public final class XMLSessionEnvironment {
	private static final String KEY_CACHE_PATH = "CachePath";
	private static final String CACHE_PATH = "cache";

	private XMLPoolContext poolContext;

	private static XMLSessionEnvironment instance;

	private XMLSessionEnvironment(final XMLPoolContext poolContext) {
		this.poolContext = poolContext;
		this.poolContext.init();

		this.registedXMLHandlers();
		
		IdentifierPool.init(new XMLIdentifierGeneratorServer(poolContext.getObjectLoader()));
	}

	
	private void registedXMLHandlers() {
//		XMLContext.registerXML(new ParameterTypeXML());
		XMLContext.registerXML(new CharacteristicTypeXML());
		XMLContext.registerXML(new CharacteristicXML());

		XMLContext.registerXML(new SystemUserXML());
		XMLContext.registerXML(new DomainXML());
		XMLContext.registerXML(new PermissionAttributesXML());
		XMLContext.registerXML(new ServerXML());
		XMLContext.registerXML(new MCMXML());
		XMLContext.registerXML(new RoleXML());
//		XMLContext.registerXML(new ServerProcessXML());		

		XMLContext.registerXML(new DeliveryAttributesXML());
		
//		XMLContext.registerXML(new EquipmentTypeXML());
//		XMLContext.registerXML(new PortTypeXML());
//		XMLContext.registerXML(new MeasurementPortTypeXML());
//		XMLContext.registerXML(new TransmissionPathTypeXML());
//		XMLContext.registerXML(new LinkTypeXML());
//		XMLContext.registerXML(new CableLinkTypeXML());
//		XMLContext.registerXML(new CableThreadTypeXML());
//		XMLContext.registerXML(new EquipmentXML());
//		XMLContext.registerXML(new PortXML());
//		XMLContext.registerXML(new MeasurementPortXML());
//		XMLContext.registerXML(new TransmissionPathXML());
		XMLContext.registerXML(new KISXML());
//		XMLContext.registerXML(new MonitoredElementXML());
//		XMLContext.registerXML(new LinkXML());
//		XMLContext.registerXML(new CableThreadXML());

//		XMLContext.registerXML(new MeasurementTypeXML());
//		XMLContext.registerXML(new AnalysisTypeXML());
//		XMLContext.registerXML(new EvaluationTypeXML());
//		XMLContext.registerXML(new ModelingTypeXML());
//		XMLContext.registerXML(new MeasurementXML());
//		XMLContext.registerXML(new AnalysisXML());
//		XMLContext.registerXML(new EvaluationXML());
//		XMLContext.registerXML(new ModelingXML());
//		XMLContext.registerXML(new MeasurementSetupXML());
//		XMLContext.registerXML(new ResultXML());
//		XMLContext.registerXML(new ParameterSetXML());
//		XMLContext.registerXML(new TestXML());
//		XMLContext.registerXML(new CronTemporalPatternXML());
//		XMLContext.registerXML(new IntervalsTemporalPatternXML());
//		XMLContext.registerXML(new PeriodicalTemporalPatternXML());
		
//		XMLContext.registerXML(new ImageResourceXML());
		XMLContext.registerXML(new LayoutItemXML());

//		XMLContext.registerXML(new SiteNodeTypeXML());
//		XMLContext.registerXML(new PhysicalLinkTypeXML());
//		XMLContext.registerXML(new CollectorXML());
//		XMLContext.registerXML(new MapXML());
//		XMLContext.registerXML(new MarkXML());
//		XMLContext.registerXML(new NodeLinkXML());
//		XMLContext.registerXML(new PhysicalLinkXML());
//		XMLContext.registerXML(new SiteNodeXML());
//		XMLContext.registerXML(new TopologicalNodeXML());

//		XMLContext.registerXML(new SchemeProtoGroupXML());
//		XMLContext.registerXML(new SchemeProtoElementXML());
//		XMLContext.registerXML(new SchemeXML());
//		XMLContext.registerXML(new SchemeElementXML());
//		XMLContext.registerXML(new SchemeOptimizeInfoXML());
//		XMLContext.registerXML(new SchemeOptimizeInfoSwitchXML());
//		XMLContext.registerXML(new SchemeOptimizeInfoRtuXML());
//		XMLContext.registerXML(new SchemeMonitoringSolutionXML());
//		XMLContext.registerXML(new SchemeDeviceXML());
//		XMLContext.registerXML(new SchemePortXML());
//		XMLContext.registerXML(new SchemeCablePortXML());
//		XMLContext.registerXML(new SchemeLinkXML());
//		XMLContext.registerXML(new SchemeCableLinkXML());
//		XMLContext.registerXML(new SchemeCableThreadXML());
//		XMLContext.registerXML(new CableChannelingItemXML());
//		XMLContext.registerXML(new SchemePathXML());
//		XMLContext.registerXML(new PathElementXML());

//		XMLContext.registerXML(new MapViewXML());
	}
	
	public void openSession() throws ApplicationException {
		StorableObjectPool.deserialize(this.poolContext.getLRUSaver());
	}

	public void closeSession() {
		StorableObjectPool.serialize(this.poolContext.getLRUSaver());
	}

	public static void createInstance() {
		final File cachePath = new File(ApplicationProperties.getString(KEY_CACHE_PATH, CACHE_PATH));
		final XMLObjectLoader xmlObjectLoader = new XMLObjectLoader(cachePath);

		final XMLPoolContext xmlPoolContext = new XMLPoolContext(xmlObjectLoader);
		instance = new XMLSessionEnvironment(xmlPoolContext);
	}

	public static XMLSessionEnvironment getInstance() {
		return instance;
	}
	
}
