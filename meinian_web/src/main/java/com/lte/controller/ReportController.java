package com.lte.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lte.constant.MessageConstant;
import com.lte.entity.Result;
import com.lte.service.MemberService;
import com.lte.service.ReportService;
import com.lte.service.SetmealService;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Auther: laite
 * @Date: 2021/8/16 - 08 - 16 - 10:47
 * @Description: com.lte.controller
 * @version: 1.0
 */
@RestController
@RequestMapping("/report")
public class ReportController {
    @Reference
    MemberService memberService;

    @Reference
    SetmealService setmealService;

    @Reference
    ReportService reportService;

    @RequestMapping("/getMemberReport")
    public Result getMemberReport(){
        try {
            /**
             * @Description 获取各个月份(前12个月)会员的数量
             * @Param
             * @return com.lte.entity.Result
             **/
            //1- 获取日历对象
            Calendar calendar = Calendar.getInstance();
            //根据当前时间，获取前12个月的日历(当前日历2021-08，12个月前，日历时间2020-09)
            //第一个参数，日历字段
            //第二个参数，要添加到字段中的日期或时间
            //当月月份往前偏移12月份
            calendar.add(Calendar.MONTH,-12);
            List<String> list = new ArrayList<>();
            for (int i = 0; i <12; i++) {
                //循环偏移12次，每次偏移+1月份
                calendar.add(Calendar.MONTH,1);
                Date date = calendar.getTime();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
                String format = sdf.format(date);
                list.add(format);
            }
            Map<String,List> map = new HashMap<>();
            //把过去12个月的集合放入map中
            map.put("months",list);
            //获取过去12个月中每个月份的会员人数
            List<Integer> count = memberService.findMemberCountByMonth(list);
            map.put("memberCount",count);
            return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }
    }

    @RequestMapping("/getSetmealReport")
    public Result getSetmealReport(){
        /*接口参数：
        * setmealNames：套餐名称
        * setmealCount：套餐名称+数量
        * */
        Map map = new HashMap();
        List<String> listNames = new ArrayList<>();

        //连表查询(t_order&t_setmeal)根据在order表中查询每个套餐的数量
       List<Map<String,Object>> listCount =  setmealService.findSetmealCount();
        for (Map<String, Object> mapCount : listCount) {
            String name = (String)mapCount.get("name");
            listNames.add(name);
        }
        map.put("setmealNames", listNames);
        map.put("setmealCount", listCount);
        return new Result(true,MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,map);
    }

    @RequestMapping("/getBusinessReportData")
    public Result getBusinessReportData(){
        try {
            Map<String,Object> map = reportService.getBusinessReportData();
            /*  参数：
            *   map.put(String,Integer)
            *   map.put(String,List<map<String,Integer>>)
            */

            return new Result(true,MessageConstant.GET_BUSINESS_REPORT_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }

    }
    @RequestMapping("/exportBusinessReport")
    public void exportBusinessReport(HttpServletResponse response, HttpServletRequest request){
        try {
            //1- 远程调用报表服务获取报表数据
            Map<String, Object> businessReportData = reportService.getBusinessReportData();
            Integer todayNewMember = (Integer) businessReportData.get("todayNewMember");
            Integer totalMember = (Integer) businessReportData.get("totalMember");
            Integer thisWeekNewMember = (Integer) businessReportData.get("thisWeekNewMember");
            Integer thisMonthNewMember = (Integer) businessReportData.get("thisMonthNewMember");
            Integer todayOrderNumber = (Integer) businessReportData.get("todayOrderNumber");
            Integer thisWeekOrderNumber = (Integer) businessReportData.get("thisWeekOrderNumber");
            Integer thisMonthOrderNumber = (Integer) businessReportData.get("thisMonthOrderNumber");
            Integer todayVisitsNumber = (Integer) businessReportData.get("todayVisitsNumber");
            Integer thisWeekVisitsNumber = (Integer) businessReportData.get("thisWeekVisitsNumber");
            Integer thisMonthVisitsNumber = (Integer) businessReportData.get("thisMonthVisitsNumber");
            List<Map> hotSetmeal = (List<Map>) businessReportData.get("hotSetmeal");

            //file separator这个代表系统中的间隔符
            //2- 获得Excel模板文件的路径
            String realPath = request.getSession().getServletContext().getRealPath("template");
            String excelPath = realPath + File.separator + "report_template.xlsx";
            //3- 获取模板文件的Excel表格对象
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(excelPath));
            XSSFSheet sheet = workbook.getSheetAt(0);

            XSSFRow row = sheet.getRow(2);
            XSSFCell cell = row.getCell(5);//日期

            row = sheet.getRow(4);
            row.getCell(5).setCellValue(todayNewMember);//新增会员数（本日）
            row.getCell(7).setCellValue(totalMember);//总会员数

            row = sheet.getRow(5);
            row.getCell(5).setCellValue(thisWeekNewMember);//本周新增会员数
            row.getCell(7).setCellValue(thisMonthNewMember);//本月新增会员数

            row = sheet.getRow(7);
            row.getCell(5).setCellValue(todayOrderNumber);//今日预约数
            row.getCell(7).setCellValue(todayVisitsNumber);//今日出游数

            row = sheet.getRow(8);
            row.getCell(5).setCellValue(thisWeekOrderNumber);//本周预约数
            row.getCell(7).setCellValue(thisWeekVisitsNumber);//本周出游数

            row = sheet.getRow(9);
            row.getCell(5).setCellValue(thisMonthOrderNumber);//本月预约数
            row.getCell(7).setCellValue(thisMonthVisitsNumber);//本月出游数

            int rowNum = 12;
            for (Map map : hotSetmeal) {
                String name = (String)map.get("name");
                Long count = (Long)map.get("setmeal_count");
                BigDecimal proportion = (BigDecimal)map.get("proportion");
                row = sheet.getRow(rowNum ++);
                row.getCell(4).setCellValue(name);//套餐名称
                row.getCell(5).setCellValue(count);//预约数量
                row.getCell(6).setCellValue(proportion.doubleValue());//占比
            }
            //4- 通过输出流进行文件下载
            ServletOutputStream outputStream = response.getOutputStream();
            // 下载的数据类型（excel类型）
            response.setContentType("application/vnd.ms-excel");
            // 设置下载形式(通过附件的形式下载)
            response.setHeader("content-Disposition", "attachment;filename=report.xlsx");
            workbook.write(outputStream);

            outputStream.flush();
            outputStream.close();
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
            try {
                request.getRequestDispatcher("/pages/error/export_excel_fail.html").forward(request,response);
            } catch (ServletException servletException) {
                servletException.printStackTrace();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}
