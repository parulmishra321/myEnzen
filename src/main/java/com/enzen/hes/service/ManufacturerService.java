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
public class ManufacturerService {

    @Autowired
    private MongoService mongoService;

    public void createManufacturer(Document manufacturerRequest) {
        manufacturerRequest.put(_ID, CommonUtils.generateUUID());
        mongoService.create(MANUFACTURER_COLLECTION, manufacturerRequest);
    }

    public List<Document> getAllManufacturers() {
        var projection = new Document();
        var result = mongoService.findAll(MANUFACTURER_COLLECTION, projection);
        if (result.isEmpty()) {
            throw new ValidationException(HttpStatus.BAD_REQUEST.value(), "Manufacturer details does not exists");
        }
        return result;
    }

    public void deleteManufacturer(String id) {
        var query = new Document();
        query.put(_ID, id);
        var result = mongoService.deleteOne(MANUFACTURER_COLLECTION, query);
        if (result.getDeleteCount() == 0) {
            throw new ValidationException(HttpStatus.BAD_REQUEST.value(), "Manufacturer details does not exists");
        }
    }

    public void updateManufacturer(Document manufacturerRequest, String id) {
        var query = new Document();
        query.put(_ID, id);
        var result = mongoService.update(MANUFACTURER_COLLECTION, query, manufacturerRequest, null, null, null, true);
        if (result.getUpdateCount() == 0) {
            throw new ValidationException(HttpStatus.BAD_REQUEST.value(), "Manufacturer details does not exists");
        }
    }
}
