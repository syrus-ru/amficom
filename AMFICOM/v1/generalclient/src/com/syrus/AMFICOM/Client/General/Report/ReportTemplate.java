package com.syrus.AMFICOM.Client.General.Report;

import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.Lang.LangModelReport;

import com.syrus.AMFICOM.Client.General.Filter.ObjectResourceFilter;

import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;

import com.syrus.AMFICOM.CORBA.Report.FirmedTextPane_Transferable;
import com.syrus.AMFICOM.CORBA.Report.ImagePane_Transferable;
import com.syrus.AMFICOM.CORBA.Report.RenderingObject_Transferable;
import com.syrus.AMFICOM.CORBA.Report.ReportTemplate_Transferable;
import com.syrus.AMFICOM.CORBA.Resource.Filter_Transferable;

import com.syrus.AMFICOM.Client.Resource.StubResource;
import com.syrus.AMFICOM.filter.LogicSchemeBase;

import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;

import java.awt.Dimension;
import java.awt.Font;

import java.util.HashMap;
import java.util.Map;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

/**
 * <p>Title: </p>
 * <p>Класс шаблона отчёта - включает в себя списки элементов, надписей и
 * фильтров, используемых в нём, а также средства их сериализации и подготовки
 * к передаче по CORBA. Также включает ряд служебных функций.</p>

 * <p>Тип шаблона характеризует из какого модуля по нему можно построить
 * отчёт </p>

 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Syrus Systems</p>
 * @author Песковский Пётр
 * @version 1.0
 */

public class ReportTemplate extends StubResource implements Serializable
{
	//общая часть - для совместимости с ObjectResource
	/**
	 * <p>Описатель класса</p>
	 */
	static final public String typ = "reporttemplate";

	/**
	 * <p>Шаблон по оценке</p>
	 */
	static final public String rtt_Evaluation = "rtt_Evaluation";
	/**
	 * <p>Шаблон по прогнозу</p>
	 */
	static final public String rtt_Prediction = "rtt_Prediction";
	/**
	 * <p>Шаблон по анализу</p>
	 */
	static final public String rtt_Analysis = "rtt_Analysis";
	/**
	 * <p>Шаблон по исследованию</p>
	 */
	static final public String rtt_Survey = "rtt_Survey";
	/**
	 * <p>Шаблон по моделированию</p>
	 */
	static final public String rtt_Modeling = "rtt_Modeling";
	/**
	 * <p>Шаблон по оптимизации</p>
	 */
	static final public String rtt_Optimization = "rtt_Optimization";
	/**
	 * <p>Шаблон по схеме</p>
	 */
	static final public String rtt_Scheme = "rtt_Scheme";
	/**
	 * <p>Шаблон по топологии</p>
	 */
	static final public String rtt_Map = "rtt_Map";
	/**
	 * <p>Шаблон по наблюдению</p>
	 */
	static final public String rtt_Observe = "rtt_Observe";
	/**
	 * <p>Шаблон по планированию</p>
	 */
	static final public String rtt_Scheduler = "rtt_Scheduler";
	/**
	 * <p>Все шаблоны</p>
	 */
	static final public String rtt_AllTemplates = "rtt_AllTemplates";

	static public Dimension A4 = new Dimension(827,1170);
	static public Dimension A3 = new Dimension(1170,1655);

	public String id = "";
	public String name = "";
	public String description = "";

	public long modified = 0L;
	public long curModified = 0L;

	// информация о текущем шаблоне
	public Dimension size = A4;
	public String templateType = "";

	/**
	 * Список всех элементов шаблона
	 */
	public List objectRenderers = new ArrayList();
	/**
	 * Список фильтров использующихся в шаблоне
	 */
	public List objectResourceFilters = new ArrayList();
	/**
	 * Список надписей из шаблона
	 */
	public List labels = new ArrayList();
	/**
	 * Список картинок из шаблона
	 */
	public List images = new ArrayList();

	/**
	 * В таблице хранится информация о том, какие ресурсы были подгружены,
	 * а какие - нет.
	 */
	public Map resourcesLoaded = new HashMap();

