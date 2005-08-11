package com.syrus.AMFICOM.Client.General.Command.Scheme;

import java.util.Set;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.General.Event.ObjectSelectedEvent;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client_.scheme.SchemeObjectsFactory;
import com.syrus.AMFICOM.client_.scheme.graph.ElementsPanel;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeTabbedPane;
import com.syrus.AMFICOM.client_.scheme.ui.SchemePathPropertiesManager;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeMonitoringSolution;
import com.syrus.AMFICOM.scheme.SchemePath;
import com.syrus.util.Log;

public class PathNewCommand extends AbstractCommand {
	SchemeTabbedPane pane;

	public PathNewCommand(SchemeTabbedPane pane) {
		this.pane = pane;
	}

	public void execute() {
		ElementsPanel panel = this.pane.getCurrentPanel();
		Scheme scheme = panel.getSchemeResource().getScheme();
		
		if (scheme == null) {
			JOptionPane.showMessageDialog(this.pane, 
					LangModelScheme.getString("Message.error.path_create_on_schemeelement"),  //$NON-NLS-1$
					LangModelScheme.getString("Message.error"), //$NON-NLS-1$
					JOptionPane.OK_OPTION);
			return;
		}
		
		try {
			SchemeMonitoringSolution solution = null;
			Set<SchemeMonitoringSolution> solutions = scheme.getSchemeMonitoringSolutions();
			if(solutions.isEmpty()) {
				solution = SchemeObjectsFactory.createSchemeMonitoringSolution(scheme);
			} else {
				solution = solutions.iterator().next();
			}
			SchemePath path = SchemeObjectsFactory.createSchemePath(solution);
//		CharacteristicType type = MiscUtil.getCharacteristicType(
//				user_id, "alarmed", CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL,
//				DataType.DATA_TYPE_STRING);
//		Characteristic ea = Characteristic.createInstance(user_id, type, "Сигнал тревоги", "",
//					CharacteristicSort.CHARACTERISTIC_SORT_SCHEMEPATH, "false",
//					path.getId(), true, true);
//			path.addCharacteristic(ea);

			ApplicationContext aContext = this.pane.getContext();
			aContext.getDispatcher().firePropertyChange(new ObjectSelectedEvent(this, path,
					SchemePathPropertiesManager.getInstance(aContext),
					ObjectSelectedEvent.SCHEME_PATH));
		} catch (CreateObjectException e) {
			Log.errorException(e);
		}
	}
}
