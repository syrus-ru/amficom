package com.syrus.AMFICOM.Client.General.Command.Config;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.StatusMessageEvent;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.ISM.KIS;
import com.syrus.AMFICOM.Client.Resource.ISM.TransmissionPath;
import com.syrus.AMFICOM.Client.Resource.Network.CableLink;
import com.syrus.AMFICOM.Client.Resource.Network.Equipment;
import com.syrus.AMFICOM.Client.Resource.Network.Link;
import com.syrus.AMFICOM.Client.Resource.Pool;

import java.util.Enumeration;
import java.util.*;

public class ImportCatalogCommand extends VoidCommand
{
	ApplicationContext aContext;

	public ImportCatalogCommand()
	{
	}

	public ImportCatalogCommand(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	public Object clone()
	{
		return new ImportCatalogCommand(aContext);
	}

	public void execute()
	{
		DataSourceInterface dataSource = aContext.getDataSourceInterface();
		if(dataSource == null)
			return;

		Dispatcher disp = aContext.getDispatcher();

		String[] ids;
		Map ht;

		ht = Pool.getMap(Equipment.typ);
		if(ht != null)
			for(Iterator it = ht.values().iterator(); it.hasNext();)
			{
				Equipment or = (Equipment)it.next();
				System.out.println("Saving " + or.getName() + " (" + or.getId() + ")...");
				if(disp != null)
					disp.notify(new StatusMessageEvent(StatusMessageEvent.STATUS_MESSAGE, "Saving " + or.getName() + " (" + or.getId() + ")..."));
				dataSource.SaveEquipment(or.getId());
			}

		ht = Pool.getMap(KIS.typ);
		if(ht != null)
			for(Iterator it = ht.values().iterator(); it.hasNext();)
			{
				KIS or = (KIS)it.next();
				System.out.println("Saving " + or.getName() + " (" + or.getId() + ")...");
				if(disp != null)
					disp.notify(new StatusMessageEvent(StatusMessageEvent.STATUS_MESSAGE, "Saving " + or.getName() + " (" + or.getId() + ")..."));
				dataSource.SaveKIS(or.getId());
			}

		ht = Pool.getMap(Link.typ);
		if(ht != null)
			for(Iterator it = ht.values().iterator(); it.hasNext();)
			{
				Link or = (Link)it.next();
				System.out.println("Saving " + or.getName() + " (" + or.getId() + ")...");
				if(disp != null)
					disp.notify(new StatusMessageEvent(StatusMessageEvent.STATUS_MESSAGE, "Saving " + or.getName() + " (" + or.getId() + ")..."));
				dataSource.SaveLink(or.getId());
			}

		ht = Pool.getMap(CableLink.typ);
		if(ht != null)
			for(Iterator it = ht.values().iterator(); it.hasNext();)
			{
				CableLink or = (CableLink)it.next();
				System.out.println("Saving " + or.getName() + " (" + or.getId() + ")...");
				if(disp != null)
					disp.notify(new StatusMessageEvent(StatusMessageEvent.STATUS_MESSAGE, "Saving " + or.getName() + " (" + or.getId() + ")..."));
				dataSource.SaveCableLink(or.getId());
			}

		ht = Pool.getMap(TransmissionPath.typ);
		if(ht != null)
			for(Iterator it = ht.values().iterator(); it.hasNext();)
			{
				TransmissionPath or = (TransmissionPath)it.next();
				System.out.println("Saving " + or.getName() + " (" + or.getId() + ")...");
				if(disp != null)
					disp.notify(new StatusMessageEvent(StatusMessageEvent.STATUS_MESSAGE, "Saving " + or.getName() + " (" + or.getId() + ")..."));
				dataSource.SavePath(or.getId());
			}

		if(disp != null)
			disp.notify(new StatusMessageEvent(StatusMessageEvent.STATUS_MESSAGE, "Done!"));

	}

}