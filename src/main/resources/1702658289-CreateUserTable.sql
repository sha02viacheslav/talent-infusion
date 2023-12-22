CREATE TABLE ti_users (
                       id SERIAL PRIMARY KEY,
                       email VARCHAR(255) NOT NULL,
                       password VARCHAR(255),
                       first_name VARCHAR(255),
                       last_name VARCHAR(255),
                       company_name VARCHAR(255),
                       photo VARCHAR(255),
                       boss_mode BOOLEAN DEFAULT false,
                       user_type VARCHAR(255),
                       parent_user_id INTEGER,
                       is_child BOOLEAN NOT NULL DEFAULT false,
                       stripe_customer_id VARCHAR(255),
                       stripe_checkout_session_id VARCHAR(255),
                       stripe_billing_portal_session_id VARCHAR(255),
                       created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
                       updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
                       reset_password_token VARCHAR(255),
                       reset_password_expires TIMESTAMPTZ,
                       FOREIGN KEY (parent_user_id) REFERENCES ti_users (id)
);