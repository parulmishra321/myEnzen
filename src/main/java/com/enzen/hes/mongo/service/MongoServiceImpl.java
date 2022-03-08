/* ***********************************************************************
 * 83incs CONFIDENTIAL
 * ***********************************************************************
 *
 *  [2017] - [2022] 83incs Ltd.
 *  All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of 83incs Ltd, IoT83 Ltd, its suppliers (if any), its subsidiaries (if any) and
 * Source Code Licensees (if any).  The intellectual and technical concepts contained
 * herein are proprietary to 83incs Ltd, IoT83 Ltd, its subsidiaries (if any) and
 * Source Code Licensees (if any) and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from 83incs Ltd or IoT83 Ltd.
 ****************************************************************************
 */

package com.enzen.hes.mongo.service;

import com.enzen.hes.constants.CommonConstants;
import com.enzen.hes.handler.exception.ValidationException;
import com.enzen.hes.mongo.commons.CustomIndexOptions;
import com.enzen.hes.mongo.dao.MongoIndex;
import com.enzen.hes.mongo.dao.MongoRepository;
import com.enzen.hes.mongo.enums.Order;
import com.enzen.hes.mongo.result.DeleteDocResult;
import com.enzen.hes.mongo.result.UpdateDocResult;
import com.enzen.hes.utils.CommonUtils;
import com.enzen.hes.utils.ValidationUtils;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoNamespace;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.DropIndexOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.enzen.hes.constants.ApplicationConstants.COMMA;


/**
 * This class is designed to provide methods for mongodb that were used throughout the project.
 * <br><br>{@link #listCollections()} This method is used to get the list of collection.
 * <br><br>{@link #count(String collection, Document matchEq, Document matchIn)} This method is used to count the number of documents in a collection of mongodb.
 * <br><br>{@link #count(Document matchEq, Document searchParams, String collection)} This method is used to count the number of documents in a collection of mongodb.
 * <br><br>{@link #findOne(String collection, Document query, Document projection)} This method is used to find single value from mongodb collection based on different-different parameters.
 * <br><br>{@link #findOne(String collection, Document query, Document projection, String sortBy, Integer sortType)} This method is used to find single value from mongodb collection based on different-different parameters.
 * <br><br>{@link #findById(String collection, String id, Document projection)} This method is used to find the data from mongodb database on the basis of collection name, id, and projection.
 * <br><br>{@link #insertAll(String collection, List)} This method is used to insert multiple documents in mongodb collection at same time.
 * <br><br>{@link #create(String collection, Document data)} This method is used to insert document in a collection of mongodb.
 * <br><br>{@link #create(String collection)} This method is used to create a new collection in mongodb.
 * <br><br>{@link #update(String collection, Document filter, Document set, Document unset, Document push, Document pushToSet, boolean multi)} This method is  used to update the document(s) of collection. Based on parameters.
 * <br><br>{@link #update(String collection, Document filter, Document set, Document unset, Document push, Document pushToSet, boolean multi, boolean upsert)} TThis method is  used to update the document(s) of collection. Based on parameters.
 * <br><br>{@link #deleteOne(String collection, Document filter)} This method is used to delete single document from a collection of mongodb.
 * <br><br>{@link #deleteMany(String collection, Document filter)} This method is used to delete multiple documents from mongodb.
 * <br><br>{@link #deleteById(String collection, String id)} This method is used to delete single document from a collection of mongodb.
 * <br><br>{@link #findDistinct(String collection, String field, Class type)} This method is used to find distinct values from collection with a particular fieldName.
 * <br><br>{@link #findDistinct(String collection, Document query, String field, Class type)} This method is used to find distinct values from collection with a particular fieldName..
 * <br><br>{@link #findList(String collection, Document matchEq, Document projection, String sortBy, Integer sortType)} This method is used to find list of values from mongodb on the basis of collectionName with different-different parameters.
 * <br><br>{@link #findList(String collection, Document matchEq, Document projection, Document searchParams, Integer offset, Integer max)} This method is used to find list of values from mongodb on the basis of collectionName with different-different
 * <br><br>{@link #findList(String collection, Document query, Document projection, Integer offset, Integer max, Document sort)} This method is used to find list of values from mongodb on the basis of collectionName with different-different parameters.
 * <br><br>{@link #findList(String collection, Document matchEq, Document projection, Document searchParams, Integer offset, Integer max, String sortBy, Integer sortType)} This method is used to find list of values from mongodb on the basis of collectionName with different-different parameters.
 * <br><br>{@link #findList(String collection, String sortBy, Integer sortType, Integer limit, Document projection)} This method is used to find list of values from mongodb on the basis of collectionName with different-different parameters.
 * <br><br>{@link #findList(String collection, Document matchEq, Document projection)} This method is used to find list of values from mongodb on the basis of collectionName with different-different parameters.
 * <br><br>{@link #findAll(String collection, Document projection)} This method is used to find list of values from mongodb on the basis of collectionName with different-different parameters.
 * <br><br>{@link #findList(String collection, Document matchEq, Document matchIn, String sortBy, int sortType, int limit, int offset, Document projection)} This method is used to find list of values from mongodb on the basis of collectionName with different-different parameters.
 * <br><br>{@link #findList(String collection, Document matchEq, Document matchIn, Document projection)} This method is used to find list of values from mongodb on the basis of collectionName with different-different parameters.
 * <br><br>{@link #findListByDuration(String collection, Document matchEq, Document projection, String sortBy, int sortType, int duration)} This method is used to find list of values from mongodb on the basis of timestamp with different-different parameters.
 * <br><br>{@link #findListByTimestamp(String collection, Document matchEq, String resourceIdOrInClause, String resourceName, long minutes, Document projection, String sortBy, Integer sortType)} This method is used to find list of values from mongodb on the basis of timestamp with different-different parameters
 * <br><br>{@link #findList(String collection, Document matchEq, String resourceIdOrInClause, String resourceName, String sortBy, int sortType, Document projection)} This method is used to find list of values from mongodb on the basis of timestamp with different-different parameters.
 * <br><br>{@link #updateDocumentFields(String collection, Document filter, Document set, boolean multi, boolean upsert)} This method is used to update the value of a field with the specified value.
 * <br><br>{@link #removeArrayFields(String collection, Document filter, Document push, boolean multi)} This method is used to update collection when user wants to remove values from array.
 * <br><br>{@link #createDatabase(String hostname, String port, String dbName)} This method is used to create a new database in mongodb .
 * <br><br>{@link #isCollectionExist(String collection)} This method is used to check if the given collection name is exist or not.
 * <br><br>{@link #getAggregatedData(String collection, Document match, Document groupBy, Document projection, Document sort)} This method is used to fetch data from mongodb using aggregation.
 * <br><br>{@link #getSortedAggregatedData(String collection, Document match, Document groupBy, Document projection, Document sort)} This method is used to fetch data from mongodb using aggregation.
 * <br><br>{@link #aggregate(Document match, Document group, Document projection, String sortBy, Integer sortType, String collection)} This method is used to fetch data from mongodb using aggregation.
 * <br><br>{@link #dropIndexes(String collection)} This method is used to drop indexes of collection of mongodb.
 * <br><br>{@link #dropIndex(String collection, String indexName, Document field, DropIndexOptions dropIndexOptions)} This method is used to drop the index of collection in mongodb.
 */
