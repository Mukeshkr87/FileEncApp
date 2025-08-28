# FileEncApp 🔒

A **Java-based File Encryption and Hiding Application** built using **Maven**.  
This project provides functionalities for securing sensitive files by encrypting them, hiding/unhiding files, and using OTP verification via email for secure access.

---

## 🚀 Features
- File **encryption** and **decryption** using secure algorithms  
- File **hide/unhide system** using database (Oracle BLOBs)  
- **Email OTP verification** using JavaMail for authentication  
- Database connection and file handling via **DAO layer**  
- Developed with **Maven** for dependency management  

---

## 🛠 Tech Stack
- **Java 17+**
- **Maven**
- **Oracle Database** (BLOB storage)
- **JavaMail API**
- **JDBC**

---

## 📂 Project Structure
FileEncApp/
├── src/main/java/
│ ├── dao/ # Data access classes
│ ├── db/ # Database connection
│ ├── model/ # Entity classes
│ ├── service/ # Services (OTP, encryption, etc.)
│ └── Views/ # Console UI
├── pom.xml # Maven configuration
└── README.md

---

## ⚡ Setup & Run
1. Clone the repository:
   ```bash
   git clone https://github.com/Mukeshkr87/FileEncApp.git
   cd FileEncApp
   Build the project with Maven:

2. Build the project with Maven:
    mvn clean install

3. Run the application:
    mvn exec:java -Dexec.mainClass="org.example.Main"

Configuration

Create a file config.properties (not included in repo for security) with:

db.url=jdbc:oracle:thin:@localhost:1521:xe
db.username=YOUR_DB_USERNAME
db.password=YOUR_DB_PASSWORD

mail.smtp.host=smtp.gmail.com
mail.smtp.port=587
mail.username=YOUR_EMAIL
mail.password=YOUR_APP_PASSWORD
