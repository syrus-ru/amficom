/**
 * $Id: MapEditorAddSchemeToViewCommand.java,v 1.3 2004/10/19 10:41:03 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Map;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;

/**
 * добавить в вид схему из списка
 * 
 * 
 * 
 * @version $Revision: 1.3 $, $Date: 2004/10/19 10:41:03 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class MapEditorAddSchemeToViewCommand extends VoidCommand
{
	MapFrame mapFrame;
	ApplicationContext aContext;

	protected ObjectResource retObj;

	public MapEditorAddSchemeToViewCommand()
	{
	}

	public MapEditorAddSchemeToViewCommand(MapFrame mapFrame, ApplicationContext aContext)
	{
		this.mapFrame = mapFrame;
		this.aContext = aContext;
	}

	public void setParameter(String param, Object val)
	{
		if(param.equals("mapFrame"))
			this.mapFrame = (MapFrame)val;
	}

	public Object clone()
	{
		return new MapEditorAddSchemeToViewCommand(mapFrame, aContext);
	}

	public ObjectResource getReturnObject()
	{
		return this.retObj;
	}

	public void execute()
	{
		DataSourceInterface dataSource = aContext.getDataSource();

		if(dataSource == null)
			return;
/*
		aContext.getDispatcher().notify(new StatusMessageEvent(
				StatusMessageEvent.STATUS_MESSAGE,
				LangModelMap.getString("MapOpening")));

		new SchemeDataSourceImage(dataSource).LoadSchemes();

		ObjectResourceChooserDialog mcd = new ObjectResourceChooserDialog(dataSource, Scheme.typ);

		List dataSet = Pool.getList(Scheme.typ);
		ObjectResourceDisplayModel odm = Scheme.getDefaultDisplayModel();
		mcd.setContents(odm, dataSet);

		// отфильтровываем по домену
		ObjectResourceTableModel ortm = mcd.getTableModel();
		ortm.setDomainId(aContext.getSessionInterface().getDomainId());
		ortm.restrictToDomain(true);//ф-я фильтрации схем по домену
		ortm.fireTableDataChanged();

		mcd.setModal(true);
		mcd.setVisible(true);
		if(mcd.getReturnCode() == mcd.RET_CANCEL)
		{
			aContext.getDispatcher().notify(new StatusMessageEvent(
					StatusMessageEvent.STATUS_MESSAGE,
					LangModel.getString("Aborted")));
			setResult(Command.RESULT_CANCEL);
			return;
		}

		if(mcd.getReturnCode() == mcd.RET_OK)
		{
			retObj = mcd.getReturnObject();

			if(mapFrame == null)
			{
				System.out.println("mapviewer is NULL");
				setResult(Command.RESULT_NO);
			}
			else
			{
				mapFrame.getMapView().addScheme((Scheme )retObj);
				mapFrame.getContext().getDispatcher().notify(new MapEvent(
						mapFrame.getMapView(),
						MapEvent.MAP_VIEW_CHANGED));
				setResult(Command.RESULT_OK);
			}
			aContext.getDispatcher().notify(new StatusMessageEvent(
					StatusMessageEvent.STATUS_MESSAGE,
					LangModel.getString("Finished")));
		}
*/
	}

}
