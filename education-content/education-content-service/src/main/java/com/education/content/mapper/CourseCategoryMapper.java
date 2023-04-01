package com.education.content.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.education.content.model.dto.CourseCategoryTreeDto;
import com.education.content.model.po.CourseCategory;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 课程分类 Mapper 接口
 * </p>
 *
 * @author itcast
 */
@Repository
public interface CourseCategoryMapper extends BaseMapper<CourseCategory> {
    public List<CourseCategoryTreeDto> selectTreeNodes(String id);
}