	ReportTemplate_Transferable transferable = new ReportTemplate_Transferable();

	/**
	 * Конструктор шаблона с заданным размером
	 * @param dsi Нужен для получения id
	 * @param nt_size Размер поля шаблона (A3,A4)
	 */
	public ReportTemplate(DataSourceInterface dsi,String nt_size)
	{
		initID_Size(dsi,nt_size);
		initResources();
	}

	/**
	 * Конструктор с выводом окна выбора размера шаблона
	 * @param dsi Нужен для получения id
	 * @throws CreateReportException если не был выбран размер
	 */
	public ReportTemplate(DataSourceInterface dsi)
			throws CreateReportException
	{
		Object[] selectValues = {"A4","A3"};
		Object resultValue = JOptionPane.showInputDialog(
				Environment.getActiveWindow(),
				LangModelReport.getString("label_chooseTemplateSize"),
				"",
				JOptionPane.QUESTION_MESSAGE,
				null,
				selectValues,
				selectValues[0]);
		if (resultValue == null)
			throw new CreateReportException ("",CreateReportException.generalError);

		initID_Size(dsi,(String)resultValue);
		initResources();
	}

	private void initID_Size (DataSourceInterface dsi,String nt_size)
	{
		if (nt_size.equals("A3"))
			size = ReportTemplate.A3;
		else if (nt_size.equals("A4"))
			size = ReportTemplate.A4;

		System.out.println(new Date(System.currentTimeMillis()).toString() +
											 " " + "Creating ReportTemplate...");
		this.id = dsi.GetUId(typ);

		System.out.println(new Date(System.currentTimeMillis()).toString() +
											 " " + "...done!");
	}

	private void initResources()
	{
		resourcesLoaded.put("alarmsLoaded","false");
		resourcesLoaded.put("netLoaded", "false");
		resourcesLoaded.put("netDirectoryLoaded", "false");
		resourcesLoaded.put("ismLoaded", "false");
		resourcesLoaded.put("schemeProtoLoaded", "false");
		resourcesLoaded.put("schemesLoaded", "false");
		resourcesLoaded.put("mapProtoElementsLoaded", "false");
		resourcesLoaded.put("mapsLoaded", "false");
		resourcesLoaded.put("schemeOptInfoLoaded", "false");
		resourcesLoaded.put("schemeMonSolutLoaded", "false");
		resourcesLoaded.put("testsLoaded", "false");
		resourcesLoaded.put("paramTypesLoaded", "false");
		resourcesLoaded.put("testTypesLoaded", "false");
		resourcesLoaded.put("analysisTypesLoaded", "false");
		resourcesLoaded.put("evaluationTypesLoaded", "false");
		resourcesLoaded.put("modelingTypesLoaded", "false");
	}

	/**
	 * Конструктор, восстанавливающий шаблон по информации с сервера
	 * @param rt_trans информация о шаблоне, сохраняемая на сервере
	 */
	public ReportTemplate(ReportTemplate_Transferable rt_trans)
	{
		this.transferable = rt_trans;
		this.setLocalFromTransferable();
		initResources();
	}

	public void setLabels(List newLabels)
	{
		this.labels = newLabels;
	}

	public Object getTransferable()
	{
		return transferable;
	}

	public String getName()
	{
		return name;
	}

	public String getId()
	{
		return id;
	}

	public String getTyp()
	{
		return typ;
	}

	public String getDomainId()
	{
		return "sysdomain";
	}

	public long getModified()
	{
		return modified;
	}

