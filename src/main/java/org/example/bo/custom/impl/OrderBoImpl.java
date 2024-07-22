package org.example.bo.custom.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import org.example.bo.custom.OrderBo;
import org.example.controller.PlaceOrderFormController;
import org.example.dao.DaoFactory;
import org.example.dao.custom.impl.OrderDaoImpl;
import org.example.dto.Order;
import org.example.entity.OrderEntity;
import org.example.util.DaoType;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class OrderBoImpl implements OrderBo {
    private final OrderDaoImpl orderDao= DaoFactory.getInstance().getDao(DaoType.ORDER);

    public boolean save(Order order){
        return orderDao.save(new ObjectMapper().convertValue(order, OrderEntity.class));
    }
    public String generateId(){
        if (orderDao.getLastId()==null){
            return "OR0001";
        }
        int num = Integer.parseInt(orderDao.getLastId().split("OR")[1]);
        num++;
        return String.format("OR%04d",num);
    }
    public boolean delete(String id){
        return orderDao.deleteById(id);
    }
    public ObservableList<Order> getAll(){
        ObservableList<OrderEntity> orderEntities = orderDao.findAll();
        ObservableList<Order> orderObservableList = FXCollections.observableArrayList();
        orderEntities.forEach(orderEntity -> {
            orderObservableList.add(new ObjectMapper().convertValue(orderEntity,Order.class));
        });
        return orderObservableList;
    }
    public ObservableList<Order> getTopOrders(int limit) {
        ObservableList<Order> allOrders = getAll();
        List<Order> sortedOrders = allOrders.stream()
                .sorted(Comparator.comparing(Order::getTotal).reversed())
                .limit(limit)
                .collect(Collectors.toList());
        return FXCollections.observableArrayList(sortedOrders);
    }

    public XYChart.Series<String, Number> getTopOrdersSeries(int limit) {
        ObservableList<Order> topOrders = getTopOrders(limit);
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        topOrders.forEach(order -> {
            series.getData().add(new XYChart.Data<>(order.getOrderId(), order.getTotal()));
        });
        return series;
    }
    public boolean update(Order order){
        return orderDao.update(new ObjectMapper().convertValue(order, OrderEntity.class));
    }
    public ObservableList<String> getOrderId(){
        ObservableList<String> list=orderDao.gettAllId();
        return list;
    }
    public Order getOrder(String id) {
        OrderEntity orderEntity = orderDao.search(id);
        return new ObjectMapper().convertValue(orderEntity,Order.class);
    }
    public void sendEmail(String receiveEmail,String text,File file) throws MessagingException {

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        String myEmail = "nemsithbaduge215@gmail.com";
        String password ="viuvuhgyogrrrnah";

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(myEmail, password);
            }
        });

        Message message = prepareMessage(session, myEmail, receiveEmail, text, file);
        Transport.send(message);
    }
    public Message prepareMessage(Session session, String myEmail, String receiveEmail, String text, File file) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myEmail));
            message.setRecipients(Message.RecipientType.TO, new InternetAddress[]{
                    new InternetAddress(receiveEmail)
            });
            message.setSubject("Order Confirmation & Receipt");
            message.setText(text);


            Multipart multipart = new MimeMultipart();

            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(text);
            multipart.addBodyPart(textPart);

            MimeBodyPart attachmentPart = new MimeBodyPart();
            attachmentPart.attachFile(file);
            multipart.addBodyPart(attachmentPart);

            message.setContent(multipart);

            return message;
        } catch (Exception e) {
            Logger.getLogger(PlaceOrderFormController.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }
}