@Component
@SuppressWarnings("unchecked")
public class MongoServiceImpl implements MongoService {
    private static final Logger LOG = LogManager.getLogger(MongoServiceImpl.class);
    @Autowired
    private MongoRepository mongoRepository;

//
//    @Autowired
//    private ApplicationConsulService applicationConsulService;

    /**
     * This method is used to get the list of collection.
     *
     * @return collectionData of type {HashMap}.
     */
    @Override
    public HashMap<String, Set<String>> listCollections() {
        long startTime = CommonUtils.getCurrentTimeInMillis();
        HashMap<String, Set<String>> collectionData = new HashMap<>();
        List<String> mongoCollections = mongoRepository.getMongoCollections();
        Document projection = new Document();
        mongoCollections.forEach(collection -> {
            projection.clear();
            projection.put("_id", 0);
            projection.put("dateCreated", 0);
            projection.put("id", 0);
            projection.put("timestamp", 0);
            projection.put("createdAt", 0);
            projection.put("createdBy", 0);
            projection.put("_class", 0);
            projection.put("hour", 0);
            Document fields = mongoRepository.findOne(collection, null, projection, null);
            if (Objects.nonNull(fields)) {
                collectionData.put(collection, fields.keySet());
            }
        });
//        checkTimeToMethodComplete(startTime);
        return collectionData;
    }

    /**
     * This method is used to count the number of documents in a collection of mongodb.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @param matchEq    it matches documents where the value of a field equals the specified value.
     * @param matchIn    it matches document where atleast one element matches the criteria.
     * @return count of type {Long}.
     */
    @Override
    public long count(String collection, Document matchEq, Document matchIn) {
        Document query = new Document();
        matchEq.forEach(query::put);

        if (ValidationUtils.nonNullOrEmpty(matchIn)) {
            matchIn.forEach((key, value) -> query.put(key, new Document("$in", value)));
        }
        return this.mongoRepository.count(collection, query);
    }

    /**
     * This method is used to count the number of documents in a collection of mongodb.
     *
     * @param matchEq      it matches documents where the value of a field equals the specified value.
     * @param searchParams Filters the documents to pass only the documents that match the specified condition(s).
     * @param collection   is the name of the collection on which operation is being performed.
     * @return count of type {Long}.
     */
    @Override
    public long count(Document matchEq, Document searchParams, String collection) {
        Document query = new Document();
        if (Objects.nonNull(matchEq)) {
            matchEq.forEach(query::put);
        }
        if (Objects.nonNull(searchParams)) {
            searchParams.forEach((k, v) -> {
                query.put(k, new Document("$regex", v).append("$options", "-i"));
            });
        }
        return this.mongoRepository.count(collection, query);
    }

    ////////////RETRIEVE/////////////////

    /**
     * This method is used to find single value from mongodb collection based on different-different parameters.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @param query      the query filter.
     * @param projection means selecting only the necessary data rather than selecting whole of the data of a document.
     * @return result of type {Document}
     */
    @Override
    public Document findOne(String collection, Document query, Document projection) {
        long startTime = CommonUtils.getCurrentTimeInMillis();
        Document sort = new Document();
        Document result = mongoRepository.findOne(collection, query, projection, sort);

//        checkTimeToMethodComplete(startTime);
        return result;
    }

    /**
     * This method is used to find single value from mongodb collection based on different-different parameters.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @param query      the query filter.
     * @param projection means selecting only the necessary data rather than selecting whole of the data of a document.
     * @param sortBy     it sorts data on the basis of some kind of values like UpdatedAt, CreatedAt etc....
     * @param sortType   it sorts the data either in ascending or descending order.
     * @return fetched data of type {Document}
     */
    @Override
    public Document findOne(String collection, Document query, Document projection, String sortBy, Integer sortType) {
        Document sort = new Document();
        if (Objects.nonNull(sortBy) && Objects.nonNull(sortType)) {
            sort.put(sortBy, sortType);
        }
        return mongoRepository.findOne(collection, query, projection, sort);
    }

    /**
     * This method is used to find the data from mongodb database on the basis of collection name, id, and projection.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @param id         it is of String type.
     * @param projection means selecting only the necessary data rather than selecting whole of the data of a document.
     * @return fetched data of type {Document}
     */
    @Override
    public Document findById(String collection, String id, Document projection) {
        Document query = new Document();
        query.put("id", id);
        return mongoRepository.findOne(collection, query, projection, null);
    }

    /**
     * This method is used to insert multiple documents in mongodb collection at same time.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @param recordList to be inserted in collection.
     */
    @Override
    public void insertAll(String collection, List<Document> recordList) {
        mongoRepository.insertMany(collection, recordList);
    }

    /**
     * This method is used to insert document in a collection of mongodb.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @param data       document to be inserted in collection.
     * @return true of type {boolean}
     */
    @Override
    public boolean create(String collection, Document data) {
        this.mongoRepository.insertOne(collection, data);
        return true;
    }

    /**
     * This method is used to create a new collection in mongodb.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @return true of type {boolean}
     */
    @Override
    public boolean create(String collection) {
        this.mongoRepository.createCollection(collection);
        return true;
    }

    /**
     * This method is  used to update the document(s) of collection. Based on parameters.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @param filter     the query filter.
     * @param set        this document has key and value which is either updated with previous value or inserted
     *                   if key does not exists.
     * @param unset      it specifies the field(s) which is going to be deleted.
     * @param push       is used to remove from an existing array all instances of a value or values that match a specified condition.
     * @param pushToSet  This is used to adds a value to an array unless the value is already present,
     *                   if value alredy exists it does nothing to that array.
     * @param multi      it is a boolean value which is used to decide to update single element or multiple element.
     * @return result of type {UpdateDocResult}
     */
    @Override
    public UpdateDocResult update(String collection, Document filter, Document set, Document unset, Document push, Document pushToSet, boolean multi) {
        UpdateDocResult result = new UpdateDocResult();
        UpdateResult updateResult = this.mongoRepository.update(collection, filter, set, unset, push, null, pushToSet, null, multi);
        if (Objects.nonNull(updateResult)) {
            result.setMatchedCount(updateResult.getMatchedCount());
            if (updateResult.isModifiedCountAvailable())
                result.setUpdateCount(updateResult.getModifiedCount());
        }
        return result;
    }

    /**
     * This method is  used to update the document(s) of collection. Based on parameters.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @param filter     the query filter.
     * @param set        this document has key and value which is either updated with previous value or inserted
     *                   if key does not exists.
     * @param unset      it specifies the field(s) which is going to be deleted.
     * @param push       is used to remove from an existing array all instances of a value or values that match a specified condition.
     * @param pushToSet  This is used to adds a value to an array unless the value is already present,
     *                   if value alredy exists it does nothing to that array.
     * @param multi      it is a boolean value which is used to decide to update single element or multiple element.
     * @param upsert     if true then new field will be added.
     * @return result of type {UpdateDocResult}
     */
    @Override
    public UpdateDocResult update(String collection, Document filter, Document set, Document unset, Document push, Document pushToSet, boolean multi, boolean upsert) {
        UpdateDocResult result = new UpdateDocResult();
        UpdateResult updateResult = this.mongoRepository.update(collection, filter, set, unset, push, null, pushToSet, null, multi, upsert);
        if (Objects.nonNull(updateResult)) {
            result.setMatchedCount(updateResult.getMatchedCount());
            if (updateResult.isModifiedCountAvailable())
                result.setUpdateCount(updateResult.getModifiedCount());
        }
        return result;
    }

