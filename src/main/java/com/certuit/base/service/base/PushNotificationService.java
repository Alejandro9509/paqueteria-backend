package com.certuit.base.service.base;

import com.certuit.base.domain.dto.Push;
import com.certuit.base.filter.HeaderRequestInterceptor;
import com.certuit.base.util.FirebaseResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class PushNotificationService  {

    private static final String FCM_KEY = "AAAAtLISHuY:APA91bGLXGS-cIyhR3fhajNxl2aJzO6o0LShJrY-MxCNmkb_w-cKfPw_vF0BGFCgMjWEOnwedRZ6DUaGPqE80PG4jPN9OMPnxuIz6Vx1Mekv-cWXms7HFInacz_x3yxDYNbtYdvfxE0I";

    private static final String FCM_API = "https://fcm.googleapis.com/fcm/send";

    public FirebaseResponse sendNotification(Push push) {
        HttpEntity<Push> request = new HttpEntity<>(push);
        CompletableFuture<FirebaseResponse> pushNotification = this.send(request);
        CompletableFuture.allOf(pushNotification).join();
        FirebaseResponse firebaseResponse = null;

        try {
            firebaseResponse = pushNotification.get();
            System.err.println("success: " + firebaseResponse.getSuccess());
            System.err.println("failure: " + firebaseResponse.getFailure());
            System.err.println("Results: " + firebaseResponse.getResults());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return firebaseResponse;
    }

    /**
     * send push notification to API FCM
     *
     * Using CompletableFuture with @Async to provide Asynchronous call.
     *
     *
     * @param entity
     * @return
     */
    @Async
    public CompletableFuture<FirebaseResponse> send(HttpEntity<Push> entity) {

        RestTemplate restTemplate = new RestTemplate();

        ArrayList<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(new HeaderRequestInterceptor("Authorization", "key=" + FCM_KEY));
        interceptors.add(new HeaderRequestInterceptor("Content-Type", "application/json"));
        restTemplate.setInterceptors(interceptors);

        FirebaseResponse firebaseResponse = restTemplate.postForObject(FCM_API, entity, FirebaseResponse.class);

        return CompletableFuture.completedFuture(firebaseResponse);
    }

}
