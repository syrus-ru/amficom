/*-
 * $Id: MarkerResource.java,v 1.3 2005/06/09 15:38:37 saa Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis;

import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;

/**
 * @author $Author: saa $
 * @version $Revision: 1.3 $, $Date: 2005/06/09 15:38:37 $
 * @module analysis_v1
 */

public class MarkerResource {
	public static final String DASH = LangModelAnalyse.getString("dash");
	
	private String aPositionM = DASH;
	private String aLoss = DASH;
	private String aReflectance = DASH;
	private String aAttenuation = DASH;
	private String aCumulativeLoss = DASH;

	private String bPositionM = DASH;
	private String abDistanceM = DASH;
	private String abLoss = DASH;
	private String abAttenuation = DASH;
	private String lsaAttenuation = DASH;
	private String abOrl = DASH;
		
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
