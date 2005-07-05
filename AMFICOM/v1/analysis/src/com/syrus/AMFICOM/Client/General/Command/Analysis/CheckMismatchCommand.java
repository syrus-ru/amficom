/*-
 * $Id: CheckMismatchCommand.java,v 1.1 2005/07/05 15:40:05 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.General.Command.Analysis;

import java.util.Iterator;
import java.util.List;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.analysis.CoreAnalysisManager;
import com.syrus.AMFICOM.analysis.Etalon;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramAlarm;
import com.syrus.AMFICOM.client.model.AbstractCommand;

public class CheckMismatchCommand extends AbstractCommand {
	public void execute() {
	    if (Heap.getBSPrimaryTrace() != null
	    		&& Heap.getMinuitAnalysisParams() != null
	    		&& Heap.getMTMEtalon() != null) {
	    	List alarms = CoreAnalysisManager.compareAndMakeAlarms(
	    			Heap.getRefAnalysisPrimary().getAR(),
	    			new Etalon(Heap.getMTMEtalon(),
	    					Heap.getMinTraceLevel(),
	    					Heap.getAnchorer()));
	    	// FIXME: debug-time console alarm listing
	    	if (alarms.size() == 0)
	    		System.out.println("No alarms");
	    	else
	    		System.out.println(alarms.size() + " alarms:");
	    	for (Iterator it = alarms.iterator(); it.hasNext(); ) {
	    		ReflectogramAlarm al = (ReflectogramAlarm)it.next();
	    		System.out.println("- " + al);
	    	}
	    	if (alarms.size() > 0) {
	    		ReflectogramAlarm first = (ReflectogramAlarm)
	    				alarms.iterator().next();
	    		Heap.setRefMismatch(first);
	    	} else {
	    		Heap.setRefMismatch(null);
	    	}
	    }
	}
}