    @Override
    public UpdateDocResult update(String collection, Document filter, Document set, boolean upsert) {
        return update(collection,filter,set,null,null,null,true,upsert);
    }

    @Override
    public UpdateDocResult update(String collection, Document filter, Document set) {
        return update(collection,filter, set,null,null,null,true,true);
    }

    /**
     * This method is used to delete single document from a collection of mongodb.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @param filter     the query filter.
     * @return result of type {DeleteDocResult}
     */
    @Override
    public DeleteDocResult deleteOne(String collection, Document filter) {
        DeleteDocResult result = new DeleteDocResult();
        DeleteResult deleteResult = this.mongoRepository.deleteOne(collection, filter);
        if (Objects.nonNull(deleteResult))
            result.setDeleteCount(deleteResult.getDeletedCount());
        return result;
    }

    /**
     * This method is used to delete multiple documents from mongodb.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @param filter     the query filter.
     * @return result of type {DeleteDocResult}
     */
    @Override
    public DeleteDocResult deleteMany(String collection, Document filter) {
        DeleteDocResult result = new DeleteDocResult();
        DeleteResult deleteResult = this.mongoRepository.deleteMany(collection, filter);
        if (Objects.nonNull(deleteResult))
            result.setDeleteCount(deleteResult.getDeletedCount());
        return result;
    }

    /**
     * This method is used to delete single document from a collection of mongodb.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @param id         it consist of id of document
     * @return result of type {DeleteDocResult}
     */
    @Override
    public DeleteDocResult deleteById(String collection, String id) {
        DeleteDocResult result = new DeleteDocResult();
        if (Objects.isNull(findById(collection, id, null))) {
            throw new ValidationException(HttpStatus.NOT_FOUND.value(), "No Resource Exist with this id");
        }
        Document query = new Document();
        query.put("_id", id);
        DeleteResult deleteResult = this.mongoRepository.deleteOne(collection, query);
        if (Objects.nonNull(deleteResult))
            result.setDeleteCount(deleteResult.getDeletedCount());
        return result;
    }

    /**
     * This method is used to find distinct values from collection with a particular fieldName.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @param field      is the field from which distinct values have been fetched.
     * @param type       is casting it to the given Class.
     * @return list of distinct values.
     */
    @Override
    public List findDistinct(String collection, String field, Class type) {
        return this.mongoRepository.findDistinct(collection, null, field, type);
    }

    /**
     * This method is used to find distinct values from collection with a particular fieldName.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @param query      the query filter.
     * @param field      is the field from which distinct values have been fetched.
     * @param type       is casting it to the given Class.
     * @return list of distinct values.
     */
    @Override
    public List findDistinct(String collection, Document query, String field, Class type) {
        return this.mongoRepository.findDistinct(collection, query, field, type);
    }

    /**
     * This method is used to find list of values from mongodb on the basis of collectionName with different-different parameters.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @param matchEq    the query filter.
     * @param projection means selecting only the necessary data rather than selecting whole of the data of a document.
     * @param sortBy     is used to sort data on the basis of some specific field.
     * @param sortType   is used to sort data either in ascending or descending order
     * @return it returns list of document.
     */
    @Override
    public List<Document> findList(String collection, Document matchEq, Document projection, String sortBy, Integer sortType) {
        Document query = new Document();
        if (Objects.nonNull(matchEq)) {
            matchEq.forEach(query::put);
        }
        Document sort = new Document();
        sort.put(sortBy, sortType);
        return this.mongoRepository.findList(collection, query, projection, sort, null, null);
    }

    /**
     * This method is used to find list of values from mongodb on the basis of collectionName with different-different
     * parameters.
     *
     * @param collection   is the name of the collection on which operation is being performed.
     * @param matchEq      the query filter.
     * @param projection   means selecting only the necessary data rather than selecting whole of the data of a document.
     * @param searchParams the query filter.
     * @param offset       is specifically used for pagination it is an input integer argument
     *                     that specifies the number of documents to be skipped in a result set
     * @param max          is specifically used for pagination it is the size of documents to be displayed in a page.
     * @return it returns list of document.
     */
    @Override
    public List<Document> findList(String collection, Document matchEq, Document projection, Document searchParams, Integer offset, Integer max) {
        long startTime = CommonUtils.getCurrentTimeInMillis();
        List<Document> response = new ArrayList<>();
        Document query = new Document();
        if (Objects.nonNull(matchEq)) {
            matchEq.forEach(query::put);
        }
        if (Objects.nonNull(searchParams)) {
            searchParams.forEach((k, v) -> {
                query.put(k, new Document("$regex", v).append("$options", "-i"));
            });
        }
        response = this.mongoRepository.findList(collection, query, projection, null, offset, max);
//        checkTimeToMethodComplete(startTime);
        return response;
    }

    /**
     * This method is used to find list of values from mongodb on the basis of collectionName with different-different
     * parameters.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @param query      the query filter.
     * @param projection means selecting only the necessary data rather than selecting whole of the data of a document.
     * @param offset     is specifically used for pagination it is an input integer argument
     *                   that specifies the number of documents to be skipped in a result set
     * @param max        is specifically used for pagination it is the size of documents to be displayed in a page.
     * @param sort       is used to sort data either in ascending or descending order on the basis of some specific field.
     * @return it returns list of document.
     */
    @Override
    public List<Document> findList(String collection, Document query, Document projection, Integer offset, Integer max, Document sort) {
        long startTime = CommonUtils.getCurrentTimeInMillis();
        List<Document> response = new ArrayList<>();
        response = mongoRepository.findList(collection, query, projection, sort, offset, max);
//        checkTimeToMethodComplete(startTime);
        return response;
    }

    /**
     * This method is used to find list of values from mongodb on the basis of collectionName with different-different
     * parameters.
     *
     * @param collection   is the name of the collection on which operation is being performed.
     * @param matchEq      the query filter.
     * @param projection   means selecting only the necessary data rather than selecting whole of the data of a document.
     * @param searchParams the query filter.
     * @param offset       is specifically used for pagination it is an input integer argument
     *                     that specifies the number of documents to be skipped in a result set
     * @param max          is specifically used for pagination it is the size of documents to be displayed in a page.
     * @param sortBy       is used to sort data on the basis of some specific field.
     * @param sortType     is used to sort data either in ascending or descending order
     * @return it returns list of document.
     */
    @Override
    public List<Document> findList(String collection, Document matchEq, Document projection, Document searchParams, Integer offset, Integer max, String sortBy, Integer sortType) {
        long startTime = CommonUtils.getCurrentTimeInMillis();
        List<Document> response = new ArrayList<>();
        Document query = new Document();
        if (Objects.nonNull(matchEq)) {
            matchEq.forEach(query::put);
        }
        if (Objects.nonNull(searchParams)) {
            searchParams.forEach((k, v) -> {
                query.put(k, new Document("$regex", v).append("$options", "-i"));
            });
        }
        Document sort = new Document();
        sort.put(sortBy, sortType);
        response = this.mongoRepository.findList(collection, query, projection, sort, offset, max);
//        checkTimeToMethodComplete(startTime);
        return response;
    }

