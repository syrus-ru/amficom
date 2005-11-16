/*
 * $Id: ReportLayout.java,v 1.9 2005/11/16 18:53:17 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.report;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.report.AbstractDataStorableElement;
import com.syrus.AMFICOM.report.AttachedTextStorableElement;
import com.syrus.AMFICOM.report.ImageStorableElement;
import com.syrus.AMFICOM.report.ReportTemplate;
import com.syrus.AMFICOM.report.StorableElement;
import com.syrus.AMFICOM.report.TextAttachingType;

/**
 * Класс проводит раскладку визульных компонентов с реализованными
 * элементами отчёта так, чтобы расстояния между компонентами были
 * равны расстоянию между компонентами на схеме шаблона отчёта.
 * @author $Author: max $
 * @version $Revision: 1.9 $, $Date: 2005/11/16 18:53:17 $
 * @module reportclient
 */
public class ReportLayout {
	private List<RenderingComponent> componentContainer = null;
	private ReportTemplate reportTemplate = null;
	
	private final Map<RenderingComponent,Boolean> componentsSetUp = new HashMap<RenderingComponent,Boolean>();
	
	public void dolayout (List<RenderingComponent> container,ReportTemplate template) throws ApplicationException
	{
		this.componentContainer = container;
		this.reportTemplate = template;		
		
		//Инициализируем таблицу "законченности" - в ней указывается,
		//установлен элемент отображения на своё место или ещё нет.
		for (RenderingComponent component : this.componentContainer)
			this.componentsSetUp.put(component,Boolean.FALSE);
			
		//Получаем упорядоченные списки значений X и Y для начал и концов
		//элементов отображения
		List<Integer> xs = new ArrayList<Integer>();
		List<Integer> ys = new ArrayList<Integer>();
		this.getAxisValuesMatrices(xs,ys);
		
		//Пока не будет завершена раскладка
		while (!this.layoutIsFinished()){
			//пытаемся "уложить" очередной элемент
			for (RenderingComponent component : this.componentContainer){
				if (this.componentsSetUp.get(component).equals(Boolean.FALSE))
					try {
						int newY = -1;
						if (component instanceof AttachedTextComponent) {
							//Привязанные надписи рисуем после того, как отрисовываем
							//объекты, к которым они привязаны
							AttachedTextStorableElement element =
								(AttachedTextStorableElement)component.getElement();
							TextAttachingType vertAttachingType = element.getVerticalAttachType();
							
							if (!vertAttachingType.equals(TextAttachingType.TO_FIELDS_TOP)) {
								RenderingComponent vertAttacherComponent =
									this.getComponentForElement(element.getVerticalAttacher());
								if (this.componentsSetUp.get(vertAttacherComponent).equals(Boolean.FALSE))
									continue;
								if (vertAttachingType.equals(TextAttachingType.TO_TOP))
									newY = vertAttacherComponent.getY() + element.getDistanceY();
								else
									newY = vertAttacherComponent.getY()
										+ vertAttacherComponent.getHeight() + element.getDistanceY();
							}
							else
								newY = this.checkToTopForElements(component,xs,ys);
						}
						else
							newY = this.checkToTopForElements(component,xs,ys);
						
						component.setY(newY);
						this.componentsSetUp.put(component,Boolean.TRUE);
					} catch (NonImplementedElementFoundException e) {
					}
			}
		}
	}

	/**
	 * Выбрасывается при вычислении Y координаты для элемента отображения, 
	 * в том случае, когда элементы, находящиеся выше на схеме, ещё не
	 * реализованы.
	 * @author $Author: max $
	 * @version $Revision: 1.9 $, $Date: 2005/11/16 18:53:17 $
	 * @module reportclient
	 */
	private class NonImplementedElementFoundException extends Exception
	{
		private static final long serialVersionUID = 3269694826007469180L;
	}
	
