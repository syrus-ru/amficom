/*-
 * $Id: DetailedEventResource.java,v 1.23 2006/03/14 14:45:51 saa Exp $
 *
 * Copyright � 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis;

import javax.swing.Icon;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.Analysis.AnalysisUtil;
import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;
import com.syrus.AMFICOM.analysis.dadara.EvaluationPerEventResult;
import com.syrus.AMFICOM.analysis.dadara.MathRef;
import com.syrus.AMFICOM.analysis.dadara.ModelTrace;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramComparer;
import com.syrus.AMFICOM.analysis.dadara.SimpleReflectogramEvent;
import com.syrus.AMFICOM.analysis.dadara.events.ConnectorDetailedEvent;
import com.syrus.AMFICOM.analysis.dadara.events.DeadZoneDetailedEvent;
import com.syrus.AMFICOM.analysis.dadara.events.DetailedEvent;
import com.syrus.AMFICOM.analysis.dadara.events.DetailedEventUtil;
import com.syrus.AMFICOM.analysis.dadara.events.EndOfTraceDetailedEvent;
import com.syrus.AMFICOM.analysis.dadara.events.HavingLoss;
import com.syrus.AMFICOM.analysis.dadara.events.LinearDetailedEvent;
import com.syrus.AMFICOM.analysis.dadara.events.NotIdentifiedDetailedEvent;
import com.syrus.AMFICOM.analysis.dadara.events.SpliceDetailedEvent;
import com.syrus.util.Log;

/**
 * @author $Author: saa $
 * @version $Revision: 1.23 $, $Date: 2006/03/14 14:45:51 $
 * @module analysis
 */

public class DetailedEventResource {
	private static final String DASH = LangModelAnalyse.getString("dash");
	private static final String DEFAULT_TYPE = DASH;
	
//	private static final Icon ICON_GAIN = (Icon) UIManager.get(AnalysisResourceKeys.ICON_ANALYSIS_GAIN);
	private static final Icon ICON_SPLICE_GAIN = (Icon) UIManager.get(AnalysisResourceKeys.ICON_ANALYSIS_SPLICE_GAIN);
	private static final Icon ICON_SPLICE_LOSS = (Icon) UIManager.get(AnalysisResourceKeys.ICON_ANALYSIS_SPLICE_LOSS);
//	private static final Icon ICON_BEND_LOSS = (Icon) UIManager.get(AnalysisResourceKeys.ICON_ANALYSIS_BEND_LOSS);
	private static final Icon ICON_REFLECTION = (Icon) UIManager.get(AnalysisResourceKeys.ICON_ANALYSIS_REFLECTION);
	private static final Icon ICON_SINGULARITY = (Icon) UIManager.get(AnalysisResourceKeys.ICON_ANALYSIS_SINGULARITY);
	private static final Icon ICON_DEADZONE = (Icon) UIManager.get(AnalysisResourceKeys.ICON_ANALYSIS_DEADZONE);
	private static final Icon ICON_END = (Icon) UIManager.get(AnalysisResourceKeys.ICON_ANALYSIS_END);
//	private static final Icon ICON_BREAK = (Icon) UIManager.get(AnalysisResourceKeys.ICON_ANALYSIS_BREAK);
	private static final Icon ICON_ANCHORED = (Icon) UIManager.get(AnalysisResourceKeys.ICON_ANCHORED);

	// Basic details
	private String number = DASH;
	private String typeGeneral = DEFAULT_TYPE;
	private String typeDetailed = DEFAULT_TYPE;
	private String location = DASH;
	private String length = DASH;
	private String reflectance = DASH;
	private String loss = DASH;
	private String attenuation = DASH;
	
	// Additional details
	private String extension = DASH; //same as length but may have different units
	private String startLevel = DASH; //all but notid 
	private String endLevel = DASH; //all but end & notid
	private String meanDeviation = DASH; //only linear
	private String maxDeviation = DASH; //only linear & notidentified
	private String reflectionLevel = DASH; //only reflection & end
	private String adz = DASH; //only dz
	private String edz = DASH; //only dz
	private String maxLevel = DASH; //only notid
	private String minLevel = DASH; //only notid
	
