package com.syrus.AMFICOM.Client.General.Filter;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JDialog;
import javax.swing.JButton;

import java.util.Vector;
import java.util.LinkedList;

import java.io.ObjectInputStream;
import java.io.IOException;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelReport;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.filter.*;
import java.awt.event.*;

public class LogicScheme extends LogicSchemeBase
{
	public LogicScheme(ObjectResourceFilter filter)
	{
		super(filter);
//    	this.restoreSchemeByText();
	}

	public Object clone(ObjectResourceFilter orf)
	{
		LogicScheme ls = new LogicScheme(orf);

		ls.schemeElements = (Vector) this.schemeElements.clone();
		ls.finishedLinks = (Vector) this.finishedLinks.clone();
		ls.activeZones = (Vector) this.activeZones.clone();

		for (int i = 0; i < ls.schemeElements.size(); i++)
		{
			LogicSchemeElement curLSE = (LogicSchemeElement) ls.schemeElements.get(i);
			if (curLSE.type.equals(LogicSchemeElement.t_result))
			{
				ls.treeResult = curLSE;
				break;
			}
		}

		ls.schemeWidth = this.schemeWidth;
		ls.schemeHeight = this.schemeHeight;

		return ls;
	}

	FilterExpressionInterface createFilterExpression()
	{
		return new FilterExpression();
	}

	public ElementsActiveZoneBase createElementsActiveZone(
			LogicSchemeElementBase owner,
				String zoneType,
				int size,
				int x,
				int y)
	{
		return new ElementsActiveZone(
				(LogicSchemeElement )owner,
				zoneType,
				size,
				x,
				y);
	}

	public FinishedLinkBase createFinishedLink(ElementsActiveZoneBase from, ElementsActiveZoneBase to)
	{
		return new FinishedLink((ElementsActiveZone )from, (ElementsActiveZone )to);
	}

	public LogicSchemeElementBase createLogicSchemeElement(
			String type,
				FilterExpressionInterface fe,
				String operandType,
				int itsX,
				int itsY,
				LogicSchemeBase ls)
	{
		return new LogicSchemeElement(
				type,
				fe,
				operandType,
				itsX,
				itsY,
				(LogicScheme )ls);
	}

	//В текст и из текста
	public String getTextValue ()
	{
		if (this.checkScheme())
			return getTextFor((LogicSchemeElement )treeResult);
		return "N/A";
	}

	private String getTextFor(LogicSchemeElement top)
	{
		if (top.type.equals(LogicSchemeElement.t_condition))
			return "\"" + LangModel.getString(LogicSchemeElement.t_condition) + " " + top.filterExpression.getListID() + "\"";

		LinkedList allTopLinks = top.input.getLinks();

		String result = "";

		for (int i = 0; i < allTopLinks.size(); i++)
		{
			FinishedLink fl = (FinishedLink )allTopLinks.get(i);

			LogicSchemeElement curInputElement = null;
			if (fl.az1.zoneType.equals(ElementsActiveZone.zt_out))
				curInputElement = (LogicSchemeElement )fl.az1.owner;
			if (fl.az1.zoneType.equals(ElementsActiveZone.zt_in))
				curInputElement = (LogicSchemeElement )fl.az2.owner;

			if (i == 0)
				result = getTextFor(curInputElement);
			else
				result = result + " " + LangModel.getString(top.operandType) + " " + getTextFor(curInputElement);
		}

		if (top != treeResult)
			return "(" + result + ")";

		return result;
	}

	private void setSchemeCoords()
	{
		//Находим максимальное расстояние между элементами в дереве
		int maxDistance = 0;
		for (int i = 0; i < this.schemeElements.size(); i++)
		{
			int distance = findDistanceFromResult(
				(LogicSchemeElement )this.schemeElements.get(i));

			if (distance > maxDistance)
				maxDistance = distance;
		}

		//устанавливаем габариты в схеме
		schemeWidth = maxDistance * 40 +
				(maxDistance + 1) * LogicSchemeElement.width;

		schemeHeight = this.getRestrictionsNumber() *
				(LogicSchemeElement.height + 10);

		//Теперь устанавливаем координаты в дереве
		//Сначала ставим координаты для treeResult
		//Потом находим все элементы одного слоя и в цикле размещаем их сверху вниз

		treeResult.x = schemeWidth - LogicSchemeElement.width;
		treeResult.y = schemeHeight / 2 - LogicSchemeElement.height / 2;

		for (int curDist = 1; curDist <= maxDistance; curDist++)
		{
			int elemOfCurDistNumber = 0;
			for (int i = 0; i < this.schemeElements.size(); i++)
			{
				LogicSchemeElement curElem =
						(LogicSchemeElement)this.schemeElements.get(i);
				int distance = findDistanceFromResult(curElem);

				if (distance == curDist)
				{
					curElem.x = schemeWidth - (curDist + 1) * LogicSchemeElement.width - curDist * 40;
					curElem.y = 10 * (elemOfCurDistNumber + 1) + LogicSchemeElement.height * elemOfCurDistNumber;
					elemOfCurDistNumber++;
				}
			}
		}
	}

	public void setUnfilledFilterExpressions(ApplicationContext aContext)
	{
		for (int i = 0; i < this.schemeElements.size(); i++)
		{
			LogicSchemeElement curLSE = (LogicSchemeElement) this.schemeElements.get(i);
			if (curLSE.type.equals(LogicSchemeElementBase.t_condition))
			{
				FilterExpressionInterface fe = curLSE.filterExpression;
				if (fe.isTemplate())
				{
					String type = (String) fe.getVec().get(0);
					FilterPanel fp = ( (ObjectResourceFilter)this.filter).
										  getColumnFilterPanel(fe.getId(), type);
					fp.setContext(aContext);

					JFrame frame = null;
					final JDialog dialog = new JDialog(
						frame,
						fe.getName(),
						true);

					JButton applyButton = new JButton (LangModelReport.getString("label_apply"));
					applyButton.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							dialog.dispose();
						}
					});

					dialog.setSize(400,300);

					Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
					dialog.setLocation(
						(int)(screenSize.getWidth() - dialog.getWidth())/2,
						(int)(screenSize.getHeight() - dialog.getHeight())/2);

					dialog.getContentPane().setLayout(new BorderLayout());
					dialog.getContentPane().add(fp,BorderLayout.CENTER);
					dialog.getContentPane().add(applyButton,BorderLayout.SOUTH);

					dialog.setVisible(true);

					curLSE.filterExpression = fp.getExpression(fe.getId(),fe.getColumnName(),false);
					((FilterExpression)curLSE.filterExpression).setTemplate(true);
					((FilterExpression)curLSE.filterExpression).setListID(fe.getListID());
				}
			}
		}
	}

	public void readObject(ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		// При восстановлении схемы мы получаем выражения фильтрации, не
		// наследующие ObjectResource - не поддреживающие отображение в
		// визуальных компонентах
		super.readObject(in);

		for (int i = 0; i < schemeElements.size(); i++)
		{
			LogicSchemeElementBase lse = (LogicSchemeElementBase) schemeElements.get(i);
			lse.filterExpression = new FilterExpression ((FilterExpressionBase)lse.filterExpression);
		}
	}

}