	/**
	 * Возвращает значение (y + height + dist) для ближайшего к elem элемента сверху?
	 * где dist - расстояние по схеме от elem до этого объекта
	 * 0, в случае если объектов выше нет
	 * и -1, если выше elem есть нераспечатанные элементы
	 * @param component рассматриваемый элемент отображения
	 * @param xs вектор значений x и x + width для всех объектов
	 * @param ys вектор значений y и y + height для всех объектов
	 * @return значение y для реализации элемента шаблона
	 * @throws ApplicationException 
	 */
	private int checkToTopForElements(
			RenderingComponent component,
			List<Integer> xs,
			List<Integer> ys) throws NonImplementedElementFoundException, ApplicationException{
		StorableElement element  = component.getElement();
		int elemMinX = element.getX();
		int elemMaxX = element.getX() + element.getWidth();
		int elementMinY = element.getY();
		if (element instanceof AbstractDataStorableElement) {
			//Смотрим: принадлежит ли точка кластеру - области, в которую
			//вписаны элемент отображения данных и прявязанные к нему надписи
			AbstractDataStorableElement dsElement =	(AbstractDataStorableElement) element;
			Rectangle bounds = this.reportTemplate.getElementClasterBounds(dsElement);
			elementMinY = bounds.y;
		}
		
		/**
		 * Значение Y для нижнего края элемента, ближайшего сверху к указанному
		 */
		int theLowestEdgeValue = -1;
		/**
		 * Элемент отображения, ближайший сверху к указанному
		 */
		RenderingComponent theLowestComponent = null; 
		
		//Проводим поиск в заданных точках (в центрах "сетки", задаваемой
		//xs,ys)
		for (int i = 0; i < xs.size() - 1; i++) {
			/**
			 * Значение абциссы центра очередной ячейки
			 */
			int cellMiddleX = (xs.get(i).intValue() + xs.get(i + 1).intValue()) / 2;

			if (!((elemMinX <= cellMiddleX) &&	(cellMiddleX <= elemMaxX)))
				//Если центр находится не над элементом переходим к следующему
				//"столбцу" ячеек сетки 				
				continue;

			//Спускаемся "вниз"
			for (int j = 0; j < ys.size() - 1; j++) {
				/**
				 * Значение ординаты центра очередной ячейки
				 */
				int cellMiddleY = (ys.get(j).intValue() + ys.get(j + 1).intValue()) / 2;
				if (cellMiddleY >= elementMinY)
					//Если спустились вниз до самого искомого элемента
					break;

				//Ищем в данной ячейке какой-либо элемент отображения
				RenderingComponent componentFound = this.getComponentAtPoint(cellMiddleX, cellMiddleY);
				
				if (componentFound == null)
					//Не нашли - движемся дальше вниз
					continue;
				
				//Нашли
				if (this.componentsSetUp.get(componentFound).equals(Boolean.FALSE))
					//Если выше данного объекта находится НЕОБРАБОТАННЫЙ
					//элемент отображения
					throw new NonImplementedElementFoundException();

				/**
				 * Координата Y нижнего края найденного компонента
				 */
				int componentFoundBottomY = 0;

				if (	(componentFound instanceof DataRenderingComponent)
					&& !(componentFound instanceof ImageRenderingComponent))
				{
					Rectangle compBounds = this.getDataComponentsClasterBounds(
							(DataRenderingComponent) componentFound);
					componentFoundBottomY = (int)compBounds.getMaxY();
				}
				else
					componentFoundBottomY = componentFound.getY() + componentFound.getHeight();
				
				if (componentFoundBottomY > theLowestEdgeValue) {
					theLowestEdgeValue = componentFoundBottomY;					
					theLowestComponent = componentFound;
				}
			}
		}

		//Если выше не нашли ни одного объекта
		if (theLowestEdgeValue == -1)
			return elementMinY;

		StorableElement lowestElement = theLowestComponent.getElement();
		int yDistanceAtScheme = component.getElement().getY()
			- (lowestElement.getY() + lowestElement.getHeight());
		
		return theLowestEdgeValue + yDistanceAtScheme;
	}
	
