package com.mindup.chat.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "gateway", path = "/api/core/")
public interface CoreFeignClient {

    @PostMapping("/user/professional/{id}")
    boolean findProfessionalById(@PathVariable String id);


}