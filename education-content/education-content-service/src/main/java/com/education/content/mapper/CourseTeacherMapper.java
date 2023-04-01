package com.education.content.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.education.content.model.dto.TeachplanDto;
import com.education.content.model.po.CourseTeacher;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 课程-教师关系表 Mapper 接口
 * </p>
 *
 * @author itcast
 */
public interface CourseTeacherMapper extends BaseMapper<CourseTeacher> {
    /**
     * @description 查询某课程的课程计划，组成树型结构
     * @param courseId
     * @return com.xuecheng.content.model.dto.TeachplanDto
     */

}
