package com.syrus.AMFICOM.Client.General.Report;

import com.syrus.AMFICOM.Client.Resource.StubResource;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.DataFlavor;

import java.io.IOException;
import java.io.Serializable;

import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.MyDataFlavor;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class ObjectsReport extends StubResource
	implements Transferable,Serializable
{
	public ReportModel model = null;
	public String field = "";
	public String view_type = "";

	public boolean isToBeFilledImmediately = true;

	private Object reserve = null;
	public String reserveName = "";

	public ObjectsReport(ReportModel model,String field, String vT,boolean toFillNow)
	{
		this.model = model;
		this.field = field;
		this.view_type = vT;
		this.isToBeFilledImmediately = toFillNow;
	}

	public Object clone()
	{
		//Резерв не копируется!!!!
		ObjectsReport newReport = new ObjectsReport(
				this.model,
				this.field,
				this.view_type,
				this.isToBeFilledImmediately);

		if (reserve instanceof String)
		{
			try
			{
				newReport.setReserve((String)reserve);
			}
			catch (CreateReportException cre){}
		}

		else if (reserve instanceof List)
    {
      List cloneList = new ArrayList();
      cloneList.addAll((List)reserve);
			newReport.reserve = cloneList;
    }
		return newReport;
	}

	public String getName()
	{
		//Текстовое отображение - в списках, RenderingObject'ах
		return model.getReportsName(this);
	}

	public void setReserve(Object reserve)
			throws CreateReportException
	{
		this.reserve = reserve;

		if (reserve instanceof String)
			reserveName = this.model.getReportsReserveName(this);
	}

	public Object getReserve()
	{
		return this.reserve;
	}

	public Object getTransferable(){return null;}
	public String getId(){return null;}
	public long getModified(){return 0L;}
	public String getTyp(){return "objectsreport";}
	public String getDomainId(){return "sysdomain";}
	public void setLocalFromTransferable(){}
	public void setTransferableFromLocal(){}
	public void updateLocalFromTransferable(){}

//////////////////////////////////////////////////
	public Object getTransferData(DataFlavor flavor)
	{
		if (flavor.getHumanPresentableName().equals("ObjectReportLabel"))
		{
			return (Object) (this);
		}
		return null;
	}

	public DataFlavor[] getTransferDataFlavors()
	{
		DataFlavor myDataFlavor = new MyDataFlavor(this.getClass(),"ObjectReportLabel");
		DataFlavor[] dfs = new DataFlavor[2];
		dfs[0] = myDataFlavor;
		dfs[1] = DataFlavor.getTextPlainUnicodeFlavor();

		return dfs;
	}

	public boolean isDataFlavorSupported(DataFlavor flavor)
	{
		return (flavor.getHumanPresentableName().equals("ObjectReportLabel"));
	}

	private void writeObject(java.io.ObjectOutputStream out)
			throws IOException
	{
		out.writeObject(this.model.getClass().getName());

		out.writeObject(this.field);
		out.writeObject(this.view_type);
		out.writeBoolean(this.isToBeFilledImmediately);

		out.writeObject(this.reserve);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		try
		{
			String modelClassName = (String) in.readObject();
			this.model = (ReportModel) Class.forName(modelClassName).newInstance();
		}
		catch (Exception exc)
		{
			System.out.println("Error reading ReportModel object!!!");
		}

		this.field = (String) in.readObject();
		this.view_type = (String) in.readObject();
		this.isToBeFilledImmediately = in.readBoolean();

		this.reserve = in.readObject();
	}
}
