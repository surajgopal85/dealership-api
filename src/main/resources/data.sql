-- Simplified: No quotes needed when globally_quoted_identifiers is disabled
INSERT INTO vehicles (make, model, vehicle_year, price, status, created_at, updated_at) VALUES
('Toyota', 'Camry', 2023, 28500.00, 'AVAILABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Honda', 'Accord', 2024, 32000.00, 'AVAILABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('BMW', 'X5', 2023, 65000.00, 'AVAILABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Tesla', 'Model 3', 2024, 45000.00, 'PENDING', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Ford', 'F-150', 2023, 42000.00, 'SOLD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);