    /**
     * This method is used to find list of values from mongodb on the basis of collectionName with different-different
     * parameters.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @param sortBy     is used to sort data on the basis of some specific field.
     * @param sortType   is used to sort data either in ascending or descending order
     * @param limit      is specifically used for pagination it is the size of documents to be displayed in a page
     * @param projection means selecting only the necessary data rather than selecting whole of the data of a document.
     * @return it returns list of document.
     */
    @Override
    public List<Document> findList(String collection, String sortBy, Integer sortType, Integer limit, Document projection) {
        Document sort = null;
        if (ValidationUtils.nonNullOrEmpty(sortBy) && Objects.isNull(sortType)) {
            sort = new Document();
            sort.put(sortBy, sortType);
        }
        if (Objects.isNull(limit)) {
            limit = 0;
        }
        return limit == 0 ? this.mongoRepository.findList(collection, null, projection, sort, null, null) : this.mongoRepository.findList(collection, null, projection, sort, 0, limit);
    }

    /**
     * This method is used to find list of values from mongodb on the basis of collectionName with different-different
     * parameters.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @param query    the query filter.
     * @param projection means selecting only the necessary data rather than selecting whole of the data of a document.
     * @return it returns list of document.
     */
    @Override
    public List<Document> findList(String collection, Document query, Document projection) {
        long startTime = CommonUtils.getCurrentTimeInMillis();
        List<Document> response = new ArrayList<>();
        response = mongoRepository.findList(collection, query, projection, null, null, null);
//        checkTimeToMethodComplete(startTime);
        return response;
    }

    /**
     * This method is used to find list of values from mongodb on the basis of collectionName with different-different
     * parameters.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @param projection means selecting only the necessary data rather than selecting whole of the data of a document.
     * @return it returns list of document.
     */
    @Override
    public List<Document> findAll(String collection, Document projection) {
        long startTime = CommonUtils.getCurrentTimeInMillis();
        Document query = new Document();
        List<Document> response = new ArrayList<>();
        response = mongoRepository.findList(collection, query, projection, null, null, null);
//        checkTimeToMethodComplete(startTime);
        return response;
    }

    /**
     * This method is used to find list of values from mongodb on the basis of collectionName with different-different
     * parameters.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @param matchEq    the query filter.
     * @param matchIn    the query filter.
     * @param sortBy     is used to sort data on the basis of some specific field.
     * @param sortType   is used to sort data either in ascending or descending order
     * @param limit      is specifically used for pagination it is the size of documents to be displayed in a page.
     * @param offset     is specifically used for pagination it is an input integer argument
     *                   that specifies the number of documents to be skipped in a result set
     * @param projection means selecting only the necessary data rather than selecting whole of the data of a document.
     * @return it returns list of document.
     */
    @Override
    public List<Document> findList(String collection, Document matchEq, Document matchIn, String sortBy, int sortType, int limit, int offset, Document projection) {
        Document query = new Document();
        if (Objects.nonNull(matchEq)) {
            matchEq.forEach((key, value) -> query.put(key, value));
        }

        if (ValidationUtils.nonNullOrEmpty(matchIn)) {
            matchIn.forEach((key, value) -> query.put(key, new Document("$in", value)));
        }
        Document sort = new Document();
        sort.put(sortBy, sortType);
        return limit == 0 ? this.mongoRepository.findList(collection, query, projection, sort, null, null) : this.mongoRepository.findList(collection, query, projection, sort, offset, limit);
    }

    /**
     * This method is used to find list of values from mongodb on the basis of collectionName with different-different
     * parameters.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @param matchEq    the query filter.
     * @param matchIn    the query filter.
     * @param projection means selecting only the necessary data rather than selecting whole of the data of a document.
     * @return it returns list of document.
     */
    @Override
    public List<Document> findList(String collection, Document matchEq, Document matchIn, Document projection) {
        Document query = new Document();
        if (Objects.nonNull(matchEq)) {
            matchEq.forEach(query::put);
        }
        if (ValidationUtils.nonNullOrEmpty(matchIn)) {
            matchIn.forEach((key, value) -> query.put(key, new Document("$in", value)));
        }
        return this.mongoRepository.findList(collection, query, projection, null, null, null);
    }

    /**
     * This method is used to find list of values from mongodb on the basis of timestamp with different-different
     * parameters.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @param matchEq    the query filter.
     * @param projection means selecting only the necessary data rather than selecting whole of the data of a document.
     * @param sortBy     is used to sort data on the basis of some specific field.
     * @param sortType   is used to sort data either in ascending or descending order
     * @param duration   is the timestamp.
     * @return it returns list of document.
     */
    @Override
    public List<Document> findListByDuration(String collection, Document matchEq, Document projection, String sortBy, int sortType, int duration) {
        long t1 = Calendar.getInstance().getTimeInMillis();
        Document query = new Document();
        Document sort = new Document();
        if (Objects.nonNull(matchEq)) {
            matchEq.forEach(query::put);
        }
        sort.put(sortBy, sortType);
//        if (sortBy.equalsIgnoreCase(CommonConstants.TIMESTAMP)) {
//            query.put(CommonConstants.TIMESTAMP, new Document("$gte", t1 - duration * 60L * 1000L).append("$lt", t1));
//        } else if (sortBy.equalsIgnoreCase(CommonConstants.DATE_CREATED)) {
//            query.put(CommonConstants.DATE_CREATED, new Document("$gte", new Date(t1 - duration * 60L * 1000L)).append("$lt", new Date(t1)));
//        } else if (sortBy.equalsIgnoreCase(CommonConstants.CREATED_AT)) {
//            query.put(CommonConstants.CREATED_AT, new Document("$gte", new Date(t1 - duration * 60L * 1000L)).append("$lt", new Date(t1)));
//        }
        return this.mongoRepository.findList(collection, query, projection, sort, null, null);
    }

