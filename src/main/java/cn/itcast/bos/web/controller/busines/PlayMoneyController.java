package cn.itcast.bos.web.controller.busines;

import cn.itcast.bos.domain.Attachment;
import cn.itcast.bos.domain.business.PlayMoneyRecord;
import cn.itcast.bos.service.AttachmentService;
import cn.itcast.bos.service.business.PlayMoneyService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gys on 2017/8/28.
 */

@RequestMapping("/playmoney")
@Controller
public class PlayMoneyController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private PlayMoneyService playMoneyService;

    @Autowired
    private AttachmentService attachmentService;

    @RequestMapping("/listPlayMoney")
    @ResponseBody
    public Object listPlayMoney(@RequestParam("bankId") String bankId, Integer page, Integer rows){
        rows = rows == null ? Integer.MAX_VALUE : rows;
        rows = rows <= 0 ? Integer.MAX_VALUE : rows;
        page = page < 0 ? 0 : page;
        Page<Object> page1 = PageHelper.startPage(page, rows);
        List<PlayMoneyRecord> records = playMoneyService.listPlayMoney(bankId);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("rows", page1.getResult());
        result.put("total", page1.getTotal());
        return result;
    }
    @RequestMapping("/toListPlayMoney")
    public String toListPlayMoney(@RequestParam("bankId") String bankId, Model model){
        model.addAttribute("bankid", bankId);
        return "person/playmoneylist";
    }


    @RequestMapping("/importPlayMoney")
    @ResponseBody
    public String doImportPlayMoneyRecord(String fileId, String date, HttpSession session){
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
            playMoneyService.importExcel(bytes, date);
            return "导入成功";
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return "出错了，" + e.getMessage();
        }

    }
}
