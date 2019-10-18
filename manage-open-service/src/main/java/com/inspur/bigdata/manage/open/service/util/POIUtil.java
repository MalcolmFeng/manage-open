package com.inspur.bigdata.manage.open.service.util;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * POI工具类
 */
public class POIUtil {

    // 扩展名
    public final static String XLS = "xls";
    public final static String XLSX = "xlsx";


    /**
     * * 读取excel文件
     *
     * @param excelFile excel文件
     * @param startRow  读取数据的起始行, 行号从0开始
     * @return
     * @throws IOException
     */
    public static List<String[]> readExcelFile(MultipartFile excelFile, int startRow) throws IOException {
        // 检查文件
        checkFile(excelFile);
        // 获得工作簿对象
        Workbook workbook = getWorkBook(excelFile);
        // 创建返回对象，把每行中的值作为一个数组，所有的行作为一个集合返回
        List<String[]> list = new ArrayList<>();
        if (workbook != null) {
            for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
                // 获取当前sheet工作表
                Sheet sheet = workbook.getSheetAt(sheetNum);
                if (sheet == null) {
                    continue;
                }
                // 获得当前sheet的结束行
                int lastRowNum = sheet.getLastRowNum();
                if (startRow < 0 || startRow > lastRowNum) {
                    throw new RuntimeException("wrong startRow");
                }
                Row rowZore = sheet.getRow(0);
                int physicalNumberOfCellsCount = rowZore.getPhysicalNumberOfCells();

                // 循环除了第一行之外的所有行
                for (int rowNum = startRow; rowNum <= lastRowNum; rowNum++) {
                    // 获得当前行
                    Row row = sheet.getRow(rowNum);
                    if (row == null) {
                        continue;
                    }
                    // 获得当前行的开始列
                    int firstCellNum = row.getFirstCellNum();
                    // 获得当前行的列数
                    int lastCellNum = physicalNumberOfCellsCount;
                    String[] cells = new String[lastCellNum];
                    // 循环当前行
                    for (int cellNum = firstCellNum; cellNum < lastCellNum; cellNum++) {
                        Cell cell = row.getCell(cellNum);
                        cells[cellNum] = getCellValue(cell);
                    }
                    list.add(cells);
                }
            }
        }
        return list;
    }


    /**
     * 生成excel文件
     *
     * @param data      数据
     * @param extension 文件扩展
     * @return
     */
    public static Workbook createExcelFile(List<String> attributes, List<List<String>> data, String extension) {
        // 1. 创建workbook
        Workbook workbook = null;
        if (StringUtils.isBlank(extension)) {
            return null;
        }

        if (extension.equalsIgnoreCase(XLS)) {
            // 2003版本
            workbook = new HSSFWorkbook();
        } else if (extension.equalsIgnoreCase(XLSX)) {
            // 2007版本
            workbook = new XSSFWorkbook();
        }

        if (workbook != null) {
            // 2. 创建sheet
            Sheet sheet = workbook.createSheet("sheet1");
            // 3. 创建row: 添加属性行
            Row row0 = sheet.createRow(0);
            for (int i = 0; i < attributes.size(); i++) {
                Cell cell = row0.createCell(i);
                cell.setCellValue(attributes.get(i).trim());
            }
            // 4. 插入数据
            if (CollectionUtils.isNotEmpty(data)) {
                for (int i = 0; i < data.size(); i++) {
                    List<String> rowInfo = data.get(i);
                    Row row = sheet.createRow(i + 1);
                    // 添加数据
                    for (int j = 0; j < rowInfo.size(); j++) {
                        row.createCell(j).setCellValue(rowInfo.get(j));
                    }
                }
            }
        }
        return workbook;
    }


    /**
     * 获取当前列数据
     *
     * @param cell 列
     * @return 列值
     */
    private static String getCellValue(Cell cell) {
        String cellValue = "";

        if (cell == null) {
            return cellValue;
        }
        // 把数字当成String来读，避免出现1读成1.0的情况
        if (cell.getCellTypeEnum() == CellType.NUMERIC) {
            cell.setCellType(CellType.STRING);
        }
        // 判断数据的类型
        switch (cell.getCellTypeEnum()) {
            case NUMERIC:
                cellValue = String.valueOf(cell.getNumericCellValue());
                break;
            case STRING:
                cellValue = String.valueOf(cell.getStringCellValue());
                break;
            case BOOLEAN:
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case FORMULA:
                cellValue = String.valueOf(cell.getCellFormula());
                break;
            case BLANK:
                cellValue = "";
                break;
            case ERROR:
                cellValue = "非法字符";
                break;
            default:
                cellValue = "未知类型";
                break;
        }
        return cellValue;
    }


    /**
     * 获得工作簿对象
     *
     * @param excelFile excel文件
     * @return 工作簿对象
     */
    public static Workbook getWorkBook(MultipartFile excelFile) {
        // 获得文件名
        String fileName = excelFile.getOriginalFilename();
        // 创建Workbook工作簿对象，表示整个excel
        Workbook workbook = null;
        try {
            // 获得excel文件的io流
            InputStream is = excelFile.getInputStream();
            // 根据文件后缀名不同(xls和xlsx)获得不同的workbook实现类对象
            if (fileName.endsWith(XLS)) {
                // 2003版本
                workbook = new HSSFWorkbook(is);
            } else if (fileName.endsWith(XLSX)) {
                // 2007版本
                workbook = new XSSFWorkbook(is);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return workbook;
    }


    /**
     * 检查文件
     *
     * @param excelFile excel文件
     * @throws IOException
     */
    public static void checkFile(MultipartFile excelFile) throws IOException {
        //判断文件是否存在
        if (null == excelFile) {
            throw new FileNotFoundException("文件不存在");
        }
        //获得文件名
        String fileName = excelFile.getOriginalFilename();
        //判断文件是否是excel文件
        if (!fileName.endsWith(XLS) && !fileName.endsWith(XLSX)) {
            throw new IOException(fileName + "不是excel文件");
        }
    }
}