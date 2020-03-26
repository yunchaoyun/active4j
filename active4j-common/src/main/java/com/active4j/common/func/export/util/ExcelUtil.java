package com.active4j.common.func.export.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @title ExcelUtil.java
 * @description 
		解析2007excel大数据
 * @time  2019年12月17日 下午1:22:28
 * @author guyp
 * @version 1.0
 */
@Slf4j
public class ExcelUtil {

	/**
	 * 读取xlsx文件，不适用于读取大文件
	 */
	public static List<List<String>> read(String filePath) throws Exception {
		List<List<String>> result = new ArrayList<>();
		Workbook wb = new XSSFWorkbook(new File(filePath));
		for (Sheet sheet : wb) {
			for (Row row : sheet) {
				List<String> rowData = getRowData(row);
				result.add(rowData);
			}
		}
		wb.close();
		return result;
	}

	/**
	 * 读取xlsx文件，不适用于读取大文件
	 * 
	 * @return map: key->sheet name, value->rowData
	 */
	public static Map<String, List<List<String>>> readMultiSheet(String filePath)
			throws Exception {
		Map<String, List<List<String>>> result = new LinkedHashMap<>();
		Workbook wb = new XSSFWorkbook(new File(filePath));
		for (Sheet sheet : wb) {
			List<List<String>> sheetResult = new ArrayList<>();
			for (Row row : sheet) {
				List<String> rowData = getRowData(row);
				sheetResult.add(rowData);
			}
			result.put(sheet.getSheetName(), sheetResult);
		}
		wb.close();
		return result;
	}

	/**
	 * 以SAX方式读取xlsx文件，读取后将结果全部加载到内存，适用于读取较大文件
	 * 
	 * @param filePath
	 *            文件路径
	 * @param minColumns
	 *            补齐到多少列，-1表示不补齐
	 */
	public static List<List<Object>> readBigFile(String filePath, int minColumns)
			throws Exception {
		File xlsxFile = new File(filePath);
		OPCPackage p = OPCPackage.open(xlsxFile.getPath(), PackageAccess.READ);
		Xlsx2ListData xlsx2ListData = new Xlsx2ListData(p, minColumns, null);
		List<List<Object>> result = xlsx2ListData.process();
		p.close();
		return result;
	}
	
	/**
	 * 以SAX方式读取xlsx文件，读取后将结果全部加载到内存，适用于读取较大文件
	 * @param in
	 * @param minColumns
	 * @return
	 * @throws Exception
	 */
	public static List<List<Object>> readBigFile2(InputStream in, int minColumns)
			throws Exception {
		OPCPackage p = OPCPackage.open(in);
		Xlsx2ListData xlsx2ListData = new Xlsx2ListData(p, minColumns, null);
		List<List<Object>> result = xlsx2ListData.process();
		p.close();
		return result;
	}

	/**
	 * 以SAX方式读取xlsx文件，读取每一行调用RowDataProcesser，适用于读超大文件
	 * 
	 * @param filePath
	 *            文件路径
	 * @param minColumns
	 *            补齐到多少列，-1表示不补齐
	 * @param rowDataProcesser
	 *            处理每一行的数据
	 */
	public static void readBigFile(String filePath, int minColumns,
			RowDataProcesser rowDataProcesser) throws Exception {
		File xlsxFile = new File(filePath);
		OPCPackage p = OPCPackage.open(xlsxFile.getPath(), PackageAccess.READ);
		Xlsx2ListData xlsx2ListData = new Xlsx2ListData(p, minColumns,
				rowDataProcesser);
		xlsx2ListData.process();
		p.close();
	}

	@SuppressWarnings("deprecation")
	private static List<String> getRowData(Row row) {
		List<String> rowData = new ArrayList<>();
		int cellNum = row.getLastCellNum();
		for (int i = 0; i < cellNum; i++) {
			Cell cell = row.getCell(i);
			if (cell == null) {
				rowData.add("");
				continue;
			}
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_STRING:
				rowData.add(cell.getRichStringCellValue().getString());
				break;
			case Cell.CELL_TYPE_NUMERIC:
				rowData.add(new DataFormatter().formatCellValue(cell));
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				rowData.add(cell.getBooleanCellValue() + "");
				break;
			case Cell.CELL_TYPE_FORMULA:
				rowData.add(cell.getCellFormula());
				break;
			case Cell.CELL_TYPE_BLANK:
			case Cell.CELL_TYPE_ERROR:
			default:
				rowData.add("");
				break;
			}
		}
		return rowData;
	}
	
	/**
	 * 
	 * @description
	 *  	读取inputstream 得到workbook
	 *  	因为xlsx文件可能过大导致读取缓慢，这里只用到读取xls方法
	 * @params
	 * @return Workbook
	 * @author guyp
	 * @time 2019年12月17日 下午4:31:26
	 */
	public static Workbook readExcel(String extName, InputStream in) {
		if (null == in) {
			return null;
		}
		Workbook wb = null;
		try {
			if (StringUtils.endsWith(extName, ".xls")) {
				wb = new HSSFWorkbook(in);
			} else if (StringUtils.endsWith(extName, ".xlsx")) {
				wb = new XSSFWorkbook(in);
			} else {
				wb = null;
			}
		} catch (FileNotFoundException e) {
			log.error("读取excel文件{}报错，错误信息{}", extName, e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			log.error("读取excel文件{}报错，错误信息{}", extName, e.getMessage());
			e.printStackTrace();
		}
		return wb;
	}
	
	@SuppressWarnings("deprecation")
	public static String getCellValue(Cell cell) {
		String cellValue = "";
		if (cell != null) {
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_NUMERIC:
				DataFormatter dataFormatter = new DataFormatter();
				dataFormatter.addFormat("###########", null);
				cellValue = dataFormatter.formatCellValue(cell);
				break;
			case Cell.CELL_TYPE_STRING:
				cellValue = cell.getStringCellValue();
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				cellValue = String.valueOf(cell.getBooleanCellValue());
				break;
			case Cell.CELL_TYPE_FORMULA:
				cellValue = String.valueOf(cell.getCellFormula());
				break;
			case Cell.CELL_TYPE_BLANK:
				cellValue = "";
				break;
			case Cell.CELL_TYPE_ERROR:
				cellValue = "";
				break;
			default:
				cellValue = cell.toString().trim();
				break;
			}
		}
		return cellValue.trim();
	}

}
