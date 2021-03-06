/*
 * $Id: AnalysisResourceKeys.java,v 1.33 2006/04/24 11:07:42 saa Exp $
 *
 * Copyright ? 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.General.Model;

import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;

import com.syrus.AMFICOM.client.resource.ResourceKeys;


/**
 * @version $Revision: 1.33 $, $Date: 2006/04/24 11:07:42 $
 * @author $Author: saa $
 * @author Vladimir Dolzhenko
 * @module analysis
 */
public interface AnalysisResourceKeys extends ResourceKeys {

	String	COLOR_DEADZONE							= "deadzone";
	String	COLOR_WELD								= "weld";
	String	COLOR_LINEZONE							= "lineZone";
	String	COLOR_NON_ID							= "nonId";
	String	COLOR_NOISE								= "noise";
	String  COLOR_PRIMARY_TRACE 					= "primaryTrace";

	// ????? ???????
	String	COLOR_EVENTS							= "com.syrus.AMFICOM.color.analisys.events";
	String  COLOR_EVENTS_LOST                       = "com.syrus.AMFICOM.color.analisys.events.lost";
	String  COLOR_EVENTS_NEW                        = "com.syrus.AMFICOM.color.analisys.events.new";
	String	COLOR_EVENTS_LOSS_CHANGED				= "com.syrus.AMFICOM.color.analisys.events.losschanged";
	String	COLOR_EVENTS_AMPLITUDE_CHANGED			= "com.syrus.AMFICOM.color.analisys.events.amplitudechanged";
	// ???? ??? ???????, ?? ??????? ???????? ?????????????? ??? ????? (??? ???? ?? ???????????, ????? ?? ???)
	String  COLOR_EVENTS_ALARM						= "com.syrus.AMFICOM.color.analisys.events.alarm";

	// ????? ?????????? ???????
	String	COLOR_EVENTS_SELECTED					= "com.syrus.AMFICOM.color.analisys.eventsSelected";
	String  COLOR_EVENTS_LOST_SELECTED				= "com.syrus.AMFICOM.color.analisys.events.lostSelected";
	String  COLOR_EVENTS_NEW_SELECTED               = "com.syrus.AMFICOM.color.analisys.events.newSelected";
	String	COLOR_EVENTS_LOSS_CHANGED_SELECTED		= "com.syrus.AMFICOM.color.analisys.events.losschangedSelected";
	String	COLOR_EVENTS_AMPLITUDE_CHANGED_SELECTED	= "com.syrus.AMFICOM.color.analisys.events.amplitudechangedSelected";
	String  COLOR_EVENTS_ALARM_SELECTED				= "com.syrus.AMFICOM.color.analisys.events.alarmSelected";

	String	COLOR_CONNECTOR							= "connectColor";
	String	COLOR_END								= "endColor";
	String	COLOR_MARKER							= "analysisMarkerColor";
	String	COLOR_TRACE_PREFIX						= "trace";
	String	COLOR_WARNING_THRESHOLD					= "com.syrus.AMFICOM.color.analisys.warningThreshold";
	String	COLOR_ALARM_THRESHOLD					= "com.syrus.AMFICOM.color.analisys.alarmThreshold";
	String	COLOR_MODELED							= "modeledColor";
	String	COLOR_MIN_TRACE_LEVEL					= "minTraceLevelColor";
	String	COLOR_SCALE								= "scaleColor";
	String	COLOR_SCALE_DIGITS						= "scaleDigitColor";
	String	COLOR_SELECT							= "selectColor";

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
	String	ICON_ANALYSIS_DOWNLOAD_MODEL			= "com.syrus.AMFICOM.icon.analisys.downloadModel";
	String	ICON_ANALYSIS_DOWNLOAD_ADD				= "com.syrus.AMFICOM.icon.analisys.downloadAdd";
	String	ICON_ANALYSIS_DOWNLOAD_REMOVE			= "com.syrus.AMFICOM.icon.analisys.downloadRemove";
	String	ICON_ANALYSIS_CHECK_MISMATCH			= "com.syrus.AMFICOM.icon.analisys.checkMismatch";
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
	String	ICON_ANALYSIS_THRESHOLD_CREATE_NEW		= "com.syrus.AMFICOM.icon.analisys.thresholdCreateNew";
	String	ICON_ANALYSIS_THRESHOLD_INCREASE		= "com.syrus.AMFICOM.icon.analisys.thresholdIncrease";
	String	ICON_ANALYSIS_THRESHOLD_DECREASE		= "com.syrus.AMFICOM.icon.analisys.thresholdDecrease";
	String	ICON_ANALYSIS_THRESHOLD_INCREASE_ALL_HARD	= "com.syrus.AMFICOM.icon.analisys.thresholdIncreaseAllHard";
	String	ICON_ANALYSIS_THRESHOLD_DECREASE_ALL_HARD	= "com.syrus.AMFICOM.icon.analisys.thresholdDecreaseAllHard";
	String	ICON_ANALYSIS_THRESHOLD_INCREASE_ALL_SOFT	= "com.syrus.AMFICOM.icon.analisys.thresholdIncreaseAllSoft";
	String	ICON_ANALYSIS_THRESHOLD_DECREASE_ALL_SOFT	= "com.syrus.AMFICOM.icon.analisys.thresholdDecreaseAllSoft";
	String	ICON_ANALYSIS_EVENTS					= "com.syrus.AMFICOM.icon.analisys.events";
	String	ICON_ANALYSIS_MODELED					= "com.syrus.AMFICOM.icon.analisys.modeled";
	String	ICON_ANALYSIS_TRACE						= "com.syrus.AMFICOM.icon.analisys.trace";
	String	ICON_SHOW_PATH_ELEMENTS					= "com.syrus.AMFICOM.icon.analisys.show_pes";
	String	ICON_ANALYSIS_WEAK_SECONDARY		= "com.syrus.AMFICOM.icon.analisys.weakSecondary";

