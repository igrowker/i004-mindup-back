package com.mindup.core.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@FeignClient(value = "gateway", path = "/api/message/")
public interface ChatFeignClient {

    @PostMapping("/subscribe-professional/{id}")
     boolean subscribeProfessional(@PathVariable String id);


}