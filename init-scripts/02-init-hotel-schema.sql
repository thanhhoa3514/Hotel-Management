-- Hotel Management Database Schema (Updated)

\c hotelmanagement;

-- Lookup Tables

-- Room Types lookup table
CREATE TABLE IF NOT EXISTS room_types (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    price_per_night DECIMAL(10,2) NOT NULL
);

-- Room Statuses lookup table
CREATE TABLE IF NOT EXISTS room_statuses (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- Reservation Statuses lookup table
CREATE TABLE IF NOT EXISTS reservation_statuses (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- Payment Statuses lookup table
CREATE TABLE IF NOT EXISTS payment_statuses (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- Main Tables

-- Rooms table
CREATE TABLE IF NOT EXISTS rooms (
    id SERIAL PRIMARY KEY,
    room_number VARCHAR(20) NOT NULL UNIQUE,
    room_type_id INTEGER NOT NULL REFERENCES room_types(id) ON DELETE RESTRICT,
    room_status_id INTEGER NOT NULL REFERENCES room_statuses(id) ON DELETE RESTRICT,
    floor INTEGER,
    note TEXT
);

-- Room Images table (NEW)
CREATE TABLE IF NOT EXISTS room_images (
    id SERIAL PRIMARY KEY,
    room_id INTEGER NOT NULL REFERENCES rooms(id) ON DELETE CASCADE,
    image_url VARCHAR(500) NOT NULL,
    description TEXT,
    is_primary BOOLEAN DEFAULT FALSE,
    display_order INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Guests table (without users table, using Keycloak)
CREATE TABLE IF NOT EXISTS guests (
    id SERIAL PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20),
    address TEXT,
    keycloak_user_id UUID UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Staff table (without users table, using Keycloak)
CREATE TABLE IF NOT EXISTS staff (
    id SERIAL PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    position VARCHAR(50),
    keycloak_user_id UUID UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Reservations table
CREATE TABLE IF NOT EXISTS reservations (
    id SERIAL PRIMARY KEY,
    guest_id INTEGER NOT NULL REFERENCES guests(id) ON DELETE CASCADE,
    status_id INTEGER NOT NULL REFERENCES reservation_statuses(id) ON DELETE RESTRICT,
    check_in DATE NOT NULL,
    check_out DATE NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Reservation Rooms (Many-to-Many)
CREATE TABLE IF NOT EXISTS reservation_rooms (
    id SERIAL PRIMARY KEY,
    reservation_id INTEGER NOT NULL REFERENCES reservations(id) ON DELETE CASCADE,
    room_id INTEGER NOT NULL REFERENCES rooms(id) ON DELETE RESTRICT,
    UNIQUE(reservation_id, room_id)
);

-- Reservation Staff (Many-to-Many)
CREATE TABLE IF NOT EXISTS reservation_staff (
    id SERIAL PRIMARY KEY,
    reservation_id INTEGER NOT NULL REFERENCES reservations(id) ON DELETE CASCADE,
    staff_id INTEGER NOT NULL REFERENCES staff(id) ON DELETE RESTRICT,
    UNIQUE(reservation_id, staff_id)
);

-- Services table
CREATE TABLE IF NOT EXISTS services (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    price DECIMAL(10,2) NOT NULL
);

-- Reservation Services (Many-to-Many with quantity)
CREATE TABLE IF NOT EXISTS reservation_services (
    id SERIAL PRIMARY KEY,
    reservation_id INTEGER NOT NULL REFERENCES reservations(id) ON DELETE CASCADE,
    service_id INTEGER NOT NULL REFERENCES services(id) ON DELETE RESTRICT,
    quantity INTEGER NOT NULL DEFAULT 1,
    total_price DECIMAL(10,2) NOT NULL
);

-- Payments table
CREATE TABLE IF NOT EXISTS payments (
    id SERIAL PRIMARY KEY,
    reservation_id INTEGER NOT NULL REFERENCES reservations(id) ON DELETE CASCADE,
    status_id INTEGER NOT NULL REFERENCES payment_statuses(id) ON DELETE RESTRICT,
    amount DECIMAL(10,2) NOT NULL,
    method VARCHAR(50) NOT NULL,
    transaction_code VARCHAR(100),
    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Invoices table
CREATE TABLE IF NOT EXISTS invoices (
    id SERIAL PRIMARY KEY,
    invoice_number VARCHAR(50) NOT NULL UNIQUE,
    reservation_id INTEGER NOT NULL REFERENCES reservations(id) ON DELETE CASCADE,
    payment_id INTEGER REFERENCES payments(id) ON DELETE SET NULL,
    staff_id INTEGER REFERENCES staff(id) ON DELETE SET NULL,
    issue_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(10,2) NOT NULL,
    tax DECIMAL(10,2) DEFAULT 0,
    discount DECIMAL(10,2) DEFAULT 0,
    final_amount DECIMAL(10,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Invoice Details table (Itemized billing)
CREATE TABLE IF NOT EXISTS invoice_details (
    id SERIAL PRIMARY KEY,
    invoice_id INTEGER NOT NULL REFERENCES invoices(id) ON DELETE CASCADE,
    item_type VARCHAR(20) NOT NULL, -- 'ROOM' or 'SERVICE'
    item_id INTEGER NOT NULL, -- room_id or service_id
    description TEXT,
    quantity INTEGER NOT NULL DEFAULT 1,
    unit_price DECIMAL(10,2) NOT NULL,
    total_price DECIMAL(10,2) NOT NULL
);

-- Audit Logs table (updated with UUID for Keycloak)
CREATE TABLE IF NOT EXISTS audit_logs (
    id SERIAL PRIMARY KEY,
    user_id UUID, -- Keycloak user ID
    user_role VARCHAR(50),
    action VARCHAR(100) NOT NULL,
    entity VARCHAR(100) NOT NULL,
    entity_id INTEGER,
    description TEXT,
    status VARCHAR(20) NOT NULL,
    ip_address VARCHAR(45),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Indexes for performance
CREATE INDEX IF NOT EXISTS idx_rooms_type ON rooms(room_type_id);
CREATE INDEX IF NOT EXISTS idx_rooms_status ON rooms(room_status_id);
CREATE INDEX IF NOT EXISTS idx_rooms_floor ON rooms(floor);
CREATE INDEX IF NOT EXISTS idx_room_images_room ON room_images(room_id);
CREATE INDEX IF NOT EXISTS idx_guests_email ON guests(email);
CREATE INDEX IF NOT EXISTS idx_guests_keycloak ON guests(keycloak_user_id);
CREATE INDEX IF NOT EXISTS idx_staff_email ON staff(email);
CREATE INDEX IF NOT EXISTS idx_staff_keycloak ON staff(keycloak_user_id);
CREATE INDEX IF NOT EXISTS idx_reservations_guest ON reservations(guest_id);
CREATE INDEX IF NOT EXISTS idx_reservations_status ON reservations(status_id);
CREATE INDEX IF NOT EXISTS idx_reservations_dates ON reservations(check_in, check_out);
CREATE INDEX IF NOT EXISTS idx_reservation_rooms_reservation ON reservation_rooms(reservation_id);
CREATE INDEX IF NOT EXISTS idx_reservation_rooms_room ON reservation_rooms(room_id);
CREATE INDEX IF NOT EXISTS idx_payments_reservation ON payments(reservation_id);
CREATE INDEX IF NOT EXISTS idx_payments_status ON payments(status_id);
CREATE INDEX IF NOT EXISTS idx_invoices_number ON invoices(invoice_number);
CREATE INDEX IF NOT EXISTS idx_invoices_reservation ON invoices(reservation_id);
CREATE INDEX IF NOT EXISTS idx_invoice_details_invoice ON invoice_details(invoice_id);
CREATE INDEX IF NOT EXISTS idx_audit_logs_user ON audit_logs(user_id);
CREATE INDEX IF NOT EXISTS idx_audit_logs_entity ON audit_logs(entity, entity_id);
CREATE INDEX IF NOT EXISTS idx_audit_logs_timestamp ON audit_logs(timestamp);

-- Insert default lookup data

-- Room Types
INSERT INTO room_types (name, description, price_per_night) VALUES
    ('SINGLE', 'Single room with 1 bed', 50.00),
    ('DOUBLE', 'Double room with 2 beds', 80.00),
    ('SUITE', 'Suite with living area', 150.00),
    ('DELUXE', 'Deluxe room with premium amenities', 200.00),
    ('PRESIDENTIAL', 'Presidential suite', 500.00)
ON CONFLICT (name) DO NOTHING;

-- Room Statuses
INSERT INTO room_statuses (name) VALUES
    ('AVAILABLE'),
    ('OCCUPIED'),
    ('MAINTENANCE'),
    ('RESERVED'),
    ('CLEANING')
ON CONFLICT (name) DO NOTHING;

-- Reservation Statuses
INSERT INTO reservation_statuses (name) VALUES
    ('PENDING'),
    ('CONFIRMED'),
    ('CHECKED_IN'),
    ('CHECKED_OUT'),
    ('CANCELLED')
ON CONFLICT (name) DO NOTHING;

-- Payment Statuses
INSERT INTO payment_statuses (name) VALUES
    ('PENDING'),
    ('COMPLETED'),
    ('FAILED'),
    ('REFUNDED'),
    ('PROCESSING')
ON CONFLICT (name) DO NOTHING;

-- Default Services
INSERT INTO services (name, description, price) VALUES
    ('Room Service', 'In-room dining service', 15.00),
    ('Laundry', 'Laundry and dry cleaning', 20.00),
    ('Spa', 'Spa and massage services', 50.00),
    ('Airport Transfer', 'Airport pickup and drop-off', 30.00),
    ('Extra Bed', 'Additional bed in room', 25.00)
ON CONFLICT (name) DO NOTHING;
