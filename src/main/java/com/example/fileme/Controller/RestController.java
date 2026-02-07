package com.example.fileme.Controller;

import com.example.fileme.Dto.Migrator;
import com.example.fileme.Dto.UserOtpData;
import com.example.fileme.Entity.PendingUsers;
import com.example.fileme.Dto.UserLoginInfo;
import com.example.fileme.Entity.UserSignUpInfo;
import com.example.fileme.Repository.PendingUserRepo;
import com.example.fileme.Repository.UserRepository;
import com.example.fileme.Service.EmailService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class RestController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PendingUserRepo pendingUserRepo;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    EmailService emailService;

    @GetMapping("/")
    public String getPage(HttpSession session){
        if(session.getAttribute("user") == null){
            return "redirect:/login";
        }
        return "fileUpload";
    }

    @GetMapping("/signup")
    public String getSignUpPage(){
        return "signup";
    }

    @GetMapping("/login")
    public String getLoginPage(){
        return "login";
    }

    @GetMapping("/upload")
    public String getUploadPage(){return "fileUpload";}


    @PostMapping("/signup")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<PendingUsers> pendingRegister(@RequestBody PendingUsers pendingUser){
        if(userRepository.findByUsername(pendingUser.getUsername()) != null){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        else if(userRepository.findByEmail(pendingUser.getEmail()) != null){
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        else{
            pendingUser.setPassword(encoder.encode(pendingUser.getPassword()));
            emailService.sendEmail(pendingUser.getEmail());
            pendingUserRepo.save(pendingUser);
            return new ResponseEntity<>(pendingUser,HttpStatus.CREATED);
        }
    }

    @Autowired
    Migrator migrator;

    @PostMapping("/signup/otp")
    public ResponseEntity<?> confirmRegister(@RequestBody UserOtpData userOtpData){
        if(!(emailService.confirmPin(userOtpData.getEmail(), userOtpData.getOtp()))){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        else{
            migrator.migrateUser(userOtpData.getEmail());
            pendingUserRepo.deleteByEmail(userOtpData.getEmail());
            userRepository.save(userRepository.findByEmail(userOtpData.getEmail()));
            return ResponseEntity.status(HttpStatus.CREATED).build();
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

}
