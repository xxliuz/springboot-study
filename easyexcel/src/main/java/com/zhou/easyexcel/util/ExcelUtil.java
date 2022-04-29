package com.zhou.easyexcel.util;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.WriteTable;
import com.zhou.easyexcel.entity.DemoData;
import com.zhou.easyexcel.listener.ModelExcelListener;
import com.zhou.easyexcel.listener.StringExcelListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Table;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
/**
 * @Author: zhou.liu
 * @Date: 2022/4/28 15:20
 * @Description: excel导入导出工具类
 *  1.支持按行导入字符串方式
 *  2.支持导入实体类映射
 *  3.支持按行导出字符串方式
 *  4.支持导出实体类映射
 */
@Slf4j
public class ExcelUtil {

    public static final String path = "D:/";
    /**
     * @Description: 使用 StringList 来读取Excel
     * @param inputStream Excel的输入流
     * @Return: java.util.List<java.util.List<java.lang.String>>
     */
    public static List<List<String>> readExcel(InputStream inputStream){
        StringExcelListener stringExcelListener = new StringExcelListener();
        EasyExcel.read(inputStream, stringExcelListener).sheet().doRead();
        return stringExcelListener.getDatas();
    }

    /**
     * @Description: 使用模型来读取Excel
     * @param inputStream Excel的输入流
     * @param clazz 模型的类
     * @Return: java.util.List<E>
     */
    public static <E> List<E> readExcel(InputStream inputStream, Class clazz){
        // 解析每行结果在listener中处理
        ModelExcelListener<E> listener = new ModelExcelListener<E>();
        //默认只有一列表头
        EasyExcel.read(inputStream, clazz,listener).sheet().doRead();
        return listener.getDataList();
    }

    /**
     * @Description: 使用StringList来写入Excel，单sheet，单table
     * @param outputStream Excel的输出流
     * @param dataList 要写入的以StringList为单位的数据
     */
    public static void writeExcel(OutputStream outputStream, List<List<String>> dataList, ExcelTypeEnum excelTypeEnum){
        //这里指定不需要表头，因为String通常表头已被包含在data里
        String fileName = path+"writeExcel" + System.currentTimeMillis() + ".xlsx";
        EasyExcel.write(outputStream).excelType(excelTypeEnum).file(outputStream).sheet("模板").doWrite(dataList);
    }

    /**
     * @Description: 使用StringList来写入Excel，单sheet，单table（返回byte数组）
     * @param outputStream Excel的输出流
     * @param dataList 要写入的以StringList为单位的数据
     * @param table 配置Excel的表的属性
     * @param excelTypeEnum Excel的格式(XLS或XLSX)
     * @Return: byte[]
     */
    public static byte[] writeExcel(ByteArrayOutputStream outputStream, List<List<String>> dataList, Table table, ExcelTypeEnum excelTypeEnum) throws Exception {
        //这里指定不需要表头，dataList
        String fileName = "writeExcel" + System.currentTimeMillis() + ".xlsx";
        EasyExcel.write(fileName).file(outputStream).excelType(excelTypeEnum).sheet("模板").doWrite(dataList);
        return outputStream.toByteArray();
    }

    /**
     * @Description: 使用模型来写入Excel，单sheet，单table
     * @param outputStream Excel的输出流
     * @param dataList 要写入的以 模型 为单位的数据
     * @param clazz 模型的类
     * @param excelTypeEnum Excel的格式(XLS或XLSX)
     */
    public static void writeExcel(OutputStream outputStream, List<? extends DemoData> dataList,
                                  Class<? extends DemoData> clazz, ExcelTypeEnum excelTypeEnum) {
        String fileName = "writeExcel" + System.currentTimeMillis() + ".xlsx";
        EasyExcel.write(fileName,clazz).file(outputStream).excelType(excelTypeEnum).sheet("模板").doWrite(dataList);
    }

    /**
     * @Description: 使用模型来写入Excel，单sheet，单table（返回字节数组）
     * @param outputStream Excel的输出流
     * @param dataList 要写入的以 模型 为单位的数据
     * @param clazz 模型的类
     * @param excelTypeEnum Excel的格式(XLS或XLSX)
     * @Return: byte[]
     */
    public static byte[] writeExcel(ByteArrayOutputStream outputStream, List<? extends DemoData> dataList,
                                    Class<? extends DemoData> clazz, ExcelTypeEnum excelTypeEnum) {
        String fileName = "writeExcel" + System.currentTimeMillis() + ".xlsx";
        EasyExcel.write(fileName,clazz).file(outputStream).excelType(excelTypeEnum).sheet("模板").doWrite(dataList);
        return outputStream.toByteArray();
    }

