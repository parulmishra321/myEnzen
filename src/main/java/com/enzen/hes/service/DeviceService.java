package com.enzen.hes.service;

import com.enzen.hes.handler.exception.ValidationException;
import com.enzen.hes.mongo.service.MongoService;
import com.enzen.hes.mongo.utils.MongoUtils;
import com.enzen.hes.utils.CommonUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;
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
public class DeviceService {
    @Autowired
    private MongoService mongoService;

    public void addDeviceAttributes(Document deviceTemplateRequest) {
        var deviceType = deviceTemplateRequest.getString("deviceType");
        if (Objects.isNull(deviceType)) {
            throw new ValidationException(HttpStatus.BAD_REQUEST.value(), "Device Type is mandatory");
        }
        deviceTemplateRequest.put(_ID, "deviceType");
        Document query = new Document(_ID, "deviceType");
        mongoService.update(DEVICE_TEMPLATE_COLLECTION, query, deviceTemplateRequest);
    }

    public List<Document> getDeviceAttributes() {
        var projection = new Document();
        projection.put(_ID, 0);
        var result = mongoService.findAll(DEVICE_TEMPLATE_COLLECTION, projection);
        if (result.isEmpty()) {
            throw new ValidationException(HttpStatus.BAD_REQUEST.value(), "Device details does not exists");
        }
        return result;
    }

    public void createDevice(@NotNull Document deviceRequest) {
        deviceRequest.put(_ID, CommonUtils.generateUUID());
        mongoService.create(DEVICE_COLLECTION, deviceRequest);
    }

    public void createDeviceBulk(Document formData, MultipartFile file)  {
        try (CSVParser parser = CSVFormat.newFormat(',').parse(new InputStreamReader(new ByteArrayInputStream(file.getBytes()), UTF8))) {
            var records = parser.getRecords();
            var itr = records.iterator();
            var headers = itr.next();
            var devices = new ArrayList<Document>();
            while (itr.hasNext()) {
                var record = itr.next();
                Document device = new Document();
                device.putAll(formData);
                for (int i = 0; i < headers.size(); i++) {
                    device.put(headers.get(i), record.get(i));
                }
                devices.add(device);
            }
            mongoService.insertAll(DEVICE_COLLECTION, devices);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public List<Document> getAllDevices() {
        var projection = new Document();
        var result = mongoService.findAll(DEVICE_COLLECTION, projection);
        if (result.isEmpty()) {
            throw new ValidationException(HttpStatus.BAD_REQUEST.value(), "Device details does not exists");
        }
        return result;
    }

    public void deleteDevice(String id) {
        var query = new Document();
        query.put(_ID, id);
        var result = mongoService.deleteOne(DEVICE_COLLECTION, query);
        if (result.getDeleteCount() == 0) {
            throw new ValidationException(HttpStatus.BAD_REQUEST.value(), "Device details does not exists");
        }
    }

    public void updateDevice(Document deviceInfoRequest, String id) {
        var query = new Document();
        query.put(_ID, id);
        var result= mongoService.update(DEVICE_COLLECTION, query, deviceInfoRequest, null, null, null, true);
        if (result.getUpdateCount()== 0) {
            throw new ValidationException(HttpStatus.BAD_REQUEST.value(), "Device details does not exists");
        }
    }
}
