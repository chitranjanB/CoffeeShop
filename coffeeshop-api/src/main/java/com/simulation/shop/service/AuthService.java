package com.simulation.shop.service;

import com.coffee.shared.entity.User;
import com.coffee.shared.request.APIResponse;
import com.coffee.shared.request.AuthRequest;
import com.coffee.shared.request.RegisterRequest;
import com.simulation.shop.repository.UserRepository;
import com.simulation.shop.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtils jwtUtils;

    public APIResponse register(RegisterRequest request) {
        APIResponse apiResponse = new APIResponse();
        User userEntity = new User();
        userEntity.setName(request.getName());
        userEntity.setEmailId(request.getEmailId());
        userEntity.setActive(Boolean.TRUE);
        userEntity.setGender(request.getGender());
        userEntity.setPhoneNumber(request.getPhoneNumber());
        userEntity.setPassword(request.getPassword());
        try{
            userEntity = userRepository.save(userEntity);
            String token = jwtUtils.generateJwt(userEntity);
            Map<String, Object> data = new HashMap<>();
            data.put("accessToken", token);
            apiResponse.setData(data);
            apiResponse.setStatus(HttpStatus.OK.value());
        }
        catch (Exception e){
            apiResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return apiResponse;
    }

    public APIResponse authenticate(AuthRequest request) {
        APIResponse apiResponse = new APIResponse();
        User user = userRepository.findOneByEmailIdIgnoreCaseAndPassword(request.getEmailId(), request.getPassword());
        if (user == null) {
            apiResponse.setData("User login failed");
            apiResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        } else {
            String token = jwtUtils.generateJwt(user);
            Map<String, Object> data = new HashMap<>();
            data.put("accessToken", token);
            apiResponse.setData(data);
            apiResponse.setStatus(HttpStatus.OK.value());
        }
        return apiResponse;
    }
}