package com.syrus.AMFICOM.Client.Resource;

import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.SessionInterface;
import com.syrus.AMFICOM.Client.General.UI.ChoosableFileFilter;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.Client.Resource.General.ElementAttributeType;
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.Map.MapLinkProtoElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeProtoElement;

import java.awt.Color;
import java.awt.Dimension;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class EmptyMapDataSource
		extends EmptyDataSource
		implements DataSourceInterface
{
	protected EmptyMapDataSource()
	{
	}

	public EmptyMapDataSource(SessionInterface si)
	{
		super(si);
	}

	protected static int idcounter = 0;

	public String GetUId(String type)
	{
		int len = Math.min(5, type.length());
		return type.substring(0, len) + idcounter++;
	}

	public void loadMapProtoElements(String[] eids, String[] lids)
	{
		LoadMapProtoElements();
	}
	
	public void LoadMapProtoElements()
	{
		if(getSession() == null)
			return;
		if(!getSession().isOpened())
			return;
			
		MapNodeProtoElement mnpe;
		MapLinkProtoElement mlpe;

		ImageCatalogue.add(
				"node",
				new ImageResource("node", "node", "images/node.gif"));
		ImageCatalogue.add(
				"void",
				new ImageResource("void", "void", "images/void.gif"));

		ImageCatalogue.add(
				"well",
				new ImageResource("well", "well", "images/well.gif"));
		ImageCatalogue.add(
				"wins",
				new ImageResource("wins", "wins", "images/wins.gif"));
		ImageCatalogue.add(
				"pc",
				new ImageResource("pc", "pc", "images/pc.gif"));
		ImageCatalogue.add(
				"net",
				new ImageResource("net", "net", "images/net.gif"));
		ImageCatalogue.add(
				"unbound",
				new ImageResource("unbound", "unbound", "images/unbound.gif"));
		ImageCatalogue.add(
				"mark",
				new ImageResource("mark", "mark", "images/mark.gif"));

	    mnpe = new MapNodeProtoElement(
			"building",
			"Дом",
			true,
			"pc",
			"descrioption");
		Pool.put(MapNodeProtoElement.typ, mnpe.getId(), mnpe);

	    mnpe = new MapNodeProtoElement(
			"well",
			"Колодец",
			true,
			"well",
			"descrioption");
		Pool.put(MapNodeProtoElement.typ, mnpe.getId(), mnpe);

		mnpe = new MapNodeProtoElement(
			"unbound",
			"Непривязанный элемент",
			true,
			"unbound",
			"desc");
		Pool.put(MapNodeProtoElement.typ, mnpe.getId(), mnpe);

	    mnpe = new MapNodeProtoElement(
			"net",
			"Сеть",
			true,
			"net",
			"descrioption");
		Pool.put(MapNodeProtoElement.typ, mnpe.getId(), mnpe);

	    mnpe = new MapNodeProtoElement(
			"khren",
			"Хрень",
			false,
			"wins",
			"description");
		Pool.put(MapNodeProtoElement.typ, mnpe.getId(), mnpe);

	    mlpe = new MapLinkProtoElement(
			"truba",
			"Труба",
			"desc",
			new Dimension(10, 14));
		mlpe.setLineSize(2);
		mlpe.setColor(Color.BLACK);
		Pool.put(MapLinkProtoElement.typ, mlpe.getId(), mlpe);

	    mlpe = new MapLinkProtoElement(
			"collector",
			"Коллектор",
			"desc",
			new Dimension(30, 40));
		mlpe.setLineSize(4);
		mlpe.setColor(Color.DARK_GRAY);
		Pool.put(MapLinkProtoElement.typ, mlpe.getId(), mlpe);

	    mlpe = new MapLinkProtoElement(
			"cable",
			"Непривязанный кабель",
			"desc",
			new Dimension(0, 0));
		mlpe.setLineSize(1);
		mlpe.setColor(Color.RED);
		Pool.put(MapLinkProtoElement.typ, mlpe.getId(), mlpe);
	}

	public void LoadAttributeTypes()
	{
		ElementAttributeType atype;

		if(getSession() == null)
			return;
		if(!getSession().isOpened())
			return;

		atype = new ElementAttributeType(
				"color",
				"Цвет",
				"integer",
				String.valueOf(MapPropertiesManager.getColor().getRGB()));
		Pool.put(ElementAttributeType.typ, atype.getId(), atype);

		atype = new ElementAttributeType(
				"alarmed_color",
				"Цвет сигнала тревоги",
				"integer",
				String.valueOf(MapPropertiesManager.getAlarmedColor().getRGB()));
		Pool.put(ElementAttributeType.typ, atype.getId(), atype);

		atype = new ElementAttributeType(
				"style",
				"Стиль",
				"string",
				MapPropertiesManager.getStyle());
		Pool.put(ElementAttributeType.typ, atype.getId(), atype);

		atype = new ElementAttributeType(
				"thickness",
				"Толщина",
				"integer",
				String.valueOf(MapPropertiesManager.getThickness()));
		Pool.put(ElementAttributeType.typ, atype.getId(), atype);

		atype = new ElementAttributeType(
				"selected_thickness",
				"Толщина при выборе",
				"integer",
				String.valueOf(MapPropertiesManager.getSelectionThickness()));
		Pool.put(ElementAttributeType.typ, atype.getId(), atype);

		atype = new ElementAttributeType(
				"alarmed_thickness",
				"Толщина с сигналом тревоги",
				"integer",
				String.valueOf(MapPropertiesManager.getAlarmedThickness()));
		Pool.put(ElementAttributeType.typ, atype.getId(), atype);

		atype = new ElementAttributeType(
				"font",
				"Шрифт",
				"string",
				MapPropertiesManager.getFont().getFontName());
		Pool.put(ElementAttributeType.typ, atype.getId(), atype);

		atype = new ElementAttributeType(
				"metric",
				"Метрика",
				"string",
				MapPropertiesManager.getMetric());
		Pool.put(ElementAttributeType.typ, atype.getId(), atype);
	}

	public void LoadAttributeTypes(String[] ids)
	{
		LoadAttributeTypes();
	}

	public void loadMaps(String[] ids)
	{
		if(getSession() == null)
			return;
		if(!getSession().isOpened())
			return;

		String fileName = openFileForReading();
		if(fileName == null)
		{
			JOptionPane.showMessageDialog(
					Environment.getActiveWindow(),
					"Ошибка чтения");
		}
		try
		{
			FileInputStream in = new FileInputStream(fileName);
			ObjectInputStream ias = new ObjectInputStream(in);
			Map mc = (Map )ias.readObject();
			ias.close();
			
			Pool.put(Map.typ, mc.getId(), mc);
		}
		catch(FileNotFoundException e)
		{
		}
		catch(ClassNotFoundException e)
		{
		}
		catch(IOException e)
		{
		}
	}
	
	public void SaveMaps(String[] mc_ids)
	{
		Map mc = (Map )Pool.get(Map.typ, mc_ids[0]);

		if(getSession() == null)
			return;
		if(!getSession().isOpened())
			return;
			
		String fileName = openFileForWriting();
		if(fileName == null)
		{
			JOptionPane.showMessageDialog(
					Environment.getActiveWindow(),
					"Ошибка записи");
		}
		try
		{
			FileOutputStream out = new FileOutputStream(fileName);
			ObjectOutputStream oas = new ObjectOutputStream(out);
			oas.writeObject(mc);
			oas.flush();
			oas.close();
		}
		catch(FileNotFoundException e)
		{
		}
		catch(IOException e)
		{
		}
	}

	String openFileForWriting()
	{
		String fileName = null;
		JFileChooser fileChooser = new JFileChooser();

		ChoosableFileFilter filter =
			new ChoosableFileFilter(
			"esf",
			"Empty session Save File");
		fileChooser.addChoosableFileFilter(filter);

		int option = fileChooser.showSaveDialog(Environment.getActiveWindow());
		if (option == JFileChooser.APPROVE_OPTION)
		{
			fileName = fileChooser.getSelectedFile().getPath();
			if (!fileName.endsWith(".esf"))
				fileName = fileName + ".esf";
		}

		if (fileName == null)
			return null;
		if ((new File(fileName)).exists())
		{
			if (JOptionPane.NO_OPTION == JOptionPane.showConfirmDialog(
					Environment.getActiveWindow(),
					"Файл существует. Перезаписать?",
					"",
					JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE))
				return null;
		}

		return fileName;
	}

	String openFileForReading()
	{
		String fileName = null;
		JFileChooser fileChooser = new JFileChooser();

		ChoosableFileFilter filter =
			new ChoosableFileFilter(
			"esf",
			"Empty session Save File");
		fileChooser.addChoosableFileFilter(filter);

		int option = fileChooser.showSaveDialog(Environment.getActiveWindow());
		if (option == JFileChooser.APPROVE_OPTION)
		{
			fileName = fileChooser.getSelectedFile().getPath();
			if (!fileName.endsWith(".esf"))
				return null;
		}

		if (fileName == null)
			return null;

		if (!(new File(fileName)).exists())
			return null;

		return fileName;
	}
}
