package dev.shubham.userservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.shubham.userservice.dtos.EmailDto;
import dev.shubham.userservice.exceptions.UserPresentException;
import dev.shubham.userservice.models.Token;
import dev.shubham.userservice.models.User;
import dev.shubham.userservice.repositories.TokenRepository;
import dev.shubham.userservice.repositories.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private TokenRepository tokenRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private KafkaTemplate<String, String> kafkaTemplate;
    private ObjectMapper objectMapper;
    public UserServiceImpl(UserRepository userRepository,
                           BCryptPasswordEncoder bCryptPasswordEncoder,
                           TokenRepository tokenRepository,
                           KafkaTemplate kafkaTemplate,
                           ObjectMapper objectMapper){
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.tokenRepository = tokenRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }
    @Override
    public User signUp(String name, String email, String password) throws UserPresentException {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        User user = null;
        if(optionalUser.isPresent()){
            throw new UserPresentException("This User" + email + "is Already Preset");
        }else{
            user = new User();
            user.setEmail(email);
            user.setName(name);;
            user.setPassword(bCryptPasswordEncoder.encode(password));

             user = userRepository.save(user);

             //public an event inside an queue

            EmailDto emailDto  = new EmailDto();
            emailDto.setTo(user.getEmail());
            emailDto.setFrom("yuvraj.shubham.ss@gmail.com");
            emailDto.setSubject("welcome to scaler");
            emailDto.setBody("happy to hear you are here");
            try {
                kafkaTemplate.send("sendEmail",objectMapper.writeValueAsString(emailDto));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }


        }
        return user;
    }

    @Override
    public Token login(String email, String password) {
       Optional<User> optionalUser = userRepository.findByEmail(email);
       User user = null;
       if(optionalUser.isEmpty()){
           //singup
       }else{
          user = optionalUser.get();
          if(!bCryptPasswordEncoder.matches(password,user.getPassword())){
              return null;
          }else{
              Token token = createToken(user);
               token = tokenRepository.save(token);
               return token;
          }
       }
        return null;
    }

    private Token createToken(User user){
        Token token = new Token();
        token.setUser(user);
        token.setValue(RandomStringUtils.randomAlphanumeric(128));

        LocalDate today = LocalDate.now();
        LocalDate afterThirtyDays = today.plus(30, ChronoUnit.DAYS);
        Date expiryAt = Date.from(afterThirtyDays.atStartOfDay(ZoneId.systemDefault()).toInstant());
        token.setExpireAt(expiryAt);
        return token;
    }

    @Override
    public void logout(String token) {
       Optional<Token> optionalToken = tokenRepository.findByValueAndAndDeleted(token,false);

       if(optionalToken.isEmpty()){
           //throw exception
       }
       Token token1 = optionalToken.get();
       token1.setDeleted(true);
       token1 = tokenRepository.save(token1);
    }

    @Override
    public User validate(String token) {
        Optional<Token> optionalToken = tokenRepository.findByValueAndDeletedAndExpireAtGreaterThan(
                token,
                false,

        new Date());
        if(optionalToken.isEmpty()){

        }
        Token token1  = optionalToken.get();
        User user = token1.getUser();
        return user;
    }

    @Override
    public User getUser(Long id) {
      Optional<User> user =  userRepository.findById(id);
      if(user.isEmpty()){

      }
      return user.get();
    }
}
