/*
 * $Id: LogicSchemeBase.java,v 1.9 2005/08/09 19:41:15 arseniy Exp $
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
 * @version $Revision: 1.9 $, $Date: 2005/08/09 19:41:15 $
 * @module filter
 */
public class LogicSchemeBase implements Serializable {
	private static final long serialVersionUID = 3258132453318537783L;

	public List<LogicSchemeElementBase> schemeElements = new ArrayList<LogicSchemeElementBase>();
	public List<FinishedLinkBase> finishedLinks = new ArrayList<FinishedLinkBase>();
	public List<ElementsActiveZoneBase> activeZones = new ArrayList<ElementsActiveZoneBase>();

	public Filter filter;

	public int schemeWidth = 0;
	public int schemeHeight = 0;

	protected LogicSchemeElementBase treeResult = null;

	public LogicSchemeBase(final Filter filter) {
		this.filter = filter;
		// restoreSchemeByText();
	}

	// Генерация схемы по умолчанию - все условия через "И" подсоединены к
	// результату
	public void organizeStandartScheme() {
		this.finishedLinks.clear();
		this.activeZones.clear();

		final List<LogicSchemeElementBase> schemeEls = new ArrayList<LogicSchemeElementBase>();
		for (final LogicSchemeElementBase se : this.schemeElements) {
			if (se.type.equals(LogicSchemeElementBase.tCondition)) {
				schemeEls.add(se);
				se.out.getLinks().clear();
				this.activeZones.add(se.out);
			}
		}
		this.schemeElements = schemeEls;

		this.treeResult = createLogicSchemeElement(LogicSchemeElementBase.tResult, null, "", 300, 50, this);

		this.schemeElements.add(this.treeResult);

		final LogicSchemeElementBase andElem = createLogicSchemeElement(LogicSchemeElementBase.tOperand,
				null,
				LogicSchemeElementBase.otAnd,
				150,
				50,
				this);

		this.schemeElements.add(andElem);

		for (int i = 0; i < this.schemeElements.size() - 2; i++) {
			LogicSchemeElementBase se = this.schemeElements.get(i);
			this.tryToAddLink(se.out, andElem.input);
		}
		this.tryToAddLink(andElem.out, this.treeResult.input);

		for (int i = 0; i < this.schemeElements.size(); i++) {
			LogicSchemeElementBase se = this.schemeElements.get(i);
			if (se.type.equals(LogicSchemeElementBase.tCondition)) {
				se.x = 5;
				se.y = 5 + (LogicSchemeElementBase.height + 3) * i;
			}
		}
	}

	// Определяем какому объекту принадлежит точка
	public ProSchemeElementBase identifyObject(final int x, final int y) {
		for (int i = 0; i < this.activeZones.size(); i++) {
			final ElementsActiveZoneBase az = this.activeZones.get(i);
			if ((az.owner.x + az.x < x)
					&& (x < az.owner.x + az.x + az.size)
					&& (az.owner.y + az.y < y)
					&& (y < az.owner.y + az.y + az.size)) {
				return az;
			}
		}

		for (int i = 0; i < this.finishedLinks.size(); i++) {
			final FinishedLinkBase fl = this.finishedLinks.get(i);
			if (fl.tryToSelect(x, y)) {
				return fl;
			}
		}

		for (int i = 0; i < this.schemeElements.size(); i++) {
			final LogicSchemeElementBase se = this.schemeElements.get(i);
			if ((se.x < x) && (x < se.x + LogicSchemeElementBase.width) && (se.y < y) && (y < se.y + LogicSchemeElementBase.height)) {
				return se;
			}
		}

		return null;
	}

