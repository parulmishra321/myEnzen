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

import com.enzen.hes.mongo.commons.CustomIndexOptions;
import com.enzen.hes.mongo.enums.Order;
import com.enzen.hes.mongo.result.DeleteDocResult;
import com.enzen.hes.mongo.result.UpdateDocResult;
import com.mongodb.client.model.DropIndexOptions;
import org.bson.Document;

import java.util.*;

/**
 * It is used to declare method for Mongo
 * <br><br>{@link #listCollections()} This method is used to get the list of collection.
 * <br><br>{@link #count(String, Document, Document)} This method is used to count the number of documents in a collection of mongodb.
 * <br><br>{@link #count(Document, Document, String)} This method is used to count the number of documents in a collection of mongodb.
 * <br><br>{@link #findOne(String, Document, Document)} This method is used to find single value from mongodb collection based on different-different parameters.
 * <br><br>{@link #findOne(String, Document, Document, String, Integer)} This method is used to find single value from mongodb collection based on different-different parameters.
 * <br><br>{@link #findById(String, String, Document)} This method is used to find the data from mongodb database on the basis of collection name, id, and projection.
 * <br><br>{@link #insertAll(String, List)} This method is used to insert multiple documents in mongodb collection at same time.
 * <br><br>{@link #read(Class, Document)} This method is used to find single value from mongodb collection based on different-different parameters.
 * <br><br>{@link #create(String, Document)} This method is used to insert document in a collection of mongodb.
 * <br><br>{@link #create(String)} This method is used to create a new collection in mongodb.
 * <br><br>{@link #update(String, Document, Document, Document, Document, Document, boolean)} This method is  used to update the document(s) of collection. Based on parameters.
 * <br><br>{@link #update(String, Document, Document, Document, Document, Document, boolean, boolean)} This method is  used to update the document(s) of collection. Based on parameters.
 * <br><br>{@link #deleteOne(String, Document)} This method is used to delete single document from a collection of mongodb.
 * <br><br>{@link #deleteMany(String, Document)} This method is used to delete multiple documents from mongodb.
 * <br><br>{@link #deleteById(String, String)} This method is used to delete single document from a collection of mongodb.
 * <br><br>{@link #findDistinct(String, String, Class)} This method is used to find distinct values from collection with a particular fieldName.
 * <br><br>{@link #findDistinct(String, Document, String, Class)} This method is used to find distinct values from collection with a particular fieldName.
 * <br><br>{@link #findList(String, Document, Document, String, Integer)} This method is used to find list of values from mongodb on the basis of collectionName with different-different parameters.
 * <br><br>{@link #findList(String, Document, Document, Integer, Integer, Document)} This method is used to find list of values from mongodb on the basis of collectionName with different-different parameters.
 * <br><br>{@link #findList(String, Document, Document, Document, Integer, Integer, String, Integer)} This method is used to find list of values from mongodb on the basis of collectionName with different-different parameters.
 * <br><br>{@link #findList(String, String, Integer, Integer, Document)} This method is used to find list of values from mongodb on the basis of collectionName with different-different parameters.
 * <br><br>{@link #findList(String, Document, Document)} This method is used to find list of values from mongodb on the basis of collectionName with different-different parameters.
 * <br><br>{@link #findList(String, Document, Document, Document, Integer, Integer)} This method is used to find list of values from mongodb on the basis of collectionName with different-different parameters.
 * <br><br>{@link #findAll(String, Document)} This method is used to find list of values from mongodb on the basis of collectionName with different-different parameters.
 * <br><br>{@link #findList(String, Document, Document, String, int, int, int, Document)} This method is used to find list of values from mongodb on the basis of collectionName with different-different parameters.
 * <br><br>{@link #findList(String, Document, Document, Document)} This method is used to find list of values from mongodb on the basis of collectionName with different-different parameters.
 * <br><br>{@link #findListByDuration(String, Document, Document, String, int, int)} This method is used to find list of values from mongodb on the basis of timestamp with different-different parameters.
 * <br><br>{@link #findListByTimestamp(String, Document, String, String, long, Document, String, Integer)} This method is used to find list of values from mongodb on the basis of timestamp with different-different parameter.
 * <br><br>{@link #findList(String, String, String, String, int, int, Document)} This method is used to find list of values from mongodb on the basis of timestamp with different-different parameter.
 * <br><br>{@link #findList(String, Document, String, String, String, int, Document)} This method is used to find list of values from mongodb on the basis of timestamp with different-different parameter.
 * <br><br>{@link #updateDocumentFields(String collection, Document filter, Document set, boolean multi, boolean upsert)} This method is used to update the value of a field with the specified value.
 * <br><br>{@link #removeDocumentFields(String, Document, Document, boolean)} This method is used to update a collection with an intention to remove a particular field.
 * <br><br>{@link #updateArrayFields(String, Document, Document, boolean, boolean)} This method is used to update collection with a specified value to an array.
 * <br><br>{@link #removeArrayFields(String, Document, Document, boolean)} This method is used to update collection when user wants to remove values from array.
 * <br><br>{@link #replaceDocument(String, Document, Document, boolean)} This method is used to replace document of collection with another document.
 * <br><br>{@link #generateHistoricalGraphDataFromDataV2(Document, Document, Document, Document, Document, Document)} This method is used to generate the historical GraphData
 * <br><br>{@link #createDatabase(String, String, String)} This method is used to create a new database in mongodb.
 * <br><br>{@link #getCollectionName(Class)} This method is used to get collectionName.
 * <br><br>{@link #getAllCollectionNames()} This method is used to get all the collectionNames of particular database in mongodb.
 * <br><br>{@link #getAllIndex(String)} This method is used to get all the indexes of a collection in mongodb.
 * <br><br>{@link #isCollectionExist(String)} This method is used to check if the given collection name is exist or not.
 * <br><br>{@link #getAggregatedData(String, Document, Document, Document, Document)} This method is used to fetch data from mongodb using aggregation.
 * <br><br>{@link #aggregate(Document, Document, Document, String, Integer, String)} This method is used to fetch data from mongodb using aggregation.
 * <br><br>{@link #aggregate(Document, Document, Document, Document, String)} This method is used to fetch data from mongodb using aggregation
 * <br><br>{@link #createCappedCollection(String, long, long)} This method is used to create capped collection in mongodb which means once a collection fills its allocated space, it makes room for new documents.
 * <br><br>{@link #createTTLForCollection(String, String, Long)} This method is used to define time to live of a collection. After expiration time collection will be deleted automatically from mongodb.
 * <br><br>{@link #createIndex(String, Document, Boolean)} This method is used to create index of a collection in mongodb.
 * <br><br>{@link #dropIndex(String, String, Document, DropIndexOptions)} This method is used to drop the index of collection in mongodb.
 */
