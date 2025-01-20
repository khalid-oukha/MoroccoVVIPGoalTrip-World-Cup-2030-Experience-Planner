package com.moroccanvviptrip.api.mvtapi.web.mapper;

import com.moroccanvviptrip.api.mvtapi.domain.User;
import com.moroccanvviptrip.api.mvtapi.web.VM.UserResponseDto;
import com.moroccanvviptrip.api.mvtapi.web.dto.request.SignUpRequest;
import com.moroccanvviptrip.api.mvtapi.web.dto.request.SignUpRequestDto;
import org.mapstruct.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    User toEntity(SignUpRequest signUpRequest);

    @Mapping(target = "authorities", source = "authorities", qualifiedByName = "mapAuthorities")
    UserResponseDto toResponse(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdate(SignUpRequestDto signUpRequestDto, @MappingTarget User user);

    @Named("mapAuthorities")
    default List<String> mapAuthorities(Collection<? extends GrantedAuthority> authorities) {
        if (authorities == null) {
            return null;
        }
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }
}