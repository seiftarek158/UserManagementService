package net.atos.usrmanagementservice.mappers;

import net.atos.usrmanagementservice.dto.UserSignUpDto;
import net.atos.usrmanagementservice.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface toUserSignUpDto {
    User toEntity(UserSignUpDto userSignUpDto);
    UserSignUpDto toDto(User user);

}
