package com.example.fileme.Controller;


import com.example.fileme.Entity.UserLoginInfo;
import com.example.fileme.Entity.UserSignUpInfo;
import com.example.fileme.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@CrossOrigin
public class RestController {
    @GetMapping("/")
    public String getPage(){
        return "chat";
    }

    @GetMapping("/signup")
    public String getSignUpPage(){
        return "signup";
    }

    @GetMapping("/login")
    public String getLoginPage(){
        return "login";
    }

    @Autowired
    UserRepository userRepository;

    @PostMapping("/signup")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<UserSignUpInfo> register(@RequestBody UserSignUpInfo user){
        if(userRepository.findByUsername(user.getUsername()) != null){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }else{
            userRepository.save(user);
            return new ResponseEntity<>(user,HttpStatus.CREATED);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginInfo> login(@RequestBody UserLoginInfo user){
        UserSignUpInfo existingUser = userRepository.findByEmail(user.getLoginEmail());

        if(existingUser!=null && existingUser.getPassword().equals(user.getLoginPassword())){
            return ResponseEntity.ok(user);
        }
        else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
}
