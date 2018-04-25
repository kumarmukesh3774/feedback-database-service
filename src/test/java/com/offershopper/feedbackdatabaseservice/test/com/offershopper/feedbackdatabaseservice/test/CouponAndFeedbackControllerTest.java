package com.offershopper.feedbackdatabaseservice.test.com.offershopper.feedbackdatabaseservice.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.offershopper.feedbackdatabaseservice.CouponFeedbackApplication;
import com.offershopper.feedbackdatabaseservice.controller.CouponAndFeedbackController;
import com.offershopper.feedbackdatabaseservice.database.CouponAndFeedbackRepository;
import com.offershopper.feedbackdatabaseservice.model.CouponAndFeedbackBean;
import com.offershopper.feedbackdatabaseservice.rabbitmq.MessageSender;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = CouponFeedbackApplication.class)
@WebMvcTest(value = CouponAndFeedbackController.class)
public class CouponAndFeedbackControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	CouponAndFeedbackRepository couponAndFeedbackRepository;
	
    
	@MockBean
	private MessageSender sendMessageToRabbit;

	// Template values to test against the controller mappings
	CouponAndFeedbackBean couponAndFeedback = new CouponAndFeedbackBean("coup-101", "user-101", "offer-101", 5, "feedback-101",false);

	List<CouponAndFeedbackBean> mockList = new ArrayList<CouponAndFeedbackBean>();

	// Testing getFeedback method for positive(valid) value, should return feedback of an offer by offerId
	@Test
	public void getFeedbackPositiveTest() {

		mockList.add(couponAndFeedback);
		Mockito.when(couponAndFeedbackRepository.findByOfferId("offer-101")).thenReturn(mockList);

		//positive value
		RequestBuilder positiveRequestBuilder = MockMvcRequestBuilders.get("/os/getfeedback/offer-101")
				.accept(MediaType.APPLICATION_JSON);
		try {

			MvcResult resultPositive = mockMvc.perform(positiveRequestBuilder).andReturn();
			
			String expectedPositive = "[{\\n\" + \n" + "	       		\"    \\\"couponId\\\" :\\\" coup-101\\\",\\n\" + \n"
					+ "	       		\"    \\\"userId\\\" : \\\"user-101\\\",\\n\" + \n"
					+ "	       		\"    \\\"offerId\\\" : \\\"offer-101\\\",\\n\" + \n"
					+ "	       		\"    \\\"rating\\\" : 5,\\n\" + \n"
					+ "	       		\"    \\\"feedback\\\" : \\\"feedback-101\\\"\\n\" + \n" + "	       		\"}]";
			
			JSONAssert.assertEquals(expectedPositive, resultPositive.getResponse().getContentAsString(), false);		
			
			MockHttpServletResponse responsePositive = resultPositive.getResponse();
			
			assertEquals(HttpStatus.OK.value(), responsePositive.getStatus());
			
		} catch (Exception e) {
			System.out.println("Error during testing positive values for getfeedback() \n" + e.getMessage());
		}

	}

	// Testing getFeedback method for negative(invalid) input, should not return feedback of an offer by offerId
	@Test
	public void getFeedbackNegativeTest() {

		mockList.add(couponAndFeedback);
		Mockito.when(couponAndFeedbackRepository.findByOfferId("offer-101")).thenReturn(mockList);

		//negative value
		RequestBuilder negativeRequestBuilder = MockMvcRequestBuilders.get("/os/getfeedback/offer-100")
				.accept(MediaType.APPLICATION_JSON);
		
		try {

			MvcResult resultNegative = mockMvc.perform(negativeRequestBuilder).andReturn();
			
			String expectedPositive = "[{\\n\" + \n" + "	       		\"    \\\"couponId\\\" :\\\" coup-101\\\",\\n\" + \n"
					+ "	       		\"    \\\"userId\\\" : \\\"user-101\\\",\\n\" + \n"
					+ "	       		\"    \\\"offerId\\\" : \\\"offer-101\\\",\\n\" + \n"
					+ "	       		\"    \\\"rating\\\" : 5,\\n\" + \n"
					+ "	       		\"    \\\"feedback\\\" : \\\"feedback-101\\\"\\n\" + \n" + "	       		\"}]";
			
			JSONAssert.assertNotEquals(expectedPositive, resultNegative.getResponse().getContentAsString(), false);
			
			MockHttpServletResponse responseNegative = resultNegative.getResponse();
			
			assertNotEquals(HttpStatus.OK.value(), responseNegative.getStatus());
			
		} catch (Exception e) {
			System.out.println("Error during testing negative values for getfeedback()\\n" + e.getMessage());
		}

	}
	
	// Testing addFeedback method for positive(valid) values, should add feedback provided by a user
	@Test
	public void addFeedbackPositiveTest() {
		
		String validFeedback = "{\n" + "    \"couponId\" :\" coup-101\",\n" + "    \"userId\" : \"user-101\",\n"
				+ "    \"offerId\" : \"offer-101\",\n" + "    \"rating\" : 5,\n"
				+ "    \"feedback\" : \"feedback-101\"\n" + "}";
		
		try {
			Mockito.when(couponAndFeedbackRepository.insert(Mockito.any(CouponAndFeedbackBean.class)))
					.thenReturn(couponAndFeedback);
			
			RequestBuilder positiveRequestBuilder = MockMvcRequestBuilders.post("/os/addfeedback")
					.accept(MediaType.APPLICATION_JSON).content(validFeedback)
					.contentType(MediaType.APPLICATION_JSON);
			
			
			MvcResult resultPositive;
			resultPositive = mockMvc.perform(positiveRequestBuilder).andReturn();
			
			MockHttpServletResponse responsePositive = resultPositive.getResponse();
			
			assertEquals(HttpStatus.OK.value(), responsePositive.getStatus());

		} catch (Exception e) {
			System.out.println("Error during testing positive values for addfeedback()\\n" + e.getMessage());
		}

	}
	
	// Testing addFeedback method for negative(invalid) values, should not add feedback provided by a user
		@Test
		public void addFeedbackNegativeTest() {
			
			String invalidFeedback = "{\n" + "    couponId :\" coup-100\",\n" + "    \"userId\" : \"user-100\",\n"
					 + "    \"rating\" : 5,\n"
					+ "    \"feedback\" : \"feedback-100\"\n" + "}";
			
			try {
				Mockito.when(couponAndFeedbackRepository.insert(Mockito.any(CouponAndFeedbackBean.class)))
						.thenReturn(couponAndFeedback);
				
				
				RequestBuilder negativeRequestBuilder = MockMvcRequestBuilders.post("/os/addfeedback")
						.accept(MediaType.APPLICATION_JSON).content(invalidFeedback)
						.contentType(MediaType.APPLICATION_JSON);
				
				MvcResult resultNegative;
				resultNegative = mockMvc.perform(negativeRequestBuilder).andReturn();
				
				MockHttpServletResponse responseNegative = resultNegative.getResponse();
				
				assertNotEquals(HttpStatus.OK.value(), responseNegative.getStatus());

				//assetEquals(HttpStatus.)
			} catch (Exception e) {
				System.out.println("Error during testing negative values for addfeedback()\\n" + e.getMessage());
			}

		}
}
