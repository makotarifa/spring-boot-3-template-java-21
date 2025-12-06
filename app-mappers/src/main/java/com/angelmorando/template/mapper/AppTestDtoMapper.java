package com.angelmorando.template.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.angelmorando.template.domain.model.AppTest;
import com.angelmorando.template.dto.AppTestDto;

@Mapper(componentModel = "spring")
public interface AppTestDtoMapper {
    AppTestDto toDto(AppTest domain);
    AppTest toDomain(AppTestDto dto);
    List<AppTestDto> toDtoList(List<AppTest> list);
}
