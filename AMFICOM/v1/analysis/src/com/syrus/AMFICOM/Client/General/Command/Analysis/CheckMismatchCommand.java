/*-
 * $Id: CheckMismatchCommand.java,v 1.8 2005/10/16 16:16:30 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.General.Command.Analysis;

import java.util.List;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.analysis.CoreAnalysisManager;
import com.syrus.AMFICOM.analysis.EtalonComparison;
import com.syrus.AMFICOM.analysis.dadara.AnalysisResult;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramMismatchImpl;
import com.syrus.AMFICOM.client.model.AbstractCommand;

public class CheckMismatchCommand extends AbstractCommand {
	@Override
	public void execute() {
		if (Heap.getPFTracePrimary() != null
				&& Heap.getMinuitAnalysisParams() != null
				&& Heap.hasEtalon()) {
			final AnalysisResult ar = Heap.getRefAnalysisPrimary().getAR();
			EtalonComparison ecomp = CoreAnalysisManager.compareToEtalon(
					ar,
					Heap.getEtalon());
			Heap.setEtalonComparison(ecomp);
			List<ReflectogramMismatchImpl> alarms =
				ecomp.getAlarms();
			// FIXME: debug-time console alarm listing
			if (alarms.size() == 0)
				System.out.println("No alarms");
			else
				System.out.println(alarms.size() + " alarms:");
			for (ReflectogramMismatchImpl al: alarms) {
				System.out.println("- " + al);
			}
		}
	}
}
