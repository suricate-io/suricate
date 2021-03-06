/*
 * Copyright 2012-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.suricate.monitoring.services.mapper;

import io.suricate.monitoring.model.dto.api.widget.LibraryResponseDto;
import io.suricate.monitoring.model.entities.Library;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Interface that manage the generation DTO/Model objects for library class
 */
@Mapper(componentModel = "spring")
public abstract class LibraryMapper {

    /**
     * Map a library into a DTO
     *
     * @param library The library to map
     * @return The library as DTO
     */
    @Named("toLibraryDTO")
    @Mapping(target = "assetToken", expression = "java( library.getAsset() != null ? io.suricate.monitoring.utils.IdUtils.encrypt(library.getAsset().getId()) : null )")
    public abstract LibraryResponseDto toLibraryDTO(Library library);
}
