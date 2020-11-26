/*
 * Copyright 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.ucab_inscription;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/** Snippets to demonstrate Firestore add, update and delete operations. */
class ManageDataSnippets {
/*
    private final Firestore db;

    ManageDataSnippets(Firestore db) {
        this.db = db;
    }

    /**
     * Add a document to a collection using a map.
     *
     * @return document data
     */
    /*
    Map<String, Object> addSimpleDocumentAsMap() throws Exception {
        // [START fs_add_doc_as_map]
        // [START firestore_data_set_from_map]
        // Create a Map to store the data we want to set
        Map<String, Object> docData = new HashMap<>();
        docData.put("name", "Los Angeles");
        docData.put("state", "CA");
        docData.put("country", "USA");
        docData.put("regions", Arrays.asList("west_coast", "socal"));
        // Add a new document (asynchronously) in collection "cities" with id "LA"
        ApiFuture<WriteResult> future = db.collection("cities").document("LA").set(docData);
        // ...
        // future.get() blocks on response
        System.out.println("Update time : " + future.get().getUpdateTime());
        // [END firestore_data_set_from_map]
        // [END fs_add_doc_as_map]
        return docData;
    }
    */

}