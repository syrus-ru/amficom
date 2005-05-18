/*
 * $Id: LogicSchemeBase.java,v 1.6 2005/05/18 12:42:49 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.filter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * @version $Revision: 1.6 $, $Date: 2005/05/18 12:42:49 $
 * @module filter_v1
 */
public class LogicSchemeBase implements Serializable
{
	private static final long serialVersionUID = 3258132453318537783L;

	public List schemeElements = new ArrayList();
	public List finishedLinks = new ArrayList();
	public List activeZones = new ArrayList();

	public Filter filter;

	public int schemeWidth = 0;
	public int schemeHeight = 0;

	protected LogicSchemeElementBase treeResult = null;

	public LogicSchemeBase(Filter filter)
	{
		this.filter = filter;
//		restoreSchemeByText();
	}

	//Генерация схемы по умолчанию - все условия через "И" подсоединены к
	//результату
	public void organizeStandartScheme()
	{
		this.finishedLinks.clear();
		this.activeZones.clear();

		List schemeEls = new ArrayList();
		for (ListIterator lIt = this.schemeElements.listIterator(); lIt.hasNext();)
		{
			LogicSchemeElementBase se = (LogicSchemeElementBase)lIt.next();

			if (se.type.equals(LogicSchemeElementBase.tCondition))
			{
				schemeEls.add(se);
				se.out.getLinks().clear();
				this.activeZones.add(se.out);
			}
		}
		this.schemeElements = schemeEls;

		this.treeResult = createLogicSchemeElement(
				LogicSchemeElementBase.tResult,
				null,
				"",
				300,
				50,
				this);

		this.schemeElements.add(this.treeResult);

		LogicSchemeElementBase andElem = createLogicSchemeElement(
				LogicSchemeElementBase.tOperand,
				null,
				LogicSchemeElementBase.otAnd,
				150,
				50,
				this);

		this.schemeElements.add(andElem);

		for (int i = 0; i < this.schemeElements.size() - 2; i++)
		{
			LogicSchemeElementBase se = (LogicSchemeElementBase)this.schemeElements.get(i);
			tryToAddLink(se.out, andElem.input);
		}
		tryToAddLink(andElem.out, this.treeResult.input);

		for (int i = 0; i < this.schemeElements.size(); i++)
		{
			LogicSchemeElementBase se = (LogicSchemeElementBase)this.schemeElements.get(i);
			if (se.type.equals(LogicSchemeElementBase.tCondition))
			{
				se.x = 5;
				se.y = 5 + (LogicSchemeElementBase.height + 3) * i;
			}
		}
	}

	//Определяем какому объекту принадлежит точка
	public ProSchemeElementBase identifyObject (int x, int y)
	{
		for (int i = 0; i < this.activeZones.size(); i++)
		{
			ElementsActiveZoneBase az = (ElementsActiveZoneBase)this.activeZones.get(i);
			if ((az.owner.x + az.x < x) && (x < az.owner.x + az.x + az.size) &&
				(az.owner.y + az.y < y) && (y < az.owner.y + az.y + az.size))
				return az;
		}

		for (int i = 0; i < this.finishedLinks.size(); i++)
		{
			FinishedLinkBase fl = (FinishedLinkBase)this.finishedLinks.get(i);
			if (fl.tryToSelect(x,y))
				return fl;
		}

		for (int i = 0; i < this.schemeElements.size(); i++)
		{
			LogicSchemeElementBase se = (LogicSchemeElementBase)this.schemeElements.get(i);
			if ((se.x < x) && (x < se.x + LogicSchemeElementBase.width) &&
				(se.y < y) && (y < se.y + LogicSchemeElementBase.height))
				return se;
		}

		return null;
	}

