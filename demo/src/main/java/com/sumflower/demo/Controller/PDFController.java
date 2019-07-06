package com.sumflower.demo.Controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.Font;
import com.sumflower.demo.dao.WorkFillDAO;
import com.sumflower.demo.model.Project;
import org.apache.tomcat.util.http.parser.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

@CrossOrigin
@Controller
public class PDFController{
    @Autowired
    WorkFillDAO workFillDAO;

    @RequestMapping(path = "/api/DownloadPDF")
    @ResponseBody
    public void pdfexport(HttpServletResponse response, @RequestParam("id") int id) {
        // 指定解析器
        System.setProperty("javax.xml.parsers.DocumentBuilderFactory",
                "com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl");
        String filename = "科技竞赛作品提交表.pdf";
        //int id = Integer.parseInt(m.get("id").toString());
        //int id = 12;
        Project p = workFillDAO.getInfo(id);
        if(p == null) return;
        try {
            //设置文件头：最后一个参数是设置下载文件名(这里我们叫：个人简历.pdf)
            response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(p.getProjectFullName()+ ".pdf", "UTF-8"));
            //response.setContentType("/application/pdf;charset=UTF-8");
            response.setContentType("application/pdf");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        OutputStream os = null;
        //FileOutputStream os = new FileOutputStream(filename);
        PdfStamper ps = null;
        PdfReader reader = null;
        try {
            os = response.getOutputStream(); // 生成的新文件路径 ，这里指页面
            /**
             * class.getResource("/") --> 返回class文件所在的顶级目录，一般为包名的顶级目录
             * 在这个目录中src/main/java和src/main/resources和src/test/java都是属于顶级目录
             * 这里pdf/就属于顶级目录下的子目录了
             *
             */
            // 2 读入pdf表单
            reader = new PdfReader(PDFController.class.getResource("/PDF/") + "科技作品申报表.pdf");

            // 3 根据表单生成一个新的pdf
            ps = new PdfStamper(reader, os);

            // 4 获取pdf表单中所有字段
            AcroFields form = ps.getAcroFields();

            // 5给表单添加中文字体 这里采用系统字体。不设置的话，中文可能无法显示
            //BaseFont bf = BaseFont.createFont("/Fonts/simsunb.TTF", BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);
            BaseFont bfChinese = BaseFont.createFont( "STSongStd-Light" ,"UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);
            //Font font = new Font(bfChinese, 10,Font.NORMAL);
            //form.addSubstitutionFont(font);
            //BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);

            // 6查询数据
            //int id = Integer.parseInt(m.get("id").toString());

            Map<String, Object> data = new HashMap<String, Object>();
            data.put("id", p.getId());
            data.put("projectName", p.getProjectName());
            data.put("college",p.getCollege());
            if(p.getCompetitionType() == 0){
                data.put("competitionType0","√");
            }
            else{
                data.put("competitionType1","√");
            }
            data.put("studentName",p.getStudentName());
            data.put("studentNumber",p.getStudentNumber());
            data.put("birthDay",p.getBirthDay());
            data.put("education",p.getEducation());
            data.put("major",p.getMajor());
            data.put("entryYear",p.getEntryYear());
            data.put("projectFullName",p.getProjectFullName());
            data.put("address",p.getAddress());
            data.put("phone",p.getPhone());
            data.put("email",p.getEmail());
            if(p.getProjectType() == 0){
                data.put("projectType","A");
            }else if(p.getProjectType() == 1){
                data.put("projectType","B");
            }else if(p.getProjectType() == 2){
                data.put("projectType","C");
            }else if(p.getProjectType() == 3){
                data.put("projectType","D");
            }else if(p.getProjectType() == 4){
                data.put("projectType","E");
            }else if(p.getProjectType() == 5){
                data.put("projectType","F");
            }
            data.put("details",p.getDetails());
            data.put("invention",p.getInvention());
            data.put("keywords",p.getKeywords());

            if(p.getAdditionalMessage()==null) {
                p.setAdditionalMessage("");
            }
            StringBuffer newAdditionMessage = new StringBuffer();
            StringBuffer oldAdditionMessage = new StringBuffer(p.getAdditionalMessage());
            oldAdditionMessage.insert(oldAdditionMessage.length()-1, ", ");
            oldAdditionMessage.insert(1, ", ");
            String str = oldAdditionMessage.toString();
            if(p.getCompetitionType()==0){
                if(str.contains(", 0,")) {
                    data.put("showFormat1","√");
                }
                if(str.contains(", 1,")) {
                    data.put("showFormat2","√");
                }
                if(str.contains(", 2,")) {
                    data.put("showFormat3","√");
                }
                if(str.contains(", 3,")) {
                    data.put("showFormat4","√");
                }
                if(str.contains(", 4,")) {
                    data.put("showFormat5","√");
                }
                if(str.contains(", 5,")) {
                    data.put("showFormat6","√");
                }
                if(str.contains(", 6,")) {
                    data.put("showFormat7","√");
                }
                if(str.contains(", 7,")) {
                    data.put("showFormat8","√");
                }
            }else{
                if(str.contains(", 0,")) {
                    data.put("researchFormat1","√");
                }
                if(str.contains(", 1,")) {
                    data.put("researchFormat2","√");
                }
                if(str.contains(", 2,")) {
                    data.put("researchFormat3","√");
                }
                if(str.contains(", 3,")) {
                    data.put("researchFormat4","√");
                }
                if(str.contains(", 4,")) {
                    data.put("researchFormat5","√");
                }
                if(str.contains(", 5,")) {
                    data.put("researchFormat6","√");
                }
                if(str.contains(", 6,")) {
                    data.put("researchFormat7","√");
                }
                if(str.contains(", 7,")) {
                    data.put("researchFormat8","√");
                }
                if(str.contains(", 8,")) {
                    data.put("researchFormat9","√");
                }
                if(str.contains(", 9,")) {
                    data.put("researchFormat10","√");
                }
                if(str.contains(", 10,")) {
                    data.put("researchFormat11","√");
                }
                if(str.contains(", 11,")) {
                    data.put("researchFormat12","√");
                }
                if(str.contains(", 12,")) {
                    data.put("researchFormat13","√");
                }
                if(str.contains(", 13,")) {
                    data.put("researchFormat14","√");
                }
                if(str.contains(", 14,")) {
                    data.put("researchFormat15","√");
                }
            }


                //Font font = new Font(bf, 12, Font.NORMAL);
            // 7遍历data 给pdf表单表格赋值
            for (String key : data.keySet()) {

                form.setFieldProperty(key, "textfont",bfChinese, null);// 设置字体
                form.setField(key, data.get(key).toString());
            }

            // 如果为false那么生成的PDF文件还能编辑，一定要设为true
            ps.setFormFlattening(true);

            System.out.println("===============PDF导出成功=============");
        } catch (Exception e) {
            System.out.println("===============PDF导出失败=============");
            e.printStackTrace();
        } finally {
            try {
                ps.close();
                reader.close();
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}