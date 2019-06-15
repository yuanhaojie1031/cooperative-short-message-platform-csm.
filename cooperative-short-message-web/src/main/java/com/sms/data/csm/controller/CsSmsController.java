package com.sms.data.csm.controller;

import com.alibaba.fastjson.JSON;
import com.sms.data.csm.BaseController;
import com.sms.data.csm.common.BaseResult;
import com.sms.data.csm.common.ResultCode;
import com.sms.data.csm.po.CsSmsPo;
import com.sms.data.csm.service.CsSmsService;
import com.sms.data.csm.service.impl.CsSmsServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 短信入口
 * 2019-05-21
 */
@Api(tags="短信接口")
@Controller
@RequestMapping("/csSms")
public class CsSmsController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(CsSmsServiceImpl.class);

    private final static String excel2003L =".xls";    //2003- 版本的excel
    private final static String excel2007U =".xlsx";   //2007+ 版本的excel

    private CsSmsService smsService;

    @Autowired
    public CsSmsController(CsSmsService smsService){
        this.smsService = smsService;
    }

    @ApiOperation(value = "消息发送页面", notes = "消息发送页面")
    @RequestMapping(value = "/sms", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult sms(@RequestBody CsSmsPo csSmsPo, HttpServletRequest request) {
        HttpSession session=request.getSession();
        csSmsPo.setUserId(Integer.parseInt(String.valueOf(session.getAttribute("userId"))));
        return new BaseResult(ResultCode.Success, 0,smsService.SendCsSms(csSmsPo));
    }

    @ApiOperation(value = "Excel消息发送页面", notes = "Excel消息发送页面")
    @RequestMapping(value = "/excelSms", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult excelSms(@RequestParam("smsFile") MultipartFile smsFile, HttpServletRequest request) {
        try {
            HttpSession session=request.getSession();
            String fileType = smsFile.getOriginalFilename().substring(smsFile.getOriginalFilename().lastIndexOf("."));
            InputStream inputStream = smsFile.getInputStream();
            Workbook wb;
            Sheet sheetAt;
            if(excel2003L.equals(fileType)){
                wb = new HSSFWorkbook(inputStream);//2003-
                sheetAt = wb.getSheetAt(0);
            }else if(excel2007U.equals(fileType)){
                wb = new XSSFWorkbook(inputStream);//2007+
                sheetAt = wb.getSheetAt(0);
            }else{
                throw new Exception("解析的文件格式有误！");
            }
            int rowNum = sheetAt.getLastRowNum() + 1;
            List<CsSmsPo> csSmsPos=new ArrayList<>();
            for (int i = 0; i < rowNum; i++) {  //行循环开始
                Row row = sheetAt.getRow(i); //行
                CsSmsPo csSmsPo=new CsSmsPo();
                csSmsPo.setCsPhone(getCellValue(row.getCell(0)));
                csSmsPo.setCsName(getCellValue(row.getCell(1)));
                csSmsPo.setTemplateCode(getCellValue(row.getCell(2)));
                csSmsPo.setPublicPhone(getCellValue(row.getCell(3)));
//                csSmsPo.setUserId(Integer.parseInt(String.valueOf(session.getAttribute("userId"))));
                csSmsPos.add(csSmsPo);
            }
//            smsService.SendBatchSms(csSmsPos);
            for (CsSmsPo csSmsPo : csSmsPos) {
                logger.info("实体类："+ JSON.toJSONString(csSmsPo));
                smsService.SendBatchCsSms(csSmsPo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new BaseResult(ResultCode.Success, 0);
    }

    @ApiOperation(value = "查询消息模板", notes = "查询消息模板")
    @RequestMapping(value = "/selectCsTemplateCodeAll", method = RequestMethod.GET)
    @ResponseBody
    public BaseResult selectCsTemplateCodeAll() {
        return new BaseResult(ResultCode.Success, 0,smsService.selectCsTemplateCodeAll());
    }

    @ApiOperation(value = "查询消息模板", notes = "查询消息模板")
    @RequestMapping(value = "/selectCsTemplateCode", method = RequestMethod.GET)
    @ResponseBody
    public BaseResult selectCsTemplateCode(@RequestParam("templateCode") String templateCode) {
        return new BaseResult(ResultCode.Success, 0,smsService.selectCsTemplateCode(templateCode));
    }


    /**
     * 描述：对表格中数值进行格式化
     * @param cell
     * @return
     */
    private   String getCellValue(Cell cell){
        //用String接收所有返回的值
        String value;
        DecimalFormat df = new DecimalFormat("0");  //格式化number String字符
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");  //日期格式化
        DecimalFormat df2 = new DecimalFormat("0.00");  //格式化数字

        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:  //String类型的数据
                value =  cell.getStringCellValue();
                break;
            case Cell.CELL_TYPE_NUMERIC:   //数值类型(取值用cell.getNumericCellValue() 或cell.getDateCellValue())
                if("General".equals(cell.getCellStyle().getDataFormatString())){
                    value = df.format(cell.getNumericCellValue());
                }else if(HSSFDateUtil.isCellDateFormatted(cell)){
                    value = sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
                }else{
                    value = df2.format(cell.getNumericCellValue());
                }
                break;
            case Cell.CELL_TYPE_BOOLEAN:  //Boolean类型
                value = String.valueOf(cell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_FORMULA: //表达式类型
                value = String.valueOf(cell.getCellFormula());
                break;
            case Cell.CELL_TYPE_ERROR: //异常类型 不知道何时算异常
                value=String.valueOf(cell.getErrorCellValue());
                break;
            case Cell.CELL_TYPE_BLANK:  //空，不知道何时算空
                value = "";
                break;
            default:
                value = "";
                break;
        }
        if(value.equals("")||value==null){
            value = "";
        }
        if (cell == null) {
            return "";
        }
        return value.replace(" ", "");
    }
}
