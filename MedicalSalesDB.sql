DROP DATABASE MedicalSalesDB;

CREATE DATABASE MedicalSalesDB;
USE MedicalSalesDB;

CREATE TABLE ClientMed (
    ClientID       INT PRIMARY KEY,
    ClientName     VARCHAR(150) NOT NULL,
    Address        VARCHAR(255),
    ContactPerson  VARCHAR(150),
    ContactInfo    VARCHAR(100)
);

CREATE TABLE Product (
    ProductID      INT NOT NULL PRIMARY KEY,
    Brand          VARCHAR(150) NOT NULL,
    Quantity       INT NOT NULL DEFAULT 0,
    Price          DECIMAL(10,2) NOT NULL,
    DateAdded      DATE NOT NULL,
    ExpirationDate DATE NOT NULL,
    LowStockLimit  INT DEFAULT 10    
);

CREATE TABLE Supplier (
    SupplierID     INT PRIMARY KEY,
    Name           VARCHAR(150) NOT NULL,
    Address        VARCHAR(255),
    ContactPerson  VARCHAR(150),
    ContactInfo    VARCHAR(100)
);

CREATE TABLE Staff (
    StaffID        INT PRIMARY KEY,
    Name           VARCHAR(150) NOT NULL,
    Credentials    VARCHAR(255),
    Status         ENUM('Active', 'Inactive') DEFAULT 'Active',
    Quota          INT DEFAULT 0
);

CREATE TABLE InventoryTransaction (
    TransactionID   INT PRIMARY KEY,
    ProductID       INT NOT NULL,
    SupplierID      INT,
    StaffID         INT,
    Quantity        INT NOT NULL,
    TransactionType ENUM('Incoming','Outgoing') NOT NULL,
    Status          VARCHAR(50),
    TransactionDate DATETIME NOT NULL,

    FOREIGN KEY (ProductID)  REFERENCES Product(ProductID),
    FOREIGN KEY (SupplierID) REFERENCES Supplier(SupplierID),
    FOREIGN KEY (StaffID)    REFERENCES Staff(StaffID)
);

CREATE TABLE Procurement (
    ProcurementID   INT PRIMARY KEY AUTO_INCREMENT,
    ProductID       INT NOT NULL,
    SupplierID      INT NOT NULL,
    Quantity        INT NOT NULL,
    TransactionDate DATETIME NOT NULL,

    FOREIGN KEY (ProductID)  REFERENCES Product(ProductID),
    FOREIGN KEY (SupplierID) REFERENCES Supplier(SupplierID)
);

CREATE TABLE Sales (
    SalesID     INT PRIMARY KEY,
    StaffID     INT NOT NULL,
    ProductID   INT NOT NULL,
    ClientID    INT NOT NULL,
    Quantity    INT NOT NULL,
    TotalPrice  DECIMAL(12,2) NOT NULL,
    SalesDate   DATETIME NOT NULL,

    FOREIGN KEY (StaffID)   REFERENCES Staff(StaffID),
    FOREIGN KEY (ProductID) REFERENCES Product(ProductID),
    FOREIGN KEY (ClientID)  REFERENCES ClientMed(ClientID)
);

CREATE TABLE ProductReturn (
    ReturnID    INT PRIMARY KEY AUTO_INCREMENT,
    ProductID   INT NOT NULL,
    ClientID    INT NOT NULL,
    StaffID     INT NOT NULL,
    Reason      VARCHAR(255),
    Quantity    INT NOT NULL,
    ReturnDate  DATETIME NOT NULL,
    PurchaseDate DATE NOT NULL,

    FOREIGN KEY (ProductID) REFERENCES Product(ProductID),
    FOREIGN KEY (ClientID)  REFERENCES ClientMed(ClientID),
    FOREIGN KEY (StaffID)   REFERENCES Staff(StaffID)
);


