package com.wishes.yearOld.common;

import java.io.*;
import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA. User: garmbrood Company: TianJi Media Group
 * DateTime:2009-4-25 19:40:09 Description:
 * 文件类操作,继承的方法参见org.apache.commons.io.FileUtils相关文档
 */
public class FileUtils extends org.apache.commons.io.FileUtils {
	// todo 其他自定义方法在该类中添加
	/**
	 * 将String写入到文件，该方法是以文本形式写得到文件中<br>
	 * --------------------------------------------------------------------------<br>
	 * 创建者：杨思勇<br>
	 * 创建日期：2004-1-5<br>
	 * <br>
	 * 修改者：<br>
	 * 修改日期：<br>
	 * 修改原因：<br>
	 * --------------------------------------------------------------------------
	 *
	 * @param fileFullName
	 *            文件全名
	 * @param fileContent
	 *            内容
	 * @param append
	 *            是否追加
	 * @throws java.io.IOException
	 *             例外
	 */
	public static void fileWrite(String fileFullName, String fileContent,
								 boolean append) throws IOException {
		File f = new File(fileFullName);
		if (!f.getParentFile().exists())
			f.getParentFile().mkdirs();
		fileWrite(f, fileContent, append);
	}

	/**
	 * 将String写入到文件，该方法是以文本形式写得到文件中<br>
	 * --------------------------------------------------------------------------<br>
	 * 创建者：杨思勇<br>
	 * 创建日期：2004-1-5<br>
	 * <br>
	 * 修改者：<br>
	 * 修改日期：<br>
	 * 修改原因：<br>
	 * --------------------------------------------------------------------------
	 *
	 * @param fileFullName
	 *            文件全名
	 * @param fileContent
	 *            内容
	 * @param append
	 *            是否追加
	 * @throws java.io.IOException
	 *             例外
	 */
	public static void fileWrite(File fileFullName, String fileContent,
								 boolean append) throws IOException {
		FileWriter writer = null;
		try {
			// ---------------------------------
			// 获得一个文件写入的句柄
			// ---------------------------------
			writer = new FileWriter(fileFullName, append);
			// ---------------------------------
			// 写入内容
			// ---------------------------------
			writer.write(fileContent);
			// ---------------------------------
			// 将内容写到碰盘上
			// ---------------------------------
			writer.flush();
		} finally {
			if (writer != null) {
				// ---------------------------------
				// 如果句柄不为空。则最后要关闭句柄
				// ---------------------------------
				try {
					writer.close();
					writer = null;
				} catch (IOException e) {
				}
			}
		}
	}

	/**
	 * 将byte数组写入到文件，本方法是以二进制的形式写到碰盘上<br>
	 * --------------------------------------------------------------------------<br>
	 * 创建者：杨思勇<br>
	 * 创建日期：2004-1-5<br>
	 * <br>
	 * 修改者：<br>
	 * 修改日期：<br>
	 * 修改原因：<br>
	 * --------------------------------------------------------------------------
	 *
	 * @param fileFullName
	 *            文件全名
	 * @param fileContent
	 *            内容
	 * @param append
	 *            是否追加
	 * @throws java.io.IOException
	 *             例外
	 */
	public static void fileWrite(String fileFullName, byte[] fileContent,
								 boolean append) throws IOException {
		fileWrite(new File(fileFullName), fileContent, append);
	}

	/**
	 * 将byte数组写入到文件，本方法是以二进制的形式写到碰盘上<br>
	 * --------------------------------------------------------------------------<br>
	 * 创建者：杨思勇<br>
	 * 创建日期：2004-1-5<br>
	 * <br>
	 * 修改者：<br>
	 * 修改日期：<br>
	 * 修改原因：<br>
	 * --------------------------------------------------------------------------
	 *
	 * @param fileFullName
	 *            文件全名
	 * @param fileContent
	 *            内容
	 * @param append
	 *            是否追加
	 * @throws java.io.IOException
	 *             例外
	 */
	public static void fileWrite(File fileFullName, byte[] fileContent,
								 boolean append) throws IOException {
		FileOutputStream outputStream = null;
		try {
			// ---------------------------------
			// 获得一个二进制写入流的句柄
			// ---------------------------------
			outputStream = new FileOutputStream(fileFullName, append);
			// ---------------------------------
			// 写入内容
			// ---------------------------------
			outputStream.write(fileContent);
			// ---------------------------------
			// 将内容写到碰盘上
			// ---------------------------------
			outputStream.flush();
		} finally {
			if (outputStream != null) {
				// ---------------------------------
				// 如果句柄不为空。则最后要关闭句柄
				// ---------------------------------
				try {
					outputStream.close();
					outputStream = null;
				} catch (Exception e) {
				}
			}
		}
	}

