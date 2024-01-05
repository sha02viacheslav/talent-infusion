package com.talent.infusion.repository.subscription;

import com.talent.infusion.entiry.subscription.TiSubscription;
import org.javalite.activejdbc.DB;

import java.util.HashMap;
import java.util.Optional;

public class SubscriptionRepository {

    private final DB db;

    public SubscriptionRepository(DB db) {
        this.db = db;
    }

    public Optional<TiSubscription> getSubscriptionById(int id) {
        return db.withDb(() -> Optional.ofNullable(TiSubscription.findById(id)));
    }

    public void createSubscription(HashMap<String, Object> data) {
        db.withDb(() -> {
            TiSubscription subscription = new TiSubscription();
            for (HashMap.Entry<String, Object> entry : data.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                subscription.set(key, value);
            }
            subscription.saveIt();
            return subscription;
        });
    }


    public void updateSubscription(int id, HashMap<String, Object> data) {
        db.withDb(() -> {
            TiSubscription subscription = TiSubscription.findById(id);
            if (subscription == null) {
                return Optional.empty();
            }

            for (HashMap.Entry<String, Object> entry : data.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                subscription.set(key, value);
            }
            subscription.saveIt();
            return Optional.of(subscription);
        });
    }


    public void updateStatusByUserId(int userId, String status) {
        db.withDb(() -> {
            TiSubscription.update("set status = ? where user_id = ?", status, userId);
            return null;
        });
    }
}
