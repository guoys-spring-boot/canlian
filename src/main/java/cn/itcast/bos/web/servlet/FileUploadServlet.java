package cn.itcast.bos.web.servlet;



import cn.itcast.bos.domain.Attachment;
import cn.itcast.bos.service.AttachmentService;
import cn.itcast.bos.utils.FileUtils;
import cn.itcast.bos.utils.UUIDUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;

import javax.servlet.ServletConfig;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * Created by gys on 2017/5/26.
 */

@WebServlet(name = "fileUploadServlet", urlPatterns = "/file/upload")
public class FileUploadServlet extends HttpServlet {

    private MultipartResolver multipartResolver;

    private Logger logger = LoggerFactory.getLogger(getClass());

    private AttachmentService attachmentService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(config.getServletContext());
        attachmentService = applicationContext.getBean(AttachmentService.class);
        multipartResolver = applicationContext.getBean(MultipartResolver.class);
    }


    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.debug("开始上传文件");
        response.setContentType("text/json;charset=utf-8");
        if(!multipartResolver.isMultipart(request)){
            response.getWriter().write("无效的访问");
            return;
        }
        MultipartHttpServletRequest multipart = multipartResolver.resolveMultipart(request);
        for (Map.Entry<String, MultipartFile> entry : multipart.getFileMap().entrySet()) {
            MultipartFile file = entry.getValue();
            Attachment attachment = new Attachment();
            String id = UUIDUtils.generatePrimaryKey();
            String actualName = id + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            String rootpath = request.getSession().getServletContext().getRealPath("/");
            String path = FileUtils.generateDir("upload");

            File realFile = new File(rootpath + "/" + path + actualName);
            if(!realFile.exists()){
                if(!realFile.getParentFile().exists()){
                    realFile.getParentFile().mkdirs();
                }
                realFile.createNewFile();
            }
            FileOutputStream outputStream = new FileOutputStream(realFile);
            outputStream.write(file.getBytes());
            outputStream.flush();
            outputStream.close();

            attachment.setId(id);
            attachment.setForeignKey(null);
            attachment.setName(file.getOriginalFilename());
            attachment.setUri(path + actualName);
            attachmentService.insert(attachment);
            ObjectMapper mapper = new ObjectMapper();
            response.getWriter().write(mapper.writeValueAsString(attachment));
        }

    }
}
