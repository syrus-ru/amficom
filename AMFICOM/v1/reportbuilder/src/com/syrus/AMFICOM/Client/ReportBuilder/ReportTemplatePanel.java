package com.syrus.AMFICOM.Client.ReportBuilder;

import javax.swing.JInternalFrame;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.BorderFactory;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;

import java.awt.*;

import java.util.Vector;
import java.util.Enumeration;
import java.util.Hashtable;

import oracle.jdeveloper.layout.XYConstraints;
import oracle.jdeveloper.layout.XYLayout;

import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ActionEvent;

import java.awt.dnd.DropTargetListener;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DnDConstants;
import java.awt.datatransfer.DataFlavor;

import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.Lang.LangModelReport;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;

import com.syrus.AMFICOM.Client.Resource.Map.MapContext;
import com.syrus.AMFICOM.Client.Resource.MapDataSourceImage;
import com.syrus.AMFICOM.Client.Map.Report.MapRenderPanel;
import com.syrus.AMFICOM.Client.Map.Report.MapReportModel;

import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Pool;

import com.syrus.AMFICOM.Client.General.Report.ObjectResourceReportModel;
import com.syrus.AMFICOM.Client.Optimize.Report.OptimizationReportModel;

import com.syrus.AMFICOM.CORBA.Scheme.SchemeOptimizeInfo_Transferable;
import com.syrus.AMFICOM.Client.Resource.Optimize.SolutionCompact;

import com.syrus.AMFICOM.Client.General.Report.*;
import com.syrus.AMFICOM.Client.Optimize.Report.SelectSolutionFrame;

/**
 * <p>Description: Панель для размещения схемы элементов шаблона</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Syrus Systems</p>
 * @author Песковский Пётр
 * @version 1.0
 */