	//Добавление связи между эл-тами
	public void tryToAddLink (ElementsActiveZoneBase from, ElementsActiveZoneBase to)
	{
		if (from.zoneType.equals(to.zoneType))
			return;

		if (from.owner.type.equals(LogicSchemeElementBase.tResult) &&
				from.getLinks().size() == 1)
			return;

		if (to.owner.type.equals(LogicSchemeElementBase.tResult) &&
				to.getLinks().size() == 1)
			return;

		for (int i = 0; i < this.finishedLinks.size(); i++)
		{
			FinishedLinkBase fl = (FinishedLinkBase)this.finishedLinks.get(i);
			if (fl.az1.equals(from) && fl.az2.equals(to) ||
				fl.az2.equals(from) && fl.az1.equals(to))
			return;
		}

		FinishedLinkBase fl = createFinishedLink(from, to);
		this.finishedLinks.add (fl);
		from.addLink(fl);
		to.addLink(fl);
	}

	//Информация о схеме
	public int getRestrictionsNumber()
	{
		int result = 0;

		for (int i = 0; i < this.schemeElements.size(); i++)
		{
			LogicSchemeElementBase se = (LogicSchemeElementBase)this.schemeElements.get(i);
			if (se.type.equals(LogicSchemeElementBase.tCondition))
				result++;
		}

		return result;
	}

	public boolean schemeIsEmpty()
	{
		return (getFilterExpressions().size() == 0);
	}

	public boolean checkScheme()
	{
		if (getFilterExpressions().size() < getRestrictionsNumber())
			return false;

		if (schemeIsEmpty())
			return false;

		for (int i = 0; i < this.activeZones.size(); i++)
		{
			ElementsActiveZoneBase curActiveZone = (ElementsActiveZoneBase)this.activeZones.get(i);
			if (curActiveZone.getLinks().size() == 0)
				return false;
		}
		return true;
	}

	//Добавление/удаление элементов
	public void deleteElement(ProSchemeElementBase element)
	{
		if (element == null)
			return;

		if (element.getTyp().equals(LogicSchemeElementBase.TYP))
		{
			LogicSchemeElementBase se = (LogicSchemeElementBase)element;

			for (int i = 0; i < this.finishedLinks.size(); i++)
			{
				FinishedLinkBase fl = (FinishedLinkBase)this.finishedLinks.get(i);

				if (se.input != null)
					if (fl.az1.equals(se.input) || fl.az2.equals(se.input))
						if (this.finishedLinks.remove(fl))
						{
							fl.az1.removeLink(fl);
							fl.az2.removeLink(fl);
							i--;
						}

				if (se.out != null)
					if (fl.az1.equals(se.out) || fl.az2.equals(se.out))
						if (this.finishedLinks.remove(fl))
						{
							fl.az1.removeLink(fl);
							fl.az2.removeLink(fl);
							i--;
						}
			}

			if (se.input != null)
				this.activeZones.remove(se.input);

			if (se.out != null)
				this.activeZones.remove(se.out);

			this.schemeElements.remove(se);
		}

		if (element.getTyp().equals(FinishedLinkBase.TYP))
		{
			FinishedLinkBase fl = (FinishedLinkBase)element;
			fl.az1.removeLink(fl);
			fl.az2.removeLink(fl);
			this.finishedLinks.remove(fl);
		}
	}

	public void addFilterExpression(FilterExpressionInterface fe)
	{
		int maxY = 0;
		for (int i = 0; i < this.schemeElements.size(); i++)
		{
			LogicSchemeElementBase lse = (LogicSchemeElementBase)this.schemeElements.get(i);
			if (lse.y > maxY)
				maxY = lse.y;
		}

		LogicSchemeElementBase se = createLogicSchemeElement(
			LogicSchemeElementBase.tCondition,
			fe,
			Integer.toString(fe.getListID()),
			5,
			maxY + 10,
			this);

		this.schemeElements.add(se);
	}

	public void replaceFilterExpression(FilterExpressionInterface fe_old, FilterExpressionInterface fe_new)
	{
		int ind = -1;
		LogicSchemeElementBase lse = null;
		for (ind = this.schemeElements.size() - 1; ind >= 0 ; ind--)
		{
			lse = (LogicSchemeElementBase)this.schemeElements.get(ind);
			if (lse.operandType.equals(Integer.toString(fe_old.getListID())))
//			if (lse.filterExpression.equals(fe_old))
				break;
		}
		if (ind >= 0)
		{
			fe_new.setListID(fe_old.getListID());
			lse.filterExpression = fe_new;
		}
	}