CREATE TABLE DeliveryOrder (
    OrderID          INT PRIMARY KEY,
    ClientID         INT NOT NULL,
    ProductID        INT NOT NULL,
    Quantity         INT NOT NULL,
    Price            DECIMAL(10,2) NOT NULL,
    OrderDate        DATETIME NOT NULL,
    DeliveryLocation VARCHAR(255),
    Status           VARCHAR(50),

    FOREIGN KEY (ClientID) REFERENCES ClientMed(ClientID),
    FOREIGN KEY (ProductID) REFERENCES Product(ProductID)
);

INSERT INTO ClientMed (ClientID, ClientName, Address, ContactPerson, ContactInfo) VALUES
  (1, 'Alabang Medical Center', 'Alabang-Zapote Road, Muntinlupa, Metro Manila', 'Dr. Anita Ty', '(02) 807-8189 / alabangmedicalcenter8@yahoo.com'),
  (2, 'AMSI Doctors Medical Center', 'Brgy. San Jose, City of Imus, Cavite', 'Maria Santos', '+63 917-234-5678'),
  (3, 'Asia Medic Hospital & Medical Center', 'Filinvest City, Alabang, Muntinlupa, Metro Manila', 'Jorge Garcia', '+63 2 8771-9000'),
  (4, 'Bacoor Doctors Medical Center', 'Bacoor, Cavite', 'Benito Cruz', '+63 918-345-6789'),
  (5, 'Balayan Bayview Hospital & Medical Center', 'Balayan, Batangas', 'Lucia Balayan', '+63 919-456-7890'),
  (6, 'Batangas Healthcare Specialists Medical Center', 'Batangas City, Batangas', 'Ramon Bautista', '+63 920-567-8901'),
  (7, 'Bethany Hospital', 'Dasmariñas, Cavite', 'Bethany Cruz', '+63 921-678-9012'),
  (8, 'Binakayan Hospital & Medical Center', '175 Covelandia Road, Binakayan, Kawit, Cavite', 'Dr. Grace Binakayan', '+63 922-789-0123'),
  (9, 'Biñan Doctors Hospital', 'Biñan, Laguna', 'Dr. Ricardo Biñan', '+63 923-890-1234'),
  (10, 'Bio-aid Diagnostic Center', 'Calamba, Laguna', 'Isabel Bioaid', '+63 924-901-2345'),
  (11, 'Calamba Doctors Hospital', 'Calamba, Laguna', 'Dr. Carlo Calamba', '+63 925-012-3456'),
  (12, 'Calamba Medical Center', 'Calamba, Laguna', 'Maria Calamba', '+63 926-123-4567'),
  (13, 'Canlubang Polyclinic', 'Canlubang, Laguna', 'Ana Polyclinic', '+63 927-234-5678'),
  (14, 'Cavite Medical Center', 'Manila-Cavite Road, Dalahican, Cavite City, Cavite', 'Dr. Celso Cavite', '+63 928-345-6789'),
  (15, 'Cavite State University Infirmary', 'Cavite State University, Indang, Cavite', 'Prof. Carmen State', '+63 929-456-7890'),
  (16, 'City of General Trias Doctors Medical Center', 'Governor’s Drive, Metropolis Green, Gen. Trias, Cavite', 'Dr. Ireneo Lumubos Jr.', '(046) 416-2222'),
  (17, 'City of Imus Doctors Hospital', 'Anabu 1-B, Imus City, Cavite', 'Dr. Emilio Imus', '(046) 435-5500'),
  (18, 'Community General Hospital of San Pablo City', 'San Pablo City, Laguna', 'Angela Community', '+63 931-567-8901'),
  (19, 'CRM Clinical Laboratory & Medical Clinic', 'San Pablo City, Laguna', 'Cristina CRM', '+63 932-678-9012'),
  (20, 'De La Salle Medical & Health Sciences Institute', 'Gov. D. Mangubat Ave, Dasmariñas, Cavite', 'Br. Augustine Boquer', '+63 46 481-8000 / inquiry@dlsmhsi.edu.ph'),
  (21, 'Divine Grace Medical Center', 'By-Pass Road, Tejero, General Trias, Cavite', 'Mario Zamora', '(046) 489-2224'),
  (22, 'Emilio Aguinaldo College Medical Center - Cavite', 'Brgy. Salitran II, Dasmariñas, Cavite', 'Allan Chua', '(046) 850-2037'),
  (23, 'First Cabuyao Hospital & Medical Center', 'Cabuyao City, Laguna', 'Dr. Felicia Cabuyao', '+63 933-789-0123');
  
  
  
  INSERT INTO Supplier (SupplierID, Name, Address, ContactPerson, ContactInfo) VALUES
  (1, 'Amhsco Enterprises', 'MacArthur Highway, San Fernando, Pampanga, 2000', 'Angela Perez', '+63 45 961-5513'),
  (2, 'JN Asistio', 'Blk 9 Lot 8 Opal St, Casa Filipina Subd, Fourth Estate, Sucat, Parañaque, 1700', 'Juan Asistio', '0998-580-9438 / jenny@jasistio.com'),
  (3, 'B Braun Medical Supplies', '15/F Sun Life Centre, 5th Avenue corner Rizal Drive, Bonifacio Global City, Taguig, Metro Manila', 'Beatrice Braun', '+63 2 588-5600 / info.ph@bbraun.com'),
  (4, 'Fujifilm Philippines', '25/F SM Aura Tower, 26th St corner McKinley Parkway, Taguig, Metro Manila, 1630', 'Kenji Fujimoto', '+63 2 8570-2695 / info.philippines@fujifilm.com'),
  (5, 'Sri Trang Gloves Philippines Inc', '32/F Unit 3207, Robinsons Equitable Tower, ADB Ave corner Poveda St, Ortigas Center, Pasig, Metro Manila, 1600', 'Santi Trang', '0919-000-6533'),
  (6, 'Avishai Star Enterprises', 'Unit 12, 8th Ave, Quezon City, Metro Manila', 'Leah Star', '+63 917-345-6789'),
  (7, 'GE Philippines', '45 Corporate Ave, Ortigas Center, Pasig City, Metro Manila', 'George Edison', '+63 918-234-5678'),
  (8, 'Ginrey Trading Corp', 'Lot 5, Hacienda Sta. Lucia, Pasig City, Metro Manila', 'Grace Ginrey', '+63 927-567-8901'),
  (9, 'Hospeco Philippines', 'Industrial Park Rd, Calamba, Laguna', 'Hector Opeco', '+63 928-678-9012'),
  (10, 'Inmed Corporation', 'Gateway Bldg, Ortigas Center, Pasig City, Metro Manila', 'Imelda Medin', '+63 929-789-0123'),
  (11, 'LabSolutions Technologies Inc', 'Science Park Bldg, Quezon City, Metro Manila', 'Luis Labsol', '+63 930-890-1234'),
  (12, 'MacTycoon Marketing', 'Ayala Tower, Makati City, Metro Manila', 'Mark Tycoon', '+63 931-901-2345'),
  (13, 'Medtrack Corporation', '22 San Miguel Ave, Mandaluyong City, Metro Manila', 'Megan Track', '+63 932-012-3456'),
  (14, 'MMS Medical & Laboratory Supplies', 'Dasmariñas, Cavite', 'Mike Santos', '+63 933-123-4567'),
  (15, 'Philippine Medical Supplies', '101 Congressional Ave, Quezon City, Metro Manila', 'Phil Medica', '+63 934-234-5678'),
  (16, 'Prestige Paper Products', '8th Ave, Cubao, Quezon City, Metro Manila', 'Paulo Prestige', '+63 935-345-6789'),
  (17, 'Qualipharma Philippines', 'Business Center Bldg, Pasay City, Metro Manila', 'Quincy Pharma', '+63 936-456-7890'),
  (18, 'Respicare Enterprises Inc', 'Medical St, Biñan, Laguna', 'Rosa Care', '+63 937-567-8901'),
  (19, 'Rite Beacon Marketing', 'Beacon St & 4th Ave, Quezon City, Metro Manila', 'Ricardo Beacon', '+63 938-678-9012'),
  (20, 'RMG Pharma', '300 Pharmaceutical St, Valenzuela City, Metro Manila', 'Ramon Medina', '+63 939-789-0123'),
  (21, 'Royal Arc Med Solution', 'Macapagal Blvd, Pasay City, Metro Manila', 'Rachel Arc', '+63 940-890-1234'),
  (22, 'Sta. Ana Healthcare Plus', 'Sta. Ana St, Pasig City, Metro Manila', 'Santiago Ana', '+63 941-901-2345'),
  (23, 'Trinity Marketing Inc', 'Trinity Tower, EDSA, Mandaluyong City, Metro Manila', 'Trisha Trinity', '+63 942-012-3456'),
  (24, 'Value-Rx Inc', 'Value Plaza, Congressional Ave, Quezon City, Metro Manila', 'Victor Rex', '+63 943-123-4567');
  
	INSERT INTO Staff (StaffID, Name, Credentials, Status, Quota) VALUES
  (1, 'Alice Santos', 'Medical Supply Clerk / Technician', 'Active', 50),
  (2, 'Brian Reyes', 'Store Executive/Manager', 'Active', 100),
  (3, 'Catherine Cruz', 'Customer Service Representative', 'Active', 30),
  (4, 'Daniel Lopez', 'Medical Supply Clerk / Technician', 'Active', 40),
  (5, 'Eva Ramirez', 'Store Executive/Manager', 'Active', 80),
  (6, 'Francis Villanueva', 'Customer Service Representative', 'Active', 25),
  (7, 'Grace Morales', 'Medical Supply Clerk / Technician', 'Active', 35),
  (8, 'Henry Aquino', 'Store Executive/Manager', 'Active', 90),
  (9, 'Isabel Navarro', 'Customer Service Representative', 'Active', 30),
  (10, 'Juan dela Cruz', 'Medical Supply Clerk / Technician', 'Active', 45);
  