    /**
     * This method is used to find list of values from mongodb on the basis of timestamp with different-different
     * parameters.
     *
     * @param collection           is the name of the collection on which operation is being performed.
     * @param matchEq              the query filter.
     * @param resourceIdOrInClause is of String type.
     * @param resourceName         is of String type.
     * @param minutes              is the timestamp in the min.
     * @param projection           means selecting only the necessary data rather than selecting whole of the data of a document.
     * @param sortBy               is used to sort data on the basis of some specific field.
     * @param sortType             is used to sort data either in ascending or descending order
     * @return result of type {List}
     */
    @Override
    public List<Document> findListByTimestamp(String collection, Document matchEq, String resourceIdOrInClause, String resourceName, long minutes, Document projection, String sortBy, Integer sortType) {
        long startTime = CommonUtils.getCurrentTimeInMillis();
        long t1 = Calendar.getInstance().getTimeInMillis();
        Document query = new Document();
        Document sort = new Document();
        if (Objects.nonNull(matchEq)) {
            matchEq.forEach(query::put);
        }
        if (Objects.nonNull(resourceIdOrInClause)) {
            if (resourceIdOrInClause.contains(COMMA)) {
                query.put(resourceName, new Document("$in", Arrays.asList(resourceIdOrInClause.split(COMMA))));
            } else {
                query.put(resourceName, resourceIdOrInClause);
            }
        }
        if (sortBy.equalsIgnoreCase(CommonConstants.TIMESTAMP)) {
            query.put(CommonConstants.TIMESTAMP, new Document("$gt", t1 - minutes * 60L * 1000L));
        } else if (sortBy.equalsIgnoreCase(CommonConstants.DATE_CREATED)) {
            query.put(CommonConstants.DATE_CREATED, new Document("$gt", new Date(t1 - minutes * 60L * 1000L)));
        } else if (sortBy.equalsIgnoreCase(CommonConstants.CREATED_AT)) {
            query.put(CommonConstants.CREATED_AT, new Document("$gt", new Date(t1 - minutes * 60L * 1000L)).append("$lt", new Date(t1)));
        }
        sort.put(sortBy, sortType);
        List<Document> result = mongoRepository.findList(collection, query, projection, sort, null, null);
        long t2 = Calendar.getInstance().getTimeInMillis();
        //checkTimeToMethodComplete(startTime);
        return result;
    }

    /**
     * This method is used to find list of values from mongodb on the basis of timestamp with different-different
     * parameters.
     *
     * @param collection           is the name of the collection on which operation is being performed.
     * @param matchEq              the query filter.
     * @param resourceIdOrInClause is of String type.
     * @param resourceName         is of String type.
     * @param projection           means selecting only the necessary data rather than selecting whole of the data of a document.
     * @param sortBy               is used to sort data on the basis of some specific field.
     * @param sortType             is used to sort data either in ascending or descending order
     * @return it returns list of document.
     */
    @Override
    public List<Document> findList(String collection, Document matchEq, String resourceIdOrInClause, String resourceName, String sortBy, int sortType, Document projection) {
        long t1 = Calendar.getInstance().getTimeInMillis();
        Document query = new Document();
        Document sort = new Document();
        if (Objects.nonNull(matchEq)) {
            matchEq.forEach(query::put);
        }
        if (Objects.nonNull(resourceIdOrInClause)) {
            if (resourceIdOrInClause.contains(COMMA)) {
                query.put(resourceName, new Document("$in", Arrays.asList(resourceIdOrInClause.split(COMMA))));
            } else {
                query.put(resourceName, resourceIdOrInClause);
            }
        }
        sort.put(sortBy, sortType);
        return this.mongoRepository.findList(collection, query, projection, sort, null, null);
    }

    /**
     * This method is used to find list of values from mongodb on the basis of timestamp with different-different
     * parameters.
     *
     * @param collection           is the name of the collection on which operation is being performed.
     * @param resourceIdOrInClause is of String type.
     * @param resourceName         is of String type.
     * @param sortBy               is used to sort data on the basis of some specific field.
     * @param limit                is specifically used for pagination it is the size of documents to be displayed in a page.
     * @param sortType             is used to sort data either in ascending or descending order
     * @param projection           means selecting only the necessary data rather than selecting whole of the data of a document.
     * @return it returns list of document.
     */
    @Override
    public List<Document> findList(String collection, String resourceIdOrInClause, String resourceName, String sortBy, int limit, int sortType, Document projection) {
        long t1 = Calendar.getInstance().getTimeInMillis();
        Document query = new Document();
        Document sort = new Document();
        if (Objects.nonNull(resourceIdOrInClause)) {
            if (resourceIdOrInClause.contains(",")) {
                query.put(resourceName, new Document("$in", Arrays.asList(resourceIdOrInClause.split(","))));
            } else {
                query.put(resourceName, resourceIdOrInClause);
            }
        }
        sort.put(sortBy, sortType);
        return limit == 0 ? this.mongoRepository.findList(collection, query, projection, sort, (Integer) null, (Integer) null) : this.mongoRepository.findList(collection, query, projection, sort, 0, limit);
    }

    /**
     * This method is used to update the value of a field with the specified value.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @param filter     the query filter.
     * @param set        this document has key and value which is either updated with previous value or inserted
     *                   if key does not exists.
     * @param multi      it is a boolean value which is used to decide to update single element or multiple element.
     * @param upsert     if true then new field will be added.
     */
    @Override
    public void updateDocumentFields(String collection, Document filter, Document set, boolean multi, boolean upsert) {
        this.mongoRepository.updateToSet(collection, filter, set, multi, upsert);
    }

    /**
     * This method is used to update a collection with an intention to remove a particular field.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @param filter     the query filter.
     * @param unset      it specifies the field(s) which is going to be deleted.
     * @param multi      it is a boolean value which is used to decide to update single element or multiple element.
     */
    @Override
    public void removeDocumentFields(String collection, Document filter, Document unset, boolean multi) {
        this.mongoRepository.updateToUnset(collection, filter, unset, multi, false);
    }

    /**
     * This method is used to update collection with a specified value to an array.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @param filter     the query filter.
     * @param push       used to append a specified value to an array.
     * @param multi      it is a boolean value which is used to decide to update single element or multiple element.
     * @param upsert     if true then new field will be added.
     */
    @Override
    public void updateArrayFields(String collection, Document filter, Document push, boolean multi, boolean upsert) {
        this.mongoRepository.updateToPush(collection, filter, push, multi, upsert);
    }

    /**
     * This method is used to update collection when user wants to remove values from array.
     * In this case value(s) in `pull` will be removed from an array.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @param filter     the query filter.
     * @param push       is used to remove from an existing array all instances of a value or values that match a specified condition.
     * @param multi      it is a boolean value which is used to decide to update single element or multiple element.
     */
    @Override
    public void removeArrayFields(String collection, Document filter, Document push, boolean multi) {
        this.mongoRepository.updateToPull(collection, filter, push, multi, false);
    }

    /**
     * This method is used to replace document of collection with another document.
     *
     * @param collection  is the name of the collection on which operation is being performed.
     * @param filter      the query filter.
     * @param replacement document with new values that needs to be updated from previous values.
     * @param upsert      if true then new field will be added.
     */
    @Override
    public void replaceDocument(String collection, Document filter, Document replacement, boolean upsert) {
        this.mongoRepository.replaceDocument(collection, filter, replacement, upsert);
    }