	public void removeFilterExpression(FilterExpressionInterface fe)
	{
		for (int i = 0; i < this.schemeElements.size(); i++)
		{
			LogicSchemeElementBase se = (LogicSchemeElementBase)this.schemeElements.get(i);

			if (se.operandType.equals(Integer.toString(fe.getListID())))
			{
				deleteElement(se);
				break;
			}
		}
	}

	public void clearFilterExpressions()
	{
		this.schemeElements.clear();
		this.finishedLinks.clear();
		this.activeZones.clear();
	}

	public List getFilterExpressions()
	{
		List result = new ArrayList();
		for (ListIterator lIt = this.schemeElements.listIterator(); lIt.hasNext();)
		{
			LogicSchemeElementBase se = (LogicSchemeElementBase)lIt.next();

			if (se.type.equals(LogicSchemeElementBase.tCondition))
				result.add(se.filterExpression);
		}

		return result;
	}

	FilterExpressionInterface createFilterExpression()
	{
		return new FilterExpressionBase();
	}

	public ElementsActiveZoneBase createElementsActiveZone(
			LogicSchemeElementBase owner,
			String zoneType,
			int size,
			int x,
			int y)
	{
		return new ElementsActiveZoneBase(
				owner,
				zoneType,
				size,
				x,
				y);
	}

	public FinishedLinkBase createFinishedLink(ElementsActiveZoneBase from, ElementsActiveZoneBase to)
	{
		return new FinishedLinkBase(from, to);
	}

	public LogicSchemeElementBase createLogicSchemeElement(
			String type,
			FilterExpressionInterface fe,
			String operandType,
			int itsX,
			int itsY,
			LogicSchemeBase ls)
	{
		return new LogicSchemeElementBase(
				type,
				fe,
				operandType,
				itsX,
				itsY,
				ls);
	}

	//Сама фильтрация
	public boolean passesAllConstraints(Object or)
	{
		boolean ok = false;
		if (!checkScheme())
			ok = true;
		else
			ok = passesConstraints(or, this.treeResult);
		return ok;
	}

	private boolean passesConstraints(Object or, LogicSchemeElementBase top)
	{
		if (top.type.equals(LogicSchemeElementBase.tCondition))
			return this.filter.expression(top.filterExpression, or);

		List allTopLinks = top.input.getLinks();

		boolean result = true;

    int index = 0;
		for (ListIterator lIt = allTopLinks.listIterator(); lIt.hasNext(); index++)
		{
			FinishedLinkBase fl = (FinishedLinkBase)lIt.next();

			LogicSchemeElementBase curInputElement = null;
			if (fl.az1.zoneType.equals(ElementsActiveZoneBase.ztOut))
				curInputElement = fl.az1.owner;
			if (fl.az1.zoneType.equals(ElementsActiveZoneBase.ztIn))
				curInputElement = fl.az2.owner;

			if (index == 0)
				result = passesConstraints(or, curInputElement);
			else
			{
				if (top.operandType.equals(LogicSchemeElementBase.otAnd))
					result = result && passesConstraints(or, curInputElement);
				if (top.operandType.equals(LogicSchemeElementBase.otOr))
				result = result || passesConstraints(or, curInputElement);
			}
		}

		return result;
	}

	//В текст и из текста
	public String getLogicString()
	{
		if (checkScheme())
			return getLogicFor(this.treeResult);
		return "N/A";
	}

