package com.syrus.AMFICOM.Client.General.Command.Scheme;

import com.syrus.AMFICOM.client.model.AbstractCommand;


public class PathNewCommand extends AbstractCommand
{
	/*ApplicationContext aContext;
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
		SchemeGraph graph = pane.getGraph();
		if (pane.getCurrentPanel().getSchemeResource().getSchemePath() == null)
			return;
		Identifier userId = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).getAccessIdentifier().user_id);

		SchemePath path;
		try {
			path = SchemePath.createInstance(userId, "");
		} 
		catch (CreateObjectException e) {
			Log.errorException(e);
			return;
		}
		
		path.setScheme(pane.getCurrentPanel().getSchemeResource().getScheme());
//		graph.setCurrentPath(path);

		Identifier user_id = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).
																				getAccessIdentifier().user_id);
		CharacteristicType type = MiscUtil.getCharacteristicType(
				user_id, "alarmed", CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL,
				DataType.DATA_TYPE_STRING);

		try {
			Characteristic ea = Characteristic.createInstance(user_id, type, "Сигнал тревоги", "",
					CharacteristicSort.CHARACTERISTIC_SORT_SCHEMEPATH, "false",
					path.getId(), true, true);
			path.addCharacteristic(ea);
		}
		catch (CreateObjectException ex) {
			ex.printStackTrace();
		}
		aContext.getDispatcher().notify(new CreatePathEvent(pane, null,
				CreatePathEvent.CREATE_PATH_EVENT));
}*/
}