    @Override
    public Document aggregateDataForArrayElementsV2(Document queryParams, String collection, Calendar dateCriteria, Document aggregationParams, String timeEntity) {
        long t1 = Calendar.getInstance().getTimeInMillis();
        Document matchCriteria = new Document(queryParams);

        getDateEntityCriteria(dateCriteria, aggregationParams, timeEntity, matchCriteria);

        Document groupAggregate = new Document()
                .append("_id", new Document((String) aggregationParams.get("label"), "$" + aggregationParams.get("unwind") + "." + aggregationParams.get("label")))
                .append((String) aggregationParams.get("operand"), new BasicDBObject((String) aggregationParams.get("operation"), "$" + aggregationParams.get("unwind") + "." + aggregationParams.get("operand")));

        BasicDBObject groupProject = new BasicDBObject()
                .append("_id", null)
                .append((String) aggregationParams.get("unwind"), new BasicDBObject("$push", new BasicDBObject((String) aggregationParams.get("label"), "$_id." + aggregationParams.get("label")).append((String) aggregationParams.get("operand"), "$" + aggregationParams.get("operand"))));

        AggregateIterable<Document> aggregationResult = mongoRepository.getDB().getCollection(collection).aggregate(Arrays.asList(
                new BasicDBObject("$match", matchCriteria),
                new BasicDBObject("$unwind", "$" + aggregationParams.get("unwind")),
                new BasicDBObject("$group", groupAggregate),
                new BasicDBObject("$group", groupProject)));

        Document resultObject = null;
        Iterator<Document> resultItr = aggregationResult.iterator();
        if (resultItr.hasNext()) {
            resultObject = resultItr.next();
        }

        long t2 = Calendar.getInstance().getTimeInMillis();
        if ((t2 - t1) > (long) CommonConstants.MONGODB_QUERY_LOG_MAX_TIME) {
            LOG.info("Collection Name :: \t" + collection + "\t" + queryParams + "------" + aggregationParams + "----" + timeEntity + "-----Time Taken ::: " + (t2 - t1));
        }
        return resultObject;
    }

    @Override
    public List<Document> aggregateDataForObject(String collection, Calendar dateCriteria, Document aggregationParams, String timeEntity) {
        Document matchCriteria = new Document();
        getDateEntityCriteria(dateCriteria, aggregationParams, timeEntity, matchCriteria);
        Document group = new Document()
                .append("_id", "$" + aggregationParams.getString("entities") + "." + aggregationParams.getString("entities"))
                .append((String) aggregationParams.get("operand"), new BasicDBObject((String) aggregationParams.get("operation"), "$" + aggregationParams.get("entities") + "." + aggregationParams.get("operand")));

        Document project = new Document()
                .append("_id", 0)
                .append((String) aggregationParams.get("entities"), "$_id")
                .append(aggregationParams.getString("operand"), "$" + aggregationParams.getString("operand"));

        AggregateIterable<Document> aggregationResult = mongoRepository.getDB().getCollection(collection).aggregate(Arrays.asList(
                new BasicDBObject("$match", matchCriteria),
                new BasicDBObject("$group", group),
                new BasicDBObject("$project", project)));
        List<Document> resultObject = new ArrayList<>();
        Iterator<Document> resultItr = aggregationResult.iterator();
        while (resultItr.hasNext()) {
            resultObject.add(resultItr.next());
        }
        return resultObject;
    }

    /**
     * This method is used to generate the historical GraphData
     *
     * @param today      it contains the history of today.
     * @param last3Days  it contains the history of last3Days.
     * @param last7Days  it contains the history of last7Days.
     * @param last10Days it contains the history of last10Days.
     * @param last30Days it contains the history of last30Days.
     * @param last90Days it contains the history of last90Days.
     * @return graphDataHistorical
     */
    @Override
    public List<Document> generateHistoricalGraphDataFromDataV2(Document today, Document last3Days,
                                                                Document last7Days, Document last10Days, Document last30Days, Document last90Days) {
        List<Document> graphDataHistorical = new ArrayList<>();
        graphDataHistorical.add(getDataMaps(today, CommonConstants.TODAY));
        graphDataHistorical.add(getDataMaps(last3Days, CommonConstants.LAST_3_DAYS));
        graphDataHistorical.add(getDataMaps(last7Days, CommonConstants.LAST_7_DAYS));
        graphDataHistorical.add(getDataMaps(last30Days, CommonConstants.LAST_30_DAYS));
        graphDataHistorical.add(getDataMaps(last90Days, CommonConstants.LAST_90_DAYS));
        return graphDataHistorical;
    }

    /**
     * This method is used to create a new database in mongodb .
     *
     * @param hostname (The hostname is what a device is called on a network).
     *                 hostname is basically the name of server where the databases have been created.
     * @param port     A port number is a way to identify a specific process to which an
     *                 Internet or other network message is to be forwarded when it arrives at a server
     * @param dbName   is the name by which user wants to create database.
     */
    @Override
    public void createDatabase(String hostname, String port, String dbName) {
        mongoRepository.createDatabase(hostname, port, dbName);
    }

    /**
     * This method is used to get collectionName.
     *
     * @param type is casting to the given Class.
     * @return collection of type {String}.
     */

    @Override
    public String getCollectionName(Class type) {
        com.enzen.hes.annotation.Document document = (com.enzen.hes.annotation.Document) type.getAnnotation(com.enzen.hes.annotation.Document.class);
        String collection = document.collection();
        if (collection.trim().isEmpty()) {
            collection = type.getSimpleName().toLowerCase();
        }
        return collection;
    }


    /**
     * This method is used to get all the collectionNames of particular database in mongodb.
     *
     * @return list of collection names.
     */
    @Override
    public List<String> getAllCollectionNames() {
        long startTime = CommonUtils.getCurrentTimeInMillis();
        List<String> response = new ArrayList<>();
        response = mongoRepository.getMongoCollections();
//        checkTimeToMethodComplete(startTime);
        return response;
    }

    /**
     * This method is used to get all the indexes of a collection in mongodb.
     *
     * @param Collection is the name of the collection on which operation is being performed.
     * @return list of indexes.
     */
    @Override
    public List<Document> getAllIndex(String Collection) {
        long startTime = CommonUtils.getCurrentTimeInMillis();
        List<Document> response = new ArrayList<>();
        response = mongoRepository.getAllIndex(Collection);
//        checkTimeToMethodComplete(startTime);
        return response;
    }

    /**
     * This method is used to check if the given collection name is exist or not.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @return true or false
     */
    @Override
    public boolean isCollectionExist(String collection) {
        return mongoRepository.getMongoCollections().contains(collection);
    }

    @Override
    public List<Map<String, Object>> aggregateReportData(String collection, int dateCriteria, long startTime, long endTime, Document aggregationParams, String timeEntity) {
        Document matchCriteria = new Document();

        this.getDateMatchQuery(dateCriteria, timeEntity, matchCriteria, startTime, endTime);

        Document group = (new Document()).append("_id", "$" + "originSerial").append((String) aggregationParams.get("aggregate"), new Document("$" + aggregationParams.get("function"), "$" + aggregationParams.get("attribute") + "." + aggregationParams.get("aggregate")));
        Document project = (new Document()).append("_id", 0).append((String) aggregationParams.get("label"), "$_id").append(aggregationParams.getString("aggregate"), "$" + aggregationParams.getString("aggregate"));
        AggregateIterable<Document> aggregationResult = mongoRepository.getDB().getCollection(collection).aggregate(Arrays.asList(new Document("$match", matchCriteria), new Document("$group", group), new Document("$project", project)));
        List<Map<String, Object>> resultObject = new ArrayList();
        MongoCursor resultItr = aggregationResult.iterator();

        while (resultItr.hasNext()) {
            resultObject.add((Map<String, Object>) resultItr.next());
        }
        return resultObject;
    }

