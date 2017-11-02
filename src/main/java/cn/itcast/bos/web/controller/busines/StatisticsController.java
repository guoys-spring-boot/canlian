package cn.itcast.bos.web.controller.busines;

import cn.itcast.bos.domain.business.Person;
import cn.itcast.bos.domain.business.PlayMoneyStaticticsBean;
import cn.itcast.bos.service.EnumService;
import cn.itcast.bos.service.business.StatisticsService;
import cn.itcast.bos.utils.FileUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gys on 2017/8/27.
 */

@RequestMapping("/statistics")
@Controller
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @Autowired
    private EnumService enumService;


    @RequestMapping("playMoneyStatistics")
    @ResponseBody
    public Object playMoneyStatistics(Integer page, Integer rows, PlayMoneyStaticticsBean bean){
        rows = rows == null ? Integer.MAX_VALUE : rows;
        rows = rows <= 0 ? Integer.MAX_VALUE : rows;
        page = page < 0 ? 0 : page;
        PageHelper.startPage(page, rows);
        List<PlayMoneyStaticticsBean> people = statisticsService.playMoneyStatistics(bean);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("rows", people);
        result.put("total", statisticsService.countPlayMoneyStatistics(bean));
        return result;
    }

    @RequestMapping("exportPlayMoneyExcel")
    public void exportExcel(PlayMoneyStaticticsBean bean, HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<String> headerRow = new ArrayList<String>();
        headerRow.add("姓名");
        headerRow.add("性别");
        headerRow.add("残疾等级");
        headerRow.add("户口性质");
        headerRow.add("身份证号");
        headerRow.add("残疾证号");
        headerRow.add("银行号卡");
        headerRow.add("银行打款人");
        headerRow.add("金额");
        headerRow.add("月份");
        headerRow.add("地址");
        headerRow.add("区域");
        List<PlayMoneyStaticticsBean> people = statisticsService.playMoneyStatistics(bean);

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("打款情况表");

        HSSFRow row = sheet.createRow(0);
        int i = 0;
        for (String s : headerRow) {

            HSSFCell cell = row.createCell(i);
            sheet.autoSizeColumn(i);
            cell.setCellValue(s);
            i++;
        }

        Map<String, String> xingzhi = enumService.getEnum("xingzhi");

        int j = 1;
        DecimalFormat format = new DecimalFormat("##,##0.00");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        for (PlayMoneyStaticticsBean person : people) {
            HSSFRow row1 = sheet.createRow(j++);
            createStringCell(row1, 0, person.getName());
            createStringCell(row1, 1, person.getSex());
            createStringCell(row1, 2, person.getCanji());
            createStringCell(row1, 3, xingzhi.get(person.getXingzhi()));
            createStringCell(row1, 4, person.getIdentity());
            createStringCell(row1, 5, person.getDeformity());
            createStringCell(row1, 6, person.getBankid());
            createStringCell(row1, 7, person.getBankname());
            createStringCell(row1, 8, format.format(person.getMoney()));
            createStringCell(row1, 9, sdf.format(person.getDate()));
            createStringCell(row1, 10, person.getAddress());
            createStringCell(row1, 11, person.getQuyu());

        }

        response.setHeader("conent-type", "application/octet-stream");
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=" +
                FileUtils.encodeDownloadFilename("执行完成情况表.xls", request.getHeader("user-agent")));
        workbook.write(response.getOutputStream());
    }

    private void createStringCell(HSSFRow row, int index, String cellValue){
        HSSFCell cell = row.createCell(index);
        cell.setCellValue(cellValue);
    }
}
