package com.syrus.AMFICOM.Client.General.Command.Scheme;


import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.CreatePathEvent;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Scheme.*;
import com.syrus.AMFICOM.Client.Resource.MiscUtil;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.general.corba.*;
import com.syrus.AMFICOM.scheme.SchemePath;
import com.syrus.util.Log;

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
		Identifier userId = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).getAccessIdentifier().user_id);

		SchemePath path;
		try {
			path = SchemePath.createInstance(userId);
		} 
		catch (CreateObjectException e) {
			Log.errorException(e);
			return;
		}
		
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
					path.getId(), true, true);
			path.addCharacteristic(ea);
		}
		catch (CreateObjectException ex) {
			ex.printStackTrace();
		}
		aContext.getDispatcher().notify(new CreatePathEvent(pane.getPanel(), null,
				CreatePathEvent.CREATE_PATH_EVENT));
}
}