    /**
     * This method is used to fetch data from mongodb using aggregation.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @param projection means selecting only the necessary data rather than selecting whole of the data of a document.
     * @param match      Filters the documents to pass only the documents that match the specified condition(s).
     * @param groupBy    documents in a collection by the specified keys and performs simple aggregation functions.
     * @param sort       is used to sort data either in ascending or descending order on the basis of some specific field.
     * @return documents of type {List}.
     */
    @Override
    public List<Document> getAggregatedData(String collection, Document match, Document groupBy, Document projection, Document sort) {
        long startTime = CommonUtils.getCurrentTimeInMillis();
        List<Document> pipeline = new ArrayList<>();

        if (Objects.nonNull(match)) {
            pipeline.add(new Document("$match", match));
        }
        if (Objects.nonNull(groupBy)) {
            pipeline.add(new Document("$group", groupBy));
        }
        if (Objects.nonNull(projection)) {
            pipeline.add(new Document("$project", projection));
        }
        if (Objects.nonNull(sort)) {
            pipeline.add(new Document("$sort", sort));
        }

        AggregateIterable<Document> iterable = mongoRepository.getDB().getCollection(collection).aggregate(pipeline);
        List<Document> documents = iterable.into(new ArrayList<>());

//        checkTimeToMethodComplete(startTime);
        return documents;
    }


    @Override
    public List<Document> getSortedAggregatedData(String collection, Document match, Document groupBy, Document projection, Document sort) {
        long startTime = CommonUtils.getCurrentTimeInMillis();
        List<Document> pipeline = new ArrayList<>();

        if (Objects.nonNull(match)) {
            pipeline.add(new Document("$match", match));
        }
        if (Objects.nonNull(sort)) {
            pipeline.add(new Document("$sort", sort));
        }
        if (Objects.nonNull(groupBy)) {
            pipeline.add(new Document("$group", groupBy));
        }
        if (Objects.nonNull(projection)) {
            pipeline.add(new Document("$project", projection));
        }


        AggregateIterable<Document> iterable = mongoRepository.getDB().getCollection(collection).aggregate(pipeline);
        List<Document> documents = iterable.into(new ArrayList<>());

//        checkTimeToMethodComplete(startTime);
        return documents;
    }


    @Override
    public List<Document> aggregateEntityDataForObject(String collection, Calendar dateCriteria, Document aggregationParams, String timeEntity) {
        Document matchCriteria = new Document();
        getDateEntityCriteria(dateCriteria, aggregationParams, timeEntity, matchCriteria);
        Document group = new Document()
                .append("_id", "$" + aggregationParams.getString("entities"))
                .append((String) aggregationParams.get("entities"), new BasicDBObject((String) aggregationParams.get("operation"), "$" + aggregationParams.get("entities")));

        Document project = new Document()
                .append("_id", 0)
                .append((String) aggregationParams.get("entities"), "$_id")
                .append(aggregationParams.getString("entities"), "$" + aggregationParams.getString("entities"));

        AggregateIterable<Document> aggregationResult = mongoRepository.getDB().getCollection(collection).aggregate(Arrays.asList(
                new BasicDBObject("$match", matchCriteria),
                new BasicDBObject("$group", group),
                new BasicDBObject("$project", project)));
        List<Document> resultObject = new ArrayList<>();
        Iterator<Document> resultItr = aggregationResult.iterator();
        while (resultItr.hasNext()) {
            resultObject.add(resultItr.next());
        }
        return resultObject;
    }

    @Override
    public void getDateEntityCriteria(Calendar dateCriteria, Document aggregationParams, String timeEntity, Document matchCriteria) {
        if (timeEntity.equals(CommonConstants.TIMESTAMP)) {
            matchCriteria.put(timeEntity, new Document(String.valueOf(aggregationParams.get("operator")), dateCriteria.getTimeInMillis()));
        } else if (timeEntity.equals(CommonConstants.DATE_CREATED)) {
            matchCriteria.put(timeEntity, new Document(String.valueOf(aggregationParams.get("operator")), dateCriteria.getTime()));
        } else if (timeEntity.equals(CommonConstants.DATE)) {
            matchCriteria.put(timeEntity, new Document(String.valueOf(aggregationParams.get("operator")), dateCriteria.getTime()));
        }
    }

    @Override
    public Document getDataMaps(Document data, String day) {
        Document dataMap = new Document();
        dataMap.put(CommonConstants.DAY, day);
        data.keySet().forEach(k -> dataMap.put(k, Double.parseDouble(CommonConstants.TWO_DECIMAL_PLACE.format(Double.parseDouble(data.get(k) == null || data.get(k).toString().equals(CommonConstants.NOT_A_NUMBER) ? String.valueOf(CommonConstants.ZERO) : data.get(k).toString())))));
        return dataMap;
    }

    /**
     * This method is used to drop collection of a mongodb.
     *
     * @param collection is the name of the collection on which operation is being performed.
     */
    @Override
    public void dropCollection(String collection) {
        mongoRepository.dropCollection(collection);
    }

    @Override
    public void getDateMatchQuery(int dateCriteria, String timeEntity, Document matchCriteria, long startDate, long endDate) {
        long t1 = Calendar.getInstance().getTimeInMillis();
        if (timeEntity.equals(CommonConstants.TIMESTAMP)) {
            matchCriteria.put("timestamp", (new Document("$gte", t1 - (long) dateCriteria * 60L * 1000L)).append("$lt", t1));
        } else if (timeEntity.equals(CommonConstants.START_TIME_END_TIME)) {
            matchCriteria.put("timestamp", (new Document("$gte", startDate)).append("$lt", endDate));
        }

    }

    /**
     * This method is used to fetch data from mongodb using aggregation.
     *
     * @param match      Filters the documents to pass only the documents that match the specified condition(s).
     * @param group      documents in a collection by the specified keys and performs simple aggregation functions.
     * @param projection means selecting only the necessary data rather than selecting whole of the data of a document.
     * @param sortBy     is used to sort data on the basis of some specific field.
     * @param sortType   is used to sort data either in ascending or descending order
     * @param collection is the name of the collection on which operation is being performed.
     * @return documents of type {List}.
     */
    @Override
    public List<Document> aggregate(Document match, Document group, Document projection, String sortBy, Integer sortType, String collection) {
        long startTime = CommonUtils.getCurrentTimeInMillis();
        List<Document> pipeline = new ArrayList<>();
        Document sort = new Document();
        if (Objects.nonNull(sortBy) && Objects.nonNull(sortType)) {
            sort.put(sortBy, sortType);
        }
        if (Objects.nonNull(match)) {
            pipeline.add(new Document("$match", match));
        }

        if (Objects.nonNull(group)) {
            pipeline.add(new Document("$group", group));
        }

        if (Objects.nonNull(projection)) {
            pipeline.add(new Document("$project", projection));
        }
        if (Objects.nonNull(sort)) {
            pipeline.add(new Document("$sort", sort));
        }
        AggregateIterable<Document> iterable = mongoRepository.getDB().getCollection(collection).aggregate(pipeline);

        List<Document> documents = iterable.into(new ArrayList<>());
//        checkTimeToMethodComplete(startTime);
        return documents;

    }

