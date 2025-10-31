CREATE TABLE room_types (
  id SERIAL PRIMARY KEY,
  name VARCHAR(100),
  description TEXT,
  price_per_night DECIMAL(10,2)
);

CREATE TABLE room_statuses (
  id SERIAL PRIMARY KEY,
  name VARCHAR(50)
);

CREATE TABLE rooms (
  id SERIAL PRIMARY KEY,
  room_number VARCHAR(20),
  room_type_id INT REFERENCES room_types(id),
  room_status_id INT REFERENCES room_statuses(id),
  floor INT,
  note TEXT
);

CREATE TABLE guests (
  id SERIAL PRIMARY KEY,
  full_name VARCHAR(100),
  email VARCHAR(100),
  phone VARCHAR(20),
  address TEXT,
  keycloak_user_id UUID
);

CREATE TABLE staff (
  id SERIAL PRIMARY KEY,
  full_name VARCHAR(100),
  email VARCHAR(100),
  position VARCHAR(50),
  keycloak_user_id UUID
);

CREATE TABLE reservation_statuses (
  id SERIAL PRIMARY KEY,
  name VARCHAR(50)
);

CREATE TABLE reservations (
  id SERIAL PRIMARY KEY,
  guest_id INT REFERENCES guests(id),
  status_id INT REFERENCES reservation_statuses(id),
  check_in DATE,
  check_out DATE,
  total_amount DECIMAL(10,2),
  created_at TIMESTAMP,
  updated_at TIMESTAMP
);

CREATE TABLE reservation_rooms (
  id SERIAL PRIMARY KEY,
  reservation_id INT REFERENCES reservations(id),
  room_id INT REFERENCES rooms(id)
);

CREATE TABLE reservation_staff (
  id SERIAL PRIMARY KEY,
  reservation_id INT REFERENCES reservations(id),
  staff_id INT REFERENCES staff(id)
);

CREATE TABLE services (
  id SERIAL PRIMARY KEY,
  name VARCHAR(100),
  description TEXT,
  price DECIMAL(10,2)
);

CREATE TABLE reservation_services (
  id SERIAL PRIMARY KEY,
  reservation_id INT REFERENCES reservations(id),
  service_id INT REFERENCES services(id),
  quantity INT,
  total_price DECIMAL(10,2)
);

CREATE TABLE payment_statuses (
  id SERIAL PRIMARY KEY,
  name VARCHAR(50)
);

CREATE TABLE payments (
  id SERIAL PRIMARY KEY,
  reservation_id INT REFERENCES reservations(id),
  status_id INT REFERENCES payment_statuses(id),
  amount DECIMAL(10,2),
  method VARCHAR(50),
  transaction_code VARCHAR(100),
  payment_date TIMESTAMP
);

CREATE TABLE invoices (
  id SERIAL PRIMARY KEY,
  invoice_number VARCHAR(50),
  reservation_id INT REFERENCES reservations(id),
  payment_id INT REFERENCES payments(id),
  staff_id INT REFERENCES staff(id),
  issue_date TIMESTAMP,
  total_amount DECIMAL(10,2),
  tax DECIMAL(10,2),
  discount DECIMAL(10,2),
  final_amount DECIMAL(10,2),
  created_at TIMESTAMP,
  updated_at TIMESTAMP
);

CREATE TABLE invoice_details (
  id SERIAL PRIMARY KEY,
  invoice_id INT REFERENCES invoices(id),
  item_type VARCHAR(20),
  item_id INT,
  description TEXT,
  quantity INT,
  unit_price DECIMAL(10,2),
  total_price DECIMAL(10,2)
);

CREATE TABLE audit_logs (
  id SERIAL PRIMARY KEY,
  user_id UUID,
  user_role VARCHAR(50),
  action VARCHAR(100),
  entity VARCHAR(100),
  entity_id INT,
  description TEXT,
  status VARCHAR(20),
  ip_address VARCHAR(45),
  timestamp TIMESTAMP
);