	//compare with etalon
	private String etalonType = DEFAULT_TYPE;
	private String etalonMaxDeviation = DASH;
	private String etalonMeanDeviation = DASH;
	private String lossDifference = DASH;
	private String locationDifference = DASH;
	private String lengthDifference = DASH;

	// extended comparison results
	private String qi = DASH;
	private String ki = DASH;

	private boolean anchored = false;

	private DetailedEvent event;
	
	public void initGeneral(DetailedEvent ev, int num, double res, double sigma) {
		this.event = ev;
		setNumber(Integer.toString(num));
		int sType = ev.getEventType();
		setTypeDetailed(AnalysisUtil.getDetailedEventName(ev));
		setLocation(Double.toString(MathRef.round_3(res * ev.getBegin())));
		setLength(Double.toString(MathRef.round_3(res * (DetailedEventUtil.getWidth(ev)))));
		
		switch (sType) {
		case SimpleReflectogramEvent.DEADZONE:
			setReflectance(DASH);
			setLoss(DASH);
			setAttenuation(DASH);
			break;
		case SimpleReflectogramEvent.LINEAR:
			setReflectance(DASH);
			setLoss(Double.toString(MathRef.round_3(((LinearDetailedEvent) ev).getLoss())));
			setAttenuation(Double.toString(MathRef.round_4(((LinearDetailedEvent) ev).getAttenuation() / res)));
			break;
		case SimpleReflectogramEvent.NOTIDENTIFIED:
			setReflectance(DASH);
			setLoss(Double.toString(MathRef.round_3(((NotIdentifiedDetailedEvent) ev).getLoss())));
			setAttenuation(DASH);
			break;
		case SimpleReflectogramEvent.CONNECTOR:
			setReflectance(Double.toString(MathRef.round_1(MathRef.calcReflectance(sigma, ((ConnectorDetailedEvent) ev).getAmpl()))));
			setLoss(Double.toString(MathRef.round_3(((ConnectorDetailedEvent) ev).getLoss())));
			setAttenuation(DASH);
			break;
		case SimpleReflectogramEvent.GAIN:
		case SimpleReflectogramEvent.LOSS:
			setReflectance(DASH);
			setLoss(Double.toString(MathRef.round_3(((SpliceDetailedEvent) ev).getLoss())));
			setAttenuation(DASH);
			break;
		case SimpleReflectogramEvent.ENDOFTRACE:
			setReflectance(Double.toString(MathRef.round_1(MathRef.calcReflectance(sigma, ((EndOfTraceDetailedEvent) ev).getAmpl()))));
			setLoss(DASH);
			setAttenuation(DASH);
			break;
		}			
	}
	
