CREATE TABLE IF NOT EXISTS tbl_company (
                                           company_code VARCHAR(50) PRIMARY KEY,
    company_name VARCHAR(100) NULL,
    company_description VARCHAR(255) NULL,
    create_date TIMESTAMP,
    create_by VARCHAR(50),
    update_date TIMESTAMP,
    update_by VARCHAR(50),
    is_deleted VARCHAR(1) DEFAULT 'N'
    );


CREATE TABLE IF NOT EXISTS tbl_employee (
                                            employee_id SERIAL PRIMARY KEY,
                                            company_code VARCHAR(50),
    period_date TIMESTAMP,
    txn_date TIMESTAMP,
    income_price NUMERIC,
    first_name VARCHAR(50) NULL,
    last_name VARCHAR(50) NULL,
    middle_name VARCHAR(50) NULL,
    phone VARCHAR(20) NULL,
    birth_date DATE NULL,
    create_date TIMESTAMP NULL,
    create_by VARCHAR(50) NULL,
    update_date TIMESTAMP NULL,
    update_by VARCHAR(50) NULL,
    is_deleted VARCHAR(1) DEFAULT 'N',
    FOREIGN KEY (company_code) REFERENCES company(company_code)
    );
CREATE TABLE IF NOT EXISTS tbl_file (
                                        employee_id VARCHAR(255) NOT NULL,
    company_code VARCHAR(255) NOT NULL,
    file_name VARCHAR(255),
    file_type VARCHAR(255),
    file_date DATE NOT NULL,
    create_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    create_by VARCHAR(255),
    update_date TIMESTAMP,
    update_by VARCHAR(255),
    is_deleted VARCHAR(1) DEFAULT 'N',
    PRIMARY KEY (employee_id, company_code, file_date)
    );
