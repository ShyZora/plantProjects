package com.education.content.service;

import com.education.base.model.PageParams;
import com.education.base.model.PageResult;
import com.education.content.model.dto.AddCourseDto;
import com.education.content.model.dto.CourseBaseInfoDto;
import com.education.content.model.dto.EditCourseDto;
import com.education.content.model.dto.QueryCourseParamsDto;
import com.education.content.model.po.CourseBase;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @program: onlineEducation
 * @description: 课程信息查询接口
 * @author: ShyZOra
 * @create: 2023-03-19 18:42
 **/

public interface CourseBaseInfoService {
    public PageResult<CourseBase> queryCourseBaseList(PageParams pageParams, QueryCourseParamsDto queryCourseParamsDto);
    public CourseBaseInfoDto createCourseBase(Long companyId, AddCourseDto dto);
    public CourseBaseInfoDto getCourseBaseInfo(Long courseId);
    public CourseBaseInfoDto updateCourseBase(Long companyId, EditCourseDto dto);
}
