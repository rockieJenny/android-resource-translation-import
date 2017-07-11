package com.lionmobi;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils {

	

    /**
     * ����EXCEL
     * @param rowHandler
     * @param fis
     * @throws IOException 
     */
    public static void resolve(RowHandler rowHandler, InputStream fis) throws Exception {
		if(! fis.markSupported()) {
			fis = new PushbackInputStream(fis, 8);
		}
		if(POIFSFileSystem.hasPOIFSHeader(fis)) {
			Workbook workbook = Workbook.getWorkbook(fis);
            resolveFromExcel2003(workbook, rowHandler);
		}
		if(POIXMLDocument.hasOOXMLHeader(fis)) {
			XSSFWorkbook wordbook = new XSSFWorkbook(fis);
            resolveFromExcel2007(wordbook, rowHandler);
		}
    }


    /**
     * ����EXCEL2003
     * @param xssfWorkbook
     * @param rowHandler
     * @throws UnsupportedEncodingException 
     */
    private static void resolveFromExcel2007(XSSFWorkbook xssfWorkbook, RowHandler rowHandler) throws UnsupportedEncodingException {
        for (int sheetNum = 0; sheetNum < xssfWorkbook.getNumberOfSheets(); sheetNum++) {
            XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(sheetNum);
            if (xssfSheet == null) {
                continue;
            }
            for (int rowNum = 0; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
                Row row = new Row(rowNum, sheetNum);
                XSSFRow xssfRow = xssfSheet.getRow(rowNum);
                if (xssfRow == null) {
                    continue;
                }
                for (int cellNum = 0; cellNum <= xssfRow.getLastCellNum(); cellNum++) {
                    XSSFCell xssfCell = xssfRow.getCell(cellNum);
                    if (xssfCell == null) {
                    	Cell cell = new Cell(cellNum);
                        cell.setContent("");
                        row.addCell(cell);
                        continue;
                    }
                    Cell cell = new Cell(cellNum);
                    cell.setContent(getValue(xssfCell));
                    row.addCell(cell);
                }
                //if (!emptyRow(row)) {
                    Boolean handleResult = rowHandler.handleRow(row);
                    if (handleResult != null && handleResult == true) {
                        return;
                    }
                //}
            }
        }
    }

    /**
     * @param row
     * @return
     */
    private static boolean emptyRow(Row row) {
        List<Cell> cells = row.getCells();
        for (Cell cell : cells) {
            if (!"".equals(cell.getContent())) {
                return false;
            }
        }
        return true;
    }

    static int i = 0;
    
    private static String getValue(XSSFCell xssfCell) throws UnsupportedEncodingException {
        if (xssfCell.getCellType() == XSSFCell.CELL_TYPE_BOOLEAN) {
            return String.valueOf(xssfCell.getBooleanCellValue());
        } else if (xssfCell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC || xssfCell.getCellType() == XSSFCell.CELL_TYPE_FORMULA) {
            return xssfCell.getRawValue();
        } else {
        	/*System.out.println(new String(xssfCell.getStringCellValue().getBytes("GBK"),"UTF-8"));
        	System.out.println(new String(xssfCell.getStringCellValue().getBytes("GB2312"),"UTF-8"));
        	System.out.println(new String(xssfCell.getStringCellValue().getBytes(),"UTF-8"));*/
        	/*System.out.println(new String(xssfCell.getStringCellValue().getBytes(Charset.forName("UTF-8")),"UTF-8"));
        	System.out.println(new String(xssfCell.getStringCellValue().getBytes(Charset.forName("GB2312")),"UTF-8"));
        	System.out.println(new String(xssfCell.getStringCellValue().getBytes(),"UTF-8"));
        	System.out.println(new String(xssfCell.getStringCellValue().getBytes("ISO8859_1"),"UTF-8"));
        	System.out.println(new String(xssfCell.getStringCellValue().getBytes(Charset.forName("GBK")),"UTF-8"));*/
        
        	byte[] bytes = xssfCell.getStringCellValue().getBytes();
        	StringBuilder sb = new StringBuilder();
        	for(byte b : bytes){
        		sb.append((int)b);
        	}
        	/*if( i++ <100 ){
        		System.out.println(sb.toString());
        		System.out.println(new String(xssfCell.getStringCellValue().getBytes("UTF-8"),"GBK"));
        	}*/
            return xssfCell.getStringCellValue(); //String.valueOf(xssfCell.getStringCellValue());
        }
    }

    /**
     * ����EXCEL2003
     * @param workbook
     * @param rowHandler
     */
    private static void resolveFromExcel2003(Workbook workbook, RowHandler rowHandler) {
        for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
            Sheet hssfSheet = workbook.getSheet(sheetNum);
            if (hssfSheet == null) {
                continue;
            }
            for (int rowNum = 0; rowNum <= hssfSheet.getColumns(); rowNum++) {
                Row row = new Row(rowNum, sheetNum);
                jxl.Cell[] hssfRow = hssfSheet.getRow(rowNum);
                if (hssfRow == null) {
                    continue;
                }
                for (int cellNum = 0; cellNum < hssfRow.length; cellNum++) {
                    jxl.Cell hssfCell = hssfRow[cellNum];
                    Cell cell = new Cell(cellNum);
                    cell.setContent(hssfCell.getContents());
                    row.addCell(cell);
                }
                if (!emptyRow(row)) {
                    Boolean handleResult = rowHandler.handleRow(row);
                    if (handleResult != null && handleResult == true) {
                        return;
                    }
                }
            }
        }
    }

}
