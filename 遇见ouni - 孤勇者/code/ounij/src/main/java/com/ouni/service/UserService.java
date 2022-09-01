package com.ouni.service;

import com.ouni.domain.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
     User getUserByOpenId(String openId);

     boolean doSign(int userId);

     int signContinueCount(int userId);

     int[] signCountArr(int userId);


}
