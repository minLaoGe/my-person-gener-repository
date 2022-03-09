/*
package io.renren.needMoudle;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.herocheer.ems.common.serializer.LocalDateTimeSerializer;
import com.herocheer.module.core.util.DateUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.Map.Entry;

*/
/**
 * @author hanjh
 * Excel 导出工具
 * @date 2019/4/18 11:42
 *//*

public class ExportExcelUtil {

    private static Logger logger = LoggerFactory.getLogger(ExportExcelUtil.class);

    private ExportExcelUtil() {
    }

    public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
    public static final int DEFAULT_COLOUMN_WIDTH = 17;

    public static Workbook exportExcels(String title, Map<String, String> headerMap, Collection<?> dataset) {
        return exportExcels(title, headerMap, dataset, DEFAULT_COLOUMN_WIDTH, DEFAULT_DATE_PATTERN);
    }

    public static Workbook exportExcels(String title, Map<String, String> headerMap, Collection<?> dataset, String datePattern) {
        return exportExcels(title, headerMap, dataset, DEFAULT_COLOUMN_WIDTH, datePattern);
    }

    public static Workbook exportExcels(String title, Map<String, String> headerMap, Collection<?> dataset, int colWidth) {
        return exportExcels(title, headerMap, dataset, colWidth, DEFAULT_DATE_PATTERN);
    }

    public static Workbook exportExcels(String title, Map<String, String> headerMap, Collection<?> dataset, int colWidth, String datePattern) {
        // 日期格式转json
        SerializeConfig config = new SerializeConfig();
        config.put(LocalDateTime.class, new LocalDateTimeSerializer());
        String json = JSON.toJSONString(dataset, config);
        JSONArray jsonArray = JSON.parseArray(json);
        return exportExcels(title, headerMap, jsonArray, colWidth, datePattern);
    }

    public static Workbook exportExcels(String title, Map<String, String> headerMap, JSONArray jsonArray) {
        return exportExcels(title, headerMap, jsonArray, DEFAULT_COLOUMN_WIDTH, DEFAULT_DATE_PATTERN);
    }

    public static Workbook exportExcels(String title, Map<String, String> headerMap, JSONArray jsonArray, String datePattern) {
        return exportExcels(title, headerMap, jsonArray, DEFAULT_COLOUMN_WIDTH, datePattern);
    }

    public static Workbook exportExcels(String title, Map<String, String> headerMap, JSONArray jsonArray, int colWidth) {
        return exportExcels(title, headerMap, jsonArray, colWidth, DEFAULT_DATE_PATTERN);
    }

    public static Workbook exportExcels(String title, Map<String, String> headerMap, JSONArray jsonArray, int colWidth, String datePattern) {
        logger.info(datePattern);
        int rowAccess = 1000;
        SXSSFWorkbook workbook = new SXSSFWorkbook(rowAccess);
        workbook.setCompressTempFiles(true);
        CellStyle titleStyle = ExcelUtil.setTitleStyle(workbook);
        CellStyle cellStyle = ExcelUtil.setCellStyle(workbook);
        SXSSFSheet sheet = (SXSSFSheet) workbook.createSheet();
        int minBytes = colWidth < DEFAULT_COLOUMN_WIDTH ? DEFAULT_COLOUMN_WIDTH : colWidth;
        int[] arrColWidth = new int[headerMap.size()];
        String[] properties = new String[headerMap.size()];
        String[] headers = new String[headerMap.size()];
        Iterator<Entry<String, String>> iter = headerMap.entrySet().iterator();
        for (int index = 0; iter.hasNext(); ++index) {
            Entry<String, String> entry = iter.next();
            properties[index] = entry.getKey();
            headers[index] = entry.getValue();
            int bytes = headers[index].getBytes().length;
            arrColWidth[index] = bytes < minBytes ? minBytes : bytes;
            sheet.setColumnWidth(index, arrColWidth[index] * 256);
        }
        int rowIndex = 0;
        for (Iterator it = jsonArray.iterator(); it.hasNext(); ++rowIndex) {
            Object obj = it.next();
            Row headerRow;
            if (rowIndex == 65535 || rowIndex == 0) {
                setColumnWidth(rowIndex, sheet, arrColWidth);
                SXSSFRow titleRow = (SXSSFRow) sheet.createRow(0);
                titleRow.createCell(0).setCellValue(title);
                titleRow.getCell(0).setCellStyle(titleStyle);
                sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headerMap.size() - 1));
                headerRow = sheet.createRow(1);
                setCell(headerRow, headers, workbook);
                rowIndex = 2;
            }
            JSONObject jo = (JSONObject) JSONObject.toJSON(obj);
            headerRow = sheet.createRow(rowIndex);
            setCellValue(jo, headerRow, properties, cellStyle);
            if (rowIndex % rowAccess == 0) {
                try {
                    sheet.flushRows();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }
        }
        return workbook;
    }

    private static Row setCell(Row headerRow, String[] headers, Workbook workbook) {
        CellStyle headerStyle = ExcelUtil.setHeaderStyle(workbook);
        for (int i = 0; i < headers.length; ++i) {
            headerRow.createCell(i).setCellValue(headers[i]);
            headerRow.getCell(i).setCellStyle(headerStyle);
        }
        return headerRow;
    }

    private static void setColumnWidth(int rowIndex, SXSSFSheet sheet, int[] arrColWidth) {
        if (rowIndex != 0) {
            for (int j = 0; j < arrColWidth.length; ++j) {
                sheet.setColumnWidth(j, arrColWidth[j] * 256);
            }
        }
    }

    private static Row setCellValue(JSONObject jo, Row headerRow, String[] properties, CellStyle cellStyle) {
        for (int i = 0; i < properties.length; ++i) {
            SXSSFCell newCell = (SXSSFCell) headerRow.createCell(i);
            Object o = jo.get(properties[i]);
            String cellValue = "";
            if (o == null) {
                cellValue = "";
            } else if (!(o instanceof Date) && !(o instanceof Calendar) && !(o instanceof LocalDateTime)) {
                if (!(o instanceof Float) && !(o instanceof Double)) {
                    cellValue = o.toString();
                } else {
                    cellValue = (new BigDecimal(o.toString())).setScale(2, 4).toString();
                }
            } else {
                cellValue = DateUtils.format(o, DEFAULT_DATE_PATTERN);
            }
            newCell.setCellValue(cellValue);
            newCell.setCellStyle(cellStyle);
        }
        return headerRow;
    }
}

*/
