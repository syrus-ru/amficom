/*
 * $Id: AnalysisResourceKeys.java,v 1.5 2005/03/23 16:38:46 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.General.Model;

import com.syrus.AMFICOM.Client.Resource.ResourceKeys;

/**
 * @version $Revision: 1.5 $, $Date: 2005/03/23 16:38:46 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module analysis_v1
 */
public interface AnalysisResourceKeys extends ResourceKeys {

	String	COLOR_EVENTS_NEW						= "com.syrus.AMFICOM.color.analisys.events.new";
	String	COLOR_EVENTS_LOSS_CHANGED				= "com.syrus.AMFICOM.color.analisys.events.losschanged";
	String	COLOR_EVENTS_AMPLITUDE_CHANGED			= "com.syrus.AMFICOM.color.analisys.events.amplitudechanged";
	String	COLOR_EVENTS							= "com.syrus.AMFICOM.color.analisys.events";

	String	COLOR_EVENTS_NEW_SELECTED				= "com.syrus.AMFICOM.color.analisys.events.newSelected";
	String	COLOR_EVENTS_LOSS_CHANGED_SELECTED		= "com.syrus.AMFICOM.color.analisys.events.losschangedSelected";
	String	COLOR_EVENTS_AMPLITUDE_CHANGED_SELECTED	= "com.syrus.AMFICOM.color.analisys.events.amplitudechangedSelected";
	String	COLOR_EVENTS_SELECTED					= "com.syrus.AMFICOM.color.analisys.eventsSelected";
	String	COLOR_CONNECTOR							= "connectColor";
	String	COLOR_END								= "endColor";
	String	COLOR_MARKER							= "analysisMarkerColor";

	String	ICON_ANALYSIS_MINI						= "com.syrus.AMFICOM.icon.analisys.mainmini";
	String	ICON_SURVEY_MINI						= "com.syrus.AMFICOM.icon.survey.mainmini";
	String	ICON_EVALUATE_MINI						= "com.syrus.AMFICOM.icon.evaluation.mainmini";
	String	ICON_ANALYSIS_CENTER_A					= "com.syrus.AMFICOM.icon.analisys.centerA";
	String	ICON_ANALYSIS_CENTER_B					= "com.syrus.AMFICOM.icon.analisys.centerB";
	String	ICON_ANALYSIS_LOSS						= "com.syrus.AMFICOM.icon.analisys.loss";
	String	ICON_ANALYSIS_REFLECT					= "com.syrus.AMFICOM.icon.analisys.reflect";
	String	ICON_ANALYSIS_NOANALYSIS				= "com.syrus.AMFICOM.icon.analisys.noanalysis";
	String	ICON_ANALYSIS_PERFORM_ANALYSIS			= "com.syrus.AMFICOM.icon.analisys.perform";
	String	ICON_ANALYSIS_INITIAL_ANALYSIS			= "com.syrus.AMFICOM.icon.analisys.initailAnalysis";
	String	ICON_ANALYSIS_DEFAULT_ANALYSIS			= "com.syrus.AMFICOM.icon.analisys.defaultAnalysis";
	String	ICON_ANALYSIS_DOWNLOAD_TRACE			= "com.syrus.AMFICOM.icon.analisys.downloadTrace";
	String	ICON_ANALYSIS_DOWNLOAD_ADD				= "com.syrus.AMFICOM.icon.analisys.downloadAdd";
	String	ICON_ANALYSIS_DOWNLOAD_REMOVE			= "com.syrus.AMFICOM.icon.analisys.downloadRemove";
	String	ICON_ANALYSIS_MARKER					= "com.syrus.AMFICOM.icon.analisys.marker";
	String	ICON_ANALYSIS_DELETE_MARKER				= "com.syrus.AMFICOM.icon.analisys.deleteMarker";
	String	ICON_ANALYSIS_THRESHOLD					= "com.syrus.AMFICOM.icon.analisys.threshold";
	String	ICON_ANALYSIS_ENLARGE_X					= "com.syrus.AMFICOM.icon.analisys.enlargex";
	String	ICON_ANALYSIS_ENLARGE_Y					= "com.syrus.AMFICOM.icon.analisys.enlargey";
	String	ICON_ANALYSIS_REDUCE_X					= "com.syrus.AMFICOM.icon.analisys.reducex";
	String	ICON_ANALYSIS_REDUCE_Y					= "com.syrus.AMFICOM.icon.analisys.reducey";
	String	ICON_ANALYSIS_FIT						= "com.syrus.AMFICOM.icon.analisys.fit";
	String	ICON_ANALYSIS_ZOOM_BOX					= "com.syrus.AMFICOM.icon.analisys.zoombox";
	String	ICON_ANALYSIS_THRESHOLD_INITIAL			= "com.syrus.AMFICOM.icon.analisys.thresholdInitial";
	String	ICON_ANALYSIS_THRESHOLD_DEFAULT			= "com.syrus.AMFICOM.icon.analisys.thresholdDefault";
	String	ICON_ANALYSIS_THRESHOLD_INCREASE		= "com.syrus.AMFICOM.icon.analisys.thresholdIncrease";
	String	ICON_ANALYSIS_THRESHOLD_DECREASE		= "com.syrus.AMFICOM.icon.analisys.thresholdDecrease";
	String	ICON_ANALYSIS_EVENTS					= "com.syrus.AMFICOM.icon.analisys.events";
	String	ICON_ANALYSIS_MODELED					= "com.syrus.AMFICOM.icon.analisys.modeled";

	String	TEXT_KM									= "km";
	String	TEXT_DB									= "dB";
	String	TEXT_NS									= "ns";
	String	TEXT_NM									= "nm";
	String	TEXT_MT									= "mt";
	String	TEXT_NO_PATTERN							= "no_pattern";
	String	TEXT_PATTERN							= "pattern";
}
