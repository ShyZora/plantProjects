package com.education.content.api;


import com.education.content.model.dto.CourseCategoryTreeDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @program: onlineEducation
 * @description: 分类
 * @author: ShyZOra
 * @create: 2023-03-24 16:26
 **/
@RestController
public class CourseCategoryController {
    @GetMapping("/course-category/tree-nodes")
    public List<CourseCategoryTreeDto> queryTreeNodes() {
        return null;
    }


}
