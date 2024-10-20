package com.hml.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

/**
 * POI相关操作
 * @author hml
 * @date Jan 14, 2019
 */
public class PoiUtils {

	/**
	 * 生成Excel文件
	 * @param workbook
	 * @param fileName
	 * @return
	 */
	public static File createExcelFile(Workbook workbook, String fileName) {
		OutputStream stream = null;
		File file = null;
		try {
			file = File.createTempFile(fileName, ".xlsx");
			stream = new FileOutputStream(file.getAbsoluteFile());
			workbook.write(stream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(workbook);
			IOUtils.closeQuietly(stream);
		}
		return file;
	}
	
	/**
     * Excel 解析成数据集合
     *
     * @return
	 * @throws IOException 
     */
    public static String[][] imporData(MultipartFile file) throws IOException {
    	  boolean isExcel2007 = true;
    	  String fileName = file.getOriginalFilename();
    	  if(fileName.endsWith("xlsx")) {
    		  isExcel2007 = false;
    	  }
          InputStream is = file.getInputStream();
          Workbook wb = null;
          if (isExcel2007) {
              wb = new HSSFWorkbook(is);
          }else{
              wb = new XSSFWorkbook(is);
          }
          Sheet sheet = wb.getSheetAt(0);
          Row row = null;
          Cell cell = null;
          int count_row = sheet.getLastRowNum();
          int count_cell = sheet.getRow(0).getPhysicalNumberOfCells();
          String[][] str = new String[count_row][count_cell];
          for (int i = 0; i < count_row; i++) {
            for (int j = 0; j < count_cell; j++)
            {
              row = sheet.getRow(i + 1);
              if (row != null)
              {
                cell = row.getCell((short)j);
                String k = "";
                if (cell != null)
                {
                  int type = cell.getCellType().getCode();
                  switch (type)
                  {
                  case 3: 
                    k = ""; break;
                  case 4: 
                    k = cell.getBooleanCellValue() + ""; break;
                  case 5: 
                    k = cell.getErrorCellValue() + ""; break;
                  case 2: 
                    k = cell.getCellFormula() + ""; break;
                  case 0: 
                	  k = cell.getNumericCellValue() + "";
                      k = new DecimalFormat("0.####").format(Double.valueOf(k));
                    break;
                  default: 
                    k = cell.getStringCellValue(); break;
                  }
                }
                str[i][j] = k.trim();
//                System.out.println(k);
              }
            }
          }
          wb = null;
          sheet = null;
          cell = null;
          row = null;
          return str;
    }
}
