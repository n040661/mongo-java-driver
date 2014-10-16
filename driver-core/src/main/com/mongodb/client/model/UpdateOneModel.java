/*
 * Copyright (c) 2008-2014 MongoDB, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mongodb.client.model;

import static com.mongodb.assertions.Assertions.notNull;

/**
 * A model describing an update to at most one document that matches the query criteria. The update to apply must include only update
 * operators.
 *
 * @param <T> the type of document to update.  In practice this doesn't actually apply to updates but is here for consistency with the other
 *            write models
 * @mongodb.driver.manual tutorial/modify-documents/ Updates
 * @mongodb.driver.manual reference/operator/update/ Update Operators
 * @since 3.0
 */
public final class UpdateOneModel<T> extends WriteModel<T> {
    private final Object criteria;
    private final Object update;
    private final UpdateOptions options;

    /**
     * Construct a new instance.
     *
     * @param criteria a document describing the query criteria, which may not be null. This can be of any type for which a {@code Codec} is
     *                 registered
     * @param update   a document describing the update, which may not be null. The update to apply must include only update operators. This
     *                 can be of any type for which a {@code Codec} is registered
     */
    public UpdateOneModel(final Object criteria, final Object update) {
        this(criteria, update, new UpdateOptions() {
            /**
             * Set to true if a new document should be inserted if there are no matches to the query criteria.
             *
             * @param upsert true if a new document should be inserted if there are no matches to the query criteria
             * @return this
             */
            public UpdateOptions upsert(final boolean upsert) {
                super.upsert(upsert);
                return this;
            }
        });
    }

    /**
     * Construct a new instance.
     *
     * @param criteria a document describing the query criteria, which may not be null. This can be of any type for which a {@code Codec} is
     *                 registered
     * @param update   a document describing the update, which may not be null. The update to apply must include only update operators. This
     *                 can be of any type for which a {@code Codec} is registered
     * @param options the options to apply
     */
    public UpdateOneModel(final Object criteria, final Object update, final UpdateOptions options) {
        this.criteria = notNull("criteria", criteria);
        this.update = notNull("update", update);
        this.options = notNull("options", options);
    }

    /**
     * Gets the query criteria.
     *
     * @return the query criteria
     */
    public Object getCriteria() {
        return criteria;
    }

    /**
     * Gets the document specifying the updates to apply to the matching document.  The update to apply must include only update operators.
     *
     * @return the document specifying the updates to apply
     */
    public Object getUpdate() {
        return update;
    }

    /**
     * Gets the options to apply.
     *
     * @return the options
     */
    public UpdateOptions getOptions() {
        return options;
    }
}