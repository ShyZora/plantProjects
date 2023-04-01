package com.education.content.model.dto;

import com.education.content.model.po.CourseCategory;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @program: onlineEducation
 * @description: 课程分类
 * @author: ShyZOra
 * @create: 2023-03-24 16:20
 **/
@Data
public class CourseCategoryTreeDto extends CourseCategory implements Serializable {
    List<CourseCategoryTreeDto> childrenTreeNodes;
}