INSERT INTO Product (ProductID, Brand, Quantity, Price, DateAdded, ExpirationDate) VALUES 
  ('1', 'Bandage', 100, 50.00, '2025-11-17', '2026-11-17'),
  ('2', 'Fujifilm Dry film HT 14x17', 10, 11000.00, '2025-11-18', '2026-06-25'),
  ('3', 'Agfa Drystar DT2B 14x17', 10, 22000.00, '2025-11-18', '2028-10-14'),
  ('4', 'Sony Thermal paper Type I/S', 100, 480.00, '2025-11-18', '2027-04-09'),
  ('5', 'Nitrilecare Nitrile Gloves S (Powderfree)', 100, 225.00, '2025-11-18', '2027-11-09'),
  ('6', 'Nitrilecare Nitrile Gloves M (Powderfree)', 100, 225.00, '2025-11-18', '2028-09-02'),
  ('7', 'Nitrilecare Nitrile Gloves L (Powderfree)', 100, 225.00, '2025-11-18', '2028-08-01'),
  ('8', 'Schullermed Nitrile Gloves S (Powderfree)', 100, 190.00, '2025-11-18', '2028-02-12'),
  ('9', 'Schullermed Nitrile Gloves M (Powderfree)', 100, 190.00, '2025-11-18', '2028-09-15'),
  ('10', 'Schullermed Nitrile Gloves L (Powderfree)', 100, 190.00, '2025-11-18', '2026-07-27'),
  ('11', 'Sri Trang Nitrile Gloves S (Powderfree)', 100, 190.00, '2025-11-18', '2027-05-19'),
  ('12', 'Rubbercare Latex Gloves S (Powderfree)', 100, 230.00, '2025-11-18', '2027-04-12'),
  ('13', 'Schullermed Latex Gloves M (Powderfree)', 100, 185.00, '2025-11-18', '2026-11-14'),
  ('14', 'Sri Trang Latex Gloves L (Powderfree)', 100, 180.00, '2025-11-18', '2026-10-10'),
  ('15', 'ORCare Surgical Gloves 7.5 (Powderfree)', 50, 1500.00, '2025-11-18', '2028-01-20'),
  ('16', 'Unimex Surgical Gloves 7.5 (Powderfree)', 50, 1000.00, '2025-11-18', '2028-04-20'),
  ('17', 'Careplus Facemask Earloop', 200, 70.00, '2025-11-18', '2028-04-29'),
  ('18', 'IV Starter Kit', 100, 85.00, '2025-11-18', '2027-10-09'),
  ('19', 'Syphilis Ab Rapid Test, Ds', 30, 42.20, '2025-11-18', '2028-01-08'),
  ('20', 'Syphilis Ab Combo Rapid Test, Cs', 30, 79.00, '2025-11-18', '2028-01-22'),
  ('21', 'HIV/Syphilis Ab Combo Rapid Test, Cs', 30, 131.67, '2025-11-18', '2027-01-08'),
  ('22', 'HIV/Syphilis/HBV/HCV Panel Rapid Test, Cs', 20, 224.00, '2025-11-18', '2027-12-14'),
  ('23', 'Dengue IgG/IgM Combo Rapid Test, Cs', 30, 237.00, '2025-11-18', '2028-11-09'),
  ('24', 'Duo Dengue Ag-IgG/IgM Rapid Test, Cs', 10, 461.00, '2025-11-18', '2027-08-16'),
  ('25', 'Dengue Ag Rapid Test, Cs', 30, 250.00, '2025-11-18', '2027-10-18'),
  ('26', 'HAV IgM Rapid Test, Cs', 30, 263.33, '2025-11-18', '2028-01-27'),
  ('27', 'HAV IgG/IgM Combo Rapid Test, Cs', 30, 316.00, '2025-11-18', '2028-09-02'),
  ('28', 'Leptospira IgG/IgM Combo Rapid Test, Cs', 30, 329.00, '2025-11-18', '2026-09-02'),
  ('29', 'Malaria Pf/Pv Ab Combo Rapid Test, Cs', 30, 131.67, '2025-11-18', '2026-08-01'),
  ('30', 'Malaria Pf/Pv Ag Rapid Test, Cs', 30, 131.67, '2025-11-18', '2027-06-25'),
  ('31', 'Malaria Pf/Pan Ag Rapid Test, Cs', 30, 171.00, '2025-11-18', '2027-12-07'),
  ('32', 'Typhoid IgG/IgM Combo Rapid Test, Cs', 30, 184.33, '2025-11-18', '2028-07-28'),
  ('33', 'Rotavirus Ag Rapid Test, Cs', 25, 210.80, '2025-11-18', '2028-08-14'),
  ('34', 'Dosifix Safeset IV Set', 100, 323.20, '2025-11-18', '2026-06-18'),
  ('35', 'Intrafix Primeline Basic IV Set', 100, 69.70, '2025-11-18', '2028-04-16'),
  ('36', 'Intrafix Safeset Caresite IV Set', 100, 150.70, '2025-11-18', '2028-04-15'),
  ('37', 'Introcan Safety IV Cannula G.18', 50, 89.80, '2025-11-18', '2027-09-21'),
  ('38', 'Sangofix IV Set', 100, 81.10, '2025-11-18', '2028-01-18'),
  ('39', 'Spinocan IV Set G. 23', 50, 90.00, '2025-11-18', '2028-04-18');