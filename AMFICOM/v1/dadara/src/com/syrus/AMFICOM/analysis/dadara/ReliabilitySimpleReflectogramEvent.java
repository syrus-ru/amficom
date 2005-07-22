/*-
 * $Id: ReliabilitySimpleReflectogramEvent.java,v 1.3 2005/07/22 06:39:51 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis.dadara;

/**
 * Дополняет SimpleReflectogramEvent параметром достоверности.
 * Как правило, событием становятся только те, которые уже
 * превысили порог значимости. Для таких событий нужен параметр,
 * указывающий достоверность этого превышения порога значимости.
 * @author $Author: saa $
 * @version $Revision: 1.3 $, $Date: 2005/07/22 06:39:51 $
 * @module
 */
public interface ReliabilitySimpleReflectogramEvent
extends SimpleReflectogramEvent {
	/**
	 * порог достоверности события
	 */
	double RELIABLE = 0.99;

	/**
	 * 
	 * @return определен ли параметр достоверности для данного события.
	 * Значение true означает, что можно вызывать метод {@link #getReliability}
	 */
	boolean hasReliability();
	/**
	 * Величина от 0 до 1, определяющая значение достоверности события
	 * в единицах, похожих на вероятность. Определена только если
	 * hasReliability() возвращает true.
	 * <p>
	 * Типичные значения:
	 * <ul>
	 * <li>0.0: событие на самом пороге принятия
	 * <li>от RELIABLE до 1.0: принятие события вполне достоверно 
	 * @return значение от 0 до 1
	 * @throws IllegalArgumentException достоверность не определена
	 */
	double getReliability();
}
