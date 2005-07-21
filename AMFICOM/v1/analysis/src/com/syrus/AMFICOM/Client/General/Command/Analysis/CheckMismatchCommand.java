/*-
 * $Id: CheckMismatchCommand.java,v 1.4 2005/07/21 14:06:46 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.General.Command.Analysis;

import java.util.List;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.analysis.CoreAnalysisManager;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramMismatch;
import com.syrus.AMFICOM.client.model.AbstractCommand;

public class CheckMismatchCommand extends AbstractCommand {
	@Override
	public void execute() {
	    if (Heap.getBSPrimaryTrace() != null
	    		&& Heap.getMinuitAnalysisParams() != null
	    		&& Heap.hasEtalon()) {
	    	List<ReflectogramMismatch> alarms =
	    		CoreAnalysisManager.compareAndMakeAlarms(
	    			Heap.getRefAnalysisPrimary().getAR(),
	    			Heap.getEtalon());
	    	if (alarms.size() > 0) {
	    		ReflectogramMismatch first = alarms.iterator().next();
	    		Heap.setRefMismatch(first);
	    	} else {
	    		Heap.setRefMismatch(null);
	    	}
	    	// FIXME: debug-time console alarm listing
	    	if (alarms.size() == 0)
	    		System.out.println("No alarms");
	    	else
	    		System.out.println(alarms.size() + " alarms:");
	    	for (ReflectogramMismatch al: alarms) {
	    		System.out.println("- " + al);
	    	}
	    }
	}
}
