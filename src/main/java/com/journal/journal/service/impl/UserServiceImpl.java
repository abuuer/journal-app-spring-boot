/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.journal.journal.service.impl;

import com.journal.journal.bean.ERole;
import com.journal.journal.bean.Role;
import com.journal.journal.bean.Tag;
import com.journal.journal.bean.User;
import com.journal.journal.bean.UserDetailsImpl;
import com.journal.journal.bean.UserRoleDetail;
import com.journal.journal.bean.UserSpecialtyDetail;
import com.journal.journal.dao.UserRepository;
import com.journal.journal.security.jwt.JwtUtils;
import com.journal.journal.security.payload.request.LoginRequest;
import com.journal.journal.security.payload.request.SignupRequest;
import com.journal.journal.security.payload.response.JwtResponse;
import com.journal.journal.security.payload.response.MessageResponse;
import com.journal.journal.service.facade.RoleService;
import com.journal.journal.service.facade.TagService;
import com.journal.journal.service.facade.UserRoleDetailService;
import com.journal.journal.service.facade.UserService;
import com.journal.journal.service.facade.UserSpecialtyDetailService;
import java.util.ArrayList;
import java.util.Iterator;
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

    @Autowired
    private UserSpecialtyDetailService userSpecialtyDetailService;

    @Autowired
    private TagService tagService;

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
        Optional<User> fUser = findByEmail(signUpRequest.getEmail());
        if (fUser.isPresent() && fUser.get().getPassword() != null) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("-1"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getFirstName(), signUpRequest.getLastName(),
                signUpRequest.getMiddleName(), signUpRequest.getEmail(),
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
        if (fUser.isPresent() && fUser.get().getPassword() == null) {
            fUser.get().setFirstName(user.getFirstName());
            fUser.get().setLastName(user.getLastName());
            fUser.get().setMiddleName(user.getMiddleName());
            fUser.get().setPassword(user.getPassword());
            fUser.get().setDegree(user.getDegree());
            fUser.get().setAdress(user.getAdress());
            fUser.get().setCountry(user.getCountry());
            fUser.get().setRegion(user.getRegion());
            fUser.get().setCity(user.getCity());
            fUser.get().setPostalCode(user.getPostalCode());
            fUser.get().setPhone(user.getPhone());
            fUser.get().setFax(user.getFax());
            fUser.get().setInstitution(user.getInstitution());
            fUser.get().setDepartement(user.getDepartement());
            fUser.get().setInstAdress(user.getInstAdress());
            fUser.get().setInstPhone(user.getInstPhone());
            save(fUser.get());
            roles.stream().map((role) -> {
                UserRoleDetail userRoleDetail = new UserRoleDetail();
                userRoleDetail.setRole(role);
                return userRoleDetail;
            }).map((userRoleDetail) -> {
                userRoleDetail.setUser(fUser.get());
                return userRoleDetail;
            }).forEachOrdered((userRoleDetail) -> {
                userRoleDetailService.save(userRoleDetail);
            });
            for (String s : signUpRequest.getSpecialty()) {
                Tag t = new Tag(s);
                Tag ftag = tagService.findByName(s);
                if (ftag == null) {
                    tagService.save(t);
                    UserSpecialtyDetail usd = new UserSpecialtyDetail(fUser.get(), t);
                    userSpecialtyDetailService.save(usd);
                } else {
                    UserSpecialtyDetail usd = new UserSpecialtyDetail(fUser.get(), ftag);
                    userSpecialtyDetailService.save(usd);
                }

            }
        } else {
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
            for (String s : signUpRequest.getSpecialty()) {
                Tag t = new Tag(s);
                Tag ftag = tagService.findByName(s);
                if (ftag == null) {
                    tagService.save(t);
                    UserSpecialtyDetail usd = new UserSpecialtyDetail(user, t);
                    userSpecialtyDetailService.save(usd);
                } else {
                    UserSpecialtyDetail usd = new UserSpecialtyDetail(user, ftag);
                    userSpecialtyDetailService.save(usd);
                }

            }
        }

        return ResponseEntity.ok(new MessageResponse("1"));
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public List<ResponseEntity<?>> authorToReviewer(List<User> users) {
        List<ResponseEntity<?>> responses = new ArrayList<>();
        for (User user : users) {
            Optional<User> fAuthor = userRepository.findById(user.getId());
            List<UserRoleDetail> userRoleDetail = userRoleDetailService.findByUser_Id(user.getId());
            if (!fAuthor.isPresent()) {
                responses.add(ResponseEntity.badRequest().body(new MessageResponse("Author doesn't exist")));
            } else {
                Role reviewerRole = roleService.findByName(ERole.ROLE_REVIEWER).get();
                UserRoleDetail urd = new UserRoleDetail(fAuthor.get(), reviewerRole);
                userRoleDetailService.save(urd);
                fAuthor.get().setAvailability("Available");
                userRepository.save(fAuthor.get());
                responses.add(ResponseEntity.badRequest().body(new MessageResponse(fAuthor.get().getLastName()
                        + " " + fAuthor.get().getFirstName() + " is a reviewer now!")));
            }
        }
        return responses;
    }

}
