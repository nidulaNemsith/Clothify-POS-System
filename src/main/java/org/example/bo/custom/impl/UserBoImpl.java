package org.example.bo.custom.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.bo.custom.UserBo;
import org.example.controller.ForgotPasswordFormController;
import org.example.dao.DaoFactory;
import org.example.dao.custom.impl.UserDaoImpl;
import org.example.dto.User;
import org.example.entity.UserEntity;
import org.example.util.DaoType;
import org.mindrot.jbcrypt.BCrypt;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserBoImpl implements UserBo {

    private final UserDaoImpl userDao= DaoFactory.getInstance().getDao(DaoType.USER);

    public boolean save(User user){
        return userDao.save(new ObjectMapper().convertValue(user, UserEntity.class));
    }
    public String generateId(){
        if (userDao.getLastId()==null){
            return "U0001";
        }
        int num = Integer.parseInt(userDao.getLastId().split("U")[1]);
        num++;
        return String.format("U%04d",num);
    }
    public User getUser(String id){
        UserEntity userEntity = userDao.search(id);
        return new ObjectMapper().convertValue(userEntity,User.class);
    }
    public boolean delete(String id){
        return userDao.deleteById(id);
    }
    public ObservableList<User> getAll(){
        ObservableList<UserEntity> userEntities = userDao.findAll();
        ObservableList<User> userList = FXCollections.observableArrayList();
        userEntities.forEach(userEntity -> {
            if (userEntity.getRole().equals("Staff")){
                userList.add(new ObjectMapper().convertValue(userEntity,User.class));
            }
        });
        return userList;
    }
    public boolean update(User user){
        return userDao.update(new ObjectMapper().convertValue(user, UserEntity.class));
    }
    public boolean updateUserPassword(String email,String password){
        return userDao.updateUserPassword(email,password);
    }
    public ObservableList<String> getUserId(){
        return userDao.gettAllId();
    }
    public boolean validateEmailAndPassword(String email, String plainTextPassword) {
        UserEntity user = userDao.getUserEntityByEmail(email);
        if (user != null) {
            String storedHashedPassword = user.getPassword();
            return checkPassword(plainTextPassword, storedHashedPassword);
        }
        return false;
    }
    public boolean isValidEmail(String email) {
        String regex ="^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }
    public User getUserByEmail(String email){
        for (UserEntity userEntity : userDao.findAll()) {
            if (userEntity.getEmail().equals(email)){
                return new ObjectMapper().convertValue(userEntity,User.class);
            }
        }
        return null;
    }
    public User getUserByPhoneNumber(String phoneNumber){
        for (UserEntity userEntity : userDao.findAll()) {
            if (userEntity.getPhoneNumber().equals(phoneNumber)){
                return new ObjectMapper().convertValue(userEntity,User.class);
            }
        }
        return null;
    }
    public Message prepareMessage(Session session, String myEmail, String receiveEmail, String text,String subject) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myEmail));
            message.setRecipients(Message.RecipientType.TO,new InternetAddress[]{
                    new InternetAddress(receiveEmail)
            });
            message.setSubject(subject);
            message.setText(text);
            return message;

        }catch (Exception e){
            Logger.getLogger(ForgotPasswordFormController.class.getName()).log(Level.SEVERE,null,e);
        }
        return null;
    }
    public void sendEmail(String receiveEmail,String text,String subject) throws MessagingException {

        Properties properties = new Properties();
        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.starttls.enable","true");
        properties.put("mail.smtp.host","smtp.gmail.com");
        properties.put("mail.smtp.port","587");

        String myEmail = "nemsithbaduge215@gmail.com";
        String password ="viuvuhgyogrrrnah";

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(myEmail,password);
            }
        });

        Message message = prepareMessage(session,myEmail,receiveEmail,text,subject);
        Transport.send(message);
    }
    public  String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
    public  boolean checkPassword(String plainTextPassword, String hashedPassword) {
        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }


}
