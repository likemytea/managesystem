package com.chenxing.managesystem.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 下载文件
 * <p>
 * Created by liuxing on 17/1/18.
 */
@RestController
public class FileController {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@GetMapping("/h5/download")
	public void downloadFile(HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException {
		String input = request.getParameter("input");
		String fileName = input + ".docx";// 文件名
		if (fileName != null) {
			// 设置文件路径
			File file = new File("E:\\" + fileName);
			if (file.exists()) {
				response.setContentType("application/force-download");// 设置强制下载不打开
				response.addHeader("Content-Disposition",
						"attachment;filename=" + new String(fileName.getBytes("gb2312"), "ISO8859-1"));
				// response.addHeader("Content-Disposition", "attachment;fileName=" +
				// fileName);// 设置文件名
				 byte[] buffer = new byte[1024]; 
				 FileInputStream fis = null;
				 BufferedInputStream bis = null;
				 try {
	                    fis = new FileInputStream(file);
	                    bis = new BufferedInputStream(fis);
	                    OutputStream os = response.getOutputStream();
	                    int i = bis.read(buffer);
	                    while (i != -1) {
	                        os.write(buffer, 0, i);
	                        i = bis.read(buffer);
	                    }
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (bis != null) {
						try {
							bis.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if (fis != null) {
						try {
							fis.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}

	}
}
