/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.translator.controller;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;
import dk.translator.dto.LoanRequestDTO;
import dk.translator.messaging.Receive;
import dk.translator.messaging.Send;
import java.io.IOException;
import java.util.HashMap;

/**
 *
 * @author marekrigan
 */
public class TranslateToBankRabbit 
{
    private static Gson gson;
    
    public static void receiveMessages() throws IOException,InterruptedException
    {
        gson = new Gson();
        
        HashMap<String,Object> objects = Receive.setUpReceiver();
        
        QueueingConsumer consumer = (QueueingConsumer) objects.get("consumer");
        Channel channel = (Channel) objects.get("channel");
        
        LoanRequestDTO loanRequestDTO;
        
        while (true) 
        {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String message = new String(delivery.getBody());

            String routingKey = delivery.getEnvelope().getRoutingKey();

            System.out.println(" [x] Received '" + routingKey + "':'" + message + "'");
            
//            loanRequestDTO = gson.fromJson(message, LoanRequestDTO.class);

//            sendMessage(loanRequestDTO);

            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        }
        
    }
    
    public static void sendMessage(LoanRequestDTO dto) throws IOException
    {
        String message = gson.toJson(dto);
        
        Send.sendMessage(message);
    }
}
