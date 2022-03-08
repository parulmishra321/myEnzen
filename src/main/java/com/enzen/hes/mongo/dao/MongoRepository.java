/* **********************************************************************
 * 83incs CONFIDENTIAL
 **********************************************************************
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
package com.enzen.hes.mongo.dao;

import com.enzen.hes.mongo.config.MongoConfig;
import com.enzen.hes.mongo.constants.MongoConstants;
import com.enzen.hes.mongo.enums.Order;
import com.mongodb.*;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.*;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.enzen.hes.mongo.utils.MongoUtils.nonNullAndEmpty;


/**
 * This class is designed to provide methods for mongodb that were used throughout the project.
 */

@Service
public class MongoRepository {
    private static final Logger LOG = LogManager.getLogger();
    public static Map<String, MongoClient> mongoClients = new HashMap<>();
    @Autowired
    private MongoConfig mongoConfig;

    public MongoClient getMongoClient() {
        CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(MongoClient.getDefaultCodecRegistry(),
                CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        MongoClientOptions.Builder mongoClientBuilder = MongoClientOptions.builder().writeConcern(WriteConcern.JOURNALED).codecRegistry(pojoCodecRegistry);

        MongoCredential mongoCredential = MongoCredential.createCredential(mongoConfig.getMongodbDatabaseUsernameAdmin(), mongoConfig.getMongodbDatabaseName(), mongoConfig.getMongodbDatabasePasswordAdmin().toCharArray());
        List<ServerAddress> serverAddressList = new ArrayList<>();
        serverAddressList.add(new ServerAddress(mongoConfig.getMongodbServer(), Integer.valueOf(mongoConfig.getMongodbPort())));

        return new MongoClient(serverAddressList, mongoCredential, mongoClientBuilder.build());
    }


    /**
     * This method is used to get mongodb  (databaseName).
     *
     * @return databaseName.
     */
    public MongoDatabase getDB() {
        MongoClient mongoClient = mongoClients.get(mongoConfig.getMongodbDatabaseName());
        if (mongoClient == null) {
            mongoClient = getMongoClient();
        }
        mongoClients.put(mongoConfig.getMongodbDatabaseName(), mongoClient);
        return mongoClient.getDatabase(mongoConfig.getMongodbDatabaseName());
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
    public void createDatabase(String hostname, String port, String dbName) {
        MongoClient mongo = new MongoClient(hostname, Integer.valueOf(port));
        MongoDatabase db = mongo.getDatabase(dbName);
        mongo.close();
    }

    /**
     * This method is used to find distinct values from collection with a particular fieldName.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @param fieldName  is the field from which distinct values have been fetched.
     * @param filter     the query filter.
     * @param clazz      is casting it to the given Class.
     * @return list of distinct values.
     */
    public List distinct(String collection, String fieldName, Document filter, Class clazz) {
        return (List) getDB().getCollection(collection).distinct(fieldName, filter, clazz).into(new ArrayList<>());
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
    public List findDistinct(String collection, Document query, String field, Class type) {
        if (Objects.isNull(query)) {
            query = new Document();
        }
        List list = (List) getDB().getCollection(collection).distinct(field, query, type).into(new ArrayList());
        return list;
    }

    /**
     * This method is used to find list of values from mongodb on the basis of collectionName with different-different
     * parameters.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @param query      the query filter.
     * @param projection means selecting only the necessary data rather than selecting whole of the data of a document.
     * @param sort       is used to sort data either in ascending or descending order on the basis of some specific field.
     * @param offset     is specifically used for pagination it is an input integer argument
     *                   that specifies the number of documents to be skipped in a result set
     * @param max        is specifically used for pagination it is the size of documents to be displayed in a page.
     * @return it returns list of documents.
     */
    public List<Document> findList(String collection, Document query, Document projection, Document sort, Integer offset, Integer max) {
        if (Objects.isNull(query)) {
            query = new Document();
        }
        FindIterable<Document> iterable = getDB().getCollection(collection).find(query);
        if (Objects.nonNull(projection)) {
            iterable = iterable.projection(projection);
        }
        if (Objects.nonNull(sort)) {
            iterable = iterable.sort(sort);
        }
        if (Objects.nonNull(offset)) {
            iterable = iterable.skip(offset);
        }
        if (Objects.nonNull(max)) {
            iterable = iterable.limit(max);
        }
        List<Document> documents = iterable.into(new ArrayList<Document>());
        return documents;
    }

    /**
     * This method is used to find single value from mongodb collection based on different-different parameters.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @param query      the query filter.
     * @param projection means selecting only the necessary data rather than selecting whole of the data of a document.
     * @param sort       is used to sort data either in ascending or descending order on the basis of some specific field.
     * @return document
     */
    public Document findOne(String collection, Document query, Document projection, Document sort) {
        if (Objects.isNull(query)) {
            query = new Document();
        }
        FindIterable<Document> iterable = getDB().getCollection(collection).find(query);
        if (Objects.nonNull(projection)) {
            iterable = iterable.projection(projection);
        }
        if (Objects.nonNull(sort)) {
            iterable = iterable.sort(sort);
        }
        Document document = iterable.first();
        return Objects.nonNull(document) ? document : new Document();
    }

    /**
     * This method is used to replaces the value of a field with the specified value.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @param query      the query filter.
     * @param set        this document has key and value which is either updated with previous value or inserted
     *                   if key does not exists.
     * @param multi      it is a boolean value which is used to decide to update single element or multiple element.
     * @param upsert     if true then new field will be added.
     * @return UpdateResult
     */

    public UpdateResult updateToSet(String collection, Document query, Document set, boolean multi, boolean upsert) {
        UpdateResult updateResult = null;
        long t1 = Calendar.getInstance().getTimeInMillis();
        Document update = new Document();
        if (nonNullAndEmpty(set)) {
            update.put("$set", set);
            updateResult = updateClauses(collection, query, update, multi, upsert, t1);
        } else {
            LOG.error("Please specify value in set");
        }
        return updateResult;
    }

    /**
     * This method is used to update a collection with an intention to remove a particular field.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @param query      the query filter.
     * @param unset      it specifies the field(s) which is going to be deleted.
     * @param multi      it is a boolean value which is used to decide to update single element or multiple element.
     * @param upsert     if true then new field will be added.
     * @return UpdateResult
     */
    public UpdateResult updateToUnset(String collection, Document query, Document unset, boolean multi, boolean upsert) {
        UpdateResult updateResult = null;
        long t1 = Calendar.getInstance().getTimeInMillis();
        Document update = new Document();
        if (nonNullAndEmpty(unset)) {
            update.put("$unset", unset);
            updateResult = updateClauses(collection, query, update, multi, upsert, t1);
        } else {
            LOG.error("Please specify value in unset");
        }
        return updateResult;
    }

    /**
     * This method is used to update collection with a specified value to an array.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @param query      the query filter.
     * @param push       used to append a specified value to an array.
     * @param multi      it is a boolean value which is used to decide to update single element or multiple element.
     * @param upsert     if true then new field will be added.
     * @return UpdateResult.
     */
    public UpdateResult updateToPush(String collection, Document query, Document push, boolean multi, boolean upsert) {
        UpdateResult updateResult = null;
        long t1 = Calendar.getInstance().getTimeInMillis();
        Document update = new Document();
        if (nonNullAndEmpty(push)) {
            update.put("$push", push);
            updateResult = updateClauses(collection, query, update, multi, upsert, t1);
        } else {
            LOG.error("Please specify value in push");
        }
        return updateResult;
    }

    /**
     * This method is used to update collection when user wants to remove values from array.
     * In this case value(s) in `pull` will be removed from an array.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @param query      the query filter.
     * @param pull       is used to remove from an existing array all instances of a value or values that match a specified condition.
     * @param multi      it is a boolean value which is used to decide to update single element or multiple element.
     * @param upsert     if true then new field will be added.
     * @return UpdateResult
     */
    public UpdateResult updateToPull(String collection, Document query, Document pull, boolean multi, boolean upsert) {
        UpdateResult updateResult = null;
        long t1 = Calendar.getInstance().getTimeInMillis();
        Document update = new Document();
        if (nonNullAndEmpty(pull)) {
            update.put("$pull", pull);
            updateResult = updateClauses(collection, query, update, multi, upsert, t1);
        } else {
            LOG.error("Please specify value in push");
        }
        return updateResult;
    }

    /**
     * This method is used to update document by a specified value.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @param query      the query filter.
     * @param increment  operator is used to increment the value of a field by a specified amount.
     *                   or adds as a new field when the specified field does not exist,
     *                   and sets the field to the specified amount
     * @param multi      it is a boolean value which is used to decide to update single element or multiple element.
     * @param upsert     if true then new field will be added.
     * @return UpdateResult
     */
    public UpdateResult updateToIncrement(String collection, Document query, Document increment, boolean multi, boolean upsert) {
        UpdateResult updateResult = null;
        long t1 = Calendar.getInstance().getTimeInMillis();
        Document update = new Document();
        if (nonNullAndEmpty(increment)) {
            update.put("$inc", increment);
            updateResult = updateClauses(collection, query, update, multi, upsert, t1);
        } else {
            LOG.error("Please specify value in push");
        }
        return updateResult;
    }

    /**
     * This method is used to ensure that there are no duplicate items added to the set.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @param query      the query filter.
     * @param addToSet   This is used to adds a value to an array unless the value is already present,
     *                   if value alredy exists it does nothing to that array.
     * @param multi      it is a boolean value which is used to decide to update single element or multiple element.
     * @param upsert     if true then new field will be added.
     * @return UpdateResult
     */
    public UpdateResult addToSet(String collection, Document query, Document addToSet, boolean multi, boolean upsert) {
        UpdateResult updateResult = null;
        long t1 = Calendar.getInstance().getTimeInMillis();
        Document update = new Document();
        if (nonNullAndEmpty(addToSet)) {
            update.put("$addToSet", new Document("$each", addToSet));
            updateResult = updateClauses(collection, query, update, multi, upsert, t1);
        } else {
            LOG.error("Please specify value in addToSet");
        }
        return updateResult;
    }

    /**
     * This method is used to remove all instances of the specified values from an existing array.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @param query      the query filter.
     * @param pullAll    this is used to removes all instances of the specified values from an existing array.
     * @param multi      it is a boolean value which is used to decide to update single element or multiple element.
     * @param upsert     if true then new field will be added.
     * @return UpdateResult
     */

    public UpdateResult updateToPullAll(String collection, Document query, Document pullAll, boolean multi, boolean upsert) {
        UpdateResult updateResult = null;
        long t1 = Calendar.getInstance().getTimeInMillis();
        Document update = new Document();
        if (nonNullAndEmpty(pullAll)) {
            update.put("$pullAll", pullAll);
            updateResult = updateClauses(collection, query, update, multi, upsert, t1);
        } else {
            LOG.error("Please specify value in addToSet");
        }
        return updateResult;
    }

    /**
     * This method is used to update the document(s) of collection. Based on parameters.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @param filter     the query filter.
     * @param set        this document has key and value which is either updated with previous value or inserted
     *                   if key does not exists.
     * @param unset      it specifies the field(s) which is going to be deleted.
     * @param push       used to append a specified value to an array.
     * @param pull       is used to remove from an existing array all instances of a value or values that match a specified condition.
     * @param pushToSet  This is used to adds a value to an array unless the value is already present,
     *                   if value alredy exists it does nothing to that array.
     * @param increment  operator is used to increment the value of a field by a specified amount.
     *                   or adds as a new field when the specified field does not exist,
     *                   and sets the field to the specified amount
     * @param multi      it is a boolean value which is used to decide to update single element or multiple element.
     * @return UpdateResult
     */
    public UpdateResult update(String collection, Document filter, Document set, Document unset, Document push, Document pull, Document pushToSet, Document increment, boolean multi) {
        return update(collection, filter, set, unset, push, pull, pushToSet, increment, multi, false);
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
     * @param pull       is used to remove from an existing array all instances of a value or values that match a specified condition.
     * @param pushToSet  This is used to adds a value to an array unless the value is already present,
     *                   if value alredy exists it does nothing to that array.
     * @param increment  operator is used to increment the value of a field by a specified amount.
     *                   or adds as a new field when the specified field does not exist,
     *                   and sets the field to the specified amount
     * @param multi      it is a boolean value which is used to decide to update single element or multiple element.
     * @param upsert     if true then new field will be added.
     * @return UpdateResult
     */
    public UpdateResult update(String collection, Document filter, Document set, Document unset, Document push, Document pull, Document pushToSet, Document increment, boolean multi, boolean upsert) {
        long t1 = Calendar.getInstance().getTimeInMillis();

        Document update = new Document();
        if (Objects.nonNull(set)) {
            update.put("$set", set);
        }
        if (Objects.nonNull(pull)) {
            update.put("$pull", pull);
        }
        if (Objects.nonNull(unset)) {
            update.put("$unset", unset);
        }
        if (Objects.nonNull(push)) {
            update.put("$push", push);
        }
        if (Objects.nonNull(pushToSet) && !pushToSet.isEmpty()) {
            update.append("$addToSet", pushToSet);
        }
        if (Objects.nonNull(increment)) {
            update.put("$inc", increment);
        }
        return updateClauses(collection, filter, update, multi, upsert, t1);
    }

    /**
     * This method is used internally to update documents.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @param filter     the query filter.
     * @param update     key(s) which needs to be updated.
     * @param multi      it is a boolean value which is used to decide to update single element or multiple element.
     * @param upsert     if true then new field will be added.
     * @param t1         is the time in milliseconds.
     * @return UpdateResult
     */
    private UpdateResult updateClauses(String collection, Document filter, Document update, boolean multi, boolean upsert, long t1) {
        UpdateResult updateResult = null;
        UpdateOptions updateOptions = new UpdateOptions().upsert(upsert);
        if (multi) {
            updateResult = getDB().getCollection(collection).updateMany(filter, update, updateOptions);
        } else {
            updateResult = getDB().getCollection(collection).updateOne(filter, update, updateOptions);
        }
        long t2 = Calendar.getInstance().getTimeInMillis();
        if ((t2 - t1) > MongoConstants.MONGODB_QUERY_LOG_MAX_TIME) {
            LOG.info("Collection Name :: \t" + collection + "Query :::" + "UPDATE" + "-----Time Taken ::: " + (t2 - t1));
        }
        return updateResult;
    }

    /**
     * This method is used to replace document of collection with another document.
     *
     * @param collection  is the name of the collection on which operation is being performed.
     * @param filter      the query filter.
     * @param replacement document with new values that needs to be updated from previous values.
     * @param upsert      if true then new field will be added.
     * @return UpdateResult
     */
    public UpdateResult replaceDocument(String collection, Document filter, Document replacement, boolean upsert) {
        UpdateResult updateResult = null;
        long t1 = Calendar.getInstance().getTimeInMillis();
        if (Objects.isNull(filter)) {
            filter = new Document();
        }
        if (nonNullAndEmpty(replacement)) {
            ReplaceOptions replaceOptions = new ReplaceOptions().upsert(upsert);
            updateResult = getDB().getCollection(collection).replaceOne(filter, replacement, replaceOptions);
        } else {
            LOG.error("Please specify value in addToSet");
        }
        long t2 = Calendar.getInstance().getTimeInMillis();
        if ((t2 - t1) > MongoConstants.MONGODB_QUERY_LOG_MAX_TIME) {
            LOG.info("Collection Name :: \t" + collection + "Query :::" + "UPDATE" + "-----Time Taken ::: " + (t2 - t1));
        }
        return updateResult;
    }

    /**
     * This method is used to get collectionName.
     *
     * @param type is casting to the given Class.
     * @return collectionName.
     */
    public String getCollectionName(Class type) {
        Document document = (Document) type.getAnnotation(Document.class);
        String collection = "";
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
    public List<String> getMongoCollections() {
        return getDB().listCollectionNames().into(new ArrayList<String>());
    }

    /**
     * This method is used to get all the indexes of a collection in mongodb.
     *
     * @param Collection is the name of the collection on which operation is being performed.
     * @return list of indexes.
     */
    public List<Document> getAllIndex(String Collection) {
        MongoCursor<Document> cursor = getDB().getCollection(Collection).listIndexes().iterator();
        List<Document> pipeline = new ArrayList<>();
        try {
            while (cursor.hasNext()) {
                pipeline.add(cursor.next());
            }
        } finally {
            cursor.close();
        }
        return pipeline;
    }

    /**
     * This method is used to drop collection of a mongodb.
     *
     * @param collection is the name of the collection on which operation is being performed.
     */
    public void dropCollection(String collection) {
        getDB().getCollection(collection).drop();
    }

    /**
     * Thi method is used to insert single document in a collection of mongodb where class is used to define
     * the structure of collection.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @param clazz      is casting it to the given Class.
     * @param data       document to be inserted in collection.
     */
    public void insertOne(String collection, Class clazz, Object data) {
        getDB().getCollection(collection, clazz).insertOne(data);
    }

    /**
     * This method is used to insert single document in a collection of mongodb.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @param document   document to be inserted in collection.
     */
    public void insertOne(String collection, Document document) {
        getDB().getCollection(collection).insertOne(document);
    }

    /**
     * This method is used to insert multiple documents in mongodb collection at same time.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @param documents  to be inserted in collection.
     */
    public void insertMany(String collection, List<Document> documents) {
        getDB().getCollection(collection).insertMany(documents);
    }

    /**
     * This method is used to insert multiple documents at same time where class is used to
     * define the structure of collection.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @param clazz      is casting it to the given Class.
     * @param documents  to be inserted in collection.
     */
    public void insertMany(String collection, Class clazz, List<Object> documents) {
        getDB().getCollection(collection, clazz).insertMany(documents);
    }

    /*DELETION METHODS*/

    /**
     * This method is used to delete single document from a collection of mongodb.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @param filter     the query filter.
     * @return DeleteResult
     */
    public DeleteResult deleteOne(String collection, Document filter) {
        return getDB().getCollection(collection).deleteOne(filter);
    }

    /**
     * This method is used to delete multiple documents from mongodb.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @param filter     the query filter.
     * @return DeleteResult
     */
    public DeleteResult deleteMany(String collection, Document filter) {
        return getDB().getCollection(collection).deleteMany(filter);
    }

    /**
     * This method is used to fetch data from mongodb using aggregation.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @param projection means selecting only the necessary data rather than selecting whole of the data of a document.
     * @param match      Filters the documents to pass only the documents that match the specified condition(s).
     * @param group      documents in a collection by the specified keys and performs simple aggregation functions.
     * @param sort       is used to sort data either in ascending or descending order on the basis of some specific field.
     * @return list od document.
     */
    public List<Document> aggregate(String collection, Document projection, Document match, Document group, Document sort) {
        List<Document> pipeline = new ArrayList<>();

        if (Objects.nonNull(projection)) {
            pipeline.add(new Document("$project", projection));
        }
        if (Objects.nonNull(match)) {
            pipeline.add(new Document("$match", match));
        }
        if (Objects.nonNull(group)) {
            pipeline.add(new Document("$group", group));
        }
        if (Objects.nonNull(sort)) {
            pipeline.add(new Document("$sort", sort));
        }
        AggregateIterable<Document> iterable = getDB().getCollection(collection).aggregate(pipeline);

        return iterable.into(new ArrayList<>());
    }

    /**
     * This method is used to count the number of documents in a collection of mongodb.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @param match      Filters the documents to pass only the documents that match the specified condition(s).
     * @return count of documents.
     */
    public long count(String collection, Document match) {
        return Objects.nonNull(match) ? getDB().getCollection(collection).estimatedDocumentCount()  : -1;
    }

    /**
     * This method is used to create index in a collection of mongodb.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @param field      from which distinct values have been fetched.
     * @param order      is used to decide to sort either in ascending or descending order.
     * @param unique     ensures that the indexed fields do not store duplicate values.
     * @return index
     */
    public String createIndex(String collection, String field, Order order, boolean unique) {
        Document index = new Document();
        IndexOptions options = new IndexOptions();
        options.unique(unique);
        index.put(field, order.getValue());
        return getDB().getCollection(collection).createIndex(index, options);
    }

    /**
     * This method is used to create index of a collection in mongodb.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @param index      is a special data structure that holds the data of few fields of documents on which the index is created.
     *                   Indexes improve the speed of search operations in database because instead of searching the whole document,
     *                   the search is performed on the indexes that holds only few fields.
     * @param unique     ensures that the indexed fields do not store duplicate values
     * @return created index.
     */
    public String createIndex(String collection, Document index, boolean unique) {
        IndexOptions options = new IndexOptions();
        options.unique(unique);
        return getDB().getCollection(collection).createIndex(index, options);
    }

    /**
     * This method is used to create multiple indexes of a collection in mongodb.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @param indexList  is used to create multiple indexes that holds the data of few fields
     *                   of documents on which the index is created.
     * @return list of created indexes.
     */
    public List<String> createIndexes(String collection, List<MongoIndex> indexList) {
        List<IndexModel> indexes = new ArrayList<>();
        indexList.forEach(i -> {
            Document index = new Document();
            index.put(i.getField(), i.getOrder().getValue());
            IndexModel indexModel = new IndexModel(index, new IndexOptions().unique(i.isUnique()));
            indexes.add(indexModel);
        });
        return getDB().getCollection(collection).createIndexes(indexes);
    }

    /**
     * This method is used to create capped collection in mongodb which means once a
     * collection fills its allocated space, it makes room for new documents by
     * overwriting the oldest documents in the collection.
     *
     * @param collection is the name of the collection on which operation is being performed.
     * @param size       is the allocated space for collection.
     * @param max        is the number of documents that can be stored.
     */
    public void createCappedCollection(String collection, long size, long max) {
        CreateCollectionOptions options = new CreateCollectionOptions();
        options.capped(true);
        options.sizeInBytes(size);
        options.maxDocuments(max);
        getDB().createCollection(collection, options);
    }


    /**
     * This method is used to define time to live of a collection. After expiration time
     * collection will be deleted automatically from mongodb.
     *
     * @param collection     is the name of the collection on which operation is being performed.
     * @param field          from which distinct values have been fetched.
     * @param expirationTime is the time after collection will be deleted automatically.
     */
    public void createTTLForCollection(String collection, String field, Long expirationTime) {
        getDB().getCollection(collection).createIndex(new Document(field, 1), new IndexOptions().expireAfter(expirationTime, TimeUnit.SECONDS));
    }


    /**
     * This method is used to create a new collection in mongodb.
     *
     * @param collection is the name of the collection on which operation is being performed.
     */
    public void createCollection(String collection) {
        getDB().createCollection(collection);
    }

    /**
     * This method is used to drop indexes of collection of mongodb.
     *
     * @param collection is the name of the collection on which operation is being performed.
     */
    public void dropIndexes(String collection) {
        getDB().getCollection(collection).dropIndexes();
    }
}





