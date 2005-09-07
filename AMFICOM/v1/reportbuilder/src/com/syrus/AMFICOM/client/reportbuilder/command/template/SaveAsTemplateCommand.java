/*
 * $Id: SaveAsTemplateCommand.java,v 1.1 2005/09/07 08:43:25 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.reportbuilder.command.template;

import java.util.HashMap;
import java.util.Map;

import com.syrus.AMFICOM.client.report.LangModelReport;
import com.syrus.AMFICOM.report.DestinationModules;

public class SaveAsTemplateCommand {
	/**
	 * Список всех модулей
	 */
	static final public Map<String,String> MODULES_MAP = new HashMap<String,String>();
	static {
		MODULES_MAP.put(DestinationModules.ANALYSIS,LangModelReport.getString(DestinationModules.ANALYSIS));
		MODULES_MAP.put(DestinationModules.EVALUATION,LangModelReport.getString(DestinationModules.EVALUATION));
		MODULES_MAP.put(DestinationModules.MAP,LangModelReport.getString(DestinationModules.MAP));
		MODULES_MAP.put(DestinationModules.MODELING,LangModelReport.getString(DestinationModules.MODELING));
		MODULES_MAP.put(DestinationModules.OBSERVE,LangModelReport.getString(DestinationModules.OBSERVE));
		MODULES_MAP.put(DestinationModules.OPTIMIZATION,LangModelReport.getString(DestinationModules.OPTIMIZATION));
		MODULES_MAP.put(DestinationModules.PREDICTION,LangModelReport.getString(DestinationModules.PREDICTION));
		MODULES_MAP.put(DestinationModules.SCHEDULER,LangModelReport.getString(DestinationModules.SCHEDULER));
		MODULES_MAP.put(DestinationModules.SCHEME,LangModelReport.getString(DestinationModules.SCHEME));
		MODULES_MAP.put(DestinationModules.SURVEY,LangModelReport.getString(DestinationModules.SURVEY));
	}

}
