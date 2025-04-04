-- MySQL dump 10.13  Distrib 8.0.41, for Win64 (x86_64)
--
-- Host: localhost    Database: students_courses_database
-- ------------------------------------------------------
-- Server version	8.0.41

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `colleges`
--

DROP TABLE IF EXISTS `colleges`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `colleges` (
  `collesge_id` int NOT NULL,
  `college_name` varchar(30) NOT NULL,
  `is_del` tinyint NOT NULL,
  PRIMARY KEY (`collesge_id`,`college_name`),
  KEY `colleges_is_del_index` (`is_del`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `colleges`
--

LOCK TABLES `colleges` WRITE;
/*!40000 ALTER TABLE `colleges` DISABLE KEYS */;
/*!40000 ALTER TABLE `colleges` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `courses`
--

DROP TABLE IF EXISTS `courses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `courses` (
  `course_id` int NOT NULL COMMENT '璇剧▼id',
  `course_name` varchar(32) NOT NULL COMMENT '璇剧▼鍚嶇О',
  `teacher_name` varchar(32) NOT NULL COMMENT '鏁欏笀鍚嶇О',
  `volume_students` int NOT NULL COMMENT '鍙绾冲鐢熸暟閲?,
  `amount_selected_students` int NOT NULL COMMENT '宸查€夌殑瀛︾敓鏁伴噺',
  `score` int NOT NULL,
  `is_del` tinyint NOT NULL,
  PRIMARY KEY (`course_id`),
  KEY `courses_course_name_index` (`course_name`),
  KEY `courses_teacher_name_course_name_index` (`teacher_name`,`course_name`),
  KEY `courses_is_del_index` (`is_del`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='璇剧▼琛?;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `courses`
--

LOCK TABLES `courses` WRITE;
/*!40000 ALTER TABLE `courses` DISABLE KEYS */;
/*!40000 ALTER TABLE `courses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `majors`
--

DROP TABLE IF EXISTS `majors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `majors` (
  `major_id` int NOT NULL,
  `major_name` varchar(32) NOT NULL,
  `college_id` int NOT NULL,
  `is_del` tinyint NOT NULL,
  PRIMARY KEY (`major_id`),
  KEY `majors_major_name_index` (`major_name`),
  KEY `majors_is_del_index` (`is_del`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `majors`
--

LOCK TABLES `majors` WRITE;
/*!40000 ALTER TABLE `majors` DISABLE KEYS */;
/*!40000 ALTER TABLE `majors` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `student_courses`
--

DROP TABLE IF EXISTS `student_courses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `student_courses` (
  `student_id` int NOT NULL,
  `course_id` int NOT NULL,
  `student_name` varchar(32) NOT NULL,
  `teacher_name` varchar(32) NOT NULL,
  `course_name` varchar(32) NOT NULL,
  `student_course_id` int NOT NULL AUTO_INCREMENT,
  `student_num` varchar(32) NOT NULL,
  `is_del` tinyint NOT NULL,
  PRIMARY KEY (`student_course_id`),
  KEY `student_courses_course_id_index` (`course_id`),
  KEY `student_courses_student_name_index` (`student_name`),
  KEY `student_courses_student_id_course_id_index` (`student_id`,`course_id`),
  KEY `student_courses_is_del_index` (`is_del`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `student_courses`
--

LOCK TABLES `student_courses` WRITE;
/*!40000 ALTER TABLE `student_courses` DISABLE KEYS */;
/*!40000 ALTER TABLE `student_courses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `students`
--

DROP TABLE IF EXISTS `students`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `students` (
  `user_id` int NOT NULL,
  `student_num` varchar(32) NOT NULL COMMENT '瀛﹀彿',
  `college_id` int NOT NULL,
  `major_id` int NOT NULL COMMENT '涓撲笟id',
  `amount_selected_courses` int NOT NULL COMMENT '宸查€夎绋嬫暟閲?,
  `grade` int NOT NULL COMMENT '骞寸骇',
  `class` int NOT NULL COMMENT '鐝骇',
  `is_del` tinyint NOT NULL,
  PRIMARY KEY (`user_id`),
  KEY `students_student_num_index` (`student_num`),
  KEY `students_is_del_index` (`is_del`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `students`
--

LOCK TABLES `students` WRITE;
/*!40000 ALTER TABLE `students` DISABLE KEYS */;
INSERT INTO `students` VALUES (17,'111111',1,1,0,1,1,0);
/*!40000 ALTER TABLE `students` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `role_type` enum('ADMIN','STUDENT') DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `sex` tinyint NOT NULL,
  `is_del` tinyint NOT NULL,
  PRIMARY KEY (`user_id`),
  KEY `users_email_index` (`email`),
  KEY `users_username_index` (`username`),
  KEY `users_is_del_index` (`is_del`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'閽熷仴瑁?,'111111','ADMIN','111111@qq.com',1,0),(2,'閽熷仴瑁?,'111111','ADMIN','111111@qq.com',1,0),(3,'閽熷仴瑁?,'111111','ADMIN','111111@qq.com',1,0),(4,'閽熷仴瑁?,'111111','ADMIN','111111@qq.com',1,0),(5,'閽熷仴瑁?,'111111','ADMIN','111111@qq.com',1,0),(6,'閽熷仴瑁?,'111111','ADMIN','111111@qq.com',1,0),(17,'123','123','STUDENT','456',1,0);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-03-22 17:29:50
