package com.offershopper.feedbackdatabaseservice.rabbitmq;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.offershopper.feedbackdatabaseservice.CouponFeedbackApplication;

//a service class that retrieves the messages from the rabbitmq server queue
@Service
public class RetrieveMessagesFromGenericQueue {

    //listing to generic queue
    @RabbitListener(queues = CouponFeedbackApplication.QUEUE_GENERIC_NAME)
    public void receiveMessage(final Message message) {
    	//retrieving the messages and storing them into a log.txt file
    	 File file =new File("./feedbackLogs.txt");
    	    if(!file.exists()){
    	 	   try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	 	}
        	FileWriter fw=null;
        	BufferedWriter bw = null;
			try {
				//writing in file
				fw = new FileWriter(file,true);
				bw = new BufferedWriter(fw);
	        	bw.write(message.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
			//closing the opened resources
			finally {
				try {
					if(bw!=null) {
						bw.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
    }
}