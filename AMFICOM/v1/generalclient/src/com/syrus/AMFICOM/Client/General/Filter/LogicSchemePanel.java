package com.syrus.AMFICOM.Client.General.Filter;

import java.awt.*;
import java.util.Vector;
import javax.swing.*;
import java.awt.event.MouseEvent;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.Filter.LogicSchemeElement;
import com.syrus.AMFICOM.Client.General.Filter.LogicScheme;
import com.syrus.AMFICOM.Client.General.Filter.FinishedLink;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;

import com.syrus.AMFICOM.filter.*;

public class LogicSchemePanel extends JPanel
{
	LogicScheme logicScheme = null;
	private LogicSchemeWindow lsWindow = null;

	ProSchemeElementBase selectedElement = null;
	ElementsActiveZone firstActiveZone = null;

	private int mouseClickedX = 0;
	private int mouseClickedY = 0;

	private int objectMouseClickedX = 0;
	private int objectMouseClickedY = 0;

	private int mouseX = 0;
	private int mouseY = 0;

  public LogicSchemePanel(LogicSchemeWindow lsWindow)
  {
	 try
	 {
		this.lsWindow = lsWindow;
	  if(lsWindow.filter != null)
			this.logicScheme = lsWindow.filter.logicScheme;
		jbInit();
	 }
	 catch(Exception e)
	 {
		e.printStackTrace();
	 }
  }

	public void setFilter (ObjectResourceFilter orf)
	{
		if(orf != null)
			this.logicScheme = orf.logicScheme;
	}


	private void jbInit() throws Exception
	{
		java.awt.event.MouseListener mouseListener =
		new java.awt.event.MouseListener()
		{
			public void mouseClicked(MouseEvent e)
			{
				mouse_clicked(e);
			}
			public void mouseEntered(MouseEvent e){}
			public void mouseExited(MouseEvent e){}
			public void mousePressed(MouseEvent e)
			{
				mouse_pressed(e);
			}
			public void mouseReleased(MouseEvent e)
			{
				mouse_released(e);
			}
		};

		java.awt.event.MouseMotionListener mouseMotionListener =
		new java.awt.event.MouseMotionListener()
		{
			public void mouseMoved(MouseEvent e){}
			public void mouseDragged(MouseEvent e)
			{
				mouse_dragged(e);
			}
		};

		this.addMouseListener(mouseListener);
		this.addMouseMotionListener(mouseMotionListener);
	}

  public void addNewSchemeElement(MouseEvent e)
  {
	 if (!(this.lsWindow.lsWindowButtonPressed.equals("")))
	 {
		LogicSchemeElement se = new LogicSchemeElement(
			 LogicSchemeElement.t_operand,
			 null,
			 this.lsWindow.lsWindowButtonPressed,
			 e.getX(),
			 e.getY(),
			 logicScheme);

		logicScheme.schemeElements.add(se);
		this.lsWindow.lsWindowButtonPressed = "";
		this.lsWindow.clearAllToggles();

		this.setPanelSize();
		this.repaint();
	 }
  }

  public void paint(Graphics g)
  {
	 super.paint(g);

	 for (int i = 0; i < logicScheme.schemeElements.size(); i++)
		((LogicSchemeElement)logicScheme.schemeElements.get(i)).paint(g);

	 for (int i = 0; i < logicScheme.finishedLinks.size(); i++)
		((FinishedLink)logicScheme.finishedLinks.get(i)).paint(g);

	 for (int i = 0; i < logicScheme.activeZones.size(); i++)
		((ElementsActiveZone)logicScheme.activeZones.get(i)).paint(g);

	 if (firstActiveZone != null){
		g.setColor(Color.black);
		g.drawLine(firstActiveZone.owner.x + firstActiveZone.x + firstActiveZone.size / 2,
					  firstActiveZone.owner.y + firstActiveZone.y + firstActiveZone.size / 2,
					  mouseX,
					  mouseY);
	 }
  }