@SuppressWarnings("unchecked")
public interface MongoService {

    /**
     * This method is used to get the list of collection.
     *
     * @return collectionData.
     */
    HashMap<String, Set<String>> listCollections();

    /**
     * This method is used to count the number of documents in a collection of mongodb.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @param params     it matches documents where the value of a field equals the specified value.
     * @param inParams   it matches document where atleast one element matches the criteria.
     * @return count of type {Long}.
     */
    long count(String collection, Document params, Document inParams);

    /**
     * This method is used to count the number of documents in a collection of mongodb.
     *
     * @param params       it matches documents where the value of a field equals the specified value.
     * @param searchParams Filters the documents to pass only the documents that match the specified condition(s).
     * @param collection   is the name of the collection on which operation is being performed.
     * @return count of type {Long}.
     */
    long count(Document params, Document searchParams, String collection);

    /**
     * This method is used to find single value from mongodb collection based on different-different parameters.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @param query      the query filter.
     * @param projection means selecting only the necessary data rather than selecting whole of the data of a document.
     * @return result of type {Document}
     */
    Document findOne(String collection, Document query, Document projection);

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
    Document findOne(String collection, Document query, Document projection, String sortBy, Integer sortType);

    /**
     * This method is used to find the data from mongodb database on the basis of collection name, id, and projection.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @param id         it is of String type.
     * @param projection means selecting only the necessary data rather than selecting whole of the data of a document.
     * @return fetched data of type {Document}
     */
    Document findById(String collection, String id, Document projection);

