package com.active4j.common.func.export.util;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @title ExportUtil.java
 * @description 
		excel导出工具类
 * @time  2019年12月18日 上午10:25:20
 * @author guyp
 * @version 1.0
 */
public class ExportUtil {

	/**
	 * 
	 * @description
	 *  	导出csv文件
	 * @params
	 * @return byte[]
	 * @author guyp
	 * @time 2019年12月18日 上午10:27:09
	 */
	public static byte[] exportCSV(List<LinkedHashMap<String, Object>> exportData) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BufferedWriter buffCvsWriter = null;
        try {
            buffCvsWriter = new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8));
            // 将body数据写入表格
            for(Iterator<LinkedHashMap<String, Object>> iterator = exportData.iterator(); iterator.hasNext();) {
                fillDataToCsv(buffCvsWriter, iterator.next());
                if(iterator.hasNext()) {
                	buffCvsWriter.newLine();
                }
            }
            // 刷新缓冲
            buffCvsWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 释放资源
            if(buffCvsWriter != null) {
                try {
                    buffCvsWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return out.toByteArray();
    }
 
	/**
	 * 
	 * @description
	 *  	将linkedHashMap中的数据，写入csv表格中
	 * @params
	 * @return void
	 * @author guyp
	 * @time 2019年12月18日 上午10:27:38
	 */
    private static void fillDataToCsv(BufferedWriter buffCvsWriter, LinkedHashMap row) throws IOException {
        Map.Entry propertyEntry;
        for(Iterator<Map.Entry> propertyIterator = row.entrySet().iterator(); propertyIterator.hasNext();) {
            propertyEntry = propertyIterator.next();
            buffCvsWriter.write("\"" + propertyEntry.getValue().toString() + "\"");
            if(propertyIterator.hasNext()) {
                buffCvsWriter.write(",");
            }
        }
    }
    
    /**
     * 
     * @description
     *  	导出xlsx文件
     * @params
     * @return byte[]
     * @author guyp
     * @time 2019年12月18日 上午10:40:56
     */
    public static byte[] exportXlsx(Map<String, List<LinkedHashMap<String, Object>>> tableData) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
     
        try {
            XSSFWorkbook workbook = new XSSFWorkbook();
            // 创建多个sheet
            for(Map.Entry<String, List<LinkedHashMap<String, Object>>> entry : tableData.entrySet()) {
            	//数据写入xlsx表格中
                fillDataToXlsx(workbook.createSheet(entry.getKey()), entry.getValue());
            }
            //写出
            workbook.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }
     
    /**
     * 
     * @description
     *  	将linkedHashMap中的数据，写入xlsx表格中
     * @params
     * @return void
     * @author guyp
     * @time 2019年12月18日 上午10:41:17
     */
    private static void fillDataToXlsx(XSSFSheet sheet, List<LinkedHashMap<String, Object>> data) {
        XSSFRow currRow;
        XSSFCell cell;
        LinkedHashMap row;
        Map.Entry propertyEntry;
        int rowIndex = 0;
        int cellIndex = 0;
        for(Iterator<LinkedHashMap<String, Object>> iterator = data.iterator(); iterator.hasNext();) {
            row = iterator.next();
            currRow = sheet.createRow(rowIndex++);
            for(Iterator<Map.Entry> propertyIterator = row.entrySet().iterator(); propertyIterator.hasNext();) {
                propertyEntry = propertyIterator.next();
                if(propertyIterator.hasNext()) {
                    String value = String.valueOf(propertyEntry.getValue());
                    cell = currRow.createCell(cellIndex++);
                    cell.setCellValue(value);
                }else {
                    String value = String.valueOf(propertyEntry.getValue());
                    cell = currRow.createCell(cellIndex++);
                    cell.setCellValue(value);
                    break;
                }
            }
            if(iterator.hasNext()) {
                cellIndex = 0;
            }
        }
    }
    
    /**
     * 
     * @description
     *  	导出xls文件
     * @params
     * @return byte[]
     * @author guyp
     * @time 2019年12月18日 上午11:26:01
     */
    public static byte[] exportXls(Map<String, List<LinkedHashMap<String, Object>>> tableData) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
     
        try {
            HSSFWorkbook workbook = new HSSFWorkbook();
            // 创建多个sheet
            for(Map.Entry<String, List<LinkedHashMap<String, Object>>> entry : tableData.entrySet()) {
            	//数据写入xls表格中
                fillDataToXls(workbook.createSheet(entry.getKey()), entry.getValue());
            }
            //写出
            workbook.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }
    
    /**
     * 
     * @description
     *  	将linkedHashMap中的数据，写入xls表格中
     * @params
     * @return void
     * @author guyp
     * @time 2019年12月18日 上午11:24:57
     */
    private static void fillDataToXls(HSSFSheet sheet, List<LinkedHashMap<String, Object>> data) {
        HSSFRow currRow;
        HSSFCell cell;
        LinkedHashMap row;
        Map.Entry propertyEntry;
        int rowIndex = 0;
        int cellIndex = 0;
        for(Iterator<LinkedHashMap<String, Object>> iterator = data.iterator(); iterator.hasNext();) {
            row = iterator.next();
            currRow = sheet.createRow(rowIndex++);
            for(Iterator<Map.Entry> propertyIterator = row.entrySet().iterator(); propertyIterator.hasNext();) {
                propertyEntry = propertyIterator.next();
                if(propertyIterator.hasNext()) {
                    String value = String.valueOf(propertyEntry.getValue());
                    cell = currRow.createCell(cellIndex++);
                    cell.setCellValue(value);
                }else {
                    String value = String.valueOf(propertyEntry.getValue());
                    cell = currRow.createCell(cellIndex++);
                    cell.setCellValue(value);
                    break;
                }
            }
            if(iterator.hasNext()) {
                cellIndex = 0;
            }
        }
    }
}
