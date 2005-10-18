/*-
 * $Id: EvaluationPerEventResult.java,v 1.3 2005/10/18 13:14:10 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis.dadara;

/**
 * Описывает общие (для всй р/г) результаты сравнения с эталоном.
 * Везде предполагается нумерация событий при сравнении с эталоном,
 * соответствующая сравниваемой р/г.
 * <p>
 * FIXME: возможно, нумерациу событий по р/г надо переделать на нумерацию по эталону
 * @author $Author: saa $
 * @version $Revision: 1.3 $, $Date: 2005/10/18 13:14:10 $
 * @module
 */
public interface EvaluationPerEventResult {
	/**
	 * Возвращает число событий результата анализа
	 * @return число событий результата анализа
	 */
	int getNEvents();

	/**
	 * Возвращает true, если Q- и K-параметры для данного события доступны.
	 * @param i номер данного события
	 * @return true, если Q- и K-параметры для данного события доступны.
	 * @throws IndexOutOfBoundsException, если
	 *   <tt> i &lt; 0 || i &gt;= {@link #getNEvents()} </tt>  
	 */
	boolean hasQK(int i);

	/**
	 * Объясняет причину, по которой Q- и K-параметры для данного
	 * события недоступны.
	 * Возвращает true, если причиной является изменение на рефлектограмме,
	 * false, если к данному событию Q и K не определены по другой причине
	 * (например, Q и K для этих событий не определимо).
	 * @param i номер данного события
	 * @throws IllegalStateException, если Q и K для данного события
	 * доступны, т.е.
	 * <tt>{@link #hasQK}(i) != false</tt>
	 * @return true, если причиной является изменение на рефлектограмме,
	 * false, если к данному событию Q и K не определены по другой причине
	 * (например, Q и K для этих событий не определимо).
	 */
	boolean isModified(int i);

	/**
	 * Возвращает Q-параметр для данного события.
	 * Значение Q-параметра лежит в диапазоне [0,1].
	 * <p>
	 * <ul>
	 * <li>Q=1 - соответствие 100%, полное соответствие
	 * <li>Q=0 - соответствие 0%, полное несоответствие,
	 * отличие равно либо превышает предельное значение
	 * </ul>
	 * @param i номер данного события
	 * @return Q-параметр для данного события.
	 * @throws IndexOutOfBoundsException, если
	 *   <tt> i &lt; 0 || i &gt;= {@link #getNEvents()} </tt>
	 * @throws IllegalStateException, если <tt>{@link #hasQK}(i) == false</tt>
	 */
	double getQ(int i);

	/**
	 * Возвращает K-параметр для данного события.
	 * Значение K-параметра лежит в диапазоне [0,1].
	 * <p>
	 * <ul>
	 * <li>K=0 - изменение 0%,
	 * <li>K=1 - изменение 100%,
	 * </ul>
	 * @param i номер данного события
	 * @return K-параметр для данного события.
	 * @throws IndexOutOfBoundsException, если
	 *   <tt> i &lt; 0 || i &gt;= {@link #getNEvents()} </tt>  
	 * @throws IllegalStateException, если <tt>{@link #hasQK}(i) == false</tt>
	 */
	double getK(int i);
}
