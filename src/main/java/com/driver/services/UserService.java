package com.driver.services;


import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.model.WebSeries;
import com.driver.repository.UserRepository;
import com.driver.repository.WebSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    WebSeriesRepository webSeriesRepository;


    public Integer addUser(User user){

        //Jut simply add the user to the Db and return the userId returned by the repository
        User u=userRepository.save(user);
        return u.getId();
    }

    public Integer getAvailableCountOfWebSeriesViewable(Integer userId){

        User user = userRepository.findById(userId).get();

        Subscription subscription = user.getSubscription();

        SubscriptionType subscriptionType = subscription.getSubscriptionType();

        List<WebSeries> webSeriesList = webSeriesRepository.findAll();

        int count=0;

        if(subscriptionType.toString().equals("BASIC")){
            for(WebSeries web : webSeriesList){
                if(web.getSubscriptionType().toString().equals("BASIC"))
                    count++;
            }
        }
        else if (subscriptionType.toString().equals("PRO")) {
            for(WebSeries web : webSeriesList){
                if(web.getSubscriptionType().toString().equals("PRO") || web.getSubscriptionType().toString().equals("BASIC") )
                    count++;
            }
        }
        else {
            for(WebSeries web : webSeriesList){
                count++;
            }
        }

        return count;
    }


}