	public static void fileWrite(File path, String fileName, String content,
								 boolean append) throws IOException {
		if (!path.exists() || !path.isDirectory())
			path.mkdirs();
		File file = new File(path, fileName);
		fileWrite(file.getPath(), content, append);
	}

	public static void fileWrite(File path, String filename, byte[] data,
								 boolean append) throws IOException {
		FileOutputStream fos = null;
		try {
			if (!path.exists()) {
				path.mkdirs();
			}
			fos = new FileOutputStream(new File(path, filename));
			fos.write(data);
			fos.close();
		} finally {
			if (fos != null)
				fos.close();
		}
	}

	/**
	 * 根据URL地址，读取URL中的内容
	 *
	 *
	 * @param path
	 *            URL地址
	 * @return URL中的内容
	 */
	public static String getUrlContent(String path) {
		String rtn = "";
		int c;
		try {
			if(path.indexOf("http://") == -1){
				path = "http://" + path;
			}
			java.net.URL l_url = new java.net.URL(path);
			java.net.HttpURLConnection l_connection = (java.net.HttpURLConnection) l_url
					.openConnection();
			l_connection.setRequestProperty("User-agent", "Mozilla/4.0");
			l_connection.connect();
			InputStream l_urlStream = l_connection.getInputStream();
			while (((c = l_urlStream.read()) != -1)) {
				int all = l_urlStream.available();
				byte[] b = new byte[all];
				l_urlStream.read(b);
				rtn += new String(b, "UTF-8");
			}
			// Thread.sleep(2000);
			l_urlStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rtn;
	}

	/**
	 * 根据内容提取图片URL
	 * @param htmlStr 内容
	 * @return 图片URL数组
	 */
	public static String[] getImgStr(String htmlStr) {
		String img = "", tmp = "";
		java.util.regex.Pattern p_image;
		java.util.regex.Matcher m_image;

		String regEx_img = "<img\\s+[^>]*src=['\"]?([^'\"\\s>]*)['\"]?\\s*[^>]*>"; // 图片链接地址
		p_image = java.util.regex.Pattern.compile(regEx_img,
				java.util.regex.Pattern.CASE_INSENSITIVE);
		m_image = p_image.matcher(htmlStr);
		while (m_image.find()) {
			img = img + "," + m_image.group(1);
		}
		if (img.indexOf(",") >= 0){
			String images = img.substring(1);
			return images.split(",");
		}else{
			return null;
		}
	}

	/**
	 *
	 * @param size   文件大小
	 * @param precision   保留的小数位
	 * @return
	 */
	public static String byteCountToDisplaySize(long size, int precision) {
		String displaySize;

		if (size / ONE_GB > 0) {
			displaySize = String.valueOf(size / ONE_GB) + " GB";
		} else if (size / ONE_MB > 0) {
			//当文件大小为MB级时，保留两位小数位
			BigDecimal bd = new BigDecimal((double)size / ONE_MB);
			double filesize = bd.setScale(precision, bd.ROUND_HALF_UP).doubleValue();
			String size2 = String.valueOf(filesize);
			if(size2.indexOf(".") == -1){
				size2 = size2+".00";
			}else{
				for(int i=0; i<precision-size2.substring(size2.indexOf(".")+1).trim().length(); i++){
					size2+="0";
				}
			}
			displaySize = size2 + " MB";
		} else if (size / ONE_KB > 0) {
			displaySize = String.valueOf(size / ONE_KB) + " KB";
		} else {
			displaySize = String.valueOf(size) + " bytes";
		}
		return displaySize;
	}


	public static void main(String[] args) {
		String[] images = FileUtils.getImgStr(FileUtils.getUrlContent("http://www.baidu.com"));
		if(images!=null && images.length>0){
			for(int i=0; i<images.length; i++){
				System.out.println(images[i]);
			}
		}
	}

}
