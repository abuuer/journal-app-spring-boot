/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.journal.journal.service.impl;

import com.journal.journal.bean.ERole;
import com.journal.journal.bean.Role;
import com.journal.journal.bean.User;
import com.journal.journal.bean.UserDetailsImpl;
import com.journal.journal.bean.UserRoleDetail;
import com.journal.journal.dao.UserRepository;
import com.journal.journal.security.jwt.JwtUtils;
import com.journal.journal.security.payload.request.LoginRequest;
import com.journal.journal.security.payload.request.SignupRequest;
import com.journal.journal.security.payload.response.JwtResponse;
import com.journal.journal.security.payload.response.MessageResponse;
import com.journal.journal.service.facade.RoleService;
import com.journal.journal.service.facade.UserRoleDetailService;
import com.journal.journal.service.facade.UserService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author anoir
 */
@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleDetailService userRoleDetailService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + email));

        return UserDetailsImpl.build(user);
    }

    @Override
    public int save(User user) {
        userRepository.save(user);
        return 1;
    }

    @Override
    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getEmail(),
                roles));
    }

    @Override
    public ResponseEntity<?> registerUser(SignupRequest signUpRequest) {
        if (existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getFirstName(), signUpRequest.getLastName(),
                signUpRequest.getMiddleName(), signUpRequest.getMiddleName(),
                encoder.encode(signUpRequest.getPassword()), signUpRequest.getDegree(), signUpRequest.getAdress(),
                signUpRequest.getCountry(), signUpRequest.getRegion(), signUpRequest.getCity(), signUpRequest.getPostalCode(),
                signUpRequest.getPhone(), signUpRequest.getFax(), signUpRequest.getInstitution(), signUpRequest.getDepartement(),
                signUpRequest.getInstAdress(), signUpRequest.getInstPhone());

        List<String> strRoles = signUpRequest.getRole();
        List<Role> roles = new ArrayList<>();

        if (strRoles == null) {
            Role authorRole = roleService.findByName(ERole.ROLE_AUTHOR)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            Role userRole = roleService.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(authorRole);
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "author":
                        Role authorRole = roleService.findByName(ERole.ROLE_AUTHOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(authorRole);

                        break;
                    case "editor":
                        Role editorRole = roleService.findByName(ERole.ROLE_EDITOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(editorRole);

                        break;
                    case "reviewer":
                        Role reviewerRole = roleService.findByName(ERole.ROLE_REVIEWER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(reviewerRole);
                        break;
                    default:
                        Role userRole = roleService.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }
        save(user);
        roles.stream().map((role) -> {
            UserRoleDetail userRoleDetail = new UserRoleDetail();
            userRoleDetail.setRole(role);
            return userRoleDetail;
        }).map((userRoleDetail) -> {
            userRoleDetail.setUser(user);
            return userRoleDetail;
        }).forEachOrdered((userRoleDetail) -> {
            userRoleDetailService.save(userRoleDetail);
        });

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

}
