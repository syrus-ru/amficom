package com.syrus.AMFICOM.Client.General.Report;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JFileChooser;

import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.sun.image.codec.jpeg.JPEGCodec;
import javax.swing.JTextPane;
import oracle.jdeveloper.layout.XYConstraints;
import oracle.jdeveloper.layout.XYLayout;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Cursor;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.font.FontRenderContext;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.File;

import java.util.Vector;

import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.ChoosableFileFilter;
import com.syrus.AMFICOM.Client.General.Lang.LangModelReport;

import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;

/*import java.io.*;
 import java.awt.print.*;
 import javax.print.*;
 import javax.print.attribute.*;
 import javax.print.attribute.standard.*;
 import javax.print.event.*;*/

/**
 * <p>Title: </p>
 * <p>Description: Панель с реализованным отчётом</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Sytus Systems</p>
 * @author Песковский Пётр
 * @version 1.0
 */

public class ReportTemplateImplementationPanel extends JPanel
	implements OperationListener
{
	ApplicationContext aContext = null;

	ReportTemplate reportTemplate = null;

	boolean fromAnotherModule = false;

	public String itemToAdd = "";

	public Vector objects = null;

	public Vector labels = null;

	public Vector images = null;

	private boolean[] labelsPrinted = null;

	private boolean[] objectsPrinted = null;

	private boolean[] imagesPrinted = null;

	private XYLayout xYLayout1 = new XYLayout();

	private int objectMousePressedX = 0;

	private int objectMousePressedY = 0;

	private String curCursorName = "";

	private Rectangle imagableRect = null;

	private JPanel sizePanel = new JPanel();

	private boolean reportDataLoaded = false;

	java.awt.event.MouseMotionListener panelMouseMotionListener =
		new java.awt.event.MouseMotionListener()
	{
		public void mouseMoved(MouseEvent e)
		{
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}

		public void mouseDragged(MouseEvent e)
		{}
};

	public ReportTemplateImplementationPanel(
		ApplicationContext aC,
		ReportTemplate rt,
		boolean fromAM) throws CreateReportException
	{
		this.reportTemplate = rt;
		this.aContext = aC;
		this.fromAnotherModule = fromAM;

		this.objects = rt.objectRenderers;
		this.labels = rt.labels;
		this.images = rt.images;

		try
		{
			jbInit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		initialize_objects();
	}

	public void operationPerformed(OperationEvent ae)
	{
		if (ae.getActionCommand().equals(ReportBuilder.ev_stopProgressBar))
		{
			reportDataLoaded = true;
		}
	}

	public ReportTemplateImplementationPanel(
		ApplicationContext aC,
		ReportTemplate rt,
		boolean fromAM,
		Rectangle imRect) throws CreateReportException
	{
		this(aC, rt, fromAM);
		imagableRect = (Rectangle) imRect.clone();

		setImagableRect();
	}

	private void setImagableRect()
	{
		int maxY = 0;

		for (int i = 0; i < this.objects.size(); i++)
		{
			RenderingObject curObject = (RenderingObject)this.objects.get(i);

			int valueToConsider = (int) (curObject.rendererPanel.getY() +
												  curObject.rendererPanel.getPreferredSize().
												  getHeight());
			if (valueToConsider > maxY)
				maxY = valueToConsider;
		}

		for (int i = 0; i < this.labels.size(); i++)
		{
			FirmedTextPane curLabel = (FirmedTextPane)this.labels.get(i);
			if (curLabel.getY() + curLabel.getHeight() > maxY)
				maxY = curLabel.getY() + curLabel.getHeight();
		}

		for (int i = 0; i < this.images.size(); i++)
		{
			ImagePanel curImage = (ImagePanel)this.images.get(i);
			if (curImage.getY() + curImage.getHeight() > maxY)
				maxY = curImage.getY() + curImage.getHeight();
		}

		imagableRect.height = maxY - imagableRect.y + 20;

		this.remove(sizePanel);
		this.add(
			sizePanel,
			new XYConstraints(0, imagableRect.y * 2 + imagableRect.height, 0, 0));
	}

	private void jbInit() throws Exception
	{
		this.setBackground(Color.white);
		this.setLayout(xYLayout1);
		this.addMouseMotionListener(panelMouseMotionListener);
		sizePanel.setPreferredSize(new Dimension(0, 0));
		aContext.getDispatcher().register(this,ReportBuilder.ev_stopProgressBar);
	}

	public void paint(Graphics g)
	{
		super.paint(g);

		if (imagableRect != null)
		{
			g.setColor(new Color(200, 200, 200));
			g.drawRect(
				imagableRect.x,
				imagableRect.y,
				imagableRect.width,
				imagableRect.height);

			g.setColor(Color.black);
		}
	}

	private void setRendererListener(JPanel renderer)
	{
		renderer.addMouseListener(
			new java.awt.event.MouseListener()
		{
			public void mouseClicked(MouseEvent e)
			{}

			public void mouseEntered(MouseEvent e)
			{}

			public void mouseExited(MouseEvent e)
			{
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}

			public void mousePressed(MouseEvent e)
			{
				renderersMouse_pressed(e);
			}

			public void mouseReleased(MouseEvent e)
			{
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		});

		renderer.addMouseMotionListener(
			new java.awt.event.MouseMotionListener()
		{
			public void mouseMoved(MouseEvent e)
			{
				renderersMouse_moved(e);
			}

			public void mouseDragged(MouseEvent e)
			{
				renderersMouse_dragged(e);
			}
		});

	}

	/**
	 * Процедура осуществляет отображение элементов схемы шаблона
	 * в графический вид
	 * @throws CreateReportException в случае, если один
	 * из элементов шаблона по какой-либо причине не может быть создан
*/
	private void initialize_objects() throws CreateReportException
	{
		// Сначала просто создаём панели с таблицами и диаграмами
		// с заданными размерами и переносим надписи с панели схемы шаблона
		//Подгрузка нужных объектов с сервера
		reportDataLoaded = false;

		ReportBuilder.loadRequiredObjects(
				 aContext,
				 reportTemplate);

		while(!reportDataLoaded)
		{
			try
			{
				wait(1000);
			}
			catch(Exception exc)
			{}
		}

		for (int i = 0; i < objects.size(); i++)
		{
			RenderingObject renderer = (RenderingObject) objects.get(i);

//filter не передаётся - null стоит
			TitledPanel reportPanel =
				ReportBuilder.createReport(renderer.getReportToRender(),
													renderer.getTableDivisionsNumber(),
													reportTemplate,
													aContext,
													this.fromAnotherModule);

			if (reportPanel.insidePanel instanceof ReportResultsTablePanel)
			{
				JScrollPane contentPane =
					(ReportResultsTablePanel) reportPanel.insidePanel;

				//Если была сохранена инф-я о ширинах столбцов, то расширяем
				//таблица так, чтобы она могла вместить их все
				int contPaneWidth = renderer.getTotalTableWidth();
				if (contPaneWidth < renderer.width)
					contPaneWidth = renderer.width;

				contentPane.setSize(
					contPaneWidth,
					((ReportResultsTablePanel) reportPanel.insidePanel).
					getTableSize().height);
				contentPane.setPreferredSize(contentPane.getSize());
			}

			/*      renderer.x -= 5;
			 renderer.y -= 15;*/

			this.add(reportPanel, new XYConstraints(
				renderer.x,
				renderer.y,
				(int) reportPanel.getPreferredSize().getWidth(),
				(int) reportPanel.getPreferredSize().getHeight()));
			reportPanel.setLocation(renderer.x, renderer.y);

			// присваиваем лисенеры новосозданной панели
			this.setRendererListener(reportPanel);
			renderer.rendererPanel = reportPanel;
		}

		for (int i = 0; i < labels.size(); i++)
		{
			FirmedTextPane tp = (FirmedTextPane) labels.get(i);
			this.add(tp, new XYConstraints(
				tp.getX(),
				tp.getY(),
				-1,
				-1));
			tp.setEnabled(!fromAnotherModule);
		}

		if (images == null)
			images = new Vector();

		for (int i = 0; i < images.size(); i++)
		{
			ImagePanel ip = (ImagePanel) images.get(i);
			this.add(ip, new XYConstraints(
				ip.getX(),
				ip.getY(),
				-1,
				-1));
			ip.setEnabled(!fromAnotherModule);

			ip.removeMouseListener(ip.getMouseListeners()[0]);
			ip.removeMouseMotionListener(ip.getMouseMotionListeners()[0]);
			this.setRendererListener(ip);
		}

		// Инициализируем массивы - ни один объект ещё не напечатан (и не имеет
		// реальных координат
		labelsPrinted = new boolean[labels.size()];
		for (int i = 0; i < labelsPrinted.length; i++)
			labelsPrinted[i] = false;

		imagesPrinted = new boolean[images.size()];
		for (int i = 0; i < imagesPrinted.length; i++)
			imagesPrinted[i] = false;

		objectsPrinted = new boolean[objects.size()];
		for (int i = 0; i < objectsPrinted.length; i++)
			objectsPrinted[i] = false;

			/*-
			 Начало основного цикла
			 Ищем элемент, выше которого нет нераспечатанных элементов
			 Ему присваиваем координаты контура с блочной схемы
			 На следующей итерации считаем, что элемент распечатан (можно
			 распечатать те, которые е и ниже его)
			 */

		Vector xs = new Vector(); //строим карту уже распечатанных элементов
		Vector ys = new Vector();
		getAxisValuesMatrices(xs, ys);

		boolean toBreak = false;
		while (!toBreak)
		{
			for (int i = 0; i < objectsPrinted.length; i++)
			{
				if (objectsPrinted[i] == false)
				{
					RenderingObject curObjectToPrint = (RenderingObject) objects.get(
						i);

					int newY = checkToTopForElements(curObjectToPrint, xs, ys);
					//вернёт -2 если есть нераспечатанные эл-ты
					//или координаты по вертикали для текущего эл-та
					if (newY == -2)
						continue;

					// Присваиваем новые координаты объекту и пишем, что он распечатан
					this.remove(curObjectToPrint.rendererPanel);
					this.add(
						curObjectToPrint.rendererPanel,
						new XYConstraints(curObjectToPrint.x, newY, -1, -1));
					curObjectToPrint.rendererPanel.setLocation(curObjectToPrint.x,
						newY);

					objectsPrinted[i] = true;
					break;
				}
			}

			for (int i = 0; i < labelsPrinted.length; i++)
			{
				if (labelsPrinted[i] == false)
				{
					int newY = -7777;
					FirmedTextPane curLabelToPrint = (FirmedTextPane) labels.get(i);

					//Если надпись ни к чему не привязана
					if ((curLabelToPrint.vertFirmer == null) &&
						 (curLabelToPrint.horizFirmer == null))
					{
						newY = checkToTopForElements(curLabelToPrint, xs, ys);
						//вернёт -2 если есть нераспечатанные эл-ты
						//или координаты по вертикали для текущего эл-та
						if (newY == -2)
							continue;
					}
					else
					{
						boolean labelCantBePrinted = false;
						for (int j = 0; j < objectsPrinted.length; j++)
						{
							// Проверяем выведены ли на экран объекты к которым привязана надпись
							RenderingObject curRO = (RenderingObject) objects.get(j);

							if ((curLabelToPrint.vertFirmer != null) &&
								 (curLabelToPrint.vertFirmer.equals(curRO)))
								if (!objectsPrinted[j])
								{
									labelCantBePrinted = true;
									break;
								}

							if ((curLabelToPrint.horizFirmer != null) &&
								 (curLabelToPrint.horizFirmer.equals(curRO)))
								if (!objectsPrinted[j])
								{
									labelCantBePrinted = true;
									break;
								}
						}

						if (labelCantBePrinted)
							continue;
					}

					curLabelToPrint.refreshCoords(imagableRect);
					if (newY == -7777)
						newY = curLabelToPrint.getY();

					this.remove(curLabelToPrint);
					this.add(curLabelToPrint, new XYConstraints(
						curLabelToPrint.getX(),
						newY,
						-1,
						-1));

					curLabelToPrint.setLocation(curLabelToPrint.getX(), newY);
					labelsPrinted[i] = true;
					break;
				}
			}

			for (int i = 0; i < imagesPrinted.length; i++)
			{
				if (imagesPrinted[i] == false)
				{
					ImagePanel curImageToPrint = (ImagePanel) images.get(i);

					int newY = checkToTopForElements(curImageToPrint, xs, ys);
					//вернёт -2 если есть нераспечатанные эл-ты
					//или координаты по вертикали для текущего эл-та
					if (newY == -2)
						continue;

					if (newY == -7777)
						newY = curImageToPrint.getY();

					this.remove(curImageToPrint);
					this.add(curImageToPrint, new XYConstraints(
						curImageToPrint.getX(),
						newY,
						-1,
						-1));

					curImageToPrint.setLocation(curImageToPrint.getX(), newY);
					imagesPrinted[i] = true;
					break;
				}
			}

			toBreak = true;
			for (int i = 0; i < labelsPrinted.length; i++)
				if (!labelsPrinted[i])
				{
					toBreak = false;
					break;
				}

			for (int i = 0; i < imagesPrinted.length; i++)
				if (!imagesPrinted[i])
				{
					toBreak = false;
					break;
				}

			for (int i = 0; i < objectsPrinted.length; i++)
				if (!objectsPrinted[i])
				{
					toBreak = false;
					break;
				}
		}

		this.repaint();
	}

	/**
		 * Возвращает значение (y + height + dist) для ближайшего к elem элемента сверху?
	 * где dist - расстояние по схеме от elem до этого объекта
	 * 0, в случае если объектов выше нет
	 * и -2, если выше elem есть нераспечатанные элементы
	 * @param elem рассматриваемый объект
	 * @param xs вектор значений x и x + width для всех объектов
	 * @param ys вектор значений y и y + height для всех объектов
	 * @return значение y для реализации элемента шаблона
*/
	private int checkToTopForElements(
		Object elem,
		Vector xs,
		Vector ys)
	{
		// Находим границы диапазона на котором мы проверяем наличие
		//объектов сверху
		int elemX = 0;
		int elemY = 0;
		int elemWidth = 0;
		int elemHeight = 0;

		RenderingObject curRO = null;

		if (elem instanceof FirmedTextPane)
		{
			FirmedTextPane tp = (FirmedTextPane) elem;
			elemX = tp.getX();
			elemY = tp.getY();
			elemWidth = (int) tp.getPreferredSize().getWidth();
			elemHeight = (int) tp.getPreferredSize().getHeight();
		}

		else if (elem instanceof ImagePanel)
		{
			ImagePanel ip = (ImagePanel) elem;
			elemX = ip.getX();
			elemY = ip.getY();
			elemWidth = (int) ip.getPreferredSize().getWidth();
			elemHeight = (int) ip.getPreferredSize().getHeight();
		}

		else if (elem instanceof RenderingObject)
		{
			RenderingObject ro = (RenderingObject) elem;
			elemX = ro.x;
			elemY = ro.y;
			elemWidth = ro.width;
			elemHeight = ro.height;
			curRO = ro;
		}

		int theLowestLabelIndex = -1; // начальные значения (-1 - ничего не найдено)
		int theLowestImageIndex = -1;
		int theLowestObjectIndex = -1;
		int theLowestEdgeValue = 0;

		for (int i = 0; i < xs.size() - 1; i++)
		{
			int curMiddleX = (int) (((Integer) xs.get(i)).intValue() +
											((Integer) xs.get(i + 1)).intValue()) / 2;

			if (!((elemX <= curMiddleX) &&
					(curMiddleX <= elemX + elemWidth)))
				continue;

			for (int j = 0; j < ys.size() - 1; j++)
			{
				int curMiddleY = (int) (((Integer) ys.get(j)).intValue() +
												((Integer) ys.get(j + 1)).intValue()) / 2;

				if (curMiddleY >= elemY)
					break;

				//Если нашли выше распечатанную надпись или объект
				int tempLLabelIndex = this.getLabelAt(curMiddleX, curMiddleY);
				if (tempLLabelIndex == -2)
					return -2;

				int tempLImageIndex = this.getImageAt(curMiddleX, curMiddleY);
				if (tempLImageIndex == -2)
					return -2;

				int tempLObjectIndex = this.getRenderingObjectClasterAt(
							  curMiddleX,
							  curMiddleY,
							  curRO);
				if (tempLObjectIndex == -2)
					return -2;

				if (tempLLabelIndex != -1)
				{
					FirmedTextPane lowestLabel = (FirmedTextPane)this.labels.get(
						tempLLabelIndex);
					int curSuggestedY = lowestLabel.getY() + lowestLabel.getHeight();

					if (curSuggestedY > theLowestEdgeValue)
					{
						theLowestEdgeValue = curSuggestedY;
						theLowestLabelIndex = tempLLabelIndex;
						theLowestObjectIndex = -1;
						theLowestImageIndex = -1;
					}
				}

				if (tempLImageIndex != -1)
				{
					ImagePanel lowestImage = (ImagePanel)this.images.get(
						tempLImageIndex);
					int curSuggestedY = lowestImage.getY() + lowestImage.getHeight();

					if (curSuggestedY > theLowestEdgeValue)
					{
						theLowestEdgeValue = curSuggestedY;
						theLowestImageIndex = tempLImageIndex;
						theLowestObjectIndex = -1;
						theLowestLabelIndex = -1;
					}
				}

				if (tempLObjectIndex != -1)
				{
					RenderingObject ro = (RenderingObject)this.objects.get(
						tempLObjectIndex);
					int curSuggestedY = ro.rendererPanel.getY() +
											  (int) ro.rendererPanel.getPreferredSize().
											  getHeight();

					if (curSuggestedY > theLowestEdgeValue)
					{
						theLowestEdgeValue = curSuggestedY;
						theLowestObjectIndex = tempLObjectIndex;
						theLowestLabelIndex = -1;
						theLowestImageIndex = -1;
					}
				}
			}
		}

		//Если выше не нашли ни одного объекта
		if ((theLowestObjectIndex == -1)
			 && (theLowestLabelIndex == -1)
			 && (theLowestImageIndex == -1))
			return elemY;

		if (theLowestLabelIndex != -1)
		{
			FirmedTextPane lowestLabel = (FirmedTextPane)this.labels.get(
				theLowestLabelIndex);
			return elemY;
		}

		if (theLowestImageIndex != -1)
		{
			ImagePanel lowestImage = (ImagePanel)this.images.get(
				theLowestImageIndex);
			return elemY;
		}

		if (theLowestObjectIndex != -1)
		{
			RenderingObject ro = (RenderingObject)this.objects.get(
				theLowestObjectIndex);

			return (elemY - (ro.y + ro.height) + ro.rendererPanel.getY() +
					  (int) ro.rendererPanel.getPreferredSize().getHeight());
		}

		return -7777;
	}

	/**
	 * Возвращает соритрованную матрицы xs и ys начал и концов объектов и надписей по x,y
	 * @param xs вектор значений x и x + width для всех объектов
	 * @param ys вектор значений y и y + height для всех объектов
*/
	private void getAxisValuesMatrices(
		Vector xs,
		Vector ys)
	{
		int elemCount = 0;

		// Непривязанные к объектам надписи
		for (int i = 0; i < this.labels.size(); i++)
		{
			FirmedTextPane curPane = (FirmedTextPane)this.labels.get(i);
			if ((curPane.horizFirmer != null) ||
				 (curPane.vertFirmer != null))
				continue;

			xs.add(new Integer(curPane.getX()));
			ys.add(new Integer(curPane.getY()));

			xs.add(new Integer(curPane.getX() + curPane.getWidth()));
			ys.add(new Integer(curPane.getY() + curPane.getHeight()));

			elemCount += 2;
		}

		for (int i = 0; i < this.images.size(); i++)
		{
			ImagePanel curImage = (ImagePanel)this.images.get(i);

			xs.add(new Integer(curImage.getX()));
			ys.add(new Integer(curImage.getY()));

			xs.add(new Integer(curImage.getX() + curImage.getWidth()));
			ys.add(new Integer(curImage.getY() + curImage.getHeight()));

			elemCount += 2;
		}

		for (int i = 0; i < this.objects.size(); i++)
		{
			RenderingObject curRO = (RenderingObject)this.objects.get(i);

			Rectangle r = null;
			r = curRO.getObjectWithLabelsBounds(this.labels);

			xs.add(new Integer(r.x));
			ys.add(new Integer(r.y));

			xs.add(new Integer(r.x + r.width));
			ys.add(new Integer(r.y + r.height));

			elemCount += 2;
		}

		for (int i = 0; i < elemCount - 1; i++) //сортируем полученные множества
			for (int j = i + 1; j < elemCount; j++)
			{
				int iValue = ((Integer) xs.get(i)).intValue();
				int jValue = ((Integer) xs.get(j)).intValue();
				if (jValue < iValue)
				{
					xs.set(i, new Integer(jValue));
					xs.set(j, new Integer(iValue));
				}
			}

		for (int i = 0; i < elemCount - 1; i++) //сортируем полученные множества
			for (int j = i + 1; j < elemCount; j++)
			{
				int iValue = ((Integer) ys.get(i)).intValue();
				int jValue = ((Integer) ys.get(j)).intValue();
				if (jValue < iValue)
				{
					ys.set(i, new Integer(jValue));
					ys.set(j, new Integer(iValue));
				}
			}
	}

	/**
	 * надпись должна быть привязанной к полю и по вертикали и по горизонтали
	 * @param x значение по x рассматриваемой точки
	 * @param y значение по y рассматриваемой точки
	 * @return возвращает индекс надписи в векторе labels, которому
	 *            принадлежит (x,y);
	 *  -1, если в этой точке нет надписей;
	 *  -2, если есть, но он не напечатан
*/
	private int getLabelAt(int x, int y)
	{
		for (int i = 0; i < labels.size(); i++)
		{
			FirmedTextPane curLabel = (FirmedTextPane) labels.get(i);
			if ((curLabel.vertFirmer == null) &&
				 (curLabel.horizFirmer == null) &&
				 curLabel.hasPoint(x, y))
			{
				if (this.labelsPrinted[i])
					return i;
				else
					return -2;
			}
		}
		return -1;
	}


	private int getImageAt(int x, int y)
	{
		for (int i = 0; i < images.size(); i++)
		{
			ImagePanel curImage = (ImagePanel) images.get(i);
			if ((curImage.getX() < x)
				 && (x < curImage.getX() + curImage.getWidth())
				 && (curImage.getY() < y)
				 && (y < curImage.getY() + curImage.getHeight()))
			{
				if (this.imagesPrinted[i])
					return i;
				else
					return -2;
			}
		}
		return -1;
	}

	/**
	 * @param x значение по x рассматриваемой точки
	 * @param y значение по y рассматриваемой точки
	 * @return возвращает индекс кластера в векторе objects,
	 * которому принадлежит (x,y);
	 * -1, если в этой точке нет объектов;
	 * -2, если есть, но он не напечатан.
*/

	private int getRenderingObjectClasterAt(int x, int y, RenderingObject curRO)
	{
		for (int i = 0; i < objects.size(); i++)
		{
			RenderingObject iRO = (RenderingObject) objects.get(i);
			if (iRO.hasPoint(x, y, this.labels))
			{
				if (this.objectsPrinted[i]
					 || ((iRO != null)
						  && iRO.equals(curRO)))
					return i;
				else
					return -2;
			}
		}
		return -1;
	}

	private void renderersMouse_moved(MouseEvent e)
	{
		if (fromAnotherModule)
			return;

		JPanel renderer = (JPanel) e.getSource();

		if ((e.getX() < 5) &&
			 (e.getY() < 5))
		{
			renderer.setCursor(new Cursor(Cursor.NW_RESIZE_CURSOR));
			return;
		}

		if ((e.getX() > renderer.getWidth() - 5) &&
			 (e.getY() > renderer.getHeight() - 5))
		{
			renderer.setCursor(new Cursor(Cursor.SE_RESIZE_CURSOR));
			return;
		}

		if ((e.getX() > renderer.getWidth() - 5) &&
			 (e.getY() < 5))
		{
			renderer.setCursor(new Cursor(Cursor.NE_RESIZE_CURSOR));
			return;
		}

		if ((e.getX() < 5) &&
			 (e.getY() > renderer.getHeight() - 5))
		{
			renderer.setCursor(new Cursor(Cursor.SW_RESIZE_CURSOR));
			return;
		}

		if (e.getX() < 5)
		{
			renderer.setCursor(new Cursor(Cursor.W_RESIZE_CURSOR));
			return;
		}

		if (e.getX() > renderer.getWidth() - 5)
		{
			renderer.setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));
			return;
		}

		if (e.getY() < 5)
		{
			renderer.setCursor(new Cursor(Cursor.N_RESIZE_CURSOR));
			return;
		}

		if (e.getY() > renderer.getHeight() - 5)
		{
			renderer.setCursor(new Cursor(Cursor.S_RESIZE_CURSOR));
			return;
		}

		renderer.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
	}

	private void renderersMouse_dragged(MouseEvent e)
	{
		if (fromAnotherModule)
			return;

		JPanel renderer = (JPanel) e.getSource();

		int dx = e.getX() - objectMousePressedX;
		int dy = e.getY() - objectMousePressedY;

		Rectangle prevBounds = new Rectangle();
		prevBounds.x = renderer.getX();
		prevBounds.y = renderer.getY();
		prevBounds.width = (int) renderer.getPreferredSize().getWidth();
		prevBounds.height = (int) renderer.getPreferredSize().getHeight();

//		renderer.setSize(renderer.getPreferredSize());
		if (curCursorName.equals("Crosshair Cursor"))
		{
			prevBounds.x += dx;
			prevBounds.y += dy;
		}

		if (curCursorName.equals("East Resize Cursor"))
		{
			objectMousePressedX += dx;
			prevBounds.width += dx;
			dy = 0;
		}

		if (curCursorName.equals("West Resize Cursor"))
		{
			prevBounds.width -= dx;
			prevBounds.x += dx;
			dy = 0;
		}

		if (curCursorName.equals("South Resize Cursor"))
		{
			objectMousePressedY += dy;
			prevBounds.height += dy;
			dx = 0;
		}

		if (curCursorName.equals("North Resize Cursor"))
		{
			prevBounds.height -= dy;
			prevBounds.y += dy;
			dx = 0;
		}

		if (curCursorName.equals("Southeast Resize Cursor"))
		{
			objectMousePressedX += dx;
			objectMousePressedY += dy;

			prevBounds.width += dx;
			prevBounds.height += dy;
		}

		if (curCursorName.equals("Northeast Resize Cursor"))
		{
			objectMousePressedX += dx;

			prevBounds.width += dx;
			prevBounds.height -= dy;

			prevBounds.y += dy;
		}

		if (curCursorName.equals("Northwest Resize Cursor"))
		{
			prevBounds.width -= dx;
			prevBounds.height -= dy;

			prevBounds.x += dx;
			prevBounds.y += dy;
		}

		if (curCursorName.equals("Southwest Resize Cursor"))
		{
			objectMousePressedY += dy;

			prevBounds.width -= dx;
			prevBounds.height += dy;

			prevBounds.x += dx;
		}

		boolean smthChanged = false;

		if ((imagableRect.x > prevBounds.x)
			 || (prevBounds.x + prevBounds.width
				  > imagableRect.x + imagableRect.width))
		{
			prevBounds.width = renderer.getWidth();
			prevBounds.x = renderer.getX();
		}
		else
			smthChanged = true;

		if ((imagableRect.y > prevBounds.y)
			 || (prevBounds.y + prevBounds.height
				  > imagableRect.y + imagableRect.height))
		{
			prevBounds.height = renderer.getHeight();
			prevBounds.y = renderer.getY();
		}
		else
			smthChanged = true;

		if (!smthChanged)
			return;

		renderer.setPreferredSize(new Dimension(prevBounds.width,
															 prevBounds.height));
		renderer.setLocation(prevBounds.x, prevBounds.y);

		this.remove(renderer);
		this.add(renderer, new XYConstraints(renderer.getX(),
														 renderer.getY(),
														 (int) renderer.getPreferredSize().
														 getWidth(),
														 (int) renderer.getPreferredSize().
														 getHeight()));
		renderer.revalidate();

		for (int i = 0; i < labels.size(); i++)
		{
			FirmedTextPane curLabel = (FirmedTextPane) labels.get(i);
			for (int j = 0; j < objects.size(); j++)
			{
				RenderingObject curRO = (RenderingObject) objects.get(j);

				if ((curRO.rendererPanel != null) &&
					 curRO.rendererPanel.equals(renderer))
					if ((curLabel.vertFirmer != null) &&
						 curLabel.vertFirmer.equals(curRO) ||
						 (curLabel.horizFirmer != null) &&
						 curLabel.horizFirmer.equals(curRO))
					{
						curLabel.refreshCoords(this.imagableRect);
						this.remove(curLabel);
						this.add(curLabel, new XYConstraints(
							curLabel.getX(),
							curLabel.getY(),
							-1,
							-1));
						curLabel.setLocation(curLabel.getX(), curLabel.getY());
					}
			}
		}

		setImagableRect();

		reportTemplate.curModified = System.currentTimeMillis();
		this.repaint();
	}

	private void renderersMouse_pressed(MouseEvent e)
	{
		if (fromAnotherModule)
			return;

		objectMousePressedX = e.getX();
		objectMousePressedY = e.getY();
		curCursorName = ((JPanel)e.getSource()).getCursor().getName();
	}

	public int saveToHTML(String fileName, boolean beforePrinting)
	{
		Vector xs = new Vector();
		Vector ys = new Vector();

		this.getHTMLAxisValuesMatrices(xs, ys);

		int[][] matrix = this.createElemsMatrix(xs, ys);
		if (matrix == null)
		{
			JOptionPane.showMessageDialog(
				Environment.getActiveWindow(),
				LangModelReport.getString("label_emptyReport"),
				LangModelReport.getString("label_error"),
				JOptionPane.ERROR_MESSAGE);
			return -7777;
		}

		if (fileName == null)
		{
			JFileChooser fileChooser = new JFileChooser();

			ChoosableFileFilter filter =
				new ChoosableFileFilter(
				"html",
				LangModelReport.getString("label_htmlFiles"));
			fileChooser.addChoosableFileFilter(filter);

			int option = fileChooser.showSaveDialog(Environment.getActiveWindow());
			if (option == JFileChooser.APPROVE_OPTION)
			{
				fileName = fileChooser.getSelectedFile().getPath();
				if (!fileName.endsWith(".html"))
					fileName = fileName + ".html";
			}
		}

		if (fileName == null)
			return -7777;
		else if ((new File(fileName)).exists() && !beforePrinting)
		{
			if (JOptionPane.showConfirmDialog(
				Environment.getActiveWindow(),
				LangModelReport.getString("label_fileExists"),
				"",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION)
				return -7777;
		}

		try
		{
			int imagesSaved = 0;
			FileOutputStream out = new FileOutputStream(fileName);

			this.writeHeader(out, (Integer) xs.get(xs.size() - 1));

			for (int row = 0; row < matrix.length; row++)
			{
				out.write("<tr>\n".getBytes());
				for (int col = 0; col < matrix[0].length; col++)
				{
					if (matrix[row][col] == -2) // -2 эти ячейки уже распечатаны
						continue;

					Point cr = findColRowSpans(matrix, col, row);
					Integer colspan = new Integer(cr.x);
					Integer rowspan = new Integer(cr.y);

					int cellHeight = ((Integer) ys.get(row + rowspan.intValue())).
										  intValue() -
										  ((Integer) ys.get(row)).intValue();

					int cellWidth = ((Integer) xs.get(col + colspan.intValue())).
										 intValue() -
										 ((Integer) xs.get(col)).intValue();

					String buffer = "<td width=\"" +
										 (new Integer(cellWidth)).toString() + "\"";
					buffer += " height=\"" +
						(new Integer(cellHeight)).toString() + "\"";
					if (colspan.intValue() > 1)
						buffer += "colspan=\"" + colspan.toString() + "\"";

					if (rowspan.intValue() > 1)
						buffer += "rowspan=\"" + rowspan.toString() + "\"";

					out.write(buffer.getBytes());

					// Заполняем ячейку
					if (matrix[row][col] != -1)
					{
						//Надпись
						if (matrix[row][col] < this.labels.size())
						{
							FirmedTextPane label = (FirmedTextPane)this.labels.get(
								matrix[row][col]);
                
              writeTextPane(label,out);  
						}
						else if (matrix[row][col] >= this.labels.size() + this.objects.size())
						{
							out.write(">".getBytes());
							File f = new File(fileName);
							String fileNameWOPNE =
								f.getName().substring(0, f.getName().indexOf("."));

							String dirName =
								f.getAbsolutePath().
								substring(0,
											 f.getAbsolutePath().lastIndexOf("\\"))
								+ "\\" +
								fileNameWOPNE + ".files";
							f = new File(dirName);
							f.mkdir();

							ImagePanel image = (ImagePanel)this.images.get(
												  matrix[row][col] - this.labels.size() - this.objects.size());
							File picToCopy = new File (image.fileName);
							FileInputStream is = new FileInputStream (picToCopy);
							String extension = image.fileName.substring(image.fileName.indexOf("."));

							String absImageFileName = dirName + "\\image" +
															  new Integer(imagesSaved).
															  toString() + extension;


							FileOutputStream os = new FileOutputStream (absImageFileName);

							int lastByte = is.read();
							while (lastByte != -1)
							{
								os.write (lastByte);
								lastByte = is.read();
							}

							is.close();
							os.close();


							String tagImageFileName =
								fileNameWOPNE +
								".files\\image" +
								new Integer(imagesSaved).toString();

							tagImageFileName += extension;

							this.writeImage(out,
												 image.getPreferredSize(),
												 tagImageFileName);
							imagesSaved++;
						}

						else
						{
							RenderingObject ro = (RenderingObject)this.objects.get(
								matrix[row][col] - this.labels.size());

							if (ro.rendererPanel.insidePanel instanceof
								 TextPanel)
              {
								//Отчёт с текстовым полем
								JScrollPane sp = (JScrollPane) ro.rendererPanel.
													  insidePanel;
                            
								if (sp == null)
									continue;

								JTextPane textPane = (JTextPane) sp.getViewport().getView();
								textPane.setSize(sp.getPreferredSize());
								this.writeTextPane(textPane,out);
              }

							else if (ro.rendererPanel.insidePanel instanceof
								 ReportResultsTablePanel)
              {
								//Таблица
								out.write("valign=\"Top\"".getBytes());
  							out.write(">".getBytes());
								JScrollPane sp = (JScrollPane) ro.rendererPanel.
													  insidePanel;
                            
								if (sp == null)
									continue;

								JTable itsTable = (JTable) sp.getViewport().getView();
								itsTable.setSize(sp.getPreferredSize());
								this.writeTable(out, itsTable, beforePrinting);
              }
							else 
							{
  							out.write(">".getBytes());              
								//Создаём каталог path/fileName.files
								File f = new File(fileName);
								String fileNameWOPNE =
														 f.getName().substring(0, f.getName().indexOf("."));

								String dirName =
									f.getAbsolutePath().
									substring(0,
												 f.getAbsolutePath().lastIndexOf("\\"))
									+ "\\" +
									fileNameWOPNE + ".files";
								f = new File(dirName);
								f.mkdir();

								String absImageFileName = dirName + "\\image" +
																  new Integer(imagesSaved).
																  toString();

								String tagImageFileName =
									fileNameWOPNE +
									".files\\image" +
									new Integer(imagesSaved).toString();

								if (ro.rendererPanel.insidePanel instanceof
									 ReportChartPanel)
								{
									//Диаграмма
									ReportChartPanel cp = (ReportChartPanel) ro.
																 rendererPanel.insidePanel;
									cp.doSave(absImageFileName + ".png");
									tagImageFileName += ".png";
								}
								else
								{
									BufferedImage bi = new BufferedImage(
										(int) ro.rendererPanel.insidePanel.getWidth(),
										(int) ro.rendererPanel.insidePanel.getHeight(),
										BufferedImage.TYPE_3BYTE_BGR);
									Graphics2D biGraphics = (Graphics2D) bi.
										createGraphics();
									ro.rendererPanel.insidePanel.paint(biGraphics);

									FileOutputStream jpgOs = new FileOutputStream(
										absImageFileName + ".jpg");

									JPEGImageEncoder enc = JPEGCodec.createJPEGEncoder(
										jpgOs);
									enc.encode(bi);
									jpgOs.close();

									tagImageFileName += ".jpg";
								}

								this.writeImage(out,
													 ro.rendererPanel.insidePanel.
													 getPreferredSize(),
													 tagImageFileName);
								imagesSaved++;
							}
						}
					}
					else
						out.write(">".getBytes());

					out.write("</td>\n".getBytes());

					for (int i = col; i < col + colspan.intValue(); i++)
						for (int j = row; j < row + rowspan.intValue(); j++)
							matrix[j][i] = -2; // Указываем, что эти ячейки распечатаны
				}
				out.write("</tr>\n".getBytes());
			}
			this.writeFooter(out);

			out.close();
		}

		catch (java.io.IOException ee)
		{
			JOptionPane.showMessageDialog(
				Environment.getActiveWindow(),
				LangModelReport.getString("error_fileNotAvailable"),
				LangModelReport.getString("label_error"),
				JOptionPane.ERROR_MESSAGE);
			return -7777;
		}
		return 0;
	}

	private void getHTMLAxisValuesMatrices(
		Vector xs,
		Vector ys)
	{
		//Возвращает соритрованную матрицы xs и ys начал и концов объектов и надписей по x,y

		int elemCount = 0;
		xs.add(new Integer(0));
		ys.add(new Integer(0));
		elemCount++;

		// Непривязанные к объектам надписи
		for (int i = 0; i < this.labels.size(); i++)
		{
			FirmedTextPane curPane = (FirmedTextPane)this.labels.get(i);

			xs.add(new Integer(curPane.getX()));
			ys.add(new Integer(curPane.getY()));

			xs.add(new Integer(curPane.getX() + curPane.getWidth()));
			ys.add(new Integer(curPane.getY() + curPane.getHeight()));

			elemCount += 2;
		}

		for (int i = 0; i < this.images.size(); i++)
		{
			ImagePanel curImage = (ImagePanel)this.images.get(i);

			xs.add(new Integer(curImage.getX()));
			ys.add(new Integer(curImage.getY()));

			xs.add(new Integer(curImage.getX() + curImage.getWidth()));
			ys.add(new Integer(curImage.getY() + curImage.getHeight()));

			elemCount += 2;
		}

		for (int i = 0; i < this.objects.size(); i++)
		{
			RenderingObject curRO = (RenderingObject)this.objects.get(i);

			xs.add(new Integer(curRO.rendererPanel.getX()));
			ys.add(new Integer(curRO.rendererPanel.getY()));

			xs.add(new Integer(curRO.rendererPanel.getX() +
									 (int) curRO.rendererPanel.getPreferredSize().
									 getWidth()));
			ys.add(new Integer(curRO.rendererPanel.getY() +
									 (int) curRO.rendererPanel.getPreferredSize().
									 getHeight()));

			elemCount += 2;
		}

		for (int i = 0; i < elemCount - 1; i++) //сортируем полученные множества
			for (int j = i + 1; j < elemCount; j++)
			{
				int iValue = ((Integer) xs.get(i)).intValue();
				int jValue = ((Integer) xs.get(j)).intValue();
				if (jValue < iValue)
				{
					xs.set(i, new Integer(jValue));
					xs.set(j, new Integer(iValue));
				}
			}

		for (int i = 0; i < elemCount - 1; i++) //сортируем полученные множества
			for (int j = i + 1; j < elemCount; j++)
			{
				int iValue = ((Integer) ys.get(i)).intValue();
				int jValue = ((Integer) ys.get(j)).intValue();
				if (jValue < iValue)
				{
					ys.set(i, new Integer(jValue));
					ys.set(j, new Integer(iValue));
				}
			}
	}

	/*
	 * Возвращает индекс надписи в векторе labels, которому принадлежит (x,y)
	 * -1, если в этой точке нет надписей;
	 */
	private int getLabelAtReport(int x, int y)
	{
		for (int i = 0; i < labels.size(); i++)
		{
			FirmedTextPane curLabel = (FirmedTextPane) labels.get(i);
			if (curLabel.hasPoint(x, y))
				return i;
		}
		return -1;
	}

	/*
	 * Возвращает индекс объекта в векторе objects, которому принадлежит (x,y)
	 * -1, если в этой точке нет объектов;
	 */
	private int getRenderingObjectAtReport(int x, int y)
	{
		for (int i = 0; i < objects.size(); i++)
		{
			RenderingObject curRO = (RenderingObject) objects.get(i);
			if (curRO.hasPoint(x, y, null))
				return i;
		}
		return -1;
	}

	private int[][] createElemsMatrix(Vector xs, Vector ys)
	{
		if (xs == null)
			return null;
		if (xs.size() < 2)
			return null;
		if (ys == null)
			return null;
		if (ys.size() < 2)
			return null;

		int[][] result = new int[ys.size() - 1][];
		for (int i = 0; i < result.length; i++)
			result[i] = new int[xs.size() - 1];

		for (int i = 0; i < ys.size() - 1; i++)
		{
			int yValue = (((Integer) ys.get(i)).intValue() +
							  ((Integer) ys.get(i + 1)).intValue()) / 2;
			for (int j = 0; j < xs.size() - 1; j++)
			{
				int xValue = (((Integer) xs.get(j)).intValue() +
								  ((Integer) xs.get(j + 1)).intValue()) / 2;
				result[i][j] = getLabelAtReport(xValue, yValue);

				int indexReturned = getRenderingObjectAtReport(xValue, yValue);
				if (indexReturned != -1)
					result[i][j] = indexReturned + this.labels.size();

				indexReturned = getImageAt(xValue, yValue);
				if (indexReturned != -1)
					result[i][j] = indexReturned + this.labels.size() + this.objects.size();

			}
		}

		return result;
	}

	private Point findColRowSpans(int[][] matrix,
											int xStartValue,
											int yStartValue)
	{
		if ((xStartValue > matrix[0].length) || (yStartValue > matrix.length))
			return null;

		int startValue = matrix[yStartValue][xStartValue];
		if (startValue == -1)
			return new Point(1, 1);

		int cols = 0;
		while (matrix[yStartValue][xStartValue + cols] == startValue)
		{
			cols++;
			if ((xStartValue + cols) >= matrix[0].length)
				break;
		}
		//Нашли количество колонок, занимаемых объектом

		int rows = 0;
		boolean toBreak = false;
		while (!toBreak)
		{
			rows++;
			if (yStartValue + rows >= matrix.length)
				break;

			for (int i = xStartValue; i < xStartValue + cols; i++)
				if (matrix[yStartValue + rows][i] != startValue)
					toBreak = true;
		}

		//Нашли количество рядов, занимаемых объектом
		return new Point(cols, rows);
	}

	private void writeHeader(FileOutputStream out, Integer mainTableWidth) throws
		java.io.FileNotFoundException, java.io.IOException
	{
		String templateName = reportTemplate.name;

		String buff = "<html>\n\n<head>\n" +
						  "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=windows-1251\">\n" +
			"<meta name=\"GENERATOR\" content=\"Microsoft FrontPage 4.0\">\n" +
			"<meta name=\"ProgId\" content=\"FrontPage.Editor.Document\">\n" +
			"<title>" +
			LangModelReport.getString("label_reportForTemplate") +
			" " + templateName + "</title>\n" +
			"</head>\n\n<body>\n\n" +
			"<table border=\"0\" width=\"" + mainTableWidth.intValue() +
			"\" style=\"font-size: 0\"> \n\n";

		out.write(buff.getBytes());
	}

	private void writeFooter(FileOutputStream out) throws java.io.
		FileNotFoundException, java.io.IOException
	{
		String buff = "</table>\n\n</body>\n\n</html>";
		out.write(buff.getBytes());
	}

	private void writeImage(FileOutputStream out,
									Dimension panelSize,
									String imageFileName) throws java.io.
		FileNotFoundException, java.io.IOException
	{
		String buffer = "<img border=\"0\" " +
							 "src=\"" + imageFileName + "\" " +
							 "width=\"" + new Integer(panelSize.width).toString()
							 + "\" " +
							 "height=\"" + new Integer(panelSize.height).toString()
							 + "\">";
		out.write(buffer.getBytes());
	}

	private void writeTable(FileOutputStream out, JTable table,
									boolean beforePrinting) throws java.io.
		FileNotFoundException, java.io.IOException
	{
		String buffer =
			"\n<table frame=\"box\" border=\"1\" width=\"" + table.getWidth() +
			"\"" +
			" style=\"font-size: 13";
		buffer += "px";

		buffer += ";\">\n\n";

		for (int i = 0; i < table.getRowCount(); i++)
		{
			buffer += "<tr>";
			for (int j = 0; j < table.getColumnCount(); j++)
			{
				float startTableWidth = table.getColumnModel().getTotalColumnWidth();
				float currTableWidth = table.getWidth();
				int columnWidth = (int) (table.getColumnModel().getColumn(j).
												 getWidth() *
												 currTableWidth / startTableWidth);

				buffer += "<td width=\"" +
					(new Integer(columnWidth)).toString() + "\"";
				buffer += ">";

				if (table.getValueAt(i, j) != "")
					buffer += table.getValueAt(i, j);
				else
					buffer += "&nbsp";
				buffer += "</td>\n";
			}
			buffer += "</tr>\n";
		}
		buffer += "</table>\n\n";
		out.write(buffer.getBytes());
	}

  private void writeTextPane (JTextPane label,
                             FileOutputStream out)
    throws java.io.FileNotFoundException, java.io.IOException
  {
    String fontName = label.getFont().getName();

    String fontSize = null;

    fontSize = new Integer(
      label.getFont().getSize()).toString();

    String textBuffer = label.getText();
    int curIndex = 0;
    while (curIndex < textBuffer.length())
    {
      if (textBuffer.charAt(curIndex) == ' ')
      {
        textBuffer = textBuffer.substring(0, curIndex) +
                 "&nbsp;" +
                 textBuffer.substring(
          curIndex + 1, textBuffer.length());
      }
      if (textBuffer.charAt(curIndex) == '\n')
      {
        textBuffer = textBuffer.substring(0, curIndex) +
                 "<br>" +
                 textBuffer.substring(
          curIndex + 1, textBuffer.length());
      }
      curIndex++;
    }

    String italicTagStart = "";
    String italicTagEnd = "";
    if (label.getFont().isItalic())
    {
      italicTagStart = "<i>";
      italicTagEnd = "</i>";
    }

    String boldTagStart = "";
    String boldTagEnd = "";
    if (label.getFont().isBold())
    {
      boldTagStart = "<b>";
      boldTagEnd = "</b>";
    }

    String fontBuffer = " style=\"font-size: " + fontSize;

    fontBuffer += "px";
    fontBuffer += ";\">" +
      "<font face=\"" + fontName + "\">" +
      boldTagStart + italicTagStart + textBuffer +
      italicTagEnd + boldTagEnd + "</font>";

    out.write(fontBuffer.getBytes());
  
  }

	public void printReport()
	{
		String s =
			System.getProperty("java.io.tmpdir")
			+ System.getProperty("file.separator")
			+ "print.tmp";

		File f = new File(s);
		try
		{
			for (int i = 0; i < labels.size(); i++)
			{
				FirmedTextPane ftp = (FirmedTextPane) labels.get(i);
				ftp.setSize(ftp.getWidth() + (int) ftp.getFont().getMaxCharBounds(
					new FontRenderContext(null, true, true)).getWidth(),
								ftp.getHeight());
			}

			if (this.saveToHTML(f.getAbsolutePath(), true) != 0)
				return;

			for (int i = 0; i < labels.size(); i++)
			{
				FirmedTextPane ftp = (FirmedTextPane) labels.get(i);
				ftp.setSize(ftp.getWidth() - (int) ftp.getFont().getMaxCharBounds(
					new FontRenderContext(null, true, true)).getWidth(),
								ftp.getHeight());
			}

			/*
//			DocFlavor psFlavor =  new DocFlavor.INPUT_STREAM("text/html; charset=windows-1251");
			 DocFlavor psFlavor = DocFlavor.INPUT_STREAM.TEXT_HTML_HOST;
//			PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
//			aset.add(MediaSizeName.ISO_A4);
			 Doc doc = null;
			 try
			 {
			 FileInputStream fis = new FileInputStream(f.getPath());
			 doc = new SimpleDoc(fis, psFlavor, null);
			 }
			 catch (IOException e)
			 {
			 System.err.println(e);
			 }
				 PrintService[] pservices = PrintServiceLookup.lookupPrintServices(psFlavor,
null);
//			PrintService pservice = PrintServiceLookup.lookupDefaultPrintService();
			 if (pservices.length > 0)
			 {
			 DocPrintJob pj = pservices[0].createPrintJob();
//        PrinterJob pj = PrinterJob.getPrinterJob();
//        if (pj.printDialog())
			 try
			 {
			 pj.print(doc,null);
			 }
			 catch (PrintException e)
			 {
			 System.err.println(e);
			 }
			 }
			 */
			/*
			 String s = null;
			 s = "file:"
			 + System.getProperty("user.dir")
			 + System.getProperty("file.separator")
			 + f.getName();
			 JEditorPane ep = new JEditorPane();
			 ep.setPage(s);
//		JDialog main = new JDialog();
//			main.setModal(true);
//			main.setSize(800,800);
//			main.getContentPane().setLayout(new java.awt.BorderLayout());
//			main.getContentPane().add(new JScrollPane(ep),java.awt.BorderLayout.CENTER);
//			main.setVisible(true);
			 DocumentRenderer dr = new DocumentRenderer();
			 dr.print(ep);*/

			String command = "rundll32 MSHTML.DLL,PrintHTML \"" + s + "\"";
			Runtime.getRuntime().exec(command);
		}
		catch (Exception e)
		{
			if (f.exists())
			{
				f.delete();
			}
		}
	}
}
