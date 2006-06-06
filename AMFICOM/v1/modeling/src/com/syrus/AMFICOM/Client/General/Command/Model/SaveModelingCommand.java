package com.syrus.AMFICOM.Client.General.Command.Model;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.measurement.Modeling;
import com.syrus.AMFICOM.measurement.ModelingType;
import com.syrus.AMFICOM.measurement.MonitoredElement;
import com.syrus.AMFICOM.measurement.Parameter;
import com.syrus.AMFICOM.measurement.ParameterSet;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.AMFICOM.measurement.corba.IdlParameterSetPackage.ParameterSetSort;
import com.syrus.AMFICOM.scheme.AbstractSchemePort;
import com.syrus.AMFICOM.scheme.PathElement;
import com.syrus.AMFICOM.scheme.SchemePath;
import com.syrus.io.BellcoreStructure;
import com.syrus.io.BellcoreWriter;
import com.syrus.util.Log;

public class SaveModelingCommand extends AbstractCommand {
	ApplicationContext aContext;
	String traceid;

	public SaveModelingCommand(ApplicationContext aContext, String id) {
		this.aContext = aContext;
		this.traceid = id;
	}

	@Override
	public void execute() {
		BellcoreStructure bs = Heap.getPFTracePrimary().getBS();
		if(bs == null) {
			JOptionPane.showMessageDialog(Environment.getActiveWindow(),
					"Ошибка при сохранении модели", 
					"Ошибка", 
					JOptionPane.OK_OPTION);
			return;
		}
		
		if(bs.monitoredElementId == null) {
			if (bs.schemePathId != null) {
				try {
					SchemePath schemePath = StorableObjectPool.getStorableObject(Identifier.valueOf(bs.schemePathId), false);
					
					SortedSet<PathElement> pathMemebers = schemePath.getPathMembers();
					if (!pathMemebers.isEmpty()) {
						AbstractSchemePort startPort = pathMemebers.first().getEndAbstractSchemePort();
						if (startPort != null) {
							Identifier measurementPortId = startPort.getMeasurementPortId();
							if (!measurementPortId.isVoid()) {
								LinkedIdsCondition condition = new LinkedIdsCondition(measurementPortId, ObjectEntities.MONITOREDELEMENT_CODE);
								Set<MonitoredElement> mes = StorableObjectPool.getStorableObjectsByCondition(condition, true);
								if (!mes.isEmpty()) {
									bs.monitoredElementId = mes.iterator().next().getId().getIdentifierString();
								}
							}
						}
					}
//					TransmissionPath tPath = path.getTransmissionPath();
//					if (tPath != null) {
//						LinkedIdsCondition condition = new LinkedIdsCondition(tPath.getId(), 
//								ObjectEntities.MONITOREDELEMENT_CODE);
//						Set<MonitoredElement> mes = StorableObjectPool.getStorableObjectsByCondition(condition, true); 
//						Set<Identifier> meIds = tPath.getMonitoredElementIds();
//						if (!mes.isEmpty()) {
//							bs.monitoredElementId = mes.iterator().next().getId().getIdentifierString();
//						}
//					}
				}	catch (ApplicationException e) { 
					Log.errorMessage(e);
				}
			}
		}
		
		if(bs.monitoredElementId == null) {
			JOptionPane.showMessageDialog(Environment.getActiveWindow(),
					"Нельзя сохранить модель, не связанную с измерительным устройством", 
					"Ошибка", 
					JOptionPane.OK_OPTION);
			return;
		}

		Identifier userId = LoginManager.getUserId();

		Set<Identifier> meIds = new HashSet<Identifier>();
		meIds.add(Identifier.valueOf(bs.monitoredElementId));

		try {
			Parameter[] parameters = new Parameter[1];
			parameters[0] = Parameter.createInstance(
					ParameterType.REFLECTOGRAMMA,
					new BellcoreWriter().write(bs));
			
			ParameterSet argumentSet = ParameterSet.createInstance(
					userId,
					ParameterSetSort.SET_SORT_MODELING_PARAMETERS,
					"",
					parameters,
					meIds);
			
			String title = bs.title + " (" + DateFormat.getDateTimeInstance().format(new Date(System.currentTimeMillis()))+ ")";
			Modeling m = Modeling.createInstance(
					userId,
					ModelingType.DADARA_MODELING,
					Identifier.valueOf(bs.monitoredElementId),
					title,
					argumentSet);
			
			Result r = m.createResult(
					userId,
					parameters);
			
			StorableObjectPool.flush(m, userId, false);
			StorableObjectPool.flush(r, userId, false);
			
			JOptionPane.showMessageDialog(
					Environment.getActiveWindow(),
					"Модель сохранена под именем :" + title,
					"Сообщение",
					JOptionPane.INFORMATION_MESSAGE);
		} catch (ApplicationException e) {
			Log.errorMessage(e);
		}
	}
}