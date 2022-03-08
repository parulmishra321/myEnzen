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
public class ZoneService {

    @Autowired
    private MongoService mongoService;

    public void createZone(Document zoneRequest) {
        zoneRequest.put(_ID, CommonUtils.generateUUID());
        mongoService.create(ZONE_COLLECTION, zoneRequest);
    }

    public List<Document> getAllZones() {
        var projection = new Document();
        var result = mongoService.findAll(ZONE_COLLECTION, projection);
        if (result.isEmpty()) {
            throw new ValidationException(HttpStatus.BAD_REQUEST.value(), "Zone details does not exists");
        }
        return result;
    }

    public void deleteZone(String id) {
        var query = new Document();
        query.put(_ID, id);
        var result = mongoService.deleteOne(ZONE_COLLECTION, query);
        if (result.getDeleteCount() == 0) {
            throw new ValidationException(HttpStatus.BAD_REQUEST.value(), "Zone details does not exists");
        }
    }

    public void updateZone(Document zoneRequest, String id) {
        var query = new Document();
        query.put(_ID, id);
        var result = mongoService.update(ZONE_COLLECTION, query, zoneRequest, null, null, null, true);
        if (result.getUpdateCount()==0) {
            throw new ValidationException(HttpStatus.BAD_REQUEST.value(), "Zone details does not exists");
        }
    }
}
