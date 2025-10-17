package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.apache.catalina.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import com.example.demo.service.FirebaseDemoService;

@RestController
@RequestMapping("v1")
public class DemoController {
    
    final private Logger logger = LoggerFactory.getLogger(DemoController.class);

    @Autowired
    private SessionRegistry sessionRegistry;

    // Llamada READ: lee todos los usuarios, con llamada HTTP tipo GET.
    @GetMapping("/index")
    public String index(){
        return "Hello World!";
    }

    @GetMapping("/index2")
    public String index2(){
        return "Contenido NO SEGURIZADO";
    }

    @GetMapping("/session")
    public ResponseEntity<?> getDetailsSession(){
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
    }


}
