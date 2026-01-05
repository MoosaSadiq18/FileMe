package com.example.fileme.Controller;


import com.example.fileme.Entity.UserLoginInfo;
import com.example.fileme.Entity.UserSignUpInfo;
import com.example.fileme.Repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class RestController {

    @GetMapping("/")
    public String getPage(HttpSession session){
        if(session.getAttribute("user") == null){
            return "redirect:/login";
        }
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

    @Autowired
    PasswordEncoder encoder;

    @PostMapping("/signup")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<UserSignUpInfo> register(@RequestBody UserSignUpInfo user){
        if(userRepository.findByUsername(user.getUsername()) != null){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }else{
            user.setPassword(encoder.encode(user.getPassword()));
            userRepository.save(user);
            return new ResponseEntity<>(user,HttpStatus.CREATED);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginInfo> login(@RequestBody UserLoginInfo user, HttpSession session){
        UserSignUpInfo existingUser = userRepository.findByEmail(user.getLoginEmail());

        if(existingUser!=null && encoder.matches(user.getLoginPassword(),existingUser.getPassword())){
            session.setAttribute("user",existingUser);
            return ResponseEntity.ok(user);
        }
        else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session){
        session.invalidate();
        return ResponseEntity.ok().build();
    }

}
