package com.sumflower.demo.dao;

import com.sumflower.demo.model.LoginTicket;
import com.sumflower.demo.model.Project;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WorkFillDAO {
    String TABLE_NAME = "Project";
    String INSERT_FILEDS = " projectName,college,competitionType,studentName,studentNumber,birthDay,education,major," +
            "entryYear,projectFullName,address,phone,email,friends,projectType,details,invention,keywords,picUrl," +
            "docUrl,videoUrl,averageScore,submitStatus " ;


    @Insert({" insert into ", TABLE_NAME, "(", INSERT_FILEDS,
    ") values ( #{projectName}, #{college}, #{competitionType}, #{studentName}, #{studentNumber}, #{birthDay}, " +
            "#{education}, #{major}, #{entryYear}, #{projectFullName}, #{address}, #{phone}, #{email}," +
            " #{friends}, #{projectType}, #{details}, #{invention}, #{keywords}, " +
            "#{picUrl}, #{docUrl}, #{videoUrl}, #{averageScore}, #{submitStatus} )"})
    int addProject(Project project);

}
