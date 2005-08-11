package com.syrus.AMFICOM.report;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;

import com.syrus.io.ImageToByte;

/**
 * <p>Title: </p>
 * <p>Description: Панель для отображения GIF и JPG</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Syrus Systems</p>
 * @author Песковский Пётр
 * @version 1.0
 */

public final class ImageRenderingElement extends RenderingElement
	implements Serializable {

	private static final long serialVersionUID = 336147260496995306L;
	private BufferedImage image = null;
	
	public BufferedImage getImage() {
		return this.image;
	}
	public void setImage(BufferedImage image) {
		this.image = image;
	}
	
	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.writeObject(this.getBounds());
		byte[] imageBytes = ImageToByte.toByteArray(this.image);
		out.writeInt(imageBytes.length);
		out.write(imageBytes);
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		this.setBounds((Rectangle)in.readObject());
		int imageBytesCount = in.readInt();
		byte[] imageBytes = new byte[imageBytesCount];
		in.readFully(imageBytes);
		this.image = ImageToByte.byteToImqge(imageBytes);
	}
}