  private void mouse_dragged(MouseEvent e)
  {
	 if (!SwingUtilities.isLeftMouseButton(e))
		return;

	 if (firstActiveZone != null)
	 {
		mouseX = e.getX();
		mouseY = e.getY();
		this.repaint();
		return;
	 }

	 if (selectedElement == null)
		return;

	 if (selectedElement.getTyp().equals(LogicSchemeElementBase.TYP))
	 {
		LogicSchemeElement se = (LogicSchemeElement)selectedElement;
		se.x = objectMouseClickedX + e.getX() - mouseClickedX;
		se.y = objectMouseClickedY + e.getY() - mouseClickedY;
		this.repaint();
	 }
  }

  private void mouse_pressed(MouseEvent e)
  {
	 if (!SwingUtilities.isLeftMouseButton(e))
		return;

	 mouseClickedX = e.getX();
	 mouseClickedY = e.getY();

	 if (selectedElement != null)
		selectedElement.selected = false;
	 selectedElement = logicScheme.identifyObject(e.getX(), e.getY());

	 if (selectedElement == null)
		return;

	 if (selectedElement.getTyp().equals(LogicSchemeElementBase.TYP))
	 {
		objectMouseClickedX = selectedElement.x;
		objectMouseClickedY = selectedElement.y;
	 }

	 if (selectedElement.getTyp().equals(ElementsActiveZoneBase.TYP))
	 {
		firstActiveZone = (ElementsActiveZone)selectedElement;
	 }
  }

  private void mouse_released(MouseEvent e)
  {
	 selectedElement = logicScheme.identifyObject(e.getX(), e.getY());
	 if (selectedElement == null)
		return;

	 selectedElement.selected = true;

	 if (selectedElement.getTyp().equals(ElementsActiveZoneBase.TYP))
	 {
		if (firstActiveZone != null)
		{
		  ElementsActiveZone secondActiveZone = (ElementsActiveZone)selectedElement;
		  logicScheme.tryToAddLink(firstActiveZone,secondActiveZone);
		  lsWindow.refreshLSTextValue();
		}
	 }
	 firstActiveZone = null;

	 this.repaint();
  }

  private void mouse_clicked(MouseEvent e)
  {
	 if (selectedElement == null)
	 {
		addNewSchemeElement(e);
		lsWindow.refreshLSTextValue();
		return;
	 }
  }

  public void deleteSelectedElementWithWarning()
  {
	 if (selectedElement == null)
		return;

	 if (selectedElement.getTyp().equals(LogicSchemeElementBase.TYP))
	 {
		LogicSchemeElement se = (LogicSchemeElement)selectedElement;

		if (se.type.equals(LogicSchemeElement.t_result) ||
			 se.type.equals(LogicSchemeElement.t_condition))
		{
		  JOptionPane.showMessageDialog(
				Environment.getActiveWindow(),
				LangModel.getString("label_cantDeleteComp"),
				LangModel.getString("label_error"),
				JOptionPane.ERROR_MESSAGE);
		  return;
		}
	 }

	 logicScheme.deleteElement(selectedElement);
	 this.repaint();
  }

  public void setPanelSize()
  {
	 int maxX = 0;
	 int maxY = 0;

	 for (int i = 0; i < logicScheme.schemeElements.size(); i++)
	 {
		LogicSchemeElement se =
					  (LogicSchemeElement)logicScheme.schemeElements.get(i);
		if (se.x > maxX)
		  maxX = se.x;

		if (se.y > maxY)
		  maxY = se.y;
	 }

	 this.setSize(new Dimension(maxX + 120 + 150, maxY + 20 + 50));
	 this.setPreferredSize(new Dimension(maxX + 120 + 150, maxY + 20 + 50));
  }

  public void setPanelSize(int w, int h)
  {
	 this.setSize(new Dimension(w + 150, h + 50));
	 this.setPreferredSize(this.getSize());
  }
}
