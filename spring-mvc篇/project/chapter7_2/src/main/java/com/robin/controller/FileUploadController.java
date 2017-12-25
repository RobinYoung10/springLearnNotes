package com.robin.controller;

import com.robin.domain.User;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

@Controller
public class FileUploadController {
    @RequestMapping(value = "/{pathName}", method = RequestMethod.GET)
    public String pathName(@PathVariable String pathName) {
        return pathName;
    }

    @RequestMapping(value = "/register")
    public String register(HttpServletRequest request,
                           @ModelAttribute User user,
                           Model model) throws Exception {
        System.out.println(user.getUsername());
        if(!user.getImage().isEmpty()) {
            //写入文件的路径,获取到的是部署到webapps的项目根目录+/images/
            String path = request.getServletContext().getRealPath("/images/");
            //获取文件名
            String filename = user.getImage().getOriginalFilename();
            //新建File对象
            File filepath = new File(path, filename);
            //如果路径不存在,就创建一个
            if(!filepath.getParentFile().exists()) {
                filepath.getParentFile().mkdir();
            }
            //将文件保存写入到一个目标文件中
            user.getImage().transferTo(new File(path + filename));
            System.out.println(path + filename);
            model.addAttribute("user", user);
            return "userInfo";
        } else {
            return "error";
        }
    }

    @RequestMapping(value = "/download")
    public ResponseEntity<byte[]> download(HttpServletRequest request,
                                           @RequestParam("filename") String filename,
                                           Model model) throws Exception {
        //下载的文件路径
        String path = request.getServletContext().getRealPath("/images/");
        //File对象
        File file = new File(path + filename);
        //HttpHeaders对象
        HttpHeaders headers = new HttpHeaders();
        //下载显示的文件名,已解决中文乱码问题
        String downloadFileName = new String(filename.getBytes("UTF-8"), "iso-8859-1");
        //通知浏览器以attachment的下载方式打开图片
        headers.setContentDispositionFormData("attachment", downloadFileName);
        //application/octet-stream:二进制流数据(最常见的文件下载)
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        //201 HttpStatus.CREATED
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
    }
}
