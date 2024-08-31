package com.hello.service.user.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hello.common.constants.CommonConstants;
import com.hello.common.constants.JwtClaimsConstant;
import com.hello.common.constants.MessageConstant;
import com.hello.common.constants.RedisKeyConstant;
import com.hello.common.exception.*;
import com.hello.common.utils.UserJwtUtil;
import com.hello.fileStarter.service.FileStorageService;
import com.hello.model.user.constants.AccountStatusConstants;
import com.hello.model.user.dtos.LoginDTO;
import com.hello.model.user.dtos.UserRegisterDTO;
import com.hello.model.user.pojos.User;
import com.hello.model.user.vos.UserRegisterVO;
import com.hello.service.user.service.UserService;
import com.hello.model.user.vos.UserLoginVO;
import com.hello.service.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import org.redisson.api.RedissonClient;
import org.springframework.util.DigestUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


    private final RedissonClient redissonClient;
    private final UserMapper userMapper;
//    @Resource
//    private final RBloomFilter<String> userRegisterCachePenetrationBloomFilter;

    /**
     * 不多比比这就是登录
     * @param loginDTO
     * @return
     */
    @Override
    public UserLoginVO login(LoginDTO loginDTO)  {
        if(StringUtils.isNotBlank(loginDTO.getUsername())&&StringUtils.isNotBlank(loginDTO.getPassword())){
            User user = getOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, loginDTO.getUsername()));
            if(user == null){
                throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
            }
            if(user.getStatus().equals(AccountStatusConstants.ACCOUNT_STATUS_DISABLE)){
                throw new AccountStatusException(MessageConstant.ACCOUNT_DISABLE);
            }
            String salt = user.getSalt();
            String password = loginDTO.getPassword();
//            String pswd = DigestUtils.md5DigestAsHex((password + salt).getBytes());
            if(!password.equals(user.getPassword())) {
                throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
            }

            String token = UserJwtUtil.getToken(user.getId().longValue());
//            HashMap<String,Object> claims = new HashMap<>();
//            claims.put(JwtClaimsConstant.USER_ID,user.getId());
//            claims.put(JwtClaimsConstant.TOKEN,token);
//            user.setSalt(CommonConstants.EMPTY_STRING);
//            user.setPassword(CommonConstants.EMPTY_STRING);
//            claims.put(JwtClaimsConstant.USER,user);

            UserLoginVO userLoginVO = UserLoginVO.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .nickName(user.getNickName())
                    .token(token)
                    .build();
            return userLoginVO;
        }
        return null;
    }

    /**
     * 注册账户
     * @param registerDTO
     * @return
     */

    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserRegisterVO register(UserRegisterDTO registerDTO) {

        RLock lock = redissonClient.getLock(RedisKeyConstant.LOCK_USER_REGISTER + registerDTO.getUsername());
        boolean tryLock = lock.tryLock();
        if(!tryLock){
            throw new UsernameAlreadyExistsException(MessageConstant.ALREADY_EXISTS);
        }
        try {
            try{
                User user = new User();
                BeanUtils.copyProperties(registerDTO,user);
                String salt = UUID.randomUUID().toString().replace("-", "");
                String password = DigestUtils.md5DigestAsHex((salt + registerDTO.getPassword()).getBytes());

                user.setCreatedTime(new Date());
                user.setSalt(salt);
                user.setPassword(password);

                int insert = userMapper.insert(user);
                log.info("用户注册 user:{},insert:{}",user,insert);
                if(insert<1){
                    throw new RegisterFailedException(MessageConstant.REGISTER_FAILED);
                }
            }catch (DuplicateKeyException e){
                log.info("用户名 【{}】 重复注册",registerDTO.getUsername());
            }
        }finally {
            lock.unlock();
        }
        return UserRegisterVO.builder()
                .username(registerDTO.getUsername())
                .nickName(registerDTO.getNickName())
                .phone(registerDTO.getPhone())
                .build();
    }
}






















