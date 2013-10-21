  
--
-- Structure for table shoppingcart_portlet
--
DROP TABLE IF EXISTS shoppingcart_portlet;
CREATE TABLE shoppingcart_portlet (
  id_portlet int default '0' NOT NULL,
  shoppingcart_feed_id varchar(100) default NULL,
  PRIMARY KEY  (id_portlet)
);
