use ManagementProduct;

CREATE TABLE `product` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `model` varchar(45) DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_model_UNIQUE` (`name`,`model`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DELIMITER $$
CREATE DEFINER=`myuser`@`%` PROCEDURE `getAllProducts`()
BEGIN
  SELECT * FROM product;
END$$
DELIMITER ;


DELIMITER $$
CREATE DEFINER=`myuser`@`%` PROCEDURE `insertProd`(IN productName varchar (30),
													IN model varchar(30),
                                                    IN price decimal(10,2))
BEGIN
INSERT INTO  product (name,model,price) VALUES (productName, model, price);
END$$
DELIMITER ;

