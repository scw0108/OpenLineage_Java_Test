CREATE DATABASE IF NOT EXISTS openlineage;
USE openlineage;
CREATE TABLE IF NOT EXISTS owners (
    id INT(4) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    address VARCHAR(255),
    city VARCHAR(255),
    telephone VARCHAR(255),
    INDEX(last_name)
) engine=InnoDB;

CREATE TABLE IF NOT EXISTS details (
    id INT(4) UNSIGNED NOT NULL,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    interest VARCHAR(255),
    PRIMARY KEY (id),
    FOREIGN KEY (id) REFERENCES owners(id)
) engine=InnoDB;


