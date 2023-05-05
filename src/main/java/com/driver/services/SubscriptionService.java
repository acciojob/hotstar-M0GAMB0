package com.driver.services;


import com.driver.EntryDto.SubscriptionEntryDto;
import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.repository.SubscriptionRepository;
import com.driver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SubscriptionService {

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    UserRepository userRepository;

    public Integer buySubscription(SubscriptionEntryDto subscriptionEntryDto){

        //Save The subscription Object into the Db and return the total Amount that user has to pay
        Subscription subscription=new Subscription();
        subscription.setSubscriptionType(subscriptionEntryDto.getSubscriptionType());
        subscription.setNoOfScreensSubscribed(subscriptionEntryDto.getNoOfScreensRequired());
        User user=userRepository.findById(subscriptionEntryDto.getUserId()).get();
        subscription.setUser(user);

        int totalAmount=0;
        if(SubscriptionType.BASIC==subscription.getSubscriptionType()){
            totalAmount=500 + 200*subscription.getNoOfScreensSubscribed();
        }
        else if(SubscriptionType.PRO==subscription.getSubscriptionType()){
            totalAmount=800 + 250*subscription.getNoOfScreensSubscribed();
        }
        else{
            totalAmount=1000 + 350*subscription.getNoOfScreensSubscribed();
        }
        subscription.setTotalAmountPaid(totalAmount);
        subscriptionRepository.save(subscription);
        return totalAmount;
    }

    public Integer upgradeSubscription(Integer userId)throws Exception{

        //If you are already at an ElITE subscription : then throw Exception ("Already the best Subscription")
        //In all other cases just try to upgrade the subscription and tell the difference of price that user has to pay
        //update the subscription in the repository
        User user=userRepository.findById(userId).get();
        if(user.getSubscription().getSubscriptionType()==SubscriptionType.ELITE){
            throw new Exception("Already the best Subscription");
        }
        int amount_basic=500 + 200*user.getSubscription().getNoOfScreensSubscribed();
        int amount_pro=800 + 250*user.getSubscription().getNoOfScreensSubscribed();
        int amount_elite=1000 + 350*user.getSubscription().getNoOfScreensSubscribed();
        if(user.getSubscription().getSubscriptionType()==SubscriptionType.BASIC){
            user.getSubscription().setSubscriptionType(SubscriptionType.PRO);
            return amount_pro-amount_basic;
        }
        userRepository.save(user);
        return amount_elite-amount_pro;
    }

    public Integer calculateTotalRevenueOfHotstar(){

        //We need to find out total Revenue of hotstar : from all the subscriptions combined
        //Hint is to use findAll function from the SubscriptionDb
        List<Subscription> subscriptionsList=subscriptionRepository.findAll();
        int totalRevenue=0;
        for(Subscription subscription:subscriptionsList){
            totalRevenue+=subscription.getTotalAmountPaid();
        }
        return totalRevenue;
    }

}
