package com.example.fileme.Controller;

import com.example.fileme.Dto.UserOtpData;
import com.example.fileme.Dto.PendingUsers;
import com.example.fileme.Entity.UserLoginInfo;
import com.example.fileme.Entity.UserSignUpInfo;
import com.example.fileme.Repository.PendingUserRepo;
import com.example.fileme.Repository.UserRepository;
import com.example.fileme.Service.EmailService;
import io.micrometer.observation.ObservationTextPublisher;
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

    @PostMapping("/signup/otp")
    public ResponseEntity<?> confirmRegister(@RequestBody UserOtpData userOtpData){
        if(!(emailService.confirmPin(userOtpData.getEmail(), userOtpData.getOtp()))){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        else{
            migrateUser(userOtpData.getEmail());
            pendingUserRepo.deleteByEmail(userOtpData.getEmail());
            userRepository.findByEmail(userOtpData.getEmail()).setOnlineStatus("Offline");
            userRepository.save(userRepository.findByEmail(userOtpData.getEmail()));
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
    }

    public void migrateUser(String email){
        UserSignUpInfo user = new UserSignUpInfo();
        user.setEmail(email);
        user.setUsername((pendingUserRepo.findByEmail(email)).getUsername());
        user.setPassword((pendingUserRepo.findByEmail(email)).getPassword());
        user.setOnlineStatus((pendingUserRepo.findByEmail(email)).getOnlineStatus());
        userRepository.save(user);
    }


    @PostMapping("/login")
    public ResponseEntity<UserLoginInfo> login(@RequestBody UserLoginInfo user, HttpSession session){
        UserSignUpInfo existingUser = userRepository.findByEmail(user.getLoginEmail());

        if(existingUser.getOnlineStatus().matches("Online")){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
        else if(existingUser!=null && encoder.matches(user.getLoginPassword(),existingUser.getPassword())){
            session.setAttribute("user",existingUser);
            changeStatus(user.getLoginEmail());
            return ResponseEntity.ok(user);
        }
        else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    public void changeStatus(String email){
        userRepository.findByEmail(email).setOnlineStatus("Online");
        userRepository.save(userRepository.findByEmail(email));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session){
        session.invalidate();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/addFriend")
    public ResponseEntity<String> searchToAdd(@RequestBody String username){
        if(userRepository.findByUsername(username) == null){
            return ResponseEntity.badRequest().body("User not found");
        }
        return ResponseEntity.ok().body(username);
    }

}