    /**
     * @Description: 使用模型来写入Excel，多sheet，单table （返回字节数组）
     * @param outputStream Excel的输出流
     * @param sheetName  sheet名集合
     * @param datas  要写入的以 模型 为单位的数据
     * @param clazzs  模型的类
     * @param excelTypeEnum  Excel的格式(XLS或XLSX)
     * @Return: byte[]
     */
    public static byte[] writeExcel(ByteArrayOutputStream outputStream,List<String> sheetName,List<List<? extends DemoData>> datas,
                                    List<Class<? extends DemoData>> clazzs, ExcelTypeEnum excelTypeEnum) {
        if (sheetName.size()!=datas.size()||datas.size()!=clazzs.size()){
            throw new ArrayIndexOutOfBoundsException();
        }
        String fileName =  "repeatedWrite" + System.currentTimeMillis() + ".xlsx";
        ExcelWriter excelWriter = null;
        int i = 0;

        excelWriter = EasyExcel.write(fileName).file(outputStream).build();
        //写第一个sheet, sheet1  数据全是List<String> 无模型映射关系
        for (String name:sheetName){
            // 每次都要创建writeSheet 这里注意必须指定sheetNo 而且sheetName必须不一样。这里注意DemoData.class 可以每次都变
            // 实际上可以一直变
            WriteSheet writeSheet = EasyExcel.writerSheet(i, name).head(clazzs.get(i)).build();
            // 分页去数据库查询数据 这里可以去数据库查询每一页的数据
            List<? extends DemoData> data = datas.get(i);
            excelWriter.write(data, writeSheet);
        }
        return outputStream.toByteArray();
    }

    /**
     * @Description: 使用模型来写入Excel，多sheet，多table
     * @param outputStream  Excel的输出流
     * @param sheetAndTable sheet和table名，格式：<sheet名，<table名集合>>
     * @param data   <sheet名，<table名，table数据集>>
     * @param clazz  <sheet名，<table名，table数据集实体class类型>>
     * @param excelTypeEnum Excel的格式(XLS或XLSX)
     * @Return: byte[]
     */
    public static byte[] writeExcel(ByteArrayOutputStream outputStream,Map<String,List<String>> sheetAndTable,
                                    Map<String,Map<String,List<? extends DemoData>>> data,Map<String,Map<String,Class<? extends DemoData>>> clazz,
                                    ExcelTypeEnum excelTypeEnum) {

        String fileName =  "repeatedWrite" + System.currentTimeMillis() + ".xlsx";
        ExcelWriter excelWriter = EasyExcel.write(fileName).file(outputStream).build();
        Iterator<Map.Entry<String, List<String>>> iterator = sheetAndTable.entrySet().iterator();
        int sheetNo = 1;
        //遍历sheet
        while (iterator.hasNext()){
            Map.Entry<String, List<String>> sheet = iterator.next();
            //当前sheet名
            String sheetName = sheet.getKey();
            //当前sheet对应的table的实体类class对象集合
            Map<String, Class<? extends DemoData>> tableClasses = clazz.get(sheetName);
            //当前sheet对应的table的数据集合
            Map<String, List<? extends DemoData>> dataListMaps = data.get(sheetName);
            WriteSheet writeSheet = EasyExcel.writerSheet(sheetName).build();

            int tableNo = 1;
            Iterator<Map.Entry<String, Class<? extends DemoData>>> iterator1 = tableClasses.entrySet().iterator();
            //遍历table
            while (iterator1.hasNext()){
                Map.Entry<String, Class<? extends DemoData>> table = iterator1.next();
                //当前table名
                String tableName = table.getKey();
                //当前table对应的class
                Class<? extends DemoData> tableClass = table.getValue();
                //当前table对应的数据集
                List<? extends DemoData> tableData = dataListMaps.get(tableName);
                WriteTable writeTable = EasyExcel.writerTable(sheetNo).head(tableClass).build();
                excelWriter.write(tableData,writeSheet,writeTable);
                tableNo++;
            }
            sheetNo++;
        }
        return outputStream.toByteArray();
    }
}