package com.angelmorando.template.mappers.apptest;

import java.util.List;

import org.mapstruct.Mapper;

import com.angelmorando.template.domain.apptest.model.AppTest;
import com.angelmorando.template.dtos.apptest.AppTestDto;

@Mapper(componentModel = "spring")
public interface AppTestDtoMapper {
    AppTestDto toDto(AppTest domain);
    AppTest toDomain(AppTestDto dto);
    List<AppTestDto> toDtoList(List<AppTest> list);
}
