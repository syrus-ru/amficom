package com.syrus.AMFICOM.Client.General.Command.Scheme;


import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.CreatePathEvent;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Scheme.SchemeFactory;
import com.syrus.AMFICOM.Client.General.Scheme.SchemeGraph;
import com.syrus.AMFICOM.Client.General.Scheme.SchemeTabbedPane;
import com.syrus.AMFICOM.Client.Resource.MiscUtil;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.CharacteristicType;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import com.syrus.AMFICOM.general.corba.CharacteristicTypeSort;
import com.syrus.AMFICOM.general.corba.DataType;
import com.syrus.AMFICOM.scheme.corba.SchemePath;

public class PathNewCommand extends VoidCommand
{
	ApplicationContext aContext;
	SchemeTabbedPane pane;

	public PathNewCommand(ApplicationContext aContext, SchemeTabbedPane pane)
	{
		this.aContext = aContext;
		this.pane = pane;
	}

	public Object clone()
	{
		return new PathNewCommand(aContext, pane);
	}

	public void execute()
	{
		SchemeGraph graph = pane.getPanel().getGraph();
		if (graph.getScheme() == null)
			return;
		SchemePath path = SchemeFactory.createPath();
		path.scheme(graph.getScheme());
		graph.setCurrentPath(path);

		Identifier user_id = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).
																				getAccessIdentifier().user_id);
		CharacteristicType type = MiscUtil.getCharacteristicType(
				user_id, "alarmed", CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL,
				DataType.DATA_TYPE_STRING);

		try {
			Characteristic ea = Characteristic.createInstance(user_id, type, "Сигнал тревоги", "",
					CharacteristicSort._CHARACTERISTIC_SORT_SCHEMEPATH, "false",
					new Identifier(path.id().transferable()), true, true);
			path.addCharacteristic(ea);
		}
		catch (CreateObjectException ex) {
			ex.printStackTrace();
		}
		aContext.getDispatcher().notify(new CreatePathEvent(pane.getPanel(), null,
				CreatePathEvent.CREATE_PATH_EVENT));
}
}
