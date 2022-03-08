package com.enzen.hes.service;

import com.enzen.hes.handler.exception.ValidationException;
import com.enzen.hes.mongo.service.MongoService;
import com.enzen.hes.utils.CommonUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.enzen.hes.mongo.constants.MongoConstants.*;

@Service
public class ProjectService {

    @Autowired
    private MongoService mongoService;

    public void createProject(Document projectRequest) {
        projectRequest.put(_ID, CommonUtils.generateUUID());
        mongoService.create(PROJECT_COLLECTION, projectRequest);
    }

    public List<Document> getAllProjects() {
        var projection = new Document();
        var result = mongoService.findAll(PROJECT_COLLECTION, projection);
        if (result.isEmpty()) {
            throw new ValidationException(HttpStatus.BAD_REQUEST.value(), "Project details does not exists");
        }
        return result;
    }

    public void deleteProject(String id) {
        var query = new Document();
        query.put(_ID, id);
        var result = mongoService.deleteOne(PROJECT_COLLECTION, query);
        if (result.getDeleteCount() == 0) {
            throw new ValidationException(HttpStatus.BAD_REQUEST.value(), "Project details does not exists");
        }
    }

    public void updateProject(Document projectRequest, String id) {
        var query = new Document();
        query.put(_ID, id);
        var result = mongoService.update(PROJECT_COLLECTION, query, projectRequest, null, null, null, true);
        if (result.getUpdateCount() == 0) {
            throw new ValidationException(HttpStatus.BAD_REQUEST.value(), "Project details does not exists");
        }
    }
}
