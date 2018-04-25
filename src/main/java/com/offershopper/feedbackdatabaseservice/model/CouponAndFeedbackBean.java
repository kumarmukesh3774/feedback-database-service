package com.offershopper.feedbackdatabaseservice.model;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "feedback")
public class CouponAndFeedbackBean {
	@Id
	private String couponId;
	@NotNull
	private String userId;
	@NotNull
	private String offerId;
	@NotNull
	private int rating;
	private String feedback;
	private boolean vendorValidationFlag;
	public CouponAndFeedbackBean() {
	
	}
	
	public CouponAndFeedbackBean(String couponId, String userId, String offerId,int rating,String feedback,boolean vendorValidationFlag) {
		super();
		this.couponId = couponId;
		this.userId = userId;
		this.offerId = offerId;
		this.rating = rating;
		this.feedback=feedback;
		this.vendorValidationFlag=vendorValidationFlag;
	}

	public boolean isVendorValidationFlag() {
    return vendorValidationFlag;
  }

  public void setVendorValidationFlag(boolean vendorValidationFlag) {
    this.vendorValidationFlag = vendorValidationFlag;
  }

  public String getCouponId() {
		return couponId;
	}

	public String getUserId() {
		return userId;
	}

	public String getOfferId() {
		return offerId;
	}

	public int getRating() {
		return rating;
	}

	public String getFeedback() {
		return feedback;
	}

	

}
