/*
 * $Id: LogicSchemeBase.java,v 1.1 2004/06/17 10:23:05 krupenn Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.filter;

import java.io.*;
import java.util.*;

/**
 * @version $Revision: 1.1 $, $Date: 2004/06/17 10:23:05 $
 * @module filter_v1
 */
public class LogicSchemeBase implements Serializable
{
	public Vector schemeElements = new Vector();
	public Vector finishedLinks = new Vector();
	public Vector activeZones = new Vector();

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
		finishedLinks.removeAllElements();
		activeZones.removeAllElements();

		Vector schemeEls = new Vector();
		for (int i = 0; i < schemeElements.size(); i++)
		{
			LogicSchemeElementBase se = (LogicSchemeElementBase)schemeElements.get(i);

			if (se.type.equals(LogicSchemeElementBase.t_condition))
			{
				schemeEls.add(se);
				se.out.getLinks().clear();
				activeZones.add(se.out);
			}
		}
		schemeElements = schemeEls;

		treeResult = createLogicSchemeElement(
				LogicSchemeElementBase.t_result,
				null,
				"",
				300,
				50,
				this);

		schemeElements.add(treeResult);

		LogicSchemeElementBase andElem = createLogicSchemeElement(
				LogicSchemeElementBase.t_operand,
				null,
				LogicSchemeElementBase.ot_and,
				150,
				50,
				this);

		schemeElements.add(andElem);

		for (int i = 0; i < schemeElements.size() - 2; i++)
		{
			LogicSchemeElementBase se = (LogicSchemeElementBase)schemeElements.get(i);
			tryToAddLink(se.out, andElem.input);
		}
		tryToAddLink(andElem.out, treeResult.input);

