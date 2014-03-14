-- MySQL dump 10.13  Distrib 5.6.13, for osx10.6 (i386)
--
-- Host: 127.0.0.1    Database: OpenServiceDB
-- ------------------------------------------------------
-- Server version	5.6.14

--
-- Table structure for table `Category`
--

CREATE TABLE Category (
  id integer identity primary key,
  name varchar(45) NOT NULL,
  description varchar(100) DEFAULT NULL
);

--
-- Table structure for table `TemporaryLink`
--

CREATE TABLE TemporaryLink (
  id integer identity primary key,
  id_org integer NOT NULL,
  "key" varchar(50) NOT NULL,
  email varchar(45) NOT NULL,
  role varchar(255) DEFAULT NULL
);

--
-- Table structure for table `Service`
--

CREATE TABLE Service (
  id integer identity primary key,
  name varchar(45) NOT NULL,
  description varchar(45) DEFAULT NULL,
  category integer DEFAULT NULL,
  license varchar(150),
  version varchar(45) NOT NULL,
  accessInformation varchar(500),
  documentation varchar(45) DEFAULT NULL,
  expiration integer DEFAULT NULL,
  state varchar(10) NOT NULL,
  creator_id integer NOT NULL,
  implementation varchar(500),
  organization_id integer NOT NULL
);

--
-- Table structure for table `Tag`
--

CREATE TABLE Tag(
	id integer identity primary key,
	id_service integer NOT NULL FOREIGN KEY REFERENCES Service(id),
	name varchar(45) NOT NULL
);

--
-- Table structure for table `ServiceHistory`
--

CREATE TABLE ServiceHistory (
  id integer identity primary key,
  operation varchar(45) NOT NULL,
  id_service integer NOT NULL,
  id_serviceMethod integer DEFAULT NULL,
  "date" date NOT NULL
) ;

--
-- Table structure for table `User`
--

CREATE TABLE User (
  id integer identity primary key,
  username varchar(45) NOT NULL,
  password varchar(145) NOT NULL,
  email varchar(45) NOT NULL,
  enabled integer NOT NULL,
  profile varchar(500),
  role varchar(45) NOT NULL
);

--
-- Table structure for table `Organization`
--

CREATE TABLE Organization (
  id integer identity primary key,
  name varchar(45) NOT NULL,
  description varchar(255) DEFAULT NULL,
  activityArea varchar(45) DEFAULT NULL,
  category integer DEFAULT NULL,
  contacts varchar(500),
  creator_id integer DEFAULT NULL,
  logo varchar(255) DEFAULT NULL
);

--
-- Table structure for table `Method`
--

CREATE TABLE Method (
  id integer identity primary key,
  name varchar(45) NOT NULL,
  synopsis varchar(45) DEFAULT NULL,
  documentation varchar(45) DEFAULT NULL,
  service_id integer DEFAULT NULL,
  testbox_properties varchar(500)
);

--
-- Table structure for table `UserRole`
--

CREATE TABLE UserRole (
  id integer identity primary key,
  id_user integer NOT NULL,
  id_org integer NOT NULL,
  role varchar(45) NOT NULL
);

-- Dump completed on 2014-02-05 10:00:46
