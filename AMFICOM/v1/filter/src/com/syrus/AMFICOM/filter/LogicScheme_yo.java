/*
 * $Id: LogicScheme_yo.java,v 1.3 2004/06/08 15:44:01 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.filter;

import java.io.*;
import java.util.*;

/**
 * @version $Revision: 1.3 $, $Date: 2004/06/08 15:44:01 $
 * @module filter_v1
 */
public class LogicScheme_yo implements Serializable
{
	public Vector schemeElements = new Vector();
	public Vector finishedLinks = new Vector();
	public Vector activeZones = new Vector();

	protected LogicSchemeElement_yo treeResult = null;

	Filter filter;

	public int schemeWidth = 0;
	public int schemeHeight = 0;

	public LogicScheme_yo(Filter filter)
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
			LogicSchemeElement_yo se = (LogicSchemeElement_yo )schemeElements.get(i);

			if (se.type.equals(LogicSchemeElement_yo.t_condition))
			{
				schemeEls.add(se);
				se.out.getLinks().clear();
				activeZones.add(se.out);
			}
		}
		schemeElements = schemeEls;

		treeResult = createLogicSchemeElement(
				LogicSchemeElement_yo.t_result,
				null,
				"",
				300,
				50,
				this);

		schemeElements.add(treeResult);

		LogicSchemeElement_yo andElem = createLogicSchemeElement(
				LogicSchemeElement_yo.t_operand,
				null,
				LogicSchemeElement_yo.ot_and,
				150,
				50,
				this);

		schemeElements.add(andElem);

		for (int i = 0; i < schemeElements.size() - 2; i++)
		{
			LogicSchemeElement_yo se = (LogicSchemeElement_yo )schemeElements.get(i);
			tryToAddLink(se.out, andElem.input);
		}
		tryToAddLink(andElem.out, treeResult.input);

		for (int i = 0; i < schemeElements.size(); i++)
		{
			LogicSchemeElement_yo se = (LogicSchemeElement_yo )schemeElements.get(i);
			if (se.type.equals(LogicSchemeElement_yo.t_condition))
			{
				se.x = 5;
				se.y = 5 + (LogicSchemeElement_yo.height + 3) * i;
			}
		}
	}

	//Определяем какому объекту принадлежит точка
	public ProSchemeElement_yo identifyObject (int x, int y)
	{
		for (int i = 0; i < activeZones.size(); i++)
		{
			ElementsActiveZone_yo az = (ElementsActiveZone_yo )activeZones.get(i);
			if ((az.owner.x + az.x < x) && (x < az.owner.x + az.x + az.size) &&
				(az.owner.y + az.y < y) && (y < az.owner.y + az.y + az.size))
				return az;
		}

		for (int i = 0; i < finishedLinks.size(); i++)
		{
			FinishedLink_yo fl = (FinishedLink_yo )finishedLinks.get(i);
			if (fl.tryToSelect(x,y))
				return fl;
		}

		for (int i = 0; i < schemeElements.size(); i++)
		{
			LogicSchemeElement_yo se = (LogicSchemeElement_yo )schemeElements.get(i);
			if ((se.x < x) && (x < se.x + LogicSchemeElement_yo.width) &&
				(se.y < y) && (y < se.y + LogicSchemeElement_yo.height))
				return se;
		}

		return null;
	}

	//Добавление связи между эл-тами
	public void tryToAddLink (ElementsActiveZone_yo from, ElementsActiveZone_yo to)
	{
		if (from.zoneType.equals(to.zoneType))
			return;

		if (from.owner.type.equals(LogicSchemeElement_yo.t_result) &&
				from.getLinks().size() == 1)
			return;

		if (to.owner.type.equals(LogicSchemeElement_yo.t_result) &&
				to.getLinks().size() == 1)
			return;

		for (int i = 0; i < finishedLinks.size(); i++)
		{
			FinishedLink_yo fl = (FinishedLink_yo )finishedLinks.get(i);
			if (fl.az1.equals(from) && fl.az2.equals(to) ||
				fl.az2.equals(from) && fl.az1.equals(to))
			return;
		}

		FinishedLink_yo fl = createFinishedLink(from, to);
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
			LogicSchemeElement_yo se = (LogicSchemeElement_yo )schemeElements.get(i);
			if (se.type.equals(LogicSchemeElement_yo.t_condition))
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
			ElementsActiveZone_yo curActiveZone = (ElementsActiveZone_yo )activeZones.get(i);
			if (curActiveZone.getLinks().size() == 0)
				return false;
		}
		return true;
	}

	//Добавление/удаление элементов
	public void deleteElement(ProSchemeElement_yo element)
	{
		if (element == null)
			return;

		if (element.getTyp().equals(LogicSchemeElement_yo.TYP))
		{
			LogicSchemeElement_yo se = (LogicSchemeElement_yo )element;

			for (int i = 0; i < finishedLinks.size(); i++)
			{
				FinishedLink_yo fl = (FinishedLink_yo )finishedLinks.get(i);

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

		if (element.getTyp().equals(FinishedLink_yo.TYP))
		{
			FinishedLink_yo fl = (FinishedLink_yo )element;
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
			LogicSchemeElement_yo lse = (LogicSchemeElement_yo )schemeElements.get(i);
			if (lse.y > maxY)
				maxY = lse.y;
		}

		LogicSchemeElement_yo se = createLogicSchemeElement(
			LogicSchemeElement_yo.t_condition,
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
		LogicSchemeElement_yo lse = null;
		for (ind = schemeElements.size() - 1; ind >= 0 ; ind--)
		{
			lse = (LogicSchemeElement_yo )schemeElements.get(ind);
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
			LogicSchemeElement_yo se = (LogicSchemeElement_yo )schemeElements.get(i);

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
			LogicSchemeElement_yo se = (LogicSchemeElement_yo )schemeElements.get(i);

			if (se.type.equals(LogicSchemeElement_yo.t_condition))
				result.add(se.filterExpression);
		}

		return result;
	}

	FilterExpressionInterface createFilterExpression()
	{
		return new FilterExpression_yo();
	}

	public ElementsActiveZone_yo createElementsActiveZone(
			LogicSchemeElement_yo owner,
			String zoneType,
			int size,
			int x,
			int y)
	{
		return new ElementsActiveZone_yo(
				owner,
				zoneType,
				size,
				x,
				y);
	}

	public FinishedLink_yo createFinishedLink(ElementsActiveZone_yo from, ElementsActiveZone_yo to)
	{
		return new FinishedLink_yo(from, to);
	}

	public LogicSchemeElement_yo createLogicSchemeElement(
			String type,
			FilterExpressionInterface fe,
			String operandType,
			int itsX,
			int itsY,
			LogicScheme_yo ls)
	{
		return new LogicSchemeElement_yo(
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

	private boolean passesConstraints(Object or, LogicSchemeElement_yo top)
	{
		if (top.type.equals(LogicSchemeElement_yo.t_condition))
			return filter.expression(top.filterExpression, or);

		LinkedList allTopLinks = top.input.getLinks();

		boolean result = true;
		for (int i = 0; i < allTopLinks.size(); i++)
		{
			FinishedLink_yo fl = (FinishedLink_yo )allTopLinks.get(i);

			LogicSchemeElement_yo curInputElement = null;
			if (fl.az1.zoneType.equals(ElementsActiveZone_yo.zt_out))
				curInputElement = fl.az1.owner;
			if (fl.az1.zoneType.equals(ElementsActiveZone_yo.zt_in))
				curInputElement = fl.az2.owner;

			if (i == 0)
				result = passesConstraints(or, curInputElement);
			else
			{
				if (top.operandType.equals(LogicSchemeElement_yo.ot_and))
					result = result && passesConstraints(or, curInputElement);
				if (top.operandType.equals(LogicSchemeElement_yo.ot_or))
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

	private String getLogicFor(LogicSchemeElement_yo top)
	{
		if (top.type.equals(LogicSchemeElement_yo.t_condition))
			return "\"" + LogicSchemeElement_yo.string(LogicSchemeElement_yo.t_condition) + " " + top.filterExpression.getListID() + "\"";

		LinkedList allTopLinks = top.input.getLinks();

		String result = "";

		for (int i = 0; i < allTopLinks.size(); i++)
		{
			FinishedLink_yo fl = (FinishedLink_yo )allTopLinks.get(i);

			LogicSchemeElement_yo curInputElement = null;
			if (fl.az1.zoneType.equals(ElementsActiveZone_yo.zt_out))
				curInputElement = fl.az1.owner;
			if (fl.az1.zoneType.equals(ElementsActiveZone_yo.zt_in))
				curInputElement = fl.az2.owner;

			if (i == 0)
				result = getLogicFor(curInputElement);
			else
				result = result + " " + LogicSchemeElement_yo.string(top.operandType) + " " + getLogicFor(curInputElement);
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
				LogicSchemeElement_yo.t_result,
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

	private LogicSchemeElement_yo getSchemeFromLogic(
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

			LogicSchemeElement_yo se = createLogicSchemeElement(
					LogicSchemeElement_yo.t_condition,
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

			LogicSchemeElement_yo se = createLogicSchemeElement(
					LogicSchemeElement_yo.t_operand,
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

				LogicSchemeElement_yo se1 = getSchemeFromLogic(subStringI, filterExpressions);
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
		if (expression.substring(0,8).equals("\"" + LogicSchemeElement_yo.string(LogicSchemeElement_yo.t_condition)) &&
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
				if (expression.substring(i, LogicSchemeElement_yo.string(LogicSchemeElement_yo.ot_and).length()).equals(LogicSchemeElement_yo.string(LogicSchemeElement_yo.ot_and)))
				{
					operatorFound = LogicSchemeElement_yo.string(LogicSchemeElement_yo.ot_and);
					positions.add(new Integer(i));
				}
				if (expression.substring(i, LogicSchemeElement_yo.string(LogicSchemeElement_yo.ot_or).length()).equals(LogicSchemeElement_yo.string(LogicSchemeElement_yo.ot_or)))
				{
					operatorFound = LogicSchemeElement_yo.string(LogicSchemeElement_yo.ot_or);
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

	protected int findDistanceFromResult(LogicSchemeElement_yo se)
	{
		if (se.type.equals(LogicSchemeElement_yo.t_result))
			return 0;

		for (int i = 0; i < finishedLinks.size(); i++)
		{
			FinishedLink_yo fl = (FinishedLink_yo )finishedLinks.get(i);

			if ((fl.az1.owner == se) && (fl.az1.zoneType.equals(ElementsActiveZone_yo.zt_out)))
				return findDistanceFromResult(fl.az2.owner) + 1;

			if ((fl.az2.owner == se) && (fl.az2.zoneType.equals(ElementsActiveZone_yo.zt_out)))
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
			LogicSchemeElement_yo se = (LogicSchemeElement_yo )schemeElements.get(i);
			if (se.type.equals(LogicSchemeElement_yo.t_condition))
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