	//Добавление связи между эл-тами
	public void tryToAddLink(final ElementsActiveZoneBase from, final ElementsActiveZoneBase to) {
		if (from.zoneType.equals(to.zoneType)) {
			return;
		}

		if (from.owner.type.equals(LogicSchemeElementBase.tResult) && from.getLinks().size() == 1) {
			return;
		}

		if (to.owner.type.equals(LogicSchemeElementBase.tResult) && to.getLinks().size() == 1) {
			return;
		}

		for (int i = 0; i < this.finishedLinks.size(); i++) {
			final FinishedLinkBase fl = this.finishedLinks.get(i);
			if (fl.az1.equals(from) && fl.az2.equals(to) || fl.az2.equals(from) && fl.az1.equals(to)) {
				return;
			}
		}

		final FinishedLinkBase fl = createFinishedLink(from, to);
		this.finishedLinks.add(fl);
		from.addLink(fl);
		to.addLink(fl);
	}

	// Информация о схеме
	public int getRestrictionsNumber() {
		int result = 0;

		for (int i = 0; i < this.schemeElements.size(); i++) {
			final LogicSchemeElementBase se = this.schemeElements.get(i);
			if (se.type.equals(LogicSchemeElementBase.tCondition)) {
				result++;
			}
		}

		return result;
	}

	public boolean schemeIsEmpty() {
		return (this.getFilterExpressions().size() == 0);
	}

	public boolean checkScheme() {
		if (this.getFilterExpressions().size() < this.getRestrictionsNumber()) {
			return false;
		}

		if (this.schemeIsEmpty()) {
			return false;
		}

		for (int i = 0; i < this.activeZones.size(); i++) {
			final ElementsActiveZoneBase curActiveZone = this.activeZones.get(i);
			if (curActiveZone.getLinks().size() == 0) {
				return false;
			}
		}
		return true;
	}

	//Добавление/удаление элементов
	public void deleteElement(final ProSchemeElementBase element) {
		if (element == null) {
			return;
		}

		if (element.getTyp().equals(LogicSchemeElementBase.TYP)) {
			final LogicSchemeElementBase se = (LogicSchemeElementBase) element;

			for (int i = 0; i < this.finishedLinks.size(); i++) {
				final FinishedLinkBase fl = this.finishedLinks.get(i);

				if (se.input != null) {
					if (fl.az1.equals(se.input) || fl.az2.equals(se.input))
						if (this.finishedLinks.remove(fl)) {
							fl.az1.removeLink(fl);
							fl.az2.removeLink(fl);
							i--;
						}
				}

				if (se.out != null) {
					if (fl.az1.equals(se.out) || fl.az2.equals(se.out))
						if (this.finishedLinks.remove(fl)) {
							fl.az1.removeLink(fl);
							fl.az2.removeLink(fl);
							i--;
						}
				}
			}

			if (se.input != null) {
				this.activeZones.remove(se.input);
			}

			if (se.out != null) {
				this.activeZones.remove(se.out);
			}

			this.schemeElements.remove(se);
		}

		if (element.getTyp().equals(FinishedLinkBase.TYP)) {
			final FinishedLinkBase fl = (FinishedLinkBase) element;
			fl.az1.removeLink(fl);
			fl.az2.removeLink(fl);
			this.finishedLinks.remove(fl);
		}
	}

	public void addFilterExpression(final FilterExpressionInterface fe) {
		int maxY = 0;
		for (int i = 0; i < this.schemeElements.size(); i++) {
			final LogicSchemeElementBase lse = this.schemeElements.get(i);
			if (lse.y > maxY) {
				maxY = lse.y;
			}
		}

		final LogicSchemeElementBase se = this.createLogicSchemeElement(LogicSchemeElementBase.tCondition,
				fe,
				Integer.toString(fe.getListID()),
				5,
				maxY + 10,
				this);

		this.schemeElements.add(se);
	}

	public void replaceFilterExpression(final FilterExpressionInterface fe_old, final FilterExpressionInterface fe_new) {
		int ind = -1;
		LogicSchemeElementBase lse = null;
		for (ind = this.schemeElements.size() - 1; ind >= 0; ind--) {
			lse = this.schemeElements.get(ind);
			if (lse.operandType.equals(Integer.toString(fe_old.getListID()))) {
				// if (lse.filterExpression.equals(fe_old))
				break;
			}
		}
		if (ind >= 0) {
			fe_new.setListID(fe_old.getListID());
			lse.filterExpression = fe_new;
		}
	}

