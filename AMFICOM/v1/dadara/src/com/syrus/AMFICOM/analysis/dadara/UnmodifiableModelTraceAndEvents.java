/*-
 * $Id: UnmodifiableModelTraceAndEvents.java,v 1.1 2005/04/11 10:35:31 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis.dadara;

/**
 * @author $Author: saa $
 * @version $Revision: 1.1 $, $Date: 2005/04/11 10:35:31 $
 * @module
 */
public class UnmodifiableModelTraceAndEvents
implements ModelTraceAndEvents
{
	private ModelTraceAndEvents obj;

	public UnmodifiableModelTraceAndEvents(ModelTraceAndEvents mtae) {
		obj = mtae;
	}

	public double getDeltaX() {
		return obj.getDeltaX();
	}
	public ComplexReflectogramEvent[] getComplexEvents() {
		return obj.getComplexEvents();
	}
	public int getEventByCoord(int x) {
		return obj.getEventByCoord(x);
	}
	public ModelTrace getModelTrace() {
		return obj.getModelTrace();
	}
	public int getNEvents() {
		return obj.getNEvents();
	}
	public SimpleReflectogramEvent getSimpleEvent(int nEvent) {
		return obj.getSimpleEvent(nEvent);
	}
	public SimpleReflectogramEvent[] getSimpleEvents() {
		return obj.getSimpleEvents();
	}
}
