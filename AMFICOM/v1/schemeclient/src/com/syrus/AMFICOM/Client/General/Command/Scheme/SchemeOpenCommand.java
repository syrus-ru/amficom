package com.syrus.AMFICOM.Client.General.Command.Scheme;

import java.util.List;

import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.SchemeElementsEvent;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceChooserDialog;
import com.syrus.AMFICOM.Client.Schematics.UI.SchemeController;
import com.syrus.AMFICOM.administration.*;
import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.scheme.SchemeStorableObjectPool;
import com.syrus.AMFICOM.scheme.corba.Scheme;

public class SchemeOpenCommand extends VoidCommand
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
		ObjectResourceChooserDialog mcd = new ObjectResourceChooserDialog(SchemeController.getInstance(), "scheme");

		try {
			Identifier domain_id = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).getAccessIdentifier().domain_id);
			Domain domain = (Domain)ConfigurationStorableObjectPool.getStorableObject(
					domain_id, true);
			DomainCondition condition = new DomainCondition(domain, ObjectEntities.SCHEME_ENTITY_CODE);
			List schemes = SchemeStorableObjectPool.getStorableObjectsByCondition(condition, true);
			mcd.setContents(schemes);
		}
		catch (ApplicationException ex) {
			ex.printStackTrace();
		}

		mcd.setModal(true);
		mcd.setVisible(true);

		if(mcd.getReturnCode() == mcd.RET_CANCEL)
			return;

		if(mcd.getReturnCode() == mcd.RET_OK)
		{
			Scheme scheme = (Scheme)mcd.getReturnObject();
			aContext.getDispatcher().notify(new SchemeElementsEvent(this, scheme,
					SchemeElementsEvent.OPEN_PRIMARY_SCHEME_EVENT));
		}
	}
}