    /**
     * This method is used to fetch data from mongodb using aggregation
     *
     * @param match      Filters the documents to pass only the documents that match the specified condition(s).
     * @param sort       is used to sort data either in ascending or descending order on the basis of some specific field.
     * @param group      documents in a collection by the specified keys and performs simple aggregation functions.
     * @param projection means selecting only the necessary data rather than selecting whole of the data of a document.
     * @param collection is the name of the collection on which operation is being performed.
     * @return documents of type {List}.
     */
    @Override
    public List<Document> aggregate(Document match, Document sort, Document group, Document projection, String collection) {
        long startTime = CommonUtils.getCurrentTimeInMillis();
        List<Document> pipeline = new ArrayList<>();

        if (Objects.nonNull(match)) {
            pipeline.add(new Document("$match", match));
        }


        if (Objects.nonNull(sort)) {
            pipeline.add(new Document("$sort", sort));
        }
        if (Objects.nonNull(group)) {
            pipeline.add(new Document("$group", group));
        }

        if (Objects.nonNull(projection)) {
            pipeline.add(new Document("$project", projection));
        }
        AggregateIterable<Document> iterable = mongoRepository.getDB().getCollection(collection).aggregate(pipeline);

        List<Document> documents = iterable.into(new ArrayList<>());
//        checkTimeToMethodComplete(startTime);
        return documents;

    }

    /**
     * This method is used to fetch data from mongodb using aggregation.
     *
     * @param pipeline   it is the possibility to execute an operation on some input and use the output as the input for the next command.
     * @param collection is the name of the collection on which operation is being performed.
     * @return documents of type {List}.
     */
    public List<Document> aggregate(List<Document> pipeline, String collection) {
        long startTime = CommonUtils.getCurrentTimeInMillis();
        AggregateIterable<Document> iterable = mongoRepository.getDB().getCollection(collection).aggregate(pipeline).allowDiskUse(true);
        List<Document> documents = iterable.into(new ArrayList<>());
//        checkTimeToMethodComplete(startTime);
        return documents;
    }


    /**
     * This method is used to create capped collection in mongodb which means once a
     * collection fills its allocated space, it makes room for new documents by
     * overwriting the oldest documents in the collection.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @param size       is the allocated space for collection.
     * @param max        is the number of documents that can be stored.
     * @return true
     */
    @Override
    public boolean createCappedCollection(String collection, long size, long max) {
        this.mongoRepository.createCappedCollection(collection, size, max);
        return true;
    }

    /**
     * This method is used to define time to live of a collection. After expiration time
     * collection will be deleted automatically from mongodb.
     *
     * @param collection     is the name of the collection on which operation is being performed.
     * @param field          from which distinct values have been fetched.
     * @param expirationTime is the time after collection will be deleted automatically.
     */
    @Override
    public void createTTLForCollection(String collection, String field, Long expirationTime) {
        mongoRepository.createTTLForCollection(collection, field, expirationTime);
    }


    /**
     * This method is used to drop indexes of collection of mongodb.
     *
     * @param collection is the name of the collection on which operation is being performed.
     */
    public void dropIndexes(String collection) {
        mongoRepository.dropIndexes(collection);
    }

    /**
     * This method is used to create index of a collection in mongodb.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @param key        is a special data structure that holds the data of few fields of documents on which the index is created.
     *                   Indexes improve the speed of search operations in database because instead of searching the whole document,
     *                   the search is performed on the indexes that holds only few fields.
     * @param options    it is of type CustomIndexOptions.
     */
    @Override
    public void createIndex(String collection, Document key, CustomIndexOptions options) {
        MongoCollection<Document> coll = mongoRepository.getDB().getCollection(collection);
        options.expireAfter(options.getExpiresIn(), TimeUnit.SECONDS);
        coll.createIndex(key, options);
    }

    /**
     * This method is used to create index in a collection of mongodb.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @param field      from which distinct values have been fetched.
     * @param order      is used to decide to sort either in ascending or descending order.
     * @param unique     ensures that the indexed fields do not store duplicate values.
     */
    @Override
    public void createIndex(String collection, String field, Order order, Boolean unique) {
        mongoRepository.createIndex(collection, field, order, unique);
    }

    /**
     * This method is used to create index of a collection in mongodb.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @param index      is a special data structure that holds the data of few fields of documents on which the index is created.
     *                   Indexes improve the speed of search operations in database because instead of searching the whole document,
     *                   the search is performed on the indexes that holds only few fields.
     * @param unique     ensures that the indexed fields do not store duplicate values
     */
    @Override
    public void createIndex(String collection, Document index, Boolean unique) {
        mongoRepository.createIndex(collection, index, unique);
    }

    public void createIndexes(String collection, List<MongoIndex> indexList) {
        mongoRepository.createIndexes(collection, indexList);
    }


    /**
     * This method is used to drop the index of collection in mongodb.
     *
     * @param collection       is the name of the collection on which operation is being performed.
     * @param indexName        is the name of index.
     * @param field            it consist of some document type data.
     * @param dropIndexOptions it of type DropIndexOptions which gives options to apply to the command when dropping indexes.
     */
    @Override
    public void dropIndex(String collection, String indexName, Document field, DropIndexOptions dropIndexOptions) {
        MongoCollection<Document> collection_list = mongoRepository.getDB().getCollection(collection);
        if (ValidationUtils.nonNullOrEmpty(indexName) && Objects.nonNull(dropIndexOptions)) {
            collection_list.dropIndex(indexName, dropIndexOptions);
        } else if (ValidationUtils.nonNullOrEmpty(field) && Objects.nonNull(dropIndexOptions)) {
            collection_list.dropIndex(field, dropIndexOptions);
        } else if (ValidationUtils.nonNullOrEmpty(indexName)) {
            collection_list.dropIndex(indexName);
        } else if (ValidationUtils.nonNullOrEmpty(field)) {
            collection_list.dropIndex(field);
        }

    }

    @Override
    public long count(String collection, Document match) {
        return mongoRepository.count(collection,match);
    }

    /**
     * This method is used to get the indexes of collection
     *
     * @param collection is the current name of the collection.
     * @return indexes of type {List}
     */
    public List<Document> getIndexes(String collection) {
        long startTime = CommonUtils.getCurrentTimeInMillis();
        MongoCollection<Document> collection_list = mongoRepository.getDB().getCollection(collection);
        Iterable<Document> itrerable = collection_list.listIndexes();
        List<Document> indexes = new ArrayList();
        itrerable.forEach((doc) -> {
            indexes.add(doc);
        });
//        checkTimeToMethodComplete(startTime);
        return indexes;
    }

    /**
     * This method is used to rename the collection
     *
     * @param originalName is the current name of the collection.
     * @param newName      is the new name of the collection.
     * @return true
     * @throws Exception this method might throw exception
     */
    public boolean renameCollection(String originalName, String newName) throws Exception {
        MongoNamespace mongoNamespace = new MongoNamespace(mongoRepository.getDB().getName(), newName);
        mongoRepository.getDB().getCollection(originalName).renameCollection(mongoNamespace);
        return true;
    }

    /**
     * This method is used to check the time taken by APIs to get the result.
     *
     * @param startTime time in milliseconds when API starts
     */
//    public void checkTimeToMethodComplete(long startTime) {
//        long timeTaken = CommonUtils.getCurrentTimeInMillis() - startTime;
//        if (timeTaken > applicationConsulService.getMongoDebugTime()) {
//            LOG.info(LOGGER_PREFIX + "MONGODB fetch time :- " + timeTaken + " milliseconds " + LOGGER_SUFFIX);
//        }
//    }


}


