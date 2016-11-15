package com.util;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Excel操作
 * 
 * @author lizhen
 *
 */
public class ExcelUtil {

    private static final Logger logger = Logger.getLogger(ExcelUtil.class);

    /**
     * 通用excel导出接口
     * 
     * @param list 导出数据集合 Object泛型
     * @param head ArrayList --> String[] 有序的String集合，String[] 中存放表头字段，表头名称，表格类型（string字符，number数值），列宽;
     * @param clazz 实际list中存储的数据泛型
     * @param sheetName sheet名称
     * @param title 表格内题头名称
     * @return
     */
    public static HSSFWorkbook export(List<Object> list, List<String[]> head, Class<?> clazz, String sheetName, String title) {
        try {
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet(sheetName);
            // 第一行 标题行
            HSSFRow row0 = sheet.createRow(0);
            HSSFCell cell0 = row0.createCell(0);
            cell0.setCellValue(title);

            // 获取该class的Method
            Map<String, Method> methods = new HashMap<String, Method>();
            HSSFRow row1 = sheet.createRow(1);
            int size = head.size();
            for (int i = 0; i < size; ++i) {
                String[] h = head.get(i);
                HSSFCell titleCell = row1.createCell(i);
                titleCell.setCellValue(h[1]);
                sheet.setColumnWidth(i, StringUtil.toInteger(h[3], 10 * 300));
                methods.put(h[0], getMethod(clazz, h[0]));
            }
            // 表格中内容
            int r = 2;
            for (Object obj : list) {
                HSSFRow row = sheet.createRow(r);
                for (int i = 0; i < size; ++i) {
                    String[] h = head.get(i);
                    HSSFCell cell = row.createCell(i);
                    Object value = methods.get(h[0]).invoke(obj);
                    if (value == null) {
                        value = "";
                    }
                    if ("number".equals(h[2])) {
                        cell.setCellValue(StringUtil.toDouble(value.toString(), 0d));
                    } else {
                        cell.setCellValue(value.toString());
                    }
                }
                r++;
            }
            return workbook;
        } catch (Exception e) {
            logger.error("excel导出异常", e);
            return null;
        }

    }

    private static Method getMethod(Class<?> clazz, String fieldName) {
        String methodName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        try {
            return clazz.getMethod("get" + methodName);
        } catch (SecurityException e) {
            logger.debug("getMethod异常", e);
        } catch (NoSuchMethodException e) {
            logger.debug("getMethod异常", e);
        }
        return null;
    }

    /**
     * 通用excel导出接口
     * 
     * @param list 导出数据集合 HashMap集合
     * @param head ArrayList --> Object[] 有序的Object集合，Object[] 中存放表头字段，表头名称，表格类型（string字符，number数值），列宽;
     * @param sheetName sheet名称
     * @param title 表格内题头名称
     * @return
     */
    public static HSSFWorkbook export(List<Map<String, Object>> list, List<String[]> head, String sheetName, String title) {
        try {
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet(sheetName);
            // 第一行 标题行
            HSSFRow row0 = sheet.createRow(0);
            HSSFCell cell0 = row0.createCell(0);
            cell0.setCellValue(title);

            HSSFRow row1 = sheet.createRow(1);
            int size = head.size();
            for (int i = 0; i < size; ++i) {
                String[] h = head.get(i);
                HSSFCell titleCell = row1.createCell(i);
                titleCell.setCellValue(h[1]);
                sheet.setColumnWidth(i, StringUtil.toInteger(h[3], 10 * 300));
            }
            // 表格中内容
            int r = 2;
            for (Map<String, Object> obj : list) {
                HSSFRow row = sheet.createRow(r);
                for (int i = 0; i < size; ++i) {
                    Object[] h = head.get(i);
                    HSSFCell cell = row.createCell(i);
                    Object value = obj.get(h[0]);
                    if (value == null) {
                        value = "";
                    }
                    if ("number".equals(h[2])) {
                        cell.setCellValue(StringUtil.toDouble(value.toString(), 0d));
                    } else {
                        cell.setCellValue(value.toString());
                    }
                }
                r++;
            }
            return workbook;
        } catch (Exception e) {
            logger.error("excel导出异常", e);
            return null;
        }

    }

    /**
     * <p>
     * 通用excel导入接口
     * </p>
     * 
     * @param in 上传文件流
     * @param sh 第几个sheet
     * @param col 读取多少列
     * @return 结果集,每一列名称以cell+列标
     */
    public static List<Map<String, Object>> importData(InputStream in, int sh, int col) {
        Workbook wb = null;
        try {
            wb = new XSSFWorkbook(in);
        } catch (Exception e) {
            try {
                wb = new HSSFWorkbook(in);
            } catch (IOException e1) {
            }
        }
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        try {
            Sheet sheet = wb.getSheetAt(sh);
            int rowNum = sheet.getLastRowNum();
            for (int i = 1; i <= rowNum; ++i) {
                Row row = sheet.getRow(i);
                Map<String, Object> data = new HashMap<String, Object>();
                for (int j = 0; j < col; ++j) {
                    data.put("cell" + j, row.getCell(j).getStringCellValue());
                }
                result.add(data);
            }
        } catch (Exception e) {
            logger.error("excel导入异常", e);
            return null;
        }
        return result;
    }

    public static void main(String[] args)throws Exception {

    }
}
