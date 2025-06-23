package com.oide.user.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.oide.user.dto.UserDTO;

import reactor.core.publisher.Mono;

@Service
public class ApiService {

    @Autowired
    private WebClient.Builder webClient;

    public Mono<Long> addProfile(UserDTO userDTO){
//        if(userDTO.getRole().equals(Roles.DOCTOR)){
//            return webClient.build().post()
//                    .uri("http://localhost:8086/profile/doctor/add").bodyValue(userDTO).retrieve().bodyToMono(Long.class);
//        }
//        else if(userDTO.getRole().equals(Roles.PATIENT)){
//            return webClient.build().post()
//                    .uri("http://localhost:8086/profile/patient/add").bodyValue(userDTO).retrieve().bodyToMono(Long.class);
//        }
            return webClient.build().post()
                    .uri("http://localhost:10002/profile/addProfile").bodyValue(userDTO).retrieve().bodyToMono(Long.class);

    }
}
