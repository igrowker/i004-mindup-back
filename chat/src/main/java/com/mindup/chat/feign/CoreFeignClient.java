package com.mindup.chat.feign;

import com.mindup.core.dtos.User.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(value = "gateway", path = "/api/core")
public interface CoreFeignClient {

    @GetMapping("/user/professional/{id}")
    ResponseEntity<Boolean> findProfessionalByUserIdAndRole(@PathVariable("id") String id);

    @GetMapping("/user/patient/{id}")
    ResponseEntity<Boolean> findPatientByUserIdAndRole(@PathVariable("id") String id);

    @PutMapping("/user/availability/{professionalId}")
    ResponseEntity<UserDTO> toggleAvailability(@PathVariable("professionalId") String professionalId);

}