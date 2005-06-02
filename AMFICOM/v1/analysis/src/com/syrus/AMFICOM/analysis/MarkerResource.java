/*-
 * $Id: MarkerResource.java,v 1.1 2005/06/02 12:53:29 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis;

import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/06/02 12:53:29 $
 * @module analysis_v1
 */

public class MarkerResource {
	public static final String DASH = LangModelAnalyse.getString("eventTypeNoType");
	
	private String aType;
	private String aPositionM;
	private String aLoss;
	private String aReflectance;
	private String aAttenuation;
	private String aCumulativeLoss;

	private String bPositionM;
	private String abDistanceM;
	private String abLoss;
	private String abAttenuation;
	private String lsaAttenuation;
	private String abOrl;
		
	public String getAAttenuation() {
		return this.aAttenuation;
	}
	
	public void setAAttenuation(String attenuation) {
		this.aAttenuation = attenuation;
	}
	
	public String getAbAttenuation() {
		return this.abAttenuation;
	}
	
	public void setAbAttenuation(String abAttenuation) {
		this.abAttenuation = abAttenuation;
	}
	
	public String getAbDistance() {
		return this.abDistanceM;
	}
	
	public void setAbDistance(String abDistance) {
		this.abDistanceM = abDistance;
	}
	
	public String getAbLoss() {
		return this.abLoss;
	}
	
	public void setAbLoss(String abLoss) {
		this.abLoss = abLoss;
	}
	
	public String getAbOrl() {
		return this.abOrl;
	}
	
	public void setAbOrl(String abOrl) {
		this.abOrl = abOrl;
	}
	
	public String getACumulativeLoss() {
		return this.aCumulativeLoss;
	}
	
	public void setACumulativeLoss(String cumulativeLoss) {
		this.aCumulativeLoss = cumulativeLoss;
	}
	
	public String getALoss() {
		return this.aLoss;
	}
	
	public void setALoss(String loss) {
		this.aLoss = loss;
	}
	
	public String getAPosition() {
		return this.aPositionM;
	}
	
	public void setAPosition(String position) {
		this.aPositionM = position;
	}
	
	public String getAReflectance() {
		return this.aReflectance;
	}
	
	public void setAReflectance(String reflectance) {
		this.aReflectance = reflectance;
	}
	
	public String getAType() {
		return this.aType;
	}
	
	public void setAType(String type) {
		this.aType = type;
	}
	
	public String getBPosition() {
		return this.bPositionM;
	}
	
	public void setBPosition(String position) {
		this.bPositionM = position;
	}
	
	public String getLsaAttenuation() {
		return this.lsaAttenuation;
	}
	
	public void setLsaAttenuation(String lsaAttenuation) {
		this.lsaAttenuation = lsaAttenuation;
	}
}
