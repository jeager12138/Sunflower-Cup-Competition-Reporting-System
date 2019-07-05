package com.sumflower.demo.Controller;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import com.sumflower.demo.dao.UploadFileDao;
import com.sumflower.demo.dao.WorkFillDAO;
import com.sumflower.demo.model.HostHolder;
import com.sumflower.demo.model.LoginTicket;
import com.sumflower.demo.model.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin
@Controller
public class UploadFileController {
    @Autowired
    HostHolder hostHolder;

    @Autowired
    UploadFileDao uploadFileDao;

    @Autowired
    WorkFillDAO workFillDAO;

    @RequestMapping(path = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public String upload(@RequestParam("id") int id , @RequestParam("file") MultipartFile file) throws IOException {// 文件上传

        System.out.println(id);

        LoginTicket loginTicket = hostHolder.getLoginTicket();
        String filename;
        System.out.println(loginTicket.getUserType());
        if(loginTicket.getUserType() == 0)//student
        {

            filename = "student_" + loginTicket.getUserId() + "_" + file.getOriginalFilename();
        }else if(loginTicket.getUserType() == 2)//committee
        {
            filename = "committee" + "_" + file.getOriginalFilename();
        }else
        {
            filename = "expert_" + file.getOriginalFilename();
        }
        /*本地测试
        BufferedOutputStream outputStream = new BufferedOutputStream(
                new FileOutputStream(new File(filename)));
        */

        BufferedOutputStream outputStream =
                new BufferedOutputStream(new FileOutputStream
                        (new File("/var/www/html/uploadfile/"+filename)));
        String FileUrl = "http://liuterry.cn/uploadfile/"+filename; //下载url 文档：pdf，图片：jpg，视频：mp4
        String docUrl = "" , picUrl = "" , videoUrl = "";
        if (FileUrl.endsWith("pdf") | FileUrl.endsWith("PDF")) {
            docUrl = FileUrl + ";";
        }else if(FileUrl.endsWith("png") | FileUrl.endsWith("PNG"))
        {
            picUrl = FileUrl + ";";
        }else if(FileUrl.endsWith("mp4") | FileUrl.endsWith("MP4"))
        {
            videoUrl = FileUrl + ";";
        }

        Project project = new Project();
        project.setDocUrl(docUrl);
        project.setPicUrl(picUrl);
        project.setVideoUrl(videoUrl);
        project.setId(id);
        //project.setId(Integer.parseInt(id.toString()));
        uploadFileDao.updateFileUrl(project);

        outputStream.write(file.getBytes());
        outputStream.flush();
        outputStream.close();
        return "Finished";
    }

    @RequestMapping(path = "/deleteFile", method = RequestMethod.POST)
    @ResponseBody
    public String delete(@RequestParam("id") int id, @RequestParam("filename") String filename)
    {
        System.out.println(id);

        LoginTicket loginTicket = hostHolder.getLoginTicket();
        String filetodelete;
        System.out.println(loginTicket.getUserType());
        if(loginTicket.getUserType() == 0)//student
        {

            filetodelete = "http://liuterry.cn/uploadfile/" + "student_" + loginTicket.getUserId() + "_" + filename + ";";
        }else if(loginTicket.getUserType() == 2)//committee
        {
            filetodelete = "http://liuterry.cn/uploadfile/" + "committee" + "_" + filename + ";";
        }else
        {
            filetodelete = "http://liuterry.cn/uploadfile/" + "expert_" + filename + ";";
        }

        Project project = new Project();
        project = workFillDAO.getInfo(id);
        String docUrltodelete = project.getDocUrl();
        String picUrltodelete = project.getPicUrl();
        String videoUrltodelete = project.getVideoUrl();

        Project newproject = new Project();
        newproject.setId(id);
        if (filetodelete.endsWith("pdf") | filetodelete.endsWith("PDF")) {
            String newdocUrl = deleteSubString(docUrltodelete, filetodelete );
            newproject.setDocUrl(newdocUrl);
        }else if(filetodelete.endsWith("png") | filetodelete.endsWith("PNG"))
        {
            String newpicUrl = deleteSubString(picUrltodelete, filetodelete);
            newproject.setPicUrl(newpicUrl);
        }else if(filetodelete.endsWith("mp4") | filetodelete.endsWith("MP4"))
        {
            String newvideoUrl = deleteSubString(videoUrltodelete, filetodelete);
            newproject.setVideoUrl(newvideoUrl);
        }

        uploadFileDao.updateFileUrl(newproject);
        return "File deleted";
    }

    public String deleteSubString(String str1,String str2) {
        StringBuffer sb = new StringBuffer(str1);
        int delCount = 0;
        String newstr = "";

        while (true) {
            int index = sb.indexOf(str2);
            if(index == -1) {
                break;
            }
            sb.delete(index, index+str2.length());
            delCount++;
        }
        newstr = sb.toString();
        return newstr;
    }
}
