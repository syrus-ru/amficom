package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.configuration.corba.IdlMonitoredElementPackage.MonitoredElementSort;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.scheme.SchemePath;
import com.syrus.io.BellcoreStructure;

public class PathElementsFrame extends AnalysisFrame {
	private static final long serialVersionUID = 4137589435411835248L;
	ApplicationContext aContext;

	public PathElementsFrame(ApplicationContext aContext, Dispatcher dispatcher, AnalysisLayeredPanel panel) {
		super(dispatcher, panel);
		this.aContext = aContext;
	}

	public PathElementsFrame(ApplicationContext aContext, Dispatcher dispatcher) {
		this(aContext, dispatcher, new PathElementsLayeredPanel(dispatcher));
	}

	
	@Override
	protected AnalysisPanel createSpecificAnalysisPanel(BellcoreStructure bs) {
		double deltaX = bs.getResolution();
		double[] y = bs.getTraceData();

		SchemePath path = null;
		try {
			MonitoredElement me = StorableObjectPool.getStorableObject(new Identifier(bs.monitoredElementId), true);

			if (me.getSort().equals(MonitoredElementSort.MONITOREDELEMENT_SORT_TRANSMISSION_PATH)) {
				LinkedIdsCondition condition = new LinkedIdsCondition(LoginManager.getDomainId(), ObjectEntities.SCHEMEPATH_CODE);
				Set paths = StorableObjectPool.getStorableObjectsByCondition(condition, true);

				Collection tpathIds = me.getMonitoredDomainMemberIds();
				for (Iterator it = paths.iterator(); it.hasNext();) {
					SchemePath sp = (SchemePath) it.next();
					/**
					 * @todo remove comment when SchemePath moves to new TransmissionPath
					 */
					if (sp.getTransmissionPath() != null && tpathIds.contains(sp.getTransmissionPath().getId())) {
						path = sp;
						break;
					}
				}
			}
			setTitle(me.getName());
		} catch (Exception ex) {
			setTitle(LangModelAnalyse.getString("analysisTitle"));
		}

		PathElementsPanel p = new PathElementsPanel((PathElementsLayeredPanel) this.panel, this.dispatcher, y, deltaX);
		if (path != null) {
			p.setPath(path);
		}
		return p;
	}
}
