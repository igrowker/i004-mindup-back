package com.mindup.chat.feign;

import com.mindup.core.dtos.User.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "gateway", path = "/api/core/")
public interface CoreFeignClient {

    @PostMapping("/user/professional/{id}")
    ResponseEntity<Boolean> findProfessionalById(@PathVariable String id);

    @PostMapping("/user/availability/{professionalId}")
    ResponseEntity<UserDTO> toggleAvailability(@PathVariable String professionalId);

}