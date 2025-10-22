package com.example.pruebas.controller;

import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.session.SessionRegistry;
//import org.springframework.security.core.session.SessionInformation;
//import org.springframework.security.core.session.SessionRegistry;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.pruebas.Models.ERole;
import com.example.pruebas.Models.RoleEntity;
import com.example.pruebas.Models.UserEntity;
import com.example.pruebas.Repository.UserRepository;
import com.example.pruebas.controller.RequestDTO.CreateUserDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import com.example.demo.service.FirebaseDemoService;

@RestController
@RequestMapping("v1")
public class PruebaSprintSecurityController {
    

    @Autowired
    SessionRegistry sessionRegistry; 
    
    final private Logger logger = LoggerFactory.getLogger(PruebaSprintSecurityController.class);

    /**@Autowired
    private SessionRegistry sessionRegistry;**/

    @Autowired
    UserRepository userRepository;

    // Llamada READ: lee todos los usuarios, con llamada HTTP tipo GET.
    @GetMapping("/index")
    public String index(){
        return "Hello World ASEGURADO!";
    }

    @GetMapping("/index2")
    public String index2(){
        return "Contenido NO SEGURIZADO";
    }

    // @GetMapping("/session")
    /**public ResponseEntity<?> getDetailsSession(){
        User user = null;
        String sessionId = "";
        
        List<Object> principals = sessionRegistry.getAllPrincipals();
        for (Object principal : principals) {
            if(principal instanceof User){
                user = (User) principal; // obtenemos el usuario
            }

            // la info de las sesiones
            List<SessionInformation> sessionsInfo = sessionRegistry.getAllSessions(principal, false);
            for (SessionInformation sessionInformation : sessionsInfo) {
                sessionId = sessionInformation.getSessionId(); // recuperamos el ID de la sesión
            }
            user = (User) principal;
        }

        Map<String, Object> response = new HashMap<>();
        response.put("response", "Hello World!");
        response.put("sessionId", sessionId);
        response.put("sessionUser", user);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }*/

    @PostMapping("/createUser")
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserDTO userRequestDTO){
       
        Set<RoleEntity> roles = userRequestDTO.getRoles().stream().map(
            roleName -> 
                RoleEntity.builder().name(ERole.valueOf(roleName)).build()).collect(Collectors.toSet()
            );
        
        UserEntity userEntity = UserEntity.builder()
            .email(userRequestDTO.getEmail())
            .username(userRequestDTO.getUsername())
            .password(userRequestDTO.getPassword())
            .roles(roles)
            .build();

        userRepository.save(userEntity);

        return ResponseEntity.ok(userEntity);
    }

    @DeleteMapping("/deleteUser")
    public String deleteUser(@RequestParam String id){
       userRepository.deleteById(Long.parseLong(id));
        return "Se ha borrado el usuario con id: " + id;
    }


}
