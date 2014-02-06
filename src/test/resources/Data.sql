-- MySQL dump 10.13  Distrib 5.6.13, for osx10.6 (i386)
--
-- Host: 127.0.0.1    Database: OpenServiceDB
-- ------------------------------------------------------
-- Server version	5.6.14

--
-- Dumping data for table `Category`
--

INSERT INTO Category VALUES (1,'book',NULL);
INSERT INTO Category VALUES (2,'amazon',NULL);
INSERT INTO Category VALUES (3,'prova',NULL);
INSERT INTO Category VALUES (4,'@Test',NULL);

--
-- Dumping data for table `TemporaryLink`
--

--
-- Dumping data for table `Service`
--

INSERT INTO Service VALUES (1,'service',NULL,'a,b',3,NULL,'1',NULL,NULL,0,'PUBLISH',1,NULL,2);
INSERT INTO Service VALUES (2,'amazon service','Amazone Service','a',2,'license','1',NULL,NULL,0,'publish',2,NULL,2);
INSERT INTO Service VALUES (3,'prova','Service di prova','c,d',3,'license','1',NULL,NULL,0,'deprecate',3,NULL,1);
INSERT INTO Service VALUES (4,'mouse','mouse','d',3,'license','1',NULL,NULL,0,'unpublish',1,NULL,2);
INSERT INTO Service VALUES (5,'service prova','my service for sara organization from web','c,d',3,'qwertyui','0.1',NULL,NULL,0,'UNPUBLISH',1,NULL,2);
INSERT INTO Service VALUES (6,'book seller','Selling book','d',1,'license','1',NULL,NULL,0,'publish',1,NULL,2);
INSERT INTO Service VALUES (7,'myServiceWithoutCategory',NULL,'e',0,'','1',NULL,NULL,0,'PUBLISH',3,NULL,2);

--
-- Dumping data for table `ServiceHistory`
--

INSERT INTO ServiceHistory VALUES (1,'service added',5,0,'2014-01-20');
INSERT INTO ServiceHistory VALUES (2,'service added',6,0,'2014-01-21');
INSERT INTO ServiceHistory VALUES (3,'service added',7,0,'2014-01-27');
INSERT INTO ServiceHistory VALUES (4,'service added',8,0,'2014-01-27');
INSERT INTO ServiceHistory VALUES (6,'PUBLISH service',8,0,'2014-01-27');
INSERT INTO ServiceHistory VALUES (7,'delete service',8,0,'2014-01-27');
INSERT INTO ServiceHistory VALUES (8,'service added',10,0,'2014-01-27');
INSERT INTO ServiceHistory VALUES (10,'PUBLISH service',10,0,'2014-01-27');
INSERT INTO ServiceHistory VALUES (11,'delete service',10,0,'2014-01-27');

--
-- Dumping data for table `User`
--

INSERT INTO User VALUES(1,'sara','$2a$10$M3hETMK7BGq2DSMGgSQu.eXBUGknPas1y8emJYnxIMx23mk5sr0kK','sara@sara.it',1,NULL,'ROLE_ADMIN');
INSERT INTO User VALUES(2,'john','$2a$10$wBkz.bAznivyBanmQpYECO7s9/SjU86dAaln.nTL1GMB/cVulX3Z6','john@john.it',0,NULL,'ROLE_NORMAL');
INSERT INTO User VALUES(3,'giulia','$2a$10$q1gp/vdTCWzY.n10JZaXluBqHfrRknzV6Gs2PYHHVudHbWWtSfVyu','giulia@giulia.it',1,NULL,'ROLE_NORMAL');

--
-- Dumping data for table `Organization`
--

INSERT INTO Organization VALUES (1,'IlMeteo3',NULL,NULL,3,NULL,1,NULL);
INSERT INTO Organization VALUES (2,'Amazon1',NULL,NULL,2,NULL,2,NULL);

--
-- Dumping data for table `Method`
--

INSERT INTO Method VALUES (1,'methods new','test methods',NULL,1,NULL);

--
-- Dumping data for table `UserRole`
--

INSERT INTO UserRole VALUES (1,1,1,'ROLE_ORGOWNER');
INSERT INTO UserRole VALUES (2,1,2,'ROLE_SERVICEOWNER');
INSERT INTO UserRole VALUES (3,3,2,'ROLE_ORGOWNER');
INSERT INTO UserRole VALUES (4,3,1,'ROLE_ORGOWNER')

-- Dump completed on 2014-02-05 10:39:56
