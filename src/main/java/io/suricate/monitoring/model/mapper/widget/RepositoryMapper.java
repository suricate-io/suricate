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

package io.suricate.monitoring.model.mapper.widget;

import io.suricate.monitoring.model.dto.api.repository.RepositoryResponseDto;
import io.suricate.monitoring.model.entity.widget.Repository;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Interface that manage the generation DTO/Model objects for Widget class
 */
@Component
@Mapper(
    componentModel = "spring",
    uses = {
        WidgetMapper.class
    }
)
public abstract class RepositoryMapper {

    /* ******************************************************* */
    /*                  Simple Mapping                         */
    /* ******************************************************* */

    /**
     * Tranform a Repository into a RepositoryResponseDto
     *
     * @param repository The repository to transform
     * @return The related repository DTO
     */
    @Named("toRepositoryDtoDefault")
    @Mappings({
        @Mapping(target = "widgets", qualifiedByName = "toWidgetDtosDefault")
    })
    public abstract RepositoryResponseDto toRepositoryDtoDefault(Repository repository);

    @Named("toRepositoryDtoWithoutWidgets")
    @Mappings({
        @Mapping(target = "widgets", ignore = true)
    })
    public abstract RepositoryResponseDto toRepositoryDtoWithoutWidgets(Repository repository);


    /**
     * Create a repository with a dto without widgets
     *
     * @param repositoryResponseDto The repository dto to transform
     * @return The repository
     */
    @Named("toRepositoryWithoutWidgets")
    @Mappings({
        @Mapping(target = "widgets", ignore = true)
    })
    public abstract Repository toRepositoryWithoutWidgets(RepositoryResponseDto repositoryResponseDto);

    /* ******************************************************* */
    /*                    List Mapping                         */
    /* ******************************************************* */

    /**
     * Tranform a list of repositories into a list of RepositoryResponseDto
     *
     * @param repositories The list of repositories to transform
     * @return The related DTOs
     */
    @Named("toRepositoryDtosDefault")
    @IterableMapping(qualifiedByName = "toRepositoryDtoDefault")
    public abstract List<RepositoryResponseDto> toRepositoryDtosDefault(List<Repository> repositories);
}
