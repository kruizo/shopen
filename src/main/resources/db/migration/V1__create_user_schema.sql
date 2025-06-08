CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    is_verified BOOLEAN NOT NULL DEFAULT FALSE,
    verification_code VARCHAR(50) UNIQUE,
    verification_expiration TIMESTAMPTZ,
    role VARCHAR(20) NOT NULL DEFAULT 'BUYER' CHECK (role IN ('BUYER', 'SELLER', 'ADMIN'))
    is_enabled BOOLEAN NOT NULL DEFAULT TRUE,
    -- TO BE ADDED:
    -- last_login TIMESTAMPTZ,
    -- last_email_change TIMESTAMPTZ,
    -- last_verification_code_request TIMESTAMPTZ,
    -- last_failed_login_attempt TIMESTAMPTZ,
    -- failed_login_attempts INT DEFAULT 0,
    
    -- last_password_reset TIMESTAMPTZ,
    -- password_reset_token VARCHAR(255) UNIQUE,
    -- password_reset_expiration TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE user_profiles (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    full_name VARCHAR(100),
    phone_number VARCHAR(20),
    profile_image_url TEXT,
    bio TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_user_profiles_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_user_profiles_user_id ON user_profiles(user_id);


CREATE TABLE seller_profiles (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    store_name VARCHAR(100),
    business_id VARCHAR(50),
    rating DECIMAL(3,2) DEFAULT 0.0,
    is_verified BOOLEAN DEFAULT FALSE,

    CONSTRAINT fk_seller_profiles_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
