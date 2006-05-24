/**
 * $Id: Block.java,v 1.2 2006/05/24 11:05:45 stas Exp $
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.impexp.unicablemap.map;

import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.AMFICOM.map.xml.XmlPipeBlock;
import com.syrus.impexp.unicablemap.UniCableMapDatabase;
import com.syrus.impexp.unicablemap.UniCableMapLink;
import com.syrus.impexp.unicablemap.UniCableMapLinkType;
import com.syrus.impexp.unicablemap.UniCableMapObject;
import com.syrus.impexp.unicablemap.UniCableMapParameter;
import com.syrus.impexp.unicablemap.UniCableMapType;

public class Block {
	private String uid;
	private boolean fromRight = false;
	private boolean fromTop = false;
	private boolean horVert = true;
	private int dimensionX = -1;
	private int dimensionY = -1;
	private final int number;
	private Map<String, Integer> pipeNumbers = new HashMap<String, Integer>();
	private String comment = "";

	public Block(int number) {
		this.number = number;
	}

	public String getId() {
		return this.uid;
	}

	public void setId(String id) {
		this.uid = id;
	}

	public void setFromRight(boolean fromRight) {
		this.fromRight = fromRight;
	}

	public void setFromTop(boolean fromTop) {
		this.fromTop = fromTop;
	}

	public int getDimensionY() {
		return this.dimensionY;
	}

	public void setDimensionY(int dimensionY) {
		this.dimensionY = dimensionY;
	}

	public int getDimensionX() {
		return this.dimensionX;
	}

	public void setDimensionX(int dimensionX) {
		this.dimensionX = dimensionX;
	}

	public XmlPipeBlock getXmlPipeBlock() {
		XmlPipeBlock xmlPipeBlock = XmlPipeBlock.Factory.newInstance();
	
		XmlIdentifier xmlId = xmlPipeBlock.addNewId();
		xmlId.setStringValue(this.uid);

		xmlPipeBlock.setNumber(this.number);
		if(this.dimensionX != -1 && this.dimensionY != -1) {
			xmlPipeBlock.setDimensionX(this.dimensionX);
			xmlPipeBlock.setDimensionY(this.dimensionY);
		}
		xmlPipeBlock.setLeftToRight(!this.fromRight);
		xmlPipeBlock.setTopToBottom(this.fromTop);
		xmlPipeBlock.setHorVert(this.horVert);

		return xmlPipeBlock;
	}

	public static Block parseBlock(UniCableMapDatabase ucmDatabase, UniCableMapObject ucmObject, int number) throws SQLException {
		Block block = new Block(number);
		int pox = 0;
		int poy = 0;
		boolean leftToRight = true;
		boolean topToBottom = false;
		for(UniCableMapParameter param : ucmObject.buf.params) {
			if(param.realParameter.text.equals(UniCableMapParameter.UCM_X)) {
				pox = Integer.parseInt(param.value);
			}
			if(param.realParameter.text.equals(UniCableMapParameter.UCM_Y)) {
				poy = Integer.parseInt(param.value);
			}
			if(param.realParameter.text.equals(UniCableMapParameter.UCM_FROM_TOP)) {
				topToBottom = Boolean.parseBoolean(param.value);
			}
			if(param.realParameter.text.equals(UniCableMapParameter.UCM_FROM_RIGHT)) {
				leftToRight = ! Boolean.parseBoolean(param.value);
			}
		}
		String comment = "";
		int freenr = pox * poy + 1;
		for(UniCableMapLink ucmLink : ucmDatabase.getChildren(ucmObject)) {
			if(ucmLink.mod.text.equals(UniCableMapLinkType.UCM_CONTAINS_INSIDE)
					&& ucmLink.child.typ.text.equals(UniCableMapType.UCM_PIPE)) {
				UniCableMapObject truba = ucmLink.child;
				int seq = truba.un - ucmObject.un;
				if(seq > pox * poy) {
					System.out.println("seq " + seq
							+ " is greater than " + pox
							+ " * " + poy
							+ " for truba " + truba.un
							+ " (" + truba.text + ") "
							+ " in block " + ucmObject.un
							+ " (" + ucmObject.text + ") ");
					seq = freenr++;
					comment += "Внимание! Труба " + truba.un 
						+ " добавлена в блок труб " + ucmObject.un 
						+ " с индексным номером " + seq + "\n";
					if(seq > pox * poy) {
						System.out.println("increase dimensions from (" + pox 
								+ ", " + poy + ") to ("
								+ pox + ", " + (poy + 1) + ")");
						poy++;
					}
					for(UniCableMapParameter param : truba.buf.params) {
						System.out.println("\t    " + param.realParameter.text + " = " + param.value);
					}
				}
				block.pipeNumbers.put(String.valueOf(truba.un), Integer.valueOf(seq));
			}
		}
		block.setDimensionX(pox);
		block.setDimensionY(poy);
		block.setFromTop(topToBottom);
		block.setFromRight(leftToRight);
		block.setComment(comment);
		block.setId(String.valueOf(ucmObject.un));
		return block;
	}

	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Map<String, Integer> getPipeNumbers() {
		return Collections.unmodifiableMap(this.pipeNumbers);
	}
}
