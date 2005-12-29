/*-
* $Id: ImageResourceHandler.java,v 1.8 2005/12/29 09:53:39 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.resources;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

import com.syrus.AMFICOM.extensions.resources.Image;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.8 $, $Date: 2005/12/29 09:53:39 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module extensions
 */
public class ImageResourceHandler implements ConcreateResourceHandler<Image> {
	
	public void load(final Image image) {
		final String id = image.getId();
		
		final boolean useSize = image.isSetSize();
		final int width = useSize ? image.getSize().getWidth() : -1;
		final int height = useSize ? image.getSize().getHeight() : -1;
		
		if (useSize) {
			assert Log.debugMessage(id 
					+ " required for scaling to : " 
					+ width 
					+ 'x' 
					+ height, 
				Log.DEBUGLEVEL08);
		}
		
		if (image.isSetFilename()) {		
			final String url = image.getFilename();
			final URL resource = ImageResourceHandler.class.getClassLoader().getResource(url);
			
			assert Log.debugMessage("load " + resource + " as " + id, Log.DEBUGLEVEL08);
			
	
			
			if (resource != null) {
				UIManager.put(id, new UIDefaults.LazyValue() {
	
					public Object createValue(final UIDefaults table) {
						java.awt.Image image2 = Toolkit.getDefaultToolkit().createImage(resource);
						final java.awt.Image image3 = useSize ? 
								image2.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH) : image2;
						return new ImageIcon(image3);
					}
				});
			} else {
				final String message = "Image file " + url + " for id '" + id + "' does not exist.";
				System.err.println(message);
				Log.errorMessage(message);
			}
		} else if (image.isSetChar()) {
			UIManager.put(id, new UIDefaults.LazyValue() {
				
				public Object createValue(final UIDefaults table) {
					return getStringIcon(image.getChar(), 
						0, 
						width > 0 ? width : 16,
						height > 0 ? height : 16);
				}
			});
		}
	}
	
	/**
	 * create Icon with size 16x16 , and draw String on it
	 * 
	 * @param s
	 *            text which will draw on Icon
	 * @param angle
	 *            rotation angle in degree
	 * @return Icon
	 */
	public ImageIcon getStringIcon(final String s,
			final int angle,
			final int w,
			final int h) {
		BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB_PRE);
		final Graphics2D g2d = (Graphics2D) img.getGraphics();
		final FontMetrics fm = g2d.getFontMetrics();
		final Font font = UIManager.getFont("Button.font");
		g2d.setFont(font);
		g2d.setColor(UIManager.getColor("Button.foreground"));
		g2d.drawString(s, w / 4, (h / 2 + fm.getHeight()) / 2);
		
		if (angle != 0) {
			final AffineTransform tx = new AffineTransform();
			tx.rotate(angle * Math.PI / 180.0, 
				img.getWidth() / 2, 
				img.getHeight() / 2);
			
			final AffineTransformOp op = 
				new AffineTransformOp(tx, 
					AffineTransformOp.TYPE_BILINEAR);
			img = op.filter(img, null);
		}
		
		return new ImageIcon(img);
	}
}
