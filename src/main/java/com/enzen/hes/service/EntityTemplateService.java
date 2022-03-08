package com.enzen.hes.service;


import com.enzen.hes.handler.exception.ValidationException;
import com.enzen.hes.mongo.service.MongoService;
import com.enzen.hes.mongo.utils.MongoUtils;
import com.enzen.hes.utils.CommonUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.enzen.hes.mongo.constants.MongoConstants.*;

@Service
public class EntityTemplateService {
    @Autowired
    private MongoService mongoService;

    public void createTemplate(Document templateRequest) {
        var validated = MongoUtils.validateKeys(templateRequest, List.of("category", "type", "attrs"));
        if (!validated) {
            throw new ValidationException(HttpStatus.BAD_REQUEST.value(), "Request validation failed");
        }
        Document query = new Document("category", templateRequest.get("category")).append("type", templateRequest.get("type"));
        mongoService.deleteOne(ENTITY_TEMPLATE_COLLECTION, query);
        templateRequest.put(_ID, CommonUtils.generateUUID());
        mongoService.create(ENTITY_TEMPLATE_COLLECTION, templateRequest);
    }

    public List<Document> listTemplate(Map<String, Object> queryParams) {
        Document query = new Document(queryParams);
        var projection = new Document();
        return mongoService.findList(ENTITY_TEMPLATE_COLLECTION, query, projection);
    }

    public void deleteTemplate(Map<String, Object> queryParams) {
        Document query = new Document(queryParams);
        mongoService.deleteMany(ENTITY_TEMPLATE_COLLECTION, query);
    }

    public void deleteTemplate(String id) {
        mongoService.deleteById(ENTITY_TEMPLATE_COLLECTION, id);
    }
}
