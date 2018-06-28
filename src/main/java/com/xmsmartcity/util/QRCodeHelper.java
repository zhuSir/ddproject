package com.xmsmartcity.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * 二维码生成帮助类
 * @author chenbinyi
 *
 */
public class QRCodeHelper {

	// 二维码颜色
	private static final int BLACK = 0xFF000000;
	// 二维码颜色
	private static final int WHITE = 0xFFFFFFFF;
	// LOGO宽度
	private static final int LOGO_WIDTH = 40;
	// LOGO高度
	private static final int LOGO_HEIGHT = 40;

	/**
	 * 把生成的二维码存入到图片中
	 * 
	 * @param matrix
	 *            zxing包下的二维码类
	 * @return
	 */
	public static BufferedImage toBufferedImage(BitMatrix matrix) {
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
			}
		}
		return image;
	}

	/**
	 * 生成二维码并写入文件
	 * 
	 * @param content
	 *            扫描二维码的内容
	 * @param format
	 *            图片格式 jpg
	 * @param file
	 *            文件
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static BufferedImage getQrcodeImage(String content, HttpServletRequest request, HashMap<String, Object> map,Integer width,Integer height)
			throws Exception {
		String dongdongstr = "咚咚维保云技术支持";
		String labelStr = "微信扫码报修";
		String equipStr = "设备名称:" + map.get("name");
		String companyStr = "厦门银江智慧城市股份有限公司";

		MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
		Map hints = new HashMap();
		// 设置UTF-8， 防止中文乱码
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		// 设置二维码四周白色区域的大小
		hints.put(EncodeHintType.MARGIN, 0);
		// 设置二维码的容错性
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

		// 画二维码
		BitMatrix bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
		BufferedImage code_image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				code_image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
			}
		}

		// logo绘制
		String path = request.getSession().getServletContext().getRealPath("/")
				+ "/resources/images/dongdong_logo.png";
		grawLogo(width, height, code_image, path);

		// 上方文字图片
		int up_height = height / 6;
		BufferedImage up_image = new BufferedImage(width, up_height, BufferedImage.TYPE_INT_RGB);
		GrawPic(up_image, width, up_height, labelStr, true);

		// 设备文字
		int equip_height = height / 8;
		BufferedImage equip_image = new BufferedImage(width, equip_height, BufferedImage.TYPE_INT_RGB);
		GrawPic(equip_image, width, equip_height, equipStr, false);

		// 公司文字
		int company_height = height / 8;
		BufferedImage company_image = new BufferedImage(width, company_height, BufferedImage.TYPE_INT_RGB);
		GrawPic(company_image, width, company_height, companyStr, false);

		// 咚咚
		int dongdong_height = height / 8;
		BufferedImage dongdong_image = new BufferedImage(width, dongdong_height, BufferedImage.TYPE_INT_RGB);
		GrawPic(dongdong_image, width, dongdong_height, dongdongstr, false);

		BufferedImage imageResult = new BufferedImage(width, up_height + height + equip_height+company_height+company_height,
				BufferedImage.TYPE_INT_RGB);
		Graphics g = imageResult.getGraphics();
		int startHeight = 0;
		g.drawImage(up_image, 0, 0, null);
		startHeight += up_image.getHeight();
		g.drawImage(code_image, 0, startHeight, null);
		startHeight += code_image.getHeight();
		g.drawImage(equip_image, 0, startHeight, null);
		startHeight += equip_image.getHeight();
		g.drawImage(company_image, 0, startHeight, null);
		startHeight += company_image.getHeight();
		g.drawImage(dongdong_image, 0, startHeight, null);

		return imageResult;
	}

	/**
	 * 生成二维码，带logo不带文字
	 * @param content
	 * @param request
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static BufferedImage getQrcodeImageByLogo(String content, HttpServletRequest request, HashMap<String, Object> map)
			throws Exception {
		int width = 240;
		int height = 240;
		MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
		Map hints = new HashMap();
		// 设置UTF-8， 防止中文乱码
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		// 设置二维码四周白色区域的大小
		hints.put(EncodeHintType.MARGIN, 0);
		// 设置二维码的容错性
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

		// 画二维码
		BitMatrix bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
		BufferedImage code_image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				code_image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
			}
		}

		// logo绘制
		String path = request.getSession().getServletContext().getRealPath("/")
				+ "/resources/images/logo_wb.jpg";
		grawLogo(width, height, code_image, path);
		return code_image;
	}
	
	/**
	 * 画logo
	 * 
	 * @param width
	 * @param height
	 * @param code_image
	 * @param path
	 * @throws IOException
	 */
	private static void grawLogo(int width, int height, BufferedImage code_image, String path) throws IOException {
		File file = new File(path);
		Image src = ImageIO.read(file);
		Image logo_image = src.getScaledInstance(LOGO_WIDTH, LOGO_HEIGHT, Image.SCALE_SMOOTH);
		BufferedImage tag = new BufferedImage(LOGO_WIDTH, LOGO_HEIGHT, BufferedImage.TYPE_INT_RGB);
		Graphics logo_g = tag.getGraphics();
		logo_g.drawImage(logo_image, 0, 0, null); // 绘制缩小后的图
		logo_g.dispose();
		src = logo_image;

		Graphics2D graph = code_image.createGraphics();
		int x = (width - LOGO_WIDTH) / 2;
		int y = (height - LOGO_HEIGHT) / 2;
		graph.drawImage(src, x, y, LOGO_WIDTH, LOGO_WIDTH, null);
		Shape shape = new RoundRectangle2D.Float(x, y, LOGO_WIDTH, LOGO_HEIGHT, 6, 6);
		graph.setStroke(new BasicStroke(3f));
		graph.draw(shape);
		graph.dispose();
	}

	/**
	 * 文字画成图片
	 * 
	 * @param image
	 * @param width
	 * @param height
	 * @param string
	 */
	public static void GrawPic(BufferedImage image, int width, int height, String string, boolean isUp) {
		Graphics2D g = image.createGraphics();
		g.setBackground(Color.WHITE);
		g.clearRect(0, 0, width, height);// 填充整个屏幕
		g.dispose();

		g = image.createGraphics();
		RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		rh.put(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		rh.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g.setRenderingHints(rh);
		g.drawImage(image, 0, 0, width, height, null);
		// 设置画笔的颜色
		g.setColor(Color.BLACK);
		// 设置字体
		Font font = new Font("微软雅黑", Font.PLAIN, isUp ? 14 : 12);
		FontMetrics metrics = g.getFontMetrics(font);
		// 文字在图片中的坐标 这里设置在中间
		int startlabelX = (width - metrics.stringWidth(string)) / 2;
//		int startlabelY = (height - metrics.getHeight()) / 2;
		int startlabelY = isUp ? (5 * height / 6) : (2*height / 3);
		g.setFont(font);
		g.drawString(string, startlabelX, startlabelY);
		g.dispose();
	}

}