    /**
     * This method is used to insert multiple documents in mongodb collection at same time.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @param recordList to be inserted in collection.
     */
    void insertAll(String collection, List<Document> recordList);

    /**
     * This method is used to insert document in a collection of mongodb.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @param data       document to be inserted in collection.
     * @return true of type {boolean}
     */
    boolean create(String collection, Document data);

    /**
     * This method is used to create a new collection in mongodb.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @return true of type {boolean}
     */
    boolean create(String collection);

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
    UpdateDocResult update(String collection, Document filter, Document set, Document unset, Document push, Document pushToSet, boolean multi);

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
    UpdateDocResult update(String collection, Document filter, Document set, Document unset, Document push, Document pushToSet, boolean multi, boolean upsert);

    UpdateDocResult update(String collection, Document filter, Document set, boolean upsert);

    UpdateDocResult update(String collection, Document filter, Document set);

    /**
     * This method is used to delete single document from a collection of mongodb.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @param filter     the query filter.
     * @return result of type {DeleteDocResult}
     */
    DeleteDocResult deleteOne(String collection, Document filter);

    /**
     * This method is used to delete multiple documents from mongodb.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @param filter     the query filter.
     * @return result of type {DeleteDocResult}
     */
    DeleteDocResult deleteMany(String collection, Document filter);

    /**
     * This method is used to delete single document from a collection of mongodb.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @param id         it consist of id of document
     * @return result of type {DeleteDocResult}
     */
    DeleteDocResult deleteById(String collection, String id);

    /**
     * This method is used to find distinct values from collection with a particular fieldName.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @param field      is the field from which distinct values have been fetched.
     * @param type       is casting it to the given Class.
     * @return list of distinct values.
     */
    List findDistinct(String collection, String field, Class type);

    /**
     * This method is used to find distinct values from collection with a particular fieldName.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @param query      the query filter.
     * @param field      is the field from which distinct values have been fetched.
     * @param type       is casting it to the given Class.
     * @return list of distinct values.
     */
    List findDistinct(String collection, Document query, String field, Class type);

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
    List<Document> findList(String collection, Document matchEq, Document projection, String sortBy, Integer sortType);

    /**
     * This method is used to find list of values from mongodb on the basis of collectionName with different-different
     * parameters.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @param matchEq    the query filter.
     * @param projection means selecting only the necessary data rather than selecting whole of the data of a document.
     * @param sort       the query filter.
     * @param offset     is specifically used for pagination it is an input integer argument
     *                   that specifies the number of documents to be skipped in a result set
     * @param max        is specifically used for pagination it is the size of documents to be displayed in a page.
     * @return it returns list of document.
     */
    List<Document> findList(String collection, Document matchEq, Document projection, Integer offset, Integer max, Document sort);

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
    List<Document> findList(String collection, Document matchEq, Document projection, Document searchParams, Integer offset, Integer max, String sortBy, Integer sortType);

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
    List<Document> findList(String collection, String sortBy, Integer sortType, Integer limit, Document projection);

    /**
     * This method is used to find list of values from mongodb on the basis of collectionName with different-different
     * parameters.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @param query    the query filter.
     * @param projection means selecting only the necessary data rather than selecting whole of the data of a document.
     * @return it returns list of document.
     */
    List<Document> findList(String collection, Document query, Document projection);

    /**
     * This method is used to find list of values from mongodb on the basis of collectionName with different-different
     * parameters.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @param query      the query filter.
     * @param projection means selecting only the necessary data rather than selecting whole of the data of a document.
     * @param sort       the query filter.
     * @param offset     is specifically used for pagination it is an input integer argument that specifies the number of documents to be skipped in a result set
     * @param max        is specifically used for pagination it is the size of documents to be displayed in a page.
     * @return it returns list of document.
     */
    List<Document> findList(String collection, Document query, Document projection, Document sort, Integer offset, Integer max);