	public void removeFilterExpression(final FilterExpressionInterface fe) {
		for (int i = 0; i < this.schemeElements.size(); i++) {
			LogicSchemeElementBase se = this.schemeElements.get(i);

			if (se.operandType.equals(Integer.toString(fe.getListID()))) {
				this.deleteElement(se);
				break;
			}
		}
	}

	public void clearFilterExpressions() {
		this.schemeElements.clear();
		this.finishedLinks.clear();
		this.activeZones.clear();
	}

	public List<FilterExpressionInterface> getFilterExpressions() {
		final List<FilterExpressionInterface> result = new ArrayList<FilterExpressionInterface>();
		for (final LogicSchemeElementBase se : this.schemeElements) {
			if (se.type.equals(LogicSchemeElementBase.tCondition)) {
				result.add(se.filterExpression);
			}
		}

		return result;
	}

	FilterExpressionInterface createFilterExpression() {
		return new FilterExpressionBase();
	}

	public ElementsActiveZoneBase createElementsActiveZone(final LogicSchemeElementBase owner,
			final String zoneType,
			final int size,
			final int x,
			final int y) {
		return new ElementsActiveZoneBase(owner, zoneType, size, x, y);
	}

	public FinishedLinkBase createFinishedLink(final ElementsActiveZoneBase from, final ElementsActiveZoneBase to) {
		return new FinishedLinkBase(from, to);
	}

	public LogicSchemeElementBase createLogicSchemeElement(final String type,
			final FilterExpressionInterface fe,
			final String operandType,
			final int itsX,
			final int itsY,
			final LogicSchemeBase ls) {
		return new LogicSchemeElementBase(type, fe, operandType, itsX, itsY, ls);
	}

	// Сама фильтрация
	public boolean passesAllConstraints(final Object or) {
		boolean ok = false;
		if (!this.checkScheme()) {
			ok = true;
		}
		else {
			ok = this.passesConstraints(or, this.treeResult);
		}
		return ok;
	}

	private boolean passesConstraints(final Object or, final LogicSchemeElementBase top) {
		if (top.type.equals(LogicSchemeElementBase.tCondition)) {
			return this.filter.expression(top.filterExpression, or);
		}

		final List<FinishedLinkBase> allTopLinks = top.input.getLinks();

		boolean result = true;

		int index = 0;
		for (final ListIterator<FinishedLinkBase> lIt = allTopLinks.listIterator(); lIt.hasNext(); index++) {
			final FinishedLinkBase fl = lIt.next();

			LogicSchemeElementBase curInputElement = null;
			if (fl.az1.zoneType.equals(ElementsActiveZoneBase.ztOut)) {
				curInputElement = fl.az1.owner;
			}
			if (fl.az1.zoneType.equals(ElementsActiveZoneBase.ztIn)) {
				curInputElement = fl.az2.owner;
			}

			if (index == 0) {
				result = passesConstraints(or, curInputElement);
			}
			else {
				if (top.operandType.equals(LogicSchemeElementBase.otAnd)) {
					result = result && passesConstraints(or, curInputElement);
				}
				if (top.operandType.equals(LogicSchemeElementBase.otOr)) {
					result = result || passesConstraints(or, curInputElement);
				}
			}
		}

		return result;
	}

	// В текст и из текста
	public String getLogicString() {
		if (this.checkScheme()) {
			return this.getLogicFor(this.treeResult);
		}
		return "N/A";
	}