	public void setLocalFromTransferable()
	{
		this.name = transferable.name;
		this.description = transferable.description;
		this.id = transferable.id;
		this.templateType = transferable.template_type;
		this.modified = transferable.modified;
		this.curModified = transferable.modified;

		this.objectRenderers = new ArrayList();
		this.labels = new ArrayList();
		this.images = new ArrayList();
		this.objectResourceFilters = new ArrayList();

		for (int i = 0; i < this.transferable.renderingObjects.length; i++)
		{
			RenderingObject curRO =
						new RenderingObject(this.transferable.renderingObjects[i]);

			this.objectRenderers.add(curRO);
		}
		//Восстановили объекты теперь надписи

		for (int i = 0; i < this.transferable.firmedTextPanes.length; i++)
		{
			FirmedTextPane curLabel = new FirmedTextPane(
				this.transferable.firmedTextPanes[i],
				this.objectRenderers);

			this.labels.add(curLabel);
		}

		// Перекачиваем рисунки

		for (int i = 0; i < transferable.imagePanes.length; i++)
		{
			ImagePanel curImage = new ImagePanel(transferable.imagePanes[i]);

			this.images.add(curImage);
		}

		//Перекачиваем фильтры
		for (int i = 0; i < transferable.filters.length; i++)
		{
			Filter_Transferable ft = (Filter_Transferable)transferable.filters[i];

			try
			{
				ObjectResourceFilter curFilter =
					(ObjectResourceFilter) Class.forName(ft.resource_typ).newInstance();

				ByteArrayInputStream bais = new ByteArrayInputStream(ft.logic_scheme);
				ObjectInputStream ois = new ObjectInputStream(bais);
				curFilter.logicScheme.readObject(ois);
				ois.close();
				bais.close();

				this.objectResourceFilters.add(curFilter);
			}
			catch (Exception exc)
			{
				System.out.println("Error recreating filter object!!!");
			}
		}
	}

	public void setTransferableFromLocal()
	{
		this.modified = this.curModified;

		this.transferable = new ReportTemplate_Transferable();
		this.transferable.modified = this.curModified;
		this.transferable.name = this.name;
		this.transferable.id = this.id;
		this.transferable.template_type = this.templateType;
		this.transferable.description = this.description;

		// Перекачиваем объекты
		int orCount = this.objectRenderers.size();
		transferable.renderingObjects = new RenderingObject_Transferable[orCount];
		for (int i = 0; i < orCount; i++)
		{
			RenderingObject curRO = (RenderingObject) this.objectRenderers.get(i);

			byte[] reportBytes = null;
			try
			{
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(baos);
				oos.writeObject(curRO.getReportToRender());
				oos.flush();
				oos.close();
				baos.flush();
				baos.close();
				reportBytes = baos.toByteArray();
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}

			transferable.renderingObjects[i] = new RenderingObject_Transferable(
					curRO.id,
					curRO.x,
					curRO.y,
					curRO.width,
					curRO.height,
					reportBytes,
					curRO.getColumnWidths(),
					curRO.getTableDivisionsNumber());
		}

		// Перекачиваем надписи
		int labelCount = this.labels.size();
		transferable.firmedTextPanes = new FirmedTextPane_Transferable[labelCount];
		for (int i = 0; i < labelCount; i++)
		{
			FirmedTextPane curLabel = (FirmedTextPane) this.labels.get(i);

			String vertFiD = "";
			if (curLabel.vertFirmer != null)
				vertFiD = curLabel.vertFirmer.id;

			String horizFiD = "";
			if (curLabel.horizFirmer != null)
				horizFiD = curLabel.horizFirmer.id;

			String curLabelsFont = curLabel.getFont().toString();
			transferable.firmedTextPanes[i] = new FirmedTextPane_Transferable(
					curLabel.getX(),
					curLabel.getY(),
					curLabel.getText(),
					curLabelsFont,
					vertFiD,
					horizFiD,
					curLabel.verticalFirmTo,
					curLabel.horizontalFirmTo,
					curLabel.distanceX,
					curLabel.distanceY);
		}

		// Перекачиваем рисунки
		int imageCount = this.images.size();
		transferable.imagePanes = new ImagePane_Transferable[imageCount];
		for (int i = 0; i < imageCount; i++)
		{
			ImagePanel curImage = (ImagePanel) this.images.get(i);
			byte[] imageData = null;
			try
			{
				File imageFile = new File(curImage.fileName);
				FileInputStream fis = new FileInputStream (imageFile);
				imageData = new byte [(int)imageFile.length()];
				fis.read(imageData);
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}

			transferable.imagePanes[i] = new ImagePane_Transferable(
					curImage.getX(),
					curImage.getY(),
					curImage.getWidth(),
					curImage.getHeight(),
					imageData);
		}

		//Перекачиваем фильтры
		int filterCount = this.objectResourceFilters.size();
		transferable.filters = new Filter_Transferable[filterCount];

		for (int i = 0; i < filterCount; i++)
		{
			ObjectResourceFilter curFilter = (ObjectResourceFilter) this.objectResourceFilters.get(i);

			byte[] lsBytes = null;
			try
			{
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(baos);
				((LogicSchemeBase)(curFilter.logicScheme)).writeObject(oos);
				oos.flush();
				oos.close();
				baos.flush();
				baos.close();
				lsBytes = baos.toByteArray();
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}

			transferable.filters[i] =
				new Filter_Transferable(curFilter.getId(),curFilter.resource_typ,lsBytes);
		}
	}

