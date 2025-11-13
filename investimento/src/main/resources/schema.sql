CREATE TABLE IF NOT EXISTS stock (
    id INT AUTO_INCREMENT PRIMARY KEY,
    symbol VARCHAR(10) NOT NULL,
    dividend_date DATE NOT NULL,
    dividend_value DECIMAL(10, 4) NOT NULL
);