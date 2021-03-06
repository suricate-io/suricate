/*
 *
 *  * Copyright 2012-2018 the original author or authors.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package io.suricate.monitoring.utils.exceptions.nashorn;

import lombok.Getter;

public class RequestException extends Exception {

    /**
     * Technical message of the request exception
     */
    @Getter
    private final String technicalData;

    /**
     * Response body of the request exception
     */
    @Getter
    private final String response;

    /**
     * Constructor
     *
     * @param technicalData The technical data
     * @param response The response body
     */
    public RequestException(String technicalData, String response) {
        super(technicalData);
        this.technicalData = technicalData;
        this.response = response;
    }
}