    /**
     * This method is used to find list of values from mongodb on the basis of collectionName with different-different
     * parameters.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @param projection means selecting only the necessary data rather than selecting whole of the data of a document.
     * @return it returns list of document.
     */
    List<Document> findAll(String collection, Document projection);

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
    List<Document> findList(String collection, Document matchEq, Document matchIn, String sortBy, int sortType, int limit, int offset, Document projection);

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
    List<Document> findList(String collection, Document matchEq, Document matchIn, Document projection);

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
    List<Document> findListByDuration(String collection, Document matchEq, Document projection, String sortBy, int sortType, int duration);

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
    List<Document> findListByTimestamp(String collection, Document matchEq, String resourceIdOrInClause, String resourceName, long minutes, Document projection, String sortBy, Integer sortType);

    /**
     * This method is used to find list of values from mongodb on the basis of timestamp with different-different
     * parameters.
     *
     * @param collection           is the name of the collection on which operation is being performed.
     * @param resourceIdOrInClause is of String type.
     * @param resourceName         is of String type.
     * @param projection           means selecting only the necessary data rather than selecting whole of the data of a document.
     * @param sortBy               is used to sort data on the basis of some specific field.
     * @param limit                is specifically used for pagination it is the size of documents to be displayed in a page.
     * @param sortType             is used to sort data either in ascending or descending order
     * @return it returns list of document.
     */
    List<Document> findList(String collection, String resourceIdOrInClause, String resourceName, String sortBy, int limit, int sortType, Document projection);

    /**
     * This method is used to find list of values from mongodb on the basis of timestamp with different-different
     * parameters.
     *
     * @param collection           is the name of the collection on which operation is being performed.
     * @param matchEq              the query filter.
     * @param resourceIdOrInClause is of String type.
     * @param resourceName         is of String type.
     * @param sortBy               is used to sort data on the basis of some specific field.
     * @param sortType             is used to sort data either in ascending or descending order
     * @param projection           means selecting only the necessary data rather than selecting whole of the data of a document.
     * @return it returns list of document.
     */
    List<Document> findList(String collection, Document matchEq, String resourceIdOrInClause, String resourceName, String sortBy, int sortType, Document projection);

    /**
     * This method is used to update the value of a field with the specified value.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @param filter     the query filter.
     * @param set        this document has key and value which is either updated with previous value or inserted if key does not exists.
     * @param multi      it is a boolean value which is used to decide to update single element or multiple element.
     * @param upsert     if true then new field will be added.
     */
    void updateDocumentFields(String collection, Document filter, Document set, boolean multi, boolean upsert);

    /**
     * This method is used to update a collection with an intention to remove a particular field.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @param filter     the query filter.
     * @param unset      it specifies the field(s) which is going to be deleted.
     * @param multi      it is a boolean value which is used to decide to update single element or multiple element.
     */
    void removeDocumentFields(String collection, Document filter, Document unset, boolean multi);

    /**
     * This method is used to update collection with a specified value to an array.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @param filter     the query filter.
     * @param push       used to append a specified value to an array.
     * @param multi      it is a boolean value which is used to decide to update single element or multiple element.
     * @param upsert     if true then new field will be added.
     */
    void updateArrayFields(String collection, Document filter, Document push, boolean multi, boolean upsert);

    /**
     * This method is used to update collection when user wants to remove values from array.
     * In this case value(s) in `pull` will be removed from an array.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @param filter     the query filter.
     * @param push       is used to remove from an existing array all instances of a value or values that match a specified condition.
     * @param multi      it is a boolean value which is used to decide to update single element or multiple element.
     */
    void removeArrayFields(String collection, Document filter, Document push, boolean multi);

    /**
     * This method is used to replace document of collection with another document.
     *
     * @param collection  is the name of the collection on which operation is being performed.
     * @param filter      the query filter.
     * @param replacement document with new values that needs to be updated from previous values.
     * @param upsert      if true then new field will be added.
     */
    void replaceDocument(String collection, Document filter, Document replacement, boolean upsert);

    Document aggregateDataForArrayElementsV2(Document queryParams, String collection, Calendar dateCriteria, Document aggregationParams, String timeEntity);

    List<Document> aggregateDataForObject(String collection, Calendar dateCriteria, Document aggregationParams, String timeEntity);

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
    List<Document> generateHistoricalGraphDataFromDataV2(Document today, Document last3Days,
                                                         Document last7Days, Document last10Days, Document last30Days, Document last90Days);

