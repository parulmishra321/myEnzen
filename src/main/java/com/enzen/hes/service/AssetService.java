package com.enzen.hes.service;


import com.enzen.hes.handler.exception.ValidationException;
import com.enzen.hes.mongo.service.MongoService;
import com.enzen.hes.mongo.utils.MongoUtils;
import com.enzen.hes.utils.CommonUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
public class AssetService {
    @Autowired
    private MongoService mongoService;

    public void addAssetAttributes(Document assetTemplateRequest) {
        var assetType = assetTemplateRequest.getString("assetType");
        if (Objects.isNull(assetType)) {
            throw new ValidationException(HttpStatus.BAD_REQUEST.value(), "Asset Type is mandatory!");
        }
        assetTemplateRequest.put(_ID, assetType);
        var query = new Document(_ID, assetType);
        var projection = new Document();
        var result = mongoService.findOne(ASSET_TEMPLATE_COLLECTION, query, projection);
        if (MongoUtils.nonNullAndEmpty(result)) {
            mongoService.update(ASSET_TEMPLATE_COLLECTION, query, assetTemplateRequest, null, null, null, true);
        } else {
            mongoService.create(ASSET_TEMPLATE_COLLECTION, assetTemplateRequest);
        }
    }

    public List<Document> getAssetAttributes() {
        var projection = new Document();
        projection.put(_ID, 0);
        var result = mongoService.findAll(ASSET_TEMPLATE_COLLECTION, projection);
        if (result.isEmpty()) {
            throw new ValidationException(HttpStatus.BAD_REQUEST.value(), "Asset details does not exists");
        }
        return result;
    }

    public void createAsset(Document assetRequest) {
        assetRequest.put(_ID, CommonUtils.generateUUID());
        assetRequest.put("from", CommonUtils.getCurrentTimeInMillis());
        assetRequest.put("to", CommonUtils.getCurrentTimeInMillis());
        mongoService.create(ASSET_COLLECTION, assetRequest);
    }

    public void createAssetBulk(Document formData, MultipartFile file)  {
        try (CSVParser parser = CSVFormat.newFormat(',').parse(new InputStreamReader(new ByteArrayInputStream(file.getBytes()), UTF8))) {
            var records = parser.getRecords();
            var itr = records.iterator();
            var headers = itr.next();
            var assets = new ArrayList<Document>();
            while (itr.hasNext()) {
                var record = itr.next();
                Document asset = new Document();
                asset.putAll(formData);
                for (int i = 0; i < headers.size(); i++) {
                    asset.put(headers.get(i), record.get(i));
                }
                assets.add(asset);
            }
            mongoService.insertAll(ASSET_COLLECTION, assets);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public List<Document> getAllAssets() {
        var projection = new Document();
        var result = mongoService.findAll(ASSET_COLLECTION, projection);
        if (result.isEmpty()) {
            throw new ValidationException(HttpStatus.BAD_REQUEST.value(), "Asset details does not exists");
        }
        return result;
    }

    public void deleteAsset(String id) {
        var query = new Document();
        query.put(_ID, id);
        var result = mongoService.deleteOne(ASSET_COLLECTION, query);
        if (result.getDeleteCount() == 0) {
            throw new ValidationException(HttpStatus.BAD_REQUEST.value(), "Asset details does not exists");
        }
    }

    public void updateAsset(Document assetRequest, String id) {
        var query = new Document();
        query.put(_ID, id);
        var result= mongoService.update(ASSET_COLLECTION, query, assetRequest, null, null, null, true);
        if (result.getUpdateCount() == 0) {
            throw new ValidationException(HttpStatus.BAD_REQUEST.value(), "Asset details does not exists");
        }
    }
}