	public void updateLocalFromTransferable()
	{

	}

	private void writeObject(java.io.ObjectOutputStream out)
			throws IOException
	{
		out.writeLong(this.modified);
		out.writeObject(this.name);
		out.writeObject(this.id);
		out.writeObject(this.templateType);
		out.writeObject(this.description);

		int labelCount = this.labels.size();
		int orCount = this.objectRenderers.size();
		int imagesCount = this.images.size();
		int filtersCount = this.objectResourceFilters.size();
		//Иициализируем массивы из transferable
		//для объектов

		// Перекачиваем объекты
		out.writeInt(orCount);
		for (int i = 0; i < orCount; i++)
		{
			RenderingObject curRO = (RenderingObject) this.objectRenderers.get(i);

			out.writeInt(curRO.x);
			out.writeInt(curRO.y);
			out.writeObject(curRO.id);

			out.writeInt(curRO.width);
			out.writeInt(curRO.height);

			out.writeObject(curRO.getReportToRender());

			int[] colWidths = curRO.getColumnWidths();
			out.writeInt(colWidths.length);
			for (int j = 0; j < colWidths.length; j++)
				out.writeInt(colWidths[j]);

			out.writeInt(curRO.getTableDivisionsNumber());
		}

		// Перекачиваем надписи
		out.writeInt(labelCount);
		for (int i = 0; i < labelCount; i++)
		{
			FirmedTextPane curLabel = (FirmedTextPane) this.labels.get(i);

			out.writeInt(curLabel.getX());
			out.writeInt(curLabel.getY());
			out.writeObject(curLabel.getText());
			out.writeObject(curLabel.labelsFont);
			if (curLabel.vertFirmer != null)
				out.writeObject(curLabel.vertFirmer.id);
			else
				out.writeObject("null");

			if (curLabel.horizFirmer != null)
				out.writeObject(curLabel.horizFirmer.id);
			else
				out.writeObject("null");

			out.writeObject(curLabel.verticalFirmTo);
			out.writeObject(curLabel.horizontalFirmTo);
			out.writeInt(curLabel.distanceX);
			out.writeInt(curLabel.distanceY);
		}

		// Перекачиваем картинки
		out.writeInt(imagesCount);
		for (int i = 0; i < imagesCount; i++)
		{
			ImagePanel curImage = (ImagePanel) this.images.get(i);

			out.writeInt(curImage.getX());
			out.writeInt(curImage.getY());
			out.writeInt(curImage.getWidth());
			out.writeInt(curImage.getHeight());

			out.writeObject(curImage.fileName);
		}

		// Перекачиваем фильтры
		out.writeInt(filtersCount);
		for (int i = 0; i < filtersCount; i++)
		{
			ObjectResourceFilter curFilter =
				(ObjectResourceFilter) this.objectResourceFilters.get(i);

			out.writeObject(curFilter.resource_typ);
			out.writeObject(curFilter.logicScheme);
		}
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{

		this.modified = in.readLong();

		this.name = (String)in.readObject();
		this.id = (String)in.readObject();
		this.templateType = (String)in.readObject();
		this.description = (String)in.readObject();

		// Перекачиваем объекты
		this.objectRenderers = new ArrayList();

		int orCount = in.readInt();
		for (int i = 0; i < orCount; i++)
		{
			int curRO_x = in.readInt();
			int curRO_y = in.readInt();
			String curRO_id = (String) in.readObject();
			int curRO_width = in.readInt();
			int curRO_height = in.readInt();

			RenderingObject curRO = new RenderingObject(curRO_x,curRO_y,new Dimension(curRO_width,curRO_height));
			curRO.id = curRO_id;

			curRO.setReportToRender((ObjectsReport) in.readObject());

			int[] colWidths = null;
			int cWLength = in.readInt();
			if (cWLength > 0)
			{
				colWidths = new int[cWLength];
				for (int j = 0; j < colWidths.length; j++)
					colWidths[j] = in.readInt();
			}

			curRO.setColumnWidths(colWidths);

			curRO.setTableDivisionsNumber(in.readInt());
			this.objectRenderers.add(curRO);
		}

		// Перекачиваем надписи
		this.labels = new ArrayList();

		int labelCount = in.readInt();
		for (int i = 0; i < labelCount; i++)
		{
			FirmedTextPane curLabel = new FirmedTextPane();
			int labelX = in.readInt();
			int labelY = in.readInt();
			curLabel.setLocation(labelX,labelY);

			curLabel.setText((String)in.readObject());
			curLabel.setFont((Font)in.readObject());

			String vertFirmerID = (String) in.readObject();// Объекты привязки
			String horizFirmerID = (String) in.readObject();
			String vertFirmingTo = (String) in.readObject();// типы привязки
			String horizFirmingTo = (String) in.readObject();

			if ( !vertFirmerID.equals("null")
				||!horizFirmerID.equals("null"))

				for (int j = 0; j < this.objectRenderers.size(); j++)
				{
					RenderingObject curRO = (RenderingObject) objectRenderers.get(j);
					if (!vertFirmerID.equals("null"))
						if (curRO.id.equals(vertFirmerID))
							curLabel.setVertFirmedTo(curRO, vertFirmingTo);

					if (!horizFirmerID.equals("null"))
						if (curRO.id.equals(horizFirmerID))
							curLabel.setHorizFirmedTo(curRO, horizFirmingTo);
				}

			curLabel.distanceX = in.readInt();
			curLabel.distanceY = in.readInt();

			this.labels.add(curLabel);
		}

		// Перекачиваем картинки
		this.images = new ArrayList();

		int imagesCount = in.readInt();
		for (int i = 0; i < imagesCount; i++)
		{
			int imageX = in.readInt();
			int imageY = in.readInt();
			int imageWidth = in.readInt();
			int imageHeight = in.readInt();
			String fileName = (String) in.readObject();

			ImagePanel curImage = null;

			try
			{
				curImage = new ImagePanel(imageX,imageY,fileName);
			}
			catch(CreateReportException exc)
			{
				System.out.println("Eror while reading images from file!");
			}
			curImage.setSize(imageWidth,imageHeight);
			curImage.setPreferredSize(curImage.getSize());

			this.images.add(curImage);
		}

		// Перекачиваем фильтры
		this.objectResourceFilters = new ArrayList();

		int filtersCount = in.readInt();
		for (int i = 0; i < filtersCount; i++)
		{
			String resource_typ = (String) in.readObject();
			try
			{
				ObjectResourceFilter curFilter =
					(ObjectResourceFilter) Class.forName(resource_typ).newInstance();

				curFilter.logicScheme.readObject(in);

				this.objectResourceFilters.add(curFilter);
			}
			catch (Exception exc)
			{
				System.out.println("Error recreating Filter object!!!");
			}
		}

		initResources();
	}

	/**
	 * @param report рассматриваемый отчёт
	 * @return элемент шаблона, содержащий данный отчёт
	 */
	public RenderingObject findROforReport (ObjectsReport report)
	{
		for (int i = 0; i < objectRenderers.size(); i++)
		{
			RenderingObject curRO = (RenderingObject)objectRenderers.get(i);
			if (curRO.getReportToRender().equals(report))
				return curRO;
		}

		return null;
	}
}
