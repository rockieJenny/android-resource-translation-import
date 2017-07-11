package com.lionmobi;

import java.io.FileInputStream;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils {

	

    /**
     * ����EXCEL
     * @param rowHandler
     * @param fis
     */
    public static void resolve(RowHandler rowHandler, FileInputStream fis) {
        //excel 2003
        try {
            XSSFWorkbook wordbook = new XSSFWorkbook(fis);
            resolveFromExcel2007(wordbook, rowHandler);
        } catch (Exception e) {//excel 2007
        	e.printStackTrace();
            try {
                Workbook workbook = Workbook.getWorkbook(fis);
                resolveFromExcel2003(workbook, rowHandler);
            } catch (Exception exc) {
            	exc.printStackTrace();
                throw new RuntimeException("the excel can not be resolved:{}", exc);
            }
        }
    }


    /**
     * ����EXCEL2003
     * @param xssfWorkbook
     * @param rowHandler
     */
    private static void resolveFromExcel2007(XSSFWorkbook xssfWorkbook, RowHandler rowHandler) {
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

    private static String getValue(XSSFCell xssfCell) {
        if (xssfCell.getCellType() == XSSFCell.CELL_TYPE_BOOLEAN) {
            return String.valueOf(xssfCell.getBooleanCellValue());
        } else if (xssfCell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC || xssfCell.getCellType() == XSSFCell.CELL_TYPE_FORMULA) {
            return xssfCell.getRawValue();
        } else {
            return String.valueOf(xssfCell.getStringCellValue());
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
                for (int cellNum = 0; cellNum <= hssfRow.length; cellNum++) {
                    jxl.Cell hssfCell = hssfRow[cellNum];
                    if (hssfCell == null) {
                        continue;
                    }
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
