package com.mindup.core.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;


@FeignClient(value = "gateway", path = "/api/message")
public interface ChatFeignClient {

    @PostMapping("/join-professional/{professionalId}")
    ResponseEntity<?> joinProfessional(@PathVariable("professionalId") String id)throws IOException;
}