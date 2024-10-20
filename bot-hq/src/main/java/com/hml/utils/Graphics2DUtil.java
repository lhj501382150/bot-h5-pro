package com.hml.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Graphics2DUtil {
  private static final String FONT_FAMILY = "楷体";
  private static final String CERTIFICATE_BASE_PATH = "/src/main/resources/static/certificate-blank.png";
  private static final String WATERMARK_IMAGE_PATH = "/src/main/resources/static/icon.png";
  
  public static void graphics2DDrawTest(String srcImgPath, String outPath) {
    try {
      BufferedImage targetImg = ImageIO.read(new File(srcImgPath));
      int imgWidth = targetImg.getWidth();
      int imgHeight = targetImg.getHeight();
      BufferedImage bufferedImage = new BufferedImage(imgWidth, imgHeight,
          BufferedImage.TYPE_INT_BGR);
      Graphics2D g = bufferedImage.createGraphics();
 
      g.drawImage(targetImg, 0, 0, imgWidth, imgHeight, null);
      g.setColor(Color.BLACK);
      // 第一行文本字体大小为120，居中显示
      Font userNameFont = new Font(FONT_FAMILY, Font.PLAIN, 120);
      g.setFont(userNameFont);
 
      String userName = "hresh";
      int[] userNameSize = getContentSize(userNameFont, userName);
 
      int userNameLeftMargin = (imgWidth - userNameSize[0]) / 2;
      int userNameTopMargin = 400 + userNameSize[1];
      g.drawString(userName, userNameLeftMargin, userNameTopMargin);
      g.dispose();
 
      FileOutputStream outImgStream = new FileOutputStream(outPath);
      ImageIO.write(bufferedImage, "png", outImgStream);
      g.dispose();
    } catch (IOException e) {
      e.getStackTrace();
    }
  }
 
  /**
   * 获取文本的长度，字体大小不同，长度也不同
   *
   * @param font
   * @param content
   * @return
   */
  public static int[] getContentSize(Font font, String content) {
    int[] contentSize = new int[2];
    FontRenderContext frc = new FontRenderContext(new AffineTransform(), true, true);
    Rectangle rec = font.getStringBounds(content, frc).getBounds();
    contentSize[0] = (int) rec.getWidth();
    contentSize[1] = (int) rec.getHeight();
    return contentSize;
  }
 
 
  public static void main(String[] args) throws IOException {
    String projectPath = System.getProperty("user.dir");
    String srcImgPath = projectPath + CERTIFICATE_BASE_PATH;
    String outPath = projectPath + "/src/main/resources/static/out/image_by_graphics2D.png";
 
    graphics2DDrawTest(srcImgPath, outPath);
  }
}
