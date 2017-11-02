package cn.itcast.bos.web.controller.busines;

import cn.itcast.bos.domain.Attachment;
import cn.itcast.bos.domain.business.Person;
import cn.itcast.bos.domain.business.PlayMoneyStaticticsBean;
import cn.itcast.bos.service.AttachmentService;
import cn.itcast.bos.service.EnumService;
import cn.itcast.bos.service.business.PersonService;
import cn.itcast.bos.utils.FileUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gys on 2017/8/26.
 */

@Controller
@RequestMapping("/business")
public class PersonController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private PersonService personService;

    @Autowired
    private EnumService enumService;

    @Autowired
    private AttachmentService attachmentService;

    @RequestMapping("/listperson")
    @ResponseBody
    public Object listPerson(Person person, Integer page, Integer rows){
        rows = rows == null ? Integer.MAX_VALUE : rows;
        rows = rows <= 0 ? Integer.MAX_VALUE : rows;
        page = page < 0 ? 0 : page;
        Page<Object> page1 = PageHelper.startPage(page, rows);
        List<Person> people = personService.listAllPerson(person);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("rows", people);
        result.put("total", page1.getTotal());
        return result;
    }

    @RequestMapping("/exportperson")
    public void exportPerson(Person person1, HttpServletRequest request, HttpServletResponse response) throws IOException {

        List<String> headerRow = new ArrayList<String>();
        headerRow.add("姓名");
        headerRow.add("性别");
        headerRow.add("身份证号");
        headerRow.add("残疾证号");
        headerRow.add("银行号卡");
        headerRow.add("地址");
        headerRow.add("手机号");
        headerRow.add("类型");
        headerRow.add("残疾");
        headerRow.add("户口性质");
        headerRow.add("状态");
        headerRow.add("备注");
        headerRow.add("区域");
        headerRow.add("是否重复");

        List<Person> people = personService.listAllPerson(person1);
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("人员表");
        HSSFRow row = sheet.createRow(0);
        int i = 0;
        for (String s : headerRow) {
            HSSFCell cell = row.createCell(i);
            sheet.autoSizeColumn(i);
            cell.setCellValue(s);
            i++;
        }

        Map<String, String> zhuangtai = enumService.getEnum("zhuangtai");
        Map<String, String> xingzhi = enumService.getEnum("xingzhi");
        Map<String, String> leixing = enumService.getEnum("leixing");

        int j = 1;
        for (Person person : people) {
            HSSFRow row1 = sheet.createRow(j++);
            createStringCell(row1, 0, person.getName());
            createStringCell(row1, 1, person.getSex());
            createStringCell(row1, 2, person.getIdentity());
            createStringCell(row1, 3, person.getDeformity());
            createStringCell(row1, 4, person.getBankid());
            createStringCell(row1, 5, person.getAddress());
            createStringCell(row1, 6, person.getPhonenumber());
            createStringCell(row1, 7, leixing.get(person.getLeixing()));
            createStringCell(row1, 8, person.getCanji());
            createStringCell(row1, 9, xingzhi.get(person.getXingzhi()));
            createStringCell(row1, 10 ,zhuangtai.get(person.getZhuangtai()));
            createStringCell(row1, 11, person.getBeizhu());
            createStringCell(row1, 12, person.getQuyu());
            createStringCell(row1, 13, person.getRepeatFlag());

        }

        response.setHeader("conent-type", "application/octet-stream");
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=" +
                FileUtils.encodeDownloadFilename("人员表.xls", request.getHeader("user-agent")));
        workbook.write(response.getOutputStream());

    }

    private void createStringCell(HSSFRow row, int index, String cellValue){
        HSSFCell cell = row.createCell(index);
        cell.setCellValue(cellValue);
    }

    @RequestMapping("/toAddPerson")
    public String toAddPerson(Model model){
        model.addAttribute("action", "add");
        Person person = new Person();
        model.addAttribute("person", person);
        return "person/personinfo";
    }

    @RequestMapping("/addPerson")
    public String doAddPerson(Person person){
        personService.insert(person);
        return "common/close";
    }

    @RequestMapping("/toUpdatePerson")
    public String toUpdatePerson(Model model, String personId){
        model.addAttribute("action", "edit");
        Person person = personService.getById(personId);
        model.addAttribute("person", person);
        return "person/personinfo";
    }


    @RequestMapping("/updatePerson")
    public String doUpdatePerson(Person person){
        personService.update(person);
        return "common/close";
    }

    @RequestMapping("/deletePerson")
    @ResponseBody
    public void doDeletePerson(String personId){
        personService.delete(personId);
    }

    @RequestMapping("/importExcel")
    @ResponseBody
    public String doImportPerson(@RequestParam("fileId") String fileId,
                                 @RequestParam("uploadDate")String uploadDate, HttpSession session){
        Attachment attachment = attachmentService.findById(fileId);
        if(attachment == null){
            return "出错了， 请重新上传文件";
        }
        String realPath = session.getServletContext().getRealPath("/");
        File file = new File(realPath + "/" + attachment.getUri());
        if(!file.exists()){
            return "出错了， 请重新上传文件";
        }
        try {
            byte[] bytes = IOUtils.toByteArray(new FileInputStream(file));
            personService.importExcel(bytes, uploadDate);
            return "导入成功";
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return "出错了，" + e.getMessage();
        }
    }

    @RequestMapping("/listAllArea")
    @ResponseBody
    public Object listAllArea(){
        List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
        List<String> strings = personService.listAllArea();
        for (String s : strings) {
            Map<String, Object> map = new HashMap<String, Object>(2);
            map.put("id", s);
            map.put("text", s);
            results.add(map);
        }
        return results;
    }

    @ResponseBody
    @RequestMapping("/compareBankId")
    public Object doCompareBankId(String fileId, HttpSession session){
        Attachment attachment = attachmentService.findById(fileId);
        if(attachment == null){
            return "出错了， 请重新上传文件";
        }
        String realPath = session.getServletContext().getRealPath("/");
        File file = new File(realPath + "/" + attachment.getUri());
        if(!file.exists()){
            return "出错了， 请重新上传文件";
        }

        try {
            byte[] bytes = IOUtils.toByteArray(new FileInputStream(file));
            List<String> lackBankIds = personService.getLackBankId(bytes);
            List<Map<String, String>> result = new ArrayList<Map<String, String>>();
            for (String lackBankId : lackBankIds) {
                Map<String, String> each = new HashMap<String, String>();
                each.put("bankid", lackBankId);
                result.add(each);
            }
            return result;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return "出错了，" + e.getMessage();
        }
    }
}