	/**
	 * Возвращает сортированные списки xs и ys начал и концов
	 * элементов отображений по x,y.
	 * @param xs вектор значений x и x + width для всех объектов
	 * @param ys вектор значений y и y + height для всех объектов
	 * @throws ApplicationException 
	 */
	private void getAxisValuesMatrices(
			List<Integer> xs,
			List<Integer> ys) throws ApplicationException {
		//Для хранения выходных данных используются списки, поскольку мы не
		//знаем заранее какие надписи привязаны, а какие нет (мы отдельно
		//учитываем только координаты непривязанных надписей) - не знаем сколько
		//выделять ячеек в массиве.
		for (RenderingComponent component : this.componentContainer) {
			StorableElement element = component.getElement();
			
			if (element instanceof AttachedTextStorableElement) {
				//Мы фиксируем координаты только непривязанных к объектам надписей
				AttachedTextStorableElement atElement =
					(AttachedTextStorableElement) element;
				
				if (	!atElement.getVerticalAttachType().equals(TextAttachingType.TO_FIELDS_TOP)
					||	!atElement.getHorizontalAttachType().equals(TextAttachingType.TO_FIELDS_LEFT))
					continue;
				
				xs.add(new Integer(element.getX()));
				ys.add(new Integer(element.getY()));

				xs.add(new Integer(element.getX() + element.getWidth()));
				ys.add(new Integer(element.getY() + element.getHeight()));
			}
			else if (element instanceof AbstractDataStorableElement) {
				Rectangle borders = this.reportTemplate.getElementClasterBounds(
						(AbstractDataStorableElement)element);
				
				xs.add(new Integer(borders.x));
				ys.add(new Integer(borders.y));

				xs.add(new Integer(borders.x + borders.width));
				ys.add(new Integer(borders.y + borders.height));
			}
			else if (element instanceof ImageStorableElement) {
				xs.add(new Integer(element.getX()));
				ys.add(new Integer(element.getY()));

				xs.add(new Integer(element.getX() + element.getWidth()));
				ys.add(new Integer(element.getY() + element.getHeight()));
			}
		}
		Collections.sort(xs);
		Collections.sort(ys);			
	}

	/**
	 * Осуществляется поиск элемента шаблона в заданной точке.
	 * @param x значение по x рассматриваемой точки
	 * @param y значение по y рассматриваемой точки
	 * @return возвращает 
	 *  -1, если в этой точке нет элемента шаблона;
	 *  -2, если есть, но он ещё не расположен в соответствующеем ему месте.
	 * @throws ApplicationException 
	 */
	private RenderingComponent getComponentAtPoint(int x,int y) throws ApplicationException {
		for (RenderingComponent component : this.componentContainer) {
			StorableElement element = component.getElement();
			if (element instanceof AttachedTextStorableElement)	{
				//Привязанные надписи не считаются
				AttachedTextStorableElement atElement =
					(AttachedTextStorableElement) element;
				
				if (	!atElement.getVerticalAttachType().equals(TextAttachingType.TO_FIELDS_TOP)
					||	!atElement.getHorizontalAttachType().equals(TextAttachingType.TO_FIELDS_LEFT))
					continue;
				
				if (element.hasPoint(x,y))
					return component;
			}
			else if (element instanceof AbstractDataStorableElement) {
				//Смотрим: принадлежит ли точка кластеру - области, в которую
				//вписаны элемент отображения данных и прявязанные к нему надписи
				AbstractDataStorableElement dsElement =	(AbstractDataStorableElement) element;
				if (this.reportTemplate.clasterContainsPoint(dsElement,x,y))
					return component;
			}
			else if (element instanceof ImageStorableElement) {
				if (element.hasPoint(x,y))
					return component;
			}
		}
		return null;
	}
	
	/**
	 * Возвращает границы кластера для уже реализованного элемента шаблона
	 * с надписями.
	 * @param component
	 * @return Габариты кластера
	 * @throws ApplicationException 
	 */
	private Rectangle getDataComponentsClasterBounds(DataRenderingComponent component) throws ApplicationException {
		Rectangle bounds = this.reportTemplate.getElementClasterBounds(
				(AbstractDataStorableElement)component.getElement());
		
		bounds.x = component.getX();
		bounds.y = component.getY();
		bounds.height += component.getHeight() - component.getElement().getHeight();
		return bounds;
	}
	
	/**
	 * @return true, если раскладка компонентов завершена.
	 */
	private boolean layoutIsFinished(){
		for (Boolean value : this.componentsSetUp.values())
			if (value.equals(Boolean.FALSE))
				return false;
		
		return true;
	}
	
	private RenderingComponent getComponentForElement(StorableElement element) {
		for (RenderingComponent component : this.componentContainer) {
			if (component.getElement().equals(element))
				return component;
		}
		return null;
	}
}
