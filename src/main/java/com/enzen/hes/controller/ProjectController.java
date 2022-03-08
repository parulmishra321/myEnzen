package com.enzen.hes.controller;

import com.enzen.hes.handler.response.ApiResponseCode;
import com.enzen.hes.handler.response.ResponseDTO;
import com.enzen.hes.handler.response.ResponseUtil;
import com.enzen.hes.service.ProjectService;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/projects")
public class ProjectController {
    @Autowired
    private ResponseUtil responseUtil;

    @Autowired
    private ProjectService projectService;

    @PostMapping
    public ResponseDTO<?> createProject(@RequestBody Document projectRequest) throws Exception {
        projectService.createProject(projectRequest);
        return responseUtil.ok(ApiResponseCode.SUCCESS);
    }

    @GetMapping
    public ResponseDTO<?> getAllProjects() throws Exception {
        var result = projectService.getAllProjects();
        return responseUtil.ok(result, ApiResponseCode.SUCCESS);
    }

    @DeleteMapping("{id}")
    public ResponseDTO<?> deleteProject(@PathVariable String id) throws Exception {
        projectService.deleteProject(id);
        return responseUtil.ok(ApiResponseCode.SUCCESS);
    }

    @PutMapping("{id}")
    public ResponseDTO<?> updateProject(@RequestBody Document projectRequest, @PathVariable String id) throws Exception {
        projectService.updateProject(projectRequest, id);
        return responseUtil.ok(ApiResponseCode.SUCCESS);
    }
}
