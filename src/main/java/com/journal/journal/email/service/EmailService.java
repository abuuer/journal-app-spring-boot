/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.journal.journal.email.service;

import com.journal.journal.bean.User;
import java.io.IOException;
import java.util.Map;
import javax.mail.MessagingException;

/**
 *
 * @author anoir
 */
public interface EmailService {

    void sendMessageUsingThymeleafTemplate(String pseudo, String email, String lastName)
            throws IOException, MessagingException;

    public void sendSimpleMessage(String to, String subject, String text);
}
