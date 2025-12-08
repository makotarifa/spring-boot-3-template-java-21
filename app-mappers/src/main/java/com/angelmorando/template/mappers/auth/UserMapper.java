package com.angelmorando.template.mappers.auth;

import com.angelmorando.template.domain.auth.User;
import com.angelmorando.template.persistence.auth.model.UserRow;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toDomain(UserRow row);
    @org.mapstruct.Mapping(target = "createdAt", ignore = true)
    @org.mapstruct.Mapping(target = "updatedAt", ignore = true)
    UserRow toRow(User user);
    List<User> toDomainList(List<UserRow> rows);
    List<UserRow> toRowList(List<User> users);
}
