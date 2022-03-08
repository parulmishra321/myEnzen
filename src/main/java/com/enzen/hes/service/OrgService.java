package com.enzen.hes.service;

import com.enzen.hes.handler.exception.ValidationException;
import com.enzen.hes.mongo.service.MongoService;
import com.enzen.hes.mongo.utils.MongoUtils;
import com.enzen.hes.utils.CommonUtils;
import com.enzen.hes.utils.ValidationUtils;
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
import java.util.Objects;

import static com.enzen.hes.mongo.constants.MongoConstants.*;

@Service
public class OrgService {
    @Autowired
    private MongoService mongoService;

    public void addOrgAttributes(Document orgTemplateRequest) {
        var orgType = orgTemplateRequest.getString("orgType");
        if (Objects.isNull(orgType)) {
            throw new ValidationException(HttpStatus.BAD_REQUEST.value(), "Org Type is mandatory");
        }
        orgTemplateRequest.put(_ID, orgType);
        var query = new Document(_ID, orgType);
        mongoService.update(ORG_TEMPLATE_COLLECTION, query, orgTemplateRequest);
    }

    public List<Document> getOrgAttributes() {
        var projection = new Document();
        projection.put(_ID, 0);
        var result = mongoService.findAll(ORG_TEMPLATE_COLLECTION, projection);
        if (result.isEmpty()) {
            throw new ValidationException(HttpStatus.BAD_REQUEST.value(), "Org details does not exists");
        }
        return result;
    }

    public void createOrg(Document orgRequest) {
        orgRequest.put(_ID, CommonUtils.generateUUID());
        mongoService.create(ORG_COLLECTION, orgRequest);
    }

    public void createOrgBulk(Document formData,MultipartFile file) {
        try (CSVParser parser = CSVFormat.newFormat(',').parse(new InputStreamReader(new ByteArrayInputStream(file.getBytes()), UTF8))) {
            var records = parser.getRecords();
            var itr = records.iterator();
            var headers = itr.next();
            var orgs = new ArrayList<Document>();
            while (itr.hasNext()) {
                var record = itr.next();
                Document org = new Document();
                org.putAll(formData);
                for (int i = 0; i < headers.size(); i++) {
                    org.put(headers.get(i), record.get(i));
                }
                orgs.add(org);
            }
            mongoService.insertAll(ORG_COLLECTION, orgs);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public List<Document> getAllOrg() {
        var projection = new Document();
        var result = mongoService.findAll(ORG_COLLECTION, projection);
        if (result.isEmpty()) {
            throw new ValidationException(HttpStatus.BAD_REQUEST.value(), "Org details does not exists");
        }
        return result;
    }

    public void deleteOrg(String id) {
        var query = new Document();
        query.put(_ID, id);
        var result = mongoService.deleteOne(ORG_COLLECTION, query);
        if (result.getDeleteCount() == 0) {
            throw new ValidationException(HttpStatus.BAD_REQUEST.value(), "Org details does not exists");
        }
    }

    public void updateOrg(Document orgRequest, String id) {
        var query = new Document();
        query.put(_ID, id);
        var result=mongoService.update(ORG_COLLECTION, query, orgRequest, null, null, null, true);
        if (result.getUpdateCount()== 0) {
            throw new ValidationException(HttpStatus.BAD_REQUEST.value(), "Org details does not exists");
        }
    }
}
