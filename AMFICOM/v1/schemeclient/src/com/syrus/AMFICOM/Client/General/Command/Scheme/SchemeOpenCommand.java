package com.syrus.AMFICOM.Client.General.Command.Scheme;

import com.syrus.AMFICOM.client.model.*;

public class SchemeOpenCommand extends AbstractCommand
{
	ApplicationContext aContext;

	public SchemeOpenCommand(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	public Object clone()
	{
		return new SchemeOpenCommand(aContext);
	}

	public void execute()
	{
		/*
		ObjectResourceChooserDialog mcd = new ObjectResourceChooserDialog(LangModelSchematics.getString("scheme"), SchemeController.getInstance());

		try {
			Identifier domain_id = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).getAccessIdentifier().domain_id);
			LinkedIdsCondition condition = new LinkedIdsCondition(domain_id, ObjectEntities.SCHEME_CODE);
			Set schemes = StorableObjectPool.getStorableObjectsByCondition(condition, true);
			mcd.setContents(schemes);
		}
		catch (ApplicationException ex) {
			ex.printStackTrace();
		}

		mcd.setModal(true);
		mcd.setVisible(true);

		if(mcd.getReturnCode() == ObjectResourceChooserDialog.RET_CANCEL)
			return;

		if(mcd.getReturnCode() == ObjectResourceChooserDialog.RET_OK)
		{
			Scheme scheme = (Scheme)mcd.getReturnObject();
			aContext.getDispatcher().notify(new SchemeElementsEvent(this, scheme,
					SchemeElementsEvent.OPEN_PRIMARY_SCHEME_EVENT));
		}*/
	}
}