public class ReportTemplatePanel extends JPanel
	implements DropTargetListener,OperationListener
{
	public ReportTemplate reportTemplate = null;

	public Rectangle imagableRect = null;

	private ReportMDIMain rtbWindow = null;

	public String itemToAdd = "";

	private XYLayout xYLayout1 = new XYLayout();

	private int mousePressedX = 0;

	private int mousePressedY = 0;

	private Rectangle prevBounds = new Rectangle();

	private RenderingObject selectedRenderingObject = null;

	private Object selectedObject = null;

	private FirmedTextPane labelToFirmTo = null;

	private String firmingType = null;

	private JPanel this_pointer = this;

	private String curCursorName = "";

	java.awt.event.MouseListener panelMouseListener =
		new java.awt.event.MouseListener()
	{
		public void mouseClicked(MouseEvent e)
		{}

		public void mouseEntered(MouseEvent e)
		{}

		public void mouseExited(MouseEvent e)
		{}

		public void mousePressed(MouseEvent e)
		{
			panelMouse_pressed(e);
		}

		public void mouseReleased(MouseEvent e)
		{
			panelMouse_clicked(e);
		}
};

	java.awt.event.MouseMotionListener panelMouseMotionListener =
		new java.awt.event.MouseMotionListener()
	{
		public void mouseMoved(MouseEvent e)
		{
			panelMouse_moved(e);
		}

		public void mouseDragged(MouseEvent e)
		{
			panelMouse_dragged(e);
		}
};

	public ReportTemplatePanel(ReportMDIMain rtbw) throws CreateReportException
	{
		rtbWindow = rtbw;

		reportTemplate = new ReportTemplate(rtbWindow.aContext.
			getDataSourceInterface(), "A4");
		try
		{
			jbInit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public ReportTemplatePanel(ReportMDIMain rtbw, ReportTemplate rt) throws
		CreateReportException
	{
		rtbWindow = rtbw;

		reportTemplate = rt;
		try
		{
			jbInit();
			setReportTemplate(rt);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception
	{
		this.setBackground(Color.white);
		this.setLayout(xYLayout1);
		this.addMouseListener(panelMouseListener);
		this.addMouseMotionListener(panelMouseMotionListener);

		DropTarget dropTarget = new DropTarget(this, this);
		dropTarget.setActive(true);

		this.rtbWindow.aContext.getDispatcher().register(this,
			SelectReportsPanel.ev_onlyReportRealized);
	}

	/**
	 * Задаёт поля на шаблоне
*/
	public void setImagableRect()
	{
		java.awt.print.PageFormat defaultPage =
			java.awt.print.PrinterJob.getPrinterJob().defaultPage();

		float koefW = (float) (defaultPage.getImageableWidth() /
			defaultPage.getWidth());
		int defaultFieldWSize = (int) (ReportTemplate.A4.width * (1 - koefW));

		float koefH = (float) (defaultPage.getImageableHeight() /
			defaultPage.getHeight());
		int defaultFieldHSize = (int) (ReportTemplate.A4.height * (1 - koefH));

		imagableRect = new Rectangle(
			defaultFieldWSize / 2,
			defaultFieldHSize / 2,
			this.getWidth() - defaultFieldWSize,
			this.getHeight() - defaultFieldHSize);
	}

	/**
	 * Заменяет шаблон, отображаемый панелью, на заданный
	 * @param rt задаваемый шаблон
*/
	public void setReportTemplate(ReportTemplate rt)
	{
		this.removeAll();
		this.reportTemplate = rt;

		for (int i = 0; i < rt.labels.size(); i++)
		{
			FirmedTextPane tp = (FirmedTextPane) rt.labels.get(i);
			this.add(tp, new XYConstraints(tp.getX(), tp.getY(), -1, -1));

			tp.setBorder(BorderFactory.createLineBorder(Color.lightGray, 2));
			//Устанавливаем для надписей listener'ы
			this.setLabelListener(tp);
		}

		for (int i = 0; i < rt.images.size(); i++)
		{
			ImagePanel ip = (ImagePanel) rt.images.get(i);
			this.add(ip, new XYConstraints(ip.getX(), ip.getY(), -1, -1));
			this.setImagePanelListener(ip);
		}


		//Обработчики для объектов в самой панели

		this.repaint();
	}

	/**
	 * @param x координата точки по горизонтали
	 * @param y координата точки по вертикали
	 * @return элемент шаблона, которому принадлежат данные координаты
*/
	private RenderingObject identifyObject(int x, int y)
	{
		for (int i = 0; i < reportTemplate.objectRenderers.size(); i++)
		{
			RenderingObject ror = (RenderingObject) reportTemplate.
				objectRenderers.
				get(i);
			if ((ror.x - 2 < x) && (x < ror.x + ror.width + 2) &&
				(ror.y - 2 < y) && (y < ror.y + ror.height + 2))
				return ror;
		}
		return null;
	}

	/**
	 * @return возвращает true, если объекты на схеме не пересекаются
*/
	public boolean checkTemplatesScheme()
	{
		// Проверка пересечений объектов отчёта
		for (int i = 0; i < reportTemplate.objectRenderers.size() - 1; i++)
		{
			RenderingObject roI = (RenderingObject) reportTemplate.
				objectRenderers.get(i);

			for (int j = i + 1; j < reportTemplate.objectRenderers.size(); j++)
			{
				RenderingObject roJ = (RenderingObject) reportTemplate.
					objectRenderers.get(j);

				if (  roJ.hasPoint(roI.x,roI.y,null)
					||	roJ.hasPoint(roI.x + roI.width,roI.y,null)
					|| roJ.hasPoint(roI.x,roI.y + roI.height,null)
					|| roJ.hasPoint(roI.x + roI.width,roI.y + roI.height,null))
					return false;
			}
		}

		// Проверка пересечений объектов отчёта с надписями
		for (int i = 0; i < reportTemplate.objectRenderers.size(); i++)
		{
			RenderingObject roI = (RenderingObject) reportTemplate.
				objectRenderers.get(i);

			for (int j = 0; j < reportTemplate.labels.size(); j++)
			{
				FirmedTextPane tpJ = (FirmedTextPane) reportTemplate.
					labels.get(j);

				if (  tpJ.hasPoint(roI.x,roI.y)
					||	tpJ.hasPoint(roI.x + roI.width,roI.y)
					|| tpJ.hasPoint(roI.x,roI.y + roI.height)
					|| tpJ.hasPoint(roI.x + roI.width,roI.y + roI.height))
					return false;
			}
		}

		// Проверка пересечений объектов отчёта с надписями
		for (int i = 0; i < reportTemplate.objectRenderers.size(); i++)
		{
			RenderingObject roI = (RenderingObject) reportTemplate.
				objectRenderers.get(i);

			for (int j = 0; j < reportTemplate.labels.size(); j++)
			{
				FirmedTextPane tpJ = (FirmedTextPane) reportTemplate.
					labels.get(j);

				if (  roI.hasPoint(tpJ.getX(),tpJ.getY(),null)
					||	roI.hasPoint(tpJ.getX() + tpJ.getWidth(),tpJ.getY(),null)
					|| roI.hasPoint(tpJ.getX(),tpJ.getY() + tpJ.getHeight(),null)
					|| roI.hasPoint(tpJ.getX() + tpJ.getWidth(),tpJ.getY() + tpJ.getHeight(),null))
					return false;
			}
		}

		// Проверка пересечений надписей
		for (int i = 0; i < reportTemplate.labels.size() - 1; i++)
		{
			FirmedTextPane tpI = (FirmedTextPane) reportTemplate.
				labels.get(i);

			for (int j = i + 1; j < reportTemplate.labels.size(); j++)
			{
				FirmedTextPane tpJ = (FirmedTextPane) reportTemplate.
					labels.get(j);

				if (  tpJ.hasPoint(tpI.getX(),tpI.getY())
					||	tpJ.hasPoint(tpI.getX() + tpI.getWidth(),tpI.getY())
					|| tpJ.hasPoint(tpI.getX(),tpI.getY() + tpI.getHeight())
					|| tpJ.hasPoint(tpI.getX() + tpI.getWidth(),tpI.getY() + tpI.getHeight()))
					return false;
			}
		}

		return true;
	}

	/**
	 * Рисует поля, элементы шаблона и синюю рамку вокруг выделенного объекта
	 * @param g графический контекст
*/
	public void paint(Graphics g)
	{
		super.paint(g);

		g.setColor(new Color(200, 200, 200));
		g.drawRect(
			imagableRect.x,
			imagableRect.y,
			imagableRect.width,
			imagableRect.height);

		g.setColor(Color.black);

		for (int i = 0; i < this.reportTemplate.objectRenderers.size(); i++)
			((RenderingObject)this.reportTemplate.objectRenderers.get(i)).paint(g);

		if (this.selectedObject != null)
		{
			g.setColor(Color.blue);
			if (selectedObject instanceof RenderingObject)
			{
				RenderingObject ro = (RenderingObject) selectedObject;
				g.drawRect(ro.x - 2, ro.y - 2, ro.width + 4, ro.height + 4);
			}
			else if (selectedObject instanceof FirmedTextPane)
			{
				FirmedTextPane tp = (FirmedTextPane) selectedObject;

				Dimension tpDim = tp.getContentBounds();
				g.drawRect(tp.getX() - 2,
					tp.getY() - 2,
					(int) tp.getWidth() + 4,
					tpDim.height + 4);
			}
			else if (selectedObject instanceof ImagePanel)
			{
				ImagePanel ip = (ImagePanel) selectedObject;
				g.drawRect(
							  ip.getX() - 2,
							  ip.getY() - 2,
							  ip.getWidth() + 4,
							  ip.getHeight() + 4);
			}

			g.setColor(Color.black);
		}
	}

	private void textPaneKeyPressed(KeyEvent e)
	{
		reportTemplate.curModified = System.currentTimeMillis();
		this.repaint();
  }

	private void textPaneMouseClicked(MouseEvent e)
	{
		FirmedTextPane textPane = (FirmedTextPane) selectedObject;
		if (textPane.getText().equals("   "))
		{
			textPane.setText(" ");
			this.repaint();
		}
		if (SwingUtilities.isRightMouseButton(e))
		{
			JPopupMenu pm = new JPopupMenu();

			JMenuItem mi1 = new JMenuItem();
			mi1.setText(LangModelReport.getString("popup_font"));
			mi1.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(ActionEvent el)
				{
					FirmedTextPane textPane = (FirmedTextPane) selectedObject;
					FontChooserDialog fcDialog = new FontChooserDialog(
						textPane.getFont());

					fcDialog.show();
					if (FontChooserDialog.selectedFont == null)
						return;

					textPane.setFont(FontChooserDialog.selectedFont);
					textPaneKeyPressed(new KeyEvent(textPane, 0, 0, 0, 0, ' '));
					reportTemplate.curModified = System.currentTimeMillis();
				}
			});

			JMenuItem mi2 = new JMenuItem();
			mi2.setText(LangModelReport.getString("popup_priv_vert"));
			mi2.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(ActionEvent el)
				{
					Vector selectItems = new Vector();
					selectItems.add(LangModelReport.getString(FirmedTextPane.
						toFieldsTop));
					selectItems.add(LangModelReport.getString(FirmedTextPane.toTop));
					selectItems.add(LangModelReport.getString(FirmedTextPane.toBottom));

					FirmedTextPane tp = (FirmedTextPane) selectedObject;
					String oldValue = tp.verticalFirmTo;
					if (oldValue == null)
						oldValue = (String) selectItems.get(0);

					firmingType = null;
					firmingType = (String) JOptionPane.showInputDialog(
						Environment.getActiveWindow(),
						LangModelReport.getString("vybor_priv1"),
						LangModelReport.getString("vybor_priv2"),
						JOptionPane.QUESTION_MESSAGE,
						null,
						selectItems.toArray(),
						LangModelReport.getString(oldValue));

					if (firmingType == null)
						return;

					// Если мы выбрали привязку по полю
					if (firmingType.equals(LangModelReport.getString(FirmedTextPane.
						toFieldsTop)))
						firmingType = FirmedTextPane.toFieldsTop;

					if (firmingType.equals(LangModelReport.getString(FirmedTextPane.
						toTop)))
						firmingType = FirmedTextPane.toTop;

					if (firmingType.equals(LangModelReport.getString(FirmedTextPane.
						toBottom)))
						firmingType = FirmedTextPane.toBottom;

					if (firmingType.equals(FirmedTextPane.toFieldsTop))
					{
						tp = (FirmedTextPane) selectedObject;
						tp.setVertFirmedTo(null, firmingType);
						tp.setFirmingDistance();
						return;
					}

					// иначе ждём пока пользователь выберет объект
					rtbWindow.innerToolBar.setEditToolBarState(false);
					rtbWindow.toolBar.setTemplateToolBarState(false);
					labelToFirmTo = (FirmedTextPane) selectedObject;
					reportTemplate.curModified = System.currentTimeMillis();
				}
			});

			JMenuItem mi3 = new JMenuItem();
			mi3.setText(LangModelReport.getString("popup_priv_horiz"));
			mi3.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(ActionEvent el)
				{
					Vector selectItems = new Vector();
					selectItems.add(LangModelReport.getString(FirmedTextPane.
						toFieldsLeft));
					selectItems.add(LangModelReport.getString(FirmedTextPane.toLeft));
					selectItems.add(LangModelReport.getString(FirmedTextPane.toRight));

					FirmedTextPane tp = (FirmedTextPane) selectedObject;
					String oldValue = tp.horizontalFirmTo;
					if (oldValue == null)
						oldValue = (String) selectItems.get(0);

					firmingType = null;
					firmingType = (String) JOptionPane.showInputDialog(
						Environment.getActiveWindow(),
						LangModelReport.getString("vybor_priv1"),
						LangModelReport.getString("vybor_priv2"),
						JOptionPane.QUESTION_MESSAGE,
						null,
						selectItems.toArray(),
						LangModelReport.getString(oldValue));

					if (firmingType == null)
						return;

					// Если мы выбрали привязку по полю
					if (firmingType.equals(LangModelReport.getString(FirmedTextPane.
						toFieldsLeft)))
						firmingType = FirmedTextPane.toFieldsLeft;

					if (firmingType.equals(LangModelReport.getString(FirmedTextPane.
						toLeft)))
						firmingType = FirmedTextPane.toLeft;

					if (firmingType.equals(LangModelReport.getString(FirmedTextPane.
						toRight)))
						firmingType = FirmedTextPane.toRight;

					if (firmingType.equals(FirmedTextPane.toFieldsLeft))
					{
						tp = (FirmedTextPane) selectedObject;
						tp.setHorizFirmedTo(null, firmingType);
						tp.setFirmingDistance();
						return;
					}

					// иначе ждём пока пользователь выберет объект
					rtbWindow.innerToolBar.setEditToolBarState(false);
					rtbWindow.toolBar.setTemplateToolBarState(false);
					labelToFirmTo = (FirmedTextPane) selectedObject;
					reportTemplate.curModified = System.currentTimeMillis();
				}
			});

			JMenuItem mi4 = new JMenuItem();
			mi4.setText(LangModelReport.getString("popup_cancel_priv"));
			mi4.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(ActionEvent el)
				{
					FirmedTextPane tp = (FirmedTextPane) selectedObject;
					tp.setHorizFirmedTo(null, FirmedTextPane.toFieldsLeft);
					tp.setVertFirmedTo(null, FirmedTextPane.toFieldsLeft);
					tp.setFirmingDistance();
					reportTemplate.curModified = System.currentTimeMillis();
					return;
				}
			});

			pm.add(mi1);
			pm.add(mi2);
			pm.add(mi3);
			pm.add(mi4);

			try
			{
				pm.show(this,
					((FirmedTextPane) e.getSource()).getX() + e.getX(),
					((FirmedTextPane) e.getSource()).getY() + e.getY());
			}
			catch (Exception eeee)
			{}
		}
	}

	private void textPaneMousePressed(MouseEvent e)
	{
		mousePressedX = e.getX();
		mousePressedY = e.getY();

		if (selectedObject instanceof FirmedTextPane)
		{
			FirmedTextPane textPane = (FirmedTextPane) selectedObject;
			if (textPane.getText().equals(""))
				textPane.setText("   ");
		}

		selectedObject = e.getSource();

		FirmedTextPane textPane = (FirmedTextPane) e.getSource();
		textPane.getCaret().setBlinkRate(500);
		textPane.getCaret().setVisible(true);
		textPane.setEditable(true);

		if (SwingUtilities.isLeftMouseButton(e))
			textPane.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		this.repaint();
	}

	private void textPaneMouseDragged(MouseEvent e)
	{
		FirmedTextPane textPane = (FirmedTextPane) e.getSource();

		// В режиме просмотра нельзя смотреть привязанные надписи
		if ((textPane.getParent()instanceof ReportTemplateImplementationPanel) &&
			((textPane.vertFirmer != null) || (textPane.horizFirmer != null)))
			return;

		int tpX = textPane.getX() + e.getX() - mousePressedX;
		int tpY = textPane.getY() + e.getY() - mousePressedY;

		int tpWidth = (int)textPane.getPreferredSize().getWidth();
		int tpHeight = (int)textPane.getPreferredSize().getHeight();


		if ((tpX <= imagableRect.x)
			|| (tpX + tpWidth >= imagableRect.x + imagableRect.width)
			|| (tpY <= imagableRect.y) ||
			(tpY + tpHeight >= imagableRect.y + imagableRect.height))
			return;

		Rectangle bounds = textPane.getBounds();
		bounds.x = tpX;
		bounds.y = tpY;

		textPane.setBounds(bounds);
//		textPane.setPreferredSize(new Dimension(tpWidth,tpHeight));

		JPanel owner = this;
		if (this.rtbWindow.layoutWCPanel != null)
			owner = this.rtbWindow.layoutWCPanel;

		owner.remove(textPane);
		owner.add(textPane, new XYConstraints(
			tpX,
			tpY,
			-1,
			-1));

		textPane.setFirmingDistance();
		reportTemplate.curModified = System.currentTimeMillis();
		this.repaint();
	}

	private void panelMouse_moved(MouseEvent e)
	{
		int x = e.getX();
		int y = e.getY();

		JPanel cursorOwner = this;
		Rectangle curCoords = null;

		if (e.getSource() instanceof ImagePanel)
		{
			curCoords = ((ImagePanel) e.getSource()).getBounds();
			cursorOwner = (ImagePanel) e.getSource();
			x += curCoords.x;
			y += curCoords.y;
		}
		else
		{
			RenderingObject renderer = this.identifyObject(x, y);
			if (renderer == null)
			{
				cursorOwner.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				return;
			}
			curCoords = renderer.getBounds();
		}

		if ((x < curCoords.x + 10) &&
			 (y < curCoords.y + 10))
	  {
		  cursorOwner.setCursor(new Cursor(Cursor.NW_RESIZE_CURSOR));
		  return;
	  }
		else if ((x > curCoords.x + curCoords.width - 10) &&
					(y > curCoords.y + curCoords.height - 10))
		{
			cursorOwner.setCursor(new Cursor(Cursor.SE_RESIZE_CURSOR));
			return;
		}
		else if ((x > curCoords.x + curCoords.width - 10) &&
					(y < curCoords.y + 10))
		{
			cursorOwner.setCursor(new Cursor(Cursor.NE_RESIZE_CURSOR));
			return;
		}
		else if ((x < curCoords.x + 10) &&
					(y > curCoords.y + curCoords.height - 10))
		{
			cursorOwner.setCursor(new Cursor(Cursor.SW_RESIZE_CURSOR));
			return;
		}
		else if (x < curCoords.x + 7)
		{
			cursorOwner.setCursor(new Cursor(Cursor.W_RESIZE_CURSOR));
			return;
		}
		else if (x > curCoords.x + curCoords.width - 7)
		{
			cursorOwner.setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));
			return;
		}
		else if (y < curCoords.y + 7)
		{
			cursorOwner.setCursor(new Cursor(Cursor.N_RESIZE_CURSOR));
			return;
		}
		else if (y > curCoords.y + curCoords.height - 7)
		{
			cursorOwner.setCursor(new Cursor(Cursor.S_RESIZE_CURSOR));
			return;
		}
		else
			cursorOwner.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
	}

	private void panelMouse_dragged(MouseEvent e)
	{
		if ((selectedRenderingObject == null) &&
			 !(e.getSource() instanceof ImagePanel))
			return;

		int dx = e.getX() - mousePressedX;
		int dy = e.getY() - mousePressedY;

		int newX = prevBounds.x;
		int newY = prevBounds.y;
		int newWidth = prevBounds.width;
		int newHeight = prevBounds.height;

		ImagePanel ip = null;
		if (e.getSource() instanceof ImagePanel)
			ip = (ImagePanel)e.getSource();

		if (curCursorName.equals("Crosshair Cursor"))
		{
			newX = prevBounds.x + dx;
			newY = prevBounds.y + dy;
		}
		else if (curCursorName.equals("East Resize Cursor"))
		{
			newWidth = prevBounds.width + dx;
			if (ip != null)
				mousePressedX = e.getX();
		}
		else if (curCursorName.equals("West Resize Cursor"))
		{
			newWidth = prevBounds.width - dx;
			newX = prevBounds.x + dx;
		}
		else if (curCursorName.equals("South Resize Cursor"))
		{
			newHeight = prevBounds.height + dy;
			if (ip != null)
				mousePressedY = e.getY();
		}
		else if (curCursorName.equals("North Resize Cursor"))
		{
			newHeight = prevBounds.height - dy;
			newY = prevBounds.y + dy;
		}
		else if (curCursorName.equals("Southeast Resize Cursor"))
		{
			newWidth = prevBounds.width + dx;
			newHeight = prevBounds.height + dy;
			if (ip != null)
			{
				mousePressedX = e.getX();
				mousePressedY = e.getY();
			}
		}
		else if (curCursorName.equals("Northeast Resize Cursor"))
		{
			newWidth = prevBounds.width + dx;
			if (ip != null)
				mousePressedX = e.getX();
			newHeight = prevBounds.height - dy;
			newY = prevBounds.y + dy;
		}
		else if (curCursorName.equals("Northwest Resize Cursor"))
		{
			newWidth = prevBounds.width - dx;
			newHeight = prevBounds.height - dy;
			newX = prevBounds.x + dx;
			newY = prevBounds.y + dy;
		}
		else if (curCursorName.equals("Southwest Resize Cursor"))
		{
			newWidth = prevBounds.width - dx;
			newHeight = prevBounds.height + dy;
			if (ip != null)
				mousePressedY = e.getY();
			newX = prevBounds.x + dx;
		}

		boolean smthChanged = false;

		if ((imagableRect.x <= newX)
			&& (newX + newWidth <= imagableRect.x + imagableRect.width))
		{
			if (selectedRenderingObject != null)
			{
				selectedRenderingObject.x = newX;
				selectedRenderingObject.width = newWidth;
				smthChanged = true;
			}
			else
			{
				ip.setLocation(newX,ip.getY());
				ip.setSize(newWidth,ip.getHeight());
				ip.setPreferredSize(ip.getSize());
			}
		}

		if ((imagableRect.y <= newY)
			&& (newY + newHeight <= imagableRect.y + imagableRect.height))
		{
			if (selectedRenderingObject != null)
			{
				selectedRenderingObject.y = newY;
				selectedRenderingObject.height = newHeight;
				smthChanged = true;
			}
			else
			{
				ip.setLocation(ip.getX(),newY);
				ip.setSize(ip.getWidth(),newHeight);
				ip.setPreferredSize(ip.getSize());
			}
		}

		if (selectedRenderingObject == null)
		{
			this.remove(ip);
			this.add(ip,new XYConstraints(ip.getX(),ip.getY(),-1,-1));
			this.repaint();
			prevBounds = ip.getBounds();
		}

		if (!smthChanged)
			return;

		for (int i = 0; i < this.reportTemplate.labels.size(); i++)
		{
			FirmedTextPane tp = (FirmedTextPane)this.reportTemplate.labels.
				get(i);
			if ((tp.vertFirmer != null) &&
				tp.vertFirmer.equals(selectedRenderingObject) ||
				(tp.horizFirmer != null) &&
				tp.horizFirmer.equals(selectedRenderingObject))
			{
				tp.refreshCoords(this.imagableRect);
				this.remove(tp);
				this.add(tp, new XYConstraints(tp.getX(), tp.getY(), -1, -1));
			}
		}

		reportTemplate.curModified = System.currentTimeMillis();

		this.repaint();
	}

	private void panelMouse_clicked(MouseEvent e)
	{
		if (selectedRenderingObject != null)
		{
			if (this.labelToFirmTo != null)
			{
				RenderingObject ro = (RenderingObject) selectedObject;

				if (firmingType.equals(FirmedTextPane.toFieldsTop) ||
					firmingType.equals(FirmedTextPane.toTop) ||
					firmingType.equals(FirmedTextPane.toBottom))
					this.labelToFirmTo.setVertFirmedTo(ro, firmingType);

				if (firmingType.equals(FirmedTextPane.toFieldsLeft) ||
					firmingType.equals(FirmedTextPane.toLeft) ||
					firmingType.equals(FirmedTextPane.toRight))
					this.labelToFirmTo.setHorizFirmedTo(ro, firmingType);

				this.labelToFirmTo.setFirmingDistance();
				this.labelToFirmTo = null;
				this.rtbWindow.innerToolBar.setEditToolBarState(true);
				this.rtbWindow.toolBar.setTemplateToolBarState(true);
			}
			else
			{
				if (SwingUtilities.isRightMouseButton(e))
				{
					JPopupMenu pm = new JPopupMenu();

					JMenuItem mi2 = new JMenuItem();
					mi2.setText(LangModelReport.getString("popup_vertRazb"));
					mi2.addActionListener(new java.awt.event.ActionListener()
					{
						public void actionPerformed(ActionEvent el)
						{
							while (true)
							{
								String inputValue = (String) JOptionPane.
									showInputDialog(
									Environment.getActiveWindow(),
									LangModelReport.getString("label_vertRazb"),
									Integer.toString(
									selectedRenderingObject.getTableDivisionsNumber()));

								if (inputValue == null)
									break;
								try
								{
									selectedRenderingObject.setTableDivisionsNumber(
										Integer.parseInt(inputValue));
									break;
								}
								catch (Exception eee)
								{
									JOptionPane.showMessageDialog(
										Environment.getActiveWindow(),
										LangModelReport.getString("error_numb_req"),
										LangModelReport.getString("label_error"),
										JOptionPane.ERROR_MESSAGE);
								}
							}
						}
					});

					pm.add(mi2);

					boolean mi2state = true;
					if (selectedRenderingObject.getReportToRender() == null)
						mi2state = false;
					else
					{
						String repType = selectedRenderingObject.getReportToRender().
							view_type;
						if (!(repType.equals(ObjectResourceReportModel.rt_statistics)
							|| repType.equals(ObjectResourceReportModel.
							rt_objectsReport)
							|| repType.equals(ObjectResourceReportModel.
							rt_objProperies)
							|| repType.equals(APOReportModel.rt_tableReport)))
							mi2state = false;
					}

					mi2.setEnabled(mi2state);

					pm.show(this_pointer, e.getX(), e.getY());
				}
			}
		}
		else
		{
			if (this.itemToAdd.equals(LangModelReport.getString("label_lb")))
			{
				FirmedTextPane newTextPane = new FirmedTextPane();
				selectedObject = newTextPane;

				this.add(newTextPane, new XYConstraints(
					e.getX(),
					e.getY(),
					-1,
					-1));
				newTextPane.setText("   ");

				newTextPane.setBorder(BorderFactory.createLineBorder(Color.
					lightGray, 2));

				this.setLabelListener(newTextPane);

				reportTemplate.labels.add(newTextPane);
			}
			else if (this.itemToAdd.equals(LangModelReport.getString("label_im")))
			{
				try
				{
					ImagePanel imagePane = new ImagePanel(e.getX(),
						e.getY());
					selectedObject = imagePane;

					this.add(imagePane, new XYConstraints(
						e.getX(),
						e.getY(),
						-1,
						-1));

					this.setImagePanelListener(imagePane);

					reportTemplate.images.add(imagePane);
				}
				catch (Exception exc)
				{}
			}

			this.itemToAdd = "";
			this.rtbWindow.innerToolBar.clearToggles();
		}

		this.repaint();
	}

	private void panelMouse_pressed(MouseEvent e)
	{
		if (selectedObject instanceof FirmedTextPane)
		{
			FirmedTextPane tp = (FirmedTextPane) selectedObject;
			tp.getCaret().setBlinkRate(0);
			tp.getCaret().setVisible(false);
			tp.setEditable(false);
			if (tp.getText().equals(""))
				tp.setText("   ");
		}

		RenderingObject renderer = this.identifyObject(e.getX(), e.getY());
		selectedRenderingObject = renderer;
		selectedObject = renderer;
		if (renderer == null)
			return;

		this.prevBounds = renderer.getBounds();

		mousePressedX = e.getX();
		mousePressedY = e.getY();
		curCursorName = getCursor().getName();
	}

	/**
	 * Удаляет выделенный объект
*/
	public void deleteselectedObject()
	{
		if (this.selectedObject != null)
		{
			if (selectedObject instanceof RenderingObject)
			{
				this.reportTemplate.objectRenderers.remove(selectedObject);

				Vector labels = this.reportTemplate.labels;
				for (int i = 0; i < labels.size(); i++)
				{
					FirmedTextPane curLabel = (FirmedTextPane) labels.get(i);
					if ((curLabel.vertFirmer != null)
						 && (curLabel.vertFirmer.equals(selectedObject))
						 && (curLabel.horizFirmer != null)
						 && (curLabel.horizFirmer.equals(selectedObject)))
					{
						labels.remove(curLabel);
						this.remove(curLabel);
					}
				}
			}
			else if (selectedObject instanceof FirmedTextPane)
			{
				this.reportTemplate.labels.remove(selectedObject);
				this.remove((FirmedTextPane) selectedObject);
			}
			else if (selectedObject instanceof ImagePanel)
			{
				this.reportTemplate.images.remove(selectedObject);
				this.remove((ImagePanel)selectedObject);
			}

			selectedObject = null;
			reportTemplate.curModified = System.currentTimeMillis();
			this.repaint();
		}
	}

	/**
	 * Преобразовывает отображённые данные в элементы схемы таким образом, что
	 * сохраняются расстояния между элементами
*/
	public void setSchemeObjectsNewParameters()
	{
		Vector theLabels = this.rtbWindow.layoutWCPanel.labels;
		Vector theImages = this.rtbWindow.layoutWCPanel.images;

		this.reportTemplate.setLabels((Vector) theLabels.clone());

		boolean[] labelsTransformed = new boolean[this.reportTemplate.labels.
			size()];
		for (int i = 0; i < labelsTransformed.length; i++)
			labelsTransformed[i] = false;

		boolean[] imagesTransformed = new boolean[this.reportTemplate.images.
			size()];
		for (int i = 0; i < imagesTransformed.length; i++)
			imagesTransformed[i] = false;

		boolean[] objectsTransformed = new boolean[reportTemplate.
			objectRenderers.size()];
		for (int i = 0; i < objectsTransformed.length; i++)
			objectsTransformed[i] = false;

		Vector xs = new Vector(); //строим карту отображённых элементов
		Vector ys = new Vector();
		getAxisValuesMatrices(xs, ys);

		boolean toBreak = false;
		while (!toBreak)
		{
			for (int i = 0; i < reportTemplate.objectRenderers.size(); i++)
			{
				if (objectsTransformed[i])
					continue;

				RenderingObject curObjectToPrint = (RenderingObject) reportTemplate.
					objectRenderers.get(i);

				int newY = checkToTopForElements(curObjectToPrint, xs, ys);
				if (newY == -2)
					continue;

				//вернёт координаты по вертикали для текущего эл-та

				if (curObjectToPrint.rendererPanel != null)
					curObjectToPrint.x = curObjectToPrint.rendererPanel.getX();
				else
					curObjectToPrint.x = curObjectToPrint.x;

				curObjectToPrint.y = newY;

				/*        curObjectToPrint.x += 5;
				 curObjectToPrint.y += 15;*/

				curObjectToPrint.width = curObjectToPrint.getTotalTableWidth();

				JComponent insidePanel = curObjectToPrint.rendererPanel.insidePanel;
				if ((insidePanel != null)
					&& ((insidePanel instanceof ReportChartPanel)
					|| (insidePanel instanceof MapRenderPanel)))
					curObjectToPrint.height = curObjectToPrint.rendererPanel.
getHeight();

				objectsTransformed[i] = true;
				break;
			}

			for (int i = 0; i < theLabels.size(); i++)
			{
				if (labelsTransformed[i])
					continue;

				FirmedTextPane curLabelToPrint = (FirmedTextPane) reportTemplate.
					labels.get(i);

				boolean labelCantBePrinted = false;
				for (int j = 0; j < objectsTransformed.length; j++)
				{
					RenderingObject curRO = (RenderingObject) reportTemplate.
						objectRenderers.get(j);
					if ((curLabelToPrint.vertFirmer != null) &&
						curLabelToPrint.vertFirmer.equals(curRO) ||
						(curLabelToPrint.horizFirmer != null) &&
						curLabelToPrint.horizFirmer.equals(curRO))
						if (!objectsTransformed[j])
						{
							labelCantBePrinted = true;
							break;
						}
				}

				if (labelCantBePrinted)
					continue;

				int newY = checkToTopForElements(curLabelToPrint, xs, ys);
				//вернёт -2 если есть нераспечатанные эл-ты
				//или координаты по вертикали для текущего эл-та
				if (newY == -2)
					continue;

				this.add(curLabelToPrint, new XYConstraints(
					curLabelToPrint.getX(),
					newY,
					-1,
					-1));

				curLabelToPrint.setLocation(curLabelToPrint.getX(), newY);
				labelsTransformed[i] = true;
				break;
			}

			for (int i = 0; i < theImages.size(); i++)
			{
				if (imagesTransformed[i])
					continue;

				ImagePanel curImageToPrint = (ImagePanel) reportTemplate.
					images.get(i);

				int newY = checkToTopForElements(curImageToPrint, xs, ys);
				//вернёт -2 если есть нераспечатанные эл-ты
				//или координаты по вертикали для текущего эл-та
				if (newY == -2)
					continue;

				curImageToPrint.removeMouseListener(curImageToPrint.getMouseListeners()[0]);
				curImageToPrint.removeMouseMotionListener(curImageToPrint.getMouseMotionListeners()[0]);
				this.setImagePanelListener(curImageToPrint);


				this.add(curImageToPrint, new XYConstraints(
					curImageToPrint.getX(),
					newY,
					-1,
					-1));

				curImageToPrint.setLocation(curImageToPrint.getX(), newY);
				imagesTransformed[i] = true;
				break;
			}

			toBreak = true;
			for (int i = 0; i < labelsTransformed.length; i++)
				if (!labelsTransformed[i])
				{
					toBreak = false;
					break;
				}
			for (int i = 0; i < objectsTransformed.length; i++)
				if (!objectsTransformed[i])
				{
					toBreak = false;
					break;
				}

		}
	}

	/**
	 *
	 * @param elem рассматриваемый объект
	 * @param xs сортированная матрица начал и концов элементов по горизонтали
	 * @param ys сортированная матрица начал и концов элементов по вертикали
	 * @return Возвращает значение (y + height + dist) для ближайшего
	 * к elem элемента сверху, где dist - расстояние по схеме от elem
	 * до этого объекта;
	 * 0, в случае если объектов выше нет;
	 * -2, если выше elem есть нераспечатанные элементы
*/
	private int checkToTopForElements(
		Object elem,
		Vector xs,
		Vector ys)
	{
		// Находим границы диапазона на котором мы проверяем наличие
		//объектов сверху
		Vector theLabels = this.rtbWindow.layoutWCPanel.labels;
		Vector theImages = this.rtbWindow.layoutWCPanel.images;
		Vector theObjects = this.rtbWindow.layoutWCPanel.objects;

		int elemX = 0;
		int elemY = 0;
		int elemWidth = 0;
		int elemHeight = 0;

		if (elem instanceof FirmedTextPane)
		{
			FirmedTextPane tp = (FirmedTextPane) elem;
			elemX = tp.getX();
			elemY = tp.getY();
			elemWidth = tp.getWidth();
			elemHeight = tp.getHeight();

			if (tp.vertFirmer != null)
			{
				int newY = tp.getY();

				if (tp.verticalFirmTo.equals(FirmedTextPane.toTop))
					newY = tp.vertFirmer.y + tp.distanceY;

				if (tp.verticalFirmTo.equals(FirmedTextPane.toBottom))
					newY = tp.vertFirmer.y + tp.vertFirmer.height + tp.distanceY;

				return newY;
			}
		}

		else if (elem instanceof ImagePanel)
		{
			ImagePanel ip = (ImagePanel) elem;
			elemX = ip.getX();
			elemY = ip.getY();
			elemWidth = ip.getWidth();
			elemHeight = ip.getHeight();
		}

		else if (elem instanceof RenderingObject)
		{
			RenderingObject ro = (RenderingObject) elem;

			if (ro.rendererPanel != null)
			{
				elemX = ro.rendererPanel.getX();
				elemY = ro.rendererPanel.getY();
				elemWidth = ro.rendererPanel.getWidth();
				elemHeight = ro.rendererPanel.getHeight();
			}
			else
			{
				elemX = ro.x;
				elemY = ro.y;
				elemWidth = ro.width;
				elemHeight = ro.height;
			}
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

				if (!(curMiddleY <= elemY))
					break;

				//Если нашли выше распечатанную надпись или объект
				int tempLLabelIndex = this.getLabelAt(curMiddleX, curMiddleY);
				int tempLImageIndex = this.getImageAt(curMiddleX, curMiddleY);
				int tempLObjectIndex = this.getRenderingObjectAt(curMiddleX,
					curMiddleY);

				if (tempLLabelIndex != -1)
				{
					FirmedTextPane lowestLabelWC = (FirmedTextPane) theLabels.get(
						tempLLabelIndex);
					int curSuggestedY = lowestLabelWC.getY()
						+ lowestLabelWC.getHeight();
					if (theLowestEdgeValue < curSuggestedY)
					{
						theLowestEdgeValue = curSuggestedY;
						theLowestLabelIndex = tempLLabelIndex;
						theLowestObjectIndex = -1;
						theLowestImageIndex = -1;
					}
				}

				if (tempLImageIndex != -1)
				{
					ImagePanel lowestImageWC = (ImagePanel) theImages.get(
						tempLImageIndex);
					int curSuggestedY = lowestImageWC.getY()
						+ lowestImageWC.getHeight();
					if (theLowestEdgeValue < curSuggestedY)
					{
						theLowestEdgeValue = curSuggestedY;
						theLowestImageIndex = tempLLabelIndex;
						theLowestObjectIndex = -1;
						theLowestLabelIndex = -1;
					}
				}

				if (tempLObjectIndex != -1)
				{
					RenderingObject ro = (RenderingObject) theObjects.get(
						tempLObjectIndex);
					int curSuggestedY = 0;
					if (ro.rendererPanel != null)
						curSuggestedY = ro.rendererPanel.getY() +
							ro.rendererPanel.getHeight();
					else
						curSuggestedY = ro.y + ro.height;

					if (theLowestEdgeValue < curSuggestedY)
					{
						theLowestEdgeValue = curSuggestedY;
						theLowestObjectIndex = tempLObjectIndex;
						theLowestImageIndex = -1;
						theLowestLabelIndex = -1;
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
			FirmedTextPane lowestLabelWC = (FirmedTextPane) theLabels.get(
				theLowestLabelIndex);
			FirmedTextPane lowestLabelWOC = (FirmedTextPane)this.reportTemplate.
				labels.get(theLowestLabelIndex);
			return (lowestLabelWOC.getY() + lowestLabelWOC.getHeight() +
				(elemY - (lowestLabelWC.getY() + lowestLabelWC.getHeight())));
		}

		if (theLowestImageIndex != -1)
		{
			ImagePanel lowestImageWC = (ImagePanel) theImages.get(
						theLowestImageIndex);
			ImagePanel lowestImageWOC = (ImagePanel) this.reportTemplate.
												 images.get(theLowestImageIndex);

			return (lowestImageWOC.getY() + lowestImageWOC.getHeight() +
				(elemY - (lowestImageWC.getY() + lowestImageWC.getHeight())));
		}

		if (theLowestObjectIndex != -1)
		{
			RenderingObject ro = (RenderingObject) theObjects.get(
				theLowestObjectIndex);

			if (ro.rendererPanel != null)
				return (elemY - (ro.rendererPanel.getY()
					+ ro.rendererPanel.getHeight()) +
					ro.y + ro.height);
			else
				return elemY;
		}
		return -7777;
	}

	/**
	 * Возвращает сортированные матрицы xs и ys начал и концов объектов
	 * и надписей по горизонтали и вертикали
	 * @param xs возвращаемые величины для горизонтали
	 * @param ys возвращаемые величины для вертикали
*/
	private void getAxisValuesMatrices(
		Vector xs,
		Vector ys)
	{
		Vector theLabels = this.rtbWindow.layoutWCPanel.labels;
		Vector theImages = this.rtbWindow.layoutWCPanel.images;
		Vector theObjects = this.rtbWindow.layoutWCPanel.objects;
		int elemCount = 0;

		for (int i = 0; i < theLabels.size(); i++)
		{
			FirmedTextPane curPane = (FirmedTextPane) theLabels.get(i);
			//Ищем только несвязные надписи
			if ((curPane.horizFirmer != null) ||
				(curPane.vertFirmer != null))
				continue;

			xs.add(new Integer(curPane.getX()));
			ys.add(new Integer(curPane.getY()));

			xs.add(new Integer(curPane.getX() + curPane.getWidth()));
			ys.add(new Integer(curPane.getY() + curPane.getHeight()));
			elemCount += 2;
		}

		for (int i = 0; i < theImages.size(); i++)
		{
			ImagePanel curImage = (ImagePanel) theImages.get(i);

			xs.add(new Integer(curImage.getX()));
			ys.add(new Integer(curImage.getY()));

			xs.add(new Integer(curImage.getX() + curImage.getWidth()));
			ys.add(new Integer(curImage.getY() + curImage.getHeight()));
			elemCount += 2;
		}

		for (int i = 0; i < theObjects.size(); i++)
		{
			RenderingObject curRO = (RenderingObject) theObjects.get(i);

			if (curRO.rendererPanel != null)
			{
				xs.add(new Integer(curRO.rendererPanel.getX()));
				ys.add(new Integer(curRO.rendererPanel.getY()));

				xs.add(new Integer(curRO.rendererPanel.getX() +
					curRO.rendererPanel.getWidth()));
				ys.add(new Integer(curRO.rendererPanel.getY() +
					curRO.rendererPanel.getHeight()));
			}
			else
			{
				xs.add(new Integer(curRO.x));
				ys.add(new Integer(curRO.y));
				xs.add(new Integer(curRO.x + curRO.width));
				ys.add(new Integer(curRO.y + curRO.height));
			}

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

				iValue = ((Integer) ys.get(i)).intValue();
				jValue = ((Integer) ys.get(j)).intValue();
				if (jValue < iValue)
				{
					ys.set(i, new Integer(jValue));
					ys.set(j, new Integer(iValue));
				}
			}
	}

	/**
	 *
	 * @param x координата точки по горизонтали
	 * @param y координата точки по вертикали
	 * @return Возвращает индекс надписи в векторе labels,
	 * которому принадлежит (x,y);
	 * -1, если в этой точке нет надписей;
	 * -2, если есть, но она не напечатана
*/
	private int getLabelAt(int x, int y)
	{
		Vector theLabels = this.rtbWindow.layoutWCPanel.labels;

		for (int i = 0; i < theLabels.size(); i++)
		{
			FirmedTextPane curLabel = (FirmedTextPane) theLabels.get(i);
			if ((curLabel.horizFirmer == null) &&
				(curLabel.vertFirmer == null) &&
				curLabel.hasPoint(x, y))
				return i;
		}
		return -1;
	}

	private int getImageAt(int x, int y)
	{
		Vector theImages = this.rtbWindow.layoutWCPanel.images;

		for (int i = 0; i < theImages.size(); i++)
		{
			ImagePanel curImage = (ImagePanel) theImages.get(i);
			if ((curImage.getX() < x)
				 && (x < curImage.getX() + curImage.getWidth())
				 && (curImage.getY() < y)
				 && (y < curImage.getY() + curImage.getHeight()))
				return i;
		}
		return -1;
	}

	/**
	 *
	 * @param x координата точки по горизонтали
	 * @param y координата точки по вертикали
	 * @return Возвращает индекс объекта в векторе objects,
	 * которому принадлежит (x,y);
	 * -1, если в этой точке нет объектов;
	 * -2, если есть, но он не напечатан
*/
	private int getRenderingObjectAt(int x, int y)
	{
		Vector theObjects = this.rtbWindow.layoutWCPanel.objects;
		for (int i = 0; i < theObjects.size(); i++)
		{
			RenderingObject curRO = (RenderingObject) theObjects.get(i);

			if (curRO.rendererPanel != null)
			{
				if ((curRO.rendererPanel.getX() < x) &&
					(x < curRO.rendererPanel.getX() + curRO.rendererPanel.getWidth()) &&
					(curRO.rendererPanel.getY() < y) &&
					(y < curRO.rendererPanel.getY() + curRO.rendererPanel.getHeight()))
					return i;
			}
			else
			{
				if ((curRO.x < x) && (x < curRO.x + curRO.width) &&
					(curRO.y < y) && (y < curRO.y + curRO.height))
					return i;
			}
		}
		return -1;
	}

	private void setImagePanelListener(ImagePanel imagePane)
	{
		imagePane.addMouseListener(new java.awt.event.MouseListener()
		{
			public void mouseClicked(MouseEvent e)
			{}

			public void mouseEntered(MouseEvent e)
			{}

			public void mouseExited(MouseEvent e)
			{}

			public void mousePressed(MouseEvent e)
			{
				ImagePanel ip = (ImagePanel)e.getSource();
				selectedObject = ip;
				selectedRenderingObject = null;
				prevBounds = ip.getBounds();

				mousePressedX = e.getX();
				mousePressedY = e.getY();
				curCursorName = ((ImagePanel)e.getSource()).getCursor().getName();

				repaint();
			}

			public void mouseReleased(MouseEvent e)
			{}
		});

		imagePane.addMouseMotionListener(new java.awt.event.MouseMotionListener()
		{
			public void mouseMoved(MouseEvent e)
			{
				panelMouse_moved(e);
			}

			public void mouseDragged(MouseEvent e)
			{
				panelMouse_dragged(e);
			}
		});
	}

	private void setLabelListener(FirmedTextPane label)
	{
		label.addKeyListener(new KeyAdapter()
		{
			public void keyPressed(KeyEvent e)
			{
				textPaneKeyPressed(e);
			}

			public void keyReleased(KeyEvent e)
			{}

			public void keyTyped(KeyEvent e)
			{}
		});

		label.addMouseListener(new java.awt.event.MouseListener()
		{
			public void mouseClicked(MouseEvent e)
			{}

			public void mouseEntered(MouseEvent e)
			{}

			public void mouseExited(MouseEvent e)
			{}

			public void mousePressed(MouseEvent e)
			{
				textPaneMousePressed(e);
			}

			public void mouseReleased(MouseEvent e)
			{
				((FirmedTextPane) e.getSource()).setCursor(new Cursor(Cursor.
					DEFAULT_CURSOR));

				textPaneMouseClicked(e);
			}
		});

		label.addMouseMotionListener(new java.awt.event.MouseMotionListener()
		{
			public void mouseMoved(MouseEvent e)
			{}

			public void mouseDragged(MouseEvent e)
			{
				textPaneMouseDragged(e);
			}
		});
	}

	/**
	 * Реализует элемент шаблона с заданным отчётом.
	 * Проверяет: нужны ли данному элементу шаблона дополнительные данные,
	 * присутствуют ли они в шаблоне (если нет - выдаётся предупреждение),
	 * нужна ли ещё какая-либо дополнительная информация (предлагается ввести).
	 * @param transf_rep добавляемый отчёт
	 * @param repLocation координаты отчёта
	 */
	public void addReport (ObjectsReport transf_rep,Point repLocation,Dimension size)
	{
		Dimension ro_size = null;
		if (size != null)
			ro_size = size;
		else
			ro_size = new Dimension (300,100);

		try
		{
			if (transf_rep.isToBeFilledImmediately)
			{
				if (transf_rep.view_type.equals(ObjectResourceReportModel.
														  rt_timefunction) ||
					 transf_rep.view_type.equals(ObjectResourceReportModel.
														  rt_gistogram))
				{
					TimeGraphProperties tgpDialog = new TimeGraphProperties(
						null,
						LangModelReport.getString("label_diagrProp"),
						true);
					tgpDialog.setLocation(300, 300);
					tgpDialog.setVisible(true);

					if (TimeGraphProperties.interval_value == null)
						return;

					transf_rep.setReserve(TimeGraphProperties.interval_value);
				}

				if (transf_rep.view_type.equals(ObjectResourceReportModel.
														  rt_objectsReport))
				{
					//Выбор полей
					if (((Vector) transf_rep.getReserve()).size() == 0)
					{
						JOptionPane.showMessageDialog(
							Environment.getActiveWindow(),
							LangModelReport.getString("error_emptyObjectReport"),
							LangModelReport.getString("label_error"),
							JOptionPane.ERROR_MESSAGE);
						return;
					}
				}

				if (transf_rep.model instanceof OptimizationReportModel)
				{
					if (SelectSolutionFrame.selectedScheme == null)
					{
						JOptionPane.showMessageDialog(
							Environment.getActiveWindow(),
							LangModelReport.getString("label_noSchemeSet"),
							LangModelReport.getString("label_error"),
							JOptionPane.ERROR_MESSAGE);
						return;
					}

					String scheme_id = SelectSolutionFrame.selectedScheme.id;
					transf_rep.setReserve(scheme_id);
				}
				if (transf_rep.field.equals(MapReportModel.rep_topology))
				{
					String scheme_id = SelectSolutionFrame.selectedScheme.id;

					if (SelectSolutionFrame.selectedMap == null)
					{
						JOptionPane.showMessageDialog(
							Environment.getActiveWindow(),
							LangModelReport.getString("label_noselectedTopology"),
							LangModelReport.getString("label_error"),
							JOptionPane.ERROR_MESSAGE);
						return;
					}

					String map_id = SelectSolutionFrame.selectedMap.id;

					transf_rep.setReserve(scheme_id + ":" + map_id);
				}
////////////
				if (transf_rep.field.equals(OptimizationReportModel.sourceData)
					 || transf_rep.field.equals(OptimizationReportModel.
														 equipFeatures)
					 || transf_rep.field.equals(OptimizationReportModel.
														 optimizeResults)
					 || transf_rep.field.equals(OptimizationReportModel.cost))
				{
					String scheme_id = SelectSolutionFrame.selectedScheme.id;

					if (SelectSolutionFrame.selectedSolution == null)
					{
						JOptionPane.showMessageDialog(
							Environment.getActiveWindow(),
							LangModelReport.getString("label_noSelectedSolution"),
							LangModelReport.getString("label_error"),
							JOptionPane.ERROR_MESSAGE);
						return;
					}

					String soInfo_id = SelectSolutionFrame.selectedSolution.id;

					transf_rep.setReserve(scheme_id + ":" + soInfo_id);
				}
			}
		}
		catch (CreateReportException exc)
		{
			System.out.println("Error setting reserve");
		}
////////////

		RenderingObject ro = new RenderingObject(repLocation.x, repLocation.y,
															  ro_size);
		ro.setReportToRender(transf_rep);
		reportTemplate.curModified = System.currentTimeMillis();

		selectedObject = ro;

		this.reportTemplate.objectRenderers.add(ro);
		ro.paint(this.getGraphics());

		//Создание надписи - заголовка отчёта

		FirmedTextPane roTP = new FirmedTextPane();
		String roTPtext = transf_rep.model.getReportsName(transf_rep);

		int divPosit = roTPtext.indexOf(':',roTPtext.indexOf(':') + 1);
		if (divPosit != -1)
			roTPtext = roTPtext.substring(0,divPosit + 1) + "\n" +
				roTPtext.substring(divPosit + 1);

		roTP.setText(roTPtext);

		roTP.setFont(new Font("Arial",Font.BOLD,14));

		this.add(roTP, new XYConstraints(
			(int)(ro.x + (ro.width - roTP.getContentBounds().getWidth())/2),
			(int)(ro.y - roTP.getPreferredSize().getHeight() - 10),
			-1,
			-1));

		roTP.setLocation(
			(int)(ro.x + (ro.width - roTP.getContentBounds().getWidth())/2),
			(int)(ro.y - roTP.getPreferredSize().getHeight() - 10));

		roTP.setBorder(BorderFactory.createLineBorder(Color.
			lightGray, 2));

		roTP.setVertFirmedTo(ro,FirmedTextPane.toTop);
		roTP.setHorizFirmedTo(ro,FirmedTextPane.toLeft);
		roTP.setFirmingDistance();

		this.setLabelListener(roTP);
		reportTemplate.labels.add(roTP);
		//Вот этот вот код

		this.repaint();
	}


	public void drop(DropTargetDropEvent dtde)
	{
		Point repPoint = dtde.getLocation();

		DataFlavor[] df = dtde.getCurrentDataFlavors();
		try
		{
			if (df[0].getHumanPresentableName().equals("ObjectReportLabel"))
			{
				ObjectsReport transf_rep = (ObjectsReport)
					((ObjectsReport) (dtde.getTransferable()).getTransferData(df[0])).
					clone();

				this.addReport(transf_rep,repPoint,null);

				dtde.acceptDrop(DnDConstants.ACTION_MOVE);
				dtde.getDropTargetContext().dropComplete(true);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void dragEnter(DropTargetDragEvent dtde)
	{}

	public void dragExit(DropTargetEvent dte)
	{}

	public void dragOver(DropTargetDragEvent dtde)
	{}

	public void dropActionChanged(DropTargetDragEvent dtde)
	{}

	public void operationPerformed (OperationEvent oe)
	{
		if (oe.getActionCommand().equals(SelectReportsPanel.ev_onlyReportRealized))
		{
			if (!rtbWindow.isTemplateSchemeMode)
				this.rtbWindow.innerToolBar.changeViewButton_actionPerformed();

			this.removeAll();
			this.reportTemplate.objectRenderers.clear();
			this.reportTemplate.labels.clear();
			this.reportTemplate.images.clear();

			Point location = new Point (this.imagableRect.x + 10,this.imagableRect.y + 70);
			Dimension size = new Dimension (this.imagableRect.width - 30,500);
			this.addReport((ObjectsReport)oe.getSource(),location,size);

			this.rtbWindow.innerToolBar.changeViewButton_actionPerformed();
		}
	}
}
