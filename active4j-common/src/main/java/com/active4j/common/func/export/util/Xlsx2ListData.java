package com.active4j.common.func.export.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.util.SAXHelper;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class Xlsx2ListData {
	/**
	 * Uses the XSSF Event SAX helpers to do most of the work of parsing the
	 * Sheet XML, and outputs the contents as a List.
	 */
	private class SheetToList implements SheetContentsHandler {
		private boolean firstCellOfRow = false;
		private int currentRow = -1;
		private int currentCol = -1;
		private Object defaultValue = "";

		List<Object> currRowData = new ArrayList<>();
		private List<List<Object>> data = new ArrayList<>();

		private void outputMissingRows(int number) {
			for (int i = 0; i < number; i++) {
				List<Object> rowData = new ArrayList<>();
				for (int j = 0; j < minColumns; j++) {
					rowData.add(defaultValue);
				}
				processRowData(rowData);
			}
		}

		private void processRowData(List<Object> rowData) {
			if (rowDataProcesser != null) {
				rowDataProcesser.processRowData(rowData);
			} else {
				data.add(rowData);
			}
			currRowData = new ArrayList<>();
		}

		public List<List<Object>> getData() {
			return data;
		}

		public void startRow(int rowNum) {
			// If there were gaps, output the missing rows
			outputMissingRows(rowNum - currentRow - 1);
			// Prepare for this row
			firstCellOfRow = true;
			currentRow = rowNum;
			currentCol = -1;
		}

		public void endRow(int rowNum) {
			// Ensure the minimum number of columns
			for (int i = currentCol; i < minColumns - 1; i++) {
				currRowData.add(defaultValue);
			}
			processRowData(currRowData);
		}

		@Override
		public void cell(String cellReference, String formattedValue,
				XSSFComment comment) {
			if (firstCellOfRow) {
				firstCellOfRow = false;
			}

			// gracefully handle missing CellRef here in a similar way as
			// XSSFCell does
			if (cellReference == null) {
				cellReference = new CellAddress(currentRow, currentCol)
						.formatAsString();
			}

			// Did we miss any cells?
			int thisCol = (new CellReference(cellReference)).getCol();
			int missedCols = thisCol - currentCol - 1;
			for (int i = 0; i < missedCols; i++) {
				currRowData.add(defaultValue);
			}
			currentCol = thisCol;

			currRowData.add(formattedValue);
		}

		public void headerFooter(String text, boolean isHeader, String tagName) {
			// Skip, ignore headers or footers
		}
	}

	// /////////////////////////////////////

	private final OPCPackage xlsxPackage;

	/**
	 * Number of columns to read starting with leftmost
	 */
	private final int minColumns;

	private final RowDataProcesser rowDataProcesser;

	/**
	 * Creates a new XLSX -> List converter
	 *
	 * @param pkg
	 *            The XLSX package to process
	 * @param minColumns
	 *            The minimum number of columns to output, or -1 for no minimum
	 * @param rowDataProcesser
	 *            process row data
	 */
	public Xlsx2ListData(OPCPackage pkg, int minColumns,
			RowDataProcesser rowDataProcesser) {
		this.xlsxPackage = pkg;
		this.minColumns = minColumns;
		this.rowDataProcesser = rowDataProcesser;
	}

	/**
	 * Parses and shows the content of one sheet using the specified styles and
	 * shared-strings tables.
	 *
	 * @param styles
	 * @param strings
	 * @param sheetInputStream
	 */
	public void processSheet(StylesTable styles,
			ReadOnlySharedStringsTable strings,
			SheetContentsHandler sheetHandler, InputStream sheetInputStream)
			throws IOException, ParserConfigurationException, SAXException {
		DataFormatter formatter = new DataFormatter();
		InputSource sheetSource = new InputSource(sheetInputStream);
		try {
			XMLReader sheetParser = SAXHelper.newXMLReader();
			ContentHandler handler = new XSSFSheetXMLHandler(styles, null,
					strings, sheetHandler, formatter, false);
			sheetParser.setContentHandler(handler);
			sheetParser.parse(sheetSource);
		} catch (ParserConfigurationException e) {
			throw new RuntimeException("SAX parser appears to be broken - "
					+ e.getMessage());
		}
	}

	/**
	 * Initiates the processing of the XLS workbook file to List.
	 *
	 * @throws IOException
	 * @throws OpenXML4JException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	public List<List<Object>> process() throws IOException, OpenXML4JException,
			ParserConfigurationException, SAXException {
		ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(
				this.xlsxPackage);
		XSSFReader xssfReader = new XSSFReader(this.xlsxPackage);
		StylesTable styles = xssfReader.getStylesTable();
		XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader
				.getSheetsData();

		List<List<Object>> result = new ArrayList<>();
		while (iter.hasNext()) {
			InputStream stream = iter.next();
			SheetToList sheetToList = new SheetToList();
			processSheet(styles, strings, sheetToList, stream);
			result.addAll(sheetToList.getData());
			stream.close();
		}
		return result;
	}

}