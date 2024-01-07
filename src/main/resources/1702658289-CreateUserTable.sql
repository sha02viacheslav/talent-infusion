CREATE TABLE ti_users (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255),
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    company_name VARCHAR(255),
    photo VARCHAR(255),
    boss_mode BOOLEAN DEFAULT false,
    user_type VARCHAR(255) NOT NULL CHECK (user_type IN ('parent', 'child', 'admin')),
    parent_user_id INTEGER,
    is_child BOOLEAN NOT NULL DEFAULT false,
    stripe_customer_id VARCHAR(255),
    stripe_checkout_session_id VARCHAR(255),
    stripe_billing_portal_session_id VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    reset_password_token VARCHAR(255),
    reset_password_expires TIMESTAMP,
    FOREIGN KEY (parent_user_id) REFERENCES ti_users (id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS ti_invitations (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    parent_user_id INTEGER REFERENCES ti_users(id) ON DELETE SET NULL,
    status VARCHAR(255) NOT NULL CHECK (status IN ('active', 'pending')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS ti_payments (
    id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES ti_users(id) ON DELETE SET NULL,
    session_id VARCHAR(255),
    intent_id VARCHAR(255),
    status VARCHAR(255) NOT NULL CHECK (status IN ('paid', 'unpaid', 'open')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS ti_subscriptions (
    id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES ti_users(id) ON DELETE SET NULL,
    stripe_subscription_id VARCHAR(255) NOT NULL,
    subscription_item_id VARCHAR(255) NOT NULL,
    cancel_at_period_end BOOLEAN,
    current_period_start TIMESTAMP,
    current_period_end TIMESTAMP,
    product_id VARCHAR(255),
    plan_id VARCHAR(255),
    status VARCHAR(255),
    recurring_type VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);