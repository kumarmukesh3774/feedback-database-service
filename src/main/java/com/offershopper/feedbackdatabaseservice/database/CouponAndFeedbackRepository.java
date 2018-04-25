package com.offershopper.feedbackdatabaseservice.database;

import java.util.List;

import org.bson.Document;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.offershopper.feedbackdatabaseservice.model.CouponAndFeedbackBean;

//connection establishment via MongoRepository
public interface CouponAndFeedbackRepository extends MongoRepository<CouponAndFeedbackBean, String>
{
	List<CouponAndFeedbackBean> findByOfferId(String offerId);


  Object findByOfferIdAndUserId(String offerId, String userId);




  boolean existsByOfferIdAndUserId(String offerId, String userId);
}