	private String getLogicFor(LogicSchemeElementBase top)
	{
		if (top.type.equals(LogicSchemeElementBase.tCondition))
			return "\"" + LogicSchemeElementBase.string(LogicSchemeElementBase.tCondition) + " " + top.filterExpression.getListID() + "\"";

		List allTopLinks = top.input.getLinks();

		String result = "";

    int index = 0;
		for (ListIterator lIt = allTopLinks.listIterator(); lIt.hasNext(); index++)
		{
			FinishedLinkBase fl = (FinishedLinkBase)lIt.next();

			LogicSchemeElementBase curInputElement = null;
			if (fl.az1.zoneType.equals(ElementsActiveZoneBase.ztOut))
				curInputElement = fl.az1.owner;
			if (fl.az1.zoneType.equals(ElementsActiveZoneBase.ztIn))
				curInputElement = fl.az2.owner;

			if (index == 0)
				result = getLogicFor(curInputElement);
			else
				result = result + " " + LogicSchemeElementBase.string(top.operandType) + " " + getLogicFor(curInputElement);
		}

		if (top != this.treeResult)
			return "(" + result + ")";

		return result;
	}

	//Блок процедур для воссановления схемы по логическому выражению
	public void restoreSchemeByLogic (String expression, List filterExpressions)
	{
		this.finishedLinks.clear();
		this.activeZones.clear();
		this.schemeElements.clear();

		this.treeResult = createLogicSchemeElement(
				LogicSchemeElementBase.tResult,
				null,
				"",
				0,
				0,
				this);

		this.schemeElements.add(this.treeResult);

		tryToAddLink((getSchemeFromLogic(expression, filterExpressions)).out,
					 this.treeResult.input);

//		setSchemeCoords();
	}

	private LogicSchemeElementBase getSchemeFromLogic(
			String expression,
			List filterExpressions)
	{
		boolean toBreak = false; // Удаляем внешние скобки
		do
		{
			String croppedString = cropBranches(expression);
			toBreak = croppedString.equals(expression);
			expression = croppedString;
		}
		while (!toBreak);

		int conditionNumber = getCondition(expression);
		if (conditionNumber != 0)
		{
			//если в строке только запись типа "Condition N"
			FilterExpressionInterface fe = getFEforListID(filterExpressions, conditionNumber);

			LogicSchemeElementBase se = createLogicSchemeElement(
					LogicSchemeElementBase.tCondition,
					fe,
					Integer.toString(fe.getListID()),
					0, //Задаём координаты (0,0) их будем устанавливать,
					0, //когда дерево построим
					this);

			this.schemeElements.add(se);

			return se;
		}
		String operatorFound = null;
		List positions = new ArrayList();

		findHighestPriorityOperators(expression, operatorFound, positions);

		LogicSchemeElementBase se = createLogicSchemeElement(
				LogicSchemeElementBase.tOperand,
				null,
				operatorFound,
				0,
				0,
				this);

		this.schemeElements.add(se);

		int curIndex = 0; //массив в строке
		int i = 0; //Это индекс в массиве координат операторов данного ранга
		do
		{
			int posI = ((Integer )positions.get(i)).intValue();

			String subStringI = expression.substring(curIndex, posI);

			LogicSchemeElementBase se1 = getSchemeFromLogic(subStringI, filterExpressions);
			this.schemeElements.add(se1);
			tryToAddLink(se1.out, se.input);

			curIndex = posI + operatorFound.length();
/*
			if (operatorFound.equals(LogicSchemeElement_yo.String("label_and")))
				curIndex = posI + 1;
			if (operatorFound.equals(LogicSchemeElement_yo.String("label_or")))
			curIndex = posI + 3;
*/
			i++;
		}
		while (i < positions.size());
		return null;
	}

	private String cropBranches (String expression)
	{
		int len = expression.length();
		if ((expression.charAt(0) == '(')
			 && (expression.charAt(len - 1) == ')'))
		{
			int bracketCount = 0;
			for (int i = 0; i < len - 1; i++)
			{
				if (expression.charAt(i) == '(')
					bracketCount++;
				if (expression.charAt(i) == ')')
					bracketCount--;
				if (bracketCount == 0)
					return expression;
			}
			return expression.substring(1,expression.length() - 1);
		}
		return expression;
	}