	String	ICON_ANALYSIS_LINEAR					= "com.syrus.AMFICOM.icon.analisys.linear";
	String	ICON_ANALYSIS_GAIN					= "com.syrus.AMFICOM.icon.analisys.gain";
	String	ICON_ANALYSIS_SPLICE_GAIN					= "com.syrus.AMFICOM.icon.analisys.splicegain";
	String	ICON_ANALYSIS_BEND_LOSS					= "com.syrus.AMFICOM.icon.analisys.bendloss";
	String	ICON_ANALYSIS_SPLICE_LOSS					= "com.syrus.AMFICOM.icon.analisys.spliceloss";
	String	ICON_ANALYSIS_REFLECTION					= "com.syrus.AMFICOM.icon.analisys.reflection";
	String	ICON_ANALYSIS_SINGULARITY					= "com.syrus.AMFICOM.icon.analisys.singularity";
	String	ICON_ANALYSIS_END						= "com.syrus.AMFICOM.icon.analisys.end";
	String	ICON_ANALYSIS_DEADZONE					= "com.syrus.AMFICOM.icon.analisys.deadzone";
	String	ICON_ANALYSIS_BREAK					= "com.syrus.AMFICOM.icon.analisys.break";
	String	ICON_ANCHORED						= "com.syrus.AMFICOM.icon.anchored";

	String	BUTTON_ANALYSIS_PERFORM						= "Button.analysis_perform";
	String	BUTTON_ANALYSIS_INITIAL						= "Button.analysis_initial";
	String	BUTTON_ANALYSIS_DEFAULTS						= "Button.analysis_defaults";
	
	String	TEXT_KM									= "Text.km";
	String	TEXT_DB									= "Text.dB";
	String	TEXT_DBKM									= "Text.dB/km";
	String	TEXT_NS									= "Text.ns";
	String	TEXT_NM									= "Text.nm";
	String	TEXT_MT									= "Text.mt";
	String	TEXT_DASH									= "Text.dash";
	String	TEXT_NOISE_LEVEL							= "Text.noise_level";
	
	String	TEXT_NO_PATTERN							= "Label.no_pattern";
	String	TEXT_PATTERN							= "Label.pattern";
	String	LABEL_QK_MODIFIED					= "Label.QK_modified";
	
	
	
	String STROKE_NOISE_HISTOGRAMM = "noise_histogramm_stroke";
	String STROKE_DEFAULT = "default_stroke";

	// Frames
	String	FRAME_NOISE					= "Window.noiseFrame";
	String	FRAME_NOISE_HISTOGRAMM				= "Window.noiseHistogrammFrame";
	String	FRAME_TRACE_SELECTOR				= "Window.selectFrame";
	String	FRAME_PRIMARY_PARAMETERS	= "Window.paramFrame";
	String	FRAME_OVERALL_STATS					= "Window.statsFrame";
	String	FRAME_OVERALL_STATS_BASIC					= "Window.statsFrame.basic";
	String	FRAME_OVERALL_STATS_COMPARATIVE					= "Window.statsFrame.comparative";
	String	FRAME_EVENTS				= "Window.eventsFrame";
	String	FRAME_MARKERS_INFO			= "Window.markersInfoFrame";
	String	FRAME_ANALYSIS_MAIN				= "Window.analysisFrame";
	String	FRAME_ANALYSIS_SELECTION	= "Window.analysisSelectFrame";
	String	FRAME_DETAILED_EVENTS		= "Window.detailedEventsFrame";
	String	FRAME_HISTOGRAMM			= "Window.histogrammFrame";
	String	FRAME_THRESHOLDS		= "Window.thresholdsFrame";
	String	FRAME_THRESHOLDS_SELECTION	= "Window.thresholdsSelectionFrame";
}