    /**
     * This method is used to create a new database in mongodb .
     *
     * @param hostname (The hostname is what a device is called on a network).
     *                 hostname is basically the name of server where the databases have been created.
     * @param port     A port number is a way to identify a specific process to which an
     *                 Internet or other network message is to be forwarded when it arrives at a server
     * @param dbName   is the name by which user wants to create database.
     */
    void createDatabase(String hostname, String port, String dbName);

    /**
     * This method is used to get collectionName.
     *
     * @param type is casting to the given Class.
     * @return collection of type {String}.
     */
    String getCollectionName(Class type);

    /**
     * This method is used to get all the collectionNames of particular database in mongodb.
     *
     * @return list of collection names.
     */
    List<String> getAllCollectionNames();

    /**
     * This method is used to get all the indexes of a collection in mongodb.
     *
     * @param Collection is the name of the collection on which operation is being performed.
     * @return list of indexes.
     */
    List<Document> getAllIndex(String Collection);

    /**
     * This method is used to check if the given collection name is exist or not.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @return true or false
     */
    boolean isCollectionExist(String collection);


    List<Map<String, Object>> aggregateReportData(String collection, int dateCriteria, long startTime, long endTime, Document aggregationParams, String timeEntity);

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
    List<Document> getAggregatedData(String collection, Document match, Document groupBy, Document projection, Document sort);


    List<Document> getSortedAggregatedData(String collection, Document match, Document groupBy, Document projection, Document sort);


    List<Document> aggregateEntityDataForObject(String collection, Calendar dateCriteria, Document aggregationParams, String timeEntity);

    void getDateEntityCriteria(Calendar dateCriteria, Document aggregationParams, String timeEntity, Document matchCriteria);

    Document getDataMaps(Document data, String day);

    /**
     * This method is used to drop collection of a mongodb.
     *
     * @param collection is the name of the collection on which operation is being performed.
     */
    void dropCollection(String collection);

    void getDateMatchQuery(int dateCriteria, String timeEntity, Document matchCriteria, long startDate, long endDate);

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
    List<Document> aggregate(Document match, Document group, Document projection, String sortBy, Integer sortType, String collection);

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
    List<Document> aggregate(Document match, Document group, Document projection, Document sort, String collection);

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
    boolean createCappedCollection(String collection, long size, long max);

    /**
     * This method is used to define time to live of a collection. After expiration time collection will be deleted automatically from mongodb.
     *
     * @param collection     is the name of the collection on which operation is being performed.
     * @param field          from which distinct values have been fetched.
     * @param expirationTime is the time after collection will be deleted automatically.
     */
    void createTTLForCollection(String collection, String field, Long expirationTime);

    /**
     * This method is used to create index of a collection in mongodb.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @param key        is a special data structure that holds the data of few fields of documents on which the index is created.
     *                   Indexes improve the speed of search operations in database because instead of searching the whole document,
     *                   the search is performed on the indexes that holds only few fields.
     * @param options    it is of type CustomIndexOptions.
     */
    void createIndex(String collection, Document key, CustomIndexOptions options);

    /**
     * This method is used to create index in a collection of mongodb.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @param field      from which distinct values have been fetched.
     * @param order      is used to decide to sort either in ascending or descending order.
     * @param unique     ensures that the indexed fields do not store duplicate values.
     */
    void createIndex(String collection, String field, Order order, Boolean unique);

    /**
     * This method is used to create index of a collection in mongodb.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @param index      is a special data structure that holds the data of few fields of documents on which the index is created.
     *                   Indexes improve the speed of search operations in database because instead of searching the whole document,
     *                   the search is performed on the indexes that holds only few fields.
     * @param unique     ensures that the indexed fields do not store duplicate values
     */
    void createIndex(String collection, Document index, Boolean unique);

    /**
     * This method is used to drop the index of collection in mongodb.
     *
     * @param collection       is the name of the collection on which operation is being performed.
     * @param indexName        is the name of index.
     * @param field            it consist of some document type data.
     * @param dropIndexOptions it of type DropIndexOptions which gives options to apply to the command when dropping indexes.
     */
    void dropIndex(String collection, String indexName, Document field, DropIndexOptions dropIndexOptions);

    long count(String collection, Document match);
}