		for (int i = 0; i < schemeElements.size(); i++)
		{
			LogicSchemeElementBase se = (LogicSchemeElementBase)schemeElements.get(i);
			if (se.type.equals(LogicSchemeElementBase.t_condition))
			{
				se.x = 5;
				se.y = 5 + (LogicSchemeElementBase.height + 3) * i;
			}
		}
	}

	//Определяем какому объекту принадлежит точка
	public ProSchemeElementBase identifyObject (int x, int y)
	{
		for (int i = 0; i < activeZones.size(); i++)
		{
			ElementsActiveZoneBase az = (ElementsActiveZoneBase)activeZones.get(i);
			if ((az.owner.x + az.x < x) && (x < az.owner.x + az.x + az.size) &&
				(az.owner.y + az.y < y) && (y < az.owner.y + az.y + az.size))
				return az;
		}

		for (int i = 0; i < finishedLinks.size(); i++)
		{
			FinishedLinkBase fl = (FinishedLinkBase)finishedLinks.get(i);
			if (fl.tryToSelect(x,y))
				return fl;
		}

		for (int i = 0; i < schemeElements.size(); i++)
		{
			LogicSchemeElementBase se = (LogicSchemeElementBase)schemeElements.get(i);
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

		if (from.owner.type.equals(LogicSchemeElementBase.t_result) &&
				from.getLinks().size() == 1)
			return;

		if (to.owner.type.equals(LogicSchemeElementBase.t_result) &&
				to.getLinks().size() == 1)
			return;

		for (int i = 0; i < finishedLinks.size(); i++)
		{
			FinishedLinkBase fl = (FinishedLinkBase)finishedLinks.get(i);
			if (fl.az1.equals(from) && fl.az2.equals(to) ||
				fl.az2.equals(from) && fl.az1.equals(to))
			return;
		}

		FinishedLinkBase fl = createFinishedLink(from, to);
		finishedLinks.add (fl);
		from.addLink(fl);
		to.addLink(fl);
	}

	//Информация о схеме
	public int getRestrictionsNumber()
	{
		int result = 0;

		for (int i = 0; i < schemeElements.size(); i++)
		{
			LogicSchemeElementBase se = (LogicSchemeElementBase)schemeElements.get(i);
			if (se.type.equals(LogicSchemeElementBase.t_condition))
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

		for (int i = 0; i < activeZones.size(); i++)
		{
			ElementsActiveZoneBase curActiveZone = (ElementsActiveZoneBase)activeZones.get(i);
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

			for (int i = 0; i < finishedLinks.size(); i++)
			{
				FinishedLinkBase fl = (FinishedLinkBase)finishedLinks.get(i);

				if (se.input != null)
					if (fl.az1.equals(se.input) || fl.az2.equals(se.input))
						if (finishedLinks.remove(fl))
						{
							fl.az1.removeLink(fl);
							fl.az2.removeLink(fl);
							i--;
						}

				if (se.out != null)
					if (fl.az1.equals(se.out) || fl.az2.equals(se.out))
						if (finishedLinks.remove(fl))
						{
							fl.az1.removeLink(fl);
							fl.az2.removeLink(fl);
							i--;
						}
			}

			if (se.input != null)
				activeZones.remove(se.input);

			if (se.out != null)
				activeZones.remove(se.out);

			schemeElements.remove(se);
		}

		if (element.getTyp().equals(FinishedLinkBase.TYP))
		{
			FinishedLinkBase fl = (FinishedLinkBase)element;
			fl.az1.removeLink(fl);
			fl.az2.removeLink(fl);
			finishedLinks.remove(fl);
		}
	}

	public void addFilterExpression(FilterExpressionInterface fe)
	{
		int maxY = 0;
		for (int i = 0; i < schemeElements.size(); i++)
		{
			LogicSchemeElementBase lse = (LogicSchemeElementBase)schemeElements.get(i);
			if (lse.y > maxY)
				maxY = lse.y;
		}

		LogicSchemeElementBase se = createLogicSchemeElement(
			LogicSchemeElementBase.t_condition,
			fe,
			Integer.toString(fe.getListID()),
			5,
			maxY + 10,
			this);

		schemeElements.add(se);
	}

	public void replaceFilterExpression(FilterExpressionInterface fe_old, FilterExpressionInterface fe_new)
	{
		int ind = -1;
		LogicSchemeElementBase lse = null;
		for (ind = schemeElements.size() - 1; ind >= 0 ; ind--)
		{
			lse = (LogicSchemeElementBase)schemeElements.get(ind);
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
		for (int i = 0; i < schemeElements.size(); i++)
		{
			LogicSchemeElementBase se = (LogicSchemeElementBase)schemeElements.get(i);

			if (se.operandType.equals(Integer.toString(fe.getListID())))
			{
				deleteElement(se);
				break;
			}
		}
	}

	public void clearFilterExpressions()
	{
		schemeElements.clear();
		finishedLinks.clear();
		activeZones.clear();
	}

	public Vector getFilterExpressions()
	{
		Vector result = new Vector();
		for (int i = 0; i < schemeElements.size(); i++)
		{
			LogicSchemeElementBase se = (LogicSchemeElementBase)schemeElements.get(i);

			if (se.type.equals(LogicSchemeElementBase.t_condition))
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
			ok = passesConstraints(or, treeResult);
		return ok;
	}

	private boolean passesConstraints(Object or, LogicSchemeElementBase top)
	{
		if (top.type.equals(LogicSchemeElementBase.t_condition))
			return filter.expression(top.filterExpression, or);

		LinkedList allTopLinks = top.input.getLinks();

		boolean result = true;
		for (int i = 0; i < allTopLinks.size(); i++)
		{
			FinishedLinkBase fl = (FinishedLinkBase)allTopLinks.get(i);

			LogicSchemeElementBase curInputElement = null;
			if (fl.az1.zoneType.equals(ElementsActiveZoneBase.zt_out))
				curInputElement = fl.az1.owner;
			if (fl.az1.zoneType.equals(ElementsActiveZoneBase.zt_in))
				curInputElement = fl.az2.owner;

			if (i == 0)
				result = passesConstraints(or, curInputElement);
			else
			{
				if (top.operandType.equals(LogicSchemeElementBase.ot_and))
					result = result && passesConstraints(or, curInputElement);
				if (top.operandType.equals(LogicSchemeElementBase.ot_or))
				result = result || passesConstraints(or, curInputElement);
			}
		}

		return result;
	}

	//В текст и из текста
	public String getLogicString()
	{
		if (checkScheme())
			return getLogicFor(treeResult);
		return "N/A";
	}

	private String getLogicFor(LogicSchemeElementBase top)
	{
		if (top.type.equals(LogicSchemeElementBase.t_condition))
			return "\"" + LogicSchemeElementBase.string(LogicSchemeElementBase.t_condition) + " " + top.filterExpression.getListID() + "\"";

		LinkedList allTopLinks = top.input.getLinks();

		String result = "";

		for (int i = 0; i < allTopLinks.size(); i++)
		{
			FinishedLinkBase fl = (FinishedLinkBase)allTopLinks.get(i);

			LogicSchemeElementBase curInputElement = null;
			if (fl.az1.zoneType.equals(ElementsActiveZoneBase.zt_out))
				curInputElement = fl.az1.owner;
			if (fl.az1.zoneType.equals(ElementsActiveZoneBase.zt_in))
				curInputElement = fl.az2.owner;

			if (i == 0)
				result = getLogicFor(curInputElement);
			else
				result = result + " " + LogicSchemeElementBase.string(top.operandType) + " " + getLogicFor(curInputElement);
		}

		if (top != treeResult)
			return "(" + result + ")";

		return result;
	}

	//Блок процедур для воссановления схемы по логическому выражению
	public void restoreSchemeByLogic (String expression, List filterExpressions)
	{
		finishedLinks.removeAllElements();
		activeZones.removeAllElements();
		schemeElements.removeAllElements();

		treeResult = createLogicSchemeElement(
				LogicSchemeElementBase.t_result,
				null,
				"",
				0,
				0,
				this);

		schemeElements.add(treeResult);

		tryToAddLink((getSchemeFromLogic(expression, filterExpressions)).out,
					 treeResult.input);

//		setSchemeCoords();
	}

	private LogicSchemeElementBase getSchemeFromLogic(
			String expression,
			List filterExpressions)
	{
		boolean toBreak = false; // Удаляем внешние скобки
		do
			toBreak = cropBranches(expression);
		while (!toBreak);

		int conditionNumber = getCondition(expression);
		if (conditionNumber != 0)
		{
			//если в строке только запись типа "Condition N"
			FilterExpressionInterface fe = getFEforListID(filterExpressions, conditionNumber);

			LogicSchemeElementBase se = createLogicSchemeElement(
					LogicSchemeElementBase.t_condition,
					fe,
					Integer.toString(fe.getListID()),
					0, //Задаём координаты (0,0) их будем устанавливать,
					0, //когда дерево построим
					this);

			return se;
		}
		else
		{
			String operatorFound = null;
			Vector positions = new Vector();

			findHighestPriorityOperators(expression, operatorFound, positions);

			LogicSchemeElementBase se = createLogicSchemeElement(
					LogicSchemeElementBase.t_operand,
					null,
					operatorFound,
					0,
					0,
					this);

			schemeElements.add(se);

			int curIndex = 0; //массив в строке
			int i = 0; //Это индекс в массиве координат операторов данного ранга
			do
			{
				int posI = ((Integer )positions.get(i)).intValue();

				String subStringI = expression.substring(curIndex, posI);

				LogicSchemeElementBase se1 = getSchemeFromLogic(subStringI, filterExpressions);
				schemeElements.add(se1);
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
		}
		return null;
	}

	private boolean cropBranches (String expression)
	{
		if ((expression.charAt(0) == '(') && (expression.charAt(0) == ')'))
		{
			int bracketCount = 0;
			for (int i = 0; i < expression.length() - 1; i++)
			{
				if (expression.charAt(i) == '(')
					bracketCount++;
				if (expression.charAt(i) == ')')
					bracketCount--;
				if (bracketCount == 0)
					return false;
			}
			return true;
		}
		return false;
	}

	private int getCondition (String expression)
	{
		if (expression.substring(0,8).equals("\"" + LogicSchemeElementBase.string(LogicSchemeElementBase.t_condition)) &&
			(expression.charAt(expression.length()) == '\"'))
		{
			int condIndex = Integer.parseInt(
				expression.substring(8, expression.length()));
			return condIndex;
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
				if (expression.substring(i, LogicSchemeElementBase.string(LogicSchemeElementBase.ot_and).length()).equals(LogicSchemeElementBase.string(LogicSchemeElementBase.ot_and)))
				{
					operatorFound = LogicSchemeElementBase.string(LogicSchemeElementBase.ot_and);
					positions.add(new Integer(i));
				}
				if (expression.substring(i, LogicSchemeElementBase.string(LogicSchemeElementBase.ot_or).length()).equals(LogicSchemeElementBase.string(LogicSchemeElementBase.ot_or)))
				{
					operatorFound = LogicSchemeElementBase.string(LogicSchemeElementBase.ot_or);
					positions.add(new Integer(i));
				}
			}
		}
	}

	private FilterExpressionInterface getFEforListID(List filterExpressions, int listID)
	{
		Iterator iterator = filterExpressions.iterator();
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
		if (se.type.equals(LogicSchemeElementBase.t_result))
			return 0;

		for (int i = 0; i < finishedLinks.size(); i++)
		{
			FinishedLinkBase fl = (FinishedLinkBase)finishedLinks.get(i);

			if ((fl.az1.owner == se) && (fl.az1.zoneType.equals(ElementsActiveZoneBase.zt_out)))
				return findDistanceFromResult(fl.az2.owner) + 1;

			if ((fl.az2.owner == se) && (fl.az2.zoneType.equals(ElementsActiveZoneBase.zt_out)))
				return findDistanceFromResult(fl.az1.owner) + 1;
		}
		return 0;
	}

	//выполнение Serializable
	public void writeObject(ObjectOutputStream out) throws IOException
	{
		Vector filterExpressions = new Vector();
		for (int i = 0; i < schemeElements.size(); i++)
		{
			LogicSchemeElementBase se = (LogicSchemeElementBase)schemeElements.get(i);
			if (se.type.equals(LogicSchemeElementBase.t_condition))
				filterExpressions.add(se.filterExpression);
		}
		out.writeInt(filterExpressions.size());
		for (int i = 0; i < filterExpressions.size(); i++)
		{
			FilterExpressionInterface filterExpression = (FilterExpressionInterface )filterExpressions.get(i);
			filterExpression.writeObject(out);
		}
//		out.writeObject(filterExpressions); // Сохранили expressionы из этой схемы

		String general_expression = getLogicString();
		out.writeObject(general_expression);
	}

	public void readObject(ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		Vector filterExpressions = new Vector();

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