	public void initAdditional(DetailedEvent ev, double res) {
		this.event = ev;
		String sDB = " " + LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_DB);
		String sMT = " " + LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_MT);
		
		int sType = ev.getEventType();
		//setType(AnalysisUtil.getDetailedEventName(ev));
		setExtension(Double.toString(MathRef.round_3(res * (DetailedEventUtil.getWidth(ev)))) + " " + LangModelAnalyse.getString(AnalysisResourceKeys.TEXT_KM));
		
		switch (sType) {
		case SimpleReflectogramEvent.DEADZONE:
			setStartLevel(-MathRef.round_2(((DeadZoneDetailedEvent)ev).getPo()) + sDB);
			setEndLevel(-MathRef.round_2(((DeadZoneDetailedEvent)ev).getY1()) + sDB);
			setEdz(Math.round(((DeadZoneDetailedEvent)ev).getEdz() * res * 1000) + sMT);
			setAdz(Math.round(((DeadZoneDetailedEvent)ev).getAdz() * res * 1000) + sMT);
			break;
		case SimpleReflectogramEvent.LINEAR:
			setStartLevel(-MathRef.round_2(((LinearDetailedEvent)ev).getY0()) + sDB);
			setEndLevel(-MathRef.round_2(((LinearDetailedEvent)ev).getY1()) + sDB);
			setMeanDeviation(MathRef.round_3(((LinearDetailedEvent)ev).getRmsDev()) + sDB);
			setMaxDeviation(MathRef.round_3(((LinearDetailedEvent)ev).getMaxDev()) + sDB);
			break;
		case SimpleReflectogramEvent.NOTIDENTIFIED:
			setMinLevel(-MathRef.round_3(((NotIdentifiedDetailedEvent)ev).getYMin()) + sDB);
			setMaxLevel(-MathRef.round_3(((NotIdentifiedDetailedEvent)ev).getYMax()) + sDB);
			setMaxDeviation(MathRef.round_3(((NotIdentifiedDetailedEvent)ev).getMaxDev()) + sDB);
			break;
		case SimpleReflectogramEvent.CONNECTOR:
			setStartLevel(-MathRef.round_2(((ConnectorDetailedEvent)ev).getY0()) + sDB);
			setEndLevel(-MathRef.round_2(((ConnectorDetailedEvent)ev).getY1()) + sDB);
			setReflectionLevel(-MathRef.round_2(((ConnectorDetailedEvent)ev).getY2()) + sDB);
			break;
		case SimpleReflectogramEvent.GAIN:
		case SimpleReflectogramEvent.LOSS:
			setStartLevel(-MathRef.round_2(((SpliceDetailedEvent)ev).getY0()) + sDB);
			setEndLevel(-MathRef.round_2(((SpliceDetailedEvent)ev).getY1()) + sDB);
			break;
		case SimpleReflectogramEvent.ENDOFTRACE:
			setStartLevel(-MathRef.round_2(((EndOfTraceDetailedEvent)ev).getY0()) + sDB);
			setReflectionLevel(-MathRef.round_2(((EndOfTraceDetailedEvent)ev).getY2()) + sDB);
			break;
		}			
	}

	// anchorer may be null
	public void initComparative(DetailedEvent dataEvent,
			DetailedEvent etalonEvent,
			ModelTrace etalonMT,
			double deltaX,
			EvaluationPerEventResult perEvent,
			int perEventId) {
		this.event = dataEvent;
		setTypeGeneral(dataEvent != null ? AnalysisUtil.getSimpleEventNameByType(dataEvent.getEventType()) : DEFAULT_TYPE);
		setEtalonType(etalonEvent != null ? AnalysisUtil.getSimpleEventNameByType(etalonEvent.getEventType()) : DEFAULT_TYPE);
		
		DetailedEvent ev = dataEvent != null ? dataEvent : etalonEvent;

		if (ev != null && ev.getBegin() < Heap.getMTAEPrimary().getModelTrace().getLength() 
				&& ev.getBegin() < etalonMT.getLength()) {
			double difference = ReflectogramComparer.getMaxDeviation(Heap.getMTAEPrimary(), etalonMT, ev);
			double meanDeviation1 = ReflectogramComparer.getMeanDeviation(Heap.getMTAEPrimary(), etalonMT, ev);
			difference = ((int) (difference * 1000.)) / 1000.; // �������� 0.001 ��
			meanDeviation1 = ((int) (meanDeviation1 * 1000.)) / 1000.;
			setEtalonMaxDeviation(difference + " " + LangModelAnalyse.getString("dB"));
			setEtalonMeanDeviation(meanDeviation1 + " " + LangModelAnalyse.getString("dB"));
		} else {
			setEtalonMaxDeviation(DASH);
			setEtalonMeanDeviation(DASH);
		}

		if (dataEvent != null && etalonEvent != null) {
			String value;
			try {
				double lossDiff = DetailedEventUtil.getLossDiff(dataEvent, etalonEvent);
				lossDiff = ((int) (lossDiff * 1000.)) / 1000.;
				value = lossDiff + " " + LangModelAnalyse.getString("dB");
			} catch (NoSuchFieldException e) {
				value = DASH;
			}
			setLossDifference(value);

			double widthDiff = DetailedEventUtil.getWidthDiff(dataEvent, etalonEvent) * deltaX;
			widthDiff = ((int) (widthDiff * 10.)) / 10.; // �������� 0.1 �
			value = String.valueOf(widthDiff) + " " + LangModelAnalyse.getString("m");
			setLengthDifference(value);

			double locationDiff = (dataEvent.getBegin() - etalonEvent.getBegin()) * deltaX;
			locationDiff = ((int) (locationDiff * 10.)) / 10.;
			value = String.valueOf(locationDiff) + " "
					+ LangModelAnalyse.getString("m");
			setLocationDifference(value);
		} else {
			setLossDifference(DASH);
			setLengthDifference(DASH);
			setLocationDifference(DASH);
		}

//		Log.debugMessage("perEventId=" + perEventId
//				+ " of nEvents=" + (perEvent != null ? "" + perEvent.getNEvents() : "<null>"),
//				Level.FINEST);
		if (dataEvent != null && perEvent != null
				&& dataEvent != null && dataEvent instanceof HavingLoss) {
//			Log.debugMessage("hasQK=" + perEvent.hasQK(perEventId),
//					Level.FINEST);
			if (perEventId >= perEvent.getNEvents()) {
				// �������� perEventId < perEvent.getNEvents() �������� �� ������ ������������ ����������� �������,
				// ���������� � ������� � ����������� ������� �� �������. (���� ������ ��� ����� '������������' ������, ���� ��� � �� ����� �������)
				Log.errorMessage("perEventId >= perEvent.getNEvents(): seems that local analysis gave result different from loaded one. Trying to ignore the problem...");
				setQi(DASH);
				setKi(DASH);
			} else if (perEvent.hasQK(perEventId)) {
				setQi(String.valueOf(MathRef.round_2(perEvent.getQ(perEventId))));
				setKi(String.valueOf(MathRef.round_2(perEvent.getK(perEventId))));
			} else {
				String text = perEvent.isModified(perEventId)
						? LangModelAnalyse.getString("QKmodified")
						: DASH;
				setQi(text);
				setKi(text);
			}
		} else {
			setQi(DASH);
			setKi(DASH);
		}
	}

	public void initAnchorer(int etalonId,
			EventAnchorer anchorer) {
		anchored = false;
		if (anchorer != null && etalonId >= 0) {
			if (!anchorer.getEventAnchor(etalonId).isVoid())
				anchored = true;
		}
	}

	public String getAttenuation() {
		return this.attenuation;
	}
	
	public void setAttenuation(String attenuation) {
		this.attenuation = attenuation;
	}
	
	public String getLength() {
		return this.length;
	}
	
	public void setLength(String length) {
		this.length = length;
	}
	
	public String getLocation() {
		return this.location;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
	
	public String getLoss() {
		return this.loss;
	}
	
	public void setLoss(String loss) {
		this.loss = loss;
	}
	
	public String getNumber() {
		return this.number;
	}
	
	public Object getImage() {
		int sType = this.event.getEventType();

		switch (sType) {
		case SimpleReflectogramEvent.DEADZONE:
			return ICON_DEADZONE;
		case SimpleReflectogramEvent.LINEAR:
			return "";
		case SimpleReflectogramEvent.NOTIDENTIFIED:
			return ICON_SINGULARITY;
		case SimpleReflectogramEvent.CONNECTOR:
			return ICON_REFLECTION;
		case SimpleReflectogramEvent.GAIN:
			return ICON_SPLICE_GAIN;
		case SimpleReflectogramEvent.LOSS:
			return ICON_SPLICE_LOSS;
		case SimpleReflectogramEvent.ENDOFTRACE:
			return ICON_END;
		default: return null;
		}
	}

	public Object getAnchorImage() {
		return anchored ? ICON_ANCHORED : "";
	}

	public void setNumber(String number) {
		this.number = number;
	}
	
	public String getReflectance() {
		return this.reflectance;
	}
	
	public void setReflectance(String reflectance) {
		this.reflectance = reflectance;
	}

	public String getTypeDetailed() {
		return this.typeDetailed;
	}

	public void setTypeDetailed(String type) {
		this.typeDetailed = type;
	}

	public String getTypeGeneral() {
		return this.typeGeneral;
	}

	public void setTypeGeneral(String type) {
		this.typeGeneral = type;
	}

	public String getAdz() {
		return this.adz;
	}
	
	public void setAdz(String adz) {
		this.adz = adz;
	}
	
	public String getEdz() {
		return this.edz;
	}
	
	public void setEdz(String edz) {
		this.edz = edz;
	}
	
	public String getEndLevel() {
		return this.endLevel;
	}
	
	public void setEndLevel(String endLevel) {
		this.endLevel = endLevel;
	}
	
	public String getMaxDeviation() {
		return this.maxDeviation;
	}
	
	public void setMaxDeviation(String maxDeviation) {
		this.maxDeviation = maxDeviation;
	}
	
	public String getMeanDeviation() {
		return this.meanDeviation;
	}
	
	public void setMeanDeviation(String meanDeviation) {
		this.meanDeviation = meanDeviation;
	}
	
	public String getReflectionLevel() {
		return this.reflectionLevel;
	}
	
	public void setReflectionLevel(String reflectionLevel) {
		this.reflectionLevel = reflectionLevel;
	}
	
	public String getStartLevel() {
		return this.startLevel;
	}
	
	public void setStartLevel(String startLevel) {
		this.startLevel = startLevel;
	}
	
	
	public String getEtalonMaxDeviation() {
		return this.etalonMaxDeviation;
	}
	
	public void setEtalonMaxDeviation(String etalonMaxDeviation) {
		this.etalonMaxDeviation = etalonMaxDeviation;
	}
	
	public String getEtalonMeanDeviation() {
		return this.etalonMeanDeviation;
	}
	
	public void setEtalonMeanDeviation(String etalonMeanDeviation) {
		this.etalonMeanDeviation = etalonMeanDeviation;
	}
	
	public String getEtalonType() {
		return this.etalonType;
	}
	
	public void setEtalonType(String etalonType) {
		this.etalonType = etalonType;
	}
	
	public String getLengthDifference() {
		return this.lengthDifference;
	}
	
	public void setLengthDifference(String lengthDifference) {
		this.lengthDifference = lengthDifference;
	}
	
	public String getLocationDifference() {
		return this.locationDifference;
	}
	
	public void setLocationDifference(String locationDifference) {
		this.locationDifference = locationDifference;
	}
	
	public String getLossDifference() {
		return this.lossDifference;
	}
	
	public void setLossDifference(String lossDifference) {
		this.lossDifference = lossDifference;
	}
		
	public String getMaxLevel() {
		return this.maxLevel;
	}
	
	public void setMaxLevel(String maxLevel) {
		this.maxLevel = maxLevel;
	}
	
	public String getMinLevel() {
		return this.minLevel;
	}
	
	public void setMinLevel(String minLevel) {
		this.minLevel = minLevel;
	}
	
	
	public String getExtension() {
		return this.extension;
	}
	
	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getKi() {
		return this.ki;
	}

	public void setKi(String ki) {
		this.ki = ki;
	}

	public String getQi() {
		return this.qi;
	}

	public void setQi(String qi) {
		this.qi = qi;
	}

	public boolean isAnchored() {
		return this.anchored;
	}

	// XXX: are such setters really needed?
	public void setAnchored(boolean b) {
		this.anchored = b;
	}
}