	private String getLogicFor(final LogicSchemeElementBase top) {
		if (top.type.equals(LogicSchemeElementBase.tCondition)) {
			return "\"" + LogicSchemeElementBase.string(LogicSchemeElementBase.tCondition) + " "
					+ top.filterExpression.getListID() + "\"";
		}

		final List<FinishedLinkBase> allTopLinks = top.input.getLinks();

		String result = "";

		int index = 0;
		for (final ListIterator<FinishedLinkBase> lIt = allTopLinks.listIterator(); lIt.hasNext(); index++) {
			final FinishedLinkBase fl = lIt.next();

			LogicSchemeElementBase curInputElement = null;
			if (fl.az1.zoneType.equals(ElementsActiveZoneBase.ztOut)) {
				curInputElement = fl.az1.owner;
			}
			if (fl.az1.zoneType.equals(ElementsActiveZoneBase.ztIn)) {
				curInputElement = fl.az2.owner;
			}

			if (index == 0) {
				result = this.getLogicFor(curInputElement);
			} else {
				result = result + " " + LogicSchemeElementBase.string(top.operandType) + " " + this.getLogicFor(curInputElement);
			}
		}

		if (top != this.treeResult) {
			return "(" + result + ")";
		}

		return result;
	}

	//Блок процедур для воссановления схемы по логическому выражению
	public void restoreSchemeByLogic(final String expression, final List<FilterExpressionInterface> filterExpressions) {
		this.finishedLinks.clear();
		this.activeZones.clear();
		this.schemeElements.clear();

		this.treeResult = createLogicSchemeElement(LogicSchemeElementBase.tResult, null, "", 0, 0, this);

		this.schemeElements.add(this.treeResult);

		this.tryToAddLink((this.getSchemeFromLogic(expression, filterExpressions)).out, this.treeResult.input);

		// setSchemeCoords();
	}

	private LogicSchemeElementBase getSchemeFromLogic(String expression, final List<FilterExpressionInterface> filterExpressions) {
		boolean toBreak = false; // Удаляем внешние скобки
		do {
			final String croppedString = cropBranches(expression);
			toBreak = croppedString.equals(expression);
			expression = croppedString;
		} while (!toBreak);

		int conditionNumber = this.getCondition(expression);
		if (conditionNumber != 0) {
			// если в строке только запись типа "Condition N"
			final FilterExpressionInterface fe = this.getFEforListID(filterExpressions, conditionNumber);

			final LogicSchemeElementBase se = createLogicSchemeElement(LogicSchemeElementBase.tCondition,
					fe,
					Integer.toString(fe.getListID()),
					0, // Задаём координаты (0,0) их будем устанавливать,
					0, // когда дерево построим
					this);

			this.schemeElements.add(se);

			return se;
		}
		String operatorFound = null;
		final List<Integer> positions = new ArrayList<Integer>();

		this.findHighestPriorityOperators(expression, operatorFound, positions);

		final LogicSchemeElementBase se = this.createLogicSchemeElement(LogicSchemeElementBase.tOperand,
				null,
				operatorFound,
				0,
				0,
				this);

		this.schemeElements.add(se);

		int curIndex = 0; // массив в строке
		int i = 0; // Это индекс в массиве координат операторов данного ранга
		do {
			final int posI = positions.get(i).intValue();

			final String subStringI = expression.substring(curIndex, posI);

			final LogicSchemeElementBase se1 = this.getSchemeFromLogic(subStringI, filterExpressions);
			this.schemeElements.add(se1);
			this.tryToAddLink(se1.out, se.input);

			curIndex = posI + operatorFound.length();
			/*
			 * if (operatorFound.equals(LogicSchemeElement_yo.String("label_and")))
			 * curIndex = posI + 1; if
			 * (operatorFound.equals(LogicSchemeElement_yo.String("label_or")))
			 * curIndex = posI + 3;
			 */
			i++;
		} while (i < positions.size());
		return null;
	}

	private String cropBranches(final String expression) {
		final int len = expression.length();
		if ((expression.charAt(0) == '(') && (expression.charAt(len - 1) == ')')) {
			int bracketCount = 0;
			for (int i = 0; i < len - 1; i++) {
				if (expression.charAt(i) == '(') {
					bracketCount++;
				}
				if (expression.charAt(i) == ')') {
					bracketCount--;
				}
				if (bracketCount == 0) {
					return expression;
				}
			}
			return expression.substring(1, expression.length() - 1);
		}
		return expression;
	}