	private int getCondition (String expression)
	{
		if (expression.startsWith("\"" + LogicSchemeElementBase.tCondition)
			 && (expression.charAt(expression.length() - 1) == '\"'))
		{
			try
			{
				String numbString = expression.substring(
								 LogicSchemeElementBase.tCondition.length() + 2,
								 expression.length() - 1);

				int condIndex = Integer.parseInt(numbString);
				return condIndex;
			}
			catch (Exception exc)
			{
				return 0;
			}
		}
		return 0;
	}

	private void findHighestPriorityOperators(
			String expression,
			String operatorFound,
			List positions)
	{
		operatorFound = null;

		int bracketCount = 0;
		for (int i = 0; i < expression.length(); i++)
		{
			if (expression.charAt(i) == '(')
				bracketCount++;
			if (expression.charAt(i) == ')')
				bracketCount--;

			if (bracketCount == 0)
			{
				if (expression.substring(i, LogicSchemeElementBase.string(LogicSchemeElementBase.otAnd).length()).equals(LogicSchemeElementBase.string(LogicSchemeElementBase.otAnd)))
				{
					operatorFound = LogicSchemeElementBase.string(LogicSchemeElementBase.otAnd);
					positions.add(new Integer(i));
				}
				if (expression.substring(i, LogicSchemeElementBase.string(LogicSchemeElementBase.otOr).length()).equals(LogicSchemeElementBase.string(LogicSchemeElementBase.otOr)))
				{
					operatorFound = LogicSchemeElementBase.string(LogicSchemeElementBase.otOr);
					positions.add(new Integer(i));
				}
			}
		}
	}

	private FilterExpressionInterface getFEforListID(List filterExpressions, int listID)
	{
		ListIterator iterator = filterExpressions.listIterator();
		while (iterator.hasNext())
		{
			FilterExpressionInterface fe = (FilterExpressionInterface) (iterator.next());
			if (fe.getListID() == listID)
				return fe;
		}
		return null;
	}

	protected int findDistanceFromResult(LogicSchemeElementBase se)
	{
		if (se.type.equals(LogicSchemeElementBase.tResult))
			return 0;

		for (int i = 0; i < this.finishedLinks.size(); i++)
		{
			FinishedLinkBase fl = (FinishedLinkBase)this.finishedLinks.get(i);

			if ((fl.az1.owner == se) && (fl.az1.zoneType.equals(ElementsActiveZoneBase.ztOut)))
				return findDistanceFromResult(fl.az2.owner) + 1;

			if ((fl.az2.owner == se) && (fl.az2.zoneType.equals(ElementsActiveZoneBase.ztOut)))
				return findDistanceFromResult(fl.az1.owner) + 1;
		}
		return 0;
	}

	//выполнение Serializable
	public void writeObject(ObjectOutputStream out) throws IOException
	{
		List filterExpressions = new ArrayList();
		for (ListIterator lIt = this.schemeElements.listIterator(); lIt.hasNext();)
		{
			LogicSchemeElementBase se = (LogicSchemeElementBase)lIt.next();
			if (se.type.equals(LogicSchemeElementBase.tCondition))
				filterExpressions.add(se.filterExpression);
		}
		out.writeInt(filterExpressions.size());
		for (ListIterator lIt = filterExpressions.listIterator(); lIt.hasNext();)
		{
			FilterExpressionInterface filterExpression = (FilterExpressionInterface )lIt.next();
			filterExpression.writeObject(out);
		}
//		out.writeObject(filterExpressions); // Сохранили expressionы из этой схемы

		String generalExpression = getLogicString();
		out.writeObject(generalExpression);
	}

	public void readObject(ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		List filterExpressions = new ArrayList();

		int count = in.readInt();
		for (int i = 0; i < count; i++)
		{
			FilterExpressionInterface filterExpression = createFilterExpression();
			filterExpression.readObject(in);
			filterExpressions.add(filterExpression);
		}
		String generalExpression = (String )in.readObject();

		restoreSchemeByLogic(generalExpression, filterExpressions);
	}
}
