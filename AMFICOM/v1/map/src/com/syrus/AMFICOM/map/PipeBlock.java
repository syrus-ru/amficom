/*-
 * $Id: PipeBlock.java,v 1.12 2005/12/02 11:24:13 bass Exp $
 *
 * Copyright ї 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_VOID_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_BADLY_INITIALIZED;
import static com.syrus.AMFICOM.general.Identifier.XmlConversionMode.MODE_RETURN_VOID_IF_ABSENT;
import static com.syrus.AMFICOM.general.ObjectEntities.PIPEBLOCK_CODE;
import static java.util.logging.Level.SEVERE;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.LocalXmlIdentifierPool;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.XmlBeansTransferable;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.AMFICOM.map.corba.IdlPipeBlock;
import com.syrus.AMFICOM.map.corba.IdlPipeBlockHelper;
import com.syrus.AMFICOM.map.xml.XmlPipeBlock;
import com.syrus.AMFICOM.resource.IntDimension;
import com.syrus.AMFICOM.resource.IntPoint;
import com.syrus.util.Log;

/**
 * Объект привязки кабелей к тоннелю. Принадлежит определенному тоннелю.
 * включает всебя список кабелей, которые проходят по данному тоннелю,
 * и матрицу пролегания кабелей по трубам тоннеля.
 *
 * @author $Author: bass $
 * @version $Revision: 1.12 $, $Date: 2005/12/02 11:24:13 $
 * @module map
 */