	private int getCondition(final String expression) {
		if (expression.startsWith("\"" + LogicSchemeElementBase.tCondition) && (expression.charAt(expression.length() - 1) == '\"')) {
			try {
				final String numbString = expression.substring(LogicSchemeElementBase.tCondition.length() + 2, expression.length() - 1);

				final int condIndex = Integer.parseInt(numbString);
				return condIndex;
			} catch (Exception exc) {
				return 0;
			}
		}
		return 0;
	}

	private void findHighestPriorityOperators(final String expression, String operatorFound, final List<Integer> positions) {
		operatorFound = null;

		int bracketCount = 0;
		for (int i = 0; i < expression.length(); i++) {
			if (expression.charAt(i) == '(') {
				bracketCount++;
			}
			if (expression.charAt(i) == ')') {
				bracketCount--;
			}

			if (bracketCount == 0) {
				if (expression.substring(i, LogicSchemeElementBase.string(LogicSchemeElementBase.otAnd).length()).equals(LogicSchemeElementBase.string(LogicSchemeElementBase.otAnd))) {
					operatorFound = LogicSchemeElementBase.string(LogicSchemeElementBase.otAnd);
					positions.add(new Integer(i));
				}
				if (expression.substring(i, LogicSchemeElementBase.string(LogicSchemeElementBase.otOr).length()).equals(LogicSchemeElementBase.string(LogicSchemeElementBase.otOr))) {
					operatorFound = LogicSchemeElementBase.string(LogicSchemeElementBase.otOr);
					positions.add(new Integer(i));
				}
			}
		}
	}

	private FilterExpressionInterface getFEforListID(final List<FilterExpressionInterface> filterExpressions, final int listID) {
		for (final FilterExpressionInterface fe : filterExpressions) {
			if (fe.getListID() == listID) {
				return fe;
			}
		}
		return null;
	}

	protected int findDistanceFromResult(final LogicSchemeElementBase se) {
		if (se.type.equals(LogicSchemeElementBase.tResult)) {
			return 0;
		}

		for (int i = 0; i < this.finishedLinks.size(); i++) {
			final FinishedLinkBase fl = this.finishedLinks.get(i);

			if ((fl.az1.owner == se) && (fl.az1.zoneType.equals(ElementsActiveZoneBase.ztOut))) {
				return findDistanceFromResult(fl.az2.owner) + 1;
			}

			if ((fl.az2.owner == se) && (fl.az2.zoneType.equals(ElementsActiveZoneBase.ztOut))) {
				return findDistanceFromResult(fl.az1.owner) + 1;
			}
		}
		return 0;
	}

	// выполнение Serializable
	public void writeObject(final ObjectOutputStream out) throws IOException {
		final List<FilterExpressionInterface> filterExpressions = new ArrayList<FilterExpressionInterface>();
		for (final LogicSchemeElementBase se : this.schemeElements) {
			if (se.type.equals(LogicSchemeElementBase.tCondition)) {
				filterExpressions.add(se.filterExpression);
			}
		}
		out.writeInt(filterExpressions.size());
		for (final FilterExpressionInterface filterExpression : filterExpressions) {
			filterExpression.writeObject(out);
		}
		// out.writeObject(filterExpressions); // Сохранили expressionы из этой
		// схемы

		final String generalExpression = this.getLogicString();
		out.writeObject(generalExpression);
	}

	public void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
		final List<FilterExpressionInterface> filterExpressions = new ArrayList<FilterExpressionInterface>();

		final int count = in.readInt();
		for (int i = 0; i < count; i++) {
			final FilterExpressionInterface filterExpression = this.createFilterExpression();
			filterExpression.readObject(in);
			filterExpressions.add(filterExpression);
		}
		final String generalExpression = (String) in.readObject();

		restoreSchemeByLogic(generalExpression, filterExpressions);
	}
}