public final class PipeBlock 
		extends StorableObject<PipeBlock>
		implements XmlBeansTransferable<XmlPipeBlock>,
		Comparable<PipeBlock> {
	private static final long serialVersionUID = -6089210980096232608L;

	private int number;
	
	/** размерность тоннеля (матрица труб) */
	private IntDimension dimension;

	/** порядок нумерации труб сверху вниз. */
	private boolean topToBottom = true;

	/** порядок нумерации слева направо. */
	private boolean leftToRight = true;

	/** порядок нумерации сначала по горизонтали, затем по вертикали. */
	private boolean horizontalVertical = true;

	/** карта привязки кабелей к трубам */
	private transient List<Object>[][] bindingMap;

	private transient boolean transientFieldsInitialized = false;

	private void initialize() {
		if(!this.transientFieldsInitialized) {
			this.bindingMap = new List[this.dimension.getWidth()][this.dimension.getHeight()];
			for (int i = 0; i < this.bindingMap.length; i++) {
				for (int j = 0; j < this.bindingMap[i].length; j++) {
					this.bindingMap[i][j] = new LinkedList<Object>();
				}
			}
			this.transientFieldsInitialized = true;
		}		
	}

	public PipeBlock(final IdlPipeBlock pltb) throws CreateObjectException {
		try {
			this.fromTransferable(pltb);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	protected PipeBlock(
			final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final int number,
			final IntDimension bindingDimension,
			final boolean leftToRight,
			final boolean topToBottom,
			final boolean horizontalVertical) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version);
		this.number = number;
		this.leftToRight = leftToRight;
		this.topToBottom = topToBottom;
		this.horizontalVertical = horizontalVertical;
		this.dimension = (bindingDimension == null)
				? new IntDimension(0, 0)
						: bindingDimension;
	}

	public static PipeBlock createInstance(
			final Identifier creatorId,
			final int number,
			final int dimensionX,
			final int dimensionY,
			final boolean leftToRight,
			final boolean topToBottom,
			final boolean horizontalVertical) throws CreateObjectException {

		assert creatorId != null : NON_NULL_EXPECTED;
		
		try {
			final PipeBlock pipeBlock = new PipeBlock(
					IdentifierPool.getGeneratedIdentifier(PIPEBLOCK_CODE),
					creatorId,
					StorableObjectVersion.INITIAL_VERSION,
					number,
					new IntDimension(dimensionX, dimensionY),
					leftToRight,
					topToBottom,
					horizontalVertical);

			assert pipeBlock.isValid() : OBJECT_BADLY_INITIALIZED;

			pipeBlock.markAsChanged();

			return pipeBlock;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	@Override
	protected synchronized void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		final IdlPipeBlock pltb = (IdlPipeBlock) transferable;
		super.fromTransferable(pltb);

		this.number = pltb.number;
		this.dimension = new IntDimension(pltb.dimensionX, pltb.dimensionY);
		this.leftToRight = pltb.leftToRight;
		this.topToBottom = pltb.topToBottom;
		this.horizontalVertical = pltb.horizontalVertical;

		this.transientFieldsInitialized = false;
	}

	@Override
	public IdlPipeBlock getTransferable(final ORB orb) {
		
		int dimensionX = this.dimension.getWidth();
		int dimensionY = this.dimension.getHeight();
		
		return IdlPipeBlockHelper.init(
				orb,
				this.id.getTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getTransferable(),
				this.modifierId.getTransferable(),
				this.version.longValue(),
				this.number,
				dimensionX,
				dimensionY,
				this.leftToRight,
				this.topToBottom,
				this.horizontalVertical);
	}

	synchronized void setAttributes(
			final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final int number,
			final int dimensionX,
			final int dimensionY,
			final boolean leftToRight,
			final boolean topToBottom,
			final boolean horizontalVertical) {
		super.setAttributes(created,
				modified,
				creatorId,
				modifierId,
				version);
		
		this.number = number;
		this.setDimension(new IntDimension(dimensionX, dimensionY));
		this.leftToRight = leftToRight;
		this.topToBottom = topToBottom;
		this.horizontalVertical = horizontalVertical;
	}

	/**
	 * Удалить кабель из тоннеля.
	 * @param object кабель (com.syrus.AMFICOM.scheme.corba.SchemeCableLink)
	 */
	public void remove(final Object object) {
		this.initialize();
		if (object != null) {
			unbind(object);
		}
	}

	/**
	 * Удалить все кабели из тоннеля.
	 */
	public void clear() {
		this.initialize();

		for (int i = 0; i < this.bindingMap.length; i++) {
			for (int j = 0; j < this.bindingMap[i].length; j++) {
				this.bindingMap[i][j] = new LinkedList<Object>();
			}
		}
	}
	
	/**
	 * Получить размерность матрицы прокладки кабелей по трубам тоннеля.
	 * @return размерность
	 */
	public IntDimension getDimension() {
		return this.dimension;
	}
	
	/**
	 * установить размерность матрицы прокладки кабелей по трубам тоннеля.
	 * @param dimension рамерность
	 */
	public void setDimension(final IntDimension dimension) {
		this.dimension.setSize(dimension);
		this.initialize();

		// создается новая матрица прокладки
		final List<Object>[][] bindingMap2 = new List[dimension.getWidth()][dimension.getHeight()];

		for (int i = 0; i < bindingMap2.length; i++) {
			for (int j = 0; j < bindingMap2[i].length; j++) {
				bindingMap2[i][j] = new LinkedList<Object>();
			}
		}
		
		// копируются привязки из старой матрицы
		if (this.bindingMap != null) {
			final int mini = Math.min(this.bindingMap.length, bindingMap2.length);

			for (int i = 0; i < mini; i++) {
				final int minj = Math.min(this.bindingMap[i].length, bindingMap2[i].length);
				for (int j = 0; j < minj; j++) {
					bindingMap2[i][j].addAll(this.bindingMap[i][j]);
				}
			}
		}
		this.bindingMap = bindingMap2;
	}
	
	/**
	 * Получить порядковый номер в тоннеле. Высчитывается по координатам в
	 * соответствии с порядком нумерации и направлением нумерации по горизонтали и
	 * вертикали ({@link #horizontalVertical}, {@link #leftToRight},
	 * {@link #topToBottom})
	 * 
	 * @param ii
	 *        координата по горизонтали
	 * @param jj
	 *        координата по вертикали
	 * @return порядковый номер сквозной нумерации
	 */
	public int getSequenceNumber(final int ii, final int jj) {
		final int sequenceNumber = -1;
		final int m = getDimension().getWidth();
		final int n = getDimension().getHeight();
		final int counter = 1;
		final int limit = n * m;

		final int istart = this.isLeftToRight() ? 0 : m - 1;
		final int jstart = this.isTopToBottom() ? 0 : n - 1;

		final int iend = m - 1 - istart;
		final int jend = n - 1 - jstart;

		int iincrement = this.isLeftToRight() ? 1 : -1;
		int jincrement = this.isTopToBottom() ? 1 : -1;

		int i = istart;
		int j = jstart;

		while (true) {
			if (i == ii && j == jj)
				break;

			if (counter > limit)
				break;

			if (this.isHorizontalVertical()) {
				if (i == iend) {
					i = istart;
					j += jincrement;
				} else
					i += iincrement;
			} else {
				if (j == jend) {
					j = jstart;
					i += iincrement;
				} else
					j += jincrement;
			}
		}
		return sequenceNumber;
	}
	
	/**
	 * Указать прокладку кабеля по трубе.
	 * 
	 * @param object
	 *        кабель
	 * @param i
	 *        координата по горизонтали
	 * @param j
	 *        координата по вертикали
	 */
	public void bind(final Object object, final int i, final int j) {
		this.initialize();
		this.unbind(object);
		this.bindingMap[i][j].add(object);
	}
	
	/**
	 * Убрать привязку кабеля к конкретной трубе в тоннеле.
	 * @param object кабель
	 */
	public void unbind(final Object object) {
		final IntPoint binding = getBinding(object);
		if (binding != null)
			this.bindingMap[binding.x][binding.y].remove(object);
	}

	/**
	 * Получить список кабелей, проходящих по трубе.
	 * @param i координата по горизонтали
	 * @param j координата по вертикали
	 * @return список привязанных по заданной координате кабелей
	 */
	public List getBound(final int i, final int j) {
		this.initialize();
		return this.bindingMap[i][j];
	}

	/**
	 * Проверить, определено ли место прохождения кабеля в тоннеле.
	 * 
	 * @param object
	 *        кабель
	 * @return <code>true</code>, если место кабеля определено,
	 *         <code>false</code> иначе
	 */
	public boolean isBound(final Object object) {
		this.initialize();
		for (int i = 0; i < this.bindingMap.length; i++) {
			for (int j = 0; j < this.bindingMap[i].length; j++) {
				if (this.bindingMap[i][j].contains(object)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Gолучить координаты трубы, по которой проходит кабель.
	 * 
	 * @param object
	 *        кабель
	 * @return координаты прохождения кабеля, или <code>null</code>, если место
	 *         кабеля не задано
	 */
	public IntPoint getBinding(final Object object) {
		this.initialize();
		for (int i = 0; i < this.bindingMap.length; i++) {
			for (int j = 0; j < this.bindingMap[i].length; j++) {
				if (this.bindingMap[i][j].contains(object)) {
					return new IntPoint(i, j);
				}
			}
		}
		return null;
	}

	/**
	 * Поменять направление нумерации по вертикали.
	 */
	public void flipTopToBottom() {
		this.topToBottom = !this.topToBottom;
	}

	/**
	 * Получить направление нумерации по вертикали.
	 * 
	 * @return <code>true</code> при нумерации сверху вниз, иначе
	 *         <code>false</code>
	 */
	public boolean isTopToBottom() {
		return this.topToBottom;
	}

	/**
	 * Поменять направление нумерации по горизонтали.
	 */
	public void flipLeftToRight() {
		this.leftToRight = !this.leftToRight;
	}


	/**
	 * Получить направление нумерации по горизонтали.
	 * 
	 * @return <code>true</code> при нумерации слева направо, иначе
	 *         <code>false</code>
	 */
	public boolean isLeftToRight() {
		return this.leftToRight;
	}

	/**
	 * Поменять порядок нумерации.
	 */
	public void flipHorizontalVertical() {
		this.horizontalVertical = !this.horizontalVertical;
	}

	/**
	 * Получить порядок нумерации.
	 * @return <code>true</code> при нумерации сначала по горизонтали,
	 * потом по вертикали, иначе <code>false</code>
	 */
	public boolean isHorizontalVertical() {
		return this.horizontalVertical;
	}

	public void setLeftToRight(boolean leftToRight) {
		this.leftToRight = leftToRight;
	}

	public void setHorizontalVertical(boolean horizontalVertical) {
		this.horizontalVertical = horizontalVertical;
	}

	public void setTopToBottom(boolean topToBottom) {
		this.topToBottom = topToBottom;
	}

	@Override
	public Set<Identifiable> getDependencies() {
		assert this.isValid() : OBJECT_BADLY_INITIALIZED;
		return Collections.emptySet();
	}

	public void getXmlTransferable(
			XmlPipeBlock xmlPipeBlock, 
			String importType, 
			boolean usePool) throws ApplicationException {
		this.id.getXmlTransferable(xmlPipeBlock.addNewId(), importType);
		xmlPipeBlock.setNumber(this.number);
		xmlPipeBlock.setDimensionX(this.dimension.getWidth());
		xmlPipeBlock.setDimensionY(this.dimension.getHeight());
		xmlPipeBlock.setHorVert(this.horizontalVertical);
		xmlPipeBlock.setLeftToRight(this.leftToRight);
		xmlPipeBlock.setTopToBottom(this.topToBottom);
	}

	private PipeBlock(
			final XmlIdentifier id,
			final String importType, 
			final Date created,
			final Identifier creatorId)
	throws IdentifierGenerationException {
		super(id, importType, PIPEBLOCK_CODE, created, creatorId);
	}

	public void fromXmlTransferable(
			XmlPipeBlock xmlPipeBlock, 
			String importType) throws ApplicationException {

		this.number = xmlPipeBlock.isSetNumber() ?
				xmlPipeBlock.getNumber()
				: 0;
		this.leftToRight = xmlPipeBlock.isSetLeftToRight() ?
			xmlPipeBlock.getLeftToRight()
			: true;
		this.topToBottom = xmlPipeBlock.isSetTopToBottom() ?
			xmlPipeBlock.getTopToBottom()
			: true;
		this.horizontalVertical = xmlPipeBlock.isSetHorVert() ?
			xmlPipeBlock.getHorVert()
			: true;

		this.dimension = (xmlPipeBlock.isSetDimensionX() 
				&& xmlPipeBlock.isSetDimensionY()) ?
			new IntDimension(
					xmlPipeBlock.getDimensionX(), 
					xmlPipeBlock.getDimensionY())
			: new IntDimension(0, 0);
	}

	public static PipeBlock createInstance(
			final Identifier creatorId,
			final String importType,
			final XmlPipeBlock xmlPipeBlock)
	throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid() : NON_VOID_EXPECTED;

		try {
			final XmlIdentifier xmlId = xmlPipeBlock.getId();
			final Date created = new Date();
			final Identifier id = Identifier.fromXmlTransferable(xmlId, importType, MODE_RETURN_VOID_IF_ABSENT);
			PipeBlock pipeBlock;
			if (id.isVoid()) {
				pipeBlock = new PipeBlock(
						xmlId,
						importType,
						created,
						creatorId);
			} else {
				pipeBlock = StorableObjectPool.getStorableObject(id, true);
				if (pipeBlock == null) {
					LocalXmlIdentifierPool.remove(xmlId, importType);
					pipeBlock = new PipeBlock(
							xmlId,
							importType,
							created,
							creatorId);
				}
			}
			pipeBlock.fromXmlTransferable(xmlPipeBlock, importType);
			assert pipeBlock.isValid() : OBJECT_BADLY_INITIALIZED;
			pipeBlock.markAsChanged();
			return pipeBlock;
		} catch (final CreateObjectException coe) {
			throw coe;
		} catch (final ApplicationException ae) {
			Log.debugMessage(ae, SEVERE);
			throw new CreateObjectException(ae);
		}
	}

	public int getNumber() {
		return this.number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int compareTo(PipeBlock o) {
		return this.number - o.getNumber();
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getWrapper()
	 */
	@Override
	protected PipeBlockWrapper getWrapper() {
		return PipeBlockWrapper.getInstance();
	}
}
